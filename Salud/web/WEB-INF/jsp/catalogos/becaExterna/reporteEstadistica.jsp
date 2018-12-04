<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Estadística: Beca Externa</title>
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
    Estadística: Beca Externa
</content>

<body>
    <form id="busquedaForm">
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
                                                  list="ambiente.periodoList" 
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
        </div>

        <div class="row">
            <div class="col-xs-12">
                <div class="btn-group pull-right">
                    <button id="sendBTN" class="btn btn-primary" type="button"><span class="fa fa-pie-chart"></span> Generar gráficas</button>
                </div>            
            </div>
        </div> <!-- Termina boton enviar -->
    </form>



    <hr/>


    <div id="porBeca" class="row" style="display: none;" >
        <div class="col-lg-12">
            <div class="main-box clearfix project-box green-box">
                <div class="main-box-body clearfix">
                    <div id="grafica-becaexterna" class="project-box-content grafica" style="width: 900px; height: 500px;"></div>
                    <br/>
                    <div class="project-box-ultrafooter clearfix">
                        <a href="#" class="link pull-right" id="linkBecaexterna" style="color: #0081c1">
                            Visualizar información en forma de tabla <i class="fa fa-arrow-circle-right fa-lg"></i>
                        </a>
                    </div>
                </div>
            </div>
        </div> <!-- Termina Grafica Otorgamientos -->
    </div> <!-- Termina Otorgamientos -->




    <div id="porUA" class="row" style="display: none;" >
        <div class="col-lg-12">
            <div class="main-box clearfix project-box green-box">
                <div class="main-box-body clearfix">
                    <div id="grafica-becaexterna2" class="project-box-content grafica" style="width: 900px; height: 500px;"></div>
                    <br/>
                    <div class="project-box-ultrafooter clearfix">
                        <a href="#" class="link pull-right" id="linkBecaexterna2" style="color: #0081c1">
                            Visualizar información en forma de tabla <i class="fa fa-arrow-circle-right fa-lg"></i>
                        </a>
                    </div>
                </div>
            </div>
        </div> <!-- Termina Grafica Otorgamientos -->
    </div> <!-- Termina Otorgamientos -->

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
                                <a id='tablabecaexterna' title='Resumen pendientes' target='_blank' href='' class="buttons-csv buttons-html5 con-popover">
                                    <img src="/resources/img/modules/excel.svg" width="35" style="padding-bottom:13px;" data-content="Descargar Excel" data-container="body" data-toggle="popover" data-placement="top" data-original-title="" title="">
                                </a>
                            </div>
                        </div>
                        <table id="listado" class="table table-striped table-hover dt-responsive" style="width: 100%">
                            <thead>
                                <tr>
                                    <th data-orderable="true">Boleta</th>
                                    <th data-orderable="true">Nombre</th>
                                    <th data-orderable="true">Curp</th>
                                    <th data-orderable="true">Unidad Academica</th>
                                    <th data-orderable="true">Beca SIBEC</th>
                                    <th data-orderable="true">Beca Externa</th>
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
        var dataArray2;
        var options;
        var dataG;
        $("#uas,#becas").select2();

        $("#nivel").on("change", function () {
            nivelOnChange();
        });

        $("#sendBTN").on("click", function (event) {
            var nivelId = $("select[name='nivel']").val();
            var unidadId = $('select[name=uas]').val();
            var periodoId = $('select[name=periodo]').val();

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

                $.getJSON("/ajax/becaexternaReporte",
                        {"nivel.id": nivelId,
                            "unidadAcademica.id": unidadId,
                            "beca.id": 0,
                            "periodo.id": periodoId,
                            "movimiento.id": 0
                        }
                ).done(function (response) {
                    ModalGenerator.cerrarModales();
                    $("#sendBTN").blur();

                    // Se limpian límites, en caso de antes haber modificado sus valores. *****VERIFICAR SI APLICA
                    document.getElementById("limit_inferior").value = "";
                    document.getElementById("limit_superior").value = "";
                    //llenarGraficas(response.data[0]);

                    var datos = response.data[0];
                    dataArray = datos;

                    var data = new google.visualization.DataTable();
                    data.addColumn('string', 'BECA');
                    data.addColumn('number', 'ALUMNOS');
                    data.addColumn('number', 'ID');


                    $.each(datos.Total, function (index, element) {
                        data.addRow([element[0], element[2], element[1]]);
                    });

                    var options = {
                        title: 'Becas Externas',
                        pieHole: 0.4,
                        chartArea: {width: '90%', height: '83%'},
                        height: 465,
                        width: 750,
                        //width: 867,
                        colors: ['#3BB7B1', '#E56385', '#ED975D', '#720026', '#7699D4', '#FFF05A', '#7CEA9C', '#6FC9F0', '#5B4E77'],
                        //Límite para visualizar los datos muy pequeños en relación al universo, evitando el agrupamiento en la categoría "others".
                        sliceVisibilityThreshold: .0005
                    };

                    var chart = new google.visualization.PieChart(document.getElementById('grafica-becaexterna'));

                    //Funcion para interactuar con la grafica
                    function selectHandler() {
                        var selectedItem = chart.getSelection()[0];
                        if (selectedItem) {
                            var topping = data.getValue(selectedItem.row, 2);
                            var nombrebeca = data.getValue(selectedItem.row, 0);
                            $("#div-tabla").hide();
                            crearSegundaGrafica(nivelId, periodoId, topping, nombrebeca);
                        }
                    }

                    google.visualization.events.addListener(chart, 'select', selectHandler);

                    chart.draw(data, options);
                    $("#porBeca").show();
//                    $('#div-tabla').hide();
//                    $('#loading-row-div-tabla').hide();

                });
            }
        });

        $("#linkBecaexterna").click(function () {
            ModalGenerator.notificacion({
                titulo: "Becas Externas",
                cuerpo: crearTablaGrafica(dataArray.Total),
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
            var total = 0;
            table += "<th class='text-center'>Total</th></tr></thead><tbody>";
            $.each(array, function (index, element) {
                table += "<tr>";
                table += "<td>" + element[0] + "</td>";
                table += "<td class='text-center'>" + element[2] + "</td></tr>";
                total += element[2]
            });
            table += "<tr>";
            table += "<td>Total</td>";
            table += "<td class='text-center'>" + total + "</td></tr>";
            table += "</tbody></table>";
            return table;
        }
        
        function crearTablaGrafica2(array) {
            var table = "<table class='table table-striped table-hover'><thead><tr><th>Estatus</th>";
            var total = 0;
            table += "<th class='text-center'>Total</th></tr></thead><tbody>";
            $.each(array, function (index, element) {
                table += "<tr>";
                table += "<td>" + element[0] + "</td>";
                table += "<td class='text-center'>" + element[1] + "</td></tr>";
                total += element[1]
            });
            table += "<tr>";
            table += "<td>Total</td>";
            table += "<td class='text-center'>" + total + "</td></tr>";
            table += "</tbody></table>";
            return table;
        }

        function crearSegundaGrafica(nivelId, periodoId, idbeca, nombrebeca) {
            $.getJSON("/ajax/becaexternaUAReporte",
                    {
                        "nivel.id": nivelId,
                        "becaExternaId": idbeca,
                        "periodo.id": periodoId,
                    }
            ).done(function (response) {

                // Se limpian límites, en caso de antes haber modificado sus valores. *****VERIFICAR SI APLICA
                document.getElementById("limit_inferior").value = "";
                document.getElementById("limit_superior").value = "";
                //llenarGraficas(response.data[0]);

                var datos = response.data[0];
                dataArray2 = datos;

                var data = new google.visualization.DataTable();
                data.addColumn('string', 'UA');
                data.addColumn('number', 'ALUMNOS');
                data.addColumn('number', 'ID_UA');


                $.each(datos.Total, function (index, element) {
                    data.addRow([element[0], element[1], element[2]]);
                });

                var options = {
                    title: nombrebeca,
                    pieHole: 0.4,
                    chartArea: {width: '90%', height: '83%'},
                    height: 465,
                    width: 750,
                    //width: 867,
                    colors: ['#3BB7B1', '#E56385', '#ED975D', '#720026', '#7699D4', '#FFF05A', '#7CEA9C', '#6FC9F0', '#5B4E77'],
                    //Límite para visualizar los datos muy pequeños en relación al universo, evitando el agrupamiento en la categoría "others".
                    sliceVisibilityThreshold: .0005
                };

                var chart = new google.visualization.PieChart(document.getElementById('grafica-becaexterna2'));

                //Funcion para interactuar con la grafica
                function selectHandler() {
                    var selectedItem = chart.getSelection()[0];
                    if (selectedItem) {
                        var idua = data.getValue(selectedItem.row, 2);
                        var nombeca = data.getValue(selectedItem.row, 0);
                        crearTable(idua, idbeca, nombeca);
                    }
                }

                google.visualization.events.addListener(chart, 'select', selectHandler);

                chart.draw(data, options);
                $("#porUA").show();
//                    $('#div-tabla').hide();
//                    $('#loading-row-div-tabla').hide();
//
                document.getElementById('porUA').scrollIntoView();
            });

            function crearTable(idua, idbeca, nombeca) {
                var url = "/ajax/tablaBecaExternaReporte.action";
                var urlReporte = "/becasexternas/descargaExcel.action";
                var periodoId = $('select[name=periodo]').val();
                urlReporte = urlReporte + "?periodo.id=" + periodoId + "&unidadacademica.id=" + idua + "&becaid=" + idbeca + "&unidadacademica.nombreCorto=" + nombeca;
                url = url + "?periodo.id=" + periodoId + "&unidadAcademica.id=" + idua + "&becaExternaId=" + idbeca;
                document.getElementById("tablabecaexterna").href = urlReporte;

                document.getElementById("tableTitle").innerHTML = nombeca;
                

                agregaPopovers();
                generarTabla("div-tabla", url, despuesDeCargarTabla, false);
                document.getElementById('tablabecaexterna').scrollIntoView();
            }
            function despuesDeCargarTabla() {

                $('.fancybox').fancybox({
                    autoSize: true,
                    afterClose: function () {
//                    mostrarTablaResultados();
                    }
                });
                
                document.getElementById('tablabecaexterna').scrollIntoView();
            }
            
            

        }

        $("#linkBecaexterna2").click(function () {
            ModalGenerator.notificacion({
                titulo: "Becas Externas",
                cuerpo: crearTablaGrafica2(dataArray2.Total),
                tipo: "SUCCESS"
            });
        });
    </script>
</content>
