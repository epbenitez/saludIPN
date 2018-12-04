<%-- 
    Document   : form
    Created on : 27/10/2015, 02:09:23 PM
    Author     : Victor Lozano
    Redesign     : Gustavo Adolfo Alamillo Medina
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Administración de procesos</title>
    <link rel="stylesheet" type="text/css" href="/vendors/fancy-box/jquery.fancybox.min.css"/>
</head> 

<content tag="tituloJSP">
    Administración de procesos
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


    <!-- Ejemplo tabla -->
    <div class="row" id="rowTabla" style="display: none;">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <div class="col-md-12" style="padding: 0px">
                    <div class="responsive">                        
                        <table id="tablaProcesos" class="table table-striped table-hover dt-responsive" style="width: 100%">
                            <thead>
                                <tr>
                                    <th data-orderable="true">Nombre</th>
                                    <th data-orderable="true">Movimiento</th>
                                    <th data-center="true"></th>
                                    <th data-center="true"></th>
                                </tr>
                            </thead>
                        </table>                        
                    </div>
                </div>
            </div>
        </div>                
    </div>
    <!--Termina ejemplo tabla --> 


    <form id="eliminarForm" action="/catalogos/eliminarTipoProceso.action" method="POST" >
        <input type="hidden" name="proceso.id" id="id" value="" />
    </form>

</body>

<content tag="endScripts">
    <script type="text/javascript" src="/vendors/fancy-box/jquery.fancybox.min.js"></script>
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js" 
            data-init="true" data-ajax="/ajax/listadoTipoProcesoAjax.action" data-div="rowTabla"
    data-function="despuesCargarTabla" data-button="botonAgregar"></script>
</content>

<content tag="inlineScripts">
    <script>
	function despuesCargarTabla() {
	    $('.fancybox').fancybox({
		afterClose: function () {
		    recargarTabla();
		}
	    });
	}
	function recargarTabla() {
	    generarTabla("rowTabla", "/ajax/listadoTipoProcesoAjax.action", despuesCargarTabla, botonAgregar);
	}

	var botonAgregar = [{
		text: '<a id="nueva-entidad" title="Nuevo proceso" href="/catalogos/formTipoProceso.action" class="fancybox fancybox.iframe solo-lectura btn btn-primary"  name="nuevo" value="Nuevo" ><i class="fa fa-plus-circle fa-lg"></i>&nbsp;&nbsp;Agregar proceso</a>',
		className: 'normal-button'
	    }];

	function eliminar(id) {
	    ModalGenerator.notificacion({
		"titulo": "¿Deseas continuar?",
		"cuerpo": "Esto borrará el registro seleccionado, ¿Está seguro de quererlo eliminar?",
		"funcionAceptar": function () {
		    $("#id").val(id);
		    $("#eliminarForm").submit();
		},
		"tipo": "ALERT"
	    });
	}
    </script>
</content>