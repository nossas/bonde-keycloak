<#import "template.ftl" as layout>
<@layout.registrationLayout displayRequiredFields=false displayMessage=false; section>
 <#if section = "header">
    ${msg("magicLinkConfirmationTitle")}
    <#-- <div id="kc-username" class="${properties.kcFormGroupClass!}">
      <label id="kc-attempted-username">${auth.attemptedUsername}</label>
      <a id="reset-login" href="${url.loginRestartFlowUrl}" aria-label="${msg("restartLoginTooltip")}">
        <div class="kc-login-tooltip">
          <i class="${properties.kcResetFlowIcon!}"></i>
          <span class="kc-tooltip-text">${msg("restartLoginTooltip")}</span>
        </div>
      </a>
    </div> -->
  <#elseif section = "description">
    ${msg("magicLinkConfirmation")}
  <#elseif section = "form">
    <form action="${url.loginAction}" method="post">
      <div class="${properties.kcFormGroupClass!}">
        <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
          <button
            type="submit"
            class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}"
          >
            ${msg("doResend")}
          </button>
          <a id="reset-login" href="${url.loginRestartFlowUrl}" aria-label="${msg("restartLoginTooltip")}" class="view-email-reset-login pf-c-button pf-m-secondary pf-m-block btn-lg">« Reiniciar login</a>
        </div>
      </div>
    </form>
  </#if>
</@layout.registrationLayout>