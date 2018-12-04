<%-- 
    Document   : Activación de tarjetas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Activar tarjeta bancaria</title>    
</head> 

<content tag="tituloJSP">
    Activar tarjeta
</content>

<body>
    <div class="row">
        <div class="col-xs-12">
            <div class="alert alert-info fade in">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                <i class="fa fa-info-circle fa-fw fa-lg"></i>
                <strong>¿Necesitas ayuda?</strong> Da click <a href="/misdatos/consultar.action#encabezado5">aquí</a> para consultar el manual de usuario.
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-xs-12">
            <s:if test="hasActionErrors()">
                <s:if test="warning==true">
                    <div class="alert alert-warning">
                        <i class="fa fa-warning fa-fw fa-lg"></i> <s:actionerror cssErrorClass="alert-warning"/>
                    </div>
                </s:if>
                <s:else>
                    <div class="alert alert-danger">
                        <i class="fa fa-times-circle fa-fw fa-lg"></i>
                        <strong>&iexcl;Error!</strong> <s:actionerror/>
                    </div>
                </s:else>
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
                <div class="main-box-body">                    
                    <div class="row">                        
                        <div class="col-sm-12">
                            <div class="panel-body">
                                <p>Ingresa los últimos 5 dígitos de la tarjeta que te fue proporcionada.</p>
                            </div>                            
                        </div>
                    </div>
                    <div class="row">                        
                        <div class="col-sm-6 col-sm-push-6">
                            <div class="panel-body">
                                <form class="form-horizontal" role="form" id="payment-form" action="/alumno/verificarTarjeta.action" >
                                    <div class="col-sm-12 form-group">
                                        <label for="numtarjeta" class="control-label col-sm-3 ">Número de tarjeta</label>
                                        <div class="col-sm-9">
                                            <div class="input-group">
                                                <s:textfield 
                                                    type="number"
                                                    cssClass="form-control"
                                                    name="numTarjeta"
                                                    placeholder="Número de tarjeta"
                                                    id="numtarjeta"
                                                    required="true"
                                                    data-bv-digits="true"
                                                    data-bv-digits-message="Debes ingresar únicamente números"
                                                    data-bv-notempty="true"
                                                    data-bv-notempty-message="Debes ingresar un número de tarjeta"
                                                    maxLength="5"
                                                    maxlength="5"
                                                    style="min-width:200px"
                                                    />
                                                <span class="input-group-addon"><i class="fa fa-credit-card"></i></span>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-sm-12 form-group">
                                        <div class="col-lg-offset-2 col-lg-10">
                                            <button id="validarBTN" type="submit" class="fancybox fancybox.iframe btn btn-primary pull-right" >Activar tarjeta</button>
                                        </div>
                                    </div>
                                </form>
                            </div> 
                        </div>
                        <div class="col-sm-6 col-sm-pull-6">
                            <center>
                                <img src="/resources/img/tarjeta_bancaria/cuenta_perfiles.png" style="width: 100%;">
                            </center>
                        </div>
                    </div>
                </div>  
            </div>
        </div>
    </div>
</body>

<content tag="endScripts">
    <!-- Plugin para validar campos -->
    <script type="text/javascript" src="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.js"></script>
</content>

<content tag="inlineScripts">                                   
    <script type="text/javascript" language="javascript" class="init">              
        $(document).ready(function () {
            $('#payment-form').bootstrapValidator();
            <s:if test="hasActionErrors()">                
                if ($('.alert-warning').length > 0) {
                    $("#numtarjeta").attr("disabled", true);
                    $(":submit").attr("disabled", true);
                }                        
            </s:if>
            <s:if test="hasActionMessages()">                                
                if ($('.alert-warning').length > 0) {
                    $("#numtarjeta").attr("disabled", true);
                    $(":submit").attr("disabled", true);
                }                
            </s:if>
        });        
    </script>    
</content>