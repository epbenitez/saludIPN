<%-- 
    Document   : datosPersonales_edicion
    Created on : 10/12/2015, 11:37:32 AM
    Author     : Tania G. Sánchez Manilla
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css" />    
    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap-datepicker/css/bootstrap-datepicker.min.css" />
    <link rel="stylesheet" type="text/css" href="/vendors/kartik-bootstrap-input/fileinput.min.css"/>
    <title>Actualización de datos del alumno</title>

    <style>
        .btn-group-vertical{}
    </style>
</head> 

<content tag="tituloJSP">
    Actualización de datos del alumno
</content>

<body>
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
            <s:if test="noInscrito == 1">
                <div class="col-sm-12">
                    <div class="alert alert-warning" role="alert">
                        <i class="fa fa-warning fa-fw fa-lg"></i>
                        Tu número de boleta ha sido reportado en Gestión Escolar como no inscrito. 
                        <p>Podrás continuar con tu solicitud, sin embargo te recomendamos verificar tu situación en Gestión Escolar de tu Unidad Académica</p>
                    </div>
                </div>
            </s:if>
            <div id="ie" class="col-sm-12" style="display: none">
                <div class="alert alert-danger" role="alert">
                    <i class="fa fa-times-circle fa-fw fa-lg"></i>                    
                    La información mostrada en este formulario sólo se puede validar 
                    desde Chrome o Mozilla. Te recomendamos que lo intentes en
                    alguno de estos navegadores.
                </div>
            </div>
            <div class="alert alert-info">
                <i class="fa fa-info-circle fa-fw fa-lg"></i>
                Recuerda que es tu responsabilidad mantener tus datos actualizados.
            </div>
        </div>
    </div>
    <div class="row main_content">
        <div class="col-sm-12">
            <div class="main-box tabs-wrapper">

                <!-- Tabs Horizontales-->
                <ul class="nav nav-tabs hidden-sm hidden-xs">
                    <li><a href="#personales-tab" data-toggle="tab">Personales</a></li>
                    <li><a href="#academicos-tab" data-toggle="tab">Académicos</a></li>
                    <li><a href="#direccion-tab" data-toggle="tab">Dirección</a></li>
                    <li><a href="#contacto-tab" data-toggle="tab">Contacto</a></li>
                    <!--<li><a href="#oportunidades-tab" data-toggle="tab">Otros programas<i class="fa"></i></a></li>-->
                </ul>
                <!-- Tabs Verticales -->
                <div class="col-xs-12 btn-group-vertical hidden-md hidden-lg hidden-xl" style="padding-bottom: 20px">                        
                    <a href="#personales-tab" data-toggle="tab" class="btn btn-primary" style="color: white">Personales</a><br>
                    <a href="#academicos-tab" data-toggle="tab" class="btn btn-primary" style="color: white">Académicos</a><br>
                    <a href="#direccion-tab" data-toggle="tab" class="btn btn-primary" style="color: white">Dirección</a><br>
                    <a href="#contacto-tab" data-toggle="tab" class="btn btn-primary" style="color: white">Contacto</a><br>
                </div>
                <form id="datos" class="form-horizontal" action="/misdatos/guardaRegistro.action" >
                    <div class="tab-content">
                        <div class="tab-pane active" id="personales-tab">
                            <div class="form-group">
                                <label class="col-sm-3 control-label">Nombre(s)</label>
                                <div class="col-sm-6">
                                    <s:textfield cssClass="form-control" name="alumno.nombre" id="nombre" placeholder="Nombre"
                                                 readonly="true" onblur="$(this).val($(this).val().toUpperCase())"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">Apellidos</label>
                                <div class="col-sm-3">
                                    <s:textfield cssClass="form-control" name="alumno.apellidoPaterno" id="apellidoPaterno" placeholder="Apellido paterno"
                                                 readonly="true" onblur="$(this).val($(this).val().toUpperCase())"/>
                                </div>
                                <div class="col-sm-3">
                                    <s:textfield cssClass="form-control" name="alumno.apellidoMaterno" id="apellidoMaterno" placeholder="Apellido materno"
                                                 readonly="true" onblur="$(this).val($(this).val().toUpperCase())"/>
                                </div>
                            </div>
                            <s:if test="alumnoSUBES" > 
                                <div class="form-group">
                                    <label class="col-sm-3 control-label">CURP</label>
                                    <div class="col-sm-6">
                                        <s:textfield cssClass="form-control" name="alumno.curp" id="curp" placeholder="CURP"
                                                     readonly="true"/>
                                        <span class="help-block" id="estadoMessage" />
                                    </div>
                                </div>
                            </s:if>
                            <s:else>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label">CURP</label>
                                    <div class="col-sm-6">
                                        <s:textfield cssClass="form-control" name="alumno.curp" id="curp" placeholder="CURP"
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
                                                     onkeyup="datosCURP();"/>
                                        <span class="help-block" id="curpMessage" />
                                        <a href="https://consultas.curp.gob.mx/CurpSP/" target="_blank"> Consulta tu CURP </a>
                                    </div>
                                </div>
                            </s:else>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">Fecha de nacimiento</label>
                                <div class="col-sm-6">
                                    <div class="input-group">
                                        <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                        <input type="text" class="form-control" id="fechaDeNacimiento" name="fechaNacimiento"
                                               value="<s:date name="alumno.fechaDeNacimiento" format="dd-MM-yyyy" />"
                                               data-bv-notempty="true"
                                               data-bv-message="Este dato no es válido"
                                               onkeydown="return false"
                                               required="true"
                                               disabled="true">
                                    </div>
                                    <span class="help-block" style='color:#ff7751'>
                                        La fecha de nacimiento es un dato sensible y elemental para poder dar de alta una cuenta bancaria, 
                                        por lo que te suplícamos verifiques que esté correcta, de no ser así por favor corrige tu CURP 
                                        y en automático la fecha será actualizada
                                    </span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">Entidad de nacimiento</label>
                                <div class="col-sm-6">
                                    <s:select id="estadosDireccion"  cssClass="form-control"
                                              list="ambiente.entidadFederativa" listKey="id" listValue="nombre" headerKey=""
                                              headerValue="-- Selecciona un estado --"
                                              name="alumno.entidadDeNacimiento.id"
                                              data-bv-notempty="true"
                                              data-bv-notempty-message="La entidad de nacimiento es requerida"
                                              disabled="true"/>
                                    <span class="help-block" id="estadoMessage" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">G&eacute;nero</label>
                                <div class="col-sm-6">
                                    <s:select id="genero" 
                                              cssClass="form-control"
                                              list="ambiente.generoList" listValue="nombre" listKey="id" headerKey=""
                                              headerValue="-- Selecciona  --"
                                              name="alumno.genero.id"
                                              data-bv-notempty="true"
                                              data-bv-notempty-message="El género es requerido"
                                              disabled="true"/>
                                    <span class="help-block" id="generoMessage" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">Estado civil</label>
                                <div class="col-sm-6">
                                    <s:select name="alumno.estadoCivil.id" list="ambiente.estadoCivil"
                                              cssClass="form-control"
                                              listKey="id" listValue="nombre"
                                              headerKey=""
                                              headerValue="-- Selecciona --"
                                              data-bv-notempty="true"
                                              data-bv-notempty-message="El estado civil es requerido" />
                                    <span class="help-block" id="civilMessage" />
                                </div>
                            </div>
                        </div>
                        <div class="tab-pane" id="academicos-tab">
                            <div class="form-group">
                                <label class="col-sm-3 control-label">Boleta</label>
                                <div class="col-sm-6">
                                    <s:textfield cssClass="form-control" name="alumno.boleta" id="boleta" placeholder="Boleta" readonly="true" />
                                    <span class="help-block" id="nombreMessage" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">Unidad Acad&eacute;mica</label>
                                <div class="col-sm-6">
                                    <s:textfield cssClass="form-control" name="alumno.datosAcademicos.unidadAcademica.nombreCorto" id="boleta" placeholder="Unidad Académica" readonly="true"/>
                                    <span class="help-block" id="estadoMessage" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">Semestre</label>
                                <div class="col-sm-6">
                                    <s:textfield cssClass="form-control" name="alumno.datosAcademicos.semestre" id="semestre" placeholder="Semestre" readonly="true" />
                                    <span class="help-block" id="nombreMessage" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">Promedio</label>
                                <div class="col-sm-6">
                                    <input type="number" class="form-control" id = "prom" disabled>
                                    <s:hidden name="alumno.datosAcademicos.promedio" id="promedio"/>
                                    <span class="help-block" id="nombreMessage" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">Carrera</label>
                                <div class="col-sm-6">
                                    <s:textfield cssClass="form-control" name="alumno.datosAcademicos.carrera.carrera" id="carrera" placeholder="Carrera" readonly="true" />
                                    <span class="help-block" id="estadoMessage" />
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-3 control-label">Plan de estudios</label>
                                <div class="col-sm-6">
                                    <s:textfield cssClass="form-control" name="alumno.datosAcademicos.carrera.planEstudios" id="planDeEstudios" placeholder="Plan de estudios" readonly="true" />
                                    <span class="help-block" id="estadoMessage" />
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="checkbox-nice col-sm-offset-3">
                                    <input id="checkbox-egresado" type="checkbox" disabled>
                                    <label for="checkbox-egresado">
                                        Egresado
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="tab-pane" id="direccion-tab">
                            <div class="form-group">
                                <label class="col-sm-3 control-label">Calle</label>
                                <div class="col-sm-6">
                                    <s:textfield cssClass="form-control" name="alumno.direccion.calle" id="calle" placeholder="Calle"
                                                 data-bv-message="Este dato no es válido"
                                                 required="true" 
                                                 data-bv-notempty="true"
                                                 data-bv-notempty-message="La calle es requerida"
                                                 pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ0-9.\s]+$" 
                                                 data-bv-regexp-message="Tu calle debe estar conformado por números o letras"
                                                 data-bv-stringlength="true" 
                                                 data-bv-stringlength-min="1" 
                                                 data-bv-stringlength-max="100" 
                                                 data-bv-stringlength-message="Tu calle debe ser de al menos 1 caracter"
                                                 />
                                    <span class="help-block" id="nombreMessage" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">N&uacute;mero</label>
                                <div class="col-sm-3">
                                    <s:textfield cssClass="form-control" name="alumno.direccion.numeroExterior" id="numeroExterior" placeholder="Número Exterior"
                                                 data-bv-message="Este dato no es válido"
                                                 required="true" 
                                                 data-bv-notempty="true"
                                                 data-bv-notempty-message="El número exterior es requerido"
                                                 pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ0-9\s]+$" 
                                                 data-bv-regexp-message="El número exterior sólo puede estar conformado por letras y número"
                                                 data-bv-stringlength="true" 
                                                 data-bv-stringlength-min="0" 
                                                 data-bv-stringlength-max="60" 
                                                 data-bv-stringlength-message="El número exterior excedió el número máximo de caracteres"
                                                 />
                                    <span class="help-block" id="paternoMessage" />
                                </div>
                                <div class="col-sm-3">
                                    <s:textfield cssClass="form-control" name="alumno.direccion.numeroInterior" id="numeroInterior" placeholder="Número Interior"
                                                 pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ0-9\s]+$" 
                                                 data-bv-regexp-message="El número interior sólo puede estar conformado por letras y número"
                                                 data-bv-stringlength="true" 
                                                 data-bv-stringlength-min="0" 
                                                 data-bv-stringlength-max="60" 
                                                 data-bv-stringlength-message="El número interior excedió el número máximo de caracteres"
                                                 />
                                    <span class="help-block" id="maternoMessage" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">Entidad</label>
                                <div class="col-sm-6">
                                    <s:select id="estado"  cssClass="form-control"
                                              cssClass="form-control"
                                              list="ambiente.entidadFederativa" listKey="id" listValue="nombre" headerKey=""
                                              headerValue="-- Selecciona un estado --"
                                              name="alumno.direccion.relacionGeografica.estado.id"
                                              data-bv-notempty="true"
                                              data-bv-notempty-message="La entidad es requerida"
                                              onchange="getMunicipios()" />

                                    <span class="help-block" id="estadoMessage" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">Municipio</label>
                                <div class="col-sm-6">
                                    <s:select id="municipio"  cssClass="form-control"
                                              list="%{{}}"
                                              headerValue="-- Selecciona --"
                                              name="alumno.direccion.relacionGeografica.municipio.id"
                                              data-bv-notempty="true"
                                              data-bv-notempty-message="El municipio es requerido"
                                              onchange="getColonias(), getLocalidad()" />
                                    <span class="help-block" id="estadoMessage" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">Colonia</label>
                                <div class="col-sm-6">
                                    <s:select id="colonia"  cssClass="form-control" 
                                              list="%{{}}"
                                              headerValue="-- Selecciona --"
                                              name="alumno.direccion.relacionGeografica.colonia.id"
                                              data-bv-notempty="true"
                                              data-bv-notempty-message="La colonia es requerida" />
                                    <span class="help-block" id="estadoMessage" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">Localidad</label>
                                <div class="col-sm-6">
                                    <s:select id="localidad"  cssClass="form-control" 
                                              list="%{{}}"
                                              headerValue="-- Selecciona --"
                                              name="alumno.direccion.inegiLocalidad.id"
                                              data-bv-notempty="true"
                                              data-bv-notempty-message="La localidad es requerida" />
                                    <span class="help-block" id="estadoMessage" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">C&oacute;digo Postal</label>
                                <div class="col-sm-6">
                                    <s:textfield cssClass="form-control" name="alumno.direccion.codigoPostal" id="codigoPostal" placeholder="Código Postal"
                                                 data-bv-message="Este dato no es válido"
                                                 required="true" 
                                                 data-bv-notempty="true"
                                                 data-bv-notempty-message="El Código Postal es requerido"
                                                 pattern="^[0-9]+$" 
                                                 data-bv-regexp-message="Tu Código Postal debe estar conformado sólo por números"
                                                 data-bv-stringlength="true" 
                                                 data-bv-stringlength-min="5" 
                                                 data-bv-stringlength-max="5" 
                                                 data-bv-stringlength-message="Tu Código Postal debe ser de 5 caracteres numéricos" />
                                    <span class="help-block" id="nombreMessage" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">Tipo de vialidad</label>
                                <div class="col-sm-6">
                                    <s:select name="alumno.direccion.inegiTipoVialidad.id" list="ambiente.inegiTipoVialidad"
                                              cssClass="form-control"
                                              listKey="id" listValue="descripcion"
                                              headerKey=""
                                              headerValue="-- Selecciona --"
                                              data-bv-notempty="true"
                                              data-bv-notempty-message="El tipo de vialidad es requerido" />
                                    <span class="help-block" id="civilMessage" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">Tipo de asentamiento</label>
                                <div class="col-sm-6">
                                    <s:select name="alumno.direccion.inegiTipoAsentamiento.id" list="ambiente.inegiTipoAsentamiento"
                                              cssClass="form-control"
                                              listKey="id" listValue="descripcion"
                                              headerKey=""
                                              headerValue="-- Selecciona --"
                                              data-bv-notempty="true"
                                              data-bv-notempty-message="El tipo de asentamiento es requerido" />
                                    <span class="help-block" id="civilMessage" />
                                </div>
                            </div>
                        </div>
                        <div class="tab-pane" id="contacto-tab">
                            <div class="alert alert-info">
                                <i class="fa fa-info-circle fa-fw fa-lg"></i>
                                Debes verificar que tu correo electrónico sea correcto, y que tengas acceso a el, 
                                ya que es nuestro medio de comunicación contigo, incluyendo el envío de tus datos 
                                de acceso al sistema, si así lo requirieras.
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">Correo electr&oacute;nico</label>
                                <div class="col-sm-6">
                                    <s:textfield name="alumno.correoElectronico" id="correoElectronico" placeholder="ejemplo@gmail.com"
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
                                                 data-bv-stringlength-message="Tu correo electrónico debe ser mínimo de 6 caracteres y máximo 60 caracteres"
                                                 />
                                    <span class="help-block" id="nombreMessage" />
                                </div>
                            </div> 
                            <div class="form-group">
                                <label class="col-sm-3 control-label">Correo electr&oacute;nico alterno</label>
                                <div class="col-sm-6">
                                    <s:textfield cssClass="form-control" name="alumno.correoElectronicoAlterno" id="correoElectronicoAlterno" placeholder="ejemplo@gmail.com"
                                                 data-bv-emailaddress="true"
                                                 data-bv-emailaddress-message="El correo electrónico que proporcionaste no parece ser válido. Por favor, verifica."
                                                 data-bv-message="El correo electrónico que proporcionaste no parece ser válido. Por favor, verifica."
                                                 pattern="^[a-zA-Z\d-_.]+@[a-zA-Z]+([\.][a-zA-Z]{1,3}){1,2}$"
                                                 data-bv-regexp-message="El correo no es válido"/>
                                    <span class="help-block" id="nombreMessage" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">Teléfono celular</label>
                                <div class="col-sm-6">
                                    <s:textfield cssClass="form-control" name="alumno.celular" id="celular" placeholder="55-55555555"
                                                 data-bv-message="Este dato no es válido"
                                                 required="true" 
                                                 data-bv-notempty="true"
                                                 data-bv-notempty-message="El teléfono celular es requerido"
                                                 pattern="\d{2,3}-\d{7,8}" 
                                                 data-bv-regexp-message="Tu celular debe estar conformado por números. Un guión separa la clave LADA: 55-55555555"
                                                 data-bv-stringlength="true" 
                                                 data-bv-stringlength-min="11" 
                                                 data-bv-stringlength-max="11" 
                                                 data-bv-stringlength-message="Tu celular debe ser de 10 caracteres numéricos" />
                                    <span class="help-block" id="nombreMessage" />
                                </div>
                            </div> 
                            <div class="form-group">
                                <label class="col-sm-3 control-label">Compa&ntilde;&iacute;a celular</label>
                                <div class="col-sm-6">
                                    <s:select id="companiaCelular"  cssClass="form-control"
                                              list="ambiente.companiaCelularList" listKey="id" listValue="nombre" headerKey=""
                                              headerValue="-- Selecciona  --"
                                              name="alumno.companiaCelular.id"
                                              data-bv-notempty="true"
                                              data-bv-notempty-message="La entidad es requerida"
                                              />
                                    <span class="help-block" id="estadoMessage" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">Teléfono de casa</label>
                                <div class="col-sm-6">
                                    <s:textfield cssClass="form-control" name="alumno.telefonoCasa" id="telefonoCasa" placeholder="55-55555555"
                                                 data-bv-message="Este dato no es válido"
                                                 pattern="\d{2,3}-\d{7,8}" 
                                                 data-bv-regexp-message="Un guión separa la clave LADA: 55-55555555 o 555-5555555" />
                                    <span class="help-block" id="nombreMessage" />
                                </div>
                            </div>
                        </div>
                        <div class="tab-pane" id="oportunidades-tab">
                            <div class="form-group">
                                <label class="col-sm-3 control-label"> </label>
                                <div class="col-sm-6">
                                    <label for="beneficiarioOportunidades">
                                        &iquest;Es beneficiario del programa PROSPERA (antes Oportunidades)?
                                    </label>
                                    <div class="radio">
                                        <s:radio list="service.respuestaBoolean" name="alumno.beneficiarioOportunidades" />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <s:if test="datosAcademicos">
                            <div id="actualiza" class="col-sm-11">
                                <button id="actualizar-button" type="submit" class="btn btn-primary pull-right">Actualizar mis datos</button>
                            </div>
                        </s:if>
                    </div>  
            </div>
        </div>
    </form>
