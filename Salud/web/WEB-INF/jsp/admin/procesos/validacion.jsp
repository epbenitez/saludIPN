<%-- 
    Document   : Validación de procesos
    Created on : 27/10/2015, 02:09:23 PM
    Author     : Victor Lozano
    Rediseño   : Gustavo A. Alamillo
    Modified   : 11-Oct-2015
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Validación de procesos</title>
    <link rel="stylesheet" type="text/css" href="/vendors/fancy-box/jquery.fancybox.min.css"/>
    <link rel="stylesheet" type="text/css" href="/vendors/select2/css/select2.min.css"/>
    <style>        
        /* Inicia: Centrar checkbox en tabla */
        .checkbox-nice input[type=checkbox] {
            visibility: visible;
            position: absolute;
            left: -9999px;
        }           
        .checkbox-nice label:before {
            left: auto;
        }
        .checkbox-nice label:after {
            left: auto;
            margin-left: 5px;
        }    
        #cuadro {
            font-size: 12px!important;
        }
        td, th { 
            font-size: 12px!important;
        }

        .chart {
            display: inline;
            width: 100%;
        }
        /* Termina: Centrar checkbox en tabla */                
    </style>
</head> 

<content tag="tituloJSP">
    Validación de procesos
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

    <div class="row">
        <div class="main-box clearfix">
            <div class="main-box-body">
                <div class="col-xs-12 col-lg-6" id="formu">
                    <form id="busquedaForm" class="form-inline" action="/ajax/graficaValidacionProcesoAjax.action" method="post">

                        <div class="form-group col-xs-12 col-lg-12">
                            <label for="periodoEscolar" class="control-label">Periodo escolar </label>
                            <s:select id="periodoEscolar" name="periodoId"
                                      cssClass="form-control" cssStyle="width:100%"
                                      list="ambiente.periodoList" 
                                      listKey="id" listValue="clave"/>
                        </div>

                        <div class="form-group col-xs-12 col-lg-12">
                            <s:if test="esAnalista">   
                                <label for="unidadAcademica" class="control-label">Unidad Académica</label>
                                <s:select cssClass="form-control" cssStyle="width:100%"
                                          id="unidadAcademica" name="uaId"
                                          list="ambiente.unidadAcademicaList" 
                                          listKey="id" listValue="nombreSemiLargo" 
                                          headerKey="" headerValue="-- Todas --"/>
                            </s:if>
                        </div> 

                        <div class="form-group col-xs-12 col-lg-12">
                            <label for="proceso" class="control-label">Movimiento</label>                         
                            <s:select id="proceso" name="movimientoId"
                                      cssClass="form-control" cssStyle="width:100%"
                                      list="ambiente.movimientoList" 
                                      listKey="id" listValue="nombre" 
                                      headerKey="" headerValue="-- Todos --"/>                            
                        </div>

                        <div class="form-group col-xs-12 col-lg-12">
                            <label for="estatus" class="control-label">Estatus</label>
                            <s:select id="estatus" name="estatusId"
                                      cssClass="form-control" cssStyle="width:100%"
                                      list="ambiente.procesoEstatusList" 
                                      listKey="id" listValue="nombre" 
                                      headerKey="" headerValue="-- Todos --"/>
                        </div>
                        <div class="form-group col-xs-12 col-lg-12">
                            <div class="pull-right">
                                <br>
                                <input type="submit" id="buscar" class="btn btn-primary" value="Aplicar filtros"/>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="col-xs-12 col-lg-6" id="graph"><br></div>
            </div>
        </div>

    </div>

    <div class="row" style="display: none;" id="tabla">
        <div class="col-xs-12">
            <div class="main-box clearfix">   
                <div class="col-md-12" style="padding: 0px;">
                    <div class="responsive">
                        <table id="listado" class="table table-striped table-hover dt-responsive" style="width: 100%">
                            <thead>
                                <tr>
                                    <th data-orderable="true">Unidad Académica</th>
                                    <th data-orderable="true">Proceso</th>
                                    <th data-orderable="true">Fecha inicial</th>
                                    <th data-orderable="true">Fecha final</th>
                                    <th>Responsable</th>
                                    <th>Funcionario</th>
                                    <th>Analista</th>
                                    <th></th>
                                    <th></th>
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

    <form id="eliminarForm" action="/admin/eliminarProceso.action?e=1" method="POST" >
        <input type="hidden" name="proceso.id" id="id" value="" />
    </form>
</body>

<content tag="endScripts">
    <script type="text/javascript" src="/vendors/fancy-box/jquery.fancybox.min.js"></script>
    <script type="text/javascript" src="/resources/js/generador-tablas.js"></script>
    <script type="text/javascript" src="/resources/js/ModalGenerator.js"></script>
    <script type="text/javascript" src="/vendors/select2/js/select2.full.min.js"></script>
    <script type="text/javascript" src="/vendors/select2/js/i18n/es.js"></script>
    <script type="text/javascript" src="/resources/js/loader.js"></script>
</content>

