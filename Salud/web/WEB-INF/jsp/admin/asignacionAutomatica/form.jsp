<%-- 
    Document   : Template SIBEC
    Created on : 28-Septiembre-2016
    Author     : Gustavo Adolfo Alamillo Medina
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Asignación automática</title>
    <link rel="stylesheet" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css" type="text/css">
    <style>
        #observaciones ul li {
            margin-top: 5px;
        }
    </style>

</head> 

<content tag="tituloJSP">
    Asignación automática
</content>

<body>

    <div class="row">
        <div class="col-lg-12">
            <s:if test="hasActionErrors()">
                <s:if test="warning==true">
                    <div class="alert alert-warning">
                        <i class="fa fa-warning fa-fw fa-lg"></i> <s:actionerror cssErrorClass="alert-warning"/>
                    </div>
                </s:if>
            </s:if>
            <s:if test="hasActionMessages()">
                <div class="alert alert-success">
                    <i class="fa fa-check-circle fa-fw fa-lg"></i>
                    <strong>&iexcl;Realizado!</strong> <s:actionmessage />
                </div>
            </s:if>
        </div>
    </div>

    <div class="row" id="observaciones">
        <div class="col-lg-12">
            <div class="main-box clearfix">
                <div class="main-box-body">
                    <h4>Consideraciones</h4>
                    <ul class="fa-ul">
                        <li><i class="fa-li fa fa-check-square"></i>
                            Para poder ejecutar la asignación automática, antes ya debió haber sido cargado el presupuesto
                            para el periodo activo.
                        </li>
                        <li><i class="fa-li fa fa-check-square"></i>
                            Esta es ejecutada únicamente para los alumnos que han solicitado beca 
                            en el periodo activo.
                        </li>
                        <li><i class="fa-li fa fa-check-square"></i>
                            Para lograr optimizar la repartición del presupuesto, 
                            es necesario que primero se ejecuten las Revalidaciones 
                            y posteriormente los Nuevos otorgamientos.
                        </li>
                        <li><i class="fa-li fa fa-check-square"></i>
                            Se toma como base las prioridades asignadas en el módulo de "Administración
                            de tipos de beca por periodo", siendo la prioridad 1 la más importante y
                            la prioridad 10 la menos importante.
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div> <!-- Observaciones -->

    <div class="row" id="div_oculto">

        <div class="col-lg-6">
            <div class="main-box">
                <header class="main-box-header clearfix">
                    <h4>Alumnos con promedio cero</h4>
                </header>
                <div class="main-box-body clearfix">
                    <ul class="list-group">
                        <li class="list-group-item">
                            <s:if test="%{alumnos0RevalidantesNMS == 0}">
                                <span class="badge badge-success"><s:property value="alumnos0RevalidantesNMS" /></span>
                            </s:if>
                            <s:else>
                                <span class="badge badge-danger"><s:property value="alumnos0RevalidantesNMS" /></span>
                            </s:else>
                            Revalidantes Nivel Medio Superior
                        </li>
                        <li class="list-group-item">
                            <s:if test="%{alumnos0NuevosNMS == 0}">
                                <span class="badge badge-success"><s:property value="alumnos0NuevosNMS" /></span>
                            </s:if>
                            <s:else>
                                <span class="badge badge-danger"><s:property value="alumnos0NuevosNMS" /></span>
                            </s:else>
                            Aspirantes nuevos otorgamientos Nivel Medio Superior
                        </li>
                        <li class="list-group-item">
                            <s:if test="%{alumnos0RevalidantesNS == 0}">
                                <span class="badge badge-success"><s:property value="alumnos0RevalidantesNS" /></span>
                            </s:if>
                            <s:else>
                                <span class="badge badge-danger"><s:property value="alumnos0RevalidantesNS" /></span>
                            </s:else>
                            Revalidantes Nivel Superior
                        </li>
                        <li class="list-group-item">
                            <s:if test="%{alumnos0NuevosNS == 0}">
                                <span class="badge badge-success"><s:property value="alumnos0NuevosNS" /></span>
                            </s:if>
                            <s:else>
                                <span class="badge badge-danger"><s:property value="alumnos0NuevosNS" /></span>
                            </s:else>
                            Aspirantes nuevos otorgamientos Nivel Superior
                        </li>
                    </ul>			
                </div>
            </div>
        </div>

        <div class="col-lg-6">
            <div class="main-box">
                <header class="main-box-header clearfix">
                    <h4>Asignaciones automáticas</h4>
                </header>
                <div class="main-box-body clearfix">
                    <form role="form" id="asignacionAutomatica" action="/admin/generaAsignacionAutomatica.action" method="post">
                        <div class="form-group">
                            <label class="control-label">Tipo de movimiento</label>
                            <s:select id="movimiento" 
                                      data-bv-notempty="true"
                                      data-bv-notempty-message="Debes seleccionar un movimiento"
                                      name="movimiento" 
                                      list="service.tipoMovimiento" 
                                      headerValue="-- Selecciona un tipo de movimiento --"
                                      headerKey="" 
                                      cssClass="form-control"/>
                        </div> 
                        <div class="form-group">
                            <label class="control-label">Nivel académico </label>
                            <s:select id="nivel" name="nivel" 
                                      data-bv-notempty="true"
                                      data-bv-notempty-message="Debes seleccionar un nivel"
                                      list="niveles" 
                                      listKey="id" 
                                      listValue="nombre" 
                                      headerValue="--Seleccione un nivel--" 
                                      headerKey=""
                                      cssClass="form-control"/>
                        </div>       
                        <div class="form-group">
                            <input id="generar" type="submit" class="btn btn-primary pull-right" value="Generar asignación" form="asignacionAutomatica"/>
                            <!--<input id="revertir" type="button" class="btn btn-danger pull-left" value="Revertir asignación" form="revertirA"/>-->
                        </div>
                    </form>
                </div><br/>
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
                            <div  id="titleGlobal"><h2></h2></div>
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
    <script src="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.js"></script>
    <script src="/resources/js/ModalGenerator.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>

        var lastActiveBar = 0;
        var executePbar = false;
        var tipoMovimiento;
        var tipoNivel;

        $("#divTotalGlobal").hide();
        $("#divGlobal").hide();
        $("#project_feed").hide();
        $("#preCarga").hide();

        $("#asignacionAutomatica").bootstrapValidator().on('success.form.bv', function (e) {
            e.preventDefault();
            ModalGenerator.notificacion({
                "titulo": "¿Estas seguro de continuar?",
                "cuerpo": "Estas apunto de comenzar las asignaciones automaticas",
                "tipo": "WARNING",
                "funcionAceptar": function () {

                    tipoMovimiento = $("#movimiento option:selected").val();
                    tipoNivel = $("#nivel option:selected").val();
                    executePbar = true;

                    $("#asignacionAutomatica").unbind('submit').submit();

                    $("#divTotalGlobal").show();
                    $("#divGlobal").show();
                    if (tipoMovimiento === '1') {
                        $("#titleGlobal").text("Progreso de la asignación automática para nuevos otorgamientos");
                    } else if (tipoMovimiento === '2') {
                        $("#titleGlobal").text("Progreso de la asignación automática para revalidantes");
                    }

//                    $("#project_feed").show();
                    $("#div_oculto").hide();
                    $("#observaciones").hide();
                    $('.alert-success').hide();
                    $("#preCarga").show();

                    setInterval(function () {
                        if (executePbar)
                            updateProgress();
                    }, 100);


                },
                "funcionCancelar": function () {
                    $("#asignacionAutomatica").data('bootstrapValidator').resetForm();
                }
            });
        });

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
            $(processedId).text("Se han procesado " + elements[2] + " alumnos de un total de  " + elements[3]);
            $(progressbar_).attr('style', 'width: ' + elements[0] + '%');
            $(escuelaId).text(elements[10]);

            if (elements[6] != "0") {
                $("#preCarga").hide();
                $("#project_feed").show();
            }

            $("#pbarGlobal").attr('style', 'width: ' + elements[4] + '%');
            $("#sPercentGlobal").text(elements[4] + '%');
            $("#sEtaGlobal").text("Tiempo estimado de espera: " + msToTime(elements[5]));
            $("#sProcessedGlobal").text(elements[6] + " alumnos procesados de un total de  " + elements[7]);
            $("#sOtorgadosSuccess").text("Alumnos asignados: " + elements[8]);
            $("#sOtorgadosError").text("Alumnos no asignados: " + elements[9]);
            $("#sProcessedBars").text("Se han revalidado " + activeBar + " tipos de beca, de un total de " + elements[12])
        }

        function updateDivNO(aData) {
            var elements = aData.data[0].toString().split(",");
            $("#pbarGlobal").attr('style', 'width: ' + elements[0] + '%');
            $("#sPercentGlobal").text(elements[0] + '%');
            $("#sEtaGlobal").text("Tiempo estimado de espera: " + msToTime(elements[1]));
            $("#sProcessedGlobal").text("Se han procesado " + elements[2] + " alumnos de un total de " + elements[3]);
            if (elements[2] != "0") {
                $("#preCarga").hide();
                $("#project_feed").show();
            }
        }

        function updateProgress() {
            var nivel = $("#nivel option:selected").val();
            var tipomov = $("#movimiento option:selected").val();
            $.ajax({
                type: 'POST',
                url: '/ajax/sincronizaAsignacionesAjax.action?sinc_nivel=' + nivel + '&sinc_tipomov=' + tipomov,
                dataType: 'json',
                success: function (aData) {
                    updateDivsRE(aData);
                    switch (tipomov) {
                        case "1":
                            $("#divTotalGlobal").hide();
                            updateDivsRE(aData);
                            break;
                        case "2":
                            updateDivsRE(aData);
                            break;
                        default:
                            break;
                    }
                }
            });
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