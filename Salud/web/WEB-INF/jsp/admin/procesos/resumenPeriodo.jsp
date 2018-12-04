<%-- 
    Document   : Template SIBEC
    Created on : 28-Septiembre-2016
    Author     : Gustavo Adolfo Alamillo Medina
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Resumen por periodo</title>
</head> 

<content tag="tituloJSP">
    Resumen por periodo
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
                                          list="ambiente.periodoList" 
                                          listKey="id"
                                          listValue="clave"
                                          headerKey=""/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Movimiento</label>
                            <div class="col-sm-10">
                                <select name="alta" id="alta" class="form-control" onchange="reload()">
                                    <option value="true">Alta</option>
                                    <option value="false">Baja</option>
                                </select> 
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-lg-offset-6 col-lg-6">                                                                                               
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
		return "/procesos/resumen/descargarProceso.action?periodo=" + per + "&i=" + al;
	    });
	}
    </script>
</content>