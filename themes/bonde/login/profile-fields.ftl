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
                name="birthday"
                label="Data de nascimento"
                helpText="Essa informação não será exibida publicamente."
                type="date"
                placeholder="dd/mm/aaaa"
                value="${(birthday!'')}"
                error="${(birthdayError!'')}" />
            
            <@forms.inputField 
                name="identification"
                label="Como você se identifica em relação ao seu gênero?"
                type="select"
                required=true
                placeholder="Selecionar"
                helpText="Essa resposta ajuda a gente a entender quem faz parte da comunidade. Ela não aparece no seu perfil."
                value="${(identification!'')}"
                error="${(identificationError!'')}"
                options=["cis_woman", "trans_woman", "cis_man", "trans_man", "non_binary", "unsure", "prefer_not"]
                optionLabels={
                    "cis_woman": "Mulher cisgênero",
                    "trans_woman": "Mulher transgênero",
                    "cis_man": "Homem cisgênero",
                    "trans_man": "Homem transgênero",
                    "non_binary": "Não binária",
                    "unsure": "Não sei responder",
                    "prefer_not": "Prefiro não responder"
                } />

            <@forms.inputField
                name="raceColor"
                label="Qual sua raça/cor, segundo os critérios do IBGE?"
                type="select"
                required=true
                placeholder="Selecionar"
                helpText="Essa informação nos ajuda a compreender quem participa da comunidade e orientar ações de equidade. Ela não aparece no seu perfil."
                value="${(raceColor!'')}"
                error="${(raceColorError!'')}"
                options=["white", "black", "brown", "yellow", "indigenous", "prefer_not"]
                optionLabels={
                    "white": "Branca",
                    "black": "Preta",
                    "brown": "Parda",
                    "yellow": "Amarela",
                    "indigenous": "Indígena",
                    "prefer_not": "Prefiro não responder"
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