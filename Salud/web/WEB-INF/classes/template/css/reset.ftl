
<span class="art-button-wrapper">
    <span class="l"> </span>
    <span class="r"> </span>

<#if parameters.type?? && parameters.type=="button">
<button type="reset"<#rt/>
<#if parameters.name??>
 name="${parameters.name?html}"<#rt/>
</#if>
<#if parameters.nameValue??>
 value="<@s.property value="parameters.nameValue"/>"<#rt/>
</#if>
  class="button art-button"
<#if parameters.cssStyle??>
 style="${parameters.cssStyle?html}"<#rt/>
</#if>
<#include "/${parameters.templateDir}/simple/scripting-events.ftl"/>
<#include "/${parameters.templateDir}/simple/common-attributes.ftl" />
<#include "/${parameters.templateDir}/simple/dynamic-attributes.ftl" />
><#if parameters.label??><@s.property value="parameters.label"/><#rt/></#if></button>
<#else>
<input type="reset"<#rt/>
<#if parameters.name??>
 name="${parameters.name?html}"<#rt/>
</#if>
<#if parameters.nameValue??>
 value="<@s.property value="parameters.nameValue"/>"<#rt/>
</#if>
  class="button art-button"
<#if parameters.cssStyle??>
 style="${parameters.cssStyle?html}"<#rt/>
</#if>
<#if parameters.title??>
 title="${parameters.title?html}"<#rt/>
</#if>
<#include "/${parameters.templateDir}/simple/scripting-events.ftl" />
<#include "/${parameters.templateDir}/simple/common-attributes.ftl" />
<#include "/${parameters.templateDir}/simple/dynamic-attributes.ftl" />
/>
</#if>
</span>