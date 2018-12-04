

<#include "/${parameters.templateDir}/${parameters.theme}/controlheader-radio-check.ftl">
<#assign itemCount = 0/>
<#if parameters.list??>

<#if parameters.maxLines?exists>
	<#assign maxLines = parameters.maxLines />
    <div class="col">
    <#assign itemCount2 = 0 />
</#if>

    <@s.iterator value="parameters.list">
        <#assign itemCount = itemCount + 1/>
        <#if parameters.listKey??>
            <#assign itemKey = stack.findValue(parameters.listKey)/>
        <#else>
            <#assign itemKey = stack.findValue('top')/>
        </#if>
        <#if parameters.listValue??>
            <#assign itemValue = stack.findString(parameters.listValue)?default("")/>
        <#else>
            <#assign itemValue = stack.findString('top')/>
        </#if>
        <#assign itemKeyStr=itemKey.toString() />

<#if parameters.maxLines?exists>
    <#if maxLines == itemCount2>
        <#assign itemCount2 = 0 />
        </div>
        <div class="col">
    </#if>
    <#assign itemCount2 = itemCount2 + 1/>
</#if>

        <input type="checkbox" name="${parameters.name?html}" value="${itemKeyStr?html}" id="${parameters.name?html}-${itemCount}"<#rt/>
        <#if tag.contains(parameters.nameValue, itemKey)>
            checked="checked"<#rt/>
        </#if>
        <#if parameters.disabled?default(false)>
            disabled="disabled"<#rt/>
        </#if>
        <#if parameters.title??>
            title="${parameters.title?html}"<#rt/>
        </#if>
        <#include "/${parameters.templateDir}/simple/scripting-events.ftl" />
        <#include "/${parameters.templateDir}/simple/common-attributes.ftl" />
        />
        <label for="${parameters.name?html}-${itemCount}" class="checkboxLabel">${itemValue?html}</label>
        ${parameters.labelseparator?default("")}
    </@s.iterator>

<#if parameters.maxLines?exists>
    </div>
    <div style="clear:both"></div>
</#if>

<#else>
    &nbsp;
</#if>
<#include "/${parameters.templateDir}/${parameters.theme}/controlfooter.ftl" />
<#if parameters.labelseparator??><#if parameters.label??><div style="clear:both"></div></#if></#if>
<#nt/>