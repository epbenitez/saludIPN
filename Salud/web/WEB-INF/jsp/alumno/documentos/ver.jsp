<%-- 
    Document   : Descarga Documentos
    Created on : 7/07/2016, 11:27:24 AM
    Author     : Tania G. Sánchez
    Redesing   : Mario Márquez
--%>

<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <link rel="stylesheet" type="text/css" href="/vendors/fancy-box/jquery.fancybox.min.css"/>
    <title>Descarga de documentos</title>

    <style>
        .document_icon {
            padding-bottom: 20px;      
        }

    </style>
</head> 

<content tag="tituloJSP">
    Descarga de documentos.
</content>

<body>
    <div class="row">
        <div class="col-xs-12">
            <div class="main-box clearfix">                
                <header class="main-box-header clearfix">
                    <h2 class="pull-left">Descarga los documentos y entregalos en tu unidad académica.</h2>
                </header>         
                <div class="main-box-body clearfix">                     
                    <div style="padding-bottom: 20px; text-align: center;">                         
                        <s:if test="ie!=null && ie.tieneSolicitudOrdinaria">
                            <div class="col-lg-2 col-xs-6 document_icon">
                                <a href="#" target='_blank' id="solicitudOrdinaria"
                                   data-toggle="popover" data-trigger="hover" data-placement="bottom" data-content="Solicitud Ordinaria" onclick="addURL(this, 5)" >
                                    <img src="/resources/img/documentos/solicitud-ordinaria.png" alt=""/>
                                </a>
                            </div>
                        </s:if>
                        <s:if test="ie!=null && ie.tieneSolicitudTransporte">
                            <div class="col-lg-2 col-xs-6 document_icon">
                                <a href="#" target='_blank' id="solicitudTransporte" 
                                   data-toggle="popover" data-trigger="hover" data-placement="bottom" data-content="Solicitud de Transporte" onclick="addURL(this, 6)">
                                    <img src="/resources/img/documentos/solicitud-transporte.png" alt=""/>
                                </a>
                            </div>
                        </s:if>
                        <div class="col-lg-2 col-xs-6 document_icon">
                            <a href="#" target='_blank' id="cartaCompromiso" 
                               data-toggle="popover" data-trigger="hover" data-placement="bottom" data-content="Carta Compromiso" onclick="addURL(this, 1)">
                                <img src="/resources/img/documentos/carta-compromiso.png" alt=""/>
                            </a>
                        </div>
                        <div class="col-lg-2 col-xs-6 document_icon">
                            <a href="#" target='_blank'  id="ingresosEgresos" 
                               data-toggle="popover" data-trigger="hover" data-placement="bottom" data-content="Comprobante de Ingresos y Egresos" onclick="addURL(this, 2)" id="comprobante">
                                <img src="/resources/img/documentos/ingresos-egresos.png" alt=""/>
                            </a>
                        </div>
                        <!--<a href="#" target='_blank' onclick="addURL(this, 3)" id="cartaBecalos"><span class="fa fa-download"></span> Carta Bécalos</a>-->
                        <div class="col-lg-2 col-xs-6 document_icon">
                            <a href="#" target='_blank'  id="noComprobables" 
                               data-toggle="popover" data-trigger="hover" data-placement="bottom" data-content="Carta de Ingresos No comprobables" onclick="addURL(this, 4)" id="cartaNoComprobable">
                                <img src="/resources/img/documentos/no-comprobables.png" alt=""/>
                            </a>
                        </div>
                        <div class="col-lg-2 col-xs-6 document_icon">
                            <a href="/resources/downloadable/Carta_Renuncia.pdf" target='_blank'  id="renuncia" 
                               data-toggle="popover" data-trigger="hover" data-placement="bottom" data-content="Carta de Renuncia de Beca">
                                <img src="/resources/img/documentos/carta-renuncia.png" alt=""/>
                            </a>
                        </div>
                    </div> 
                </div>
            </div>        
        </div> <!-- Termina primer cuadro blanco -->

        <s:if test="ie.egresos!=null || ie.egresos.isEmpty()">
            <div class="row">
                <div class="col-xs-12">
                    <div class="main-box clearfix">                
                        <header class="main-box-header clearfix">
                            <h2 class="pull-left">Para tu formato de ingresos y egresos, considera la siguiente información que has introducido en tu <s:property value="ie.fuenteDeInformacion" escape="false"  />, pues <b>es de suma importancia que tengan consistencia</b>.</h2>
                        </header>
                    </div>
                </div>
            </div>
        </s:if>
        <s:if test="ie.ingresosPerCapita!=null && ie.totalIntegrantes!=null">
            <div class="row">
                <div class="col-lg-6 col-sm-6 col-xs-12">
                    <div class="main-box infographic-box">
                        <i class="fa fa-users yellow-bg"></i>
                        <span class="headline">Integrantes de la familia</span>
                        <span class="value">
                            <span class="timer" data-from="120" data-to="2562" data-speed="1000" data-refresh-interval="50">
                                <s:property  value="getText('{0,number,#,##0}',{ie.totalIntegrantes})"/>
                            </span>
                        </span>
                    </div>
                </div>
                <div class="col-lg-6 col-sm-6 col-xs-12">
                    <div class="main-box infographic-box">
                        <i class="fa fa-money green-bg "></i>
                        <span class="headline">Ingreso Mensual</span>
                        <span class="value">
                            <span class="timer" data-from="120" data-to="2562" data-speed="1000" data-refresh-interval="50">
                                $<s:property value="ie.totalIngresos" />
                            </span>
                        </span>
                    </div>
                </div>
                <div class="col-lg-6 col-sm-6 col-xs-12">
                    <div class="main-box infographic-box">
                        <i class="fa fa-dollar  emerald-bg "></i>
                        <span class="headline">Ingreso Per Cápita</span>
                        <span class="value">
                            <span class="timer" data-from="120" data-to="2562" data-speed="1000" data-refresh-interval="50">
                                $<s:property value="ie.ingresosPerCapita" />
                            </span>
                        </span>
                    </div>
                </div>
                <div class="col-lg-6 col-sm-6 col-xs-12">
                    <div class="main-box infographic-box">
                        <i class="fa fa-lightbulb-o red-bg "></i>
                        <span class="headline">Ésta información ha sido obtenida de los datos ingresados en tu <s:property value="ie.fuenteDeInformacion" escape="false"  /></span>
                        <span class="value">
                            <span >

                            </span>
                        </span>
                    </div>
                </div>
            </div>

        </s:if>
        <s:if test="ie.egresos!=null || ie.egresos.isEmpty()">
            <div class="row">
                <div class="col-xs-12">
                    <div class="main-box clearfix">        
                        <div class="main-box-body clearfix">                     
                            <table id="" class="table table-striped table-hover dt-responsive" style="width: 100%">
                                <thead>
                                    <tr>
                                        <th>Concepto</th>
                                        <th>Monto</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <s:iterator value="ie.egresos">
                                        <tr>
                                            <td><s:property value="nombre" escape="false"/></td>
                                            <td><s:property value="monto"/></td>
                                        </tr>
                                    </s:iterator>
                                </tbody>
                            </table> 
                        </div>  
                    </div>
                </div>        
            </div>
        </s:if>
    </div>
