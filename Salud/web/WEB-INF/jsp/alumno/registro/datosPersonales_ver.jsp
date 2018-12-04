<%-- 
    Document   : datosPersonales_ver
    Created on : 10/12/2015, 11:37:32 AM
    Author     : Tania G. Sánchez
--%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Datos personales</title>
    <script type="text/javascript" src="/bootstrap-validator/js/jquery.min.js"></script>    
    <!-- libraries -->
    <link rel="stylesheet" type="text/css" href="/css/compiled/theme_styles.css" />
    <!-- this page specific styles -->
    <link rel="stylesheet" type="text/css" href="/css/compiled/wizard.css">
</head> 

<content tag="tituloJSP">
    Visualización de datos personales
</content>

<body>
    <div class="row">
        <div class="col-xs-12">
            <s:if test="%{contestoESE}">
                <div class="alert alert-info fade in">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                    <i class="fa fa-info-circle fa-fw fa-lg"></i>
                    <strong>¿Necesitas ayuda?</strong> Da click <a href="/misdatos/consultar.action#encabezado1">aquí</a> para consultar el manual de usuario.
                </div> 
            </s:if>
        </div>
    </div>
    <div class="row main_content">
        <div class="col-lg-12 col-md-12 col-sm-12">
            <div class="main-box tabs-wrapper">
                <ul class="nav nav-tabs">
                    <li class="active"><a href="#personales-tab" data-toggle="tab">Personales <i class="fa"></i></a></li>
                    <li><a href="#academicos-tab" data-toggle="tab">Acad&eacute;micos <i class="fa"></i></a></li>
                    <li><a href="#direccion-tab" data-toggle="tab">Direcci&oacute;n<i class="fa"></i></a></li>
                    <li><a href="#contacto-tab" data-toggle="tab">Contacto<i class="fa"></i></a></li>
                    <!--<li><a href="#oportunidades-tab" data-toggle="tab">Otros programas<i class="fa"></i></a></li>-->
                </ul>

                <div class="tab-content">
                    <div class="tab-pane fade in active" id="personales-tab">
                        <form id="accountForm" method="post" class="form-horizontal" action="/registro/guardaRegistro.action">
                            <div class="col-sm-12 form-group">
                                <label class="col-sm-3 control-label">Nombre(s)</label>
                                <div class="col-sm-8">
                                    <s:textfield class="form-control" name="alumno.nombre" id="nombre" placeholder="Nombre"
                                                 readonly="true"/>
                                </div>
                            </div>
                            <div class="col-sm-12 form-group">
                                <label class="col-sm-3 control-label">Apellidos</label>
                                <div class="col-sm-4">
                                    <s:textfield class="form-control" name="alumno.apellidoPaterno" id="apellidoPaterno" placeholder="Apellido paterno"
                                                 readonly="true"/>
                                </div>
                                <div class="col-sm-4">
                                    <s:textfield class="form-control" name="alumno.apellidoMaterno" id="apellidoMaterno" placeholder="Apellido materno"
                                                 readonly="true"/>
                                </div>
                            </div>
                            <div class="col-sm-12 form-group">
                                <label class="col-sm-3 control-label">CURP</label>
                                <div class="col-sm-8">
                                    <s:textfield class="form-control" name="alumno.curp" id="curp" placeholder="CURP"
                                                 readonly="true"/>
                                </div>
                            </div>
                            <div class="col-sm-12 form-group">
                                <label class="col-sm-3 control-label">Fecha de nacimiento</label>
                                <div class="col-sm-8">
                                    <div class="input-group">
                                        <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                        <input type="text" class="form-control" id="fechaNacimiento" name="fechaDeNacimiento"
                                               value="<s:date name="alumno.fechaDeNacimiento" format="dd-MM-yyyy" />"
                                               data-bv-notempty="true"
                                               data-bv-message="Este dato no es válido"
                                               required="true"
                                               disabled>
                                    </div>
                                    <span class="help-block">Formato dd-mm-yyyy</span>
                                </div>
                            </div>

                            <div class="col-sm-12 form-group">
                                <label class="col-sm-3 control-label">Entidad de nacimiento</label>
                                <div class="col-sm-8">
                                    <s:textfield class="form-control" name="alumno.entidadDeNacimiento.nombre" id="estadosDireccion" placeholder="Entidad de nacimiento"
                                                 readonly="true"/>
                                </div>
                            </div>
                            <div class="col-sm-12 form-group">
                                <label class="col-sm-3 control-label">G&eacute;nero</label>
                                <div class="col-sm-8">
                                    <s:textfield class="form-control" name="alumno.genero.nombre" id="genero" placeholder="Género"
                                                 readonly="true"/>
                                </div>
                            </div>
                            <div class="col-sm-12 form-group">
                                <label class="col-sm-3 control-label">Estado civil</label>
                                <div class="col-sm-8">
                                    <s:textfield class="form-control" name="alumno.estadoCivil.nombre" id="civil" placeholder="Estado civil"
                                                 readonly="true"/>
                                </div>
                            </div>
                        </form>
                    </div>

                    <div class="tab-pane fade" id="academicos-tab">
                        <form id="accountForm" method="post" class="form-horizontal" action="/registro/guardaRegistro.action">
                            <div class="col-sm-12 form-group">
                                <label class="col-sm-3 control-label">Boleta</label>
                                <div class="col-sm-8">
                                    <s:textfield class="form-control" name="alumno.boleta" id="boleta" placeholder="Boleta"
                                                 readonly="true"/>
                                </div>
                            </div>
                            <div class="col-sm-12 form-group">
                                <label class="col-sm-3 control-label">Unidad Acad&eacute;mica</label>
                                <div class="col-sm-8">
                                    <s:textfield class="form-control" name="alumno.datosAcademicos.unidadAcademica.nombreCorto" id="boleta" placeholder="Unidad Académica"
                                                 readonly="true"/>
                                </div>
                            </div>
                            <div class="col-sm-12 form-group">
                                <label class="col-sm-3 control-label">Semestre</label>
                                <div class="col-sm-8">
                                    <s:textfield class="form-control" name="alumno.datosAcademicos.semestre" id="semestre" placeholder="Semestre"
                                                 readonly="true"/>
                                </div>
                            </div>
                            <div class="col-sm-12 form-group">
                                <label class="col-sm-3 control-label">Promedio</label>
                                <div class="col-sm-8">
                                    <input type="number" class="form-control" id = "prom" disabled>
                                    <s:hidden name="alumno.datosAcademicos.promedio" id="promedio"/>
                                </div>
                            </div>
                            <div class="col-sm-12 form-group">
                                <label class="col-sm-3 control-label">Carrera</label>
                                <div class="col-sm-8">
                                    <s:textfield class="form-control" name="alumno.datosAcademicos.carrera.carrera" id="carrera" placeholder="Carrera"
                                                 readonly="true"/>
                                </div>
                            </div>
                            <div class="col-sm-12 form-group">
                                <label class="col-sm-3 control-label">Plan de estudios</label>
                                <div class="col-sm-8">
                                    <s:textfield class="form-control" name="alumno.datosAcademicos.carrera.planEstudios" id="planDeEstudios" placeholder="Plan de estudios"
                                                 readonly="true"/>
                                </div>
                            </div>
                        </form>
                    </div>

                    <div class="tab-pane fade" id="direccion-tab">
                        <form id="accountForm" method="post" class="form-horizontal" action="/registro/guardaRegistro.action">
                            <div class="col-sm-12 form-group">
                                <label class="col-sm-3 control-label">Calle</label>
                                <div class="col-sm-8">
                                    <s:textfield class="form-control" name="alumno.direccion.calle" id="calle" placeholder="Calle"
                                                 readonly="true"/>
                                </div>
                            </div>
                            <div class="col-sm-12 form-group">
                                <label class="col-sm-3 control-label">N&uacute;mero</label>
                                <div class="col-sm-4">
                                    <s:textfield class="form-control" name="alumno.direccion.numeroExterior" id="numeroExterior" placeholder="Número exterior"
                                                 readonly="true"/>
                                </div>
                                <div class="col-sm-4">
                                    <s:textfield class="form-control" name="alumno.direccion.numeroInterior" id="numeroInterior" placeholder="Número interior"
                                                 readonly="true"/>
                                </div>
                            </div>
                            <div class="col-sm-12 form-group">
                                <label class="col-sm-3 control-label">Entidad</label>
                                <div class="col-sm-8">
                                    <s:textfield class="form-control" name="alumno.direccion.relacionGeografica.estado.nombre" id="estado" placeholder="Entidad"
                                                 readonly="true"/>
                                </div>
                            </div>
                            <div class="col-sm-12 form-group">
                                <label class="col-sm-3 control-label">Municipio</label>
                                <div class="col-sm-8">
                                    <s:textfield class="form-control" name="alumno.direccion.relacionGeografica.municipio.nombre" id="municipio" placeholder="Municipio"
                                                 readonly="true"/>
                                </div>
                            </div>
                            <div class="col-sm-12 form-group">
                                <label class="col-sm-3 control-label">Colonia</label>
                                <div class="col-sm-8">
                                    <s:textfield class="form-control" name="alumno.direccion.relacionGeografica.colonia.nombre" id="colonia" placeholder="Colonia"
                                                 readonly="true"/>
                                </div>
                            </div>
                            <div class="col-sm-12 form-group">
                                <label class="col-sm-3 control-label">Localidad</label>
                                <div class="col-sm-8">
                                    <s:textfield class="form-control" name="alumno.direccion.inegiLocalidad.localidad" id="colonia" placeholder="Colonia"
                                                 readonly="true"/>
                                </div>
                            </div>
                            <div class="col-sm-12 form-group">
                                <label class="col-sm-3 control-label">C&oacute;digo Postal</label>
                                <div class="col-sm-8">
                                    <s:textfield class="form-control" name="alumno.direccion.codigoPostal" id="codigoPostal" placeholder="Código Postal"
                                                 readonly="true"/>
                                </div>
                            </div>
                            <div class="col-sm-12 form-group">
                                <label class="col-sm-3 control-label">Tipo de vialidad</label>
                                <div class="col-sm-8">
                                    <s:textfield class="form-control" name="alumno.direccion.inegiTipoVialidad.descripcion" id="inegiTipoVialidad" placeholder="Vialidad"
                                                 readonly="true"/>
                                </div>
                            </div>
                            <div class="col-sm-12 form-group">
                                <label class="col-sm-3 control-label">Tipo de asentamiento</label>
                                <div class="col-sm-8">
                                    <s:textfield class="form-control" name="alumno.direccion.inegiTipoAsentamiento.descripcion" id="inegiTipoAsentamiento" placeholder="Asentamiento"
                                                 readonly="true"/>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="tab-pane fade" id="contacto-tab">
                        <form id="accountForm" method="post" class="form-horizontal" action="/registro/guardaRegistro.action">
                            <div class="col-sm-12 form-group">
                                <label class="col-sm-3 control-label">Correo electr&oacute;nico</label>
                                <div class="col-sm-8">
                                    <s:textfield class="form-control" name="alumno.correoElectronico" id="correoElectronico" placeholder="ejemplo@gmail.com"
                                                 readonly="true"/>
                                    <span class="help-block" id="nombreMessage" />
                                </div>
                            </div> 
                            <div class="col-sm-12 form-group">
                                <label class="col-sm-3 control-label">Correo electrónico alterno</label>
                                <div class="col-sm-8">
                                    <s:textfield cssClass="form-control" name="alumno.correoElectronicoAlterno" id="correoElectronicoAlterno" placeholder="ejemplo@gmail.com"
                                                 readonly="true"/>
                                </div>
                            </div>
                            <div class="col-sm-12 form-group">
                                <label class="col-sm-3 control-label">Teléfono celular</label>
                                <div class="col-sm-8">
                                    <s:textfield class="form-control" name="alumno.celular" id="celular" placeholder="55-55555555"
                                                 readonly="true"/>
                                </div>
                            </div> 
                            <div class="col-sm-12 form-group">
                                <label class="col-sm-3 control-label">Compa&ntilde;&iacute;a celular</label>
                                <div class="col-sm-8">
                                    <s:textfield class="form-control" name="alumno.companiaCelular.nombre" id="companiaCelular" placeholder="55-55555555"
                                                 readonly="true"/>
                                </div>
                            </div>
                            <div class="col-sm-12 form-group">
                                <label class="col-sm-3 control-label">Teléfono de casa</label>
                                <div class="col-sm-8">
                                    <s:textfield cssClass="form-control" name="alumno.telefonoCasa" id="telefonoCasa" placeholder="55-55555555"
                                                 readonly="true"/>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="tab-pane fade" id="oportunidades-tab">
                        <form id="accountForm" method="post" class="form-horizontal" action="/registro/guardaRegistro.action">
                            <div class="col-sm-12 form-group">
                                <label class="col-sm-3 control-label"> </label>
                                <div class="col-sm-8">
                                    <label for="beneficiarioOportunidades">
                                        &iquest;Es beneficiario del programa PROSPERA (antes Oportunidades)?
                                    </label>
                                    <div class="radio">
                                        <s:radio list="service.respuestaBoolean" name="alumno.beneficiarioOportunidades" disabled="true"/>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>                    
            </div>
        </div>
    </div> 
</body>

<content tag="endScripts">
</content>

<content tag="inlineScripts">
    <script>
        $(function () {
            //SE AGREGA LA CLASE A ELEMENTOS QUE SON CREADOS MEDIANTE
            //STRUTS
            $('input[type=text]').addClass('form-control');
            $('select').addClass('form-control');
            var entidad = '<s:property value="alumno.direccion.relacionGeografica.estado.id" />';
            //        if (entidad.trim().length > 0) {
            //            getMunicipios();
            //        }
        });
        $(document).ready(function () {
            var number =<s:property value="alumno.datosAcademicos.promedio"/>;
            document.getElementById("prom").value = number.toFixed(2);
        });
    </script>
</content>