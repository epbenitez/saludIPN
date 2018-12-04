<%-- 
    Document   : Panel de Control Alumno
    Redesing   : Mario Márquez
--%>

<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <link rel="stylesheet" type="text/css" href="/vendors/hopscotch/hopscotch.min.css">    
    <link rel="stylesheet" type="text/css" href="/vendors/Notifications/css/ns-default.min.css" />
    <link rel="stylesheet" type="text/css" href="/vendors/Notifications/css/ns-style-bar.css" />
    <link rel="stylesheet" type="text/css" href="/vendors/Notifications/css/ns-style-theme.min.css" />  

    <style>
        .bg-tema{background-color: #1ABC9C;}
        .texto-tema{color: #1ABC9C;}
        .bg-beca{background-color: #B71C62;}
        .texto-beca{color: #B71C62;}
        .bg-opaque {background-color: #484848}
        .txt-opaque {color: #484848}
        .bg-pago {background-color: #1C3144}
        .txt-pago {color: #C75F84}
        .bg-rechazo{background-color: #dd191d;}

        td{text-align: center;}

        <s:if test="!isAccount" >
            .encabezado-cc i {background-color: #484848;}
            #numTarjetaB {color: #484848;}
        </s:if>
        <s:elseif test="stsId == 14" >
            .encabezado-cc i {background-color: #dd191d;}
            #numTarjetaB {color: #dd191d;}
        </s:elseif>        
        <s:else>
            .encabezado-cc i {background-color: #1ABC9C;}
            #numTarjetaB {color: #1ABC9C;}
        </s:else>
        .infographic-box .div-img {
            display: block;
            float: left;
            margin-right: 15px;
            width: 60px;
            height: 60px;
            line-height: 60px;
            border-radius: 50%;
            background-clip: padding-box;            
            /* stops bg color from leaking outside the border: */
            color: #fff; }        
        </style>

        <title>Bienvenido</title>    
    </head> 

    <content tag="tituloJSP">
        Panel de control
    </content>

    <body>
        <div class="row">
        <div class="col-xs-12">
            <div class="alert alert-info fade in">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                <i class="fa fa-info-circle fa-fw fa-lg"></i>
                <a href="http://www.ipn.mx/dse/Becas/Documents/becas_principal/convocatoria/convocatoria_2018-2019.pdf" target="_blank">
                    <strong>
                        Descarga la convocatoria general de becas 2018 - 2019 
                    </strong>
                </a>                                
            </div>
        </div>
        <s:if test="mostrarMsjReferenciaUMS" >
            <div class="col-xs-12">
                <div class="alert alert-warning fade in">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                    <i class="fa fa-warning fa-fw fa-lg"></i>
                    <strong>Atención</strong> Las ordenes de pago referenciadas para la Beca Universal de NMS, estarán disponibles hasta el 18 de Octubre de 2017.
                </div>
            </div>
        </s:if>
    </div>

    <div class="row">
        <s:if test="tarjetaBancaria != null" >
            <div class="col-md-6">
                <div class="main-box infographic-box encabezado-cc" style="margin-bottom: 0px;border-bottom: 0px;">                
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
                    <!--Mensaje principal-->
                    <a href="<s:property  value="thirdMsgLink" />" style="text-decoration: none;">
                        <span class="value" id="numTarjetaB">
                            <s:property  value="mainMsg" />
                        </span>
                    </a>
                    <!--Mensaje secundario-->
                    <span class="headline">
                        <s:property  value="secondMsg" />
                    </span>                
                </div>
                <div class="main-box clearfix project-box">
                    <div class="main-box-body clearfix">
                        <div class="project-box-ultrafooter clearfix" style="border-top: 0px">
                            <a href="<s:property  value="thirdMsgLink" />" class="link pull-right" id="linkMovimiento" style="color: #1a83bc">
                                <s:property  value="thirdMsg" /> <i class="fa fa-arrow-circle-right fa-lg"></i>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </s:if>

        <!--Inicia beca-->
        <div class="col-md-6">
            <div class="main-box infographic-box" style="margin-bottom: 0px;border-bottom: 0px;">                                        
                <i class="fa fa-graduation-cap ${stsId == 13 && isItTime && hasOtorgamientoPeriodoActual ? 'bg-beca' : 'bg-opaque'}"></i>
                <span class="value">
                    <span class="timer ${stsId == 13 && isItTime && hasOtorgamientoPeriodoActual ? 'texto-beca' : 'txt-opaque'}">
                        <s:property  value="textoPrincipalBeca" />
                    </span>
                </span>
                <span class="headline"><s:property escape="false" value="textoSecundarioBeca" /></span>
            </div>
            <div class="main-box clearfix project-box">
                <div class="main-box-body clearfix">
                    <div class="project-box-ultrafooter clearfix" style="border-top: 0px">
                        <a href="<s:property  value="enlaceBeca" />" class="link pull-right" id="linkMovimiento" style="color: #1a83bc">
                            <s:property escape="false" value="textoEnlaceBeca" />&nbsp&nbsp <i class="fa fa-arrow-circle-right fa-lg"></i>
                        </a>
                    </div>
                </div>
            </div>
        </div>
        <!--Termina beca-->

        <!--Pago referenciado-->
        <s:if test="mostrarReferencia">
            <div class="col-md-6">
                <div class="main-box infographic-box" style="margin-bottom: 0px;border-bottom: 0px;">                
                    <!--<i class="fa fa-credit-card"></i>-->                        
                    <div class="div-img bg-pago">
                        <img src="/resources/img/panelAlumno/orden-deposito.png" style="margin-left: 13px; margin-top: -2px">
                    </div>                    
                    <!--Mensaje principal-->
                    <a href="/depositos/verAdministraDepositos.action" style="text-decoration: none;">
                        <span class="value txt-pago">
                            Pago referenciado
                        </span>
                    </a>
                    <!--Mensaje secundario-->
                    <span class="headline">Periodo <s:property  value="referencePeriod" /></span>
                </div>
                <div class="main-box clearfix project-box">
                    <div class="main-box-body clearfix">
                        <div class="project-box-ultrafooter clearfix" style="border-top: 0px">
                            <a href="/misdatos/descargar/pdfReferenciaBancaria.action" target='_blank' class="link pull-right" id="descarga" style="color: #1a83bc">
                                Descarga aquí tu orden de pago referenciada &nbsp&nbsp <i class="fa fa-arrow-circle-right fa-lg"></i>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </s:if>
        <!--Termina Pago referenciado-->

        <!--Pago referenciado universal-->
        <s:if test="mostrarReferenciaU">
            <div class="col-md-6">
                <div class="main-box infographic-box" style="margin-bottom: 0px;border-bottom: 0px;">                
                    <div class="div-img bg-pago">

                        <img src="/resources/img/panelAlumno/orden-deposito.png" style="margin-left: 13px; margin-top: -2px">
                    </div>                    
                    <!--Mensaje principal-->
                    <a href="<s:property  value="refUniversalURL" />" style="text-decoration: none;" target='_blank'>
                        <span class="value txt-pago">
                            Beca Universal
                        </span>
                    </a>
                    <!--Mensaje secundario-->
                    <span class="headline">Orden de pago referenciada</span>
                </div>
                <div class="main-box clearfix project-box">
                    <div class="main-box-body clearfix">
                        <div class="project-box-ultrafooter clearfix" style="border-top: 0px">
                            <a href="<s:property  value="refUniversalURL" />" target='_blank' class="link pull-right" id="descargaBU" style="color: #1a83bc">
                                Descarga aquí tu orden de pago referenciada &nbsp&nbsp <i class="fa fa-arrow-circle-right fa-lg"></i>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </s:if>
        <!--Termina Pago referenciado universal-->
        <!--Inicia depósitos-->
        <div class="col-md-6">
            <s:if test="%{depositos == null}">
                <div class="main-box infographic-box" style="margin-bottom: 0px;border-bottom: 0px;">                                    
                    <!--<i class="fa fa-credit-card"></i>-->                        
                    <div class="div-img bg-opaque">
                        <img src="/resources/img/panelAlumno/depositos2.png" style="margin-left: 7.5px; margin-top: -2px">
                    </div>                    
                    <!--Mensaje principal-->
                    <a href="/depositos/verAdministraDepositos.action" style="text-decoration: none;">
                        <span class="value txt-opaque">
                            Sin depósitos
                        </span>
                    </a>
                    <!--Mensaje secundario-->
                    <span class="headline">
                        En el periodo actual
                    </span>                
                </div>
            </s:if>
            <s:else>
                <div class="main-box" style="margin-bottom: 0px;border-bottom: 0px;">
                    <div class="row">                    
                        <div class="main-box-body clearfix">
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="table-responsive">
                                        <table class="table table-striped table-hover">
                                            <thead>
                                                <tr>
                                                    <th class="text-center"><span>Beca</span></th>
                                                    <th class="text-center"><span>Fecha</span></th>
                                                    <th class="text-center"><span>Mes</span></th>
                                                    <th class="text-center"><span>Cantidad</span></th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <s:iterator value="depositos">
                                                    <tr>
                                                        <td><s:property value="otorgamiento.tipoBecaPeriodo.tipoBeca.nombre"/></td>
                                                        <td><s:property value="fechaDeposito"/></td>
                                                        <td><s:property value="ordenDeposito.mes"/></td>
                                                        <td><s:property value="monto"/></td>
                                                    </tr>
                                                </s:iterator>
                                            </tbody>                            
                                        </table>                        
                                    </div>
                                    <h4 class="pull-right" style="margin-bottom: 10px">Últimos Depósitos</h4>
                                </div>
                            </div>
                        </div>
                    </div>                    
                </div>
            </s:else>
            <div class="main-box clearfix project-box">
                <div class="main-box-body clearfix">
                    <div class="project-box-ultrafooter clearfix" style="border-top: 0px">
                        <a href="/depositos/verAdministraDepositos.action" class="link pull-right" id="linkMovimiento" style="color: #1a83bc">
                            Para más información sobre tus depósitos da click aquí &nbsp&nbsp <i class="fa fa-arrow-circle-right fa-lg"></i>
                        </a>
                    </div>
                </div>
            </div>
        </div> 

        <!--Inicia depósitos rechazados-->
        <s:if test="errorDeposit" >
            <div class="col-md-6">
                <div class="main-box infographic-box" style="margin-bottom: 0px;border-bottom: 0px;">
                    <div class="div-img bg-rechazo">
                        <img src="/resources/img/panelAlumno/deposito-rechazados.png" style="margin-left: 10px; margin-top: -2px">
                    </div>    
                    <a href="/depositos/verAdministraDepositos.action" style="text-decoration: none;">
                        <span class="value">
                            <span class="timer txt-opaque">
                                Depósitos rechazados
                            </span>
                        </span>
                    </a>
                    <span class="headline">Revisa tu historial de depósitos</span>
                </div>
                <div class="main-box clearfix project-box">
                    <div class="main-box-body clearfix">
                        <div class="project-box-ultrafooter clearfix" style="border-top: 0px">
                            <a href="/depositos/verAdministraDepositos.action" class="link pull-right" id="linkRechazados" style="color: #1a83bc">
                                Para más información sobre tus depósitos da click aquí &nbsp&nbsp <i class="fa fa-arrow-circle-right fa-lg"></i>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </s:if>
        <!--Termina depósitos rechazados-->

        <!--Inicia Solicitudes-->
        <s:if test="%{solicitudList != null}">
            <div class="col-md-6">
                <div class="main-box" style="margin-bottom: 0px;border-bottom: 0px;">
                    <div class="row">                    
                        <div class="main-box-body clearfix">
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="table-responsive">
                                        <table class="table table-striped table-hover">
                                            <thead>
                                                <tr>
                                                    <th class="text-center"><span>Solicitud</span></th>
                                                    <th class="text-center"><span>Estatus</span></th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <s:iterator value="solicitudList">
                                                    <tr>
                                                        <s:iterator>
                                                            <td><s:property escape="false"/></td>
                                                        </s:iterator>
                                                    </tr>
                                                </s:iterator>
                                            </tbody>                            
                                        </table>                        
                                    </div>
                                    <h4 class="pull-right" style="margin-bottom: 10px">Solicitudes</h4>
                                </div>
                            </div>
                        </div>
                    </div>                    
                </div>
                <div class="main-box clearfix project-box">
                    <div class="main-box-body clearfix">
                        <div class="project-box-ultrafooter clearfix" style="border-top: 0px">
                            <a href="/misdatos/verEstatusSolicitud.action" class="link pull-right" id="linkSolicitudes" style="color: #1a83bc">
                                Para más información sobre tus solicitudes da click aquí &nbsp&nbsp <i class="fa fa-arrow-circle-right fa-lg"></i>
                            </a>
                        </div>
                    </div>
                </div>
            </div> 
        </s:if>
        <!--Termina solicitudes-->
    </div>
</body>

<content tag="endScripts">
    <script src="/vendors/hopscotch/hopscotch.min.js" type="text/javascript"></script>
    <script src="/vendors/maphighligth/map.js" type="text/javascript"></script>  
    <script src="/vendors/Notifications/js/classie.min.js" type="text/javascript"></script>
    <script src="/vendors/Notifications/js/modernizr.custom.min.js" type="text/javascript"></script>
    <script src="/vendors/Notifications/js/notificationFx.js" type="text/javascript"></script>
</content>


<content tag="inlineScripts">
    <script>
        <s:if test="stsId == 13" >
        var texto = $.trim($('#numTarjetaB').text());
        if (texto.length > 11)
            var textoArr = [texto.substring(0, 3), texto.substring(3, 6), texto.substring(6, 17), texto.substring(17)];
        else
            var textoArr = [texto.substring(0, 4), texto.substring(4, 8), texto.substring(8, 11)];
        var textoCC = textoArr.join(" ");
        $('#numTarjetaB').text(textoCC);
        </s:if>

        $(document).ready(function () {
            if (<s:property value="notificacionesRolSize" /> !== 0)
            {
                var notification = new NotificationFx({
                    color: "<s:property value="notificacionesRol[0].tipoNotificacion.color"/>",
                    icono: "<s:property value="notificacionesRol[0].tipoNotificacion.icono"/>",
                    nombre: "<s:property value="notificacionesRol[0].titulo"/>",
                    texto: "<s:property value="notificacionesRol[0].notificacion" escape="false" />",
                    ttl: 5000
                });
                // show the notification
                notification.show();
            }
        });

    </script>
</content>

<security:authorize ifAnyGranted="ROLE_JEFEBECAS,ROLE_JEFEADMINISTRATIVO">


</security:authorize>