<%-- 
    Document   : mostrar
    Created on : 26/10/2017, 11:07:12 AM
    Author     : Tania G. Sánchez Manilla
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css" />    
    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap-datepicker/css/bootstrap-datepicker.min.css" />
    <link rel="stylesheet" type="text/css" href="/vendors/kartik-bootstrap-input/fileinput.min.css"/>
    <title>Datos bancarios</title>
</head> 

<content tag="tituloJSP">
    Datos bancarios
</content>

<body>
    <!-- Ejemplo de caja blanca -->
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
    </div> <!-- Termina ejemplo caja blanca -->

    <div class="row">
        <!--Alumno sin datos bancarios pero con cuenta/clabe.-->
        <s:if test="sinDatosConCuenta">
            <div class="col-xs-12">
                <div class="alert alert-info">
                    <i class="fa fa-info-circle fa-fw fa-lg"></i>
                    Has ingresado una cuenta/clabe por lo que no necesitas ingresar datos bancarios. Para modificar tu cuenta/clabe ingresa a 
                    <a href="/misdatos/configuracionCuentaBancaria.action" target="_blank">
                        Configuración de Cuenta
                    </a>.
                </div>
            </div>
        </s:if>
        <!--Alumno con datos bancarios.-->
        <s:else>
            <div class="col-xs-12">
                <div class="main-box clearfix">
                    <div class="main-box-body">
                        <form id="datos" class="form-horizontal" action="/misdatos/guardaDatosBancarios.action" >
                            <!--Puede actualizar los datos bancarios-->
                            <s:if test="actualizarDatosBanco">
                                <s:if test="mensajeEstatusTarjeta != null">
                                    <div class="alert alert-danger fade in" id="msjSts">
                                        <i class="fa fa-times-circle fa-fw fa-lg"></i>
                                        <s:property value="mensajeEstatusTarjeta" />
                                    </div>
                                </s:if>
                                <s:if test="menorEdad">
                                    <div class="col-sm-12 form-group">
                                        <p><strong>Recuerda que es tu responsabilidad mantener tus datos actualizados.</strong></p>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-3 control-label">Nombre(s)</label>
                                        <div class="col-sm-8">
                                            <s:textfield cssClass="form-control" name="alumnoDatosBanco.nombre" id="nombreTutor" placeholder="Nombre(s)"
                                                         data-bv-message="Este dato no es válido."
                                                         required="true" 
                                                         data-bv-notempty="true"
                                                         data-bv-notempty-message="El nombre es requerido."
                                                         pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s]+$" 
                                                         data-bv-regexp-message="El nombre sólo puede estar conformado por letras."
                                                         data-bv-stringlength="true" 
                                                         data-bv-stringlength-min="1" 
                                                         data-bv-stringlength-max="25" 
                                                         data-bv-stringlength-message="El nombre debe tener mínimo 1 letra."
                                                         onblur="$(this).val($(this).val().toUpperCase())"/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-3 control-label">Apellidos</label>
                                        <div class="col-sm-4">
                                            <s:textfield cssClass="form-control" name="alumnoDatosBanco.apellidoPaterno" id="apellidoPaternoTutor" placeholder="Apellido paterno"
                                                         data-bv-message="Este dato no es válido."
                                                         required="true" 
                                                         data-bv-notempty="true"
                                                         data-bv-notempty-message="El apellido paterno es requerido."
                                                         pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s]+$" 
                                                         data-bv-regexp-message="El apellido paterno sólo puede estar conformado por letras."
                                                         data-bv-stringlength="true" 
                                                         data-bv-stringlength-min="1" 
                                                         data-bv-stringlength-max="25" 
                                                         data-bv-stringlength-message="El apellido paterno debe tener mínimo 1 letra."
                                                         onblur="$(this).val($(this).val().toUpperCase())"/>
                                        </div>
                                        <div class="col-sm-4">
                                            <s:textfield cssClass="form-control" name="alumnoDatosBanco.apellidoMaterno" id="apellidoMaternoTutor" placeholder="Apellido materno"
                                                         data-bv-message="Este dato no es válido."
                                                         pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s]+$" 
                                                         data-bv-regexp-message="El apellido materno sólo puede estar conformado por letras."
                                                         data-bv-stringlength="true" 
                                                         data-bv-stringlength-min="1" 
                                                         data-bv-stringlength-max="25" 
                                                         data-bv-stringlength-message="El apellido materno debe tener mínimo 1 letra."
                                                         onblur="$(this).val($(this).val().toUpperCase())"/>
                                        </div>
                                    </div> 
                                    <div class="form-group">
                                        <label class="col-sm-3 control-label">Fecha de nacimiento</label>
                                        <div class="col-sm-8">
                                            <div class="input-group">
                                                <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                                <input type="text" class="form-control" id="fechaDeNacimientoTutor" name="fechaNacimientoTutor"
                                                       value="<s:date name="alumnoDatosBanco.fechaDeNacimiento" format="dd-MM-yyyy" />"
                                                       data-bv-notempty="true"
                                                       data-bv-message="Este dato no es válido"
                                                       onkeydown="return false"
                                                       required="true">
                                            </div>
                                            <span class="help-block">Formato dd-mm-yyyy</span>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-3 control-label">Correo electrónico</label>
                                        <div class="col-sm-8">
                                            <s:textfield name="alumnoDatosBanco.correoElectronico" id="correoElectronicoTutor" placeholder="ejemplo@gmail.com"
                                                         cssClass="form-control"
                                                         data-bv-emailaddress="true"
                                                         data-bv-emailaddress-message="El correo electrónico que proporcionaste no parece ser válido. Por favor, verifica."
                                                         data-bv-message="El correo electrónico que proporcionaste no parece ser válido. Por favor, verifica."
                                                         required="true" 
                                                         pattern="^[a-zA-Z\d-_.]+@[a-zA-Z]+([\.][a-zA-Z]{1,3}){1,2}$"
                                                         data-bv-regexp-message="El correo no es válido"
                                                         data-bv-notempty="true"
                                                         data-bv-notempty-message="El correo electrónico es requerido"
                                                         data-bv-stringlength="true" 
                                                         data-bv-stringlength-min="6" 
                                                         data-bv-stringlength-max="60" 
                                                         data-bv-stringlength-message="El correo electrónico debe ser mínimo de 6 caracteres y máximo 60 caracteres"
                                                         onblur="$(this).val($(this).val().toUpperCase())"/>
                                            <span class="help-block" id="nombreMessage" />
                                        </div>
                                    </div> 
                                    <div class="form-group">
                                        <label class="col-sm-3 control-label">Ocupación</label>
                                        <div class="col-sm-8">
                                            <s:textfield cssClass="form-control" name="alumnoDatosBanco.ocupacion" id="ocupacionTutor" placeholder="Ocupación"
                                                         data-bv-message="Este dato no es válido."
                                                         required="true" 
                                                         data-bv-notempty="true"
                                                         data-bv-notempty-message="La ocupación es requerido."
                                                         pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s]+$" 
                                                         data-bv-regexp-message="La ocupación sólo puede estar conformado por letras."
                                                         data-bv-stringlength="true" 
                                                         data-bv-stringlength-min="1" 
                                                         data-bv-stringlength-max="25" 
                                                         data-bv-stringlength-message="La ocupación debe tener mínimo 1 letra."
                                                         onblur="$(this).val($(this).val().toUpperCase())"/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-3 control-label">Estado civil</label>
                                        <div class="col-sm-8">
                                            <s:select id="civilTutor" cssClass="form-control"
                                                      list="ambiente.estadoCivil" listValue="nombre" listKey="id" headerKey=""
                                                      headerValue="-- Selecciona tu estado civil --"
                                                      name="alumnoDatosBanco.estadoCivil.id"
                                                      data-bv-notempty="true"
                                                      data-bv-notempty-message="El estado civil es requerido" />
                                            <span class="help-block" id="civilMessage" />
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-3 control-label">Género</label>
                                        <div class="col-sm-8">
                                            <s:select id="generoTutor" cssClass="form-control"
                                                      list="ambiente.generoList" listValue="nombre" listKey="id" headerKey=""
                                                      headerValue="-- Selecciona  --"
                                                      name="alumnoDatosBanco.genero.id"
                                                      data-bv-notempty="true"
                                                      data-bv-notempty-message="El género es requerido"/>
                                            <span class="help-block" id="generoMessage"/>
                                        </div>
                                    </div>
                                </s:if>
                                <s:if test="datosTutor">
                                    El SIBec ha detectado que eres un alumno mayor de edad, para dar de alta una cuenta a tu nombre es necesario que cuentes con INE 
                                    (Credencial para votar). En caso de aún no contar con ella, puedes registrar los datos de tu padre o tutor.<br>
                                    <div class="col-sm-12 form-group" style="padding-left: 35px">
                                        <div class="checkbox-nice">
                                            <input type="checkbox" 
                                                   name="mayorEdadConTutor"
                                                   id="mayorEdadConTutor" 
                                                   value="check"
                                                   onclick="mostrarDatosTutor()">
                                            <label for="mayorEdadConTutor">
                                                Soy alumno mayor de edad y debido a que no cuento con INE, deseo solicitar el alta de una cuenta a nombre de mi padre o tutor.
                                            </label>
                                        </div>
                                    </div> 
                                </s:if>
                                <div id = "informacionTutor">
                                    <s:if test="datosTutor">
                                        <div class="col-sm-12 form-group">
                                            <strong>Recuerda que en esta sección debes ingresar los datos de tu padre o tutor. </strong>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-3 control-label">Nombre(s)</label>
                                            <div class="col-sm-8">
                                                <s:textfield cssClass="form-control" name="alumnoDatosBanco.nombre" id="nombreTutor" placeholder="Nombre(s)"
                                                             data-bv-message="Este dato no es válido."
                                                             required="true" 
                                                             data-bv-notempty="true"
                                                             data-bv-notempty-message="El nombre es requerido."
                                                             pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s]+$" 
                                                             data-bv-regexp-message="El nombre sólo puede estar conformado por letras."
                                                             data-bv-stringlength="true" 
                                                             data-bv-stringlength-min="1" 
                                                             data-bv-stringlength-max="25" 
                                                             data-bv-stringlength-message="El nombre debe tener mínimo 1 letra."
                                                             onblur="$(this).val($(this).val().toUpperCase())"
                                                             disabled = "true"/>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-3 control-label">Apellidos</label>
                                            <div class="col-sm-4">
                                                <s:textfield cssClass="form-control" name="alumnoDatosBanco.apellidoPaterno" id="apellidoPaternoTutor" placeholder="Apellido paterno"
                                                             data-bv-message="Este dato no es válido."
                                                             required="true" 
                                                             data-bv-notempty="true"
                                                             data-bv-notempty-message="El apellido paterno es requerido."
                                                             pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s]+$" 
                                                             data-bv-regexp-message="El apellido paterno sólo puede estar conformado por letras."
                                                             data-bv-stringlength="true" 
                                                             data-bv-stringlength-min="1" 
                                                             data-bv-stringlength-max="25" 
                                                             data-bv-stringlength-message="El apellido paterno debe tener mínimo 1 letra."
                                                             onblur="$(this).val($(this).val().toUpperCase())"
                                                             disabled = "true"/>
                                            </div>
                                            <div class="col-sm-4">
                                                <s:textfield cssClass="form-control" name="alumnoDatosBanco.apellidoMaterno" id="apellidoMaternoTutor" placeholder="Apellido materno"
                                                             data-bv-message="Este dato no es válido."
                                                             pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s]+$" 
                                                             data-bv-regexp-message="El apellido materno sólo puede estar conformado por letras."
                                                             data-bv-stringlength="true" 
                                                             data-bv-stringlength-min="1" 
                                                             data-bv-stringlength-max="25" 
                                                             data-bv-stringlength-message="El apellido materno debe tener mínimo 1 letra."
                                                             onblur="$(this).val($(this).val().toUpperCase())"/>
                                            </div>
                                        </div> 
                                        <div class="form-group">
                                            <label class="col-sm-3 control-label">Fecha de nacimiento</label>
                                            <div class="col-sm-8">
                                                <div class="input-group">
                                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                                    <input type="text" class="form-control" id="fechaDeNacimientoTutor" name="fechaNacimientoTutor"
                                                           value="<s:date name="alumnoDatosBanco.fechaDeNacimiento" format="dd-MM-yyyy" />"
                                                           data-bv-notempty="true"
                                                           data-bv-message="Este dato no es válido"
                                                           onkeydown="return false"
                                                           required="true">
                                                </div>
                                                <span class="help-block">Formato dd-mm-yyyy</span>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-3 control-label">Correo electrónico</label>
                                            <div class="col-sm-8">
                                                <s:textfield name="alumnoDatosBanco.correoElectronico" id="correoElectronicoTutor" placeholder="ejemplo@gmail.com"
                                                             cssClass="form-control"
                                                             data-bv-emailaddress="true"
                                                             data-bv-emailaddress-message="El correo electrónico que proporcionaste no parece ser válido. Por favor, verifica."
                                                             data-bv-message="El correo electrónico que proporcionaste no parece ser válido. Por favor, verifica."
                                                             required="true" 
                                                             pattern="^[a-zA-Z\d-_.]+@[a-zA-Z]+([\.][a-zA-Z]{1,3}){1,2}$"
                                                             data-bv-regexp-message="El correo no es válido"
                                                             data-bv-notempty="true"
                                                             data-bv-notempty-message="El correo electrónico es requerido"
                                                             data-bv-stringlength="true" 
                                                             data-bv-stringlength-min="6" 
                                                             data-bv-stringlength-max="60" 
                                                             data-bv-stringlength-message="El correo electrónico debe ser mínimo de 6 caracteres y máximo 60 caracteres"
                                                             onblur="$(this).val($(this).val().toUpperCase())"
                                                             disabled = "true"/>
                                                <span class="help-block" id="nombreMessage" />
                                            </div>
                                        </div> 
                                        <div class="form-group">
                                            <label class="col-sm-3 control-label">Ocupación</label>
                                            <div class="col-sm-8">
                                                <s:textfield cssClass="form-control" name="alumnoDatosBanco.ocupacion" id="ocupacionTutor" placeholder="Ocupación"
                                                             data-bv-message="Este dato no es válido."
                                                             required="true" 
                                                             data-bv-notempty="true"
                                                             data-bv-notempty-message="La ocupación es requerido."
                                                             pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s]+$" 
                                                             data-bv-regexp-message="La ocupación sólo puede estar conformado por letras."
                                                             data-bv-stringlength="true" 
                                                             data-bv-stringlength-min="1" 
                                                             data-bv-stringlength-max="25" 
                                                             data-bv-stringlength-message="La ocupación debe tener mínimo 1 letra."
                                                             onblur="$(this).val($(this).val().toUpperCase())"
                                                             disabled = "true"/>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-3 control-label">Estado civil</label>
                                            <div class="col-sm-8">
                                                <s:select id="civilTutor" cssClass="form-control"
                                                          list="ambiente.estadoCivil" listValue="nombre" listKey="id" headerKey=""
                                                          headerValue="-- Selecciona tu estado civil --"
                                                          name="alumnoDatosBanco.estadoCivil.id"
                                                          data-bv-notempty="true"
                                                          data-bv-notempty-message="El estado civil es requerido"
                                                          disabled = "true"/>
                                                <span class="help-block" id="civilMessage" />
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-3 control-label">Género</label>
                                            <div class="col-sm-8">
                                                <s:select id="generoTutor" cssClass="form-control"
                                                          list="ambiente.generoList" listValue="nombre" listKey="id" headerKey=""
                                                          headerValue="-- Selecciona  --"
                                                          name="alumnoDatosBanco.genero.id"
                                                          data-bv-notempty="true"
                                                          data-bv-notempty-message="El género es requerido"
                                                          disabled = "true"/>
                                                <span class="help-block" id="generoMessage"/>
                                            </div>
                                        </div>
                                    </s:if>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label">Calle</label>
                                    <div class="col-sm-8">
                                        <s:textfield cssClass="form-control" name="alumnoDatosBanco.calle" id="calleBanco" placeholder="Calle"
                                                     data-bv-message="Este dato no es válido."
                                                     required="true" 
                                                     data-bv-notempty="true"
                                                     data-bv-notempty-message="La calle es requerida."
                                                     pattern="^[-a-zA-ZáéíóúñÁÉÍÓÚÑ0-9.#/_\s]+$"
                                                     data-bv-regexp-message="Tu calle debe estar conformado por letras y/o números."
                                                     data-bv-stringlength="true" 
                                                     data-bv-stringlength-min="1" 
                                                     data-bv-stringlength-max="100" 
                                                     data-bv-stringlength-message="Tu calle debe ser de al menos 1 caracter." 
                                                     onblur="$(this).val($(this).val().toUpperCase())"/>
                                        <span class="help-block" id="nombreMessage" />
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label">Número</label>
                                    <div class="col-sm-4">
                                        <s:textfield cssClass="form-control" name="alumnoDatosBanco.numeroExterior" id="numeroExteriorBanco" placeholder="Número Exterior"
                                                     data-bv-message="Este dato no es válido."
                                                     required="true" 
                                                     data-bv-notempty="true"
                                                     data-bv-notempty-message="El número exterior es requerido."
                                                     pattern="^[-a-zA-ZáéíóúñÁÉÍÓÚÑ0-9.#/_\s]+$"
                                                     data-bv-regexp-message="El número exterior sólo puede estar conformado por letras y/o números."
                                                     data-bv-stringlength="true" 
                                                     data-bv-stringlength-min="0" 
                                                     data-bv-stringlength-max="60" 
                                                     data-bv-stringlength-message="El número exterior excedió el número máximo de caracteres." 
                                                     onblur="$(this).val($(this).val().toUpperCase())"/>
                                        <span class="help-block" id="paternoMessage" />
                                    </div>
                                    <div class="col-sm-4">
                                        <s:textfield cssClass="form-control" name="alumnoDatosBanco.numeroInterior" id="numeroInteriorBanco" placeholder="Número Interior"
                                                     pattern="^[-a-zA-ZáéíóúñÁÉÍÓÚÑ0-9.#/_\s]+$"
                                                     data-bv-regexp-message="El número interior sólo puede estar conformado por letras y/o números."
                                                     data-bv-stringlength="true" 
                                                     data-bv-stringlength-min="0" 
                                                     data-bv-stringlength-max="60" 
                                                     data-bv-stringlength-message="El número interior excedió el número máximo de caracteres." 
                                                     onblur="$(this).val($(this).val().toUpperCase())"/>
                                        <span class="help-block" id="maternoMessage" />
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label">Estado</label>
                                    <div class="col-sm-8">
                                        <s:textfield cssClass="form-control" name="alumnoDatosBanco.estado" id="estadoBanco" placeholder="Estado"
                                                     data-bv-message="Este dato no es válido."
                                                     required="true" 
                                                     data-bv-notempty="true"
                                                     data-bv-notempty-message="El estado es requerido."
                                                     pattern="^[-a-zA-ZáéíóúñÁÉÍÓÚÑ0-9.#/_\s]+$"
                                                     data-bv-regexp-message="Tu estado debe estar conformado por letras."
                                                     data-bv-stringlength="true" 
                                                     data-bv-stringlength-min="1" 
                                                     data-bv-stringlength-max="100" 
                                                     data-bv-stringlength-message="Tu calle debe ser de al menos 1 caracter." 
                                                     onblur="$(this).val($(this).val().toUpperCase())"/>
                                        <span class="help-block" id="nombreMessage" />
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label">Municipio</label>
                                    <div class="col-sm-8">
                                        <s:textfield cssClass="form-control" name="alumnoDatosBanco.municipio" id="municipioBanco" placeholder="Municipio"
                                                     data-bv-message="Este dato no es válido."
                                                     required="true" 
                                                     data-bv-notempty="true"
                                                     data-bv-notempty-message="La calle es requerida."
                                                     pattern="^[-a-zA-ZáéíóúñÁÉÍÓÚÑ0-9.#/_\s]+$"
                                                     data-bv-regexp-message="Tu municipio debe estar conformado por letras."
                                                     data-bv-stringlength="true" 
                                                     data-bv-stringlength-min="1" 
                                                     data-bv-stringlength-max="100" 
                                                     data-bv-stringlength-message="Tu calle debe ser de al menos 1 caracter." 
                                                     onblur="$(this).val($(this).val().toUpperCase())"/>
                                        <span class="help-block" id="nombreMessage" />
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label">Colonia</label>
                                    <div class="col-sm-8">
                                        <s:textfield cssClass="form-control" name="alumnoDatosBanco.colonia" id="coloniaBanco" placeholder="Colonia"
                                                     data-bv-message="Este dato no es válido."
                                                     required="true" 
                                                     data-bv-notempty="true"
                                                     data-bv-notempty-message="La colonia es requerida."
                                                     pattern="^[-a-zA-ZáéíóúñÁÉÍÓÚÑ0-9.#/_\s]+$"
                                                     data-bv-regexp-message="Tu colonia debe estar conformado por letras y/o números.."
                                                     data-bv-stringlength="true" 
                                                     data-bv-stringlength-min="1" 
                                                     data-bv-stringlength-max="100" 
                                                     data-bv-stringlength-message="Tu colonia debe ser de al menos 1 caracter." 
                                                     onblur="$(this).val($(this).val().toUpperCase())"/>
                                        <span class="help-block" id="nombreMessage" />
                                    </div>
                                </div>   
                                <div class="form-group">
                                    <label class="col-sm-3 control-label">Código Postal</label>
                                    <div class="col-sm-8">
                                        <s:textfield cssClass="form-control" name="alumnoDatosBanco.codigoPostal" id="codigoPostalBanco" placeholder="Código Postal"
                                                     data-bv-message="Este dato no es válido"
                                                     required="true" 
                                                     data-bv-notempty="true"
                                                     data-bv-notempty-message="El código postal es requerido"
                                                     pattern="^[0-9]+$" 
                                                     data-bv-regexp-message="Tu código postal debe estar conformado sólo por números"
                                                     data-bv-stringlength="true" 
                                                     data-bv-stringlength-min="5" 
                                                     data-bv-stringlength-max="5" 
                                                     data-bv-stringlength-message="Tu código postal debe ser de 5 caracteres numéricos" />
                                        <span class="help-block" id="nombreMessage" />
                                    </div>
                                </div>
                                <a target="_blank" style="color:#56a9a8;margin-left: 20px; margin-right: 20px;" href="http://www.sibec.ipn.mx/avisos/cartel/manual_tarjetas.pdf" class="text-right">
                                    <i class="fa fa-download text-right" aria-hidden="true"></i>
                                    <b class="text-right">Descarga el manual aqui</b>
                                </a>
                                <div class="col-sm-12 form-group">
                                    <div class="col-sm-12">
                                        <br>
                                        <p style="margin-left: 20px; margin-right: 20px; text-justify: inter-word; text-align: justify;">
                                            En caso de resultar beneficiado, éstos datos serán utilizados para solicitar la generación de una cuenta bancaria, 
                                            en caso de no tener una ya asignada. Su captura no compromete al IPN a la asignación de una beca.
                                        </p>                                                                
                                    </div>
                                </div>
                                <div class="col-sm-12 form-group" style="padding-left: 35px">
                                    <div class="checkbox-nice">
                                        <input type="checkbox" 
                                               name="alumno.datosBancarios"
                                               id="datosBancarios" 
                                               required="true"
                                               value="check" 
                                               data-bv-notempty="true" 
                                               data-bv-notempty-message="Debes aceptar los términos para continuar."
                                               <s:if test="alumno.datosBancarios==1">
                                                   checked
                                               </s:if>
                                               >
                                        <label for="datosBancarios">
                                            He leído el manual y he ingresado mis datos de acuerdo a las indicaciones señaladas.                                                                    
                                        </label>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <s:if test="datosAcademicos">
                                        <div id="actualiza" class="col-sm-11">
                                            <s:hidden name="alumno.id"/>
                                            <s:hidden name="alumnoDatosBanco.id"/>
                                            <s:hidden name="alumnoDatosBanco.intentos" id="alumnoDatosBanco.intentos"/>
                                            <button id="actualizar-button" type="submit" class="btn btn-primary pull-right">Actualizar mis datos</button>
                                        </div>
                                    </s:if>
                                </div> 
                            </s:if>
                            <!--Los datos bancarios sólo son de lectura-->
                            <s:else>
                                <s:if test="mensajeEstatusTarjeta != null">
                                    <div class="alert alert-info fade in">
                                        <i class="fa fa-info-circle fa-fw fa-lg"></i>
                                        <s:property value="mensajeEstatusTarjeta" />
                                    </div>
                                </s:if>
                                <s:if test="vigenteTutor">
                                    <div class="col-sm-12 form-group">
                                        <strong>Recuerda que en esta sección debes ingresar los datos de tu padre o tutor. </strong>
                                    </div>
                                    <div class="col-sm-12 form-group">
                                        <label class="col-sm-3 control-label">Nombre(s)</label>
                                        <div class="col-sm-8">
                                            <s:textfield cssClass="form-control" name="alumnoDatosBanco.nombre" id="nombreTutor" placeholder="Nombre(s)" readonly="true"/>
                                        </div>
                                    </div>
                                    <div class="col-sm-12 form-group">
                                        <label class="col-sm-3 control-label">Apellidos</label>
                                        <div class="col-sm-4">
                                            <s:textfield cssClass="form-control" name="alumnoDatosBanco.apellidoPaterno" id="apellidoPaternoTutor" placeholder="Apellido paterno"
                                                         readonly="true"/>
                                        </div>
                                        <div class="col-sm-4">
                                            <s:textfield cssClass="form-control" name="alumnoDatosBanco.apellidoMaterno" id="apellidoMaternoTutor" placeholder="Apellido materno"
                                                         readonly="true"/>
                                        </div>
                                    </div> 
                                    <div class="col-sm-12 form-group">
                                        <label class="col-sm-3 control-label">Fecha de nacimiento</label>
                                        <div class="col-sm-8">
                                            <div class="input-group">
                                                <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                                <input type="text" class="form-control" id="fechaDeNacimientoTutor" name="fechaNacimientoTutor"
                                                       value="<s:date name="alumnoDatosBanco.fechaDeNacimiento" format="dd-MM-yyyy" />"
                                                       data-bv-notempty="true"
                                                       data-bv-message="Este dato no es válido"
                                                       onkeydown="return false"
                                                       required="true"
                                                       disabled>
                                            </div>
                                            <span class="help-block">Formato dd-mm-yyyy</span>
                                        </div>
                                    </div>
                                    <div class="col-sm-12 form-group">
                                        <label class="col-sm-3 control-label">Correo electrónico</label>
                                        <div class="col-sm-8">
                                            <s:textfield name="alumnoDatosBanco.correoElectronico" id="correoElectronicoTutor" placeholder="ejemplo@gmail.com"
                                                         cssClass="form-control" readonly="true"/>
                                            <span class="help-block" id="nombreMessage" />
                                        </div>
                                    </div> 
                                    <div class="col-sm-12 form-group">
                                        <label class="col-sm-3 control-label">Ocupación</label>
                                        <div class="col-sm-8">
                                            <s:textfield cssClass="form-control" name="alumnoDatosBanco.ocupacion" id="ocupacionTutor" placeholder="Ocupación" readonly="true"/>
                                        </div>
                                    </div>
                                    <div class="col-sm-12 form-group">
                                        <label class="col-sm-3 control-label">Estado civil</label>
                                        <div class="col-sm-8">
                                            <s:textfield cssClass="form-control" name="alumnoDatosBanco.estadoCivil.nombre" id="civilTutor" placeholder="Estado Civil" readonly="true"/>
                                        </div>
                                    </div>
                                    <div class="col-sm-12 form-group">
                                        <label class="col-sm-3 control-label">Género</label>
                                        <div class="col-sm-8">
                                            <s:textfield cssClass="form-control" name="alumnoDatosBanco.genero.nombre" id="generoTutor" placeholder="Género" readonly="true"/>
                                        </div>
                                    </div>
                                </s:if>
                                <div class="col-sm-12 form-group">
                                    <label class="col-sm-3 control-label">Calle</label>
                                    <div class="col-sm-8">
                                        <s:textfield cssClass="form-control" name="alumnoDatosBanco.calle" id="calleBanco" placeholder="Calle" readonly="true"/>
                                        <span class="help-block" id="nombreMessage" />
                                    </div>
                                </div>
                                <div class="col-sm-12 form-group">
                                    <label class="col-sm-3 control-label">Número</label>
                                    <div class="col-sm-4">
                                        <s:textfield cssClass="form-control" name="alumnoDatosBanco.numeroExterior" id="numeroExteriorBanco" placeholder="Número Exterior"
                                                     readonly="true"/>
                                        <span class="help-block" id="paternoMessage" />
                                    </div>
                                    <div class="col-sm-4">
                                        <s:textfield cssClass="form-control" name="alumnoDatosBanco.numeroInterior" id="numeroInteriorBanco" placeholder="Número Interior"
                                                     readonly="true"/>
                                        <span class="help-block" id="maternoMessage" />
                                    </div>
                                </div>
                                <div class="col-sm-12 col-sm-12 form-group">
                                    <label class="col-sm-3 control-label">Entidad</label>
                                    <div class="col-sm-8">
                                        <s:textfield cssClass="form-control" name="alumnoDatosBanco.estado" id="estado" placeholder="Entidad" readonly="true"/>
                                    </div>
                                </div>
                                <div class="col-sm-12 col-sm-12 form-group">
                                    <label class="col-sm-3 control-label">Municipio</label>
                                    <div class="col-sm-8">
                                        <s:textfield cssClass="form-control" name="alumnoDatosBanco.municipio" id="municipio" placeholder="Municipio" readonly="true"/>
                                    </div>
                                </div>
                                <div class="col-sm-12 col-sm-12 form-group">
                                    <label class="col-sm-3 control-label">Colonia</label>
                                    <div class="col-sm-8">
                                        <s:textfield cssClass="form-control" name="alumnoDatosBanco.colonia" id="colonia" placeholder="Colonia" readonly="true"/>
                                    </div>
                                </div>
                                <div class="col-sm-12 form-group">
                                    <label class="col-sm-3 control-label">Código Postal</label>
                                    <div class="col-sm-8">
                                        <s:textfield cssClass="form-control" name="alumnoDatosBanco.codigoPostal" id="codigoPostalBanco" placeholder="Código Postal"
                                                     readonly="true"/>
                                        <span class="help-block" id="nombreMessage" />
                                    </div>
                                </div>
                                <div class="col-sm-12 form-group" style="padding-left: 35px">
                                    <div class="checkbox-nice">
                                        <input type="checkbox" 
                                               name="alumno.datosBancarios"
                                               id="datosBancarios" 
                                               required="true"
                                               value="check" 
                                               data-bv-notempty="true" 
                                               data-bv-notempty-message="Debes aceptar los términos para continuar."
                                               checked disabled readonly="true">
                                        <label for="datosBancarios">
                                            He leído el manual y he ingresado mis datos de acuerdo a las indicaciones señaladas.                                                                    
                                        </label>
                                    </div>
                                </div>
                            </s:else>
                        </form>
                    </div>
                </div>
            </div>
        </s:else>
    </div>
