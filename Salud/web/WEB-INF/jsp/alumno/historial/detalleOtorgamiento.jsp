<%-- 
    Document   : Template SIBEC
    Created on : 28-Septiembre-2016
    Author     : Gustavo Adolfo Alamillo Medina
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Detalle de movimientos del otorgamiento</title>
</head> 

<content tag="tituloJSP">
	Detalle de movimientos del otorgamiento
</content>

<body>
    <s:if test="hasActionErrors()">
        <div class="alert alert-danger">
            <i class="fa fa-times-circle fa-fw fa-sm"></i>
            <strong>&iexcl;Error!</strong> <s:actionerror/>
        </div>
    </s:if>
    <s:else>
        <!-- Ejemplo para formularios -->
        <div class="row">
            <div class="col-xs-12">
                <div class="main-box clearfix">
                    <div class="main-box-body">
                        <form id="busquedaForm" class="form-horizontal"
                              data-bv-feedbackicons-valid="glyphicon glyphicon-ok"
                              data-bv-feedbackicons-invalid="glyphicon glyphicon-remove"
                              data-bv-feedbackicons-validating="glyphicon glyphicon-refresh">
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Boleta</label>
                                <div class="col-sm-10">
                                    <s:textfield name="alumno.boleta" 
                                                 cssClass="form-control" 
                                                 readonly="true"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Nombre</label>
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
                                <label class="col-sm-2 control-label">Unidad Académica</label>
                                <div class="col-sm-10">
                                    <s:textfield name="alumno.datosAcademicos.unidadAcademica.nombreCorto" cssClass="form-control" readonly="true"/>
                                </div>
                            </div> 
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <!--Termina ejemplo para formularios -->

        <!-- Ejemplo tabla -->
        <div class="row" id="tablaHisto" style="display: none;">
            <div class="col-xs-12">
                <div class="main-box clearfix">
                    <div class="col-md-12" style="padding: 0px">
                        <div class="responsive">                        
                            <table id="listadoTabla" class="table table-striped table-hover dt-responsive" style="width: 100%">
                                <thead>
                                    <tr>
										<th >Movimiento</th>
                                                                                <th >Periodo</th>
										<th >Tipo de Movimiento</th>
										<th >Beca</th>
										<th >Unidad Académica</th>
										<th >Proceso</th>
										<th >Fecha</th>
                                    </tr>
                                </thead>
                            </table>                        
                        </div>
                    </div>
                </div>
            </div>                
        </div>
        <!--Termina ejemplo tabla --> 
        
        <security:authorize ifAnyGranted="ROLE_JEFEBECAS">
                    <div class="clearfix" >&nbsp;</div>
                        <div class="form-group">
                            <div class="col-sm-12 col-md-12 col-lg-12">
                                <div class="pull-right">
                                    <a class="btn btn-primary fancybox fancybox.iframe table-link"  href="javascript:history.back()"><i class="fa fa-arrow-circle-left"></i></span> REGRESAR</a>
                                    <!--<a href="javascript:history.back()" class="btn btn-primary" role="button">Regresar</a>-->
                                </div>
                            </div>
                    </div>
        </security:authorize>
    </s:else>
</body>

<content tag="endScripts">
    <script type="text/javascript" src="/resources/js/generador-tablas.js"  data-function="despuesCargarTabla"
    data-init="true" data-div="tablaHisto" data-ajax="/ajax/histoBecasDisponiblesAjax.action?otorgamientoId=<s:property value="otorgamiento.id"/>" ></script>
</content>
<content tag="inlineScripts">
    <script>
		function despuesCargarTabla() {
			$('[data-toggle="tooltip"]').tooltip();
		}

		var enviarBoton = [{
				text: '<a title="Historial Becas" class="btn btn-primary fancybox fancybox.iframe table-link"  href="/misdatos/detalleHistorialBecas.action?numeroDeBoleta=<s:property value="alumno.boleta"/>"><i class="fa fa-arrow-circle-left"></i></span> REGRESAR</a>'			
                            }];
                            
                //data-button='enviarBoton'
    </script>
</content>