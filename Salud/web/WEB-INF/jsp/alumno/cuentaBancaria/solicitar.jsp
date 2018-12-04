<%-- 
    Document   : solicitar
    Created on : 22/02/2017, 03:12:17 PM
    Author     : Tania G. Sánchez
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css" />    
    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap-datepicker/css/bootstrap-datepicker.min.css" />
    <link rel="stylesheet" type="text/css" href="/vendors/kartik-bootstrap-input/fileinput.min.css"/>
    <title>Renuncia de cuenta a nombre de padre o tutor</title>
</head> 

<content tag="tituloJSP">
    Renuncia de cuenta a nombre de padre o tutor
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
                <div class="alert alert-success">
                    <i class="fa fa-check-circle fa-fw fa-lg"></i>
                    <strong>&iexcl;Realizado!</strong> <s:actionmessage />
                </div>
            </s:if>
        </div>
    </div>
    <div class="row">
        <div class="col-xs-12">
            <div class="main-box" >
                <div class="main-box-body clearfix">
                    <div class="tabs-wrapper">
                        <div class="main-box-body clearfix">
                            <%--Alumno menor de edad--%>
                            <s:if test="menorEdad">
                                <div class="alert alert-warning">
                                    <i class="fa fa-warning fa-fw fa-lg"></i>
                                    Se ha detectado que eres un alumno menor de edad, por lo que no puedes renunciar a la cuenta que tienes asociada a tu padre o tutor.
                                </div>
                            </s:if>
                            <%--Alumno mayor de edad--%>
                            <s:else>
                                <%--Con datos bancarios--%>
                                <s:if test="datosBanco">
                                    <%--Con datos de tutor--%>
                                    <s:if test="cuentaTutor">
                                        <%--Con tarjeta asociada a los datos de tutor--%>
                                        <s:if test="tarjeta">
                                            <form id="datos" class="form-horizontal" action="/misdatos/renunciaCuentaBancaria.action">
                                                <div class="col-sm-12 form-group">
                                                    Este módulo te permite renunciar a la cuenta que fue creada a nombre de tu padre o tutor con el propósito de generar una a tu nombre.
                                                </div>
                                                <div class="tab-content">
                                                    <div class="col-sm-12 form-group" style="padding-left: 35px">
                                                        <div class="checkbox-nice">
                                                            <input type="checkbox" 
                                                                   name="alumno.datosBancarios"
                                                                   id="datosBancarios" 
                                                                   required="true"
                                                                   value="check" 
                                                                   data-bv-notempty="true" 
                                                                   data-bv-notempty-message="Debes aceptar los términos para continuar.">
                                                            <label for="datosBancarios">
                                                                Entiendo que con esta acción los depósitos de beca dejarán de ser recibidos en la cuenta <br>
                                                                de mi padre o tutor. Así mismo, solicito el alta de una nueva cuenta bancaria a mi nombre <br>
                                                                debido a que ya cuento con mi credencial del INE.
                                                            </label>
                                                        </div>
                                                    </div>
                                                </div>
                                                <%--Si la tarjeta esta en tramite no le permite renunciar a ella--%>
                                                <s:if test="!enTramite">
                                                    <div class="form-group">
                                                        <div id="actualiza" class="col-sm-11">
                                                            <button id="actualizar-button" type="submit" class="btn btn-primary pull-right">Renunciar a cuenta del padre o tutor</button>
                                                        </div>
                                                    </div> 
                                                </s:if>
                                            </form>
                                        </s:if>
                                        <%--Sin tarjeta asociada a los datos de tutor--%>
                                        <s:else>
                                            <div class="alert alert-warning">
                                                <i class="fa fa-warning fa-fw fa-lg"></i>
                                                La solicitud de tu tarjeta aún no ha sido procesada, por lo que no es posible continuar con el proceso de renuncia.
                                            </div>
                                        </s:else>
                                    </s:if>
                                    <%--Sin datos de tutor--%>
                                    <s:else>
                                        <div class="alert alert-warning">
                                            <i class="fa fa-warning fa-fw fa-lg"></i>
                                            Este módulo permite renunciar a una cuenta creada a nombre del padre o tutor, por lo que en tu situación, no es necesario que realices ningún movimiento.
                                        </div>
                                    </s:else>
                                </s:if>
                                <%--Sin datos bancarios--%>
                                <s:else>
                                    <div class="alert alert-warning">
                                        <i class="fa fa-warning fa-fw fa-lg"></i>
                                        Actualmente no cuentas con datos bancarios por lo que no puedes hacer uso de este módulo, te sugerimos actualizar tus datos bancarios en opción de Datos Personales.
                                    </div>
                                </s:else>
                            </s:else>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
<content tag="endScripts">        
    <script type="text/javascript" src="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.js"></script>        
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>
    <script type="text/javascript" src="/vendors/bootstrap-datepicker/js/bootstrap-datepicker.min.js"></script>
    <script type="text/javascript" src="/vendors/bootstrap-datepicker/locales/bootstrap-datepicker.es.min.js"></script>   
</content>

<content tag="inlineScripts">
    <script>
        $(document).ready(function () {
            $('#datos').bootstrapValidator({
                excluded: [':disabled'],
            }).on('success.form.bv', function (e) {
                e.preventDefault();
                actualizarDialog();
            });
        });

        function actualizarDialog() {
            ModalGenerator.notificacion({
                "titulo": "Atención",
                "cuerpo": "¿Estás seguro que deseas renunciar a la cuenta de tu padre o tutor?",
                "tipo": "WARNING",
                "funcionAceptar": function () {
                    document.getElementById('datos').submit();
                },
                "funcionCancelar": function () {
                    $('#actualizar-button').blur();
                },
            });
        }
    </script>
</content>