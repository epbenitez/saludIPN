<%-- 
    Document   : editar
    Created on : 4/06/2018, 12:50:46 PM
    Author     : Admin CAE
--%>

<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Solicitud Alumno</title>
    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css" />
    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap-datepicker/css/bootstrap-datepicker.min.css" />
</head>

<content tag="tituloJSP">
    Solicitud Alumno
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

    <div class="row">
        <div class="col-xs-12">
            <div class="main-box clearfix" >
                <div class="main-box-body ">
                    <form id="nuevoAcceso" action="/admin/guardarAccesoAlumno.action" method="post" class="form-horizontal">

                        <div class="form-group">
                            <label class="col-sm-2 control-label">Periodo</label>
                            <div class="col-sm-10">
                                <s:select id = "periodo" name="nuevoAcceso.periodo.id" list="ambiente.periodoList"
                                          cssClass="form-control"
                                          listKey="id" listValue="clave"
                                          headerKey="" data-bv-notempty="true"
                                          headerValue="-- Seleccione un periodo --" />
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-2 control-label">Boleta</label>
                            <div class="col-sm-10">
                                <s:textfield cssClass="form-control" name="nuevoAcceso.alumno.boleta" id="boleta" placeHolder="Boleta"
                                             data-bv-message="Este dato no es válido"
                                             required="true" 
                                             data-bv-notempty="true"
                                             data-bv-notempty-message="La boleta es requerido"   
                                             pattern="^[0-9a-zA-Z]+$"
                                             data-bv-regexp-message="La Boleta sólo puede estar conformado por letras y números"
                                             data-bv-stringlength="true"
                                             data-by-stringlength-max="10"
                                             data-bv-stringlength-message="La boleys debe tener 10 caracteres" />
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-2 control-label">Fecha inicial</label>
                            <div class="col-sm-10">
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                    <input type="text" class="form-control" id="fechaInicial" name="fechaInicial"
                                           value="<s:date name="nuevoAcceso.fechaInicio" format="dd/MM/yyyy" />"
                                           data-bv-notempty="true"
                                           data-bv-message="Este dato no es válido"
                                           placeholder="Fecha inicial"
                                           onkeydown="return false">
                                </div>
                                <span class="help-block">Formato dd/mm/yyyy</span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-2 control-label">Fecha final</label>
                            <div class="col-sm-10">
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                    <input type="text" class="form-control" id="fechaFinal" name="fechaFinal"
                                           value="<s:date name="nuevoAcceso.fechaFin" format="dd/MM/yyyy" />"
                                           data-bv-notempty="true"
                                           data-bv-message="Este dato no es válido"
                                           placeholder="Fecha final"
                                           onkeydown="return false">
                                </div>
                                <span class="help-block">Formato dd/mm/yyyy</span>
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-12">
                                <s:hidden name="nuevoAcceso.id"/>
                                <button type="submit" class="btn btn-primary pull-right">Guardar</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</body>

<content tag="endScripts">
    <script type="text/javascript" src="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.js"></script>
    <script type="text/javascript" src="/vendors/bootstrap-datepicker/js/bootstrap-datepicker.min.js"></script>
    <script type="text/javascript" src="/vendors/bootstrap-datepicker/locales/bootstrap-datepicker.es.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>
                                               $(document).ready(function () {
                                                   $('#nuevoAcceso').bootstrapValidator();
                                               });

                                               $(function () {
                                                   $('#fechaInicial, #fechaFinal').datepicker({
                                                       format: 'dd/mm/yyyy',
                                                       language: 'es',
                                                       startDate: new Date()
                                                   });
                                               });
    </script>
</content>