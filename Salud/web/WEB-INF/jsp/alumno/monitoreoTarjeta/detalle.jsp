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
        .texto-tema{color: #03A9F4;}
    </style>

    <title>Monitoreo de cuentas</title>    
</head> 

<content tag="tituloJSP">
    Monitoreo de cuentas
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
        </div>
    </div>
    <div clas="row">
        <div class="col-lg-12 col-sm-12 col-xs-12">
            <div class="main-box infographic-box pull-left merged" style="width: 100%">
                <i class="fa fa-credit-card emerald-bg"></i>
                <span class="value texto-tema" id="numTarjetaB">
                    <s:property  value="numeroTarjetaBancaria" />
                </span>
                <span class="headline">Número de tarjeta asignada</span>
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
                                    <th data-orderable="true">Tipo</th>
                                    <th data-orderable="true">N&uacute;mero</th>
                                    <th data-orderable="true">Estado</th>
                                    <th data-orderable="true" data-default-order="true" data-default-dir="desc">Fecha</th>
                                    <th data-orderable="true">Hora</th>
                                    <th data-orderable="true">Observaciones</th>
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
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js"
    data-init="true" data-div="div-tabla"
    data-ajax="/ajax/listadoBitacoraAlumnoTarjetaAjax.action?numeroTarjetaBancaria=<s:property  value="numeroTarjetaBancaria" />&numeroDeBoleta=<s:property  value="numeroDeBoleta"/>">
    </script>
</content>

<content tag="inlineScripts">
    <script>
        $(document).ready(function () {
            formatearTarjeta();
        });

        function formatearTarjeta() {
            var texto = $.trim($('#numTarjetaB').text());
            var textoArr = [texto.substring(0, 4), texto.substring(4, 8), texto.substring(8, 12), texto.substring(12, 16)];
            var textoCC = textoArr.join(" ");
            $('#numTarjetaB').text(textoCC);
        }
    </script>    
</content>
