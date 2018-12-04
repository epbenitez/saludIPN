<%-- 
    Document   : ver
    Created on : 9/05/2016, 12:48:48 PM
    Author     : Victor Lozano
    Redisign   : Gustavo Adolfo Alamillo Medina
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Becas disponibles</title>
</head> 

<content tag="tituloJSP">
    Becas disponibles
</content>

<body>
    <security:authorize ifAnyGranted="ROLE_ALUMNO">
        <div class="alert alert-info fade in">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
            <i class="fa fa-info-circle fa-fw fa-lg"></i>
            <strong>¿Necesitas ayuda?</strong> Da click <a href="/misdatos/consultar.action#encabezado3">aquí</a> para consultar el manual de usuario.
        </div>
    </security:authorize>
    
    <s:if test="puedeManutencion">
        <div class="alert alert-info">
            <i class="fa fa-info-circle fa-fw fa-lg"></i>
            <strong>Toma en cuenta</strong> Para las becas de Manutención debes completar tu registro en el sistema SUBES.
        </div>
    </s:if>
        
    <s:if test="!periodoActivo && esAlumno">
        <div class="preasignacion alert alert-warning">
            <i class="fa fa-warning fa-fw fa-lg"></i>
            No hay informacion en este modulo hasta la apertura de un nuevo registro.
        </div>        
    </s:if>        
    <s:else>        
        <div class="row">
            <div class="col-xs-12">
                <div class="main-box clearfix" >
                    <div class="main-box-body clearfix">                        
                        <s:if test="error == 0">
                            <div class="preasignacion">
                                <security:authorize ifAnyGranted="ROLE_ALUMNO">
                                    <p style=" text-align: justify;text-justify: distribute">De acuerdo a tu historial académico te hemos postulado para las siguientes becas (las becas
                                        mostradas están sujetas al presupuesto disponible y la asignación la realiza el Subcomité de Becas
                                        de tu Unidad Académica. Asimismo, no podrás contar con más de una beca a menos que sean compatibles )
                                    </p>
                                </security:authorize>
                                <security:authorize ifNotGranted="ROLE_ALUMNO">
                                    <p style=" text-align: justify;text-justify: distribute">De acuerdo al historial académico del alumno le hemos postulado para las siguientes becas (las becas
                                        mostradas están sujetas al presupuesto disponible y la asignación la realiza el Subcomité de Becas
                                        de la Unidad Académica del alumno. Asimismo, no podrá contar con más de una beca a menos que sean compatibles )
                                    </p>
                                </security:authorize>
                            </div>
                            
                            <s:text name="becas" />
                        </s:if>
                        <s:else>
                            <div class="preasignacion">
                                <div class="alert alert-block alert-warning">
                                    <h4>Atención</h4>
                                    <s:text name="becas" />
                                </div>                                
                            </div>
                        </s:else>                        
                    </div>
                </div>
            </div>
        </div>
    </s:else>
</body>