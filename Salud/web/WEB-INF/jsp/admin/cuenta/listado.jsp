<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <link rel="stylesheet" type="text/css" href="/vendors/fancy-box/jquery.fancybox.min.css"/>
    <style>
        textarea { 
            resize: vertical;
        }
    </style>

    <title>Solicitud de Cuentas (Banamex)</title>
</head> 

<content tag="tituloJSP">
    Solicitud de Cuentas (Banamex)
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
        <div class="col-sm-7">
            <div class="main-box clearfix" >
                <header class="main-box-header clearfix">
                    <h2 class="pull-left">Generación de solicitud de cuentas</h2>
                </header>
                <div class="main-box-body clearfix">
                    <form class="form-horizontal">
                        <div class="form-group col-sm-12">
                            <label class="col-sm-2 control-label">Tipo</label>
                            <div class="col-sm-10">
                                <select id="tipoAsignacion" class="form-control">
                                    <option value="1">Nuevos</option>
				    <option value="2">Cancelaciones por mayoría de edad</option>
				    <option value="3">Rechazos</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group col-sm-12">
                            <label class="col-sm-2 control-label">Periodo</label>
                            <div class="col-sm-10">
                                <s:select id="periodo"
                                          cssClass="form-control"
                                          name="periodo" 
                                          list="ambiente.periodoList" 
                                          listKey="id" 
                                          listValue="clave" />
                            </div>
                        </div>
                        <div class="form-group col-sm-12">
                            <label class="col-sm-2 control-label">Boleta</label>
                            <div class="col-sm-10">
                                <input type="text" 
                                       class="form-control"
                                       id="boleta"
                                       name = "boleta"
                                       placeholder="Boleta"
                                       pattern="^[\.\s1234567890]+$" 
                                       data-bv-stringlength="true" 
                                       data-bv-stringlength-max="10"/>
                            </div>
                        </div>
                        <div class="form-group col-sm-12">
                            <label class="col-sm-2 control-label">Nivel</label>
                            <div class="col-sm-10">
                                <select id="nivel" class="form-control" onchange="getUnidades()">
                                    <option value="0">Todos</option>
                                    <option value="1">Medio superior</option>
                                    <option value="2">Superior</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group col-sm-12">
                            <label class="col-sm-2 control-label">Unidad Académica</label>
                            <div class="col-sm-10">
                                <select name="unidadAcademica"
                                        id="unidadAcademica"
                                        class="form-control">
                                    <option value="0" data-clave="0" >Todas</option>
                                    <s:iterator value="unidadAcademicaList" status="ua">
                                        <option value="<s:property value="id"/>" data-clave="<s:property value="clave"/>"><s:property value="nombreCorto"/></option>
                                    </s:iterator>
                                </select>
                            </div>                            
                        </div>
                        <div class="col-xs-12 form-group">
                            <div class="col-xs-offset-2 col-xs-10">                                
                                <button id="btn-busqueda-principal" type="button" class="btn btn-primary pull-right">Buscar</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>

	<div class="col-sm-5">
	    <div class="main-box clearfix" >
		<header class="main-box-header clearfix">
		    <h2 class="pull-left">Búsqueda</h2>
		</header>
		<div class="main-box-body clearfix">
		    <form class="form-horizontal">
			<div class="form-group col-sm-12 ">
			    <label class="col-sm-3 control-label">Identificador de la carga</label>
			    <div class="col-sm-9">
				<input id="identificadorBusqueda" type="text" class="form-control"/>
			    </div>
			</div>
			<div class="col-xs-12 form-group">
			    <div class="col-xs-offset-2 col-xs-10">
                                <button id="btn-busqueda-identificador" type="button" class="btn btn-primary pull-right">Buscar</button>
			    </div>
			</div>
		    </form>
		</div>
	    </div>
	</div>
    </div>

    <div class="row" id="div-asignar" style="display: none;">
        <div class="col-sm-12">
            <div class="main-box clearfix" >
                <header class="main-box-header clearfix">
                    <h2 class="pull-left">Generar solicitud</h2>
                </header>
                <div class="main-box-body clearfix">
                    <form class="form-horizontal" method="post" action="" id="formAsignar">
                        <div class="form-group col-sm-12 ">
                            <label class="col-sm-2 control-label">Cuentas a solicitar</label>
                            <div class="col-sm-10">
                                <s:textfield id="tarjetasDisponibles"
                                             cssClass="form-control"
                                             name="tarjetasDisponibles" 
                                             readonly="true"
                                             disabled="true"
                                             />
                            </div>
                        </div>
                        <div class="form-group col-sm-12">
                            <label class="col-sm-2 control-label">Identificador de la carga</label>
                            <div class="col-sm-10">
                                <s:textfield id="identificador"
                                             cssClass="form-control"
                                             name="identificador" 
                                             required="true" 
                                             data-bv-notempty="true"
                                             data-bv-notempty-message="Es necesario especificar un identificador de carga"
                                             />
                            </div>
                        </div>
                        <s:hidden name="tipoAsignacion" id="tipoAsignacionh"/>
                        <s:hidden name="nivel" id="nivelh" />
                        <s:hidden name="boleta" id="boletah" />
                        <s:hidden name="unidadAcademica" id="unidadAcademicah"/>
                        <s:hidden name="periodo" id="periodoh" />
                        <div class="col-xs-12 form-group">
                            <div class="col-xs-offset-2 col-xs-10">
				<a href="#" target='_blank' onclick="addURL(this)" id="generarbtn" class="solo-lectura btn btn btn-primary pull-right">Generar solicitud</a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <div class="row" style="display: none;" id="div-datosAsignacion">
        <div class="col-xs-12">
            <div class="main-box clearfix" >
		<header class="main-box-header clearfix">
                    <h2 class="pull-left">Datos solicitud</h2>
                </header>
                <div class="main-box-body clearfix">
		    <form class="form-horizontal">
                        <div class="form-group col-sm-12 ">
                            <label class="col-sm-2 control-label">Solicitud</label>
                            <div class="col-sm-10">
                                <input type="text" disabled class="form-control" id="identf"/>
                            </div>
                        </div>
			<div class="form-group col-sm-12 ">
                            <label class="col-sm-2 control-label">Número de alumnos</label>
                            <div class="col-sm-10">
                                <input type="text" disabled class="form-control" id="noAlumnos"/>
                            </div>
                        </div>
                        <div class="form-group col-sm-12">
                            <label class="col-sm-2 control-label">Fecha de generación.</label>
                            <div class="col-sm-10">
                                <input type="text" disabled class="form-control" id="fechaGen"/>
                            </div>
                        </div>
			<div class="form-group col-sm-12">
                            <label class="col-sm-2 control-label">Usuario generación.</label>
                            <div class="col-sm-10">
                                <input type="text" disabled class="form-control" id="usuarioGen"/>
                            </div>
                        </div>

                        <div class="col-xs-12 form-group" id="botonesRevertir">
                            <div class="btn-group pull-right">
				<security:authorize ifAnyGranted="ROLE_JEFEBECAS">
				    <a href="#" onclick="eliminar()" id="cancelarbtn" class="btn btn btn-primary">Cancelar Solicitud</a>
				</security:authorize>
				<a href="#" target='_blank' onclick="addURLB(this)" id="imprimirbtn" class="btn btn btn-primary pull-right">Archivo solicitud</a>
				<a href="#" onclick="addURLC(this)" id="enviarbtn" class="pre fancybox fancybox.iframe btn btn-primary pull-right" >Enviar Correo</a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Tabla -->
    <div class="row" id="div-tabla" style="display: none;">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <div class="col-md-12" style="padding: 0px">
                    <div class="responsive">                        
                        <table id="listado" class="table table-striped table-hover dt-responsive" style="width: 100%">
                            <thead>
                                <tr>
                                    <th>Unidad Académica</th>
                                    <th>Nombre</th>
                                    <th>CURP</th>
                                    <th>Boleta</th>
                                </tr>
                            </thead>
                        </table>                        
                    </div>
                </div>
            </div>
        </div>                
    </div>
    <!--Termina Tabla --> 
    <form id="eliminarForm" action="/admin/cancelarSolicitudBanamex.action" method="POST" >
        <input type="hidden" name="identff" id="identff" value="" />
    </form>

