<%--
    Document   : form
    Created on : 27/06/2018, 01:31:07 PM
    Author     : Admin CAE
--%>

<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Administración de Accesos</title>
    <link rel="stylesheet" type="text/css" href="/vendors/fancy-box/jquery.fancybox.min.css"/>
    <style>
        a:hover{
            color: white;
            text-decoration: none;
        }
    </style>
</head>

<content tag="tituloJSP">
    Administración de Accesos
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

    <!--Tabs-->
    <div class="row">
        <div class="col-xs-12">
            <div class="main-box clearfix">                   
                <div class="tabs-wrapper profile-tabs">
                    <ul class="nav nav-tabs" id="navBar">
                        <li class="active" id="tabLiAlumno">
                            <a href="#alumnoTab" data-toggle="tab" onclick="actualizarTablas()">Alumno</a>
                        </li>
                        <li class="" id="tabLiNivel">
                            <a data-toggle="tab" href="#nivelTab" onclick="actualizarTablas()">Nivel</a>
                        </li>
                        <li class="" id="tabLiUa">
                            <a data-toggle="tab" href="#uaTab" onclick="actualizarTablas()">Unidad Académica</a>
                        </li>
                    </ul>                        

                    <!--Contenido de las tabs-->
                    <div class="tab-content" id="contenido-tab" style="padding:0px;">

                        <!--Tab Alumno-->
                        <div class="tab-pane fade active in" id="alumnoTab">
                            <div id="contenedorAlumno">                                                 
                                <table id="listadoAl" class="table table-striped table-hover dt-responsive" style="width: 100%">
                                    <thead>
                                        <tr>
                                            <th data-orderable="true">Periodo</th>
                                            <th data-orderable="true">Boleta</th>
                                            <th data-orderable="true">Nombre</th>
                                            <th data-orderable="true">Fecha de Inicio</th>
                                            <th data-orderable="true">Fecha de Fin</th>
                                            <th></th>
                                            <th></th>
                                        </tr>
                                    </thead>
                                </table>                           
                            </div>  
                            <form id="eliminarFormAl" action="/admin/eliminarAccesoAlumno.action" method="POST" >
                                <input type="hidden" name="nuevoAcceso.id" id="idAl"/>
                            </form>
                        </div>  


                        <!--Tab Nivel-->
                        <div class="tab-pane fade" id="nivelTab">
                            <div id="contenedorNvl">                                                        
                                <table id="listadoNvl" class="table table-striped table-hover dt-responsive" style="width: 100%">
                                    <thead>
                                        <tr>
                                            <th data-orderable="true">Periodo</th>
                                            <th data-orderable="true">Nivel</th>
                                            <th data-orderable="true">Fecha de Inicio</th>
                                            <th data-orderable="true">Fecha de Fin</th>
                                            <th></th>
                                            <th></th>
                                        </tr>
                                    </thead>
                                </table>                           
                            </div>  
                            <form id="eliminarFormNvl" action="/admin/eliminarAccesoNivel.action" method="POST" >
                                <input type="hidden" name="nuevoAcceso.id" id="idNvl" value=""/>
                            </form>
                        </div>

                        <!--Tab Unidad Académica-->
                        <div class="tab-pane fade" id="uaTab">
                            <div id="contenedorUa">                      
                                <table id="listadoUa" class="table table-striped table-hover dt-responsive" style="width: 100%">
                                    <thead>
                                        <tr>
                                            <th data-orderable="true">Periodo</th>
                                            <th data-orderable="true">Unidad Academica</th>
                                            <th data-orderable="true">Fecha de Inicio</th>
                                            <th data-orderable="true">Fecha de Fin</th>
                                            <th></th>
                                            <th></th>
                                        </tr>
                                    </thead>
                                </table>                           
                            </div>             
                            <form id="eliminarFormUa" action="/admin/eliminarAccesoUAcademica.action" method="POST" >
                                <input type="hidden" name="nuevoAcceso.id" id="idUa" value=""/>
                            </form>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>
</body>

