<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<head>
    <title>Envio de correos</title>
    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css"/>
    <link rel="stylesheet" type="text/css" href="/vendors/fancy-box/jquery.fancybox.min.css"/>
    <link rel="stylesheet" type="text/css" href="/vendors/kartik-bootstrap-input/fileinput.min.css"/>
    <style>
        .extra-space {
            margin-bottom: 10px;
        }
        .btn.btn-default[disabled] {
            background-color: #EFF0F1;
        }
        /* Inicia: Centrar checkbox en tabla */
        .checkbox-nice input[type=checkbox] {
            visibility: visible;
            position: absolute;
            left: -9999px;
        }           
        .checkbox-nice label:before {
            left: auto;
        }
        .checkbox-nice label:after {
            left: auto;
            margin-left: 5px;
        }    
        #cuadro {
            font-size: 12px!important;
        }
        td, th { 
            font-size: 12px!important;
        }
        /* Termina: Centrar checkbox en tabla */ 
    </style>
</head> 

<content tag="tituloJSP">
    Envio de correos
</content>

<body>
    <div class="row" id="aviso">
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
        <div class="col-lg-12 col-md-12 col-sm-12">
            <div class="main-box clearfix">
                <div class="tabs-wrapper profile-tabs">
                    <ul class="nav nav-tabs">
                        <li class="active">
                            <a href="#esp-tab" data-toggle="tab">
                                Especifico
                            </a>
                        </li>
                        <li>
                            <a href="#fil-tab" data-toggle="tab">
                                Filtros 
                            </a>
                        </li>
                        <li>
                            <a href="#pre-tab" data-toggle="tab">
                                Predeterminado
                            </a>
                        </li>
                    </ul>
                    <div class="tab-content">
                        <!-- Inicia predeterminado -->
                        <div class="tab-pane" id="pre-tab">
                            <div class="clearfix">&nbsp;</div> 
                            <div class="form-group col-sm-12">
                                <label class="col-sm-2 control-label">Nivel</label>
                                <div class="col-sm-10">
                                    <s:select id="nivelPre" name="nivelPre"
                                              cssClass="form-control pre"
                                              list="ambiente.nivelActivoList"
                                              listKey="id" listValue="nombre"
                                              headerKey="0" 
                                              headerValue="-Todos-"
                                              disabled="true"
                                              onChange = "cambioNivelPre()"/>
                                </div>
                            </div>
                            <div class="form-group col-sm-12">
                                <label class="col-sm-2 control-label">Unidad Académica</label>
                                <security:authorize ifAnyGranted="ROLE_RESPONSABLE_UA">
                                    <div class="col-sm-10">
                                        <s:select id="unidadAcademicaPre"
                                                  name="unidadAcademicaPre"
                                                  cssClass ="form-control"
                                                  list="ambiente.unidadAcademicaList"
                                                  listKey="id" 
                                                  listValue="nombreCorto" 
                                                  headerKey=""
                                                  disabled="true"
                                                  hidden="true"/>
                                    </div>
                                </security:authorize>
                                <security:authorize ifNotGranted="ROLE_RESPONSABLE_UA">
                                    <div class="col-sm-10">
                                        <s:select id="unidadAcademicaPre"
                                                  name="unidadAcademicaPre"
                                                  cssClass="form-control pre"
                                                  list="ambiente.unidadAcademicaList"
                                                  listKey="id" 
                                                  listValue="nombreCorto" 
                                                  headerKey="0"
                                                  headerValue="-Todas-"
                                                  disabled="true"
                                                  onchange="cambioValoresPre()"/>
                                    </div>
                                </security:authorize>
                            </div>

                            <div class="form-group col-sm-12">
                                <div class="clearfix">
                                    <div class="infographic-box merged merged-top pull-left">
                                        <i class="fa fa-pencil-square-o purple-bg"></i>
                                        <span class="value" id="d1">--</span>
                                        <span class="headline extra-space">Alumnos registrados en el periodo actual</span>
                                        <div class="btn-group pull-right">
                                            <button id="d1Btn" disabled class="pre fancybox fancybox.iframe btn btn-primary" type="button" onclick="addURL(this, 1)">Redactar</button>
                                            <button id="d1BtnD" data-opt="1" disabled class="pre btn btn-default" type="button" title="Descargar resumen" onclick="addURLD(this)"><span class="fa fa-download"></span></button>
                                        </div>

                                    </div>
                                    <div class="infographic-box merged merged-top merged-right pull-left">
                                        <i class="fa fa-users green-bg"></i>
                                        <span class="value" id="d2">--</span>
                                        <span class="headline extra-space">Revalidantes que aún no se han registrado</span>
                                        <div class="btn-group pull-right">
                                            <button id="d2Btn" disabled class="pre fancybox fancybox.iframe btn btn-primary" type="button" onclick="addURL(this, 2)">Redactar</button>
                                            <button id="d2BtnD" data-opt="2" disabled class="pre btn btn-default" type="button" title="Descargar resumen" onclick="addURLD(this)"><span class="fa fa-download"></span></button>
                                        </div>
                                    </div>
                                </div>
                                <div class="clearfix">
                                    <div class="infographic-box merged pull-left">
                                        <i class="fa fa-file-archive-o yellow-bg"></i>
                                        <span class="value" id="d3">--</span>
                                        <span class="headline extra-space">Alumnos registrados que no tienen su ESE finalizado</span>
                                        <div class="btn-group pull-right">
                                            <button id="d3Btn" disabled class="pre fancybox fancybox.iframe btn btn-primary" type="button" onclick="addURL(this, 3)">Redactar</button>
                                            <button id="d3BtnD" data-opt="3" disabled class="pre btn btn-default" type="button" title="Descargar resumen" onclick="addURLD(this)"><span class="fa fa-download"></span></button>
                                        </div>
                                    </div>
                                    <div class="infographic-box merged merged-right pull-left">
                                        <i class="fa fa-credit-card red-bg"></i>
                                        <span class="value" id="d4">--</span>
                                        <span class="headline extra-space">Alumnos con datos bancarios incorrectos</span>                                                                                                                        
                                        <div class="btn-group pull-right">
                                            <button id="d4Btn" disabled class="pre fancybox fancybox.iframe btn btn-primary" type="button" onclick="addURL(this, 4)">Redactar</button>
                                            <button id="d4BtnD" data-opt="4" disabled class="pre btn btn-default" type="button" title="Descargar resumen" onclick="addURLD(this)"><span class="fa fa-download"></span></button>
                                        </div>
                                        <span class="col-xs-4 pull-right">
                                            <s:select id="periodoPre" name="periodoPre"
                                                      cssClass="form-control"
                                                      list="ambiente.periodoList"
                                                      listKey="id" listValue="clave"
                                                      onChange = "cambioValoresPre()"/></span>
                                    </div>
                                </div>                                
                            </div>
                        </div> <!-- Termina Predeterminado -->
                        <!--Inicia filtros-->
                        <div class="tab-pane clearfix" id="fil-tab"><!------------------------------------------------------------------------------------------------------------------------------------------------------------->
                            <div class="clearfix">&nbsp;</div>
                            <div class="form-group col-sm-12">
                                <label class="col-sm-2 control-label">Nivel</label>
                                <div class="col-sm-10">
                                    <s:select id="nivel" name="nivel"
                                              cssClass="form-control"
                                              list="ambiente.nivelActivoList"
                                              listKey="id" listValue="nombre"
                                              headerKey="0" 
                                              headerValue="-Todos-"
                                              />
                                </div>
                            </div>
                            <div class="form-group col-sm-12">
                                <label class="col-sm-2 control-label">Unidad Académica</label>
                                <security:authorize ifAnyGranted="ROLE_RESPONSABLE_UA">
                                    <div class="col-sm-10">
                                        <s:select id="unidadAcademica"
                                                  name="unidadAcademica"
                                                  cssClass="form-control"
                                                  list="ambiente.unidadAcademicaList"
                                                  listKey="id" 
                                                  listValue="nombreCorto" 
                                                  headerKey=""
                                                  disabled="true"/>
                                    </div>
                                </security:authorize>
                                <security:authorize ifNotGranted="ROLE_RESPONSABLE_UA">
                                    <div class="col-sm-10">
                                        <s:select cssClass="form-control"
                                                  id="unidadAcademica"
                                                  name="unidadAcademica"
                                                  headerKey="-Todas-"
                                                  list="ambiente.unidadAcademicaList"
                                                  listKey="id" 
                                                  listValue="nombreCorto" 
                                                  headerKey=""
                                                  headerValue="-Todas-"/>
                                    </div>
                                </security:authorize>
                            </div>
                            <div class="form-group col-sm-12">
                                <label class="col-sm-2 control-label">Beca</label>
                                <div class="col-sm-10">
                                    <s:select name="beca" id="beca"
                                              list="ambiente.becaList" 
                                              listKey="id" listValue="nombre"
                                              cssClass="form-control"
                                              headerKey=""
                                              headerValue="-Todas-"
                                              onChange="getTipoBeca()"/> 
                                </div>
                            </div>   
                            <div class="form-group col-sm-12">
                                <label class="col-sm-2 control-label">Tipo Beca</label>
                                <div class="col-sm-10">
                                    <s:select name="tipoBeca" id="tipoBeca"
                                              list="ambiente.tipoBecaList" 
                                              listKey="id" listValue="nombre"
                                              cssClass="form-control"
                                              headerKey=""
                                              headerValue="-Todas-" />
                                </div>
                            </div>
                            <div class="form-group col-sm-12">
                                <label class="col-sm-2 control-label">Movimiento</label>
                                <div class="col-sm-10">
                                    <s:select id="movimiento"  
                                              cssClass="form-control"
                                              name="movimiento"
                                              list="ambiente.movimientoList" 
                                              listKey="id" 
                                              listValue="nombre" 
                                              headerKey=""
                                              data-bv-notempty="true"
                                              data-bv-notempty-message="El tipo de movimiento es requerido"
                                              headerValue="-Todos-"
                                              onChange="getProcesos()"/> 
                                </div>
                            </div>   
                            <div class="form-group col-sm-12">
                                <label class="col-sm-2 control-label">Proceso</label>
                                <div class="col-sm-10">
                                    <s:select id="proceso"  
                                              cssClass="form-control"
                                              name="proceso"
                                              list="ambiente.procesoList" 
                                              listKey="id" 
                                              listValue="nombre" 
                                              data-bv-notempty="true"
                                              data-bv-notempty-message="El tipo de proceso es requerido"
                                              headerKey=""
                                              headerValue="-Todos-"/> 
                                </div>
                            </div>
                            <div class ="form-group col-sm-2 col-sm-offset-10">
                                <button id="buscar" type="button" style="margin-right: 8px;" class="btn btn-primary btn-primary-form-margin pull-right">Buscar</button>
                                <div class="clearfix">&nbsp;</div>
                            </div>
                        </div><!--Termina filtros-->
                        <!-- Inicia Específico  -->
                        <div class="tab-pane active" id="esp-tab"><!------------------------------------------------------------------------------------------------------------------------------------------------------------->
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
                                                        Para realizar un env&iacute;o de correos masivo, puede ingresar un excel
                                                        con los números de boleta de los alumnos.
                                                    </li>

                                                    <li>
                                                        Es necesario considerar que el archivo Excel que intente cargar, 
                                                        debe tener s&oacute;lo una columna, y el primer rengl&oacute;n no 
                                                        se considerar&aacute;.

                                                    </li>

                                                    <li>
                                                        Una vez que esté listo el archivo, seleccionelo y al dar clic en el 
                                                        bot&oacute;n de "Cargar archivo", el sistema buscar&aacute; a los 
                                                        alumnos que correspondan a las boletas enviadas en el archivo.

                                                    </li>

                                                    <li>
                                                        El sistema mostrar&aacute; el bot&oacute;n "Enviar", el cual permitir&aacute;
                                                        el ingreso de los datos del correo electr&oacute;nico por enviar.

                                                    </li>
                                                </ul>
                                                <div class="col-xs-12 text-center" style="padding-bottom: 5px" >
                                                    <a href="/resources/downloadable/Ejemplo_envio_correos.xlsx" class="btn btn-primary btn-lg">
                                                        <i class='fa fa-download'></i> Archivo de ejemplo
                                                    </a>
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
                                            <s:form id="cargaForm" action="cargaEnvioCorreos.action" method="POST" namespace="/admin" enctype="multipart/form-data" cssClass="form-horizontal">
                                                <div class="form-group">
                                                    <label for="archivo" class="control-label col-sm-2">Archivo excel con números de boleta</label>
                                                    <div class="col-sm-8">
                                                        <s:file id="archivo" cssClass="file form-control" 
                                                                labelposition="left" 
                                                                accept="application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" 
                                                                name="upload" 
                                                                data-bv-notempty="true" 
                                                                data-bv-notempty-message="Es necesario que selecciones un archivo."
                                                                data-show-preview="false" />
                                                    </div>
                                                    <div class="col-sm-2">
                                                        <input type="submit" id="cargar" value="Cargar archivo" class="btn btn-primary"/>
                                                    </div>
                                                </div>
                                            </s:form>
                                        </div>
                                    </div>
                                </div>
                            </div> 
                        </div> <!-- Termina Específico  -->
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!--Inicia tabla de búsqueda-->
    <div class="row" id="tabla" style="display: none">
        <div class="col-xs-12">
            <div class="main-box clearfix" >
                <div class="col-md-12" style="padding: 0px;">
                    <div class="responsive">
                        <table id="listado" class="table table-striped table-hover dt-responsive" style="width: 100%">
                            <thead>
                                <tr>
                                    <th data-orderable="false">
                                        <div class='checkbox-nice' style="margin-bottom: 5px;" data-content='Todos' data-container='body' data-toggle="popover" data-placement="top">
                                            <input id='sel-todos' type="checkbox" checked/>
                                            <label for='sel-todos'></label>
                                        </div>
                                    </th>
                                    <th data-orderable="true" data-center="true">Boleta</th>
                                    <th data-orderable="true" >Nombre</th>
                                    <th data-orderable="true">Unidad Académica</th>
                                </tr>
                            </thead>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div> <!--Termina tabla de búsqueda-->
    
    <s:if test="%{carga}">
    <div class="row" id="resume-div">        
        <div class="${hayBoletasErroneas ? 'col-xs-6' : 'col-xs-12'}">
            <div class="main-box small-graph-box green-bg">
                <span class="value" id="resume-value"><s:property value="%{getAlumnosLst().size()}"/></span>
                <span class="headline">Correctas</span>
                <span class="subinfo" id="resume-resmn">
                    <i class="fa fa-check"></i> 
                    <span>Boletas cargadas correctamente</span>                    
                </span>
            </div>
        </div>        
        <s:if test="%{hayBoletasErroneas}">        
        <div class="col-xs-6">
            <div class="main-box small-graph-box red-bg">
                <span class="value" id="fail-resume-value"><s:property value="%{getBoletasError().size()}"/></span>
                <span class="headline">Fallidas</span>
                <span class="subinfo" id="fail-resume-resmn">
                    <i class="fa fa-times"></i> 
                    <span>Boletas no pudieron ser cargadas</span>
                </span>
            </div>
        </div>
        </s:if>       
        <div class="col-xs-12">
            <a class="pre fancybox fancybox.iframe btn btn-primary" title="Redactar correo" href="#" onclick="addURL(this, 7)">Redactar</a>
        </div> 
    </div>
    </s:if>

    <!-- Inicia Tabla errores excel -->
    <s:if test="%{carga && hayBoletasErroneas}">
    <div class="row" id="contenedor">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <div class="col-md-12" style="padding: 0px">
                    <div class="responsive">
                        <table id="adentrocontendedor" class="table table-striped table-hover dt-responsive" style="width: 100%">
                            <thead>
                                <tr>
                                    <th data-orderable="true" data-center="true">Boleta</th>
                                    <th data-orderable="true" >Error</th>
                                </tr>
                            </thead>				
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div> <!-- Termina Tabla errores excel -->
    </s:if>
