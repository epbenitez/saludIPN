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
            <s:if test="periodoActivo">
                <div class="alert alert-info fade in">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                    <i class="fa fa-info-circle fa-fw fa-lg"></i>
                    Debes contestar <strong>TODAS</strong> las preguntas, después oprime el botón <strong>Finalizar</strong>, para terminar encuesta.
                </div>
            </s:if>
            <s:else>
                <div class="alert alert-danger">
                    <i class="fa fa-times-circle fa-fw fa-lg"></i>
                    El periodo de registro para la solicitud de beca ha finalizado. Por lo que no puedes llenar esta encuesta.
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
                            <h2 id="seccion"></h2>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-xs-11">
                            <div class="btn-group pull-right">                                    
                                <button type="submit" id="btnFinalizar" class="btn btn-info">Finalizar</button>
                            </div>
                        </div>
                    </div>

                </form>
                <s:form action="finalizarSeguimientoBecarios.action?cuestionarioId=6 "id="formFinalizar" name="formFinalizar" method="post">
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
        //Arreglo que almacenara todos los arreglos de hijos
        var padres = [];
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
                //Valor de la respuesta que genera a las hijas, viene de la base de datos de la tabla
                //ent_cuestionario_pregunta
                var id_respuestadependiente = <s:property  value="pregunta.respuestaDependiente.id" />;
                var hijos = [];
                var id_respuesta;

                //Primer elemento a ingresar en padres.
                if (preguntaPadre !== 0 && padres.length === 0 && id_respuestadependiente !== 0)
                {
                    hijos.push(preguntaPadre);
                    hijos.push(id_respuestadependiente);
                    hijos.push(preguntaId);
                    padres.push(hijos);
                    //Si el padre es igual y la respuesta dependiente es igual, solo se agregan los hijos necesarios
                } else if (preguntaPadre !== 0 && padres[padres.length - 1][0] === preguntaPadre && padres[padres.length - 1][1] === id_respuestadependiente)
                {
                    hijos.push(preguntaId);
                    padres[padres.length - 1].push(hijos);
                    //Si el padre es igual, pero la respuesta dependiente es diferente, se agrega un nuevo registro del padre con la nueva respuesta dependiente y sus respectivos hijos
                } else if (preguntaPadre !== 0 && padres[padres.length - 1][0] === preguntaPadre && padres[padres.length - 1][1] !== id_respuestadependiente)
                {
                    hijos.push(preguntaPadre);
                    hijos.push(id_respuestadependiente);
                    hijos.push(preguntaId);
                    padres.push(hijos);
                    // Si el padre es diferente, se crea un nuevo registro con el nuevo padre
                } else if (preguntaPadre !== 0 && padres[padres.length - 1][0] !== preguntaPadre)
                {
                    hijos.push(preguntaPadre);
                    hijos.push(id_respuestadependiente);
                    hijos.push(preguntaId);
                    padres.push(hijos);
                }

                var respuestas = new Array();

                //Se recorre la lista con las respuestas del cuestionario de salud
            <s:iterator var="resp" value="respuestas">
                var respuestasInner = new Array();
                respuestasInner['id'] = "<s:property  value="#resp.id" />";
                respuestasInner['nombre'] = "<s:property  value="#resp.nombre" />";
                respuestas.push(respuestasInner);
            </s:iterator>

                //Se recorren las respuestas del usuario
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

            //Valida el formulario, excluyendo las preguntas que están ocultas
            $('#cuestionario').bootstrapValidator({
                excluded: [':disabled', ':hidden', ':not(:visible)']
            }).on('success.form.bv', function (e) {
                e.preventDefault();
                finalizarReg();
            }).on('error.form.bv', function (e) {
                ModalGenerator.notificacion({
                    "titulo": "Datos incompletos",
                    "cuerpo": "Es necesario completar todos los campos. Por favor, verifica las preguntas marcadas en rojo.",
                    "tipo": "ALERT"
                });
            }).on('status.field.bv', function (e, data) {
                var $form = $(e.target),
                        validator = data.bv,
                        $tabPane = data.element.parents('.tab-pane'),
                        tabId = $tabPane.attr('id');
                if (tabId) {
                    var $icon = $('a[href="#' + tabId + '"][data-toggle="tab"]').parent().find('i');
                    if (data.status === validator.STATUS_INVALID) {
                        $icon.removeClass('fa-check').addClass('fa-times');
                    } else if (data.status === validator.STATUS_VALID) {
                        var isValidTab = validator.isValidContainer($tabPane);
                        $icon.removeClass('fa-check fa-times').addClass(isValidTab ? 'fa-check' : 'fa-times');
                    }
                }
            });
        });

        function finalizarReg() {
            var stringEnvia = "";
            var totalRespuestas = 0;
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
                if ($(this)[0].name === 49)
                    discapacidad = true;
            });
            if (discapacidad) {
                totalRespuestas = totalRespuestas - 1;
            }

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

        function estatusFalse() {
            ModalGenerator.notificacion({
                "titulo": "Atención",
                "cuerpo": "No se puede finalizar tu cuestionario porque tus datos académicos no han sido actualizados.",
                "tipo": "INFO"
            });
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
            var nivel = <s:property  value="nivelSuperior" escape="false"/>;
            //Preguntas cerradas
            if (idTipoPregunta === 1)
            {
                if (nivel === false && preguntaId === 227 && idPadre === 0)
                {
                    cadena += "<div class='form-group col-sm-12' style='display: none;' id=\"d_" + preguntaId + "\">";
                    cadena += "<label class=\"col-sm-3 control-label\" id=\"l_" + preguntaId + "\">" + preguntaString + "</label>";
                    cadena += "<div class='col-sm-9'>";
                    cadena += "<select required data-bv-notempty-message=\"Por favor, selecciona una opción\" class='form-control' name=\"S" + preguntaId + "\" id=\"" + preguntaId + "\">";
                    cadena += "<option value=\"\" selected>Seleccione una respuesta</option>";
                    for (var i = 0; i < respuestas.length; i++) {
                        cadena += "<option value=\"" + respuestas[i]['id'] + "\">" + respuestas[i]['nombre'] + "</option>";
                    }
                    cadena += "</select>";
                    cadena += "</div>";
                }
                //Cuando son preguntas Padre, siempre se muestran.
                if (idPadre === 0)
                {
                    cadena += "<div class='form-group col-sm-12' id=\"d_" + preguntaId + "\">";
                    cadena += "<label class=\"col-sm-12 formatoPregunta\" id=\"l_" + preguntaId + "\">" + preguntaString + "</label>";
                    cadena += "<div class='col-sm-12'>";
                    cadena += "<select required data-bv-notempty-message=\"Por favor, selecciona una opción\" onchange=\"despliegue(this.value, " + preguntaId + ")\" class='form-control' name=\"S" + preguntaId + "\" id=\"" + preguntaId + "\">";
                    cadena += "<option value=\"\" selected>Seleccione una respuesta</option>";
                    for (var i = 0; i < respuestas.length; i++) {
                        cadena += "<option value=\"" + respuestas[i]['id'] + "\">" + respuestas[i]['nombre'] + "</option>";
                    }
                    cadena += "</select>";
                    cadena += "</div>";

                } else
                {
                    //Cuando son preguntas hijas con padre activo, se muestran.
                    if (document.getElementById("" + idPadre + "").value === "" + idRespuestaDependiente + "")
                    {
                        cadena += "<div class='form-group col-sm-12' id=\"d_" + preguntaId + "\">";
                        cadena += "<label class=\"col-sm-3 control-label\" id=\"l_" + preguntaId + "\">" + preguntaString + "</label>";
                        cadena += "<div class='col-sm-9'>";
                        cadena += "<select required data-bv-notempty-message=\"Por favor, selecciona una opción\" class='form-control' name=\"S" + preguntaId + "\" id=\"" + preguntaId + "\">";
                        cadena += "<option value=\"\" selected>Seleccione una respuesta</option>";
                        for (var i = 0; i < respuestas.length; i++) {
                            cadena += "<option value=\"" + respuestas[i]['id'] + "\">" + respuestas[i]['nombre'] + "</option>";
                        }
                        cadena += "</select>";
                        cadena += "</div>";
                        //Cuando son preguntas hijas con padre inactivo, se ocultan.
                    } else if (document.getElementById("" + idPadre + "").value !== "" + idRespuestaDependiente + "")
                    {
                        cadena += "<div class='form-group col-sm-12' style='display: none;' id=\"d_" + preguntaId + "\">";
                        cadena += "<label class=\"col-sm-3 control-label\" id=\"l_" + preguntaId + "\">" + preguntaString + "</label>";
                        cadena += "<div class='col-sm-9'>";
                        cadena += "<select required data-bv-notempty-message=\"Por favor, selecciona una opción\" class='form-control' name=\"S" + preguntaId + "\" id=\"" + preguntaId + "\">";
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
                //Cuando son preguntas Padre, siempre se muestran.
                if (idPadre === 0)
                {
                    cadena += "<div class='form-group col-sm-12' id=\"d_" + preguntaId + "\">";
                    cadena += "<p class=\"col-xs-10 pull-left\" style=\"padding-left: 20px;\">" + preguntaString.replace("</br>", " ") + "</p>";
                    for (var i = 0; i < respuestas.length; i++)
                    {
                        cadena += "<div class=\"checkbox-nice checkbox col-sm-3 col-sm-offset-1 col-xs-5 col-xs-offset-1\" >";
                        if (i === 0)
                            cadena += "<input data-bv-notempty=\"true\" data-bv-notempty-message=\"Por favor, selecciona una opción\" type='checkbox' name=\"" + preguntaId + "\" value=\"" + respuestas[i]['id'] + "\" id=\"" + respuestas[i]['id'] + "\">";
                        else
                            cadena += "<input type='checkbox' name=\"" + preguntaId + "\" value=\"" + respuestas[i]['id'] + "\" id=\"" + respuestas[i]['id'] + "\">";
                        cadena += "<label for=\"" + respuestas[i]['id'] + "\">" + respuestas[i]['nombre'] + "</label>";
                        cadena += "</div>";

                    }
                    //Cuando son preguntas hijas con padre activo, se muestran.
                } else if (document.getElementById("" + idPadre + "").value === "" + idRespuestaDependiente + "")
                {
                    cadena += "<div class='form-group col-sm-12' id=\"d_" + preguntaId + "\">";
                    cadena += "<p class=\"col-xs-10 pull-left\" style=\"padding-left: 20px;\">" + preguntaString.replace("</br>", " ") + "</p>";
                    for (var i = 0; i < respuestas.length; i++)
                    {
                        cadena += "<div class=\"checkbox-nice checkbox col-sm-3 col-sm-offset-1 col-xs-5 col-xs-offset-1\" >";
                        if (i === 0)
                            cadena += "<input data-bv-notempty=\"true\" data-bv-notempty-message=\"Por favor, selecciona una opción\" type='checkbox' name=\"" + preguntaId + "\" value=\"" + respuestas[i]['id'] + "\" id=\"" + respuestas[i]['id'] + "\">";
                        else
                            cadena += "<input type='checkbox' name=\"" + preguntaId + "\" value=\"" + respuestas[i]['id'] + "\" id=\"" + respuestas[i]['id'] + "\">";
                        cadena += "<label for=\"" + respuestas[i]['id'] + "\">" + respuestas[i]['nombre'] + "</label>";
                        cadena += "</div>";
                    }
                    //Cuando son preguntas hijas con padre inactivo, se ocultan.
                } else if (document.getElementById("" + idPadre + "").value !== "" + idRespuestaDependiente + "")
                {
                    cadena += "<div class='form-group col-sm-12' style='display: none;' id=\"d_" + preguntaId + "\">";
                    cadena += "<p class=\"col-xs-10 pull-left\" style=\"padding-left: 20px;\">" + preguntaString.replace("</br>", " ") + "</p>";
                    for (var i = 0; i < respuestas.length; i++)
                    {
                        cadena += "<div class=\"checkbox-nice checkbox col-sm-3 col-sm-offset-1 col-xs-5 col-xs-offset-1\" >";
                        if (i === 0)
                            cadena += "<input data-bv-notempty=\"true\" data-bv-notempty-message=\"Por favor, selecciona una opción\" type='checkbox' name=\"" + preguntaId + "\" value=\"" + respuestas[i]['id'] + "\" id=\"" + respuestas[i]['id'] + "\">";
                        else
                            cadena += "<input type='checkbox' name=\"" + preguntaId + "\" value=\"" + respuestas[i]['id'] + "\" id=\"" + respuestas[i]['id'] + "\">";
                        cadena += "<label for=\"" + respuestas[i]['id'] + "\">" + respuestas[i]['nombre'] + "</label>";
                        cadena += "</div>";
                    }
                }

                //Preguntas Abiertas
            } else if (idTipoPregunta === 3) {
                //Cuando son preguntas Padre, siempre se muestran.
                if (idPadre === 0)
                {
                    cadena += "<div class='form-group col-sm-12' id=\"d_" + preguntaId + "\">";
                    cadena += "<label class=\"col-xs-12 formatoPregunta\" id=\"l_" + preguntaId + "\">" + preguntaString + "</label>";
                    cadena += "<div class='col-sm-12'>";
                    cadena += "<input data-bv-notempty=\"true\" data-bv-notempty-message=\"Es necesario completar este campo\" type='number' class='form-control' name=\"T" + preguntaId + "\" id=\"" + preguntaId + "\">";
                    cadena += "</div>";
                    //Cuando son preguntas hijas con padre activo, se muestran.
                } else if (document.getElementById("" + idPadre + "").value === "" + idRespuestaDependiente + "")
                {
                    cadena += "<div class='form-group col-sm-12' id=\"d_" + preguntaId + "\">";
                    cadena += "<label class=\"col-xs-3 control-label\" id=\"l_" + preguntaId + "\">" + preguntaString + "</label>";
                    cadena += "<div class='col-sm-9'>";
                    cadena += "<input data-bv-notempty=\"true\" data-bv-notempty-message=\"Es necesario completar este campo\" type='text' class='form-control' name=\"T" + preguntaId + "\" id=\"" + preguntaId + "\">";
                    cadena += "</div>";
                    //Cuando son preguntas hijas con padre inactivo, se ocultan.
                } else if (document.getElementById("" + idPadre + "").value !== "" + idRespuestaDependiente + "")
                {
                    cadena += "<div class='form-group col-sm-12' style='display: none;' id=\"d_" + preguntaId + "\">";
                    cadena += "<label class=\"col-xs-3 control-label\" id=\"l_" + preguntaId + "\">" + preguntaString + "</label>";
                    cadena += "<div class='col-sm-9'>";
                    cadena += "<input data-bv-notempty=\"true\" data-bv-notempty-message=\"Es necesario completar este campo\" type='text' class='form-control' name=\"T" + preguntaId + "\" id=\"" + preguntaId + "\">";
                    cadena += "</div>";
                }
            }
            cadena += "</div>";
            return cadena;
        }

        //Función que maneja el mostrar u ocultar las preguntas hijas
        //Solo funciona para las preguntas padre cerradas
        //Recibe como primer parámetro el valor de la respuesta actual y el id de la pregunta padre
        function despliegue(val, padre) {

            //Arreglo que dicta el número de registros con el mismo padre (Por si hay preguntas que tengan diferentes preguntas hijas dependiendo de la respuesta que se les de)
            var index = [];
            for (i = 0; i < padres.length; i++)
            {
                if (padre === padres[i][0])
                {
                    index.push(i);
                }

            }

            //Recorre los registros con el padre indicado, verificando el valor de la respuesta que se pasó como parametro, contra el que viene de la base de datos que se almaceno en 
            //el arreglo "padres" al pintar el formulario
            for (i = 0; i < index.length; i++)
            {
                if (val == padres[index[i]][1])
                {
                    for (j = 2; j < padres[index[i]].length; j++)
                    {
                        document.getElementById("d_" + padres[index[i]][j] + "").style.display = "block";
                    }
                } else {
                    for (j = 2; j < padres[index[i]].length; j++)
                    {
                        document.getElementById("d_" + padres[index[i]][j] + "").style.display = "none";
                    }
                }
            }
        }
    </script>
</content>