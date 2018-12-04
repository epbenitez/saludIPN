<%-- 
    Document   : form
    Created on : 29/10/2015, 12:37:37 PM
    Author     : Usre-05
    Redisign   : Gustavo Adolfo Alamillo Medina
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Tipo de beca/periodo</title>
    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css" />
</head> 

<content tag="tituloJSP">
    Nuevo tipo de beca/periodo
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

    <!-- Ejemplo para formularios -->
    <div class="row">
        <form id="tipoBecaPeriodo" action="/catalogos/guardaTipoBecaPeriodo.action" method="post" class="form-horizontal"
              data-bv-feedbackicons-valid="glyphicon glyphicon-ok"
              data-bv-feedbackicons-invalid="glyphicon glyphicon-remove"
              data-bv-feedbackicons-validating="glyphicon glyphicon-refresh">
            <div class="col-xs-12">
                <div class="main-box clearfix">
                    <div class="tabs-wrapper profile-tabs">
                        <ul class="nav nav-tabs">
                            <li class="active"><a href="#uno-tab" data-toggle="tab">Características básicas <i class="fa"></i></a></li>
                            <li><a href="#dos-tab" data-toggle="tab">Características del alumno <i class="fa"></i></a></li>
                            <li><a href="#tres-tab" data-toggle="tab">Detalle <i class="fa"></i></a></li>
                        </ul>

                        <div class="tab-content">

                            <div class="tab-pane fade active in" id="uno-tab">
                                <div class="form-group">
                                    <label class="col-sm-3 control-label">Beca</label>
                                    <div class="col-sm-6">
                                        <s:select name="tipoBecaPeriodo.tipoBeca.id" list="ambiente.tipoBecaList" 
                                                  listKey="id" listValue="nombre"
                                                  required="true" 
                                                  cssClass="form-control"
                                                  data-bv-notempty="true"
                                                  data-bv-notempty-message="El nombre es requerido"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label">Nivel</label>
                                    <div class="col-sm-6">
                                        <s:select name="tipoBecaPeriodo.nivel.id" list="ambiente.nivelList" 
                                                  listKey="id" listValue="nombre"
                                                  required="true" 
                                                  cssClass="form-control"
                                                  data-bv-notempty="true"
                                                  data-bv-notempty-message="El nivel es requerido"/>
                                    </div>
                                </div>


                                <div class="form-group">
                                    <label class="col-sm-3 control-label">Periodo</label>
                                    <div class="col-sm-6">
                                        <s:select name="tipoBecaPeriodo.periodo.id" list="ambiente.periodoList" 
                                                  listKey="id" listValue="descripcion"
                                                  required="true" 
                                                  cssClass="form-control"
                                                  data-bv-notempty="true"
                                                  data-bv-notempty-message="El periodo es requerido"/>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-sm-3 control-label">Prioridad</label>
                                    <div class="col-sm-6">
                                        <s:textfield cssClass="form-control" name="tipoBecaPeriodo.prioridad" id="prioridad" placeholder="Prioridad"
                                                     data-bv-message="Este dato no es válido"
                                                     required="true" 
                                                     data-bv-notempty="true"
                                                     data-bv-notempty-message="La prioridad es requerida"
                                                     pattern="^[\d]+$" 
                                                     data-bv-regexp-message="El campo no es válido"
                                                     data-bv-stringlength="true" 
                                                     data-bv-stringlength-max="3"
                                                     data-bv-stringlength-message="El campo debe tener hasta 3 caracteres"/>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-sm-3 control-label">Estatus</label>
                                    <div class="col-sm-6">
                                        <s:select name="tipoBecaPeriodo.estatus.id" list="ambiente.estatusList" 
                                                  listKey="id" listValue="nombre"
                                                  required="true" 
                                                  cssClass="form-control"
                                                  data-bv-notempty="true"
                                                  data-bv-notempty-message="El estatus es requerido"/>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-sm-3 control-label">Monto</label>
                                    <div class="col-sm-6">
                                        <s:textfield cssClass="form-control" name="tipoBecaPeriodo.monto" id="monto" placeholder="Monto"
                                                     data-bv-message="Este dato no es válido"
                                                     required="true" 
                                                     data-bv-notempty="true"
                                                     data-bv-notempty-message="El monto es requerido"
                                                     pattern="^([1-9]+[\d]*[\.])?[1-9]+[\d]*$" 
                                                     data-bv-regexp-message="El campo no es válido"
                                                     data-bv-stringlength="true" 
                                                     data-bv-stringlength-max="15"
                                                     data-bv-stringlength-message="El monto debe tener hasta 15 caracteres"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label">Temporalidad (meses)</label>
                                    <div class="col-sm-6">
                                        <s:textfield cssClass="form-control" name="tipoBecaPeriodo.duracion" id="duracion" placeholder="Duración"
                                                     data-bv-message="Este dato no es válido"
                                                     required="true" 
                                                     data-bv-notempty="true"
                                                     data-bv-notempty-message="La duracion de la beca es requerida"
                                                     pattern="^[\d]+$" 
                                                     data-bv-regexp-message="Solo se permiten números"
                                                     data-bv-stringlength="true" 
                                                     data-bv-stringlength-max="2"
                                                     data-bv-stringlength-message="El campo debe tener  hasta 2 caracteres"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label">Fases</label>
                                    <div class="col-sm-6">
                                        <s:textfield cssClass="form-control" name="tipoBecaPeriodo.fases" id="fases" placeholder="Fases"
                                                     data-bv-message="Este dato no es válido"
                                                     required="true" 
                                                     data-bv-notempty="true"
                                                     data-bv-notempty-message="La duracion de la beca es requerida"
                                                     pattern="^[\d]+$" 
                                                     data-bv-regexp-message="Solo se permiten números"
                                                     data-bv-stringlength="true" 
                                                     data-bv-stringlength-max="2"
                                                     data-bv-stringlength-message="El campo debe tener  hasta 2 caracteres"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label"> Validacion de inscripcion </label>
                                    <div class="col-sm-6">
                                        <s:select name="tipoBecaPeriodo.validaciondeinscripcion" list="service.respuestaInteger" 
                                                  headerKey=""
                                                  cssClass="form-control"
                                                  required="true" 
                                                  data-bv-notempty="true"
                                                  data-bv-notempty-message="Campo requerido"/>
                                    </div>
                                </div>
                            </div>

                            <div class="tab-pane fade" id="dos-tab">

                                <div class="form-group">
                                    <label class="col-sm-3 control-label">Modalidad</label>
                                    <div class="col-sm-6">
                                        <s:select name="tipoBecaPeriodo.modalidad.id" list="ambiente.modalidadList" 
                                                  listKey="id" listValue="nombre"
                                                  cssClass="form-control"
                                                  required="true" 
                                                  data-bv-notempty="true"
                                                  data-bv-notempty-message="La modalidad es requerida"
                                                  headerKey="0"
                                                  headerValue="Independiente de la modalidad" />
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-sm-3 control-label">Área</label>
                                    <div class="col-sm-6">
                                        <s:select name="tipoBecaPeriodo.areasconocimiento.id" list="ambiente.areasConocimientoList" 
                                                  listKey="id" listValue="nombre"
                                                  cssClass="form-control"
                                                  required="true" 
                                                  data-bv-notempty="true"
                                                  data-bv-notempty-message="El área es requerida"
                                                  headerKey="0"
                                                  headerValue="Independiente del área" />
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-sm-3 control-label"> ¿El alumno debe ser regular? </label>
                                    <div class="col-sm-6">
                                        <s:select name="tipoBecaPeriodo.regular" list="service.respuestaInteger"
                                                  headerKey=""
                                                  cssClass="form-control"
                                                  required="true" 
                                                  data-bv-notempty="true"
                                                  data-bv-notempty-message="Campo requerido"/>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-sm-3 control-label"> Carga </label>
                                    <div class="col-sm-6">
                                        <s:select name="tipoBecaPeriodo.cumpleCargaMinima" list="service.Carga" 
                                                  headerKey=""
                                                  cssClass="form-control"
                                                  required="true" 
                                                  data-bv-notempty="true"
                                                  data-bv-notempty-message="Campo requerido"/>
                                    </div>
                                </div>
                                    
                                <div class="form-group">
                                    <label class="col-xs-12 col-sm-11 col-lg-offset-1">Materias reprobadas</label>
                                    <label class="col-xs-2 col-sm-3 control-label">Mínimas</label>
                                    <div class="col-sm-2 col-xs-4">
                                        <s:textfield cssClass="form-control" name="tipoBecaPeriodo.reprobadasMinimo" 
                                                     id="reprobadas-min" 
                                                     placeholder="Reprobadas mínimo"
                                                     pattern="^[0-9][0-9]?$" 
                                                     data-bv-regexp-message="Solo se permiten numeros 0 - 99"
                                                     />
                                    </div>
                                    <label class="col-xs-2 col-sm-1 control-label">Máximo</label>
                                    <div class="col-sm-2 col-xs-4">
                                        <s:textfield cssClass="form-control" name="tipoBecaPeriodo.reprobadasMaximo" 
                                                     id="reprobadas-max" 
                                                     placeholder="Reprobadas máximo"
                                                     pattern="^[0-9][0-9]?$" 
                                                     data-bv-regexp-message="Solo se permiten numeros 0 - 99"
                                                     />
                                    </div>
                                </div>
                            </div>

                            <div class="tab-pane clearfix fade" id="tres-tab">

                                <div class="form-group">
                                    <label class="col-sm-3 control-label">Promedio mínimo</label>
                                    <div class="col-sm-6">
                                        <s:textfield cssClass="form-control" name="tipoBecaPeriodo.promedioMinimo" 
                                                     id="promedioMinimo" 
                                                     placeholder="Promedio mínimo"
                                                     data-bv-message="Este dato no es válido"
                                                     required="true" 
                                                     data-bv-notempty="true"
                                                     data-bv-notempty-message="El promedio mínimo es requerido"
                                                     pattern="^(100.00|100|100.0|(([\d]{1,2}[\.])[\d]{1,3})|([\d]{1,2}))$" 
                                                     data-bv-regexp-message="El campo no es válido"
                                                     data-bv-stringlength="true" 
                                                     data-bv-stringlength-max="7"/>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-sm-3 control-label">Promedio máximo</label>
                                    <div class="col-sm-6">
                                        <s:textfield cssClass="form-control" name="tipoBecaPeriodo.promedioMaximo"
                                                     id="promedioMaximo" 
                                                     placeholder="Promedio máximo"
                                                     data-bv-message="Este dato no es válido"
                                                     required="true" 
                                                     data-bv-notempty="true"
                                                     data-bv-notempty-message="El promedio máximo es requerido"
                                                     pattern="^(100.00|100|100.0|(([\d]{1,2}[\.])[\d]{1,3})|([\d]{1,2}))$" 
                                                     data-bv-regexp-message="El campo no es válido"
                                                     data-bv-stringlength="true" 
                                                     data-bv-stringlength-max="7"                                                     />
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-sm-3 control-label">Semestre mínimo</label>
                                    <div class="col-sm-6">
                                        <s:textfield cssClass="form-control" name="tipoBecaPeriodo.semestreMinimo" 
                                                     id="semestreMinimo" 
                                                     placeholder="Semestre mínimo"
                                                     data-bv-message="Este dato no es válido"
                                                     required="true" 
                                                     data-bv-notempty="true"
                                                     data-bv-notempty-message="El semestre mínimo es requerido"
                                                     pattern="^([0-2][\d]|[\d])$" 
                                                     data-bv-regexp-message="El campo no es válido"
                                                     data-bv-stringlength="true" 
                                                     data-bv-stringlength-max="2"/>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-sm-3 control-label">Semestre máximo</label>
                                    <div class="col-sm-6">
                                        <s:textfield cssClass="form-control" name="tipoBecaPeriodo.semestreMaximo" 
                                                     id="semestreMaximo"
                                                     placeholder="Semestre máximo"
                                                     data-bv-message="Este dato no es válido"
                                                     required="true" 
                                                     data-bv-notempty="true"
                                                     data-bv-notempty-message="El semestre maximo es requerido"                                                     
                                                     pattern="^([0-2][\d]|[\d])$" 
                                                     data-bv-regexp-message="El campo no es válido"
                                                     data-bv-stringlength="true" 
                                                     data-bv-stringlength-max="2"/>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-sm-3 control-label">Número de materias aprobadas</label>
                                    <div class="col-sm-6">
                                        <s:textfield cssClass="form-control" name="tipoBecaPeriodo.numMateriasAprobadas" 
                                                     id="numMateriasAprobadas" 
                                                     placeholder="Materias aprobadas"
                                                     data-bv-message="Este dato no es válido"
                                                     required="true" 
                                                     data-bv-notempty="true"
                                                     data-bv-notempty-message="El número de materias aprobadas es requerido"
                                                     pattern="^[\d]+$" 
                                                     data-bv-regexp-message="Solo se permiten números"
                                                     data-bv-stringlength="true" 
                                                     data-bv-stringlength-max="4"
                                                     data-bv-stringlength-message="El campo debe tener  hasta 4 caracteres"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label">Ingreso en salarios mínimos</label>
                                    <div class="col-sm-6">
                                        <s:textfield cssClass="form-control" name="tipoBecaPeriodo.ingresoSalarios" 
                                                     id="ingresoSalarios" 
                                                     placeholder="Ingreso en salarios"
                                                     data-bv-message="Este dato no es válido"
                                                     pattern="^[\d.]+$"
                                                     data-bv-regexp-message="El campo no es válido"
                                                     data-bv-stringlength="true" 
                                                     data-bv-stringlength-max="8"                                              
                                                     data-bv-stringlength-message="El campo debe tener hasta 8 números"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label">Monto expresado en</label>
                                    <div class="col-sm-6">
                                        <s:select name="tipoBecaPeriodo.montoExpresadoEnDeciles" list="service.MontoExpresado" 
                                                  cssClass="form-control" headerKey="" required="true" data-bv-notempty="true"
                                                  data-bv-notempty-message="Campo requerido"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-11 col-sm-3 control-label">Vulnerabilidad SUBES</label>
                                    <div class="col-xs-11 col-sm-6">
                                        <s:select name="tipoBecaPeriodo.vulnerabilidadSubes" list="service.RespuestaBoolean" 
                                                  cssClass="form-control" headerKey="" required="true" data-bv-notempty="true"
                                                  data-bv-notempty-message="Campo requerido"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-xs-12">
                            <s:hidden name="tipoBecaPeriodo.id" />
                            <button id="submitBtn" type="submit" class="btn btn-primary pull-right">Guardar cambios</button>
                        </div>
                    </div>
                </div>
                            
            </div>
        </form>
    </div>
    <!--Termina ejemplo para formularios -->

</body>

<content tag="endScripts">
    <script type="text/javascript" src="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>
        $(document).ready(function () {
            $('#tipoBecaPeriodo')
                    .bootstrapValidator({
                        excluded: [':disabled'],
                    }).on('status.field.bv', function (e, data) {
                var $form = $(e.target),
                        validator = data.bv,
                        $tabPane = data.element.parents('.tab-pane'),
                        tabId = $tabPane.attr('id');

                if (tabId) {
                    var $icon = $('a[href="#' + tabId + '"][data-toggle="tab"]').parent().find('i');

                    // Add custom class to tab containing the field
                    if (data.status === validator.STATUS_INVALID) {
                        $icon.removeClass('fa-check').addClass('fa-times');
                    } else if (data.status === validator.STATUS_VALID) {
                        var isValidTab = validator.isValidContainer($tabPane);
                        $icon.removeClass('fa-check fa-times')
                                .addClass(isValidTab ? 'fa-check' : 'fa-times');
                    }
                }
            });
        });
    </script>
</content>