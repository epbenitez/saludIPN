<%-- 
    Document   : datosBANCARIOS
    Created on : 25/01/2017
    Author     : JLRM
--%>

<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Datos Bancarios</title>
    <style>
        .profile-img{
            border: 0px!important; 
        }

    </style>
</head>
<content tag="tituloJSP">
    Datos Bancarios
</content>

<body>
    <div class="row">
        <div class="col-xs-12">

            <!-- Trigger the modal with a button 
            <button type="button" class="btn btn-warning" data-toggle="modal" data-target="#myModal" style="float: right">Diagrama Estados</button> -->

            <!-- Modal -->
            <div class="modal" id="myModal" role="dialog">
                <div class="modal-dialog modal-lg">

                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title" style="color: #0082ac">Diagrama de estatus de cuentas</h4>
                        </div>
                        <div class="modal-body">
                            <img src="/resources/img/estatus_tarjetas/diagrama_estatus_tarjetas.png" class="img-fluid img-responsive" alt="Responsive image" style="padding-left: 4%; padding-right: 4%">
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-primary" data-dismiss="modal">Cerrar</button>
                        </div>
                    </div>
                </div>
            </div>

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

    <div class="row ">
        <div class="col-xs-12">
            <form id="darBajaDiversasForm" name="darBajaDiversasForm" action="/misdatos/guardaDAEPermisoCambio.action" method="post" class="form-horizontal">
                <div class="clearfix" >&nbsp;</div>
                <div class="main-box clearfix profile-box-stats">
                    <div class="main-box-body clearfix">
                        <div class="profile-box-header emerald-bg clearfix">
                            <h2>
                                <s:property value="estatusSolicitud" ></s:property> 
                                </h2>
                                <div class="job-position">
                                <s:property value="observSolicitud" ></s:property> 
                                </div>
                                <div class="infographic-box profile-img img-responsive">
                                <s:if test="stsId == 13">
                                    <i class="fa fa-thumbs-o-up green-bg"></i>
                                </s:if>
                                <s:elseif test="stsId == 14">
                                    <i class="fa fa-thumbs-o-down red-bg"></i>
                                </s:elseif>
                                <s:else>
                                    <i class="fa fa-exclamation yellow-bg"></i>
                                </s:else>
                            </div>
                        </div>

                        <div class="profile-box-footer clearfix">                            
                            <s:if test="menorEdad">
                                <div class="col-sm-12 form-group">
                                    <label class="col-sm-3 control-label">Datos del tutor</label>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label">Nombre(s)</label>
                                    <div class="col-sm-8">
                                        <s:textfield cssClass="form-control" name="alumnoDatosBanco.nombre" id="nombreTutor" placeholder="Nombre(s)"
                                                     data-bv-message="Este dato no es válido."
                                                     required="true" 
                                                     data-bv-notempty="true"
                                                     readonly="true"
                                                     data-bv-notempty-message="El nombre es requerido."
                                                     pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s]+$" 
                                                     data-bv-regexp-message="El nombre sólo puede estar conformado por letras."
                                                     data-bv-stringlength="true" 
                                                     data-bv-stringlength-min="1" 
                                                     data-bv-stringlength-max="25" 
                                                     data-bv-stringlength-message="El nombre debe tener mínimo 1 letra."/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label">Apellidos</label>
                                    <div class="col-sm-4">
                                        <s:textfield cssClass="form-control" name="alumnoDatosBanco.apellidoPaterno" id="apellidoPaternoTutor" placeholder="Apellido paterno"
                                                     data-bv-message="Este dato no es válido."
                                                     required="true" 
                                                     data-bv-notempty="true"
                                                     readonly="true"
                                                     data-bv-notempty-message="El apellido paterno es requerido."
                                                     pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s]+$" 
                                                     data-bv-regexp-message="El apellido paterno sólo puede estar conformado por letras."
                                                     data-bv-stringlength="true" 
                                                     data-bv-stringlength-min="1" 
                                                     data-bv-stringlength-max="25" 
                                                     data-bv-stringlength-message="El apellido paterno debe tener mínimo 1 letra."/>
                                    </div>
                                    <div class="col-sm-4">
                                        <s:textfield cssClass="form-control" name="alumnoDatosBanco.apellidoMaterno" id="apellidoMaternoTutor" placeholder="Apellido materno"
                                                     data-bv-message="Este dato no es válido."
                                                     readonly="true"
                                                     pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s]+$" 
                                                     data-bv-regexp-message="El apellido materno sólo puede estar conformado por letras."
                                                     data-bv-stringlength="true" 
                                                     data-bv-stringlength-min="1" 
                                                     data-bv-stringlength-max="25" 
                                                     data-bv-stringlength-message="El apellido materno debe tener mínimo 1 letra."/>
                                    </div>
                                </div> 
                                <div class="form-group">
                                    <label class="col-sm-3 control-label">Fecha de nacimiento</label>
                                    <div class="col-sm-8">
                                        <div class="input-group">
                                            <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                            <input type="text" class="form-control" id="fechaDeNacimientoTutor" name="fechaNacimientoTutor"
                                                   value="<s:date name="alumnoDatosBanco.fechaDeNacimiento" format="dd-MM-yyyy" />"
                                                   data-bv-notempty="true"
                                                   readonly="true"
                                                   data-bv-message="Este dato no es válido"
                                                   onkeydown="return false"
                                                   required="true">
                                        </div>
                                        <span class="help-block">Formato dd-mm-yyyy</span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label">Correo electrónico</label>
                                    <div class="col-sm-8">
                                        <s:textfield name="alumnoDatosBanco.correoElectronico" id="correoElectronicoTutor" placeholder="ejemplo@gmail.com"
                                                     cssClass="form-control"
                                                     data-bv-emailaddress="true"
                                                     data-bv-emailaddress-message="El correo electrónico que proporcionaste no parece ser válido. Por favor, verifica."
                                                     data-bv-message="El correo electrónico que proporcionaste no parece ser válido. Por favor, verifica."
                                                     required="true" 
                                                     data-bv-regexp-message="El correo no es válido"
                                                     data-bv-notempty="true"
                                                     readonly="true"
                                                     data-bv-notempty-message="El correo electrónico es requerido"
                                                     data-bv-stringlength="true" 
                                                     data-bv-stringlength-min="6" 
                                                     data-bv-stringlength-max="60" 
                                                     data-bv-stringlength-message="El correo electrónico debe ser mínimo de 6 caracteres y máximo 60 caracteres"/>
                                        <span class="help-block" id="nombreMessage" />
                                    </div>
                                </div> 
                                <div class="form-group">
                                    <label class="col-sm-3 control-label">Ocupación</label>
                                    <div class="col-sm-8">
                                        <s:textfield cssClass="form-control" name="alumnoDatosBanco.ocupacion" id="ocupacionTutor" placeholder="Ocupación"
                                                     data-bv-message="Este dato no es válido."
                                                     required="true" 
                                                     data-bv-notempty="true"
                                                     readonly="true"
                                                     data-bv-notempty-message="La ocupación es requerido."
                                                     pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s]+$" 
                                                     data-bv-regexp-message="La ocupación sólo puede estar conformado por letras."
                                                     data-bv-stringlength="true" 
                                                     data-bv-stringlength-min="1" 
                                                     data-bv-stringlength-max="25" 
                                                     data-bv-stringlength-message="La ocupación debe tener mínimo 1 letra."/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label">Estado civil</label>
                                    <div class="col-sm-8">
                                        <s:select id="civilTutor" cssClass="form-control"
                                                  list="ambiente.estadoCivil" listValue="nombre" listKey="id" headerKey=""
                                                  headerValue="-- Selecciona tu estado civil --"
                                                  name="alumnoDatosBanco.estadoCivil.id"
                                                  data-bv-notempty="true"
                                                  readonly="true"
                                                  data-bv-notempty-message="El estado civil es requerido" />
                                        <span class="help-block" id="civilMessage" />
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label">Género</label>
                                    <div class="col-sm-8">
                                        <s:select id="generoTutor" cssClass="form-control"
                                                  list="ambiente.generoList" listValue="nombre" listKey="id" headerKey=""
                                                  headerValue="-- Selecciona  --"
                                                  name="alumnoDatosBanco.genero.id"
                                                  data-bv-notempty="true"
                                                  readonly="true"
                                                  data-bv-notempty-message="El género es requerido"/>
                                        <span class="help-block" id="generoMessage"/>
                                    </div>
                                </div>
                            </s:if> 
                            <div class="form-group">
                                <label class="col-sm-3 control-label">Calle</label>
                                <div class="col-sm-8">
                                    <s:textfield cssClass="form-control" name="alumnoDatosBanco.calle" id="calleBanco" placeholder="Calle"
                                                 data-bv-message="Este dato no es válido."
                                                 required="true" 
                                                 data-bv-notempty="true"
                                                 data-bv-notempty-message="La calle es requerida."
                                                 pattern="^[-a-zA-ZáéíóúñÁÉÍÓÚÑ0-9.#/_\s]+$"
                                                 data-bv-regexp-message="Tu calle debe estar conformado por letras y/o números."
                                                 data-bv-stringlength="true" 
                                                 readonly="true"
                                                 data-bv-stringlength-min="1" 
                                                 data-bv-stringlength-max="100" 
                                                 data-bv-stringlength-message="Tu calle debe ser de al menos 1 caracter." />
                                    <span class="help-block" id="nombreMessage" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">Número</label>
                                <div class="col-sm-4">
                                    <s:textfield cssClass="form-control" name="alumnoDatosBanco.numeroExterior" id="numeroExteriorBanco" placeholder="Número Exterior"
                                                 data-bv-message="Este dato no es válido."
                                                 required="true" 
                                                 data-bv-notempty="true"
                                                 readonly="true"
                                                 data-bv-notempty-message="El número exterior es requerido."
                                                 pattern="^[-a-zA-ZáéíóúñÁÉÍÓÚÑ0-9.#/_\s]+$"
                                                 data-bv-regexp-message="El número exterior sólo puede estar conformado por letras y/o números."
                                                 data-bv-stringlength="true" 
                                                 data-bv-stringlength-min="0" 
                                                 data-bv-stringlength-max="60" 
                                                 data-bv-stringlength-message="El número exterior excedió el número máximo de caracteres." />
                                    <span class="help-block" id="paternoMessage" />
                                </div>
                                <div class="col-sm-4">
                                    <s:textfield cssClass="form-control" name="alumnoDatosBanco.numeroInterior" id="numeroInteriorBanco" placeholder="Número Interior"
                                                 pattern="^[-a-zA-ZáéíóúñÁÉÍÓÚÑ0-9.#/_\s]+$"
                                                 data-bv-regexp-message="El número interior sólo puede estar conformado por letras y/o números."
                                                 data-bv-stringlength="true" 
                                                 readonly="true"
                                                 data-bv-stringlength-min="0" 
                                                 data-bv-stringlength-max="60" 
                                                 data-bv-stringlength-message="El número interior excedió el número máximo de caracteres." />
                                    <span class="help-block" id="maternoMessage" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">Estado</label>
                                <div class="col-sm-8">
                                    <s:textfield cssClass="form-control" name="alumnoDatosBanco.estado" id="estadoBanco" placeholder="Estado"
                                                 data-bv-message="Este dato no es válido."
                                                 required="true" 
                                                 data-bv-notempty="true"
                                                 readonly="true"
                                                 data-bv-notempty-message="El estado es requerido."
                                                 pattern="^[-a-zA-ZáéíóúñÁÉÍÓÚÑ0-9.#/_\s]+$"
                                                 data-bv-regexp-message="Tu estado debe estar conformado por letras."
                                                 data-bv-stringlength="true" 
                                                 data-bv-stringlength-min="1" 
                                                 data-bv-stringlength-max="100" 
                                                 data-bv-stringlength-message="Tu calle debe ser de al menos 1 caracter." />
                                    <span class="help-block" id="nombreMessage" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">Municipio</label>
                                <div class="col-sm-8">
                                    <s:textfield cssClass="form-control" name="alumnoDatosBanco.municipio" id="municipioBanco" placeholder="Municipio"
                                                 data-bv-message="Este dato no es válido."
                                                 required="true" 
                                                 data-bv-notempty="true"
                                                 readonly="true"
                                                 data-bv-notempty-message="La calle es requerida."
                                                 pattern="^[-a-zA-ZáéíóúñÁÉÍÓÚÑ0-9.#/_\s]+$"
                                                 data-bv-regexp-message="Tu municipio debe estar conformado por letras."
                                                 data-bv-stringlength="true" 
                                                 data-bv-stringlength-min="1" 
                                                 data-bv-stringlength-max="100" 
                                                 data-bv-stringlength-message="Tu calle debe ser de al menos 1 caracter." />
                                    <span class="help-block" id="nombreMessage" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">Colonia</label>
                                <div class="col-sm-8">
                                    <s:textfield cssClass="form-control" name="alumnoDatosBanco.colonia" id="coloniaBanco" placeholder="Colonia"
                                                 data-bv-message="Este dato no es válido."
                                                 required="true" 
                                                 data-bv-notempty="true"
                                                 readonly="true"
                                                 data-bv-notempty-message="La colonia es requerida."
                                                 pattern="^[-a-zA-ZáéíóúñÁÉÍÓÚÑ0-9.#/_\s]+$"
                                                 data-bv-regexp-message="Tu colonia debe estar conformado por letras y/o números.."
                                                 data-bv-stringlength="true" 
                                                 data-bv-stringlength-min="1" 
                                                 data-bv-stringlength-max="100" 
                                                 data-bv-stringlength-message="Tu colonia debe ser de al menos 1 caracter." />
                                    <span class="help-block" id="nombreMessage" />
                                </div>
                            </div>   
                            <div class="form-group">
                                <label class="col-sm-3 control-label">Código Postal</label>
                                <div class="col-sm-8">
                                    <s:textfield cssClass="form-control" name="alumnoDatosBanco.codigoPostal" id="codigoPostalBanco" placeholder="Código Postal"
                                                 data-bv-message="Este dato no es válido"
                                                 required="true" 
                                                 data-bv-notempty="true"
                                                 readonly="true"
                                                 data-bv-notempty-message="El código postal es requerido"
                                                 pattern="^[0-9]+$" 
                                                 data-bv-regexp-message="Tu código postal debe estar conformado sólo por números"
                                                 data-bv-stringlength="true" 
                                                 data-bv-stringlength-min="5" 
                                                 data-bv-stringlength-max="5" 
                                                 data-bv-stringlength-message="Tu código postal debe ser de 5 caracteres numéricos" />
                                    <span class="help-block" id="nombreMessage" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">Fecha de modificación</label>
                                <div class="col-sm-8">
                                    <div class="input-group">
                                        <input type="text" class="form-control" id="fechaDeNacimientoTutor" name="fechaNacimientoTutor"
                                               value="<s:date name="alumnoDatosBanco.fechaModificacion" format="dd-MM-yyyy HH:mm" />"
                                               data-bv-notempty="true"
                                               readonly="true"
                                               data-bv-message="Este dato no es válido"
                                               onkeydown="return false"
                                               required="true">
                                    </div>
                                </div>
                            </div>
                            </form>
                        </div>
                    </div>
                </div>
        </div>
    </div>
    <!-- Ejemplo de caja blanca -->
    <div class="row">
        <div class="col-xs-12">
            <div class="main-box clearfix" >
                <div class="row">
                    <header class="main-box-header clearfix">
                        <h2 class="pull-left">Listado de solicitudes para creación de cuenta bancaria</h2>
                    </header>
                </div>
                <div class="row" id="rowTable" style="display: none;">
                    <div class="col-xs-12">
                        <div class="col-md-12" style="padding: 0px">
                            <div class="responsive">                        
                                <table id="tablaPeriodos" class="table table-striped table-hover dt-responsive" style="width: 100%">
                                    <thead>
                                        <tr>
                                            <th>Fecha</th>
                                            <th>Estatus</th>
                                        </tr>
                                    </thead>
                                </table>                        
                            </div>
                        </div>
                    </div>                
                </div>
            </div>
        </div>
    </div> <!-- Termina ejemplo caja blanca -->


</body>

<content tag="endScripts">
     <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>
    <script type="text/javascript" src="/vendors/fancy-box/jquery.fancybox.min.js"></script>
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js" data-init="true" 
    data-ajax="/ajax/listadoSolicitudTarjetaAjax.action?datosBancarios=<s:property value="alumnoDatosBanco.id"/>" data-div="rowTable"data-function="despuesCargarTabla"></script>
</content>

<content tag="inlineScripts">
    <script>
                                                   $(document).ready(function () {
                                                       $('input[type=text]').addClass('form-control');

                                                       $('#rowTable').on('draw.dt', function () {
                                                           $("#dataTableHeader-rowTable").hide();
                                                           $("#paginateFooter-rowTable").hide();
                                                       });
                                                   });
                                                   function despuesCargarTabla() {
                                                       $('.fancybox').fancybox({
                                                           afterClose: function () {
                                                               recargarTabla();
                                                           }
                                                       });
                                                   }
                                                   function recargarTabla() {
                                                       generarTabla("rowTable", "/ajax/listadoPeriodoAjax.action", despuesCargarTabla);
                                                   }
    </script>
</content>

