<#import "template.ftl" as layout>
<#import "forms.ftl" as forms>
<@layout.registrationLayout displayInfo=false; section>

    <#if section = "header">
        Crie seu perfil
    <#elseif section = "description">
        Você pode completar ou editar suas informações a qualquer momento.
    <#elseif section = "form">
        <form id="kc-profile-form" action="${url.loginAction}" method="post">
            
            <#-- Mensagem global de erro/sucesso -->
            <#if message?has_content && message.summary?has_content>
                <@forms.alert 
                    type="${message.type!'info'}" 
                    message="${message.summary}" />
            </#if>
            
            <@forms.inputField
              name="firstName"
              label="Primeiro nome"
              required=true
              placeholder="Seu nome"
              value="${(firstName!'')}"
              error="${(firstNameError!'')}" />

            <@forms.inputField
              name="lastName"
              label="Sobrenome"
              required=true
              placeholder="Seu sobrenome"
              value="${(lastName!'')}"
              error="${(lastNameError!'')}" />

            <@forms.inputField 
                name="dateOfBirth"
                label="Data de nascimento"
                helpText="Essa informação não será exibida publicamente."
                type="date"
                placeholder="dd/mm/aaaa"
                value="${(dateOfBirth!'')}"
                error="${(dateOfBirthError!'')}" />
            
            <@forms.inputField 
                name="identification"
                label="Como você se identifica?"
                type="select"
                required=true
                placeholder="Selecionar"
                helpText="Essa resposta ajuda a gente a entender quem faz parte da comunidade. Ela não aparece no seu perfil."
                value="${(identification!'')}"
                error="${(identificationError!'')}"
                options=["woman", "man", "non_binary", "prefer_not"]
                optionLabels={
                    "woman": "Mulher (ela)",
                    "man": "Homem (ele)",
                    "non_binary": "Pessoa não-binária (elu)",
                    "prefer_not": "Prefiro não dizer"
                } />
            
            <@forms.inputField 
                name="state"
                label="Estado"
                type="select"
                required=true
                placeholder="Selecionar"
                value="${(state!'')}"
                error="${(stateError!'')}"
                options=["AC","AL","AP","AM","BA","CE","DF","ES","GO","MA","MT","MS","MG","PA","PB","PR","PE","PI","RJ","RN","RS","RO","RR","SC","SP","SE","TO"]
                optionLabels={
                    "AC": "Acre", "AL": "Alagoas", "AP": "Amapá", "AM": "Amazonas",
                    "BA": "Bahia", "CE": "Ceará", "DF": "Distrito Federal", "ES": "Espírito Santo",
                    "GO": "Goiás", "MA": "Maranhão", "MT": "Mato Grosso", "MS": "Mato Grosso do Sul",
                    "MG": "Minas Gerais", "PA": "Pará", "PB": "Paraíba", "PR": "Paraná",
                    "PE": "Pernambuco", "PI": "Piauí", "RJ": "Rio de Janeiro", "RN": "Rio Grande do Norte",
                    "RS": "Rio Grande do Sul", "RO": "Rondônia", "RR": "Roraima", "SC": "Santa Catarina",
                    "SP": "São Paulo", "SE": "Sergipe", "TO": "Tocantins"
                } />
            
            <@forms.inputField 
                name="city"
                label="Cidade"
                type="text"
                required=true
                placeholder="Sua cidade"
                value="${(city!'')}"
                error="${(cityError!'')}" />
            
            <div class="checkbox-group">
              <@forms.checkbox 
                  name="allowMemberMessages"
                  label="Permitir que membros enviem mensagens para você."
                  value="${(allowMemberMessages!'')}" />
                  
              <@forms.checkbox 
                  name="showProfileDirectory"
                  label="Mostrar seu perfil no diretório de membros."
                  value="${(showProfileDirectory!'')}" />
            </div>
            
            <div class="${properties.kcFormGroupClass!}">
              <div id="kc-form-buttons">
                  <#if isAppInitiatedAction??>
                      <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonLargeClass!}" type="submit" value="Continuar" />
                      <button class="${properties.kcButtonClass!} ${properties.kcButtonDefaultClass!} ${properties.kcButtonLargeClass!}" type="submit" name="cancel-aia" value="true" formnovalidate>${msg("doCancel")}</button>
                  <#else>
                      <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}" type="submit" value="Continuar" />
                  </#if>
              </div>
          </div>
        </form>
    </#if>
</@layout.registrationLayout>