<%-- 
    Document   : datospersonales_success
    Created on : 09-ago-2015, 21:37:22
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
        #register-box-inner {
            background: #fff;
            border-radius: 0 0 3px 3px;
            background-clip: padding-box;
            /* stops bg color from leaking outside the border: */
            border: 1px solid #e1e1e1;
            border-bottom-width: 5px;
            padding: 40px 25px; 
        }
        #register-box-inner.with-heading {
            padding-top: 20px; }
        #register-box-inner h4 {
            margin-top: 0;
            margin-bottom: 10px; }
        #register-box-inner .reset-pass-input {
            padding: 15px 0;
            margin-bottom: 0; }
        #register-box-inner .input-group > .form-control, #register-box-inner .input-group > .input-group-addon {
            height: 46px;
            line-height: 42px;
            padding-top: 0;
            padding-bottom: 0; }
        #register-box-inner .input-group > .input-group-addon {
            height: 44px; }
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
                                    <img src="/resources/img/template_img/logo-sibec.png" alt="SIBEC" align="middle" />
                                </div>
                            </header>
                            <div id="register-box-inner" style="padding-bottom: 40px">
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
                                                </div>
                                                <div class="col-sm-12">
                                                    <s:if test="noInscrito == 1">
                                                        <div class="alert alert-warning" role="alert">
                                                            <i class="fa fa-warning fa-fw fa-lg"></i>
                                                            Tu número de boleta ha sido reportado en Gestión Escolar como no inscrito. 
                                                            <p>Podrás continuar con tu solicitud, sin embargo te recomendamos verificar tu situación en Gestión Escolar de tu Unidad Académica</p>
                                                        </div>
                                                    </s:if>
                                                </div>
                                                <div class="col-sm-12">
                                                    <div class="alert alert-info">
                                                        <i class="fa fa-info-circle fa-fw fa-lg"></i>
                                                        Hemos enviado tu usuario y contraseña a tu correo electronico. 
                                                        <a href="/login.action">¡Inicia sesión aquí!</a>
                                                    </div>
                                                </div>
                                            </div>      
                                            <div class="row main_content">      
                                                <div class="col-lg-12 col-md-12 col-sm-12">
                                                    <div class="main-box tabs-wrapper">
                                                        <ul class="nav nav-tabs">
                                                            <li class="active"><a href="#personales-tab" data-toggle="tab">Personales <i class="fa"></i></a></li>
                                                            <li><a href="#academicos-tab" data-toggle="tab">Académicos <i class="fa"></i></a></li>
                                                            <li><a href="#direccion-tab" data-toggle="tab">Dirección<i class="fa"></i></a></li>
                                                            <li><a href="#contacto-tab" data-toggle="tab">Contacto<i class="fa"></i></a></li>
                                                            <!--<li><a href="#oportunidades-tab" data-toggle="tab">Otros programas<i class="fa"></i></a></li>-->
                                                        </ul>
                                                        <form id="accountForm" class="form-horizontal" style="margin-top: 20px;">
                                                            <div class="tab-content">
                                                                <div class="tab-pane active" id="personales-tab">
                                                                    <div class="col-sm-12 form-group">
                                                                        <label class="col-sm-2 control-label">Nombre(s)</label>
                                                                        <div class="col-sm-10">
                                                                            <s:textfield cssClass="form-control" name="alumno.nombre" id="nombre" 
                                                                                         placeholder="Nombre" readonly="true"/>
                                                                        </div>
                                                                    </div>
                                                                    <div class="col-sm-12 form-group">
                                                                        <label class="col-sm-2 control-label">Apellidos</label>
                                                                        <div class="col-sm-5">
                                                                            <s:textfield cssClass="form-control" name="alumno.apellidoPaterno" id="apellidoPaterno" 
                                                                                         placeholder="Apellido paterno" readonly="true"/>
                                                                        </div>
                                                                        <div class="col-sm-5">
                                                                            <s:textfield cssClass="form-control" name="alumno.apellidoMaterno" id="apellidoMaterno" 
                                                                                         placeholder="Apellido materno" readonly="true"/>
                                                                        </div>
                                                                    </div>
                                                                    <div class="col-sm-12 form-group">
                                                                        <label class="col-sm-2 control-label">CURP</label>
                                                                        <div class="col-sm-10">
                                                                            <s:textfield cssClass="form-control" name="alumno.curp" id="curp" placeholder="CURP"
                                                                                         readonly="true"/>
                                                                        </div>
                                                                    </div>
                                                                    <div class="col-sm-12 form-group">
                                                                        <label class="col-sm-2 control-label">Fecha de nacimiento</label>
                                                                        <div class="col-sm-10">
                                                                            <div class="input-group">
                                                                                <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                                                                <input type="text" 
                                                                                       class="form-control" 
                                                                                       id="fechaNacimiento" 
                                                                                       name="fechaDeNacimiento"
                                                                                       value="<s:date name='alumno.fechaDeNacimiento' format='dd-MM-yyyy' />"
                                                                                       readonly="true"/>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                    <div class="col-sm-12 form-group">
                                                                        <label class="col-sm-2 control-label">Entidad de nacimiento</label>
                                                                        <div class="col-sm-10">
                                                                            <s:textfield cssClass="form-control" name="alumno.entidadDeNacimiento.nombre" id="entidadDeNacimiento" 
                                                                                         placeholder="Entidad de nacimiento" readonly="true"/>
                                                                        </div>
                                                                    </div>
                                                                    <div class="col-sm-12 form-group">
                                                                        <label class="col-sm-2 control-label">Género</label>
                                                                        <div class="col-sm-10">
                                                                            <s:textfield cssClass="form-control" name="alumno.genero.nombre" id="genero" placeholder="Género"
                                                                                         readonly="true"/>
                                                                        </div>
                                                                    </div>
                                                                    <div class="col-sm-12 form-group">
                                                                        <label class="col-sm-2 control-label">Estado civil</label>
                                                                        <div class="col-sm-10">
                                                                            <s:textfield cssClass="form-control" name="alumno.estadoCivil.nombre" id="civil" placeholder="Estado civil"
                                                                                         readonly="true"/>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div class="tab-pane" id="academicos-tab">
                                                                    <div class="col-sm-12 form-group">
                                                                        <label class="col-sm-2 control-label">Boleta</label>
                                                                        <div class="col-sm-10">
                                                                            <s:textfield cssClass="form-control" name="alumno.boleta" id="boleta" placeholder="Boleta"
                                                                                         readonly="true"/>
                                                                        </div>
                                                                    </div>
                                                                    <div class="col-sm-12 form-group">
                                                                        <label class="col-sm-2 control-label">Unidad Académica</label>
                                                                        <div class="col-sm-10">
                                                                            <s:textfield cssClass="form-control" name="alumno.datosAcademicos.unidadAcademica.nombre"
                                                                                         id="unidadAcademica" placeholder="Unidad Académica" readonly="true"/>
                                                                        </div>
                                                                    </div>
                                                                    <s:if test="activoDatosDAE" >
                                                                        <div class="col-sm-12 form-group">
                                                                            <label class="col-sm-2 control-label">Semestre</label>
                                                                            <div class="col-sm-10">
                                                                                <s:textfield cssClass="form-control" name="alumno.datosAcademicos.semestre" id="semestre"
                                                                                             placeholder="Semestre" readonly="true"/>
                                                                            </div>
                                                                        </div>
                                                                        <div class="col-sm-12 form-group">
                                                                            <label class="col-sm-2 control-label">Promedio</label>
                                                                            <div class="col-sm-10">
                                                                                <input type="number" class="form-control" id = "prom" disabled>
                                                                                <s:hidden name="alumno.datosAcademicos.promedio" id="promedio"/>
                                                                            </div>
                                                                        </div>
                                                                        <div class="col-sm-12 form-group">
                                                                            <label class="col-sm-2 control-label">Carrera</label>
                                                                            <div class="col-sm-10">
                                                                                <s:textfield cssClass="form-control" name="alumno.datosAcademicos.carrera.carrera" id="carrera"
                                                                                             placeholder="Carrera" readonly="true"/>
                                                                            </div>
                                                                        </div>
                                                                        <div class="col-sm-12 form-group">
                                                                            <label class="col-sm-2 control-label">Plan de estudios</label>
                                                                            <div class="col-sm-10">
                                                                                <s:textfield cssClass="form-control" name="alumno.datosAcademicos.carrera.planEstudios"
                                                                                             id="planEstudios" placeholder="Plan de estudios" readonly="true"/>
                                                                            </div>
                                                                        </div>
                                                                    </s:if>
                                                                </div>
                                                                <div class="tab-pane" id="direccion-tab">                                                                
                                                                    <div class="col-sm-12 form-group">
                                                                        <label class="col-sm-2 control-label">Calle</label>
                                                                        <div class="col-sm-10">
                                                                            <s:textfield cssClass="form-control" name="alumno.direccion.calle" id="calle" placeholder="Calle"
                                                                                         readonly="true"/>
                                                                        </div>
                                                                    </div>
                                                                    <div class="col-sm-12 form-group">
                                                                        <label class="col-sm-2 control-label">Número</label>
                                                                        <div class="col-sm-5">
                                                                            <s:textfield cssClass="form-control" name="alumno.direccion.numeroExterior" id="numeroExterior"
                                                                                         placeholder="Número exterior" readonly="true"/>
                                                                        </div>
                                                                        <div class="col-sm-5">
                                                                            <s:textfield cssClass="form-control" name="alumno.direccion.numeroInterior" id="numeroInterior"
                                                                                         placeholder="Número interior" readonly="true"/>
                                                                        </div>
                                                                    </div>
                                                                    <div class="col-sm-12 form-group">
                                                                        <label class="col-sm-2 control-label">Entidad</label>
                                                                        <div class="col-sm-10">
                                                                            <s:textfield cssClass="form-control" name="alumno.direccion.relacionGeografica.estado.nombre"
                                                                                         id="estado" placeholder="Entidad" readonly="true"/>
                                                                        </div>
                                                                    </div>
                                                                    <div class="col-sm-12 form-group">
                                                                        <label class="col-sm-2 control-label">Municipio</label>
                                                                        <div class="col-sm-10">
                                                                            <s:textfield cssClass="form-control" name="alumno.direccion.relacionGeografica.municipio.nombre"
                                                                                         id="municipio" placeholder="Municipio" readonly="true"/>
                                                                        </div>
                                                                    </div>
                                                                    <div class="col-sm-12 form-group">
                                                                        <label class="col-sm-2 control-label">Colonia</label>
                                                                        <div class="col-sm-10">
                                                                            <s:textfield cssClass="form-control" name="alumno.direccion.relacionGeografica.colonia.nombre" 
                                                                                         id="colonia" placeholder="Colonia" readonly="true"/>
                                                                        </div>
                                                                    </div>
                                                                    <div class="col-sm-12 form-group">
                                                                        <label class="col-sm-2 control-label">Localidad</label>
                                                                        <div class="col-sm-10">
                                                                            <s:textfield cssClass="form-control" name="alumno.direccion.inegiLocalidad.localidad" id="colonia"
                                                                                         placeholder="Colonia" readonly="true"/>
                                                                        </div>
                                                                    </div>
                                                                    <div class="col-sm-12 form-group">
                                                                        <label class="col-sm-2 control-label">Código Postal</label>
                                                                        <div class="col-sm-10">
                                                                            <s:textfield cssClass="form-control" name="alumno.direccion.codigoPostal" id="codigoPostal"
                                                                                         placeholder="Código Postal" readonly="true"/>
                                                                        </div>
                                                                    </div>
                                                                    <div class="col-sm-12 form-group">
                                                                        <label class="col-sm-2 control-label">Tipo de vialidad</label>
                                                                        <div class="col-sm-10">
                                                                            <s:textfield cssClass="form-control" name="alumno.direccion.inegiTipoVialidad.descripcion"
                                                                                         id="inegiTipoVialidad" placeholder="Vialidad" readonly="true"/>
                                                                        </div>
                                                                    </div>
                                                                    <div class="col-sm-12 form-group">
                                                                        <label class="col-sm-2 control-label">Tipo de asentamiento</label>
                                                                        <div class="col-sm-10">
                                                                            <s:textfield cssClass="form-control" name="alumno.direccion.inegiTipoAsentamiento.descripcion"
                                                                                         id="inegiTipoAsentamiento" placeholder="Asentamiento" readonly="true"/>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div class="tab-pane" id="contacto-tab">                                                                
                                                                    <div class="col-sm-12 form-group">
                                                                        <label class="col-sm-2 control-label">Correo electrónico</label>
                                                                        <div class="col-sm-10">
                                                                            <s:textfield cssClass="form-control" name="alumno.correoElectronico" id="correoElectronico"
                                                                                         placeholder="ejemplo@gmail.com" readonly="true"/>
                                                                        </div>
                                                                    </div>
                                                                    <div class="col-sm-12 form-group">
                                                                        <label class="col-sm-2 control-label">Correo electrónico alterno</label>
                                                                        <div class="col-sm-10">
                                                                            <s:textfield cssClass="form-control" name="alumno.correoElectronicoAlterno" id="correoElectronicoAlterno"
                                                                                         placeholder="ejemplo@gmail.com" readonly="true"/>
                                                                        </div>
                                                                    </div> 
                                                                    <div class="col-sm-12 form-group">
                                                                        <label class="col-sm-2 control-label">Teléfono celular</label>
                                                                        <div class="col-sm-10">
                                                                            <s:textfield cssClass="form-control" name="alumno.celular" id="celular" placeholder="55-55555555"
                                                                                         readonly="true"/>
                                                                        </div>
                                                                    </div> 
                                                                    <div class="col-sm-12 form-group">
                                                                        <label class="col-sm-2 control-label">Compañía celular</label>
                                                                        <div class="col-sm-10">
                                                                            <s:textfield cssClass="form-control" name="alumno.companiaCelular.nombre" id="companiaCelular"
                                                                                         placeholder="55-55555555" readonly="true"/>
                                                                        </div>
                                                                    </div>
                                                                    <div class="col-sm-12 form-group">
                                                                        <label class="col-sm-2 control-label">Teléfono de casa</label>
                                                                        <div class="col-sm-10">
                                                                            <s:textfield cssClass="form-control" name="alumno.telefonoCasa" id="telefonoCasa" placeholder="55-55555555"
                                                                                         readonly="true"/>
                                                                        </div>
                                                                    </div> 
                                                                </div>
                                                                <div class="tab-pane" id="oportunidades-tab">
                                                                    <div class="col-sm-12 form-group">
                                                                        <div class="col-sm-10 col-sm-offset-2">
                                                                            <label for="beneficiarioOportunidades">
                                                                                ¿Es beneficiario del programa PROSPERA (antes Oportunidades)?
                                                                            </label>
                                                                            <div class="radio">
                                                                                <s:radio list="service.respuestaBoolean" name="alumno.beneficiarioOportunidades" disabled="true"/>
                                                                            </div>
                                                                        </div>
                                                                    </div>
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
    </div>    
    <!-- global scripts -->
    <script src="/vendors/jQuery/jquery-3.1.1.min.js"></script>
    <script src="/vendors/bootstrap/bootstrap-3.3.7.min.js"></script>
    <script src="/vendors/nanoscroller/jquery.nanoscroller-0.8.7.min.js"></script>
    <!-- theme scripts -->
    <script src="/resources/js/scripts.min.js"></script>    
    <!-- this page specific inline scripts -->
    <script type="text/javascript" src="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.js"></script>    
    <script type="text/javascript" src="/vendors/bootstrap-datepicker/js/bootstrap-datepicker.min.js"></script>    
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>
    <script>
                                                                                               $(document).ready(function () {
                                                                                                   var number =<s:property value="alumno.datosAcademicos.promedio"/>;
                                                                                                   document.getElementById("prom").value = number.toFixed(2);
                                                                                               });
    </script>
</body>