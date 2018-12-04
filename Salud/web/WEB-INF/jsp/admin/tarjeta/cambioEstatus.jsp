<%-- 
    Document   : form
    Created on : 27/10/2015, 02:09:23 PM
    Author     : Victor Lozano
    Redesing   : Mario Márquez
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <link rel="stylesheet" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css" type="text/css" />    
    <link rel="stylesheet" type="text/css" href="/vendors/fancy-box/jquery.fancybox.min.css"/>
    <title>Estatus de tarjetas</title>
</head> 

<content tag="tituloJSP">
    Estatus de tarjetas
</content>

<body>

    <!-- Alertas -->
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
    </div> <!-- Terminan Alertas -->

    <div class="row">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <div class="main-box-body">
                    <form class="form-horizontal" id="busquedaForm"
                          data-bv-feedbackicons-valid="glyphicon glyphicon-ok"
                          data-bv-feedbackicons-invalid="glyphicon glyphicon-remove"
                          data-bv-feedbackicons-validating="glyphicon glyphicon-refresh">
                        <div class="form-group ">
                            <label class="col-sm-2 control-label">Identificador de carga</label>
                            <div class="col-sm-10">
                                <s:select 
                                    id="identificadores"
                                    name="identificadores"
                                    cssClass="form-control"
                                    list="identificadores"
                                    headerValue="Todos"
                                    headerKey=""
                                    />
                            </div>
                        </div>
                        <security:authorize ifAnyGranted="ROLE_ANALISTAADMINISTRATIVO,ROLE_JEFEBECAS">
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Unidad Académica</label>
                                <div class="col-sm-10">
                                    <s:select 
                                        id="unidadesAcademicas"  
                                        cssClass="form-control"
                                        name="unidadAcademicaId"
                                        list="unidadAcademicaList" 
                                        headerValue="Todos"
                                        headerKey=""
                                        listKey="id" 
                                        listValue="nombreCorto"
                                        />
                                </div>
                            </div>
                        </security:authorize>

                        <div class="form-group">
                            <label class="col-sm-2 control-label">Estatus tarjeta bancaria</label>
                            <div class="col-sm-10">
                                <s:select 
                                    id="estatus"  
                                    cssClass="form-control"
                                    name="estatus"
                                    list="estatus" 
                                    headerValue="Todos"
                                    headerKey=""
                                    listKey="id" 
                                    listValue="nombre"
                                    />
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-2 control-label">Tarjeta</label>
                            <div class="col-sm-10">
                                <s:textfield cssClass="form-control numeric"
                                             id="noTarjeta"
                                             name = "noTarjeta"
                                             placeholder="Tarjeta"/>
                            </div>                            
                        </div>
                        <div class=" form-group">
                            <div class="col-xs-12">
                                <button id="buscarButton" type="submit" class="btn btn-primary pull-right">Buscar tarjetas</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Ejemplo tabla -->
    <div class="row" id="div-tabla" style="display: none;">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <div class="col-md-12" style="padding: 0px">
                    <div class="responsive">                        
                        <table id="lista" class="table table-striped table-hover dt-responsive" style="width: 100%">
                            <thead>
                                <tr>
                                    <th><input type="checkbox" id="todos"/></th>
                                    <th data-orderable="true" data-default-order="true" data-default-dir="desc" >No. Tarjeta</th>
                                    <th data-orderable="true">Alumno</th>
                                    <th data-orderable="true">Estatus</th>
                                    <th>Estatus siguiente</th>
                                    <th>Observaciones</th>
                                </tr>
                            </thead>
                        </table>                        
                    </div>
                </div>
            </div>
        </div>                
    </div>
    <!--Termina ejemplo tabla --> 
    <form id="cambiarForm" action="/admin/cambiarEstatusTarjeta.action" method="POST" >
        <input type="hidden" name="cambiarEstatus" id="cambiarEstatus" value="," />
    </form>
</body>

<content tag="endScripts">
    <script type="text/javascript" src="/vendors/fancy-box/jquery.fancybox.min.js"></script>
    <script type="text/javascript" src="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.js"></script>
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js"></script>
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>
</content>

<content tag="inlineScripts">
    <script type="text/javascript" language="javascript" class="init">
        $(document).ready(function () {
            $('#busquedaForm').on('submit', function (e) {
                e.preventDefault();
                $('#buscarButton').blur();
                buscar();
            });

            $("#todos").change(function () {
                if (this.checked) {
                    $('.checkTarjeta:enabled').prop('checked', true);
                } else {
                    $('.checkTarjeta').prop('checked', false);
                }
            });

            $('.numeric').on('input', function (event) {
                this.value = this.value.replace(/[^0-9]/g, '');
            });

        });

        function buscar() {
            var url = "/ajax/listadoTarjetaAjax.action";
            var tar = $('#noTarjeta').val();
            var iden = $("#identificadores").val();
            url += "?tarjeta=" + tar + "&identificador=" + iden;

            if ($("#unidadesAcademicas").val() !== null && $("#unidadesAcademicas").val() !== "") {
                url += "&unidadAcademica=" + $("#unidadesAcademicas").val();
            }
            if ($("#estatus").val() !== null && $("#estatus").val() !== "") {
                url += "&estatus=" + $("#estatus").val();
            }

            generarTabla("div-tabla", url, tablaDibujada, true, botones);
        }

        var botones = [{
                text: '<a href="/resources/img/estatus_tarjetas/diagrama_estatus_tarjetas.png" class="fancybox fancybox.iframe btn btn-primary">Diagrama de Estatus de Tarjetas</a>'
            },
            {
                text: ' <a href="#" onclick="agregarValores()" class="btn btn-primary"  id="guardar"><i class="fa fa-plus-circle fa-lg"></i>&nbsp;&nbsp;Guardar</a>'
            }];

        function tablaDibujada() {
            $(".fancybox").fancybox({
                autoSize: true,
                afterClose: function () {
                    buscar();
                }
            });
            $('.checkTarjeta').each(function (index, element) {
                var id = $(element).attr("id");
                if ($("#" + id + "_1").is(':disabled')) {
                    $(element).prop("disabled", true);
                }
            });

        }

        function agregarValores() {
            var resultado = "";
            var map = {};
            $('.checkTarjeta:checked').each(function (index, element) {
                var id = $(element).attr("id");
                map[id] = $("#" + id + "_1").val() + ',' + $("#" + id + "_2").val();
            });
            for (var tarjeta in map) {
                resultado += tarjeta + ',' + map[tarjeta] + '|';
            }
            ModalGenerator.notificacion({
                "titulo": "Atención",
                "cuerpo": "¿Está seguro que desea cambiar el estatus de estos elementos?",
                "funcionAceptar": function () {
                    $("#cambiarEstatus").val(resultado);
                    $('#cambiarForm').submit();
                },
                "tipo": "WARNING"
            });
        }
    </script>
</content>