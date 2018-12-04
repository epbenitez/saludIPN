<%-- 
    Document   : lista
    Created on : 14/09/2017, 05:39:42 PM
    Author     : Tania G. Sánchez Manilla
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Administración de monto máximo de ingresos</title>
    <link rel="stylesheet" type="text/css" href="/vendors/fancy-box/jquery.fancybox.min.css"/>
</head> 

<content tag="tituloJSP">
    Administración de monto máximo de ingresos
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
    <div class="row" id="rowTable" style="display: none;">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <div class="col-md-12" style="padding: 0px">
                    <div class="responsive">                        
                        <table id="tablaMontos" class="table table-striped table-hover dt-responsive" style="width: 100%">
                            <thead>
                                <tr>
                                    <th data-orderable="true">Periodo</th>
                                    <th data-orderable="true">Monto</th>
                                    <th data-orderable="true">Deciles</th>
                                    <th data-orderable="true"></th>
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
    <script type="text/javascript" src="/vendors/fancy-box/jquery.fancybox.min.js"></script>
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js" 
            data-init="true" data-ajax="/ajax/listadoMontoMaximoIngresosAjax.action" data-div="rowTable"
    data-function="despuesCargarTabla" data-button="botonAgregar"></script>
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>
        function despuesCargarTabla() {
            $('.fancybox').fancybox({
                afterClose: function () {
                    recargarTabla();
                }
            });
        }
        function recargarTabla() {
            generarTabla("rowTable", "/ajax/listadoMontoMaximoIngresosAjax.action", despuesCargarTabla, botonAgregar);
        }

        var botonAgregar = [{
                text: ' <a id="nueva-entidad" title="Nuevo monto máximo de ingresos" href="/catalogos/formularioMontoMaximoIngresos.action" class="fancybox fancybox.iframe btn btn-primary pull-right" name="nuevo" value="Nuevo" ><i class="fa fa-plus-circle fa-lg"></i>&nbsp;&nbsp;Agregar monto máximo de ingresos</a>',
                className: 'normal-button'
            }];
    </script>
</content>