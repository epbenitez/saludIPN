

<#include "/${parameters.templateDir}/${parameters.theme}/controlheader.ftl" />
<#-- <#include "/${parameters.templateDir}/simple/text.ftl" /> -->

<input type="text"<#rt/>
 name="${parameters.name?default("")?html}"<#rt/>
<#if parameters.get("size")??>
 size="${parameters.get("size")?html}"<#rt/>
</#if>
<#if parameters.maxlength??>
 maxlength="${parameters.maxlength?html}"<#rt/>
</#if>
<#if parameters.nameValue??>
 value="<@s.property value="parameters.nameValue"/>"<#rt/>
</#if>
<#if parameters.class??>
 class="${parameters.class}"<#rt/>
</#if>
<#if parameters.disabled?default(false)>
 disabled="disabled"<#rt/>
</#if>
<#if parameters.readonly?default(false)>
 readonly="readonly"<#rt/>
</#if>
<#if parameters.tabindex??>
 tabindex="${parameters.tabindex?html}"<#rt/>
</#if>
<#if parameters.id??>
 id="${parameters.id?html}"<#rt/>
</#if>
<#include "/${parameters.templateDir}/simple/css.ftl" />
<#if parameters.title??>
 title="${parameters.title?html}"<#rt/>
</#if>
<#if parameters.constraints??>
 constraints='${parameters.constraints}'<#rt/>
</#if>

<#include "/${parameters.templateDir}/simple/scripting-events.ftl" />
<#include "/${parameters.templateDir}/simple/common-attributes.ftl" />
<#include "/${parameters.templateDir}/simple/dynamic-attributes.ftl" />
/>
<#include "/${parameters.templateDir}/${parameters.theme}/controlfooter.ftl" />