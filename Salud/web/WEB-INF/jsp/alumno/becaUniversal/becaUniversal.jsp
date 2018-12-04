<%-- 
    Document   : becaUniversal.jsp
    Created on : 16/10/2017, 04:26:33 PM
    Author     : Mario Márquez
--%>

<%-- 
    Document   : Template SIBEC
    Created on : 28-Septiembre-2016
    Author     : Gustavo Adolfo Alamillo Medina
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Beca Universal</title>

</head> 

<content tag="tituloJSP">
    Beca Universal
</content>

<body>
    <s:if test="hasActionErrors()">
    <div class="alert alert-danger">
        <i class="fa fa-times-circle fa-fw fa-lg"></i>
        <strong>&iexcl;Error!</strong> <s:actionerror/>
    </div>
    </s:if>
</body>