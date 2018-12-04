<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Tablero de control</title>
    <link rel="stylesheet" type="text/css" href="/vendors/fancy-box/jquery.fancybox.min.css" />
    <link rel="stylesheet" type="text/css" href="/vendors/Notifications/css/ns-default.min.css" />
    <link rel="stylesheet" type="text/css" href="/vendors/Notifications/css/ns-style-bar.css" />
    <link rel="stylesheet" type="text/css" href="/vendors/Notifications/css/ns-style-theme.min.css" /> 
    <style>
        /*Evita que se generen espacios blancos no deseados por saltos de linea responsivos*/
        .no-newline {
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }
        /* Efecto de pulsación en los iconos que se desplegan al ser > 0 */
        .pulse-button {
            border: none;
            box-shadow: 0 0 0 0 rgba(255, 255, 255, .7);
            background-color: rgba(255, 255, 255, 1);
            border-radius:50%;
            cursor: pointer;
            -webkit-animation: pulse 1s infinite cubic-bezier(0.66, 0, 0, 1);
            -moz-animation: pulse 1s infinite cubic-bezier(0.66, 0, 0, 1);
            -ms-animation: pulse 1s infinite cubic-bezier(0.66, 0, 0, 1);
            animation: pulse 5s infinite cubic-bezier(0.66, 0, 0, 1);
        }
        .pulse-button:hover {
            -webkit-animation: none;-moz-animation: none;-ms-animation: none;animation: none;
        }

        @-webkit-keyframes pulse {to {box-shadow: 0 0 0 45px rgba(255, 255, 255, 0);}}
        @-moz-keyframes pulse {to {box-shadow: 0 0 0 45px rgba(255, 255, 255, 0);}}
        @-ms-keyframes pulse {to {box-shadow: 0 0 0 45px rgba(255, 255, 255, 0);}}
        @keyframes pulse {to {box-shadow: 0 0 50px 30px rgba(255, 255, 255, 0);}}
        
        #icon-thumbs{
            color: #EC766C;
            font-size: 3em !important;
            cursor: pointer;
        }
        
        #icon-thumbs-up{
            color: #F6916C;
            font-size: 3em !important;
        }

        .side-box{
            display: block;
            float: left;
            padding: 0 30px;
            /*width: 30%;*/
        }

        .offset-1{
            margin-left: 8%;
        }

        .fondo-gris {
        }
        
        .color-gris{
        }

        .sub-section {
            display: none;
        }

        #whole-responsive {
            cursor: pointer;
        }
        
        .color-white{
            color: white !important;
        }
        
        .font-size-1_7{
            font-size: 1.7em !important;
        }
        
        .text-tablero{
            font-size: 20px !important;
            position: absolute;
            bottom: 0;
            right: -25px;
        }
        
        .fondo-solicitudes-ordinarias{
            background-color: #7AC6C2;
        }
        
        .fondo-solicitudes-transporte{
            background-color: #6FC8F3;
        }
        
        .fondo-asignada{
            background-color: #7AA2D6;
        }
        
        .fondo-total-solicitudes{
            background-color: #80B4E3;
        }
        
        .fondo-rechazada{
            background-color: #8187C5;
        }
        
        .fondo-lista-espera{
            background-color: #905DA2;
        }
        
        .fondo-pendiente{
            background-color: #914691;
        }
        
        .fondo-alumnos-censo-salud{
            background-color: #ADD08C;
        }
        
        .fondo-alumnos-pregistro{
            background-color: #87C48B;
        }
        
        .fondo-alumnos-otorgamietos{
            background-color: #F6916C;
        }
        
        .fondo-alumnos-baja{
            background-color: #EC766C;
        }
        
        .fondo-otorgamiento-otro-periodo{
            background-color: #EE8486;
        }
        
        .fondo-otorgamiento-periodo{
            background-color: #F56376;
        }
        
        .font-size-13{
            font-size: 13px;
        }
        
        .fa-stack-1x{
            left: .68em;
        }
    </style>
</head> 

<content tag="tituloJSP">
    Tablero de control
</content>

