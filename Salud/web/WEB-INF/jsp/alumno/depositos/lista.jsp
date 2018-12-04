<%-- 
    Document   : lista
    Created on : 14/01/2016, 09:30:49 AM
    Author     : Monserrat Fuentes
--%>

<%@taglib prefix="s" uri="/struts-tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.0//EN" "http://www.w3.org/TR/REC-html40/strict.dtd">
<html>
    <head>
        <title>Consulta de depósitos</title>
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
        <script type="text/javascript" language="javascript" class="init">
            $(document).ready(function () {
                $('.fancybox').fancybox({
                    maxWidth: 700,
                    maxHeight: 600,
                    afterClose: function () {
                        reload();
                    }
                });
                $('#listado').hide();
                reload();
                var tabla = $('#listado').DataTable();
            });

            function reload() {
                $('#listado').show();
                if ($.fn.dataTable.isDataTable('#listado')) {
                    table = $('#listado').DataTable({
                        destroy: true,
                        responsive: true
                    });
                    table.destroy();
                }
                $('#listado').DataTable({
                    "ajax": '/ajax/listadoDepositoAjax.action?noIdalumno=<s:property value="idAlumno"/>&idPeriodo=0',
                    "order": [[0, 'asc'], [4, 'asc']]
                });
            }
        </script>
    </head>

    <body>
        <div class="container">
            <h1>Consulta de depósitos </h1>
            <div class="row main_content">
                <s:set var="alumno" value="alumno"/>
                <security:authorize ifAnyGranted="ROLE_ALUMNO">
                    <div class="alert alert-info fade in" style="margin-bottom:0px;">
                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                        <i class="fa fa-info-circle fa-fw fa-lg"></i>
                        <strong>¿Necesitas ayuda?</strong> Da click <a href="/misdatos/consultar.action#encabezado8">aquí</a> para consultar el manual de usuario.
                    </div>
                    <div class="clearfix" >&nbsp;</div>
                </security:authorize>
                <div class="col-sm-12 main-box">
                    <div class="clearfix" >&nbsp;</div>
                    <form class="form-horizontal">
                        <div class="form-group col-sm-12 ">
                            <label class="col-sm-3 control-label">
                                Boleta
                            </label>
                            <div class="col-sm-9">
                                <s:textfield name="alumno.boleta" cssClass="form-control" readonly="true"/>
                            </div>
                        </div>

                        <div class="form-group col-sm-12">
                            <label class="col-sm-3 control-label">
                                Nombre
                            </label>
                            <div class="col-sm-3">
                                <s:textfield name="alumno.nombre" cssClass="form-control" readonly="true"/>
                            </div>
                            <div class="col-sm-3">
                                <s:textfield name="alumno.apellidoPaterno" cssClass="form-control" readonly="true"/>
                            </div>
                            <div class="col-sm-3">
                                <s:textfield name="alumno.apellidoMaterno" cssClass="form-control" readonly="true"/>
                            </div>
                        </div>

                        <div class="form-group col-sm-12">
                            <label class="col-sm-3 control-label">
                                Unidad Académica
                            </label>
                            <div class="col-sm-9">
                                <s:textfield name="alumno.datosAcademicos.unidadAcademica.nombreCorto" cssClass="form-control" readonly="true"/>
                            </div>
                        </div>
                            
                        <div class="form-group col-sm-12">
                            <label class="col-sm-3 control-label">
                                Tarjeta activa
                            </label>
                            <div class="col-sm-9">
                                <s:textfield name="numero_tarjeta" cssClass="form-control" readonly="true"/>
                            </div>
                        </div>     
                    </form>
                    <div class="clearfix" >&nbsp;</div>
                </div>

                <div class="col-sm-12 main-box">
                    <div class="clearfix" >&nbsp;</div>
                    <div class="clearfix" >&nbsp;</div>
                    <div id="error" >
                        <s:property  value="alumno.error"  />
                    </div>
                    <div class="responsive-table table-responsive clearfix">
                        <table id="listado" class="table table-striped table-hover">
                            <thead>
                                <tr>
                                    <th>Periodo</th>
                                    <th>Tipo de beca</th>
                                    <th>Mes</th>
                                    <th>Fecha de depósito</th>
                                    <th>Monto</th>
                                    <th>Tarjeta</th>
                                </tr>
                            </thead>
                        </table>
                    </div>               
                </div>
            </div>
    </body>
</html>