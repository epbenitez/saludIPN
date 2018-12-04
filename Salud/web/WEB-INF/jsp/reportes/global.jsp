<%-- 
    Document   : Reporte de género
    Created on : 29-Noviembre-2016
    Author     : Gustavo Adolfo Alamillo Medina
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Estadísticas: Otorgamientos/Bajas</title>
    <link rel="stylesheet" type="text/css" href="/vendors/select2/css/select2.min.css" />
    <script src = "https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <style>
        .tooltip-wrapper {
            display: inline-block; /* display: block works as well */
            margin: 50px; /* make some space so the tooltip is visible */
        }

        .tooltip-wrapper #but1 #but2[disabled] {
            /* don't let button block mouse events from reaching wrapper */
            pointer-events: none;
        }

        .tooltip-wrapper.disabled {
            /* OPTIONAL pointer-events setting above blocks cursor setting, so set it here */
            cursor: not-allowed;
        }

        <s:if test="responsable">
            #div-nivel, #div-ua {            
                display: none;                      
            }
        </s:if>

        /* bootstrap hack: soluciona el ancho para el contenido de tabs ocultas */
        .tab-content > .tab-pane:not(.active),
        .pill-content > .pill-pane:not(.active) {
            display: block;
            height: 0;
            overflow-y: hidden;
        } 

        .chart {
            width: 100%; 
            min-height: 450px;
        }
        /* Termina bootstrap hack */
    </style>
</head> 

<content tag="tituloJSP">
    Estadísticas: Otorgamientos/Bajas
</content>

