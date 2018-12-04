<%-- 
    Document   : edicion
    Created on : 3/11/2015, 11:02:56 AM
    Author     : Victor Lozano
    Redesign   : Gustavo Adolfo Alamillo Medina
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Edición de proceso</title>
    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css" />
    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap-datepicker/css/bootstrap-datepicker.min.css" />
    <link rel="stylesheet" type="text/css" href="/vendors/select2/select2-old/select2.min.css"/>
    <link rel="stylesheet" type="text/css" href="/vendors/select2/select2-old/select2-bootstrap.min.css"/>
</head> 

<content tag="tituloJSP">
    Edición de proceso
</content>

<body>

    <div class="row">
        <div class="col-sm-12">
            <s:if test="hasActionErrors()">
                <div class="alert alert-danger">
                    <i class="fa fa-times-circle fa-fw fa-sm"></i>
                    <strong>&iexcl;Error!</strong> <s:actionerror/>
                </div>
            </s:if>
            <s:if test="hasActionMessages()">
                <div class="alert alert-success">
                    <i class="fa fa-check-circle fa-fw fa-sm"></i>
                    <strong>&iexcl;Realizado!</strong> <s:actionmessage />
                </div>
            </s:if>
        </div>
    </div>

    <div class="row">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <div class="main-box-body">
                    <form id="busquedaForm" class="form-horizontal"
                          action="/admin/guardaProceso.action" method="post"
                          data-bv-feedbackicons-valid="glyphicon glyphicon-ok"
                          data-bv-feedbackicons-invalid="glyphicon glyphicon-remove"
                          data-bv-feedbackicons-validating="glyphicon glyphicon-refresh">
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Unidad Académica</label>
                            <div class="col-sm-10">
                                <s:select id="unidadAcademica"  
                                          cssClass="form-control"
                                          name="proceso.unidadAcademica.id"
                                          list="ambiente.unidadAcademicaList" 
                                          listKey="id" 
                                          listValue="nombreCorto" 
                                          headerKey=""
                                          data-bv-notempty="true"
                                          data-bv-notempty-message="La Unidad Académica es requerida"
                                          headerValue="-- Seleccione una Unidad Académica --" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Periodo escolar</label>
                            <div class="col-sm-10">
                                <s:textfield id="periodoEscolar"
                                             cssClass="form-control"
                                             name="proceso.periodo.clave"  />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Movimiento</label>
                            <div class="col-sm-10">
                                <s:select id="movimiento"  
                                          cssClass="form-control"
                                          name="proceso.tipoProceso.movimiento.id"
                                          list="ambiente.movimientoList" 
                                          listKey="id" 
                                          listValue="nombre" 
                                          headerKey=""
                                          data-bv-notempty="true"
                                          data-bv-notempty-message="El tipo de movimiento es requerido"
                                          headerValue="-- Seleccione un movimiento --"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Proceso</label>
                            <div class="col-sm-10">
                                <s:select id="proceso"  
                                          cssClass="form-control"
                                          name="proceso.tipoProceso.id"
                                          list="ambiente.procesoList" 
                                          listKey="id" 
                                          listValue="nombre" 
                                          data-bv-notempty="true"
                                          data-bv-notempty-message="El tipo de proceso es requerido"
                                          headerKey=""
                                          headerValue="-- Seleccione un proceso --"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Fecha inicial</label>
                            <div class="col-sm-10">
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                    <input type="text" class="form-control" id="fechaInicial" name="fechaInicial"
                                           value="<s:date name="proceso.fechaInicial" format="dd/MM/yyyy" />"
                                           data-bv-notempty="true"
                                           data-bv-message="Este dato no es válido"
                                           required="true"
                                           placeholder="Fecha inicial"
                                           onkeydown="return false"
                                           >
                                </div>
                                <span class="help-block" id="fechaInicialMessage">Formato dd/mm/yyyy</span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Fecha final</label>
                            <div class="col-sm-10">
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                    <input type="text" class="form-control" id="fechaFinal" name="fechaFinal"
                                           value="<s:date name="proceso.fechaFinal" format="dd/MM/yyyy" />"
                                           data-bv-notempty="true"
                                           data-bv-message="Este dato no es válido"
                                           required="true" 
                                           placeholder="Fecha final"
                                           onkeydown="return false"
                                           >
                                </div>
                                <span class="help-block" id="fechaFinalMessage">Formato dd/mm/yyyy</span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Becas</label>
                            <div class="col-sm-10">
                                <s:select id="becasL"
                                          name="becasL"
                                          cssClass="form-control"
                                          list="ambiente.becaList" 
                                          listKey="id" 
                                          listValue="nombre" 
                                          data-bv-notempty="true"
                                          data-bv-notempty-message="La beca es requerida"
                                          multiple="true"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-12 col-md-12 col-lg-12">
                                <div class="pull-right">
                                    <input type="submit" id="buscar" class="btn btn-primary" value="Guardar"/>
                                </div>
                            </div>
                        </div>
                        <s:hidden name="proceso.procesoEstatus.id" />
                        <s:hidden name="proceso.id" />
                        <s:hidden name="proceso.periodo.id" id="periodoEscolar"/>
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
    <script type="text/javascript" src="/vendors/select2/select2-old/select2.min.js"></script>
    <script type="text/javascript" src="/vendors/select2/select2-old/select2_locale_es.js"></script>
</content>

<content tag="inlineScripts">
    <script>
        $('#fechaFinal, #fechaInicial').datepicker({language: "es", format: 'dd/mm/yyyy'});
        
        $("#unidadAcademica, #periodoEscolar, #movimiento, #proceso").prop('disabled', true);

        $("#busquedaForm").bootstrapValidator().on('success.form.bv', function (e) {
            $("#unidadAcademica, #periodoEscolar, #movimiento, #proceso").removeAttr('disabled');
        });

        $(document).ready(function () {
            var selectBeca = $("#becasL").select2({language: "es"});
            selectBeca.val([<s:property  value="becasString"/>]).trigger("change");
        });


    </script>
</content>