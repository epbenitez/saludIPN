<%-- 
    Document   : Listado
    Created on : 28-ago-2015, 10:23:12
    Author     : Tania Sánchez
    Redesign     : Gustavo Adolfo Alamillo Medina
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Administración de periodos</title>
    <link rel="stylesheet" type="text/css" href="/vendors/fancy-box/jquery.fancybox.min.css"/>
</head> 

<content tag="tituloJSP">
    Administración de periodos
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

    <div class="row" id="rowTable" style="display: none;">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <div class="col-md-12" style="padding: 0px">
                    <div class="responsive">                        
                        <table id="tablaPeriodos" class="table table-striped table-hover dt-responsive" style="width: 100%">
                            <thead>
                                <tr>
                                    <th data-orderable="true">Clave</th>
                                    <th data-orderable="true">Descripción</th>
                                    <th data-orderable="true">Fecha inicial</th>
                                    <th data-orderable="true">Fecha final</th>
                                    <th data-orderable="true">Periodo anterior</th>
                                    <th data-orderable="true">Ciclo Escolar</th>
                                    <th data-orderable="true">Estatus</th>
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

    <form id="eliminarForm" action="/catalogos/eliminarPeriodo.action" method="POST" >
        <input type="hidden" name="periodo.id" id="id" value="" />
    </form>
</body>

<content tag="endScripts">
    <script type="text/javascript" src="/vendors/fancy-box/jquery.fancybox.min.js"></script>
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js" 
            data-init="true" data-ajax="/ajax/listadoPeriodoAjax.action" data-div="rowTable"
    data-function="despuesCargarTabla" data-button="botonAgregar"></script>
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>
        function despuesCargarTabla() {
            $('.fancybox').fancybox({
                afterClose: function () {
                    recargarTabla();
                }
            });
        }
        function recargarTabla() {
            generarTabla("rowTable", "/ajax/listadoPeriodoAjax.action", despuesCargarTabla, botonAgregar);
        }

        var botonAgregar = [{
                text: ' <a id="nueva-entidad" title="Nuevo periodo" href="/catalogos/formPeriodo.action" class="fancybox fancybox.iframe solo-lectura btn btn-primary pull-right" name="nuevo" value="Nuevo" ><i class="fa fa-plus-circle fa-lg"></i>&nbsp;&nbsp;Agregar periodo</a>',
                className: 'normal-button'
            }];

        function eliminar(id) {

            ModalGenerator.notificacion({
                "titulo": "¿Deseas continuar?",
                "cuerpo": "Esto borrará el registro seleccionado, ¿Está seguro de quererlo eliminar?",
                "funcionAceptar": function () {
                    $("#id").val(id);
                    $("#eliminarForm").submit();
                },
                "tipo": "ALERT"
            });
        }
    </script>
</content>