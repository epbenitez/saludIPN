<%-- 
    Created on : 28-Septiembre-2016
    Author     : Gustavo Adolfo Alamillo Medina
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Estatus de solicitud de beca</title>
</head>

<content tag="tituloJSP">
    Estatus de solicitud de beca
</content>

<body>
    <security:authorize ifAnyGranted="ROLE_ALUMNO">
        <div class="alert alert-info fade in">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
            <i class="fa fa-info-circle fa-fw fa-lg"></i>
            <strong>¿Necesitas ayuda?</strong> Da click <a href="/misdatos/consultar.action#encabezado4">aquí</a> para consultar el manual de usuario.
        </div> 
    </security:authorize>          
    <s:if test="!datosActual">
        <div class="alert alert-info">
            <i class="fa fa-info-circle fa-fw fa-lg"></i>
            <strong>Toma en cuenta</strong> El alumno no cuenta con datos académicos actualizados.
        </div>        
    </s:if>
    <s:if test="!conSolicitud">
        <div class="alert alert-info">
            <i class="fa fa-info-circle fa-fw fa-lg"></i>
            <strong>Toma en cuenta</strong> El alumno no ha realizado una solicitud de beca para este periodo.
        </div>        
    </s:if>
    
    <s:if test="solicitudAnteriorVigente">
        <div class="alert alert-info">
            <i class="fa fa-info-circle fa-fw fa-lg"></i>
            <strong>Toma en cuenta</strong> La solicitud mostrada corresponde al periodo pasado, sin embargo, aún es vigente.
        </div>        
    </s:if>
    <div class="row">
        <div class="col-xs-12">
            <div class="main-box clearfix" >
                <div class="main-box-body ">
                    <h2>Datos académicos</h2>
                    <hr/>
                    <form id="passwordForm" class="form-horizontal">
                        <div class="form-group">
                            <label class="control-label col-sm-2">
                                Boleta
                            </label>
                            <div class="col-sm-10">
                                <s:textfield name="alumno.boleta" cssClass="form-control" readonly="true"/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class=" control-label col-sm-2">
                                Nombre
                            </label>
                            <div class="col-sm-4">
                                <s:textfield name="alumno.nombre" cssClass="form-control" readonly="true"/>
                            </div>
                            <div class="col-sm-3">
                                <s:textfield name="alumno.apellidoPaterno" cssClass="form-control" readonly="true"/>
                            </div>
                            <div class="col-sm-3">
                                <s:textfield name="alumno.apellidoMaterno" cssClass="form-control" readonly="true"/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class=" control-label col-sm-2">
                                Unidad Académica
                            </label>
                            <div class="col-sm-10">
                                <s:textfield name="alumno.datosAcademicos.unidadAcademica.nombreCorto" cssClass="form-control" readonly="true"/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class=" control-label col-sm-2">
                                Carrera
                            </label>
                            <div class="col-sm-10">
                                <s:textfield name="alumno.datosAcademicos.carrera.carrera" cssClass="form-control" readonly="true"/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class=" control-label col-sm-2">
                                Periodo
                            </label>
                            <div class="col-sm-10">
                                <s:textfield name="periodo.clave" cssClass="form-control" readonly="true"/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="control-label col-sm-2">
                                Beca semestre anterior
                            </label>
                            <div class="col-sm-10">
                                <s:textfield name="becaAnterior" cssClass="form-control" readonly="true"/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="control-label col-sm-2">
                                Semestre
                            </label>
                            <div class="col-sm-10">
                                <s:if test="asignado"> 
                                    <s:textfield name="otorgamiento.datosAcademicos.semestre" cssClass="form-control" readonly="true"/>
                                </s:if>
                                <s:else>
                                    <s:textfield name="alumno.datosAcademicos.semestre" cssClass="form-control" readonly="true"/>
                                </s:else>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="control-label col-sm-2">
                                Promedio
                            </label>
                            <div class="col-sm-10">
                                <s:if test="asignado">
                                    <s:textfield name="otorgamiento.datosAcademicos.promedio" cssClass="form-control" readonly="true"/>
                                </s:if>
                                <s:else>
                                    <s:textfield name="alumno.datosAcademicos.promedio" cssClass="form-control" readonly="true"/>
                                </s:else>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <s:if test="conSolicitud">
        <div class="row" id="panel">
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="main-box clearfix">
                    <div class="tabs-wrapper profile-tabs">
                        <ul class="nav nav-tabs">
                            <s:iterator value="stsList" status="incr">
                                <s:if test="%{#incr.index == 0}">
                                    <li class="active">
                                    </s:if>
                                    <s:else>
                                    <li>    
                                    </s:else>
                                    <a href="#tab-<s:property value="clave"/>" data-toggle="tab"><s:property value="nombre"/></a>
                                </li>
                            </s:iterator>
                        </ul>                    
                        <div class="tab-content" style="padding:0px;">
                            <s:iterator value="stsList" status="incr">                                
                                <s:if test="%{#incr.index == 0}">
                                <div class="tab-pane fade active in" id="tab-<s:property value="clave"/>">
                                </s:if>
                                <s:else>
                                <div class="tab-pane fade" id="tab-<s:property value="clave"/>">
                                </s:else>
                                    <security:authorize ifNotGranted="ROLE_ALUMNO">
                                    <div class="col-xs-12">
                                        <!--<i class="fa fa-info-circle fa-fw fa-lg"></i>-->
                                        <s:if test="%{estatuId == -1}"><span class="label label-info"></s:if>
                                        <s:elseif test="%{estatuId == 1}"><span class="label label-success"></s:elseif>
                                        <s:elseif test="%{estatuId == 2}"><span class="label label-danger"></s:elseif>
                                        <s:elseif test="%{estatuId == 3}"><span class="label label-warning"></s:elseif>
                                            Solicitud <s:property value="estatus"/> 
                                            <s:if test="%{estatuId == 2}"> : <s:property value="motivo"/></s:if>
                                        </span>
                                    </div>      
                                    </security:authorize>
                                    <security:authorize ifAnyGranted="ROLE_ALUMNO">
                                        <s:if test="mostrarDatosBeca">
                                            <div class="col-xs-12">
                                                <!--<i class="fa fa-info-circle fa-fw fa-lg"></i>-->
                                                <s:if test="%{estatuId == -1}"><span class="label label-info"> Solicitud <s:property value="estatus"/> <s:property value="beca"/></s:if>
                                                <s:elseif test="%{estatuId == 1 && !otorgamientoDefinitivo}"><span class="label label-info"> Solicitud Pendiente asignada </s:elseif>    
                                                <s:elseif test="%{estatuId == 1 && otorgamientoDefinitivo}"><span class="label label-success"> Solicitud <s:property value="estatus"/> <s:property value="beca"/></s:elseif>
                                                <s:elseif test="%{estatuId == 2}"><span class="label label-danger"> Solicitud <s:property value="estatus"/> <s:property value="beca"/></s:elseif>
                                                <s:elseif test="%{estatuId == 3}"><span class="label label-warning"> Solicitud <s:property value="estatus"/> <s:property value="beca"/></s:elseif>
                                                    <s:if test="%{estatuId == 2}"> : <s:property value="motivo"/></s:if>
                                                </span>
                                            </div>      
                                        </s:if>                                    
                                    </security:authorize>
                                    <div id="conte-<s:property value="clave"/>">
                                        <table id="table-<s:property value="clave"/>" class="table table-striped table-hover dt-responsive" style="width: 100%">
                                            <thead>
                                                <tr>
                                                    <th data-center="true">Beca</th>
                                                    <th data-center="true">Ingresos máximos</th>
                                                    <th data-center="true">Gasto Transporte</th>
                                                    <th data-center="true">Vulnerabilidad</th>
                                                    <th data-center="true">Regular</th>
                                                    <th data-center="true">Inscripción</th>
                                                    <th data-center="true">Promedio</th>
                                                    <th data-center="true">Semestre</th>
                                                    <th data-center="true">Carga</th>
                                                </tr>
                                            </thead>
                                        </table>   
                                    </div>
                                </div>
                                </s:iterator>
                            </div>                    
                        </div>
                    </div>
                </div>
            </div>
        </s:if>    
