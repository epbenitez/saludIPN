<%@taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.0//EN" "http://www.w3.org/TR/REC-html40/strict.dtd">
<html>
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />

        <title>Estadísticas</title>

        <!-- bootstrap -->
        <link rel="stylesheet" type="text/css" href="/vendors/bootstrap/bootstrap.min-3.3.7.css" />

        <!-- global styles -->
        <link rel="stylesheet" type="text/css" href="/resources/css/theme_styles.min.css" />

        <!-- libraries -->
        <link rel="stylesheet" type="text/css" href="/vendors/font-awesome/font-awesome.min-4.6.3.css" />

        <!-- Favicon -->
        <link type="image/x-icon" href="/favicon.png" rel="shortcut icon" />

        <!-- Struts utils -->
        <link rel="stylesheet" href="/vendors/struts/styles.min.css" type="text/css">

        <!-- google font libraries -->
        <link href='https://fonts.googleapis.com/css?family=Open+Sans:400,600,700,300|Titillium+Web:200,300,400' rel='stylesheet' type='text/css'>

        <!-- this page specific styles -->
        <link rel="stylesheet" type="text/css" href="/resources/css/font_login.min.css" />
        <link rel="stylesheet" type="text/css" href="/resources/css/login.min.css" />
        <link rel="stylesheet" type="text/css" href="/vendors/nanoscroller/nanoscroller.min.css" />                
        <link rel="stylesheet" type="text/css" href="/vendors/fancy-box/jquery.fancybox.min.css" />
    </head>

    <body>
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="row">
                        <div class="col-xs-12">
                            <header>
                                <h1 id="titulo"></h1>
                            </header>
                            <div class="row">
                                <div class="col-xs-12">
                                    <div class="main-box clearfix">
                                        <div class="row">
                                            <!-- Espacio para errores -->
                                            <div class="col-xs-12">
                                                <s:if test="hasActionErrors()">
                                                    <div class="alert alert-danger">
                                                        <i class="fa fa-times-circle fa-fw fa-lg"></i>
                                                        <strong>¡Error!</strong> <s:actionerror/>
                                                    </div>
                                                </s:if>
                                                <s:if test="hasActionMessages()">
                                                    <div class="alert alert-success">
                                                        <i class="fa fa-check-circle fa-fw fa-lg"></i>
                                                        <strong>¡Realizado!</strong> <s:actionmessage />
                                                    </div>
                                                </s:if>   
                                            </div>
                                        </div>
                                        <div class="row">
                                            <!-- Espacio para errores -->
                                            <div class="col-xs-12">
                                                <div class="alert alert-warning fade in" id="msj" style="display: none;">
                                                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                                                    <i class="fa fa-warning fa-fw fa-lg"></i>
                                                    No hay alumnos de nivel medio superior que cumplan con los parámetros de búsqueda.
                                                </div>
                                                <div class="alert alert-warning fade in" id="msj2" style="display: none;">
                                                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                                                    <i class="fa fa-warning fa-fw fa-lg"></i>
                                                    No hay alumnos de nivel superior que cumplan con los parámetros de búsqueda.
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="main-box-body clearfix">
                                                <div class="col-xs-12">
                                                    <div id="detalle"></div>                                                
                                                    <div id="detalle2"></div>
                                                </div>
                                                <s:hidden name="est" />
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

        <script src="/vendors/jQuery/jquery-3.1.1.min.js"></script>
        <script src="/vendors/bootstrap/bootstrap-3.3.7.min.js"></script>
        <script src="/vendors/nanoscroller/jquery.nanoscroller-0.8.7.min.js"></script>
        <script src="/resources/js/scripts.min.js" type="text/javascript"></script>       

        <script type="text/javascript" src="/resources/js/loader.js"></script>
        <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>

        <!-- this page specific inline scripts -->
        <script>
            $(document).ready(function () {
                google.charts.load("current", {packages: ["corechart"]});
                reload();
            });

            function reload() {
                $('#detalle').show();

                var est = $("#est").val();
                var url;
                switch (est) {
                    case "1":
                        url = "/ajax/getDatosInscritosEstadisticasAjax.action?est=1&";
                        $("#titulo").html("Alumnos inscritos por UA");
                        break;
                    case "2":
                        url = "/ajax/getDatosInscritosEstadisticasAjax.action?est=2&";
                        $("#titulo").html("Alumnos con Solicitud de Beca");
                        break;
                    case "3":
                        url = "/ajax/getDatosInscritosEstadisticasAjax.action?est=3&";
                        $("#titulo").html("Alumnos con Solicitud de Beca ordinaria");
                        break;
                    case "4":
                        url = "/ajax/getDatosInscritosEstadisticasAjax.action?est=4&";
                        $("#titulo").html("Alumnos con Solicitud de Beca de transporte");
                        break;
                    case "5":
                        url = "/ajax/getDatosInscritosEstadisticasAjax.action?est=5&";
                        $("#titulo").html("Alumnos con Solicitud de Beca pendiente");
                        break;
                    case "6":
                        url = "/ajax/getDatosInscritosEstadisticasAjax.action?est=6&";
                        $("#titulo").html("Alumnos con Solicitud de Beca en espera");
                        break;
                    case "7":
                        url = "/ajax/getDatosInscritosEstadisticasAjax.action?est=7&";
                        $("#titulo").html("Alumnos con Solicitud de Beca rechazadas");
                        break;
                    case "8":
                        url = "/ajax/getDatosInscritosEstadisticasAjax.action?est=8&";
                        $("#titulo").html("Alumnos con Solicitud de Beca asignadas");
                        break;
                    case "9":
                        url = "/ajax/getDatosInscritosEstadisticasAjax.action?est=9&";
                        $("#titulo").html("Alumnos con Censo de salud completo");
                        break;
                    case "10":
                        url = "/ajax/getDatosInscritosEstadisticasAjax.action?est=10&";
                        $("#titulo").html("Alumnos con otorgamiento");
                        break;
                    case "11":
                        url = "/ajax/getDatosInscritosEstadisticasAjax.action?est=11&";
                        $("#titulo").html("Alumnos con baja");
                        break;
                    case "12":
                        url = "/ajax/getDatosInscritosEstadisticasAjax.action?est=12&";
                        $("#titulo").html("Alumnos con Beca Universal");
                        break;
                    case "13":
                        url = "/ajax/getDatosInscritosEstadisticasAjax.action?est=13&";
                        $("#titulo").html("Alumnos Pendiente Beca de Manutención");
                        break;
                    case "14":
                        url = "/ajax/getDatosInscritosEstadisticasAjax.action?est=14&";
                        $("#titulo").html("Alumnos Pendiente Otras Becas");
                        break;
                    default:
                        url = "/ajax/getDatosInscritosEstadisticasAjax.action?est=1&";
                        $("#titulo").html("Alumnos inscritos por UA");
                }

                google.charts.setOnLoadCallback(NMS);
                google.charts.setOnLoadCallback(NS);
                function NMS() {
                    $.ajax({
                        type: 'POST',
                        url: url + 'nivel=1',
                        dataType: 'json',
                        cache: false,
                        success: function (aData) {
                            var datos = aData.data[0];
                            var tem = datos.toString().split("/");

                            var data = new google.visualization.DataTable();
                            data.addColumn('string', 'UA');
                            data.addColumn('number', 'ALUMNOS');

                            if (est == "2" || est == "10" || est == "11") {
                                data.addColumn({type: 'string', role: 'tooltip'});
                            }

                            var i = 0
                            for (i; i < tem.length - 1; i++) {
                                var ax = tem[i].split(",");
                                var a1 = String(ax[0]);
                                var a2 = Number(ax[1]);
                                if (est == "2" || est == "10" || est == "11") {
                                    var a3 = String(ax[2]);
                                    data.addRow([a1, a2, a3]);
                                } else {
                                    data.addRow([a1, a2]);
                                }
                            }

                            var options = {
                                title: 'NIVEL MEDIO SUPERIOR',
                                chartArea: {width: '65%', height: '87%'},
                                height: i * 25,
                                legend: {position: 'none'},
                                vAxis: {
                                    textStyle: {
                                        fontSize: 11
                                    }
                                }
                            };

                            var chart = new google.visualization.BarChart(document.getElementById('detalle'));
                            chart.draw(data, options);
                        }
                        ,
                        error: function () {
                            $('#msj').show();
                            ModalGenerator.notificacion({
                                "titulo": "Atención",
                                "cuerpo": "No hay alumnos de nivel medio superior que cumplan con los parámetros de búsqueda.",
                                "tipo": "ALERT",
                            });
                        }
                    });
                }

                function NS() {
                    $.ajax({
                        type: 'POST',
                        url: url + 'nivel=2',
                        dataType: 'json',
                        cache: false,
                        success: function (aData) {
                            var datos = aData.data[0];
                            var tem = datos.toString().split("/");

                            var data = new google.visualization.DataTable();
                            data.addColumn('string', 'UA');
                            data.addColumn('number', 'ALUMNOS');

                            if (est == "2" || est == "10" || est == "11") {
                                data.addColumn({type: 'string', role: 'tooltip'});
                            }

                            var i = 0
                            for (i; i < tem.length - 1; i++) {
                                var ax = tem[i].split(",");
                                var a1 = String(ax[0]);
                                var a2 = Number(ax[1]);
                                if (est == "2" || est == "10" || est == "11") {
                                    var a3 = String(ax[2]);
                                    data.addRow([a1, a2, a3]);
                                } else {
                                    data.addRow([a1, a2]);
                                }
                            }

                            var options = {
                                title: 'NIVEL SUPERIOR',
                                chartArea: {width: '65%', height: '87%'},
                                height: i * 25,
                                legend: {position: 'none'},
                                colors: ['#7BC8A4'],
                                vAxis: {
                                    textStyle: {
                                        fontSize: 11
                                    }
                                }
                            };

                            var chart = new google.visualization.BarChart(document.getElementById('detalle2'));
                            chart.draw(data, options);
                        },
                        error: function () {
                            $('#msj2').show();
                            ModalGenerator.notificacion({
                                "titulo": "Atención",
                                "cuerpo": "No hay alumnos de nivel superior que cumplan con los parámetros de búsqueda.",
                                "tipo": "ALERT",
                            });
                        }
                    });
                }
            }
        </script>
    </body>

</html>