<body>
    <form action="/descargaTotalBecasGlobal.action">
        <div class="row">
            <div                     
                <s:if test="responsable">
                    class="col-lg-4"
                </s:if>
                <s:else>
                    class="col-lg-2"
                </s:else>
                >
                <div class="main-box clearfix xs-12">
                    <div class="row">
                        <header class="main-box-header clearfix">
                            <h2 class="pull-left">Periodo</h2>
                        </header>
                        <div class="main-box-body clearfix">
                            <div class="row">
                                <div class="col-md-12 xs-12">
                                    <div class="form-group">
                                        <s:select
                                            cssClass="form-control"
                                            id="periodo"
                                            name="periodo"
                                            list="ambiente.periodoList" 
                                            listKey="id" 
                                            listValue="clave"
                                            />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div id="div-nivel" class="col-lg-2 xs-12">
                <div class="main-box clearfix xs-12" >
                    <div class="row">
                        <header class="main-box-header clearfix">
                            <h2 class="pull-left">Nivel</h2>
                        </header>
                        <div class="main-box-body clearfix">
                            <div class="row">
                                <div class="col-md-12">

                                    <div class="form-group">
                                        <select id="nivel" name="nivel" class="form-control">
                                            <s:if test="responsable">
                                                <option value="<s:property value="ua.nivel.id"/>"><s:property value="ua.nivel.nombre"/></option>
                                            </s:if>
                                            <s:else>                                                
                                                <option value="0">Todos</option>
                                                <option value="2">Nivel superior</option>
                                                <option value="1">Nivel medio superior</option>
                                            </s:else>
                                        </select>
                                    </div>

                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div id="div-ua" class="col-lg-2 xs-12 ">
                <div class="main-box clearfix xs-12" >
                    <div class="row">
                        <header class="main-box-header clearfix">
                            <h2 class="pull-left">U. Académica</h2>
                        </header>
                        <div class="main-box-body clearfix">
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">                                    
                                        <select id="uas" name="uas" class="form-control" disabled>
                                            <s:if test="responsable">
                                                <option value="<s:property value="ua.id"/>"><s:property value="ua.nombreSemiLargo"/></option>
                                            </s:if>
                                            <s:else>
                                                <option>Seleccione primero el nivel</option>
                                            </s:else>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div                     
                <s:if test="responsable">
                    class="col-lg-4"
                </s:if>
                <s:else>
                    class="col-lg-3"
                </s:else>
                >
                <div class="main-box clearfix xs-12">
                    <div class="row">
                        <header class="main-box-header clearfix">
                            <h2 class="pull-left">Programa de beca</h2>
                        </header>
                        <div class="main-box-body clearfix">
                            <div class="row">
                                <div class="col-md-12 xs-12">
                                    <div class="form-group">
                                        <s:select name="becas" id="becas"
                                                  list="ambiente.becaList" 
                                                  listKey="id" listValue="nombre"
                                                  cssClass="form-control"
                                                  headerKey="0"
                                                  headerValue="Todos los tipos de beca"/> 
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div                     
                <s:if test="responsable">
                    class="col-lg-4"
                </s:if>
                <s:else>
                    class="col-lg-3"
                </s:else>
                >
                <div class="main-box clearfix" >
                    <div class="row">
                        <header class="main-box-header clearfix">
                            <h2 class="pull-left">Movimiento</h2>
                        </header>
                        <div class="main-box-body clearfix">
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <s:select id="movimientos"  
                                                  cssClass="form-control"
                                                  name="movimientos"
                                                  list="ambiente.movimientoList" 
                                                  listKey="id" 
                                                  listValue="nombre" 
                                                  headerKey="0"
                                                  headerValue="Todos los movimientos"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div> <!-- Termina filtro -->

        <div class="row">
            <div class="col-xs-12">
                <input id="sendBTN" type="button" class="btn btn-primary pull-right" value="Obtener información"/>
            </div>
        </div> <!-- Termina boton enviar -->

        <hr/>
        <!------------------------------------------------------ Comienzan Gráficas desde Tabs ------------------------------------------------------------------------------------------------------>
        <div class="row" style="display: none;">
            <div class="col-xs-12">
                <div class="main-box clearfix">
                    <div class="tabs-wrapper">
                        <ul class="nav nav-tabs">
                            <li class="active"><a href="#tab-otorgamientos" data-toggle="tab">Otorgamientos</a></li>
                            <li class=""><a href="#tab-bajas" data-toggle="tab">Bajas</a></li>
                        </ul>

                        <div class="tab-content">

                            <!--Tab de Otorgamientos-->
                            <div class="tab-pane fade active in" id="tab-otorgamientos">                
                                <div class="row" style="display: none;" class="div-graph col-xs-12">


                                    <!-- Grafica Otorgamientos Total -->
                                    <div class="col-lg-4" id="div-t-otorgamientos">
                                        <div class="main-box">
                                            <header class="main-box-header clearfix">
                                                <h2>Total de becas</h2>
                                            </header>

                                            <div class="main-box-body clearfix">
                                                <div class="center-block" style="width: 200px;">
                                                    <div style="display: inline; width: 200px; height: 200px;">
                                                        <input class="dialTotal" 
                                                               data-width="200" 
                                                               data-fgcolor="#ED975D" 
                                                               data-angleArc=250
                                                               data-angleOffset=-125 
                                                               data-readOnly="true"
                                                               style="width: 104px; height: 66px; position: absolute; vertical-align: middle; margin-top: 66px; margin-left: -152px; border: 0px; background: none; font-style: normal; font-variant: normal; font-weight: bold; font-stretch: normal; font-size: 40px; line-height: normal; font-family: Arial; text-align: center; color: rgb(3, 169, 244); padding: 0px; -webkit-appearance: none;">
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div id="but1" class="tooltip-wrapper disabled">
                                            <button id="downloadBTNBecas" class="btn btn-primary pull-right" data-toggle="tooltip" type="submit"><span class="fa fa-download"></span> Descargar</button>
                                        </div>
                                    </div>
                                    <!-- Termina Grafica Otorgamientos Total -->

                                    <!-- Total de becarios -->
                                    <div class="col-lg-4" id="div-t-otorgamientos">
                                        <div class="main-box">
                                            <header class="main-box-header clearfix">
                                                <h2>Total becarios</h2>
                                            </header>

                                            <div class="main-box-body clearfix">
                                                <div class="center-block" style="width: 200px;">
                                                    <div style="display: inline; width: 200px; height: 200px;">
                                                        <input class="dialBecarios" 
                                                               data-width="200" 
                                                               data-fgcolor="#3BB7B1" 
                                                               data-angleArc=250
                                                               data-angleOffset=-125 
                                                               data-readOnly="true"
                                                               style="width: 104px; height: 66px; position: absolute; vertical-align: middle; margin-top: 66px; margin-left: -152px; border: 0px; background: none; font-style: normal; font-variant: normal; font-weight: bold; font-stretch: normal; font-size: 40px; line-height: normal; font-family: Arial; text-align: center; color: rgb(3, 169, 244); padding: 0px; -webkit-appearance: none;">
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div id="but2" class="tooltip-wrapper disabled">
                                            <button id="downloadBTNBecarios" class="btn btn-primary pull-right" type="submit" formaction="/descargaTotalBecariosGlobal.action"><span class="fa fa-download"></span> Descargar</button>
                                        </div>
                                    </div> 
                                    <!-- Termina gráfica total de becarios -->
                                </div>


                                <!--Gráfica por tipo de movimiento-->
                                <div class="row" style="display: none;">
                                    <div class="col-lg-12">
                                        <div class="main-box clearfix project-box green-box">
                                            <div class="main-box-body clearfix">
                                                <div id="grafica-movimiento" class="project-box-content"></div>
                                                <br/>
                                                <div class="project-box-ultrafooter clearfix">
                                                    <a href="#" class="link pull-right" id="linkMovimiento" style="color: #0081c1">
                                                        Visualizar información en forma de tabla <i class="fa fa-arrow-circle-right fa-lg"></i>
                                                    </a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <!--Gráfica por programa de beca-->
                                <div class="row"  style="display: none;">
                                    <div class="col-lg-12">
                                        <div class="main-box clearfix project-box green-box">
                                            <div class="main-box-body clearfix">
                                                <div id="grafica-programaBeca" class="project-box-content"></div>
                                                <br/>
                                                <div class="project-box-ultrafooter clearfix">
                                                    <a href="#" class="link pull-right" id="linkBeca" style="color: #0081c1">
                                                        Visualizar información en forma de tabla <i class="fa fa-arrow-circle-right fa-lg"></i>
                                                    </a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <!--Gráfica por semestres-->
                                <div class="row"  style="display: none;">
                                    <div class="col-lg-12">
                                        <div class="main-box clearfix project-box green-box">
                                            <div class="main-box-body clearfix">
                                                <div id="grafica-semestres" class="project-box-content"></div>
                                                <br/>
                                                <div class="project-box-ultrafooter clearfix">
                                                    <a href="#" class="link pull-right" id="linkSemestres" style="color: #0081c1">
                                                        Visualizar información en forma de tabla <i class="fa fa-arrow-circle-right fa-lg"></i>
                                                    </a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <!--Gráfica por promedios-->
                                <div class="row"  style="display: none;">
                                    <div class="col-lg-12">
                                        <div class="main-box clearfix project-box green-box">
                                            <div class="main-box-body clearfix">
                                                <div id="grafica-promedios" class="project-box-content"></div>
                                                <br/>
                                                <div class="project-box-ultrafooter clearfix">
                                                    <a href="#" class="link pull-right" id="linkPromedios" style="color: #0081c1">
                                                        Visualizar información en forma de tabla <i class="fa fa-arrow-circle-right fa-lg"></i>
                                                    </a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <!--Gráfica por carrera-->
                                <div class="row"  style="display: none;" id="graficaCarrera">
                                    <div class="col-lg-12">
                                        <div class="main-box clearfix project-box green-box">
                                            <div class="main-box-body clearfix">
                                                <div id="grafica-carreras" class="project-box-content"></div>
                                                <br/>
                                                <div class="project-box-ultrafooter clearfix">
                                                    <a href="#" class="link pull-right" id="linkCarreras" style="color: #0081c1">
                                                        Visualizar información en forma de tabla <i class="fa fa-arrow-circle-right fa-lg"></i>
                                                    </a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!--Tab de Bajas-->
                            <div class="tab-pane fade" id="tab-bajas">                
                                <div class="row" style="display: none;" class="div-graph">    

                                    <div class="col-lg-4">
                                        <div class="main-box">
                                            <header class="main-box-header clearfix">
                                                <h2>Total Bajas</h2>
                                            </header>

                                            <div class="main-box-body clearfix">
                                                <div class="center-block" style="width: 200px;">
                                                    <div style="display: inline; width: 200px; height: 200px;">
                                                        <input class="dialTotalB" 
                                                               data-width="200" 
                                                               data-fgcolor="#ED975D" 
                                                               data-angleArc=250
                                                               data-angleOffset=-125 
                                                               data-readOnly="true"
                                                               style="width: 104px; height: 66px; position: absolute; vertical-align: middle; margin-top: 66px; margin-left: -152px; border: 0px; background: none; font-style: normal; font-variant: normal; font-weight: bold; font-stretch: normal; font-size: 40px; line-height: normal; font-family: Arial; text-align: center; color: rgb(3, 169, 244); padding: 0px; -webkit-appearance: none;">
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div> <!--Termina Grafica Bajas Totales -->  
                                </div>


                                <!--Gráfica por tipo de movimiento-->
                                <div class="row"  style="display: none;">
                                    <div class="col-lg-12 xs-12">
                                        <div class="main-box clearfix project-box green-box xs-12">
                                            <div class="main-box-body clearfix xs-12">
                                                <div id="grafica-movimientoB" class="project-box-content xs-12"></div>
                                                <br/>
                                                <div class="project-box-ultrafooter clearfix">
                                                    <a href="#" class="link pull-right" id="linkMovimientoB" style="color: #0081c1">
                                                        Visualizar información en forma de tabla <i class="fa fa-arrow-circle-right fa-lg"></i>
                                                    </a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <!--Gráfica por programa de beca-->
                                <div class="row"  style="display: none;">
                                    <div class="col-lg-12">
                                        <div class="main-box clearfix project-box green-box">
                                            <div class="main-box-body clearfix">
                                                <div id="grafica-programaBecaB" class="project-box-content"></div>
                                                <br/>
                                                <div class="project-box-ultrafooter clearfix">
                                                    <a href="#" class="link pull-right" id="linkBecaB" style="color: #0081c1">
                                                        Visualizar información en forma de tabla <i class="fa fa-arrow-circle-right fa-lg"></i>
                                                    </a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>           

                                <!--Gráfica por semestre-->
                                <div class="row"  style="display: none;">
                                    <div class="col-lg-12">
                                        <div class="main-box clearfix project-box green-box">
                                            <div class="main-box-body clearfix">
                                                <div id="grafica-semestresB" class="project-box-content"></div>
                                                <br/>
                                                <div class="project-box-ultrafooter clearfix">
                                                    <a href="#" class="link pull-right" id="linkSemestresB" style="color: #0081c1">
                                                        Visualizar información en forma de tabla <i class="fa fa-arrow-circle-right fa-lg"></i>
                                                    </a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <!--Gráfica por promedio-->
                                <div class="row"  style="display: none;">
                                    <div class="col-lg-12">
                                        <div class="main-box clearfix project-box green-box">
                                            <div class="main-box-body clearfix">
                                                <div id="grafica-promediosB" class="project-box-content"></div>
                                                <br/>
                                                <div class="project-box-ultrafooter clearfix">
                                                    <a href="#" class="link pull-right" id="linkPromediosB" style="color: #0081c1">
                                                        Visualizar información en forma de tabla <i class="fa fa-arrow-circle-right fa-lg"></i>
                                                    </a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>  

                                <!--Gráfica por carrera-->
                                <div class="row"  style="display: none;" id="graficaCarreraB">
                                    <div class="col-lg-12">
                                        <div class="main-box clearfix project-box green-box">
                                            <div class="main-box-body clearfix">
                                                <div id="grafica-carrerasB" class="project-box-content"></div>
                                                <br/>
                                                <div class="project-box-ultrafooter clearfix">
                                                    <a href="#" class="link pull-right" id="linkCarrerasB" style="color: #0081c1">
                                                        Visualizar información en forma de tabla <i class="fa fa-arrow-circle-right fa-lg"></i>
                                                    </a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>            
                        </div>
                    </div>

                </div>
            </div>
        </div> <!-- Terminan Gráficas -->   
    </form>
