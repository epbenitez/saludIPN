<%-- 
    Document   : Formulario de Nuevo
    Created on : 4/11/2015, 01:02:11 PM
    Author     : Tania G. Sánchez
    Redesign   : Gustavo Adolfo Alamillo Medina
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Nueva Unidad Académica</title>
    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css" />
</head> 

<content tag="tituloJSP">
    Nueva Unidad Académica
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
                    <form id="unidadAcademica" action="/catalogos/guardaUnidadAcademica.action" method="post" class="form-horizontal"
                          data-bv-feedbackicons-valid="glyphicon glyphicon-ok"
                          data-bv-feedbackicons-invalid="glyphicon glyphicon-remove"
                          data-bv-feedbackicons-validating="glyphicon glyphicon-refresh">
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Nombre largo</label>
                            <div class="col-sm-10">
                                <s:textfield cssClass="form-control" name="unidadAcademica.nombre" id="nombre" placeholder="Nombre"
                                             data-bv-message="Este dato no es válido"
                                             required="true" 
                                             data-bv-notempty="true"
                                             data-bv-notempty-message="El nombre largo es requerido"
                                             pattern='^[a-zA-ZáéíóúñÁÉÍÓÚÑ(),"0-9\s]+$'
                                             data-bv-regexp-message="El nombre largo sólo puede estar conformado por letras, números y paréntesis"
                                             data-bv-stringlength="true" 
                                             data-bv-stringlength-min="7" 
                                             data-bv-stringlength-max="100" 
                                             data-bv-stringlength-message="El nombre largo debe tener mínimo 7 caracteres"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Nombre corto</label>
                            <div class="col-sm-10">
                                <s:textfield cssClass="form-control" name="unidadAcademica.nombreCorto" id="nombreCorto" placeholder="NombreCorto"
                                             data-bv-message="Este dato no es válido"
                                             required="true" 
                                             data-bv-notempty="true"
                                             data-bv-notempty-message="El nombre corto es requerido"
                                             pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ0-9\s]+$" 
                                             data-bv-regexp-message="El nombre corto sólo puede estar conformado por letras y números"
                                             data-bv-stringlength="true" 
                                             data-bv-stringlength-min="3" 
                                             data-bv-stringlength-max="20" 
                                             data-bv-stringlength-message="La descripcion debe tener mínimo 3 caracteres"
                                             />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Clave</label>
                            <div class="col-sm-10">
                                <s:textfield cssClass="form-control" name="unidadAcademica.clave" id="clave" placeholder="Clave"
                                             data-bv-message="Este dato no es válido"
                                             required="true" 
                                             data-bv-notempty="true"
                                             data-bv-notempty-message="La clave es requerida"
                                             pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ0-9\s]+$" 
                                             data-bv-regexp-message="La clave sólo puede estar conformado por letras y números"
                                             data-bv-stringlength="true" 
                                             data-bv-stringlength-min="1" 
                                             data-bv-stringlength-max="3" 
                                             data-bv-stringlength-message="La clave debe tener de 1 a 3 números" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Nivel</label>
                            <div class="col-sm-10">
                                <s:select name="unidadAcademica.nivel.id" list="ambiente.nivelList" 
                                          listKey="id" listValue="nombre" headerKey=""
                                          headerValue="-- Seleccione un nivel --" 
                                          data-bv-notempty="true"
                                          data-bv-notempty-message="El nivel es requerido"
                                          cssClass="form-control"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Área de conocimiento</label>
                            <div class="col-sm-10">
                                <s:select name="unidadAcademica.areasConocimiento.id" list="ambiente.areasConocimientoList" 
                                          listKey="id" listValue="nombre" headerKey=""
                                          headerValue="-- Seleccione un área de conocimiento --"
                                          data-bv-notempty="true"
                                          data-bv-notempty-message="El área de conocimiento de deposito es requerido"
                                          cssClass="form-control"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Dependencia</label>
                            <div class="col-sm-10">
                                <s:select name="unidadAcademica.dependencia" list="service.respuestaBoolean"
                                          cssClass="form-control"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Dirección</label>
                            <div class="col-sm-10">
                                <s:textfield cssClass="form-control" name="unidadAcademica.direccion" id="direccion" placeholder="Dirección"
                                             data-bv-message="Este dato no es válido"
                                             pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ.,/0-9\s]+$" 
                                             data-bv-regexp-message="La direccion sólo puede estar conformado por letras, números, puntos y comas" />
                            </div>
                        </div> 
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Teléfono</label>
                            <div class="col-sm-10">
                                <s:textfield cssClass="form-control" name="unidadAcademica.telefono" id="telefono" placeholder="Teléfono"
                                             data-bv-message="Este dato no es válido"
                                             pattern="^(\(?([0-9]{2,3})\)?([ \.-]?)([0-9]{4}[ -]?[0-9]{4}))$"
                                             data-bv-regexp-message="El teléfono no es válido"
                                             data-bv-stringlength="true"
                                             data-bv-stringlength-min="10" 
                                             data-bv-stringlength-max="18"
                                             data-bv-stringlength-message="El teléfono debe tener 8 números y LADA"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Latitud</label>
                            <div class="col-sm-10">
                                <s:textfield cssClass="form-control" name="unidadAcademica.latitud" id="latitud" placeholder="Latitud"
                                             data-bv-message="Este dato no es válido"
                                             pattern="^[0-9.-]+$" 
                                             data-bv-regexp-message="La latitud sólo puede estar conformado por números" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Longitud</label>
                            <div class="col-sm-10">
                                <s:textfield cssClass="form-control" name="unidadAcademica.longitud" id="longitud" placeholder="Longitud"
                                             data-bv-message="Este dato no es válido"
                                             pattern="^[0-9.-]+$" 
                                             data-bv-regexp-message="La longitud sólo puede estar conformado por números" />
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-12">
                                <s:hidden name="unidadAcademica.id" />
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
            $('#unidadAcademica').bootstrapValidator();
        });
    </script>
</content>