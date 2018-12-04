<%-- 
    Document   : configuracion
    Created on : 10/10/2017, 11:56:24 AM
    Author     : Tania G. Sánchez Manilla
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<head>
    <title>Configuración de Cuenta</title>
    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css" />
    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap-datepicker/css/bootstrap-datepicker.min.css" />
    <link rel="stylesheet" type="text/css" href="/vendors/Notifications/css/ns-default.min.css" />
    <link rel="stylesheet" type="text/css" href="/vendors/Notifications/css/ns-style-bar.css" />
    <link rel="stylesheet" type="text/css" href="/vendors/Notifications/css/ns-style-theme.min.css" /> 
    <style>
        .btn-space {
            margin-right: 5px;
        }

        /* Efecto de pulsación en los iconos que se desplegan al ser > 0 */
        .pulse-button {
            display: inline-block;
            border: none;
            padding: 0.2em 0.2em;
            background-color: rgba(255, 255, 255, 1);
            box-shadow: 0 0 0 0 rgba(255, 255, 255, .7);
            border-radius:80%;
            cursor: pointer;
            -webkit-animation: pulse 1s infinite cubic-bezier(0.88, 0, 0, 1);
            -moz-animation: pulse 1s infinite cubic-bezier(0.88, 0, 0, 1);
            -ms-animation: pulse 1s infinite cubic-bezier(0.88, 0, 0, 1);
            animation: pulse 5s infinite cubic-bezier(0.88, 0, 0, 1);
        }

        @-webkit-keyframes pulse {to {box-shadow: 0 0 0 85px rgba(255, 255, 255, 0);}}
        @-moz-keyframes pulse {to {box-shadow: 0 0 0 85px rgba(255, 255, 255, 0);}}
        @-ms-keyframes pulse {to {box-shadow: 0 0 0 85px rgba(255, 255, 255, 0);}}
        @keyframes pulse {to {box-shadow: 0 0 50px 30px rgba(255, 255, 255, 0);}}
    </style>
