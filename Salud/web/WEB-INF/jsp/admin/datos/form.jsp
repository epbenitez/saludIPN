<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Editar Datos</title>
    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css" />
    <link href="/vendors/bootstrap-toggle-master/css/bootstrap2-toggle.min.css" rel="stylesheet">
</head> 

<content tag="tituloJSP">
    Datos Personales
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
                    <form id="usuarioForm" action="/admin/guardaDatos.action" method="post" class="form-horizontal">
                        <div class="form-group" >
                                <s:hidden id="usuario"
                                             cssClass="form-control"
                                             name="usuario.usuario" 
                                             data-bv-notempty="true"
                                             data-bv-notempty-message="El usuario es requerido"
                                             placeholder="Usuario"
                                             show="false"
                                             />
                        </div>
                        <div class="form-group">
                                <s:hidden id="password"
                                             cssClass="form-control"
                                             name="usuario.password" 
                                             data-bv-message="Este dato no es válido"
                                             required="true" 
                                             pattern="(?!.*[ 	/.=\[\]:])(?=.*[a-zA-Z\d])(?=.*[$@!%?&]).{8}"
                                             data-bv-notempty="true"
                                             data-bv-notempty-message="La contraseña es requerida"
                                             data-bv-regexp-message="La contraseña debe estar conformada por letras, números y algún caracter especial: $@!%?&"
                                             data-bv-stringlength="true" 
                                             data-bv-stringlength-min="8" 
                                             data-bv-stringlength-max="8" 
                                             data-bv-stringlength-message="Tu contraseña debe ser de 8 caracteres"
                                             />
                        </div>
                        <div class="form-group">
                                <s:hidden id="passwordRep"
                                             cssClass="form-control"
                                             data-bv-message="Este dato no es válido"
                                             required="true" 
                                             pattern="(?!.*[ 	/.=\[\]:])(?=.*[a-zA-Z\d])(?=.*[$@!%?&]).{8}" 
                                             data-bv-notempty="true"
                                             data-bv-notempty-message="La contraseña es requerida"
                                             data-bv-regexp-message="La contraseña debe estar conformada por letras, números y algún caracter especial: $@!%?&"
                                             data-bv-stringlength="true" 
                                             data-bv-stringlength-min="8" 
                                             data-bv-stringlength-max="8" 
                                             data-bv-stringlength-message="Tu contraseña debe ser de 8 caracteres"
                                             placeholder="********"
                                             />
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Nombre(s)</label>
                            <div class="col-sm-10">
                                <s:textfield id="nombres"
                                             cssClass="form-control"
                                             name="personalAdministrativo.nombre" 
                                             data-bv-notempty="true"
                                             data-bv-message="Este dato no es válido"
                                             data-bv-notempty-message="El nombre es requerido"
                                             pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s\.]+$" 
                                             data-bv-regexp-message="Tu nombre sólo puede estar conformado por letras"
                                             data-bv-stringlength="true" 
                                             data-bv-stringlength-min="3" 
                                             data-bv-stringlength-max="50" 
                                             data-bv-stringlength-message="Tu nombre debe tener de 3 a 50 letras"
                                             placeholder="Nombre(s)"
                                             />
                                <span class="help-block" id="nombresMessage" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Apellido paterno</label>
                            <div class="col-sm-10">
                                <s:textfield id="apPaterno"
                                             cssClass="form-control"
                                             name="personalAdministrativo.apellidoPaterno" 
                                             data-bv-notempty="true"
                                             data-bv-message="Este dato no es válido"
                                             required="true" 
                                             data-bv-notempty-message="El apellido paterno es requerido"
                                             pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s]+$" 
                                             data-bv-regexp-message="Tu apellido sólo puede estar conformado por letras"
                                             data-bv-stringlength="true" 
                                             data-bv-stringlength-min="2" 
                                             data-bv-stringlength-max="60" 
                                             data-bv-stringlength-message="Tu apellido debe tener de 2 a 60 letras"
                                             placeholder="Apellido paterno"
                                             />
                                <span class="help-block" id="apPaternoMessage" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Apellido materno</label>
                            <div class="col-sm-10">
                                <s:textfield id="apMaterno"
                                             cssClass="form-control"
                                             name="personalAdministrativo.apellidoMaterno" 
                                             data-bv-message="Este dato no es válido"
                                             required="true" 
                                             data-bv-notempty="true"
                                             data-bv-notempty-message="El apellido materno es requerido"
                                             pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s]+$" 
                                             data-bv-regexp-message="Tu apellido sólo puede estar conformado por letras"
                                             data-bv-stringlength="true" 
                                             data-bv-stringlength-min="3" 
                                             data-bv-stringlength-max="50" 
                                             data-bv-stringlength-message="Tu apellido debe tener de 3 a 50 letras"
                                             placeholder="Apellido materno"
                                             />
                                <span class="help-block" id="apMaternoMessage" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">CURP</label>
                            <div class="col-sm-10">
                                <s:textfield id="curp"
                                             cssClass="form-control"
                                             name="personalAdministrativo.curp" 
                                             placeholder="CURP"
                                             data-bv-message="Este dato no es válido"
                                             required="true" 
                                             data-bv-notempty="true"
                                             data-bv-notempty-message="El CURP es requerido"
                                             pattern="^[a-zA-Z]{4}((\d{2}((0[13578]|1[02])(0[1-9]|[12]\d|3[01])|(0[13456789]|1[012])(0[1-9]|[12]\d|30)|02(0[1-9]|1\d|2[0-8])))|([02468][048]|[13579][26])0229)(H|M)(AS|BC|BS|CC|CL|CM|CS|CH|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|SM|NE)([a-zA-Z]{3})([a-zA-Z0-9\s]{1})\d{1}$" 
                                             data-bv-regexp-message="Tu CURP no tiene el formato correcto"
                                             data-bv-stringlength="true" 
                                             data-bv-stringlength-min="18" 
                                             data-bv-stringlength-max="18" 
                                             data-bv-stringlength-message="Tu CURP debe estar conformado por 18 caracteres"
                                             onblur="$(this).val($(this).val().toUpperCase())"
                                             />
                                <span class="help-block" id="estadoMessage" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Unidad Académica</label>
                            <div class="col-sm-10">
                                <s:select id="unidadAcademica"  
                                          cssClass="form-control"
                                          name="personalAdministrativo.unidadAcademica.id"
                                          list="ambiente.unidadAcademicaListx" 
                                          listKey="id" 
                                          listValue="nombreCorto" 
                                          headerKey=""
                                          data-bv-notempty="true"
                                          data-bv-notempty-message="La Unidad Académica es requerida"
                                          headerValue="-- Seleccione una Unidad Académica --" 
                                          disabled="true"
                                          />
                                <span class="help-block" id="unidadAcademicaMessage" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Teléfono</label>
                            <div class="col-sm-10">
                                <s:textfield id="telefono"
                                             cssClass="form-control"
                                             name="personalAdministrativo.telefono" 
                                             data-bv-message="Este dato no es válido"
                                             required="true" 
                                             data-bv-notempty="true"
                                             data-bv-notempty-message="El teléfono es requerido"
                                             pattern="\d{2,3}-\d{7,8}" 
                                             data-bv-regexp-message="Tu teléfono debe estar conformado por números. Un guión separa la clave LADA: 55-55555555"
                                             data-bv-stringlength="true" 
                                             data-bv-stringlength-min="11" 
                                             data-bv-stringlength-max="11" 
                                             data-bv-stringlength-message="Tu teléfono debe ser de 10 caracteres numéricos"
                                             placeholder="55-55555555"
                                             />
                                <span class="help-block" id="telefonoMessage" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Extensión</label>
                            <div class="col-sm-10">
                                <s:textfield id="extension"
                                             cssClass="form-control"
                                             name="personalAdministrativo.extension" 
                                             placeholder="563"
                                             pattern="^[\d]{1,6}$"
                                             data-bv-regexp-message="El campo no es válido"
                                             />
                                <span class="help-block" id="extensionMessage" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Correo electrónico</label>
                            <div class="col-sm-10">
                                <s:textfield id="correo"
                                             cssClass="form-control"
                                             name="personalAdministrativo.correoElectronico" 
                                             placeholder="correo@dominio.com"
                                             data-bv-emailaddress="true"
                                             data-bv-emailaddress-message="El correo electrónico que proporcionaste no parece ser válido. Por favor, verifica."
                                             data-bv-message="El correo electrónico que proporcionaste no parece ser válido. Por favor, verifica."
                                             required="true"
                                             pattern="^[a-zA-Z\d-_]+@[a-zA-Z]+([\.][a-zA-Z]{1,3}){1,2}$"
                                             data-bv-regexp-message="El correo no es válido"
                                             data-bv-notempty="true"
                                             data-bv-notempty-message="El correo electrónico es requerido"
                                             data-bv-stringlength="true" 
                                             data-bv-stringlength-min="6" 
                                             data-bv-stringlength-max="30" 
                                             data-bv-stringlength-message="Tu correo debe ser mínimo de 6 caracteres y máximo 30 caracteres"
                                             />
                                <span class="help-block" id="correoMessage" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Rol</label>
                            <div class="col-sm-10">
                                <s:select id="rol"  
                                          cssClass="form-control"
                                          name="privilegio"
                                          list="roles" 
                                          listKey="id" 
                                          listValue="clave" 
                                          data-bv-notempty="true"
                                          data-bv-notempty-message="El rol es requerido"
                                          headerKey=""
                                          headerValue="-- Seleccione un rol --"
                                          disabled="true"
                                          />
                                <span class="help-block" id="rolMessage" />
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-2 control-label">Cargo</label>
                            <div class="col-sm-10">
                                <s:select id="cargo"  
                                          cssClass="form-control"
                                          name="personalAdministrativo.cargo.id"
                                          list="ambiente.cargoList" 
                                          listKey="id" 
                                          listValue="descripcion" 
                                          headerKey=""
                                          data-bv-notempty="true"
                                          data-bv-notempty-message="El cargo es requerido"
                                          headerValue="-- Seleccione un cargo --" 
                                          disabled="true"
                                          />
                                <span class="help-block" id="cargoMessage" />
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-12">
                                <s:hidden name="personalAdministrativo.id" />
                                <s:hidden name="personalAdministrativo.fechaAlta" />
                                <s:hidden name="usuario.id" />
                                <s:hidden name="personalAdministrativo.cargo.id" />
                                <s:hidden name="privilegio" />
                                <s:hidden name="personalAdministrativo.unidadAcademica.id" />

                                <button id="guardar" type="submit" class="btn btn-primary pull-right">Guardar usuario</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

</body>

<content tag="endScripts">
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>
    <script type="text/javascript" src="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.js"></script>
    <script type="text/javascript" src="/vendors/bootstrap-toggle-master/js/toogle.js"></script>
</content>

<content tag="inlineScripts">
    <script>

    </script>
</content>