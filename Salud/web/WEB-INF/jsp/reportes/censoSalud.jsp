<%-- 
    Document   : cuestionarios
    Created on : 18/06/2018, 11:49:29 AM
    Author     : Augusto I. Hernández Ruiz
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Estadísticas: Censo Salud</title>
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

        .padd
        {
            padding-bottom: 10px;
        }

        .link
        {
            cursor: pointer;
        }
        /* Termina bootstrap hack */
    </style>
</head> 

<content tag="tituloJSP">
    Estadísticas: Censo de Salud
</content>

<body>
    <div class="row">
        <div class="col-lg-12">
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
    </div> <!-- Termina filtro -->


    <div class="row">
        <div class="col-xs-12">
            <div class="btn-group pull-right links">
                <button id="downloadBTN" class="btn btn-primary" type="button" style="display: none;" onclick="generatePDF()"><span class="fa fa-download" ></span> Descargar PDF</button>
                <button id="sendBTN" class="btn btn-primary" type="button"><span class="fa fa-pie-chart"></span> Generar gráfica</button>
            </div>            
        </div>

    </div> <!-- Termina boton enviar -->


    <hr/>

    <!-- Tabs -->
    <div class="row">
        <div class="col-xs-12">
            <div class="main-box clearfix" style="display: none;" id="main">                   
                <div class="tabs-wrapper profile-tabs">
                    <ul class="nav nav-tabs" id="navBar">
                    </ul>                        
                    <div class="tab-content" id="contenido-tab">
                        <h2 id="seccion"></h2>
                    </div>
                </div>
                <div class="row" id="rowGrafica" style="display: none;" >
                    <div class="col-lg-12">
                        <div class="main-box clearfix project-box green-box">
                            <div class="main-box-body clearfix" id="grafica-cuestionarios">

                            </div>
                        </div>
                    </div>
                </div>   
            </div>
        </div>
    </div>

    <!-- div para almacenar las imagenes de las gráficas que serán exportadas a pdf -->
    <div id='graph-images' style='display:none'></div>

</body>

<content tag="endScripts">
    <script type="text/javascript" src="/resources/js/loader.js"></script>
    <script type="text/javascript" src="/vendors/select2/js/select2.full.min.js"></script>
    <script type="text/javascript" src="/vendors/knob/jquery.knob.min.js" ></script>
    <script type="text/javascript" src="/resources/js/ModalGenerator.js"></script>
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js"></script>
    <script type="text/javascript" src="/vendors/fancy-box/jquery.fancybox.min.js"></script>
    <script type="text/javascript" src="/vendors/jsPDF-1.3.2/jspdf.debug.js"></script>
</content>

