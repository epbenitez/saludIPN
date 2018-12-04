<%-- 
    Document   : ver
    Created on : 19/02/2016, 12:13:16 PM
    Author     : Tania G. Sánchez
    Redesing   : Mario Márquez
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>        
    <style>
        .bg-tema{background-color: #1ABC9C;}
        .texto-tema{color: #1ABC9C;}
        
        <s:if test="!isAccount || stsId == 14" >
            .encabezado-cc i {background-color: #484848;}
            #numTarjetaB {color: #484848;}
        </s:if>
        <s:else>
            .encabezado-cc i {background-color: #1ABC9C;}
            #numTarjetaB {color: #1ABC9C;}
        </s:else>
        
        .texto_subtitulo {
            font-size: 1.1em !important;
            font-weight: 300 !important;
            text-transform: none !important;
        }
    </style>
    <title>Monitoreo</title>    
</head> 

<content tag="tituloJSP">
    Monitoreo
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
    <!--Situación Bancaria-->
    <div clas="row">
        <div class="col-xs-12">
            <div class="main-box infographic-box pull-left merged encabezado-cc" style="width: 100%">
                <s:if test="isAccount" >
                    <s:if test="stsId == 14" >
                        <i class="fa fa-times"></i>                            
                    </s:if>
                    <s:else>
                        <i class="fa fa-bank"></i>
                    </s:else>                        
                </s:if>
                <s:else>
                    <i class="fa fa-credit-card"></i>                        
                </s:else> 
                
                <span class="value" id="numTarjetaB">
                    <s:property  value="mainMsg" />
                </span>
                <span class="headline texto_subtitulo"><s:property  value="msgAccountorCard" /></span>
            </div>
        </div>
    </div>

    <div class="row" id="div-tabla" style="display: none;">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <div class="col-md-12" style="padding: 0px">
                    <div class="responsive">                        
                        <table id="listado" class="table table-striped table-hover dt-responsive" style="width: 100%">
                            <thead>
                                <tr>
                                    <th>Tipo</th>
                                    <th>N&uacute;mero</th>
                                    <th>Estado</th>
                                    <th>Fecha</th>
                                    <th>Hora</th>
                                    <th>Observaciones</th>
                                </tr>
                            </thead>
                        </table>                        
                    </div>
                </div>
            </div>
        </div>                
    </div>
</body>

<content tag="endScripts">
    <script type="text/javascript" src="/vendors/fancy-box/jquery.fancybox.min.js"></script>
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js" data-init="true" data-ajax="/ajax/listadoBitacoraAlumnoTarjetaAjax.action?numeroTarjetaBancaria=<s:property  value="numeroTarjetaBancaria" />" data-div="div-tabla"></script>
</content>

<content tag="inlineScripts">
    <script>
        $(document).ready(function () {
                <s:if test="stsId == 13" >
                    formatearTarjeta();
                </s:if>                
        });

        function formatearTarjeta() {
            var texto = $.trim($('#numTarjetaB').text());
            if (texto.length > 11)
                var textoArr = [texto.substring(0, 3), texto.substring(3, 6), texto.substring(6, 17), texto.substring(17)];
            else
                var textoArr = [texto.substring(0, 4), texto.substring(4, 8), texto.substring(8, 11)];
            var textoCC = textoArr.join(" ");
            $('#numTarjetaB').text(textoCC);
        }
    </script>    
</content>
