<%-- 
    Document   : Asignación de becas
    Created on : 14-Noviembre-2016
    Author     : Gustavo Adolfo Alamillo Medina
    Redesing   : Mario Márquez
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Asignación de becas</title>
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

        /* 
        con-popover: Todo elemento que sea de esta clase, será preparado 
        para mostrar un popover 
        */
        .con-popover {}

    </style>
</head> 

<content tag="tituloJSP">
    Asignación de becas
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
                                            list="ambiente.unidadAcademicaOrderedList" 
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
    </div> <!-- Termina row procesos -->

    <div class="row" id="leyendaProcesoRow" style="display:none;">
        <div class="col-xs-12">            
            <div class="row">
                <div class="col-sm-6">
                    <div class="leyendasProceso">
                        <b>PROCESOS</b>
                        <span class="abierto">&#9679;</span> Abiertos
                        <span class="cerrado">&#9679;</span> Cerrados
                        <span class="activo">&#9679;</span> Seleccionado
                    </div>
                </div>
            </div>                            
        </div>        
    </div>

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
                        <div class="form-group col-md-3">
                            <label for="becasPeriodo" class="sr-only">Tipo de Beca</label>
                            <s:select id="becasPeriodo"  
                                      cssClass="form-control"
                                      cssStyle="width:100%;"
                                      name="becasPeriodo"
                                      list="%{{}}"
                                      required="true"
                                      title="Lista de tipos de beca"/>
                            <p class="help-block">Selecciona tipo de beca</p>
                        </div>
                        <div class="form-group col-md-2">
                            <label for="documentacion" class="sr-only">Documentación completa o Incompleta</label>
                            <select name="documentacion" id="documentacion"
                                    class="form-control">
                                <option value="0">Todos</option>
                                <option value="1">Documentación completa</option>
                                <option value="2">Documentación incompleta</option>
                            </select>
                            <p class="help-block">¿Documentación completa?</p>
                        </div>
                        <div class="form-group col-md-2">
                            <label for="antecedente" class="sr-only">Antecedente de beca</label>
                            <select name="antecedente" id="antecedente"
                                    class="form-control">
                                <option value="0">Todos</option>
                                <option value="1">Si</option>
                                <option value="2">No</option>
                            </select>
                            <p class="help-block">¿Antecedente de beca?</p>
                        </div>
                        <div class="form-group col-md-2">
                            <label for="preasignado" class="sr-only">Preasignado</label>
                            <select name="preasignado" id="preasignado"
                                    class="form-control">
                                <option value="0">Todos</option>
                                <option value="1">Si</option>
                                <option value="2">No</option>
                            </select>
                            <p class="help-block">¿Preasignado?</p>
                        </div>  
                        <div class="form-group col-md-2">
                            <label for="boleta" class="sr-only">Boleta del alumno</label>
                            <div class="input-group">
                                <span class="input-group-addon"><i class="fa fa-male"></i></span>
                                <input class="form-control" id="boleta" type="text"  placeholder="Boleta">
                            </div>
                            <p class="help-block">Boleta del alumno</p>
                        </div>
                        <div class="form-group col-md-1">
                            <button type="button" id="aplicarFiltrosBtn" class="btn btn-primary">                                
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
                        <li id="tabLiCandidatos" class="active"><a href="#tab-candidatos" data-toggle="tab">Candidatos a becas <span id="totalCandidatos"></span></a></li>
                        <li id="tabLiAsignados" class=""><a href="#tab-asignados" data-toggle="tab">Con beca aplicada <span id="totalAsignados"></span></a></li>
                        <li id="tabLiRechazados" class=""><a href="#tab-rechazados" data-toggle="tab">Rechazados <span id="totalRechazados"></span></a></li>
                        <li id="tabLiEspera" class=""><a href="#tab-espera" data-toggle="tab">En lista de espera <span id="totalEspera"></span></a></li>
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
                                            <th data-center="true" data-orderable="true">BOLETA</th>
                                            <th data-orderable="true">NOMBRE</th>
                                            <th data-orderable="true">CARRERA</th>
                                            <th data-center="true" data-orderable="true">PROM.</th>
                                            <th data-center="true" data-orderable="true">SEM.</th>
                                            <th data-center="true" data-orderable="true">PROSPERA</th>
                                            <th data-center="true" data-orderable="true">CON HAMBRE</th>                                            
                                            <th data-center="true" data-orderable="true">MARGINACION</th>
                                            <th data-center="true" data-orderable="true"><img src="/resources/img/asignacionManual/d.png" width="25" height="25"/></th>
                                            <th data-center="true" data-orderable="true">ANTECEDENTE</th>
                                            <th data-orderable="true">SOLICITADA</th>
                                            <th data-orderable="true">PREASIGNADA</th>
                                            <th data-center="true"></th>
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
                                                    <input type="checkbox" id="todosAsignados"/> 
                                                    <label><i class="glyphicon glyphicon-ok"></i></label>
                                                </div>
                                            </th>
                                            <th data-center="true" data-orderable="true">BOLETA</th>
                                            <th data-orderable="true">NOMBRE</th>
                                            <th data-orderable="true">CARRERA</th>
                                            <th data-center="true" data-orderable="true">PROM.</th>
                                            <th data-center="true" data-orderable="true">SEM.</th>
                                            <th data-center="true" data-orderable="true">PROSPERA</th>
                                            <th data-center="true" data-orderable="true">CON HAMBRE</th>
                                            <th data-center="true" data-orderable="true">MARGINACION</th>
                                            <th data-center="true" data-orderable="true"><img src="/resources/img/asignacionManual/d.png" width="25" height="25"/></th>
                                            <th data-orderable="true">SOLICITADA</th>
                                            <th data-orderable="true">ASIGNADA</th>
                                            <th data-center="true"></th>
                                        </tr>
                                    </thead>
                                </table>   
                            </div>
                        </div>
                        <!-- Separación tabs -->
                        <div class="tab-pane fade" id="tab-rechazados">
                            <div id="contenedorRechazados">
                                <table id="tablaRechazados" class="table table-striped table-hover dt-responsive" style="width: 100%">
                                    <thead>
                                        <tr>
                                            <th data-center="true">
                                                <div class="pretty outline-success smooth" >
                                                    <input type="checkbox" id="todosRechazados"/> 
                                                    <label><i class="glyphicon glyphicon-ok"></i></label>
                                                </div>
                                            </th>
                                            <th data-center="true" data-orderable="true">BOLETA</th>
                                            <th data-orderable="true">NOMBRE</th>
                                            <th data-orderable="true">CARRERA</th>
                                            <th data-center="true" data-orderable="true">PROM.</th>
                                            <th data-center="true" data-orderable="true">SEM.</th>
                                            <th data-center="true" data-orderable="true">PROSPERA</th>
                                            <th data-center="true" data-orderable="true">CON HAMBRE</th>
                                            <th data-center="true" data-orderable="true">MARGINACION</th>
                                            <th data-center="true" data-orderable="true"><img src="/resources/img/asignacionManual/d.png" width="25" height="25"/></th>
                                            <th data-orderable="true">SOLICITADA</th>
                                            <th data-orderable="true">MOTIVO</th>
                                            <th data-center="true"></th>
                                        </tr>
                                    </thead>
                                </table>   
                            </div>
                        </div>
                        <!-- Separación tabs -->
                        <div class="tab-pane fade" id="tab-espera">
                            <div id="contenedorEspera">
                                <table id="tablaEspera" class="table table-striped table-hover dt-responsive" style="width: 100%">
                                    <thead>
                                        <tr>
                                            <th data-center="true">
                                                <div class="pretty outline-success smooth" >
                                                    <input type="checkbox" id="todosEspera"/> 
                                                    <label><i class="glyphicon glyphicon-ok"></i></label>
                                                </div>
                                            </th>
                                            <th data-center="true" data-orderable="true">BOLETA</th>
                                            <th data-orderable="true">NOMBRE</th>
                                            <th data-orderable="true">CARRERA</th>
                                            <th data-center="true" data-orderable="true">PROM.</th>
                                            <th data-center="true" data-orderable="true">SEM.</th>
                                            <th data-center="true" data-orderable="true">PROSPERA</th>
                                            <th data-center="true" data-orderable="true">CON HAMBRE</th>
                                            <th data-center="true" data-orderable="true">MARGINACION</th>
                                            <th data-center="true" data-orderable="true"><img src="/resources/img/asignacionManual/d.png" width="25" height="25"/></th>
                                            <th data-orderable="true">SOLICITADA</th>
                                            <th data-orderable="true">TURNO</th>
                                            <th data-center="true"></th>
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
    <script type="text/javascript" src="/vendors/fancy-box/jquery.fancybox.min.js"></script>
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js" ></script>
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js" ></script>
    <script type="text/javascript" src="/vendors/select2/select2-old/select2.min.js"></script>
    <script type="text/javascript" src="/vendors/select2/select2-old/select2_locale_es.js"></script>
