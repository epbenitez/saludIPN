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
    <div id="div-datosPrograma" style="display: none;">
    <div class="row">
        <div class="col-xs-12">
            <div class="main-box clearfix" >
                <div class="row">
                    <header class="main-box-header clearfix">
                        <h2 class="pull-left">Presupuesto por Unidad Académica</h2>
                    </header>                    
                </div>
                <div class="col-md-12" style="padding: 0px">
                    <div class="responsive">                        
                        <table id="datosPrograma" class="table table-striped table-hover dt-responsive" style="width: 100%">
                            <thead>
                                <tr>
                                    <th data-orderable="false">Periodo</th>
                                    <th data-orderable="false">Programa</th>
                                    <th>Monto por Beca Mensual</th>
                                    <th data-orderable="false">Monto por Beca por Periodo</th>
                                    <th data-orderable="false">Presupuesto asignado</th>
                                    <th data-orderable="false">Presupuesto ejercido</th>
                                </tr>
                            </thead>
                        </table>                        
                    </div>
                </div>
                
                <div class="col-sm-12 col-md-12 col-lg-12">
                    <div class="pull-right">
                        <a href="/admin/listaPresupuesto.action?periodo.id=<s:property  value = "periodo.id" />" class="btn btn-primary" role="button">
                            <i class="fa fa-arrow-circle-left fa-lg"></i>
                            Regresar a presupuesto por tipo de beca
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
                                    <th>Unidad académica</th>
                                    <th>Becas asignadas</th>
                                    <th>Presupuesto asignado</th>
                                    <th>Becas ejercidas</th>
                                    <th>Presupuesto ejercido</th>
                                    <th>Becas disponibles</th>
                                    <th></th>
                                </tr>
                            </thead>
                        </table>                        
                    </div>
                    <s:hidden id="presupuestoTipoBecaPeriodoId" cellspacing="0" name="presupuestoTipoBecaPeriodo.id" />
                </div>
            </div>
        </div>                
    </div>
    </div>
    <!--Termina ejemplo tabla --> 
</body>

<content tag="endScripts">
    <script type="text/javascript" src="/resources/js/generador-tablas.js" data-init="true" data-ajax="/ajax/datosTipoBecaPresupuestoAjax.action?periodoId=<s:property value="periodo.id"/>&tipoBecaId=<s:property  value="tipoBeca.id" />" data-div="div-datosPrograma"></script>
    <script type="text/javascript" src="/resources/js/generador-tablas.js" data-init="true" data-ajax="/ajax/listadoUnidadAcademicaPresupuestoAjax.action?periodoId=<s:property value="periodo.id"/>&tipoBecaId=<s:property  value = "tipoBeca.id" />&presupuestoTipoBecaPeriodoId=<s:property  value = "presupuestoTipoBecaPeriodo.id" />" data-div="div-listado"></script>
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>
        function reload() {             
            var idPeriodo = "<s:property value="periodo.id"/>";
            var tipoBeca = "<s:property  value="tipoBeca.id" />";
            var presupuestoTB = "<s:property  value = "presupuestoTipoBecaPeriodo.id" />";
            var urldatosPrograma = "/ajax/listadoTipoBecaPresupuestoAjax.action?periodoId=" 
                + idPeriodo + "&tipoBecaId=" + tipoBeca;
            var urlListado = "/ajax/listadoUnidadAcademicaPresupuestoAjax.action?periodoId" 
                + idPeriodo + "&tipoBecaId=" + tipoBeca + "&presupuestoTipoBecaPeriodoId=" 
                + presupuestoTB;
            
            generarTabla("div-listado", urlListado);
            generarTabla("div-datosPrograma", urldatosPrograma);
            
            $('#datosPrograma_length').hide();
            $('#datosPrograma_filter').hide();
            $('#datosPrograma_info').hide();
            $('#paginateFooter-div-datosPrograma').hide();
        }
        
        function showInput(id) {
            $('#becas_' + id).show();
            $('#becas_' + id).addClass('form-control');
            $('#becas_' + id).focus();
            $('#div_' + id).hide();
            $('#ver_' + id).hide();
            $('li #ver_' + id).hide();      //POR SI EL BOTON SE MUESTRA EN EL DETALLE DE LAS COLUMNAS NO VISIBLES
            $('#guardar_' + id).show();
            $('li #guardar_' + id).show();      //POR SI EL BOTON SE MUESTRA EN EL DETALLE DE LAS COLUMNAS NO VISIBLES
        }
        
        function hideInput(id, numbecas) {
            var disponibles = numbecas - $('#ejercidas_' + id).html();
            $('#becas_' + id).hide();
            $('#becas_' + id).addClass('form-control');
            $('#becas_' + id).focus();
            $('#div_' + id).html((numbecas));
            $('#disponibles_' + id).html((disponibles));
            $('#div_' + id).show();
            $('#ver_' + id).show();
            $('li #ver_' + id).show();      //POR SI EL BOTON SE MUESTRA EN EL DETALLE DE LAS COLUMNAS NO VISIBLES
            $('#guardar_' + id).hide();
            $('li #guardar_' + id).hide();      //POR SI EL BOTON SE MUESTRA EN EL DETALLE DE LAS COLUMNAS NO VISIBLES
        }
        
        
        
        function save(id) {
            var presupuestoTipoBecaPeriodoId = $('#presupuestoTipoBecaPeriodoId').val();
            var becas = $('#becas_' + id).val();
            var unidadAcademicaId = $('#unidad_' + id).val();
            guardaDatos(unidadAcademicaId, presupuestoTipoBecaPeriodoId, becas,id);
        }

        function guardaDatos(unidadAcademicaId, presupuestoTipoBecaPeriodoId, becas, id)  {
            $.ajax({
                type: 'POST',
                url: '/ajax/guardarUnidadAcademicaPresupuestoAjax.action',
                beforeSend: function () {
                    ModalGenerator.notificacion({
                        "titulo": "Guardando",
                        "cuerpo": "Se está guardando su información. Por favor, espere.",
                        "tipo": "INFO",
                        "sePuedeCerrar": false
                    });                           
                },
                dataType: 'json',
                data: {unidadAcademicaId: unidadAcademicaId,
                    presupuestoTipoBecaPeriodoId: presupuestoTipoBecaPeriodoId,
                    becas: becas},
                cache: false,
                success: function (aData) {
                    ModalGenerator.cerrarModales();
                    if (aData.data[0] !== "OK") {
                        ModalGenerator.notificacion({
                            "titulo": "Atención",
                            "cuerpo": aData.data[0],
                            "tipo": "WARNING",                            
                        });                                                 
                    } else {
                        //reload();
                        hideInput(id, becas);
                    }

                },
                error: function (aData) {
                    ModalGenerator.cerrarModales();
                    var jsonobject = JSON.stringify(aData.responseText);
                    ModalGenerator.notificacion({
                        "titulo": "Error",
                        "cuerpo": eval(jsonobject.replace('[', '').replace(']', '')),
                        "tipo": "ALERT",                            
                    });                                        
                }
            });
            return false;
        }
    </script>
</content>