<%-- 
    Document   : unidadacademica
    Created on : 14-ene-2016, 10:23:12
    Author     : Patricia Benitez
    Redesing   : Mario Márquez
--%>

<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Administración de presupuestos</title>

</head> 

<content tag="tituloJSP">
    Administración de presupuestos
</content>

<body>

    <!-- Ejemplo de caja blanca -->
    <div id="div-datosPeriodo" style="display: none;">
        <div class="row">
            <div class="col-xs-12">
                <div class="main-box clearfix" >
                    <div class="row">
                        <header class="main-box-header clearfix">
                            <h2 class="pull-left">Presupuesto por periodo</h2>
                        </header>                    
                    </div>
                    <div class="col-md-12" style="padding: 0px">
                        <div class="responsive">                        
                            <table id="datosPeriodo" class="table table-striped table-hover dt-responsive" style="width: 100%">
                                <thead>
                                    <tr>
                                        <th data-orderable="false">Periodo</th>
                                        <th data-orderable="false">Presupuesto total</th>
                                        <th data-orderable="false">Presupuesto asignado</th>
                                        <th data-orderable="false">Presupuesto ejercido</th>
                                    </tr>
                                </thead>
                            </table>                        
                        </div>
                    </div>

                    <div class="col-sm-12 col-md-12 col-lg-12">
                        <div class="pull-right">
                            <a href="/admin/listaPresupuesto.action" class="btn btn-primary" role="button">
                                <i class="fa fa-arrow-circle-left fa-lg"></i>
                                Regresar a presupuesto por periodo
                            </a>
                            <br>
                        </div>
                    </div>

                </div>
            </div>
        </div> <!-- Termina ejemplo caja blanca -->
    </div>

    <!-- Ejemplo tabla -->
    <div id="div-listado" style="display: none;">
        <div class="row" >
            <div class="col-xs-12">
                <div class="main-box clearfix">
                    <div class="col-md-12" style="padding: 0px">
                        <div class="responsive">                        
                            <table id="listado" class="table table-striped table-hover dt-responsive" style="width: 100%">
                                <thead>
                                    <tr>
                                        <th>Programa</th>
                                        <th>Presupuesto total</th>
                                        <th>Presupuesto asignado</th>
                                        <th>Presupuesto ejercido</th>
                                        <th></th>
                                        <th></th>
                                    </tr>
                                </thead>
                            </table>                        
                        </div>
                    </div>
                </div>
            </div>                
        </div>
    </div>
    <!--Termina ejemplo tabla --> 
</body>

<content tag="endScripts">
    <script type="text/javascript" src="/resources/js/generador-tablas.js" data-init="true" data-ajax="/ajax/datosPeriodoPresupuestoAjax.action?periodoId=<s:property value="periodo.id"/>" data-div="div-datosPeriodo"></script>
    <script type="text/javascript" src="/resources/js/generador-tablas.js" data-init="true" data-ajax="/ajax/listadoTipoBecaPresupuestoAjax.action?periodoId=<s:property value="periodo.id"/>" data-div="div-listado"></script>
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>

</content>

