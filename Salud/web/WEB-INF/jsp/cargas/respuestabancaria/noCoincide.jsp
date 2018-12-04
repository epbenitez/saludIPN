<%-- 
    Document   : noCoincide
    Created on : 29/03/2016, 01:18:34 PM
    Author     : Tania G. Sánchez
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<head>
    <title>Carga de respuestas bancarias</title>
    <!-- this page specific styles -->
    <link rel="stylesheet" type="text/css" href="/css/compiled/wizard.css">

    <link rel="stylesheet" type="text/css" href="/css/compiled/theme_styles.css" />
    <link rel="stylesheet" type="text/css" href="/css/bootstrap/bootstrap.min.css" />

    <script type="text/javascript" src="/bootstrap-validator/js/jquery.min.js"></script>
    <script type="text/javascript" src="/bootstrap-validator/js/bootstrap.min.js"></script>
    <!-- bootstrap validator -->
    <!--    <link rel="stylesheet" type="text/css" href="/css/bootstrap/bootstrap.css" /> -->
    <link rel="stylesheet" href="/bootstrap-validator/css/bootstrapValidator.css"/>

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


    <link rel="stylesheet" type="text/css" href="/css/libs/font-awesome.css" />

    <link rel="stylesheet" type="text/css" href="/dataTables/media/css/jquery.dataTables.css">
    <link rel="stylesheet" type="text/css" href="/dataTables/resources/syntax/shCore.css">
    <script type="text/javascript" language="javascript" src="/js/jquery-1.11.3.min.js"></script>
    <script type="text/javascript" language="javascript" src="/dataTables/media/js/jquery.dataTables.js"></script>
    <script type="text/javascript" language="javascript" src="/dataTables/resources/syntax/shCore.js"></script>

    <!--<link rel="stylesheet" type="text/css" href="/dataTables/buttons/1.1.0/css/jquery.dataTables.min.css" />-->
    <link rel="stylesheet" type="text/css" href="/dataTables/buttons/1.1.0/css/buttons.dataTables.min.css" />
    <script type="text/javascript" src="/dataTables/buttons/1.1.0/js/dataTables.buttons.min.js"></script>
    <script type="text/javascript" src="/dataTables/jszip/2.5.0/jszip.min.js"></script>
    <script type="text/javascript" src="/dataTables/pdfmake/0.1.18/build/pdfmake.min.js"></script>
    <script type="text/javascript" src="/dataTables/pdfmake/0.1.18/build/vfs_fonts.js"></script>
    <script type="text/javascript" src="/dataTables/buttons/1.1.0/js/buttons.html5.min.js"></script>
    <script type="text/javascript" src="/dataTables/buttons/1.1.0/js/buttons.print.min.js"></script>

    <!-- Responsive DataTables-->
    <!--        <link href="/dataTables/responsive/responsive.dataTables.min.css" rel="stylesheet" type="text/css"/>
            <link href="/dataTables/buttons/1.1.0/css/jquery.dataTables.min.css" rel="stylesheet" type="text/css"/>-->
    <script src="/dataTables/responsive/dataTables.responsive.min.js" type="text/javascript"></script>

    <script type="text/javascript" language="javascript" class="init">
        $(document).ready(function () {
            $('#listado').hide();
            reload();
        });

        function reload() {
            $('#listado').show();
            if ($.fn.dataTable.isDataTable('#listado')) {
                table = $('#listado').DataTable();
                table.destroy();
            }

            var url = '/ajax/detalleDepositoNoCoincideAjax.action';
            var ordenId = $('#ordenId').val();
            url = url + "?ordenId=" + ordenId;

            $('#listado').DataTable({
                "ajax": url
            });
        }
    </script>
  
</head>
<body class=" theme-whbl">
    <div class="col-sm-12">
        <div class="clearfix">
            <h1>Depósitos sin coincidencias</h1>
            <div class="clearfix">&nbsp;</div>  
            <div class="col-md-12">
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
        <div class="col-sm-12">
            <div class="responsive-table table-responsive main-box clearfix">
                <s:hidden name="ordenId" />
                <table id="listado" class="table table-striped table-hover">
                    <thead>
                        <tr>
                            <th>Número de boleta</th>
                            <th>Número de tarjeta bancaria</th>
                            <th>Monto</th>
                            <th>Estatus</th>
                        </tr>
                    </thead>
                </table>
            </div>
        </div>
    </div>
</body>