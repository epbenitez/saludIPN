<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<head>
    <title>Padr&oacute;n Prospera</title>
    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css"/>
    <link rel="stylesheet" type="text/css" href="/vendors/fancy-box/jquery.fancybox.min.css"/>
    <link rel="stylesheet" type="text/css" href="/vendors/kartik-bootstrap-input/fileinput.min.css"/>
    <style>
        .extra-space {
            margin-bottom: 10px;
        }
    </style>
</head> 

<content tag="tituloJSP">
    Padrón Prospera
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
                    <strong>&iexcl;Realizado!</strong> <s:actionmessage /> <s:property value="successMsj"/>
                </div>
            </s:if>
            <s:if test="!hasActionErrors() && !hasActionMessages() && carga">
                <div class="alert alert-info">
                    <i class="fa fa-info-circle fa-fw fa-lg"></i>
                    <strong>Toma en cuenta</strong> <s:property value="successMsj"/>
                </div>
            </s:if>
        </div>
    </div>
    <div class="row">
        <div class="col-xs-12">
            <div class="panel-group accordion" id="accordion">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
                                Instrucciones
                            </a>
                        </h4>
                    </div>
                </div>
                <div id="collapseOne" class="panel-collapse collapse">
                    <div class="panel-body">
                        <ul>
                            <li>
                                Para realizar la carga de folios PROSPERA deberá ingresar un archivo excel
                                con el formato especificado en el archivo de ejemplo.
                            </li>

                            <li>
                                Los datos necesarios para garantizar la carga correcta del archivo son el CURP y el nombre.
                            </li>

                            <li>
                                Una vez que esté listo el archivo, selecciónelo y al dar clic en el 
                                bot&oacute;n de "Cargar archivo", el sistema buscar&aacute; a los 
                                alumnos por medio de su CURP o nombre dentro del archivo.

                            </li>

                            <li>
                                El sistema mostrar&aacute; un listado de los alumnos que no ha podido encontrar.
                            </li>
                        </ul>
                        <div class="col-xs-12 text-center" style="padding-bottom: 5px" >
                            <a href="/resources/downloadable/Ejemplo_prospera.xlsx" class="btn btn-primary btn-lg">
                                <i class='fa fa-download'></i> Archivo de ejemplo
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div> 
    <div class="row">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <div class="main-box-body">
                    <s:form id="cargaForm" action="cargaProspera.action" method="POST" namespace="/admin" enctype="multipart/form-data" cssClass="form-horizontal">
                        <div class="form-group">
                            <label for="archivo" class="control-label col-sm-3">Archivo excel PROSPERA</label>
                            <div class="col-sm-7" style="padding-bottom: 5px;">
                                <s:file id="archivo" cssClass="file form-control" 
                                        labelposition="left" 
                                        accept="application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" 
                                        name="upload" 
                                        data-bv-notempty="true" 
                                        data-bv-notempty-message="Es necesario que selecciones un archivo."
                                        data-show-preview="false" />
                            </div>
                            <div class="col-sm-2">
                                <input type="submit" id="cargar" value="Cargar archivo" class="btn btn-primary"/>
                            </div>
                        </div>
                    </s:form>
                </div>
            </div>
        </div>
    </div> 
    <s:if test="!hasActionMessages()">
	<s:if test="%{carga}">
	    <div class="row">
		<div class="col-xs-12">
		    <div class="alert alert-warning">
			<i class="fa fa-warning fa-fw fa-lg"></i>
			<strong>&iexcl;Atención!</strong> Los alumnos que se muestran en este listado no han sido encontrados en el sistema.
		    </div>
		</div>
	    </div>
	    <!-- Tabla -->
	    <div class="row" id="contenedor">
		<div class="col-xs-12">
		    <div class="main-box clearfix">
			<div class="col-md-12" style="padding: 0px">
			    <div class="responsive">                        
				<table id="adentrocontendedor" class="table table-striped table-hover dt-responsive" style="width: 100%">
				    <thead>
					<tr>
					    <th data-orderable="true">Nombre</th>
					    <th data-orderable="true">CURP</th>
					    <th data-orderable="true">Folio</th>
					</tr>
				    </thead>
				</table>                        
			    </div>
			</div>
		    </div>
		</div>                
	    </div>
	    <!--Termina tabla -->  
	</s:if>           
    </s:if>           
</body>

<content tag="endScripts">
    <s:if test="%{carga}">
        <script>
	    var dataSets = new Array();
            <s:iterator value="alumnosLst" status="ls">
	    dataSets.push(["<s:property value="nombres"/>" + " <s:property value="apellidoPaterno"/>" + " <s:property value="apellidoMaterno"/>", "<s:property value="curp"/>", "<s:property value="folio"/>"]);
            </s:iterator>
        </script>
        <script type="text/javascript" src="/resources/js/generador-tablas.min.js" data-init="true" data-ajax="dataSets" data-div="contenedor"></script>
    </s:if>
    <script type="text/javascript" src="/vendors/kartik-bootstrap-input/fileinput.min.js"></script>
    <script type="text/javascript" src="/vendors/kartik-bootstrap-input/locales/es.js"></script>
    <script type="text/javascript" src="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.js"></script>
    <script type="text/javascript" src="/vendors/fancy-box/jquery.fancybox.min.js"></script>    
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>
	    $(document).ready(function () {
		$("#cargaForm").bootstrapValidator();
		$("#cargaForm").bootstrapValidator().on('success.form.bv', function (e) {
		    ModalGenerator.notificacion({
			"titulo": "Cargando información",
			"cuerpo": "Estamos procesando los datos de los alumnos, favor de no cerrar la ventana ni presionar 'atrás' en el navegador.",
			"tipo": "WARNING",
			"sePuedeCerrar": false
		    });
		});

		$(".fancybox").fancybox({
		    autoSize: true,
		    afterClose: function () {
			$("#alumnos").val(",");
			reload();
		    }
		});

		$('#buscar').click(function (e) {
		    lista();
		    $('#buscar').blur();
		});
	    });

	    $("#archivo").fileinput({
		showUpload: false,
		language: "es",
		allowedFileExtensions: ['xls', 'xlsx']
	    });
    </script>
</content>