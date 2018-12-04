<%-- 
    Document   : datosPersonales_registro
    Created on : 21-ago-2015, 12:31:18
    Author     : Patricia Benitez
    Redesing   : Mario Márquez
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.opensymphony.xwork2.ActionContext;" %>

<head>    
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <!-- bootstrap -->
    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap/bootstrap.min-3.3.7.css" />
    <!-- global styles -->
    <link rel="stylesheet" type="text/css" href="/resources/css/theme_styles.min.css" />
    <!-- libraries -->
    <link rel="stylesheet" type="text/css" href="/vendors/font-awesome/font-awesome.min-4.6.3.css" />
    <link rel="stylesheet" type="text/css" href="/vendors/nanoscroller/nanoscroller.min.css" />
    <!-- Favicon -->
    <link type="image/x-icon" href="/favicon.png" rel="shortcut icon" />
    <!-- Struts utils -->
    <link rel="stylesheet" href="/vendors/struts/styles.min.css" type="text/css">
    <!-- google font libraries -->
    <link href='https://fonts.googleapis.com/css?family=Open+Sans:400,600,700,300|Titillium+Web:200,300,400' rel='stylesheet' type='text/css'>
    <!-- this page specific styles -->
    <link rel="stylesheet" type="text/css" href="/resources/css/font_login.min.css" />
    <link rel="stylesheet" type="text/css" href="/resources/css/sign_up.css" />    
    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css" />
    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap-datepicker/css/bootstrap-datepicker.min.css" />    
    <style>
        #register-box{
            box-shadow: 20px 20px 10px grey;
            -webkit-box-shadow: 20px 20px 10px grey;
            -moz-box-shadow: 20px 20px 10px grey;
            max-width: 850px;
            min-width: 280px;
            margin: 60px auto 20px;
            overflow: hidden;
            border-radius: 3px 3px 0 0;
            background-clip: padding-box;
        }
        #div-logo-registro {
            max-width: 850px;
            background: none repeat scroll 0 0 #ccc;
            color: #fff;
            display: block;
            font-size: 2em;
            font-weight: 400;
            padding: 18px 0;
            text-align: center;
            text-transform: uppercase;
        }
        #div-logo-registro > img {
            display: block;
            height: 96px;
            margin: 0 auto; 
        }
        #p-intro {
            padding-left: 18px;
            padding-bottom: 18px; 
        }
        /*#curp{text-transform:uppercase}*/
    </style>
    <title>Registro de alumnos - SIBEC</title>
</head> 

