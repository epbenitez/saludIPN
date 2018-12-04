<%-- 
    Document   : asignacion
    Created on : 01/03/2016, 09:13:14 PM
    Author     : Victor Lozano
    Redesing   : Mario Márquez  
--%>

<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <style>
        textarea { 
            resize: vertical;
        }
    </style>

    <title>Asignación de tarjetas</title>
</head> 

<content tag="tituloJSP">
    Asignación de tarjetas
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
    </div> <!-- Terminan alertas -->

    <!-- Instrucciones -->
    <div class="row">
        <div class="col-xs-12">
            <div class="panel-group accordion" id="accordion">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
                                Instrucciones
                            </a>
                        </h4>
                    </div>
                </div>
                <div id="collapseOne" class="panel-collapse collapse">
                    <div class="panel-body">
                        <p>
                            Para asignar tarjetas primero debe ingresar los 
                            parámetros de búsqueda y posteriormente presionar 
                            el botón buscar. Una vez que se ha mostrado el 
                            listado deberá ingresar el identificador de carga, 
                            las observaciones y presionar el botón ejecutar 
                            lo cual asignara las tarjetas en el orden y con 
                            las características que ha marcado. Para deshacer 
                            la asignación de tarjetas deberá realizar una 
                            búsqueda por el identificador de carga y presionar 
                            el botón deshacer.</p>
                    </div>
                </div>
            </div>
        </div>
    </div> <!-- Terminan Instrucciones -->

    <div class="row">
        <div class="col-sm-7">
            <div class="main-box clearfix" >
                <header class="main-box-header clearfix">
                    <h2 class="pull-left">Búsqueda</h2>
                </header>
                <div class="main-box-body clearfix">
                    <form class="form-horizontal">
                        <div class="form-group col-sm-12">
                            <label class="col-sm-2 control-label">Tipo de asignación</label>
                            <div class="col-sm-10">
                                <select id="tipoAsignacion" class="form-control">
                                    <option value="1">Reemplazos</option>
                                    <option value="2">Nuevos</option>
                                    <option value="3">Remplazos rechazados</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group col-sm-12">
                            <label class="col-sm-2 control-label">Periodo</label>
                            <div class="col-sm-10">
                                <s:select id="periodo"
                                          cssClass="form-control"
                                          name="periodo" 
                                          list="ambiente.periodoList" 
                                          listKey="id" 
                                          listValue="clave" />
                            </div>
                        </div>
                        <div class="form-group col-sm-12">
                            <label class="col-sm-2 control-label">Boleta</label>
                            <div class="col-sm-10">
                                <input type="text" 
                                       class="form-control"
                                       id="boleta"
                                       name = "boleta"
                                       placeholder="Boleta"
                                       pattern="^[\.\s1234567890]+$" 
                                       data-bv-stringlength="true" 
                                       data-bv-stringlength-max="10"/>
                            </div>
                        </div>
                        <div class="form-group col-sm-12">
                            <label class="col-sm-2 control-label">Nivel</label>
                            <div class="col-sm-10">
                                <select id="nivel" class="form-control" onchange="getUnidades()">
                                    <option value="0">Todos</option>
                                    <option value="1">Medio superior</option>
                                    <option value="2">Superior</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group col-sm-12">
                            <label class="col-sm-2 control-label">Unidad Académica</label>
                            <div class="col-sm-10">
                                <select name="unidadAcademica"
                                        id="unidadAcademica"
                                        class="form-control">
                                    <option value="0" data-clave="0" >Todas</option>
                                    <s:iterator value="unidadAcademicaList" status="ua">
                                        <option value="<s:property value="id"/>" data-clave="<s:property value="clave"/>"><s:property value="nombreCorto"/></option>
                                    </s:iterator>
                                </select>
                            </div>                            
                        </div>
                        <div class="col-xs-12 form-group">
                            <div class="col-xs-offset-2 col-xs-10">                                
                                <button id="btn-busqueda-principal" type="button" class="btn btn-primary pull-right">Buscar</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Termina Búsqueda -->

        <div class="col-sm-5">
            <div class="main-box clearfix" >
                <header class="main-box-header clearfix">
                    <h2 class="pull-left">Tarjetas asignadas</h2>
                </header>
                <div class="main-box-body clearfix">
                    <form class="form-horizontal">
                        <div class="form-group col-sm-12 ">
                            <label class="col-sm-3 control-label">Identificador de la carga</label>
                            <div class="col-sm-9">
                                <input id="identificadorBusqueda" type="text" class="form-control"/>
                            </div>
                        </div>
                        <div class="col-xs-12 form-group">
                            <div class="col-xs-offset-2 col-xs-10">
                                <button class="btn btn-primary pull-right" id="boton-TAsignadas">Buscar</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>                            
        <!-- Termina Div Tarjetas asignadas -->
    </div>

    <div class="row" id="div-asignar" style="display: none;">
        <div class="col-sm-12">
            <div class="main-box clearfix" >
                <header class="main-box-header clearfix">
                    <h2 class="pull-left">Asignar</h2>
                </header>
                <div class="main-box-body clearfix">
                    <form class="form-horizontal" method="post" action="/admin/asignarTarjeta.action" id="formAsignar">
                        <div class="form-group col-sm-12 ">
                            <label class="col-sm-2 control-label">Tarjetas disponibles</label>
                            <div class="col-sm-10">
                                <s:textfield id="tarjetasDisponibles"
                                             cssClass="form-control"
                                             name="tarjetasDisponibles" 
                                             readonly="true"
                                             disabled="true"
                                             />
                            </div>
                        </div>
                        <div class="form-group col-sm-12">
                            <label class="col-sm-2 control-label">Identificador de la carga</label>
                            <div class="col-sm-10">
                                <s:textfield id="identificador"
                                             cssClass="form-control"
                                             name="identificador" 
                                             required="true" 
                                             data-bv-notempty="true"
                                             data-bv-notempty-message="Es necesario especificar un identificador de carga"
                                             />
                            </div>
                        </div>
                        <div class="form-group col-sm-12">
                            <label class="col-sm-2 control-label">Observaciones</label>
                            <div class="col-sm-10">
                                <s:textarea cssClass="form-control"
                                            name="observaciones"
                                            id="observaciones"
                                            placeholder="Observaciones"
                                            cols="45"
                                            rows="5" 
                                            data-bv-message="Este dato no es válido"
                                            required="true" 
                                            data-bv-notempty="true"
                                            data-bv-notempty-message="Las observaciones son requeridas"
                                            pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s1234567890]+$" 
                                            data-bv-regexp-message="No se permiten caracteres especiales"
                                            data-bv-stringlength="true" 
                                            data-bv-stringlength-min="3" 
                                            data-bv-stringlength-max="250" 
                                            maxlength="250" />
                            </div>
                        </div>
                        <s:hidden name="tipoAsignacion" id="tipoAsignacionh"/>
                        <s:hidden name="nivel" id="nivelh" />
                        <s:hidden name="boleta" id="boletah" />
                        <s:hidden name="unidadAcademica" id="unidadAcademicah"/>
                        <s:hidden name="periodo" id="periodoh" />
                        <div class="col-xs-12 form-group">
                            <div class="col-xs-offset-2 col-xs-10">
                                <button type="submit" class="btn btn-primary pull-right" id="ejecutar">Ejecutar</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div> <!-- Termina Div Asignar -->



    <div class="row" style="display: none;" id="div-datosAsignacion">
        <div class="col-xs-12">
            <div class="main-box clearfix" >
                <div class="main-box-body clearfix">
                    <form class="form-horizontal" method="post" action="/admin/revertirEnviarTarjeta.action">
                        <div class="form-group col-sm-12 ">
                            <label class="col-sm-2 control-label">Número de alumnos</label>
                            <div class="col-sm-10">
                                <input type="text" disabled class="form-control" id="noAlumnos"/>
                            </div>
                        </div>
                        <div class="form-group col-sm-12">
                            <label class="col-sm-2 control-label">Fecha de ejecución</label>
                            <div class="col-sm-10">
                                <input type="text" disabled class="form-control" id="fechaEjecucion"/>
                            </div>
                        </div>
                        <div class="form-group col-sm-12">
                            <label class="col-sm-2 control-label">Observaciones</label>
                            <div class="col-sm-10">
                                <textarea disabled class="form-control" id="observacionesEjecutados"  cols="45" rows="5" ></textarea>
                            </div>
                        </div>
                        <div class="form-group col-sm-12">
                            <label class="col-sm-2 control-label">¿Envío correo?</label>
                            <div class="col-sm-10">
                                <input type="text" disabled class="form-control" id="correoEjecutados"/>
                            </div>
                        </div>
                        <div class="col-xs-12 form-group" id="botonesRevertir">
                            <div class="btn-group pull-right">
                                <a href="" class="btn btn-primary" id="archivoPejecutados">Plantilla</a>
                                <a href="" class="btn btn-primary" id="archivoAejecutados">Personal</a>
                                <button type="submit" class="btn btn-primary" id="btn-deshacer">Deshacer</button>
                                <button type="submit" class="btn btn-primary" id="enviar">Enviar correos</button>
                            </div>
                        </div>
                        <s:hidden name="identificador" id="identificadorr"/>
                        <s:hidden name="operacion" id="operacion"/>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Tabla -->
    <div class="row" id="div-tabla" style="display: none;">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <div class="col-md-12" style="padding: 0px">
                    <div class="responsive">                        
                        <table id="listado" class="table table-striped table-hover dt-responsive" style="width: 100%">
                            <thead>
                                <tr>
                                    <th>Unidad Académica</th>
                                    <th>Nombre</th>
                                    <th>CURP</th>
                                    <th>Boleta</th>
                                    <th>No. Tarjeta</th>
                                </tr>
                            </thead>
                        </table>                        
                    </div>
                </div>
            </div>
        </div>                
    </div>
    <!--Termina Tabla --> 

