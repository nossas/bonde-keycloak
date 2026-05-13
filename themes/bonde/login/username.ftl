<#macro restartLogin>
<div id="kc-username" class="${properties.kcFormGroupClass!}">
    <label id="kc-attempted-username">
        <span>${msg("showUsername")}</span> <strong>${auth.attemptedUsername}</strong>
    </label>
    <a id="reset-login" href="${url.loginRestartFlowUrl}" aria-label="${msg("restartLoginTooltip")}">
        ${msg("restartLogin")}
        <div class="kc-login-tooltip">
            <i class="${properties.kcResetFlowIcon!}"></i>
            <span class="kc-tooltip-text">${msg("restartLoginTooltip")}</span>
        </div>
    </a>
</div>
</#macro>