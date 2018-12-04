<%-- 
    Document   : form
    Created on : 15/08/2017, 10:57:22 AM
    Author     : Othoniel Herrera
--%>

<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Preasignación masiva</title>
    <link rel="stylesheet" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css" type="text/css">
</head>

<content tag="Preasignacion Masiva">
    Preasignación masiva
</content>

<body>
    <div class="row" id="observaciones">
        <div class="col-lg-12">
            <div class="main-box clearfix">
                <div class="main-box-body">
                    <h4>Consideraciones</h4>
                    <ul class="fa-ul">
                        <li><i class="fa-li fa fa-check-square"></i>
                            Hue hue hue
                        </li>
                        <li><i class="fa-li fa fa-check-square"></i>
                            Waka waka
                        </li>
                        <li><i class="fa-li fa fa-check-square"></i>
                            La wea fome
                        </li>
                        <li><i class="fa-li fa fa-check-square"></i>
                            Brbrbr
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-6">
            <div class="main-box">
                <header class="main-box-header clearfix">
                    <h4>Solicitudes a considerar</h4>
                </header>
                <div class="main-box-body clearfix">
                    <ul class="list-group">
                        <li class="list-group-item">
                            <span class="badge badge-success">
                                <div id="actual">
                                    <s:property value="totalAlumnosSolicitudPendienteActivo"/>
                                </div>
                            </span>
                            Solicitudes del periodo actual
                        </li>
                        <li class="list-group-item">
                            <span class="badge badge-success">
                                <div id="esperaActivo">
                                    <s:property value="totalAlumnosSolicitudEsperaActivo" />
                                </div>
                            </span>
                            Solicitudes en lista de espera del periodo actual
                        </li>
                        <s:if test="totalAlumnosSolicitudEsperaPasado != null">
                            <li class="list-group-item">
                                <span class="badge badge-success">
                                    <div id="esperaPasado">
                                        <s:property value="totalAlumnosSolicitudEsperaPasado" />
                                    </div>
                                </span>
                                Solicitudes en lista de espera del periodo pasado
                            </li>
                        </s:if>
                        <li class="list-group-item">
                            <span class="badge badge-success">
                                <div id="total">
                                    <s:property  value="totalAlumnosSolicitud" />
                                </div>
                            </span>
                            Total de solicitudes
                        </li>
                    </ul>			
                </div>
            </div>
        </div>
        <div class="col-lg-6">
            <div class="main-box">
                <header class="main-box-header clearfix">
                    <h4>Filtros</h4>
                </header>
                <div class="main-box-body clearfix">
                    <form role="form" id="preasignacionMasiva" action="/admin/generarPreasignacionMasiva.action" method="post">
                        <div class="form-group">
                            <label class="control-label">Nivel académico </label>
                            <s:select id="nivel" 
                                      name="nivel" 
                                      data-bv-notempty="true"
                                      data-bv-notempty-message="Debes seleccionar un nivel"
                                      list="ambiente.nivelActivoList" 
                                      listKey="id" 
                                      listValue="nombre" 
                                      headerValue="--Todos los niveles--" 
                                      headerKey="0"
                                      onChange = "getUnidades()"
                                      cssClass="form-control"/>
                        </div>       
                        <div class="form-group">
                            <label class="control-label">Unidad académica</label>
                            <s:select id="unidadAcademica" 
                                      name="unidadAcademica" 
                                      data-bv-notempty="true"
                                      data-bv-notempty-message="Debes seleccionar un movimiento"
                                      list="ambiente.unidadAcademicaList" 
                                      listKey="id" 
                                      listValue="nombreCorto"
                                      headerValue="--Todas las unidades académicas--"
                                      headerKey="0" 
                                      onChange="actualizarCifras()"
                                      cssClass="form-control"/>
                        </div>
                        <div class="form-group">
                            <label class="control-label">Sobreescribir preasignaciones</label>
                            <div class="checkbox-nice">
                                <input class="chck" type="checkbox" checked="" id="sobreEscribir" name="sobreEscribir" value="true" onchange="actualizarCifras()">
                                <label for="sobreEscribir"></label>
                            </div>
                        </div> 
                        <div class="form-group">
                            <input id="generar" type="submit" class="btn btn-primary pull-right" value="Generar preasignación" form="preasignacionMasiva"/>
                        </div>
                    </form>
                </div><br>
            </div>
        </div>
    </div>

    <div class="row" id="preCarga">
        <div class="main-box feed">
            <div class="main-box-body clearfix"><h4> Estamos preparando la información del presupuesto, en breve comenzará el proceso.</h4>
                <div class="progress progress-striped active  progress-4x">
                    <div class="progress-bar" role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width: 100%">
                        <span class="">Cargando información ...</span>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="main-box feed" id="project_feed">
            <div class="main-box-body clearfix">            
                <ul id ="divTotalGlobal" style="margin-left:-50px;">
                    <li class="clearfix" style="border-bottom-width: 1px solid red;">
                        <div class="title">
                            <div  id="titleGlobal"><h2>Progreso de la preasignación masiva</h2></div>
                        </div>
                        <div class="post-time">
                            <div style="font-size:15px">
                                <p id ="sProcessedGlobal"></p>
                                <p id ="sProcessedBars"></p>
                                <p id ="sOtorgadosSuccess"></p>
                                <p id ="sOtorgadosError"></p>
                                <div id="pbarClassGlobal" class="progress progress-striped active progress-4x">
                                    <div id="pbarGlobal" class="progress-bar progress-bar-info" role="progressbar" aria-valuenow="45" aria-valuemin="0" aria-valuemax="100" >
                                        <span id ="sPercentGlobal"></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="time-ago">
                            <i class="fa fa-clock-o"></i><span id ="sEtaGlobal"></span>.
                        </div>
                    </li>
                </ul>  
                <ul id="divGlobal" style="display: block;border-top:1px solid #EBEBEB;">
                    <li id="divPbarContainer" class="clearfix">
                        <div class="img">
                            <img src="/resources/img/automaticas/logo_preasignaciones.png" alt="">
                        </div>
                        <div class="title">
                            <span id ="sInfo"></span>
                        </div>
                        <div class="post-time">
                            <span id ="sProcessed"></span> 
                        </div>
                        <div class="time-ago">
                            <i class="fa fa-clock-o"></i> <span id ="sEta"></span>.
                        </div>
                        <div id="pbarClass" class="progress progress-striped active progress-4x">
                            <div id="pbar" class="progress-bar" role="progressbar" aria-valuenow="45" aria-valuemin="0" aria-valuemax="100" >
                                <span id ="sPercent"></span>
                            </div>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</body>

