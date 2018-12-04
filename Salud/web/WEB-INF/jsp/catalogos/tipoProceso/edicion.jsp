<%-- 
    Document   : form
    Created on : 29/10/2015, 12:37:37 PM
    Author     : Usre-05
    Redisign   : Gustavo Adolfo Alamillo Medina
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Editar proceso</title>
    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css" />
</head> 

<content tag="tituloJSP">
    Editar proceso
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

    <div class="row">
        <div class="col-xs-12">
            <div class="main-box clearfix" >
                <div class="main-box-body ">
                    <form id="proceso" action="/catalogos/guardaTipoProceso.action" method="post"  class="form-horizontal"
                          data-bv-feedbackicons-valid="glyphicon glyphicon-ok"
                          data-bv-feedbackicons-invalid="glyphicon glyphicon-remove"
                          data-bv-feedbackicons-validating="glyphicon glyphicon-refresh">
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Nombre</label>
                            <div class="col-sm-10">
                                <s:textfield cssClass="form-control" name="proceso.nombre" id="nombre" placeholder="Nombre"
                                             data-bv-message="Este dato no es válido"
                                             required="true" 
                                             data-bv-notempty="true"
                                             data-bv-notempty-message="El nombre es requerido"
                                             pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s1234567890]+$" 
                                             data-bv-regexp-message="No se permiten caracteres especiales"
                                             data-bv-stringlength="true" 
                                             data-bv-stringlength-min="3" 
                                             data-bv-stringlength-max="50" 
                                             data-bv-stringlength-message="El nombre debe tener de 3 a 50 caracteres"
                                             />
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-2 control-label">Movimiento</label>
                            <div class="col-sm-10">
                                <s:select id="movimiento"  
                                          cssClass="form-control"
                                          name="proceso.movimiento.id"
                                          list="ambiente.movimientoList" 
                                          listKey="id" 
                                          listValue="nombre" 
                                          headerKey=""
                                          data-bv-notempty="true"
                                          data-bv-notempty-message="El tipo de movimiento es requerido"
                                          headerValue="-- Selecciona un movimiento --"/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-2 control-label">Descripcion</label>
                            <div class="col-sm-10">
                                <s:textfield cssClass="form-control" name="proceso.descripcion" id="descripcion" placeholder="Descripcion"
                                             data-bv-message="Este dato no es válido"
                                             required="true" 
                                             data-bv-notempty="true"
                                             data-bv-notempty-message="La descripcion es requerida"
                                             pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s1234567890]+$" 
                                             data-bv-regexp-message="No se permiten caracteres especiales"
                                             data-bv-stringlength="true" 
                                             data-bv-stringlength-min="3" 
                                             data-bv-stringlength-max="300" 
                                             data-bv-stringlength-message="La descripción debe tener de 3 a 300 caracteres."
                                             />
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-12">
                                <s:hidden name="proceso.id" />
                                <button type="submit" class="btn btn-primary pull-right">Guardar proceso</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

</body>

<content tag="endScripts">
    <script type="text/javascript" src="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>
        $(document).ready(function () {
            $('#proceso').bootstrapValidator();
        });
    </script>
</content>