<%-- 
    Document   : Validación inscripción
    Created on : 28-Septiembre-2016
    Author     : Rafael Cárdenas
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Resumen Preasignación</title>
</head> 

<content tag="tituloJSP">
    Resumen Preasignación
</content>

<body>
    <!-- Ejemplo de caja blanca -->
    <div class="row">
        <div class="col-xs-12">
            <div class="main-box">
                <div class="main-box-body clearfix">
                    <div class="alert alert-info">
                        <i class="fa fa-info-circle fa-fw fa-lg"></i>
                        <strong>Total</strong> Se intento preasignar un total de <s:property value="total" /> alumnos.
                    </div>
                     <s:if test="buenos>0">
                    <div class="alert alert-success">
                        <i class="fa fa-check-circle fa-fw fa-lg"></i>
                        <strong>&iexcl;Éxito!</strong> Se preasignó correctamente a <s:property value="buenos" /> alumnos.
                    </div>
                     </s:if>
                    <s:if test="sinCambios>0">
                        <div class="alert alert-success">
                            <i class="fa fa-check-circle fa-fw fa-lg"></i>
                            <strong></strong> No hubo cambios para <s:property value="sinCambios" /> alumnos.
                        </div>
                    </s:if>
                    <s:if test="malos>0">
                    <div class="alert alert-warning">
                        <i class="fa fa-warning fa-fw fa-lg"></i>
                        <strong>&iexcl;Atención!</strong> Se encontraron problemas en la preasignación de <s:property value="malos" /> alumnos.
                    </div>
                    </s:if>
                </div>   
            </div> 
        </div> 
    </div>
</body>

<content tag="endScripts">

</content>

<content tag="inlineScripts">    
</content>