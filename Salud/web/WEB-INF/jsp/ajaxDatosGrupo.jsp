<%-- 
    Document   : ajaxDatosGrupo
    Created on : 29/12/2009, 11:09:20 PM
    Author     : maricarmen
--%>

<%@taglib prefix="s" uri="/struts-tags" %>

<s:iterator value="resultado">
    grupoDatos.value ='<s:property value="value" escape="false"/>'
</s:iterator>


