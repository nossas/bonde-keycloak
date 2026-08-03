package org.bonde.keycloak.eventlistener;

import org.jboss.logging.Logger;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.events.admin.OperationType;
import org.keycloak.events.admin.ResourceType;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.util.JsonSerialization;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Notifies an n8n webhook whenever an admin action (Console, Admin REST API, kcadm, ...)
 * enables or disables a user. This is a single choke point: it fires regardless of which
 * client performed the change, because it hooks into Keycloak's own admin event bus instead
 * of a specific caller.
 */
public class BondeDisableWebhookEventListenerProvider implements EventListenerProvider {

    private static final Logger logger = Logger.getLogger(BondeDisableWebhookEventListenerProvider.class);
    private static final Pattern USER_ID_PATTERN = Pattern.compile("^users/([^/]+)");

    private final KeycloakSession session;
    private final HttpClient httpClient;
    private final ExecutorService executor;
    private final String webhookUrl;
    private final String webhookSecret;
    private final int maxRetries;
    private final long retryBackoffMs;
    private final int requestTimeoutMs;
    private final ConcurrentMap<String, Boolean> lastKnownEnabledState;

    public BondeDisableWebhookEventListenerProvider(KeycloakSession session, HttpClient httpClient, ExecutorService executor,
                                                      String webhookUrl, String webhookSecret,
                                                      int maxRetries, long retryBackoffMs, int requestTimeoutMs,
                                                      ConcurrentMap<String, Boolean> lastKnownEnabledState) {
        this.session = session;
        this.httpClient = httpClient;
        this.executor = executor;
        this.webhookUrl = webhookUrl;
        this.webhookSecret = webhookSecret;
        this.maxRetries = maxRetries;
        this.retryBackoffMs = retryBackoffMs;
        this.requestTimeoutMs = requestTimeoutMs;
        this.lastKnownEnabledState = lastKnownEnabledState;
    }

    @Override
    public void onEvent(Event event) {
        // User-facing events (login, logout, ...) are not relevant for account disablement.
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean includeRepresentation) {
        if (webhookUrl == null || webhookUrl.isBlank()) {
            return;
        }
        if (adminEvent.getResourceType() != ResourceType.USER) {
            return;
        }
        if (adminEvent.getOperationType() == OperationType.DELETE) {
            String deletedUserId = extractUserId(adminEvent.getResourcePath());
            if (deletedUserId != null) {
                lastKnownEnabledState.remove(deletedUserId);
            }
            return;
        }
        if (adminEvent.getOperationType() != OperationType.UPDATE) {
            return;
        }

        String representation = adminEvent.getRepresentation();
        if (representation == null) {
            return;
        }

        UserRepresentation userRepresentation;
        try {
            userRepresentation = JsonSerialization.readValue(representation, UserRepresentation.class);
        } catch (Exception e) {
            logger.warnf(e, "bonde-disable-webhook: failed to parse user representation for admin event %s", adminEvent.getId());
            return;
        }

        // A partial update (e.g. a PUT with just {"enabled": false}) leaves every other field
        // absent/null here, so a null "enabled" means this particular update did not touch the flag.
        Boolean enabled = userRepresentation.isEnabled();
        if (enabled == null) {
            return;
        }

        String userId = extractUserId(adminEvent.getResourcePath());
        if (userId == null) {
            logger.warnf("bonde-disable-webhook: could not extract user id from resourcePath '%s'", adminEvent.getResourcePath());
            return;
        }

        // The Admin Console always sends the full user representation on save, including
        // "enabled", even when only an unrelated field (e.g. firstName) was edited. So a
        // present "enabled" value alone does not mean the flag actually changed - it must be
        // compared against the last value seen for this user to detect a real transition.
        // A null previous value means this is the first time this user is seen (e.g. right
        // after a restart): the baseline is learned but nothing is fired, since we can't tell
        // whether it's a real transition.
        Boolean previousEnabled = lastKnownEnabledState.put(userId, enabled);
        if (previousEnabled == null || previousEnabled.equals(enabled)) {
            return;
        }

        // The representation is often partial, so the full user (name, email, attributes) is
        // fetched fresh here. Session is only valid for the lifetime of this request, so this
        // must happen before the HTTP call is handed off to the background executor.
        RealmModel realm = session.realms().getRealm(adminEvent.getRealmId());
        UserModel user = realm != null ? session.users().getUserById(realm, userId) : null;

        String username = user != null ? user.getUsername() : userRepresentation.getUsername();
        String email = user != null ? user.getEmail() : userRepresentation.getEmail();
        String firstName = user != null ? user.getFirstName() : userRepresentation.getFirstName();
        String lastName = user != null ? user.getLastName() : userRepresentation.getLastName();
        String circleCommunityMemberId = user != null ? user.getFirstAttribute("circle_community_member_id") : null;

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("event", enabled ? "user.enabled" : "user.disabled");
        payload.put("realmId", adminEvent.getRealmId());
        payload.put("userId", userId);
        payload.put("username", username);
        payload.put("email", email);
        payload.put("firstName", firstName);
        payload.put("lastName", lastName);
        payload.put("circle_community_member_id", circleCommunityMemberId);
        payload.put("actorId", adminEvent.getAuthDetails() != null ? adminEvent.getAuthDetails().getUserId() : null);
        payload.put("timestamp", Instant.ofEpochMilli(adminEvent.getTime()).toString());

        String body;
        try {
            body = JsonSerialization.writeValueAsString(payload);
        } catch (Exception e) {
            logger.warnf(e, "bonde-disable-webhook: failed to serialize payload for user %s", userId);
            return;
        }

        executor.submit(() -> sendWithRetry(body));
    }

    private void sendWithRetry(String body) {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(webhookUrl))
                .timeout(Duration.ofMillis(requestTimeoutMs))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body));
        if (webhookSecret != null && !webhookSecret.isBlank()) {
            requestBuilder.header("X-Webhook-Secret", webhookSecret);
        }
        HttpRequest request = requestBuilder.build();

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
                if (response.statusCode() >= 200 && response.statusCode() < 300) {
                    return;
                }
                logger.warnf("bonde-disable-webhook: attempt %d/%d got HTTP %d", attempt, maxRetries, response.statusCode());
            } catch (Exception e) {
                logger.warnf("bonde-disable-webhook: attempt %d/%d failed: %s", attempt, maxRetries, e.getMessage());
            }

            if (attempt < maxRetries) {
                try {
                    Thread.sleep(retryBackoffMs * (1L << (attempt - 1)));
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
        logger.errorf("bonde-disable-webhook: giving up after %d attempts, payload=%s", maxRetries, body);
    }

    private static String extractUserId(String resourcePath) {
        if (resourcePath == null) {
            return null;
        }
        Matcher matcher = USER_ID_PATTERN.matcher(resourcePath);
        return matcher.find() ? matcher.group(1) : null;
    }

    @Override
    public void close() {
    }
}
