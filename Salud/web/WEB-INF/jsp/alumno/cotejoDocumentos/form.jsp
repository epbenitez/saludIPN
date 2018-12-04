<%-- 
    Document   : Formulario de visualización
    Created on : 7/12/2015, 11:26:09 AM
    Author     : Tania G. Sánchez
    Redesing   : Mario Márquez
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Revisión de documentos</title>
</head> 

<content tag="tituloJSP">
    Revisión de documentos
</content>

<body>
    <div class="row">
        <div class="col-xs-12">
            <div class="alert alert-warning">
                <strong>NOTA:</strong> En caso de no haber presentado aún tus documentos en el departamento de becas de tu Unidad Académica, 
                favor de presentarlos lo antes posible para que tu trámite continúe.
            </div>
        </div>
    </div> 
    <div class="row" id="div-tabla">
        <div class="col-sm-12">
            <div class="main-box-body clearfix">                        
                <div class="col-sm-12">
                    <p>El estatus de validación de cada uno de tus documentos es el siguiente:</p>
                </div>                        
            </div>
            <div class="main-box clearfix">                
                <div class="col-sm-12" style="padding: 0px">
                    <div class="responsive">                        
                        <table id="listado" class="table table-striped table-hover dt-responsive" style="width: 100%">
                            <tbody>
                                <tr>
                                    <td class="text-center">
                                        <s:set var="alumno" value="alumno"/>
                                        <s:if test="%{documentos.estudioSocioeconomico}">
                                            <a class="table-link">
                                                <span class="fa-stack">
                                                    <i class="fa fa-square fa-stack-2x"></i>
                                                    <i class="fa fa-check fa-stack-1x fa-inverse"></i> 
                                                </span>
                                            </a>
                                        </s:if>
                                        <s:else>
                                            <a class="table-link" data-toggle="tooltip" data-placement="left" title="No se ha registrado este documento como entregado en tu Unidad Académica"> 
                                                <span style="color:red" class="fa-stack">
                                                    <i class="fa fa-square fa-stack-2x"></i>
                                                    <i class="fa fa-times fa-stack-1x fa-inverse"></i> 
                                                </span>
                                            </a>
                                        </s:else>
                                    </td>   
                                    <td>
                                        
                                        Estudio socioeconómico
                                    </td>
                                </tr>
                                <tr>                                    
                                    <td class="text-center">
                                        <s:if test="%{documentos.cartaCompromiso}">
                                            <a class="table-link">
                                                <span class="fa-stack">
                                                    <i class="fa fa-square fa-stack-2x"></i>
                                                    <i class="fa fa-check fa-stack-1x fa-inverse"></i> 
                                                </span>
                                            </a>
                                        </s:if>
                                        <s:else>
                                            <a class="table-link" data-toggle="tooltip" data-placement="left" title="No se ha registrado este documento como entregado en tu Unidad Académica"> 
                                                <span style="color:red" class="fa-stack">
                                                    <i class="fa fa-square fa-stack-2x"></i>
                                                    <i class="fa fa-times fa-stack-1x fa-inverse"></i> 
                                                </span>
                                            </a>
                                        </s:else>
                                    </td>
                                    <td>
                                        Carta compromiso
                                    </td>
                                </tr>
                                <tr>                                    
                                    <td class="text-center">
                                        <s:if test="%{documentos.curp}">
                                            <a class="table-link">
                                                <span class="fa-stack">
                                                    <i class="fa fa-square fa-stack-2x"></i>
                                                    <i class="fa fa-check fa-stack-1x fa-inverse"></i> 
                                                </span>
                                            </a>
                                        </s:if>
                                        <s:else>
                                            <a class="table-link" data-toggle="tooltip" data-placement="left" title="No se ha registrado este documento como entregado en tu Unidad Académica"> 
                                                <span style="color:red" class="fa-stack">
                                                    <i class="fa fa-square fa-stack-2x"></i>
                                                    <i class="fa fa-times fa-stack-1x fa-inverse"></i> 
                                                </span>
                                            </a>
                                        </s:else>
                                    </td>
                                    <td>
                                        CURP
                                    </td>
                                </tr>
                                <tr>                                    
                                    <td class="text-center">
                                        <s:if test="%{documentos.comprobanteIngresosEgresos}">
                                            <a class="table-link">
                                                <span class="fa-stack">
                                                    <i class="fa fa-square fa-stack-2x"></i>
                                                    <i class="fa fa-check fa-stack-1x fa-inverse"></i> 
                                                </span>
                                            </a>
                                        </s:if>
                                        <s:else>
                                            <a class="table-link" data-toggle="tooltip" data-placement="left" title="No se ha registrado este documento como entregado en tu Unidad Académica"> 
                                                <span style="color:red" class="fa-stack">
                                                    <i class="fa fa-square fa-stack-2x"></i>
                                                    <i class="fa fa-times fa-stack-1x fa-inverse"></i> 
                                                </span>
                                            </a>
                                        </s:else>
                                    </td>
                                    <td>
                                        Comprobante de ingresos y egresos
                                    </td>
                                </tr>
                                <tr>                                    
                                    <td class="text-center">
                                        <s:if test="%{documentos.acuseSubes}">
                                            <a class="table-link">
                                                <span class="fa-stack">
                                                    <i class="fa fa-square fa-stack-2x"></i>
                                                    <i class="fa fa-check fa-stack-1x fa-inverse"></i> 
                                                </span>
                                            </a>
                                        </s:if>
                                        <s:else>
                                            <a class="table-link" data-toggle="tooltip" data-placement="left" title="No se ha registrado este documento como entregado en tu Unidad Académica"> 
                                                <span style="color:red" class="fa-stack">
                                                    <i class="fa fa-square fa-stack-2x"></i>
                                                    <i class="fa fa-times fa-stack-1x fa-inverse"></i> 
                                                </span>
                                            </a>
                                        </s:else>
                                    </td>
                                    <td>
                                        Acuse del SUBES
                                        <br>(Opcional para los alumnos de beca Manutención)
                                    </td>
                                </tr>
                                <tr>                                    
                                    <td class="text-center">
                                        <s:if test="%{documentos.folioSubes}">
                                            <a class="table-link">
                                                <span class="fa-stack">
                                                    <i class="fa fa-square fa-stack-2x"></i>
                                                    <i class="fa fa-check fa-stack-1x fa-inverse"></i> 
                                                </span>
                                            </a>
                                        </s:if>
                                        <s:else>
                                            <a class="table-link" data-toggle="tooltip" data-placement="left" title="No se ha registrado este documento como entregado en tu Unidad Académica"> 
                                                <span style="color:red" class="fa-stack">
                                                    <i class="fa fa-square fa-stack-2x"></i>
                                                    <i class="fa fa-times fa-stack-1x fa-inverse"></i> 
                                                </span>
                                            </a>
                                        </s:else>
                                    </td>
                                    <td>
                                        Folio el SUBES
                                        <br>(Opcional para los alumnos de beca Manutención)
                                    </td>
                                </tr>                                
                            </tbody>
                        </table>                        
                    </div>
                </div>
            </div>
        </div>                
    </div>
</body>