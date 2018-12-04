<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Estadísticas: Estatus de Cuentas</title>
    <link rel="stylesheet" type="text/css" href="/vendors/select2/css/select2.min.css" />
    <link rel="stylesheet" type="text/css" href="/vendors/fancy-box/jquery.fancybox.min.css"/>
    <style>
        /* bootstrap hack: soluciona el ancho para el contenido de tabs ocultas */
        .tab-content > .tab-pane:not(.active),
        .pill-content > .pill-pane:not(.active) {
            display: block;
            height: 0;
            overflow-y: hidden;
        }

        .excel{
            right: 2%;
        }

        .links a:visited {
            color: white;
        }

        .grafica text:hover, g:hover { 
            cursor: pointer;
        }
        /* Termina bootstrap hack */
    </style>
</head> 

<content tag="tituloJSP">
    Estadísticas: Estatus de Cuentas
</content>

<body>
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
    </div> <!-- Termina filtro -->

    <div class="row">
        <div class="col-xs-12">
            <div class="btn-group pull-right links">
                <a id="downloadBTN" href="" class="btn btn-primary" type="submit"><span class="fa fa-download"></span> Descargar reporte</button></a>
                <button id="sendBTN" class="btn btn-primary" type="button"><span class="fa fa-pie-chart"></span> Generar gráfica</button>
            </div>            
        </div>

    </div> <!-- Termina boton enviar -->


    <hr/>

    <div class="row" style="display: none;">
        <div class="col-xs-12">
            <div class="row" style="display: none;" >
                <div class="col-lg-12">
                    <div class="main-box clearfix project-box green-box">
                        <div class="main-box-body clearfix">
                            <div id="grafica-otorgamientos" class="project-box-content grafica" style="width: 900px; height: 500px;"></div>
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

    <!-- DIV para la tabla -->
    <div class="row" id="div-tabla" style="display: none;">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <div class="col-md-12" style="padding: 0px">
                    <div class="responsive">
                        <br>
                        <div class="row">
                            <div class="col-lg-6">
                                <h1 id="tableTitle"></h1>
                            </div>
                            <div class="col-lg-6 text-right excel">
                                <a id='pendientes' title='Resumen pendientes' target='_blank' href='' class="buttons-csv buttons-html5 con-popover">
                                    <img src="/resources/img/modules/excel.svg" width="35" style="padding-bottom:13px;" data-content="Descargar Excel" data-container="body" data-toggle="popover" data-placement="top" data-original-title="" title="">
                                </a>
                            </div>
                        </div>
                        <table id="listado" class="table table-striped table-hover dt-responsive" style="width: 100%">
                            <thead>
                                <tr>
                                    <th data-orderable="true">Boleta</th>
                                    <th data-orderable="true">Nombre</th>
                                    <th data-orderable="true">Unidad Académica</th>
                                    <th></th>
                                </tr>
                            </thead>
                        </table>                        
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
    <script type="text/javascript" src="/vendors/fancy-box/jquery.fancybox.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>
        google.charts.load("current", {packages: ["corechart"]});

        $(document).ready(function () {

        });

        function agregaPopovers() {
            $('.buttons-csv img').attr('data-content', "Descargar Excel");

            $('.con-popover img').attr('data-container', "body");
            $('.con-popover img').attr('data-toggle', "popover");
            $('.con-popover img').attr('data-placement', "top");
            $('[data-toggle="popover"]').popover({
                trigger: 'hover'
            });
        }

        var dataArray;
        var options;
        var dataG;
        $("#uas,#becas").select2();

        $("#nivel").on("change", function () {
            nivelOnChange();
        });

        <s:if test="!responsable">
        nivelOnChange();
        </s:if>

        $("#downloadBTN").on("click", function (event) {
            var id = -1;
            var nivelId = $("select[name='nivel']").val();
            var unidadId = $('select[name=uas]').val();
            var periodoId = $('select[name=periodo]').val();
            var urlReporte = "estatusCuentas.action";
            urlReporte = urlReporte + "?nivel=" + nivelId + "&uas=" + unidadId + "&periodo=" + periodoId + "&estatus=" + id;
            document.getElementById("downloadBTN").href = urlReporte;
        });

        $("#sendBTN").on("click", function (event) {
            var nivelId = $("select[name='nivel']").val();
            var unidadId = $('select[name=uas]').val();
            var periodoId = $('select[name=periodo]').val();
            $('#div-tabla').hide();
            $('#grafica-otorgamientos').show();

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
                $.getJSON("/ajax/cuentasReporte",
                        {"nivel.id": nivelId,
                            "unidadAcademica.id": unidadId,
                            "beca.id": 0,
                            "periodo.id": periodoId,
                            "movimiento.id": 0
                        }
                ).done(function (response) {
                    ModalGenerator.cerrarModales();
                    $("#sendBTN").blur();

//					llenarGraficas(response.data[0]);
                    var datos = response.data[0];
                    dataArray = datos;

                    //Gráfica
                    var data = new google.visualization.DataTable();
                    data.addColumn('string', 'ESTATUS');
                    data.addColumn('number', 'ALUMNOS');
                    data.addColumn('number', 'ID');

                    $.each(datos.genero, function (index, element) {
                        data.addRow(['Liberada', element.ax1, 13]);
                        data.addRow(['En trámite', element.ax2, 12]);
                        data.addRow(['Depósito por referencia', element.ax3, 17]);
                        data.addRow(['Corrección de datos', element.ax4, 15]);
                        data.addRow(['Cuenta cancelada', element.ax5, 16]);
                        data.addRow(['Referencia Cancelada', element.ax6, 18]);
                        data.addRow(['Rechazada', element.ax7, 14]);
                        data.addRow(['Referencia cobrada', element.ax8, 19]);
                        data.addRow(['Sin cuenta', element.ax9, 0]);
                    });

                    var options = {
                        title: 'ESTATUS DE CUENTAS',
                        pieHole: 0.4,
                        chartArea: {width: '90%', height: '83%'},
                        height: 465,
                        width: 750,
                        //width: 867,
                        colors: ['#3BB7B1', '#E56385', '#ED975D', '#720026', '#E9724C', '#FFF05A', '#7CEA9C', '#6FC9F0', '#2E5EAA', '#5B4E77', '#7699D4'],
                        //Límite para visualizar los datos muy pequeños en relación al universo, evitando el agrupamiento en la categoría "others".
                        sliceVisibilityThreshold: .0005
                    };

                    var chart = new google.visualization.PieChart(document.getElementById('grafica-otorgamientos'));

                    //Funcion para interactuar con la grafica
                    function selectHandler() {
                        var selectedItem = chart.getSelection()[0];
                        if (selectedItem) {
                            var topping = data.getValue(selectedItem.row, 2);
                            mostrarTablaResultados(topping);
                        }
                    }
                    google.visualization.events.addListener(chart, 'select', selectHandler);

                    chart.draw(data, options);
                    $(".row").show();
                    $('#div-tabla').hide();
                    $('#loading-row-div-tabla').hide();
                });
            }
        });





        $("#linkOtorgamientos").click(function () {
            ModalGenerator.notificacion({
                titulo: "Otorgamientos por fechas",
                cuerpo: crearTablaGrafica(dataArray.genero),
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

        function crearTablaGrafica(array) {
            var table = "<table class='table table-striped table-hover'><thead><tr><th>Estatus</th>";
            table += "<th class='text-center'>Total</th></tr></thead><tbody>";
            $.each(array, function (index, element) {
                table += "<tr>";
                table += "<td>Liberada</td>";
                table += "<td class='text-center'>" + element.ax1 + "</td></tr>";
                table += "<tr>";
                table += "<td>En trámite</td>";
                table += "<td class='text-center'>" + element.ax2 + "</td></tr>";
                table += "<tr>";
                table += "<td>Depósito por referencia</td>";
                table += "<td class='text-center'>" + element.ax3 + "</td></tr>";
                table += "<tr>";
                table += "<td>Corrección de datos</td>";
                table += "<td class='text-center'>" + element.ax4 + "</td></tr>";
                table += "<tr>";
                table += "<td>Cuenta cancelada</td>";
                table += "<td class='text-center'>" + element.ax5 + "</td></tr>";
                table += "<tr>";
                table += "<td>Referencia Cancelada</td>";
                table += "<td class='text-center'>" + element.ax6 + "</td></tr>";
                table += "<tr>";
                table += "<td>Rechazada</td>";
                table += "<td class='text-center'>" + element.ax7 + "</td></tr>";
                table += "<tr>";
                table += "<td>Referencia cobrada</td>";
                table += "<td class='text-center'>" + element.ax8 + "</td></tr>";
                table += "<tr>";
                table += "<td>Sin cuenta</td>";
                table += "<td class='text-center'>" + element.ax9 + "</td></tr>";
                table += "<tr>";
                table += "<td>Total</td>";
                table += "<td class='text-center'>" + element.total + "</td></tr>";
            });
            table += "</tbody></table>";
            return table;
        }

        function mostrarTablaResultados(id) {
            var url = "/ajax/tablaCuentasReporte.action";
            var urlReporte = "estatusCuentas.action";
            var nivelId = $("select[name='nivel']").val();
            var unidadId = $('select[name=uas]').val();
            var periodoId = $('select[name=periodo]').val();
            urlReporte = urlReporte + "?nivel=" + nivelId + "&uas=" + unidadId + "&periodo=" + periodoId + "&estatus=" + id;
            url = url + "?nivelId=" + nivelId + "&uaId=" + unidadId + "&periodoId=" + periodoId + "&estatusId=" + id;
            document.getElementById("pendientes").href = urlReporte;
            var names = [];

        <s:iterator value="ambiente.estatusTarjetaBancariaList" >
            names.push("<s:property value="nombre" escape="false"/>");
        </s:iterator>
            if (id === 0)
            {
                document.getElementById("tableTitle").innerHTML = " Estatus | Sin cuenta";
            } else
            {
                document.getElementById("tableTitle").innerHTML = "Estatus | " + names[id - 1];
            }

            agregaPopovers();
            generarTabla("div-tabla", url, despuesDeCargarTabla, true);
        }

        function despuesDeCargarTabla() {
            $('.fancybox').fancybox({
                autoSize: true,
                afterClose: function () {
//                    mostrarTablaResultados();
                }
            });
        }
    </script>
    <style>
        .md-modal{position: absolute !important;}
    </style> 
</content>