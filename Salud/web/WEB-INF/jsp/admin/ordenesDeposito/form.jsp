<%-- 
    Document   : form
    Created on : 14/01/2016, 11:22:14 AM
    Author     : Tania G. Sánchez
    Redesing   : Mario Márquez
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css" />
    <title>Generación de órdenes de depósito</title>
</head> 

<content tag="tituloJSP">
    Generación de órdenes de depósito
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
        <div class="col-xs-12">
            <div class="main-box" >
                <div class="main-box-body clearfix">
                    <form id="ordenes" class="form-horizontal"
                          action="/admin/generarOrdenesDeposito.action" method="post">                    
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Periodo</label>
                            <div class="col-sm-10">
                                <s:select 
                                    id="periodo"
                                    name="periodo"
                                    cssClass="form-control"
                                    list="ambiente.periodoList"
                                    headerKey=""                                        
                                    listKey="id"
                                    listValue="clave"
                                    headerValue="-- Selecciona un periodo --"
                                    data-bv-notempty="true"
                                    data-bv-notempty-message="El periodo es requerido." />
                            </div>
                        </div>
                        <!--El mes depende del periodo selccionado-->
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Mes</label>
                            <div class="col-sm-10">
                                <s:select
                                    id="mes"
                                    cssClass="form-control"
                                    list="%{{}}"
                                    name="mes"
                                    headerKey=""
                                    headerValue="-- Selecciona primero un periodo --"
                                    data-bv-notempty="true"
                                    data-bv-notempty-message="El mes es requerido"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Programa de beca</label>
                            <div class="col-sm-10">
                                <s:select 
                                    id="programaBeca"
                                    name="programaBeca"
                                    cssClass="form-control"
                                    list="ambiente.becaList"
                                    listKey="id" listValue="nombre"
                                    headerKey=""
                                    headerValue="--Selecciona un programa de beca--"
                                    data-bv-notempty="true"                                        
                                    data-bv-notempty-message="El nombre de la beca es requerido." />
                            </div>
                        </div>
                        <!--El origen de los recursos depende del programa de beca-->
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Origen de los recursos</label>
                            <div class="col-sm-10">
                                <s:select 
                                    id="origenRecursos"
                                    name="origenRecursos"
                                    cssClass="form-control"                                        
                                    list="%{{}}"  
                                    headerKey=""
                                    headerValue="-- Selecciona un origen de los recursos --"
                                    data-bv-notempty="true"
                                    data-bv-notempty-message="El origen de los recursos es requerido." />
                            </div>
                        </div>

                        <div id="drecursos" class="ocultar form-group">
                            <label class="col-sm-2 control-label">Determinación de recursos</label>
                            <div class="col-sm-10">
                                <s:select id="determinacionRecursos" name="determinacionRecursos" cssClass="form-control"
                                          list="service.determinacionRecursos"/>
                            </div>
                        </div>
                        <div id="csubes" class="ocultar form-group">
                            <label class="col-sm-2 control-label">Convocatoria</label>
                            <div class="col-sm-10">
                                <s:select id="convocatoria" name="convocatoria" cssClass="form-control"
                                          list="ambiente.convocatoriasSubesList"
                                          listKey="id"
                                          listValue="descripcion"
                                          />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Nivel académico</label>
                            <div class="col-sm-10">
                                <s:select 
                                    id="nivelAcademico"
                                    name="nivelAcademico"
                                    cssClass="form-control"
                                    list="ambiente.nivelActivoList"
                                    listKey="id"
                                    listValue="nombre"
                                    headerKey=""
                                    headerValue="-- Selecciona un nivel académico --"
                                    data-bv-notempty="true"
                                    data-bv-notempty-message="El nivel académico es requerido." />
                            </div>
                        </div>
                        <!--La unidad Académica depende del nivel académico y del origen de los recursos selccionado-->
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Unidad Académica</label>
                            <div class="col-sm-10">
                                <s:select
                                    id="unidadAcademica"
                                    cssClass="form-control"
                                    list="%{{}}"
                                    headerKey=""
                                    headerValue="-- Selecciona primero un nivel académico --"
                                    data-bv-notempty="true"
                                    data-bv-notempty-message="La unidad académica es requerida."
                                    name="unidadAcademica" />
                            </div>
                        </div>
                        <!--El tipo de proceso depende del periodo selccionado-->
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Tipo de proceso </label>
                            <div class="col-sm-10">
                                <s:select
                                    id="tipoProceso"
                                    cssClass="form-control"
                                    list="%{{}}"
                                    headerKey=""
                                    headerValue="-- Selecciona un proceso --"
                                    name="tipoProceso"/>
                                <font color="blue">
                                    Sólo se pueden generar órdenes de depósito para procesos cerrados y validados por el analista.
                                </font>
                                <br>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Identificador de otorgamiento</label>
                            <div class="col-sm-10">
                                <s:select id="idOtorgamiento" name="idOtorgamiento" cssClass="form-control" 
                                          list="ambiente.identificadorOtorgamientoList" headerKey="0"
                                          listKey="id" listValue="nombre"
                                          headerValue="Todos"/>
                                <span class="help-block" id="nombreMessage" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Tipo de deposito </label>
                            <div class="col-sm-10">
                                <s:select
                                    id="tipoDeposito"
                                    name="tipoDeposito"
                                    list="service.tipoDeposito"                                 
                                    headerKey=""
                                    headerValue="-- Selecciona un tipo de deposito --"
                                    cssClass="form-control"
                                    data-bv-notempty="true" />
                            </div>
                        </div>
                        <div class="form-group" style="display: block" id="pago">
                            <label class="col-sm-2 control-label">Forma de pago</label>
                            <div class="col-sm-10">
                                <s:select
                                    id="formaPagoLong"
                                    name="formaPagoLong"
                                    list="service.formaPago"
                                    headerValue="-- Selecciona una forma de pago --"
                                    headerKey=""
                                    cssClass="form-control"
                                    data-bv-notempty="true"
                                    />
                            </div>
                        </div>
                        <div class="form-group" style="display: none" id="pagoShort">
                            <label class="col-sm-2 control-label">Forma de pago</label>
                            <div class="col-sm-10">
                                <s:select
                                    id="formaPagoShort"
                                    name="formaPagoShort"
                                    list="service.formaPagoShort"
                                    headerValue="-- Selecciona una forma de pago --"
                                    headerKey=""
                                    cssClass="form-control"
                                    data-bv-notempty="true" />
                            </div>
                        </div>
                        <s:hidden id="formaPago" name="formaPago" />
                        <div class="form-group">
                            <div class="col-sm-12">
                                <button type="button" id="buscar" class="btn btn-primary pull-right ">Buscar orden de depósito</button>    
                            </div>
                        </div>  
                    </form>     
                </div>
            </div>
        </div>
    </div> <!-- Termina primer cuadro blanco -->  


    <div class="row" id="div-tabla-nuevo" style="display: none;">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <div class="col-md-12" style="padding: 0px">
                    <div class="responsive">                        
                        <table id="tablaId" class="table table-striped table-hover dt-responsive" style="width: 100%">
                            <thead>
                                <tr>
                                    <th data-orderable="true" data-default-order="true" data-default-dir="desc">Unidad Académica</th>
                                    <th data-orderable="true">Tipo de beca</th>
                                    <th data-orderable="true">Becarios</th>
                                    <th data-orderable="true">Importe</th>
                                </tr>
                            </thead>
                        </table>                        
                    </div>
                </div>
            </div>
        </div>                
    </div>