<body>
    <!-- Barra Solicitudes -->
    <div class="row">
        <div class="primera">
            <!-- Solicitudes Ordinarias responsividad -->
            <div class="col-xs-12 col-sm-6 col-md-4" >
                <div class="main-box infographic-box colored fondo-gris fondo-solicitudes-ordinarias" data-original-color='#7AC6C2' id="ordinarias">
                    <i class="fa fa-pencil-square"></i> 
                    <security:authorize ifAnyGranted="ROLE_JEFEBECAS,ROLE_ANALISTABECAS">
                        <a title="Detalle" class="fancybox fancybox.iframe table-link color-white"  href="/admin/listaEstadisticas.action?est=3">
                        </security:authorize>
                        <span class="headline no-newline">Solicitudes ordinarias</span>
                        <span id="totalAlumnosESECompleto" class="totalAlumnosESECompleto value no-newline"></span>
                        <security:authorize ifAnyGranted="ROLE_JEFEBECAS,ROLE_ANALISTABECAS">
                        </a>
                    </security:authorize>
                </div>
            </div>                    

            <!-- Solicitudes de Transporte responsividad -->
            <div class="col-xs-12 col-sm-6 col-md-4" >
                <div class="main-box infographic-box colored fondo-gris fondo-solicitudes-transporte" id="transporte" data-original-color='#6FC8F3'>
                    <i class="fa fa-bus"></i>                            
                    <security:authorize ifAnyGranted="ROLE_JEFEBECAS,ROLE_ANALISTABECAS">
                        <a title="Detalle" class="fancybox fancybox.iframe table-link color-white"  href="/admin/listaEstadisticas.action?est=4">
                        </security:authorize>
                        <span class="headline no-newline">Solicitudes transporte</span>
                        <span id="totalAlumnosTransporte" class="totalAlumnosTransporte value no-newline"></span>
                        <security:authorize ifAnyGranted="ROLE_JEFEBECAS,ROLE_ANALISTABECAS">
                        </a>
                    </security:authorize> 
                </div>
            </div>                    

            <!-- Total de Solicitudes responsividad -->
            <div class="col-xs-12 col-md-4">
                <div data-toggle-div="solicitudes-detail" id="BigTotal" data-original-color='#80B4E3' data-color-b='#b2b09b' class="infographic-box colored main-box fondo-gris fondo-total-solicitudes">
                    <i id="icon-circle" class="fa fa-circle pulse-button"></i>
                    <security:authorize ifAnyGranted="ROLE_JEFEBECAS,ROLE_ANALISTABECAS">
                        <a title="Detalle" class="fancybox fancybox.iframe table-link color-white"  href="/admin/listaEstadisticas.action?est=2">
                        </security:authorize>
                        <span class="headline no-newline">Total de solicitudes</span>
                        <security:authorize ifAnyGranted="ROLE_JEFEBECAS,ROLE_ANALISTABECAS">
                        </a>
                    </security:authorize>
                    <span id="totalAlumnosSolicitud" class="totalAlumnosSolicitud value no-newline"></span>
                    
                    <security:authorize ifAnyGranted="ROLE_JEFEBECAS,ROLE_ANALISTABECAS,ROLE_RESPONSABLE_UA,ROLE_FUNCIONARIO_UA">
                        <a id='pendientes' title='Resumen pendientes' target='_blank' href='/descargaEstadisticaSolicitudes.action?periodo=x' class="color-white">
                            <i class='fa fa-file-excel-o text-tablero'></i>
                        </a> 
                        
                    </security:authorize>
                </div>
            </div>  
        </div>
    </div>

    <!--Solicitudes detalle-->
    <div class="row">
        <div id="solicitudes-detail" class="col-sm-12 sub-section first">
            <div class="row">
                <!-- Asignada -->
                <div id="status1" class="col-md-3 col-xs-12 col-sm-6">
                    <div class="main-box infographic-box colored fondo-gris fondo-asignada" data-original-color='#7AA2D6' id="asignada">
                        <i class="fa"><img alt="" src="/resources/img/tablero/1.png"/></i>
                            <security:authorize ifAnyGranted="ROLE_JEFEBECAS,ROLE_ANALISTABECAS">
                            <a title="Detalle" class="fancybox fancybox.iframe table-link color-white"  href="/admin/listaEstadisticas.action?est=8">
                            </security:authorize>
                            <span id = "sAlumnosPreNS" class="headline no-newline" >Asignada</span>
                            <span class="value no-newline totalAlumnosSolicitudAsignada font-size-1_7"></span>
                            <security:authorize ifAnyGranted="ROLE_JEFEBECAS,ROLE_ANALISTABECAS">
                            </a>
                        </security:authorize>
                        <div>&nbsp;</div>
                        <security:authorize ifAnyGranted="ROLE_JEFEBECAS,ROLE_ANALISTABECAS,ROLE_RESPONSABLE_UA,ROLE_FUNCIONARIO_UA">
                            <a  title='Resumen' target='_blank' href='/tablero/solicitudes/detalleSolicitudesResumenSolicitudes.action?op=1' class="color-white">
                                <i class='fa fa-file-pdf-o text-tablero'></i>
                            </a>
                        </security:authorize>
                    </div>
                </div>

                <!-- Rechazada -->
                <div id="status2" class="col-md-3 col-xs-12 col-sm-6">
                    <div class="main-box infographic-box colored fondo-gris fondo-rechazada" data-original-color='#8187C5' id="rechazada">
                        <i class="fa"><img alt="" src="/resources/img/tablero/2.png"/></i>
                            <security:authorize ifAnyGranted="ROLE_JEFEBECAS,ROLE_ANALISTABECAS">
                            <a title="Detalle" class="fancybox fancybox.iframe table-link color-white"  href="/admin/listaEstadisticas.action?est=7">
                            </security:authorize>
                            <span id = "sAlumnosPreNS" class="headline no-newline" >Rechazada</span>
                            <span id ="totalAlumnosSolicitudRechazada" class="value no-newline font-size-1_7"></span>
                            <security:authorize ifAnyGranted="ROLE_JEFEBECAS,ROLE_ANALISTABECAS">
                            </a>
                        </security:authorize>
                        <div>&nbsp;</div>
                        <security:authorize ifAnyGranted="ROLE_JEFEBECAS,ROLE_ANALISTABECAS,ROLE_RESPONSABLE_UA,ROLE_FUNCIONARIO_UA">
                            <a  title='Resumen' target='_blank' href='/tablero/solicitudes/detalleSolicitudesResumenSolicitudes.action?op=2' class="color-white">
                                <i class='fa fa-file-pdf-o text-tablero'></i>
                            </a>
                        </security:authorize>
                    </div>
                </div>

                <!-- Lista de Espera -->
                <div id="status3" class="col-md-3 col-xs-12 col-sm-6">
                    <div class="main-box infographic-box colored fondo-gris fondo-lista-espera" data-original-color='#905DA2' id="espera">
                        <i class="fa"><img alt="" src="/resources/img/tablero/3.png"/></i>
                            <security:authorize ifAnyGranted="ROLE_JEFEBECAS,ROLE_ANALISTABECAS">
                            <a title="Detalle" class="fancybox fancybox.iframe table-link color-white"  href="/admin/listaEstadisticas.action?est=6">
                            </security:authorize>
                            <span id = "sAlumnosPreNS" class="headline no-newline" >Lista de espera</span>
                            <span id ="totalAlumnosSolicitudEspera" class="value no-newline font-size-1_7"></span>
                            <security:authorize ifAnyGranted="ROLE_JEFEBECAS,ROLE_ANALISTABECAS">
                            </a>
                        </security:authorize>
                        <div>&nbsp;</div>
                        <security:authorize ifAnyGranted="ROLE_JEFEBECAS,ROLE_ANALISTABECAS,ROLE_RESPONSABLE_UA,ROLE_FUNCIONARIO_UA">
                            <a  title='Resumen' target='_blank' href='/tablero/solicitudes/detalleSolicitudesResumenSolicitudes.action?op=3' class="color-white">
                                <i class='fa fa-file-pdf-o text-tablero'></i>
                            </a>
                        </security:authorize>
                    </div>
                </div>

                <!-- Pendiente -->
                <div id="status4" class="col-md-3 col-xs-12 col-sm-6">
                    <div class="main-box infographic-box colored fondo-gris fondo-pendiente" data-original-color='#914691' id="pendiente">
                        <a onclick="show_pendientes()" id="circle"><i class="fa fa-circle-o-notch"></i></a>
                            <security:authorize ifAnyGranted="ROLE_JEFEBECAS, ROLE_ANALISTABECAS">
                            <a title="Detalle" class="fancybox fancybox.iframe table-link color-white"  href="/admin/listaEstadisticas.action?est=5">
                            </security:authorize>
                            <span id = "sAlumnosPreNS" class="headline no-newline " >Pendiente</span>                               
                            <span class="value no-newline totalASolicitudesPendientes font-size-1_7" title="Todos excepto manutención"></span>
                            <security:authorize ifAnyGranted="ROLE_JEFEBECAS,ROLE_ANALISTABECAS">
                            </a>
                        </security:authorize>
                        <div>&nbsp;</div>
                        
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Barra de Censo de Salud | Alumnos Pre Asignados | Alumnos con Baja -->
    <div class="row segunda">
        <!-- Censo de Salud -->
        <div class="col-sm-6 col-xs-12">
            <div class="main-box infographic-box colored fondo-gris fondo-alumnos-censo-salud" data-original-color='#ADD08C' id="salud">
                <i class="fa fa-hospital-o"></i>
                <security:authorize ifAnyGranted="ROLE_JEFEBECAS,ROLE_ANALISTABECAS">
                    <a title="Detalle" class="fancybox fancybox.iframe table-link color-white"  href="/admin/listaEstadisticas.action?est=9">
                    </security:authorize>
                    <span class="headline no-newline font-size-13">Alumnos con Censo de salud</span>
                    <span id="totalAlumnosSalud" class="value"></span>

                    <security:authorize ifAnyGranted="ROLE_JEFEBECAS,ROLE_ANALISTABECAS">
                    </a>
                </security:authorize>
                <div>&nbsp;</div>
                <security:authorize ifAnyGranted="ROLE_JEFEBECAS,ROLE_CONSULTA">
                    <a  title='Resumen' target='_blank' href='/admin/descargarEstadisticas.action?est=4' class="color-white">
                        <i class='fa fa-file-excel-o text-tablero'></i>
                    </a>
                </security:authorize>
            </div>
        </div>

        <!-- Alumnos Pre-Asignados -->
        <div class="col-sm-6 col-xs-12">
            <div class="main-box infographic-box colored fondo-gris fondo-alumnos-pregistro" data-original-color='#87C48B' id="preasignados" >
                <i class="fa fa-magic"></i>
                <span id = "sAlumnosPreNS" class="headline no-newline" >Alumnos pre-asignados</span>
                <span id ="totalAlumnosPreNS" class="value"></span>
                <div>&nbsp;</div>
            </div>                    
        </div>
    </div>


    <div class="row">
        <!-- Alumnos Con Otorgamiento -->
        <div class="col-sm-6 col-xs-12">
            <div class="main-box infographic-box colored fondo-gris fondo-alumnos-otorgamietos" data-original-color='#F6916C' id="otorgamiento">
                <i class="fa fa-circle"></i>   
                <i id="icon-thumbs-up" class="fa fa-thumbs-o-up fa-stack-1x color-gris" data-original-color='#F6916C'></i>
                <security:authorize ifAnyGranted="ROLE_JEFEBECAS,ROLE_ANALISTABECAS,ROLE_EJECUTIVO">                        
                    <a title="Detalle" class="fancybox fancybox.iframe color-white"  href="/admin/listaEstadisticas.action?est=10">
                    </security:authorize>                            
                    <span class="headline no-newline">Alumnos con otorgamiento</span>
                    <span class="value totalAlumnosAsignados"></span>
                    <security:authorize ifAnyGranted="ROLE_JEFEBECAS,ROLE_ANALISTABECAS,ROLE_EJECUTIVO">
                    </a>
                </security:authorize>
                <div>&nbsp;</div>
                <security:authorize ifAnyGranted="ROLE_JEFEBECAS,ROLE_ANALISTABECAS,ROLE_RESPONSABLE_UA,ROLE_CONSULTA,ROLE_ANALISTAADMINISTRATIVO,ROLE_EJECUTIVO">
                    <a  title='Resumen' target='_blank' href='/procesos/resumen/descargarProceso.action?periodo=x&i=true'>
                        <i class='fa fa-file-excel-o text-tablero'></i>
                    </a>
                </security:authorize>
            </div>
        </div>

        <!-- Alumnos Con Baja -->
        <div class="col-sm-6 col-xs-12">
            <div class="main-box infographic-box colored fondo-gris fondo-alumnos-baja" id="BigBaja" data-toggle-div="barraBajas" data-original-color='#EC766C' data-color-b='#b2b09b'>
                    <i class="fa fa-circle pulse-button"></i>   
                    <i id="icon-thumbs" class="fa fa-thumbs-o-down fa-stack-1x color-gris" data-original-color="#EC766C"></i>
                <security:authorize ifAnyGranted="ROLE_JEFEBECAS,ROLE_ANALISTABECAS,ROLE_EJECUTIVO">
                    <a title="Detalle" class="fancybox fancybox.iframe table-link color-white"  href="/admin/listaEstadisticas.action?est=11">                    
                    </security:authorize>
                    <span class="headline no-newline" >Alumnos con baja</span>
                    <security:authorize ifAnyGranted="ROLE_JEFEBECAS,ROLE_ANALISTABECAS,ROLE_EJECUTIVO">
                    </a>
                </security:authorize>
                    <span id="totalAlumnosBajas" class="value"></span>
                <div>&nbsp;</div>
                <security:authorize ifAnyGranted="ROLE_JEFEBECAS,ROLE_ANALISTABECAS,ROLE_RESPONSABLE_UA,ROLE_CONSULTA,ROLE_ANALISTAADMINISTRATIVO,ROLE_EJECUTIVO">
                    <a  title='Resumen' target='_blank' href='/procesos/resumen/descargarProceso.action?periodo=x&i=false'>
                        <i class='fa fa-file-excel-o text-tablero'></i>
                    </a>
                </security:authorize>
            </div>
        </div>
    </div>

    <!--Inicia barra bajas-->
    <div class="row">
        <div id="barraBajas" class="col-sm-12 sub-section first">
            <div class="row">
                <div class="col-sm-6 col-xs-12">
                    <div class="main-box infographic-box colored fondo-gris fondo-otorgamiento-otro-periodo" data-original-color='#EE8486' id="otrosperiodos">
                        <!-- Otros periodos -->
                        <i class="fa fa-calendar"></i>                        
                        <span id = "sAlumnosPreNS" class="headline no-newline" >De otorgamientos del periodo anterior</span>
                        <span class="value no-newline bajasOtroPeriodo"></span>
                    </div>
                </div>


                <div class="col-sm-6 col-xs-12">
                    <div class="main-box infographic-box colored fondo-gris fondo-otorgamiento-periodo" data-original-color='#F56376' id="periodoactual">
                        <!-- Periodo actual -->
                        <i class="fa fa-calendar-o"></i>
                        <span id = "sAlumnosPreNS" class="headline no-newline" >De otorgamientos del periodo</span>
                        <span class="value no-newline bajasPeriodoActual"></span>
                    </div>
                </div>
            </div> 
        </div>
    </div>
