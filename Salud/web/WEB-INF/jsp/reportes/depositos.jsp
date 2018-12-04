<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Estadísticas: Estatus de Depósitos</title>
    <link rel="stylesheet" type="text/css" href="/vendors/select2/css/select2.min.css" />


    <style>
        /* bootstrap hack: soluciona el ancho para el contenido de tabs ocultas */
        .tab-content > .tab-pane:not(.active),
        .pill-content > .pill-pane:not(.active) {
            display: block;
            height: 0;
            overflow-y: hidden;
        } 
        /*Hace las tablas dentro de los modals responsivas*/
        .table-responsive {
            display: block;
            width: 100%;
            overflow-x: auto;
            -webkit-overflow-scrolling: touch;
            -ms-overflow-style: -ms-autohiding-scrollbar;
        }

        .table tbody>tr>th {
            font-size: .875em;
        }

        /* Termina bootstrap hack */
    </style>
</head> 

<content tag="tituloJSP">
    Estadísticas: Estatus de Depósitos
</content>

<body>
    <form id="busquedaForm" action="/descargaDepositosEstadisticaDepositos.action">
        <div class="row">
            <div class="col-lg-12">
                <s:if test="hasActionErrors()">
                    <div class="alert alert-danger">
                        <i class="fa fa-times-circle fa-fw fa-lg"></i>
                        <strong>&iexcl;Error!</strong> <s:actionerror/>
                    </div>
                </s:if>
                <div class="alert alert-info">
                    <i class="fa fa-info-circle fa-fw fa-lg"></i>
                    La información mostrada en este reporte no considera la información sobre bajas diversas.
                </div>
            </div>
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
                                        <s:select id="periodo"
                                                  cssClass="form-control espera"
                                                  name="periodo" 
                                                  list="ambiente.periodoList" 
                                                  listKey="id" 
                                                  listValue="clave"
                                                  disabled="true" />
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
                    class="col-lg-2"
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
                    class="col-lg-3"
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

            <div class="col-lg-3">
                <div class="main-box clearfix" >
                    <div class="row">
                        <header class="main-box-header clearfix">
                            <h2 class="pull-left">Tipo Beca</h2>
                        </header>
                        <div class="main-box-body clearfix">
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <select id="tbp" name="tbp" class="form-control" disabled>
                                            <option value="0">Todos los tipos de beca</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-lg-2">
                <div class="main-box clearfix" >
                    <div class="row">
                        <header class="main-box-header clearfix">
                            <h2 class="pull-left">Mes</h2>
                        </header>
                        <div class="main-box-body clearfix">
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <select id="mes" name="mes" class="form-control espera" role="menu" disabled>
                                            <option value="1">Enero</option>
                                            <option value="2">Febrero</option>
                                            <option value="3">Marzo</option>
                                            <option value="4">Abril</option>
                                            <option value="5">Mayo</option>
                                            <option value="6">Junio</option>
                                            <option value="7">Julio</option>
                                            <option value="8">Agosto</option>
                                            <option value="9">Septiembre</option>
                                            <option value="10">Octubre</option>
                                            <option value="11">Noviembre</option>
                                            <option value="12">Diciembre</option>
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
                    <button id="downloadAdeudos" class="btn btn-primary" type="submit" formaction="/descargaReporteDepositosEstadisticaDepositos.action"><span class="fa fa-download"></span> Descargar reporte de pagos y adeudos</button>
                    <button id="downloadBTN" class="btn btn-primary espera" type="submit" disabled><span class="fa fa-download"></span> Descargar reporte</button>
                    <button id="sendBTN" class="btn btn-primary espera" type="button" disabled><span class="fa fa-pie-chart"></span>Generar gráficas</button>
                </div>
            </div>
        </div> <!-- Termina boton enviar -->
    </form>


    <hr/>

    <div class="row" style="display: none;">
        <div class="col-xs-12">
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
                            <h2>Total Alumnos</h2>
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

        var chartHeght = 800;
        var dataArray;
        var options;
        var dataG;

        $("#uas,#becas").select2();

        $(".dialHombres,.dialMujeres,.dialTotal").knob();

        <s:if test="!responsable">
        nivelOnChange();
        </s:if>

        $("#nivel").on("change", function () {
            nivelOnChange();
        });


        getMes();
        $("#periodo").on("change", function () {
            $(".espera").prop("disabled", true);
            getMes();
            getTipoBeca();
        });

        $("#uas").on("change", function () {
            getTipoBeca();
        });


        $("#sendBTN").on("click", function (event) {
            var nivelId = $("select[name='nivel']").val();
            var unidadId = $('select[name=uas]').val();
            var periodoId = $('select[name=periodo]').val();
            var tipoBecaId = $('select[name=tbp]').val();

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

                $.getJSON("/ajax/depositosReporte",
                        {
                            "nivel.id": nivelId,
                            "unidadAcademica.id": unidadId,
                            "periodo.id": periodoId,
                            tipoBecaPeriodoId: tipoBecaId
                        }
                ).done(function (response) {
                    ModalGenerator.cerrarModales();
                    $("#sendBTN").blur();
                    llenarGraficas(response.data[0]);
                });
            }
        });

        $("#linkOtorgamientos").click(function () {
            ModalGenerator.notificacion({
                titulo: "Estatus de depositos por mes",
                cuerpo: crearTablaGrafica(dataArray.otorgamientos),
                tipo: "SUCCESS"
            });

            $('#modal-id-1').modal().css({
                "width": "auto"
            });

            transpuesta();

        });


        //Función que hace la transpuesta de la tabla por gráfica
        function transpuesta()
        {
            //Encuentra el objeto table y lo recorre   
            $("table").each(function () {
                var $this = $(this);
                var newrows = [];
                //Luego encuentra todos los tableRows de la tabla y los recorre
                $this.find("tr").each(function () {
                    var i = 0;
                    var j = 0;
                    //Luego encuentra todos los tableHeaders de tableRow actual y los recorre
                    $(this).find("th").each(function () {
                        j++;
                        //Si encuentra que el elemento j del arreglo newrows es un objeto indefinido,
                        //lo convierte en un tableRow
                        if (newrows[j] === undefined) {
                            newrows[j] = $("<tr></tr>");
                        }
                        //Y le agrega los datos del tableHeader de la iteración actual
                        newrows[j].append($(this));
                    });

                    //Hace lo mismo para los tableDatacell
                    $(this).find("td").each(function () {
                        i++;
                        if (newrows[i] === undefined) {
                            newrows[i] = $("<tr></tr>");
                        }
                        newrows[i].append($(this));
                    });
                });

                //Luego encuentra todos los TableRows del objeto table (La tabla original) y los elimina
                $this.find("tr").remove();

                //Después recorre el arreglo de las nuevas filas y se lo agrega al objeto table original
                $.each(newrows, function () {
                    $this.append(this);
                });
            });
            return false;
        }

        //Función que busca los meses con base en el periodo
        function getMes() {
            $.ajax({
                type: 'POST',
                url: '/ajax/getMesMesPeriodo.action',
                dataType: 'json',
                data: {pkPeriodo: $('#periodo').val()},
                cache: false,
                success: function (aData) {
                    $('#mes').get(0).options.length = 0; //Borra todos los elementos
                    $.each(aData.data, function (i, item) {
                        $('#mes').get(0).options[$('#mes').get(0).options.length] = new Option(item[1], item[0]);
                    });
                    // Se activan los botones de la sección "Predeterminado"
                    habDeshabilitar([".espera"]);
                },
                error: function () {
                    ModalGenerator.notificacion({
                        "titulo": "Atención",
                        "cuerpo": "Hubo un problema que impidió que se completara la operación.",
                        "tipo": "DANGER"
                    });
                }
            });
            return false;
        }

        function habDeshabilitar(arr) {
            arr.forEach(function (item) {
                $(item).each(function () {
                    var actualState = $(this).prop("disabled");
                    if (actualState === false) {
                        $(this).prop("disabled", true);
                    } else {
                        $(this).prop("disabled", false);
                    }
                });
            });
        }

        function nivelOnChange() {
            if ($("select[name='nivel']").val() >= 0) {

                //Obtiene Datos de Unidad Académica para Select
                $.get("/ajax/getUnidadAcademicaAjax",
                        {pkNivel: $("#nivel option:selected").val()}
                ).done(function (response) {
                    $("#uas").removeAttr("disabled").empty();
                    var array = $.parseJSON(response);
                    $.each(array.data, function (index, value) {
                        $("#uas").append("<option value='" + value[0] + "'>" + value[1] + "</option>");
                    });
                });

                getTipoBeca();
            }
        }

        function getTipoBeca()
        {
            //Obtiene Datos de Tipo Beca para Select
            $.get("/ajax/becasEstatusDepositoTipoBecaPeriodoAjax",
                    {
                        nivelId: $("#nivel option:selected").val(),
                        periodoId: $("#periodo option:selected").val(),
                        unidadAcademica: $("#uas option:selected").val()

                    }
            ).done(function (response) {
                $("#tbp").removeAttr("disabled").empty();
                $("#tbp").append("<option value='0'>Todos los tipos de beca</option>");
                var array = $.parseJSON(response);
                $.each(array.data, function (index, value) {
                    $("#tbp").append("<option value='" + value[0] + "'>" + value[1] + "</option>");
                });
            });
        }

        function crearTablaGrafica(array) {
            var table = "<table class='table table-striped table-hover table-responsive' id = 'tableGrafica'><thead><tr><th></th>";
            table += "<th class='text-left'>En espera</th>\n";
            table += "<th class='text-left'>Aplicado</th>\n";
            table += "<th class='text-left'>Reembolso</th>\n";
            table += "<th class='text-left'>Rechazado</th>\n";
            table += "<th class='text-left'>Cancelado</th>\n";
//            table += "<th class='text-left'>Rechazo Trabajado</th>\n";
            table += "<th class='text-left'>Referencia</th>\n";
            table += "<th class='text-left'>Depósito especial</th>\n";
            table += "<th class='text-left'>Pendientes</th>\n";
            table += "<th class='text-left'>No aplica 6° depósito</th>\n";
            table += "<th class='text-left'>Total</th></tr></thead><tbody>";
            $.each(array, function (index, element) {
                table += "<tr>";
                table += "<td>" + element.nombre + "</td>";
                table += "<td class='text-center'>" + element.ax1 + "</td>";
                table += "<td class='text-center'>" + element.ax2 + "</td>";
                table += "<td class='text-center'>" + element.ax3 + "</td>";
                table += "<td class='text-center'>" + element.ax4 + "</td>";
                table += "<td class='text-center'>" + element.ax5 + "</td>";
//                table += "<td class='text-center'>" + element.ax6 + "</td>";
                table += "<td class='text-center'>" + element.ax7 + "</td>";
                table += "<td class='text-center'>" + element.ax8 + "</td>";
                table += "<td class='text-center'>" + element.ax9 + "</td>";
                // Se comentan pues fueron excluidas los estatus 7, 8 y 9. Se agrega el 10
                table += "<td class='text-center'>" + element.ax10 + "</td>";
                // table += "<td class='text-center'>" + element.ax11 + "</td>";
                table += "<td class='text-center'>" + element.total + "</td></tr>";
            });


            // Inicia creación de fila inferior "Totales"
            var axTotal = [];
            // Inicializa un arreglo para contener la suma cada uno de los ax's. Límite superior: cantidad de ax a usar
            for (var i = 0; i < 9; i++) {
                axTotal.push(0);
            }
            // Obtiene las llaves de cada elemento de la BD
            var keys = Object.keys(array[0]);
            // Recorre y suma los resultados de la BD
            for (var i = 0; i < array.length; i++) {
                for (var j = 0; j < axTotal.length; j++) {
                    axTotal[j] += array[i][keys[j + 6]];
                }
            }
            // Crea filas
            table += "<tr>";
            table += "<td>Totales</td>";
            $.each(axTotal, function (index, element) {
                if (index != 5) {
                    table += "<td class='text-center'>" + element + "</td>";
                }
            });
            table += "<td class='text-center'> - </td></tr>";
            // Fin creación de "Totales"

            table += "</tbody></table>";
            return table;
        }

        function llenarGraficas(data) {
            dataArray = data;

            $(".row").show();

            $('#div-t-hombres').hide();
            $('#div-t-mujeres').hide();
            $('#div-t-otorgamientos').hide();

            //--- Gráfica otorgamientos del periodo
            var dataGraph = data.otorgamientos;
            var titulo = "Depositos";
            var divPrincipal = 'grafica-otorgamientos';
            var nElementos = dataGraph.length;
            var banderas = crearBanderas(nElementos);

            crearGraph(dataGraph, titulo, divPrincipal, banderas);
        }

        function crearGraph(dataGraph, titulo, divPrincipal, banderas) {
            limpiarDiv(divPrincipal);
            prepareGraph(dataGraph);
            createOptions(titulo, banderas[1]);
            drawGraph(divPrincipal);
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

        function prepareGraph(dataGraph) {
            // Crea el header de la gráfica
            createHeader(false);
            // Agrega datos a las filas
            $.each(dataGraph, function (index, element) {
                addElement(element, false);
            });
        }

        // Dibuja grafica
        function drawGraph(divPrincipal) {
            var view = new google.visualization.DataView(dataG);
            view.setColumns([0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
                {
                    calc: function (dt, row) {
                        return dt.getValue(row, 1) + dt.getValue(row, 2) + dt.getValue(row, 3) + dt.getValue(row, 4) + dt.getValue(row, 5) + dt.getValue(row, 6) + dt.getValue(row, 7) + dt.getValue(row, 8) + dt.getValue(row, 9) + dt.getValue(row, 10);
                    },
                    type: "number",
                    role: "annotation"
                }
            ]);
            var chart = new google.visualization.ColumnChart(document.getElementById(divPrincipal));
            chart.draw(view, options[0]);
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
                dataG.addColumn('string', 'Estatus');
            dataG.addColumn('number', 'En espera');
            dataG.addColumn('number', 'Aplicado');
            dataG.addColumn('number', 'Reembolso');
            dataG.addColumn('number', 'Rechazado');
            dataG.addColumn('number', 'Cancelado');
            dataG.addColumn('number', 'Rechazo Trabajado');
            dataG.addColumn('number', 'Referencia expirada');
            dataG.addColumn('number', 'Depósito especial');
            dataG.addColumn('number', 'Pendientes');
            dataG.addColumn('number', 'No aplica 6° depósito');
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
                var date = new Date(year, month, day);
            } else {
                var date = element.nombre
            }

            dataG.addRow([
                date,
                element.ax1,
                element.ax2,
                element.ax3,
                element.ax4,
                element.ax5,
                element.ax6,
                element.ax7,
                element.ax8,
                element.ax9,
                element.ax10
                        // Se comentan pues fueron excluidas los estatus 7, 8 y 9.                         
                        // element.ax11
            ]);
        }

        // Crea las opciones para la gráfica
        function createOptions(title, option) {
            var optionsC = {
                theme: 'material',
                chartArea: {width: '65%', height: '83%'},
                colors: ['#3BB7B1', '#E56385', '#ED975D', '#720026', '#E9724C', '#FFF05A', '#7CEA9C', '#3BB7B1', '#2E5EAA', '#5B4E77', '#7699D4'],
                hAxis: {showTextEvery: 1, type: 'category'},
                vAxis: {viewWindowMode: 'maximized'},
                legend: {position: 'right'},
                animation: {
                    duration: 1000,
                    easing: 'out',
                    startup: true
                },
                height: chartHeght,
                title: title + " (" + $('select[name=periodo] option:selected').text() + ")",
                isStacked: true,
            };
            // En caso de ser más de 10 elementos pero menos de 20, las 
            // etiquetas se dibujan inclinadas
            if (option) {
                optionsC['hAxis']['slantedText'] = true;
            }
            var optionsRF = {
                theme: 'material',
                height: 70,
                colors: ['#3BB7B1', '#E56385', '#ED975D', '#720026', '#E9724C', '#FFF05A', '#7CEA9C', '#3BB7B1', '#2E5EAA', '#5B4E77', '#7699D4'],
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
                colors: ['#3BB7B1', '#E56385', '#ED975D', '#720026', '#E9724C', '#FFF05A', '#7CEA9C', '#3BB7B1', '#2E5EAA', '#5B4E77', '#7699D4'],
            };
            options = [optionsC, optionsRF, optionsP];
        }

    </script>
    <style>
        .md-modal{position: absolute !important;}
    </style> 
</content>