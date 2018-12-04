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
                google.charts.load('current', {packages: ['corechart', 'bar']});

                $('.fancybox').fancybox({
                    autoSize: true,
                    afterClose: function () {
                        reload();
                    }
                });

                $('#listadoTodo').hide();
                $('#listadoGenero').hide();
                reload();
                recarga();
            });

            function reload() {
                $('#listadoTodo').show();
                $('#grafica').show();
                $('#msj').hide();
                if ($.fn.dataTable.isDataTable('#listadoTodo')) {
                    table = $('#listadoTodo').DataTable();
                    table.destroy();
                }

                google.charts.setOnLoadCallback(drawChart);
                function drawChart() {
                    var data = new google.visualization.DataTable();
                    data.addColumn('string', 'NIVEL');
                    data.addColumn('number', 'ALUMNOS');

                    data.addRow(['Altas', 45666]);
                    data.addRow(['Bajas', 45666]);
                    data.addRow(['Transferencias', 45666]);

                    var options = {
                        title: 'OTORGAMIENTOS POR PROCESO',
                        pieHole: 0.4,
                        chartArea: {width: '95%'},
                        height: 500,
                        legend: {position: 'top'}
                    };

                    var chart = new google.visualization.PieChart(document.getElementById('grafica'));
                    chart.draw(data, options);
                }
            }
        </script>
    </head>
    <body>
        <div class="container">
            <h1>Estadísticas por proceso</h1>
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
                <div class="form-group col-sm-12">
                    <label class="col-sm-5 control-label text-right">
                        Periodo
                    </label>
                    <div class="col-sm-7">
                        <s:select id="periodo" name="periodo"
                                  cssClass="form-control"
                                  list="ambiente.periodoList" 
                                  listKey="id" listValue="clave"
                                  headerKey="" onChange = "reload()"/>
                    </div>
                </div>
                <div class="form-group col-sm-12">
                    <label class="col-sm-5 control-label text-right">
                        Unidad Académica
                    </label>
                    <div class="col-sm-7">
                        <s:select id="unidadAcademica" name="unidadAcademica"
                                  cssClass="form-control"
                                  list="ambiente.unidadAcademicaList"
                                  listKey="id" listValue="nombreCorto" 
                                  headerKey=""/>
                    </div>
                </div>
            </div>

            <div class="clearfix" >&nbsp;</div>

            <div class="row main-box">
                <div class="col-sm-12">
                    <div class="col-sm-6">
                        <table id="listadoTodo" class="stripe table-hover display responsive  dataTable no-footer ">
                            <thead>
                                <tr>
                                    <th>Proceso</th>
                                    <th>Alumnos</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <th>Altas</th>
                                    <th><a>45666</a></th>
                                </tr>
                                <tr>
                                    <th>Bajas</th>
                                    <th><a>45666</a></th>
                                </tr>
                                <tr>
                                    <th>Transferencias</th>
                                    <th><a>45666</a></th>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <div id="grafica" class="col-sm-6"></div>
                </div>
                <div class="col-sm-12">
                    <div id="detalle"></div>
                </div>
            </div>    
        </div>
        <s:hidden id="datos" name="datos" />
        <s:hidden id="est" name="est" />
        <script type="text/javascript" language="javascript">
            function recarga() {
                google.charts.setOnLoadCallback(drawBasic);

                function drawBasic() {

                    var data = new google.visualization.DataTable();
                    data.addColumn('date', 'FECHA');
                    data.addColumn('number', 'OTORGAMIENTOS');

                    data.addRows([
                        [new Date(2016, 10, 7), 459],
                        [new Date(2016, 10, 8), 356],
                        [new Date(2016, 10, 9), 450],
                        [new Date(2016, 10, 10), 698],
                        [new Date(2016, 10, 11), 854],
                        [new Date(2016, 10, 12), 1547],
                        [new Date(2016, 10, 13), 165],
                        [new Date(2016, 10, 14), 1089],
                        [new Date(2016, 10, 15), 1958],
                    ]);

                    var options = {
                        title: 'OTORGAMIENTOS PROCESO POR FECHA',
                        height: 650,
                        chartArea: {width: '90%'},
                        hAxis: {
                            format: 'd/M/yy',
                            gridlines: {count: 9}
                        },
                        legend: {position: 'none'}
                    };

                    var chart = new google.visualization.ColumnChart(
                            document.getElementById('detalle'));

                    chart.draw(data, options);
                }
            }
        </script>
    </body>
</html>