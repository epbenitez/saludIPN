<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Administraci&oacute;n de alumnos</title>
    <link rel="stylesheet" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css" type="text/css" />    
    <link rel="stylesheet" type="text/css" href="/vendors/fancy-box/jquery.fancybox.min.css"/>
    <style>

        a.table-link.danger {
            white-space: pre;
        }

        .cursor{
            cursor: pointer;
        }
    </style>
</head> 

<content tag="tituloJSP">
    Administración de alumnos
</content>

<body>

    <div class="row" id="errorFiltros" style="display:none;">
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
                <div class="row">
                    <div class="main-box-body">
                        <div class="col-md-12">
                            <form id="busquedaForm" class="form-horizontal" role="form"
                                  data-bv-feedbackicons-valid="glyphicon glyphicon-ok"
                                  data-bv-feedbackicons-invalid="glyphicon glyphicon-remove"
                                  data-bv-feedbackicons-validating="glyphicon glyphicon-refresh">
                                <div class="form-group">
                                    <label class="col-sm-1 control-label">Boleta</label>
                                    <div class="col-sm-11">
                                        <s:textfield 
                                            cssClass="form-control"
                                            id="alumnoBoleta"
                                            name = "alumnoBoleta"
                                            placeholder="Boleta"
                                            data-bv-stringlength="true" 
                                            data-bv-stringlength-max="10"
                                            data-bv-stringlength-min="8"
                                            data-bv-stringlength-message="La boleta debe tener entre 8 y 10 dígitos"
                                            />
                                    </div>
                                </div> 
                                <div class="form-group">
                                    <label class="col-sm-1 control-label">Curp</label>
                                    <div class="col-sm-11">
                                        <s:textfield cssClass="form-control" id="alumnoCurp" name="curp" placeholder="Curp"
                                                     data-bv-message="Este dato no es válido"                                                                                                       
                                                     pattern="^[a-zA-Z]{4}((\d{2}((0[13578]|1[02])(0[1-9]|[12]\d|3[01])|(0[13456789]|1[012])(0[1-9]|[12]\d|30)|02(0[1-9]|1\d|2[0-8])))|([02468][048]|[13579][26])0229)(H|M)(AS|BC|BS|CC|CL|CM|CS|CH|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|SM|NE)([a-zA-Z]{3})([a-zA-Z0-9\s]{1})\d{1}$" 
                                                     data-bv-regexp-message="Tu CURP no tiene el formato correcto"
                                                     data-bv-stringlength="true" 
                                                     data-bv-stringlength-min="18" 
                                                     data-bv-stringlength-max="18" 
                                                     data-bv-stringlength-message="La CURP debe estar conformada por 18 caracteres"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-1 control-label">Nombre</label>
                                    <div class="col-sm-4">
                                        <s:textfield cssClass="form-control" id="alumnoNombre" name="nombre" placeholder="Nombre" 
                                                     pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s]+$" 
                                                     data-bv-regexp-message="Tu nombre sólo puede estar conformado por letras"
                                                     data-bv-stringlength="true" 
                                                     data-bv-stringlength-min="3" 
                                                     data-bv-stringlength-max="50" 
                                                     data-bv-stringlength-message="Tu nombre debe tener mínimo 3 letras"/>
                                    </div>
                                    <div class="col-sm-4">
                                        <s:textfield cssClass="form-control" id="alumnoApP" name="apPaterno" placeholder="Paterno"
                                                     pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s]+$" 
                                                     data-bv-regexp-message="Tu apellido sólo puede estar conformado por letras"
                                                     data-bv-stringlength="true" 
                                                     data-bv-stringlength-min="2" 
                                                     data-bv-stringlength-max="60" 
                                                     data-bv-stringlength-message="Tu apellido debe tener mínimo 2 letras"/>
                                    </div>
                                    <div class="col-sm-3">
                                        <s:textfield cssClass="form-control" id="alumnoApM" name="apMaterno" placeholder="Materno"
                                                     pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s]+$" 
                                                     data-bv-regexp-message="Tu apellido sólo puede estar conformado por letras"
                                                     data-bv-stringlength="true" 
                                                     data-bv-stringlength-min="3" 
                                                     data-bv-stringlength-max="50" 
                                                     data-bv-stringlength-message="Tu apellido debe tener mínimo 3 letras"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-sm-12 col-md-12 col-lg-12">
                                        <div class="pull-right">
                                            <input type="submit" id="buscar" class="btn btn-primary" value="Buscar alumno"/>
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div> <!-- Termina primer cuadro blanco -->

    <!-- DIV para la tabla -->
    <div class="row" id="div-tabla" style="display: none;">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <div class="col-md-12" style="padding: 0px">
                    <div class="responsive">                        
                        <table id="listado" class="table table-striped table-hover dt-responsive text-center" style="width: 100%">
                            <thead>
                                <tr>
                                    <th class="text-center" data-orderable="true"><b>Boleta</b></th>
                                    <th class="text-center" data-orderable="true"><b>Nombre</b></th>
                                    <th class="text-center" data-orderable="true"><b>Unidad Académica</b></th>
                                    <th class="text-center" data-orderable="true"><b>Cuestionario Salud</b></th>
                                </tr>
                            </thead>
                        </table>                        
                    </div>
                </div>
            </div>
        </div>                
    </div>

    <form id="eliminarForm" action="/misdatos/banderaESEPermisoCambio.action" method="post" >
        <input type="hidden" id="numeroDeBoleta" name="numeroDeBoleta" value="" />
    </form>
</body>

<content tag="endScripts">
    <script type="text/javascript" src="/vendors/fancy-box/jquery.fancybox.min.js"></script>
    <script type="text/javascript" src="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.js"></script>    
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js"></script>
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>
        $(document).ready(function () {
            $('#busquedaForm').bootstrapValidator().on('success.form.bv', function (e) {
                e.preventDefault();
                if (validarNoCamposVacios()) {
                    $("#errorFiltros").hide();
                    mostrarTablaResultados();
                } else {
                    $("#busquedaForm").data('bootstrapValidator').resetForm();
                    $("#buscar").blur();
                    $("#errorFiltros").show();
                }
            });
        });

        function mostrarTablaResultados() {
            var url = "/ajax/listadoPermisoCambioAjax.action";
            var bol = $('#alumnoBoleta').val();
            var cur = $('#alumnoCurp').val();
            var nom = $('#alumnoNombre').val();
            var ap = $('#alumnoApP').val();
            var am = $('#alumnoApM').val();
            url = url + "?numeroDeBoleta=" + bol + "&curp=" + cur + "&nombre=" + nom + "&apPaterno=" + ap + "&apMaterno=" + am;
            generarTabla("div-tabla", url, despuesDeCargarTabla);
            $("#buscar").blur();
            $("#busquedaForm").data('bootstrapValidator').resetForm();
        }

        function despuesDeCargarTabla() {
            $('.fancybox').fancybox({
                autoSize: true,
                afterClose: function () {
                    RecargarTabla();
                }
            });
        }

        function RecargarTabla() {
            var table = $('#listado').DataTable();
            table.draw(false);
        }


        function ESENoActivo() {
            ModalGenerator.notificacion({
                "titulo": "Alerta",
                "cuerpo": "El alumno no tiene contestado este cuestionario",
                "tipo": "ALERT"
            });
        }


        function validarNoCamposVacios() {
            var esValido = false;
            $('#busquedaForm *').filter('[type=text]').each(function () {
                if ($(this).val().length !== 0) {
                    esValido = true;
                }
            });
            return esValido;
        }

    </script>
</content>