<content tag="inlineScripts">
    <script>
        $(document).ready(function () {
            document.getElementById("graph").style.display = 'none';
            if (document.getElementById("graph").style.display === 'none')
            {
                $("#formu").removeClass("col-lg-6");
                $("#formu").addClass("col-lg-12");
            } 
            $("#busquedaForm").submit(function (event) {
                event.preventDefault();
                $("#buscar").blur();
                $("#formu").removeClass("col-lg-12");
                $("#formu").addClass("col-lg-6");
                recargarTabla();
                graficaEstatus();
            });
            $("#unidadAcademica,#proceso,#estatus,#periodoEscolar").select2({language: "es"});
        });


        function graficaEstatus() {
            document.getElementById("graph").style.display = 'block';
            var alta = 0;
            var responsable = 0;
            var funcionario = 0;
            var analista = 0;

            var id_proceso = Number($('#proceso option:selected').val());
            var id_periodo = Number($('#periodoEscolar option:selected').val());
            var id_ua = Number($('#unidadAcademica option:selected').val());
            var id_estatus = Number($('#estatus option:selected').val());

            $.ajax({
                url: '/ajax/graficaProcesoAjax.action',
                cache: false,
                dataType: 'json',
                data: {periodoId: id_periodo, estatusId: id_estatus, movimientoId: id_proceso, uaId: id_ua},
                success: function (result) {
                    alta = result.data[0];
                    responsable = result.data[1];
                    funcionario = result.data[2];
                    analista = result.data[3];

                    drawChart(alta, responsable, funcionario, analista);
                }

            });
        }

        function recargarTabla() {
            var id_proceso = Number($('#proceso option:selected').val());
            var id_periodo = Number($('#periodoEscolar option:selected').val());
            var id_ua = Number($('#unidadAcademica option:selected').val());
            var id_estatus = Number($('#estatus option:selected').val());
            var url = "/ajax/listadoValidacionProcesoAjax.action?search_id_periodo=" + id_periodo + "&search_id_proceso=" + id_proceso + "&search_id_ua=" + id_ua + "&search_id_estatus=" + id_estatus;
            generarTabla("tabla", url, despuesCargarTabla, true, botonAgregar);
        }

        function despuesCargarTabla() {
            $('.fancybox').fancybox({
                autoSize: true,
                afterClose: function () {
                    actualizarTabla();
                }
            });
            var esAnalista = <s:property value="esAnalista" />;
            var table = tablas['tabla'];
            if (!esAnalista) {
                table.column(0).visible(false);
            }
        }

        var botonAgregar = [{
                text: '<a title="Agregar proceso" href="/admin/formProceso.action" class="fancybox fancybox.iframe btn btn-info solo-lectura"  name="nuevo" value="Nuevo" ><i class="fa fa-plus-circle fa-lg"></i>&nbsp;&nbsp;Agregar proceso</a>',
                className: 'normal-button'
            }
        ];

        function guardar(id_proceso, id_estatus) {
            $.ajax({
                type: 'POST',
                url: '/ajax/validaProcesoAjax.action',
                dataType: 'json',
                data: {id_proceso: id_proceso, id_estatus: id_estatus},
                cache: false,
                success: function () {
                    actualizarTabla();
                },
                error: function () {
                    ModalGenerator.notificacion({
                        "titulo": "Atención",
                        "cuerpo": "Hubo un problema que impidió que se completara la operación.",
                        "tipo": "WARNING"
                    });
                    actualizarTabla();
                }
            });
            return false;
        }

        function actualizarTabla() {
            var table2 = $('#listado').DataTable();
            table2.draw(false);
        }

        function validar(check, estatus_proceso_id, proceso_id, element_id) {
            if (check) {
                ModalGenerator.notificacion({
                    "titulo": "Atención",
                    "cuerpo": "Con esta acción valida y da por terminado su proceso. Al dar clic en 'Aceptar' ya no podrá realizar ningún movimiento, ¿está seguro?",
                    "tipo": "ALERT",
                    "funcionAceptar": function () {
                        var aux = parseInt(estatus_proceso_id) + 1
                        guardar(proceso_id, aux);
                    },
                    "funcionCancelar": function () {
                        $("#" + element_id + "").prop('checked', false);
                    }
                });
            } else {
                ModalGenerator.notificacion({
                    "titulo": "Atención",
                    "cuerpo": "¿Esta seguro que desea quitar la validacion de este elemento?",
                    "tipo": "ALERT",
                    "funcionAceptar": function () {
                        guardar(proceso_id, estatus_proceso_id);
                    },
                    "funcionCancelar": function () {
                        $("#" + element_id + "").prop('checked', true);
                    }
                });
            }
        }

        function eliminar(id) {
            ModalGenerator.notificacion({
                "titulo": "Advertencia",
                "cuerpo": "Esto borrará el registro seleccionado, ¿Está seguro de quererlo eliminar?",
                "tipo": "ALERT",
                "funcionAceptar": function () {
                    $("#id").val(id);
                    $("#eliminarForm").submit();
                }
            });
        }

        // Load the Visualization API and the corechart package.
        google.charts.load('current', {'packages': ['corechart']});

        // Set a callback to run when the Google Visualization API is loaded.
        google.charts.setOnLoadCallback(drawChart);

        // Callback that creates and populates a data table,
        // instantiates the pie chart, passes in the data and
        // draws it.
        function drawChart(alta, res, fun, ana) {

            // Create the data table.
            var data = new google.visualization.DataTable();
            data.addColumn('string', 'Tipo');
            data.addColumn('number', 'Numero');
            data.addRows([
                ['Alta', Number(alta)],
                ['Responsable', Number(res)],
                ['Funcionario', Number(fun)],
                ['Analista', Number(ana)]
            ]);

            // Set chart options
            var options = {'title': 'Estatus de procesos por periodo',
                height: 300,
                is3D: true};

            // Instantiate and draw our chart, passing in some options.
            var chart = new google.visualization.PieChart(document.getElementById('graph'));
            chart.draw(data, options);
        }

        $(window).resize(function () {
            drawChart();
        }
        );
    </script>
</content>