<%-- 
    Document   : Presupuesto para la Unidad Académica
    Created on : 8/12/2015, 11:20:13 AM
    Author     : Victor Lozano
    Redesing   : Mario Márquez
--%>

<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>                
    <title>Presupuesto para la Unidad Académica</title>
</head>
    
<content tag="tituloJSP">
    Presupuesto para la Unidad Académica
</content>

<body>
    <div class="row">
        <div class="col-sm-12">            
            <s:if test="hasActionErrors()">
                <div class="row">
                    <div class="col-sm-12">
                        <div class="alert alert-danger">
                            <i class="fa fa-times-circle fa-fw fa-lg"></i>
                            <strong>&iexcl;Error!</strong> <s:actionerror/>
                        </div>
                    </div>
                </div>
            </s:if>
            <s:else>
                <!-- Form -->
                <div class="row">
                    <div class="col-xs-12">
                        <div class="main-box clearfix">
                            <div class="main-box-body">
                                <form class="form-horizontal">
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">  Periodo</label>
                                        <div class="col-sm-10">
                                            <s:textfield 
                                                cssClass="form-control"
                                                name="periodo" 
                                                readonly="true"
                                                />
                                        </div>
                                    </div>
                                    <security:authorize ifNotGranted="ROLE_RESPONSABLE_UA">
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label"> Unidad Académica </label>
                                            <div class="col-sm-10">
                                                <s:select id="unidadesAcademicas"
                                                          cssClass="form-control"
                                                          name="unidadAcademicaId"
                                                          list="unidadesAcademicas" 
                                                          listKey="id" 
                                                          listValue="nombreCorto"/>
                                                <span class="help-block" id="tipoBecaPeriodoMessage" />
                                            </div>
                                        </div>
                                    </security:authorize>
                                    <security:authorize ifAnyGranted="ROLE_RESPONSABLE_UA">
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label"> Unidad Académica </label>
                                            <div class="col-sm-10">
                                                <s:textfield 
                                                    id="unidadAcademica"
                                                    cssClass="form-control"
                                                    name="UA.nombreCorto" 
                                                    readonly="true"
                                                    />
                                            </div>
                                        </div>
                                    </security:authorize>
                                    <div class="clearfix" >&nbsp;</div>
                                </form>                           
                            </div>                                    
                        </div>   
                    </div>
                </div> <!-- Termina Form -->
                <!-- Tabla -->
                <div class="row" id="div-tabla">
                    <div class="col-xs-12">
                        <div class="main-box clearfix">
                            <div class="col-xs-12" style="padding: 0px">
                                <div class="responsive">                        
                                    <table id="tabla" class="table table-striped table-hover dt-responsive" style="width: 100%">
                                        <thead>
                                            <tr>
                                                <th>Tipo de beca</th>                                                            
                                                <th>Monto presupuestado</th>
                                                <th>Monto ejercido</th>                                                            
                                                <th>Monto por beca</th>
                                                <th>Becas disponibles</th>
                                            </tr>
                                        </thead>                                        
                                    </table>                        
                                </div>
                            </div>
                        </div>
                    </div>                
                </div> <!--Termina tabla -->               
            </s:else>
        </div>
    </div>    
</body>

<content tag="endScripts">
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js" data-init="true" data-ajax="/ajax/listadoVWPresupuestoUAAjax.action?id_ua=<security:authorize ifNotGranted="ROLE_RESPONSABLE_UA"><s:property value="unidadesAcademicas[0].id"/></security:authorize><security:authorize ifAnyGranted="ROLE_RESPONSABLE_UA"><s:property value="UA.id"/></security:authorize>" data-div="div-tabla"></script>
</content>

<content tag="inlineScripts">
    <script type="text/javascript" language="javascript">
        
        $(document).ready(function () {            
            var id_ua = $('#unidadAcademica').val();
            
            if (!id_ua) {
                $('#unidadesAcademicas').on('change', function () {
                    reload();
                });
            }
            
            // Cuando la tabla termina de dibujar, se actualiza el tamaño
            // del fancybox
            $('#div-tabla').on('draw.dt', function () {
                parent.jQuery.fancybox.update();
            });
        });

        function reload() {
            var id_uas = $('#unidadesAcademicas option:selected').val();
            var url = '/ajax/listadoVWPresupuestoUAAjax.action?id_ua=' + id_uas;
            generarTabla("div-tabla", url, null, false);
        }
    </script>  
</content>