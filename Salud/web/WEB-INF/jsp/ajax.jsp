<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<s:iterator value="resultado">
    obj.options[obj.options.length] = new Option('<s:property value="value" escape="false" />','<s:property value="key" />');
</s:iterator>