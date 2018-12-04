<%@taglib prefix="decorator" uri="/WEB-INF/decorators/tld/sitemesh-decorator.tld" %>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@taglib prefix="page" uri="/WEB-INF/decorators/tld/sitemesh-page.tld" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>

<security:authorize ifAnyGranted="ROLE_ANONYMOUS">
    <!-- ROL ANONIMO-->
    <!DOCTYPE html>
    <html lang="es-mx">
        <head>
            <meta charset="UTF-8" />
            <title>Inicio - SIBEC</title>
            <meta http-equiv="X-UA-Compatible" content="IE=edge;chrome=1" />
            <s:head />

            <decorator:head />
        </head>
        <body<decorator:getProperty property="body.onload" writeEntireProperty="true" />
            <decorator:getProperty property="body.class" writeEntireProperty="true" />
            <security:authorize ifNotGranted="ROLE_ANONYMOUS">
                oncontextmenu="return false;"
            </security:authorize> 
            <decorator:body />
    </body>
</html>
</security:authorize>

<security:authorize ifNotGranted="ROLE_ANONYMOUS">
    <!DOCTYPE html>
    <html lang="es-mx">
        <head>
            <!-- Google Tag Manager -->
            <script>(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':
            new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],
            j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
            'https://www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
            })(window,document,'script','dataLayer','GTM-K99368F');</script>
            <!-- End Google Tag Manager -->
            <meta charset="UTF-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1.0" />
            <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />

            <title><decorator:title default="IPN " /> - SIBEC</title>

            <!-- bootstrap -->
            <link rel="stylesheet" type="text/css" href="/vendors/bootstrap/bootstrap.min-3.3.7.css" />

            <!-- global styles -->
            <link rel="stylesheet" type="text/css" href="/resources/css/theme_styles.min.css" />

            <!-- libraries -->
            <link rel="stylesheet" type="text/css" href="/vendors/font-awesome/font-awesome.min-4.6.3.css" />
            <link rel="stylesheet" type="text/css" href="/vendors/nanoscroller/nanoscroller.min.css" />

            <!-- Favicon -->
            <link type="image/x-icon" href="/favicon.png" rel="shortcut icon" />

            <!-- Struts utils -->
            <link rel="stylesheet" href="/vendors/struts/styles.min.css" type="text/css">

            <!-- google font libraries -->
            <link href='https://fonts.googleapis.com/css?family=Open+Sans:400,600,700,300|Titillium+Web:200,300,400' rel='stylesheet' type='text/css'>

            <!-- this page specific styles -->
            <decorator:head />

            <!--[if lt IE 9]>
        <script src="js/html5shiv.js"></script>
        <script src="js/respond.min.js"></script>
        <![endif]-->
        </head>

        <security:authorize ifAnyGranted="ROLE_ALUMNO">
            <body  class="theme-turquoise fixed-header fixed-footer boxed-layout pace-done fixed-leftmenu">
            </security:authorize>
            <security:authorize ifNotGranted="ROLE_ALUMNO">
            <body  class="theme-whbl fixed-header fixed-footer boxed-layout pace-done fixed-leftmenu">
            </security:authorize>
            <!-- Google Tag Manager (noscript) -->
            <noscript><iframe src="https://www.googletagmanager.com/ns.html?id=GTM-K99368F"
            height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript>
            <!-- End Google Tag Manager (noscript) -->
   
            <div id="theme-wrapper">
                <header class="navbar" id="header-navbar">
                    <div class="container">
                        <a id="logo" class="navbar-brand hidden-md hidden-lg hidden-xl pull-left" href="mostrar.action">                           
                            <img class="rounded float-left" src="/resources/img/login/Logo Blancoooo.svg" alt="Logo Sibec">
                        </a>
                        <div class="clearfix">
                            <button class="navbar-toggle" data-target=".navbar-ex1-collapse" data-toggle="collapse" type="button">
                                <span class="sr-only">Toggle navigation</span>
                                <span class="fa fa-bars"></span>
                            </button>

                            <div class="nav-no-collapse navbar-left pull-left hidden-sm hidden-xs">
                                <ul class="nav navbar-nav pull-left">
                                    <li>
                                        <a class="btn" id="make-small-nav">
                                            <i class="fa fa-bars"></i>
                                        </a>
                                    </li>
                                    <security:authorize ifAnyGranted="ROLE_JEFEBECAS,ROLE_JEFEADMINISTRATIVO">
                                        <li class="dropdown hidden-xs">
                                            <a class="btn dropdown-toggle" data-toggle="dropdown">
                                                <i class="fa fa-archive"></i>
                                                <s:if test="odList.size>0">
                                                    <span class="count"><s:property value="odList.size" /></span>
                                                </s:if>
                                            </a>
                                            <ul class="dropdown-menu notifications-list">
                                                <li class="pointer">
                                                    <div class="pointer-inner">
                                                        <div class="arrow"></div>
                                                    </div>
                                                </li>
                                                <li class="item-header">Tienes <s:property value="odList.size" /> órdenes de depósito pendientes</li>
                                                <!--Listamos sólo las últimas 10 órdenes de depósito-->
                                                <s:set var="i" value="0"></s:set>
                                                <s:iterator value="odList">
                                                    <s:if test="#i <=10">
                                                        <s:set var="i" value="%{#i+1}"/>
                                                        <li class="item">
                                                            <a  class="fancybox fancybox.iframe"  href="/becas/detalleOrdenesDeposito.action?ordenId=<s:property value="id" />">
                                                                <i class="fa fa-credit-card"></i>
                                                                <span class="content"><s:property value="nombreOrdenDeposito" /></span>
                                                                <span class="time"><i class="fa fa-clock-o"></i><s:property value="elapsedTime" /></span>
                                                            </a>
                                                        </li>
                                                    </s:if>
                                                </s:iterator>
                                                <li class="item-footer">
                                                    <a href="/becas/listaOrdenesDeposito.action">
                                                        Ver todas las órdenes de depósito
                                                    </a>
                                                </li>
                                            </ul>
                                        </li>
                                        <li class="dropdown hidden-xs">
                                            <a class="btn dropdown-toggle" data-toggle="dropdown">
                                                <s:if test="notificacionesRolSize > 0">
                                                    <i class="fa fa-bell"></i>
                                                    <span class="count"><s:property value="notificacionesRolSize" /></span>
                                                </s:if>
                                                <s:else>
                                                    <i class="fa fa-bell-o"></i>
                                                </s:else>
                                            </a>
                                            <ul class="dropdown-menu notifications-list">
                                                <li class="pointer">
                                                    <div class="pointer-inner">
                                                        <div class="arrow"></div>
                                                    </div>
                                                </li>
                                                <s:if test="notificacionesRolSize > 0">
                                                    <li class="item-header">Tienes <s:property value="notificacionesRolSize" /> notificaciones</li>
                                                    </s:if>
                                                    <s:else>
                                                    <li class="item-header">Tienes 0 notificaciones</li>
                                                    </s:else>

                                                <s:iterator value="notificacionesRol">
                                                    <li class="item" onclick='showNotification("<s:property value="tipoNotificacion.color"/>",
                                                                    "<s:property value="tipoNotificacion.icono"/>",
                                                                    "<s:property value="titulo"/>",
                                                                    "<s:property value="notificacion.replaceAll('\\'', '')" escape="false"/>")'>
                                                        <a>
                                                            <i class="<s:property value="tipoNotificacion.icono"/>"></i>
                                                            <span class="content"><s:property value="titulo"/></span>
                                                            <span class="time" title="<s:property value="fechaFinal" />"><i class="fa fa-clock-o"></i><s:property value="fechaFinal" /></span>
                                                        </a>
                                                    </li>
                                                </s:iterator>
                                                <li class="item-footer">
                                                    <a href="/admin/administrarNotificaciones.action">
                                                        <i class="fa fa-stack"></i>
                                                        <span class="content">Administrar Notificaciones</span>
                                                    </a>
                                                </li>
                                            </ul>
                                        </li>
                                    </security:authorize>
                                    <security:authorize ifAnyGranted="ROLE_CONSULTA">
                                        <li class="dropdown hidden-xs">
                                            <a style="pointer-events: none; cursor: default;">
                                                <span>Usuario SOLO LECTURA</span>
                                            </a>
                                        </li>
                                    </security:authorize>
                                    <!--Notificaciones para alumno-->
                                    <security:authorize ifAnyGranted="ROLE_ALUMNO">
                                        <li class="dropdown hidden-xs">
                                            <a class="btn dropdown-toggle" data-toggle="dropdown">
                                                <s:if test="!notificaciones.get(0).tipo.toString().equals('VACIA')">
                                                    <i class="fa fa-archive"></i>
                                                    <span class="count"><s:property value="notificaciones.size" /></span>
                                                </s:if>
                                                <s:else>
                                                    <i class="fa fa-archive"></i>
                                                </s:else>
                                            </a>
                                            <ul class="dropdown-menu notifications-list">
                                                <li class="pointer">
                                                    <div class="pointer-inner">
                                                        <div class="arrow"></div>
                                                    </div>
                                                </li>
                                                <s:if test="!notificaciones.get(0).tipo.toString().equals('VACIA')">
                                                    <li class="item-header">Tienes <s:property value="notificaciones.size" /> mensajes</li>
                                                    </s:if>
                                                    <s:else>
                                                    <li class="item-header">Tienes 0 mensajes</li>
                                                    </s:else>

                                                <s:iterator value="notificaciones">
                                                    <li class="item">
                                                        <a  class="fancybox fancybox.iframe"  href="#">
                                                            <s:if test="tipo.toString().equals('ERRORCONFIG')">
                                                                <i class="fa fa-exclamation-circle" style="color: #495057"></i>
                                                            </s:if>
                                                            <s:elseif  test="tipo.toString().equals('DEPOSITO')">
                                                                <i class="fa fa-money" style="color: #66B370"></i>
                                                            </s:elseif>
                                                            <s:elseif  test="tipo.toString().equals('ERRORDEPOSITO')">
                                                                <i class="fa fa-times" style="color: #dd191d"></i>
                                                            </s:elseif>
                                                            <s:elseif  test="tipo.toString().equals('VACIA')">                                                            
                                                                <i class="fa fa-bell-o"></i>
                                                            </s:elseif>
                                                            <s:else>                                                            
                                                                <i class="fa fa-truck" style="color: #EFC945"></i>
                                                            </s:else>

                                                            <span class="content"><s:property value="mensaje" escape="false"/></span>
                                                            <span class="time" title="<s:property value="fechaString" />"><i class="fa fa-clock-o"></i><s:property value="fechaCorta" /></span>
                                                        </a>
                                                    </li>
                                                </s:iterator>
                                            </ul>
                                        </li>

                                        <li class="dropdown hidden-xs">
                                            <a class="btn dropdown-toggle" data-toggle="dropdown">
                                                <s:if test="notificacionesRolSize > 0">
                                                    <i class="fa fa-bell"></i>
                                                    <span class="count"><s:property value="notificacionesRolSize" /></span>
                                                </s:if>
                                                <s:else>
                                                    <i class="fa fa-bell-o"></i>
                                                </s:else>
                                            </a>
                                            <ul class="dropdown-menu notifications-list">
                                                <li class="pointer">
                                                    <div class="pointer-inner">
                                                        <div class="arrow"></div>
                                                    </div>
                                                </li>
                                                <s:if test="notificacionesRolSize > 0">
                                                    <li class="item-header">Tienes <s:property value="notificacionesRolSize" /> notificaciones</li>
                                                    </s:if>
                                                    <s:else>
                                                    <li class="item-header">Tienes 0 notificaciones</li>
                                                    </s:else>

                                                <s:iterator value="notificacionesRol">
                                                    <li class="item" onclick='showNotification("<s:property value="tipoNotificacion.color"/>",
                                                                    "<s:property value="tipoNotificacion.icono"/>",
                                                                    "<s:property value="titulo"/>",
                                                                    "<s:property value="notificacion.replaceAll('\\'', '')" escape="false"/>")'>
                                                        <a>
                                                            <i class="<s:property value="tipoNotificacion.icono"/>"></i>
                                                            <span class="content"><s:property value="titulo"/></span>
                                                            <span class="time" title="<s:property value="fechaFinal" />"><i class="fa fa-clock-o"></i><s:property value="fechaFinal" /></span>
                                                        </a>
                                                    </li>
                                                </s:iterator>
                                            </ul>
                                        </li>
                                    </security:authorize>
                                </ul>
                            </div>

                            <div class="nav-no-collapse pull-right" id="header-nav">
                                <ul class="nav navbar-nav pull-right">                                    
                                    <li class="dropdown profile-dropdown">
                                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                            <img src="/resources/img/template_img/student_small.png" width="32" height="32" alt=""/>
                                            <span class="hidden-xs"><%=ActionContext.getContext().getSession().get("nombreCompleto")%></span> <b class="caret"></b>
                                        </a>
                                        <ul class="dropdown-menu dropdown-menu-right">
                                            <li><a id="sUA" href="#"><i class="fa fa-building"></i><%=ActionContext.getContext().getSession().get("unidadAcademica")%></a></li>
                                            <li><a href="#"><i class="fa fa-user"></i><%=ActionContext.getContext().getSession().get("folio")%></a></li>
                                            <li><a href="#"><i class="fa fa-star"></i><p id="getrol"><%=ActionContext.getContext().getSession().get("rol")%></p></a></li>
                                            <li><a href="/j_spring_security_logout"><i class="fa fa-power-off"></i>Cerrar sesión</a></li>
                                        </ul>
                                    </li>
                                    <li class="hidden-xxs">
                                        <a class="btn" href="/j_spring_security_logout">
                                            <i class="fa fa-power-off"></i>
                                        </a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </header>
                <div id="page-wrapper" class="container">
                    <div class="row">
                        <div id="nav-col">
                            <section id="col-left" class="col-left-nano">
                                <div id="col-left-inner" class="col-left-nano-content">
                                    <div id="user-left-box" class="clearfix hidden-sm hidden-xs dropdown profile2-dropdown">
                                        <a href="/"><img alt="Logo Sibec" src="/resources/img/login/logo-sibec.svg" width="145" height="65"/></a>
                                    </div>
                                    <div class="collapse navbar-collapse navbar-ex1-collapse" id="sidebar-nav">	
                                        <%=ActionContext.getContext().getSession().get("menu")%>
                                    </div>
                                </div>
                            </section>
                            <div id="nav-col-submenu"></div>
                        </div>
                        <div id="content-wrapper">

                            <div class="row">
                                <div class="col-lg-12">
                                    <div id="content-header" class="clearfix">
                                        <div class="pull-left">
                                            <h1><decorator:extractProperty property="page.tituloJSP"/></h1>
                                        </div>
                                        <div class="pull-right hidden-xs">
                                            <decorator:extractProperty property="page.tituloDerecho"/>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Empieza body de esta página -->
                            <decorator:body/>

                            <!-- Termina body de esta página -->

                            <footer id="footer-bar" class="row" style="background-color: #e2e2e2">
                                <p id="footer-copyright" class="col-xs-12">Unidad Profesional "Adolfo López Mateos", Av. Miguel Bernard Esq. Miguel Othón de Mendizabal s/n Col.<br/>Residencial La Escalera, Edif. Secretaría Gestión Estratégica 2° Piso, Ciudad de México, C.P. 07738<br/>Tel. 57296000, Ext. 51844</p>
                            </footer>
                        </div>
                    </div>
                </div>
            </div>
            <security:authorize ifAnyGranted="ROLE_CONSULTA">
                <span class="sr-only" id="bandera-usuario-solo-lectura">Solo lectura</span>
            </security:authorize>

            <!-- global scripts -->
            <script src="/vendors/jQuery/jquery-3.1.1.min.js"></script>
            <script src="/vendors/bootstrap/bootstrap-3.3.7.min.js"></script>
            <script src="/vendors/nanoscroller/jquery.nanoscroller-0.8.7.min.js"></script>
            <script src="/resources/js/solo-lectura.min.js"></script>

            <!-- this page specific scripts -->
            <script>
                function showNotification(color, icono, nombre, texto) {
                    var notification = new NotificationFx({
                        color: color,
                        icono: icono,
                        nombre: nombre,
                        texto: texto,
                        ttl: 5000
                    });
                    // show the notification
                    notification.show();
                }                
                deshabilitarUsuarioLectura();
            </script>
            
            
            <decorator:extractProperty property="page.endScripts"/>
            

            <!-- theme scripts -->
            <script src="/resources/js/scripts.min.js"></script>
            <script src="/vendors/pace/pace.min-1.0.js"></script>

            <!-- this page specific inline scripts -->
            <decorator:extractProperty property="page.inlineScripts"/>

        </body>
    </html>
</security:authorize>
