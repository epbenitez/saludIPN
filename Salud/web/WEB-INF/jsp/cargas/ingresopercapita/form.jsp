<%-- 
    Document   : Carga masiva de ingresos per cápita.
    Created on : 30-Noviembre-2016
    Author     : Tania Gabriela Sánchez Manilla
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<head>
    <title>Ingresos per cápita (Carga Masiva)</title>
    <link rel="stylesheet" type="text/css" href="/vendors/kartik-bootstrap-input/fileinput.min.css" />
</head> 
<content tag="tituloJSP">
    Ingresos per cápita (Carga Masiva)
</content>
<body>
    <div class="col-sm-12">
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
                    <div id="collapseOne" class="panel-collapse collapse">
                        <div class="panel-body">
                            <p>Descargue el archivo de ejemplo e ingrese los datos de los alumnos que presentaron la documentación que el dato de sus ingresos per cápita es incorrecto. Una vez que haya finalizado, cargue el archivo al sistema para almacenar la información actualizada.</p>
                            <div class="col-xs-12 text-center" style="padding-bottom: 5px" >
                                <a href="/resources/downloadable/Ejemplo_carga_ingreso_per_capita.xlsx" class="btn btn-primary btn-lg">
                                    <i class='fa fa-download'></i> Archivo de ejemplo
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div> 
        </div>
    </div>
    <!-- Ejemplo para formularios -->
    <div class="row">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <div class="main-box-body">
                    <s:form cssClass="form-horizontal" id="cargaForm" namespace="/carga" action="cargaIngresoPerCapita.action" method="POST" enctype="multipart/form-data">
                        <div class="form-group">
                            <label for="archivo" class="control-label col-sm-2">Selecciona el archivo Excel</label>
                            <div class="col-sm-8">
                                <s:file id="archivo" cssClass="file form-control" labelposition="left" 
                                        accept="application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" 
                                        name="upload" data-show-preview="false"
                                        data-bv-notempty="true" 
                                        data-bv-notempty-message="Es necesario que selecciones un archivo."/>
                            </div>
                            <div class="col-sm-2">
                                <input type="submit"  class="btn btn-primary" value="Cargar archivo"/>
                            </div>
                        </div>
                        <s:hidden id="noLote" name="noLote" />
                    </s:form>
                </div>
            </div>
        </div>
    </div>
    <!--Termina ejemplo para formularios -->
</body>

<content tag="endScripts">
    <script type="text/javascript" src="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.js"></script>
    <script type="text/javascript" src="/vendors/kartik-bootstrap-input/fileinput.min.js"></script>
    <script type="text/javascript" src="/vendors/kartik-bootstrap-input/locales/es.js"></script>
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>

        $("#cargaForm").bootstrapValidator()
                .on('success.form.bv', function (e) {
                    ModalGenerator.notificacion({
                        "titulo": "Estamos procesando tu solicitud...",
                        "cuerpo": "Estamos actualizando la información de los alumnos, favor de no cerrar la ventana ni presionar atrás en el navegador.",
                        "tipo": "INFO",
                        "sePuedeCerrar": false
                    });
                });

        $("#archivo").fileinput({
            showUpload: false,
            language: "es",
            allowedFileExtensions: ['xls', 'xlsx']
        });

//        $("#cargaForm").submit(function () {
//
//        });
    </script>
</content>