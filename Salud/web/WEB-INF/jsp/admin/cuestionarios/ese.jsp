<%-- 
    Document   : ese
    Created on : 1/08/2016, 05:38:24 PM
    Author     : Tania G. Sánchez
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>    
    <style>
        .icon-intrucc {
            position: absolute; 
            top: 50%; 
            margin-top: -17px;        
        }
        .para-instrucc {
            display:block; 
            margin-left: 30px;
        }
    </style>
    <link rel="stylesheet" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css" type="text/css" />    

    <title>Solicitud Ordinaria</title>

</head> 

<content tag="tituloJSP">
    Solicitud Ordinaria
</content>

<body>
    <!-- Mensajes -->
    <div class="row">
        <div class="col-xs-12">
            <s:if test="hasActionErrors()">
                <div class="alert alert-danger">
                    <i class="fa fa-times-circle fa-fw fa-lg"></i>
                    <strong>&iexcl;Error!</strong> <s:actionerror/>
                </div>
            </s:if>
            <s:if test="( validacionInscripcion) ">
                <div class="row">
                    <div class="col-lg-12">
                        <div class="alert alert-warning">
                            <i class="fa fa-warning fa-fw fa-lg"></i>
                            <strong>Atención</strong> Los datos que se muestran son del periodo pasado debido a que se ejecutó el proceso de "Validación de Inscripción" en este periodo.
                        </div>
                    </div>
                </div>
            </s:if>
            <s:if test="hasActionMessages()">
                <div class="alert alert-success">
                    <i class="fa fa-check-circle fa-fw fa-lg"></i>
                    <strong>&iexcl;Realizado!</strong> <s:actionmessage />
                </div>
            </s:if>
            <security:authorize ifAnyGranted="ROLE_ALUMNO">
                <s:if test="contestoCuestionario">
                    <s:if test="!contestoSalud">
                        <div class="col-md-12">
                            <div class="alert alert-warning col-sm-12">
                                Aún no has contestado tu encuesta de salud. Necesitas contestarla para poder 
                                descargar el archivo PDF de la Solicitud Ordinaria.
                                <br>Da clic en en el siguiente link 
                                <!--<a href="http://www.ebecas.ipn.mx/portal/f?p=127:3:::::BOLETA:<s:property value="alumno.boleta"/>">-->
                                <a href="/misdatos/cuestionarioCuestionarioSalud.action">
                                    Censo de Salud</a>.
                            </div>
                        </div> 
                    </s:if>
                    <div class="col-md-12">
                        <div class="alert alert-warning col-sm-8">
                            Deberás entregar la Solicitud Ordinaria y la carta compromiso impresos en tu Unidad Académica.
                        </div>
                        <div class="col-sm-4 btn-group-vertical">
                            <s:if test="contestoSalud">
                                <a href="#" target='_blank' onclick="addURL(this, 1, <s:property value="alumno.id"/>)" id="ese" class="btn btn btn-default"><span class="fa fa-download"></span> Descargar archivo PDFf</a>
                            </s:if>
                            <s:else>
                                <a class="btn btn-default" id="btn-msj">Descargar archivo PDF</a>
                            </s:else>
                            <a href="#" target='_blank' onclick="addURL(this, 2, <s:property value="alumno.id"/>)" id="cartaCompromiso" class="btn btn btn-default"><span class="fa fa-download"></span> Carta Compromiso</a>
                        </div>
                    </div>
                </s:if>
                <s:else>
                    <div class="alert alert-warning col-sm-12">
                        El alumno no ha finalizado el estudio socioeconómico.
                    </div>
                </s:else>            
            </security:authorize>
            <security:authorize ifAnyGranted="ROLE_JEFEBECAS, ROLE_ANALISTABECAS">
                <div class="col-sm-3 pull-right">
                    <a href="#" target='_blank' onclick="addURL(this, 1, <s:property value="alumno.id"/>)" id="ese" class="btn btn btn-default"><span class="fa fa-download"></span> Descargar archivo PDF</a>
                </div>	
                <div class="clearfix" >&nbsp;</div>
                <div class="clearfix" >&nbsp;</div>
            </security:authorize>
        </div>
    </div> <!-- Terminan Mensajes -->

    <!-- Tabs -->
    <div class="row">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <form id="cuestionario" class="form-horizontal">                    
                    <div class="tabs-wrapper profile-tabs">
                        <ul class="nav nav-tabs" id="navBar"></ul>                        
                        <div class="tab-content" id="contenido-tab">
                            <h2 id="seccion">Características de la vivienda</h2>
                        </div>
                    </div>
                </form>  
            </div>
        </div>
    </div>
    <!--Termina ejemplo Tabs -->
</body>

