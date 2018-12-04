<%@taglib prefix="s" uri="/struts-tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Envio de Correos</title>
    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css" />
    <link rel="stylesheet" type="text/css" href="/vendors/kartik-bootstrap-input/fileinput.min.css"/>
</head>

<content tag="tituloJSP">
    Envio de Correos
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
    <s:if test="%{hasActionErrors() || hasActionMessages()}">
        <!-- Inicia Resumen -->
        <div class="row" id="resume-div">        
            <div class="col-xs-6">
                <div class="main-box small-graph-box green-bg">
                    <span class="value" id="resume-value"><s:property value="totalSent"/></span>
                    <span class="headline">Correctos</span>
                    <span class="subinfo" id="resume-resmn">
                        <i class="fa fa-check"></i> 
                        <span>Enviados correctamente</span>
                    </span>
                </div>
            </div>
            <div class="col-xs-6">
                <div class="main-box small-graph-box red-bg">
                    <span class="value" id="fail-resume-value"><s:property value="totalFailed"/></span>
                    <span class="headline">Fallidos</span>
                    <span class="subinfo" id="fail-resume-resmn">
                        <i class="fa fa-times"></i> 
                        <span>No pudieron ser enviados</span>
                    </span>
                </div>
            </div>
        </div> <!-- Termina Resumen -->
    </s:if>
    <s:if test="hasActionErrors()">
        <div class="row" id="rowTable">
            <div class="col-xs-12">
                <div class="main-box clearfix">
                    <div class="col-md-12" style="padding: 0px">
                        <div class="responsive">                        
                            <table id="table" class="table table-striped table-hover dt-responsive" style="width: 100%">
                                <thead>
                                    <tr>
                                        <th>Boleta</th>                                                            
                                        <th>Nombre</th>                                                            
                                        <th>Correo</th>                                    
                                    </tr>
                                </thead>
                            </table>                        
                        </div>
                    </div>
                </div>
            </div>                
        </div>
    </s:if>
    <!-- Inicia Progreso -->
    <div class="row" style="display: none;" id="progress-div">        
        <div class="col-xs-12">
            <div class="main-box small-graph-box purple-bg">
                <span class="value" id="progress-value">0%</span>
                <span class="headline">Completado</span>
                <div class="progress">
                    <div id="progress" style="width: 0%;" aria-valuemax="0" aria-valuemin="0" aria-valuenow="0" role="progressbar" class="progress-bar">
                        <span class="sr-only">0% completado</span>
                    </div>
                </div>
                <span class="subinfo" id="progress-resmn">
                    <i class="fa fa-check"></i> 
                    <span></span>
                </span>
                <span class="subinfo" id="no-progress-resmn">
                    <i class="fa fa-times"></i> 
                    <span></span>
                </span>
                <span class="subinfo" id="description">
                    <i class="fa fa-info"> </i> 
                    <span></span>
                </span>
            </div>
        </div>
    </div> <!-- Termina Progreso -->
    <!-- Form -->
    <s:if test="!hasActionMessages() && !hasActionErrors()">
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
                            <ul>
                                <li>
                                    El envío de correos es un proceso que lleva tiempo. Considera una taza de 200 correos por minuto
                                </li>

                                <li>
                                    Te recomendamos enviar un correo de prueba, antes de hacer el envío definitivo para que puedas
                                    confirmar el contenido de tu correo
                                </li>

                                <li>
                                    El correo de prueba será enviado a la cuenta de correo que tienes registrada en el sistema
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div> 
        <div class="row">
            <div class="col-xs-12">
                <div class="main-box clearfix">
                    <div class="row">
                        <div class="main-box-body">
                            <s:form id="enviarCorreosForm" name="enviarCorreosForm" method="POST" enctype="multipart/form-data" cssClass="form-horizontal">
                                <div class="form-group" >
                                    <label class="col-xs-2 control-label">Asunto*</label>
                                    <div class="col-sm-9">
                                        <s:textfield id="asunto"
                                                     name="asunto" 
                                                     cssClass="form-control"
                                                     data-bv-notempty="true"
                                                     data-bv-notempty-message="El asunto es requerido"
                                                     placeholder="Asunto"
                                                     />
                                    </div>
                                </div>
                                <div class="form-group" >
                                    <label class="col-xs-2 control-label">Atentamente</label>
                                    <div class="col-sm-9">
                                        <s:textfield id="atentamente"
                                                     name="atentamente" 
                                                     cssClass="form-control"
                                                     placeholder="Atentamente"
                                                     />
                                    </div>
                                </div>
                                <security:authorize ifAnyGranted="ROLE_JEFEBECAS">
                                <div class="form-group">
                                    <label class="control-label col-sm-2" for="archivo" >Adjunto</label>
                                    <div class="col-sm-9">
                                        <s:file 
                                            id="archivo" 
                                            cssClass="file form-control" 
                                            labelposition="left" 
                                            accept="application/vnd.ms-excel,
                                            application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,
                                            application/pdf,
                                            image/jpeg,
                                            image/png" 
                                            name="upload" />
                                    </div>
                                </div>
                                </security:authorize>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label">Mensaje*</label>
                                    <div class="col-sm-9">
                                        <s:textarea cssClass="form-control"
                                                    name="mensaje"
                                                    id="mensaje"
                                                    cols="50" rows="10" 
                                                    data-bv-message="Este dato no es válido"
                                                    required="true" 
                                                    data-bv-notempty="true"
                                                    data-bv-notempty-message="El mensaje es requerido"
                                                    data-bv-stringlength="true" 
                                                    data-bv-stringlength-min="3" 
                                                    data-bv-stringlength-max="5000"/>
                                    </div>
                                    <div class="col-xs-12 col-sm-offset-2">
                                        <p class="help-block">Los campos marcados con un asterisco (*) son obligatorios.</p>
                                    </div>
                                </div>                                
                                <div class="form-group col-xs-12">
                                    <s:hidden name="opcion" />
                                    <s:hidden name="nivel" />
                                    <s:hidden name="unidadAcademica" />
                                    <s:hidden name="beca" />
                                    <s:hidden name="tipoBeca" />
                                    <s:hidden name="movimiento" />
                                    <s:hidden name="proceso" />
                                    <s:hidden name="alumnos" />
                                    <s:hidden name="alumnosL" />
                                    <s:hidden name="periodo" />                                    
                                    <s:submit type="button" value="Enviar" cssClass="btn btn-primary pull-right" id="btn-enviar" />
                                    <s:submit type="button" value="Enviar prueba" cssClass="btn btn-default pull-right" id="btn-enviar-prueba" />
                                </div>
                            </s:form>
                        </div>
                    </div>
                </div>
            </div>
        </div> <!-- Termina Form -->
    </s:if>              
