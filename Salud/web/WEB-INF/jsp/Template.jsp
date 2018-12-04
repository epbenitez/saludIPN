<%-- 
    Document   : Template SIBEC
    Created on : 28-Septiembre-2016
    Author     : Gustavo Adolfo Alamillo Medina
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title></title>

</head> 

<content tag="tituloDerecho">

</content>

<content tag="tituloJSP">

</content>

<body>

    <!-- Ejemplo de caja blanca -->
    <div class="row">
        <div class="col-xs-12">
            <div class="main-box clearfix" >
                <div class="row">
                    <header class="main-box-header clearfix">
                        <h2 class="pull-left">Título del cuadro blanco</h2>
                    </header>
                    <div class="main-box-body clearfix">
                        <div class="row">
                            <div class="col-md-12">
                                Contenido
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div> <!-- Termina ejemplo caja blanca -->

    <!-- Ejemplo para formularios -->
    <div class="row">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <div class="main-box-body">
                    <form id="busquedaForm" class="form-horizontal"
                          data-bv-feedbackicons-valid="glyphicon glyphicon-ok"
                          data-bv-feedbackicons-invalid="glyphicon glyphicon-remove"
                          data-bv-feedbackicons-validating="glyphicon glyphicon-refresh">
                        <div class="form-group">
                            <label class="col-sm-1 control-label">Boleta</label>
                            <div class="col-sm-11">
                                <s:textfield cssClass="form-control" id="alumnoBoleta" name="numeroDeBoleta" placeholder="Boleta"
                                             data-bv-stringlength="true" 
                                             data-bv-stringlength-min="10" 
                                             data-bv-stringlength-max="10" 
                                             data-bv-stringlength-message="La boleta debe tener 10 caracteres"/>
                            </div>
                        </div> 
                        <div class="form-group">
                            <div class="col-sm-12 col-md-12 col-lg-12">
                                <div class="pull-right">
                                    <input type="submit" id="buscar" class="btn btn-primary" value="Buscar alumno"/>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <!--Termina ejemplo para formularios -->

    <!-- Ejemplo tabla -->
    <div class="row" id="" style="display: none;">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <div class="col-md-12" style="padding: 0px">
                    <div class="responsive">                        
                        <table id="" class="table table-striped table-hover dt-responsive" style="width: 100%">
                            <thead>
                                <tr>
                                    <th></th>
                                    <th></th>
                                </tr>
                            </thead>
                        </table>                        
                    </div>
                </div>
            </div>
        </div>                
    </div>
    <!--Termina ejemplo tabla --> 

    <!--Ejemplo tabla adentro de una tab -->
    <div class="tab-pane fade" id="tab-newsfeed">
        <table id="" class="table table-striped table-hover dt-responsive" style="width: 100%">
            <thead>
                <tr>
                    <th>1</th>
                    <th>2</th>
                </tr>
            </thead>
        </table>    
    </div>

    <!-- Termina ejemplo de tabla adentro de una tab -->



</body>

<content tag="endScripts">

</content>

<content tag="inlineScripts">

</content>