<%-- 
    Document   : form
    Created on : 27/10/2015, 02:09:23 PM
    Author     : Victor Lozano
    Redesing   : Mario Márquez  
--%>

<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <link rel="stylesheet" type="text/css" href="/vendors/fancy-box/jquery.fancybox.min.css"/>
    <title>Personalización de tarjetas</title>
</head> 

<content tag="tituloJSP">
    Personalización de tarjetas
</content>

<body>

    <!-- Ejemplo de caja blanca -->
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
                <div class="main-box-body clearfix">
                    <form class="form-horizontal" id="busquedaForm">
                        <div class="form-group col-sm-12 ">
                            <label class="col-sm-2 control-label">Identificador de asignación</label>
                            <div class="col-sm-10">
                                <s:select 
                                    id="identificadores"
                                    name="identificadores"
                                    cssClass="form-control"
                                    list="identificadores"
                                    headerValue="Todos"
                                    headerKey=""
                                    />
                            </div>
                        </div>

                        <div class="col-xs-12 form-group">
                            <div class="col-xs-offset-2 col-xs-10">
                                <!--<a href="#" onclick='reload()' class="btn btn-primary">Buscar</a>-->
                                <!--<a href="#" onclick="agregarValores()" class="btn btn-primary"  name="nuevo" value="Nuevo" id="guardar" >Guardar</a>-->
                                <button id="buscarButton" type="button" class="btn btn-primary pull-right">Buscar</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div> <!-- Termina Div Form -->

    <!-- Ejemplo tabla -->
    <div class="row" id="div-tabla" style="display: none;">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <div class="col-md-12" style="padding: 0px">
                    <div class="responsive">                        
                        <table id="lista" class="table table-striped table-hover dt-responsive" style="width: 100%">
                            <thead>
                                <tr>
                                    <th><input type="checkbox" id="todasliberada"><br/>L</th>
                                    <th>R</th>
                                    <th data-orderable="true">No. Tarjeta</th>
                                    <th data-orderable="true">Boleta</th>
                                    <th data-orderable="true">Nombre</th>
                                    <th>Observaciones</th>
                                </tr>
                            </thead>
                        </table>                        
                    </div>
                </div>
            </div>
        </div>                
    </div>
    <!--Termina ejemplo tabla --> 
    <form id="personaliza"  name="cambiarForm">
        <input type="hidden" name="liberadas" id="liberadas" />
        <input type="hidden" name="rechazadas" id="rechazadas" />
    </form>
</body>

<content tag="endScripts">
    <script type="text/javascript" src="/vendors/fancy-box/jquery.fancybox.min.js"></script>
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js"></script>
    <!--<script type="text/javascript" src="/resources/js/min/generador-tablas.min.js" data-init="true" data-ajax="/ajax/listadoPersonalizacionTarjetaAjax.action?identificador=<s:property value="identificador.id"/>" data-div="div-tabla"></script>-->
</content>

<content tag="inlineScripts">
    <script>
    $(document).ready(function () {
        $('#buscarButton').click(function (e) {
            buscar();
            $('#buscarButton').blur();
        });

        $("#todasliberada").change(function () {
            if (this.checked) {
                $('.rl').prop('checked', true);
            } else {
                $('.rl').prop('checked', false);
            }
        });
        
    });
    
    function editarObservaciones(element) {
        var id = $(element).attr("id");
        var b = "#bitacora" + id;
        $(b).val("");
        $(b).focus();
    }
    
    function confirmar(){
        var liberados = "", rechazados = "";
            var len = $('.rl:checked').length;
            var flagObservaciones = false;
            $('.rl:checked').each(function (index, element) {
                var id = $(element).attr("id");
                var b = "#bitacora" + id;
                if ($.trim($(b).val()) == "") {
                    $(b).css("border-color", "#dd191d");
                    $(b).focus();
                    flagObservaciones = true;
                } else {
                    liberados += id + "_" + $(b).val();
                    if (index < len - 1)
                        liberados += "|";
                }
            });
            $("#liberadas").val(liberados);
            len = $('.rr:checked').length;
            $('.rr:checked').each(function (index, element) {
                var id = $(element).attr("id");
                var b = "#bitacora" + id;
                if ($.trim($(b).val()) == "") {
                    $(b).css("border-color", "#dd191d");
                    $(b).focus();
                    flagObservaciones = true;
                } else {
                    rechazados += id + "_" + $(b).val();
                    if (index < len - 1)
                        rechazados += "|";
                }
            });
            if (flagObservaciones)
                return false;
            $("#rechazadas").val(rechazados);
            $.ajax({
                type: 'POST',
                url: '/ajax/personalizarTarjetaAjax.action',
                dataType: 'json',
                data: {liberadas: liberados,
                    rechazadas: rechazados},
                cache: false,
                success: function (adata) {
                    buscar();
                    if (adata.data[0][0] != "success") {
                        BootstrapDialog.alert("Ocurrió un error al confirmar.");
                        //BootstrapDialog.alert("Se confirmo correctamente.");
                    }
                    $("#todasliberada").prop('checked', false);
                }
            });
            //reload();
    }

    function buscar() {
        var url = "/ajax/listadoPersonalizacionTarjetaAjax.action";
        var iden = $("#identificadores").val();
        url = url + "?identificador=" + iden;
        generarTabla("div-tabla", url,null, true, botones);
        }

        var botones = [
            {                   
                text: '<a id="confirmar" class="btn btn-primary " onClick="confirmar()">Guardar</a>',
            }];
    
    
    </script>        
</content>