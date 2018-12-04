<%-- 
    Document   : form
    Created on : 29/10/2015, 12:37:37 PM
    Author     : Usre-05
    Redesign     : Gustavo Adolfo Alamillo Medina
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Nuevo tipo de beca</title>
    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css" />
</head> 

<content tag="tituloJSP">
    Nuevo tipo de beca
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
                    <form id="tipoBeca" action="/catalogos/guardaTipoBeca.action" method="post" class="form-horizontal"
                          data-bv-feedbackicons-valid="glyphicon glyphicon-ok"
                          data-bv-feedbackicons-invalid="glyphicon glyphicon-remove"
                          data-bv-feedbackicons-validating="glyphicon glyphicon-refresh">

                        <div class="form-group">
                            <label class="col-xs-2 control-label">Nombre</label>
                            <div class="col-xs-10">
                                <s:textfield cssClass="form-control" name="tipoBeca.nombre" id="nombre" placeholder="Nombre"
                                             data-bv-message="Este dato no es válido"
                                             required="true" 
                                             data-bv-notempty="true"
                                             data-bv-notempty-message="El nombre es requerido"
                                             pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s1234567890]+$" 
                                             data-bv-regexp-message="No se permiten caracteres especiales"
                                             data-bv-stringlength="true" 
                                             data-bv-stringlength-min="3" 
                                             data-bv-stringlength-max="25" 
                                             data-bv-stringlength-message="Tu nombre debe tener de 3 a 25 caracteres"
                                             />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-2 control-label">Beca</label>
                            <div class="col-xs-10">
                                <s:select name="tipoBeca.beca.id" list="ambiente.becaList" 
                                          listKey="id" listValue="nombre"
                                          cssClass="form-control"
                                          headerKey=""
                                          headerValue="-- Seleccione un nivel --" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-2 control-label">Clave</label>
                            <div class="col-xs-10">
                                <s:textfield cssClass="form-control" name="tipoBeca.clave" id="clave" placeholder="Clave"
                                             data-bv-message="Este dato no es válido"
                                             required="true" 
                                             data-bv-notempty="true"
                                             data-bv-notempty-message="La clave es requerida"
                                             pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s1234567890]+$" 
                                             data-bv-regexp-message="No se permiten caracteres especiales"
                                             data-bv-stringlength="true" 
                                             data-bv-stringlength-min="3" 
                                             data-bv-stringlength-max="10" 
                                             data-bv-stringlength-message="La clave debe tener de 3 a 10 caracteres" />
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-xs-12">
                                <s:hidden name="tipoBeca.id" />
                                <button type="submit" class="btn btn-primary pull-right">Guardar tipo de beca</button>
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
            $('#tipoBeca').bootstrapValidator({});
        });
    </script>
</content>