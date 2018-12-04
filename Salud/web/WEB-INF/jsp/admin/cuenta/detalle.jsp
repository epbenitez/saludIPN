<%-- 
    Document   : Detalle Solicitud Cuenta
    Created on : 07-Abril-2016
    Author     : Rafael Cárdenas Reséndiz
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Detalle de la Solicitud de Cuentas</title>
</head> 

<content tag="tituloJSP">
    Detalle de la Solicitud de Cuentas
</content>

<body>
    <div class="row">
        <div class="col-xs-12">
            <div class="main-box clearfix" >
                <div class="main-box-body clearfix">
                    <div class="col-xs-6">
                        <form class="form-horizontal">
                            <div class="col-xs-12 form-group">
                                <label class="col-xs-5 control-label">Identificador</label>
                                <div class="col-xs-7">
                                    <s:textfield cssClass="form-control" name="solicitud.identificador" id="identificador" placeholder="Identificador" readonly="true"/>
                                </div>
                            </div>
                            <div class="col-xs-12 form-group">
                                <label class="col-xs-5 control-label">Periodo generación</label>
                                <div class="col-xs-7">
                                    <s:textfield cssClass="form-control" name="solicitud.periodoGeneracion.clave" id="periodo" placeholder="Periodo" readonly="true"/>
                                </div>
                            </div>
                            <div class="col-xs-12 form-group">
                                <label class="col-xs-5 control-label">Usuario generación</label>
                                <div class="col-xs-7">
                                    <s:textfield cssClass="form-control" name="solicitud.usuarioGeneracion.usuario" id="usuario" placeholder="Usuario" readonly="true"/>
                                </div>
                            </div>
                            <div class="col-xs-12 form-group">
                                <label class="col-xs-5 control-label">Fecha generación</label>
                                <div class="col-xs-7">
                                    <s:textfield cssClass="form-control" name="solicitud.fechaGeneracion" id="fechaGeneracion" placeholder="fechaGeneracion" readonly="true"/>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="col-xs-6">
                        <div id="piechart" style="width: 400px; height: 300px;"></div>
                    </div>   
                </div>
            </div> 
        </div>
    </div>
    <div class="row" id="rowTable" style="display: none;">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <div class="col-md-12" style="padding: 0px">
                    <div class="responsive">                        
                        <table id="table" class="table table-striped table-hover dt-responsive" style="width: 100%">
                            <thead>
                                <tr>
                                    <th data-default-order="true" data-default-dir="asc">Unidad Académica</th>
                                    <th>Boleta</th>
                                    <th>Nombre</th>
                                    <th>Tarjeta/Cuenta</th>
                                    <th>Tipo</th>
                                    <th>Estatus</th>
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
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js"
            data-init="true" data-div="rowTable" data-length="100"
            data-button="botonDescargar"  data-function="despuesCargarTabla"
            data-ajax="/ajax/detalleCuentasAjax.action?solicitudId=<s:property value="solicitud.id"/>"></script>
</content>


<content tag="inlineScripts">
    <script>
        google.charts.load('current', {'packages': ['corechart']});
        google.charts.setOnLoadCallback(drawChart);
        
        var botonDescargar = ["excel"];

        function drawChart() {
            var data = google.visualization.arrayToDataTable([
                ['Estatus de la solicitud', 'No. de cuentas'],
        <s:property value="datosGrafica"/>
            ]);

            var options = {
                title: 'Estadisticas de la solicitud'
            };

            var chart = new google.visualization.PieChart(document.getElementById('piechart'));

            chart.draw(data, options);
        }

        function despuesCargarTabla() {
            parent.$.fancybox.update();
        }
    </script>
</content>
