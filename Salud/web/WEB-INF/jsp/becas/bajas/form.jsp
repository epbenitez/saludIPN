<%-- 
    Document   : form
    Created on : 29/10/2015, 12:37:37 PM
    Author     : JLRM
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<head>
    <title>Baja de becas</title>
</head>
<content tag="tituloJSP">
    Baja diversa
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
    <div class="row ">
	<div class="col-xs-12">
	    <div class="form-group col-xs-12">
		<label class="col-xs-4 control-label">
		    <span class="pull-right">
			Tipo de baja
		    </span>
		</label>
		<div class="col-xs-7">
		    <s:select cssClass="form-control"
			      name="idTipoBaja"
			      id="idTipoBaja"
			      list="ambiente.tipoBajasDetalleList" 
			      listKey="id" listValue="nombre"/>
		</div>
	    </div>
	    <div class="form-group col-xs-12">
		<label class="col-xs-4 control-label">
		    <span class="pull-right">
			Observaciones
		    </span>
		</label>
		<div class="col-xs-7">
		    <s:textarea cssClass="form-control"
				name="observaciones"
				id="observaciones"
				placeholder="Observaciones"
				cols="50" rows="10" 
				data-bv-message="Este dato no es válido"
				required="true" 
				data-bv-notempty="true"
				data-bv-notempty-message="Las observaciones son requeridas"
				pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s1234567890]+$" 
				data-bv-regexp-message="No se permiten caracteres especiales"
				data-bv-stringlength="true" 
				data-bv-stringlength-min="3" 
				data-bv-stringlength-max="250"/>
		</div>
	    </div>
	    <div class="form-group">
		<div class="col-xs-11">
		    <button onclick="datos()" class="btn btn-primary pull-right">Confirmar</button>
		</div>
	    </div>
	</div>
</body>

<content tag="endScripts">
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>
    <script type="text/javascript" src="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>
			$(document).ready(function () {
			    $('#tipoBeca').bootstrapValidator({});
			    parent.setB();
			    var current = $("#bajas").val();
			    if (current.length <= 1) {
				parent.$.fancybox.close();
			    }
			});

			function datos() {
			    parent.setDatos($('#idTipoBaja option:selected').val(), $('#observaciones').val());
			    parent.$.fancybox.close();
			}
    </script>
</content>

