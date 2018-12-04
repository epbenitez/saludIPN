<%@taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />

        <title>Recuperar contraseña - SIBEC</title>

        <!-- bootstrap -->
        <link rel="stylesheet" type="text/css" href="/vendors/bootstrap/bootstrap.min-3.3.7.css" />

        <!-- global styles -->
        <link rel="stylesheet" type="text/css" href="/resources/css/theme_styles.min.css" />

        <!-- libraries -->
        <link rel="stylesheet" type="text/css" href="/vendors/font-awesome/font-awesome.min-4.6.3.css" />

        <!-- Favicon -->
        <link type="image/x-icon" href="/favicon.png" rel="shortcut icon" />

        <!-- Struts utils -->
        <link rel="stylesheet" href="/vendors/struts/styles.min.css" type="text/css">

        <!-- google font libraries -->
        <link href='https://fonts.googleapis.com/css?family=Open+Sans:400,600,700,300|Titillium+Web:200,300,400' rel='stylesheet' type='text/css'>

        <!-- this page specific styles -->
        <link rel="stylesheet" type="text/css" href="/resources/css/font_login.min.css" />
        <link rel="stylesheet" type="text/css" href="/resources/css/login.min.css" />
        <link rel="stylesheet" type="text/css" href="/vendors/nanoscroller/nanoscroller.min.css" />

        <!--[if lt IE 9]>
      <script src="js/html5shiv.js"></script>
      <script src="js/respond.min.js"></script>
      <![endif]-->

    </head>
    <body id="login-page">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div id="login-box">
                        <div id="login-box-holder">
                            <div class="row">
                                <div class="col-xs-12">
                                    <header id="login-header">
                                        <div id="login-logo">
                                            RECUPERAR CONTRASEÑA
                                        </div>
                                    </header>

                                    <s:if test="hasActionErrors()">
                                        <div class="col-xs-12 errorIniciar">
                                            <s:set var="error"> <s:actionerror/></s:set>
                                            <s:actionerror/>
                                        </div>
                                    </s:if>
                                    <s:if test="hasActionMessages()">
                                        <div class="col-xs-12 exitoIniciar">
                                            <s:actionmessage />
                                        </div>
                                    </s:if>

                                    <div id="login-box-inner">
                                        <div class="row">
                                            <div class="col-lg-5" id="img_login">
                                                <img src="/resources/img/login/logo-sibec.svg">
                                            </div> 
                                            <div class="col-lg-7">
                                                <form role="form" id="j_spring_security_check" name="j_spring_security_check" action="/contrasenia/recuperaContrasenia.action" method="post" class="formularioLogeo" data-toggle="validator" role="form">
                                                    <div class="form-group">
                                                        <div class="input-group">
                                                            <span class="input-group-addon">
                                                                <img src="/resources/img/login/usuario.png" width="23" height="23">
                                                            </span>    
                                                            <input class="form-control"
                                                                   type="text" 
                                                                   placeholder="Nombre de usuario" 
                                                                   name="boleta"
                                                                   id="boleta" 
                                                                   required="true" 
                                                                   data-bv-notempty="true"
                                                                   data-bv-notempty-message="El usuario es requerido"
                                                                   pattern="^[a-zA-Z0-9]+$" 
                                                                   data-bv-regexp-message="Tu usuario debe estar conformado por números o letras"
                                                                   maxlength="20"/>
                                                        </div>   
                                                    </div>
						    <div class="clearfix">&nbsp;</div> 
						    <div class="clearfix">&nbsp;</div> 
                                                    <button type="submit" id="enviarBtn" class="btn btn-success pull-right">Recuperar</button>
                                                </form>
                                            </div> 
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div id="login-box-footer">
                            <div class="row">
                                <div class="col-xs-12" >
                                    <span>Aún no te has inscrito para <b>solicitar tu beca?</b></span><br/>
                                    <a href="/registro/inicioRegistro.action" style="color:#56a9a8;">
                                        <b>¡Registrate aquí!</b>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- global scripts -->
        <script src="/vendors/jQuery/jquery-3.1.1.min.js"></script>
        <script src="/vendors/bootstrap/bootstrap-3.3.7.min.js"></script>
        <script src="/vendors/nanoscroller/jquery.nanoscroller-0.8.7.min.js"></script>
        <script src="/resources/js/scripts.min.js" type="text/javascript"></script>

        <!-- this page specific scripts -->
        <script type="text/javascript" src="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.js"></script>

        <!-- this page specific inline scripts -->
        <script>

	    $(document).ready(function () {
		$('#j_spring_security_check').bootstrapValidator();
		if ($(".errorMessage")) {
		    $(".errorMessage li span").unwrap().unwrap();
		}
	    });

	    var errorLogin = '<s:property  value="error" escapeJavaScript="true"/>';
	    if (errorLogin.indexOf('Exception') >= 0) {
		$("ul.errorMessage").html('<li><span>Estamos realizando tareas de mejora. Por favor, int&eacute;nte accesar m&aacute;s tarde.</span></li>');
		console.log(errorLogin);
	    }
	    if (errorLogin.indexOf('ent_usuario') >= 0) {
		$("ul.errorMessage").html('<li><span>Estamos realizando tareas de mejora. Por favor, int&eacute;nte accesar m&aacute;s tarde.</span></li>');
		console.log(errorLogin);
	    }

        </script>

    </body>
</html>