</head> 
<content tag="tituloJSP"> Configuración de Cuenta </content>
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
    <div class="row">
        <!--Los alumnos pueden hacer uso del modulo mientras el periodo este activo.
            Los administrativos pueden usarlo siempre.-->
        <s:if test="periodoActivo">
            <!--Validamos si la cuenta fue creada por el IPN.-->
            <s:if test="cuentaBanamexIPN">
                <!--Si la cuenta es Banamex creada por el IPN no le dejará utilizar el modulo al alumno.-->

                <%--2018-01-08: Se solicita que el alumno sea capaz de dar de baja una cuenta creada a solicitud del IPN 
                <security:authorize ifAnyGranted="ROLE_ALUMNO">
                    <div class="alert alert-info">Ya tienes una cuenta Banamex. Si necesitas darla de baja acude con tu analista.</div>
                </security:authorize>
                <security:authorize ifNotGranted="ROLE_ALUMNO">--%>
                <div class="alert alert-info">Recuerda que esta es una cuenta Banamex. Si deseas puedes darla de baja aunque no es recomendable.</div>
                <div class="col-xs-12">
                    <div class="main-box clearfix" >
                        <div class="main-box-body ">
                            <div>
                                <div class="col-sm-12">
                                    <h4>Esta es la información relacionada a tu cuenta bancaria o cuenta CLABE.</h4>
                                    <p style="text-align: justify;">Si ya tienes una cuenta bancaria registrada en el SIBec por haber sido 
                                        becario en el periodo escolar de febrero a junio de 2017, <b>no debes</b> de 
                                        capturar una nueva cuenta bancaria.</p>
                                    <p style="text-align: justify;">En caso de que necesites dar de baja ésta cuenta, ingresa en el campo 
                                        correspondiente tu contraseña de acceso al SIBec, y una vez que la
                                        verifiques con el botón "Validar contraseña", podrás darla de baja 
                                        danco clic en "Dar de baja cuenta".</p>
                                    <p>&nbsp;</p>

                                </div>
                            </div>
                            <security:authorize ifAnyGranted="ROLE_ALUMNO">
                                <form id="bajaCuenta" name="bajaCuenta" action="/misdatos/bajaCuentaBancaria.action" method="post" class="form-horizontal">
                                </security:authorize>
                                <security:authorize ifNotGranted="ROLE_ALUMNO">
                                    <form id="bajaCuenta" name="bajaCuenta" action="/admin/bajaCuentaBancaria.action?numeroDeBoleta=<s:property value="numeroDeBoleta"/>" method="post" class="form-horizontal">
                                    </security:authorize>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">Número</label>
                                        <div class="col-sm-8">
                                            <s:textfield cssClass="form-control" name="tarjetaAsteriscos" id="tarjetaAsteriscos" placeholder="Número" readonly="true"/>
                                        </div>
                                    </div>
                                    <s:if test="tarjetaBancaria.tarjetaBancaria.cuenta == 1">
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label">Tipo</label>
                                            <div class="col-sm-8">
                                                <s:textfield cssClass="form-control" name="tipoCuenta" id="tipoCuenta" placeholder="Tipo de cuenta" readonly="true" value="Cuenta"/>
                                            </div>
                                        </div>
                                    </s:if>
                                    <s:elseif test="tarjetaBancaria.tarjetaBancaria.cuenta == 3">
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label">Tipo</label>
                                            <div class="col-sm-8">
                                                <s:textfield cssClass="form-control" name="tipoCuenta" id="tipoCuenta" placeholder="Tipo de cuenta" readonly="true" value="Clabe"/>
                                            </div>
                                        </div>
                                    </s:elseif>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">Banco</label>
                                        <div class="col-sm-8">
                                            <s:textfield cssClass="form-control" name="banco" id="banco" placeholder="Banco" readonly="true"/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">Contraseña</label>
                                        <div class="col-sm-8">
                                            <input type="password" class="form-control" placeholder="Contraseña" 
                                                   id="contraseñaAlumno" name="contraseñaAlumno" maxlength="15" 
                                                   data-bv-notempty="true"
                                                   data-bv-notempty-message="Debes introducir una contraseña"
                                                   id="j_spring_security_check_j_password"
                                                   ondragstart="return false" onselectstart="return false"/>
                                            <label class="control-label">La contraseña que debes colocar es con la que inicias sesión en el SIBec.</label>
                                        </div>
                                    </div>
                                    <div class="form-group  pull-right">
                                        <div class="col-sm-12 button-group">
                                            <button name="darBaja" id="darBaja" disabled type="button" class="btn btn-primary pull-right">Dar de baja cuenta</button>
                                            <button name="validarPass" type="button" class="btn btn-primary btn-space" onclick="contraseñaCorrecta(1)">Validar contraseña</button>
                                        </div>
                                    </div>
                                </form>
                        </div>
                    </div>
                </div>
                <%--</security:authorize>--%>
            </s:if>
            <!--Le permite dar de baja la cuenta.-->
            <s:else>
                <!--Datos de lectura, permite dar de de baja la cuenta.-->
                <s:if test="cuentaLiberada">
                    <div class="col-xs-12">
                        <div class="main-box clearfix" >
                            <div class="main-box-body ">
                                <div>
                                    <div class="col-sm-12">
                                        <h4>Esta es la información relacionada a tu cuenta bancaria o cuenta CLABE.</h4>
                                        <p style="text-align: justify;">Si ya tienes una cuenta bancaria registrada en el SIBec por haber sido 
                                            becario en el periodo escolar de febrero a junio de 2017, <b>no debes</b> de 
                                            capturar una nueva cuenta bancaria.</p>
                                        <p style="text-align: justify;">En caso de que necesites dar de baja ésta cuenta, ingresa en el campo 
                                            correspondiente tu contraseña de acceso al SIBec, y una vez que la
                                            verifiques con el botón "Validar contraseña", podrás darla de baja 
                                            danco clic en "Dar de baja cuenta".</p>
                                        <p>&nbsp;</p>
                                    </div>
                                </div>
                                <security:authorize ifAnyGranted="ROLE_ALUMNO">
                                    <form id="bajaCuenta" name="bajaCuenta" action="/misdatos/bajaCuentaBancaria.action" method="post" class="form-horizontal">
                                    </security:authorize>
                                    <security:authorize ifNotGranted="ROLE_ALUMNO">
                                        <form id="bajaCuenta" name="bajaCuenta" action="/admin/bajaCuentaBancaria.action?numeroDeBoleta=<s:property value="numeroDeBoleta"/>" method="post" class="form-horizontal">
                                        </security:authorize>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label">Número</label>
                                            <div class="col-sm-8">
                                                <s:textfield cssClass="form-control" name="tarjetaAsteriscos" id="tarjetaAsteriscos" placeholder="Número" readonly="true"/>
                                            </div>
                                        </div>
                                        <s:if test="tarjetaBancaria.tarjetaBancaria.cuenta == 1">
                                            <div class="form-group">
                                                <label class="col-sm-2 control-label">Tipo</label>
                                                <div class="col-sm-8">
                                                    <s:textfield cssClass="form-control" name="tipoCuenta" id="tipoCuenta" placeholder="Tipo de cuenta" readonly="true" value="Cuenta"/>
                                                </div>
                                            </div>
                                        </s:if>
                                        <s:elseif test="tarjetaBancaria.tarjetaBancaria.cuenta == 3">
                                            <div class="form-group">
                                                <label class="col-sm-2 control-label">Tipo</label>
                                                <div class="col-sm-8">
                                                    <s:textfield cssClass="form-control" name="tipoCuenta" id="tipoCuenta" placeholder="Tipo de cuenta" readonly="true" value="Clabe"/>
                                                </div>
                                            </div>
                                        </s:elseif>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label">Banco</label>
                                            <div class="col-sm-8">
                                                <s:textfield cssClass="form-control" name="banco" id="banco" placeholder="Banco" readonly="true"/>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label">Contraseña</label>
                                            <div class="col-sm-8">
                                                <input type="password" class="form-control" placeholder="Contraseña" 
                                                       id="contraseñaAlumno" name="contraseñaAlumno" maxlength="15" 
                                                       data-bv-notempty="true"
                                                       data-bv-notempty-message="Debes introducir una contraseña"
                                                       id="j_spring_security_check_j_password"
                                                       ondragstart="return false" onselectstart="return false"/>
                                                <label class="control-label">La contraseña que debes colocar es con la que inicias sesión en el SIBec.</label>
                                            </div>
                                        </div>
                                        <div class="form-group pull-right">
                                            <div class="col-sm-12 btn-group pull-right">
                                                <button name="darBaja" id="darBaja" disabled type="button" class="btn btn-primary btn-space">Dar de baja cuenta</button>
                                                <button name="validarPass" type="button" class="btn btn-primary" onclick="contraseñaCorrecta(1)">Validar contraseña</button>
                                            </div>
                                        </div>
                                    </form>
                            </div>
                        </div>
                    </div>
                </s:if>
                <!--Permite completar datos y guardar.-->
                <s:else>
                    <div class="alert alert-info">
                        <i class="fa fa-info-circle fa-fw fa-lg"></i>
                        Ingresa los 11 dígitos de tu cuenta BANAMEX o los 18 dígitos de tu cuenta CLABE (en caso de que el banco sea diferente a BANAMEX).
                    </div>
                    <div class="col-xs-12">
                        <div class="main-box clearfix" >
                            <div class="main-box-body ">
                                <security:authorize ifAnyGranted="ROLE_ALUMNO">
                                    <form id="guardar" name="guardar" action="/misdatos/guardarCuentaBancaria.action" method="post" class="form-horizontal">
                                    </security:authorize>
                                    <security:authorize ifNotGranted="ROLE_ALUMNO">
                                        <form id="guardar" name="guardar" action="/admin/guardarCuentaBancaria.action?numeroDeBoleta=<s:property value="numeroDeBoleta"/>" method="post" class="form-horizontal">
                                        </security:authorize>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label">Número</label>
                                            <div class="col-sm-8">
                                                <s:textfield cssClass="form-control" name="numero" id="numero" placeholder="Número"
                                                             onchange="datosNumero();"
                                                             data-bv-message="Este dato no es válido"
                                                             required="true" 
                                                             data-bv-notempty="true"
                                                             data-bv-notempty-message="El número es requerido."
                                                             pattern="^[0-9]+$" 
                                                             data-bv-regexp-message="Tu número debe estar conformado sólo por números."
                                                             data-bv-stringlength="true" 
                                                             data-bv-stringlength-min="11" 
                                                             data-bv-stringlength-max="18" 
                                                             data-bv-stringlength-message="Tu número debe ser de 11 o 18 dígitos." />
                                                <span class="help-block" id="nombreMessage" />
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label">Tipo</label>
                                            <div class="col-sm-8">
                                                <s:textfield cssClass="form-control" name="tipoCuenta" id="tipoCuenta" placeholder="Tipo de cuenta" readonly="true"/>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label">Banco</label>
                                            <div class="col-sm-8">
                                                <s:textfield cssClass="form-control" name="banco" id="banco" placeholder="Banco" readonly="true"/>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label">Contraseña</label>
                                            <div class="col-sm-8">
                                                <input type="password" class="form-control" placeholder="Contraseña" 
                                                       id="contraseñaAlumno" name="contraseñaAlumno" maxlength="15" 
                                                       data-bv-notempty="true"
                                                       data-bv-notempty-message="Debes introducir una contraseña"
                                                       id="j_spring_security_check_j_password"
                                                       ondragstart="return false" onselectstart="return false"/>
                                                <label class="control-label">La contraseña que debes colocar es con la que inicias sesión en el SIBec.</label>
                                            </div>
                                        </div>
                                        <div class="col-sm-12">
                                            <p style="text-align: justify;">
                                                ​Solicito a la Dirección de Servicios Estudiantiles (D.S.E.) que realice el depósito de mi beca a la cuenta 
                                                arriba indicada. Manifiesto que los datos son correctos y soy titular de dicha cuenta o en caso de ser menor 
                                                de edad se encuentra a nombre de mi madre, padre o tutor.<br>
                                                <br>
                                                Asumo la responsabilidad de verificar la autenticidad de información proporcionada a la D.S.E. en la Institución
                                                Bancaria con la cual celebre el contrato de apertura de cuenta.<br>
                                                <br>
                                                Deslindo a la D.S.E. y a sus funcionarios de cualquier responsabilidad relacionada o resultante por el envío de
                                                pagos a la cuenta proporcionada por un servidor, comprometiéndome a sacar a salvo y sin afectación monetaria o
                                                administrativa a las entidades antes mencionadas.
                                            </p>
                                            <div class="checkbox-nice checkbox">
                                                <input data-bv-notempty="true" 
                                                       data-bv-notempty-message="Tienes que aceptar para poder continuar" 
                                                       type="checkbox" name="checkbox" value="check" id="checkbox">
                                                <label for="checkbox">Sí acepto</label>
                                            </div>
                                        </div>
                                        <div class="btn-group pull-right">
                                            <button name="validarPass" type="button" class="btn btn-primary pull-right" onclick="validarDatosCompletos()">Validar contraseña</button>
                                            <button name="guardarCuenta" disabled style="margin-bottom: 10px" id="guardarBoton" type="submit" class="btn btn-primary btn-space" onclick="validarCheck()">Guardar</button>
                                            <!--<button name="guardarCuenta" disabled style="margin-bottom: 10px" id="guardarBoton" type="button" class="btn btn-primary btn-space">Guardar</button>-->
                                        </div>
                                    </form>
                            </div>
                        </div>
                    </div>
                </s:else>
            </s:else>
        </s:if>
        <!--Si el periodo no esta activo y son alumnos no permite hacer uso del modulo.-->
        <s:else>
            <div class="alert alert-info">
                <s:property value="mensaje"/>
            </div>
            <div class="col-xs-12">
                <div class="main-box clearfix">
                    <div class="main-box-body">
                        <form id="bajaCuenta" name="bajaCuenta" action="/misdatos/bajaCuentaBancaria.action" method="post" class="form-horizontal">
                            <div>
                                <div class="col-sm-12">
                                    <h4>Esta es la información relacionada a tu cuenta bancaria o cuenta CLABE.</h4>
                                    <p>&nbsp;</p>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Número</label>
                                <div class="col-sm-8">
                                    <s:textfield cssClass="form-control" name="tarjetaAsteriscos" id="tarjetaAsteriscos" placeholder="Número" readonly="true"/>
                                </div>
                            </div>
                            <s:if test="tarjetaBancaria.tarjetaBancaria.cuenta == 1">
                                <div class="form-group">
                                    <label class="col-sm-2 control-label">Tipo</label>
                                    <div class="col-sm-8">
                                        <s:textfield cssClass="form-control" name="tipoCuenta" id="tipoCuenta" placeholder="Tipo de cuenta" readonly="true" value="Cuenta"/>
                                    </div>
                                </div>
                            </s:if>
                            <s:elseif test="tarjetaBancaria.tarjetaBancaria.cuenta == 3">
                                <div class="form-group">
                                    <label class="col-sm-2 control-label">Tipo</label>
                                    <div class="col-sm-8">
                                        <s:textfield cssClass="form-control" name="tipoCuenta" id="tipoCuenta" placeholder="Tipo de cuenta" readonly="true" value="Clabe"/>
                                    </div>
                                </div>
                            </s:elseif>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Banco</label>
                                <div class="col-sm-8">
                                    <s:textfield cssClass="form-control" name="banco" id="banco" placeholder="Banco" readonly="true"/>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </s:else>
    </div>