<body id="login-page-full">
    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <div id="register-box">
                    <div class="row">
                        <div class="col-xs-12">
                            <header id="login-header">
                                <div id="div-logo-registro">
                                    <img src="/resources/img/login/logo-sibec.svg" alt="SIBEC" align="middle" />
                                </div>
                            </header>
                            <div class="row">
                                <div class="col-xs-12">
                                    <div class="main-box clearfix">
                                        <div class="row">
                                            <!-- Espacio para errores -->
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
                                                <div class="alert alert-info">
                                                    <i class="fa fa-info-circle fa-fw fa-lg"></i>
                                                    Para finalizar tu registro como usuario del sistema tendrás que llenar todos los datos sin excepción de cada una de las siguientes pestañas:
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row main_content">
                                            <div class="col-xs-12">
                                                <div class="panel-group accordion" id="accordion">
                                                    <div class="panel panel-default">
                                                        <div class="panel-heading">
                                                            <h4 class="panel-title">
                                                                <a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
                                                                    Aviso de Privacidad Simplificado
                                                                </a>
                                                            </h4>
                                                        </div>
                                                        <div id="collapseOne" class="panel-collapse collapse">
                                                            <div class="panel-body">
                                                                <p style="text-align: justify;">El Instituto Polit&eacute;cnico Nacional, a trav&eacute;s de la 
                                                                    Direcci&oacute;n de Servicios Estudiantiles de conformidad a lo dispuesto en el 
                                                                    art&iacute;culo 53 fracci&oacute;n V del Reglamento Org&aacute;nico del 
                                                                    Instituto Polit&eacute;cnico Nacional, es el responsable del tratamiento de los datos personales 
                                                                    que nos proporciones, los cuales ser&aacute;n protegidos conforme a lo dispuesto por la 
                                                                    Ley General de Protecci&oacute;n de Datos Personales en Posesi&oacute;n de Sujetos Obligados, 
                                                                    y dem&aacute;s normatividad que resulte aplicable.</p>
                                                                <p style="text-align: justify;"><strong>&iquest;Para qu&eacute; fines utilizaremos sus datos personales?</strong></p>
                                                                <p style="text-align: justify;">Los datos personales que solicitamos los utilizaremos para las siguientes finalidades: 
                                                                    Llevar a cabo el proceso de selecci&oacute;n y otorgamiento de becas que oferta el Instituto Polit&eacute;cnico Nacional.</p>
                                                                <p><strong>&iquest;Con qui&eacute;n compartimos su informaci&oacute;n personal y para qu&eacute; fines?</strong></p>
                                                                <p style="text-align: justify;">Se informa que no se realizar&aacute;n transferencias de datos personales, salvo aqu&eacute;llas 
                                                                    que sean necesarias para atender requerimientos de informaci&oacute;n de una autoridad competente, que est&eacute;n 
                                                                    debidamente fundados y motivados.</p><p style="text-align: justify;">Para conocer mayor informaci&oacute;n sobre los 
                                                                    t&eacute;rminos y condiciones en que ser&aacute;n tratados sus datos personales, como los terceros con quienes 
                                                                    compartimos su informaci&oacute;n personal y la forma en que podr&aacute; ejercer sus derechos ARCO, puede consultar el 
                                                                    aviso de privacidad integral en: <a href="https://www.ipn.mx/dse/becas/Paginas/inicio.aspx" target="_blank"  > <strong>https://www.ipn.mx/dse/becas/Paginas/inicio.aspx</strong> </a>
                                                                </p>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div> 
                                            </div>

                                            <div class="col-lg-12 col-md-12 col-sm-12">
                                                <!--base-->   
                                                <div class="tabs-wrapper profile-tabs">
                                                    <ul class="nav nav-tabs">
                                                        <li class="active"><a href="#personales-tab" data-toggle="tab">Personales <i class="fa"></i></a></li>
                                                        <li><a href="#academicos-tab" data-toggle="tab">Académicos <i class="fa"></i></a></li>
                                                        <li><a href="#direccion-tab" data-toggle="tab">Dirección<i class="fa"></i></a></li>
                                                        <li><a href="#contacto-tab" data-toggle="tab">Contacto<i class="fa"></i></a></li>
                                                        <!--<li><a href="#oportunidades-tab" data-toggle="tab">Otros programas<i class="fa"></i></a></li>-->
                                                    </ul>
                                                    <form id="accountForm" method="post" class="form-horizontal" action="/registro/guardaRegistro.action" style="margin-top: 20px;">
                                                        <div class="tab-content">
                                                            <div class="tab-pane active" id="personales-tab">
                                                                <div class="col-sm-12 form-group">
                                                                    <label class="col-sm-3 control-label">Nombre(s)</label>
                                                                    <div class="col-sm-6">
                                                                        <s:if test="activoDatosDAE" >
                                                                            <s:textfield cssClass="form-control" name="alumno.nombre" id="nombre" placeholder="Nombre"
                                                                                         readonly="true" onblur="$(this).val($(this).val().toUpperCase())"/>
                                                                        </s:if>
                                                                        <s:else>                                                                                
                                                                            <s:textfield cssClass="form-control" name="alumno.nombre" id="nombre" placeholder="Nombre"
                                                                                         data-bv-message="Este dato no es válido"
                                                                                         required="true" 
                                                                                         data-bv-notempty="true"
                                                                                         data-bv-notempty-message="El nombre es requerido"
                                                                                         pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s]+$" 
                                                                                         data-bv-regexp-message="Tu nombre sólo puede estar conformado por letras"
                                                                                         data-bv-stringlength="true" 
                                                                                         data-bv-stringlength-min="3" 
                                                                                         data-bv-stringlength-max="50" 
                                                                                         data-bv-stringlength-message="Tu nombre debe tener mínimo 3 letras"
                                                                                         onblur="$(this).val($(this).val().toUpperCase())"/>
                                                                            <span class="help-block" id="nombreMessage" />
                                                                        </s:else>
                                                                    </div>
                                                                </div>
                                                                <div class="col-sm-12 form-group">
                                                                    <label class="col-sm-3 control-label">Apellidos</label>
                                                                    <div class="col-sm-3">
                                                                        <s:if test="activoDatosDAE" >
                                                                            <s:textfield cssClass="form-control" name="alumno.apellidoPaterno" id="apellidoPaterno" readonly="true"
                                                                                         placeholder="Apellido paterno" onblur="$(this).val($(this).val().toUpperCase())"/>
                                                                        </s:if>
                                                                        <s:else>                                                                                    
                                                                            <s:textfield cssClass="form-control" name="alumno.apellidoPaterno" id="apellidoPaterno" 
                                                                                         placeholder="Apellido paterno"
                                                                                         data-bv-message="Este dato no es válido"
                                                                                         required="true" 
                                                                                         data-bv-notempty="true"
                                                                                         data-bv-notempty-message="El apellido paterno es requerido"
                                                                                         pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s]+$" 
                                                                                         data-bv-regexp-message="Tu apellido sólo puede estar conformado por letras"
                                                                                         data-bv-stringlength="true" 
                                                                                         data-bv-stringlength-min="3" 
                                                                                         data-bv-stringlength-max="60" 
                                                                                         data-bv-stringlength-message="Tu apellido debe tener mínimo 3 letras"
                                                                                         onblur="$(this).val($(this).val().toUpperCase())"/>
                                                                            <span class="help-block" id="paternoMessage" />
                                                                        </s:else>
                                                                    </div>
                                                                    <div class="col-sm-3">
                                                                        <s:if test="activoDatosDAE" >
                                                                            <s:textfield cssClass="form-control" name="alumno.apellidoMaterno" id="apellidoMaterno" readonly="true"
                                                                                         placeholder="Apellido materno" onblur="$(this).val($(this).val().toUpperCase())"/>
                                                                        </s:if>
                                                                        <s:else>                                                                                    
                                                                            <s:textfield cssClass="form-control" name="alumno.apellidoMaterno" id="apellidoMaterno" 
                                                                                         placeholder="Apellido materno"
                                                                                         data-bv-message="Este dato no es válido"
                                                                                         required="true" 
                                                                                         data-bv-notempty="true"
                                                                                         data-bv-notempty-message="El apellido materno es requerido"
                                                                                         pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s]+$" 
                                                                                         data-bv-regexp-message="Tu apellido sólo puede estar conformado por letras"
                                                                                         data-bv-stringlength="true" 
                                                                                         data-bv-stringlength-min="3" 
                                                                                         data-bv-stringlength-max="50" 
                                                                                         data-bv-stringlength-message="Tu apellido debe tener mínimo 3 letras"
                                                                                         onblur="$(this).val($(this).val().toUpperCase())"/>
                                                                            <span class="help-block" id="maternoMessage" />
                                                                        </s:else>
                                                                    </div>
                                                                </div>
                                                                <div class="col-sm-12 form-group">
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
                                                                                     onkeyup="datosCURP()"/>
                                                                        <span class="help-block" id="curpMessage" />
                                                                        <a href="https://consultas.curp.gob.mx/CurpSP/" target="_blank">  Consulta tu CURP </a>
                                                                    </div>
                                                                </div>
                                                                <div class="col-sm-12 form-group">
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
                                                                        <span class="help-block">Formato dd-mm-yyyy</span>
                                                                    </div>
                                                                </div>
                                                                <div class="col-sm-12 form-group">
                                                                    <label class="col-sm-3 control-label">Entidad de nacimiento</label>
                                                                    <div class="col-sm-6">
                                                                        <s:select id="estadosDireccion"  cssClass="form-control"
                                                                                  list="ambiente.entidadFederativaTotal" listKey="id" listValue="nombre" headerKey=""
                                                                                  headerValue="-- Selecciona un estado --"
                                                                                  name="alumno.entidadDeNacimiento.id"
                                                                                  data-bv-notempty="true"
                                                                                  data-bv-notempty-message="La entidad de nacimiento es requerida"
                                                                                  disabled="true"/>
                                                                        <span class="help-block" id="estadoMessage" />
                                                                    </div>
                                                                </div>
                                                                <div class="col-sm-12 form-group">
                                                                    <label class="col-sm-3 control-label">Género</label>
                                                                    <div class="col-sm-6">
                                                                        <s:select id="genero" cssClass="form-control"
                                                                                  list="ambiente.generoList" listValue="nombre" listKey="id" headerKey=""
                                                                                  headerValue="-- Selecciona  --"
                                                                                  name="alumno.genero.id"
                                                                                  data-bv-notempty="true"
                                                                                  data-bv-notempty-message="El género es requerido"
                                                                                  disabled="true"/>
                                                                        <span class="help-block" id="generoMessage"/>
                                                                    </div>
                                                                </div>
                                                                <div class="col-sm-12 form-group">
                                                                    <label class="col-sm-3 control-label">Estado civil</label>
                                                                    <div class="col-sm-6">
                                                                        <s:select id="civil" cssClass="form-control"
                                                                                  list="ambiente.estadoCivil" listValue="nombre" listKey="id" headerKey=""
                                                                                  headerValue="-- Selecciona tu estado civil --"
                                                                                  name="alumno.estadoCivil.id"
                                                                                  data-bv-notempty="true"
                                                                                  data-bv-notempty-message="El estado civil es requerido" />
                                                                        <span class="help-block" id="civilMessage" />
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <div class="tab-pane" id="academicos-tab">
                                                                <div class="col-sm-12 form-group">
                                                                    <label class="col-sm-3 control-label">Boleta</label>
                                                                    <div class="col-sm-6">
                                                                        <s:textfield cssClass="form-control" name="alumno.boleta" id="boleta" placeholder="Boleta"
                                                                                     readonly="true" />
                                                                        <span class="help-block" id="nombreMessage" />
                                                                    </div>
                                                                </div>
                                                                <div class="col-sm-12 form-group">
                                                                    <label class="col-sm-3 control-label">Unidad Acad&eacute;mica</label>
                                                                    <div class="col-sm-6">
                                                                        <s:if test="activoDatosDAE" >
                                                                            <s:textfield cssClass="form-control" name="alumno.datosAcademicos.unidadAcademica.nombreCorto" id="nombreCorto" placeholder="Unidad Académica"
                                                                                         readonly="true" />
                                                                        </s:if>
                                                                        <s:else>                                                                                    
                                                                            <s:select id="unidadAcademica"  
                                                                                      list="ambiente.unidadAcademicaList" listValue="nombreCorto" listKey="id" headerKey=""
                                                                                      headerValue="-- Selecciona una Unidad Académica --"
                                                                                      name="alumno.datosAcademicos.unidadAcademica.id"
                                                                                      data-bv-notempty="true"
                                                                                      data-bv-notempty-message="La Unidad Académica es requerida" />
                                                                            <span class="help-block" id="estadoMessage" />
                                                                            <s:hidden name="unidadAcademica.id" />
                                                                        </s:else>
                                                                        <span class="help-block" id="estadoMessage" />
                                                                    </div>
                                                                </div>
                                                                <s:if test="activoDatosDAE" >
                                                                    <div class="col-sm-12 form-group">
                                                                        <label class="col-sm-3 control-label">Semestre</label>
                                                                        <div class="col-sm-6">
                                                                            <s:textfield cssClass="form-control" name="alumno.datosAcademicos.semestre" id="semestre" placeholder="Semestre"
                                                                                         readonly="true" />
                                                                            <span class="help-block" id="nombreMessage" />
                                                                        </div>
                                                                    </div>
                                                                    <div class="col-sm-12 form-group">
                                                                        <label class="col-sm-3 control-label">Promedio</label>
                                                                        <div class="col-sm-6">
                                                                            <input type="number" class="form-control" id = "prom" disabled>
                                                                            <s:hidden name="alumno.datosAcademicos.promedio" id="promedio"/>
                                                                            <span class="help-block" id="nombreMessage" />
                                                                        </div>
                                                                    </div>
                                                                    <div class="col-sm-12 form-group">
                                                                        <label class="col-sm-3 control-label">Carrera</label>
                                                                        <div class="col-sm-6">
                                                                            <s:textfield cssClass="form-control" name="alumno.datosAcademicos.carrera.carrera" id="carrera" placeholder="Carrera"
                                                                                         readonly="true"/>
                                                                            <span class="help-block" id="estadoMessage" />
                                                                        </div>
                                                                    </div>
                                                                    <div class="col-sm-12 form-group">
                                                                        <label class="col-sm-3 control-label">Plan de estudios</label>
                                                                        <div class="col-sm-6">
                                                                            <s:textfield cssClass="form-control" name="alumno.datosAcademicos.carrera.planEstudios" id="planEstudios" placeholder="Plan de Estudios"
                                                                                         readonly="true"/>
                                                                            <span class="help-block" id="estadoMessage" />
                                                                        </div>
                                                                    </div>
                                                                    <div class="col-sm-12 form-group">
                                                                        <div class="checkbox-nice col-sm-offset-3">
                                                                            <input id="checkbox-egresado" type="checkbox" disabled>
                                                                            <label for="checkbox-egresado">
                                                                                Egresado
                                                                            </label>
                                                                        </div>
                                                                    </div>
                                                                </s:if>
                                                                <%--<s:hidden name="alumno.datosAcademicos.inscrito" />--%>
                                                            </div>
                                                            <div class="tab-pane" id="direccion-tab">
                                                                <p style="margin-left: 20px; margin-right: 20px; text-justify: inter-word; text-align: justify;">
                                                                    En caso de que vengas de algun estado pero estas rentando o viviendo temporalmente cerca de tu 
                                                                    Unidad Académica deberás ingresar el lugar en el que resides normalmente miestras asistes a la Unidad Académica. 
                                                                </p>
                                                                <div class="clearfix" >&nbsp;</div>
                                                                <div class="col-sm-12 form-group">
                                                                    <label class="col-sm-3 control-label">Calle</label>
                                                                    <div class="col-sm-6">
                                                                        <s:textfield cssClass="form-control" name="alumno.direccion.calle" id="calle" placeholder="Calle"
                                                                                     data-bv-message="Este dato no es válido"
                                                                                     required="true" 
                                                                                     data-bv-notempty="true"
                                                                                     data-bv-notempty-message="La calle es requerida"
                                                                                     pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ0-9\s\.]+$" 
                                                                                     data-bv-regexp-message="Tu calle debe estar conformado por números o letras"
                                                                                     data-bv-stringlength="true" 
                                                                                     data-bv-stringlength-min="1" 
                                                                                     data-bv-stringlength-max="100" 
                                                                                     data-bv-stringlength-message="Tu calle debe ser de al menos 1 caracter"
                                                                                     />
                                                                        <span class="help-block" id="nombreMessage" />
                                                                    </div>
                                                                </div>
                                                                <div class="col-sm-12 form-group">
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
                                                                                     value =""
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
                                                                <div class="col-sm-12 form-group">
                                                                    <label class="col-sm-3 control-label">Entidad</label>
                                                                    <div class="col-sm-6">
                                                                        <s:select id="estado"  cssClass="form-control"
                                                                                  list="ambiente.entidadFederativa" listKey="id" listValue="nombre" headerKey=""
                                                                                  headerValue="-- Selecciona un estado --"
                                                                                  name="alumno.direccion.relacionGeografica.estado.id"
                                                                                  data-bv-notempty="true"
                                                                                  data-bv-notempty-message="La entidad es requerida"
                                                                                  onchange="getMunicipios()"
                                                                                  />
                                                                        <span class="help-block" id="estadoMessage" />
                                                                    </div>
                                                                </div>
                                                                <div class="col-sm-12 form-group">
                                                                    <label class="col-sm-3 control-label">Municipio</label>
                                                                    <div class="col-sm-6">
                                                                        <s:select id="municipio"  cssClass="form-control"
                                                                                  list="%{{}}"
                                                                                  headerValue="-- Selecciona un municipio --"
                                                                                  name="alumno.direccion.relacionGeografica.municipio.id"
                                                                                  data-bv-notempty="true"
                                                                                  data-bv-notempty-message="El municipio es requerido"
                                                                                  onchange="getColonias(), getLocalidad()"/>
                                                                        <span class="help-block" id="estadoMessage" />
                                                                    </div>
                                                                </div>
                                                                <div class="col-sm-12 form-group">
                                                                    <label class="col-sm-3 control-label">Colonia</label>
                                                                    <div class="col-sm-6">
                                                                        <s:select id="colonia"  cssClass="form-control" 
                                                                                  list="%{{}}"
                                                                                  headerValue="-- Selecciona --"
                                                                                  name="alumno.direccion.relacionGeografica.colonia.id"
                                                                                  data-bv-notempty="true"
                                                                                  data-bv-notempty-message="La colonia es requerida"/>
                                                                        <span class="help-block" id="estadoMessage" />
                                                                    </div>
                                                                </div>
                                                                <div class="col-sm-12 form-group">
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
                                                                <div class="col-sm-12 form-group">
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
                                                                <div class="col-sm-12 form-group">
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
                                                                <div class="col-sm-12 form-group">
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
                                                                <div class="alert alert-warning col-sm-12">
                                                                    Debes verificar que tu correo electrónico sea correcto, y que tengas acceso a el, 
                                                                    ya que es nuestro medio de comunicación contigo, incluyendo el envío de tus datos 
                                                                    de acceso al sistema, si así lo requirieras.
                                                                </div>
                                                                <div class="col-sm-12 form-group">
                                                                    <label class="col-sm-3 control-label">Correo electr&oacute;nico</label>
                                                                    <div class="col-sm-6">
                                                                        <s:textfield cssClass="form-control" name="alumno.correoElectronico" id="correoElectronico" placeholder="ejemplo@gmail.com"
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
                                                                                     data-bv-stringlength-message="Tu correo electrónico debe ser mínimo de 6 caracteres y máximo 60 caracteres"/>
                                                                        <span class="help-block" id="nombreMessage" />
                                                                    </div>
                                                                </div> 
                                                                <div class="col-sm-12 form-group">
                                                                    <label class="col-sm-3 control-label">Confirmar correo electr&oacute;nico</label>
                                                                    <div class="col-sm-6">
                                                                        <s:textfield cssClass="form-control" name="repetirEmail" id="repetirEmail" placeholder="ejemplo@gmail.com"
                                                                                     data-bv-emailaddress="true"
                                                                                     data-bv-emailaddress-message="El correo electrónico que proporcionaste no parece ser válido. Por favor, verifica."
                                                                                     data-bv-message="El correo electrónico que proporcionaste no parece ser válido. Por favor, verifica."
                                                                                     required="true"
                                                                                     pattern="^[a-zA-Z\d-_.]+@[a-zA-Z]+([\.][a-zA-Z]{1,3}){1,2}$"
                                                                                     data-bv-regexp-message="El correo no es válido"
                                                                                     data-bv-notempty="true"
                                                                                     data-bv-notempty-message="Es necesario confirmar el correo"
                                                                                     data-bv-stringlength="true" 
                                                                                     data-bv-stringlength-min="6" 
                                                                                     data-bv-stringlength-max="60" 
                                                                                     data-bv-stringlength-message="Tu correo electrónico debe ser mínimo de 6 caracteres y máximo 60 caracteres"/>
                                                                        <span class="help-block" id="nombreMessage" />
                                                                    </div>
                                                                </div> 
                                                                <div class="col-sm-12 form-group">
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
                                                                <div class="col-sm-12 form-group">
                                                                    <label class="col-sm-3 control-label">Teléfono celular</label>
                                                                    <div class="col-sm-6">
                                                                        <s:textfield cssClass="form-control" name="alumno.celular" id="celular" placeholder="55-55555555"
                                                                                     data-bv-message="Este dato no es válido"
                                                                                     required="true" 
                                                                                     data-bv-notempty="true"
                                                                                     data-bv-notempty-message="El teléfono es requerido"
                                                                                     pattern="\d{2,3}-\d{7,8}" 
                                                                                     data-bv-regexp-message="Un guión separa la clave LADA: 55-55555555 o 555-5555555"
                                                                                     data-bv-stringlength="true" 
                                                                                     data-bv-stringlength-min="11" 
                                                                                     data-bv-stringlength-max="11" 
                                                                                     data-bv-stringlength-message="Tu teléfono debe ser de 10 caracteres numéricos" />
                                                                        <span class="help-block" id="nombreMessage" />
                                                                    </div>
                                                                </div> 
                                                                <div class="col-sm-12 form-group">
                                                                    <label class="col-sm-3 control-label">Compa&ntilde;&iacute;a celular</label>
                                                                    <div class="col-sm-6">
                                                                        <s:select id="companiaCelular"  cssClass="form-control"
                                                                                  list="ambiente.companiaCelularList" listKey="id" listValue="nombre" headerKey=""
                                                                                  headerValue="-- Selecciona tu compañia celular --"
                                                                                  name="alumno.companiaCelular.id"
                                                                                  data-bv-notempty="true"
                                                                                  data-bv-notempty-message="La entidad es requerida"/>
                                                                        <span class="help-block" id="estadoMessage" />
                                                                    </div>
                                                                </div>
                                                                <div class="col-sm-12 form-group">
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
                                                                <div class="col-sm-12 form-group">
                                                                    <div class="col-sm-10 col-sm-offset-2">
                                                                        <label for="beneficiarioOportunidades">
                                                                            &iquest;Es beneficiario del programa PROSPERA (antes Oportunidades)?
                                                                        </label>
                                                                        <div class="radio">
                                                                            <s:radio list="service.respuestaBoolean" 
                                                                                     name="alumno.beneficiarioOportunidades" 
                                                                                     required="true"/>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="col-sm-12 form-group">
                                                            <div class="col-sm-12">
                                                                <h3 style="margin-top: 5px; margin-left: 10px;">Finalizar Registro</h3>
                                                                <p style="margin-left: 20px; margin-right: 20px; text-justify: inter-word; text-align: justify;">
                                                                    Autorizo a la instancia pertinente para que pueda verificar los datos asentados en esta registro y 
                                                                    en caso de falta de probidad, podrá ser motivo de que el apoyo se cancele,  aún cuando este ya se me 
                                                                    haya asignado.<br>
                                                                    Manifiesto conocer los derechos y obligaciones asociados a la beca solicitada  y me comprometo a 
                                                                    cumplir con las responsabilidades  que se deriven de la asignación de la misma.                                                                    
                                                                </p>                                                                
                                                            </div>
                                                        </div>
                                                        <div class="col-sm-12 form-group" style="padding-left: 35px">
                                                            <div class="checkbox-nice">
                                                                <input type="checkbox" 
                                                                       name="checkbox"
                                                                       id="checkbox" 
                                                                       required="true"
                                                                       value="check" 
                                                                       data-bv-notempty="true" 
                                                                       data-bv-notempty-message="Debes aceptar los términos para continuar."/>
                                                                <label for="checkbox">
                                                                    Sí acepto
                                                                </label>
                                                            </div>
                                                        </div>

                                                        <div class="col-sm-12 form-group">
                                                            <div class="col-sm-12">
                                                                <s:if test="!(hasActionMessages())">
                                                                    <button type="submit" id="boton-finalizar" class="btn btn-primary pull-right">Finalizar registro</button>
                                                                </s:if>
                                                            </div>
                                                        </div>
                                                    </form>
                                                </div>                                                          
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- global scripts -->
    <script src="/vendors/jQuery/jquery-3.1.1.min.js"></script>
    <script src="/vendors/bootstrap/bootstrap-3.3.7.min.js"></script>
    <script src="/vendors/nanoscroller/jquery.nanoscroller-0.8.7.min.js"></script>
    <!-- Struts utils -->
    <script src="/vendors/struts/utils.min.js"></script>
    <!-- theme scripts -->
    <script src="/resources/js/scripts.min.js"></script>    
    <!-- this page specific inline scripts -->
    <script type="text/javascript" src="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.js"></script>    
    <script type="text/javascript" src="/vendors/bootstrap-datepicker/js/bootstrap-datepicker.min.js"></script>    
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>
    <script>
                                                                                       $(document).ready(function () {
                                                                                           genericoCURP();
                                                                                           setCheckbox('checkbox-egresado', '<s:property value="alumno.datosAcademicos.egresado" />');
                                                                                           var entidad = '<s:property value="alumno.direccion.relacionGeografica.estado.id" />';
                                                                                           if (entidad.trim().length > 0)
                                                                                               getMunicipios();
                                                                                           var number =<s:property value="alumno.datosAcademicos.promedio"/>;
                                                                                           document.getElementById("prom").value = number.toFixed(2);
                                                                                           $('#accountForm').bootstrapValidator({
                                                                                               excluded: [':disabled']
                                                                                           }).on('success.form.bv', function (e) {
                                                                                               console.log('exito');
                                                                                               e.preventDefault();
                                                                                               console.log('previno');
                                                                                               validarCamposEspeciales();
                                                                                           }).on('status.field.bv', function (e, data) {
                                                                                               var $form = $(e.target),
                                                                                                       validator = data.bv,
                                                                                                       $tabPane = data.element.parents('.tab-pane'),
                                                                                                       tabId = $tabPane.attr('id');
                                                                                               if (tabId) {
                                                                                                   var $icon = $('a[href="#' + tabId + '"][data-toggle="tab"]').parent().find('i');

                                                                                                   if (data.status == validator.STATUS_INVALID) {
                                                                                                       $icon.removeClass('fa-check').addClass('fa-times');
                                                                                                   } else if (data.status == validator.STATUS_VALID) {
                                                                                                       var isValidTab = validator.isValidContainer($tabPane);
                                                                                                       $icon.removeClass('fa-check fa-times').addClass(isValidTab ? 'fa-check' : 'fa-times');
                                                                                                   }
                                                                                               }
                                                                                           });
                                                                                       });
                                                                                       function validarCamposEspeciales() {
                                                                                           var correoElectronico = $('#correoElectronico').val();
                                                                                           var repetirEmail = $('#repetirEmail').val();
                                                                                           if (correoElectronico !== repetirEmail) {
                                                                                               ModalGenerator.notificacion({
                                                                                                   "titulo": "Atención",
                                                                                                   "cuerpo": "Los correos electrónicos no coinciden. Favor de verificarlos.",
                                                                                                   "tipo": "WARNING",
                                                                                                   "funcionAceptar": function () {
                                                                                                       $("#boton-finalizar").blur();
                                                                                                   },
                                                                                                   "funcionCancelar": function () {
                                                                                                       $("#boton-finalizar").blur();
                                                                                                   }
                                                                                               });
                                                                                           } else {
                                                                                               ModalGenerator.notificacion({
                                                                                                   "titulo": "Atención",
                                                                                                   "cuerpo": "¿Estás seguro que tus datos son correctos?",
                                                                                                   "tipo": "INFO",
                                                                                                   "funcionAceptar": function () {
                                                                                                       $("#accountForm")[0].submit();
                                                                                                   },
                                                                                                   "funcionCancelar": function () {
                                                                                                       $("#boton-finalizar").blur();
                                                                                                   }
                                                                                               });
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

                                                                                       // Revisamos que el curp no sea genérico
                                                                                       function genericoCURP() {
                                                                                           var curp = document.getElementById("curp");
                                                                                           if (String(curp.value.substr(4, 6)) !== "999999") {
                                                                                               // console.log(curp.value.substr(4, 6));
                                                                                               datosCURP();
                                                                                           }
                                                                                       }

                                                                                       // Palomea checkbox
                                                                                       function setCheckbox(id, value) {
                                                                                           if (value === '1' || value === true) {
                                                                                               $('#' + id).prop("checked", true);
                                                                                           }
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
                                                                                               } else {
                                                                                                   document.getElementById("estadosDireccion").disabled =  false;
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
</body>