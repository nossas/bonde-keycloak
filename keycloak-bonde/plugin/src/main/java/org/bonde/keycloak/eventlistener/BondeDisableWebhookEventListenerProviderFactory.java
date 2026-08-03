package org.bonde.keycloak.eventlistener;

import org.jboss.logging.Logger;
import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

import java.time.Duration;
import java.net.http.HttpClient;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Registers "bonde-disable-webhook" as a realm Event Listener (Realm Settings -> Events -> Event listeners).
 * Configuration is read from KC_SPI_EVENTS_LISTENER_BONDE_DISABLE_WEBHOOK_* env vars, e.g.
 * KC_SPI_EVENTS_LISTENER_BONDE_DISABLE_WEBHOOK_WEBHOOK_URL and ..._WEBHOOK_SECRET.
 */
public class BondeDisableWebhookEventListenerProviderFactory implements EventListenerProviderFactory {

    public static final String PROVIDER_ID = "bonde-disable-webhook";
    private static final Logger logger = Logger.getLogger(BondeDisableWebhookEventListenerProviderFactory.class);

    private String webhookUrl;
    private String webhookSecret;
    private int maxRetries;
    private long retryBackoffMs;
    private int requestTimeoutMs;
    private HttpClient httpClient;
    private ExecutorService executor;
    // Shared across all requests/provider instances so a "not enabled -> enabled" transition
    // detected on one request is visible to the next one. See BondeDisableWebhookEventListenerProvider
    // for why this is needed (the Admin Console always sends "enabled" on every save).
    private final ConcurrentMap<String, Boolean> lastKnownEnabledState = new ConcurrentHashMap<>();

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public EventListenerProvider create(KeycloakSession session) {
        return new BondeDisableWebhookEventListenerProvider(session, httpClient, executor,
                webhookUrl, webhookSecret, maxRetries, retryBackoffMs, requestTimeoutMs, lastKnownEnabledState);
    }

    @Override
    public void init(Config.Scope config) {
        webhookUrl = config.get("webhookUrl");
        webhookSecret = config.get("webhookSecret");
        maxRetries = config.getInt("maxRetries", 3);
        retryBackoffMs = config.getLong("retryBackoffMs", 1000L);
        requestTimeoutMs = config.getInt("requestTimeoutMs", 5000);
        int connectTimeoutMs = config.getInt("connectTimeoutMs", 3000);

        if (webhookUrl == null || webhookUrl.isBlank()) {
            logger.warn("bonde-disable-webhook: webhookUrl not configured (KC_SPI_EVENTS_LISTENER_BONDE_DISABLE_WEBHOOK_WEBHOOK_URL). " +
                    "The listener will be inactive until it is set.");
        }
        if (webhookSecret == null || webhookSecret.isBlank()) {
            logger.warn("bonde-disable-webhook: webhookSecret not configured, requests will be sent without the X-Webhook-Secret header.");
        }

        httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(connectTimeoutMs))
                .build();
        executor = Executors.newFixedThreadPool(2, runnable -> {
            Thread thread = new Thread(runnable, "bonde-disable-webhook");
            thread.setDaemon(true);
            return thread;
        });
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
    }

    @Override
    public void close() {
        if (executor != null) {
            executor.shutdown();
        }
    }
}
