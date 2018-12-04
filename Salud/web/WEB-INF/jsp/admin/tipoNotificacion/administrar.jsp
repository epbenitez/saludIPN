<%-- 
    Document   : Listado
    Created on : 3/11/2015, 05:04:00 PM
    Author     : augusto Ignacio
--%>

<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <link rel="stylesheet" type="text/css" href="/vendors/fancy-box/jquery.fancybox.min.css"/>
    <link rel="stylesheet" type="text/css" href="/vendors/Notifications/css/ns-default.min.css" />
    <link rel="stylesheet" type="text/css" href="/vendors/Notifications/css/ns-style-bar.css" />
    <link rel="stylesheet" type="text/css" href="/vendors/Notifications/css/ns-style-theme.min.css" /> 

    <title>Administrar Tipo de Notificaciones.</title>
</head> 

<content tag="tituloJSP">
    Administrar Tipo de Notificaciones.
</content>

<body>
    <div class="row" id="div-tabla" style="display: none;">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <div class="col-md-12" style="padding: 0px">
                    <div class="responsive">                        
                        <table id="listado" class="table table-striped table-hover dt-responsive text-center" style="width: 100%">
                            <thead>
                                <tr>
                                    <th data-orderable="true" class="text-center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Nombre</th>
                                    <th data-orderable="true" class="text-center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Color</th>
                                    <th data-orderable="true" class="text-center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Ícono</th>
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
    <form id="eliminarForm" action="/admin/eliminarTipoNotificacion.action" method="POST" >
        <input type="hidden" name="tipoNotificacion.id" id="id" value="" />
    </form>    
</body>



<content tag="endScripts">
    <script type="text/javascript" src="/vendors/fancy-box/jquery.fancybox.min.js"></script>
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js" data-init="true"  
    data-div="div-tabla" data-ajax="/ajax/listadoTipoNotificacion.action" data-function="despuesCargarTabla" data-button="agregarNotificacion"></script>
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>    
    <script src="/vendors/Notifications/js/classie.min.js" type="text/javascript"></script>
    <script src="/vendors/Notifications/js/modernizr.custom.min.js" type="text/javascript"></script>
    <script src="/vendors/Notifications/js/notificationFx.js" type="text/javascript"></script>
</content>

<content tag="inlineScripts">
    <script>

        $(document).ready(function () {
        <s:if test="hasActionErrors()">
            ModalGenerator.notificacion({
                "titulo": "Error",
                "cuerpo": "<s:property value="error"/>",
                "tipo": "ALERT"
            });
        </s:if>

        <s:if test="hasActionMessages()">
            ModalGenerator.notificacion({
                "titulo": "Correcto",
                "cuerpo": "<s:property value="correcto"/>",
                "tipo": "SUCCESS"
            });
        </s:if>

        });
        var agregarNotificacion = [{
                text: '<a title="Nueva Notificación" href="/admin/crearTipoNotificacion.action" class="fancybox fancybox.iframe btn btn-primary solo-lectura"  name="nuevo" value="Nuevo" ><i class="fa fa-plus-circle fa-lg"></i> &nbsp;Agregar Tipo Notificación</a>',
                className: 'normal-button'
            }
        ];

        function despuesCargarTabla() {
            $('.fancybox').fancybox({
                autoSize: true,
                afterClose: function () {
                    reload();
                }
            });
        }

        function reload() {
            var url = '/ajax/listadoTipoNotificacion.action';
            generarTabla("div-tabla", url, despuesCargarTabla, false, agregarNotificacion);
        }

        function eliminar(id) {
            ModalGenerator.notificacion({
                "titulo": "Atención",
                "cuerpo": "Esto borrará la notificación seleccionado, ¡Está seguro de querer eliminarla?",
                "tipo": "ALERT",
                "funcionAceptar": function (result) {
                    $("#id").val(id);
                    $("#eliminarForm").submit();
                }
            });
        }
    </script>        
</content>