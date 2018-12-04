<%-- 
    Document   : buscador
    Created on : 4/05/2016, 01:31:54 PM
    Author     : Patricia Benítez
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<head>
    <title>Resultados</title>
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

    <style >
        .dataTables_wrapper table thead,#listado_filter, #listado_info{
            display:none;
        }
    </style>
    <script type="text/javascript" language="javascript" class="init">
        $(document).ready(function () {

            $('.fancybox').fancybox({
                autoSize: true,
                afterClose: function () {
                    reload();
                }
            });

            $('#listado').hide();

            $("#buscar").click(function () {
                reload();
            });

            reload();
        });

        function reload() {
            $('#listado').show();
            if ($.fn.dataTable.isDataTable('#listado')) {
                table = $('#listado').DataTable();
                table.destroy();
            }

            $('#listado').DataTable({
                "bPaginate": false
            });


        }


    </script>
</head>
<div class="container">
    <h1>Resultados de la b&uacute;squeda</h1>

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

    <div class="row main_content">

        <div class="clearfix" >&nbsp;</div>
        <div class="col-sm-12">
            <div class="main-box clearfix">

                <table id="listado" class="table-hover display responsive ">
                    <thead>
                        <tr>
                            <th>Descripci&oacute;n</th>
                            <th></th>
                        </tr>
                    </thead>
                    <s:iterator value="menu">
                        <tr>
                            <td><s:property  value="nombre" /></td>
                            <td>
                                <a id="ver_12" href="<s:property  value="url" />" > 
                                    <span class="fa-stack"><i class="fa fa-square fa-stack-2x"></i> 
                                        <i class="fa fa-link fa-stack-1x fa-inverse"></i>
                                    </span> 
                                </a>
                            </td>
                        </tr>
                    </s:iterator>
                </table>

            </div>
        </div>
    </div>
</div>