package org.bonde.keycloak.requiredaction;

import org.keycloak.Config;
import org.keycloak.authentication.RequiredActionFactory;
import org.keycloak.authentication.RequiredActionProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

public class BondeInterestFieldsRequiredActionFactory implements RequiredActionFactory {
    
    @Override
    public String getId() {
        return BondeInterestFieldsRequiredAction.PROVIDER_ID;
    }

    @Override
    public String getDisplayText() {
        return "BONDE - Update Interest Fields";
    }
    
    @Override
    public RequiredActionProvider create(KeycloakSession session) {
        return new BondeInterestFieldsRequiredAction();
    }
    
    @Override
    public void init(Config.Scope config) {}

    @Override
    public void postInit(KeycloakSessionFactory factory) {}

    @Override
    public void close() {}
}