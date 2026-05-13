<#macro btn>
  <#if auth?has_content && auth.showTryAnotherWayLink()>
    <form action="${url.loginAction}" method="post" id="kc-magic-link">
      <button class="pf-c-button pf-m-secondary pf-m-block btn-lg" type="submit" name="authenticationExecution" value="1437b020-7ba6-483e-bab7-1d9b0ae3c7ed">${msg('magicLinkButton')}</button>
    </form>
  </#if>
</#macro>