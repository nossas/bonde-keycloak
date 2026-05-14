<#macro btn>
  <#if auth?has_content && auth.showTryAnotherWayLink()>
    <form action="${url.loginAction}" method="post" id="kc-magic-link">
      <#list auth.authenticationSelections as authenticationSelection>
        <#if authenticationSelection.displayName == "ext-magic-form-display-name">
          <button class="pf-c-button pf-m-secondary pf-m-block btn-lg" type="submit" name="authenticationExecution" value="${authenticationSelection.authExecId}">
            ${msg('magicLinkButton')}
          </button>
        </#if>
      </#list>
    </form>
  </#if>
</#macro>