</body>

<content tag="endScripts">
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js"></script>
    <script type="text/javascript" src="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.js"></script>
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>
                                    var tipoAsignacion,
                                            boleta,
                                            nivel,
                                            unidadAcademica,
                                            periodoId;
                                    $(document).ready(function () {

                                        $("#formAsignar").bootstrapValidator({
                                            feedbackIcons: {
                                                valid: 'glyphicon glyphicon-ok',
                                                invalid: 'glyphicon glyphicon-remove',
                                                validating: 'glyphicon glyphicon-refresh'
                                            }
                                        });

                                        $("#btn-deshacer").on('click', function () {
                                            $("#identificadorr").val($("#identificadorBusqueda").val());
                                            $("operacion").val(1); // etiqueta s oculta
                                        });

                                        $("#btn-busqueda-principal").on('click', function () {
                                            $('#div-datosAsignacion').css('display', 'none');

                                            tipoAsignacion = $('#tipoAsignacion option:selected').val();
                                            boleta = $('#boleta').val();
                                            nivel = $('#nivel option:selected').val();
                                            unidadAcademica = $('#unidadAcademica option:selected').val();
                                            claveUA = $('#unidadAcademica option:selected').attr('data-clave');
                                            periodoId = $('#periodo option:selected').val();
                                            var nuevoIdentifficador = "";

                                            if (claveUA < 10)
                                                claveUA = "0" + claveUA;
                                            if (tipoAsignacion == 2) {
                                                nuevoIdentifficador += "NU-";
                                            } else {
                                                nuevoIdentifficador += "RE-";
                                            }
                                            if ($.trim(boleta) == "") {
                                                if (nivel == 1) {
                                                    nuevoIdentifficador += "NMS-";
                                                } else if (nivel == 2) {
                                                    nuevoIdentifficador += "NS-";
                                                }
                                                if (unidadAcademica != 0) {
                                                    nuevoIdentifficador += claveUA + "-";
                                                }
                                                var per = $('#periodo option:selected').text().replace("-", "");
                                                nuevoIdentifficador += per + "-";
                                            } else {
                                                nuevoIdentifficador += boleta + "-";
                                            }

                                            $.ajax({
                                                type: 'POST',
                                                url: '/ajax/identificadorTarjetaAjax.action',
                                                dataType: 'json',
                                                data: {identificador: nuevoIdentifficador},
                                                cache: false,
                                                success: function (adata) {
                                                    $('#identificador').val(adata.data[0][0]);
                                                    $('#tarjetasDisponibles').val(adata.data[0][1]);
                                                }
                                            });
                                            $('#tipoAsignacionh').val(tipoAsignacion);
                                            $('#nivelh').val(nivel);
                                            $('#boletah').val(boleta);
                                            $('#unidadAcademicah').val(unidadAcademica);
                                            $('#periodoh').val(periodoId);
                                            $('#observaciones').val("");

                                            mostrarTabla();
                                        });
                                        $('#identificadorBusqueda').on('keypress', function (e) {
                                            if (e.which === 13) {
                                                $(this).attr("disabled", "disabled");
                                                buscar();
                                                $(this).removeAttr("disabled")
                                            }
                                        });
                                        $("#boton-TAsignadas").on('click', function (e) {
                                            e.preventDefault();
                                            $(this).attr("disabled", "disabled");
                                            buscar();
                                            $(this).removeAttr("disabled")
                                        });
                                    });

                                    function buscar() {
                                        $('#div-tabla').css('display', 'none');
                                        $('#div-asignar').css('display', 'none');

                                        $.ajax({
                                            type: 'POST',
                                            url: '/ajax/asignadosTarjetaAjax.action',
                                            data: {identificador: $("#identificadorBusqueda").val()},
                                            dataType: 'json',
                                            success: function (aData) {
                                                if (aData.data[0] != undefined) {
                                                    $('#div-datosAsignacion').css('display', 'block');
                                                    $("#noAlumnos").val(aData.data[0][0]);
                                                    $("#fechaEjecucion").val(aData.data[0][1]);
                                                    $("#observacionesEjecutados").val(aData.data[0][2]);
                                                    $("#correoEjecutados").val(aData.data[0][3]);
                                                    $("#archivoPejecutados").attr('href', aData.data[0][4] + "&tipoArchivo=2");
                                                    $("#archivoAejecutados").attr('href', aData.data[0][4] + "&tipoArchivo=1");
                                                    $("#botonesRevertir").show();
                                                } else {
                                                    $('#div-datosAsignacion').css('display', 'none');
                                                    ModalGenerator.notificacion({
                                                        "titulo": "Atención",
                                                        "cuerpo": "No hay asiganaciones con ese identificador de carga.",
                                                        "tipo": "WARNING",
                                                    });
                                                }
                                            },
                                            error: function () {
                                                ModalGenerator.notificacion({
                                                    "titulo": "Atención",
                                                    "cuerpo": "Hubo un problema que impidió que se completara la operación.",
                                                    "tipo": "WARNING",
                                                });
                                            }
                                        });
                                    }
                                    function mostrarTabla() {
                                        var url = '/ajax/asignacionTarjetaAjax.action?tipoAsignacion=' + tipoAsignacion + '&boleta=' + boleta + '&nivel=' + nivel + '&unidadAcademica=' + unidadAcademica + '&periodoId=' + periodoId;
                                        generarTabla("div-tabla", url);

                                        $('#div-asignar').css('display', 'block');
                                    }

                                    function getUnidades() {
                                        $.ajax({
                                            type: 'POST',
                                            url: '/ajax/getUnidadAcademicaAjax.action',
                                            dataType: 'json',
                                            data: {pkNivel: $('#nivel').val()},
                                            cache: false,
                                            success: function (aData) {

                                                $('#unidadAcademica').get(0).options.length = 0;
                                                var res = '';
                                                $.each(aData.data, function (i, item) {
                                                    res += "<option data-clave = '" + item[2] + "'value='" + item[0] + "'>" + item[1] + "</option>";
                                                });
                                                $('#unidadAcademica').html(res);
                                            },
                                            error: function () {
                                                ModalGenerator.notificacion({
                                                    "titulo": "Atención",
                                                    "cuerpo": "Hubo un problema que impidió que se completara la operación.",
                                                    "tipo": "WARNING",
                                                });
                                            }
                                        });
                                        return false;
                                    }
    </script>
</content>
