<%-- 
    Document   : form
    Created on : 27/10/2015, 02:09:23 PM
    Author     : Victor Lozano
    Redisign   : Gustavo Alamillo Medina
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Variables del sistema</title>
</head> 

<content tag="tituloJSP">
    Variables del sistema
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

    <div class="row" id="rowTabla" style="display: none;">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <div class="col-md-12" style="padding: 0px">
                    <div class="responsive">                        
                        <table id="tablaVariables" class="table table-striped table-hover dt-responsive" style="width: 100%">
                            <thead>
                                <tr>
                                    <th>Variable</th>
                                    <th>Descripción</th>
                                    <th>Valor</th>
                                    <th data-center="true"></th>
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
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js" 
    data-init="true" data-ajax="/ajax/listadoConfiguracionAjax.action" data-div="rowTabla"></script>
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>

        function cargarTabla() {
            generarTabla("rowTabla", "/ajax/listadoConfiguracionAjax.action");
        }

        function editar(id) {
            $('#' + id + '_1').hide();
            $('#' + id + '_3').hide();
            $('#' + id + '_5').hide();
            $('#' + id + '_2').show();
            $('#' + id + '_4').show();
            $('#' + id + '_6').show();
        }

        function guardar(id) {
            $.ajax({
                type: 'POST',
                url: '/ajax/guardaConfiguracionAjax.action',
                dataType: 'json',
                data: {id: id, tip: $('#' + id + '_2_1').val(), val: $('#' + id + '_4_1').val()},
                cache: false,
                success: function () {
                    cargarTabla();
                },
                error: function () {
                    ModalGenerator.notificacion({
                        "titulo": "Ocurrio un problema",
                        "cuerpo": "Hubo un problema que impidió que se completara la operación",
                        "tipo": "ALERT"
                    });
                }
            });

            $('#' + id + '_1').show();
            $('#' + id + '_3').show();
            $('#' + id + '_5').show();

            $('#' + id + '_2').hide();
            $('#' + id + '_4').hide();
            $('#' + id + '_6').hide();
            return false;
        }

    </script>
</content>