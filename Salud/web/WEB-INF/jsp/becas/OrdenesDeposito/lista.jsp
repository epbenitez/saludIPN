<%-- 
    Document   : lista
    Created on : 18-ene-2016, 10:50:07
    Author     : Victor Lozano
    Redesing   : Mario Márquez
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <link rel="stylesheet" type="text/css" href="/vendors/fancy-box/jquery.fancybox.min.css"/>      
    <style>
        td.details-control {
            cursor: pointer;
        }
    </style>
    <title>Ordenes de deposito</title>
</head> 

<content tag="tituloJSP">
    Órdenes de depósito
</content>

<content tag="tituloDerecho">
    <div class="pull-right hidden-xs">
        <s:iterator value="datosPivot">
            <div class="xs-graph pull-left" style="padding: 5px; padding-right: 10px;">
                <div class="graph-label">
                    <!--aqui se pone con una variable del iterator el parametro-->
                    <a href="/becas/descargaPendientesOrdenesDeposito.action?cuentaId=<s:property value="cuentaId" />" target="_blank">
                        <b><i class="fa fa-money"></i> <s:property  value="nombre" /></b> 
                    </a>
                </div>
                <div class="graph-content spark-<s:property  value="nombre" />">
                    <canvas width="150" height="25" style="display: inline-block; width: 150px; height: 25px; vertical-align: top; padding-right: 10px;"></canvas>
                </div>
            </div>
        </s:iterator>
        <s:if test="datosPivot!=null && !datosPivot.isEmpty()">
            <p style="color:#ccc; font-style: italic; text-align: center">Respuestas bancarias pendientes por tipo y periodo</p>
        </s:if>
    </div>