</body>

<content tag="endScripts">
    <script type="text/javascript" src="/vendors/fancy-box/jquery.fancybox.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>
                                   $(document).ready(function () {
                                       $(".fancybox").fancybox();
                                       $(function () {
                                           $('[data-toggle="popover"]').popover();
                                       });
                                   });

                                   function addURL(element, op) {
                                       $("#cartaCompromiso").blur();// Evita que el botón sea desactivado                            
                                       $("#comprobante").blur();// Evita que el botón sea desactivado    
                                       $("#cartaBecalos").blur();// Evita que el botón sea desactivado    
                                       $("#cartaNoComprobable").blur();// Evita que el botón sea desactivado 
                                       $("#renuncia").blur();// Evita que el botón sea desactivado  
                                       $(element).attr('href', function () {
                                           if (op === 1) {
                                               return "/misdatos/descargar/cartaCompromisoDocumentos.action";
                                           } else if (op === 2) {
                                               return "/misdatos/descargar/comprobanteDocumentos.action";
                                           } else if (op === 3) {
                                               return "/misdatos/descargar/cartaBecalosDocumentos.action";
                                           } else if (op === 4) {
                                               return "/misdatos/descargar/cartaNoComprobablesIngresosNoComprobables.action";
                                           } else if (op === 5) {
                                               return "/misdatos/descargar/pdfEstudioSocioeconomico.action?cuestionarioId=1";
                                           } else if (op === 6) {
                                               return "/misdatos/descargar/pdfEstudioSocioeconomicoTransporte.action?cuestionarioId=2";
                                           } else if (op === 7) {
                                               return "/misdatos/descargar/pdfEstudioSocioeconomicoTransporte.action?cuestionarioId=2";
                                           }
                                       });
                                   }
    </script>        
</content>