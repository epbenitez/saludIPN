<%-- 
    Document   : inicioBecaUniversal
    Created on : 13/10/2017, 02:18:39 PM
    Author     : Tania G. Sánchez Manilla
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.opensymphony.xwork2.ActionContext;" %>

<head>    
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
    <link rel="stylesheet" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css" type="text/css" />

    <title>Registro de alumnos - SIBEC</title>
</head> 

<body id="login-page">
    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <div id="login-box">
                    <div id="login-box-holder">
                        <div class="row">
                            <div class="col-xs-12">
                                <header id="login-header">
                                    <div id="login-logo">
                                        BECA UNIVERSAL
                                    </div>
                                </header>

                                <s:if test="hasActionErrors()">
                                    <div class="col-xs-12 errorIniciar">
                                        <s:set var="error"> <s:actionerror cssClass="errors"/></s:set>
                                        <s:actionerror cssClass="errors"/>
                                    </div>
                                </s:if>
                                <s:if test="hasActionMessages()">
                                    <div class="col-xs-12 exitoIniciar">                                            
                                        <s:actionmessage />
                                    </div>
                                </s:if>

                                <div id="login-box-inner">
                                    <div class="row">
                                        <div class="col-lg-5" id="img_login">
                                            <img src="/resources/img/login/logo-sibec.svg">
                                        </div> 
                                        <div class="col-lg-7">                                                
                                            <form role="form"  name="inicio" action="/registro/guardarBecaUniversalRegistro.action" method="POST" class="formularioRegistro" id="form-registro">
                                                <div class="form-group">
                                                    <div class="input-group">
                                                        <span class="input-group-addon">
                                                            <img src="/resources/img/login/icono_boleta.jpg" width="23" height="23">
                                                        </span>    
                                                        <input id="boleta" name="numeroDeBoleta" class="form-control" type="text" placeholder="Boleta" 
                                                               required="true" 
                                                               data-bv-notempty="true"
                                                               data-bv-notempty-message="La boleta es necesaria"
                                                               pattern="^[a-zA-Z0-9\s]+$" 
                                                               data-bv-regexp-message="El número de boleta sólo puede estar conformado por números y letras."
                                                               data-bv-stringlength="true" 
                                                               data-bv-stringlength-min="8" 
                                                               data-bv-stringlength-message="La boleta debe ser de 10 dígitos y/o letras."
                                                               maxlength="10"
                                                               onblur="$(this).val($(this).val().toUpperCase())"/>
                                                    </div>   
                                                </div>
                                                <br>
                                                <button type="submit" id="enviarBtn" class="btn btn-success pull-right">¡Regístrate!</button>
                                            </form>
                                        </div> 
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div id="login-box-footer">
                        <div class="row">
                            <div class="pull-right col-xs-12" >
                                <span>&iquest;Ya te has inscrito para <b>solicitar tu beca?</b></span><br/>
                                <a href="/login.action" style="color:#56a9a8;">
                                    <b>¡Inicia sesión aquí!</b>
                                </a>
                            </div>
                        </div>
                    </div>
                    <header id="login-header">
                        <div id="login-logo">
                            <h1></h1>
                        </div>
                    </header>
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
    <script>
                                                                   $(document).ready(function () {
                                                                       $('#form-registro').bootstrapValidator();
                                                                   });
    </script>  
</body>