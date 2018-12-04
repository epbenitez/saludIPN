<%-- 
    Document   : Resultado carga masiva de ingresos per cápita.
    Created on : 30-Noviembre-2016
    Author     : Tania Gabriela Sánchez Manilla
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<head>
    <title>Resultado carga masiva de ingresos per cápita</title>
    <style>
        table, th, td {
            text-align: center;
        }
    </style>
</head> 
<content tag="tituloJSP">
    Resultado carga masiva de ingresos per cápita
</content>
<body>
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
    <s:if test="!hasActionErrors()">
        <!-- Ejemplo tabla -->
        <div class="row" id="rowtabla">
            <div class="col-lg-12">
                <div class="main-box clearfix">
                    <div class="responsive">                        
                        <table id="tabla" class="table table-striped table-hover dt-responsive" style="width: 100%">
                            <thead>
                                <tr>
                                    <th>Boleta</th>
                                    <th>Ingreso Per Cápita<br> Anterior</th>
                                    <th>Ingreso Per Cápita<br> Nuevo</th>
                                    <th>Resultado</th>
                                </tr>
                            </thead>
                            <tbody>
                                <s:iterator value="log" >
                                    <tr>
                                        <s:property escape="false"/>
                                    </tr>
                                </s:iterator>
                            </tbody>
                        </table>                        
                    </div>
                </div>
            </div>                
        </div>
        <!--Termina ejemplo tabla -->
    </s:if>
</body>

<content tag="endScripts">
    <script type="text/javascript" src="/vendors/datatables/datatables.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>
//        $("#tabla").dataTable();
    </script>
</content>