

<#setting number_format="#.#####">
<select<#rt/>
    <#lt /> name="${parameters.name?default("")?html}"<#rt/>
    <#if parameters.get("size")??>
        <#lt /> size="${parameters.get("size")?html}"<#rt/>
    </#if>
    <#if parameters.disabled?default(false)>
        <#lt /> disabled="disabled"<#rt/>
    </#if>
    <#if parameters.tabindex??>
        <#lt /> tabindex="${parameters.tabindex?html}"<#rt/>
    </#if>
    <#if parameters.id??>
        <#lt /> id="${parameters.id?html}"<#t/>
    </#if>
    <#include "/${parameters.templateDir}/simple/css.ftl" />
    <#if parameters.title??>
        <#lt /> title="${parameters.title?html}"<#t/>
    </#if>
    <#if parameters.multiple?default(false)>
        <#lt /> multiple="multiple"<#t/>
    </#if>
    <#include "/${parameters.templateDir}/simple/scripting-events.ftl" />
    <#include "/${parameters.templateDir}/simple/common-attributes.ftl" />
    <#include "/${parameters.templateDir}/simple/dynamic-attributes.ftl" />
>
<#if parameters.headerKey?? && parameters.headerValue??>
    <option value="${parameters.headerKey?html}"<#t />
    <#if tag.contains(parameters.nameValue, parameters.headerKey) == true>
        <#lt /> selected="selected"<#rt/>
    </#if>
    >${parameters.headerValue?html}</option><#lt />
</#if>
<#if parameters.emptyOption?default(false)>
    <option value=""></option>
</#if>

<#if parameters.listType?exists>
    <#assign listType = parameters.listType />
    <#assign itemCount = 0 />
</#if>

<@s.iterator value="parameters.list">
    <#if parameters.listKey??>
        <#if stack.findValue(parameters.listKey)??>
            <#assign itemKey = stack.findValue(parameters.listKey)/>
            <#assign itemKeyStr = itemKey.toString()/>
        <#else>
            <#assign itemKey = ''/>
            <#assign itemKeyStr = ''/>
        </#if>
    <#else>
        <#assign itemKey = stack.findValue('top')/>
        <#assign itemKeyStr = itemKey.toString()/>
    </#if>
    <#if parameters.listValue??>
        <#if stack.findString(parameters.listValue)??>
          <#assign itemValue = stack.findString(parameters.listValue)/>
        <#else>
          <#assign itemValue = ''/>
        </#if>
    <#else>
        <#assign itemValue = stack.findString('top')/>
    </#if>

    <#if parameters.listType?exists>
        <#assign itemCount = itemCount + 1/>
        <#if itemCount == 3>
            <#if listType == 2>
                <option value="-1" disabled="disabled">--------------------------------------</option>
            </#if>
        </#if>
        <#if itemCount == 4>
            <#if listType == 1>
                <option value="-1" disabled="disabled">--------------------------------------</option>
            </#if>
        </#if>
    </#if>

    <option value="${itemKeyStr?html}"<#rt/>
        <#if tag.contains(parameters.nameValue, itemKey) == true>
            <#lt /> selected="selected"<#rt/>
        </#if>
    >${itemValue?html}</option><#lt/>

</@s.iterator>
<#include "/${parameters.templateDir}/simple/optgroup.ftl" />
</select>