</content>

<content tag="inlineScripts">
    <script>
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
        var motivoRechazo;
        var botonesAsignados;
        var datos;
        var botonDescargarCSVCandidatos = {
            extend: 'csv',
            text: 'Descargar excel',
            className: 'btn-primary con-popover',
            exportOptions: {
                columns: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]
            },
            customize: function (data1, data2) {
                return "\uFEFF" + data1;
            },
            filename: "Candidatos"
        };

        var botonDescargarCSVAsignados = {
            extend: 'csv',
            text: 'Descargar excel',
            className: 'btn-primary con-popover',
            exportOptions: {
                columns: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
            },
            customize: function (data1, data2) {
                return "\uFEFF" + data1;
            },
            filename: "Asignados"
        };

        var botonDescargarCSVRechazados = {
            extend: 'csv',
            text: 'Descargar excel',
            className: 'btn-primary con-popover',
            exportOptions: {
                columns: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10,11]
            },
            customize: function (data1, data2) {
                return "\uFEFF" + data1;
            },
            filename: "Rechazados"
        };

        var botonDescargarCSVEspera = {
            extend: 'csv',
            text: 'Descargar excel',
            className: 'btn-primary con-popover',
            exportOptions: {
                columns: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
            },
            customize: function (data1, data2) {
                return "\uFEFF" + data1;
            },
            filename: "Espera"
        };

        var botonRequisitos = {
            text: 'Requisitos',
            className: 'fancybox fancybox.iframe btn btn-default boton-requisitos con-popover'
        };

        var botonPresupuesto = {
            text: 'Presupuesto',
            className: 'fancybox fancybox.iframe btn btn-default boton-presupuesto con-popover'
        };

        $(document).ready(function () {
            $("body").removeClass("boxed-layout");
            $("#becasPeriodo").prepend("<option id='todasBecasSeleccionadas' value='undefined' selected='selected'>Mostrar todas las becas</select>");
            $("#unidadesAcademicas,#becasPeriodo").select2({language: "es"});
            actualizarProcesos();
            $("#ocultarProcesos").click(function () {
                toogleProcesos();
            });
            $("#aplicarFiltrosBtn").click(function () {
                actualizarTablas();
            });

            $("#tabLiCandidatos, #tabLiAsignados, #tabLiRechazados, #tabLiEspera").click(function () {
                actualizarFiltroBecas(this);
            });

            $("#todosCandidatos").change(function () {
                if (this.checked) {
                    $("#tablaCandidatos").find('.alumno:enabled').prop('checked', true);
                } else {
                    $("#tablaCandidatos").find('.alumno').prop('checked', false);
                }
            });
            $("#todosEspera").change(function () {
                if (this.checked) {
                    $("#tablaEspera").find('.alumno:enabled').prop('checked', true);
                } else {
                    $("#tablaEspera").find('.alumno').prop('checked', false);
                }
            });
            $("#todosRechazados").change(function () {
                if (this.checked) {
                    $("#tablaRechazados").find('.alumno:enabled').prop('checked', true);
                } else {
                    $("#tablaRechazados").find('.alumno').prop('checked', false);
                }
            });
            $("#todosAsignados").change(function () {
                if (this.checked) {
                    $("#tablaAsignados").find('.alumno:enabled').prop('checked', true);
                } else {
                    $("#tablaAsignados").find('.alumno').prop('checked', false);
                }
            });
            $('#contenedorCandidatos').on('draw.dt', function () {
                agregaEnlaces();
                agregaPopovers();
            });
        });

        function actualizarFiltroBecas(elemento) {
            var url;
            var data;
        <security:authorize ifAnyGranted="ROLE_ANALISTABECAS,ROLE_JEFEBECAS">
            procUAId = $("#unidadesAcademicas option:selected").val();
        </security:authorize>
        <security:authorize ifNotGranted="ROLE_ANALISTABECAS,ROLE_JEFEBECAS">
            procUAId = <s:property value="UA.id"/>;
        </security:authorize>
            if ($(elemento).attr("id") === "tabLiAsignados") {
                data = {unidadAcademica: procUAId};
                url = '/ajax/getBecasPorNivelTipoBecaPeriodoAjax.action';
            } else {
                data = 'undefined';
                url = '/ajax/getProgramasBecasAsignacionesBecaAjax.action';
            }

            $.ajax({
                type: 'POST',
                url: url,
                data: data,
                dataType: 'json',
                cache: false,
                success: function (aData) {
                    $('#becasPeriodo').get(0).options.length = 1; //Borra todos los elementos
                    $.each(aData.data, function (i, item) {
                        $('#becasPeriodo').get(0).options[$('#becasPeriodo').get(0).options.length] = new Option(item[1], item[0]);
                    });
                },
                error: function () {
                    ModalGenerator.notificacion({
                        "titulo": "Atención",
                        "cuerpo": "Hubo un problema que impidió que se completara la operación.",
                        "tipo": "DANGER"
                    });
                }
            });
        }

        function agregaEnlaces() {
            var rulPresupuesto = '/becas/presupuestoAsignaciones.action?uAJefe=';
            var rulRequisitos = '/becas/requisitosAsignaciones.action?uAJefe=';

        <security:authorize ifAnyGranted="ROLE_ANALISTABECAS,ROLE_JEFEBECAS">
            rulPresupuesto += $('#unidadesAcademicas').val();
            rulRequisitos += $('#unidadesAcademicas').val();
        </security:authorize>
            $('.boton-presupuesto').attr('href', rulPresupuesto);
            $('.boton-requisitos').attr('href', rulRequisitos);
            $('.boton-presupuesto').fancybox({
                autoSize: true
            });
            $('.boton-requisitos').fancybox({
                autoSize: true
            });
        }
        function agregaPopovers() {
            $('.boton-presupuesto img').attr('data-content', "Ver presupuesto");
            $('.boton-requisitos img').attr('data-content', "Ver requisitos \n\
                de las becas");

            $('.buttons-csv img').attr('data-content', "Descargar Excel");

            $('.con-popover img').attr('data-container', "body");
            $('.con-popover img').attr('data-toggle', "popover");
            $('.con-popover img').attr('data-placement', "top");

            $('[data-toggle="popover"]').popover({
                trigger: 'hover'
            });
        }

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
                $("h1").first().text("Asignación de becas");
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
            $("#filtro, #errorMensaje, #panel").hide();
        <security:authorize ifAnyGranted="ROLE_ANALISTABECAS,ROLE_JEFEBECAS">
            procUAId = $("#unidadesAcademicas option:selected").val();
            if (procUAId === "") {
                return;
            }
            actualizarFiltroBecas();
        </security:authorize>
        <security:authorize ifNotGranted="ROLE_ANALISTABECAS,ROLE_JEFEBECAS">
            procUAId = <s:property value="UA.id"/>;
            actualizarFiltroBecas();
        </security:authorize>
            $.ajax({
                type: 'POST',
                url: '/ajax/getProcesoAjax.action',
                dataType: 'json',
                data: {unidadAcademica: procUAId},
                cache: false,
                success: function (aData) {
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
                        $("#textoErrorMensaje").html("El proceso ya no se encuentra activo, ha sido validado por el <b>responsable</b>y el <b>funcionario</b>. No se podrá realizar ninguna modificación en los otorgamientos. ");
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

            botonesCandidatos = [];
            botonesAsignados = [];
            botonesRechazados = [];
            botonesEspera = [];

            // Si el proceso está activo, se agregan los botones
            // para aplicar o revertir asignaciones.
            if (procSelectActivo === 1) {
//              Si el movimiento es de tipo Validación (7) o Transferencia (1)
//               no se muestran pestañas de Lista de Espera ni de Rechazo
                if (procSelectMovimientoId === 7) {
                    botonesCandidatos.push({
                        text: 'Aplicar seleccionados',
                        className: 'btn btn-primary solo-lectura',
                        action: function (e, dt, node, config) {
                            asignar();
                        }
                    });
                    botonesAsignados.push({
                        text: 'Revertir seleccionados',
                        className: 'btn btn-primary solo-lectura',
                        action: function (e, dt, node, config) {
                            revertir();
                        }
                    });
                    $('#tabLiRechazados').hide();
                    $('#tabLiEspera').hide();
                } else if (procSelectMovimientoId === 1) {
                    botonesAsignados.push({
                        text: 'Revertir seleccionados',
                        className: 'btn btn-primary solo-lectura',
                        action: function (e, dt, node, config) {
                            revertir();
                        }
                    });
                    $('#tabLiRechazados').hide();
                    $('#tabLiEspera').hide();
                } else {
                    botonesCandidatos.push({
                        text: 'Aplicar seleccionados',
                        className: 'btn btn-primary solo-lectura',
                        action: function (e, dt, node, config) {
                            asignar();
                        }
                    },
                    {
                        text: 'Rechazar seleccionados',
                        className: 'btn btn-primary solo-lectura',
                        action: function (e, dt, node, config) {
                            rechazar();
                        }
                    },
                    {
                        text: 'Lista Espera seleccionados',
                        className: 'btn btn-primary solo-lectura',
                        action: function (e, dt, node, config) {
                            listaEspera();
                        }
                    });
                    botonesAsignados.push({
                        text: 'Revertir seleccionados',
                        className: 'btn btn-primary solo-lectura',
                        action: function (e, dt, node, config) {
                            revertir();
                        }
                    });
                    botonesRechazados.push({
                        text: 'Revertir seleccionados',
                        className: 'btn btn-primary solo-lectura',
                        action: function (e, dt, node, config) {
                            revertir();
                        }
                    });
                    botonesEspera.push({
                        text: 'Revertir seleccionados',
                        className: 'btn btn-primary solo-lectura',
                        action: function (e, dt, node, config) {
                            revertir();
                        }
                    });
                    $('#tabLiRechazados').show();
                    $('#tabLiEspera').show();
                }
            }

            botonesCandidatos.push(botonRequisitos, botonPresupuesto, botonDescargarCSVCandidatos);
            botonesAsignados.push(botonRequisitos, botonPresupuesto, botonDescargarCSVAsignados);
            botonesRechazados.push(botonRequisitos, botonPresupuesto, botonDescargarCSVRechazados);
            botonesEspera.push(botonRequisitos, botonPresupuesto, botonDescargarCSVEspera);
            actualizarTablas();
        }

        function rechazar() {
            var postParams;
            var totalProcesar = 0;
            $(this).attr('href', function () {
                var scid = "";
                var len = $('.alumno:checked').length;
                $("#tablaCandidatos").find(".alumno:checked").each(function (index, element) {
                    totalProcesar++;
                    scid += $(this).attr('scid');
                    if (index < len - 1) {
                        scid += ",";
                    }
                });
                $(this).prop('disabled', true);
                postParams = "proceso.id=" + procSelectProcesoId + "&solicitudes=" + scid;
            });
            var select = '<select id="motivoRechazoSelect" class="form-control" data-bv-notempty="true" data-bv-notempty-message="El motivo es requerido">'
                    +
        <s:iterator value="motivoRechazo" >
            '<option value = "' + '<s:property value="id"/>' + '">' + '<s:property value="nombre" />' + '</option>' +
        </s:iterator>
            '</select>';
            ModalGenerator.notificacion({
                "titulo": "Ingresa el motivo",
                "tipo": "PRIMARY",
                "tache": false,
                "funcionAfterCerrar": function () {
                    ModalGenerator.notificacion({
                        "titulo": "Procesando la petición",
                        "tipo": "INFO",
                        "sePuedeCerrar": false,
                        "cuerpo": "Se esta procesando tu solicitud <ol>"
                                + "<li>(" + totalProcesar + ") Rechazos(s)</li>"
                                + "</ol>"
                    });
                },
                "funcionAceptar": function () {
                    motivoRechazo = $("#motivoRechazoSelect").val();
                    $.ajax({
                        type: 'POST',
                        url: "/ajax/rechazarMasivoAsignacionesAjax.action",
                        data: postParams + "&motivoRechazo=" + motivoRechazo,
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
                                        + "<strong>" + obj.data[0].bien + "</strong> Rechazos realizados"
                                        + "</div></div><div class='col-lg-6'><div class='alert alert-danger'><i class='fa fa-times-circle fa-fw fa-lg'></i>"
                                        + " <strong>" + obj.data[1].mal + "</strong> Rechazos con error"
                                        + "</div></div></div>";

                                if (Number(obj.data[1].mal) === 0) {
                                    cuerpo += "<p>El proceso de rechazos concluyo sin errores, ¡Felicidades!</p>";
                                } else {
                                    cuerpo += "<div id='contenedorResultado'><table id='tablaResultados' class='table table-striped table-hover dt-responsive' style='width: 100%'><thead><tr>"
                                            + "<th>Boleta</th><th>Alumno</th><th>Razón de no otorgamiento</th>"
                                            + "</tr></thead></table></div>";
                                }

                                ModalGenerator.notificacion({
                                    "titulo": "Resultado de la operación",
                                    "tipo": "PRIMARY",
                                    "cuerpo": cuerpo,
                                    "tache": false,
                                    "funcionCerrar": function () {
                                        actualizarTablas();
                                    }
                                });

                                if (Number(obj.data[1].mal) !== 0) {
                                    datos = obj.data.slice(2, obj.data.length);
                                    generarTabla("contenedorResultado", "datos", null, false, null, 5);
                                }
                            }
                        }
                        ,
                        error: function () {
                            ModalGenerator.notificacion({
                                "titulo": "Ocurrió un error",
                                "cuerpo": "No se pudo realizar la operación",
                                "tipo": "ALERT"
                            });
                        }
                    });
                },
                "cuerpo": select
            });
        }

        function asignar() {
            var postParams;
            var totalProcesar = 0;
            $(this).attr('href', function () {
                var scid = "";
                $("#tablaCandidatos").find('.alumno:checked').each(function (index, element) {
                    var len = $('.alumno:checked').length;
                    totalProcesar++;
                    scid += $(this).attr('scid');
                    if (index < len - 1) {
                        scid += ",";
                    }
                });
                $(this).prop('disabled', true);
                postParams = "proceso.id=" + procSelectProcesoId + "&solicitudes=" + scid;
            });
            ModalGenerator.notificacion({
                "titulo": "Procesando la petición",
                "tipo": "INFO",
                "sePuedeCerrar": false,
                "cuerpo": "Se esta procesando tu solicitud <ol>"
                        + "<li>(" + totalProcesar + ") Otorgamiento(s)</li>"
                        + "</ol>"
            });
            $.ajax({
                type: 'POST',
                url: "/ajax/asignarMasivoAsignacionesAjax.action",
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
                                + "<strong>" + obj.data[0].bien + "</strong> Otorgamientos realizados"
                                + "</div></div><div class='col-lg-6'><div class='alert alert-danger'><i class='fa fa-times-circle fa-fw fa-lg'></i>"
                                + " <strong>" + obj.data[1].mal + "</strong> Otorgamientos con error"
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
                            "tache": false,
                            "funcionCerrar": function () {
                                actualizarTablas();
                            }
                        });

                        if (Number(obj.data[1].mal) !== 0) {
                            datos = obj.data.slice(2, obj.data.length);
                            generarTabla("contenedorResultado", "datos", null, false, null, 5);
                        }
                    }
                }
                ,
                error: function () {
                    ModalGenerator.notificacion({
                        "titulo": "Ocurrió un error",
                        "cuerpo": "No se pudo realizar la operación",
                        "tipo": "ALERT"
                    });
                }
            });
        }

        function listaEspera() {
            var postParams;
            var totalProcesar = 0;
            $(this).attr('href', function () {
                var scid = "";
                var len = $('.alumno:checked').length;
                $("#tablaCandidatos").find(".alumno:checked").each(function (index, element) {
                    totalProcesar++;
                    scid += $(this).attr('scid');
                    if (index < len - 1) {
                        scid += ",";
                    }
                });
                $(this).prop('disabled', true);
                postParams = "proceso.id=" + procSelectProcesoId + "&solicitudes=" + scid;
            });
            ModalGenerator.notificacion({
                "titulo": "Procesando la petición",
                "tipo": "INFO",
                "sePuedeCerrar": false,
                "cuerpo": "Se esta procesando tu solicitud <ol>"
                        + "<li>(" + totalProcesar + ") Envíos a lista de espera</li>"
                        + "</ol>"
            });
            $.ajax({
                type: 'POST',
                url: "/ajax/esperaMasivoAsignacionesAjax.action",
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
                                + "<strong>" + obj.data[0].bien + "</strong> Envíos a lista de espera realizados"
                                + "</div></div><div class='col-lg-6'><div class='alert alert-danger'><i class='fa fa-times-circle fa-fw fa-lg'></i>"
                                + " <strong>" + obj.data[1].mal + "</strong> Envíos a lista de espera con error"
                                + "</div></div></div>";

                        if (Number(obj.data[1].mal) === 0) {
                            cuerpo += "<p>El proceso de envíos a lista de espera concluyo sin errores, ¡Felicidades!</p>";
                        } else {
                            cuerpo += "<div id='contenedorResultado'><table id='tablaResultados' class='table table-striped table-hover dt-responsive' style='width: 100%'><thead><tr>"
                                    + "<th>Boleta</th><th>Alumno</th><th>Razón de no envío a lista de espera</th>"
                                    + "</tr></thead></table></div>";
                        }

                        ModalGenerator.notificacion({
                            "titulo": "Resultado de la operación",
                            "tipo": "PRIMARY",
                            "cuerpo": cuerpo,
                            "tache": false,
                            "funcionCerrar": function () {
                                actualizarTablas();
                            }
                        });

                        if (Number(obj.data[1].mal) !== 0) {
                            datos = obj.data.slice(2, obj.data.length);
                            generarTabla("contenedorResultado", "datos", null, false, null, 5);
                        }
                    }
                }
                ,
                error: function () {
                    ModalGenerator.notificacion({
                        "titulo": "Ocurrió un error",
                        "cuerpo": "No se pudo realizar la operación",
                        "tipo": "ALERT"
                    });
                }
            });
        }

        function revertir() {
            var tablaid = "";
            if ($("#tabLiAsignados").hasClass("active")) {
                tab = 1;
                tablaid = "#tablaAsignados";
            } else if ($("#tabLiRechazados").hasClass("active")) {
                tab = 2;
                tablaid = "#tablaRechazados";
            } else {
                tab = 3;
                tablaid = "#tablaEspera";
            }
            var postParams;
            var totalProcesar = 0;
            $(this).attr('href', function () {
                var alumnos = "";
                var scid = "";
                var len = $('.alumno:checked').length;
                $(tablaid).find(".alumno:checked").each(function (index, element) {
                    totalProcesar++;
                    alumnos += $(this).attr('value');
                    scid += $(this).attr('scid');
                    if (index < len - 1) {
                        alumnos += ",";
                        scid += ",";
                    }
                });
                postParams = "alumnos=" + alumnos + "&scIDs=" + scid + "&proceso.id=" + procSelectProcesoId + "&tab=" + Number(tab);
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
                url: "/ajax/revertirMasivoAsignacionesAjax.action",
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
                            "tipo": "ALERT",
                            "sePuedeCerrar": true
                        });
                    } else {

                        var cuerpo = "<div class='row'><div class='col-lg-6'><div class='alert alert-success'><i class='fa fa-check-circle fa-fw fa-lg'></i>"
                                + "<strong>" + obj.data[0].bien + "</strong> Reversiones realizados"
                                + "</div></div><div class='col-lg-6'><div class='alert alert-danger'><i class='fa fa-times-circle fa-fw fa-lg'></i>"
                                + " <strong>" + obj.data[1].mal + "</strong> Reversiones con error"
                                + "</div></div></div>";

                        if (Number(obj.data[1].mal) === 0) {
                            cuerpo += "<p>El proceso de reversión concluyo sin errores, ¡Felicidades!</p>";
                        } else {
                            cuerpo += "<div id='contenedorResultado'><table id='tablaResultados' class='table table-striped table-hover dt-responsive' style='width: 100%'><thead><tr>"
                                    + "<th>Boleta</th><th>Alumno</th><th>Razón de no reversión</th>"
                                    + "</tr></thead></table></div>";
                        }

                        ModalGenerator.notificacion({
                            "titulo": "Resultado de la operación",
                            "tipo": "PRIMARY",
                            "cuerpo": cuerpo,
                            "sePuedeCerrar": false,
                            "cerrarEnOverlay": false,
                            "botonContinuar": true,
                            "funcionCerrar": function () {
                                actualizarTablas();
                            }
                        });
                        if (Number(obj.data[1].mal) !== 0) {
                            datos = obj.data.slice(2, obj.data.length);
                            generarTabla("contenedorResultado", "datos", null, false, null, 5);
                        }

                    }
                },
                error: function () {
                    ModalGenerator.notificacion({
                        "titulo": "Ocurrió un error",
                        "cuerpo": "No se pudo realizar la operación",
                        "tipo": "ALERT",
                        "sePuedeCerrar": true
                    });
                }
            });
        }

        function addProceso(element) {
            $(element).attr('href', function () {
                if ($("#tabLiCandidatos").hasClass("active")) {
                    tab = 0;
                } else if ($("#tabLiAsignados").hasClass("active")) {
                    tab = 1;
                } else if ($("#tabLiRechazados").hasClass("active")) {
                    tab = 2;
                } else {
                    tab = 3;
                }
                if (this.href.indexOf("proceso.id") >= 0)
                    return this.href;
                else
                    return this.href + "&proceso.id=" + procSelectProcesoId + "&tab=" + Number(tab);
                //else 
                //  return this.href;
            });
        }

        function actualizarTablas() {
            //Variables de filtro
            var tipoBeca = $("#becasPeriodo").val();
            var documentacion = $("#documentacion").val();
            var antecedente = $("#antecedente").val();
            var preasignado = $("#preasignado").val();
            var boleta = $("#boleta").val();
            $("#panel").show();
            var urlCandidatos = '/ajax/listadoAsignacionesAjax.action?tipoproceso=' + procSelectMovimientoId
                    + '&proceso.id=' + procSelectProcesoId + '&tab=0&search_boleta='
                    + boleta + '&search_documentacion=' + documentacion + '&unidadAcademicaId='
                    + procUAId + '&tipoBeca=' + tipoBeca + '&activo=1' + '&antecedente=' + antecedente
                    + '&preasignado=' + preasignado;
            var urlAsignados = '/ajax/listadoAsignacionesAjax.action?tipoproceso=' + procSelectMovimientoId
                    + '&proceso.id=' + procSelectProcesoId + '&tab=1&search_boleta='
                    + boleta + '&search_documentacion=' + documentacion + '&unidadAcademicaId='
                    + procUAId + '&tipoBeca=' + tipoBeca + '&activo=1' + '&antecedente=' + antecedente
                    + '&preasignado=' + preasignado;
            var urlRechazados = '/ajax/listadoAsignacionesAjax.action?tipoproceso=' + procSelectMovimientoId
                    + '&proceso.id=' + procSelectProcesoId + '&tab=2&search_boleta='
                    + boleta + '&search_documentacion=' + documentacion + '&unidadAcademicaId='
                    + procUAId + '&tipoBeca=' + tipoBeca + '&activo=1' + '&antecedente=' + antecedente
                    + '&preasignado=' + preasignado;
            var urlEspera = '/ajax/listadoAsignacionesAjax.action?tipoproceso=' + procSelectMovimientoId
                    + '&proceso.id=' + procSelectProcesoId + '&tab=3&search_boleta='
                    + boleta + '&search_documentacion=' + documentacion + '&unidadAcademicaId='
                    + procUAId + '&tipoBeca=' + tipoBeca + '&activo=1' + '&antecedente=' + antecedente
                    + '&preasignado=' + preasignado;
            generarTabla("contenedorCandidatos", urlCandidatos, despuesCargarCandidatos, true, botonesCandidatos);
            generarTabla("contenedorAsignados", urlAsignados, despuesCargarAsignados, true, botonesAsignados);
            generarTabla("contenedorRechazados", urlRechazados, despuesCargarRechazados, true, botonesRechazados);
            generarTabla("contenedorEspera", urlEspera, despuesCargarEspera, true, botonesEspera);
        }

        function actualizarTodas() {
            var table = $('#tablaEspera').DataTable();
            table.draw(false);
            var table2 = $('#tablaRechazados').DataTable();
            table2.draw(false);
            var table3 = $('#tablaCandidatos').DataTable();
            table3.draw(false);
            var table4 = $('#tablaAsignados').DataTable();
            table4.draw(false);
        }

        function despuesCargarAsignados(oSettings) {
            $('.tab1').fancybox({
                autoSize: true,
                afterClose: function () {
                    // FIX 20/02/17 - Al cerrar un fancybox la tabla se
                    // actualiza pero ahora se mantiene en la página actual
                    actualizarTodas();
                }
            });
            var encontrados = oSettings._iRecordsDisplay;
            $("#totalAsignados").text("(" + encontrados + ")");
            limpiarCheckBoxes();
        }

        function despuesCargarCandidatos(oSettings) {
            $('.tab0').fancybox({
                autoSize: true,
                afterClose: function () {
                    // FIX 20/02/17 - Al cerrar un fancybox la tabla se
                    // actualiza pero ahora se mantiene en la página actual
                    actualizarTodas();
                }
            });
            var encontrados = oSettings._iRecordsDisplay;
            $("#totalCandidatos").text("(" + encontrados + ")");
            limpiarCheckBoxes();
        }

        function despuesCargarRechazados(oSettings) {
            $('.tab2').fancybox({
                autoSize: true,
                afterClose: function () {
                    // FIX 20/02/17 - Al cerrar un fancybox la tabla se
                    // actualiza pero ahora se mantiene en la página actual
                    actualizarTodas();
                }
            });
            var encontrados = oSettings._iRecordsDisplay;
            $("#totalRechazados").text("(" + encontrados + ")");
            limpiarCheckBoxes();
        }

        function despuesCargarEspera(oSettings) {
            $('.tab3').fancybox({
                autoSize: true,
                afterClose: function () {
                    // FIX 20/02/17 - Al cerrar un fancybox la tabla se
                    // actualiza pero ahora se mantiene en la página actual
                    actualizarTodas();
                }
            });
            var encontrados = oSettings._iRecordsDisplay;
            $("#totalEspera").text("(" + encontrados + ")");
            limpiarCheckBoxes();
        }

        function limpiarCheckBoxes() {
            $("#todosCandidatos").prop("checked", false);
            $("#todosAsignados").prop("checked", false);
            $("#todosRechazados").prop("checked", false);
            $("#todosEspera").prop("checked", false);
        }

        function getBecasPeriodo(procUAId) {
            $.ajax({
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
                        "tipo": "ALERT"
                    });
                }
            });
            return false;
        }
    </script>
</content>
