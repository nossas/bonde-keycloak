package org.bonde.keycloak.requiredaction;

import org.jboss.logging.Logger;
import org.keycloak.authentication.RequiredActionContext;
import org.keycloak.authentication.RequiredActionProvider;
import org.keycloak.models.UserModel;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MultivaluedMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BondeInterestFieldsRequiredAction implements RequiredActionProvider {
    
    public static final String PROVIDER_ID = "UPDATE_INTEREST_FIELDS";
    private static final Logger logger = Logger.getLogger(BondeInterestFieldsRequiredAction.class);
    
    @Override
    public void evaluateTriggers(RequiredActionContext context) {
        logger.debugf("Evaluating triggers for %s", PROVIDER_ID);
    }
    
    @Override
    public void requiredActionChallenge(RequiredActionContext context) {
        logger.debugf("Rendering challenge for %s", PROVIDER_ID);
        logger.info("=== START requiredActionChallenge ===");
        logger.infof("User: %s", context.getUser().getUsername());
        
        // Dump de todos os atributos do usuário
        logger.info("User attributes:");
        context.getUser().getAttributes().forEach((key, value) -> {
            logger.infof("  %s = %s", key, value);
        });
        
        // Dump dos parâmetros da requisição
        logger.info("Request parameters:");
        context.getHttpRequest().getDecodedFormParameters().forEach((key, value) -> {
            logger.infof("  %s = %s", key, value);
        });
        
        logger.info("=== END requiredActionChallenge ===");

        UserModel user = context.getUser();

        // Recupera os atributos de interesse do usuário
        List<String> themes = user.getAttributes().get("themes");
        List<String> interests = user.getAttributes().get("interests");
        
        // Cria o formulário com os dados atuais
        logger.debugf("THEMES %s", themes);
        Response challenge = context.form()
            .setAttribute("themes", themes != null ? themes : Collections.emptyList())
            .setAttribute("interests", interests != null ? interests : Collections.emptyList())
            .createForm("interest-fields.ftl");
        
        context.challenge(challenge);
    }
    
    @Override
    public void processAction(RequiredActionContext context) {
        logger.debugf("Processing action for %s", PROVIDER_ID);

        logger.info("=== START processAction ===");
        
        MultivaluedMap<String, String> params = context.getHttpRequest().getDecodedFormParameters();
        
        // Log ALL parameters
        logger.info("ALL FORM PARAMETERS:");
        for (String key : params.keySet()) {
            List<String> values = params.get(key);
            logger.infof("  %s = %s (size: %d)", key, values, values.size());
        }
        
        // Log específico para themes
        List<String> themes = params.get("themes");
        logger.infof("Themes received: %s", themes);
        if (themes != null) {
            for (int i = 0; i < themes.size(); i++) {
                logger.infof("  themes[%d] = %s", i, themes.get(i));
            }
        }
        
        logger.info("=== END processAction ===");
        
        UserModel user = context.getUser();
        
        // Pega os parâmetros do formulário
        List<String> themesParam = context.getHttpRequest().getDecodedFormParameters().get("themes");
        List<String> interestsParam = context.getHttpRequest().getDecodedFormParameters().get("interests");
        
        // Valida se algo foi selecionado
        if ((themesParam == null || themesParam.isEmpty()) && 
            (interestsParam == null || interestsParam.isEmpty())) {
            
            // Recria o formulário com erro
            Response challenge = context.form()
                .setError("Selecione pelo menos um tema ou interesse")
                .setAttribute("themes", Collections.emptyList())
                .setAttribute("interests", Collections.emptyList())
                .createForm("interest-fields.ftl");
            context.challenge(challenge);
            return;
        }
        
        // Salva os temas (se houver)
        if (themesParam != null && !themesParam.isEmpty()) {
            user.setAttribute("themes", themesParam);
        } else {
            user.removeAttribute("themes");
        }
        
        // Salva os interesses (se houver)
        if (interestsParam != null && !interestsParam.isEmpty()) {
            user.setAttribute("interests", interestsParam);
        } else {
            user.removeAttribute("interests");
        }
        
        // Remove a required action após completar
        user.removeRequiredAction(PROVIDER_ID);
        
        context.success();
    }
    
    @Override
    public void close() {
        // Liberação de recursos, se necessário
        logger.debugf("Closing %s", PROVIDER_ID);
    }
}