</div>
</body>
<content tag="endScripts">        
    <script type="text/javascript" src="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.js"></script>        
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>
    <script type="text/javascript" src="/vendors/bootstrap-datepicker/js/bootstrap-datepicker.min.js"></script>
    <script type="text/javascript" src="/vendors/bootstrap-datepicker/locales/bootstrap-datepicker.es.min.js"></script>   
</content>

<content tag="inlineScripts">
    <script>
                                                           if (navigator.appName === 'Microsoft Internet Explorer') {
                                                               document.getElementById("ie").style.display = "block";
                                                               document.getElementById("actualizar-button").disabled = true;
                                                           }
                                                           $(document).ready(function () {
        <s:if test="mensajeEstatusTarjeta != null">
                                                               var msjToSearch = "No olvides revisar el manual";
                                                               var strongMsj = "<strong>No olvides revisar el manual</strong>";
                                                               var msjSts = $('#msjSts').html().replace(msjToSearch, strongMsj);
                                                               $('#msjSts').html(msjSts);
        </s:if>
                                                               if (navigator.appName === 'Microsoft Internet Explorer') {
                                                                   noUsesIE();
                                                               }
                                                               if ($("#fechaDeNacimientoTutor").val() === '')
                                                                   $("#fechaDeNacimientoTutor").val("01-01-1970");
                                                               $('#datos').bootstrapValidator({
                                                                   excluded: [':disabled'],
                                                               }).on('success.form.bv', function (e) {
                                                                   e.preventDefault();
                                                                   actualizarDialog();
                                                               });
                                                               $('#fechaDeNacimientoTutor').datepicker({
                                                                   language: 'es', format: 'dd-mm-yyyy'
                                                               });
                                                               $("#informacionTutor").hide();
                                                           });

                                                           function noUsesIE() {
                                                               document.getElementById("ie").style.display = "block";
                                                               $("#datos :input").prop('disabled', true);
                                                               $('#fechaDeNacimientoTutor').data("DateTimePicker").disable();
                                                           }

                                                           function actualizarDialog() {
                                                               ModalGenerator.notificacion({
                                                                   "titulo": "Atención",
                                                                   "cuerpo": "¿Estás seguro que tus datos son correctos?",
                                                                   "tipo": "WARNING",
                                                                   "funcionAceptar": function () {
                                                                       document.getElementById('datos').submit();
                                                                   },
                                                                   "funcionCancelar": function () {
                                                                       $('#actualizar-button').blur();
                                                                   },
                                                               });
                                                           }

                                                           function mostrarDatosTutor() {
                                                               if (document.getElementById('mayorEdadConTutor').checked) {
                                                                   $("#informacionTutor").show();
                                                                   $("#nombreTutor").removeAttr('disabled');
                                                                   $("#apellidoPaternoTutor").removeAttr('disabled');
                                                                   $("#correoElectronicoTutor").removeAttr('disabled');
                                                                   $("#ocupacionTutor").removeAttr('disabled');
                                                                   $("#civilTutor").removeAttr('disabled');
                                                                   $("#generoTutor").removeAttr('disabled');
                                                               } else {
                                                                   $("#informacionTutor").hide();
                                                                   $("#nombreTutor").attr('disabled', 'disabled');
                                                                   $("#apellidoPaternoTutor").attr('disabled', 'disabled');
                                                                   $("#correoElectronicoTutor").attr('disabled', 'disabled');
                                                                   $("#ocupacionTutor").attr('disabled', 'disabled');
                                                                   $("#civilTutor").attr('disabled', 'disabled');
                                                                   $("#generoTutor").attr('disabled', 'disabled');
                                                               }
                                                           }
    </script>
</content>