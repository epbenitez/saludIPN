<%-- 
    Document   : Resumen de procesos
    Redesing   : Mario Márquez
--%>

<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <link rel="stylesheet" type="text/css" href="/vendors/pretty-checkbox/pretty.min.css"/>
    
    <title>Resumen de procesos</title>       
</head> 

<content tag="tituloJSP">
    Resumen de procesos
</content>

<body>
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
        </div>
    </div> 
    <div class="row">
        <div class="col-xs-12">
            <div class="main-box" >
                <div class="main-box-body clearfix">
                    <form class="form-horizontal" role="form">
                        <div class="form-group">
                            <label for="periodo" class="col-sm-2 control-label">Periodo</label>                                                            
                            <div class="col-sm-10">
                                <s:select 
                                    id="periodo"
                                    cssClass="form-control"
                                    name="periodo"
                                    list="ambiente.periodoList" 
                                    listKey="id"
                                    listValue="clave"                                    
                                    headerKey=""/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="movimiento" class="col-sm-2 control-label">Movimiento</label>                                                            
                            <div class="col-sm-10">
                                <s:select 
                                    id="movimiento"  
                                    cssClass="form-control"
                                    name="movimiento"
                                    list="resumenMovimientosList" 
                                    listKey="id" 
                                    listValue="nombre"                                     
                                    headerKey=""/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="unidadAcademica" class="col-sm-2 control-label">Unidad Académica</label>                                                                                        
                            <div class="col-sm-10">
                                <security:authorize ifAnyGranted="ROLE_RESPONSABLE_UA">
                                    <s:select 
                                        id="unidadAcademica"
                                        name="ua"
                                        cssClass="form-control"
                                        list="ambiente.unidadAcademicaList"
                                        listKey="id" 
                                        listValue="nombreCorto" 
                                        headerKey=""
                                        disabled="true"/>
                                </security:authorize>
                                <security:authorize ifNotGranted="ROLE_RESPONSABLE_UA">
                                    <s:select 
                                        id="unidadAcademica"
                                        name="ua"
                                        cssClass="form-control"
                                        list="ambiente.unidadAcademicaList"
                                        listKey="id" 
                                        listValue="nombreCorto"                                        
                                        headerKey=""/>
                                </security:authorize>
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-lg-12">                                                                                               
                                <button id="boton-filtro" type="submit" class="btn btn-primary pull-right">Aplicar filtros</button>
                            </div>                            
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- DIV para la tabla -->
    <div class="row" id="div-tabla" style="display: none">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <div class="col-md-12" style="padding: 0px" >
                    <div class="responsive">
                        <table id="listado" class="table table-striped table-hover dt-responsive" style="width: 100%">
                            <thead>
                                <tr>
                                    <th data-orderable="true">Nombre</th>
                                    <th data-orderable="true">Descripción</th>
                                    <th data-center="true">
                                        <div class="pretty outline-success smooth" >
                                            <input type="checkbox" id="todos"/> 
                                            <label><i class="glyphicon glyphicon-ok"></i></label>
                                        </div>
                                    </th>
                                </tr>
                            </thead>
                        </table>
                    </div>                    
                </div>
            </div>
        </div>
    </div>
    <form id="transferirForm" method="POST" >
        <input type="hidden" name="procesos-seleccionados" id="procesos-seleccionados" value="," />
    </form>            
</body>

<content tag="endScripts">    
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js"></script>
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>
        $(document).ready(function () {
            $("#boton-filtro").on("click", function (e) {
                e.preventDefault();
                filtrar();
            });
            
            $("#todos").change(function () {
                if (this.checked) {
                    $("#listado").find('input:checkbox:enabled').prop('checked', true);
                } else {
                    $("#listado").find('input:checkbox').prop('checked', false);
                }
            });
        });
        
        function filtrar() {

            var url = "/ajax/listadoAcumuladoProcesoAjax.action";
            var mov = $('#movimiento option:selected').val();
            var per = $('#periodo option:selected').val();
            var ua = $('#unidadAcademica option:selected').val();

            if (ua === 0) {
                ua = null;
            }
            if (per === 0) {
                per = null;
            }
            if (mov === 0) {
                mov = null;
            }
            url = url + "?periodo=" + per + "&movimiento=" + mov + "&ua=" + ua;

            var botonGenerarReporte = [{
                    text: 'Generar reporte',
                    className: 'btn-info',
                    action: function (e, dt, node, config) {
                        var current = $("#procesos-seleccionados").val();

                        if (current.length <= 1) {
                            ModalGenerator.notificacion({
                                "titulo": "Atención",
                                "cuerpo": "No ha sido seleccionado ningún elemento.",
                                "tipo": "INFO"
                            });
                            e.preventDefault();
                        } else {
                            var urlPdf = addURL(current);
                            window.open(urlPdf);
                        }
                    }
                }
            ];

            generarTabla("div-tabla", url, null, false, botonGenerarReporte);
            $("#boton-filtro").blur();                      // Evita que el botón sea desactivado
        }
        function transferir(check, id) {
            var current = $("#procesos-seleccionados").val();
            if (check) {
                if (current.indexOf("," + id + ",") > 0) {
                    //No agrega el elemento a la lista
                } else {
                    $("#procesos-seleccionados").val(current + id + ",");
                }
            } else {
                var porEliminar = "," + id + ",";
                current = current.replace(porEliminar, ",");
                $("#procesos-seleccionados").val(current);
            }
        }

        function clearURL() {
            $("#procesos-seleccionados").val(",");
        }

        function addURL(current) {
            clearURL();
            $("#boton-generar-reporte").blur();             // Evita que el botón sea desactivado
            return "/procesos/resumen/cuadroResumenAcumuladoProceso.action?procesosReportesList=" + current;
        }
    </script>
</content>