<%-- 
    Document   : Template SIBEC
    Created on : 28-Septiembre-2016
    Author     : Gustavo Adolfo Alamillo Medina
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Bitácora de procesos</title>
    <link rel="stylesheet" type="text/css" href="/vendors/datatables/datatables.min.css">
    <link rel="stylesheet" type="text/css" href="/resources/css/customTables.min.css" />
</head> 

<content tag="tituloJSP">
    Bitácora de procesos
</content>
<!--cambiar el formato
ordenarlo desde la query-->
<body>

    <div class="row" id="bitacoraRow">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <div class="col-md-12" style="padding: 0px">
                    <div class="responsive">                        
                        <table id="bitacoraTable" class="table table-striped table-hover dt-responsive" style="width: 100%">
                            <thead>
                                <tr>
                                    <th>Proceso</th>
                                    <th>Estatus</th>
                                    <th>Usuario</th>
                                    <th data-orderable="true">Fecha</th>
                                </tr>
                            </thead>
                            <s:iterator value="bitacoraEstatusProcesosList" status="procesoListStatus">
                                <tr>
                                    <td><s:property value="proceso.tipoProceso.nombre"/>&nbsp;</td>
                                    <td><s:property value="procesoEstatus.nombre"/>&nbsp;</td>
                                    <td><s:property value="usuario.usuario"/>&nbsp;</td>
                                    <td><s:date name="fechamodificacion" format="yyyy/MM/dd" /></td>
                                </tr>
                            </s:iterator>
                        </table>                        
                    </div>
                </div>
            </div>
        </div>                
    </div>
</body>

<content tag="endScripts">
    <script type="text/javascript" src="/vendors/datatables/datatables.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>
        $(document).ready(function () {
            $("#bitacoraTable").dataTable({
                "dom": "t",
                "order": [[3, "desc"]]
            });
        });
    </script>
</content>