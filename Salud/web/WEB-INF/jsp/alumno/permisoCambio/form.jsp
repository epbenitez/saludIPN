<%-- 
    Document   : form
    Created on : 29/10/2015, 12:37:37 PM
    Author     : JLRM
--%>

<%@taglib prefix="s" uri="/struts-tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<head>
    <title>Cambio de correo</title>
    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css" />
</head>
<content tag="tituloJSP">
    Cambio de correo
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
    <s:if test="!hasActionMessages()">
        <div class="row ">
	    <div class="main-box clearfix">
		<div class="col-xs-12">
		    <div class="clearfix" >&nbsp;</div>
		    <form id="darBajaDiversasForm" name="darBajaDiversasForm" action="/misdatos/cambioPermisoCambio.action" method="post" class="form-horizontal">
			<div class="form-group col-xs-12">
			    <label class="col-xs-4 control-label">
				<span class="pull-right">
				    Correo actual
				</span>
			    </label>
			    <div class="col-xs-7">
				<s:textfield class="form-control" name="correoAnterior" id="correoAnterior" placeholder="ejemplo@gmail.com" readonly="true"/>
				<span class="help-block" id="nombreMessage" />
			    </div>
			</div>
			<div class="form-group col-xs-12">
			    <label class="col-xs-4 control-label">
				<span class="pull-right">
				    Correo electr&oacute;nico
				</span>
			    </label>
			    <div class="col-xs-7">
				<input type="email" required="true" class="form-control" name="correoNuevo" id="correoNuevo" placeholder="ejemplo@gmail.com">
			    </div>
			</div>
			<div class="form-group col-xs-12">
			    <label class="col-xs-4 control-label">
				<span class="pull-right">
				    Confirmaci&oacute;n correo
				</span>
			    </label>
			    <div class="col-xs-7">
				<input type="email" required="true" class="form-control" name="correoNuevoC" id="correoNuevoC" placeholder="ejemplo@gmail.com">
				<span class="help-block" id="nombreMessage" />
			    </div>
			</div> 
			<div class="form-group">
			    <div class="col-xs-11">
				<s:hidden name="numeroDeBoleta" />
				<button type="submit" class="btn btn-primary pull-right solo-lectura">Guardar</button>
				<div class="clearfix" >&nbsp;</div>
			    </div>
			</div>
		    </form>
		</div>
	    </div>
	</div>
    </s:if>                   
</body>

<content tag="endScripts">
    <script type="text/javascript" src="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>
		    $(document).ready(function () {
			$('#tipoBeca').bootstrapValidator();

			$('input[type=text]').addClass('form-control');
		    });
    </script>
</content>