<content tag="endScripts">
    <script type="text/javascript" src="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.js"></script>
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>
        var lastActiveBar = 0;
        var executePbar = false;
        var nivel;
        var unidadAcademica;

        $("#divTotalGlobal").hide();
        $("#divGlobal").hide();
        $("#project_feed").hide();
        $("#preCarga").hide();

        $("#preasignacionMasiva").bootstrapValidator().on('success.form.bv', function (e) {
            e.preventDefault();
            ModalGenerator.notificacion({
                "titulo": "¿Estás seguro de continuar?",
                "cuerpo": "Estás apunto de comenzar el proceso de preasignación masiva",
                "tipo": "WARNING",
                "tache": false,
                "funcionAceptar": function () {

                    nivel = $("#nivel option:selected").val();
                    unidadAcademica = $("#unidadAcademica option:selected").val();
                    executePbar = true;

                    $("#preasignacionMasiva").unbind('submit').submit();

                    $("#divTotalGlobal").show();
                    $("#divGlobal").show();

//                    $("#project_feed").show();
                    $("#div_oculto").hide();
                    $("#observaciones").hide();
                    $('.alert-success').hide();
                    $("#preCarga").show();

                    setInterval(function () {
                        if (executePbar){
                            updateProgress();
                        }
                    }, 100);


                },
                "funcionCancelar": function () {
                    $("#preasignacionMasiva").data('bootstrapValidator').resetForm();
                }
            });
        });


        function actualizarCifras() {
            $("#generar").attr("disabled", "true");
            return $.ajax({
                type: 'POST',
                url: '/ajax/getCifrasPreasignacionAjax.action',
                dataType: 'json',
                data: {nivelId: Number($('#nivel').val()),
                    unidadAcademicaId: Number($('#unidadAcademica').val()),
                    sobreEscribir: Boolean($('#sobreEscribir').is(':checked'))},
                cache: false,
                success: function (aData) {
                    $("#generar").removeAttr("disabled");
                    if($("#esperaPasado").length > 0){
                        $("#actual").html(aData.data[0]);
                        $("#esperaActivo").html(aData.data[1]);
                        $("#esperaPasado").html(aData.data[2]);                    
                        $("#total").html(aData.data[3]);                                                
                    } else {
                        $("#actual").html(aData.data[0]);
                        $("#esperaActivo").html(aData.data[1]);               
                        $("#total").html(aData.data[2]);   
                    }
                },
                error: function () {
                    BootstrapDialog.alert({
                        title: 'Atención',
                        message: 'Hubo un problema que impidió que se completara la operación.',
                        type: BootstrapDialog.TYPE_DANGER
                    });
                }
            });
        }

        function getUnidades() {
            $("#generar").attr("disabled", "true");
            $("#unidadAcademica").attr("disabled", "true");
            $.ajax({
                type: 'POST',
                url: '/ajax/getUnidadAcademicaAjax.action',
                dataType: 'json',
                data: {pkNivel: Number($('#nivel').val())},
                cache: false,
                success: function (aData) {
                    $('#unidadAcademica').get(0).options.length = 0;
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
            }).done(function () {
                actualizarCifras().done(function () {
                    $("#generar").removeAttr("disabled");
                    $("#unidadAcademica").removeAttr("disabled");
                });
            });
            return false;
        }

        function updateProgress() {
            var niv = $("#nivel option:selected").val();
            var ua = $("#unidadAcademica option:selected").val();
            $.ajax({
                type: 'POST',
                url: '/ajax/sincronizaPreasignacionAjax.action?sinc_nivel=' + niv + '&sinc_ua=' + ua,
                dataType: 'json',
                success: function (aData) {
                    updateDivsRE(aData);
                }
            });
        }

        function updateDivsRE(aData) {
            if (aData.data[0] === undefined) {
                return null;
            }
            var elements = aData.data[0].toString().split(",");
            var progressbar_ = "#pbar";
            var percentId = "#sPercent";
            var etaId = "#sEta";
            var processedId = "#sProcessed";
            var escuelaId = "#sInfo";
            var activeBar = elements[11];

            if (activeBar > 0) {
                if (activeBar !== lastActiveBar) {
                    var tempPbar = "#pbar";
                    var tempEta = "#sEta";
                    var tempProcessed = "#sProcessed";
                    var tempPercent = "#sPercent";
                    if (lastActiveBar > 0) {
                        tempPbar += lastActiveBar;
                        tempEta += lastActiveBar;
                        tempProcessed += lastActiveBar;
                        tempPercent += lastActiveBar;
                    }

                    $(tempEta).text("Completado");
                    $(tempPbar).removeClass("progress-bar").addClass("progress-bar progress-bar-success");
                    cloneDiv(activeBar);
                    tempPbar = "#pbar" + activeBar;
                    $(tempPbar).removeClass("progress-bar progress-bar-success").addClass("progress-bar");
                    lastActiveBar = activeBar;
                }
                percentId += activeBar;
                etaId += activeBar;
                progressbar_ += activeBar;
                processedId += activeBar;
                escuelaId += activeBar;
            }

            $(percentId).text(elements[0] + '%');
            $(etaId).text(" Tiempo estimado de espera: " + msToTime(elements[1]));
            $(processedId).text("Se han procesado " + elements[2] + " solicitudes de un total de  " + elements[3]);
            $(progressbar_).attr('style', 'width: ' + elements[0] + '%');
            $(escuelaId).text(elements[10]);

            if (elements[6] !== "0") {
                $("#preCarga").hide();
                $("#project_feed").show();
            }

            $("#pbarGlobal").attr('style', 'width: ' + elements[4] + '%');
            $("#sPercentGlobal").text(elements[4] + '%');
            $("#sEtaGlobal").text("Tiempo estimado de espera: " + msToTime(elements[5]));
            $("#sProcessedGlobal").text(elements[6] + " solicitudes procesadas de un total de  " + elements[7]);
            $("#sOtorgadosSuccess").text("Solicitudes preasignadas: " + elements[8]);
            $("#sOtorgadosError").text("Alumnos no asignados: " + elements[9]);
            $("#sProcessedBars").text("Se han procesado " + activeBar + " unidades academicas, de un total de " + elements[12]);
        }
        
        function cloneDiv(newId) {
            var clone = $("#divPbarContainer").clone(false);
            $("*", clone).add(clone).each(function () {
                if (this.id) {
                    this.id = this.id + newId;
                }
                if (this.name) {
                    this.name = this.name + newId;
                }
            });
            $("#pbar" + newId).attr('style', 'width: 0%');
            $("#divGlobal").append(clone);
        }

        function msToTime(s) {
            var ms = s % 1000;
            s = (s - ms) / 1000;
            var secs = s % 60;
            s = (s - secs) / 60;
            var mins = s % 60;
            var hrs = (s - mins) / 60;

            if (mins === 0)
                mins = '';
            else {
                if (mins < 10)
                    mins = '0' + mins + ' minutos ';
                else
                    mins = mins + ' minutos ';
            }
            if (secs < 10)
                secs = '0' + secs;
            if (hrs === 0)
                hrs = '';
            else {
                if (hrs < 10)
                    hrs = '0' + hrs + ' horas ';
                else
                    hrs = hrs + ' horas ';
            }
            return hrs + mins + secs + ' segundos';
        }
    </script>
</content>