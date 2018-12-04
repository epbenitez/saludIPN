<%-- 
    Document   : formEncuesta
    Created on : 19/11/2015, 10:06:53 AM
    Author     : Tania G. Sánchez
    Redesing   : Mario Márquez
    ReRedesing: Augusto H.
--%>

<%--////////////////////////////////////////////////////////////////////////////////////////////////////////
                                    IMPORTANTE!!
Para documentación de las funciones usadas para este jsp, favor de revisar el archivo...
censodesalud/form.jsp
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

        .formatoPregunta {
            font-weight: bold;
            padding-top: 8px;
        }
    </style>
    <link rel="stylesheet" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css" type="text/css" />    
    <title>Seguimiento a Becarios</title>
</head> 

<content tag="tituloJSP">
    Seguimiento a Becarios
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
                        <ul class="nav nav-tabs" id="navBar">
                        </ul>                        
                        <div class="tab-content" id="contenido-tab">
                            <h2 id="seccion"></h2>
                        </div>
                    </div>
                </form>
            </div>
            <div class="col-xs-11">
                <div class="btn-group pull-right">   
                    <!--<a class="btn btn-primary" href="/panelcontrol/mostrar.action" role="button">Regresar al Inicio</a>-->
                </div>
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

            var idSeccionCurrent = "";
            var idSeccion = "";
            var counter = 0;


        <s:iterator var="preg" value="preguntas">
            idSeccionCurrent = <s:property  value="seccion.id" />;
            if (idSeccion !== idSeccionCurrent)
            {
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
                var preguntaPadre = <s:property  value="pregunta.padre" />;
                var id_respuestadependiente = <s:property  value="pregunta.respuestaDependiente.id" />;
                var id_respuesta;

                var respuestas = new Array();

            <s:iterator var="resp" value="respuestas">
                var respuestasInner = new Array();
                respuestasInner['id'] = "<s:property  value="#resp.id" />";
                respuestasInner['nombre'] = "<s:property  value="#resp.nombre" />";
                respuestas.push(respuestasInner);
            </s:iterator>

            <s:iterator value="respuestasUsuario">
                <s:if test="respuestaAbierta!=null">
                setValue('<s:property  value="pregunta.id"/>', '<s:property  value="respuestaAbierta"/>');
                id_respuesta = '<s:property  value="respuestaAbierta"/>';
                </s:if>
                <s:else>
                selectedValueSet('<s:property  value="pregunta.id"/>', '<s:property  value="respuesta.id"/>');
                id_respuesta = '<s:property  value="respuesta.id"/>';
                </s:else>
            </s:iterator>
                var formText = crearForm(preguntaId, preguntaNombre, idTipoPregunta, respuestas, preguntaPadre, id_respuestadependiente);
                var seccionString = idSeccion + "-tab";
                $('#' + seccionString).append(formText);
                $('#contenido-tab').append("</div>");
            }
        </s:iterator>

            //Agrega las imagenes a los tabs
            $("#navBar li").click(function () {
                switch ($(this).attr('id')) {
                    case "1":
                        $("#seguimiento1").attr("src", "/resources/img/seguimientoBecarios/1.jpg");
                        $("#seguimiento2").attr("src", "/resources/img/seguimientoBecarios/2-2.jpg");
                        $("#seguimiento3").attr("src", "/resources/img/seguimientoBecarios/3-3.jpg");
                        break;
                    case "2":
                        $("#seguimiento1").attr("src", "/resources/img/seguimientoBecarios/1-1.jpg");
                        $("#seguimiento2").attr("src", "/resources/img/seguimientoBecarios/2.jpg");
                        $("#seguimiento3").attr("src", "/resources/img/seguimientoBecarios/3-3.jpg");
                        break;
                    case "3":
                        $("#seguimiento1").attr("src", "/resources/img/seguimientoBecarios/1-1.jpg");
                        $("#seguimiento2").attr("src", "/resources/img/seguimientoBecarios/2-2.jpg");
                        $("#seguimiento3").attr("src", "/resources/img/seguimientoBecarios/3.jpg");
                        break;
                }
            });
        });

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
                case 8:
                    var txtNavBar = "<li class='active' id='1'><a href=\"#" + idSeccion + "-tab\" data-toggle=\"tab\"><span class='visible-xs'>" + nombreNavBar + " </span><img id='seguimiento1' src='/resources/img/seguimientoBecarios/1.jpg' class='hidden-xs'><i class=\"fa\"></i></a></li>";
                    break;
                case 9:
                    var txtNavBar = "<li id='2'><a href=\"#" + idSeccion + "-tab\" data-toggle=\"tab\"><span class='visible-xs'>" + nombreNavBar + " </span><img id='seguimiento2' src='/resources/img/seguimientoBecarios/2-2.jpg' class='hidden-xs'><i class=\"fa\"></i></a></li>";
                    break;
                case 10:
                    var txtNavBar = "<li id='3'><a href=\"#" + idSeccion + "-tab\" data-toggle=\"tab\"><span class='visible-xs'>" + nombreNavBar + " </span><img id='seguimiento3' src='/resources/img/seguimientoBecarios/3-3.jpg' class='hidden-xs'><i class=\"fa\"></i></a></li>";
                    break;
            }
            // Agregamos los elementos de las listas al elemento ul
            $("#navBar").append(txtNavBar);
        }
        function agregarDivsTabs(tabActivo, idSeccion) {
            $('#contenido-tab').append("<div class=\"tab-pane fade" + (tabActivo === 0 ? "content active\"" : "\"") + " id=\"" + idSeccion + "-tab\">");
        }

        function crearForm(preguntaId, preguntaNombre, idTipoPregunta, respuestas, idPadre, idRespuestaDependiente) {
            var preguntaString = (preguntaNombre.replace("<strong>", "")).replace("</strong>", "");
            var cadena = "";

            //Preguntas cerradas
            if (idTipoPregunta === 1)
            {
                //Cuando son preguntas Padre.
                if (idPadre === 0)
                {
                    cadena += "<div class='form-group col-sm-12' id=\"d_" + preguntaId + "\">";
                    cadena += "<label class=\"col-sm-12 formatoPregunta\" id=\"l_" + preguntaId + "\">" + preguntaString + "</label>";
                    cadena += "<div class='col-sm-12'>";
                    cadena += "<select required data-bv-notempty-message=\"Por favor, selecciona una opción\" class='form-control' name=\"S" + preguntaId + "\" id=\"" + preguntaId + "\" disabled>";
                    cadena += "<option value=\"\" selected>Seleccione una respuesta</option>";
                    for (var i = 0; i < respuestas.length; i++) {
                        cadena += "<option value=\"" + respuestas[i]['id'] + "\">" + respuestas[i]['nombre'] + "</option>";
                    }
                    cadena += "</select>";
                    cadena += "</div>";

                } else
                {
                    if (document.getElementById("" + idPadre + "").value === "" + idRespuestaDependiente + "")
                    {
                        cadena += "<div class='form-group col-sm-12' id=\"d_" + preguntaId + "\">";
                        cadena += "<label class=\"col-sm-3 control-label\" id=\"l_" + preguntaId + "\">" + preguntaString + "</label>";
                        cadena += "<div class='col-sm-9'>";
                        cadena += "<select required data-bv-notempty-message=\"Por favor, selecciona una opción\" class='form-control' name=\"S" + preguntaId + "\" id=\"" + preguntaId + "\" disabled>";
                        cadena += "<option value=\"\" selected>Seleccione una respuesta</option>";
                        for (var i = 0; i < respuestas.length; i++) {
                            cadena += "<option value=\"" + respuestas[i]['id'] + "\">" + respuestas[i]['nombre'] + "</option>";
                        }
                        cadena += "</select>";
                        cadena += "</div>";
                    }
                }
                //Preguntas Múltiples
            } else if (idTipoPregunta === 2)
            {
                if (idPadre === 0)
                {
                    cadena += "<div class='form-group col-sm-12' id=\"d_" + preguntaId + "\">";
                    cadena += "<p class=\"col-xs-10 pull-left\" style=\"padding-left: 20px;\">" + preguntaString.replace("</br>", " ") + "</p>";
                    for (var i = 0; i < respuestas.length; i++)
                    {
                        cadena += "<div class=\"checkbox-nice checkbox col-sm-3 col-sm-offset-1 col-xs-5 col-xs-offset-1\" >";
                        if (i === 0)
                            cadena += "<input data-bv-notempty=\"true\" data-bv-notempty-message=\"Por favor, selecciona una opción\" type='checkbox' name=\"" + preguntaId + "\" value=\"" + respuestas[i]['id'] + "\" id=\"" + respuestas[i]['id'] + "\" disabled>";
                        else
                            cadena += "<input type='checkbox' name=\"" + preguntaId + "\" value=\"" + respuestas[i]['id'] + "\" id=\"" + respuestas[i]['id'] + "\" disabled>";
                        cadena += "<label for=\"" + respuestas[i]['id'] + "\">" + respuestas[i]['nombre'] + "</label>";
                        cadena += "</div>";

                    }
                } else if (document.getElementById("" + idPadre + "").value === "" + idRespuestaDependiente + "")
                {
                    cadena += "<div class='form-group col-sm-12' id=\"d_" + preguntaId + "\">";
                    cadena += "<p class=\"col-xs-10 pull-left\" style=\"padding-left: 20px;\">" + preguntaString.replace("</br>", " ") + "</p>";
                    for (var i = 0; i < respuestas.length; i++)
                    {
                        cadena += "<div class=\"checkbox-nice checkbox col-sm-3 col-sm-offset-1 col-xs-5 col-xs-offset-1\" >";
                        if (i === 0)
                            cadena += "<input data-bv-notempty=\"true\" data-bv-notempty-message=\"Por favor, selecciona una opción\" type='checkbox' name=\"" + preguntaId + "\" value=\"" + respuestas[i]['id'] + "\" id=\"" + respuestas[i]['id'] + "\" disabled>";
                        else
                            cadena += "<input type='checkbox' name=\"" + preguntaId + "\" value=\"" + respuestas[i]['id'] + "\" id=\"" + respuestas[i]['id'] + "\" disabled>";
                        cadena += "<label for=\"" + respuestas[i]['id'] + "\">" + respuestas[i]['nombre'] + "</label>";
                        cadena += "</div>";
                    }
                }

                //Preguntas Abiertas
            } else if (idTipoPregunta === 3) {
                if (idPadre === 0)
                {
                    cadena += "<div class='form-group col-sm-12' id=\"d_" + preguntaId + "\">";
                    cadena += "<label class=\"col-xs-12 formatoPregunta\" id=\"l_" + preguntaId + "\">" + preguntaString + "</label>";
                    cadena += "<div class='col-sm-12'>";
                    cadena += "<input data-bv-notempty=\"true\" data-bv-notempty-message=\"Es necesario completar este campo\" type='text' class='form-control' name=\"T" + preguntaId + "\" id=\"" + preguntaId + "\" disabled>";
                    cadena += "</div>";
                } else if (document.getElementById("" + idPadre + "").value === "" + idRespuestaDependiente + "")
                {
                    cadena += "<div class='form-group col-sm-12' id=\"d_" + preguntaId + "\">";
                    cadena += "<label class=\"col-xs-3 control-label\" id=\"l_" + preguntaId + "\">" + preguntaString + "</label>";
                    cadena += "<div class='col-sm-9'>";
                    cadena += "<input data-bv-notempty=\"true\" data-bv-notempty-message=\"Es necesario completar este campo\" type='text' class='form-control' name=\"T" + preguntaId + "\" id=\"" + preguntaId + "\" disabled>";
                    cadena += "</div>";
                }
            }
            cadena += "</div>";
            return cadena;
        }


    </script>
</content>