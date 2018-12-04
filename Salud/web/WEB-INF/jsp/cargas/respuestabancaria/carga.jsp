<%-- 
    Document   : carga
    Created on : 16-mar-2016, 16:48:19
    Author     : Tania G. Sánchez
    Redesign   : Gustavo Adolfo Alamillo Medina
--%>

<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Carga de respuestas bancarias</title>
</head> 

<content tag="tituloJSP">
    Carga de respuestas bancarias
</content>

<body>
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
    <s:if test="!referencia">
        <div class="row">
            <div class="col-lg-12">
                <div class="main-box clearfix">
                    <div class="main-box-body clearfix">
                        <div class="table-responsive">

                            <div class="col-lg-3 col-sm-6 col-xs-12">
                                <div class="main-box infographic-box colored green-bg">
                                    <i class="fa fa-star"></i>
                                    <span class="headline">Respuestas Exitosas</span>
                                    <span class="value"><s:property value="exitosos"/></span>
                                </div>
                            </div>
                            <div class="col-lg-3 col-sm-6 col-xs-12">
                                <div class="main-box infographic-box colored red-bg">
                                    <i class="fa fa-star-o"></i>
                                    <span class="headline">Respuestas Rechazadas</span>
                                    <span class="value"><s:property value="rechazados"/></span>
                                </div>
                            </div>
                            <div class="col-lg-3 col-sm-6 col-xs-12">
                                <div class="main-box infographic-box colored purple-bg">
                                    <i class="fa fa-times-circle"></i>
                                    <span class="headline">No Procesadas</span>
                                    <span class="value"><s:property value="noProcesados"/></span>
                                </div>
                            </div>
                            <div class="col-lg-3 col-sm-6 col-xs-12">
                                <div class="main-box infographic-box colored emerald-bg">
                                    <i class="fa fa-envelope"></i>
                                    <span class="headline">Correos Enviados</span>
                                    <span class="value"><s:property value="correosEnviados"/></span>
                                </div>
                            </div>
                            <div class="col-lg-12 col-sm-12 col-xs-12">
                                <div class=" text-right">
                                    <span >Total de respuestas en el archivo: <s:property value="total"/></span>
                                </div>
                            </div>
                            <div class="clearfix" >&nbsp;</div>
                            <s:if test="depositosRechazados!=null">
                                <h3 style="margin-top: 5px; margin-left: 10px">Registros rechazados o no procesados</h3>
                                <table id="listado" class="table table-striped table-hover">
                                    <thead>
                                        <tr>
                                            <th>Unidad Académica</th>
                                            <th>Número de boleta</th>
                                            <th>Nombre</th>
                                            <th>Tipo de beca</th>
                                            <th>Observaciones</th>
                                        </tr>
                                    </thead>
                                    <s:iterator value="depositosRechazados">
                                        <tr>
                                            <td><s:property value="otorgamiento.datosAcademicos.unidadAcademica.nombreCorto" /></td>
                                            <td><s:property value="alumno.boleta"/></td>
                                            <td><s:property value="alumno.fullName" /></td>
                                            <td><s:property value="otorgamiento.tipoBecaPeriodo.tipoBeca.beca.nombre" /></td>
                                            <td><s:property value="observaciones" /></td>
                                        </tr>
                                    </s:iterator>
                                </table>
                            </s:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </s:if>
    <s:if test="referencia">
        <div class="row">
            <div class="col-lg-12">
                <div class="main-box clearfix">
                    <div class="clearfix">&nbsp;</div>   
                    <h3 style="margin-top: 5px; margin-left: 10px">Resultados</h3>
                    <div class="main-box-body clearfix">
                        <div class="table-responsive">
                            <table class="table">
                                <tbody>
                                    <tr class="warning">
                                        <td>Procesados</td>
                                        <td><s:property value="total"/></td>
                                    </tr>
                                    <tr class="success">
                                        <td>Correctos</td>
                                        <td><s:property value="exitosos"/></td>
                                    </tr>
                                    <tr class="info">
                                        <td>Rechazados</td>
                                        <td><s:property value="rechazados"/></td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row" id="rowProcesados" >
            <div class="col-xs-12">
                <div class="col-md-12">                        
                    <table id="tablaProcesados" class="table table-striped" >
                        <thead>
                            <tr>
                                <th>LINEA</th>
                                <th>ESTATUS</th>
                            </tr>
                        </thead>
                    </table>              
                </div>
            </div>                
        </div>
    </s:if>
</body>
<s:if test="referencia">
    <content tag="endScripts">
        <script type="text/javascript" src="/resources/js/generador-tablas.min.js" data-init="true" data-ajax="dataSets" data-div="rowProcesados"></script>
    </content>
    <content tag="inlineScripts">
        <script>
            var dataSets = new Array();
            <s:iterator value="listaCampos">
            dataSets.push(["<s:property value="nombreCompleto"/>", "<s:property value="statProceso"/>"]);
            </s:iterator>
        </script>
    </content>
</s:if>

