<%-- 
    Document   : permitir
    Created on : 6/04/2017, 12:17:37 PM
    Author     : Tania G. Sánchez
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Ingreso de cuentas Banamex Externas</title>    
    
    <link rel="stylesheet" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css" type="text/css" />
    <link rel="stylesheet" type="text/css" href="/vendors/kartik-bootstrap-input/fileinput.min.css" />
</head> 

<content tag="tituloJSP">
    Ingreso de cuentas Banamex Externas
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
                            <p>Descargue el archivo de ejemplo e ingrese los números de boleta de los alumnos a los que se les permitira 
                                ingresar una cuenta Banamex externa. Una vez que haya finalizado, cargue el archivo al sistema para poder 
                                ejecutar y visualizar los cambios correspondientes.</p>
                            <div class="col-xs-12 text-center" style="padding-bottom: 5px" >
                                <a href="/resources/downloadable/Ejemplo_ingreso_cuentas_banamex.xlsx" id="bton-descargar" class="btn btn-primary btn-lg">
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
            <div class="main-box clearfix">
                <div class="main-box-body">
                    <form id="form-excluir-masivo" action="/admin/cargarCuentasExternas.action" method="post" enctype="multipart/form-data" class="form-horizontal">
                        <div class="form-group">
                            <label class="control-label col-sm-2">Archivo Excel</label>
                            <div class="col-sm-8" style="padding-bottom: 5px">
                                <s:file id="archivo" cssClass="file form-control" labelposition="left" 
                                        accept="application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" 
                                        name="upload"  data-show-preview="false"
                                        data-bv-notempty="true" 
                                        data-bv-notempty-message="Es necesario que selecciones un archivo."/>
                            </div>
                            <div class="col-sm-2">
                                <input type="hidden" name="accion" id="accion">
                                <button id="botonExcluir" class="btn btn-primary" type="submit">Cargar archivo</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <!--Termina ejemplo para formularios -->

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
</body>

<content tag="endScripts">
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js"></script>
    <script type="text/javascript" src="/vendors/kartik-bootstrap-input/fileinput.min.js"></script>
    <script type="text/javascript" src="/vendors/kartik-bootstrap-input/locales/es.js"></script>
    <script type="text/javascript" src="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.js"></script>
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>
        $('#form-excluir-masivo').bootstrapValidator().on('success.form.bv', function (e) {
            ModalGenerator.notificacion({
                "titulo": "Cargando...",
                "cuerpo": "Estamos validando, favor de no cerrar la ventana ni presionar 'atrás' en el navegador.",
                "tipo": "WARNING",
                "sePuedeCerrar": false
            });
        });

        $("#archivo").fileinput({
            showUpload: false,
            language: "es",
            allowedFileExtensions: ['xls', 'xlsx']
        });
    </script>
</content>