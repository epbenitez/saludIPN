<%-- 
    Document   : visualizar
    Created on : 27/10/2016, 11:53:30 AM
    Author     : Tania G. Sánchez
    Redesing   : Mario Márquez
--%>

<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Historial Becas</title>
    <link rel="stylesheet" type="text/css" href="/vendors/fancy-box/jquery.fancybox.min.css"/>
	<link href="https://gitcdn.github.io/bootstrap-toggle/2.2.0/css/bootstrap-toggle.min.css" rel="stylesheet">
</head> 

<content tag="tituloJSP">
    Historial Becas
</content>

<body>
    <s:if test="hasActionErrors()">
        <div class="row">
            <div class="col-xs-12">
                <div class="alert alert-danger">
                    <i class="fa fa-times-circle fa-fw fa-lg"></i>
                    <strong>&iexcl;Error!</strong> <s:actionerror/>
                </div>
            </div>
        </div>
    </s:if>
    <s:else>
		<!-- Formularios -->
		<div class="row">
			<div class="col-xs-12">
				<div class="main-box clearfix">
					<div class="main-box-body">
						<form class="form-horizontal">
							<div class="form-group">
								<label class="col-sm-2 control-label">Boleta</label>
								<div class="col-sm-10">
									<s:textfield name="alumno.boleta" cssClass="form-control" readonly="true"/>
								</div>
							</div> 
							<div class="form-group">
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
		<!--Termina formularios -->

		<s:if test="mostrarOtorgamiento">
			<div class="main-box otorgamiento col-sm-12">            
				<s:if test="becas==18">
					<img src="/resources/img/becasDisponibles/becalos1.png" style="width: 100%;"/>
				</s:if>
				<s:if test="becas==19">
					<img src="/resources/img/becasDisponibles/becalos2.png" style="width: 100%;"/>
				</s:if>
				<s:if test="becas==20">
					<img src="/resources/img/becasDisponibles/becalos3.png" style="width: 100%;"/>
				</s:if>
				<s:if test="becas==21">
					<img src="/resources/img/becasDisponibles/becalos4.png" style="width: 100%;"/>
				</s:if>
				<s:if test="becas==24 || becas==25">
					<img src="/resources/img/becasDisponibles/harp-helu.png" style="width: 100%;"/>
				</s:if>
				<s:if test="becas==1 || becas==4">
					<img src="/resources/img/becasDisponibles/ipn-a.png" style="width: 100%;"/>
				</s:if>
				<s:if test="becas==2 || becas==5">
					<img src="/resources/img/becasDisponibles/ipn-b.png" style="width: 100%;"/>
				</s:if>
				<s:if test="becas==3 || becas==6">
					<img src="/resources/img/becasDisponibles/ipn-c.png" style="width: 100%;"/>
				</s:if>
				<s:if test="becas==32">
					<img src="/resources/img/becasDisponibles/manutencion1.png" style="width: 100%;"/>
				</s:if>
				<s:if test="becas==33">
					<img src="/resources/img/becasDisponibles/manutencion2.png" style="width: 100%;"/>
				</s:if>
				<s:if test="becas==34">
					<img src="/resources/img/becasDisponibles/manutencion3.png" style="width: 100%;"/>
				</s:if>
				<s:if test="becas==35">
					<img src="/resources/img/becasDisponibles/manutencion4.png" style="width: 100%;"/>
				</s:if>
				<s:if test="becas==26 || becas==27">
					<img src="/resources/img/becasDisponibles/telmex.png" style="width: 100%;"/>
				</s:if>
			</div>
		</s:if>

		<!-- Tabla -->

		<div class="row" id="div-listado" style="display: none;" >
			<div class="col-xs-12">
				<div id="error" >
					<s:property  value="alumno.error"  />
				</div>
				<div class="main-box clearfix">
					<div class="col-md-12" style="padding: 0px">
						<div class="responsive">                        
							<table id="listado" class="table table-striped table-hover dt-responsive" style="width: 100%">
								<thead>
									<tr>
										<th >Periodo</th>
										<th >Unidad Académica</th>
										<th >Beca</th>
										<th >Tipo de Movimiento</th>
										<th >Movimiento</th>
										<th >Proceso</th>
										<th >Número de depositos</th>
										<th >Fecha</th>
										<th >Semestre</th>
										<th >Promedio</th>
									</tr>
								</thead>
							</table>                        
						</div>
					</div>                
				</div>
			</div>                
		</div>

    </s:else>
    <!--Termina tabla --> 
</body>


<content tag="endScripts">
    <script type="text/javascript" src="/vendors/fancy-box/jquery.fancybox.min.js"></script>
    <script type="text/javascript" src="/resources/js/generador-tablas.js"  data-function="despuesCargarTabla"
    data-init="true" data-ajax="/ajax/listBecasDisponiblesAjax.action?numeroDeBoleta=<s:property value="alumno.boleta"/>" data-div="div-listado"></script>
	<script src="https://gitcdn.github.io/bootstrap-toggle/2.2.0/js/bootstrap-toggle.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>
		function despuesCargarTabla() {
			$('.fancybox').fancybox({
				autoSize: true
			});
			$('[data-toggle="tooltip"]').tooltip();
		}
    </script>
</content>