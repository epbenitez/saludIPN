<%-- 
    Document   : Carga de asignaciones de cuentas bancarias
    Created on : 7/04/2017, 01:12:13 PM
    Author     : Rafael Cárdenas Reséndiz
--%>

<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Cuentas bancarias.</title>
    <link rel="stylesheet" type="text/css" href="/vendors/kartik-bootstrap-input/fileinput.min.css"/>
    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css"/>
</head> 

<content tag="tituloJSP">
    Cuentas bancarias.
</content>

<body>

    <div class="col-sm-12">
        <s:if test="hasActionErrors()">
            <div class="alert alert-danger">
                <i class="fa fa-times-circle fa-fw fa-lg"></i>
                <strong>&iexcl;Error!</strong> <s:actionerror/>
            </div>
        </s:if>
        <s:if test="hasActionMessages()">
            <div class="alert alert-success">
                <i class="fa fa-check-circle fa-fw fa-lg"></i>
                <strong>&iexcl;Realizado!</strong> <s:actionmessage />
            </div>
        </s:if>
    </div>

    <!-- Ejemplo para formularios -->
    <div class="row">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <header class="main-box-header clearfix">
                    <s:if test="tipoCarga==0">
                        <h2 class="pull-left">Carga de cuentas liberadas <s:property  value="nombreOrden" /></h2>
                    </s:if>
                    <s:elseif test="tipoCarga==1">
                        <h2 class="pull-left">Carga de cuentas rechazadas <s:property  value="nombreOrden" /></h2>
                    </s:elseif>
					<s:else>
                        <h2 class="pull-left">Carga de cuentas <s:property  value="nombreOrden" /></h2>
					</s:else>
                </header>
                <div class="main-box-body">
                    <s:form id="cargaForm" action="cargaSolicitudesCuentasRespuestaBancaria.action"
                            method="POST" enctype="multipart/form-data" 
                            cssClass="form-horizontal">
						<s:if test="tipoCarga==null">
							<div class="form-group col-sm-12">
								<label class="col-sm-2 control-label">Tipo de carga</label>
								<div class="col-sm-8">
									<select id="tipoCarga" name="tipoCarga" class="form-control">
										<option value="0">Cuentas liberadas</option>
										<option value="1">Cuentas rechazadas</option>
									</select>
								</div>
							</div>
						</s:if>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Asignacion de cuentas bancarias</label>
                            <div class="col-sm-8">
                                <s:file 
                                    id="archivo"
                                    class="file form-control" 
                                    name="upload" 
                                    accept=".xlsx"
                                    data-bv-notempty="true"
                                    data-bv-notempty-message="Debes seleccionar un archivo"
                                    />
                            </div>
                            <div class="col-sm-2">
                                <div class="pull-right">
									<s:if test="tipoCarga!=null">
										<s:hidden name="ordenId"/>
										<s:hidden name="tipoCarga" />
									</s:if>
                                    <input type="submit" id="buscar" class="btn btn-primary" value="Cargar archivo"/>
                                </div>
                            </div>
                        </div>                        
                    </s:form>
                </div>
            </div>
        </div>
    </div>
    <!--Termina ejemplo para formularios -->


</body>

<content tag="endScripts">
    <script type="text/javascript" src="/vendors/kartik-bootstrap-input/fileinput.min.js"></script>
    <script type="text/javascript" src="/vendors/kartik-bootstrap-input/locales/es.js"></script>
    <script type="text/javascript" src="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>
		$("#cargaForm").bootstrapValidator();

		$("#archivo").fileinput({
			showUpload: false,
			showPreview: false,
			allowedFileExtensions: ['xlsx'],
			language: "es"
		});
    </script>
</content>