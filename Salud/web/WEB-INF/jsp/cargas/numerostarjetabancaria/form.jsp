<%-- 
    Document   : form
    Created on : 07-ene-2016, 16:44:12
    Author     : Patricia Benitez    
    Redesign   : Gustavo Adolfo Alamillo Medina
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Carga de nuevos números de tarjetas bancarias</title>
    <link rel="stylesheet" type="text/css" href="/vendors/kartik-bootstrap-input/fileinput.min.css"/>
    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css"/>
    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap-datepicker/css/bootstrap-datepicker.min.css"/>
</head> 

<content tag="tituloJSP">
    Carga de nuevos números de tarjetas bancarias
</content>

<body>

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

    <!-- Ejemplo de caja blanca -->
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
                                En caso de que requiera cargar nuevos números de tarjeta a un lote existente,
                                simplemente deberá seleccionar el lote correspondiente en el combo "Lote" y 
                                los nuevos números de tarjeta serán agregados al lote seleccionado.
                            </li>

                            <li>
                                En caso de que requiera cargar nuevos números de tarjeta a un lote nuevo, 
                                simplemente seleccione la opción al final del combo que comienza con el texto 
                                "Nuevo...", de esta manera se generará un nuevo número de lote en el que 
                                serán agregados los nuevos números de tarjeta.
                            </li>

                            <li>
                                En caso de que el archivo proporcionado tenga contraseña, simplemente escriba
                                la contraseña en el cuadro de texto "Contraseña del archivo" y de esta manera
                                no habrá ningún problema para poder cargar los nuevos números de tarjeta.
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div> <!-- Termina ejemplo caja blanca -->


    <!-- Ejemplo de caja blanca -->
    <div class="row">
        <div class="col-xs-12">
            <div class="main-box" >
                <div class="main-box-body clearfix">
                    <s:form id="cargaForm" action="cargaNumerosTarjetaBancaria.action" method="POST" enctype="multipart/form-data" cssClass="form-horizontal">
                        <div class="form-group">
                            <label class="control-label col-sm-2">Lote</label>
                            <div class="col-sm-10">
                                <s:select id="lote"  
                                          cssClass="form-control"
                                          name="lote"
                                          list="lote" 
                                          required="true" 
                                          data-bv-notempty="true"
                                          data-bv-notempty-message="El número de lote es requerido"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2">Fecha del lote</label>
                            <div class="col-sm-10">
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                    <input type="text" class="form-control" id="fechaLote" name="fechaLote"
                                           value="<s:date name="fechaL" format="dd/MM/yyyy" />"
                                           data-bv-notempty="true"
                                           data-bv-message="Este dato no es válido"
                                           required="true" 
                                           placeholder="Fecha del lote"
                                           cssClass="form-control"
                                           onkeydown="return false"
                                           >
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2">Contraseña del archivo</label>
                            <div class="col-sm-10">
                                <s:textfield class="form-control" 
                                             cssClass="form-control"
                                             name="password" 
                                             id="password" 
                                             placeholder="Contraseña del archivo"/>
                                <span class="help-block" >En caso que el archivo tenga contraseña</span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2">Archivo excel con números de tarjeta</label>
                            <div class="col-sm-10">
                                <s:file id="archivo" class="file form-control" 
                                        labelposition="left" 
                                        accept="application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" 
                                        name="upload" 
                                        data-bv-notempty="true" 
                                        data-bv-notempty-message="Es necesario que selecciones un archivo."
                                        data-show-preview="false" />
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-12">
                                <button type="submit" id="cargar" class="pull-right btn btn-success">Cargar nuevos números</button>
                            </div>
                        </div>
                        <s:hidden id="noLote" name="noLote" />
                    </s:form>
                </div>
            </div>
        </div>
    </div> <!-- Termina ejemplo caja blanca -->

</body>

<content tag="endScripts">
    <script type="text/javascript" src="/vendors/kartik-bootstrap-input/fileinput.min.js"></script>
    <script type="text/javascript" src="/vendors/kartik-bootstrap-input/locales/es.js"></script>
    <script type="text/javascript" src="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.js"></script>
    <script type="text/javascript" src="/vendors/bootstrap-datepicker/js/bootstrap-datepicker.min.js"></script>
    <script type="text/javascript" src="/vendors/bootstrap-datepicker/locales/bootstrap-datepicker.es.min.js"></script>
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>
        $(document).ready(function () {
            $("#cargaForm").bootstrapValidator();
            $("#archivo").fileinput({
                showUpload: false,
                allowedFileExtensions: ['xls', 'xlsx'],
                language: "es"
            });
            var regExp = /\(([^)]+)\)/;
            var matches = regExp.exec($('#lote option:selected').text());
            $("#noLote").val("" + (matches ? matches[1] : $('#lote option:selected').text()));
            $('#fechaLote').datepicker({
                language: 'es',
                format: 'dd/mm/yyyy'
            });

            $("#lote").on('keyup change mouseup', function () {
                $("#noLote").attr("type", "text");
                $("#noLote").val("" + (matches ? matches[1] : $('#lote option:selected').text()));
                $("#noLote").attr("type", "hidden");
            });
            $("option").removeAttr("selected");

            $("#cargaForm").bootstrapValidator().on('success.form.bv', function (e) {
                ModalGenerator.notificacion({
                    "titulo": "Cargando información",
                    "cuerpo": "Estamos guardando los números, favor de no cerrar la ventana ni presionar 'atrás' en el navegador.",
                    "tipo": "WARNING",
                    "sePuedeCerrar": false
                });
            });

        });
    </script>
</content>