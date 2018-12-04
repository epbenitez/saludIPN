<%@taglib prefix="decorator" uri="/WEB-INF/decorators/tld/sitemesh-decorator.tld" %>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@taglib prefix="page" uri="/WEB-INF/decorators/tld/sitemesh-page.tld" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<security:authorize ifNotGranted="ROLE_ANONYMOUS">

    <!DOCTYPE html>
    <html>
        <head>
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

            <style>
                hr{
                    background-color: #eee;
                    border: 0 none;
                    color: #eee;
                    height: 2px;
                    margin:0px;
                    margin-bottom: 30px;
                }
            </style>

        </head>
        <body class="theme-whbl boxed-layout pace-done">
            <div id="theme-wrapper">

                <div id="page-wrapper" class="container">
                    <div class="row">

                        <div id="content-wrapper" style="min-height: 50px !important;">

                            <div class="row" style="opacity: 1;">
                                <div class="col-lg-12">
                                    <div id="content-header" class="clearfix">
                                        <div class="pull-left">
                                            <h1>
                                                <h1><decorator:extractProperty property="page.tituloJSP"/></h1>
                                            </h1>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <decorator:body/>

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