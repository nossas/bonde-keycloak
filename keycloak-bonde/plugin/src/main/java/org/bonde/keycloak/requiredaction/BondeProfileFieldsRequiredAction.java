package org.bonde.keycloak.requiredaction;

import org.jboss.logging.Logger;
import org.keycloak.authentication.RequiredActionContext;
import org.keycloak.authentication.RequiredActionProvider;
import org.keycloak.models.UserModel;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MultivaluedMap;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class BondeProfileFieldsRequiredAction implements RequiredActionProvider {

    private static final Logger logger = Logger.getLogger(BondeProfileFieldsRequiredAction.class);
    public static final String PROVIDER_ID = "UPDATE_PROFILE_FIELDS";

    @Override
    public void evaluateTriggers(RequiredActionContext context) {
        logger.debugf("Evaluating triggers for %s", PROVIDER_ID);
    }

    @Override
    public void requiredActionChallenge(RequiredActionContext context) {
        logger.debugf("Rendering challenge for %s", PROVIDER_ID);
        
        UserModel user = context.getUser();
        
        // Recupera os atributos do usuário
        String firstName = user.getFirstName() != null ? user.getFirstName() : "";
        String lastName = user.getLastName() != null ? user.getLastName() : "";
        
        // Para nome completo, podemos combinar ou usar um campo separado
        // Vamos usar firstName como nome completo por simplicidade
        
        String birthday = user.getFirstAttribute("birthday") != null ? 
                             user.getFirstAttribute("birthday") : "";
        String identification = user.getFirstAttribute("identification") != null ?
                                user.getFirstAttribute("identification") : "";
        String raceColor = user.getFirstAttribute("raceColor") != null ?
                           user.getFirstAttribute("raceColor") : "";
        String state = user.getFirstAttribute("state") != null ?
                       user.getFirstAttribute("state") : "";
        String city = user.getFirstAttribute("city") != null ? 
                      user.getFirstAttribute("city") : "";
        String allowMemberMessages = user.getFirstAttribute("allowMemberMessages") != null ? 
                                     user.getFirstAttribute("allowMemberMessages") : "false";
        String showProfileDirectory = user.getFirstAttribute("showProfileDirectory") != null ? 
                                      user.getFirstAttribute("showProfileDirectory") : "false";
        
        // Cria o formulário com os dados atuais
        Response challenge = context.form()
            .setAttribute("firstName", firstName)
            .setAttribute("lastName", lastName)
            .setAttribute("birthday", birthday)
            .setAttribute("identification", identification)
            .setAttribute("raceColor", raceColor)
            .setAttribute("state", state)
            .setAttribute("city", city)
            .setAttribute("allowMemberMessages", allowMemberMessages)
            .setAttribute("showProfileDirectory", showProfileDirectory)
            .createForm("profile-fields.ftl");
        
        context.challenge(challenge);
    }

    @Override
    public void processAction(RequiredActionContext context) {
        logger.debugf("Processing action for %s", PROVIDER_ID);
        
        UserModel user = context.getUser();
        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
        
        // Pega os valores enviados pelo formulário
        String firstName = formData.getFirst("firstName");
        String lastName = formData.getFirst("lastName");
        String birthday = formData.getFirst("birthday");
        String identification = formData.getFirst("identification");
        String raceColor = formData.getFirst("raceColor");
        String state = formData.getFirst("state");
        String city = formData.getFirst("city");
        String allowMemberMessages = formData.getFirst("allowMemberMessages");
        String showProfileDirectory = formData.getFirst("showProfileDirectory");
        
        // Validação
        String firstNameError = null;
        String lastNameError = null;
        String birthdayError = null;
        String identificationError = null;
        String raceColorError = null;
        String stateError = null;
        String cityError = null;
        boolean hasError = false;
        
        if (firstName == null || firstName.trim().isEmpty()) {
            firstNameError = "Nome é obrigatório";
            hasError = true;
        }

        if (lastName == null || lastName.trim().isEmpty()) {
            lastNameError = "Sobrenome é obrigatório";
            hasError = true;
        }
        
        if (birthday != null && !birthday.trim().isEmpty()) {
            logger.debugf("DATA DE NASCIMENTO %s", birthday);
            try {
                LocalDate.parse(birthday);
            } catch (DateTimeParseException e) {
                birthdayError = "Data de nascimento inválida. Use o formato dd/mm/aaaa";
                hasError = true;
            }
        }
        
        logger.debugf("DATA DE NASCIMENTO %s", birthday);
        if (identification == null || identification.trim().isEmpty()) {
            identificationError = "Como você se identifica é obrigatório";
            hasError = true;
        }

        if (raceColor == null || raceColor.trim().isEmpty()) {
            raceColorError = "Como você se identifica em relação à sua raça/cor é obrigatório";
            hasError = true;
        }

        if (state == null || state.trim().isEmpty()) {
            stateError = "Estado é obrigatório";
            hasError = true;
        }
        
        if (city == null || city.trim().isEmpty()) {
            cityError = "Cidade é obrigatória";
            hasError = true;
        }
        
        if (hasError) {
            // Recria o formulário com erros
            Response challenge = context.form()
                .setAttribute("firstName", firstName != null ? firstName : "")
                .setAttribute("lastName", lastName != null ? lastName : "")
                .setAttribute("birthday", birthday != null ? birthday : "")
                .setAttribute("identification", identification != null ? identification : "")
                .setAttribute("raceColor", raceColor != null ? raceColor : "")
                .setAttribute("state", state != null ? state : "")
                .setAttribute("city", city != null ? city : "")
                .setAttribute("allowMemberMessages", allowMemberMessages != null ? "true" : "false")
                .setAttribute("showProfileDirectory", showProfileDirectory != null ? "true" : "false")
                .setAttribute("firstNameError", firstNameError != null ? firstNameError : "")
                .setAttribute("lastNameError", lastNameError != null ? lastNameError : "")
                .setAttribute("birthdayError", birthdayError != null ? birthdayError : "")
                .setAttribute("identificationError", identificationError != null ? identificationError : "")
                .setAttribute("raceColorError", raceColorError != null ? raceColorError : "")
                .setAttribute("stateError", stateError != null ? stateError : "")
                .setAttribute("cityError", cityError != null ? cityError : "")
                .createForm("profile-fields.ftl");
            context.challenge(challenge);
            return;
        }
        
        // Salva os dados no usuário
        user.setFirstName(firstName.trim());
        user.setLastName(lastName.trim());
        
        if (birthday != null && !birthday.trim().isEmpty()) {
            logger.debugf("DATA DE NASCIMENTO ANTES DO SET %s", birthday);
            user.setSingleAttribute("birthday", birthday);
        }
        
        if (identification != null && !identification.trim().isEmpty()) {
            user.setSingleAttribute("identification", identification);
        }

        if (raceColor != null && !raceColor.trim().isEmpty()) {
            user.setSingleAttribute("raceColor", raceColor);
        }

        if (state != null && !state.trim().isEmpty()) {
            user.setSingleAttribute("state", state);
        }
        
        if (city != null && !city.trim().isEmpty()) {
            user.setSingleAttribute("city", city);
        }
        
        user.setSingleAttribute("allowMemberMessages", allowMemberMessages != null ? allowMemberMessages : "off");
        user.setSingleAttribute("showProfileDirectory",  showProfileDirectory != null ? showProfileDirectory : "off");

        
        // Remove esta ação do usuário
        user.removeRequiredAction(PROVIDER_ID);
        
        logger.infof("User %s completed profile fields step", user.getUsername());
        
        context.success();
    }

    @Override
    public void close() {
        // Cleanup
    }
}