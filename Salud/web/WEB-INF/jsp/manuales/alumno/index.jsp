<%-- 
    Document   : Manual de usuario
    Created on : 22-Noviembre-2016
    Author     : Gustavo Adolfo Alamillo Medina
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Manual de usuario</title> 
    <style>
        #indiceLinks li{
            margin-bottom: 10px;
            font-size:17px;
        }

        .img-manual {
            box-shadow:15px 15px 40px 1px #888888; 
            margin-bottom: 40px;
        }
    </style>
</head> 

<content tag="tituloJSP">
    Manual de usuario
</content>

<body>

    <div class="row">
        <div class="col-xs-12">
            <div class="main-box clearfix" >
                <div class="row" id="cielo">
                    <header class="main-box-header clearfix">
                        <a download href="/resources/downloadable/manual_alumno.pdf" targ class="btn btn-primary pull-right">
                            <span class="fa fa-download"></span>
                            Descargar
                        </a>
                    </header>
                    <div class="main-box-body clearfix">
                        <div class="row">                            
                            <div class="col-md-12">
                                <div class="panel-group accordion" id="accordion">
                                    <!-- Datos Personales-->
                                    <div class="panel panel-default">
                                        <div class="panel-heading">
                                            <h4 class="panel-title">
                                                <a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
                                                    <i class="fa fa-user"></i> Datos Personales
                                                </a>
                                            </h4>
                                        </div>
                                        <div id="collapseOne" class="panel-collapse collapse" style="height: 2px;">
                                            <div class="panel-body">
                                                <h4 id="encabezado1">
                                                    <a href="/misdatos/datosRegistro.action" >Datos Personales</a>
                                                    <small>(Da click en el título para ir al módulo)</small>
                                                </h4>
                                                <div class="alert alert-info">
                                                    <i class="fa fa-info-circle fa-fw fa-lg"></i>
                                                    <strong>OBJETIVO:</strong> 
                                                    Visualizar los datos personales ingresados en el registro o editar estos en caso de ser revalidantes.
                                                </div>
                                                <h4>Funcionamiento</h4>
                                                <p>Da clic al botón datos personales que se encuentra del lado izquierdo en la parte superior como lo muestra la siguiente pantalla.</p>
                                                <img src="/resources/img/manuales/alumno/datos/0.jpg" width="871" height="694">
                                                <p>Si eres un alumno revalidante podrás actualizar los datos personales que registraste en tu última solicitud de beca. Los datos de la pestaña de datos académicos no se pueden modificar dado que esos datos se obtienen directamente de la DAE.</p>
                                                <img src="/resources/img/manuales/alumno/datos/1.JPG" width="90%" class="center-block img-manual">
                                                <img src="/resources/img/manuales/alumno/datos/2.JPG" width="90%" class="center-block img-manual">
                                                <img src="/resources/img/manuales/alumno/datos/3.JPG" width="900">
                                                <img src="/resources/img/manuales/alumno/datos/4.JPG" width="90%" class="center-block img-manual">
                                                <p>Actualiza los datos de cada una de las pestañas del registro, asegúrate de que tus datos estén correctos dado que una vez que actualices los datos personales no podrás modificarlos.</p>
                                                <p>Da clic en el botón actualizar mis datos para que se guarden tus cambios.</p>
                                                <img src="/resources/img/manuales/alumno/datos/7.PNG" width="80%" class="center-block img-manual">
                                                <p>Una vez que actualices los datos, estos pasaran a ser de sólo lectura.</p>
                                                <p>Si acabas de registrarte, tus datos serán desde un inicio de sólo lectura.</p>
                                                <img src="/resources/img/manuales/alumno/datos/8.PNG" width="80%" class="center-block img-manual">
                                                <img src="/resources/img/manuales/alumno/datos/9.PNG" width="90%" class="center-block img-manual">
                                            </div>
                                        </div>
                                    </div>
                                    <!-- CAMBIO DE CONTRASEÑA-->
                                    <div class="panel panel-default">
                                        <div class="panel-heading">
                                            <h4 class="panel-title">
                                                <a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo">
                                                    <i class="fa fa-lock"></i> Cambio de contraseña
                                                </a>
                                            </h4>
                                        </div>
                                        <div id="collapseTwo" class="panel-collapse collapse" style="height: 2px;">
                                            <div class="panel-body">                                                
                                                <h4 id="encabezado2">
                                                    <a href="/cambioContrasenia.action">Cambio de contraseña</a>
                                                    <small>(Da click en el título para ir al módulo)</small>                                                    
                                                </h4>
                                                <div class="alert alert-info">
                                                    <i class="fa fa-info-circle fa-fw fa-lg"></i>
                                                    <strong>OBJETIVO:</strong> 
                                                    Cambiar la contraseña del usuario en sesión.
                                                </div>
                                                <h4>Funcionamiento</h4>
                                                <p>Dentro del menú principal, da clic a la opción <b>"Cambio de contraseña"</b> ubicada en la sección <b>"Mis datos"</b>.</p>
                                                <p>Se mostrará una pantalla con los tres campos: <b>"Contraseña actual</b>, <b>"Contraseña nueva"</b> y <b>"Repite contraseña nueva"</b>.</p>
                                                <img src="/resources/img/manuales/alumno/pass/1.jpg" width="869" height="656">

                                                <p>
                                                <ul>
                                                    <li>La contraseña debe ser exactamente de 8 caracteres, incluyendo un número y alguno de los siguientes caracteres: <b>$@!%?&.-_</b></li>
                                                    <li>Una vez completados todos los campos da clic al botón <b>"Cambiar contraseña"</b>.</li>
                                                </ul>
                                                Si se completó la operación con éxito, se mostrará el siguiente mensaje: 
                                                </p>
                                                <img src="/resources/img/manuales/alumno/pass/2.jpg" width="900">
                                                <p>La próxima vez que entres al sistema deberás ingresar con tu nueva contraseña.</p>
                                                <p>En caso de que tu contraseña no coincida se mostrará el siguiente mensaje:</p>
                                                <img src="/resources/img/manuales/alumno/pass/3.jpg" width="900">
                                            </div>
                                        </div>
                                    </div>
                                    <!-- Estatus de mi solicitud -->
                                    <div class="panel panel-default">
                                        <div class="panel-heading">
                                            <h4 class="panel-title">
                                                <a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion" href="#collapse4">
                                                    <i class="fa fa-check-square-o"></i> Estatus de mi solicitud
                                                </a>
                                            </h4>
                                        </div>
                                        <div id="collapse4" class="panel-collapse collapse" style="height: 2px;">
                                            <div class="panel-body">
                                                <h4 id="encabezado4">
                                                    <a href="/misdatos/verEstatusSolicitud.action">Estatus de mi solicitud</a>
                                                    <small>(Da click en el título para ir al módulo)</small>
                                                </h4>
                                                <div class="alert alert-info">
                                                    <i class="fa fa-info-circle fa-fw fa-lg"></i>
                                                    <strong>OBJETIVO:</strong> 
                                                    Visualizar de manera específica y puntual los requisitos con los que está o no cumpliendo un alumno solicitante de beca.
                                                </div>
                                                <h4>Funcionamiento</h4>
                                                <p>
                                                    Dentro del menú principal, da clic en la opción  <b>"Estatus de mi solicitud"</b> ubicada en la sección <b>"Mi Beca"</b>. Se mostrará una pantalla con tus datos personales.
                                                </p>
                                                <img src="/resources/img/manuales/alumno/estatus/1.png" width="900">
                                                <p>En la sección <b>"Datos académicos"</b> podrás visualizar tus datos registrados dentro del sistema.</p>
                                                <p>La sección <b>"Requisitos del trámite de beca"</b> muestra cuales de estos requisitos cumples.</p>
                                                <p>En la parte inferior de esta sección encontrarás los datos de tu beca.</p>
                                                <img src="/resources/img/manuales/alumno/estatus/2.jpg" width="900">
                                                <p>En caso de no contar con una beca y que el periodo de asignaciones se encuentre abierto, se mostrará el siguiente mensaje:</p>
                                                <img src="/resources/img/manuales/alumno/estatus/3.jpg" width="841" height="430">
                                                <p>En caso de que el periodo de asignaciones no se encuentre activo, se mostrará el siguiente mensaje:  </p>
                                                <img src="/resources/img/manuales/alumno/estatus/4.jpg" width="817" height="404">
                                            </div>
                                        </div>
                                    </div>
                                    <!--Monitoreo de cuentas -->
                                    <div class="panel panel-default">
                                        <div class="panel-heading">
                                            <h4 class="panel-title">
                                                <a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion" href="#collapse6">
                                                    <i class="fa fa-bank"></i> Monitoreo de cuentas
                                                </a>
                                            </h4>
                                        </div>
                                        <div id="collapse6" class="panel-collapse collapse" style="height: 2px;">
                                            <div class="panel-body">
                                                <h4 id="encabezado6">
                                                    <a href="/tarjeta/verMonitoreoTarjetaBancaria.action">Monitoreo de cuentas</a>
                                                    <small>(Da click en el título para ir al módulo)</small>
                                                </h4>
                                                <div class="alert alert-info">
                                                    <i class="fa fa-info-circle fa-fw fa-lg"></i>
                                                    <strong>OBJETIVO:</strong> 
                                                    Visualizar el historial de cambios de estado de la tarjeta de un alumno, desde que le es asignada, hasta el momento en que es activa.
                                                </div>
                                                <h4>Funcionamiento</h4>
                                                <p>
                                                    Dentro del menú principal, da clic a la opción <b>"Monitoreo de cuentas"</b> ubicada en la sección <b>"Tarjeta bancaria"</b>. </p>
                                                <img src="/resources/img/manuales/alumno/monitoreo/1.jpg" width="900">
                                                <p>
                                                    Aquí podrás visualizar si ya te fue asignada una tarjeta y el estatus en el que se encuentra.
                                                </p>
                                                <p>
                                                    Si aún no te ha asignada alguna tarjeta, visualizarás el mensaje que se muestra en la siguiente pantalla.
                                                </p>
                                                <img src="/resources/img/manuales/alumno/monitoreo/2.jpg" width="900">
                                                <p>Si tienes una tarjeta asignada, visualizarás el número de tu tarjeta y el estatus actual de dicha tarjeta  como se muestra en la pantalla siguiente.</p>
                                                <img src="/resources/img/manuales/alumno/monitoreo/3.jpg" width="821" height="534">
                                            </div>
                                        </div>
                                    </div>
                                    <!-- Consulta de depositos  -->
                                    <div class="panel panel-default">
                                        <div class="panel-heading">
                                            <h4 class="panel-title">
                                                <a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion" href="#collapse9">
                                                    <i class="fa fa-money"></i> Consulta de depositos
                                                </a>
                                            </h4>
                                        </div>
                                        <div id="collapse9" class="panel-collapse collapse" style="height: 2px;">
                                            <div class="panel-body">
                                                <h4 id="encabezado8">
                                                    <a href="/depositos/verAdministraDepositos.action">Consula de depósitos</a>
                                                    <small>(Da click en el título para ir al módulo)</small>
                                                </h4>
                                                <div class="alert alert-info">
                                                    <i class="fa fa-info-circle fa-fw fa-lg"></i>
                                                    <strong>OBJETIVO:</strong> 
                                                    Visualizar el estado de los depósitos de beca de un alumno, para el periodo actual y para los periodos históricos.
                                                </div>
                                                <h4>Funcionamiento</h4>
                                                <p>
                                                    Dentro del menú principal, da clic en la opción  <b>"Reportar  tarjeta perdida"</b> que se encuentra en la sección <b>"Tarjeta bancaria"</b>.
                                                </p>
                                                <p>
                                                    Dentro del menú principal, da clic en la opción  <b>"Consulta de depósitos"</b> que se encuentra en la sección <b>"Depósitos"</b>. Se mostrará una pantalla con tus datos personales.
                                                </p>
                                                <img src="/resources/img/manuales/alumno/deposito/1.jpg" width="900">
                                                <p>
                                                    Dentro de la misma tabla encontrarás el detalle de los depósitos de tu beca así como la fecha en que estos fueron efectuados.
                                                </p>
                                                <p></p>En caso de no contar con ningún depósito, se mostrará el siguiente mensaje. 
                                                </p>
                                                <img src="/resources/img/manuales/alumno/deposito/2.jpg" width="900">
                                            </div>
                                        </div>
                                    </div>                                    
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</body>

<content tag="endScripts">

</content>

<content tag="inlineScripts">
    <script>
        function moverMenu(index, event) {
            event.preventDefault();
            $('html, body').animate({
                scrollTop: $("#encabezado" + index).offset().top - 80
            }, 600);
        }

        $(".irArriba").click(function (e) {
            e.preventDefault();
            $('html, body').animate({
                scrollTop: $("#cielo").offset().top - 80
            }, 600);
        });
    </script>
</content>