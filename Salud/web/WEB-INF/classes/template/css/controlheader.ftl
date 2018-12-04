<#-- 

<#include "/${parameters.templateDir}/${parameters.theme}/controlheader-core.ftl">
<#if parameters.labelposition?default("top") == 'top'>
    <div <#rt/>
<#else>
    <span <#rt/>
</#if>
<#if parameters.id??>id="wwctrl_${parameters.id}"<#rt/></#if> class="wwctrl">

-->