<%-- 
    Document   : Resultado carga de promedios y semestre
    Created on : 23-Noviembre-2016
    Author     : Gustavo Adolfo Alamillo Medina
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Resultado carga masiva de alumnos</title>
</head> 

<content tag="tituloJSP">
    Resultado carga masiva de alumnos
</content>

<body>

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
    <s:if test="!hasActionErrors()">
        <!-- Ejemplo tabla -->
        <div class="row" id="rowtabla">
            <div class="col-lg-12">
                <div class="main-box clearfix">

                    <div class="responsive">                        
                        <table id="tabla" class="table table-striped table-hover" style="width: 100%">
                            <thead>
                                <tr>
                                    <th data-center="true">#</th>
                                    <th data-center="true">Boleta</th>
                                    <th data-center="true">Promedio anterior</th>
                                    <th data-center="true">Promedio nuevo</th>
                                    <th data-center="true">Semestre anterior</th>
                                    <th data-center="true">Semestre nuevo</th>
                                    <th data-center="true">Estatus de inscripción anterior</th>
                                    <th data-center="true">Estatus de inscripción nuevo</th>
                                    <th data-center="true">Estatus de regularidad anterior</th>
                                    <th data-center="true">Estatus de regularidad nuevo</th>
                                    <th data-center="true">Carga académica anterior</th>
                                    <th data-center="true">Carga académica nueva</th>
                                    <th data-center="true">Resultado</th>
                                </tr>
                            </thead>
                        </table>                        
                    </div>

                </div>
            </div>                
        </div>
        <!--Termina ejemplo tabla --> 
        <div class="row">
            <div class="col-lg-12">
                <div class="main-box ">

                    <div class="table-responsive">
                        <table class="table table-striped table-hover dt-responsive" style="width: 100%">
                            <thead>
                                <tr>
                                    <th class="text-center"><span>Código</span></th>
                                    <th><span>Descripción</span></th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td class="text-center"><span class="label label-warning">AD01</span></td>
                                    <td>Los datos se cargaron igual a los datos que ya se encontraban en el sistema.</td>
                                </tr>
                                <tr>
                                    <td class="text-center"><span class="label label-danger">EE01</span></td>
                                    <td>Error en el formato de la columna.</td>
                                </tr>
                                <tr>
                                    <td class="text-center"><span class="label label-danger">EE02</span></td>
                                    <td>No existe un alumno con ésta boleta.</td>
                                </tr>
                                <tr>
                                    <td class="text-center"><span class="label label-danger">EE03</span></td>
                                    <td>El promedio no es valido.</td>
                                </tr>
                                <tr>
                                    <td class="text-center"><span class="label label-danger">EE04</span></td>
                                    <td>El semestre no es valido para la carrera del alumno.</td>
                                </tr>
                                <tr>
                                    <td class="text-center"><span class="label label-danger">EE05</span></td>
                                    <td> El alumno ya cuenta con un otorgamiento.</td>
                                </tr>
                                <tr>
                                    <td class="text-center"><span class="label label-danger">EE06</span></td>
                                    <td>El estatus de inscripción no es valido.</td>
                                </tr>
                                <tr>
                                    <td class="text-center"><span class="label label-danger">EE07</span></td>
                                    <td>El estatus de regularidad no es valido.</td>
                                </tr>
                                <tr>
                                    <td class="text-center"><span class="label label-danger">EE08</span></td>
                                    <td>El alumno parece no tener sus datos consolidados en la DAE, favor de verificar su carrera.</td>
                                </tr>
                                <tr>
                                    <td class="text-center"><span class="label label-danger">EE09</span></td>
                                    <td>El alumno no cuenta con datos académicos para este periodo.</td>
                                </tr>
                                <security:authorize ifAnyGranted="ROLE_RESPONSABLE_UA">
                                    <tr>
                                        <td class="text-center"><span class="label label-danger">EE10</span></td>
                                        <td>El alumno no pertenece a su Unidad Académica.</td>
                                    </tr> 
                                </security:authorize>
                                <tr>
                                    <td class="text-center"><span class="label label-danger">EE11</span></td>
                                    <td>La carga académica no es válida.</td>
                                </tr>
                            </tbody>

                        </table>
                    </div>

                </div>
            </div>
        </div>
    </s:if>



</body>

<content tag="endScripts">
    <script type="text/javascript" src="/vendors/datatables/datatables.min.js"></script>
    <script>
        var dataSets = new Array();
        <s:iterator value="log" var="lista">
            var innerDataSets = new Array();
            <s:iterator value="lista">
                innerDataSets.push("<s:property escape="false"/>")                
            </s:iterator>
            dataSets.push(innerDataSets);
        </s:iterator>
    </script>
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js" data-scrollX="true" data-init="true" data-ajax="dataSets" data-div="rowtabla" data-function="despuesCargarTabla"></script>
</content>

<content tag="inlineScripts">
    <script>
        function despuesCargarTabla() {
            $('[data-toggle="tooltip"]').tooltip();
        }
    </script>
</content>