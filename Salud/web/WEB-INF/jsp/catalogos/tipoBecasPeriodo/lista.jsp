<%-- 
    Document   : listado
    Created on : 27/10/2015, 02:05:19 PM
    Author     : Usre-05
    Redesign   : Gustavo Adolfo Alamillo Medina
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Tipo de beca por periodo</title>
    <link rel="stylesheet" type="text/css" href="/vendors/fancy-box/jquery.fancybox.min.css"/>
    <link rel="stylesheet" type="text/css" href="/vendors/select2/css/select2.min.css"/>
</head> 

<content tag="tituloJSP">
    Tipo de beca por periodo
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
                    <form class="form-horizontal"
                          data-bv-feedbackicons-valid="glyphicon glyphicon-ok"
                          data-bv-feedbackicons-invalid="glyphicon glyphicon-remove"
                          data-bv-feedbackicons-validating="glyphicon glyphicon-refresh">
                        <div class="form-group">
                            <label class="col-sm-2 control-label">
                                Periodo escolar
                            </label>
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
                        <table id="tablaTipoPeriodo" class="table table-striped table-hover dt-responsive" style="width: 100%">
                            <thead>
                                <tr>
                                    <th data-orderable="true">Nombre</th>
                                    <th data-orderable="true">Nivel</th>
                                    <th data-orderable="true">Periodo</th>
                                    <th data-orderable="true" data-center="true">Importe de la beca</th>
                                    <th data-center="true">Transferir</th>
                                    <th data-orderable="true" data-center="true">Estatus</th>
                                    <th data-center="true"></th>
                                    <th data-center="true"></th>
                                </tr>
                            </thead>
                        </table>                        
                    </div>
                </div>
            </div>
        </div>                
    </div>

    <form id="eliminarForm" action="/catalogos/eliminarTipoBecaPeriodo.action" method="POST" >
        <input type="hidden" name="tipoBecaPeriodo.id" id="id" value="" />
    </form>

    <form id="transferirForm" action="/catalogos/transferirTipoBecaPeriodo.action" method="POST" >
        <input type="hidden" name="transferencias" id="transferencias" value="," />
    </form>

</body>

<content tag="endScripts">
    <script type="text/javascript" src="/vendors/fancy-box/jquery.fancybox.min.js"></script>
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js" 
            data-init="true" 
            data-div="rowTabla"
            data-ajax="/ajax/listadoTipoBecaPeriodoAjax.action"
            data-function="despuesCargarTabla" 
            data-button="botonAgregar">
    </script>
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>
    <script type="text/javascript" src="/vendors/select2/js/select2.full.min.js"></script>
    <script type="text/javascript" src="/vendors/select2/js/i18n/es.js"></script>
</content>

<content tag="inlineScripts">
    <script>
                $("#periodoEscolar").select2({
                    language: "es"
                }).on("select2:select", function (e) {
                    var text = $('#periodoEscolar option:selected').text();
                    if (text === "-- Todos --") {
                        tablas["rowTabla"].column(2).search("").draw();
                    } else {
                        tablas["rowTabla"].column(2).search(text).draw();
                    }
                });

                function despuesCargarTabla() {
                    $('.fancybox').fancybox({
                        afterClose: function () {
                            recargarTabla();
                        }
                    });
                }

                function recargarTabla() {
                    generarTabla("rowTabla", "/ajax/listadoTipoBecaPeriodoAjax.action", despuesCargarTabla, botonAgregar);
                }

                var botonAgregar = [{
                        text: ' <a href="#" onclick="transferirSubmit()" class="btn btn-primary solo-lectura"  name="nuevo" value="Nuevo" >Transferir tipos de beca seleccionados</a>',
                        className: 'normal-button'
                    }, {
                        text: ' <a id="nueva-entidad" href="/catalogos/formTipoBecaPeriodo.action" class="fancybox fancybox.iframe solo-lectura btn btn-primary"  name="nuevo" value="Nuevo" ><i class="fa fa-plus-circle fa-lg"></i>&nbsp;&nbsp;Agregar tipo de beca</a>',
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

                function transferirSubmit() {
                    $("#transferirForm").submit();
                }

                function transferir(check, id) {
                    var current = $("#transferencias").val();
                    if (check) {
                        if (current.indexOf("," + id + ",") < 0) {
                            $("#transferencias").val(current + id + ",");
                        }
                    } else {
                        var porEliminar = "," + id + ",";
                        current = current.replace(porEliminar, ",");
                        $("#transferencias").val(current);
                    }
                }
    </script>
</content>