<input type="hidden" name="alumnos" id="alumnos" value="," />
</body>

<content tag="endScripts">
    <s:if test="%{carga && hayBoletasErroneas}">
        <script>
            var botonExcel = {
                extend: 'csv',
                text: 'Descargar excel',
                className: 'btn-primary',
                exportOptions: {
                    columns: [1, 2]
                },
                filename: "Errores"
            };            
        
            function despuesCargarTabla() {
                $(".buttons-copy, .buttons-print, .buttons-pdf").hide();
            }
            
            var dataSets = new Array();
            <s:iterator value="boletasError">
                var subDataSets = new Array();
                <s:iterator>
                    console.log("<s:property/>");
                    subDataSets.push("<s:property/>");
                </s:iterator>
                dataSets.push(subDataSets);
            </s:iterator>
        </script>
        <script type="text/javascript" src="/resources/js/generador-tablas.min.js" data-init="true" data-div="contenedor" data-function="despuesCargarTabla" data-ajax="dataSets" data-button='botonExcel'></script>
    </s:if>
    <script type="text/javascript" src="/vendors/kartik-bootstrap-input/fileinput.min.js"></script>
    <script type="text/javascript" src="/vendors/kartik-bootstrap-input/locales/es.js"></script>
    <script type="text/javascript" src="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.js"></script>
    <script type="text/javascript" src="/vendors/fancy-box/jquery.fancybox.min.js"></script>    
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>
    <s:if test="%{!carga}">
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js"></script>    
    </s:if>    
