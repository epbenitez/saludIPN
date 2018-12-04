<%-- 
    Document   : cargaSolicitudes
    Created on : 7/04/2017, 03:36:57 PM
    Author     : Rafael Cárdenas Rséndiz
--%>

<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Carga de respuestas bancarias</title>

</head> 

<content tag="tituloJSP">
    Carga de respuestas bancarias
</content>

<body>

    <div class="col-md-12">
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
    </div>
    
     <div class="row">
        <div class="col-sm-12">
            <div class="col-sm-4 col-xs-12">
                <ul> Existosos : <s:property value="exitosos"/> </ul>
            </div>
            <div class="col-sm-4 col-xs-12">
                <ul> Rechazados : <s:property value="rechazados"/> </ul>
            </div>
            <div class="col-sm-4 col-xs-12">
                <ul> Total: <s:property value="total"/>  </ul>
            </div>
            
        </div>
    </div>
    <div class="row" id="rowError" >
        <div class="col-xs-12">
            <div class="col-md-12">                        
                <table id="tablaError" class="table table-striped" >
                    <thead>
                        <tr>
                            <th>BOLETA</th>
                            <th>ERROR</th>
                        </tr>
                    </thead>
                </table>              
            </div>
        </div>                
    </div>

</body>


<content tag="endScripts">
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js" data-init="true" data-ajax="dataSets" data-div="rowError"></script>
</content>

<content tag="inlineScripts">
    <script>
        var dataSets = new Array();
            <s:iterator value="errores">
            dataSets.push(["<s:property value="k1"/>"  , "<s:property value="k2"/>"]);
            </s:iterator>
    </script>


</content>