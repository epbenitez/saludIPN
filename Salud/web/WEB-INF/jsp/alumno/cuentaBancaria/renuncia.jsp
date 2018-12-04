<%-- 
    Document   : renuncia
    Created on : 1/03/2017, 01:00:09 PM
    Author     : Tania G. Sánchez
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Renuncia a cuenta</title>
</head> 

<content tag="tituloJSP">
    Renuncia a cuenta
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
                    <strong>&iexcl;Realizado!</strong> <s:actionmessage/>
                </div>
            </s:if>
        </div>
    </div>
    <div class="row">
        <div class="col-xs-12">
            <div class="main-box" >
                <div class="clearfix" >&nbsp;</div>
                <div class="main-box-body clearfix">
                    "La solicitud de tu cuenta será procesada en breve. Te sugerimos acudir a una sucursal de banco para realizar 
                    la cancelación de la cuenta de tu padre o tutor y así no generar comisiones por manejo de cuenta.

                    Ahora es indispensable la actualización de tus datos bancarios en el módulo de 
                    <a href="/misdatos/datosRegistro.action"> Datos Personales </a>
                    , ya que sin estos datos, la generación de tu cuenta no puede ser procesada"
                </div>
                <div class="clearfix" >&nbsp;</div>
                <div class="clearfix" >&nbsp;</div>
            </div>
        </div>
    </div>
</body>
