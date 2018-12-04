<%-- 
    Document   : listado
    Created on : 27/10/2015, 02:05:19 PM
    Author     : Usre-05
    Redisign   : Gustavo Adolfo Alamillo Medina
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Administración de tipos de beca</title>
    <link rel="stylesheet" type="text/css" href="/vendors/fancy-box/jquery.fancybox.min.css"/>
</head> 

<content tag="tituloJSP">
    Administración de tipos de beca
</content>

<body>
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

    <div class="row" id="tiposRow" style="display: none;">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <div class="col-md-12" style="padding: 0px">
                    <div class="responsive">                        
                        <table id="usuariosTabla" class="table table-striped table-hover dt-responsive" style="width: 100%">
                            <thead>
                                <tr>
                                    <th data-orderable="true">Nombre</th>
                                    <th data-center="true">Clave</th>
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

    <form id="eliminarForm" action="/catalogos/eliminarTipoBeca.action" method="POST" >
        <input type="hidden" name="tipoBeca.id" id="id" value="" />
    </form>
</body>

<content tag="endScripts">
    <script type="text/javascript" src="/vendors/fancy-box/jquery.fancybox.min.js"></script>
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js" 
            data-init="true" data-ajax="/ajax/listadoTipoBecaAjax.action" data-div="tiposRow"
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
            generarTabla("tiposRow", "/ajax/listadoTipoBecaAjax.action", despuesCargarTabla, botonAgregar);
        }

        var botonAgregar = [{
                text: '<a id="nueva-entidad"title="Nuevo tipo de beca" href="/catalogos/formTipoBeca.action" class="fancybox fancybox.iframe solo-lectura btn btn-primary"  name="nuevo" value="Nuevo" ><i class="fa fa-plus-circle fa-lg"></i>&nbsp;&nbsp;Agregar tipo de beca</a>',
                className: 'normal-button'
            }];

        function eliminar(id) {

            ModalGenerator.notificacion({
                "titulo": "¿Deseas continuar?",
                "cuerpo": "Esto borrará el registro seleccionado, ¿Está seguro de quererlo eliminar?",
                "funcionAceptar": function () {
                    $("#id").val(id);
                    $("#eliminarForm").submit();
                },
                "tipo": "ALERT"
            });
        }
    </script>
</content>