<content tag="endScripts">
    <script type="text/javascript" src="/vendors/fancy-box/jquery.fancybox.min.js"></script>
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>

    <script type="text/javascript" src="/resources/js/generador-tablas.min.js" 
            data-init="true" data-ajax="/ajax/listadoAlAccesosAjax.action" data-div="contenedorAlumno"  
    data-function="despuesCargarAlumno" data-button="botonAgregarAlumno"></script>
</content>

<content tag="inlineScripts">
    <script>
                                //Datos Alumno
                                var botonAgregarAlumno = [{
                                        text: '<a title="Nueva solicitud" href="/admin/formAccesoAlumno.action" class="alumno fancybox fancybox.iframe btn btn-primary solo-lectura pull-right"><i class="fa fa-plus-circle fa-lg"></i>&nbsp;&nbsp;Agregar solicitud</a>',
                                        className: 'btn btn-primary solo-lectura'
                                    }];

                                var urlAlumno = '/ajax/listadoAlAccesosAjax.action';

                                //Datos Nivel
                                var botonAgregarNivel = [{
                                        text: '<a title="Nueva solicitud" href="/admin/formAccesoNivel.action" style="color:white" class="nivel fancybox fancybox.iframe"><i class="fa fa-plus-circle fa-lg"></i>&nbsp;&nbsp;Agregar solicitud</a>',
                                        className: 'btn btn-primary solo-lectura'
                                    }];

                                var urlNivel = '/ajax/listadoNvlAccesosAjax.action';

                                //Datos Unidad Académica
                                var botonAgregarUa = [{
                                        text: '<a title="Nueva solicitud" href="/admin/formAccesoUAcademica.action" class="ua fancybox fancybox.iframe btn btn-primary solo-lectura pull-right"><i class="fa fa-plus-circle fa-lg"></i>&nbsp;&nbsp;Agregar solicitud</a>',
                                        className: 'btn btn-primary solo-lectura'
                                    }];

                                var urlUa = '/ajax/listadoUaAccesosAjax.action';


                                function actualizarTablas()
                                {
                                    generarTabla("contenedorAlumno", urlAlumno, despuesCargarAlumno, false);
                                    generarTabla("contenedorNvl", urlNivel, despuesCargarAlumno, false, botonAgregarNivel);
                                    generarTabla("contenedorUa", urlUa, despuesCargarAlumno, false, botonAgregarUa);
                                }

                                function despuesCargarAlumno()
                                {
                                    $('.alumno').fancybox({
                                        autoSize: true,
                                        afterClose: function () {
                                            actualizarTablas();
                                        }
                                    });

                                    $('.fancybox').fancybox({
                                        autoSize: true,
                                        afterClose: function () {
                                            actualizarTablas();
                                        }
                                    });
                                }

                                function eliminarAl(id) {
                                    ModalGenerator.notificacion({
                                        "titulo": "Atención",
                                        "cuerpo": "Esto borrará el registro seleccionado, ¡Está seguro de querer eliminarlo?",
                                        "tipo": "ALERT",
                                        "funcionAceptar": function () {
                                            $("#idAl").val(id);
                                            $("#eliminarFormAl").submit();
                                        },
                                        "funcionCerrar": function () {
                                            actualizarTablas();
                                        }
                                    });
                                }

                                function eliminarNvl(id) {
                                    ModalGenerator.notificacion({
                                        "titulo": "Atención",
                                        "cuerpo": "Esto borrará el registro seleccionado, ¡Está seguro de querer eliminarlo?",
                                        "tipo": "ALERT",
                                        "funcionAceptar": function () {
                                            $("#idNvl").val(id);
                                            $("#eliminarFormNvl").submit();
                                        },
                                        "funcionCerrar": function () {
                                            actualizarTablas();
                                        }
                                    });
                                }

                                function eliminarUa(id) {
                                    ModalGenerator.notificacion({
                                        "titulo": "Atención",
                                        "cuerpo": "Esto borrará el registro seleccionado, ¡Está seguro de querer eliminarlo?",
                                        "tipo": "ALERT",
                                        "funcionAceptar": function () {
                                            $("#idUa").val(id);
                                            $("#eliminarFormUa").submit();
                                        },
                                        "funcionCerrar": function () {
                                            actualizarTablas();
                                        }
                                    });
                                }



    </script>
</content>