<content tag="endScripts">        
    <script type="text/javascript" src="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.js"></script>    
    <script type="text/javascript" src="/resources/js/generador-tablas.js"></script>   
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>
        $(document).ready(function () {
            $(".ocultar").hide();

            $('#ordenes').bootstrapValidator();
//            $('#ordenes').bootstrapValidator().on('success.form.bv', function (e) {
//                console.log("aquiii");
////                e.preventDefault();
//            });

            $('#formaPagoShort').click(function(){
                if(this.value===""){return;}
                $('input[name="formaPago"]:hidden').val(this.value);
            });
            $('#formaPagoLong').click(function(){
                if(this.value===""){return;}
                $('input[name="formaPago"]:hidden').val(this.value);
            });
            
            $("#buscar").click(function () {
                $("#ordenes").data('bootstrapValidator').validate();
                if ($("#ordenes").data('bootstrapValidator').isValid()) {
                    cargaTablaNuevo();
//                    // El orgen de los recursos para la orden sea 'Deposita Fundación'
//                    // y el programa de beca sea 'Telmex'
//                    var telmexFund = ($("#programaBeca option:selected").val()) === '3' && ($("#origenRecursos option:selected").val()) === '0';
//                    // Solución temporal para la validación de un campo dinámico
//                    // en este caso #programaBeca
//                    var validaProgramaBeca = ($('#programaBeca').val()) !== "--Selecciona un programa de beca--";
//                    if (validaProgramaBeca) {
//                        if (!telmexFund) {
//                            cargaTablaNuevo();
//                        } else {
//                            ModalGenerator.notificacion({
//                                "titulo": "Información",
//                                "cuerpo": "Los depositos de Telmex que realiza \
//                                          la fundación no se administran en el sistema.",
//                                "tipo": "WARNING"
//                            });
//                        }
//                    }
                }
            });

            $("#periodo").change(function () {
                ocultarTabla();
                getMes();
                getTipoProceso();
            });
            
            $("#tipoDeposito").change(function () {
                ocultarTabla();
                if ($('#tipoDeposito').val() === "2")
                {
                    document.getElementById("pago").style.display = "none";
                    document.getElementById("pagoShort").style.display = "block";
                } else
                {
                    document.getElementById("pago").style.display = "block";
                    document.getElementById("pagoShort").style.display = "none";
                }
            });

            $("#programaBeca").change(function () {
                ocultarTabla();
                getOrigenRecursos();
                if ($('#programaBeca').val() === "5" || $('#programaBeca').val() === "7") { //Si es manutención, se muestran las opciones de determinacion de recursos
                    $(".ocultar").show();
                } else {
                    $(".ocultar").hide();
                }
            });
            
            $('#nivelAcademico').change(function () {
                ocultarTabla();
                getUnidadAcademica();
            });

            $("#mes").change(function(){
                ocultarTabla();
            });
            
            $("#origenRecursos").change(function(){
                ocultarTabla();
            });
            
            $("#determinacionRecursos").change(function(){
                ocultarTabla();
            });
            
            $("#convocatoria").change(function(){
                ocultarTabla();
            });
            
            $("#unidadAcademica").change(function(){
                ocultarTabla();
            });
            
            $("#tipoProceso").change(function(){
                ocultarTabla();
            });
            
            $("#idOtorgamiento").change(function(){
                ocultarTabla();
            });
            
            $("#formaPagoLong").change(function(){
                ocultarTabla();
            });
            
            $("#formaPagoShort").change(function(){
                ocultarTabla();
            });
            
            $("#formaPago").change(function(){
                ocultarTabla();
            });

        });

        var botonGenerar = [{
                text: 'Generar orden',
                id: 'generaOD',
                className: 'normal-button solo-lectura',
                action: function (e, dt, node, config) {
                    if ($('#grupoBotones-div-tabla-nuevo div a').hasClass('deshabilitar'))
                        return;
                    document.getElementById("ordenes").submit();
                    $('#grupoBotones-div-tabla-nuevo div a').addClass('deshabilitar');
                    $('#grupoBotones-div-tabla-nuevo div a').css('cursor', 'not-allowed');
                    $('#grupoBotones-div-tabla-nuevo div a').css('background-color', '#ccc');
                }
            }];

        function cargaTablaNuevo() {
            var url = '/ajax/listadoOrdenesDepositoAjax.action';
            var ordenDeposito = $('#ordenDeposito').val();
            var periodo = $('#periodo').val();
            var mes = $('#mes').val();
            var origenRecursos = $('#origenRecursos').val();
            var programaBeca = $('#programaBeca').val();
            var nivelAcademico = $('#nivelAcademico').val();
            var unidadAcademica = $('#unidadAcademica').val();
            var tipoProceso = $('#tipoProceso').val();
            var tipoDeposito = $('#tipoDeposito').val();
            var formaPago = $('#formaPago').val();
            var idOtorgamiento = $('#idOtorgamiento').val();
            var determinacionRecursos = $('#determinacionRecursos').val();
            var convocatoria = $('#convocatoria').val();

            url += "?ordenDeposito=0" + "&periodo=" + periodo + "&mes=" + mes + "&origenRecursos=" + Number(origenRecursos)
                    + "&programaBeca=" + programaBeca + "&nivelAcademico=" + nivelAcademico + "&unidadAcademica=" + unidadAcademica
                    + "&tipoProceso=" + tipoProceso + "&tipoDeposito=" + tipoDeposito + "&formaPago=" + formaPago + "&idOtorgamiento=" + idOtorgamiento
                    + "&determinacionRecursos=" + Number(determinacionRecursos) + "&convocatoria=" + convocatoria;
            generarTabla("div-tabla-nuevo", url, null, true, botonGenerar);
            //Intervalo de tiempo para verificar el valor del monto
            //en caso de ser 0 oculta el botón
            var inter = setInterval(function(){
                var monto = document.getElementById("monto");
                if(monto != null){
                    if(monto.textContent !== "nulo"){
                        if(monto.textContent === "$0"){
                            $(".normal-button").hide();
                            //document.getElementsByClassName("normal-button").disabled: true,
                        }else{
                            $(".normal-button").show();
                        }
                        $(".dataTables_length").hide();
                        $(".table-footer").hide();
                        clearInterval(inter);
                    }
                }
            }, 500);
        }

        //Función que busca los meses con base en el periodo
        function getMes() {
            $.ajax({
                type: 'POST',
                url: '/ajax/getMesMesPeriodo.action',
                dataType: 'json',
                data: {pkPeriodo: $('#periodo').val()},
                cache: false,
                success: function (aData) {
                    $('#mes').get(0).options.length = 1; //Borra todos los elementos
                    $.each(aData.data, function (i, item) {
                        $('#mes').get(0).options[$('#mes').get(0).options.length] = new Option(item[1], item[0]);
                    });
                },
                error: function () {
                    ModalGenerator.notificacion({
                        "titulo": "Atención",
                        "cuerpo": "Hubo un problema que impidió que se completara la operación.",
                        "tipo": "DANGER"
                    });
                }
            });
            return false;
        }


        //Función que busca los Programas de Beca con base en el origen de los recursos.
        function getOrigenRecursos() {
            $.ajax({
                type: 'POST',
                url: '/ajax/getRecursosOrigenRecursos.action',
                dataType: 'json',
                data: {pkProgramaBeca: $('#programaBeca').val()},
                cache: false,
                success: function (aData) {
                    $('#origenRecursos').get(0).options.length = 0;
                    $('#origenRecursos').get(0).options[0] = new Option("--Selecciona un origen de los recursos--");
                    $.each(aData.data, function (i, item) {
                        $('#origenRecursos').get(0).options[$('#origenRecursos').get(0).options.length] = new Option(item[1], item[0]);
                    });
                },
                error: function () {
                    BootstrapDialog.alert({
                        title: 'Atención',
                        message: 'Hubo un problema que impidió que se completara la operación.',
                        type: BootstrapDialog.TYPE_DANGER
                    });
                }
            });
            return false;
        }

        //Función que busca las Unidades Académicas con base en el Nivel Académico y el origen de los recursos.
        function getUnidadAcademica() {
            $.ajax({
                type: 'POST',
                url: '/ajax/getNivelUnidadAcademica.action',
                dataType: 'json',
                data: {pkOrigenRecursos: Number($('#origenRecursos').val()),
                    pkProgramaBeca: Number($('#programaBeca').val()),
                    pkNivelAcademico: Number($('#nivelAcademico').val())},
                cache: false,
                success: function (aData) {
                    $('#unidadAcademica').get(0).options.length = 1;
                    $('#unidadAcademica').get(0).options[1] = new Option("Todas", "0");
                    $.each(aData.data, function (i, item) {
                        $('#unidadAcademica').get(0).options[$('#unidadAcademica').get(0).options.length] = new Option(item[1], item[0]);
                    });
                },
                error: function () {
                    BootstrapDialog.alert({
                        title: 'Atención',
                        message: 'Hubo un problema que impidió que se completara la operación.',
                        type: BootstrapDialog.TYPE_DANGER
                    });
                }
            });
            return false;
        }

        function getTipoProceso() {
            $.ajax({
                type: 'POST',
                url: '/ajax/getPeriodoTipoProceso.action',
                dataType: 'json',
                data: {pkPeriodo: $('#periodo').val()},
                cache: false,
                success: function (aData) {
                    $('#tipoProceso').get(0).options.length = 0; //Borra todos los elementos
                    $('#tipoProceso').get(0).options[0] = new Option("Todos", "0");
                    $.each(aData.data, function (i, item) {
                        $('#tipoProceso').get(0).options[$('#tipoProceso').get(0).options.length] = new Option(item[1], item[0]);
                    });
                },
                error: function () {
                    BootstrapDialog.alert({
                        title: 'Atención',
                        message: 'Hubo un problema que impidió que se completara la operación.',
                        type: BootstrapDialog.TYPE_DANGER
                    });
                }
            });
            return false;
        }
        
        function ocultarTabla(){
            $("#dataTableHeader-div-tabla-nuevo").hide();
            $("#div-tabla-nuevo").hide();
            $(".table-footer").hide();
            if(document.getElementById("monto") != null){
                document.getElementById("monto").innerHTML = "nulo";
            }
        }
    </script>
</content>