<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Estadísticas: Solicitudes de Beca</title>
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
    Estadísticas: Solicitudes de Beca
</content>

<body>
    <form id="busquedaForm" action="/descargaEstadisticaSolicitudes.action">
        <input style="display: none" value="" name="limiteInferior" id="limit_inferior">
        <input style="display: none" value="" name="limiteSuperior" id="limit_superior">
        <div class="row">

            <div class="col-lg-3">
                <div class="main-box clearfix" >
                    <div class="row">
                        <header class="main-box-header clearfix">
                            <h2 class="pull-left">Periodo</h2>
                        </header>
                        <div class="main-box-body clearfix">
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <s:select id="periodo"
                                                  cssClass="form-control"
                                                  name="periodo" 
                                                  list="periodos" 
                                                  listKey="id" 
                                                  listValue="clave" />
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
                    class="col-lg-5"
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
                                                <option value="0">Seleccione primero el nivel</option>
                                            </s:else>
                                        </select>
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
                    <button id="downloadBTN" class="btn btn-primary" type="submit"><span class="fa fa-download"></span> Descargar reporte</button>
                    <button id="sendBTN" class="btn btn-primary" type="button"><span class="fa fa-pie-chart"></span> Generar gráficas</button>
                </div>            
            </div>

        </div> <!-- Termina boton enviar -->
    </form>
    <hr/>

    <div class="row" style="display: none;">
        <div class="col-xs-12">
            <div class="row" style="display: none;" >

                <!-- Grafica Solicitudes Total -->
                <div class="col-lg-4" id="div-t-otorgamientos">
                    <div class="main-box">
                        <header class="main-box-header clearfix">
                            <h2>Total Solicitudes</h2>
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
                </div>

                <!--Gráfica de Total de Solicitudes de Hombre y Mujer-->
                <div class="col-lg-12">
                    <div class="main-box clearfix project-box green-box">
                        <div class="main-box-body clearfix">
                            <div id="grafica-solicitudes" class="project-box-content"></div>
                            <br/>
                            <div class="project-box-ultrafooter clearfix">
                                <a href="#" class="link pull-right" id="linkSolicitudes" style="color: #0081c1">
                                    Visualizar información en forma de tabla <i class="fa fa-arrow-circle-right fa-lg"></i>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>

                <!--Gráfica de Programa Beca Solicitada-->
                <div class="col-lg-12">
                    <div class="main-box clearfix project-box green-box">
                        <div class="main-box-body clearfix">
                            <div id="grafica-programaBeca" class="project-box-content"></div>
                            <br/>
                            <div class="project-box-ultrafooter clearfix">
                                <a href="#" class="link pull-right" id="linkProgramaBeca" style="color: #0081c1">
                                    Visualizar información en forma de tabla <i class="fa fa-arrow-circle-right fa-lg"></i>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>

                <!--Gráfica de Tipo Beca Preasignada-->
                <div class="col-lg-12">
                    <div class="main-box clearfix project-box green-box">
                        <div class="main-box-body clearfix">
                            <div id="grafica-becaPreasignada" class="project-box-content"></div>
                            <br/>
                            <div class="project-box-ultrafooter clearfix">
                                <a href="#" class="link pull-right" id="linkBecaPreasignada" style="color: #0081c1">
                                    Visualizar información en forma de tabla <i class="fa fa-arrow-circle-right fa-lg"></i>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>

            </div> 
        </div>
    </div>          
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
        var dataArrayS;
        var options;
        var dataG;
        var nivelId;
        var unidadId;
        var periodoId;

        $("#uas,#becas").select2();

        $(".dialTotal").knob();

        $("#nivel").on("change", function () {
            nivelOnChange();
        });

        $("#sendBTN").on("click", function (event) {
            nivelId = $("select[name='nivel']").val();
            unidadId = $('select[name=uas]').val();
            periodoId = $('select[name=periodo]').val();

            if (nivelId === undefined || unidadId === undefined || periodoId === undefined) {
                ModalGenerator.notificacion({
                    "titulo": "¡Atención!",
                    "cuerpo": "Debes seleccionar al menos el nivel de estudios al que quieres sacar estadísticas.",
                    "tipo": "WARNING"
                });
            } else {
                ModalGenerator.notificacion({
                    "titulo": "Procesando su solicitud",
                    "cuerpo": "Estamos realizando los cálculos necesarios para mostrarle la información, por favor sea paciente y no cierre ni actualice esta ventana.",
                    "tipo": "INFO",
                    "sePuedeCerrar": false
                });

                //Obtener datos para tabla hombres/mujeres por fecha    
                $.getJSON("/ajax/registroReporte",
                        {"nivel.id": nivelId,
                            "unidadAcademica.id": unidadId,
                            "beca.id": 0,
                            "periodo.id": periodoId,
                            "movimiento.id": 0
                        }
                ).done(function (response) {
                    ModalGenerator.cerrarModales();
                    $("#sendBTN").blur();
                    // Se limpian límites, en caso de antes haber modificado sus valores.
                    document.getElementById("limit_inferior").value = "";
                    document.getElementById("limit_superior").value = "";
                    llenarGraficas(response.data[0]);
                });

                //Obtener datos para tablas de Beca Preasignada y Beca Solicitada  
                $.getJSON("/ajax/solicitudesReporte",
                        {"nivel.id": nivelId,
                            "unidadAcademica.id": unidadId,
                            "periodo.id": periodoId,
                            "fechaInicial": String.null,
                            "fechaFinal": String.null
                        }
                ).done(function (response) {
                    ModalGenerator.cerrarModales();
                    $("#sendBTN").blur();
                    graficasSolicitudes(response.data[0]);
                });
            }
        });

        //Tabla de Datos Gráfica Solicitudes Total
        $("#linkSolicitudes").click(function () {
            ModalGenerator.notificacion({
                titulo: "Solicitudes de Beca por fechas",
                cuerpo: crearTablaGrafica(dataArray.otorgamientos),
                tipo: "SUCCESS"
            });
        });

        //Tabla de Datos Gráfica Programa Beca Solicitada
        $("#linkProgramaBeca").click(function () {
            ModalGenerator.notificacion({
                titulo: "Programa de Beca Solicitada",
                cuerpo: crearTablaPrograma(dataArrayS.Solicitada),
                tipo: "SUCCESS"
            });
        });

        //Tabla de Datos Gráfica Tipo Beca Preasignada
        $("#linkBecaPreasignada").click(function () {
            ModalGenerator.notificacion({
                titulo: "Tipo de Beca Preasignada",
                cuerpo: crearTablaPreasignada(dataArrayS.Preasignada),
                tipo: "SUCCESS"
            });
        });

        function nivelOnChange() {
            if ($("select[name='nivel']").val() >= 0) {
                $.get("/ajax/getUnidadAcademicaAjax",
                        {pkNivel: $("#nivel option:selected").val()}
                ).done(function (response) {
                    $("#uas").removeAttr("disabled").empty();
                    var array = $.parseJSON(response);
                    $.each(array.data, function (index, value) {
                        $("#uas").append("<option value='" + value[0] + "'>" + value[1] + "</option>");
                    });
                });
            }
        }

        //Tabla de la Gráfica de Total Hombres/Mujeres
        function crearTablaGrafica(array) {
            var table = "<table class='table table-striped table-hover'><thead><tr><th class='text-center'>Fecha</th>";
            table += "<th class='text-center'>Hombres</th><th class='text-center'>Mujeres</th><th class='text-center'>Total</th></tr></thead><tbody>";
            $.each(array, function (index, element) {
                table += "<tr>";
                table += "<td class='text-center'>" + element.nombre + "</td>";
                table += "<td class='text-center'>" + element.totalHombre + "</td>";
                table += "<td class='text-center'>" + element.totalMujer + "</td>";
                table += "<td class='text-center'>" + element.total + "</td></tr>";
            });
            table += "</tbody></table>";
            return table;
        }

        //Tabla de la Gráfica de Total solicitudes Preasignadas
        function crearTablaPreasignada(array) {
            total = array[array.length - 1 ].totalSolicitudes;
            var table = "<table class='table table-striped table-hover'><thead><tr>";
            table += "<th class='text-center'>Nombre</th><th class='text-center'>Total</th></tr></thead><tbody>";
            $.each(array, function (index, element) {
                table += "<tr>";
                table += "<td class='text-center'>" + element.nombre + "</td>";
                table += "<td class='text-center'>" + element.total + "</td></tr>";
            });
            table += "<tr>";
            table += "<td class ='text-center'><b>Total Solicitudes</b></td>";
            table += "<td class ='text-center'><b>" + total + "</b></td></tr>";
            table += "</tbody></table>";
            return table;
        }

        //Tabla de la Gráfica de Total Programa Solicituda
        function crearTablaPrograma(array) {
            total = array[array.length - 1 ].totalSolicitudes;
            var table = "<table class='table table-striped table-hover'><thead><tr>";
            table += "<th class='text-center'>Nombre</th><th class='text-center'>Total</th></tr></thead><tbody>";
            $.each(array, function (index, element) {
                table += "<tr>";
                table += "<td class='text-center'>" + element.nombre + "</td>";
                table += "<td class='text-center'>" + element.total + "</td></tr>";
            });
            table += "<tr>";
            table += "<td class ='text-center'><b>Total Solicitudes</b></td>";
            table += "<td class ='text-center'><b>" + total + "</b></td></tr>";
            table += "</tbody></table>";
            table += "</tbody></table>";
            return table;
        }


        function graficasSolicitudes(data)
        {

            dataArrayS = data;

            //--- Gráfica Solicitudes Preasignadas
            var dataGraph = data.Preasignada;
            var titulo = "Solicitudes Preasignadas";
            var divPrincipal = 'grafica-becaPreasignada';

            crearGraph(dataGraph, titulo, divPrincipal, null);

            dataGraph = null;

            //--- Gráfica Solicitudes Solicitadas
            var dataGraph = data.Solicitada;
            var titulo = "Programa de Beca Solicitada";
            var divPrincipal = 'grafica-programaBeca';

            crearGraph(dataGraph, titulo, divPrincipal, null);
        }

        function llenarGraficas(data) {
            dataArray = data;

            $(".row").show();

            $('#div-t-otorgamientos').show();

            //Gráfica Herradura de Total de Alumnos
            $.each(data.genero, function (index, element) {
                $('.dialTotal').trigger('configure', {
                    "min": 0,
                    "max": element.total
                }).val(element.total).trigger('change');

            });

            //--- Gráfica otorgamientos del periodo
            var dataGraph = data.otorgamientos;
            var titulo = "Solicitudes de beca por fecha";
            var divPrincipal = 'grafica-solicitudes';
            var nElementos = dataGraph.length;
            var banderas = crearBanderas(nElementos);

            crearGraph(dataGraph, titulo, divPrincipal, banderas);
        }

        function crearGraph(dataGraph, titulo, divPrincipal, banderas) {
            if (banderas === null)
            {
                drawChartSolicitudes(dataGraph, titulo, divPrincipal);
            } else if (banderas[0]) {
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

        /*
         *
         */
        function drawChartSolicitudes(datos, titulo, div)
        {
            var data = new google.visualization.DataTable();
            var total = 0;

            //El total viene en el último objeto de la lista
            total = datos[datos.length - 1 ].totalSolicitudes;

            //console.log(new Intl.NumberFormat('mxn-MXN', {maximumSignificantDigits: 3 }).format(total));

            data.addColumn('string', 'Nombre');
            data.addColumn('number', 'Total' + ' (' + new Intl.NumberFormat('mxn-MXN', {maximumSignificantDigits: 6}).format(total) + ')');

            for (k = 0; k < datos.length; k++)
            {
                //console.log("[" + datos[k].nombre + "," + datos[k].total + "]");
                data.addRow([datos[k].nombre + " (" + datos[k].total + ")", datos[k].total]);
            }

            var options = {
                title: titulo,
                theme: 'material',
                legend: {position: 'bottom'},
                width: 800,
                height: 800,
                chartArea: {width: 400, right: 100},
                haxis: {maxValue: 13000}
            };

            var chart = new google.visualization.BarChart(document.getElementById(div));

            chart.draw(data, options);
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
                    var totalHombres = 0, totalMujeres = 0, total = 0, flag = 0, i;
                    var fechaInicial, fechaFinal;

                    //Recorre cada una de las filas de la tabla, agrega al total los valores individuales
                    // y sobreescribe la fecha final, con la fecha de cada fila
                    for (i = 0; i < dataG.getNumberOfRows(); i++) {
                        //se ejecuta si la fila esta dentro del intervalo seleccionado
                        if (state.range.start <= dataG.getValue(i, 0) && state.range.end >= dataG.getValue(i, 0)) {
                            totalHombres += dataG.getValue(i, 1);
                            totalMujeres += dataG.getValue(i, 2);
                            total += dataG.getValue(i, 3);

                            if (flag === 0) {
                                fechaInicial = dataG.getValue(i, 0);
                                flag = 1;
                            }

                            fechaFinal = dataG.getValue(i, 0);

                        }

                    }

                    var inicial = getFormattedDate(fechaInicial);
                    var result = new Date(fechaFinal);
                    result.setDate(result.getDate() + 1);


                    result = getFormattedDate(result);


                    //Obtener datos para tablas de Beca Preasignada y Beca Solicitada  
                    $.getJSON("/ajax/solicitudesReporte",
                            {"nivel.id": nivelId,
                                "unidadAcademica.id": unidadId,
                                "periodo.id": periodoId,
                                "fechaInicial": inicial,
                                "fechaFinal": result
                            }
                    ).done(function (response) {
                        ModalGenerator.cerrarModales();
                        $("#sendBTN").blur();
                        graficasSolicitudes(response.data[0]);
                    });

                    //Actualizar los valores de las graficas
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

        function getFormattedDate(date) {
            var year = date.getFullYear();

            var month = (1 + date.getMonth()).toString();
            month = month.length > 1 ? month : '0' + month;

            var day = date.getDate().toString();
            day = day.length > 1 ? day : '0' + day;

            return day + '/' + month + '/' + year;
        }

        // Dibuja grafica
        function drawGraph(divPrincipal) {
            var variable = new google.visualization.ColumnChart(document.getElementById(divPrincipal));

            variable.draw(dataG, options[0]);
        }

        // Dibuja graficas Solicitudes
        function drawGraphSolicitudes(divPrincipal) {
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

    </script>
    <style>
        .md-modal{position: absolute !important;}
    </style> 
</content>