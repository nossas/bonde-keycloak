FROM quay.io/keycloak/keycloak:26.5.2

#
LABEL org.opencontainers.image.source="https://github.com/nossas/bonde-keycloak"
LABEL org.opencontainers.image.description="Keycloak with Bonde custom theme"

COPY themes/bonde /opt/keycloak/themes/bonde