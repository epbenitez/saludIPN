<%-- 
    Document   : eset
    Created on : 1/08/2016, 05:38:24 PM
    Author     : Tania G. Sánchez
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Solicitud de transporte</title>
    <link rel="stylesheet" type="text/css" href="/vendors/fancy-box/jquery.fancybox.min.css"/>
    <link rel="stylesheet" type="text/css" href="/vendors/select2/select2-old/select2.min.css"/>
    <link rel="stylesheet" type="text/css" href="/vendors/select2/select2-old/select2-bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="/vendors/pretty-checkbox/pretty.min.css"/>
</head> 

<content tag="tituloJSP">
    Solicitud de transporte
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
            <s:if test="validacionInscripcion">
                <div class="row">
                    <div class="col-lg-12">
                        <div class="alert alert-warning">
                            <i class="fa fa-warning fa-fw fa-lg"></i>
                            <strong>Atención</strong> Los datos que se muestran son del periodo pasado debido a que se ejecutó el proceso de "Validación de Inscripción" en este periodo.
                        </div>
                    </div>
                </div>
            </s:if>
            <security:authorize ifAnyGranted="ROLE_JEFEBECAS, ROLE_ANALISTABECAS, ROLE_ALUMNO">
                <div class="col-sm-3 pull-right">
                    <a href="#" target='_blank' onclick="addURL(this)" id="eset" class="btn btn btn-default"><span class="fa fa-download"></span> Descargar archivo PDF</a>
                </div>
                <div class="clearfix" >&nbsp;</div>
                <div class="clearfix" >&nbsp;</div>
            </security:authorize>
        </div>
    </div>
    <div class="row" id="panel" style="display:none;">
        <div class="col-lg-12 col-md-12 col-sm-12">
            <div class="main-box clearfix">
                <div class="tabs-wrapper profile-tabs">
                    <ul class="nav nav-tabs">
                        <li id="tabLiCandidatos" class="active"><a href="#traslado-tab" data-toggle="tab">Datos de traslado</a></li>
                        <li class=""><a href="#familiares-tab" data-toggle="tab">Datos familiares</a></li>
                        <li class=""><a href="#recursos-tab" data-toggle="tab">Recursos para estudios</a></li>
                        <li class=""><a href="#economico-tab" data-toggle="tab">Situación económica</a></li>
                    </ul>
                    <form id="eseTransporte" class="form-horizontal" >
                        <div class="tab-content">
                            <div class="tab-pane fade active in" id="traslado-tab">
                                <div id="contenedorCandidatos">
                                    <table id="tablaCandidatos" class="table table-striped table-hover dt-responsive" style="width: 100%">
                                        <thead>
                                            <tr>
                                                <th class="text-center">Transporte Utilizado</th>
                                                <th class="text-center">Punto de <br>Partida</th>
                                                <th class="text-center">Punto de <br>Llegada</th>
                                                <th class="text-center">Costo<br>($ sólo números)</th>
                                                <th class="text-center">Trayecto</th>
                                            </tr>
                                        </thead>
                                    </table>    
                                </div>
                            </div>
                            <!-- Separación tabs -->
                            <div class="tab-pane fade" id="familiares-tab">
                                <div id="contenedorAsignados">
                                    <table id="tablaAsignados" class="table table-striped table-hover dt-responsive" style="width: 100%">
                                        <thead>
                                            <tr>
                                                <th class="text-center">Nombre del Familiar</th>
                                                <th class="text-center">Parentesco con el solicitante</th>
                                                <th class="text-center">Edad</th>
                                                <th class="text-center">Ocupación</th>
                                                <th class="text-center">Aportación Económica Mensual<br>($ sólo números)</th>
                                            </tr>
                                        </thead>
                                    </table>   
                                </div>
                            </div>
                            <!-- Separación tabs -->
                            <div class="tab-pane fade" id="recursos-tab">
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">¿Padeces alguna enfermedad crónica?</label>
                                    <div class="col-sm-5">
                                        <div class="radio col-sm-6" style="padding-left: 20px;">
                                            <input type="radio" id="cronicaRadios1" value="1" disabled
                                                   <s:if test="respuestasTransporte.enfermedadCronica==1">
                                                       checked
                                                   </s:if>
                                                   >
                                            <label for="cronicaRadios1">
                                                Si
                                            </label>
                                        </div>
                                        <div class="radio col-sm-6" style="padding-left: 20px;">
                                            <input type="radio" id="cronicaRadios2" value="0" disabled
                                                   <s:if test="respuestasTransporte.enfermedadCronica==0">
                                                       checked
                                                   </s:if>
                                                   >
                                            <label for="cronicaRadios2">
                                                No
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">¿Tienes alguna discapacidad?</label>
                                    <div class="col-sm-5">
                                        <s:textfield cssClass="form-control" name="respuestasTransporte.discapacidad.nombre" 
                                                     id="respuestasTransporte.discapacidad.nombre" readonly="true"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">¿Algún familiar tiene alguna enfermedad crónica?</label>
                                    <div class="col-sm-5">                                    
                                        <div class="radio col-sm-6" style="padding-left: 20px;">
                                            <input type="radio" id="famRadios1" value="1" disabled
                                                   <s:if test="respuestasTransporte.familiarenfermcronica==1">
                                                       checked
                                                   </s:if>
                                                   >
                                            <label for="famRadios1">
                                                Si
                                            </label>
                                        </div>
                                        <div class="radio col-sm-6" style="padding-left: 20px;">
                                            <input type="radio" name="becaTransporte.enfermedadCronica" id="famRadios2" value="0" disabled
                                                   <s:if test="respuestasTransporte.familiarenfermcronica==0">
                                                       checked
                                                   </s:if>
                                                   >
                                            <label for="famRadios2">
                                                No
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">
                                        Para continuar con tus estudios de educación superior,<br>
                                        ¿tuviste que cambiar tu lugar de residencia?
                                    </label>
                                    <div class="col-sm-5">
                                        <div class="radio col-sm-6" style="padding-left: 20px;">
                                            <input type="radio" id="cambioRadios1" value="1" disabled
                                                   <s:if test="respuestasTransporte.cambioresidencia==1">
                                                       checked
                                                   </s:if>
                                                   >
                                            <label for="cambioRadios1">
                                                Si
                                            </label>
                                        </div>
                                        <div class="radio col-sm-6" style="padding-left: 20px;">
                                            <input type="radio" name="becaTransporte.enfermedadCronica" id="cambioRadios2" value="0" disabled
                                                   <s:if test="respuestasTransporte.cambioresidencia==0">
                                                       checked
                                                   </s:if>
                                                   >
                                            <label for="cambioRadios2">
                                                No
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">¿Cómo te enteraste del apoyo de esta beca?</label>
                                    <div class="col-sm-5">
                                        <s:textfield cssClass="form-control" name="respuestasTransporte.enteroBeca.nombre" 
                                                     id="respuestasTransporte.enteroBeca.nombre" readonly="true"/>
                                    </div>
                                </div>
                            </div>
                            <!-- Separación tabs -->
                            <div class="tab-pane fade" id="economico-tab">
                                <p>Gasto familiar mensual de los siguientes rubros:</p><br>
                                <div class="form-group">
                                    <label class="col-sm-4 control-label" align="right">Alimentación</label>
                                    <div class="col-sm-5 input-group">
                                        <span class="input-group-addon"><i class="fa fa-dollar"></i></span>
                                            <s:textfield cssClass="form-control" name="respuestasTransporte.alimentacion" 
                                                         id="respuestasTransporte.alimentacion" readonly="true"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-4 control-label" align="right">Renta</label>
                                    <div class="col-sm-5 input-group">
                                        <span class="input-group-addon"><i class="fa fa-dollar"></i></span>
                                            <s:textfield cssClass="form-control" name="respuestasTransporte.renta" 
                                                         id="respuestasTransporte.renta" readonly="true"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-4 control-label" align="right">Vivienda (Agua, Luz, Telefono, TV de paga, Internet,
                                        Servicios, etc.)</label>
                                    <div class="col-sm-5 input-group">
                                        <span class="input-group-addon"><i class="fa fa-dollar"></i></span>
                                            <s:textfield cssClass="form-control" name="respuestasTransporte.vivienda" 
                                                         id="respuestasTransporte.vivienda" readonly="true"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-4 control-label" align="right">Escolares</label>
                                    <div class="col-sm-5 input-group">
                                        <span class="input-group-addon"><i class="fa fa-dollar"></i></span>
                                            <s:textfield cssClass="form-control" name="respuestasTransporte.escolares" 
                                                         id="respuestasTransporte.escolares" readonly="true"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-4 control-label" align="right">Salud</label>
                                    <div class="col-sm-5 input-group">
                                        <span class="input-group-addon"><i class="fa fa-dollar"></i></span>
                                            <s:textfield cssClass="form-control" name="respuestasTransporte.salud" 
                                                         id="respuestasTransporte.salud" readonly="true"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-4 control-label" align="right">Transporte</label>
                                    <div class="col-sm-5 input-group">
                                        <span class="input-group-addon"><i class="fa fa-dollar"></i></span>
                                            <s:textfield cssClass="form-control" name="respuestasTransporte.transporte" 
                                                         id="respuestasTransporte.transporte" readonly="true"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-4 control-label" align="right">Actividades culturales y recreativas</label>
                                    <div class="col-sm-5 input-group">
                                        <span class="input-group-addon"><i class="fa fa-dollar"></i></span>
                                            <s:textfield cssClass="form-control" name="respuestasTransporte.actividades" 
                                                         id="respuestasTransporte.actividades" readonly="true"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-4 control-label" align="right">Otros</label>
                                    <div class="col-sm-5 input-group">
                                        <span class="input-group-addon"><i class="fa fa-dollar"></i></span>
                                            <s:textfield cssClass="form-control" name="respuestasTransporte.otros" 
                                                         id="respuestasTransporte.otros" readonly="true"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>         
