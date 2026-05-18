<#-- 
    Macro para campos de input seguindo o padrão PatternFly do Keycloak
    Baseado nos templates nativos login-update-profile.ftl e register.ftl [citation:1]
-->

<#macro inputField
    name 
    label 
    type="text" 
    required=false 
    placeholder="" 
    helpText=""
    value="" 
    error="" 
    options=[] 
    optionLabels={}>

    <div class="${properties.kcFormGroupClass!}">
        <label for="${name}" class="${properties.kcLabelClass!}">
            ${label}
            <#if required>
                <span class="required">*</span>
            </#if>
        </label>
        <#if type == "text" || type == "email" || type == "date" || type == "tel">
            <input type="${type}" 
                    id="${name}"
                    name="${name}"
                    value="${(value!'')}"
                    placeholder="${placeholder}"
                    class="${properties.kcInputClass!} ${(error?has_content)?then('pf-m-error', '')}"
                    aria-invalid="<#if error?has_content>true<#else>false</#if>"
                    <#if required>required</#if>>

        <#elseif type == "select">
            <select id="${name}" 
                    name="${name}" 
                    class="${properties.kcInputClass!} ${(error?has_content)?then('pf-m-error', '')}"
                    aria-invalid="<#if error?has_content>true<#else>false</#if>"
                    <#if required>required</#if>>
                <option value="">${placeholder}</option>
                <#list options as option>
                    <option value="${option}" 
                        <#if value == option>selected</#if>>
                        ${optionLabels[option]!option}
                    </option>
                </#list>
            </select>

        <#elseif type == "textarea">
            <textarea id="${name}" 
                        name="${name}" 
                        class="${properties.kcInputClass!} ${(error?has_content)?then('pf-m-error', '')}"
                        aria-invalid="<#if error?has_content>true<#else>false</#if>"
                        <#if required>required</#if>
                        rows="4">${(value!'')}</textarea>
        </#if>

        <#if helpText?has_content>
            <div class="pf-c-helper-text">
                <div class="pf-c-helper-text__item">
                    <span class="pf-c-helper-text__item-text">${kcSanitize(helpText)?no_esc}</span>
                </div>
            </div>
        </#if>

        <#if error?has_content>
            <span id="input-error-${name}" class="${properties.kcInputErrorMessageClass!}" aria-live="polite">
                ${kcSanitize(error)?no_esc}
            </span>
        </#if>
    </div>
</#macro>

<#-- 
    Macro para checkbox seguindo PatternFly
    Usa a estrutura dos checkboxes nos formulários nativos
-->
<#macro checkbox 
    name 
    label 
    value="">

    <label class="pf-c-switch" for="${name}">
        <input
            class="pf-c-switch__input"
            type="checkbox"
            id="${name}" 
            name="${name}"
            <#if value == "on">checked</#if>
        />

        <span class="pf-c-switch__toggle"></span>
        <span class="pf-c-switch__text">${label}</span>
    </label>
</#macro>

<#-- 
    Macro para grupo de checkboxes (múltiplas opções)
-->
<#macro checkboxGroup 
    name 
    label=""
    options=[] 
    selectedValues=[]
    required=false
    error=""
    helpText="">

    <div class="${properties.kcFormGroupClass!}">
        <#if label?has_content>
            <#if error?has_content><div class="label-error"></#if>
            <label class="${properties.kcLabelClass!}">
                ${label}
                <#if required><span class="required">*</span></#if>
            </label>
            <#if error?has_content>
                <span id="input-error-${name}" class="${properties.kcInputErrorMessageClass!}" aria-live="polite">
                    ${kcSanitize(error)?no_esc}
                </span>
            </#if>
            <#if error?has_content></div></#if>
        </#if>
        
        <#if helpText?has_content>
            <div class="pf-c-helper-text">
                <div class="pf-c-helper-text__item">
                    <span class="pf-c-helper-text__item-text">${kcSanitize(helpText)?no_esc}</span>
                </div>
            </div>
        </#if>
        
        <div class="pf-c-toggle-group" role="group" aria-label="${label}">
            <#list options as option>
                <#local isChecked = selectedValues?has_content && selectedValues?seq_contains(option.value) />
                <div class="pf-c-toggle-group__item">
                    <input class="pf-c-toggle-group__input" 
                        type="checkbox" 
                        id="${name}-${option.value}" 
                        name="${name}" 
                        value="${option.value}"
                        ${isChecked?then('checked', '')}>
                    <label class="pf-c-toggle-group__button" for="${name}-${option.value}">
                        <span class="pf-c-toggle-group__text">
                            <#if option.icon??>
                                <i class="${option.icon}" aria-hidden="true"></i>
                            </#if>
                            ${option.label}
                        </span>
                        <#if option.description?has_content>
                            <span class="pf-c-toggle-group__description">${option.description}</span>
                        </#if>
                    </label>
                </div>
            </#list>
        </div>
    </div>
</#macro>

<#-- 
    Macro para alertas/mensagens (PatternFly alert)
-->
<#macro alert type="info" message="">
    <#if message?has_content>
        <div class="pf-c-alert pf-m-${type}" aria-label="${type} alert">
            <div class="pf-c-alert__icon">
                <#if type == "danger">
                    <i class="fas fa-exclamation-circle"></i>
                <#elseif type == "success">
                    <i class="fas fa-check-circle"></i>
                <#else>
                    <i class="fas fa-info-circle"></i>
                </#if>
            </div>
            <div class="pf-c-alert__title">
                ${kcSanitize(message)?no_esc}
            </div>
        </div>
    </#if>
</#macro>