</body>

<content tag="endScripts">
    <script type="text/javascript" src="/resources/js/loader.js"></script>
    <script type="text/javascript" src="/vendors/select2/js/select2.full.min.js"></script>
    <script type="text/javascript" src="/vendors/knob/jquery.knob.min.js" ></script>
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>

        google.charts.load('current', {packages: ['corechart', 'bar']});

        var graficaMovimiento = undefined;
        var graficaMovimientoB = undefined;
        var graficaProgramaBeca = undefined;
        var graficaProgramaBecaB = undefined;
        var graficaSemestres = undefined;
        var graficaSemestresB = undefined;
        var graficaPromedios = undefined;
        var graficaPromediosB = undefined;
        var graficaCarreras = undefined;
        var graficaCarrerasB = undefined;
        var graficaOtorgamientos = undefined;
        var graficaBajas = undefined;

        var chartHeght = 350;
        var dataArray;
        var options;
        var dataG;

        $("#uas,#becas,#movimientos").select2({width: '100%'});

        $(".dialTotal,\n\
            .dialTotalB").knob();

        $(".dialBecarios").knob();

        <s:if test="!responsable">
        nivelOnChange();
        </s:if>

        $("#nivel").on("change", function () {
            nivelOnChange();
        });

        $("#sendBTN").on("click", function (event) {
            var nivelId = $("select[name='nivel']").val();
            var unidadId = $('select[name=uas]').val();
            var programaBecaId = $('select[name=becas]').val();
            var periodoId = $('select[name=periodo]').val();
            var movimientoId = $('select[name=movimientos]').val();

            if (nivelId === undefined || unidadId === undefined ||
                    programaBecaId === undefined || periodoId === undefined || movimientoId === undefined) {
                ModalGenerator.notificacion({
                    "titulo": "¡Atención!",
                    "cuerpo": "Debes seleccionar al menos el nivel de estudios al que quieres sacar estadísticas.",
                    "tipo": "WARNING"
                });
            } else {
                ModalGenerator.notificacion({
                    "titulo": "Procesando tu solicitud",
                    "cuerpo": "Estamos realizando los cálculos necesarios para mostrarle la información, por favor sea paciente y no cierre ni actualice esta ventana.",
                    "tipo": "INFO",
                    "sePuedeCerrar": false
                });

                $.getJSON("/ajax/informacionReporte",
                        {"nivel.id": nivelId,
                            "unidadAcademica.id": unidadId,
                            "beca.id": programaBecaId,
                            "periodo.id": periodoId,
                            "movimiento.id": movimientoId
                        }
                ).done(function (response) {
                    ModalGenerator.cerrarModales();
                    $("#sendBTN").blur();
                    llenarGraficas(response.data[0]);
                    $(window).on('resize', llenarGraficas(response.data[0]));
                });
            }
        });

        $("#linkMovimiento").click(function () {
            ModalGenerator.notificacion({
                titulo: "Otorgamientos por movimiento",
                cuerpo: crearTablaGrafica(dataArray.movimientos),
                tipo: "SUCCESS"
            });
        });

        $("#linkMovimientoB").click(function () {
            ModalGenerator.notificacion({
                titulo: "Bajas por movimiento",
                cuerpo: crearTablaGrafica(dataArray.movimientosB),
                tipo: "SUCCESS"
            });
        });

        $("#linkBeca").click(function () {
            ModalGenerator.notificacion({
                titulo: "Otorgamientos por programa de beca",
                cuerpo: crearTablaGrafica(dataArray.programaBeca),
                tipo: "SUCCESS"
            });
        });

        $("#linkBecaB").click(function () {
            ModalGenerator.notificacion({
                titulo: "Bajas por programa de beca",
                cuerpo: crearTablaGrafica(dataArray.programaBecaB),
                tipo: "SUCCESS"
            });
        });

        $("#linkSemestres").click(function () {
            ModalGenerator.notificacion({
                titulo: "Otorgamientos por semestres",
                cuerpo: crearTablaGrafica(dataArray.semestres),
                tipo: "SUCCESS"
            });
        });

        $("#linkSemestresB").click(function () {
            ModalGenerator.notificacion({
                titulo: "Otorgamientos por semestres",
                cuerpo: crearTablaGrafica(dataArray.semestreBaja),
                tipo: "SUCCESS"
            });
        });

        $("#linkPromedios").click(function () {
            ModalGenerator.notificacion({
                titulo: "Otorgamientos por promedios",
                cuerpo: crearTablaGrafica(dataArray.promedios),
                tipo: "SUCCESS"
            });
        });

        $("#linkPromediosB").click(function () {
            ModalGenerator.notificacion({
                titulo: "Bajas según promedio",
                cuerpo: crearTablaGrafica(dataArray.promedioBaja),
                tipo: "SUCCESS"
            });
        });

        $("#linkCarreras").click(function () {
            ModalGenerator.notificacion({
                titulo: "Otorgamientos por carreras",
                cuerpo: crearTablaGrafica(dataArray.carreras),
                tipo: "SUCCESS"
            });
        });

        $("#linkCarrerasB").click(function () {
            ModalGenerator.notificacion({
                titulo: "Bajas según carreras",
                cuerpo: crearTablaGrafica(dataArray.carrerasB),
                tipo: "SUCCESS"
            });
        });

        function nivelOnChange() {
            if ($("select[name='nivel']").val() >= 0) {
                $.get("/ajax/getUnidadAcademicaAjax",
                        {pkNivel: $("#nivel option:selected").val(),
                            uACorto: true}
                ).done(function (response) {
                    $("#uas").removeAttr("disabled").empty();
                    var array = $.parseJSON(response);
                    $.each(array.data, function (index, value) {
                        $("#uas").append("<option value='" + value[0] + "'>" + value[1] + "</option>");
                    });
                });
            }
        }

        function crearTablaGrafica(array) {
            var totalhombres = 0;
            var totalmujeres = 0;
            var totaltotal = 0;
            var table = "<table class='table table-striped table-hover'><thead><tr><th></th>";
            table += "<th class='text-center'>Hombres</th><th class='text-center'>Mujeres</th><th class='text-center'>Total</th></tr></thead><tbody>";
            $.each(array, function (index, element) {
                table += "<tr>";
                table += "<td>" + element.nombre + "</td>";
                table += "<td class='text-center'>" + element.totalHombre + "</td>";
                totalhombres += element.totalHombre;
                table += "<td class='text-center'>" + element.totalMujer + "</td>";
                totalmujeres += element.totalMujer;
                table += "<td class='text-center'>" + element.total + "</td></tr>";
                totaltotal += element.total;
            });
            table += "<tr><td> Total </td>";
            table += "<td class='text-center'>" + totalhombres + "</td>";
            table += "<td class='text-center'>" + totalmujeres + "</td>";
            table += "<td class='text-center'>" + totaltotal + "</td></tr>";
            table += "</tbody></table>";
            return table;
        }

        //Gráfica movimientos
        function CrearGraficaMovimientos(data)
        {
            createHeader();
            $.each(data.movimientos, function (index, element) {
                addElement(element);
            });
            createOptions("Otorgamientos por tipo de movimiento");
            if (graficaMovimiento === undefined) {
                graficaMovimiento = new google.charts.Bar(document.getElementById('grafica-movimiento'));
            }
            graficaMovimiento.draw(dataG, google.charts.Bar.convertOptions(options[3]));
        }

        function llenarGraficas(data)
        {
            dataArray = data;

            $(".row").show();

            var countBeca;
            var countBecarios;


            var nPendientes;
            $.each(data.generoP, function (index, element) {
                nPendientes = element.total;
            });


            $('#div-t-hombres').show();
            $('#div-t-mujeres').show();
            $('#div-t-otorgamientos').show();


            //Gráfica de género
            $.each(data.genero, function (index, element) {
                $('.dialTotal').trigger('configure', {
                    "min": 0,
                    "max": element.total
                }).val(element.total).trigger('change');

                countBeca = element.total;

                $('.dialBecarios').trigger('configure', {
                    "min": 0,
                    "max": element.becarios
                }).val(element.becarios).trigger('change');

                countBecarios = element.becarios
            });


            if ((countBeca === 0) || (countBeca === null)) {
                document.getElementById("downloadBTNBecas").disabled = true;
                document.getElementById("but1").title = "Sin datos que descargar";
                $('#but1').tooltip({position: "bottom"});
            } else {
                document.getElementById("downloadBTNBecas").disabled = false;
                document.getElementById("but1").removeAttribute("title");
                document.getElementById("but1").removeAttribute("data-original-title");
            }
            if ((countBecarios === 0) || (countBecarios === null)) {
                document.getElementById("downloadBTNBecarios").disabled = true;
                document.getElementById("but2").title = "Sin datos que descargar";
                $('#but2').tooltip({position: "bottom"});
            } else {
                document.getElementById("downloadBTNBecarios").disabled = false;
                document.getElementById("but2").removeAttribute("title");
                document.getElementById("but2").removeAttribute("data-original-title");
            }

            //Gráfica de género baja
            $.each(data.generoB, function (index, element) {
                $('.dialTotalB').trigger('configure', {
                    "min": 0,
                    "max": element.total
                }).val(element.total).trigger('change');

            });

            CrearGraficaMovimientos(data);

            //Gráfica baja por movimiento
            createHeader();
            $.each(data.movimientosB, function (index, element) {
                addElement(element);
            });
            createOptions("Bajas por tipo de movimiento");
            if (graficaMovimientoB === undefined) {
                graficaMovimientoB = new google.charts.Bar(document.getElementById('grafica-movimientoB'));
            }
            graficaMovimientoB.draw(dataG, google.charts.Bar.convertOptions(options[3]));

            //Gráfica programa de beca
            createHeader();
            $.each(data.programaBeca, function (index, element) {
                addElement(element);
            });
            createOptions("Otorgamientos por programa de beca");
            if (graficaProgramaBeca === undefined) {
                graficaProgramaBeca = new google.charts.Bar(document.getElementById('grafica-programaBeca'));
            }
            graficaProgramaBeca.draw(dataG, google.charts.Bar.convertOptions(options[3]));

            //Gráfica bajas programa de beca
            createHeader();
            $.each(data.programaBecaB, function (index, element) {
                addElement(element);
            });
            createOptions("Bajas por programa de beca");
            if (graficaProgramaBecaB === undefined) {
                graficaProgramaBecaB = new google.charts.Bar(document.getElementById('grafica-programaBecaB'));
            }
            graficaProgramaBecaB.draw(dataG, google.charts.Bar.convertOptions(options[3]));

            //Gráfica semestres
            createHeader();
            $.each(data.semestres, function (index, element) {
                addElement(element);
            });
            createOptions("Otorgamientos por semestres");
            if (graficaSemestres === undefined) {
                graficaSemestres = new google.charts.Bar(document.getElementById('grafica-semestres'));
            }
            graficaSemestres.draw(dataG, google.charts.Bar.convertOptions(options[3]));

            //Gráfica bajas por semestre
            createHeader();
            $.each(data.semestreBaja, function (index, element) {
                addElement(element);
            });
            createOptions("Bajas por semestre");
            if (graficaSemestresB === undefined) {
                graficaSemestresB = new google.charts.Bar(document.getElementById('grafica-semestresB'));
            }
            graficaSemestresB.draw(dataG, google.charts.Bar.convertOptions(options[3]));

            //Gráfica promedios
            createHeader();
            $.each(data.promedios, function (index, element) {
                addElement(element);
            });
            createOptions("Otorgamientos por promedios");
            if (graficaPromedios === undefined) {
                graficaPromedios = new google.charts.Bar(document.getElementById('grafica-promedios'));
            }
            graficaPromedios.draw(dataG, google.charts.Bar.convertOptions(options[3]));

            //Gráfica promedios Baja
            createHeader();
            $.each(data.promedioBaja, function (index, element) {
                addElement(element);
            });
            createOptions("Bajas según promedio");
            if (graficaPromediosB === undefined) {
                graficaPromediosB = new google.charts.Bar(document.getElementById('grafica-promediosB'));
            }
            graficaPromediosB.draw(dataG, google.charts.Bar.convertOptions(options[3]));

            //Gráfica carreras
            if (Number($('select[name=uas] option:selected').val()) === 0) {
                $("#graficaCarrera").hide();
            } else {
                $("#graficaCarrera").show();
                createHeader();
                $.each(data.carreras, function (index, element) {
                    addElement(element);
                });
                createOptions("Otorgamientos por carreras");
                if (graficaCarreras === undefined) {
                    graficaCarreras = new google.charts.Bar(document.getElementById('grafica-carreras'));
                }
                graficaCarreras.draw(dataG, google.charts.Bar.convertOptions(options[3]));
            }

            //Gráfica bajas según carrera
            if (Number($('select[name=uas] option:selected').val()) === 0) {
                $("#graficaCarreraB").hide();
            } else {
                $("#graficaCarreraB").show();
                createHeader();
                $.each(data.carrerasB, function (index, element) {
                    addElement(element);
                });
                createOptions("Bajas por carrera");
                if (graficaCarrerasB === undefined) {
                    graficaCarrerasB = new google.charts.Bar(document.getElementById('grafica-carrerasB'));
                }
                graficaCarrerasB.draw(dataG, google.charts.Bar.convertOptions(options[3]));
            }
        }

        function crearGraphPastel(dataGraph, titulo, divPrincipal,
                banderas) {

            prepareGraphP(dataGraph);
            createOptions(titulo, banderas[1]);
            drawGraphP(divPrincipal);
        }

        function prepareGraphP(dataGraph) {
            // Crea el header de la gráfica
            createHeaderP(false);
            // Agrega datos a las filas
            $.each(dataGraph, function (index, element) {
                addElementP(element, false);
            });

        }

        function createHeaderP(option) {
            dataG = new google.visualization.DataTable();

            dataG.addColumn('string', 'Porción');
            dataG.addColumn('number', 'Cantidad');
        }

        function addElementP(element, option) {
            dataG.addRow([
                "Hombres",
                element.totalHombre,
            ]);
            dataG.addRow([
                "Mujeres",
                element.totalMujer,
            ]);
        }

        // Dibuja grafica
        function drawGraphP(divPrincipal) {
            var variable = new google.visualization.PieChart(document.getElementById(divPrincipal));

            variable.draw(dataG, options[2]);
        }

        // Crea las opciones para la gráfica
        function createOptions(title, option) {
            var optionsM =
                    {
                        chart:
                                {
                                    title: title + " (" + $('select[name=periodo] option:selected').text() + ")",
                                    subtitle: $('select[name=uas] option:selected').text() + " - " + $('select[name=becas] option:selected').text() + " - " + $('select[name=movimientos] option:selected').text()
                                },
                        axes:
                                {
                                    x:
                                            {
                                                0: {side: 'bottom', label: ''}
                                            }
                                },
                        vAxis: {format: 'decimal'},
                        height: chartHeght,
                        chartArea: {width: '100%'},
                        colors: ['#3BB7B1', '#E56385', '#ED975D']
                    };

            var optionsC = {
                theme: 'material',
                chartArea: {width: '100%'},
                colors: ['#3BB7B1', '#E56385', '#ED975D'],
                hAxis: {showTextEvery: 1, type: 'category'},
                vAxis: {viewWindowMode: 'maximized'},
                legend: {position: 'top'},
                height: chartHeght,
                title: title + " (" + $('select[name=periodo] option:selected').text() + ")",
            };
            // En caso de ser más de 10 elementos pero menos de 20, las 
            // etiquetas se dibujan inclinadas
            if (option) {
                optionsC['hAxis']['slantedText'] = true;
            }
            var optionsRF = {
                theme: 'material',
                height: 70,
                colors: ['#3BB7B1', '#E56385', '#ED975D'],
            };
            var optionsP = {
                chartArea: {width: '100%', height: '80%'},
                pieSliceText: 'value',
                pieHole: 0.5,
                tooltip: {
                    textStyle: {
                        fontName: 'Roboto',
                        fontSize: 12,
                    }
                },
                legend: {position: 'bottom'},
                colors: ['#3BB7B1', '#E56385', '#ED975D'],
            };
            options = [optionsC, optionsRF, optionsP, optionsM];
        }

        // nElementos contiene el número de valores para
        // el eje X. Se utiliza como bandera para dibujar las etiquetas 
        // en diagonal, u horizontalmente. También, para guardar la etiqueta
        // en una variable tipo 'String' o 'Date'.
        // Si son más de 10, se dibujan en daigonal. Si son menos de 10
        // o más de 20, se dibujan horizontalmente pero son variables tipo
        // 'Date' (con tal de que se visualice correctamente en la gráfica
        // de zoom)            
        function crearBanderas(nElementos) {
            if (nElementos > 20) {
                var flag1 = true;
                var flag2 = false;
            } else {
                var flag1 = false;
                var flag2 = (nElementos >= 10 && nElementos <= 20) ? true : false;
            }
            return [flag1, flag2];
        }

        function createHeader() {
            dataG = new google.visualization.DataTable();
            dataG.addColumn('string', 'Movimiento');
            dataG.addColumn('number', 'Hombres');
            dataG.addColumn('number', 'Mujeres');
            dataG.addColumn('number', 'Total');
        }

        function addElement(element) {
            dataG.addRow([
                element.nombre,
                element.totalHombre,
                element.totalMujer,
                element.total
            ]);
        }

        $(window).trigger('resize');


    </script>
    <style>
        .md-modal{position: absolute !important;}
    </style> 
</content>