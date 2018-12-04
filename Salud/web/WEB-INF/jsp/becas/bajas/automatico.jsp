<%-- 
    Document   : Baja automatica
    Created on : 30/01/17
    Author     : JLRM
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Baja automatica de Becas</title>
    <link rel="stylesheet" type="text/css" href="/vendors/fancy-box/jquery.fancybox.min.css"/>
    <link rel="stylesheet" type="text/css" href="/vendors/select2/select2-old/select2.min.css"/>
    <link rel="stylesheet" type="text/css" href="/vendors/select2/select2-old/select2-bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="/vendors/pretty-checkbox/pretty.min.css"/>
    <style>
        /* RECUADROS */
        .rectangulo{
            display: table;
            height: 49px;
            text-align: center;
            color:white;
            line-height: 110%;
            border:2px solid white;
        }

        .rectangulo:hover{
            cursor: pointer;
            border:none;
        }

        .rectangulo span {
            display: table-cell;
            vertical-align: middle;
        }

        .leyendasProceso{
            margin-bottom: 20px;
            margin-top: -5px;
            color: #7B7B7B;
        }

        .leyendasProceso span{
            margin-left: 20px;
            font-size: 14pt;
        }

        /* CIRCULOS */
        .abierto{color: #A2CB7C;}
        .cerrado{color: #EF3555;}
        .activo{color: #6BC1C5;}

        .activo-bg{
            background-color: #82BFBF !important;
        }

    </style>
</head> 

<content tag="tituloJSP">
    Baja automatica de Becas
</content>

<body>
    <div class="row" id="rowProcesos">
        <div class="col-xs-12">
            <div class="main-box clearfix" >
                <div class="row">
                    <header class="main-box-header clearfix" style=" padding-bottom: 0px;">
                        <h2 class="col-lg-5">
			    Movimientos
                        </h2>
                    </header>
                    <div class="main-box-body clearfix">
                        <div class="row" id="listaProcesos">
                        </div>
                        <br/>
		    </div>
		</div>
	    </div>
	</div>
    </div>

    <div class="row" id="leyendaProcesoRow" style="display:none;">
	<div class="col-lg-12">
	    <div class="leyendasProceso">
		<b>MOVIMIENTOS</b>
		<span class="abierto">&#9679;</span> Abiertos
		<span class="cerrado">&#9679;</span> Cerrados
		<span class="activo">&#9679;</span> Seleccionado
	    </div>
	</div>
    </div>
    <!-- Termina row procesos -->

    <div class="row" id="errorMensaje" style="display: none;">
	<div class="col-lg-12">
	    <div class="alert alert-warning">
		<i class="fa fa-warning fa-fw fa-lg"></i>
		<strong>Advertencia!</strong> <span id="textoErrorMensaje">texto</span>
	    </div>
	</div>
    </div>

    <div class="row" id="panel" style="display:none;">
        <div class="col-xs-12">
	    <div class="main-box clearfix">
		<div class="main-box-body clearfix">
		    <div class="row" id="contenedorCandidatos">
		    </div>
		</div>
	    </div>
	</div>
    </div>
</body>

<content tag="inlineScripts">
    <script>
	$("#listaProcesos").show(); //Quitar
	/*PROCESO SELECCIONADO*/
	var procSelectActivo;
	var tipoBaja;

	$(document).ready(function () {
	    $("#unidadesAcademicas,#becasPeriodo").select2({language: "es"});
	    actualizarProcesos();

	    $('#pasantiaButton').click(function (e) {
		darBajaAuto(5);
		$('#pasantiaButton').blur();
	    });

	    $('#incumplimientoButton').click(function (e) {
		darBajaAuto(4);
		$('#incumplimientoButton').blur();
	    });
	});

	function activo(elemento) {
	    procSelectActivo = Number($(elemento).data("activo"));
	    tipoBaja = Number($(elemento).data("baja"));
	    if (procSelectActivo !== 1) {
		$("#errorMensaje").show();
		$("#textoErrorMensaje").html("No es posible realizar el movimiento. La secuencia de Procesos Automaticos no se encuentra activa.");
		$("#panel").hide();

	    } else {
		$("#errorMensaje").hide();
	    }
	    //Interfaz
	    $("#listaProcesos div").removeClass("activo-bg");
	    $(elemento).addClass("activo-bg");

	    if (procSelectActivo === 1) {
		mostrarDatos();
	    }
	}

	function actualizarProcesos() {
	    $.ajax({
		type: 'POST',
		url: '/ajax/automaticasBecasBajasAjax.action',
		dataType: 'json',
		cache: false,
		success: function (aData) {
		    if (aData.data.length !== 0) {
			$("#listaProcesos").show().empty();
			$.each(aData.data, function (i, item) {
			    $("#listaProcesos").append(item);
			});
			$("#leyendaProcesoRow").show();
		    } else {
			$("#listaProcesos").hide();
			ModalGenerator.notificacion({
			    "titulo": "Información",
			    "cuerpo": "Hubo un problema que impidió que se completara la operación.",
			    "tipo": "WARNING"
			});
		    }
		},
		error: function () {
		    $("#listaProcesos").hide();
		    ModalGenerator.notificacion({
			"titulo": "Ocurrió un error",
			"cuerpo": "Hubo un problema que impidió que se completara la operación.",
			"tipo": "DANGER"
		    });
		}
	    });
	}

	function mostrarDatos() {
	    $.ajax({
		type: 'POST',
		url: '/ajax/mostrarDatosBecasBajasAjax.action',
		dataType: 'json',
		data: {prc: procSelectActivo, tipoBaja: tipoBaja},
		cache: false,
		success: function (aData) {
		    if (aData.data.length !== 0) {
			$("#contenedorCandidatos").show().empty();
			$.each(aData.data, function (i, x) {
			    $("#contenedorCandidatos").append(x);
			});
			$("#panel").show();
		    } else {
			$("#contenedorCandidatos").hide();
			ModalGenerator.notificacion({
			    "titulo": "Información",
			    "cuerpo": "Hubo un problema que impidió que se completara la operación.",
			    "tipo": "WARNING"
			});
		    }
		},
		error: function () {
		    $("#contenedorCandidatos").hide();
		    ModalGenerator.notificacion({
			"titulo": "Ocurrió un error",
			"cuerpo": "Hubo un problema que impidió que se completara la operación.",
			"tipo": "DANGER"
		    });
		}
	    });
	}

	function darBajaAuto(tb) {
	    var postParams = "tipoBaja=" + tb + "&alta=true";

	    ModalGenerator.notificacion({
		"titulo": "Procesando la petición",
		"tipo": "INFO",
		"sePuedeCerrar": false,
		"cuerpo": "Se esta procesando tu solicitud"
	    });

	    $.ajax({
		type: 'POST',
		url: "/ajax/darBajaAutoBecasBajasAjax.action",
		data: postParams,
		cache: false,
		success: function (aData) {
		    ModalGenerator.cerrarModales();
		    var obj = jQuery.parseJSON(aData);
		    var json_obj = obj.data[0];
		    if (json_obj.hasOwnProperty("error")) {
			ModalGenerator.notificacion({
			    "titulo": "Ocurrió un error",
			    "cuerpo": json_obj.error,
			    "tipo": "ALERT"
			});
		    } else {
			var cuerpo = "<div class='row'><div class='col-lg-6'><div class='alert alert-success'><i class='fa fa-check-circle fa-fw fa-lg'></i>"
				+ "<strong>" + obj.data[0].bien + "</strong> Bajas realizadas"
				+ "</div></div><div class='col-lg-6'><div class='alert alert-danger'><i class='fa fa-times-circle fa-fw fa-lg'></i>"
				+ " <strong>" + obj.data[1].mal + "</strong> Bajas con error"
				+ "</div></div></div>";

			if (Number(obj.data[1].mal) === 0) {
			    cuerpo += "<p>El proceso de bajas concluyo sin errores, ¡Felicidades!</p>";
			} else {
			    cuerpo += "<div id='contenedorResultado'><table id='tablaResultados' class='table table-striped table-hover dt-responsive' style='width: 100%'><thead><tr>"
				    + "<th>Boleta</th><th>Alumno</th><th>Razón</th>"
				    + "</tr></thead></table></div>";
			}
			ModalGenerator.notificacion({
			    "titulo": "Resultado de la operación",
			    "tipo": "PRIMARY",
			    "cuerpo": cuerpo,
			    "funcionCerrar": function () {
				mostrarDatos();
			    }
			});
			if (Number(obj.data[1].mal) !== 0) {
			    generarTabla("contenedorResultado", obj.data.slice(2, obj.data.length), null, false, null, 5);
			}
		    }

		},
		error: function () {
		    ModalGenerator.notificacion({
			"titulo": "Ocurrió un error",
			"cuerpo": "No se pudo realizar la operación",
			"tipo": "ALERT"
		    });
		}
	    });

	}
    </script>
</content>

<content tag="endScripts">
    <script type="text/javascript" src="/vendors/fancy-box/jquery.fancybox.min.js"></script>
    <script type="text/javascript" src="/vendors/datatables/datatables.js" ></script>
    <script type="text/javascript" src="/resources/js/ModalGenerator.js" ></script>
    <script type="text/javascript" src="/resources/js/generador-tablas.js" ></script>
    <script type="text/javascript" src="/vendors/select2/select2-old/select2.min.js"></script>
    <script type="text/javascript" src="/vendors/select2/select2-old/select2_locale_es.js"></script>
</content>
