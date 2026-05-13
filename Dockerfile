FROM quay.io/keycloak/keycloak:26.5.2

#
LABEL org.opencontainers.image.source="https://github.com/nossas/bonde-keycloak"
LABEL org.opencontainers.image.description="Keycloak with Bonde custom theme"

COPY themes/bonde /opt/keycloak/themes/bonde
COPY providers/keycloak-restrict-client-auth.jar /opt/keycloak/providers/keycloak-restrict-client-auth.jar
COPY providers/keycloak-magic-link-0.57.jar /opt/keycloak/providers/keycloak-magic-link-0.57.jar
COPY providers/keycloak-bonde-plugin-0.1.jar /opt/keycloak/providers/keycloak-bonde-plugin-0.1.jar