</body>


<content tag="endScripts">
    <script type="text/javascript" src="/vendors/fancy-box/jquery.fancybox.min.js"></script>
    <script src="/vendors/Notifications/js/classie.min.js" type="text/javascript"></script>
    <script src="/vendors/Notifications/js/modernizr.custom.min.js" type="text/javascript"></script>
    <script src="/vendors/Notifications/js/notificationFx.js" type="text/javascript"></script>
</content>

<content tag="inlineScripts">
    <script>

                            $(document).ready(function () {

                                //ColorBoxes exteriores
                                var all = ["#ordinarias", "#transporte", "#salud", "#preasignados", "#otorgamiento", "#BigTotal", "#BigBaja"];

                                //ColorBoxes Total Solicitudes
                                var inside1 = ["#asignada", "#rechazada", "#espera", "#pendiente"];

                                //ColorBoxes Total Bajas
                                var inside2 = ["#otrosperiodos", "#periodoactual"];
                                
                                //ColorThumbs
                                var inside3 = ["#icon-thumbs", "#icon-thumbs-up"];

                                $('.fancybox').fancybox({
                                    minHeight: 650
                                });
        
                                $('#icon-circle, #icon-thumbs').on('click', function () {
                                    if(this.id === 'icon-circle'){
                                        chow(document.getElementById('BigTotal'), all, inside1, inside2, inside3);
                                    }else if(this.id === 'icon-thumbs'){
                                        chow(document.getElementById('BigBaja'), all, inside1, inside2, inside3);
                                    }
                                });

                                $('[data-toggle="tooltip"]').tooltip();
                                $(".totalAlumnosESECompleto").html(Number(<s:property value="totalAlumnosESECompleto"/>).toLocaleString('en'));
                                $(".totalAlumnosTransporte").html(Number(<s:property value="totalAlumnosTransporte"/>).toLocaleString('en'));
                                $(".totalAlumnosSolicitud").html(Number(<s:property value="totalAlumnosSolicitud"/>).toLocaleString('en'));
                                $(".totalAlumnosBecaUniversal").html(Number(<s:property value="totalAlumnosBecaUniversal"/>).toLocaleString('en'));
                                $(".totalAlumnosOtrasBecas").html(Number(<s:property value="totalAlumnosOtrasBecas"/>).toLocaleString('en'));
                                $(".totalAlumnosSolicitudAsignada").html(Number(<s:property value="totalAlumnosSolicitudAsignada"/>).toLocaleString('en'));
                                $("#totalAlumnosSolicitudRechazada").html(Number(<s:property value="totalAlumnosSolicitudRechazada"/>).toLocaleString('en'));
                                $("#totalAlumnosSolicitudEspera").html(Number(<s:property value="totalAlumnosSolicitudEspera"/>).toLocaleString('en'));
                                $(".totalASolicitudesPendientes").html(Number(<s:property value="totalASolicitudesPendientes"/>).toLocaleString('en'));
                                $(".totalAlumnosSolicitudPendiente").html(Number(<s:property value="totalASolicitudPendienteElse"/>).toLocaleString('en'));
                                $(".totalAlumnosSolicitudPendienteManu").html(Number(<s:property value="totalASolicitudPendienteManu"/>).toLocaleString('en'));
                                $("#totalAlumnosSalud").html(Number(<s:property value="totalAlumnosSalud"/>).toLocaleString('en'));
                                $("#totalAlumnosPreNS").html(Number(<s:property value="totalAlumnosPreNS"/>).toLocaleString('en'));
                                $("#totalAlumnosPreS").html(Number(<s:property value="totalAlumnosPreS"/>).toLocaleString('en'));
                                $(".totalAlumnosAsignados").html(Number(<s:property value="totalAlumnosAsignados"/>).toLocaleString('en'));
                                $("#totalAlumnosBajas").html(Number(<s:property value="totalAlumnosBajas"/>).toLocaleString('en'));
                                $(".bajasPeriodoActual").html(Number(<s:property value="bajasPeriodoActual"/>).toLocaleString('en'));
                                $(".bajasOtroPeriodo").html(Number(<s:property value="bajasOtroPeriodo"/>).toLocaleString('en'));


                                if (<s:property value="bajasOtroPeriodo"/> != 0) {
                                    // Si hay en otro periodo
                                    // mostrar la sección extra
                                    // si no, pues no
                                }

                                var isResponsableOrFuncionario = <s:property value="isResponsableOrFuncionario"/>
                                if (isResponsableOrFuncionario === true) {
                                    if ($("#totalAlumnosPreS").text() === "0") {
                                        $("#divAlumnosPreS").hide();
                                        $("#sAlumnosPreNS").text("Alumnos pre-asignados de " + $("#sUA").text());
                                    }
                                    if ($("#totalAlumnosPreNS").text() === "0") {
                                        $("#divAlumnosPreNS").hide();
                                        $("#sAlumnosPreS").text("Alumnos pre-asignados de " + $("#sUA").text());
                                    }
                                    if ($("#totalAlumnosSolicitudPendiente").text() === "0") {
                                        $("#pendientes").hide();
                                    }
                                }

                                if (<s:property value="notificacionesRolSize" /> !== 0)
                                {
                                    var notification = new NotificationFx({
                                        color: "<s:property value="notificacionesRol[0].tipoNotificacion.color"/>",
                                        icono: "<s:property value="notificacionesRol[0].tipoNotificacion.icono"/>",
                                        nombre: "<s:property value="notificacionesRol[0].titulo"/>",
                                        texto: "<s:property value="notificacionesRol[0].notificacion" escape="false" />",
                                        ttl: 5000
                                    });
                                    // show the notification
                                    notification.show();
                                }
                                
                                if (<s:property value="totalAlumnosSolicitud"/> == "0") {
                                    document.getElementById('icon-circle').className = "fa fa-circle";
                                }

                                if (<s:property value="totalAlumnosBajas"/> == "0") {
                                    document.getElementById('icon-thumbs').className = "fa fa-thumbs-o-down fa-stack-1x";
                                }

                            });

                            function chow(element, all, inside1, inside2, inside3) {
                                // obtiene el nombre del div a ocultar (viene dentro del data tag)
                                var divID = $(element).data("toggle-div");
                                // consigue el div
                                var div = $('#' + divID);
                                // decide si ocultarlo, o mostrarlo
                                if ($(div).css("display") === 'none') {
                                    $(div).css("display", 'block');

                                    //Pinta el padre y los hijos y los demás de gris
                                    $(".fondo-gris").css("background-color", "#DDDDDD");
                                    $(".color-gris").css("color", "#DDDDDD");
                                    var originalColorPadre = $(element).data('original-color');
                                    $(element).css('background-color', originalColorPadre);
                                    
                                    if (element.id === "BigTotal") {
                                        for (var i = 0; i < inside1.length; i++)
                                        {
                                            var originalColor = $(inside1[i]).data('original-color');
                                            $(inside1[i]).css('background-color', originalColor);
                                        }
                                    }
                                    else {
                                        for (var i = 0; i < inside2.length; i++)
                                        {
                                            var originalColor = $(inside2[i]).data('original-color');
                                            $(inside2[i]).css('background-color', originalColor);
                                        }
                                        var originalColorThumble = $(inside3[0]).data('original-color');
                                        $(inside3[0]).css('color', originalColorThumble);
                                    }
                                } else if ($("#barraBajas").css("display") === 'block' && $("#solicitudes-detail").css("display") === 'block')
                                {
                                    $("#barraBajas").css("display", 'none');
                                    $("#solicitudes-detail").css("display", 'none');
                                    for (var i = 0; i < all.length; i++)
                                    {
                                        var originalColor = $(all[i]).data('original-color');
                                        $(all[i]).css('background-color', originalColor);
                                    }
                                    for (var x = 0; x < inside3.length; x++){
                                        var originalColor2 = $(inside3[x]).data('original-color');
                                        $(inside3[x]).css('color', originalColor2);
                                    }
                                } else if ($(div).css("display") === 'block') {
                                    $(div).css("display", 'none');
                                    for (var i = 0; i < all.length; i++)
                                    {
                                        var originalColor = $(all[i]).data('original-color');
                                        $(all[i]).css('background-color', originalColor);
                                    }
                                    for (var x = 0; x < inside3.length; x++){
                                        var originalColor2 = $(inside3[x]).data('original-color');
                                        $(inside3[x]).css('color', originalColor2);
                                    }
                                }
                            }

                            function show_pendientes() {
                                if (document.getElementById('BarraAsignacionesPendientes').style.display === 'none')
                                {
                                    document.getElementById('BarraAsignacionesPendientes').style.display = 'block';
                                } else if (document.getElementById('BarraAsignacionesPendientes').style.display === 'block')
                                {
                                    document.getElementById('BarraAsignacionesPendientes').style.display = 'none';
                                }
                            }
    </script>
</content>
