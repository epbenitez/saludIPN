<%-- 
    Document   : JSP Boton Validacion Inscripcion.
    Created on : 28-Septiembre-2016
    Author     : Gustavo Adolfo Alamillo Medina
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Validar Inscripción</title>

</head> 

<content tag="tituloJSP">
    Validar Inscripción
</content>

<body>
    <div class="row">
        <div class="col-sm-8">
            <div class="main-box">
                <header class="main-box-header clearfix">
                    <h4><strong>Consideraciones</strong></h4>
                </header>
                <div class="main-box-body clearfix">                        
                    <p style="text-align: justify;">El proceso de Validación de Inscripción permite realizar:<br><br>
                        <strong>1.</strong> El pase de datos académicos de la vista de la DAE a la estructura de datos del SIBec.<br>
                        <strong>2.</strong> La pre-asignación de las solicitudes del periodo anterior con las becas del periodo actual.<br><br>
                        Para comenzar el primer paso, de clic en el siguiente botón y al terminar, obtendrá un resumen de la ejecución del proceso.</p>
                    <form id="validar" class="form-horizontal" action="/admin/listaValidacionInscripcion.action"
                          method="post">
                        <div class="form-group pull-right">
                            <div class="col-sm-12 col-md-12 col-lg-12 ">
                                <input type="submit" id="validar" class="btn btn-primary" value="Paso 1: Actualizar Datos Académicos."/>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div class="col-sm-4">
            <div class="row" id="contenedor">
                <div class="main-box">
                    <header class="main-box-header clearfix">
                        <h4>Lista de becas consideradas</h4>
                    </header>
                    <div class="main-box clearfix">
                        <div class="col-md-12" style="padding: 0px">
                            <div class="responsive">                        
                                <table id="adentrocontendedor" class="table table-striped table-hover dt-responsive" style="width: 100%">
                                    <thead>
                                        <tr>
                                            <th data-orderable="true">Nombre</th>
                                        </tr>
                                    </thead>
                                </table>                        
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>

<content tag="endScripts">
    <script>
        var dataSets = new Array();
        <s:iterator value="becas" var="beca" status="ls">
        dataSets.push(["<s:property value="beca"/>"]);
        </s:iterator>
        function alteraTabla() {
            $("#dataTableHeader-contenedor").hide();
            $("#divInfoPaginacion-contenedor").hide();
            $("#divSelectPaginacion-contenedor").removeClass("col-sm-5");
        }
    </script>
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js" data-init="true" data-ajax="dataSets" data-div="contenedor" data-function="alteraTabla" data-length="5"></script>
</content>

<content tag="inlineScripts">
</content>