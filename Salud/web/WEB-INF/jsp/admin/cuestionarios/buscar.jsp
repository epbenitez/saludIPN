<%-- 
    Document   : buscar
    Created on : 29/07/2016, 02:16:34 PM
    Author     : Tania G. Sánchez
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<head>
    <title>Periodo</title>
    <!-- Add fancyBox main JS and CSS files -->
    <script type="text/javascript" src="/resources/js/fancybox/source/jquery.fancybox.js?v=2.1.5"></script>
    <link rel="stylesheet" type="text/css" href="/resources/js/fancybox/source/jquery.fancybox.css?v=2.1.5" media="screen" />
    <!-- Add Button helper (this is optional) -->
    <link rel="stylesheet" type="text/css" href="/resources/js/fancybox/source/helpers/jquery.fancybox-buttons.css?v=1.0.5" />
    <script type="text/javascript" src="/resources/js/fancybox/source/helpers/jquery.fancybox-buttons.js?v=1.0.5"></script>
    <!-- Add Thumbnail helper (this is optional) -->
    <link rel="stylesheet" type="text/css" href="/resources/js/fancybox/source/helpers/jquery.fancybox-thumbs.css?v=1.0.7" />
    <script type="text/javascript" src="/resources/js/fancybox/source/helpers/jquery.fancybox-thumbs.js?v=1.0.7"></script>
    <!-- Add Media helper (this is optional) -->
    <script type="text/javascript" src="/resources/js/fancybox/source/helpers/jquery.fancybox-media.js?v=1.0.6"></script>
    <script type="text/javascript" language="javascript" class="init">
        $(function () {
            //SE AGREGA LA CLASE A ELEMENTOS QUE SON CREADOS MEDIANTE STRUTS
            $('input[type=text]').addClass('form-control');
            $('select').addClass('form-control');
        });

        $(document).ready(function () {
            $('.fancybox').fancybox({
                autoSize: true,
                afterClose: function () {
                    $('#nueva-entidad').insertAfter("h1");
                }
            });
            $('#busca').hide();
            $('#lista').hide();
            $('#eses').bootstrapValidator({});
            $('#buscar').on('click', function (e) {
                var numeroBoleta = $('#numeroBoleta').val();
                var curp = $('#curp').val();
                var nombre = $('#nombre').val();
                var aPaterno = $('#aPaterno').val();
                var aMaterno = $('#aMaterno').val();
                if (numeroBoleta !== '' || curp !== '' || nombre !== '' || aPaterno !== '' || aMaterno !== '') {
                    buscarAlumno();
                } else {
                    BootstrapDialog.alert("Ingrese al menos un parámetro a la búsqueda.");
                }
            });
        });

        function buscarAlumno() {
            $('#busca').show();
            $('#lista').show();
            if ($.fn.dataTable.isDataTable('#listado')) {
                table = $('#listado').DataTable();
                table.destroy();
            }
            var url = '/ajax/buscarCuestionarioTransporteAjax.action';
            var numeroBoleta = $('#numeroBoleta').val();
            var curp = $('#curp').val();
            var nombre = $('#nombre').val();
            var aPaterno = $('#aPaterno').val();
            var aMaterno = $('#aMaterno').val();
            url = url + "?numeroBoleta=" + numeroBoleta + "&curp=" + curp + "&nombre=" + nombre
                    + "&aPaterno=" + aPaterno + "&aMaterno=" + aMaterno;
            $('#listado').DataTable({
                "ajax": url,
                "order": [[0, 'asc']]
            });
        }
    </script>
