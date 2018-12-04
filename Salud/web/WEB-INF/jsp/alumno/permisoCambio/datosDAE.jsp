<%-- 
    Document   : datosDAE
    Created on : 25/01/2017
    Author     : JLRM
--%>

<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<head>
    <title>Datos DAE</title>
    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css" />
</head>
<content tag="tituloJSP">
    Datos DAE
</content>
<body>
    <div class="row">
        <div class="col-xs-12">
            <div class="alert alert-info fade in">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                <i class="fa fa-info-circle fa-fw fa-lg"></i>
                <strong>Toma en cuenta</strong> Al hacer clic en guardar, actualizaremos los datos que tenemos en SIBEC, con la información que proporciona la DAE.
            </div>
            <s:if test="hasActionErrors()">
                <div class="alert alert-danger">
                    <i class="fa fa-times-circle fa-fw fa-lg"></i>
                    <strong>&iexcl;Error!</strong> <s:actionerror/>
                </div>
            </s:if>
            <s:if test="hasActionMessages()">
                <script type="text/javascript" language="javascript" class="init">
		    $(document).ready(function () {
			setTimeout(function () {
			    window.parent.$.fancybox.close();
			}, 800);
		    });
                </script>
                <div class="alert alert-success">
                    <i class="fa fa-check-circle fa-fw fa-lg"></i>
                    <strong>&iexcl;Realizado!</strong> <s:actionmessage />
                </div>
            </s:if>
        </div>
    </div>
    <s:if test="!hasActionMessages()">
        <div class="row ">
	    <div class="main-box clearfix">
		<div class="col-xs-12">
		    <div class="clearfix" >&nbsp;</div>
		    <form id="darBajaDiversasForm" name="darBajaDiversasForm" action="/misdatos/guardaDAEPermisoCambio.action" method="post" class="form-horizontal">
			<table class="table table-bordered">
			    <thead>
				<tr>
				    <th></th>
				    <th class="text-center">DAE</th>
				    <th class="text-center">SIBec</th>
				</tr>
			    </thead>
			    <tbody>      
				<tr>
				    <td>
					Unidad Académica
				    </td>
				    <td class="text-center">
					<s:property  value="unidadAcademica" />
				    </td>
				    <td class="text-center">
					<s:property  value="alumno.datosAcademicos.unidadAcademica.nombreCorto" />
				    </td>
				</tr>
				<tr>
				    <td>
					Plan de Estudios
				    </td>
				    <td class="text-center">
					<s:property  value="planEstudios" />
				    </td>
				    <td class="text-center">
					<s:property  value="alumno.datosAcademicos.carrera.planEstudios" />
				    </td>
				</tr>
				<tr>
				    <td>
					Carrera
				    </td>
				    <td class="text-center">
					<s:property  value="carrera" />
				    </td>
				    <td class="text-center">
					<s:property  value="alumno.datosAcademicos.carrera.carrera" />
				    </td>
				</tr>
				<tr>
				    <td>
					Especialidad
				    </td>
				    <td class="text-center">
					<s:property  value="especialidad" />
				    </td>
				    <td class="text-center">
					<s:property  value="alumno.datosAcademicos.carrera.especialidad" />
				    </td>
				</tr>
				<tr>
				    <td>
					Inscripcion
				    </td>
				    <td class="text-center">
					<s:property  value="inscripcion" />
				    </td>
				    <td class="text-center">
					<s:property  value="pe" />
				    </td>
				</tr>
				<tr>
				    <td>
					Regularidad				    
				    </td>
				    <td class="text-center">
					<s:property  value="regularidad" />
				    </td>
				    <td class="text-center">
					<s:property  value="crr" />
				    </td>
				</tr>
				<tr>
				    <td>
					Semestre
				    </td>
				    <td class="text-center">
					<s:property  value="semestre" />
				    </td>
				    <td class="text-center">
					<s:property  value="alumno.datosAcademicos.semestre" />
				    </td>
				</tr>
				<tr>
				    <td>
					Promedio
				    </td>
				    <td class="text-center">
					<s:property  value="promedio" />
				    </td>
				    <td class="text-center">
					<s:property  value="alumno.datosAcademicos.promedio" />
				    </td>
				</tr>
				<tr>
				    <td>
					Modalidad
				    </td>
				    <td class="text-center">
					<s:property  value="modalidad" />
				    </td>
				    <td class="text-center">
					<s:property  value="alumno.datosAcademicos.modalidad.nombre" />
				    </td>
				</tr>
				<tr>
				    <td>
					Carga
				    </td>
				    <td class="text-center">
					<s:property  value="carga" />
				    </td>
				    <td class="text-center">
					<s:property  value="cargaAl" />
				    </td>
				</tr>
                                <tr>
				    <td>
					Materias reprobadas
				    </td>
				    <td class="text-center">
					<s:property  value="materiasReprobadas" />
				    </td>
				    <td class="text-center">
					<s:property  value="alumno.datosAcademicos.reprobadas" />
				    </td>
				</tr>
				<tr>
				    <td>
					Egresado
				    </td>
				    <td class="text-center">
					<s:property  value="egresadoDAE" />
				    </td>
				    <td class="text-center">
					<s:property  value="egresado" />
				    </td>
				</tr>
			    </tbody>
			</table>
			<security:authorize ifAnyGranted="ROLE_JEFEBECAS,ROLE_ANALISTABECAS">
			    <div class="form-group">
				<div class="col-xs-11">
				    <s:hidden name="numeroDeBoleta" />
				    <button type="submit" class="btn btn-primary pull-right">Guardar</button>
				    <div class="clearfix" >&nbsp;</div>
				</div>
			    </div>
			</security:authorize>
		    </form>
		</div>
	    </div>
	</div>
    </s:if>                   
</body>

<content tag="endScripts">
    <script type="text/javascript" src="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.js"></script>
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>
		    $(document).ready(function () {
			$('#tipoBeca').bootstrapValidator();

			$('input[type=text]').addClass('form-control');
		    });

		    $("#darBajaDiversasForm").bootstrapValidator().on('success.form.bv', function (e) {
			e.preventDefault();
			ModalGenerator.notificacion({
			    "titulo": "¿Deseas continuar?",
			    "cuerpo": "Esta acción colocará los datos de la DAE en el SIBec sin posibilidad de revertir los cambios, ¿Está seguro de querer continuar con la operación?",
			    "funcionAceptar": function () {
				$("#darBajaDiversasForm").unbind('submit').submit();
			    },
			    "funcionCancelar": function () {
				$("#darBajaDiversasForm").data('bootstrapValidator').resetForm();
			    },
			    "tipo": "ALERT"
			});
		    });
    </script>
</content>