</body>

<content tag="endScripts">
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js"></script>
    <script type="text/javascript" src="/vendors/fancy-box/jquery.fancybox.min.js"></script>
    <script type="text/javascript" src="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.js"></script>
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>
				    var tipoAsignacion,
					    boleta,
					    nivel,
					    unidadAcademica,
					    periodoId;
				    $(document).ready(function () {

					$("#formAsignar").bootstrapValidator({
					    feedbackIcons: {
						valid: 'glyphicon glyphicon-ok',
						invalid: 'glyphicon glyphicon-remove',
						validating: 'glyphicon glyphicon-refresh'
					    }
					});

					$("#btn-deshacer").on('click', function () {
					    $("#identificadorr").val($("#identificadorBusqueda").val());
					    $("operacion").val(1); // etiqueta s oculta
					});

					$("#btn-busqueda-principal").on('click', function () {
					    $('#div-datosAsignacion').css('display', 'none');
					    $('#generarbtn').hide();

					    tipoAsignacion = $('#tipoAsignacion option:selected').val();
					    boleta = $('#boleta').val();
					    nivel = $('#nivel option:selected').val();
					    unidadAcademica = $('#unidadAcademica option:selected').val();
					    claveUA = $('#unidadAcademica option:selected').attr('data-clave');
					    periodoId = $('#periodo option:selected').val();
					    var nuevoIdentifficador = "";

					    if (claveUA < 10)
						claveUA = "0" + claveUA;
					    if (tipoAsignacion == 1) {
						nuevoIdentifficador += "NU-";
					    } else {
						nuevoIdentifficador += "RC-";
					    }
					    if ($.trim(boleta) == "") {
						if (nivel == 1) {
						    nuevoIdentifficador += "NMS-";
						} else if (nivel == 2) {
						    nuevoIdentifficador += "NSS-";
						} else {
						    nuevoIdentifficador += "TDS-";
						}
						if (unidadAcademica != 0) {
						    nuevoIdentifficador += claveUA + "-";
						} else {
						    nuevoIdentifficador += "00-";
						}
						var per = $('#periodo option:selected').text().replace("-", "");
						nuevoIdentifficador += per + "-";
					    } else {
						nuevoIdentifficador += boleta + "-";
					    }

					    $.ajax({
						type: 'POST',
						url: '/ajax/identificadorCuentaTarjetaAjax.action',
						dataType: 'json',
						data: {identificador: nuevoIdentifficador,
						    periodoId: periodoId,
						    nivel: nivel,
						    unidadAcademica: unidadAcademica,
						    boleta: boleta,
						    tipoAsignacion: tipoAsignacion},
						cache: false,
						success: function (adata) {
						    $('#identificador').val(adata.data[0][0]);
						    $('#tarjetasDisponibles').val(adata.data[0][1]);

						    if (adata.data[0][1] == "0") {
							$('#generarbtn').hide();
							$('#identificador').val("");
						    } else {
							$('#generarbtn').show();
						    }
						}
					    });
					    $('#tipoAsignacionh').val(tipoAsignacion);
					    $('#nivelh').val(nivel);
					    $('#boletah').val(boleta);
					    2
					    $('#unidadAcademicah').val(unidadAcademica);
					    $('#periodoh').val(periodoId);
					    $('#observaciones').val("");
					    $('#div-asignar').css('display', 'block');
//					    mostrarTabla();
					});

					$("#btn-busqueda-identificador").on('click', function () {
					    $('#div-asignar').css('display', 'none');
					    $('#botonesRevertir').hide();

					    var ident = $('#identificadorBusqueda').val();

					    $.ajax({
						type: 'POST',
						url: '/ajax/identificadorCuentaBTarjetaAjax.action',
						dataType: 'json',
						data: {identificador: ident},
						cache: false,
						success: function (adata) {
						    var dat = adata.data[0];
						    $('#identf').val(dat[0]);
						    $('#noAlumnos').val(dat[1]);
						    $('#fechaGen').val(dat[2]);
						    $('#usuarioGen').val(dat[3]);

						    if (dat[1] == "0") {
							$('#botonesRevertir').hide();
						    } else {
							$('#botonesRevertir').show();
						    }
						},
						error: function () {
						    $('#identf').val("");
						    $('#noAlumnos').val("");
						    $('#fechaGen').val("");
						    $('#usuarioGen').val("");
						}

					    });

					    $('#div-datosAsignacion').css('display', 'block');
//					    mostrarTabla();
					});

					$('#identificadorBusqueda').on('keypress', function (e) {
					    if (e.which === 13) {
						$(this).attr("disabled", "disabled");
						buscar();
						$(this).removeAttr("disabled")
					    }
					});
					$("#boton-TAsignadas").on('click', function (e) {
					    e.preventDefault();
					    $(this).attr("disabled", "disabled");
					    buscar();
					    $(this).removeAttr("disabled")
					});

					$(".fancybox").fancybox({
					    autoSize: true
					});
				    });

				    function buscar() {
					$('#div-tabla').css('display', 'none');
					$('#div-asignar').css('display', 'none');

					$.ajax({
					    type: 'POST',
					    url: '/ajax/asignadosTarjetaAjax.action',
					    data: {identificador: $("#identificadorBusqueda").val()},
					    dataType: 'json',
					    success: function (aData) {
						if (aData.data[0] != undefined) {
						    $('#div-datosAsignacion').css('display', 'block');
						    $("#noAlumnos").val(aData.data[0][0]);
						    $("#fechaEjecucion").val(aData.data[0][1]);
						    $("#observacionesEjecutados").val(aData.data[0][2]);
						    $("#correoEjecutados").val(aData.data[0][3]);
						    $("#archivoPejecutados").attr('href', aData.data[0][4] + "&tipoArchivo=2");
						    $("#archivoAejecutados").attr('href', aData.data[0][4] + "&tipoArchivo=1");
						    $("#botonesRevertir").show();
						} else {
						    $('#div-datosAsignacion').css('display', 'none');
						    ModalGenerator.notificacion({
							"titulo": "Atención",
							"cuerpo": "No hay asiganaciones con ese identificador de carga.",
							"tipo": "WARNING",
						    });
						}
					    },
					    error: function () {
						ModalGenerator.notificacion({
						    "titulo": "Atención",
						    "cuerpo": "Hubo un problema que impidió que se completara la operación.",
						    "tipo": "WARNING",
						});
					    }
					});
				    }

				    function mostrarTabla() {
					var url = '/ajax/solicitudCuentaTarjetaAjax.action?tipoAsignacion=' + tipoAsignacion + '&boleta=' + boleta + '&nivel=' + nivel + '&unidadAcademica=' + unidadAcademica + '&periodoId=' + periodoId;
					generarTabla("div-tabla", url);
				    }

				    function getUnidades() {
					$.ajax({
					    type: 'POST',
					    url: '/ajax/getUnidadAcademicaAjax.action',
					    dataType: 'json',
					    data: {pkNivel: $('#nivel').val()},
					    cache: false,
					    success: function (aData) {

						$('#unidadAcademica').get(0).options.length = 0;
						var res = '';
						$.each(aData.data, function (i, item) {
						    res += "<option data-clave = '" + item[2] + "'value='" + item[0] + "'>" + item[1] + "</option>";
						});
						$('#unidadAcademica').html(res);
					    },
					    error: function () {
						ModalGenerator.notificacion({
						    "titulo": "Atención",
						    "cuerpo": "Hubo un problema que impidió que se completara la operación.",
						    "tipo": "WARNING",
						});
					    }
					});
					return false;
				    }

				    function addURL(element) {
					$(element).attr('href', function () {
					    return "/admin/descargarListadoBanamex.action?periodo=" + periodoId + "&numeroDeBoleta=" + boleta + "&nivel=" + nivel + "&ua=" + unidadAcademica + "&identificador=" + $('#identificador').val() + "&tipoAsignacion=" + tipoAsignacion;
					});
				    }

				    function addURLB(element) {
					$(element).attr('href', function () {
					    return "/admin/descargarListadoIdntBanamex.action?identificador=" + $('#identf').val();
					});
				    }

				    function addURLC(element) {
					var url = '/admin/formEnvioCorreos.action';
					var idn = $('#identf').val();

					$(element).attr('href', function () {
					    return url + "?opcion=8&alumnosL=" + idn;
					});
				    }

				    function eliminar() {
					var idn = $('#identf').val();
					ModalGenerator.notificacion({
					    "titulo": "¿Deseas continuar?",
					    "cuerpo": "Esto borrará los registros de la orden seleccionada y no se podran recuperar los datos de los alumnos incluidos dentro de esta solicitud, ¿Está seguro de querer continuar con la operación?",
					    "funcionAceptar": function () {
						$('#identff').val(idn);
						$("#eliminarForm").submit();
					    },
					    "tipo": "ALERT"
					});
				    }
    </script>
</content>
