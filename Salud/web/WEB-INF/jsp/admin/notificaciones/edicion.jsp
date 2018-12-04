<%-- 
    Document   : editarNotificaciones
    Created on : 5/12/2017, 11:12:30 AM
    Author     : Augusto I. Hernández Ruiz  
--%>

<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<head>
    <title>Editar Notificaciones</title>
    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css"/>
    <link rel="stylesheet" type="text/css" href="/vendors/quill/quill.snow.css"/>
    <style>
        .align-middle {
            vertical-align: middle !important;
        }
    </style>
</head> 

<content tag="tituloJSP">
    Editar Notificaciones
</content>

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
    <div class="main-box clearfix" >
        <br>
        <br>
        <form id="notificaciones" action="/admin/guardaNotificaciones.action" method="post" class="form-horizontal">

            <!-- Tipo de Notificación -->
            <div class="form-group">
                <label class="col-sm-2 control-label">Tipo de Notificación </label>
                <div class="col-sm-9 input-group">
                    <s:select
                        list="tipoNotificacionList"
                        listValue="Nombre"
                        required="true"
                        listKey="id"
                        cssClass="form-control"
                        name="notificacion.tipoNotificacion.id"
                        id="tipoNotificacionSelect"
                        headerKey=""
                        headerValue="Selecciona un Tipo de Notificación"
                        data-bv-notempty="true"
                        data-bv-notempty-message="El tipo de notificación es requerido."/>
                </div>
            </div>

            <!-- Título de Notificación -->
            <div class="form-group">
                <label class="control-label col-sm-2">Titulo de Notificación</label>                                            
                <div class="col-sm-9 input-group">
                    <!--                        <span class="input-group-addon"><i class="fa fa-header" aria-hidden="true"></i></span>-->
                    <s:textfield id="titulo" name="notificacion.titulo" 
                                 cssClass="numero form-control" placeholder="Título de Notificación"
                                 data-bv-message="Este dato no es válido"
                                 required="true" 
                                 data-bv-notempty="true"
                                 data-bv-notempty-message="El Título es requerido"
                                 pattern='^[a-zA-ZáéíóúñÁÉÍÓÚÑ(),"0-9\s]+$'
                                 data-bv-regexp-message="El nombre largo sólo puede estar conformado por letras, números y paréntesis"
                                 data-bv-stringlength="true" 
                                 data-bv-stringlength-min="5" 
                                 data-bv-stringlength-max="100" 
                                 data-bv-stringlength-message="El nombre largo debe tener mínimo 5 caracteres"/>
                </div>                                        
            </div>

            <!-- Alerta de error en falta de texto -->
            <div class="alert alert-danger col-sm-12 text-center" id="errortexto" style="display:none;">
                <a href="#" class="close" data-dismiss="alert" aria-label="close">×</a>
                <strong>Error! </strong><br>El contenido de la notificación es requerido.
            </div>

            <!-- Texto de Notificación -->
            <div class="form-group">
                <label class="control-label col-sm-2 text-center align-middle">Texto de Notificación</label>
                <div class="col-sm-9 input-group">
                    <s:hidden id="text_hidden"  name = "notificacion.notificacion"/>
                    <div id="toolbar"></div>
                    <div id="editor"></div>            
                </div>
            </div>

            <!-- Fecha Inicial de Notificación -->
            <div class="form-group">
                <label class="col-sm-2 control-label text-center">Fecha inicial</label>
                <div class="col-sm-9 input-group date" data-provide="datepicker">
                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                    <input type="text" class="form-control" id="fechaInicio" name="notificacion.fechaInicio"
                           value="<s:date name="notificacion.fechaInicio" format="dd/MM/yyyy"/>">
                </div>
            </div>

            <!-- Alerta de error en alidación de fechas -->
            <div class="alert alert-danger col-sm-12 text-center" id="errorfecha" style="display:none;">
                <a href="#" class="close" data-dismiss="alert" aria-label="close">×</a>
                <strong>Error! </strong><br>La fecha final debe ser posterior a la fecha inicial.
            </div>

            <!-- Fecha Final de Notificación -->
            <div class="form-group">
                <label class="col-sm-2 control-label text-center">Fecha Final</label>
                <div class="col-sm-9 input-group date" data-provide = "datepicker">
                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                    <input type="text" class="form-control" id="fechaFinal" name="notificacion.fechaFinal"                           
                           value="<s:date name="notificacion.fechaFinal" format="dd/MM/yyyy"/>">
                </div>
            </div>

            <!-- CheckBoxes de Roles -->
            <div class="form-group">
                <label class="col-sm-2 control-label text-center">Rol a Enviar </label>
                <div class="col-sm-6 form-check">
                    <s:hidden id="rol_hidden" name = "checkboxRol"/>
                    <s:iterator value="ambiente.rolList">
                        <div class="checkbox-nice">
                            <input type="checkbox" id="<s:property value="id" />" name="rol"
                                   data-bv-notempty="true"
                                   required="true"
                                   data-bv-notempty-message="Al menos un rol es requerido."/>
                            <label for="<s:property value="id" />">
                                <s:property value="clave" />
                            </label>
                        </div>                           
                    </s:iterator>
                </div>
            </div>

            <!-- Botón de envio -->         
            <div class="col-xs-12 pull-right" style="margin-bottom: 2%">
                <s:hidden name="notificacion.id"/>
                <button type="submit" class="btn btn-primary pull-right" id="save">Guardar</button>                    
            </div>
        </form>
    </div>
