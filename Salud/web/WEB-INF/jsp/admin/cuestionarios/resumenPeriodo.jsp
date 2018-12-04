<%-- 
    Document   : Template SIBEC
    Created on : 28-Septiembre-2016
    Author     : Gustavo Adolfo Alamillo Medina
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Resumen de Solicitudes de Beca</title>
</head> 

<content tag="tituloJSP">
    Resumen de Solicitudes de Beca
</content>

<body>

    <div class="row">
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
    </div>

    <!-- Ejemplo de caja blanca -->
    <div class="row">
        <div class="col-xs-12">
            <div class="main-box clearfix" >
                <div class="main-box-body clearfix">
                    <form class="form-horizontal">
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Periodo</label>
                            <div class="col-sm-10">
                                <s:select id="periodo"
                                          cssClass="form-control"
                                          name="periodo"
										  list="periodos" 
                                          listKey="id"
                                          listValue="clave"
                                          headerKey=""/>
                            </div>
                        </div>
<!--                        <div class="form-group">
                            <label class="col-sm-2 control-label">Estatus Solicitud</label>
                            <div class="col-sm-10">
                                <select name="estatusSolicitud" id="estatusSolicitud" class="form-control" onchange="reload()">
                                    <option value="1">Asignada</option>
                                    <option value="2">Rechazada</option>
                                    <option value="3">En Espera</option>
                                    <option value="4">Pendientes</option>
                                </select> 
                            </div>
                        </div>-->
                        <div class="form-group">
                            <div class="col-lg-offset-6 col-lg-6">                                                                                               
								<a href="#" target='_blank' onclick="addURL(this)" id="fncbx" class="btn btn btn-primary pull-right">Generar reporte</a>
                            </div>                            
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div> <!-- Termina ejemplo caja blanca -->

</body>

<content tag="inlineScripts">
    <script>
		function addURL(element) {
			var per = $('#periodo option:selected').val();
			var op = $('#estatusSolicitud option:selected').val();
			$(element).attr('href', function () {
//				return "/tablero/solicitudes/descargarResumenSolicitudes.action?periodo=" + per + "&op=" + op;
				return "/tablero/solicitudes/descargaResumenSolicitudes.action?periodo=" + per;
			});
		}
    </script>
</content>