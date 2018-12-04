<%-- 
    Document   : Template SIBEC
    Created on : 28-Septiembre-2016
    Author     : Gustavo Adolfo Alamillo Medina
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Detalle de la orden de depósito</title>
    <script type="text/javascript" src="/resources/js/loader.js"></script>
    <script type="text/javascript">
        google.charts.load('current', {'packages': ['corechart']});
        google.charts.setOnLoadCallback(drawChart);
        function drawChart() {
            var data = google.visualization.arrayToDataTable([
                ['Estatus del deposito', 'No. de depositos'],
        <s:property value="datosGrafica"/>
            ]);
            var options = {
                title: 'Estatus del deposito',
                is3D: true
            };
            var chart = new google.visualization.PieChart(document.getElementById('piechart'));
            chart.draw(data, options);
        }
    </script>
</head> 

<content tag="tituloJSP">
    Detalle de la orden de depósito
</content>

<body>
    <div class="row">
        <div class="col-xs-12">
            <div class="main-box clearfix" >
                <div class="main-box-body clearfix">
                    <div class="col-sm-6">
                        <form class="form-horizontal">
                            <div class="col-xs-12 form-group">
                                <label class="col-xs-5 control-label">Periodo</label>
                                <div class="col-xs-7">
                                    <s:textfield cssClass="form-control" name="ordenDeposito.periodo.descripcion" id="periodo" placeholder="Periodo" readonly="true"/>
                                </div>
                            </div>
                            <div class="col-xs-12 form-group">
                                <label class="col-xs-5 control-label">Mes</label>
                                <div class="col-xs-7">
                                    <s:textfield cssClass="form-control" name="ordenDeposito.nombreMes" id="nombreMes" placeholder="Mes" readonly="true"/>
                                </div>
                            </div>
                            <div class="col-xs-12 form-group">
                                <label class="col-xs-5 control-label">Unidad Académica</label>
                                <div class="col-xs-7">
                                    <s:textfield cssClass="form-control" name="ordenDeposito.unidadAcademica.nombre" id="unidadAcademica" placeholder="Unidad Académica" readonly="true"/>
                                </div>
                            </div>
                            <div class="col-xs-12 form-group">
                                <label class="col-xs-5 control-label">Programa</label>
                                <div class="col-xs-7">
                                    <s:textfield cssClass="form-control" name="ordenDeposito.programaBeca.nombre" id="programaBeca" placeholder="Programa" readonly="true"/>
                                </div>
                            </div>
                            <div class="col-xs-12 form-group">
                                <label class="col-xs-5 control-label">Tipo de proceso</label>
                                <div class="col-xs-7">
                                    <s:textfield cssClass="form-control" name="ordenDeposito.tipoProceso.nombre" id="tipoProceso" placeholder="Tipo de proceso" readonly="true"/>
                                </div>
                            </div>
                            <div class="col-xs-12 form-group">
                                <label class="col-xs-5 control-label">Tipo de deposito</label>
                                <div class="col-xs-7">
                                    <s:textfield cssClass="form-control" name="ordenDeposito.tipoDeposito.nombre" id="tipoProceso" placeholder="Tipo de deposito" readonly="true"/>
                                </div>
                            </div>
                            <div class="col-xs-12 form-group">
                                <label class="col-xs-5 control-label">Total</label>
                                <div class="col-xs-7">
                                    <s:textfield cssClass="form-control" name="ordenDeposito.conteo" id="conteo" placeholder="Total" readonly="true"/>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="col-sm-1">
                        <div id="piechart" style="width: 450px; height: 450px;"></div>
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
                                        <th data-default-order="true" data-default-dir="desc">Unidad Académica</th>
                                        <th>Programa</th>
                                        <th>Becarios</th>
                                        <!--<th>Pagos</th>-->
                                        <th>Importe</th>
                                    </tr>
                                </thead>
                            </table>                        
                        </div>
                    </div>
                </div>
            </div>                
        </div>
    </div>
</body>

<content tag="endScripts">
    <!--La biblioteca jszip será necesaria cuando se utilice un botón de tipo 'excel'-->
    <script type="text/javascript" src="/vendors/jszip/jszip.min.js"></script>
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js"
            data-init="true" data-div="rowTable" data-length="100"
            data-button="botonDescargar"  data-function="despuesCargarTabla"
    data-ajax="/ajax/detalleOrdenesDepositoAjax.action?noOrden=<s:property value="ordenId"/>"></script>
</content>

<content tag="inlineScripts">
    <script>
        var botonDescargar = ["excel"];

        function despuesCargarTabla() {
            parent.$.fancybox.update();
        }
    </script>
</content>
