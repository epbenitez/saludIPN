<%-- 
    Document   : form
    Created on : 28/10/2016, 12:44:47 PM
    Author     : Rafael Cardenas Resendiz
--%>

<%@taglib prefix="s" uri="/struts-tags" %>
<head>
    <title>Registro SUBES</title>
    <link rel="stylesheet" href="/vendors/kartik-bootstrap-input/fileinput.min.css" type="text/css" />
</head>

<content tag="tituloJSP">
    Registro SUBES
</content>

<body>
    <div class="row">
        <div class="col-sm-12">
            <div class="clearfix">&nbsp;</div>  
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
                                <h5><strong>Cargar el archivo del SUBES.</strong></h5>
                                <ol>
                                    <li>Seleccione el tipo de convocatoria.</li>
                                    <li>Seleccione el periodo.</li>
                                    <li>Seleccione el tipo de archivo.</li>
                                    <li>Click en el botón de cargar archivo.</li>
                                </ol>
                                <br>
                                <h5><strong>Descargar reporte por periodo.</strong></h5>
                                <ol>
                                    <li>Seleccione el tipo de convocatoria.</li>
                                    <li>Seleccione el periodo.</li>
                                    <li>Click en el botón de descargar reporte.</li>
                                </ol>
                            </div>
                        </div>
                    </div>
                </div> 
            </div>
        </div>

        <div class="col-sm-12">
            <div class="alert alert-info">
                <i class="fa fa-info-circle fa-fw fa-lg"></i>
                <strong>Toma en cuenta</strong> Solo se crearan solicitudes de beca al cargar un archivo del periodo actual.
            </div>
            <div class="alert alert-warning">
                <i class="fa fa-info-circle fa-fw fa-lg"></i>
                <strong>Importante!</strong> Se debe cargar en primer lugar el archivo de Manutención y posteriormente el de Transporte.
            </div>
        </div>

    </div>

    <div class="row">
        <div class="col-xs-12">
            <div class="main-box clearfix" >
                <header class="main-box-header clearfix">
                    <h2 class="pull-left">Archivo</h2>
                </header>
                <div class="main-box-body clearfix">
                    <div class="row">
                        <div class="col-xs-12">
                            <s:form cssClass="form-horizontal" id="cargaForm" namespace="/carga" action="cargaRegistroSubes.action" method="POST" enctype="multipart/form-data">                                
                                <div class="form-group">
                                    <label for="convocatoria" class="control-label col-xs-2" style="font-size: 14px;">Convocatoria</label> 
                                    <div class="col-xs-10">
                                        <s:select cssClass="form-control"
                                                  name="convocatoria"
                                                  list="convocatorias"
                                                  listKey="id"
                                                  listValue="descripcion"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="periodo" class="control-label col-xs-2" style="font-size: 14px;">Periodo</label> 
                                    <div class="col-xs-10">
                                        <s:select cssClass="form-control"
                                                  name="periodo"
                                                  list="ambiente.periodoList"
                                                  listKey="id"
                                                  listValue="clave"/>
                                        <span class="help-block" ></span>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="archivo" class="control-label col-xs-2" style="font-size: 14px;">Archivo</label>
                                    <div class="col-xs-4">
                                        <select name="tipoCarga" id="cargaForm_tipoCarga" class="form-control">
                                            <option value="1">Manutención</option>
                                            <option value="2">Beca Apoyo a tu Transporte</option>
                                        </select>
                                    </div>
                                    <div class="col-sm-6">
                                        <s:file id="archivo" cssClass="file form-control" labelposition="left" 
                                                accept="application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" 
                                                name="upload" data-show-preview="false"
                                                data-bv-notempty="false" />
                                                <%--data-bv-notempty-message="Es necesario que selecciones un archivo."/>--%>                                    
                                    </div>
                                    <br><br><br>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="btn-group pull-right links">
                                                <input type="submit" id="buscar" class="btn btn-primary" value="Cargar archivo"/>
                                                <a id="downloadBTN" href="" class="btn btn-primary" type="submit"><span class="fa fa-download"></span> Descargar reporte</button></a>
                                            </div>            
                                        </div>
                                    </div>
                                </div>
                            </s:form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="row" style="display: none;" id="progress-div">
        <div class="col-lg-12">
            <div class="main-box clearfix">
                <div class="clearfix">&nbsp;</div>   
                <h3 style="margin-top: 5px; margin-left: 10px">Resultados</h3>
                <div class="main-box-body clearfix">
                    <div class="table-responsive">
                        <table class="table">
                            <tbody>
                                <tr class="warning">
                                    <td>
                                        Cargados a Padron Subes
                                    </td>
                                    <td>
                                        <span class="value" id="d1">--</span>
                                    </td>
                                </tr>
                                <tr class="info">
                                    <td>
                                        Solicitudes de manutencion creadas
                                    </td>
                                    <td>
                                        <span class="value" id="d2">--</span>
                                    </td>
                                </tr>
                                <tr class="info">
                                    <td>
                                        Solicitudes de transporte creadas
                                    </td>
                                    <td>
                                        <span class="value" id="d3">--</span>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

</body>

<content tag="endScripts">
    <script type="text/javascript" src="/vendors/kartik-bootstrap-input/fileinput.min.js"></script>
    <script type="text/javascript" src="/vendors/kartik-bootstrap-input/locales/es.js"></script>
</content>

<content tag="inlineScripts">
    <script>
        $(document).ready(function () {
//			$("#archivo").fileinput({
//				showUpload: false,
//				language: "es",
//				allowedFileExtensions: ['excel']
//			});
            var cargado = false;

            $("#cargaForm").on('submit', function (e) {
                var $form = $(this);

                if ($form.data('submitted') === true) {
                    e.preventDefault();
                } else {
                    $form.data('submitted', true);

                    $('input[type=submit]', this).attr('disabled', 'disabled');
                    cargado = true;
                }
            });

            setInterval(function () {
                if (cargado) {
                    $('#progress-div').css('display', 'inline');
                    updateProgress("0");
                }
            }, 1500);
        });

        $("#archivo").fileinput({
            showUpload: false,
            language: "es",
            allowedFileExtensions: ['xls', 'xlsx']
        });

        function updateProgress(o) {
            var op = o;
            $.ajax({
                type: 'POST',
                url: '/ajax/valoresPadronAjax.action',
                dataType: 'json',
                success: function (aData) {
                    var datos = aData.data[0];
                    var arr = datos.toString().split(",");
                    $("#d1").html(arr[0]);
                    $("#d2").html(arr[1]);
                    $("#d3").html(arr[2]);
                },
                error: function () {
                    console.log("Hubo un problema que impidió que se completara la operación.");
                }
            });
        }

        $("#downloadBTN").on("click", function (event) {
            var convocatoriaId = $('select[name=convocatoria]').val();
            var periodoId = $('select[name=periodo]').val();

            var urlReporte = "reporteRegistroSubes.action";
            urlReporte = urlReporte + "?periodo=" + periodoId + "&convocatoria=" + convocatoriaId;
            document.getElementById("downloadBTN").href = urlReporte;
        });
    </script>
</content>