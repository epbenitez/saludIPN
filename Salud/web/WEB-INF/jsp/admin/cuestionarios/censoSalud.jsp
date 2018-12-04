<%-- 
    Document   : censoSalud
    Created on : 13/12/2016, 12:59:39 PM
    Author     : Tania Gabriela Sánchez Manilla
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
    <title>Censo de Salud</title>
</head> 

<content tag="tituloJSP">
    Censo de Salud
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
                            <h2 id="seccion">Datos generales de salud</h2>
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
                        $("#paso1").attr("src", "/resources/img/censoSalud/paso1-activo.png");
                        $("#paso2").attr("src", "/resources/img/censoSalud/paso2.png");
                        $("#paso3").attr("src", "/resources/img/censoSalud/paso3.png");
                        $("#seccion").html($('span:first', this).text());
                        break;
                    case "2":
                        $("#paso1").attr("src", "/resources/img/censoSalud/paso1.png");
                        $("#paso2").attr("src", "/resources/img/censoSalud/paso2-activo.png");
                        $("#paso3").attr("src", "/resources/img/censoSalud/paso3.png");
                        $("#seccion").html($('span:first', this).text());
                        break;
                    case "3":
                        $("#paso1").attr("src", "/resources/img/censoSalud/paso1.png");
                        $("#paso2").attr("src", "/resources/img/censoSalud/paso2.png");
                        $("#paso3").attr("src", "/resources/img/censoSalud/paso3-activo.png");
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
        });

        function agregarImagenesTabs(idSeccion, nombreNavBar) {
            switch (idSeccion) {
                case 5:
                    var txtNavBar = "<li class='active' id='1'><a href=\"#" + idSeccion + "-tab\" data-toggle=\"tab\"><span class='visible-xs'>" + nombreNavBar + " </span><img id='paso1' src='/resources/img/censoSalud/paso1-activo.png' class='hidden-xs'><i class=\"fa\"></i></a></li>";
                    break;
                case 6:
                    var txtNavBar = "<li id='2'><a href=\"#" + idSeccion + "-tab\" data-toggle=\"tab\"><span class='visible-xs'>" + nombreNavBar + " </span><img id='paso2' src='/resources/img/censoSalud/paso2.png' class='hidden-xs'><i class=\"fa\"></i></a></li>";
                    break;
                case 7:
                    var txtNavBar = "<li id='3'><a href=\"#" + idSeccion + "-tab\" data-toggle=\"tab\"><span class='visible-xs'>" + nombreNavBar + " </span><img id='paso3' src='/resources/img/censoSalud/paso3.png' class='hidden-xs'><i class=\"fa\"></i></a></li>";
                    break;
            }
            // Agregamos los elementos de las listas al elemento ul
            $("#navBar").append(txtNavBar);
        }
        function agregarDivsTabs(tabActivo, idSeccion) {
            $('#contenido-tab').append("<div class=\"tab-pane fade" + (tabActivo === 0 ? "content active\"" : "\"") + " id=\"" + idSeccion + "-tab\">");
        }

        function crearForm(preguntaId, preguntaNombre, idTipoPregunta, respuesta, respuestaAbierta, idPreguntaCurrent) {
            var preguntaString = (preguntaNombre.replace("<strong>", "")).replace("</strong>", "")
            var cadena = "";
            cadena += "<div class='form-group col-sm-12'>"
            if (idTipoPregunta === 1) {
                cadena += "<label class=\"col-sm-5 control-label\" id=\"l_" + preguntaId + "\">" + preguntaString + "</label>";
                cadena += "<div class='col-sm-7'>";
                cadena += "<input type='text' class='form-control' value='" + respuesta + "' disabled>";
                cadena += "</div>";
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
            }
            cadena += "</div>";
            return cadena;
        }
    </script>
</content>