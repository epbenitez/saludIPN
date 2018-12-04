<%-- 
    Document   : Baja de becas
    Created on : 16-Diciembre-2016
    Author     : JLRM
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Baja de Becas</title>
    <link rel="stylesheet" type="text/css" href="/vendors/fancy-box/jquery.fancybox.min.css"/>
    <link rel="stylesheet" type="text/css" href="/vendors/select2/select2-old/select2.min.css"/>
    <link rel="stylesheet" type="text/css" href="/vendors/select2/select2-old/select2-bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="/vendors/pretty-checkbox/pretty.min.css"/>
    <style>
        /* RECUADROS */
        .rectangulo{
            display: table;
            height: 49px;
            text-align: center;
            color:white;
            line-height: 110%;
            border:2px solid white;
        }


        .rectangulo:hover{
            cursor: pointer;
            border:none;
        }

        .rectangulo span {
            display: table-cell;
            vertical-align: middle;
        }

        .leyendasProceso{
            margin-bottom: 20px;
            margin-top: -5px;
            color: #7B7B7B;
        }

        .leyendasProceso span{
            margin-left: 20px;
            font-size: 14pt;
        }

        /*  FILTROS */
        #filtroForm label{
            display: block;
        }

        #filtroForm  .form-group{
            margin-right: 13px;
        }

        /* MOSTRAR OCULTAR PROCESO */
        #ocultarProcesos{
            color: #8C4380;
            cursor: pointer;
            font-size: 14pt !important;
        }

        /* CIRCULOS */
        .abierto{color: #A2CB7C;}
        .cerrado{color: #EF3555;}
        .activo{color: #6BC1C5;}

        .activo-bg{
            background-color: #82BFBF !important;
        }

    </style>
</head> 

<content tag="tituloJSP">
    Baja de becas
</content>

<content tag="tituloDerecho">
    <h1 id="ocultarProcesos">Ocultar procesos</h1>
</content>

<body>
    <div class="row" id="rowProcesos">
        <div class="col-xs-12">
            <div class="main-box clearfix" >
                <div class="row">
                    <header class="main-box-header clearfix" style=" padding-bottom: 0px;">
                        <h4 class="col-lg-4">
                            <security:authorize ifAnyGranted="ROLE_ANALISTABECAS,ROLE_JEFEBECAS">
                                Selecciona la unidad académica
                            </security:authorize>
                            <security:authorize ifNotGranted="ROLE_ANALISTABECAS,ROLE_JEFEBECAS">
                                Procesos abiertos para <s:property value="UA.nombreSemiLargo"/>
                            </security:authorize>
                        </h4>
                        <security:authorize ifAnyGranted="ROLE_ANALISTABECAS,ROLE_JEFEBECAS">
                            <div class="col-lg-8 col-md-12">
                                <form>
                                    <div class="form-group">
                                        <s:select 
                                            id="unidadesAcademicas" 
                                            name="unidadesAcademicas" 
                                            list="ambiente.unidadAcademicaList" 
                                            listKey="id"
                                            listValue="nombreSemiLargo"
                                            headerKey=""
                                            headerValue="-- Selecciona una unidad académica --"
                                            cssClass="form-control"
                                            cssStyle=" width: 100%;"
                                            onchange="actualizarProcesos()"/>
                                    </div>
                                </form>
                            </div>
                        </security:authorize>
                    </header>
                    <div class="main-box-body clearfix">
                        <div class="row" id="listaProcesos" 
                             <security:authorize ifAnyGranted="ROLE_ANALISTABECAS,ROLE_JEFEBECAS">
                                 style="display:none" 
                             </security:authorize>
                             >
                        </div>
                        <br/>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="row" id="leyendaProcesoRow" style="display:none;">
        <div class="col-lg-12">
            <div class="leyendasProceso">
                <b>PROCESOS</b>
                <span class="abierto">&#9679;</span> Abiertos
                <span class="cerrado">&#9679;</span> Cerrados
                <span class="activo">&#9679;</span> Seleccionado
            </div>
        </div>
    </div>
    <!-- Termina row procesos -->

    <div class="row" id="errorMensaje" style="display: none;">
        <div class="col-lg-12">
            <div class="alert alert-warning">
                <i class="fa fa-warning fa-fw fa-lg"></i>
                <strong>Advertencia!</strong> <span id="textoErrorMensaje">texto</span>
            </div>
        </div>
    </div>

    <div class="row" id="filtro" style="display:none;">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <div class="main-box-body">
                    <form id="filtroForm" class="form-inline" role="form">
                        <div class="form-group col-lg-4 col-md-4 col-sm-4 col-xs-12">
                            <label for="becasPeriodo">Selecciona tipo de beca</label>
                            <s:select id="becasPeriodo"  
                                      cssClass="form-control"
                                      cssStyle="width:100%;"
                                      name="becasPeriodo"
                                      list="becasPeriodo" 
                                      listKey="tipoBeca.id" 
                                      data-toggle="tooltip"
                                      data-placement="top"
                                      required="true"
                                      title="Lista de tipos de beca"
                                      listValue="tipoBeca.nombre"/>
                        </div>
                        <div class="form-group">
                            <label for="boleta">Boleta del alumno</label>
                            <div class="input-group">
                                <span class="input-group-addon"><i class="fa fa-male"></i></span>
                                <input class="form-control" id="boleta" type="text"  placeholder="Boleta">
                            </div>
                        </div>
                        <div class="form-group">
                            <button type="button" id="aplicarFiltrosBtn" class="btn btn-primary pull-right" style="margin-top:20px;">
                                Aplicar filtros
                            </button>
                        </div>
                    </form>
                    <br/>
                </div>
            </div>
        </div>
    </div>

    <div class="row" id="panel" style="display:none;">
        <div class="col-lg-12 col-md-12 col-sm-12">
            <div class="main-box clearfix">
                <div class="tabs-wrapper profile-tabs">
                    <ul class="nav nav-tabs">
                        <li id="tabLiCandidatos" class="active"><a href="#tab-candidatos" data-toggle="tab">Candidatos a baja <span id="totalCandidatos"></span></a></li>
                        <li class=""><a href="#tab-asignados" data-toggle="tab">Con baja aplicada <span id="totalAsignados"></span></a></li>
                    </ul>

                    <div class="tab-content" style="padding:0px;">
                        <div class="tab-pane fade active in" id="tab-candidatos">
                            <div id="contenedorCandidatos">
                                <table id="tablaCandidatos" class="table table-striped table-hover dt-responsive" style="width: 100%">
                                    <thead>
                                        <tr>
                                            <th data-center="true">
                                                <div class="pretty outline-success smooth" >
                                                    <input type="checkbox" id="todosCandidatos"/> 
                                                    <label><i class="glyphicon glyphicon-ok"></i></label>
                                                </div>
                                            </th>
                                            <th data-orderable="true" data-default-order="true" data-default-dir="asc">BOLETA</th>
                                            <th data-orderable="true">NOMBRE</th>
                                            <th data-orderable="true">CARRERA</th>
                                            <th data-orderable="true">PERIODO</th>
                                            <th data-center="true" data-orderable="true">PROM.</th>
                                            <th data-center="true" data-orderable="true">SEM.</th>
                                            <th data-center="true">BECA</th>
                                            <th data-orderable="true">INSCRITO</th>
                                            <th data-orderable="true">REGULAR</th>
                                        </tr>
                                    </thead>
                                </table>    
                            </div>
                        </div>
                        <!-- Separación tabs -->
                        <div class="tab-pane fade" id="tab-asignados">
                            <div id="contenedorAsignados">
                                <table id="tablaAsignados" class="table table-striped table-hover dt-responsive" style="width: 100%">
                                    <thead>
                                        <tr>
                                            <th data-center="true">
                                                <div class="pretty outline-success smooth" >
                                                    <input type="checkbox" id="todosAsignado"/> 
                                                    <label><i class="glyphicon glyphicon-ok"></i></label>
                                                </div>
                                            </th>
                                            <th data-orderable="true" data-default-order="true" data-default-dir="asc">BOLETA</th>
                                            <th data-orderable="true">NOMBRE</th>
                                            <th data-orderable="true">CARRERA</th>
                                            <th data-orderable="true">PERIODO</th>
                                            <th data-center="true" data-orderable="true">PROM.</th>
                                            <th data-center="true" data-orderable="true">SEM.</th>
                                            <th data-center="true">BECA</th>
                                            <th data-orderable="true">INSCRITO</th>
                                            <th data-orderable="true">REGULAR</th>
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

<content tag="inlineScripts">
    <script>
        $("#listaProcesos").show(); //Quitar
        /*PROCESO SELECCIONADO*/
        var procSelectEstatus;
        var procSelectMovimientoId;
        var procSelectProcesoId;
        var procSelectActivo;
        var procSelectNombre;
        var procSelectInicio;
        var procSelectFin;
        var procUAId;
        var tablaModal;

        var botonesCandidatos;
        var botonesAsignados;
        var botonDescargarCSV = {
            extend: 'csv',
            text: 'Descargar excel',
            className: 'btn btn-primary',
            exportOptions: {
                columns: [1, 2, 3, 4, 5, 6, 7, 8, 9]
            },
            customize: function (data1, data2) {
                return "\uFEFF" + data1;
            },
            filename: "Candidatos"
        };

        var tipoBaja;
        var observaciones;
        var bandera = true;
        var bndr = true;

        $(document).ready(function () {
            $("#becasPeriodo").prepend("<option id='todasBecasSeleccionadas' value='undefined' selected='selected'>Mostrar todas las becas</select>");
            $("#unidadesAcademicas,#becasPeriodo").select2({language: "es"});
            actualizarProcesos();
            $("#ocultarProcesos").click(function () {
                toogleProcesos();
            });
            $("#aplicarFiltrosBtn").click(function () {
                actualizarTablas();
            });
            $("#todosCandidatos").change(function () {
                if (this.checked) {
                    $("#tablaCandidatos").find('input:checkbox:enabled').prop('checked', true);
                } else {
                    $("#tablaCandidatos").find('input:checkbox').prop('checked', false);
                }
            });
            $("#todosAsignado").change(function () {
                if (this.checked) {
                    $("#tablaAsignados").find('input:checkbox:enabled').prop('checked', true);
                } else {
                    $("#tablaAsignados").find('input:checkbox').prop('checked', false);
                }
            });

            bandera = true;
            bndr = true;
        });

        function toogleProcesos() {
            if ($('#rowProcesos').is(':visible')) {
                $("#ocultarProcesos").text("Mostrar procesos");
                $("#rowProcesos,#leyendaProcesoRow").hide();
                if (procSelectNombre === undefined) {
                    procSelectNombre = "Ninguno seleccionado";
                }
                var uatext = $("#unidadesAcademicas option:selected").text();
                if (uatext.length === 0) {
                    $("h1").first().text("<s:property value="UA.nombreCorto"/> - " + procSelectNombre);
                } else {
                    $("h1").first().text(uatext + " - " + procSelectNombre);
                }
            } else {
                $("#rowProcesos,#leyendaProcesoRow").show();
                $("#ocultarProcesos").text("Ocultar procesos");
                $("h1").first().text("Baja de becas");
            }
            $("#make-small-nav").click();
        }

        function actualizarProcesos() {
            procSelectEstatus = undefined;
            procSelectMovimientoId = undefined;
            procSelectProcesoId = undefined;
            procSelectActivo = undefined;
            procSelectNombre = undefined;
            procSelectInicio = undefined;
            procSelectFin = undefined;
            procUAId = undefined;
            $("#filtro,#errorMensaje").hide();
        <security:authorize ifAnyGranted="ROLE_ANALISTABECAS,ROLE_JEFEBECAS">
            procUAId = $("#unidadesAcademicas option:selected").val();
            if (procUAId === "") {
                return;
            }
            getBecasPeriodo(procUAId);
        </security:authorize>
            <security:authorize ifNotGranted="ROLE_ANALISTABECAS,ROLE_JEFEBECAS">
            procUAId = <s:property value="UA.id"/>;
        </security:authorize>

            $.ajax({
                type: 'POST',
                url: '/ajax/getBajasProcesoAjax.action',
                dataType: 'json',
                data: {unidadAcademica: procUAId},
                cache: false,
                success: function (aData) {
                    $("#panel").hide();
                    $("#filtro").hide();
                    if (aData.data.length !== 0) {
                        $("#listaProcesos").show().empty();
                        $.each(aData.data, function (i, item) {
                            $("#listaProcesos").append(item);
                        });
                        $("#leyendaProcesoRow").show();
                    } else {
                        $("#listaProcesos").hide();
                        ModalGenerator.notificacion({
                            "titulo": "Información",
                            "cuerpo": "No hay algún proceso en la Unidad Académica para el periodo en curso.",
                            "tipo": "WARNING"
                        });
                    }
                },
                error: function () {
                    $("#listaProcesos").hide();
                    ModalGenerator.notificacion({
                        "titulo": "Ocurrió un error",
                        "cuerpo": "Hubo un problema que impidió que se completara la operación.",
                        "tipo": "DANGER"
                    });
                }
            });
        }

        function activo(elemento) {
            //Extraemos valores
            procSelectEstatus = Number($(elemento).data("estatus"));
            procSelectMovimientoId = $(elemento).data("proceso");
            procSelectProcesoId = $(elemento).data("procesoid");
            procSelectActivo = Number($(elemento).data("activo"));
            procSelectNombre = $(elemento).data("nombre");
            procSelectInicio = $(elemento).data("inicio");
            procSelectFin = $(elemento).data("fin");
            if (procSelectActivo !== 1) {
                switch (procSelectEstatus) {
                    case 1:
                        $("#errorMensaje").show();
                        $("#textoErrorMensaje").html("El proceso ya no se encuentra activo y no ha sido validado por ningún usuario. Fecha de inicio: " + procSelectInicio + " Fecha de termino: " + procSelectFin);
                        break;
                    case 2:
                        $("#errorMensaje").show();
                        $("#textoErrorMensaje").html("El proceso ya no se encuentra activo, ha sido validado por el <b>responsable</b>. No se podrá realizar ninguna modificación en los otorgamientos. ");
                        break;
                    case 3:
                        $("#errorMensaje").show();
                        $("#textoErrorMensaje").html("El proceso ya no se encuentra activo, ha sido validado por el <b>responsable</b>, el <b>funcionario</b>. No se podrá realizar ninguna modificación en los otorgamientos. ");
                        break;
                    case 4:
                        $("#errorMensaje").show();
                        $("#textoErrorMensaje").html("El proceso ya no se encuentra activo, ha sido validado por el <b>responsable</b>, el <b>funcionario</b> y el <b>analista</b>. No se podrá realizar ninguna modificación en los otorgamientos. ");
                        break;
                    default:
                        $("#errorMensaje").show();
                        $("#textoErrorMensaje").html("No hay algún proceso en la Unidad Académica para el periodo en curso.");
                        break;
                }

            } else {
                $("#errorMensaje").hide();
            }

            //Interfaz
            $("#filtro").show();
            $("#listaProcesos div").removeClass("activo-bg");
            $(elemento).addClass("activo-bg");

            botonesCandidatos = [botonDescargarCSV];
            if (procSelectActivo === 1) {
                botonesCandidatos.push({
                    text: 'Aplicar seleccionados',
                    className: 'btn btn-primary solo-lectura',
                    action: function (e, dt, node, config) {
                        asignar();
                    }
                });
            }

            botonesAsignados = [botonDescargarCSV];
            if (procSelectActivo === 1) {
                botonesAsignados.push({
                    text: 'Revertir seleccionados',
                    className: 'btn btn-primary solo-lectura',
                    action: function (e, dt, node, config) {
                        revertir();
                    }
                });
            }
            actualizarTablas();
        }

        function asignar() {
            var postParams;
            var totalProcesar = 0;
            var oaid = "";
            $(this).attr('href', function () {
                var len = $('.chck:checked').length;
                $('#tablaCandidatos').find('.chck:checked').each(function (index, element) {
                    totalProcesar++;
                    oaid += $(this).attr('oaId');
                    if (index < len - 1) {
                        oaid += ",";
                    }
                });
                postParams = "oaIDs=" + oaid + "&prc=" + procSelectProcesoId + "&observaciones=" + observaciones + "&tipoBaja=" + tipoBaja + "&alta=true";
            });

            if (procSelectMovimientoId == 6 && oaid.length > 1 && bandera) {
                if (bndr) {
                    $.fancybox({
                        autoScale: true,
                        type: 'iframe',
                        href: '/becas/formBajaBecas.action',
                        afterClose: function () {
                            asignar();
                        }
                    });
                }
            } else {
                bandera = true;
                ModalGenerator.notificacion({
                    "titulo": "Procesando la petición",
                    "tipo": "INFO",
                    "sePuedeCerrar": false,
                    "cuerpo": "Se esta procesando tu solicitud <ol>"
                            + "<li>(" + totalProcesar + ") Baja(s)</li>"
                            + "</ol>"
                });

                $.ajax({
                    type: 'POST',
                    url: "/ajax/darBajaBecasBajasAjax.action",
                    data: postParams,
                    cache: false,
                    success: function (aData) {
                        ModalGenerator.cerrarModales();
                        var obj = jQuery.parseJSON(aData);
                        var json_obj = obj.data[0];
                        if (json_obj.hasOwnProperty("error")) {
                            ModalGenerator.notificacion({
                                "titulo": "Ocurrió un error",
                                "cuerpo": json_obj.error,
                                "tipo": "ALERT"
                            });
                        } else {
                            var cuerpo = "<div class='row'><div class='col-lg-6'><div class='alert alert-success'><i class='fa fa-check-circle fa-fw fa-lg'></i>"
                                    + "<strong>" + obj.data[0].bien + "</strong> Bajas realizadas"
                                    + "</div></div><div class='col-lg-6'><div class='alert alert-danger'><i class='fa fa-times-circle fa-fw fa-lg'></i>"
                                    + " <strong>" + obj.data[1].mal + "</strong> Bajas con error"
                                    + "</div></div></div>";

                            if (Number(obj.data[1].mal) === 0) {
                                cuerpo += "<p>El proceso de bajas concluyo sin errores, ¡Felicidades!</p>";
                            } else {
                                cuerpo += "<div id='contenedorResultado'><table id='tablaResultados' class='table table-striped table-hover dt-responsive' style='width: 100%'><thead><tr>"
                                        + "<th>Boleta</th><th>Alumno</th><th>Razón</th>"
                                        + "</tr></thead></table></div>";
                            }
                            ModalGenerator.notificacion({
                                "titulo": "Resultado de la operación",
                                "tipo": "PRIMARY",
                                "cuerpo": cuerpo,
                                "funcionCerrar": function () {
                                    actualizarTablas();
                                }
                            });
                            if (Number(obj.data[1].mal) !== 0) {
                                generarTabla("contenedorResultado", obj.data.slice(2, obj.data.length), null, false, null, 5);
                            }

                        }

                    },
                    error: function () {
                        ModalGenerator.notificacion({
                            "titulo": "Ocurrió un error",
                            "cuerpo": "No se pudo realizar la operación",
                            "tipo": "ALERT"
                        });
                    }
                });
            }
            bndr = true;
        }

        function revertir() {
            var postParams;
            var totalProcesar = 0;
            $(this).attr('href', function () {
                var oaid = "";
                var len = $('.chck:checked').length;
                $('#tablaAsignados').find(".chck:checked").each(function (index, element) {
                    totalProcesar++;
                    oaid += $(this).val();
                    if (index < len - 1) {
                        oaid += ",";
                    }
                });
                postParams = "oaIDs=" + oaid + "&prc=" + procSelectProcesoId + "&observaciones=" + "lol" + "&tipoBaja=4&alta=false";
            });

            ModalGenerator.notificacion({
                "titulo": "Procesando la petición",
                "tipo": "INFO",
                "sePuedeCerrar": false,
                "cuerpo": "Se esta procesando tu solicitud <ol>"
                        + "<li>(" + totalProcesar + ") Reversiones(s)</li>"
                        + "</ol>"
            });

            $.ajax({
                type: 'POST',
                url: "/ajax/darBajaBecasBajasAjax.action",
                data: postParams,
                cache: false,
                success: function (aData) {
                    ModalGenerator.cerrarModales();
                    var obj = jQuery.parseJSON(aData);
                    var json_obj = obj.data[0];
                    if (json_obj.hasOwnProperty("error")) {
                        ModalGenerator.notificacion({
                            "titulo": "Ocurrió un error",
                            "cuerpo": json_obj.error,
                            "tipo": "ALERT"
                        });
                    } else {

                        var cuerpo = "<div class='row'><div class='col-lg-6'><div class='alert alert-success'><i class='fa fa-check-circle fa-fw fa-lg'></i>"
                                + "<strong>" + obj.data[0].bien + "</strong> Reversiones realizados"
                                + "</div></div><div class='col-lg-6'><div class='alert alert-danger'><i class='fa fa-times-circle fa-fw fa-lg'></i>"
                                + " <strong>" + obj.data[1].mal + "</strong> Reversiones con error"
                                + "</div></div></div>";

                        if (Number(obj.data[1].mal) === 0) {
                            cuerpo += "<p>El proceso de otorgamiento concluyo sin errores, ¡Felicidades!</p>";
                        } else {
                            cuerpo += "<div id='contenedorResultado'><table id='tablaResultados' class='table table-striped table-hover dt-responsive' style='width: 100%'><thead><tr>"
                                    + "<th>Boleta</th><th>Alumno</th><th>Razón de no otorgamiento</th>"
                                    + "</tr></thead></table></div>";
                        }

                        ModalGenerator.notificacion({
                            "titulo": "Resultado de la operación",
                            "tipo": "PRIMARY",
                            "cuerpo": cuerpo,
                            "funcionCerrar": function () {
                                actualizarTablas();
                            }
                        });

                        if (Number(obj.data[1].mal) !== 0) {
                            generarTabla("contenedorResultado", obj.data.slice(2, obj.data.length), null, false, null, 5);
                        }

                    }

                },
                error: function () {
                    ModalGenerator.notificacion({
                        "titulo": "Ocurrió un error",
                        "cuerpo": "No se pudo realizar la operación",
                        "tipo": "ALERT"
                    });
                }
            });
        }

        function actualizarTablas() {
            var url;
            switch (procSelectMovimientoId) {
                case 6:
                    url = "/ajax/listadoBajasDiversasBecasBajasAjax.action";
                    break;
                case 5:
                    url = "/ajax/listadoBajasPasantiaBecasBajasAjax.action";
                    break;
                default:
                    url = "/ajax/listadoBajasIncumplimientoBecasBajasAjax.action";
            }
            //Variables de filtro
            var tipoBeca = $("#becasPeriodo").val();
            var boleta = $("#boleta").val();
            $("#panel").show();
            var urlCandidatos = url + "?alta=true&unidad=" + procUAId + "&boleta=" + boleta + "&tipoBeca=" + tipoBeca + "&procesoid=" + procSelectProcesoId;
            var urlAsignados = url + "?alta=false&unidad=" + procUAId + "&boleta=" + boleta + "&tipoBeca=" + tipoBeca + "&procesoid=" + procSelectProcesoId;

            generarTabla("contenedorCandidatos", urlCandidatos, despuesCargarCandidatos, true, botonesCandidatos);
            generarTabla("contenedorAsignados", urlAsignados, despuesCargarAsignados, true, botonesAsignados);
        }

        function despuesCargarAsignados(oSettings) {
            $('.fancybox').fancybox({
                autoSize: true,
                afterClose: function () {
                    actualizarTablas();
                }
            });
            var encontrados = oSettings.fnRecordsDisplay();
            $("#totalAsignados").text("(" + encontrados + ")");
            limpiarCheckBoxes();
        }

        function despuesCargarCandidatos(oSettings) {
            $('.fancybox').fancybox({
                autoSize: true,
                afterClose: function () {
                    actualizarTablas();
                }
            });
            var encontrados = oSettings.fnRecordsDisplay();
            $("#totalCandidatos").text("(" + encontrados + ")");
            limpiarCheckBoxes();
        }

        function limpiarCheckBoxes() {
            $("#todosCandidatos").prop("checked", false);
            $("#todosAsignado").prop("checked", false);
        }

        function setDatos(tb, ob) {
            tipoBaja = tb;
            observaciones = ob;
            bandera = false;
        }

        function setB() {
            bndr = false;
        }

        function getBecasPeriodo(procUAId) {
            $.ajax({
                xhr: function () {
                    var xhr = new window.XMLHttpRequest();

                    // Upload progress
                    xhr.upload.addEventListener("progress", function (evt) {
                        if (evt.lengthComputable) {
                            var percentComplete = evt.loaded / evt.total;
                            //Do something with upload progress
                            console.log(percentComplete);
                        }
                    }, false);

                    // Download progress
                    xhr.addEventListener("progress", function (evt) {
                        if (evt.lengthComputable) {
                            var percentComplete = evt.loaded / evt.total;
                            // Do something with download progress
                            console.log(percentComplete);
                        }
                    }, false);

                    return xhr;
                },
                type: 'POST',
                url: "/ajax/getBecasPorNivelAsignacionesAjax.action",
                dataType: 'json',
                data: {unidadAcademicaId: procUAId},
                cache: false,
                success: function (aData) {
                    $('#becasPeriodo').get(0).options.length = 0;
                    $('#becasPeriodo').get(0).options[0] = new Option("Mostrar todas las becas", "");
                    $.each(aData.data, function (i, item) {
                        $('#becasPeriodo').get(0).options[$('#becasPeriodo').get(0).options.length] = new Option(item[1], item[0]);
                    });
                },
                error: function () {
                    ModalGenerator.notificacion({
                        "titulo": "Error",
                        "cuerpo": "Hubo un problema que impidió que se completara la operación.",
                        "tipo": "ALERT",
                    });
                }
            });
            return false;
        }
    </script>
</content>

<content tag="endScripts">
    <script type="text/javascript" src="/vendors/fancy-box/jquery.fancybox.min.js"></script>
    <script type="text/javascript" src="/vendors/datatables/datatables.js" ></script>
    <script type="text/javascript" src="/resources/js/ModalGenerator.js" ></script>
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js" ></script>
    <script type="text/javascript" src="/vendors/select2/select2-old/select2.min.js"></script>
    <script type="text/javascript" src="/vendors/select2/select2-old/select2_locale_es.js"></script>
</content>