<content tag="inlineScripts">
    <script>

                    google.charts.load("current", {packages: ["corechart"]});

                    /* Arreglos globales que se usarán para generar las tablas que mostrarán las respuestas asociadas a una pregunta en particular,
                     * que serán utilizados en la función modalTablaGrafica()
                     */
                    var preguntaRespuestas = [];
                    var pregRespIndv = [];
                    var counter = 0;
                    var periodoClave = "<s:property value="periodo"/>";

                    $("#sendBTN").on("click", function (event) {
                        $('#rowGrafica').show();
                        $('#main').show();
                        $('#downloadBTN').show();

                        var cuestionarioId = 3;
                        var periodoId = $('select[name=periodo]').val();
                        if (cuestionarioId === undefined || periodoId === undefined)
                        {
                            ModalGenerator.notificacion({
                                "titulo": "¡Atención!",
                                "cuerpo": "Debes seleccionar el periodo y el cuestionario.",
                                "tipo": "WARNING"
                            });
                        } else
                        {
                            ModalGenerator.notificacion({
                                "titulo": "Procesando su solicitud",
                                "cuerpo": "Estamos realizando los cálculos necesarios para mostrarle la información, por favor sea paciente y no cierre ni actualice esta ventana.",
                                "tipo": "INFO",
                                "sePuedeCerrar": false
                            });
                            $.getJSON("/ajax/cuestionariosReporte",
                                    {
                                        "periodo.id": periodoId,
                                        "cuestionario.id": cuestionarioId
                                    }
                            )
                                    .done(function (response)
                                    {
                                        ModalGenerator.cerrarModales();
                                        $("#sendBTN").blur();

                                        var datos = response.data[0];
                                        var dataArray = datos.Cuestionarios;
                                        var preguntaIdAux;
                                        var preguntaAux;
                                        var seccionAux;
                                        var respuestas = [];
                                        var totalRespuestas = [];
                                        var nombreNavBar;

                                        for (i in dataArray)
                                        {
                                            var idActual = dataArray[i].preguntaId;
                                            var preguntaActual = dataArray[i].pregunta;
                                            var respuestaActual = dataArray[i].respuesta;
                                            var seccionActual = dataArray[i].seccion;


                                            /*Si es el primer registro, se agrega el id de la pregunta al array "pregRespIndv" que almacena en su primer elemento
                                             *el id de la pregunta, en el segundo el string de la pregunta y en el siguente, el array con todos los pares [respuesta,total]
                                             * asociados a la pregunta con el id anterior
                                             */
                                            if (i == 0)
                                            {
                                                pregRespIndv.push(idActual);
                                                pregRespIndv.push(preguntaActual);
                                                crearTabs(seccionActual, counter);
                                                seccionAux = seccionActual;
                                            }

                                            if (seccionActual != seccionAux)
                                            {
                                                crearTabs(seccionActual, counter);
                                                seccionAux = seccionActual;
                                            }


                                            // 1. Crear Array                                
                                            if (i == 0 || idActual === preguntaIdAux)
                                            {
                                                preguntaIdAux = idActual;
                                                preguntaAux = dataArray[i].pregunta;

                                                //Se agrega la respuesta y su total al array que almacena el par [respuesta,total]
                                                respuestas.push(respuestaActual);
                                                respuestas.push(dataArray[i].totalRespuesta);

                                                //Se agrega al array de la pregunta el par creado en el paso anterior
                                                totalRespuestas.push(respuestas);
                                                respuestas = [];

                                            } else
                                            {
                                                // 2. Crear div
                                                drawDiv(preguntaIdAux, seccionActual, preguntaAux);

                                                //3. Pasar datos a función que crea la gráfica
                                                drawChart(preguntaAux, totalRespuestas, preguntaIdAux);

                                                preguntaIdAux = idActual;
                                                preguntaAux = preguntaActual;

                                                /*Se agrega al segundo elemento de pregRespIndv, el array que contiene todos los pares [respuesta,total]
                                                 * asociados a la pregunta con id almacenado en la primer posición 
                                                 */
                                                pregRespIndv.push(totalRespuestas);

                                                //Se agrega al array que almacena toda la información de las preguntas con sus respectivas respuestas el array [[id_pregunta, stringPregunta, [pregRespIndv]]]
                                                preguntaRespuestas.push(pregRespIndv);

                                                pregRespIndv = [];
                                                totalRespuestas = [];

                                                pregRespIndv.push(idActual);
                                                pregRespIndv.push(preguntaActual);

                                                //Se agrega la respuesta y su total al array que almacena el par [respuesta,total]
                                                respuestas.push(dataArray[i].respuesta);
                                                respuestas.push(dataArray[i].totalRespuesta);

                                                //Se agrega al array de la pregunta el par creado en el paso anterior
                                                totalRespuestas.push(respuestas);
                                                respuestas = [];

                                            }

                                        }
                                    })
                                    ;
                        }
                    });

                    function crearTabs(idSeccion, tabActivo)
                    {
                        switch (idSeccion)
                        {
                            case 5:
                                var txtNavBar = "<li onclick='tabs(" + idSeccion + ")' class='active' id='1'><a href=\"#" + idSeccion + "-tab\" data-toggle=\"tab\"><img id='salud1' src='/resources/img/ese/Paso1-active.png' class='hidden-xs'><i class=\"fa\"></i></a></li>";
                                break;
                            case 6:
                                var txtNavBar = "<li onclick='tabs(" + idSeccion + ")' id='2'><a href=\"#" + idSeccion + "-tab\" data-toggle=\"tab\"><img id='salud2' src='/resources/img/ese/Paso2.png' class='hidden-xs'><i class=\"fa\"></i></a></li>";
                                break;
                            case 7:
                                var txtNavBar = "<li onclick='tabs(" + idSeccion + ")' id='3'><a href=\"#" + idSeccion + "-tab\" data-toggle=\"tab\"><img id='salud3' src='/resources/img/ese/Paso3.png' class='hidden-xs'><i class=\"fa\"></i></a></li>";
                                break;
                        }
                        $("#navBar").append(txtNavBar);
                        $('#contenido-tab').append("<div class=\"tab-pane fade" + (tabActivo === 0 ? "content active\"" : "\"") + " id=\"" + idSeccion + "-tab\">");
                        counter++;
                    }


                    //Agrega las imagenes a los tabs
                    function tabs(id)
                    {
                        switch (id) {
                            case 5:
                                $("#salud1").attr("src", "/resources/img/ese/Paso1-active.png");
                                $("#salud2").attr("src", "/resources/img/ese/Paso2.png");
                                $("#salud3").attr("src", "/resources/img/ese/Paso3.png");
                                break;
                            case 6:
                                $("#salud1").attr("src", "/resources/img/ese/Paso1.png");
                                $("#salud2").attr("src", "/resources/img/ese/Paso2-active.png");
                                $("#salud3").attr("src", "/resources/img/ese/Paso3.png");
                                break;
                            case 7:
                                $("#salud1").attr("src", "/resources/img/ese/Paso1.png");
                                $("#salud2").attr("src", "/resources/img/ese/Paso2.png");
                                $("#salud3").attr("src", "/resources/img/ese/Paso3-active.png");
                                break;
                        }
                    }

                    function drawChart(titulo, respuestas, div)
                    {
                        var data = new google.visualization.DataTable();
                        data.addColumn('string', 'Respuesta');
                        data.addColumn('number', 'Total');
                        for (k = 0; k < respuestas.length; k++)
                        {
                            respuestas[k][0] = respuestas[k][0] + " (" + respuestas[k][1].toString() + ")";
                            data.addRow(respuestas[k]);
                        }

                        var options = {
                            title: titulo,
                            legend: {position: 'bottom', textStyle: {color: '#8f9091'}},
                            is3D: true,
                            colors: ['#3BB7B1', '#E56385', '#ED975D', '#720026', '#E9724C', '#FFF05A', '#7CEA9C', '#6FC9F0', '#2E5EAA', '#5B4E77', '#7699D4']
                        };
                        var chart = new google.visualization.PieChart(document.getElementById("d_" + div));

                        //Funcionalidad para obtener una imagen por gráfica, para agregarla al reporte PDF!
                        google.visualization.events.addListener(chart, 'ready', function () {
                            var content = '<img src="' + chart.getImageURI() + '">';
                            $('#graph-images').append(content);
                        });

                        chart.draw(data, options);
                        $(".rowGrafica").show();
                    }

                    function drawDiv(preguntaid, seccion, pregunta)
                    {
                        divGrafica = "";
                        divGrafica += "<div id=\"d_" + preguntaid + "\" class=\"project-box-content grafica\" ></div>";
                        divGrafica += "<br/>";
                        divGrafica += "<div class=\"project-box-ultrafooter clearfix\">";
                        divGrafica += "<p class=\"link pull-right padd link'\" onclick=\"modalTablaGrafica(" + preguntaid + "\)\" style=\"color: #0081c1\">";
                        divGrafica += "Visualizar información en forma de tabla <i class=\"fa fa-arrow-circle-right fa-lg\"></i>";
                        divGrafica += "</p>";
                        divGrafica += "</div>";
                        divGrafica += "<br/>";

                        var seccionString = seccion + "-tab";
                        $('#' + seccionString).append(divGrafica);
                        $('#contenido-tab').append("</div>");
                    }

                    function modalTablaGrafica(pregId) {
                        datosAux = [];
                        datosAux = encuentraPregunta(pregId);
                        ModalGenerator.notificacion({
                            titulo: datosAux[1],
                            cuerpo: crearTablaGrafica(datosAux),
                            tipo: "SUCCESS"
                        });
                    }

                    function encuentraPregunta(preg)
                    {
                        for (x in preguntaRespuestas)
                        {
                            if (preguntaRespuestas[x][0] === preg)
                                return preguntaRespuestas[x];
                        }
                    }

                    function crearTablaGrafica(array) {
                        var j = 0;
                        var table = "<table class='table table-striped table-hover'><thead>";
                        table += "<th class='text-left'><strong>RESPUESTA</strong></th>";
                        table += "<th class='text-left'><strong>TOTAL</strong></th></tr>";
                        for (j = 2; j < array.length; j++)
                        {
                            for (a = 0; a < array[j].length; a++)
                            {
                                table += "<tr>";
                                for (b in array[j][a])
                                {
                                    table += "<td class='text-left'>" + array[j][a][b] + "</td>";
                                }
                                table += "</tr>";
                            }
                        }
                        table += "</tbody></table>";
                        return table;
                    }

                    function generatePDF() {
                        var d = new Date();
                        var n = d.toLocaleString();//.substring(0, d.toLocaleString().indexOf(' '));
                        var doc = new jsPDF('p', 'pt', 'a4', false); /* Creates a new Document*/
                        doc.setFontSize(10); /* Define font size for the document */
                        var yAxis = 30;
                        var imageTags = $('#graph-images img');
                        for (var i = 0; i < imageTags.length; i++) {
                            if (i % 4 == 0 && i != 0) { /* I want only two images in a page */
                                doc.addPage();  /* Adds a new page*/
                                yAxis = 30; /* Re-initializes the value of yAxis for newly added page*/
                            }
                            doc.text('SIBec | Censo de Salud (' + periodoClave + ')', 350, 30);
                            doc.text('' + n + '', 460, 810);
                            yAxis = yAxis + 20; /* Update yAxis */
                            doc.addImage(imageTags[i], 'png', 40, yAxis, 530, 140, undefined, 'none');
                            yAxis = yAxis + 150; /* Update yAxis */
                        }
                        doc.save('SIBec - Censo de Salud (' + periodoClave + ') .pdf'); /* Prompts user to save file on his/her machine */
                    }

    </script>
</content>