</content>

<content tag="inlineScripts">
    <script>
            $(document).ready(function () {
                $("#cargaForm").bootstrapValidator();
                $("#cargaForm").bootstrapValidator().on('success.form.bv', function (e) {
                    ModalGenerator.notificacion({
                        "titulo": "Cargando información",
                        "cuerpo": "Estamos buscando los números de boleta de los alumnos, favor de no cerrar la ventana ni presionar <b>atrás</b> en el navegador.",
                        "tipo": "INFO",
                        "sePuedeCerrar": false
                    });
                });

                $("#nivel").on('change', function () {
                    getUnidades();
                    getBecas();
                });

                $(".fancybox").fancybox({
                    autoSize: true,
                    afterClose: function () {
                        $("#alumnos").val(",");
                    }
                });

                $('#buscar').click(function (e) {
                    lista();
                    $('#buscar').blur();
                });

                setTimeout(
                        function () {
                            getValores();
                        }, 200);
            });

            $("#archivo").fileinput({
                showUpload: false,
                language: "es",
                allowedFileExtensions: ['xls', 'xlsx']
            });

            // Para activar o desactivar todas las casillas
            $('#sel-todos').change(function () {
                if ($('#sel-todos:checked').length > 0) {
                    $('.chck').change().prop("checked", true);
                } else {
                    $('.chck').change().prop("checked", false);
                }
            });

            // Para agregar popover
            $('[data-toggle="popover"]').popover({
                trigger: 'hover'
            });

            $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
                $('#tabla').hide();
                $('#paginateFooter-tabla').hide();
                $('#dataTableHeader-tabla').hide();
                $('#contenedor').hide();
                $('#paginateFooter-contenedor').hide();
                $('#dataTableHeader-contenedor').hide();
                $('#aviso').hide();
            });

            function lista() {
                var url = "/ajax/listaEnvioCorreosAjax.action";
                var nv = $('#nivel').val();
                var ua = $('#unidadAcademica').val();
                var bc = $('#beca').val();
                var tb = $('#tipoBeca').val();
                var mv = $('#movimiento').val();
                var pr = $('#proceso').val();
                url += "?nivel=" + nv + "&unidadAcademica=" + ua + "&beca=" + bc + "&tipoBeca=" + tb + "&movimiento=" + mv + "&proceso=" + pr;

                generarTabla("tabla", url, fncbx, true, agregarBoton);
                $("#buscar").blur();
            }

            function fncbx() {
                $(".fancybox").fancybox();
            }

            var agregarBoton = [{
                    text: '<a class="pre fancybox fancybox.iframe btn btn-primary pull-right" title="Redactar correo" href="#" onclick="addURL(this, 6)">Redactar</a>',
                    className: 'normal-button'
                }
            ];

            function excluir(check, id) {
                var current = $("#alumnos").val();
                if (!check) {
                    if (current.indexOf("," + id + ",") > 0) {
                    } else {
                        $("#alumnos").val(current + id + ",");
                    }
                } else {
                    var porEliminar = "," + id + ",";
                    current = current.replace(porEliminar, ",");
                    $("#alumnos").val(current);
                }
            }
            function habDeshabilitar(arr) {
                arr.forEach(function (item) {
                    $(item).each(function () {
                        var actualState = $(this).prop("disabled");
                        if (actualState == false) {
                            $(this).prop("disabled", true);
                        } else {
                            $(this).prop("disabled", false);
                        }
                    });
                });
            }

            function getUnidades() {
                $.ajax({
                    type: 'POST',
                    url: '/ajax/getUnidadAcademicaAjax.action',
                    dataType: 'json',
                    data: {
                        pkNivel: Number($('#nivel').val())
                    },
                    cache: false,
                    success: function (aData) {
                        var ar = ["#unidadAcademica", "#buscar"];
                        habDeshabilitar(ar);
                        $('#unidadAcademica').get(0).options.length = 0;
                        $.each(aData.data, function (i, item) {
                            $('#unidadAcademica').get(0).options[$('#unidadAcademica').get(0).options.length] = new Option(item[1], item[0]);
                        });
                        habDeshabilitar(ar);
                    },
                    error: function () {
                        BootstrapDialog.alert({
                            title: 'Atención',
                            message: 'Hubo un problema que impidió que se completara la operación.',
                            type: BootstrapDialog.TYPE_DANGER
                        });
                    }
                });
                return false;
            }

            function cambioValoresPre() {
                $(".pre").prop("disabled", true);
                getValores();
            }

            function cambioNivelPre() {
                $(".pre").prop("disabled", true);
                getUnidadesPre();
            }

            function getUnidadesPre() {
                $.ajax({
                    type: 'POST',
                    url: '/ajax/getUnidadAcademicaAjax.action',
                    dataType: 'json',
                    data: {pkNivel: Number($('#nivelPre').val())},
                    cache: false,
                    success: function (aData) {
                        $('#unidadAcademicaPre').get(0).options.length = 0;
                        $.each(aData.data, function (i, item) {
                            $('#unidadAcademicaPre').get(0).options[$('#unidadAcademicaPre').get(0).options.length] = new Option(item[1], item[0]);
                        });
                        getValores();
                    },
                    error: function () {
                        BootstrapDialog.alert({
                            title: 'Atención',
                            message: 'Hubo un problema que impidió que se completara la operación.',
                            type: BootstrapDialog.TYPE_DANGER
                        });
                    }
                });
            }
            function getBecas() {
                $.ajax({
                    type: 'POST',
                    url: '/ajax/getBecasporNivelBecaAjax.action',
                    dataType: 'json',
                    data: {
                        nivelId: Number($('#nivel').val()),
                    },
                    cache: false,
                    success: function (aData) {
                        var ar = ["#beca", "#buscar"];
                        habDeshabilitar(ar);
                        $('#beca').get(0).options.length = 0;
                        $.each(aData.data, function (i, item) {
                            $('#beca').get(0).options[$('#beca').get(0).options.length] = new Option(item[1], item[0]);
                        });
                        habDeshabilitar(ar);
                    }
                });
            }
            function getTipoBeca() {
                $.ajax({
                    type: 'POST',
                    url: '/ajax/getTipoBecaAjax.action',
                    dataType: 'json',
                    data: {pkBeca: Number($('#beca').val())},
                    cache: false,
                    success: function (aData) {
                        var ar = ["#becas", "#buscar"];
                        habDeshabilitar(ar);
                        $('#tipoBeca').get(0).options.length = 0;
                        $.each(aData.data, function (i, item) {
                            $('#tipoBeca').get(0).options[$('#tipoBeca').get(0).options.length] = new Option(item[1], item[0]);
                        });
                        habDeshabilitar(ar);
                    },
                    error: function () {
                        BootstrapDialog.alert({
                            title: 'Atención',
                            message: 'Hubo un problema que impidió que se completara la operación.',
                            type: BootstrapDialog.TYPE_DANGER
                        });
                    }
                });
                return false;
            }

            function getProcesos() {
                $.ajax({
                    type: 'POST',
                    url: '/ajax/getTipoProcesoAjax.action',
                    dataType: 'json',
                    data: {pkMovimiento: Number($('#movimiento').val())},
                    cache: false,
                    success: function (aData) {
                        var ar = ["#proceso", "#buscar"];
                        habDeshabilitar(ar);
                        $('#proceso').get(0).options.length = 0;
                        $.each(aData.data, function (i, item) {
                            $('#proceso').get(0).options[$('#proceso').get(0).options.length] = new Option(item[1], item[0]);
                        });
                        habDeshabilitar(ar);
                    },
                    error: function () {
                        BootstrapDialog.alert({
                            title: 'Atención',
                            message: 'Hubo un problema que impidió que se completara la operación.',
                            type: BootstrapDialog.TYPE_DANGER
                        });
                    }
                });
                return false;
            }
            function addURLD(element) {
                var url = '/admin/downResumenEnvioCorreos.action';
                var op = $(element).attr("data-opt");
                var nv = $('#nivelPre option:selected').val();
                var ua = $('#unidadAcademicaPre option:selected').val();
                var p = $('#periodoPre').val();

                url += "?opcion=" + op + "&nivel=" + nv + "&unidadAcademica=" + ua + "&periodo=" + p;

                window.open(url, "_blank");
            }

            function addURL(element, op) {
                var url = '/admin/formEnvioCorreos.action';
                var nv = $('#nivelPre option:selected').val();
                var ua = $('#unidadAcademicaPre option:selected').val();
                var bc = $('#beca option:selected').val();
                var tb = $('#tipoBeca option:selected').val();
                var mv = $('#movimiento option:selected').val();
                var pr = $('#proceso option:selected').val();
                var a = $('#alumnos').val();
                var p = $('#periodoPre').val();

                if (op == 6) {
                    nv = $('#nivel option:selected').val();
                    ua = $('#unidadAcademica option:selected').val();
                }

                $(element).attr('href', function () {
                    return url + "?opcion=" + op + "&nivel=" + nv + "&unidadAcademica=" + ua + "&beca=" + bc + "&tipoBeca=" + tb + "&movimiento=" + mv + "&proceso=" + pr + "&alumnos=" + a + "&periodo=" + p;
                });
            } // Agergar form oculta con estos datos, y con la lista de alumnos.

            function cambiarValores(arr) {
                $("#d1").html(arr[0]);
                $("#d2").html(arr[1]);
                $("#d3").html(arr[2]);
                $("#d4").html(arr[3]);
            }

            function desactivarEnviosVacios(arr, btnArr) {
                for (var i = 0; i <= 3; i++) {
                    if (Number(arr[i]) == 0)
                        habDeshabilitar(btnArr[i]);
                }
            }

            function getValores() {
                return $.ajax({
                    type: 'POST',
                    url: '/ajax/cargaEnvioCorreosAjax.action',
                    dataType: 'json',
                    data: {
                        unidadAcademica: $('#unidadAcademicaPre').val(),
                        periodo: $('#periodoPre').val(),
                        nivel: $('#nivelPre').val()
                    },
                    cache: false,
                    success: function (aData) {
                        var datos = aData.data[0];
                        var ax = datos.toString().split(",");
                        // Se actualizan los valores
                        cambiarValores(ax);
                        // Se activan los botones de la sección "Predeterminado"
                        habDeshabilitar([".pre"]);
                        // Desactivamos aquellos botones que llevarían a un 
                        // envío de correo para 0 destinatarios
                        var btnArr = [
                            ["#d1Btn", "#d1BtnD"], ["#d2Btn", "#d2BtnD"],
                            ["#d3Btn", "#d3BtnD"], ["#d4Btn", "#d4BtnD"]
                        ];
                        desactivarEnviosVacios(ax, btnArr);
                    },
                    error: function () {
                        BootstrapDialog.alert({
                            title: 'Atención',
                            message: 'Hubo un problema que impidió que se completara la operación.',
                            type: BootstrapDialog.TYPE_DANGER
                        });
                    }
                });
            }
    </script>
</content>