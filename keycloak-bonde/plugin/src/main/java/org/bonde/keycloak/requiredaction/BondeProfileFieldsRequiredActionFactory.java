package org.bonde.keycloak.requiredaction;

import org.keycloak.Config;
import org.keycloak.authentication.RequiredActionFactory;
import org.keycloak.authentication.RequiredActionProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

public class BondeProfileFieldsRequiredActionFactory implements RequiredActionFactory {

    @Override
    public String getId() {
        return BondeProfileFieldsRequiredAction.PROVIDER_ID;
    }

    @Override
    public String getDisplayText() {
        return "BONDE - Update Profile Fields";
    }

    @Override
    public RequiredActionProvider create(KeycloakSession session) {
        return new BondeProfileFieldsRequiredAction();
    }

    @Override
    public void init(Config.Scope config) {}

    @Override
    public void postInit(KeycloakSessionFactory factory) {}

    @Override
    public void close() {}
}