<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Folio Beca Externa</title>
    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css" />
    <link href="/vendors/bootstrap-toggle-master/css/bootstrap2-toggle.min.css" rel="stylesheet">
</head> 

<content tag="tituloJSP">
    Folio Beca Externa
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
            <div class="panel-group accordion" id="accordion">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
                                Instrucciones
                            </a>
                        </h4>
                    </div>
                    <div id="collapseOne" class="panel-collapse collapse">
                        <div class="panel-body">
                            <p>En caso de existir, ingrese el "folio" del documento que avala que el alumno esta dado de baja en la beca externa, de lo contrario deje el campo vacio.</p>
                            <p>Puede dejar algún comentario o una nota.
                        </div>
                    </div>
                </div>
            </div> 
        </div>
    </div>
    
    <div class="row">
        <div class="col-xs-12">
            <div class="main-box clearfix" >
                <div class="main-box-body ">
                    <form id="becaExterna"  method="post" class="form-horizontal"
                          data-bv-feedbackicons-valid="glyphicon glyphicon-ok"
                          data-bv-feedbackicons-invalid="glyphicon glyphicon-remove"
                          data-bv-feedbackicons-validating="glyphicon glyphicon-refresh">
                        <div class="form-group">
                            <div class="col-sm-12">
                                <span><h3 id="cabeza"><%= request.getParameter("curp")%></h3></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Folio Documento</label>
                            <div class="col-sm-10">
                                <s:textfield cssClass="form-control" name="bitacoraOtorgamientoExterno.foliobaja" id="foliobaja" placeholder="Folio"
                                             data-bv-message="Este dato no es válido"
                                             required="false" 
                                             data-bv-notempty="false"
                                             pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ0-9\s-]+$" 
                                             data-bv-regexp-message="El folio sólo puede estar conformado por letras y números"
                                             data-bv-stringlength="true" 
                                             data-bv-stringlength-min="0" 
                                             data-bv-stringlength-max="20" 
                                             data-bv-stringlength-message="El folio debe tener de 1 a 20 caracteres" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Comentario</label>
                            <div class="col-sm-10">
                                <s:textarea cssClass="form-control" name="bitacoraOtorgamientoExterno.comentarios" id="comentarios" placeholder="Comentario"
                                             data-bv-message="Este dato no es válido"
                                             required="false" 
                                             data-bv-notempty="false"
                                             pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ0-9,.\s]+$" 
                                             data-bv-regexp-message="El comentario sólo puede estar conformado por letras, números, comas y puntos"
                                             data-bv-stringlength="true" 
                                             data-bv-stringlength-min="0" 
                                             data-bv-stringlength-max="500" 
                                             data-bv-stringlength-message="La descripcion debe tener máximo 500 caracteres"
                                             />
                            </div>
                        </div>
                            
                        <div class="form-group">
                            <label for="periodoEscolar" class="col-sm-2 control-label">Periodo escolar </label>
                            <div class="col-sm-10">
                            <s:select id="periodoEscolar" name="periodoId"
                                      cssClass="form-control" cssStyle="width:100%"
                                      list="ambiente.periodoList" 
                                      listKey="id" listValue="clave"
                                      name="bitacoraOtorgamientoExterno.periodo.id"
                                      />
                            </div>
                        </div>

                        <div class="form-group">
                                <label for="unidadAcademica" class="col-sm-2 control-label">Unidad Académica</label>
                                <div class="col-sm-10">
                                <s:select cssClass="form-control" cssStyle="width:100%"
                                          id="unidadAcademica" name="uaId"
                                          list="ambiente.unidadAcademicaList" 
                                          listKey="id" listValue="nombreSemiLargo" 
                                          headerKey="" headerValue="-- Todas --"
                                          name="bitacoraOtorgamientoExterno.unidadacademica.id"
                                          />
                            
                                </div> 
                        </div>
                        
                            
                         <div class="form-group">
                            <label class="col-sm-2 control-label">Beca Activa</label>
                            <div class="col-sm-10" onClick="toogle_button()">
                                <input type="checkbox" 
                                data-toggle="toggle" 
                                data-on="No" 
                                data-off="Si" 
                                data-offstyle="primary" 
                                id="switch"
                                name="activo">
                            </div>
                        </div>
                            
                        <div class="form-group">
                            <div class="col-sm-12">
                                <s:hidden name="bitacoraOtorgamientoExterno.fecha" id="fecha"/>
                                <s:hidden name="bitacoraOtorgamientoExterno.usuario.id" id="usuario"/>
                                <s:hidden name="bitacoraOtorgamientoExterno.otorgamientoExterno.id" id="otorExte"/>
                                <s:hidden name="curp" />
                                
                                <button type="submit" class="btn btn-primary pull-right">Guardar</button>
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
    <script type="text/javascript" src="/vendors/bootstrap-toggle-master/js/toogle.js"></script>
</content>

<content tag="inlineScripts">
    <script>
        $(document).ready(function () {
            var act = <%= request.getParameter("activ")%>;
            
            document.getElementById("fecha").value= Date();
            document.getElementById("usuario").value = <%= request.getParameter("usuario")%>;
            document.getElementById("otorExte").value = <%= request.getParameter("otorg")%>;
            
            if((act==0) || (act==null)){
                $('#switch').bootstrapToggle('on');
                document.getElementById("switch").value = "on";
            }else{
                $('#switch').bootstrapToggle('off');
                document.getElementById("switch").value = "off";
            }
            $('#becaExterna').bootstrapValidator();
        });
        
       function toogle_button(){
           var result;
           if(document.getElementById("switch").value == "off"){
               document.getElementById("switch").value = "on";
               result = "false";
           }else{
               document.getElementById("switch").value = "off";
               result = "true";
           }
            document.getElementById("becaExterna").setAttribute("action", "/becasexternas/guardaBecaExterna.action?curp=<%= request.getParameter("curp")%>&usuario=<%= request.getParameter("usuario")%>&otorg=<%= request.getParameter("otorg")%>&activ="+result);
        }
        
    </script>
</content>

 