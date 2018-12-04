<%-- 
    Document   : form
    Created on : 18/02/2016, 12:28:09 PM
    Author     : Tania G. Sánchez
    Redesing   : Mario Márquez
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Monitoreo</title>
    <link rel="stylesheet" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css" type="text/css" />    
    <link rel="stylesheet" type="text/css" href="/vendors/fancy-box/jquery.fancybox.min.css"/>
</head> 

<content tag="tituloJSP">
    Monitoreo
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
    </div> <!-- Terminan Alertas -->

    <!-- DIV Form -->
    <div class="row">
        <div class="col-xs-12">
            <div class="main-box clearfix" >
                <div class="row">                    
                    <div class="main-box-body clearfix">
                        <form class="form-horizontal" id="busquedaForm">
                            <div class="form-group ">
                                <label class="col-sm-2 control-label">Número de cuenta/tarjeta</label>
                                <div class="col-sm-10">
                                    <s:textfield 
                                        cssClass="form-control numeric"                                         
                                        id="numTarjeta" 
                                        name="numTarjeta"                                         
                                        placeholder="Número de tarjeta"                                        
                                        pattern="^[0-9\s]+$"                                         
                                        maxlength="16"
                                        data-bv-stringlength="true" 
                                        data-bv-stringlength-min="11" 
                                        data-bv-stringlength-max="16" 
                                        data-bv-regexp-message="El número de tarjeta sólo puede estar conformado por números"
                                        data-bv-stringlength-message="El número de tarjeta debe tener 16 números"                                         
                                        />
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-2 control-label">Boleta</label>
                                <div class="col-sm-10">
                                    <s:textfield 
                                        cssClass="form-control"
                                        id="alumnoBoleta"
                                        name = "numeroDeBoleta"
                                        placeholder="Boleta"
                                        pattern="^[a-zA-Z,0-9\s]+$"                                          
                                        data-bv-stringlength="true" 
                                        data-bv-stringlength-min="10" 
                                        data-bv-stringlength-max="10"                                        
                                        maxlength="10" 
                                        data-bv-regexp-message="La boleta no tiene el formato correcto"
                                        data-bv-stringlength-message="La boleta debe tener 10 caracteres"
                                        />
                                </div>
                            </div>

                            <div class="form-group ">
                                <label class="col-sm-2 control-label">Curp</label>
                                <div class="col-sm-10">
                                    <s:textfield 
                                        cssClass="form-control uppercase"
                                        id="alumnoCurp"
                                        name = "curp"
                                        placeholder="Curp"
                                        pattern="^[A-Z]{1}[AEIOU]{1}[A-Z]{2}[0-9]{2}(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])[HM]{1}(AS|BC|BS|CC|CS|CH|CL|CM|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|NE)[B-DF-HJ-NP-TV-Z]{3}[0-9A-Z]{1}[0-9]{1}$" 
                                        data-bv-stringlength="true" 
                                        data-bv-stringlength-min="18" 
                                        data-bv-stringlength-max="18" 
                                        maxlength="18" 
                                        data-bv-regexp-message="Tu CURP no tiene el formato correcto"
                                        data-bv-stringlength-message="La CURP debe estar conformada por 18 caracteres"
                                        />
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-2 control-label">Nombre</label>
                                <div class="col-sm-4">
                                    <s:textfield 
                                        cssClass="form-control"
                                        id="alumnoNombre"
                                        name = "nombre"
                                        placeholder="Nombre"
                                        pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s]+$"                                         
                                        data-bv-stringlength="true" 
                                        data-bv-stringlength-min="3" 
                                        data-bv-stringlength-max="50" 
                                        maxlength="50" 
                                        data-bv-regexp-message="Tu nombre sólo puede estar conformado por letras"
                                        data-bv-stringlength-message="Tu nombre debe tener mínimo 3 letras"
                                        />
                                </div>
                                <div class="col-sm-3">
                                    <s:textfield 
                                        cssClass="form-control"
                                        id="alumnoApP"
                                        name = "apPaterno"
                                        placeholder="Paterno"
                                        pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s]+$" 
                                        data-bv-regexp-message="Tu apellido sólo puede estar conformado por letras"
                                        data-bv-stringlength="true" 
                                        data-bv-stringlength-min="2" 
                                        data-bv-stringlength-max="60" 
                                        maxlength="60" 
                                        data-bv-stringlength-message="Tu apellido debe tener mínimo 2 letras"
                                        />
                                </div>
                                <div class="col-sm-3">
                                    <s:textfield 
                                        cssClass="form-control"
                                        id="alumnoApM"
                                        name = "apMaterno"
                                        placeholder="Materno"
                                        data-bv-message="Este dato no es válido"
                                        pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s]+$" 
                                        data-bv-regexp-message="Tu apellido sólo puede estar conformado por letras"
                                        data-bv-stringlength="true" 
                                        data-bv-stringlength-min="3" 
                                        data-bv-stringlength-max="50" 
                                        maxlength="50" 
                                        data-bv-stringlength-message="Tu apellido debe tener mínimo 3 letras"
                                        />
                                </div>
                            </div>
                            <div class=" form-group">
                                <div class="col-xs-12">
                                    <button id="buscarButton" type="submit" class="btn btn-primary pull-right">Buscar alumno</button>
                                </div>                            
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div> <!-- Termina Div Form -->

    <!-- Div tabla -->
    <div class="row" id="div-tabla" style="display: none;">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <div class="col-md-12" style="padding: 0px">
                    <div class="responsive">                        
                        <table id="listado" class="table table-striped table-hover dt-responsive" style="width: 100%">
                            <thead>
                                <tr>
                                    <th data-orderable="true" data-center="true">Boleta</th>
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
    </div>
    <!--Termina tabla --> 

