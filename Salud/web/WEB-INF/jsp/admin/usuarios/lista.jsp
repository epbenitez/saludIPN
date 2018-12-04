<%-- 
    Document   : lista
    Created on : 11/11/2015, 09:38:21 AM
    Author     : Victor Lozano
    Redisign     : Gustavo Adolfo Alamillo Medina
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>


<head>
    <title>Administración de usuarios</title>
    <link rel="stylesheet" type="text/css" href="/vendors/fancy-box/jquery.fancybox.min.css"/>
    <link rel="stylesheet" type="text/css" href="/vendors/pretty-checkbox/pretty.min.css"/>
</head> 

<content tag="tituloJSP">
    Administración de usuarios
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

    <div class="row" id="rowTabla" style="display: none;">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <div class="col-md-12" style="padding: 0px">
                    <div class="responsive">                        
                        <table id="tablaUsuarios" class="table table-striped table-hover dt-responsive" style="width: 100%">
                            <thead>
                                <tr>
                                    <th data-center="true">
                                        <div class="pretty outline-success smooth" >
                                            <input type="checkbox" id="todosCandidatos"/> 
                                            <label><i class="glyphicon glyphicon-ok"></i></label>
                                        </div>
                                    </th>
                                    <th data-orderable="true">Usuario</th>
                                    <th data-orderable="true">Nombre</th>
                                    <th data-orderable="true">Rol(es)</th>
                                    <th data-orderable="true">Cargo</th>
                                    <th data-orderable="true">Unidad Académica</th>
                                    <th></th>
                                    <th></th>
                                </tr>
                            </thead>
                        </table>                        
                    </div>
                </div>
            </div>
        </div>                
    </div>

    <form id="eliminarForm" action="/admin/eliminarUsuario.action" method="POST" >
        <input type="hidden" name="personalAdministrativo.id" id="id" value="" />
    </form>

</body>

<content tag="endScripts">
    <script type="text/javascript" src="/vendors/fancy-box/jquery.fancybox.min.js"></script>    
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js" data-init="true" 
            data-ajax="/ajax/listadoUsuarioAjax.action" data-div="rowTabla"
    data-function="despuesCargarTabla" data-button="botonesTabla"></script>
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>


        var botonesTabla = [];
        var selected = [];

        botonesTabla.push({
            text: 'Copiar correos al portapapeles',
            className: 'btn btn-primary solo-lectura',
            action: function (e, dt, node, config) {
                copiar();
            }
        });


        function despuesCargarTabla() {
            $('.fancybox').fancybox({
                afterClose: function () {
                    recargarTabla();
                }
            });
        }
        function recargarTabla() {
            generarTabla("rowTabla", "/ajax/listadoUsuarioAjax.action", despuesCargarTabla, false, botonesTabla);
        }

        var botonAgregar = [{
                text: ' <a id="nueva-entidad" title="Nuevo usuario" href="/admin/formUsuario.action" class="fancybox fancybox.iframe btn btn-primary pull-right solo-lectura" name="nuevo"><i class="fa fa-plus-circle fa-lg"></i>&nbsp;&nbsp;Agregar usuario</a>',
                className: 'normal-button'
            }];

        botonesTabla.push(botonAgregar);

        function eliminar(id) {
            ModalGenerator.notificacion({
                "titulo": "¿Deseas continuar?",
                "cuerpo": "Esto borrará el registro seleccionado, ¿Está seguro de quererlo eliminar?",
                "funcionAceptar": function () {
                    $("#id").val(id);
                    $("#eliminarForm").submit();
                },
                "tipo": "ALERT"
            });
        }

        $("#todosCandidatos").change(function () {
            if (this.checked) {
                $("#tablaUsuarios").find('.personal:enabled').prop('checked', true);
            } else {
                $("#tablaUsuarios").find('.personal').prop('checked', false);
            }
        });

        function copiar()
        {
            selected = [];
            var mailString = "";
            var i = 0;
            var checkBox = document.getElementById("todosCandidatos");

            if (checkBox.checked)
                i = 1;

            $('#tablaUsuarios input:checked').each(function () {
                selected.push($(this).attr('value'));
            });

            if (selected.length < 1)
            {
                ModalGenerator.notificacion({
                    "titulo": "Usuarios no seleccionados.",
                    "cuerpo": "Para Copiar los correos al portapapeles, seleccione por lo menos un usuario utilizando las casillas que se encuentran en cada fila.",
                    "tipo": "ALERT"
                });
            } else {

                for (i; i < selected.length; i++)
                {
                    if (i === selected.length - 1)
                        mailString += selected[i];

                    else
                        mailString += selected[i] + ",";
                }

                console.log(mailString);

                var txt = document.createElement('textarea');
                txt.value = mailString;
                txt.setAttribute('readonly', '');
                txt.style = {position: 'absolute', left: '-9999px'};
                document.body.appendChild(txt);
                txt.select();
                document.execCommand('copy');
                document.body.removeChild(txt);

                ModalGenerator.notificacion({
                    "titulo": "Datos copiados al portapapeles.",
                    "cuerpo": "Se han copiado los correos de los usuarios seleccionados. Para utilizarlos use Ctrl+V en su cliente de correo",
                    "tipo": "SUCCESS"
                });
            }
        }


    </script>
</content>