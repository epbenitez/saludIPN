<%-- 
    Document   : lista
    Created on : 4/07/2016, 04:31:47 PM
    Author     : Victor Lozano
    Rediseño   : Gustavo A. Alamillo
    Modified   : 11-Oct-2015
--%>

<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<head>
    <title>Becas disponibles</title>        
    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css"/>
    <link rel="stylesheet" type="text/css" href="/vendors/fancy-box/jquery.fancybox.min.css"/>
</head> 

<content tag="tituloJSP">
    Becas disponibles
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
            <div class="main-box" >
                <div class="main-box-body">
                    <form class="form-horizontal" id="busquedaForm" role="form"
                          data-bv-feedbackicons-valid="glyphicon glyphicon-ok"
                          data-bv-feedbackicons-invalid="glyphicon glyphicon-remove"
                          data-bv-feedbackicons-validating="glyphicon glyphicon-refresh">
                        <div class="form-group">
                            <label for="alumnoBoleta" class="col-sm-1 control-label">Boleta</label>                            
                            <div class="col-sm-11">                                
                                <s:textfield cssClass="form-control" id="alumnoBoleta" name="numeroDeBoleta"  placeholder="Boleta"
                                             data-bv-stringlength="true" 
                                             data-bv-stringlength-max="10"
                                             data-bv-stringlength-min="10"
                                             data-bv-stringlength-message="La boleta debe tener 10 caracteres"/>                            
                            </div>
                        </div>                         
                        <div class="form-group">
                            <label class="col-sm-1 control-label" for="alumnoCurp">Curp</label>                            
                            <div class="col-sm-11">
                                <s:textfield 
                                    cssClass="form-control" id="alumnoCurp" name="curp" placeholder="Curp"                                                                                                      
                                    pattern="^[a-zA-Z]{4}((\d{2}((0[13578]|1[02])(0[1-9]|[12]\d|3[01])|(0[13456789]|1[012])(0[1-9]|[12]\d|30)|02(0[1-9]|1\d|2[0-8])))|([02468][048]|[13579][26])0229)(H|M)(AS|BC|BS|CC|CL|CM|CS|CH|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|SM|NE)([a-zA-Z]{3})([a-zA-Z0-9\s]{1})\d{1}$" 
                                    data-bv-regexp-message="La CURP no tiene el formato correcto"
                                    data-bv-stringlength="true" 
                                    data-bv-stringlength-min="18" 
                                    data-bv-stringlength-max="18" 
                                    data-bv-stringlength-message="La CURP debe estar conformada por 18 caracteres"/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="numTarjeta" class="col-sm-1 control-label">Nombre</label>
                            <div class="col-sm-4">
                                <s:textfield cssClass="form-control" id="alumnoNombre" name = "nombre" placeholder="Nombre" 
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
    </div> <!-- Termina primer cuadro blanco -->


    <div class="row" id="tabla" style="display: none">
        <div class="col-xs-12">
            <div class="main-box clearfix" >
                <div class="col-md-12" style="padding: 0px;">
                    <div class="responsive">
                        <table id="listado" class="table table-striped table-hover dt-responsive" style="width: 100%">
                            <thead>
                                <tr>
                                    <th data-center="true" data-orderable="true">Boleta</th>
                                    <th data-orderable="true">Nombre</th>
                                    <th data-orderable="true">Unidad Académica</th>
                                    <th data-center="true"></th>
                                </tr>
                            </thead>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div> <!-- Termina segundo cuadro blanco -->

</body>

<content tag="endScripts">
    <script type="text/javascript" src="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.js"></script>
    <script type="text/javascript" src="/vendors/fancy-box/jquery.fancybox.min.js"></script>    
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>
        $(document).ready(function () {

            $("#busquedaForm").bootstrapValidator()
                    .on('success.form.bv', function (e) {
                        e.preventDefault();
                        if (validarNoCamposVacios()) {
                            $("#errorFiltros").hide();
                            buscarBecasDisponibles();
                        } else {
                            $("#busquedaForm").data('bootstrapValidator').resetForm();
                            $("#buscar").blur();
                            $("#errorFiltros").show();
                        }
                    });

            $("#cleanBtn").on("click", function () {
                $("#busquedaForm").data('bootstrapValidator').resetForm();
                $(this).blur();
            });

        });

        function buscarBecasDisponibles() {

            var url = "/ajax/listadoBecasDisponiblesAjax.action";
            var bol = $('#alumnoBoleta').val();
            var cur = $('#alumnoCurp').val();
            var nom = $('#alumnoNombre').val();
            var ap = $('#alumnoApP').val();
            var am = $('#alumnoApM').val();
            url = url + "?numeroDeBoleta=" + bol + "&curp=" + cur + "&nombre=" + nom + "&apPaterno=" + ap + "&apMaterno=" + am;
            generarTabla("tabla", url, ponerFancyBox);
            $("#busquedaForm").data('bootstrapValidator').resetForm();
            $("#buscar").blur();
        }

        function ponerFancyBox() {
            $('.fancybox').fancybox({
                autoSize: true,
                afterClose: function () {
                    var table = $('#listado').DataTable();
                    table.draw(false);
                }
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
