<%-- 
    Document   : lista
    Created on : 14/01/2016, 09:30:49 AM
    Author     : Monserrat Fuentes
--%>

<%@taglib prefix="s" uri="/struts-tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.0//EN" "http://www.w3.org/TR/REC-html40/strict.dtd">
<html>
    <title>Consulta de depósitos</title>
    <head>
        <!-- Add fancyBox main JS and CSS files -->
        <script type="text/javascript" src="/resources/js/fancybox/source/jquery.fancybox.js?v=2.1.5"></script>
        <link rel="stylesheet" type="text/css" href="/resources/js/fancybox/source/jquery.fancybox.css?v=2.1.5" media="screen" />

        <!-- Add Button helper (this is optional) -->
        <link rel="stylesheet" type="text/css" href="/resources/js/fancybox/source/helpers/jquery.fancybox-buttons.css?v=1.0.5" />
        <script type="text/javascript" src="/resources/js/fancybox/source/helpers/jquery.fancybox-buttons.js?v=1.0.5"></script>

        <!-- Add Thumbnail helper (this is optional) -->
        <link rel="stylesheet" type="text/css" href="/resources/js/fancybox/source/helpers/jquery.fancybox-thumbs.css?v=1.0.7" />
        <script type="text/javascript" src="/resources/js/fancybox/source/helpers/jquery.fancybox-thumbs.js?v=1.0.7"></script>

        <!-- Add Media helper (this is optional) -->
        <script type="text/javascript" src="/resources/js/fancybox/source/helpers/jquery.fancybox-media.js?v=1.0.6"></script>
    </head>
    <body>
        <div class="container">
            <h1>Consulta depósitos</h1>
            <div class="row main_content">
                <div class="col-sm-12 main-box">
                    <div class="form-group col-sm-12 ">
                        </br></br>
                        <div class="alert alert-warning">
                            <i class="fa fa-warning fa-fw fa-lg"></i>
                            <ul class="errorMessage">
                                <li>
                                    <span>¡No tienes ningun depósito!</span>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>      
            </div>
        </div>  
    </body>
</html>