</body>
<content tag="endScripts">
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>
    <script type="text/javascript" src="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.js"></script>
    <script type="text/javascript" src="/vendors/moment/moment.min.js"></script>
    <script src="/vendors/Notifications/js/classie.min.js" type="text/javascript"></script>
    <script src="/vendors/Notifications/js/modernizr.custom.min.js" type="text/javascript"></script>
    <script src="/vendors/Notifications/js/notificationFx.js" type="text/javascript"></script>
</content>
<content tag="inlineScripts">
    <script>
                                                $(document).ready(function () {
                                                    $("#darBaja").click(function () {
                                                        ModalGenerator.notificacion({
                                                            "titulo": "¿Estás seguro?",
                                                            "cuerpo": "Esta acción desvinculará la cuenta bancaria de tu usuario, ¿estás seguro que deseas proceder?",
                                                            "tipo": "WARNING",
                                                            "funcionAceptar": function () {
                                                                $("#bajaCuenta").submit();
                                                            }
                                                        });
                                                    });

                                                    // create the notification
                                                    var notification = new NotificationFx({
                                                        color: '#03A9F4',
                                                        icono: 'fa fa-exclamation-triangle',
                                                        nombre: 'Registro de Cuenta Bancaria',
                                                        texto: '<p style="font-size: 20px;">Si tienes dudas sobre como registrar tu cuenta bancaria, descarga el manual <a download href="/resources/downloadable/manual-registro.pdf" style="opacity:1;"><i class="fa fa-download pulse-button" style="color:#03A9F4; margin-left:.5em;"></i></a></p>',
                                                        ttl: 50000, // notice, warning or error
                                                        onClose: function () {
                                                            bttnBouncyflip.disabled = false;
                                                        }
                                                    });

                                                    // show the notification
                                                    notification.show();

                                                    // disable the button (for demo purposes only)
                                                    this.disabled = true;


                                                });
                                                function datosNumero() {
                                                    var numero = document.getElementById("numero").value;
                                                    //Se valida que el número ingresado tenga longitud de 11 o 18 dígitos.
                                                    if (numero.length === 11) {
                                                        // Es cuenta.
                                                        document.getElementById("tipoCuenta").value = "Cuenta";
                                                        document.getElementById("banco").value = "BANAMEX";
                                                        return true;
                                                    } else if (numero.length === 18) {
                                                        // Es clabe.
                                                        document.getElementById("tipoCuenta").value = "Clabe";
                                                        buscarBanco();
                                                        return true;
                                                    } else {
                                                        ModalGenerator.notificacion({
                                                            "titulo": "Datos incorrectos.",
                                                            "cuerpo": "El número que ingresaste no coincide con el formato esperado. Sólo se permiten 11 dígitos de tu cuenta BANAMEX o 18 dígitos de tu cuenta CLABE.",
                                                            "tipo": "ALERT"
                                                        });
                                                        return false;
                                                    }
                                                }

                                                function buscarBanco() {
                                                    $.ajax({
                                                        type: 'POST',
                                                        url: '/ajax/buscarBancoAjax.action?numero=' + document.getElementById("numero").value,
                                                        dataType: 'json',
                                                        cache: false,
                                                        success: function (aData) {
                                                            manipularRespuesta(aData);
                                                        },
                                                        error: function () {
                                                            console.log("Hubo un problema que impidió que se completara la operación.");
                                                        }
                                                    });
                                                }

                                                function manipularRespuesta(aData) {
                                                    if (aData.data[0] === undefined)
                                                        return null;
                                                    var element = aData.data[0].toString();
                                                    if (element === "Error") {
                                                        ModalGenerator.notificacion({
                                                            "titulo": "Datos incorrectos.",
                                                            "cuerpo": "La cuenta clabe que ingresaste no es valida. Ingresa nuevamente tu clabe.",
                                                            "tipo": "ALERT"
                                                        });
                                                        document.getElementById("numero").value = "";
                                                    } else if (element === "BANAMEX") {
                                                        ModalGenerator.notificacion({
                                                            "titulo": "Datos incorrectos.",
                                                            "cuerpo": "La cuenta clabe ingresada corresponde a Banamex. Si tu cuenta es Banamex debes ingresar el número de cuenta.",
                                                            "tipo": "ALERT"
                                                        });
                                                        document.getElementById("numero").value = "";
                                                    } else {
                                                        document.getElementById("banco").value = element;
                                                    }
                                                }

                                                function validarCheck() {
                                                    if (document.guardar.checkbox.checked) {
                                                        return true;
                                                    } else {
                                                        return false;
                                                    }
                                                }

                                                function validarDatosCompletos() {
                                                    var check = validarCheck();
                                                    var numero = datosNumero();
                                                    if (check && numero) {
                                                        contraseñaCorrecta(2);
                                                    } else {
                                                        ModalGenerator.notificacion({
                                                            'titulo': 'Datos incompletos.',
                                                            'cuerpo': 'Debes dar clic en "Sí Acepto" y completar todos los datos para continuar',
                                                            'tipo': 'INFO'
                                                        });
                                                        return false;
                                                    }
                                                }

                                                function contraseñaCorrecta(opcion) {
                                                    var pwd = encodeURIComponent($("#contraseñaAlumno").val());
                                                    $.ajax({
                                                        type: 'POST',
                                                        url: '/ajax/compararContraseñaAjax.action?pss=' + pwd,
                                                        dataType: 'json',
                                                        cache: false,
                                                        success: function (aData) {
                                                            validarContraseña(aData, opcion);
                                                        },
                                                        error: function () {
                                                            console.log("Hubo un problema que impidió que se completara la operación.");
                                                        }
                                                    });
                                                }

                                                function validarContraseña(aData, opcion) {
                                                    if (aData.data[0] === undefined)
                                                        return null;
                                                    var element = aData.data[0].toString();
                                                    if (element === "true") {
                                                        if (opcion === 1) {
                                                            document.bajaCuenta.validarPass.disabled = true;
                                                            document.bajaCuenta.darBaja.disabled = false;
                                                        } else if (opcion === 2) {
                                                            document.guardar.validarPass.disabled = true;
                                                            document.guardar.guardarCuenta.disabled = false;
                                                        }
                                                        ModalGenerator.notificacion({
                                                            "titulo": "Datos correctos.",
                                                            "cuerpo": "La constraseña introducida es correcta, ahora puedes guardar tu cuenta/clabe.",
                                                            "tipo": "SUCCESS"
                                                        });
                                                    } else {
                                                        if (opcion === 1) {
                                                            document.bajaCuenta.darBaja.disabled = false;
                                                        } else if (opcion === 2) {
                                                            document.guardar.guardarCuenta.disabled = false;
                                                        }
                                                        ModalGenerator.notificacion({
                                                            "titulo": "Datos incorrectos.",
                                                            "cuerpo": "La contraseña ingresada no coincide con la contraseña del usario en sesión.",
                                                            "tipo": "ALERT"
                                                        });
                                                    }
                                                }
    </script>
</content>
