<%-- 
    Document   : revalidantes
    Created on : 21/12/2015, 12:50:22 PM
    Author     : Tania G. Sánchez
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<head>
    <title>Asignación automática</title>
    <link rel="stylesheet" type="text/css" href="/dataTables/buttons/1.1.0/css/buttons.dataTables.min.css" />
    <script type="text/javascript" src="/dataTables/buttons/1.1.0/js/dataTables.buttons.min.js"></script>
    <script type="text/javascript" src="/dataTables/jszip/2.5.0/jszip.min.js"></script>
    <script type="text/javascript" src="/dataTables/pdfmake/0.1.18/build/pdfmake.min.js"></script>
    <script type="text/javascript" src="/dataTables/pdfmake/0.1.18/build/vfs_fonts.js"></script>
    <script type="text/javascript" src="/dataTables/buttons/1.1.0/js/buttons.html5.min.js"></script>
    <script type="text/javascript" src="/dataTables/buttons/1.1.0/js/buttons.print.min.js"></script>
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

    <script type="text/javascript">
        $(document).ready(function () {
            $('#listado').DataTable({
                dom: 'Bfrtip',
                lengthMenu: [
                    [10, 25, 50, -1],
                    ['10 registros', '25 registros', '50 registros', 'Todos']
                ],
                buttons: [
                    {
                        extend: 'pageLength',
                        text: 'Tamaño de Página'
                    },
                    'excel'
                ]
            });
        });
    </script>
    <style>
        .dt-buttons{
            margin-top: 10px;
            margin-left: 10px;
        }
    </style>
</head>
<div class="container">
    <h1>Listado del resultado de la asignación automática</h1>
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
            <div class="responsive-table table-responsive main-box clearfix">
                <table id="listado" class="table table-striped table-hover">
                    <thead>
                        <tr>
                            <th >Unidad Académica</th>
                            <th >Tipo de beca</th>
                            <th >No. becarios</th>
                            <th >Presupuesto asignado</th>
                            <th >No. alumnos no asignados</th>
                        </tr>
                    </thead>
                    <s:iterator value="resumen">
                        <tr>
                            <td><s:property  value="unidadAcademica" /></td>
                            <td><s:property  value="tipoDeBeca" /></td>
                            <td><s:property  value="alumnosAsignados" /></td>
                            <td><s:property  value="presupuestoAsignado" /></td>
                            <td><s:property  value="alumnosNoAsignados" /></td>
                        </tr>
                    </s:iterator>
                </table>
            </div>
        </div>
    </div>
</div>