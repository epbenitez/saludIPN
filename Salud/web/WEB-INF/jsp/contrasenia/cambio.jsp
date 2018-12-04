<%-- 
    Document   : Cambio de contraseña
    Created on : 18-ago-2015, 11:54:02
    Author     : Patricia Benitez
    Rediseño   : Gustavo A. Alamillo
    Modified   : 11-Oct-2015
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>


<head>
    <title>Cambio de contraseña</title>
    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css"/>
</head> 

<content tag="tituloJSP">
    Cambio de contraseña
</content>

<body>
    <div class="row">
        <div class="col-xs-12">
            <s:if test="hasActionErrors()">
                <div class="alert alert-danger fade-in">
                    <i class="fa fa-times-circle fa-fw fa-lg"></i>
                    <strong>&iexcl;Error!</strong> <s:actionerror/>
                </div>
            </s:if>
            <s:if test="hasActionMessages()">
                <div class="alert alert-success fade-out">
                    <i class="fa fa-check-circle fa-fw fa-lg"></i>
                    <strong>&iexcl;Realizado!</strong> <s:actionmessage />
                </div>
            </s:if>
            <security:authorize ifAnyGranted="ROLE_ALUMNO">
                <div class="alert alert-info fade in">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                    <i class="fa fa-info-circle fa-fw fa-lg"></i>
                    <strong>¿Necesitas ayuda?</strong> Da click <a href="/misdatos/consultar.action#encabezado2">aquí</a> para consultar el manual de usuario.
                </div>
            </security:authorize>
        </div>
    </div>

    <div class="row">
        <div class="col-xs-12">
            <div class="main-box clearfix" >
                <div class="main-box-body">
                    <form id="passwordForm" method="post" class="form-horizontal" action="/actualizaContrasenia.action"
                          data-bv-feedbackicons-valid="glyphicon glyphicon-ok"
                          data-bv-feedbackicons-invalid="glyphicon glyphicon-remove"
                          data-bv-feedbackicons-validating="glyphicon glyphicon-refresh">
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Contrase&ntilde;a actual</label>
                            <div class="col-sm-10">
                                <input type="password" class="form-control" name="contraseniaActual" id="contraseniaActual" placeholder="Contraseña actual"
                                       required="true" 
                                       data-bv-notempty="true"
                                       data-bv-notempty-message="La contraseña actual es requerida"
                                       data-bv-stringlength="true" 
                                       data-bv-stringlength-min="6" 
                                       data-bv-stringlength-max="12" 
                                       data-bv-stringlength-message="Tu contraseña debe tener mínimo 6 caracteres"/>
                            </div>
                        </div>   
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Contrase&ntilde;a nueva</label>
                            <div class="col-sm-10">
                                <input type="password" class="form-control" name="contraseniaNueva" id="contraseniaNueva" placeholder="Nueva contraseña"
                                       required="true" 
                                       data-bv-notempty="true"
                                       data-bv-notempty-message="La nueva contraseña es requerida"
                                       pattern="(?!.*[ 	/.=\[\]:])(?=.*[a-zA-Z\d])(?=.*[$@!%?&]).{8}" 
                                       data-bv-regexp-message="La contraseña debe estar conformada por letras, números y algún caracter especial: $@!%?&.-_"
                                       data-bv-stringlength="true" 
                                       data-bv-stringlength-min="8" 
                                       data-bv-stringlength-max="8" 
                                       data-bv-stringlength-message="Tu contraseña debe ser de 8 caracteres" 
                                       maxlength="8"
                                       />
                            </div>
                        </div> 
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Repite contrase&ntilde;a nueva</label>
                            <div class="col-sm-10">
                                <input type="password" class="form-control"  name="contraseniaNuevaRepetir" id="contraseniaNuevaRepetir" placeholder="Nueva contraseña"
                                       required="true" 
                                       data-bv-notempty="true"
                                       data-bv-notempty-message="Debes repetir la contraseña nueva"
                                       pattern="(?!.*[ 	/.=\[\]:])(?=.*[a-zA-Z\d])(?=.*[$@!%?&]).{8}" 
                                       data-bv-regexp-message="La contraseña debe estar conformada por letras, números y algún caracter especial: $@!%?&.-_"
                                       data-bv-stringlength="true" 
                                       data-bv-stringlength-min="8" 
                                       data-bv-stringlength-max="8" 
                                       data-bv-stringlength-message="Tu contraseña debe ser de 8 caracteres"
                                       maxlength="8"
                                       />
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-xs-12">
                                <input type="submit" id="buscar" class="btn btn-primary pull-right" value="Cambiar contraseña"/>
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
            $("#passwordForm").bootstrapValidator();
        });
    </script>
</content>