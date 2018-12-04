<%-- 
    Document   : error
    Created on : 19/02/2016, 12:13:25 PM
    Author     : Tania G. Sánchez
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<head>
    <title>Monitoreo de cuentas</title>
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
    <script type="text/javascript" language="javascript" class="init">
        $(document).ready(function () {
            $('.fancybox').fancybox({
                autoSize: true,
                afterClose: function () {
                    reload();
                }
            });
        });
    </script>
</head>
<div class="container">
    <h1>Monitoreo de cuentas</h1>
    <div class="row">
        <div class="col-sm-12">
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
    <div class="col-sm-12 main-box">
        <div class="clearfix" >&nbsp;</div>
        <div class="alert alert-warning">
            <i class="fa fa-warning fa-fw fa-lg"></i> 	
            <span>
                ¡No tienes ninguna tarjeta asignada!
            </span>
        </div>
        <div class="clearfix" >&nbsp;</div>
    </div>
</div>