<%-- 
    Document   : Validación inscripción
    Created on : 28-Septiembre-2016
    Author     : Rafael Cárdenas
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Resumen Validar Inscripción</title>
</head> 

<content tag="tituloJSP">
    Resumen Validar Inscripción
</content>

<body>
    <!-- Ejemplo de caja blanca -->
    <div class="row">   
        <div class="col-xs-6">
            <div class="main-box">
                <div class="main-box-body clearfix">
                    <div class="alert alert-info">
                        <i class="fa fa-info-circle fa-fw fa-lg"></i>
                        <strong>Total</strong> Se intento validar la inscripción de un total de <s:property value="total" /> alumnos.
                    </div>
                    <div class="alert alert-success">
                        <i class="fa fa-check-circle fa-fw fa-lg"></i>
                        <strong>&iexcl;Éxito!</strong> Se valido la inscripcion correctamente de <s:property value="buenos" /> alumnos.
                    </div>
                    <div class="alert alert-warning">
                        <i class="fa fa-warning fa-fw fa-lg"></i>
                        <strong>&iexcl;Atención!</strong> Se encontraron problemas con  <s:property value="malos" /> alumnos.
                    </div>
                </div>
            </div> 
        </div>
        <div class="col-xs-6">
            <div class="main-box">
                <div class="main-box-body clearfix">
                    <header class="main-box-header clearfix">
                        <h4 class="pull-right"><strong>Consideraciones</strong></h4>
                    </header>
                    <br>
                    <p class="text-right">Para concluir con el proceso de Validación de Inscripción, se realizará la pre-asignación de las solicitudes
                        de beca de los alumnos con beca marcada con <strong>validación de inscripción</strong>, lo que facilitará su otorgamiento en el
                        módulo de Asignaciones.<br><br>
                        Al final, podrá observar un resumen con el resultado de la ejecución</p><br>
                    <form id="validar" class="form-horizontal" action="/admin/resumenValidacionInscripcion.action"
                          method="post">
                        <div class="form-group pull-right">
                            <div class="col-sm-12 col-md-12 col-lg-12 ">
                                <input type="submit" id="validar" class="btn btn-primary" value="Paso 2: Preasignar solicitudes"/>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>


    <div class="row" id="contenedor">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <header class="main-box-header clearfix">
                    <h2 class="pull-left">Resumen</h2>
                </header>
                <div class="col-md-12" style="padding: 0px">
                    <div class="responsive">                        
                        <table id="adentrocontendedor" class="table table-striped table-hover dt-responsive" style="width: 100%">
                            <thead>
                                <tr>
                                    <th>Boleta</th>
                                    <th>Error</th>
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
    <script>
        var dataSets = new Array();
        <s:iterator value="resumen">
        dataSets.push(["<s:property value="alumno.boleta"/>", "<s:property value="error"/>"]);
        </s:iterator>
    </script>
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js" data-init="true" data-ajax="dataSets" data-div="contenedor"></script>
</content>

<content tag="inlineScripts">    
</content>