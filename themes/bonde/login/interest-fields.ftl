<#import "template.ftl" as layout>
<#import "forms.ftl" as forms>
<@layout.registrationLayout displayInfo=false displayMessage=false; section>

    <#if section = "header">
        Como você quer participar?
    <#elseif section = "description">
        Escolha os temas que você quer acompanhar e agir junto com a comunidade.
    <#elseif section = "form">
        <form id="kc-profile-form" action="${url.loginAction}" method="post">
            
            <#-- Mensagem global de erro/sucesso -->
            <#-- <#if message?has_content && message.summary?has_content>
                <@forms.alert 
                    type="${message.type!'info'}" 
                    message="${message.summary}" />
            </#if> -->
            
            <@forms.checkboxGroup
                name="themes"
                label="Temas de interesse"
                helpText="Escolha até 3 temas para aprimorar sua experiência."
                error=themesError
                required=true
                selectedValues=themes
                options=[
                    {"label": "🌱 Clima e meio ambiente", "value": "clima_meio_ambiente"},
                    {"label": "🏹 Direitos indígenas", "value": "direitos_indigenas"},
                    {"label": "🏠 Território e moradia", "value": "territorio_moradia"},
                    {"label": "📚 Educação", "value": "educacao"},
                    {"label": "🗳️ Política e democracia", "value": "politica_democracia"},
                    {"label": "🏳️‍🌈 Gênero e diversidade", "value": "genero_diversidade"},
                    {"label": "🏥 Saúde coletiva", "value": "saude_coletiva"},
                    {"label": "👮 Segurança pública", "value": "seguranca_publica"},
                    {"label": "🧑‍🤝‍🧑 Juventude", "value": "juventude"},
                    {"label": "🎨 Cultura e arte", "value": "cultura_arte"},
                    {"label": "📱 Comunicação e mídia", "value": "comunicacao_midia"},
                    {"label": "💼 Trabalho e economia", "value": "trabalho_economia"}
                ]
            />

            <hr />

            <@forms.checkboxGroup
                name="interests"
                label="Como você quer participar?"
                helpText="Escolha formas de participação que fazem sentido para você dentro do BONDE."
                error=interestsError
                required=true
                selectedValues=interests
                options=[
                    {"label": "Quero aprender", "description": "Formação, conteúdos, aulas e materiais de aprofundamento teórico e prático.", "value": "aprender"},
                    {"label": "Quero agir", "description": "Ações concretas, petições, campanhas e iniciativas de impacto imediato.", "value": "agir"},
                    {"label": "Quero mobilizar pessoas", "description": "Articular pessoas, organizar grupos, construir redes e liderar processos coletivos.", "value": "mobilizar"}
                ]
            />
            
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