<%-- 
    Document   : Detalle Solicitud Cuenta
    Created on : 07-Abril-2016
    Author     : Rafael Cárdenas Reséndiz
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Estatus de la Solicitud de Cuentas</title>
</head> 

<content tag="tituloJSP">
    Estatus de la Solicitud de Cuentas
</content>

<body>
    <div class="row">
        <div class="col-xs-12">
            <div class="main-box clearfix" >
                <div class="main-box-body clearfix">
                    <h3>Detalles de la cancelación.</h3>
                    <ul>
                        <li>
                            Usuario cancelación: <s:property value="usuarioCancelacion"/>
                        </li>
                        <li>
                            Fecha cancelación: <s:property value="fechaCancelacion"/>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
