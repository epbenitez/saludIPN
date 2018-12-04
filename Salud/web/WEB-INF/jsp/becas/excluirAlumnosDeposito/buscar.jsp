<%-- 
    Document   : Excluir alumnos de depósito
    Created on : 26/04/2016, 11:44:09 AM
    Author     : Tania G. Sánchez
    Redesing   : Mario Márquez
--%>

<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <link rel="stylesheet" type="text/css" href="/vendors/kartik-bootstrap-input/fileinput.min.css" />
    <link rel="stylesheet" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css" type="text/css" />
    <link rel="stylesheet" type="text/css" href="/vendors/fancy-box/jquery.fancybox.min.css"/>      
    <title>Excluir/Incluir alumnos de depósito</title>    
</head> 

<content tag="tituloJSP">
    Excluir/Incluir alumnos de depósitos
</content>

<body>
    <div class="row" id="div-msj-error-filtros" style="display:none;">
        <div class="col-xs-12">
            <div class="alert alert-danger">
                <i class="fa fa-times-circle fa-fw fa-lg"></i>
                <strong>Error!</strong> Debes introducir al menos un criterio de búsqueda.
            </div>
        </div>
    </div>


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

    <div class="row">
        <div class="col-xs-12">
            <div class="main-box clearfix" >
                <div class="tabs-wrapper">
                    <ul class="nav nav-tabs">
                        <li class="active"><a id="tab-masiva" href="#panel-masiva" data-toggle="tab">Masivamente<i class="fa"></i></a></li>
                        <li><a id="tab-individual" href="#panel-individual" data-toggle="tab">Individualmente<i class="fa"></i></a></li>
                    </ul>
                    <div class="tab-content">
                        <div class="tab-pane fade in active" id="panel-masiva">
                            <div class="row">
                                <div class="main-box-body">
                                    <div class="row">
                                        <div class="col-xs-12">                                        
                                            <div class="panel-group accordion" id="accordion" style="width: 100%">
                                                <div class="panel panel-default">
                                                    <div class="panel-heading">
                                                        <h4 class="panel-title">
                                                            <a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
                                                                Instrucciones
                                                            </a>
                                                        </h4>
                                                    </div>
                                                    <div id="collapseOne" class="panel-collapse collapse">
                                                        <div class="panel-body">
                                                            <p>Descargue el archivo de ejemplo e ingrese los números de 
                                                                boleta de los alumnos que desea excluir de depósito. Una vez 
                                                                que haya finalizado, cargue el archivo al sistema para poder 
                                                                ejecutar y visualizar los cambios correspondientes.</p>
                                                            <ul>
                                                                <li>El valor elegido en el campo "Estatus" permitirá que un alumno sea visible, o no, por el módulo de generación de órdenes de depósito. Si se selecciona "Excluir", el alumno no será elegible en una dispersión.</li>
                                                                <li>El campo "Identificador de otorgamiento" permitirá establecer una razón para la exclusión de un otorgamiento, facilitando el seguimiento del estatus del alumno.</li>
                                                                <li>La boleta que incluya/excluya en el excel, será incluida/excluida en todos sus otorgamientos.</li>
                                                                <li>Para aplicar el mismo identificador de otorgamiento a todas las boletas del archivo, elija el identificador correspondiente desde el combo <b>Identificador de otorgamientos.</b></li>
                                                                <li>Si prefiere asignar un identificador en específico para cada una de las boletas, agrégelo a la columna correspondiente para cada boleta, y active la opción <b>Procesar desde Excel.</b></li>
                                                            </ul>
                                                            <div class="col-xs-12 text-center" style="padding-bottom: 5px" >
                                                                <a href="/resources/downloadable/Ejemplo_excluir_alumnos.xlsx" id="bton-descargar" class="btn btn-primary btn-lg">
                                                                    <i class='fa fa-download'></i> Archivo de ejemplo
                                                                </a>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div> 
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-xs-12">
                                            <form id="form-excluir-masivo" action="/becas/cargarExcluirAlumnosDeposito.action" method="post" enctype="multipart/form-data" class="form-horizontal">
                                                <div class="form-group">
                                                    <label class="col-sm-3 control-label">Periodo</label>
                                                    <div class="col-sm-8">
                                                        <s:select id="periodoM" name="periodoM" cssClass="form-control"
                                                                  list="ambiente.periodoList" listKey="id" listValue="clave" 
                                                                  headerKey="" headerValue="-- Selecciona un periodo --" 
                                                                  data-bv-notempty="true" 
                                                                  data-bv-notempty-message="El periodo es requerido."
                                                                  required="true"/>
                                                        <span class="help-block" id="nombreMessage" />
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-sm-3 control-label">Identificador de otorgamientos</label>
                                                    <div class="col-sm-3">
                                                        <s:select id="select-identificador" name="idOtorgamientoId" cssClass="form-control"
                                                                  list="idsOtorgamientos" listKey="id" listValue="nombre" 
                                                                  data-bv-notempty="true" 
                                                                  data-bv-notempty-message="Es necesario seleccione un identificador."
                                                                  required="true"/>
                                                        <span class="help-block" id="nombreMessage" />
                                                    </div>
                                                    <div class="form-check checkbox-nice checkbox-inline col-sm-offset-1">
                                                        <input id="checkbox-excel" 
                                                               type="checkbox" 
                                                               name="checkboxExcel" 
                                                               >
                                                        <label for="checkbox-excel">
                                                            Procesar desde Excel
                                                        </label>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="control-label col-sm-3">Archivo Excel</label>
                                                    <div class="col-sm-8">
                                                        <s:file id="archivo" class="file" 
                                                                data-bv-notempty="true" labelposition="left" 
                                                                accept="application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" 
                                                                name="upload"  data-show-preview="false" />
                                                    </div>
                                                </div>
                                                <div class="form-group text-center">
                                                    <input type="hidden" name="accion" id="accion">
                                                    <div class="btn-group" role="group" aria-label="Excluir o incluir lista de alumnos">
                                                        <button class="btn btn-primary solo-lectura" type="submit" onclick="document.getElementById('accion').value = 1;">Excluir lista alumnos</button>
                                                        <button id="botonIncluir" class="btn btn-primary solo-lectura" type="submit" onclick="document.getElementById('accion').value = 0;">Incluir lista alumnos</button>
                                                    </div>                                                    
                                                </div>
                                            </form>
                                        </div>
                                    </div>                                        
                                </div>
                            </div>                            
                        </div>  
                        <div class="tab-pane fade" id="panel-individual">
                            <div class="main-box-body">
                                <form id="form-excluir-individual" method="post" class="form-horizontal" action="/becas/buscarExcluirAlumnosDeposito.action">
                                    <div class="form-group">
                                        <label class="col-xs-2 control-label">Periodo</label>
                                        <div class="col-xs-10">
                                            <s:select id="periodoI" 
                                                      cssClass="form-control"
                                                      name="periodoI"
                                                      list="ambiente.periodoList" 
                                                      listKey="id" 
                                                      listValue="clave" 
                                                      headerKey=""
                                                      headerValue="-- Selecciona un periodo --" 
                                                      data-bv-notempty="true" 
                                                      data-bv-notempty-message="El periodo es requerido."
                                                      required="true"/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-2 control-label">Número de boleta</label>
                                        <div class="col-xs-10">
                                            <s:textfield cssClass="form-control" name="numeroBoleta" id="numeroBoleta" placeholder="Número de boleta"
                                                         value="" data-bv-message="Este dato no es válido"
                                                         pattern="^[a-zA-Z0-9\s]+$" 
                                                         data-bv-regexp-message="El número de boleta sólo puede estar conformado por números"
                                                         data-bv-stringlength="true" 
                                                         data-bv-stringlength-min="10" 
                                                         data-bv-stringlength-message="La boleta debe ser de 10 dígitos"
                                                         data-bv-stringlength-max="10" 
                                                         maxlength="10"
                                                         onblur="$(this).val($(this).val().toUpperCase())" />
                                        </div>                                        
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-2 control-label">Nombre</label>
                                        <div class="col-xs-4">
                                            <s:textfield cssClass="form-control" 
                                                         name="nombre" 
                                                         id="nombre" 
                                                         placeholder="Nombre"
                                                         value="" 
                                                         data-bv-message="Este dato no es válido"
                                                         pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s]+$"  
                                                         data-bv-regexp-message="El nombre sólo puede estar conformado por letras"
                                                         data-bv-stringlength="true" 
                                                         maxlength="16"/>
                                        </div>
                                        <div class="col-xs-3">
                                            <s:textfield cssClass="form-control" name="aPaterno" id="aPaterno" placeholder="Apellido paterno"
                                                         value="" data-bv-message="Este dato no es válido"
                                                         pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s]+$"  
                                                         data-bv-regexp-message="El apellido paterno sólo puede estar conformado por letras"
                                                         data-bv-stringlength="true" 
                                                         maxlength="16"/>
                                        </div>
                                        <div class="col-xs-3">
                                            <s:textfield cssClass="form-control" name="aMaterno" id="aMaterno" placeholder="Apellido materno"
                                                         value="" data-bv-message="Este dato no es válido"
                                                         pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s]+$"  
                                                         data-bv-regexp-message="El apellido materno sólo puede estar conformado por letras"
                                                         data-bv-stringlength="true" 
                                                         maxlength="16"/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="col-xs-12">
                                            <button id="bton-buscar" class="btn btn-primary pull-right" type="submit">Buscar</button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>                    
                </div>
            </div>
        </div>
    </div> 
    <s:if test="%{!log.isEmpty}">
        <div class="row" id="div-tabla-carga">
            <div class="col-xs-12">
                <div class="main-box clearfix">
                    <div class="col-md-12" style="padding: 0px">
                        <div class="responsive">  
                            <table id="log" class="table table-striped table-hover dt-responsive" style="width: 100%">
                                <thead>
                                    <tr>
                                        <th>Número de boleta</th>
                                        <th>Nombre</th>
                                        <th>Identificador de Otorgamiento</th>
                                        <th>Estatus</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <s:iterator value="log" >
                                        <tr>
                                            <s:property escape="false" ></s:property>
                                            </tr>
                                    </s:iterator>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </s:if>
    <!-- Tabla búsqueda -->
    <div class="row" id="div-tabla-buscar" style="display: none;">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <div class="col-md-12" style="padding: 0px">
                    <div class="responsive">                        
                        <table id="listado" class="table table-striped table-hover dt-responsive" style="width: 100%">
                            <thead>
                                <tr>
                                    <th>Número de boleta</th>
                                    <th>Nombre</th>
                                    <th>Beca</th>
                                    <th>Estatus</th>
                                    <th></th>
                                </tr>
                            </thead>
                        </table>                        
                    </div>
                </div>
            </div>
        </div>
        <form id="excluirForm" action="/becas/excluirExcluirAlumnosDeposito.action" method="POST" >
            <input type="hidden" name="otorgamiento.id" id="idE" value="" />
        </form>
        <form id="incluirForm" action="/becas/incluirExcluirAlumnosDeposito.action" method="POST" >
            <input type="hidden" name="otorgamiento.id" id="idI" value="" />
        </form>
    </div>
    <!--Termina tabla búsqueda -->     
