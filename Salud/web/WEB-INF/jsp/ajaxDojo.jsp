

<%@taglib prefix="s" uri="/struts-tags" %>

<s:iterator value="resultado">
    obj.addOption(dojo.create("option", {label: '<s:property value="value" escape="false" />', value: "<s:property value="key" />"}));
</s:iterator>