</div>
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
            var number = "<s:property value="alumno.datosAcademicos.promedio"/>";
            setCheckbox('checkbox-egresado', '<s:property value="alumno.datosAcademicos.egresado" />');
            if (number === "") {
                number = 0;
            }
            document.getElementById("prom").value = (number * 1).toFixed(2);
<s:if test="mensajeEstatusTarjeta != null">
            var msjToSearch = "No olvides revisar el manual";
            var strongMsj = "<strong>No olvides revisar el manual</strong>";
            var msjSts = $('#msjSts').html().replace(msjToSearch, strongMsj);
            $('#msjSts').html(msjSts);
</s:if>
            if (navigator.appName === 'Microsoft Internet Explorer') {
                noUsesIE();
            }
            var entidad = '<s:property value="alumno.direccion.relacionGeografica.estado.id" />';
            if (entidad.trim().length > 0)
                getMunicipios();
            $('#datos').bootstrapValidator({
                excluded: [':disabled'],
            }).on('success.form.bv', function (e) {
                e.preventDefault();
                actualizarDialog();
            });
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
        
        // Palomea checkbox
        function setCheckbox(id, value) {
            if (value === '1' || value === true) {
                $('#' + id).prop("checked", true);
            }
        }

        function getMunicipios() {
            $.ajax({
                type: 'POST',
                url: '/ajax/getDireccionMunicipios.action',
                dataType: 'json',
                data: {pkEstado: $('#estado').val()},
                cache: false,
                success: function (aData) {
                    $('#municipio').get(0).options.length = 0;
                    $('#municipio').get(0).options[0] = new Option("-- Selecciona --", "");
                    var selectedMun = "<s:property value="alumno.direccion.relacionGeografica.municipio.id" />";
                    $.each(aData.data, function (i, item) {
                        $('#municipio').get(0).options[$('#municipio').get(0).options.length] = new Option(item[1], item[0]);
                        if (selectedMun !== "") {
                            if (item[0] == selectedMun) {
                                $('#municipio  option[value=' + item[0] + ']').attr('selected', 'selected');
                            }
                        }
                    });
                },
                error: function () {
                    ModalGenerator.notificacion({
                        "titulo": "Error",
                        "cuerpo": "Hubo un problema que impidió que se completara la operación.",
                        "tipo": "ALERT",
                    });
                }
            });
            var selectedMun = "<s:property value="alumno.direccion.relacionGeografica.municipio.id" />";
            if (selectedMun !== "") {
                $('#municipio').change();
            }

            return false;
        }

        function getColonias() {
            var municipioId = $('#municipio').val() === null ? '<s:property value="alumno.direccion.relacionGeografica.municipio.id" />' : $('#municipio').val();
            $.ajax({
                type: 'POST',
                url: '/ajax/getDireccionColonias.action',
                dataType: 'json',
                data: {pkEstado: $('#estado').val(),
                    pkMunicipio: municipioId
                },
                cache: false,
                success: function (aData) {
                    $('#colonia').get(0).options.length = 0;
                    $('#colonia').get(0).options[0] = new Option("-- Selecciona --", "");
                    var selectedCol = "<s:property value="alumno.direccion.relacionGeografica.colonia.id" />";
                    $.each(aData.data, function (i, item) {
                        $('#colonia').get(0).options[$('#colonia').get(0).options.length] = new Option(item[1], item[0]);
                        if (selectedCol !== "") {
                            if (item[0] == selectedCol) {
                                $('#colonia  option[value=' + item[0] + ']').attr('selected', 'selected');
                            }
                        }
                    });
                },
                error: function () {
                    ModalGenerator.notificacion({
                        "titulo": "Error",
                        "cuerpo": "Hubo un problema que impidió que se completara la operación.",
                        "tipo": "ALERT",
                    });
                }
            });
            $('#colonia').change();
            return false;
        }

        function getLocalidad() {
            var municipioId = $('#municipio').val() === null ? '<s:property value="alumno.direccion.relacionGeografica.municipio.id" />' : $('#municipio').val();
            $.ajax({
                type: 'POST',
                url: '/ajax/getDireccionLocalidad.action',
                dataType: 'json',
                data: {pkEstado: $('#estado').val(),
                    pkMunicipio: municipioId
                },
                cache: false,
                success: function (aData) {
                    $('#localidad').get(0).options.length = 0;
                    $('#localidad').get(0).options[0] = new Option("-- Selecciona --", "");
                    var selectedCol = "<s:property value="alumno.direccion.inegiLocalidad.id" />";
                    $.each(aData.data, function (i, item) {
                        $('#localidad').get(0).options[$('#localidad').get(0).options.length] = new Option(item[1], item[0]);
                        if (selectedCol !== "") {
                            if (item[0] == selectedCol) {
                                $('#localidad  option[value=' + item[0] + ']').attr('selected', 'selected');
                            }
                        }
                    });
                },
                error: function () {
                    ModalGenerator.notificacion({
                        "titulo": "Error",
                        "cuerpo": "Hubo un problema que impidió que se completara la operación.",
                        "tipo": "ALERT",
                    });
                }
            });
            $('#localidad').change();
            return false;
        }

        function datosCURP() {
            var curp = document.getElementById("curp");
            //Obtenemos la fecha de nacimiento
            if ((curp.value.substr(4, 6)).length === 6) {
                var año = "";
                if (curp.value.substr(4, 2) < "17") {
                    año = "20" + curp.value.substr(4, 2);
                } else {
                    año = "19" + curp.value.substr(4, 2);
                }
                $("#fechaDeNacimiento").val(curp.value.substr(8, 2) + "-" + curp.value.substr(6, 2) + "-" + año);
            }
            //Obtenemos la entidad de nacimiento
            if ((curp.value.substr(11, 2)).length === 2) {
                if (curp.value.substr(11, 2) === "AS") {
                    $("select[name='alumno.entidadDeNacimiento.id'] option[value='1']").attr('selected', 'selected');
                } else if (curp.value.substr(11, 2) === "BC") {
                    $("select[name='alumno.entidadDeNacimiento.id'] option[value='2']").attr('selected', 'selected');
                } else if (curp.value.substr(11, 2) === "BS") {
                    $("select[name='alumno.entidadDeNacimiento.id'] option[value='3']").attr('selected', 'selected');
                } else if (curp.value.substr(11, 2) === "CC") {
                    $("select[name='alumno.entidadDeNacimiento.id'] option[value='4']").attr('selected', 'selected');
                } else if (curp.value.substr(11, 2) === "CL") {
                    $("select[name='alumno.entidadDeNacimiento.id'] option[value='5']").attr('selected', 'selected');
                } else if (curp.value.substr(11, 2) === "CM") {
                    $("select[name='alumno.entidadDeNacimiento.id'] option[value='6']").attr('selected', 'selected');
                } else if (curp.value.substr(11, 2) === "CS") {
                    $("select[name='alumno.entidadDeNacimiento.id'] option[value='7']").attr('selected', 'selected');
                } else if (curp.value.substr(11, 2) === "CH") {
                    $("select[name='alumno.entidadDeNacimiento.id'] option[value='8']").attr('selected', 'selected');
                } else if (curp.value.substr(11, 2) === "DF") {
                    $("select[name='alumno.entidadDeNacimiento.id'] option[value='9']").attr('selected', 'selected');
                } else if (curp.value.substr(11, 2) === "DG") {
                    $("select[name='alumno.entidadDeNacimiento.id'] option[value='10']").attr('selected', 'selected');
                } else if (curp.value.substr(11, 2) === "GT") {
                    $("select[name='alumno.entidadDeNacimiento.id'] option[value='11']").attr('selected', 'selected');
                } else if (curp.value.substr(11, 2) === "GR") {
                    $("select[name='alumno.entidadDeNacimiento.id'] option[value='12']").attr('selected', 'selected');
                } else if (curp.value.substr(11, 2) === "HG") {
                    $("select[name='alumno.entidadDeNacimiento.id'] option[value='13']").attr('selected', 'selected');
                } else if (curp.value.substr(11, 2) === "JC") {
                    $("select[name='alumno.entidadDeNacimiento.id'] option[value='14']").attr('selected', 'selected');
                } else if (curp.value.substr(11, 2) === "MC") {
                    $("select[name='alumno.entidadDeNacimiento.id'] option[value='15']").attr('selected', 'selected');
                } else if (curp.value.substr(11, 2) === "MN") {
                    $("select[name='alumno.entidadDeNacimiento.id'] option[value='16']").attr('selected', 'selected');
                } else if (curp.value.substr(11, 2) === "MS") {
                    $("select[name='alumno.entidadDeNacimiento.id'] option[value='17']").attr('selected', 'selected');
                } else if (curp.value.substr(11, 2) === "NT") {
                    $("select[name='alumno.entidadDeNacimiento.id'] option[value='18']").attr('selected', 'selected');
                } else if (curp.value.substr(11, 2) === "NL") {
                    $("select[name='alumno.entidadDeNacimiento.id'] option[value='19']").attr('selected', 'selected');
                } else if (curp.value.substr(11, 2) === "OC") {
                    $("select[name='alumno.entidadDeNacimiento.id'] option[value='20']").attr('selected', 'selected');
                } else if (curp.value.substr(11, 2) === "PL") {
                    $("select[name='alumno.entidadDeNacimiento.id'] option[value='21']").attr('selected', 'selected');
                } else if (curp.value.substr(11, 2) === "QT") {
                    $("select[name='alumno.entidadDeNacimiento.id'] option[value='22']").attr('selected', 'selected');
                } else if (curp.value.substr(11, 2) === "QR") {
                    $("select[name='alumno.entidadDeNacimiento.id'] option[value='23']").attr('selected', 'selected');
                } else if (curp.value.substr(11, 2) === "SP") {
                    $("select[name='alumno.entidadDeNacimiento.id'] option[value='24']").attr('selected', 'selected');
                } else if (curp.value.substr(11, 2) === "SL") {
                    $("select[name='alumno.entidadDeNacimiento.id'] option[value='25']").attr('selected', 'selected');
                } else if (curp.value.substr(11, 2) === "SR") {
                    $("select[name='alumno.entidadDeNacimiento.id'] option[value='26']").attr('selected', 'selected');
                } else if (curp.value.substr(11, 2) === "TC") {
                    $("select[name='alumno.entidadDeNacimiento.id'] option[value='27']").attr('selected', 'selected');
                } else if (curp.value.substr(11, 2) === "TS") {
                    $("select[name='alumno.entidadDeNacimiento.id'] option[value='28']").attr('selected', 'selected');
                } else if (curp.value.substr(11, 2) === "TL") {
                    $("select[name='alumno.entidadDeNacimiento.id'] option[value='29']").attr('selected', 'selected');
                } else if (curp.value.substr(11, 2) === "VZ") {
                    $("select[name='alumno.entidadDeNacimiento.id'] option[value='30']").attr('selected', 'selected');
                } else if (curp.value.substr(11, 2) === "YN") {
                    $("select[name='alumno.entidadDeNacimiento.id'] option[value='31']").attr('selected', 'selected');
                } else if (curp.value.substr(11, 2) === "ZS") {
                    $("select[name='alumno.entidadDeNacimiento.id'] option[value='32']").attr('selected', 'selected');
                }
            }
            //Obtenemos el genero
            if (curp.value.substr(10, 1) === "M") {
                $("select[name='alumno.genero.id'] option[value='1']").attr('selected', 'selected');
            } else if (curp.value.substr(10, 1) === "H") {
                $("select[name='alumno.genero.id'] option[value='2']").attr('selected', 'selected');
            }
        }
    </script>
</content>