<content tag="inlineScripts">
    <script>
        function formatPesos(num) {
            var p = num.split(".");
            return "$" + p[0].split("").reverse().reduce(function (acc, num, i, orig) {
                return  num == "-" ? acc : num + (i && !(i % 3) ? "," : "") + acc;
            }, ""); // + "." + p[1];
        }

        function reload() {
            var idPeriodo = "<s:property value="periodo.id"/>";
            var urlListado = "/ajax/listadoTipoBecaPresupuestoAjax.action?periodoId=" + idPeriodo;
            var urlDatosPeriodo = "/ajax/datosPeriodoPresupuestoAjax.action?periodoId=" + idPeriodo;

            generarTabla("div-listado", urlListado);
            generarTabla("div-datosPeriodo", urlDatosPeriodo);

            $('#datosPeriodo_length').hide();
            $('#datosPeriodo_filter').hide();
            $('#datosPeriodo_info').hide();
            $('#paginateFooter-div-datosPeriodo').hide();
        }

        function asignarPresupuesto(periodoId, tipoBecaPeriodo_id, presupuestoTipoBecaPeriodo_id) {
            if ($('#div_' + tipoBecaPeriodo_id).html() == "$0") {
                ModalGenerator.notificacion({
                    "titulo": "Atención",
                    "cuerpo": "Para poder administrar este presupuesto, es necesario establecer el monto para el presupuesto total del programa de beca seleccionado.",
                    "tipo": "WARNING",
                });
                showInput(tipoBecaPeriodo_id);
                return false;
            } else {
                window.location = "/admin/listaPresupuesto.action?periodo.id=" + periodoId + "&tipoBeca.id=" + tipoBecaPeriodo_id + "&presupuestoTipoBecaPeriodo.id=" +presupuestoTipoBecaPeriodo_id;
            }
        }

        function showInput(id) {
            $('#monto_' + id).show();
            $('#monto_' + id).addClass('form-control');
            $('#monto_' + id).focus();
            $('#div_' + id).hide();
            $('#ver_' + id).hide();
            $('li #ver_' + id).hide();      //POR SI EL BOTON SE MUESTRA EN EL DETALLE DE LAS COLUMNAS NO VISIBLES
            $('#guardar_' + id).show();
            $('li #guardar_' + id).show();      //POR SI EL BOTON SE MUESTRA EN EL DETALLE DE LAS COLUMNAS NO VISIBLES
        }

        function hideInput(id, monto) {
            $('#monto_' + id).hide();
//            $('#monto_' + id).addClass('form-control');
//            $('#monto_' + id).focus();
            $('#div_' + id).show();
            $('#div_' + id).html(formatPesos((monto)));
            $('#ver_' + id).show();
            $('li #ver_' + id).show();      //POR SI EL BOTON SE MUESTRA EN EL DETALLE DE LAS COLUMNAS NO VISIBLES
            $('#guardar_' + id).hide();
            $('li #guardar_' + id).hide();      //POR SI EL BOTON SE MUESTRA EN EL DETALLE DE LAS COLUMNAS NO VISIBLES
        }

        function save(id) {
            var periodoId = $('#periodo_' + id).val();
            var tipoBecaId = $('#tipoBeca_' + id).val();
            var monto = $('#monto_' + id).val();
            guardaDatos(tipoBecaId, periodoId, monto, id);
        }

        function guardaDatos(tipoBecaId, periodoId, monto, id) {
            $.ajax({
                type: 'POST',
                url: '/ajax/guardarTipoBecaPresupuestoAjax.action',
                beforeSend: function () {
                    ModalGenerator.notificacion({
                        "titulo": "Guardando",
                        "cuerpo": "Se está guardando su información. Por favor, espere.",
                        "tipo": "INFO",
                        "sePuedeCerrar": false
                    });

                },
                dataType: 'json',
                data: {periodoId: periodoId,
                    tipoBecaId: tipoBecaId,
                    monto: monto},
                cache: false,
                success: function (aData) {
                    ModalGenerator.cerrarModales();
                    if (aData.data[0] != "OK") {
                        ModalGenerator.notificacion({
                            "titulo": "Atención",
                            "cuerpo": aData.data[0],
                            "tipo": "WARNING",
                        });
                    } else {
//                        reload();

                        hideInput(id, monto);
                    }

                },
                error: function (aData) {
                    ModalGenerator.cerrarModales();
                    var jsonobject = JSON.stringify(aData.responseText);
                    ModalGenerator.notificacion({
                        "titulo": "Error",
                        "cuerpo": eval(jsonobject.replace('[', '').replace(']', '')),
                        "tipo": "WARNING",
                    });
                }
            });
            return false;
        }
    </script>
</content>