</body>

<content tag="endScripts">
    <s:if test="conSolicitud">            
        <s:iterator value="stsList" status="incr">
            <script>
            var <s:property value="clave"/> = new Array();
                <s:iterator value="becaRequisitos" status="incre">
            var arrg = new Array();
                    <s:iterator status="increm">
                        <s:if test="cadena">
            arrg.push("<s:property value='texto'/>");
                        </s:if>
                        <s:else>
                            <s:if test="cumple">
            arrg.push("<span data-toggle='tooltip' data-placement='auto' style='color:green' class='fa-stack' title='<s:property value='texto'/>'><i class='fa fa-check-square fa-stack-2x'></i></span>");
                            </s:if>
                            <s:else>
            arrg.push("<span data-toggle='tooltip' data-placement='auto' style='color:red' class='fa-stack' title='<s:property value='texto'/>'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-times  fa-stack-1x fa-inverse'></i></span>");
                            </s:else>
                        </s:else>
                    </s:iterator>
                    <s:property value="clave"/>.push(arrg);
                </s:iterator>
            </script>
            <script type="text/javascript" 
                    src="/resources/js/generador-tablas.min.js" 
                    data-init="true" 
                    data-div="conte-<s:property value="clave"/>" 
                    data-ajax="<s:property value="clave"/>"
                    data-function="despuesCargarTabla"
                    >
            </script>
        </s:iterator>
    </s:if>
</content>

<content tag="inlineScripts">
    <script>
        function despuesCargarTabla() {
            $('[data-toggle="tooltip"]').tooltip();
        }
    </script>
</content>