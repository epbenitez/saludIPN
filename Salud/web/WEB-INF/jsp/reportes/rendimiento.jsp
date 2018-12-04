<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Estadísticas: Proceso de Otorgamientos</title>
    <link rel="stylesheet" type="text/css" href="/vendors/select2/css/select2.min.css" />


    <style>
        /* bootstrap hack: soluciona el ancho para el contenido de tabs ocultas */
        .tab-content > .tab-pane:not(.active),
        .pill-content > .pill-pane:not(.active) {
            display: block;
            height: 0;
            overflow-y: hidden;
        } 
        /* Termina bootstrap hack */
    </style>
</head> 

<content tag="tituloJSP">
    Estadísticas: Proceso de Otorgamientos
</content>

<body>
    <form id="busquedaForm" action="/descargaReporteRendimiento.action">
    <input style="display: none" value="" name="limiteInferior" id="limit_inferior">
    <input style="display: none" value="" name="limiteSuperior" id="limit_superior">
    <div class="row">

        <div class="col-lg-2">
            <div class="main-box clearfix" >
                <div class="row">
                    <header class="main-box-header clearfix">
                        <h2 class="pull-left">Periodo</h2>
                    </header>
                    <div class="main-box-body clearfix">
                        <div class="row">
                            <div class="col-md-12">
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

        <div                     
            <s:if test="responsable">
                style="display:none;"
            </s:if>
            <s:else>
                class="col-lg-3"
            </s:else>
            >
            <div class="main-box clearfix" >
                <div class="row">
                    <header class="main-box-header clearfix">
                        <h2 class="pull-left">Nivel</h2>
                    </header>
                    <div class="main-box-body clearfix">
                        <div class="row">
                            <div class="col-md-12">
                                <div class="form-group">
                                    <div class="form-group">
                                        <select id="nivel" name="nivel" class="form-control">
                                            <s:if test="responsable">
                                                <option value="<s:property value="ua.nivel.id"/>"><s:property value="ua.nivel.nombre"/></option>
                                            </s:if>
                                            <s:else>                                                
                                                <option value="0">Todos los niveles</option>
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
        </div>

        <div                     
            <s:if test="responsable">
                style="display:none;"
            </s:if>
            <s:else>
                class="col-lg-4"
            </s:else>
            >
            <div class="main-box clearfix" >
                <div class="row">
                    <header class="main-box-header clearfix">
                        <h2 class="pull-left">Unidad Académica</h2>
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
                class="col-lg-10"
            </s:if>
            <s:else>
                class="col-lg-3"
            </s:else>
            >
            <div class="main-box clearfix" >
                <div class="row">
                    <header class="main-box-header clearfix">
                        <h2 class="pull-left">Programa de beca</h2>
                    </header>
                    <div class="main-box-body clearfix">
                        <div class="row">
                            <div class="col-md-12">
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
    </div> <!-- Termina filtro -->

    <div class="row">
            <div class="col-xs-12">
                <div class="btn-group pull-right">
                    <button id="downloadBTN" class="btn btn-primary" type="submit"><span class="fa fa-download"></span> Descargar reporte otorgamientos</button>
                    <button id="sendBTN" class="btn btn-primary" type="button"><span class="fa fa-pie-chart"></span> Generar gráficas</button>
                </div>            
            </div>

        </div> <!-- Termina boton enviar -->
    </form>
    <hr/>

    <div class="row" style="display: none;">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <div class="tabs-wrapper">
                    <ul class="nav nav-tabs">
                        <li class="active"><a href="#tab-otorgamientos" data-toggle="tab">Otorgamientos</a></li>
                        <li class=""><a href="#tab-bajas" data-toggle="tab">Bajas</a></li>
                    </ul>

                    <div class="tab-content">
                        <div class="tab-pane fade active in" id="tab-otorgamientos">                
                            <div class="row" style="display: none;" >
                                <!-- Grafica Otorgamientos Mujeres -->
                                <div class="col-lg-4" id="div-t-mujeres">
                                    <div class="main-box">
                                        <header class="main-box-header clearfix">
                                            <h2>Total Mujeres</h2>
                                        </header>
                                        <div class="main-box-body clearfix">
                                            <div class="center-block" style="width: 200px;">
                                                <div style="display: inline; width: 200px; height: 200px;">
                                                    <input class="dialMujeres" 
                                                           data-width="200" 
                                                           data-fgcolor="#E56385" 
                                                           data-angleArc=250
                                                           data-angleOffset=-125
                                                           data-readOnly="true"
                                                           style="width: 104px; height: 66px; position: absolute; vertical-align: middle; margin-top: 66px; margin-left: -152px; border: 0px; background: none; font-style: normal; font-variant: normal; font-weight: bold; font-stretch: normal; font-size: 40px; line-height: normal; font-family: Arial; text-align: center; color: rgb(3, 169, 244); padding: 0px; -webkit-appearance: none;">
                                                </div>
                                            </div>
                                        </div>
                                    </div> 
                                </div> <!-- Termina Grafica Otorgamientos Mujeres -->
                                <!-- Grafica Otorgamientos Hombres -->
                                <div class="col-lg-4" id="div-t-hombres">
                                    <div class="main-box">
                                        <header class="main-box-header clearfix">
                                            <h2>Total hombres</h2>
                                        </header>

                                        <div class="main-box-body clearfix">
                                            <div class="center-block" style="width: 200px;">
                                                <div style="display: inline; width: 200px; height: 200px;">
                                                    <input class="dialHombres" 
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
                                </div> <!-- Termina Grafica Otorgamientos Hombres -->
                                <!-- Grafica Otorgamientos Total -->
                                <div class="col-lg-4" id="div-t-otorgamientos">
                                    <div class="main-box">
                                        <header class="main-box-header clearfix">
                                            <h2>Total otorgamientos</h2>
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
                                </div> <!-- Termina Grafica Otorgamientos Total -->
                                <!-- Termina Grafica Genero -->

                                <!-- Grafica Pastel Otorgamientos -->
                                <div class="col-lg-4" id="div-pastel-otorgamientos">
                                    <div class="main-box">
                                        <header class="main-box-header clearfix" style="padding-bottom: 0;">
                                            <h2>Otorgamientos</h2>
                                        </header>

                                        <div class="main-box-body" style="padding: 0;">
                                            <div class="center-block" style="width: 200px;">
                                                <div id="dial-otorgamientos" style="width: 200px; height: 225px;"></div>                                            
                                            </div>
                                        </div>
                                    </div> <!-- Termina Grafica Pastel Otorgamientos -->
                                </div>
                                <!-- Grafica Pastel Otorgamientos Pendientes -->
                                <div class="col-lg-4" id="div-pastel-pendientes">
                                    <div class="main-box">
                                        <header class="main-box-header clearfix" style="padding-bottom: 0;">
                                            <h2>Otorgamientos Pendientes</h2>
                                        </header>

                                        <div class="main-box-body" style="padding: 0;">
                                            <div class="center-block" style="width: 200px;">
                                                <!--<div id="dial-bajas" class="project-box-content"></div>-->
                                                <div id="dial-bajas" style="width: 200px; height: 225px;">                            
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div> <!-- Termina Grafica Pastel Otorgamientos Pendientes -->
                                <!-- Grafica Otorgamientos Totales -->
                                <div class="col-lg-4" id="div-pastel-t">
                                    <div class="main-box">
                                        <header class="main-box-header clearfix">
                                            <h2>Total otorgamientos</h2>
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
                                </div> <!-- Grafica Otorgamientos Totales -->
                                <!-- Termina Grafica genero pendiente -->

                                <div class="col-lg-12">
                                    <div class="main-box clearfix project-box green-box">
                                        <div class="main-box-body clearfix">
                                            <div id="grafica-otorgamientos" class="project-box-content"></div>
                                            <br/>
                                            <div class="project-box-ultrafooter clearfix">
                                                <a href="#" class="link pull-right" id="linkOtorgamientos" style="color: #0081c1">
                                                    Visualizar información en forma de tabla <i class="fa fa-arrow-circle-right fa-lg"></i>
                                                </a>
                                            </div>
                                        </div>
                                    </div>
                                </div> <!-- Termina Grafica Otorgamientos -->
                            </div> <!-- Termina Otorgamientos -->
                        </div>
                        <div class="tab-pane fade" id="tab-bajas">                
                            <div class="row" style="display: none;" >
                                <div class="col-lg-4">
                                    <div class="main-box">
                                        <header class="main-box-header clearfix">
                                            <h2>Total Mujeres Bajas</h2>
                                        </header>
                                        <div class="main-box-body clearfix">
                                            <div class="center-block" style="width: 200px;">
                                                <div style="display: inline; width: 200px; height: 200px;">
                                                    <input class="dialMujeresB" 
                                                           data-width="200" 
                                                           data-fgcolor="#E56385" 
                                                           data-angleArc=250
                                                           data-angleOffset=-125
                                                           data-readOnly="true"
                                                           style="width: 104px; height: 66px; position: absolute; vertical-align: middle; margin-top: 66px; margin-left: -152px; border: 0px; background: none; font-style: normal; font-variant: normal; font-weight: bold; font-stretch: normal; font-size: 40px; line-height: normal; font-family: Arial; text-align: center; color: rgb(3, 169, 244); padding: 0px; -webkit-appearance: none;">
                                                </div>
                                            </div>
                                        </div>
                                    </div> 
                                </div> <!-- Termina Grafica Mujeres Bajas -->
                                <div class="col-lg-4">
                                    <div class="main-box">
                                        <header class="main-box-header clearfix">
                                            <h2>Total hombres Bajas</h2>
                                        </header>

                                        <div class="main-box-body clearfix">
                                            <div class="center-block" style="width: 200px;">
                                                <div style="display: inline; width: 200px; height: 200px;">
                                                    <input class="dialHombresB" 
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
                                </div> <!-- Termina Grafica Hombres Bajas -->
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

                                <div class="col-lg-12">
                                    <div class="main-box clearfix project-box green-box">
                                        <div class="main-box-body clearfix">
                                            <div id="grafica-bajas" class="project-box-content" style="width:auto !important;"></div>
                                            <br/>
                                            <div class="project-box-ultrafooter clearfix">
                                                <a href="#" class="link pull-right" id="linkBajas" style="color: #0081c1">
                                                    Visualizar información en forma de tabla <i class="fa fa-arrow-circle-right fa-lg"></i>
                                                </a>
                                            </div>
                                        </div>
                                    </div>
                                </div> <!-- Grafica bajas -->
                            </div> <!-- Termina Bajas -->
                        </div>            
                    </div>
                </div>

            </div>
        </div>
    </div> <!-- Terminan Gráficas -->            
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
        google.charts.load('visualization', '1.0', {
            'packages': ['controls']
        });

        var chartHeght = 350;
        var dataArray;
        var options;
        var dataG;

        $("#uas,#becas").select2({width: '100%'});

        $(".dialHombres, .dialMujeres,"
                + ".dialTotal, .dialHombresB,"
                + ".dialMujeresB, .dialTotalB").knob();

        <s:if test="!responsable">
        nivelOnChange();
        </s:if>

        $("#nivel").on("change", function () {
            nivelOnChange();
        });
        
        /*$("#downloadBTN").on("click", function (event) {
            var nivelId = $("select[name='nivel']").val();
            var unidadId = $('select[name=uas]').val();
            var periodoId = $('select[name=periodo]').val();
            var becaid = $('select[name=beca]').val();
            var urlReporte = "descargadocRendimiento.action";
            urlReporte = urlReporte + "?nivel=" + nivelId + "&uas=" + unidadId + "&periodo=" + periodoId + "&becaid=" + becaid;
            document.getElementById("downloadBTN").href = urlReporte;
        });*/

        $("#sendBTN").on("click", function (event) {
            var nivelId = $("select[name='nivel']").val();
            var unidadId = $('select[name=uas]').val();
            var programaBecaId = $('select[name=becas]').val();
            var periodoId = $('select[name=periodo]').val();

            if (nivelId === undefined || unidadId === undefined ||
                    programaBecaId === undefined || periodoId === undefined) {
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

                $.getJSON("/ajax/rendimientoReporte",
                        {"nivel.id": nivelId,
                            "unidadAcademica.id": unidadId,
                            "beca.id": programaBecaId,
                            "periodo.id": periodoId,
                            "movimiento.id": 0
                        }
                ).done(function (response) {
                    ModalGenerator.cerrarModales();
                    $("#sendBTN").blur();
                    document.getElementById("limit_inferior").value = "";
                    document.getElementById("limit_superior").value = "";
                    llenarGraficas(response.data[0]);
                });
            }
        });

        $("#linkOtorgamientos").click(function () {
            ModalGenerator.notificacion({
                titulo: "Otorgamientos por fechas",
                cuerpo: crearTablaGrafica(dataArray.otorgamientos),
                tipo: "SUCCESS"
            });
        });

        $("#linkBajas").click(function () {
            ModalGenerator.notificacion({
                titulo: "Bajas por fechas",
                cuerpo: crearTablaGrafica(dataArray.bajas),
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
            var table = "<table class='table table-striped table-hover'><thead><tr><th></th>";
            table += "<th class='text-center'>Hombres</th><th class='text-center'>Mujeres</th><th class='text-center'>Total</th></tr></thead><tbody>";
            $.each(array, function (index, element) {
                table += "<tr>";
                table += "<td>" + element.nombre + "</td>";
                table += "<td class='text-center'>" + element.totalHombre + "</td>";
                table += "<td class='text-center'>" + element.totalMujer + "</td>";
                table += "<td class='text-center'>" + element.total + "</td></tr>";
            });
            table += "</tbody></table>";
            return table;
        }

        function llenarGraficas(data) {
            dataArray = data;

            $(".row").show();

            var nPendientes;
            $.each(data.generoP, function (index, element) {
                nPendientes = element.total;
            });

            if (nPendientes <= 0) {

                toggleDivsPastel(false);

                //Gráfica de género
                $.each(data.genero, function (index, element) {
                    $('.dialMujeres').trigger('configure', {
                        "min": 0,
                        "max": element.total
                    }).val(element.totalMujer).trigger('change');

                    $('.dialHombres').trigger('configure', {
                        "min": 0,
                        "max": element.total
                    }).val(element.totalHombre).trigger('change');

                    $('.dialTotal').trigger('configure', {
                        "min": 0,
                        "max": element.total
                    }).val(element.total).trigger('change');

                });
            } else {
                toggleDivsPastel(true);

                //--- Gráfica Otorgamientos Pastel

                var dataGraph = data.genero;
                var titulo = "Género";
                var divPrincipal = 'dial-otorgamientos';
                var nElementos = dataGraph.length;
                var banderas = crearBanderas(nElementos);
                // Para las graficas tipo pastel se utiliza la funcion crearGraphPastel
                crearGraphPastel(dataGraph, titulo, divPrincipal,
                        banderas);

                //--- Gráfica Otorgamientos Pendientes Pastel

                var dataGraph = data.generoP;
                var titulo = "Pendientes";
                var divPrincipal = 'dial-bajas';
                var nElementos = dataGraph.length;
                var banderas = crearBanderas(nElementos);
                // Para las graficas tipo pastel se utiliza la funcion crearGraphPastel
                crearGraphPastel(dataGraph, titulo, divPrincipal,
                        banderas);

                //--- Gráfica Otorgamientos Total

                $.each(data.genero, function (index, element) {
                    $('.dialTotal').trigger('configure', {
                        "min": 0,
                        "max": element.total + nPendientes
                    }).val(element.total + nPendientes).trigger('change');
                });
            }

            //Gráfica de género baja
            $.each(data.generoB, function (index, element) {
                $('.dialMujeresB').trigger('configure', {
                    "min": 0,
                    "max": element.total
                }).val(element.totalMujer).trigger('change');

                $('.dialHombresB').trigger('configure', {
                    "min": 0,
                    "max": element.total
                }).val(element.totalHombre).trigger('change');

                $('.dialTotalB').trigger('configure', {
                    "min": 0,
                    "max": element.total
                }).val(element.total).trigger('change');

            });

            //--- Gráfica otorgamientos del periodo

            var dataGraph = data.otorgamientos;
            var titulo = "Otorgamientos por fechas";
            var divPrincipal = 'grafica-otorgamientos';
            var nElementos = dataGraph.length;
            var banderas = crearBanderas(nElementos);

            crearGraph(dataGraph, titulo, divPrincipal, banderas);

            //--- Gráfica bajas del periodo            

            var dataGraph = data.bajas;
            var titulo = "Bajas por fechas";
            var divPrincipal = 'grafica-bajas';
            var nElementos = dataGraph.length;
            var banderas = crearBanderas(nElementos);

            crearGraph(dataGraph, titulo, divPrincipal, banderas);
        }

        function toggleDivsPastel(option) {
            if (option) {
                $('#div-t-hombres').hide();
                $('#div-t-mujeres').hide();
                $('#div-t-otorgamientos').hide();

                $('#div-pastel-otorgamientos').show();
                $('#div-pastel-pendientes').show();
                $('#div-pastel-t').show();
            } else {
                $('#div-pastel-otorgamientos').hide();
                $('#div-pastel-pendientes').hide();
                $('#div-pastel-t').hide();

                //$('#div-t-hombres').show();
                //$('#div-t-mujeres').show();
                $('#div-t-otorgamientos').show();
            }
        }

        function crearGraphPastel(dataGraph, titulo, divPrincipal,
                banderas) {

            prepareGraphP(dataGraph);
            createOptions(titulo, banderas[1]);
            drawGraphP(divPrincipal);
        }

        function crearGraph(dataGraph, titulo, divPrincipal,
                banderas) {
            if (banderas[0]) {
                limpiarDiv(divPrincipal);
                prepareGraphRF(divPrincipal, dataGraph);
                createOptions(titulo, banderas[1]);
                drawGraphRF(divPrincipal);
            } else {
                limpiarDiv(divPrincipal);
                prepareGraph(dataGraph);
                createOptions(titulo, banderas[1]);
                drawGraph(divPrincipal);
            }
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
            if (nElementos > 10) {
                var flag1 = true;
                var flag2 = false;
            } else {
                var flag1 = false;
                var flag2 = (nElementos >= 10 && nElementos <= 20) ? true : false;
            }
            return [flag1, flag2];
        }

        // Gráfica junto con un filtro de rango
        // (para gráficas con más de 20 valores)
        function prepareGraphRF(divPrincipal, dataGraph) {

            // Agrega Div para el filtro y para la gráfica principal
            $('#' + divPrincipal).append(
                    "<div id='" + divPrincipal + "-c'></div>" +
                    "<div id='" + divPrincipal + "-rf'></div>"
                    );


            // Crea el header de la gráfica
            createHeader(true);
            // Agrega datos a las filas
            $.each(dataGraph, function (index, element) {
                addElement(element, true);
            });
        }

        function prepareGraph(dataGraph) {
            // Crea el header de la gráfica
            createHeader(false);
            // Agrega datos a las filas
            $.each(dataGraph, function (index, element) {
                addElement(element, false);
            });

        }

        function prepareGraphP(dataGraph) {
            // Crea el header de la gráfica
            createHeaderP(false);
            // Agrega datos a las filas
            $.each(dataGraph, function (index, element) {
                addElementP(element, false);
            });

        }

        // Gráfica junto con un filtro de rango
        // (para gráficas con más de 20 valores)
        function drawGraphRF(divPrincipal) {
            // Para agregar a la gráfica un filtro de rango, es necesario
            // crear un Dashboard, un ControlWrapper y un ChartWrapper.
            // Dentro del Dashboard se unirá la gráfica y el filtro. Se
            // especifica el div donde estará.
            // El ControlWrapper es el filtro para la gráfica. Por sí mismo
            // es una gráfica; es posible especificar opciones y es necesa-
            // rio especificar el div donde estará.
            // El ChartWrapper es la gráfica. Para este se crean opciones,
            // y se le asigna un div.
            var variable = new google.visualization.Dashboard(
                    document.getElementById(divPrincipal));

            var rangeSlider = new google.visualization.ControlWrapper({
                'controlType': 'ChartRangeFilter',
                'containerId': divPrincipal + "-rf",
                'options': {
                    filterColumnLabel: 'Movimiento',
                    ui: {
                        chartType: 'AreaChart',
                        chartOptions: options[1],
                        minRangeSize: 86400000,
                    },
                }
            });
            // Create a chart, passing some options
            var chart = new google.visualization.ChartWrapper({
                'chartType': 'ColumnChart',
                'containerId': divPrincipal + "-c",
                'options': options[0]
            });
            variable.bind(rangeSlider, chart);


            //Funcion que se ejecuta al momento de hacer un cambio en el slider
            google.visualization.events.addListener(rangeSlider, 'statechange', function (e) {
                    //Solo cuando se ha soltado el slider
                    if (!e.inProgress) {
                        var state = rangeSlider.getState();
                        var totalHombres=0, totalMujeres=0, total=0, flag=0, i;
                        var fechaInicial, fechaFinal;

                        //Recorre cada una de las filas de la tabla, agrega al total los valores individuales
                        // y sobreescribe la fecha final, con la fecha de cada fila
                        for (i = 0; i < dataG.getNumberOfRows(); i++) {
                            //se ejecuta si la fila esta dentro del intervalo seleccionado
                            if(state.range.start <= dataG.getValue(i, 0) && state.range.end >= dataG.getValue(i, 0)){
                                totalHombres += dataG.getValue(i, 1);
                                totalMujeres += dataG.getValue(i, 2);
                                total += dataG.getValue(i, 3);
                                
                                if (flag===0) {
                                    fechaInicial = dataG.getValue(i, 0);
                                    flag=1;
                                }
                                
                                fechaFinal = dataG.getValue(i, 0);             
                                
                            }

                        }
                        
                        //Actualizar los valores de las graficas
                        $('.dialMujeres').trigger('configure', {
                                "min": 0,
                                "max": total
                        }).val(totalMujeres).trigger('change');

                        $('.dialHombres').trigger('configure', {
                                "min": 0,
                                "max": total
                        }).val(totalHombres).trigger('change');

                        $('.dialTotal').trigger('configure', {
                                "min": 0,
                                "max": total
                        }).val(total).trigger('change');
                        
                        document.getElementById("limit_inferior").value = fechaInicial.getFullYear() + "-" + (fechaInicial.getMonth() + 1) + "-" + fechaInicial.getDate();
                        document.getElementById("limit_superior").value = fechaFinal.getFullYear() + "-" + (fechaFinal.getMonth() + 1) + "-" + fechaFinal.getDate();

                    }
            });
            
            
            variable.draw(dataG);
        }

        // Dibuja grafica
        function drawGraph(divPrincipal) {
            var variable = new google.visualization.ColumnChart(document.getElementById(divPrincipal));

            variable.draw(dataG, options[0]);
        }

        // Dibuja grafica
        function drawGraphP(divPrincipal) {
            var variable = new google.visualization.PieChart(document.getElementById(divPrincipal));

            variable.draw(dataG, options[2]);
        }

        // Si existe (ya fue creado antes), eliminar los contenidos del div
        function limpiarDiv(divPrincipal) {
            if (!$('#' + divPrincipal).is(':empty')) {
                $('#' + divPrincipal).empty();
            }
        }

        function createHeader(option) {
            dataG = new google.visualization.DataTable();

            // Si son más de 20 elementos, el tipo de la columna será 'date'
            // para que funcione correctamente con el filtro de rango
            if (option)
                dataG.addColumn('date', 'Movimiento');
            else
                dataG.addColumn('string', 'Movimiento');
            dataG.addColumn('number', 'Hombres');
            dataG.addColumn('number', 'Mujeres');
            dataG.addColumn('number', 'Total');
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

        // Esta función agrega los valores a la gráfica. En caso de que el total
        // sea mayor a 20, la fecha se guarda en una variable tipo "date" para
        // que funcione correctamente el filtro de rango.
        function addElement(element, option) {
            if (option) {
                var cadena = element.nombre;
                var day = cadena.substring(0, 2);
                var month = cadena.substring(3, 5);
                var year = cadena.substring(6, 10);
                var date = new Date(year, month - 1, day);
            } else {
                var date = element.nombre
            }

            dataG.addRow([
                date,
                element.totalHombre,
                element.totalMujer,
                element.total
            ]);
        }

        // Crea las opciones para la gráfica
        function createOptions(title, option) {
            var optionsC = {
                theme: 'material',
                chartArea: {width: '90%'},
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
                chartArea: {width: '90%', height: '80%'},
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
            options = [optionsC, optionsRF, optionsP];
        }

        function llenarTiposBeca() {
            //Tipos de beca
            $.get("/ajax/getBecasPorNivelTipoBecaAjax",
                    {nivel: $("#nivel option:selected").val()}
            ).done(function (response) {
                $("#becas").removeAttr("disabled").empty();
                var array = $.parseJSON(response);
                $("#becas").append("<option value='0'>Todos los tipos de beca</option>");
                $.each(array.data, function (index, value) {
                    $("#becas").append("<option value='" + value[0] + "'>" + value[1] + "</option>");
                });
            });
            $("#sendBTN").removeAttr("disabled");
        }

    </script>
    <style>
        .md-modal{position: absolute !important;}
    </style> 
</content>