</body>

<content tag="inlineScripts">
    <script>
        $(document).ready(function () {
            setTimeout(function () {
                actualizarTablas();
            }, 200);
            $('#contenedorCandidatos').on('draw.dt', function () {
                $("#dataTableHeader-contenedorCandidatos").hide();
                $("#paginateFooter-contenedorCandidatos").hide();
            });
            $('#contenedorAsignados').on('draw.dt', function () {
                $("#dataTableHeader-contenedorAsignados").hide();
                $("#paginateFooter-contenedorAsignados").hide();
            });
        });

        function actualizarTablas() {
            $("#panel").show();
            var urlCandidatos = '/ajax/trasladoCuestionarioTransporteAjax.action?transporteId=' + <s:property value="respuestasTransporte.id" />;
            var urlAsignados = '/ajax/familiarCuestionarioTransporteAjax.action?transporteId=' + <s:property value="respuestasTransporte.id" />;
            generarTabla("contenedorCandidatos", urlCandidatos, despuesCargarCandidatos, true);
            generarTabla("contenedorAsignados", urlAsignados, despuesCargarAsignados, true);
        }

        function despuesCargarAsignados(oSettings) {
            parent.jQuery.fancybox.update();
        }

        function despuesCargarCandidatos(oSettings) {
            parent.jQuery.fancybox.update();
        }

        function addURL(element) {
            $("#eset").blur();// Evita que el botón sea desactivado    
            $(element).attr('href', function () {
                return "/misdatos/descargar/pdfEstudioSocioeconomicoTransporte.action?cuestionarioId=2&alumnoId=<s:property value="alumnoId"/>";
            });
        }
    </script>
</content>

<content tag="endScripts">
    <script type="text/javascript" src="/vendors/fancy-box/jquery.fancybox.min.js"></script>
    <script type="text/javascript" src="/vendors/datatables/datatables.js" ></script>
    <script type="text/javascript" src="/resources/js/ModalGenerator.js" ></script>
    <script type="text/javascript" src="/resources/js/generador-tablas.js" ></script>
    <script type="text/javascript" src="/vendors/select2/select2-old/select2.min.js"></script>
    <script type="text/javascript" src="/vendors/select2/select2-old/select2_locale_es.js"></script>
</content>