<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<head>
    <title>Carga de Ficha Escolar</title>
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
    Carga de Ficha Escolar
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
                    <strong>&iexcl;Realizado!</strong> <s:actionmessage />
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
                                Descargue el archivo de ejemplo e ingrese los datos de los alumnos a quienes desea actualizar su informaci&oacute;n académica.
                            </li>

                            <li>
                                Si los datos del alumno ya existe en el SIBec, los datos acad&eacute;micos se sobre escribir&aacute;n. Si no existen, se inserta en el sistema.
                            </li>

                            <li>
                                Una vez cargado el Excel, el sistema le mostrar&aacute; un resumen por boleta de los alumnos actualizados y los insertados.
                            </li>
                        </ul>
                        <div class="col-xs-12 text-center" style="padding-bottom: 5px" >
                            <a href="/resources/downloadable/Ejemplo_ficha_escolar.xls" class="btn btn-primary btn-lg">
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
                    <s:form id="cargaForm" action="cargaFichaEscolar.action" method="POST" namespace="/admin" enctype="multipart/form-data" cssClass="form-horizontal">
                        <div class="form-group">
                            <label for="archivo" class="control-label col-sm-3">Archivo excel</label>
                            <div class="col-sm-7">
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

    <s:if test="%{carga}">
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
                                        <th data-orderable="true">Boleta</th>
                                        <th data-orderable="true">Resultado</th>
                                        <th data-orderable="true">Estatus</th>
                                        <th data-orderable="true">ESE</th>
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
</body>

<content tag="endScripts">
    <s:if test="%{carga}">
	<script>
	    var dataSets = new Array();
	    <s:iterator value="alumnosLst" status="ls">
	    dataSets.push(["<s:property value="nombre"/>" + " " + "<s:property value="apellido_pat"/>" + " " + "<s:property value="apellido_mat"/>", "<s:property value="boleta"/>", 
	    <s:if test="%{resultadoCarga}">"ACTUALIZADO"</s:if><s:else>"NUEVO"</s:else>, 
	    <s:if test="%{registradoSistema}">"REGISTRADO EN SIBEC"</s:if><s:else>"NO EXISTE EN SIBEC"</s:else>, 
	    <s:if test="%{llenoESE}">"CONTESTO ESE"</s:if><s:else>"SIN ESE"</s:else>]);
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
			"cuerpo": "Estamos los datos de los alumnos, favor de no cerrar la ventana ni presionar 'atrás' en el navegador.",
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