</body>

<content tag="endScripts">
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js"></script>
    <script type="text/javascript" src="/vendors/kartik-bootstrap-input/fileinput.min.js"></script>
    <script type="text/javascript" src="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.js"></script>
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>
    <script type="text/javascript" src="/vendors/fancy-box/jquery.fancybox.min.js"></script>    
</content>

<content tag="inlineScripts">
    <script>
                                                            $(document).ready(function () {
                                                                $('#tab-masiva').click(function () {
                                                                    $('#div-tabla-buscar').hide();
                                                                    $('#paginateFooter').hide();
                                                                    $('#dataTableHeader').hide();
                                                                    $("#div-msj-error-filtros").hide();
                                                                });

                                                                $('#checkbox-excel').change(function (element) {
                                                                    toggleSelect(this);
                                                                });

                                                                $("#tab-individual").click(function () {
                                                                    $("#div-tabla-carga").hide();
                                                                });

                                                                $('#form-excluir-masivo').bootstrapValidator().on('success.form.bv', function (e) {
                                                                    ModalGenerator.notificacion({
                                                                        "titulo": "Cargando...",
                                                                        "cuerpo": "Estamos validando, favor de no cerrar la ventana ni presionar 'atrás' en el navegador.",
                                                                        "tipo": "WARNING",
                                                                        "sePuedeCerrar": false
                                                                    });
                                                                });

                                                                $('#form-excluir-individual').bootstrapValidator({
                                                                    feedbackIcons: {
                                                                        valid: 'glyphicon glyphicon-ok',
                                                                        invalid: 'glyphicon glyphicon-remove',
                                                                        validating: 'glyphicon glyphicon-refresh'
                                                                    }
                                                                }).on('success.form.bv', function (e) {
                                                                    e.preventDefault();

                                                                    if (validarNoCamposVacios()) {
                                                                        $("#div-msj-error-filtros").hide();
                                                                        mostrarTablaIndividual();
                                                                    } else {
                                                                        $("#form-excluir-individual").data('bootstrapValidator').resetForm();
                                                                        $("#bton-buscar").blur();
                                                                        $("#div-msj-error-filtros").show();
                                                                    }
                                                                });

                                                                $("#archivo").fileinput({
                                                                    showUpload: false,
                                                                    allowedFileExtensions: ['xls', 'xlsx']
                                                                });

                                                                $("#cargaForm").submit(function () {
                                                                    ModalGenerator.notificacion({
                                                                        "titulo": "Cargando...",
                                                                        "cuerpo": "Estamos validando, favor de no cerrar la ventana ni presionar 'atrás' en el navegador.",
                                                                        "tipo": "WARNING",
                                                                        "sePuedeCerrar": false
                                                                    });
                                                                });
                                                            });

                                                            function toggleSelect(element) {
                                                                if (element.checked) {
                                                                    $("#select-identificador").prop("disabled", true);
                                                                } else {
                                                                    $("#select-identificador").prop("disabled", false);
                                                                }
                                                            }

                                                            function excluir(id) {
                                                                ModalGenerator.notificacion({
                                                                    "titulo": "Atención",
                                                                    "cuerpo": "Esto excluirá al alumno de los depósitos para el periodo seleccionado, ¿Está seguro de querer excluirlo?",
                                                                    "tipo": "WARNING",
                                                                    "funcionAceptar": function () {
                                                                        $("#idE").val(id);
                                                                        $("#excluirForm").submit();
                                                                    }
                                                                });
                                                            }

                                                            function incluir(id) {
                                                                ModalGenerator.notificacion({
                                                                    "titulo": "Atención",
                                                                    "cuerpo": "Esto incluirá al alumno en los depósitos para el periodo selccionado, ¿Está seguro de querer incluirlo?",
                                                                    "tipo": "WARNING",
                                                                    "funcionAceptar": function () {
                                                                        $("#idI").val(id);
                                                                        $("#incluirForm").submit();
                                                                    }
                                                                });
                                                            }

                                                            function mostrarTablaIndividual() {
                                                                var url = '/ajax/listadoExcluirDepositoAlumnosAjax.action';
                                                                var periodoI = $('#periodoI').val();
                                                                var numeroBoleta = $('#numeroBoleta').val();
                                                                var nombre = $('#nombre').val();
                                                                var aPaterno = $('#aPaterno').val();
                                                                var aMaterno = $('#aMaterno').val();

                                                                url = url + "?periodoI=" + periodoI + "&numeroBoleta=" + numeroBoleta + "&nombre=" + nombre + "&aPaterno=" + aPaterno + "&aMaterno=" + aMaterno;

                                                                generarTabla("div-tabla-buscar", url, "despuesCargarTabla", false);
                                                            }

                                                            function despuesCargarTabla() {
                                                                $('.fancybox').fancybox({
                                                                    autoSize: true,
                                                                    afterClose: function () {
                                                                        mostrarTablaIndividual();
                                                                    }
                                                                });
                                                            }

                                                            function validarNoCamposVacios() {
                                                                var esValido = false;
                                                                $('#form-excluir-individual *').filter('[type=text]').each(function () {
                                                                    if ($(this).val().length !== 0) {
                                                                        esValido = true;
                                                                    }
                                                                });
                                                                return esValido;
                                                            }
    </script>
</content>