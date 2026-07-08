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
        String participationFrequency = user.getFirstAttribute("participationFrequency") != null ?
                                        user.getFirstAttribute("participationFrequency") : "";
        String termsAccepted = user.getFirstAttribute("termsAccepted") != null ?
                               user.getFirstAttribute("termsAccepted") : "off";
        String privacyPolicyAccepted = user.getFirstAttribute("privacyPolicyAccepted") != null ?
                                       user.getFirstAttribute("privacyPolicyAccepted") : "off";
        String marketingCommsAccepted = user.getFirstAttribute("marketingCommsAccepted") != null ?
                                        user.getFirstAttribute("marketingCommsAccepted") : "off";

        // Cria o formulário com os dados atuais
        Response challenge = context.form()
            .setAttribute("themes", themes != null ? themes : Collections.emptyList())
            .setAttribute("interests", interests != null ? interests : Collections.emptyList())
            .setAttribute("participationFrequency", participationFrequency)
            .setAttribute("termsAccepted", termsAccepted)
            .setAttribute("privacyPolicyAccepted", privacyPolicyAccepted)
            .setAttribute("marketingCommsAccepted", marketingCommsAccepted)
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
        String participationFrequencyParam = context.getHttpRequest().getDecodedFormParameters().getFirst("participationFrequency");
        String termsAcceptedParam = context.getHttpRequest().getDecodedFormParameters().getFirst("termsAccepted");
        String privacyPolicyAcceptedParam = context.getHttpRequest().getDecodedFormParameters().getFirst("privacyPolicyAccepted");
        String marketingCommsAcceptedParam = context.getHttpRequest().getDecodedFormParameters().getFirst("marketingCommsAccepted");

        // Valida se algo foi selecionado
        Boolean hasError = false;
        String themesError = null;
        String interestsError = null;
        String participationFrequencyError = null;
        String termsAcceptedError = null;
        String privacyPolicyAcceptedError = null;

        if ((themesParam == null || themesParam.isEmpty()) || themesParam.size() < 3) {
            themesError = "Selecione pelo menos 3 temas";
            hasError = true;
        } else if (themesParam.size() > 3) {
            themesError = "Selecione no máximo 3 temas";
            hasError = true;
        }

        if ((interestsParam == null || interestsParam.isEmpty())) {
            interestsError = "Selecione pelo menos um interesse";
            hasError = true;
        }

        if (participationFrequencyParam == null || participationFrequencyParam.trim().isEmpty()) {
            participationFrequencyError = "Selecione uma opção";
            hasError = true;
        }

        if (!"on".equals(termsAcceptedParam)) {
            termsAcceptedError = "Você precisa aceitar o Termo de Adesão para continuar";
            hasError = true;
        }

        if (!"on".equals(privacyPolicyAcceptedParam)) {
            privacyPolicyAcceptedError = "Você precisa aceitar a Política de Privacidade para continuar";
            hasError = true;
        }

        if (hasError) {
            // Recria o formulário com erro
            Response challenge = context.form()
                .setAttribute("themes", themesParam != null ? themesParam : Collections.emptyList())
                .setAttribute("themesError", themesError)
                .setAttribute("interests", interestsParam != null ? interestsParam : Collections.emptyList())
                .setAttribute("interestsError", interestsError)
                .setAttribute("participationFrequency", participationFrequencyParam != null ? participationFrequencyParam : "")
                .setAttribute("participationFrequencyError", participationFrequencyError)
                .setAttribute("termsAccepted", termsAcceptedParam != null ? termsAcceptedParam : "off")
                .setAttribute("termsAcceptedError", termsAcceptedError)
                .setAttribute("privacyPolicyAccepted", privacyPolicyAcceptedParam != null ? privacyPolicyAcceptedParam : "off")
                .setAttribute("privacyPolicyAcceptedError", privacyPolicyAcceptedError)
                .setAttribute("marketingCommsAccepted", marketingCommsAcceptedParam != null ? marketingCommsAcceptedParam : "off")
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

        // Salva a frequência de participação
        if (participationFrequencyParam != null && !participationFrequencyParam.trim().isEmpty()) {
            user.setSingleAttribute("participationFrequency", participationFrequencyParam);
        } else {
            user.removeAttribute("participationFrequency");
        }

        // Salva a aceitação dos termos
        user.setSingleAttribute("termsAccepted", termsAcceptedParam);
        user.setSingleAttribute("privacyPolicyAccepted", privacyPolicyAcceptedParam);
        user.setSingleAttribute("marketingCommsAccepted", marketingCommsAcceptedParam != null ? marketingCommsAcceptedParam : "off");

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