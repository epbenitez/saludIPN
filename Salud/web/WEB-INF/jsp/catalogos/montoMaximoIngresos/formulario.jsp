<%-- 
    Document   : formulario
    Created on : 25/09/2017, 12:26:30 PM
    Author     : Tania G. Sánchez Manilla
--%>

<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Nuevo monto máximo de ingresos</title>
    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css" />
</head> 

<content tag="tituloJSP">
    Nuevo monto máximo de ingresos
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
                    <form id="montoIngresos" action="/catalogos/guardaMontoMaximoIngresos.action" method="post" class="form-horizontal"
                          data-bv-feedbackicons-valid="glyphicon glyphicon-ok"
                          data-bv-feedbackicons-invalid="glyphicon glyphicon-remove"
                          data-bv-feedbackicons-validating="glyphicon glyphicon-refresh">
                        <div class="form-group">
                            <label class="col-sm-3 control-label">Periodo</label>
                            <div class="col-sm-8">
                                <s:if test="deshabilitados">
                                    <s:select id="periodo" name="montoMaximoIngresos.periodo.id" list="ambiente.periodoList"
                                              cssClass="form-control" listKey="id" listValue="clave"
                                              headerKey="" data-bv-notempty="true"
                                              headerValue="-- Seleccione un periodo --"
                                              disabled = "true" />
                                    <s:hidden name="montoMaximoIngresos.periodo.id" />
                                </s:if>
                                <s:else>
                                    <s:select id= "periodo" name="montoMaximoIngresos.periodo.id" list="ambiente.periodoList"
                                              cssClass="form-control" listKey="id" listValue="clave"
                                              headerKey="" data-bv-notempty="true"
                                              headerValue="-- Seleccione un periodo --" />
                                </s:else>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">Monto</label>
                            <div class="col-sm-8">
                                <s:textfield cssClass="form-control" name="montoMaximoIngresos.monto" id="monto" placeholder="Monto"
                                             required="true" data-bv-message="Este dato no es válido"
                                             data-bv-notempty="true" data-bv-notempty-message="El monto es requerido."
                                             pattern="^[.0-9\s]+$" 
                                             data-bv-regexp-message="El monto sólo puede estar conformado por números."
                                             data-bv-stringlength="true" 
                                             data-bv-stringlength-min="7" 
                                             data-bv-stringlength-max="10" 
                                             data-bv-stringlength-message="La clave debe tener al menos 7 números." />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">Deciles</label>
                            <div class="col-sm-8">
                                <s:if test="deshabilitados">
                                    <s:select name="montoMaximoIngresos.deciles" list="service.respuestaBoolean" cssClass="form-control" disabled = "true"/>
                                    <s:hidden name="montoMaximoIngresos.deciles" />
                                </s:if>
                                <s:else>
                                    <s:select name="montoMaximoIngresos.deciles" list="service.respuestaBoolean" cssClass="form-control"/>
                                </s:else>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-12">
                                <s:hidden name="montoMaximoIngresos.id" />
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
</content>

<content tag="inlineScripts">
    <script>
        $(document).ready(function () {
            $('#montoIngresos').bootstrapValidator();
        });
    </script>
</content>