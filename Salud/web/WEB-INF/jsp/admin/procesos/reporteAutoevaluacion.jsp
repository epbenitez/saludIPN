<%-- 
    Document   : reporteAutoevaluacion
    Created on : 3/05/2017, 10:51:06 AM
    Author     : Sistemas
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Reporte de Autoevaluación</title>
</head> 

<content tag="tituloJSP">
    Reporte de Autoevaluación
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
                            <label for="periodo" class="col-sm-1 control-label">Periodo</label>
                            <div class="col-sm-8 col-lg-9" style="padding-bottom: 5px">
                                <s:select cssClass="form-control"
                                          id="periodo" name="periodo"
                                          list="ambiente.periodoList" 
                                          listKey="id" listValue="clave"/>
                            </div>
                            <div class="col-sm-3 col-lg-2">
                                <!--<button id="boton-filtro" target='_blank' onclick="addURL(this)"  class="btn btn-primary pull-right">Generar reporte</button>-->
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
	    var al = $('#alta option:selected').val();
	    $(element).attr('href', function () {
//                return "/procesos/resumen/cuadroResumenPeriodoProceso.action?periodo=" + per + "&i=" + al;
		return "/procesos/resumen/descargarReporteProceso.action?periodo=" + per + "&i=" + al;
	    });
	}
    </script>
</content>