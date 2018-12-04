<%-- 
    Document   : Resultado de template
    Created on : 28-Septiembre-2016
    Author     : Gustavo Adolfo Alamillo Medina
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Resultado de carga</title>
    <link rel="stylesheet" type="text/css" href="/vendors/datatables/datatables.min.css">
    <link rel="stylesheet" type="text/css" href="/resources/css/customTables.min.css" />
</head> 

<content tag="tituloJSP">
    Resultado de la carga de tarjeta bancarias
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

    <s:if test="!hasActionErrors()">
        <div class="row" id="rowTabla">
            <div class="col-xs-12">
                <div class="main-box clearfix">
                    <div class="col-md-12" style="padding: 0px">
                        <div class="responsive">                        
                            <table id="resultadoTabla" class="table table-striped table-hover dt-responsive" style="width: 100%">
                                <thead>
                                    <tr>
                                        <th># Renglón</th>
                                        <th>Número de tarjeta</th>
                                        <th>Estatus</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <s:iterator value="log" >
                                        <tr>
                                            <s:property escape="false" ></s:property>
                                            </tr>
                                    </s:iterator>
                                </tbody>
                            </table>                        
                        </div>
                    </div>
                </div>
            </div>                
        </div>
    </s:if>

</body>

<content tag="endScripts">
    <script type="text/javascript" src="/vendors/datatables/datatables.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>
        $(document).ready(function () {
            $("#resultadoTabla").dataTable({
                "dom": "t",
                "order": [[0, "asc"]]
            });
        });
    </script>
</content>