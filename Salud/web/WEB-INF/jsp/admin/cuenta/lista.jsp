%<-- 
Document   : lista
Created on : 22/03/2017, 12:38:19 PM
Author     : Rafael Cardenas
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

    <title>Bandeja de solicitudes de cuentas</title>
</head> 

<content tag="tituloJSP">
    Bandeja de solicitudes de cuentas
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
    <div class="row" id="cargaGeneral" style="display: none;">
        <div class="col-xs-12">
            <div class="panel-group accordion" id="accordion">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
                                Carga general de cuentas
                            </a>
                        </h4>
                    </div>
                </div>
                <div id="collapseOne" class="panel-collapse collapse">
                    <div class="panel-body">
                        <ul>
                            <li>
                                La carga de respuestas general, se realiza para todas las solicitudes existentes sin distincion alguna.
                            </li>
                            <li>
                                Al realizar la operación: "Carga de Archivo", el sistema buscará todas las referencias en el sistema, contenidas en el archivo cargado/seleccionado, 
                                y actualizará todos los datos asociados a dicha referencia del alumno y cuenta.
                            </li>
                            <li>
                                Asegurese que el archivo a cargar cuente con el formato apropiado.
                            </li>
                        </ul>
                        <div class="col-xs-12 text-center" style="padding-bottom: 5px" >
                            <a href="#" onclick="addURL(this)" id="enviarbtn" class="fancybox fancybox.iframe btn btn-primary btn-lg" >
                                <i class='fa fa-arrow-circle-up'></i> Carga de Archivo
                            </a>
                            <a href="/resources/downloadable/Ejemplo_respuesta_cuentas.xlsx" class="btn btn-primary btn-lg">
                                <i class='fa fa-download'></i> Archivo de ejemplo
                            </a>
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
                                    <th>Solicitud de Cuentas</th>
                                    <th>Usuario de generación</th>
                                    <th>Fecha de generación</th>
                                    <th data-center="true"></th>
                                    <th data-center="true"></th>
                                    <th data-center="true"></th>
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
    <script src="/resources/js/ModalGenerator.min.js"></script>
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js" 
            data-init="true" data-ajax='/ajax/listadoAdminSolicitudCuentasAjax.action' data-div="divTabla2"
    data-function="despuesCargarTabla"></script>
    <script type="text/javascript" src="/vendors/fancy-box/jquery.fancybox.min.js"></script>    
</content>

<content tag="inlineScripts">
    <script>
        function finalizar(e) {
            event.preventDefault();
            ModalGenerator.notificacion({
                "titulo": "¿Estas seguro de continuar?",
                "cuerpo": "Estas apunto de finalizar las solicitudes",
                "tipo": "WARNING",
                "funcionAceptar": function () {
                    console.log();
                    location.href = "/admin/finalizaOrdenesSolicitudCuentas.action?solicitud.id=" + e;
                }
            });

        }

        function despuesCargarTabla() {
            $('#cargaGeneral').show();
            $('.fancybox').fancybox({
                autoSize: true
            });
        }

        function addURL(element) {
            var url = '/carga/formSolicitudRespuestaBancaria.action?ordenId=0';

            $(element).attr('href', function () {
                return url;
            });
        }
    </script>
</content>