</head>
<div class="row">
    <div class="col-sm-12">
        <div class="row">
            <div class="col-sm-12">
                <h1>Listado de Alumnos</h1>
            </div>
        </div>
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
        <div class="row">
            <div class="col-sm-12">
                <div class="main-box clearfix" >
                    <div class="main-box-body ">
                        <form id="eses" action="/admin/buscarCuestionario.action" method="post" class="form-horizontal">
                            <div class="form-group">
                                <label class="col-sm-2 control-label" align="right">Número de boleta</label>
                                <div class="col-sm-9">
                                    <s:textfield class="form-control" name="numeroBoleta" id="numeroBoleta" placeholder="Número de boleta"
                                                 value="" data-bv-message="Este dato no es válido"
                                                 pattern="^[0-9\s]+$" 
                                                 data-bv-regexp-message="El número de boleta sólo puede estar conformado por números"
                                                 data-bv-stringlength="true" 
                                                 data-bv-stringlength-min="10" 
                                                 data-bv-stringlength-max="10" 
                                                 maxlength="16"/>
                                    <span class="help-block" id="estadoMessage" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label" align="right">CURP</label>
                                <div class="col-sm-9">
                                    <s:textfield class="form-control" name="alumno.curp" id="curp" placeholder="CURP"
                                                 value="" data-bv-message="Este dato no es válido"
                                                 required="true" 
                                                 data-bv-notempty-message="El CURP es requerido"
                                                 pattern="^[a-zA-Z]{4}((\d{2}((0[13578]|1[02])(0[1-9]|[12]\d|3[01])|(0[13456789]|1[012])(0[1-9]|[12]\d|30)|02(0[1-9]|1\d|2[0-8])))|([02468][048]|[13579][26])0229)(H|M)(AS|BC|BS|CC|CL|CM|CS|CH|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|SM|NE)([a-zA-Z]{3})([a-zA-Z0-9\s]{1})\d{1}$" 
                                                 data-bv-regexp-message="Tu CURP no tiene el formato correcto"
                                                 data-bv-stringlength="true" 
                                                 data-bv-stringlength-min="18" 
                                                 data-bv-stringlength-max="18" 
                                                 data-bv-stringlength-message="Tu CURP debe estar conformado por 18 caracteres"
                                                 onblur="$(this).val($(this).val().toUpperCase())" />
                                    <span class="help-block" id="curpMessage" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label" align="right">Nombre</label>
                                <div class="col-sm-3">
                                    <s:textfield class="form-control" name="nombre" id="nombre" placeholder="Nombre"
                                                 value="" data-bv-message="Este dato no es válido"
                                                 pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s]+$"  
                                                 data-bv-regexp-message="El nombre sólo puede estar conformado por letras"
                                                 data-bv-stringlength="true" 
                                                 maxlength="16"/>
                                    <span class="help-block" id="estadoMessage" />
                                </div>
                                <div class="col-sm-3">
                                    <s:textfield class="form-control" name="aPaterno" id="aPaterno" placeholder="Apellido paterno"
                                                 value="" data-bv-message="Este dato no es válido"
                                                 pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s]+$"  
                                                 data-bv-regexp-message="El apellido paterno sólo puede estar conformado por letras"
                                                 data-bv-stringlength="true" 
                                                 maxlength="16"/>
                                </div>
                                <div class="col-sm-3">
                                    <s:textfield class="form-control" name="aMaterno" id="aMaterno" placeholder="Apellido materno"
                                                 value="" data-bv-message="Este dato no es válido"
                                                 pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s]+$"  
                                                 data-bv-regexp-message="El apellido materno sólo puede estar conformado por letras"
                                                 data-bv-stringlength="true" 
                                                 maxlength="16"/>
                                    <span class="help-block" id="estadoMessage" />
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-11">
                                    <button id="buscar" class="btn btn-primary pull-right">Buscar</button>
                                </div>
                            </div>
                        </form>  
                    </div>
                </div>
                <div id = "lista">
                    <div class="col-sm-12 main-box clearfix">
                        <div class="responsive-table table-responsive clearfix">
                            <table id="listado" class="table table-striped table-hover">
                                <thead>
                                    <tr>
                                        <th>Número de boleta</th>
                                        <th>CURP</th>
                                        <th>Nombre</th>
                                        <th>Unidad Académica</th>
                                        <th>Semestre</th>
                                        <th>Promedio</th>
                                        <th>ESE</th>
                                        <th>ESET</th>
                                    </tr>
                                </thead>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>