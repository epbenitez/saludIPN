<%--
    Document   : ajaxUploadTicket
    Created on : Feb 15, 2011, 01:27:21 PM
    Author     : Patricia Ben�tez
--%>

<%@taglib prefix="s" uri="/struts-tags" %>

<s:if test="resultado.uploadAdjuntoFileName != null && resultado.uploadAdjuntoFileName.length() > 0">
    fileName1.value = '<s:property value="resultado.uploadAdjuntoFileName" />';
</s:if>