<%-- 
    Document   : editar
    Created on : 6/07/2018, 04:01:57 PM
    Author     : Mario Márquez
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Excluir/Incluir alumno de depósitos</title>
</head> 

<content tag="tituloJSP">
    Excluir/Incluir alumno de depósitos
</content>


<body>
    <!--Inicia mensajes error-->
    <div class="row">
        <div class="col-xs-12">
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
    <!--Termina mensajes error-->

    <div class="row">
        <div class="col-xs-12">
            <div class="panel-group accordion" id="accordion">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
                                Instrucciones
                            </a>
                        </h4>
                    </div>
                </div>
                <div id="collapseOne" class="panel-collapse collapse">
                    <div class="panel-body">
                        <ul>
                            <li>El valor elegido en el campo "Estatus" permitirá que un alumno sea visible, o no, por el módulo de generación de órdenes de depósito. Si se selecciona "Excluir", el alumno no será elegible en una dispersión.</li>
                            <li>El campo "Identificador de otorgamiento" permitirá establecer una razón para la exclusión de un otorgamiento, facilitando el seguimiento del estatus del alumno.</li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div> 
    <div class="row">
        <div class="col-xs-12">
            <div class="main-box clearfix">
                <div class="row">
                    <div class="main-box-body">
                        <s:form id="editarEstatusForm" name="editarEstatusForm" method="POST" enctype="multipart/form-data" cssClass="form-horizontal" action="guardarExcluirAlumnosDeposito.action">
                            <div class="form-group" >
                                <label class="col-xs-3 control-label col-sm-offset-1">Estatus</label>
                                <div class="col-sm-5">
                                    <select id="estatus-select" name="estatusOption" class="form-control" disabled>
                                        <option value="0">Incluído</option>
                                        <option value="1">Excluido</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group" >
                                <label class="col-xs-3 control-label col-sm-offset-1">Identificador de otorgamiento</label>
                                <div class="col-sm-5">
                                    <s:select id="select-identificador" name="idOtorgamientoId" cssClass="form-control"
                                              list="idsOtorgamientos" 
                                              listKey="id" listValue="nombre" 
                                              data-bv-notempty="true" 
                                              data-bv-notempty-message="Es necesario seleccione un identificador."
                                              required="true"
                                              disabled="true"
                                              />
                                </div>
                            </div>
                            <div class="form-group col-sm-9">
                                <s:hidden name="idOtorgamiento" />
                                <s:submit type="button" value="Guardar" cssClass="btn btn-primary pull-right" id="btn-enviar" />
                            </div>
                        </s:form>
                    </div>
                </div>
            </div>
        </div>
    </div> <!-- Termina Form -->
</body>

<content tag="endScripts">
</content>

<content tag="inlineScripts">
    <script>
        $(document).ready(function () {
            $("#estatus-select").val("<s:property value='estatusOption' />");
            $("#estatus-select").prop('disabled', false);
            $("#select-identificador").prop('disabled', false);
        });
    </script>
</content>