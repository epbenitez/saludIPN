<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Bitacora Beca Externa</title>
    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css" />
    <link href="/vendors/bootstrap-toggle-master/css/bootstrap2-toggle.min.css" rel="stylesheet">
</head> 

<content tag="tituloJSP">
    Bitácora Beca Externa
</content>

<body>
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
    
    <div class="row" id="div-tabla" style="display: none;">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <div class="col-md-12" style="padding: 0px">
                    <div class="responsive">                        
                        <table id="bitac" class="table table-striped table-hover dt-responsive" style="width: 100%">
                            <thead>
                                <tr>
                                    <th data-orderable="true">Folio Baja</th>
                                    <th data-orderable="true">Unidad Académica</th>
                                    <th data-orderable="true">Periodo</th>
                                    <th data-center="true">Comentarios</th>
                                    <th data-center="true">Usuario</th>
                                    <th data-orderable="true">Fecha</th>
                                </tr>
                            </thead>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>


<content tag="endScripts">
    <script type="text/javascript" src="/vendors/fancy-box/jquery.fancybox.min.js"></script>
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js" data-init="true"  
    data-div="div-tabla" data-ajax="/ajax/bitacBecaExterna.action?otorextgid=<%= request.getParameter("otorextgid")%>" data-function="despuesCargarTabla"></script>
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>
        function despuesCargarTabla() {
            $('.fancybox').fancybox({
                autoSize: true,
                afterClose: function () {
                    reload();
                }
            });
        }

        function reload() {
            var url = '/ajax/bitacBecaExterna.action';
            generarTabla("div-tabla", url, despuesCargarTabla, false);
        }
        
    </script>
</content>

 