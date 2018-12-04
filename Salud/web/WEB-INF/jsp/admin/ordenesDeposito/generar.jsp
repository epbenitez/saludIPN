<%-- 
Document   : generar
Created on : 15/01/2016, 01:55:38 PM
Author     : Tania G. Sánchez    
Redesign   : Gustavo Adolfo Alamillo Medina
--%>

<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Detalle de la orden de depósito</title>

</head> 

<content tag="tituloJSP">
    Detalle de la orden de depósito
</content>

<body>

    <div class="row">
        <div class="col-sm-12">
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
    </div>

    <div class="row" id="rowTable" style="display: none;">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <div class="col-md-12" style="padding: 0px">
                    <div class="responsive">                        
                        <table id="table" class="table table-striped table-hover dt-responsive" style="width: 100%">
                            <thead>
                                <tr>
                                    <th data-orderable="true" data-default-order="true" data-default-dir="desc">Unidad Académica</th>
                                    <th>Programa</th>
                                    <th>Becarios</th>
                                    <th>Importe</th>
                                </tr>
                            </thead>
                        </table>                        
                    </div>
                </div>
            </div>
        </div>                
    </div>

</body>

<content tag="endScripts">
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js"
            data-init="true" data-div="rowTable" data-length="50"
            data-button="botonExcel" 
    data-ajax="/ajax/resumenOrdenesDepositoAjax.action?noOrden=<s:property value="ordenId"/>"></script> 
</content>

<content tag="inlineScripts">
    <script>
        var botonExcel = ["excel"];
    </script>
</content>