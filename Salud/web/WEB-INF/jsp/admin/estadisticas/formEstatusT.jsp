<%@taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.0//EN" "http://www.w3.org/TR/REC-html40/strict.dtd">
<html>
    <head>
        <title>Estadísticas</title>
        <!-- Add fancyBox main JS and CSS files -->
        <script type="text/javascript" src="/resources/js/fancybox/source/jquery.fancybox.js?v=2.1.5"></script>
        <link rel="stylesheet" type="text/css" href="/resources/js/fancybox/source/jquery.fancybox.css?v=2.1.5" media="screen" />
        <!-- Add Button helper (this is optional) -->
        <link rel="stylesheet" type="text/css" href="/resources/js/fancybox/source/helpers/jquery.fancybox-buttons.css?v=1.0.5" />
        <script type="text/javascript" src="/resources/js/fancybox/source/helpers/jquery.fancybox-buttons.js?v=1.0.5"></script>
        <!-- Add Thumbnail helper (this is optional) -->
        <link rel="stylesheet" type="text/css" href="/resources/js/fancybox/source/helpers/jquery.fancybox-thumbs.css?v=1.0.7" />
        <script type="text/javascript" src="/resources/js/fancybox/source/helpers/jquery.fancybox-thumbs.js?v=1.0.7"></script>
        <!-- Add Media helper (this is optional) -->
        <script type="text/javascript" src="/resources/js/fancybox/source/helpers/jquery.fancybox-media.js?v=1.0.6"></script>
        <!--GOOGLE CHARTS-->
        <script type="text/javascript" src="/resources/js/loader.js"></script>
        <script type="text/javascript" language="javascript" class="init">
            $(document).ready(function () {
                google.charts.load("current", {packages: ["corechart"]});

                $('.fancybox').fancybox({
                    autoSize: true,
                    afterClose: function () {
                        reload();
                    }
                });

                $('#listadoTodo').hide();
                $('#listadoGenero').hide();
                $('#msj').hide();
                reload();
            });

            function reload() {
                $('#listadoTodo').show();
                $('#grafica').show();
                $('#msj').hide();
                if ($.fn.dataTable.isDataTable('#listadoTodo')) {
                    var table = $('#listadoTodo').DataTable();
                    table.destroy();
                }

                var url = '/ajax/listadoTodoEstadisticasAjax.action?periodo=' + $('#periodo option:selected').val() + '&est=' + $('#est').val();

                var listadoDT = $('#listadoTodo').DataTable({
                    "ajax": url,
                    "initComplete": function () {
                        if (!listadoDT.data().count()) {
                            $('#listadoTodo').hide();
                            $('#listadoGenero').hide();
                            $('#grafica').hide();
                            $('#detalle').hide();
                            $('#msj').show();
                        }
                    },
                    //opciones ocultadas
                    "pageLength": 50,
                    "bFilter": false,
                    "bLengthChange": false,
                    "bPaginate": false,
                    "bSort": false,
                    "bInfo": false
                });

                google.charts.setOnLoadCallback(drawChart);
                function drawChart() {
                    $.ajax({
                        type: 'POST',
                        url: '/ajax/getDatosTodoEstadisticasAjax.action',
                        dataType: 'json',
                        data: {periodo: $('#periodo option:selected').val()},
                        cache: false,
                        success: function (aData) {
                            var datos = aData.data[0];
                            var tem = datos.toString().split("/");

                            var data = new google.visualization.DataTable();
                            data.addColumn('string', 'NIVEL');
                            data.addColumn('number', 'ALUMNOS');

                            for (var i = 0; i < tem.length - 1; i++) {
                                var ax = tem[i].split(",");
                                var a1 = String(ax[0]);
                                var a2 = Number(ax[1]);
                                data.addRow([a1, a2]);
                            }

                            var options = {
                                title: 'BECARIOS - TIPO DE BECA',
                                pieHole: 0.4,
                                chartArea: {width: '95%'},
                                height: 500,
                                legend: {position: 'top'}
                            };

                            var chart = new google.visualization.PieChart(document.getElementById('grafica'));
                            chart.draw(data, options);
                        },
                        error: function () {
                            BootstrapDialog.alert({
                                title: 'Atención',
                                message: 'Hubo un problema que impidió que se completara la operación.',
                                type: BootstrapDialog.TYPE_DANGER
                            });
                        }
                    });
                }
            }
        </script>
    </head>
    <body>
        <div class="container">
            <h1>Estadísticas estatus de tarjetas</h1>
            <div class="row">
                <div class="col-sm-12">
                    <s:if test="hasActionErrors()">
                        <div class="alert alert-danger">
                            <i class="fa fa-times-circle fa-fw fa-lg"></i>
                            <strong>&iexcl;Error!</strong> <s:actionerror/>
                        </div>
                    </s:if>
                    <s:if test="hasActionMessages()">
                        <div class="alert alert-success">
                            <i class="fa fa-check-circle fa-fw fa-lg"></i>
                            <strong>&iexcl;Realizado!</strong> <s:actionmessage />
                        </div>
                    </s:if>
                </div>
            </div>

            <div class="clearfix" >&nbsp;</div>

            <div class="col-sm-6  main-box">
                <div class="clearfix" >&nbsp;</div>
                <form class="form-horizontal" >
                    <div class="form-group">
                        <label class="col-sm-5 control-label text-right">
                            Periodo
                        </label>
                        <div class="col-sm-6">
                            <s:select id="periodo" name="periodo"
                                      cssClass="form-control"
                                      list="ambiente.periodoList" 
                                      listKey="id" listValue="clave"
                                      headerKey="" onChange = "reload()"/>
                        </div>
                    </div>
                </form>
            </div>

            <div class="clearfix" >&nbsp;</div>

            <div class="row main-box">
                <div class="col-sm-6">
                    <table id="listadoTodo" class="stripe table-hover display responsive ">
                        <thead>
                            <tr>
                                <th>Nivel</th>
                                <th>Alumnos</th>
                            </tr>
                        </thead>
                    </table>
                    <div class="clearfix" >&nbsp;</div>
                    <div id="grafica"></div>
                </div>
                <div class="col-sm-6">
                    <table id="listadoGenero" class="stripe table-hover display responsive ">
                        <thead>
                            <tr>
                                <th>Estatus</th>
                                <th>Tarjetas</th>
                            </tr>
                        </thead>
                    </table>
                    <div class="clearfix" >&nbsp;</div>
                    <div id="detalle"></div>
                </div>
                <div class="col-sm-12"><div class="alert alert-danger" id="msj">No hay datos de acuerdo a los parametros seleccionados</div></div>
            </div>    
        </div>
        <s:hidden id="datos" name="datos" />
        <s:hidden id="est" name="est" />
        <script type="text/javascript" language="javascript">
            function recarga(periodo, idNivel) {
                $('#listadoGenero').show();
                $('#detalle').show();
                $('#msj').hide();
                if ($.fn.dataTable.isDataTable('#listadoGenero')) {
                    var table = $('#listadoGenero').DataTable();
                    table.destroy();
                }

                var url = '/ajax/listadoNivelEstatusTEstadisticasAjax.action?periodo=' + periodo + '&nivel=' + idNivel;

                $('#listadoGenero').DataTable({
                    "ajax": url,
                    //opciones ocultadas
                    "pageLength": 50,
                    "bFilter": false,
                    "bLengthChange": false,
                    "bPaginate": false,
                    "bSort": false,
                    "bInfo": false
                });

                google.charts.setOnLoadCallback(drawCharts);
                function drawCharts() {
                    $.ajax({
                        type: 'POST',
                        url: '/ajax/getDatosNivelEstatusTEstadisticasAjax.action', dataType: 'json',
                        data: {periodo: periodo, nivel: idNivel},
                        cache: false,
                        success: function (aData) {
                            var datos = aData.data[0];
                            var tem = datos.toString().split("/");

                            var data = new google.visualization.DataTable();
                            data.addColumn('string', 'ESTATUS');
                            data.addColumn('number', 'TARJETAS');

                            for (var i = 0; i < tem.length - 1; i++) {
                                var ax = tem[i].split(",");
                                var a1 = String(ax[0]);
                                var a2 = Number(ax[1]);
                                data.addRow([a1, a2]);
                            }

                            var options = {
                                title: 'ESTATUS TARJETAS',
                                pieHole: 0.4,
                                chartArea: {width: '95%'},
                                height: 500,
                                legend: {position: 'top'}
                            };

                            var chart = new google.visualization.PieChart(document.getElementById('detalle'));
                            chart.draw(data, options);
                        },
                        error: function () {
                            BootstrapDialog.alert({
                                title: 'Atención',
                                message: 'Hubo un problema que impidió que se completara la operación.',
                                type: BootstrapDialog.TYPE_DANGER
                            });
                        }
                    });
                }
            }
        </script>
    </body>
</html>