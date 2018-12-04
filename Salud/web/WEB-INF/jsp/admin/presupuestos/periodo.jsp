
<%-- 
    Document   : listado
    Created on : 28-ago-2015, 10:23:12
    Author     : Patricia Benitez
    Redesign     : Gustavo Adolfo Alamillo Medina
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Administración de presupuestos</title>
    <link rel="stylesheet" type="text/css" href="/vendors/fancy-box/jquery.fancybox.min.css"/>
</head> 

<content tag="tituloJSP">
    Presupuesto por periodo
</content>

<body>

    <div class="row" id="rowTabla" style="display: none;">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <div class="col-md-12" style="padding: 0px">
                    <div class="responsive">                        
                        <table id="tablaPeriodopresupuesto" class="table table-striped table-hover dt-responsive" style="width: 100%">
                            <thead>
                                <tr>
                                    <th>Periodo</th>
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

</body>

<content tag="endScripts">
    <script type="text/javascript" src="/vendors/fancy-box/jquery.fancybox.min.js"></script>
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js" 
            data-init="true" data-ajax="/ajax/listadoPeriodoPresupuestoAjax.action" data-div="rowTabla"
    data-function="despuesCargarTabla"></script>
</content>

<content tag="inlineScripts">
    <script>

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

        function recargarPagina() {
            $('#listado').show();
            generarTabla("rowTabla", "/ajax/listadoPeriodoPresupuestoAjax.action", despuesCargarTabla);

        }

        function despuesCargarTabla() {
            $('.fancybox').fancybox({
                afterClose: function () {
                    recargarTabla();
                }
            });
        }

        function recargarTabla() {
            generarTabla("rowTabla", "/ajax/listadoPeriodoPresupuestoAjax.action", despuesCargarTabla);
        }

        function save(id) {
            var periodoId = $('#periodo_' + id).val();
            var presupuestoId = $('#presupuesto_' + id).val();
            var monto = $('#monto_' + id).val();
            guardaDatos(presupuestoId, periodoId, monto);
        }

        function guardaDatos(presupuestoId, periodoId, monto) {
            $.ajax({
                type: 'POST',
                url: '/ajax/guardarPeriodoPresupuestoAjax.action',
                beforeSend: function () {
                    ModalGenerator.notificacion({
                        "titulo": "Información",
                        "cuerpo": "Se está guardando su información. Por favor, espere",
                        "tipo": "WARNING",
                        "sePuedeCerrar": false
                    });
                },
                dataType: 'json',
                data: {periodoId: periodoId,
                    presupuestoId: presupuestoId,
                    monto: monto},
                cache: false,
                success: function (aData) {
                    cerrarModal();
                    if (aData.data[0] !== "OK") {
                        ModalGenerator.notificacion({
                            "titulo": "Atención",
                            "cuerpo": aData.data[0],
                            "tipo": "ALERT"
                        });
                    } else {
                       recargarPagina();
                    }

                },
                error: function () {
                    cerrarModal();
                    ModalGenerator.notificacion({
                        "titulo": "Atención",
                        "cuerpo": 'Hubo un problema que impidió que se completara la operación.',
                        "tipo": "ALERT"
                    });
                }
            });
            return false;
        }

    </script>
</content>