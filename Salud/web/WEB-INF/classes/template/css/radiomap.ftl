

<#include "/${parameters.templateDir}/${parameters.theme}/controlheader-radio-check.ftl">

<#if parameters.maxLines?exists>
	<#assign maxLines = parameters.maxLines />
    <div class="col">
    <#assign itemCount = 0 />
</#if>

<@s.iterator value="parameters.list">
    <#if parameters.listKey??>
        <#assign itemKey = stack.findValue(parameters.listKey)/>
    <#else>
        <#assign itemKey = stack.findValue('top')/>
    </#if>
    <#assign itemKeyStr = itemKey.toString() />
    <#if parameters.listValue??>
        <#assign itemValue = stack.findString(parameters.listValue)/>
    <#else>
        <#assign itemValue = stack.findString('top')/>
    </#if>

<#if parameters.maxLines?exists>
    <#if maxLines == itemCount>
        <#assign itemCount = 0 />
        </div>
        <div class="col">
    </#if>
    <#assign itemCount = itemCount + 1/>
</#if>
    <input type="radio"<#t/>
        <#if parameters.name??>
            <#lt /> name="${parameters.name?html}"<#rt/>
        </#if>
        <#lt /> id="${parameters.id?html}${itemKeyStr?html}"<#rt/>
        <#if tag.contains(parameters.nameValue?default(''), itemKeyStr)>
            <#lt /> checked="checked"<#rt/>
        </#if>
        <#if itemKey??>
            <#lt /> value="${itemKeyStr?html}"<#rt/>
        </#if>
        <#if parameters.disabled?default(false)>
            <#lt /> disabled="disabled"<#rt/>
        </#if>
        <#if parameters.tabindex??>
            <#lt /> tabindex="${parameters.tabindex?html}"<#rt/>
        </#if>
        <#if parameters.cssClass??>
            <#lt /> class="${parameters.cssClass?html}"<#rt/>
        </#if>
        <#if parameters.cssStyle??>
            <#lt /> style="${parameters.cssStyle?html}"<#rt/>
        </#if>
        <#if parameters.title??>
            <#lt /> title="${parameters.title?html}"<#rt/>
        </#if>
        <#include "/${parameters.templateDir}/simple/scripting-events.ftl" />
        <#include "/${parameters.templateDir}/simple/common-attributes.ftl" />
    /><#t/>
    <label for="${parameters.id?html}${itemKeyStr?html}">${itemValue}</label><#t/>
    ${parameters.labelseparator?default("")}
</@s.iterator>

<#if parameters.maxLines?exists>
    </div>
    <div style="clear:both"></div>
</#if>

<#include "/${parameters.templateDir}/${parameters.theme}/controlfooter.ftl" />
<#if parameters.labelseparator??><#if parameters.label??><div style="clear:both"></div></#if></#if>
<#nt/>