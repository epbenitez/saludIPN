<%-- 
    Document   : Bitácora
    Created on : 28-Septiembre-2016
    Author     : Gustavo Adolfo Alamillo Medina
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Monitoreo de cuentas</title>
</head> 

<content tag="tituloJSP">
    Monitoreo de cuentas
</content>

<body>
    <s:if test="hasActionErrors()">
        <div class="alert alert-danger">
            <i class="fa fa-times-circle fa-fw fa-sm"></i>
            <strong>&iexcl;Error!</strong> <s:actionerror/>
        </div>
    </s:if>
    <s:else>
        <div class="row">
            <div class="col-xs-12">
                <div class="main-box clearfix">
                    <div class="main-box-body">
                        <form class="form-horizontal">
                            <div class="form-group">
                                <label class="col-sm-2 control-label">
                                    Boleta
                                </label>
                                <div class="col-sm-10">
                                    <s:textfield name="alumno.boleta" cssClass="form-control" readonly="true"/>
                                </div>
                            </div>

                            <div class="form-group ">
                                <label class="col-sm-2 control-label">
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
                                <label class="col-sm-2 control-label">
                                    Unidad Académica
                                </label>
                                <div class="col-sm-10">
                                    <s:textfield name="alumno.datosAcademicos.unidadAcademica.nombreCorto" cssClass="form-control" readonly="true"/>
                                </div>
                            </div>

<!--                            <div class="form-group">
                                <label class="col-sm-2 control-label">
                                    Cuenta/tarjeta activa
                                </label>
                                <div class="col-sm-10">
                                    <s:textfield name="numero_tarjeta" cssClass="form-control" readonly="true"/>
                                </div>
                            </div>     -->
                        </form>
                        <div id="error" >
                            <s:property  value="alumno.error"  />
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row" id="tablarow" style="display: none;">
            <div class="col-xs-12">
                <div class="main-box clearfix">
                    <div class="col-md-12" style="padding: 0px">
                        <div class="responsive">                        
                            <table id="tabla" class="table table-striped table-hover dt-responsive" style="width: 100%">
                                <thead>
                                    <tr>
                                        <th>Tipo</th>
                                        <th>N&uacute;mero</th>
                                        <th data-orderable="true">Estado</th>
                                        <th data-orderable="true" >Fecha</th>
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
    </s:else>
</body>

<content tag="endScripts">
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js"
    data-init="true" data-div="tablarow" data-ajax="/ajax/listadoBitacoraAlumnoTarjetaAjax.action?numeroDeBoleta=<s:property value="alumno.boleta"/>"></script>
</content>