<%-- 
    Document   : fundacion
    Created on : 28/08/2017, 01:23:10 PM
    Author     : Tania G. Sánchez Manilla
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<head>
    <title>Reporte Fundación</title>
    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css" />
    <link rel="stylesheet" type="text/css" href="/vendors/select2/select2-old/select2.min.css"/>
    <link rel="stylesheet" type="text/css" href="/vendors/select2/select2-old/select2-bootstrap.min.css"/>
</head> 
<content tag="tituloJSP">
    Reporte Fundación
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
    <!-- Ejemplo de caja blanca -->
    <div class="row">
        <div class="col-xs-12">
            <div class="main-box clearfix" >
                <div class="main-box-body clearfix">
                    <form class="form-horizontal">
                        <div class="form-group">
                            <label class="col-sm-3 control-label">Periodo</label>
                            <div class="col-sm-8">
                                <s:select cssClass="form-control" id="periodo" name="periodo" list="ambiente.periodoList"
                                          listKey="id" listValue="clave" data-bv-notempty="true"
                                          data-bv-notempty-message="El periodo es requerida."
                                          onchange="getBeca()"/>
                                <span class="help-block" id="estadoMessage" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">Programa de beca</label>
                            <div class="col-sm-8">
                                <s:select cssClass="form-control" id="beca" name="beca" list="%{{}}"
                                          data-bv-notempty="true" data-bv-notempty-message="La beca es requerida"
                                          multiple="true"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">Origen de los recursos</label>
                            <div class="col-sm-8">
                                <s:select cssClass="form-control" id="origenRecursos" name="origenRecursos" list="service.origenRecursos"
                                          headerKey="-1" headerValue="Todos" data-bv-notempty="true"
                                          data-bv-notempty-message="El origen de los recursos es requerido."/>
                                <span class="help-block" id="estadoMessage" />
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-lg-offset-6 col-lg-6">                                                                                               
                                <a href="#" target='_blank' onclick="addURL(this)" id="fncbx" class="btn btn btn-primary pull-right">Generar reporte</a>
                            </div>                            
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div> <!-- Termina ejemplo caja blanca -->
</body>

<content tag="endScripts">
    <script type="text/javascript" src="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.js"></script>
    <script type="text/javascript" src="/vendors/select2/select2-old/select2.min.js"></script>
    <script type="text/javascript" src="/vendors/select2/select2-old/select2_locale_es.js"></script>
</content>

<content tag="inlineScripts">
    <script>
        $(document).ready(function () {
            $("#beca").select2({language: "es"});
            if ($('#periodo').val() > 0) {
                getBeca();
            }
        });

        function addURL(element) {
            var periodoId = $('#periodo option:selected').val();
            var becas = $('#beca').val();
            var origenRecursosId = $('#origenRecursos option:selected').val();
            $(element).attr('href', function () {
                return "/admin/reportes/descargarReportes.action?periodoId=" + periodoId + "&becas=" + becas + "&origenRecursosId=" + origenRecursosId;
            });
        }

        function getBeca() {
            $.ajax({
                type: 'POST',
                url: '/ajax/getProgramaBeca.action',
                dataType: 'json',
                data: {pkPeriodo: $('#periodo').val()},
                cache: false,
                success: function (aData) {
                    $('#beca').get(0).options.length = 0;
                    $.each(aData.data, function (i, item) {
                        $('#beca').get(0).options[$('#beca').get(0).options.length] = new Option(item[1], item[0]);
                    });
                },
                error: function () {
                    ModalGenerator.notificacion({
                        "titulo": "Error",
                        "cuerpo": "Hubo un problema que impidió que se completara la operación.",
                        "tipo": "ALERT",
                    });
                }
            });
            $("#beca").select2({language: "es"}).val([0]).trigger("change");
            return false;
        }
    </script>
</content>