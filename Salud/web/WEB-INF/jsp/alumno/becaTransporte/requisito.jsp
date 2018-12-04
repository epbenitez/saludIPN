<%-- 
    Document   : requisito
    Created on : 1/07/2016, 12:15:43 PM
    Author     : Tania G. Sánchez
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.0//EN" "http://www.w3.org/TR/REC-html40/strict.dtd">
<html>
    <head>
        <title>ESE Beca de Transporte</title>
    </head>
    <body>
        <div class="main-box clearfix">
            <div class="otorgamiento col-sm-10 col-sm-offset-1">
                <div class="clearfix" >&nbsp;</div>
                <img src="/resources/img/eseTransporte/beca-transporte.jpg" alt="Solicitud de Transporte" style="width: 100%;"/>
                <div class="clearfix" >&nbsp;</div>

                <s:if test="periodoActivo && becaCompatibleESE && !tieneValidacionInscripcion && !mandarPorManutencion">
                    <a href="/misdatos/eseBecaTransporte.action" id="B341267725464428117" title="Continuar con la solicitud" type="button" class="btn btn-primary btn-lg pull-right">
                        Continuar
                    </a>
                    <a href="/misdatos/eseBecaTransporte.action" title="Continuar con la solicitud" class="sr-only">
                        Continuar con la solicitud
                    </a>
                </s:if>                
                <div class="clearfix" >&nbsp;</div>
                <div class="clearfix" >&nbsp;</div>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 text-center">            
                <s:if test="periodoActivo">
                    <s:if test="tieneValidacionInscripcion">
                    <div class="alert alert-info col-xs-12 col-sm-6 col-sm-offset-3">
                        <i class="fa fa-info-circle fa-fw fa-lg" style="font-size: 50px; margin-top: 10px"></i>
                        Este semestre no es necesario que vuelvas a contestar la solicitud.
                    </div>
                    </s:if>    
                    <s:elseif test="tieneBecaExterna">
                    <div class="alert alert-warning col-xs-12 col-sm-6 col-sm-offset-3">
                        <div class="col-xs-3">
                            <i class="fa fa-warning fa-fw fa-lg" style="font-size: 50px; margin-top: 10px"></i>
                        </div>
                        <div class="col-xs-9">
                            Hemos detectado que ya eres beneficiario de una beca en una institución fuera del Instituto Politécnico Nacional, 
                            por lo que de acuerdo a normativas para la asignación de becas, no podemos ofrecerte realizar una solicitud 
                            para éste periodo.
                        </div>
                    </div>      
                    </s:elseif>
                    <s:elseif test="!becaCompatibleESE">
                    <div class="alert alert-warning col-xs-12 col-sm-6 col-sm-offset-3">
                        <div class="col-xs-3">
                            <i class="fa fa-warning fa-fw fa-lg" style="font-size: 50px; margin-top: 10px"></i>
                        </div>
                        <div class="col-xs-9">
                            Ya tienes otorgada una beca que no es compatible con la <strong>Beca de Transporte</strong>.
                            Por lo que no puedes llenar esta solicitud.
                        </div>
                    </div>      
                    </s:elseif>
                    <s:elseif test="mandarPorManutencion">
                    <div class="alert alert-warning col-xs-12 col-sm-6 col-sm-offset-3">
                        <div class="col-xs-3">
                            <i class="fa fa-warning fa-fw fa-lg" style="font-size: 50px; margin-top: 10px"></i>
                        </div>
                        <div class="col-xs-9">
                            Por el momento la convocatoria disponible es para becas Manutención.
                             Por favor,
                            <a href="https://www.subes.sep.gob.mx/">dirígete al sitio de SUBES y realiza tu registro.</a>
                             En caso de resultar beneficiado, podrás darle seguimiento aquí, en tu sesión de SIBec.
                        </div>
                    </div>
                    </s:elseif>
                </s:if>
                <s:else>
                <div class="alert alert-warning col-xs-12 col-sm-6 col-sm-offset-3">
                    <div class="col-xs-3">
                        <i class="fa fa-warning fa-fw fa-lg" style="font-size: 50px; margin-top: 10px"></i>
                    </div>
                    <div class="col-xs-9">
                        <strong>El periodo de registro para la Beca de Transporte ha finalizado. </strong>
                        Por lo que no puedes llenar esta solicitud.
                    </div>
                </div>
                <!--Se mantiene la imagen por si fuera necesario regresar.-->
                <!--<center><img src="/resources/img/eseTransporte/mensajeFinRegistro.png"/></center>-->
                </s:else>                
            </div>
        </div>
    </body>
</html>