</body>

<content tag="endScripts">
    <script type="text/javascript" src="/vendors/fancy-box/jquery.fancybox.min.js"></script>
    <script type="text/javascript" src="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.js"></script>    
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>
        $(document).ready(function () {

            $('#busquedaForm').bootstrapValidator({
                feedbackIcons: {
                    valid: 'glyphicon glyphicon-ok',
                    invalid: 'glyphicon glyphicon-remove',
                    validating: 'glyphicon glyphicon-refresh'
                }}).on('success.form.bv', function (e) {
                e.preventDefault();
                if (validarNoCamposVacios()) {
                    $("#errorFiltros").hide();
                    buscar();
                    $("#busquedaForm").data('bootstrapValidator').resetForm();
                    $("#buscarButton").blur();
                } else {
                    $("#busquedaForm").data('bootstrapValidator').resetForm();
                    $("#buscarButton").blur();
                    $("#errorFiltros").show();
                }
            }
            );
            $('.numeric').on('input', function (event) {
                this.value = this.value.replace(/[^0-9]/g, '');
            });
        });

        function validarNoCamposVacios() {
            var esValido = false;
            $('#busquedaForm *').filter('[type=text]').each(function () {
                if ($(this).val().length !== 0) {
                    esValido = true;
                }
            });
            return esValido;
        }

        function buscar() {
            $.fancybox.close();

            var url = "/ajax/alumnosBitacoraTarjetaAjax.action";
            var noTarjeta = $('#numTarjeta').val();
            var bol = $('#alumnoBoleta').val();
            var cur = $('#alumnoCurp').val();
            var nom = $('#alumnoNombre').val();
            var ap = $('#alumnoApP').val();
            var am = $('#alumnoApM').val();
            url += "?noTarjeta=" + noTarjeta + "&numeroDeBoleta=" + bol + "&curp=" + cur + "&nombre=" + nom + "&apPaterno=" + ap + "&apMaterno=" + am;

            generarTabla("div-tabla", url, tablaDibujada, false);
        }

        function tablaDibujada() {
            $(".fancybox").fancybox({
                autoSize: true
            });
        }
    </script>
</content>