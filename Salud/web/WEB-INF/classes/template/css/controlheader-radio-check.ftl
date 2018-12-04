

<#--
	Only show message if errors are available.
	This will be done if ActionSupport is used.
-->
<#assign hasFieldErrors = parameters.name?? && fieldErrors?? && fieldErrors[parameters.name]??/>
<div <#if parameters.id??>id="wwgrp_${parameters.id}"<#rt/></#if> class="wwgrp"  >
<#if hasFieldErrors>
   ${error?html}
</#if>
<#if parameters.label??>
    <#if parameters.labelposition?default("top") == 'top'>
        <div <#t/>
    <#else>
        <span <#t/>
    </#if>
    <#if parameters.id??>id="wwlbl_${parameters.id}"<#t/></#if> class="wwlbl<#if parameters.labelseparator?? && parameters.labelposition?default("top") != 'especial'> col</#if>"><#t />
    <label <#t/>
        <#if parameters.id??>
            for="${parameters.id?html}" <#t/>
        </#if>
        <#if hasFieldErrors>
            class="errorLabel"<#t/>
        <#else>
            class="label"<#t/>
        </#if>
    ><#t/>
    ${parameters.label?html}<#t /><#-- ${parameters.labelseparator!":"?html} -->
    <#--
        Show required message to the right
    -->
    <#if parameters.required?default(false)>
        <span class="required">*</span><#t />
    </#if>
    <#lt /> :<#rt />
	</label><#t/>
    <#if parameters.labelposition?default("top") == 'top'>
        </div><#lt/>
    <#else>
        </span><#lt/>
    </#if>
</#if>
<#if parameters.labelposition?default("top") == 'top'>
    <div <#rt/>
<#else>
    <span <#rt/>
</#if>
<#if parameters.id??>id="wwctrl_${parameters.id}"<#rt/></#if> class="wwctrl<#if parameters.labelseparator??><#if parameters.label??> col</#if></#if>">