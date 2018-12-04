<%-- 
    Document   : Requisitos de las becas
    Created on : 9/08/2016, 04:07:22 PM
    Author     : Victor Lozano
    Redesing   : Mario Márquez
--%>

<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Requisitos de las becas</title>
</head>

<content tag="tituloJSP">
    Requisitos de las becas
</content>

<body class=" theme-whbl">
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
                                </form>                           
                            </div>                                    
                        </div>   
                    </div>
                </div> <!-- Termina Form -->
                <!-- Tabla -->
                <div class="row" id="div-tabla" style="display: none;">
                    <div class="col-xs-12">
                        <div class="main-box clearfix">
                            <div class="col-xs-12" style="padding: 0px">
                                <div class="responsive">                        
                                    <table id="tabla" class="table table-striped table-hover dt-responsive" style="width: 100%">
                                        <thead>
                                            <tr>
                                                <th>Tipo de beca</th>                                                            
                                                <th>¿Necesario que el alumno sea regular?</th>                                                            
                                                <th>Promedio mínimo</th>
                                                <th>Promedio máximo</th>
                                                <th>Semestre mínimo</th>
                                                <th>Semestre máximo</th>
                                                <th>Ingreso en salarios mínimos</th>
                                                <th>Modalidad</th>
                                                <th>Área</th>
                                                <th>No. de semestres del otorgamiento</th>                    
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
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js" data-init="true" data-div="div-tabla" data-ajax="/ajax/requisitosTipoBecaAjax.action?uaId=<security:authorize ifNotGranted="ROLE_RESPONSABLE_UA"><s:property value="unidadesAcademicas[0].id"/></security:authorize><security:authorize ifAnyGranted="ROLE_RESPONSABLE_UA"><s:property value="UA.id"/></security:authorize>"></script>
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
            var uaId = $('#unidadesAcademicas option:selected').val();
            var url = '/ajax/requisitosTipoBecaAjax.action?uaId=' + uaId;
            generarTabla("div-tabla", url, null, false);
        }
    </script>
</content>