<content tag="endScripts">
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>
    <script type="text/javascript" src="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>
                        $(document).ready(function () {
        <s:set var="idPregunta" value="" />
        <s:set var="idPreguntaCurrent" value="" />
                            var idSeccionCurrent = "";
                            var idSeccion = "";
                            var counter = 0;
                            var idPreguntaCurrent = "";
        <s:iterator var="ru" value="respuestasUsuario">
                            idSeccionCurrent = <s:property  value="seccion.id" />;
                            if (idSeccion !== idSeccionCurrent) {
                                // Se asigna el id de la sección a una variable.
                                idSeccion = <s:property  value="seccion.id" />;
                                // Se asigna el nombre de la sección a una variable.
                                var nombreNavBar = "<s:property  value="seccion.nombre" />";
                                // Creamos el código html para la lista de las secciones
                                agregarImagenesTabs(idSeccion, nombreNavBar);
                                // Creamos los div para cada una de las secciones
                                agregarDivsTabs(counter, idSeccion);
                                counter++;
                            }
                            if (counter > 0) {
                                var preguntaId = <s:property  value="#ru.pregunta.id" />;
                                var preguntaNombre = "<s:property  value="pregunta.nombre"  escape="false"/>";
                                var idTipoPregunta = <s:property  value="pregunta.cuestionarioPreguntaTipo.id" />;
                                var respuesta = "<s:property  value="respuesta.nombre"  escape="false"/>";
                                var respuestaAbierta = "<s:property  value="respuestaAbierta"  escape="false"/>";
                                var formText = crearForm(preguntaId, preguntaNombre, idTipoPregunta, respuesta, respuestaAbierta, idPreguntaCurrent);
                                idPreguntaCurrent = <s:property  value="#ru.pregunta.id" />;
                                var seccionString = idSeccion + "-tab";
                                $('#' + seccionString).append(formText);
                                $('#contenido-tab').append("</div>");
                            }
        </s:iterator>

                            $("#navBar li").click(function () {
                                switch ($(this).attr('id')) {
                                    case "1":
                                        $("#casa").attr("src", "/resources/img/ese/casa-active.png");
                                        $("#familia").attr("src", "/resources/img/ese/familia.png");
                                        $("#transporte").attr("src", "/resources/img/ese/transporte.png");
                                        $("#ambiente").attr("src", "/resources/img/ese/ambiente.png");
                                        $("#beca").attr("src", "/resources/img/ese/beca.png");
                                        $("#seccion").html($('span:first', this).text());
                                        break;
                                    case "2":
                                        $("#casa").attr("src", "/resources/img/ese/casa.png");
                                        $("#familia").attr("src", "/resources/img/ese/familia-active.png");
                                        $("#transporte").attr("src", "/resources/img/ese/transporte.png");
                                        $("#ambiente").attr("src", "/resources/img/ese/ambiente.png");
                                        $("#beca").attr("src", "/resources/img/ese/beca.png");
                                        $("#seccion").html($('span:first', this).text());
                                        break;
                                    case "3":
                                        $("#casa").attr("src", "/resources/img/ese/casa.png");
                                        $("#familia").attr("src", "/resources/img/ese/familia.png");
                                        $("#transporte").attr("src", "/resources/img/ese/transporte-active.png");
                                        $("#ambiente").attr("src", "/resources/img/ese/ambiente.png");
                                        $("#beca").attr("src", "/resources/img/ese/beca.png");
                                        $("#seccion").html($('span:first', this).text());
                                        break;
                                    case "4":
                                        $("#casa").attr("src", "/resources/img/ese/casa.png");
                                        $("#familia").attr("src", "/resources/img/ese/familia.png");
                                        $("#transporte").attr("src", "/resources/img/ese/transporte.png");
                                        $("#ambiente").attr("src", "/resources/img/ese/ambiente-active.png");
                                        $("#beca").attr("src", "/resources/img/ese/beca.png");
                                        $("#seccion").html($('span:first', this).text());
                                        break;
                                    case "5":
                                        $("#casa").attr("src", "/resources/img/ese/casa.png");
                                        $("#familia").attr("src", "/resources/img/ese/familia.png");
                                        $("#transporte").attr("src", "/resources/img/ese/transporte.png");
                                        $("#ambiente").attr("src", "/resources/img/ese/ambiente.png");
                                        $("#beca").attr("src", "/resources/img/ese/beca-active.png");
                                        $("#seccion").html($('span:first', this).text());
                                        break;
                                }
                            });
                            $('#cuestionario').bootstrapValidator({
                                excluded: [':disabled'],
                            }).on('success.form.bv', function (e) {
                                e.preventDefault();
                                finalizarReg();
                            }).on('status.field.bv', function (e, data) {
                                var $form = $(e.target),
                                        validator = data.bv,
                                        $tabPane = data.element.parents('.tab-pane'),
                                        tabId = $tabPane.attr('id');
                                if (tabId) {
                                    var $icon = $('a[href="#' + tabId + '"][data-toggle="tab"]').parent().find('i');

                                    if (data.status == validator.STATUS_INVALID) {
                                        $icon.removeClass('fa-check').addClass('fa-times');
                                    } else if (data.status == validator.STATUS_VALID) {
                                        var isValidTab = validator.isValidContainer($tabPane);
                                        $icon.removeClass('fa-check fa-times').addClass(isValidTab ? 'fa-check' : 'fa-times');
                                    }
                                }
                            });

                            $('#btn-msj').on('click', function () {
                                mensajeSalud();
                            });
                        });

                        function agregarImagenesTabs(idSeccion, nombreNavBar) {
                            switch (idSeccion) {
                                case 1:
                                    var txtNavBar = "<li class='active' id='1'><a href=\"#" + idSeccion + "-tab\" data-toggle=\"tab\"><span class='visible-xs'>" + nombreNavBar + " </span><img id='casa' src='/resources/img/ese/casa-active.png' class='hidden-xs'><i class=\"fa\"></i></a></li>";
                                    break;
                                case 2:
                                    var txtNavBar = "<li id='2'><a href=\"#" + idSeccion + "-tab\" data-toggle=\"tab\"><span class='visible-xs'>" + nombreNavBar + " </span><img id='familia' src='/resources/img/ese/familia.png' class='hidden-xs'><i class=\"fa\"></i></a></li>";
                                    break;
                                case 3:
                                    var txtNavBar = "<li id='3'><a href=\"#" + idSeccion + "-tab\" data-toggle=\"tab\"><span class='visible-xs'>" + nombreNavBar + " </span><img id='transporte' src='/resources/img/ese/transporte.png' class='hidden-xs'><i class=\"fa\"></i></a></li>";
                                    break;
                                case 4:
                                    var txtNavBar = "<li id='4'><a href=\"#" + idSeccion + "-tab\" data-toggle=\"tab\"><span class='visible-xs'>" + nombreNavBar + " </span><img id='ambiente' src='/resources/img/ese/ambiente.png' class='hidden-xs'><i class=\"fa\"></i></a></li>";
                                    break;
                                case 5:
                                    var txtNavBar = "<li id='5'><a href=\"#" + idSeccion + "-tab\" data-toggle=\"tab\"><span class='visible-xs'>" + nombreNavBar + " </span><img id='beca' src='/resources/img/ese/beca.png' class='hidden-xs'><i class=\"fa\"></i></a></li>";
                                    break;
                            }
                            // Agregamos los elementos de las listas al elemento ul
                            $("#navBar").append(txtNavBar);
                        }
                        function agregarDivsTabs(tabActivo, idSeccion) {
                            $('#contenido-tab').append("<div class=\"tab-pane fade" + (tabActivo === 0 ? "content active\"" : "\"") + " id=\"" + idSeccion + "-tab\">");
                        }

                        function mensajeSalud() {
                            ModalGenerator.notificacion({
                                "titulo": "Atención",
                                "cuerpo": "Aún no has contestado tu encuesta de salud. Necesitas contestarla para poder descargar el archivo PDF de tu Solicitud Ordinaria.",
                                "tipo": "WARNING",
                                "sePuedeCerrar": false
                            });
                        }

                        function crearForm(preguntaId, preguntaNombre, idTipoPregunta, respuesta, respuestaAbierta, idPreguntaCurrent) {
                            var preguntaString = (preguntaNombre.replace("<strong>", "")).replace("</strong>", "")
                            var cadena = "";
                            cadena += "<div class='form-group col-sm-12'>"
                            if (idTipoPregunta === 1) {
                                var testSinSelectA = preguntaString.includes("El (los) medio(s) de");
                                var testSinSelectB = preguntaString.includes("El gasto mensual que generan");
                                if (testSinSelectA || testSinSelectB) {
                                    if (testSinSelectB)
                                        var posicionBr = preguntaString.indexOf("<br/> ");
                                    else
                                        var posicionBr = preguntaString.indexOf("</br>");
                                    cadena += "<label class=\"col-xs-12\" style=\"padding-left: 20px;\">" + preguntaString.substr(0, posicionBr) + "</label>";
                                    cadena += "<label class=\"col-sm-5 control-label\" id=\"l_" + preguntaId + "\">" + preguntaString.substr(posicionBr + 5) + "</label>";
                                    cadena += "<div class='col-sm-7'>";
                                    cadena += "<input type='text' class='form-control' value='" + respuesta + "' disabled>";
                                    cadena += "</div>";
                                } else {
                                    if (preguntaId !== 13) {
                                        cadena += "<label class=\"col-sm-5 control-label\" id=\"l_" + preguntaId + "\">" + preguntaString + "</label>";
                                        cadena += "<div class='col-sm-7'>";
                                        cadena += "<input type='text' class='form-control' value='" + respuesta + "' disabled>";
                                        cadena += "</div>";
                                    }
                                }
                            } else if (idTipoPregunta === 2) {
                                if (idPreguntaCurrent === preguntaId) {
                                    cadena += "<label class=\"col-sm-5 control-label\" id=\"l_" + preguntaId + "\"></label>";
                                } else {
                                    cadena += "<label class=\"col-sm-5 control-label\" id=\"l_" + preguntaId + "\">" + preguntaString + "</label>";
                                }
                                cadena += "<div class='col-sm-7'>";
                                cadena += "<input type='text' class='form-control' value='" + respuesta + "' disabled>";
                                cadena += "</div>";
                            } else if (idTipoPregunta === 3) {
                                cadena += "<label class=\"col-sm-5 control-label\" id=\"l_" + preguntaId + "\">" + preguntaString + "</label>";
                                cadena += "<div class='col-sm-7'>";
                                cadena += "<input type='text' class='form-control' value='" + respuestaAbierta + "' disabled>";
                                cadena += "</div>";
                            } else if (idTipoPregunta === 4) {          //Preguntas numerias con 2 decimales
                                cadena += "<label class=\"col-xs-3 control-label\" id=\"l_" + preguntaId + "\">" + preguntaString + "</label>";
                                cadena += "<div class='col-sm-9'>";
                                cadena += "<input data-bv-notempty=\"true\"  value='" + respuestaAbierta + "' data-bv-notempty-message=\"Es necesario completar este campo\" type='number' min='0' step='.01' class='form-control condecimal' onkeyup='salariosMinimos();' name=\"T" + preguntaId + "\" id=\"" + preguntaId + "\">";
                                cadena += "</div>";
                            } else if (idTipoPregunta === 5) {          // Preguntas numericas sin decimales
                                cadena += "<label class=\"col-xs-3 control-label\" id=\"l_" + preguntaId + "\">" + preguntaString + "</label>";
                                cadena += "<div class='col-sm-9'>";
                                cadena += "<input data-bv-notempty=\"true\"  value='" + respuestaAbierta + "' data-bv-notempty-message=\"Es necesario completar este campo\" type='number' min='0' class='form-control sindecimal' onkeyup='salariosMinimos();' name=\"T" + preguntaId + "\" id=\"" + preguntaId + "\">";
                                cadena += "</div>";
                            }//Paso 5 - Selección de Beca
                            else if (idTipoPregunta === 0) {
                                //Ingresos por persona
                                cadena += "<label class=\"col-sm-5 control-label\" id=\"l\">" + "Ingresos por persona en tu familia:" + "</label>";
                                cadena += "<div class='col-sm-7'>";
                                cadena += "<input type='text' class='form-control' value='" + <s:property value="ingresoPorPersona"/> + "' disabled>";
                                cadena += "</div>";
                                cadena += "<div class='clearfix'>&nbsp;</div>";
                                //Programa Beca
                                cadena += "<label class=\"col-sm-5 control-label\" id=\"l_" + preguntaId + "\">" + preguntaString + "</label>";
                                cadena += "<div class='col-sm-7'>";
                                cadena += "<input type='text' class='form-control' value='" + respuesta + "' disabled>";
                                cadena += "</div>";
                                //Checked transferencia 
                                cadena += "<div class='clearfix'>&nbsp;</div><div class='clearfix'>&nbsp;</div>";
                                cadena += "<div class='col-sm-12 form-group' style='padding-left: 35px'>";
                                cadena += "<div class='checkbox-nice'>";
                                cadena += "<input type='checkbox' name='-1' id='-1' value='check' disabled "
        <s:if test="transferencia == 1">
                                cadena += "checked"
        </s:if>
                                cadena += ">";
                                cadena += "<label for='-1'>";
                                cadena += "Acepto que si mis condiciones académicas lo permiten, y no logro cumplir las características</br>";
                                cadena += "de la beca que solicité, me sea otorgada alguna otra beca en función del presupuesto dispuesto";
                                cadena += "</label>";
                                cadena += "</div>";
                                cadena += "</div>";
                            }
                            cadena += "</div>";
                            return cadena;
                        }

                        function addURL(element, op, id) {
                            $("#ese").blur();// Evita que el botón sea desactivado    
                            $("#cartaCompromiso").blur();// Evita que el botón sea desactivado
                            $(element).attr('href', function () {
                                if (op === 1) {
                                    return "/misdatos/descargar/pdfEstudioSocioeconomico.action?cuestionarioId=1&alumnoId=" + id;
                                } else if (op === 2) {
                                    return "/misdatos/descargar/cartaCompromisoDocumentos.action?alumnoId=" + id;
                                }
                            });
                        }
    </script>
</content>