<%-- 
    Document   : Reportar tarjeta perdida
    Redesing   : Mario Márquez
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>        
    <link rel="stylesheet" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css" type="text/css" />            
    
    <title>Reportar tarjeta perdida</title>
</head> 

<content tag="tituloJSP">
    Reportar extravío de tarjeta bancaria
</content>

<body>
    <div class="row">
        <div class="col-xs-12">
            <div class="alert alert-info fade in">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                <i class="fa fa-info-circle fa-fw fa-lg"></i>
                <strong>¿Necesitas ayuda?</strong> Da click <a href="/misdatos/consultar.action#encabezado7">aquí</a> para consultar el manual de usuario.
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
            <div class="main-box" >
                <div class="main-box-body clearfix">                                                
                    <p>Recuerda realizar el reporte de extravío en el banco, de lo contrario no se te generará un nuevo número de tarjeta y en consecuencia, no podrás recibir tus depósitos siguientes, en caso de haberlos.</p>                                            
                    
                    <form class="form-horizontal" role="form" id="payment-form" action="/alumno/suspenderTarjeta.action">
                            <div class="col-sm-12 form-group">
                                <label for="numTarjeta" class="col-sm-3 control-label">Número de tarjeta</label>
                                <div class="col-sm-9">
                                    <div class="input-group">
                                        <s:textfield 
                                            cssClass="form-control cc-number"
                                            name="tarjeta.tarjetaBancaria.numtarjetabancaria"
                                            placeholder="Número de tarjeta"
                                            autocomplete="cc-number"
                                            x-autocompletetype="cc-number"
                                            id="numtarjeta"
                                            required="true"
                                            disabled="true"
                                            data-bv-notempty="true"
                                            data-bv-notempty-message="Debes ingresar un número de tarjeta"
                                            />                                        
                                        <span class="input-group-addon"><i class="fa fa-credit-card"></i></span>
                                    </div>
                                </div>
                            </div>

                            <div class="col-sm-12 form-group">
                                <label  class="col-sm-3 control-label" id='label' for="numReporte">Número de reporte bancario</label>
                                <div class="col-sm-9">
                                    <s:textfield 
                                        cssClass="form-control"
                                        name="numReporte"
                                        id="numReporte"
                                        required="true"
                                        data-bv-notempty="true"
                                        data-bv-notempty-message="Debes ingresar un número de reporte"
                                    />                                    
                                </div>
                            </div>
                            <div class="col-sm-12 form-group">
                                <label  class="col-sm-3 control-label" id='labelMotivo' for="motivo">Motivo del reporte</label>
                                <div class="col-sm-9">
                                    <s:textfield 
                                        cssClass="form-control"
                                        name="motivo"
                                        id="motivo"
                                        required="true"
                                        data-bv-notempty="true"
                                        data-bv-notempty-message="Debes ingresar un motivo del reporte"
                                        maxLength="100"
                                        maxlength="100"
                                    />
                                </div>
                            </div>                            
                            
                            <div class="col-sm-12 form-group">
                                <div class="col-lg-offset-2 col-lg-10">
                                    <s:hidden name="tarjeta.id" />
                                    <button id="sendButton" type="submit" class="btn btn-primary pull-right">Reportar tarjeta</button>
                                </div>                            
                            </div>
                    </form>                                    
                </div>
            </div>
        </div>
    </div> 
</body>

<content tag="endScripts">    
    <!-- Plugin para validar campos -->
    <script type="text/javascript" src="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.js"></script>
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>
        
        $(document).ready(function () {
            $('#payment-form').bootstrapValidator({
                feedbackIcons: {
                    valid: 'glyphicon glyphicon-ok',
                    invalid: 'glyphicon glyphicon-remove',
                    validating: 'glyphicon glyphicon-refresh'
                }
            }).on('success.form.bv', function(e) {
                e.preventDefault();                                
                ModalGenerator.notificacion({
                    "titulo": "Atención",
                    "cuerpo": "¿Estás seguro de querer reportar tu tarjeta como extraviada? Este proceso, no podrá ser revertido y tus depósitos ya no podrán ser realizados a dicha tarjeta",
                    "tipo": "WARNING",
                    "funcionAceptar": function () {
                        document.getElementById('payment-form').submit();
                    },
                    "funcionCancelar": function () {
                        $('#sendButton').blur();
                    },
                });       
            });
            <s:if test="hasActionErrors()">
                if ($('.alert-warning').length > 0) {
                    $("#numtarjeta").attr("disabled", true);
                    $('#numReporte').attr('disabled', true);
                    $('#motivo').attr('disabled', true);
                    $("#del_error").remove();
                    $('#sendButton').attr('disabled', true);
                }
                if ($('#del_error').length > 0) {
                    $("#numtarjeta").attr("disabled", false);
                    $('#numReporte').attr('disabled', false);
                    $('#motivo').attr('disabled', true);
                    $('#sendButton').attr('disabled', true);
                    $("#del_error").remove();
                }
                $('#numtarjeta').val(''); 
            </s:if>
            <s:if test="hasActionMessages()">
                if ($('.alert-warning').length > 0) {
                    $("#numtarjeta").attr('disabled', true);
                    $('#numReporte').attr('disabled', true);
                    $('#motivo').attr('disabled', true);
                    $('#sendButton').attr('disabled', true);
                }
            </s:if>                                   
        });
    </script>
</content>