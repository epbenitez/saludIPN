<%-- 
    Document   : Formulario de Nuevo
    Created on : 28/10/2015, 12:49:33 PM
    Author     : Tania G. Sánchez
    Redesign   : Gustavo Adolfo Alamillo Medina
--%>


<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Periodo</title>
    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css" />
    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap-datepicker/css/bootstrap-datepicker.min.css" />
</head> 

<content tag="tituloJSP">
    Periodo
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
                    <form id="periodo" action="/catalogos/guardaPeriodo.action" method="post" class="form-horizontal">
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Clave</label>
                            <div class="col-sm-10">
                                <s:textfield cssClass="form-control" name="periodo.clave" id="clave" placeholder="Clave"
                                             data-bv-message="Este dato no es válido"
                                             required="true" 
                                             data-bv-notempty="true"
                                             data-bv-notempty-message="La clave es requerido"
                                             pattern="^[2][0][0-9][0-9][-][12]$" 
                                             data-bv-stringlength-max="6"
                                             data-bv-regexp-message="El formato de la clave es 20XX-X"
                                             data-bv-stringlength="true" 
                                             data-bv-stringlength-message="El formato de la clave es 20XX-X"
                                             />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Descripcion</label>
                            <div class="col-sm-10">
                                <s:textfield cssClass="form-control" name="periodo.descripcion" id="descripcion" placeholder="Descripcion"
                                             data-bv-message="Este dato no es válido"
                                             required="true" 
                                             data-bv-notempty="true"
                                             data-bv-notempty-message="La descripcion es requerido"
                                             pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ0-9-\s]+$" 
                                             data-bv-regexp-message="La descripcion sólo puede estar conformado por letras y números"
                                             data-bv-stringlength="true" 
                                             data-bv-stringlength-min="4" 
                                             data-bv-stringlength-max="50" 
                                             data-bv-stringlength-message="La descripcion debe tener mínimo 4 caracteres"
                                             />
                            </div>
                        </div>     
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Fecha inicial</label>
                            <div class="col-sm-10">
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                    <input type="text" class="form-control" id="fechaInicial" name="fechaInicial"
                                           value="<s:date name="periodo.fechaInicial" format="dd/MM/yyyy" />"
                                           data-bv-notempty="true"
                                           data-bv-message="Este dato no es válido"
                                           required="true" 
                                           placeholder="Fecha inicial"
                                           onkeydown="return false">
                                </div>
                                <span class="help-block">Formato dd/mm/yyyy</span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-2 control-label">Fecha final</label>
                            <div class="col-sm-10">
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                    <input type="text" class="form-control" id="fechaFinal" name="fechaFinal"
                                           value="<s:date name="periodo.fechaFinal" format="dd/MM/yyyy" />"
                                           data-bv-notempty="true"
                                           data-bv-message="Este dato no es válido"
                                           required="true" 
                                           placeholder="Fecha final"
                                           onkeydown="return false">
                                </div>
                                <span class="help-block">Formato dd/mm/yyyy</span>
                            </div>
                        </div>              

                        <div class="form-group">
                            <label class="col-sm-2 control-label">Periodo anterior</label>
                            <div class="col-sm-10">
                                <s:select id= "periodoAnterior" name="periodo.periodoAnterior.id" list="ambiente.periodoList"
                                          cssClass="form-control"
                                          listKey="id" listValue="clave"
                                          headerKey="" data-bv-notempty="true"
                                          headerValue="-- Seleccione un periodo --" />
                            </div>
                        </div> 

                        <div class="form-group">
                            <label class="col-sm-2 control-label">Ciclo Escolar</label>
                            <div class="col-sm-10">
                                <s:select id= "cicloEscolar" name="periodo.cicloEscolar.id" list="ambiente.cicloList"
                                          cssClass="form-control"
                                          listKey="id" listValue="nombre"
                                          headerKey="" data-bv-notempty="true"
                                          headerValue="-- Seleccione un ciclo escolar --" />
                            </div>
                        </div> 

                        <div class="form-group">
                            <div class="col-sm-12">
                                <s:hidden name="periodo.id"/>
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
    <script type="text/javascript" src="/vendors/moment/moment.min.js"></script>
    <script type="text/javascript" src="/vendors/bootstrap-datepicker/js/bootstrap-datepicker.min.js"></script>
    <script type="text/javascript" src="/vendors/bootstrap-datepicker/locales/bootstrap-datepicker.es.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>

                                               $(document).ready(function () {
                                                   $('#periodo').bootstrapValidator();
                                                   $('#fechaInicial, #fechaFinal').val(crearFecha());
                                               });

                                               $(function () {
                                                   $('#fechaInicial, #fechaFinal').datepicker({
                                                       format: 'dd/mm/yyyy', language: 'es'
                                                   });
                                               });

                                               function crearFecha() {
                                                   var now = new Date();
                                                   var day = ("0" + now.getDate()).slice(-2);
                                                   var month = ("0" + (now.getMonth() + 1)).slice(-2);
                                                   var today = day + "/" + (month) + "/" + now.getFullYear();

                                                   return today;
                                               }
    </script>
</content>