<%-- 
    Document   : lista
    Created on : 27/10/2015, 02:09:23 PM
    Author     : Victor Lozano Author     
    Redesign   : Gustavo Adolfo Alamillo Medina
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Estado de procesos</title>
    <link rel="stylesheet" type="text/css" href="/vendors/fancy-box/jquery.fancybox.min.css"/>
    <link rel="stylesheet" type="text/css" href="/vendors/select2/select2-old/select2.min.css"/>
    <link rel="stylesheet" type="text/css" href="/vendors/select2/select2-old/select2-bootstrap.min.css"/>
</head> 

<content tag="tituloJSP">
    Estado de procesos
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

    <div class="row">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <div class="main-box-body">
                    <form id="busquedaForm" class="form-horizontal">
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Periodo escolar </label>
                            <div class="col-sm-10">
                                <s:select id="periodoEscolar"
                                          cssClass="form-control"
                                          cssStyle="width:100%"
                                          name="proceso.periodo.id" 
                                          list="ambiente.periodoList" 
                                          listKey="id" 
                                          listValue="clave"
                                          headerKey=""
                                          headerValue="-- Todos --" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">
                                Proceso
                            </label>
                            <div class="col-sm-10">
                                <s:select id="proceso"  
                                          cssClass="form-control"
                                          cssStyle="width:100%"
                                          name="proceso.proceso.id"
                                          list="procesoList" 
                                          listKey="id" 
                                          listValue="nombre" 
                                          headerKey=""
                                          headerValue="-- Todos --"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">
                                Unidad Académica
                            </label>
                            <div class="col-sm-10">
                                <s:select id="unidadAcademica"  
                                          cssClass="form-control"
                                          cssStyle="width:100%"
                                          name="proceso.unidadAcademica.id"
                                          list="ambiente.unidadAcademicaList" 
                                          listKey="id" 
                                          listValue="nombreSemiLargo" 
                                          headerKey=""
                                          headerValue="-- Todas --"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">
                                Estatus
                            </label>
                            <div class="col-sm-10">
                                <s:select id="estatus"
                                          cssClass="form-control"
                                          cssStyle="width:100%"
                                          name="proceso.estatus.id" 
                                          list="ambiente.procesoEstatusList" 
                                          listKey="id" 
                                          listValue="nombre" 
                                          headerKey=""
                                          headerValue="-- Todos --"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-12 col-md-12 col-lg-12">
                                <div class="pull-right">
                                    <input type="submit" id="buscar" class="btn btn-primary" value="Aplicar filtros"/>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <div class="row" id="rowTabla" style="display: none;">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <div class="col-md-12" style="padding: 0px">
                    <div class="responsive">                        
                        <table id="estadoProcesosTabla" class="table table-striped table-hover dt-responsive" style="width: 100%">
                            <thead>
                                <tr>
                                    <th data-orderable="true">Unidad Académica</th>
                                    <th data-orderable="true">Periodo</th>
                                    <th data-orderable="true" datate-order="ASC" >Proceso</th>
                                    <th data-orderable="true">Fecha inicial</th>
                                    <th data-orderable="true">Fecha final</th>
                                    <th data-orderable="true">Estatus</th>
                                    <th></th>
                                    <th></th>
                                    <th></th>
                                </tr>
                            </thead>
                        </table>                        
                    </div>
                </div>
            </div>
        </div>                
    </div>

    <form id="eliminarForm" action="/admin/eliminarProceso.action" method="POST" >
        <input type="hidden" name="proceso.id" id="id" value="" />
    </form>

</body>

<content tag="endScripts">
    <script type="text/javascript" src="/vendors/fancy-box/jquery.fancybox.min.js"></script>
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js"></script>
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>
    <script type="text/javascript" src="/vendors/select2/select2-old/select2.min.js"></script>
    <script type="text/javascript" src="/vendors/select2/select2-old/select2_locale_es.js"></script>
</content>

<content tag="inlineScripts">
    <script>

        var botonAgregar = [{
                text: '<a title="Agregar proceso" href="/admin/formProceso.action" class="fancybox fancybox.iframe btn btn-info"  name="nuevo" value="Nuevo" ><i class="fa fa-plus-circle fa-lg"></i>&nbsp;&nbsp;Agregar proceso</a>',
                className: 'normal-button'
            }
        ];

        $(document).ready(function () {
            $("#busquedaForm").submit(function (event) {
                event.preventDefault();
                $("#buscar").blur();
                recargarTabla();
            });

            $("#unidadAcademica,#proceso,#estatus,#periodoEscolar").select2({language: "es"});
        });

        function recargarTabla() {
            var id_proceso = Number($('#proceso option:selected').val());
            var id_periodo = Number($('#periodoEscolar option:selected').val());
            var id_ua = Number($('#unidadAcademica option:selected').val());
            var id_estatus = Number($('#estatus option:selected').val());
            var url = "/ajax/listadoProcesoAjax.action?search_id_periodo=" + id_periodo + "&search_id_proceso=" + id_proceso + "&search_id_ua=" + id_ua + "&search_id_estatus=" + id_estatus;
            generarTabla("rowTabla", url, despuesCargarTabla, true, botonAgregar);
        }

        function despuesCargarTabla() {
            $('.fancybox').fancybox({
                autoSize: true,
                afterClose: function () {
                    recargarTabla();
                }
            });
        }

        function eliminar(id) {

            ModalGenerator.notificacion({
                "titulo": "Advertencia",
                "cuerpo": "Esto borrará el registro seleccionado, ¿Está seguro de quererlo eliminar?",
                "tipo": "ALERT",
                "funcionAceptar": function () {
                    $("#id").val(id);
                    $("#eliminarForm").submit();
                }
            });

        }
    </script>
</content>