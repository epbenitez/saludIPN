<%-- 
    Document   : formEncuesta
    Created on : 19/11/2015, 10:06:53 AM
    Author     : Tania G. Sánchez
    Redesing   : Mario Márquez
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>    
    <style>
        
        input[type=number]::-webkit-inner-spin-button, 
        input[type=number]::-webkit-outer-spin-button { 
          -webkit-appearance: none; 
          margin: 0; 
        }
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
            <s:if test="hasActionMessages()">
                <div class="alert alert-success">
                    <i class="fa fa-check-circle fa-fw fa-lg"></i>
                    <strong>&iexcl;Realizado!</strong> <s:actionmessage />
                </div>
            </s:if>
            <s:if test="periodoActivo">
                <div class="alert alert-info fade in">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                    <i class="fa fa-info-circle fa-fw fa-lg"></i>
                    El botón <strong>Guardar</strong>, te servirá para conservar los avances de tus respuestas.
                    Pero sólo hasta que oprimas el botón <strong>Finalizar</strong>, se tomará como terminada tu solicitud de beca.
                </div>
                <div class="col-xs-12">
                    <div class="panel-group accordion" id="accordion">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h4 class="panel-title">
                                    <a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
                                        Aviso de Privacidad Simplificado
                                    </a>
                                </h4>
                            </div>
                            <div id="collapseOne" class="panel-collapse collapse">
                                <div class="panel-body">
                                    <p style="text-align: justify;">El Instituto Polit&eacute;cnico Nacional, a trav&eacute;s de la 
                                        Direcci&oacute;n de Servicios Estudiantiles de conformidad a lo dispuesto en el 
                                        art&iacute;culo 53 fracci&oacute;n V del Reglamento Org&aacute;nico del 
                                        Instituto Polit&eacute;cnico Nacional, es el responsable del tratamiento de los datos personales 
                                        que nos proporciones, los cuales ser&aacute;n protegidos conforme a lo dispuesto por la 
                                        Ley General de Protecci&oacute;n de Datos Personales en Posesi&oacute;n de Sujetos Obligados, 
                                        y dem&aacute;s normatividad que resulte aplicable.</p>
                                    <p style="text-align: justify;"><strong>&iquest;Para qu&eacute; fines utilizaremos sus datos personales?</strong></p>
                                    <p style="text-align: justify;">Los datos personales que solicitamos los utilizaremos para las siguientes finalidades: 
                                        Llevar a cabo el proceso de selecci&oacute;n y otorgamiento de becas que oferta el Instituto Polit&eacute;cnico Nacional.</p>
                                    <p><strong>&iquest;Con qui&eacute;n compartimos su informaci&oacute;n personal y para qu&eacute; fines?</strong></p>
                                    <p style="text-align: justify;">Se informa que no se realizar&aacute;n transferencias de datos personales, salvo aqu&eacute;llas 
                                        que sean necesarias para atender requerimientos de informaci&oacute;n de una autoridad competente, que est&eacute;n 
                                        debidamente fundados y motivados.</p><p style="text-align: justify;">Para conocer mayor informaci&oacute;n sobre los 
                                        t&eacute;rminos y condiciones en que ser&aacute;n tratados sus datos personales, como los terceros con quienes 
                                        compartimos su informaci&oacute;n personal y la forma en que podr&aacute; ejercer sus derechos ARCO, puede consultar el 
                                        aviso de privacidad integral en: <a href="https://www.ipn.mx/dse/becas/Paginas/inicio.aspx" target="_blank"  > <strong>https://www.ipn.mx/dse/becas/Paginas/inicio.aspx</strong> </a>
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div> 
                </div>
            </s:if>
            <s:else>
                <div class="alert alert-danger">
                    <i class="fa fa-times-circle fa-fw fa-lg"></i>
                    El periodo de registro para la solicitud de beca ha finalizado. Por lo que no puedes llenar esta solicitud.
                </div>
            </s:else>
        </div>
    </div> <!-- Terminan Mensajes -->

    <!-- Tabs -->
    <div class="row">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <form id="cuestionario" class="form-horizontal">                    
                    <div class="tabs-wrapper profile-tabs">
                        <ul class="nav nav-tabs" id="navBar">
                        </ul>                        
                        <div class="tab-content" id="contenido-tab">
                            <h2 id="seccion">Características de la vivienda</h2>
                        </div>
                    </div>
                    <s:if test="periodoActivo">
                        <div class="form-group">
                            <div class="col-xs-11">
                                <div class=" pull-right">
                                    <button type="button" onclick="guardarDatos()" id="btnGuardar" class="btn btn-default">Guardar</button>
                                    &nbsp;
                                    <s:if test="estatus">
                                        <button type="submit" id="btnFinalizar" class="btn btn-default">Finalizar</button>
                                    </s:if>
                                    <s:else>
                                        <button type="button" class="btn btn-default" onclick="estatusFalse()">Finalizar</button>
                                    </s:else>
                                </div>
                            </div>
                        </div>
                    </s:if>
                </form>                                    
                <s:form action="guardarEstudioSocioeconomico.action?cuestionarioId=1" id="formGuardar" name="formGuardar" method="post">
                    <s:hidden id="hResultdos" name="hResultdos"/>
                    <s:hidden id="cuestionarioId" name="cuestionarioId"/>
                </s:form> 
                <s:form action="finalizarEstudioSocioeconomico.action?cuestionarioId=1" id="formFinalizar" name="formFinalizar" method="post">
                    <s:hidden id="hResultdos2" name="hResultdos"/>
                    <s:hidden id="cuestionarioId" name="cuestionarioId"/>
                </s:form> 
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
                                                $("#btnFinalizar").hide();

        <s:set var="idPregunta" value="" />
        <s:set var="idTipoPregunta" value="" />
        <s:set var="idPreguntaCurrent" value="" />
                                                var idSeccionCurrent = "";
                                                var idSeccion = "";
                                                var counter = 0;
                                                var rangosSalarios = new Array();
        <s:iterator var="preg" value="preguntas">
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
                                                    var preguntaId = <s:property  value="#preg.id" />;
                                                    var preguntaNombre = "<s:property  value="pregunta.nombre"  escape="false"/>";
                                                    var idTipoPregunta = <s:property  value="pregunta.cuestionarioPreguntaTipo.id" />;
                                                    var respuestas = new Array();
            <s:iterator var="resp" value="respuestas">
                                                    var respuestasInner = new Array();
                                                    respuestasInner['id'] = "<s:property  value="#resp.id" />";
                                                    respuestasInner['nombre'] = "<s:property  value="#resp.nombre" />";
                                                    respuestas.push(respuestasInner);
            </s:iterator>
                                                    var formText = crearForm(preguntaId, preguntaNombre, idTipoPregunta, respuestas, rangosSalarios);
                                                    var seccionString = idSeccion + "-tab";
                                                    $('#' + seccionString).append(formText);
                                                    $('#contenido-tab').append("</div>");
                                                }
        </s:iterator>
        <s:iterator value="respuestasUsuario">
            <s:if test="respuestaAbierta!=null">
                                                setValue('<s:property  value="pregunta.id"/>', '<s:property  value="respuestaAbierta"/>');
            </s:if>
            <s:else>
                                                selectedValueSet('<s:property  value="pregunta.id"/>', '<s:property  value="respuesta.id"/>');
            </s:else>
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
                                                }).on('error.form.bv', function (e) {
                                                    ModalGenerator.notificacion({
                                                        "titulo": "Datos incompletos",
                                                        "cuerpo": "Es necesario completar todos los campos. Por favor, verifica las preguntas marcadas en rojo.",
                                                        "tipo": "ALERT",
                                                    });
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
                                                salariosMinimos();
                                                mostrarLeyenda();

                                                //Se solicita que el botón de finalizar sólo aparezca en el Paso 5
                                                $('#navBar li').click(function () {
                                                    var id = $(this).attr('id');
                                                    $("#btnFinalizar").hide();
                                                    if (id === "5") {
                                                        $("#btnFinalizar").show();
                                                    }
                                                });
                                            });
                                            function finalizarReg() {
                                                var stringEnvia = "";
                                                var totalRespuestas = 0;
                                                var totalPreguntas = (<s:property  value="totalPreguntas" />);
                                                var discapacidad = false;
                                                $("option:selected").each(function () {
                                                    if ($(this).val() !== '') {
                                                        stringEnvia += $(this).parent()[0].id + "," + $(this)[0].value + "@";
                                                        totalRespuestas++;
                                                    }
                                                });
                                                $(':input[type="number"]').each(function () {
                                                    if ($(this).val() !== "") {
                                                        stringEnvia += $(this)[0].id + "," + $(this)[0].value + "@";
                                                        totalRespuestas++;
                                                    }
                                                });
                                                var lastCheckbox = "";
                                                $("input:checkbox:checked").each(function () {

                                                    if (lastCheckbox !== $(this)[0].name) {
                                                        stringEnvia += "@";
                                                        stringEnvia += $(this)[0].name + "," + $(this)[0].value;
                                                        totalRespuestas++;
                                                    } else {
                                                        stringEnvia += "-" + $(this)[0].value;
                                                    }
                                                    lastCheckbox = $(this)[0].name;
                                                    if ($(this)[0].name == 49)
                                                        discapacidad = true;
                                                });
                                                if (discapacidad) {
                                                    totalRespuestas = totalRespuestas - 1;
                                                }
                                                // La resta fue elevada a 3 por los dos campos vacíos nuevos
                                                if (totalRespuestas < totalPreguntas - 3) {
                                                    console.log(totalRespuestas);
                                                    console.log(totalPreguntas - 1);
                                                    ModalGenerator.notificacion({
                                                        "titulo": "Datos incompletos",
                                                        "cuerpo": "Es necesario completar todos los campos. Por favor, verifica las preguntas marcadas en rojo.",
                                                        "tipo": "ALERT",
                                                    });
                                                } else {
                                                    ModalGenerator.notificacion({
                                                        "titulo": "Atención",
                                                        "cuerpo": "¿Estas seguro de completar el formulario (Después de finalizar no se podrán cambiar las respuestas)?",
                                                        "tipo": "WARNING",
                                                        "funcionAceptar": function () {
                                                            $("#hResultdos2").val(stringEnvia);
                                                            $("#formFinalizar").submit();
                                                        }
                                                    });
                                                }
                                            }

                                            function estatusFalse() {
                                                ModalGenerator.notificacion({
                                                    "titulo": "Atención",
                                                    "cuerpo": "No se puede finalizar tu Solicitud Ordinaria porque tus datos académicos no han sido actualizados.",
                                                    "tipo": "INFO",
                                                });
                                            }

                                            function guardarDatos() {
                                                var stringEnvia = "";
                                                var lastCheckbox = "";
                                                $("option:selected").each(function () {
                                                    if ($(this).val() !== "") {
                                                        stringEnvia += $(this).parent()[0].id + "," + $(this)[0].value + "@";
                                                    }
                                                });
                                                $(':input[type="number"]').each(function () {
                                                    if ($(this).val() !== "") {
                                                        stringEnvia += $(this)[0].id + "," + $(this)[0].value + "@";
                                                    }
                                                });
                                                $("input:checkbox:checked").each(function () {
                                                    if (lastCheckbox !== $(this)[0].name) {
                                                        stringEnvia += "@";
                                                        stringEnvia += $(this)[0].name + "," + $(this)[0].value;
                                                    } else {
                                                        stringEnvia += "-" + $(this)[0].value;
                                                    }
                                                    lastCheckbox = $(this)[0].name;
                                                });
                                                console.log(stringEnvia);
                                                $("#hResultdos").val(stringEnvia);
                                                $("#formGuardar").submit();
                                            }

                                            function selectedValueSet(name, SelectedValue) {
                                                $("select[name='S" + name + "'] option[value='']").removeAttr("selected");
                                                $("select[name='S" + name + "'] option[value='" + SelectedValue + "']").attr('selected', 'selected');
                                                $('input[name="' + name + '"][value="' + SelectedValue + '"]').attr('checked', true);
                                            }
                                            function setValue(name, SelectedValue) {
                                                $("input[name='T" + name + "']").val(SelectedValue);
                                            }

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
                                                $('#contenido-tab').append("<div class=\"tab-pane fade" + (tabActivo == 0 ? "content active\"" : "\"") + " id=\"" + idSeccion + "-tab\">");
                                            }



                                            function crearForm(preguntaId, preguntaNombre, idTipoPregunta, respuestas, rangosSalarios) {
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
                                                        cadena += "<label class=\"col-sm-3 control-label\" id=\"l_" + preguntaId + "\">" + preguntaString.substr(posicionBr + 5) + "</label>";
                                                        cadena += "<div class='col-sm-9'>";
                                                        cadena += "<select required data-bv-notempty-message=\"Por favor, selecciona una opción\" class='form-control' name=\"S" + preguntaId + "\" id=\"" + preguntaId + "\">"
                                                        cadena += "<option value=\"\" selected>Seleccione una respuesta</option>"
                                                        for (var i = 0; i < respuestas.length; i++) {
                                                            cadena += "<option value=\"" + respuestas[i]['id'] + "\">" + respuestas[i]['nombre'] + "</option>";
                                                        }
                                                        cadena += "</select>"
                                                        cadena += "</div>";
                                                    } else {
                                                        if (preguntaId !== 13) {
                                                            cadena += "<label class=\"col-sm-3 control-label\" id=\"l_" + preguntaId + "\">" + preguntaString + "</label>";
                                                            cadena += "<div class='col-sm-9'>";
                                                            cadena += "<select required data-bv-notempty-message=\"Por favor, selecciona una opción\" class='form-control' name=\"S" + preguntaId + "\" id=\"" + preguntaId + "\">"
                                                            cadena += "<option value=\"\" selected>Seleccione una respuesta</option>"
                                                            for (var i = 0; i < respuestas.length; i++) {
                                                                cadena += "<option value=\"" + respuestas[i]['id'] + "\">" + respuestas[i]['nombre'] + "</option>";
                                                            }
                                                            cadena += "</select>";
                                                            cadena += "</div>";
                                                        }
                                                    }
                                                } else if (idTipoPregunta === 2) {
                                                    cadena += "<p class=\"col-xs-10 pull-left\" style=\"padding-left: 20px;\">" + preguntaString.replace("</br>", " ") + "</p>";
                                                    for (var i = 0; i < respuestas.length; i++) {
                                                        cadena += "<div class=\"checkbox-nice checkbox col-sm-3 col-sm-offset-1 col-xs-5 col-xs-offset-1\" >";
                                                        if (i == 0)
                                                            cadena += "<input data-bv-notempty=\"true\" data-bv-notempty-message=\"Por favor, selecciona una opción\" type='checkbox' name=\"" + preguntaId + "\" value=\"" + respuestas[i]['id'] + "\" id=\"" + respuestas[i]['id'] + "\">";
                                                        else
                                                            cadena += "<input type='checkbox' name=\"" + preguntaId + "\" value=\"" + respuestas[i]['id'] + "\" id=\"" + respuestas[i]['id'] + "\">";
                                                        cadena += "<label for=\"" + respuestas[i]['id'] + "\">" + respuestas[i]['nombre'] + "</label>";
                                                        cadena += "</div>";
                                                    }
                                                } else if (idTipoPregunta === 3) {
                                                    cadena += "<label class=\"col-xs-3 control-label\" id=\"l_" + preguntaId + "\">" + preguntaString + "</label>";
                                                    cadena += "<div class='col-sm-9'>";
                                                    cadena += "<input data-bv-notempty=\"true\" data-bv-notempty-message=\"Es necesario completar este campo\" type='text' class='form-control' onkeyup='salariosMinimos();' name=\"T" + preguntaId + "\" id=\"" + preguntaId + "\">";
                                                    cadena += "</div>";
                                                } else if (idTipoPregunta === 4) {          //Preguntas numericas con 2 decimales
                                                    cadena += "<label class=\"col-xs-3 control-label\" id=\"l_" + preguntaId + "\">" + preguntaString + "</label>";
                                                    cadena += "<div class='col-sm-9'>";
                                                    cadena += "<input data-bv-notempty=\"true\" data-bv-notempty-message=\"Es necesario completar este campo\" type='number' min='0' step='.01' class='form-control condecimal' onkeyup='salariosMinimos();' name=\"T" + preguntaId + "\" id=\"" + preguntaId + "\">";
                                                    cadena += "</div>";
                                                } else if (idTipoPregunta === 5) {          // Preguntas numericas sin decimales
                                                    cadena += "<label class=\"col-xs-3 control-label\" id=\"l_" + preguntaId + "\">" + preguntaString + "</label>";
                                                    cadena += "<div class='col-sm-9'>";
                                                    cadena += "<input data-bv-notempty=\"true\" data-bv-notempty-message=\"Es necesario completar este campo\" type='number' min='0' class='form-control sindecimal' onkeyup='salariosMinimos();' name=\"T" + preguntaId + "\" id=\"" + preguntaId + "\">";
                                                    cadena += "</div>";
                                                }//Paso 5 - Selección de Beca
                                                else if (idTipoPregunta === 0) {
                                                    //Ingresos por persona en tu familia
                                                    cadena += "<div class='col-sm-9'>";
                                                    cadena += "<div name=\"salarios\" id=\"salarios\">";
                                                    cadena += "</div></div><div class='clearfix'>&nbsp;</div><div class='clearfix'>&nbsp;</div>";
                                                    //Programas de Beca
                                                    cadena += "<p style='margin-left: 20px; margin-right: 20px; text-justify: inter-word; text-align: justify;'>";
                                                    cadena += "A continuación te mostramos un listado de los programas de beca disponibles para éste periodo. Por favor, selecciona la beca por la que deseas participar:</p>";
                                                    cadena += "<div class='col-sm-9'>";
                                                    cadena += "<select onchange='mostrarLeyenda()' required data-bv-notempty-message='Por favor, selecciona una opción' class='form-control' name='S0' id='0'>";
                                                    cadena += "<option value=\"\" selected>Selecciona un programa de beca</option>";
                                                    for (var i = 0; i < respuestas.length; i++) {
                                                        cadena += "<option value=\"" + respuestas[i]['id'] + "\">" + respuestas[i]['nombre'] + "</option>";
                                                    }
                                                    cadena += "</select>";
                                                    cadena += "</div>";
                                                    //Rangos de ingresos por persona en la familia requerido
                                                    cadena += "<div class='col-sm-9'>";
                                                    cadena += "<div name=\"rangos\" id=\"rangos\"></p>";
                                                    cadena += "</div>";
                                                    //Check de transferencia
                                                    cadena += "<div class='clearfix'>&nbsp;</div><div class='clearfix'>&nbsp;</div>";
                                                    cadena += "<div class='col-sm-12 form-group' style='padding-left: 35px'>";
                                                    cadena += "<div class='checkbox-nice'>";
                                                    cadena += "<input type='checkbox' name='-1' id='-1' value='check'";
        <s:if test="transferencia == 1">
                                                    cadena += "checked";
        </s:if>
                                                    cadena += ">";
                                                    cadena += "<label for='-1'>";
                                                    cadena += "Acepto que en caso de no poder ser beneficiado con el tipo de beca que solicité, me pueda ser</br>";
                                                    cadena += "otorgada otra, previo cumplimiento de los requisitos (sujeto a disponibilidad de presupuesto). Así</br>";
                                                    cadena += "mismo que en caso de resultar becario en este ciclo escolar, acepto que en el proceso de validación</br>";
                                                    cadena += "de mi beca se me transfiera a otra si dejo de cumplir con los requisitos de la que se me haya otorgado</br>";
                                                    cadena += "inicialmente (sujeto a disponibilidad de presupuesto).";
                                                    cadena += "</label>";
                                                    cadena += "</div>";
                                                    cadena += "</div>";
                                                }
                                                cadena += "</div>";
                                                return cadena;
                                            }

                                            function salariosMinimos() {
                                                $("select[name='S13'] option[value='184']").attr('selected', false);
                                                $("select[name='S13'] option[value='62']").attr('selected', false);
                                                $("select[name='S13'] option[value='63']").attr('selected', false);
                                                $("select[name='S13'] option[value='64']").attr('selected', false);
                                                $("select[name='S13'] option[value='65']").attr('selected', false);
                                                $("select[name='S13'] option[value='66']").attr('selected', false);
                                                $("select[name='S13'] option[value='67']").attr('selected', false);
                                                $("select[name='S13'] option[value='68']").attr('selected', false);
                                                $("select[name='S13'] option[value='69']").attr('selected', false);
                                                $("select[name='S13'] option[value='70']").attr('selected', false);
                                                $("select[name='S13'] option[value='71']").attr('selected', false);
                                                $("select[name='S13'] option[value='-1']").attr('selected', false);
                                                var sumaIngresos = document.getElementById("167").value;
                                                var integrantes = document.getElementById("168").value;
                                                //Se valida que la suma del ingreso sea de 6 dígitos, de 1 a 999999.
                                                if (parseInt(sumaIngresos) === 0) {
                                                    ModalGenerator.notificacion({
                                                        "titulo": "Atención",
                                                        "cuerpo": "No se pueden poner los ingresos en cero.",
                                                        "tipo": "ALERT"
                                                    });
                                                    document.getElementById("167").value = "1";
                                                } else if (parseInt(sumaIngresos) > 99999) {
                                                    ModalGenerator.notificacion({
                                                        "titulo": "Datos incompletos",
                                                        "cuerpo": "No se pueden poner mas de 99,999 de ingresos.",
                                                        "tipo": "ALERT"
                                                    });
                                                    document.getElementById("167").value = "1";
                                                }
                                                //Se valida que el total de integrantes sea de 2 dígitos, del 1 al 99.
                                                if (parseInt(integrantes) === 0) {
                                                    ModalGenerator.notificacion({
                                                        "titulo": "Datos incompletos",
                                                        "cuerpo": "No se pueden poner los integrantes en cero.",
                                                        "tipo": "ALERT"
                                                    });
                                                    document.getElementById("168").value = "1";
                                                } else if (parseInt(integrantes) > 99) {
                                                    ModalGenerator.notificacion({
                                                        "titulo": "Datos incompletos",
                                                        "cuerpo": "No se pueden poner más de 99 integrantes.",
                                                        "tipo": "ALERT"
                                                    });
                                                    document.getElementById("168").value = "1";
                                                }
                                                //se realiza el calculo de los ingresos por persona en la familia.
                                                var salarios = parseFloat(sumaIngresos) / parseInt(integrantes);
                                                //$("#salarios").val(salarios.toFixed(2));
                                                $("#salarios").html("Ingresos por persona en tu familia: " + salarios.toFixed(2));
                                                // realizamos el calculo de los salarios mínimos.
                                                var valor1 = document.getElementById("167").value;
                                                var valor2 = document.getElementById("168").value;
                                                var salariosMinimos = (((parseFloat(valor1) / parseInt(valor2)) / 2338.56).toString()).split(".", 1);
                                                if (salariosMinimos === "0") {
                                                    $("select[name='S13'] option[value='184']").attr('selected', 'selected');
                                                } else if (salariosMinimos === "1") {
                                                    $("select[name='S13'] option[value='62']").attr('selected', 'selected');
                                                } else if (salariosMinimos === "2") {
                                                    $("select[name='S13'] option[value='63']").attr('selected', 'selected');
                                                } else if (salariosMinimos === "3") {
                                                    $("select[name='S13'] option[value='64']").attr('selected', 'selected');
                                                } else if (salariosMinimos === "4") {
                                                    $("select[name='S13'] option[value='65']").attr('selected', 'selected');
                                                } else if (salariosMinimos === "5") {
                                                    $("select[name='S13'] option[value='66']").attr('selected', 'selected');
                                                } else if (salariosMinimos === "6") {
                                                    $("select[name='S13'] option[value='67']").attr('selected', 'selected');
                                                } else if (salariosMinimos === "7") {
                                                    $("select[name='S13'] option[value='68']").attr('selected', 'selected');
                                                } else if (salariosMinimos === "8") {
                                                    $("select[name='S13'] option[value='69']").attr('selected', 'selected');
                                                } else if (salariosMinimos === "9") {
                                                    $("select[name='S13'] option[value='70']").attr('selected', 'selected');
                                                } else if (parseInt(salariosMinimos) >= 10) {
                                                    $("select[name='S13'] option[value='71']").attr('selected', 'selected');
                                                } else {
                                                    $("select[name='S13'] option[value='-1']").attr('selected', 'selected');
                                                }
                                                $(".condecimal").blur(function() {
                                                    this.value = parseFloat(this.value).toFixed(2); 
                                                });

                                                $( ".sindecimal").blur(function() { 
                                                    $(this).val($(this).val().replace(/[^\d].+/, "")); 
                                                });
                                            }
                                            
                                            function mostrarLeyenda() {
        <s:iterator var="rango" value="rangosSalarios">
                                                if ('<s:property  value="#rango.pregunta"/>' === document.getElementById("0").value) {
                                                    $("#rangos").html("<s:property  value="#rango.respuesta" />");
                                                }
        </s:iterator>
                                            }                                      
    </script>
</content>