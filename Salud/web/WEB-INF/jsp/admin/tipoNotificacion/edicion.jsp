<%-- 
    Document   : editarNotificaciones
    Created on : 5/12/2017, 11:12:30 AM
    Author     : Augusto I. Hernández Ruiz  
--%>

<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<head>
    <title>Editar Tipo Notificación</title>
    <link rel="stylesheet" type="text/css" href="/vendors/color_picker/css/colorpicker.css"/>
    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css"/>
    <link rel="stylesheet" type="text/css" href="/vendors/fancy-box/jquery.fancybox.min.css"/>
    <link rel="stylesheet" type="text/css" href="/vendors/fontawesome-iconpicker/css/fontawesome-iconpicker.min.css"/>
    <style>
        .align-middle {
            vertical-align: middle !important;
        }
    </style>
</head> 

<content tag="tituloJSP">
    Editar Tipo Notificación.
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

    <div class="main-box clearfix col-xs-12" >
        <br>
        <br>
        <form id="tipoNotificaciones" action="/admin/guardaTipoNotificacion.action" method="post">

            <!-- Nombre -->
            <div class="form-group row">
                <label for="colFormLabelSm" class="col-sm-2 col-form-label col-form-label-sm text-center">Nombre de Tipo de Notificación</label>
                <div class="col-sm-10">
                    <s:textfield 
                        name="tipoNotificacion.Nombre"
                        id="nombreTipoNotificacion"  
                        cssClass="form-control form-control-sm" placeholder="Nombre de Tipo de Notificación"
                        data-bv-message="Este dato no es válido"
                        required="true" 
                        data-bv-notempty="true"
                        data-bv-notempty-message="El nombre es requerido"
                        pattern='^[a-zA-ZáéíóúñÁÉÍÓÚÑ(),"0-9\s]+$'
                        data-bv-regexp-message="El nombre largo sólo puede estar conformado por letras, números y paréntesis"
                        data-bv-stringlength="true" 
                        data-bv-stringlength-min="5" 
                        data-bv-stringlength-max="100" 
                        data-bv-stringlength-message="El nombre debe tener mínimo 5 caracteres"/>
                </div>
            </div>

            <!-- Color -->
            <div class="form-group row">
                <label for="colFormLabelSm" class="col-sm-2 col-form-label col-form-label-sm text-center">Color de Tipo de Notificación</label>
                <div class="col-sm-10">
                    <s:textfield 
                        name="tipoNotificacion.Color"
                        id="colorpicker"  
                        cssClass="form-control form-control-sm"
                        required="true" 
                        data-bv-notempty="true"
                        data-bv-notempty-message="El color es requerido"
                        data-bv-stringlength="true" 
                        data-bv-stringlength-min="7" 
                        data-bv-stringlength-max="100" 
                        data-bv-stringlength-message="El nombre color debe tener 7 caracteres"/>
                </div>
            </div>

            <!-- Ícono -->
            <div class="form-group">
                <label for="colFormLabelSm" class="col-sm-2 col-form-label col-form-label-sm text-center">Ícono de Tipo de Notificación</label>
                <div class="input-group">
                    <input data-placement="bottomRight"
                           class="form-control icp icp-auto"
                           type="text" 
                           id="icon" 
                           name="tipoNotificacion.Icono"
                           required="true" 
                           data-bv-notempty="true"
                           data-bv-stringlength-min="5" 
                           data-bv-notempty-message="El ícono es requerido"
                           data-bv-stringlength="true"
                           data-bv-stringlength-message="El ícono debe tener mínimo 5 caracteres"/>
                    <span class="input-group-addon" id="icon_span-1"><i class="" id="icon_span" style="color: white"></i></span>
                </div>
            </div>

            <!-- Botón de envio -->         
            <div class="col-xs-12 pull-right" style="margin-bottom: 2%">
                <s:hidden name="tipoNotificacion.id"/>
                <button type="submit" class="btn btn-primary pull-right" id="save" onmouseup="correct_icon()">Guardar</button>                    
            </div>
        </form>
    </div>
</body>

<content tag="endScripts">
    <script type="text/javascript" src="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.js"></script>
    <script type="text/javascript" src="/vendors/bootstrap-datepicker/js/bootstrap-datepicker.min.js"></script>
    <script type="text/javascript" src="/vendors/bootstrap-datepicker/locales/bootstrap-datepicker.es.min.js"></script>
    <script type="text/javascript" src="/vendors/color_picker/js/colorpicker.js"></script>
    <script type="text/javascript" src="/vendors/fontawesome-iconpicker/js/fontawesome-iconpicker.js"></script>
</content>

<content tag="inlineScripts">
    <script>

                    $(document).ready(function () {
                        $('#tipoNotificaciones').bootstrapValidator();
                        var text = "<s:property value="tipoNotificacion.Icono"/>";
                        text = text.replace('fa ', "");
                        document.getElementById('icon').value = text;
                        document.getElementById('icon_span').value = text;
                        $("#icon_span").attr('class', '<s:property value="tipoNotificacion.Icono"/> ');
                        $("#icon_span-1").css("background-color", "<s:property value="tipoNotificacion.Color"/>");
                    });

                    $('#colorpicker').ColorPicker({
                        color: '#0000ff',
                        onShow: function (colpkr) {
                            $(colpkr).fadeIn(500);
                            return false;
                        },
                        onHide: function (colpkr) {
                            $(colpkr).fadeOut(500);
                            $("#color_span").css("background-color", '#' + hex);
                            return false;
                        },
                        onChange: function (hsb, hex, rgb) {
                            $('#colorSelector div').css('backgroundColor', '#' + hex);
                            document.getElementById('colorpicker').value = '#' + hex;
                            $("#color_span").css("background-color", '#' + hex);
                            $("#icon_span-1").css("background-color", '#' + hex);
                            //$('#colorpicker').css('backgroundColor', '#' + hex);             
                        }
                    });

                    $('#icon').iconpicker({
                        title: 'Selecciona el ícono de la notificación',
                        icons: ['fa-bell-o', 'fa-bell', 'fa-exclamation-triangle', 'fa-exclamation-circle', 'fa-exclamation'],
                        placement: 'topRight'
                    });

                    function correct_icon() {
                        document.getElementById('icon').value = 'fa ' + document.getElementById('icon').value;
                    }
    </script>
</content>