</content>
<body>
    <div class="row">
        <div class="col-xs-12">
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

    <div class="row">
        <div class="col-xs-12">
            <div class="main-box clearfix" >
                <div class="main-box-body clearfix">
                    <form class="form-horizontal">
                        <div class="form-group">
                            <label class="col-sm-1 control-label">Periodo</label>
                            <div class="col-sm-11">
                                <s:select 
                                    id="periodo" 
                                    name="periodo"
                                    cssClass="form-control"
                                    list="ambiente.periodoList" 
                                    listKey="id" 
                                    listValue="clave"
                                    headerKey="" 
                                    headerValue="-- Selecciona un periodo --"
                                    />
                            </div>
                        </div>                        
                    </form>
                </div>
            </div>
        </div>
    </div>
    <div class="row" id="referenciaBancaria" style="display: none;">
        <div class="col-xs-12">
            <div class="panel-group accordion" id="accordion">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
                                Carga de referencia Bancaria
                            </a>
                        </h4>
                    </div>
                </div>
                <div id="collapseOne" class="panel-collapse collapse">
                    <div class="panel-body">
                        <ul>
                            <li>
                                La carga de respuestas por "referencia bancaria" se realiza para los alumnos que tienen un otorgamiento en el periodo actual, 
                                pero que NO tienen una cuenta a la cual depositar.
                            </li>
                            <li>
                                La "referencia bancaria" normalmente corresponde a varios depósitos de distintos meses y de distintas becas de un mismo alumno. 
                            </li>
                            <li>
                                Al realizar la operación: "Carga de Archivo", el sistema buscará todas las referencias en el sistema, contenidas en el archivo cargado/seleccionado, 
                                y actualizará todos los depósitos al estatus de "Aplicado", asociados a dicha referencia.
                            </li>
                            <li>
                                Asegurese que el archivo a cargar cuente con el formato apropiado.
                            </li>
                        </ul>
                        <div class="col-xs-12 text-center" style="padding-bottom: 5px" >
                            <div class="col-xs-12 text-center" style="padding-bottom: 5px" >
                                <a href="#" onclick="addURL(this)" class="btn btn-primary fancybox fancybox.iframe solo-lectura" >
                                    Carga de Archivo &ensp; <i class='fa fa-arrow-circle-up'></i> 
                                </a>
                            </div>
                            <div class='btn-group'>
                                <button id = 'cuadro' type='button' class='btn btn-primary dropdown-toggle' data-toggle='dropdown' >Archivo ejemplo &ensp; <span class='caret'></span> </button> 
                                <ul class='dropdown-menu' role='menu'> 
                                    <li>
                                        <a title='Archivo de días anteriores' target="_blank" href="/resources/downloadable/Respuesta_Bancaria_Ant.txt">
                                            <i class='fa fa-download'></i>Archivo de días anteriores
                                        </a>
                                    </li> 
                                    <li>
                                        <a title='Archivo del mismo día' target="_blank" href="/resources/downloadable/Respuesta_Bancaria_Mismo.txt">
                                            <i class='fa fa-download'></i>Archivo del mismo día
                                        </a>
                                    </li>
                                </ul>
                            </div> 
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div> 
    <div class="row" id="divTabla2" style="display: none;">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <div class="col-md-12" style="padding: 0px">
                    <div class="responsive">                        
                        <table id="listadoTabla" class="table table-striped table-hover dt-responsive" style="width: 100%">
                            <thead>
                                <tr>
                                    <th data-hidden="true">Detalle</th>
                                    <th data-orderable="true">Orden</th>
                                    <th data-orderable="true">Fecha</th>
                                    <th data-hidden="true">Tipo deposito</th>
                                    <th data-hidden="true">Periodo</th>
                                    <th data-hidden="true">Mes</th>
                                    <th data-hidden="true">Nivel A.</th>
                                    <th data-orderable="true">Estatus</th>
                                    <th data-center="true"></th>
                                    <th data-center="true"></th>
                                    <th data-center="true"></th>
                                    <th data-center="true"></th>
                                    <th data-hidden="true">Unidad A.</th>
                                    <th data-hidden="true">Beca</th>
                                    <th data-orderable="true">Usuario</th>
                                    <th data-hidden="true">Proceso</th>
                                    <th data-hidden="true">Total</th>
                                    <th data-hidden="true">Id</th>
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
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js"></script>    
    <script type="text/javascript" src="/vendors/fancy-box/jquery.fancybox.min.js"></script>  
    <script src="/vendors/sparkline/jquery.sparkline.js" type="text/javascript"></script>
</content>

<content tag="inlineScripts">
    <script>
                                    $(document).ready(function () {
                                        $('#periodo').change(function () {
                                            $('#referenciaBancaria').hide();
                                            mostrarTabla();
                                        });

                                        /* SPARKLINE - graph in header */
                                        <s:iterator value="datosPivot">
                                        var values<s:property  value="nombre" /> = [<s:property  value="valoresStr" />];
                                        $('.spark-<s:property  value="nombre" />').sparkline(values<s:property  value="nombre" />, {
                                            type: 'bar',
                                            barColor: '#ced9e2',
                                            height: 35,
                                            barWidth: 10,
                                            tooltipFormat: '{{offset:offset}} : {{value}}',
                                            tooltipValueLookups: {
                                                'offset': {<s:property  value="valoresStrEjeX" />
                                                }
                                            }
                                        });
                                         </s:iterator>

                                    });

                                    function despuesCargarTabla() {
                                        $('#referenciaBancaria').show();
                                        $('.fancybox').fancybox({
                                            autoSize: true
                                        });
                                    }

                                    function mostrarTabla() {
                                        var url = '/ajax/listadoAdminOrdenesDepositoAjax.action?periodo=' + $('#periodo option:selected').val();
                                        generarTabla("divTabla2", url, despuesCargarTabla, false);
                                    }

                                    function addURL(element) {
                                        var url = '/carga/formRespuestaBancaria.action?ordenId=0';
                                        var idn = $('#periodo option:selected').val();

                                        $(element).attr('href', function () {
                                            return url + "&periodo=" + idn;
                                        });
                                    }
    </script>
</content>