</body>

<content tag="endScripts">
    <script type="text/javascript" src="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.js"></script>
    <script type="text/javascript" src="/vendors/bootstrap-datepicker/js/bootstrap-datepicker.min.js"></script>
    <script type="text/javascript" src="/vendors/bootstrap-datepicker/locales/bootstrap-datepicker.es.min.js"></script>
    <script type="text/javascript" src="/vendors/quill/quill.js"></script>
</content>

<content tag="inlineScripts">
    <script>

        var toolbarOptions = [
            ["bold", "italic", "link"]
        ];

        var quill = new Quill('#editor', {
            theme: 'snow',
            modules: {
                toolbar: toolbarOptions
            }
        });

        // change the link placeholder to www.github.com
        var tooltip = quill.theme.tooltip;
        var input = tooltip.root.querySelector("input[data-link]");
        input.dataset.link = 'https://sibec.ipn.mx';

        $(document).ready(function () {

            $('#notificaciones').bootstrapValidator();

            $.fn.datepicker.defaults.format = "dd/mm/yyyy";
            $('.datepicker').datepicker({
            });

            quill.clipboard.dangerouslyPasteHTML(0, '<s:property escape="false" value="notificacion.notificacion"/>');

            var today = new Date();
            var mm = today.getMonth() + 1;
            var dd = today.getDate();
            var yyyy = today.getFullYear();
            if (dd < 10)
            {
                dd = "0" + dd;
            } else if (mm < 10)
            {
                mm = "0" + mm;
            }
            var todayString = dd + "/" + mm + "/" + yyyy;

            //document.getElementById("fechaFinal").value = todayString;
            //document.getElementById("fechaInicio").value = todayString;

            //Código mágico de Paty para llenar los checkboxes anteriores
            var checkboxlist = "<s:property value="checkboxRol"/>";
            var x = checkboxlist.split(",");
            if (x.length > 0) {
                for (i = 0; i < x.length; i++) {
                    $("#" + x[i].trim()).prop('checked', true);
                }
            }
        });

        //Función que valida que la FechaInicio sea anterior a la FechaFinal.
        $("form").submit(function (event)
        {
            var html = quill.root.innerHTML;
            document.getElementById("text_hidden").value = html;

            if (html.length < 12)
            {
                document.getElementById('errortexto').style.display = 'block';
                event.preventDefault();
            }

            var date_aux = document.getElementById("fechaInicio").value.split("/");
            var date1 = new Date(parseInt(date_aux[2]), parseInt(date_aux[1]), parseInt(date_aux[0]));

            date_aux = document.getElementById("fechaFinal").value.split("/");
            var date2 = new Date(parseInt(date_aux[2]), parseInt(date_aux[1]), parseInt(date_aux[0]));

            //Validamos que la fecha 2 no sea menor de la fecha inicial
            if (Date.parse(date1) > Date.parse(date2))
            {
                document.getElementById('errorfecha').style.display = 'block';
                event.preventDefault();
            }

            var checkboxes = document.getElementsByName('rol');
            var vals = "";
            for (var i = 0, n = checkboxes.length; i < n; i++)
            {
                if (checkboxes[i].checked)
                {
                    vals += "," + checkboxes[i].id;
                }
            }
            if (vals) {
                vals = vals.substring(1);
            }
            document.getElementById("rol_hidden").value = vals;
        });
        
        $("#notificaciones").data('bootstrapValidator').validate();
        if ($("#notificaciones").data('bootstrapValidator').isValid())
        {
        } else {
            //alert("Hola");
            event.preventDefault();
        }
    </script>
</content>