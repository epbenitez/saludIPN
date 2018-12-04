<%-- 
    Document   : Listado
    Created on : 3/11/2015, 05:04:00 PM
    Author     : Tania SÃ¡nchez
    Redesing   : Mario MÃ¡rquez
--%>

<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <link rel="stylesheet" type="text/css" href="/vendors/fancy-box/jquery.fancybox.min.css"/>

    <title>Unidades Académicas</title>
</head> 

<content tag="tituloJSP">
    Administración de Unidades Académicas
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
    <div class="row" id="div-tabla" style="display: none;">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <div class="col-md-12" style="padding: 0px">
                    <div class="responsive">                        
                        <table id="listado" class="table table-striped table-hover dt-responsive" style="width: 100%">
                            <thead>
                                <tr>
                                    <th data-orderable="true">Nombre corto</th>
                                    <th data-orderable="true">Clave</th>
                                    <th data-orderable="true">Nivel</th>
                                    <th data-orderable="true">Área de conocimiento</th>
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
    <form id="eliminarForm" action="/catalogos/eliminarUnidadAcademica.action" method="POST" >
        <input type="hidden" name="unidadAcademica.id" id="id" value="" />
    </form>
</body>

<a title='Nueva Unidad Académica' href="/catalogos/formUnidadAcademica.action" id="nueva-entidad" class="fancybox fancybox.iframe btn btn-primary pull-right"  name="nuevo" value="Nuevo" >
    <i class="fa fa-plus-circle fa-lg"></i> Agregar Unidad Académica
</a>

<content tag="endScripts">
    <script type="text/javascript" src="/vendors/fancy-box/jquery.fancybox.min.js"></script>
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js" data-init="true"  
    data-div="div-tabla" data-ajax="/ajax/listadoUnidadAcademicaAjax.action" data-function="despuesCargarTabla" data-button="agregarUnidadAcademica"></script>
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>
        var agregarUnidadAcademica = [{
                text: '<a title="Nueva unidad académica" href="/catalogos/formUnidadAcademica.action" class="fancybox btn btn-primary solo-lectura"  name="nuevo" value="Nuevo" ><i class="fa fa-plus-circle fa-lg"></i>&nbsp;&nbsp;Agregar unidad académica</a>',
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
            var url = '/ajax/listadoUnidadAcademicaAjax.action';
            generarTabla("div-tabla", url, despuesCargarTabla, false, agregarUnidadAcademica);
        }

        function eliminar(id) {
            ModalGenerator.notificacion({
                "titulo": "Atención",
                "cuerpo": "Esto borrará el registro seleccionado, ¡Está seguro de querer eliminarlo?",
                "tipo": "ALERT",
                "funcionAceptar": function (result) {
                    $("#id").val(id);
                    $("#eliminarForm").submit();
                }
            });
        }
    </script>        
</content>