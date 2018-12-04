<%-- 
    Document   : noContestar
    Created on : 15/02/2017, 12:20:51 PM
    Author     : Tania G. Sánchez
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>    
    <style>
        .icon-intrucc {
            position: absolute; 
            top: 50%; 
            margin-top: -17px;        
        }
        .para-instrucc {
            display:block; 
            margin-left: 30px;
        }
    </style>
    <link rel="stylesheet" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css" type="text/css" />    
    <title>Censo de Salud.</title>
</head> 

<content tag="tituloJSP">
    Censo de Salud.
</content>

<body>
    <!-- Mensajes -->
    <div class="row">
        <div class="col-xs-12">
            <s:if test="hasActionErrors()">
                <div class="alert alert-danger">
                    <i class="fa fa-times-circle fa-fw fa-lg"></i>
                    <strong>&iexcl;Error!</strong> <s:actionerror/>
                </div>
            </s:if>
            <s:if test="hasActionMessages()">
                <div class="alert alert-success">
                    <i class="fa fa-check-circle fa-fw fa-lg"></i>
                    <strong>&iexcl;Realizado!</strong> <s:actionmessage />
                </div>
            </s:if>
            <div class="alert alert-warning" id="reasonDiv" style="display: none">
                <i class="fa fa-info-circle fa-fw fa-lg"></i>
                <s:property value="reason" escape="false" />
            </div>
        </div>
    </div> 

<content tag="inlineScripts">
    <script>
        //JSP que muestra los mensajes de error o avisos que genera AdministraCuestionarioSaludAction
        $(document).ready(function () {
            if ("<s:property value="reason" escape="false" />" !== "")
                document.getElementById("reasonDiv").style.display = "block";
        });
    </script>
</content>
</body>
