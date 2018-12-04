<%-- 
    Document   : Carga de respuestas bancarias
    Created on : 10-Septiembre-2016
    Author     : Gustavo Adolfo Alamillo Medina
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Carga de respuestas bancarias</title>
    <link rel="stylesheet" type="text/css" href="/vendors/kartik-bootstrap-input/fileinput.min.css"/>
    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css"/>
</head> 

<content tag="tituloJSP">
    Carga de respuestas bancarias
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
					<s:if test="!referencia">
						<h2 class="pull-left"><s:property  value="nombreOrden" /></h2>
					</s:if>
					<s:else>
						<h2 class="pull-left">Referencia Bancaria</h2>
					</s:else>                </header>
                <div class="main-box-body">
                    <s:form id="cargaForm" action="cargaRespuestaBancaria.action"
                            method="POST" enctype="multipart/form-data" 
                            cssClass="form-horizontal">
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Respuesta bancaria</label>
                            <div class="col-sm-8">
                                <s:file 
                                    id="archivo"
                                    class="file form-control" 
                                    name="upload" 
                                    accept=".tef,.txt"
                                    data-bv-notempty="true"
                                    data-bv-notempty-message="Debes seleccionar un archivo"
                                    />

                            </div>
                            <div class="col-sm-2">
                                <div class="pull-right">
                                    <s:hidden name="ordenId"/>
									<s:hidden name="referencia"/>
									<s:hidden name="periodo"/>
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
			allowedFileExtensions: ['tef', 'txt'],
			language: "es"
		});
    </script>
</content>