</body>

<content tag="endScripts">
    <s:if test="hasActionErrors()">
        <script>
            function despuesCargarTabla() {
                $(".buttons-copy, .buttons-print, .buttons-pdf").hide();
            }
            var botonExcel = {
                extend: 'csv',
                text: 'Descargar excel',
                className: 'btn-primary',
                exportOptions: {
                    columns: [1, 2, 3]
                },
                customize: function (data1, data2) {
                    return "\uFEFF" + data1;
                },
                filename: "EnviosError"
            };
            var dataSets = new Array();
            <s:iterator value="alumnosError">
            dataSets.push(["<s:property value="boleta"/>",
                "<s:property value="apellidoPaterno"/>" + " " + "<s:property value="apellidoMaterno"/>" + " " + "<s:property value="nombre"/>",
                "<s:property value="correoElectronico"/>"
            ]);
            </s:iterator>
        </script>  
        <script type="text/javascript" src="/resources/js/generador-tablas.min.js" data-init="true" data-div="rowTable" data-button="botonExcel" data-ajax="dataSets" data-function="despuesCargarTabla"></script>
    </s:if>
    <script type="text/javascript" src="/vendors/kartik-bootstrap-input/fileinput.min.js"></script>
    <script type="text/javascript" src="/vendors/kartik-bootstrap-input/locales/es.js"></script>
    <script type="text/javascript" src="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.js"></script>
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>
            $(document).ready(function () {
                var enviado = false;
                var archivoCorrecto = true;
                $("#enviarCorreosForm").bootstrapValidator({
                    feedbackIcons: {
                        valid: 'glyphicon glyphicon-ok',
                        invalid: 'glyphicon glyphicon-remove',
                        validating: 'glyphicon glyphicon-refresh'
                    }
                }).on('success.form.bv', function (e) {
                    e.preventDefault();
                    if (archivoCorrecto) {
                        ModalGenerator.notificacion({
                            "titulo": "Estas apunto de hacer un envío",
                            "cuerpo": "¿Estas seguro de continuar?",
                            "tipo": "WARNING",
                            "funcionAceptar": function () {
                                $("#enviarCorreosForm").unbind('submit').submit();
                                enviado = true;
                            }
                        });
                    } else {
                        $("#btn-enviar, #btn-enviar-prueba").prop('disabled', false);
                    }
                });

                $('#archivo').on('fileerror', function (event, data, msg) {
                    archivoCorrecto = false;
                }).on('filecleared', function (event) {
                    archivoCorrecto = true;                    
                });

                $("#btn-enviar").on("click", function () {
                    $("#enviarCorreosForm").attr("action", "/admin/enviarCorreosEnvioCorreos.action")
                });
                $("#btn-enviar-prueba").on("click", function () {
                    $("#enviarCorreosForm").attr("action", "/admin/enviarPruebaEnvioCorreos.action")
                });

                setInterval(function () {
                    if (enviado) {
                        updateProgress();
                        $('#progress-div').show();
                    }
                }, 500);
            });

            $("#archivo").fileinput({
                showUpload: false,
                language: "es",
                allowedFileExtensions: ['xls', 'xlsx', 'jpg', 'jpeg', 'png', 'pdf'],
                maxFileSize: 2000
            });

            function updateProgress() {
                var op = "<s:property value="opcion" />";
                $.ajax({
                    type: 'POST',
                    url: '/ajax/sincronizaEnvioCorreosAjax.action',
                    dataType: 'json',
                    data: {
                        option: op
                    },
                    cache: false,
                    success: function (aData) {
                        manipularRespuesta(aData);
                    },
                    error: function () {
                        console.log("Hubo un problema que impidió que se completara la operación.");
                    }
                });
            }

            function manipularRespuesta(aData) {
                if (aData.data[0] === undefined)
                    return null;
                var elements = aData.data[0].toString().split(",");
                var porcentaje = elements[0];
                var valActual = elements[8];
                $('#progress').css("width", porcentaje + "%");
                $('#progress span').html(porcentaje + "% completado");
                $('#progress').html("aria-valuemax", elements[3]);
                $('#progress').html("aria-valuenow", valActual);
                $('#progress-value').html(porcentaje + "%");
                $('#progress-resmn span').html(valActual + " elementos enviados correctamente");
                $('#no-progress-resmn span').html(elements[9] + " envios fallidos");
                $('#description span').html(" " + elements[10]);
            }

    </script>
</content>