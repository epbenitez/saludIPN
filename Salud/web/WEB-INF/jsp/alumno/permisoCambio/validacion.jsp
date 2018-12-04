<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<head>
    <title>Asignación de becas</title>

    <style>
        .nav>li>a:hover, .nav>li>a:focus {
            text-decoration: none;
            background-color: #03a9f4;
            color: #fff;
        }
    </style>
</head>
<body>
    <div class="row">
        <div class="col-sm-12">
            <s:if test="hasActionErrors()">
                <s:if test="warning==true">
                    <div class="row">
                        <div class="col-sm-12">
                            <div class="alert alert-warning">
                                <i class="fa fa-warning fa-fw fa-lg"></i>
                                <s:actionerror/>
                            </div>
                        </div>
                    </div>
                </s:if>
                <s:else>
                    <div class="alert alert-danger">
                        <i class="fa fa-times-circle fa-fw fa-lg"></i>
                        <strong>&iexcl;Error!</strong> <s:actionerror/>
                    </div>
                </s:else>
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
        <div class="col-sm-12">
            <div class="main-box-body clearfix">
                <div class="col-sm-3" style="border-right: 1px solid #ddd;">
                    <br><br><br>
                    <ul class="nav nav-pills nav-fill">
                        <security:authorize ifAnyGranted="ROLE_JEFEBECAS, ROLE_ANALISTABECAS">
                        <li class="nav-item"><a class="nav-link" href="#contraseña" data-toggle="tab">Contraseña</a></li>
                        </security:authorize>
                        <li class="nav-item"><a class="nav-link active" href="#personales" data-toggle="tab">Datos Personales</a></li>                               
                        <li class="nav-item"><a class="nav-link" href="#contacto" data-toggle="tab">Datos de Contacto</a></li>                               
                        <li class="nav-item"><a class="nav-link" href="#editar" data-toggle="tab">Unidad Académica / Carrera</a></li>                               
                        <li class="nav-item"><a class="nav-link" href="#estatus" data-toggle="tab">Datos Académicos</a></li>                               
                        <li class="nav-item"><a class="nav-link" href="#cotejar" data-toggle="tab">Validación de documentos</a></li>
                        <li class="nav-item"><a class="nav-link" href="#salarios" data-toggle="tab">Salarios mínimos</a></li>
                    </ul>                    
                </div>
                <div class="col-sm-9">
                    <div class="tab-content">
                        <div class="tab-pane text-style" id="contraseña">
                            <h1>Contraseña</h1>
                            <div class="col-sm-12 main-box">
                                <div class="clearfix" >&nbsp;</div>
                                <div class="form-group col-sm-12">
                                    <label class="col-sm-3 control-label">Contraseña</label>
                                    <div class="col-sm-8">
                                        <s:textfield cssClass="form-control" 
                                                     name="password"
                                                     id="password" 
                                                     placeholder="Contraseña"
                                                     readonly="true"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="tab-pane text-style active" id="personales">
                            <h1>Datos personales</h1>
                            <div class="col-sm-12 main-box">
                                <div class="clearfix" >&nbsp;</div>
                                <div class="form-group col-sm-12">
                                    <label class="col-sm-3 control-label">Nombre(s)</label>
                                    <div class="col-sm-8">
                                        <s:textfield cssClass="form-control" 
                                                     name="alumno.nombre"
                                                     id="nombre" 
                                                     placeholder="Nombre"
                                                     readonly="true"/>
                                    </div>
                                </div>
                                <div class="form-group col-sm-12">
                                    <label class="col-sm-3 control-label">Apellidos</label>
                                    <div class="col-sm-4">
                                        <s:textfield cssClass="form-control" 
                                                     name="alumno.apellidoPaterno" 
                                                     id="apellidoPaterno" 
                                                     placeholder="Apellido paterno"
                                                     readonly="true"/>
                                    </div>
                                    <div class="col-sm-4">
                                        <s:textfield cssClass="form-control" 
                                                     name="alumno.apellidoMaterno" 
                                                     id="apellidoMaterno" 
                                                     placeholder="Apellido materno" 
                                                     readonly="true"/>
                                    </div>
                                </div>
                                <div class="form-group col-sm-12">
                                    <label class="col-sm-3 control-label">CURP</label>
                                    <div class="col-sm-8">
                                        <s:textfield cssClass="form-control" 
                                                     name="alumno.curp" 
                                                     id="curp" 
                                                     placeholder="CURP" 
                                                     readonly="true"/>
                                    </div>
                                </div>
                                <div class="form-group col-sm-12">
                                    <label class="col-sm-3 control-label">Fecha de nacimiento</label>
                                    <div class="col-sm-8">
                                        <input type="text" 
                                               class="form-control" 
                                               id="fechaNacimiento" 
                                               name="fechaDeNacimiento"
                                               value="<s:date 
                                                   name="alumno.fechaDeNacimiento" 
                                                   format="dd-MM-yyyy" />"
                                               data-bv-notempty="true"
                                               data-bv-message="Este dato no es válido"
                                               required="true"
                                               disabled>
                                    </div>
                                </div>

                                <div class="form-group col-sm-12">
                                    <label class="col-sm-3 control-label">Entidad de nacimiento</label>
                                    <div class="col-sm-8">
                                        <s:textfield cssClass="form-control" 
                                                     name="alumno.entidadDeNacimiento.nombre"
                                                     id="estadosDireccion"
                                                     placeholder="Entidad de nacimiento"
                                                     readonly="true"/>
                                    </div>
                                </div>
                                <div class="form-group col-sm-12">
                                    <label class="col-sm-3 control-label">Género</label>
                                    <div class="col-sm-8">
                                        <s:textfield cssClass="form-control" 
                                                     name="alumno.genero.nombre" 
                                                     id="genero" 
                                                     placeholder="Género"
                                                     readonly="true"/>
                                    </div>
                                </div>
                                <div class="form-group col-sm-12">
                                    <label class="col-sm-3 control-label">Estado civil</label>
                                    <div class="col-sm-8">
                                        <s:textfield cssClass="form-control" 
                                                     name="alumno.estadoCivil.nombre" 
                                                     id="civil" 
                                                     placeholder="Estado civil"
                                                     readonly="true"/>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="tab-pane text-style" id="contacto">
                            <h1>Datos de contacto</h1>
                            <div class="col-sm-12 main-box">
                                <div class="clearfix" >&nbsp;</div>
                                <div class="form-group col-sm-12">
                                    <label class="col-sm-3 control-label">Teléfono celular / Casa</label>
                                    <div class="col-sm-8">
                                        <s:textfield cssClass="form-control" 
                                                     name="alumno.celular"
                                                     id="celular" 
                                                     placeholder="55-55555555"
                                                     readonly="true"/>
                                    </div>
                                </div> 
                                <div class="form-group col-sm-12">
                                    <label class="col-sm-3 control-label">Compañía celular</label>
                                    <div class="col-sm-8">
                                        <s:textfield cssClass="form-control" 
                                                     name="alumno.companiaCelular.nombre" 
                                                     id="companiaCelular" 
                                                     placeholder="55-55555555"
                                                     readonly="true"/>
                                    </div>
                                </div>
                                <div class="form-group col-sm-12">
                                    <label class="col-sm-3 control-label">Correo electrónico</label>
                                    <div class="col-sm-8">
                                        <s:textfield cssClass="form-control" 
                                                     name="alumno.correoElectronico"
                                                     id="correoElectronico" 
                                                     placeholder="ejemplo@gmail.com"
                                                     readonly="true"/>
                                    </div>
                                </div> 
                            </div>
                        </div>

                        <div class="tab-pane text-style" id="editar">
                            <h1>Unidad Académica / Carrera</h1>
                            <s:if test="( !datosActualizados) ">
                                <div class="row">
                                    <div class="col-lg-12">
                                        <div class="alert alert-warning">
                                            <i class="fa fa-warning fa-fw fa-lg"></i>
                                            <strong>¡Atención!</strong> Los datos que se muestran corresponden al periodo <s:property  value="periodoDatos"/>
                                        </div>
                                    </div>
                                </div>
                            </s:if>
                            <div class="row">
                                <div class="col-lg-12">
                                    <s:if test="otorgamientoActual">
                                        <div class="row">
                                            <div class="col-sm-12">
                                                <div class="alert alert-info">
                                                    <i class="fa fa-info-circle fa-fw fa-lg"></i>
                                                    <strong>Toma en cuenta</strong> El alumno cuenta con un otorgamiento en el periodo actual, por lo que no se puede editar la información.
                                                </div>
                                            </div>
                                        </div>
                                    </s:if>
                                    <s:if test="clasificacionSolicitudBeca">
                                        <div class="row">
                                            <div class="col-sm-12">
                                                <div class="alert alert-info">
                                                    <i class="fa fa-info-circle fa-fw fa-lg"></i>
                                                    <strong>Toma en cuenta</strong> El alumno cuenta con una solicitud otorgada, rechazada o en espera, por lo que no se puede editar la información.
                                                </div>
                                            </div>
                                        </div>
                                    </s:if>    
                                    <div class="main-box clearfix">
                                        <div class="main-box-body">
                                            <form id="editarForm" action="/misdatos/unidadCarreraPermisoCambio.action" method="post"  class="form-horizontal">
                                                <s:if test="!otorgamientoActual && !clasificacionSolicitudBeca">
                                                    <div class="form-group col-sm-12">
                                                        <label class="col-sm-3 control-label">Unidad Académica</label>
                                                        <security:authorize ifAnyGranted="ROLE_JEFEBECAS, ROLE_ANALISTABECAS, ROLE_ADMIN, ROLE_RESPONSABLE_UA">
                                                            <div class="col-sm-8">
                                                                <s:select cssClass="form-control"
                                                                          id="unidadAcademica"
                                                                          name="alumno.datosAcademicos.unidadAcademica.id"
                                                                          list="ambiente.unidadAcademicaList"
                                                                          listKey="id" 
                                                                          listValue="nombreCorto"
                                                                          headerValue = 'Selecciona una opción'
                                                                          headerKey = '-1'
                                                                          onChange = "getPlanE()"/>
                                                            </div>
                                                        </security:authorize>
                                                        <security:authorize ifNotGranted="ROLE_JEFEBECAS, ROLE_ANALISTABECAS, ROLE_ADMIN, ROLE_RESPONSABLE_UA">
                                                            <div class="col-sm-8">
                                                                <s:select id="unidadAcademica"
                                                                          name="alumno.datosAcademicos.unidadAcademica.id"
                                                                          cssClass="form-control"
                                                                          list="ambiente.unidadAcademicaList"
                                                                          listKey="id" 
                                                                          listValue="nombreCorto" 
                                                                          disabled="true"/>
                                                            </div>
                                                        </security:authorize>
                                                    </div>
                                                    <div class="form-group col-sm-12">
                                                        <label class="col-sm-3 control-label">Plan Estudios</label>
                                                        <div class="col-sm-8">
                                                            <s:select cssClass="form-control"
                                                                      id="planE"
                                                                      name="alumno.datosAcademicos.carrera.planEstudios"
                                                                      list="ambiente.unidadAcademicaList"
                                                                      listKey="id" 
                                                                      listValue="nombreCorto"
                                                                      headerValue = 'Selecciona una opción'
                                                                      headerKey = '-1'
                                                                      onChange = "getCarrera()"/>
                                                        </div>
                                                    </div>
                                                    <div class="form-group col-sm-12">
                                                        <label class="col-sm-3 control-label">Carrera</label>
                                                        <div class="col-sm-8">
                                                            <s:select cssClass="form-control"
                                                                      id="carrera"
                                                                      name="alumno.datosAcademicos.carrera.claveCarrera"
                                                                      list="ambiente.unidadAcademicaList"
                                                                      listKey="id" 
                                                                      listValue="nombreCorto"
                                                                      headerValue = 'Selecciona una opción'
                                                                      headerKey = '-1'
                                                                      onChange = "getEspecialidad()"
                                                                      required="true" 
                                                                      />
                                                        </div>
                                                    </div>
                                                    <div class="form-group col-sm-12">
                                                        <label class="col-sm-3 control-label">Especialidad</label>
                                                        <div class="col-sm-8">
                                                            <s:select cssClass="form-control"
                                                                      id="especialidad"
                                                                      name="alumno.datosAcademicos.carrera.especialidad"
                                                                      list="ambiente.unidadAcademicaList"
                                                                      listKey="id" 
                                                                      headerValue = 'Selecciona una opción'
                                                                      headerKey = '-1'
                                                                      listValue="nombreCorto"/>
                                                        </div>
                                                    </div>
                                                    <security:authorize ifAnyGranted="ROLE_JEFEBECAS,ROLE_ANALISTABECAS">
                                                        <div class="form-group">
                                                            <div class="col-sm-12">
                                                                <button type="submit" class="responsable btn btn-primary pull-right solo-lectura">Guardar</button>
                                                            </div>
                                                            <div class="clearfix" >&nbsp;</div>
                                                        </div>
                                                    </security:authorize>
                                                    <s:hidden name="alumno.id"/>
                                                    <s:hidden name="alumno.boleta"/>
                                                    <s:hidden name="permisoCambio"/>
                                                    <s:hidden name="sec" />
                                                </s:if>
                                                <s:else>
                                                    <div class="form-group col-sm-12">
                                                        <label class="col-sm-3 control-label">Unidad Académica</label>
                                                        <div class="col-sm-8">
                                                            <s:select id="unidadAcademica"
                                                                      name="alumno.datosAcademicos.unidadAcademica.id"
                                                                      cssClass="form-control"
                                                                      list="ambiente.unidadAcademicaList"
                                                                      listKey="id" 
                                                                      listValue="nombreCorto" 
                                                                      disabled="true"/>
                                                        </div>
                                                    </div>
                                                    <div class="form-group col-sm-12">
                                                        <label class="col-sm-3 control-label">Plan Estudios</label>
                                                        <div class="col-sm-8">
                                                            <s:select cssClass="form-control"
                                                                      id="planE"
                                                                      name="alumno.datosAcademicos.carrera.planEstudios"
                                                                      list="ambiente.unidadAcademicaList"
                                                                      listKey="id" 
                                                                      listValue="nombreCorto"
                                                                      disabled="true"/>
                                                        </div>
                                                    </div>
                                                    <div class="form-group col-sm-12">
                                                        <label class="col-sm-3 control-label">Carrera</label>
                                                        <div class="col-sm-8">
                                                            <s:select cssClass="form-control"
                                                                      id="carrera"
                                                                      name="alumno.datosAcademicos.carrera.claveCarrera"
                                                                      list="ambiente.unidadAcademicaList"
                                                                      listKey="id" 
                                                                      listValue="nombreCorto"
                                                                      disabled="true"/>
                                                        </div>
                                                    </div>
                                                    <div class="form-group col-sm-12">
                                                        <label class="col-sm-3 control-label">Especialidad</label>
                                                        <div class="col-sm-8">
                                                            <s:select cssClass="form-control"
                                                                      id="especialidad"
                                                                      name="alumno.datosAcademicos.carrera.especialidad"
                                                                      list="ambiente.unidadAcademicaList"
                                                                      listKey="id" 
                                                                      disabled="true"/>
                                                        </div>
                                                    </div>
                                                </s:else>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="tab-pane text-style" id="estatus">
                            <h1>Datos Académicos</h1>
                            <s:if test="( !datosActualizados) ">
                                <div class="row">
                                    <div class="col-lg-12">
                                        <div class="alert alert-warning">
                                            <i class="fa fa-warning fa-fw fa-lg"></i>
                                            <strong>¡Atención!</strong> Los datos que se muestran corresponden al periodo <s:property  value="periodoDatos"/>
                                        </div>
                                    </div>
                                </div>
                            </s:if>
                            <s:if test="otorgamientoActual">
                                <div class="row">
                                    <div class="col-sm-12">
                                        <div class="alert alert-info">
                                            <i class="fa fa-info-circle fa-fw fa-lg"></i>
                                            <strong>Toma en cuenta</strong> El alumno cuenta con un otorgamiento en el periodo actual, por lo que no se puede editar la información.
                                        </div>
                                    </div>
                                </div>
                            </s:if>
                            <s:if test="clasificacionSolicitudBeca">
                                <div class="row">
                                    <div class="col-sm-12">
                                        <div class="alert alert-info">
                                            <i class="fa fa-info-circle fa-fw fa-lg"></i>
                                            <strong>Toma en cuenta</strong> El alumno cuenta con una solicitud otorgada, rechazada o en espera, por lo que no se puede editar la información.
                                        </div>
                                    </div>
                                </div>
                            </s:if>
                            <div class="row">
                                <div class="col-lg-12">
                                    <div class="main-box clearfix">
                                        <div class="main-box-body">
                                            <form id="estatusForm" action="/misdatos/editarPermisoCambio.action" method="post" >
                                                <s:if test="!otorgamientoActual && !clasificacionSolicitudBeca">
                                                    <div class="form-group">
                                                        <label class="col-sm-3 control-label">Semestre</label>
                                                        <div class="col-sm-8">
                                                            <s:textfield cssClass="form-control"
                                                                         name="alumno.datosAcademicos.semestre" 
                                                                         id="semestre" 
                                                                         placeholder="Semestre"
                                                                         data-bv-message="Este dato no es válido"
                                                                         required="true" 
                                                                         data-bv-notempty="true"
                                                                         data-bv-notempty-message="El semestre es requerido"
                                                                         pattern="^[\.\s1234567890]+$" 
                                                                         data-bv-regexp-message="Solo se permiten numeros"
                                                                         data-bv-stringlength="true" 
                                                                         data-bv-stringlength-max="20"/>
                                                            <span class="help-block" id="semestreMessage" />
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label class="col-sm-3 control-label">Promedio</label>
                                                        <div class="col-sm-8">
                                                            <security:authorize ifAnyGranted="ROLE_JEFEBECAS, ROLE_RESPONSABLE_UA, ROLE_ANALISTABECAS">
                                                                <s:textfield cssClass="form-control"
                                                                             name="promedioA" 
                                                                             id="promedioMinimo" 
                                                                             placeholder="Promedio"
                                                                             data-bv-message="Este dato no es válido"
                                                                             required="true" 
                                                                             data-bv-notempty="true"
                                                                             data-bv-notempty-message="El promedio es requerido"
                                                                             pattern="^[\.\s1234567890]+$" 
                                                                             data-bv-regexp-message="Solo se permiten numeros"
                                                                             data-bv-stringlength="true" 
                                                                             data-bv-stringlength-max="20"/>
                                                            </security:authorize> 
                                                            <security:authorize ifNotGranted="ROLE_JEFEBECAS, ROLE_RESPONSABLE_UA, ROLE_ANALISTABECAS">
                                                                <s:textfield cssClass="form-control"
                                                                             name="promedioA" 
                                                                             id="promedioMinimo" 
                                                                             placeholder="Promedio"
                                                                             data-bv-message="Este dato no es válido"
                                                                             required="true" 
                                                                             data-bv-notempty="true"
                                                                             data-bv-notempty-message="El promedio es requerido"
                                                                             pattern="^[\.\s1234567890]+$" 
                                                                             data-bv-regexp-message="Solo se permiten numeros"
                                                                             data-bv-stringlength="true" 
                                                                             data-bv-stringlength-max="20" 
                                                                             disabled = "true"
                                                                             />
                                                            </security:authorize> 
                                                            <span class="help-block" id="promedioMinimoMessage" />
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label class="col-sm-3 control-label">Modalidad</label>
                                                        <div class="col-sm-8">
                                                            <security:authorize ifAnyGranted="ROLE_JEFEBECAS, ROLE_RESPONSABLE_UA, ROLE_ANALISTABECAS">
                                                                <s:select name="alumno.datosAcademicos.modalidad.id" 
                                                                          list="ambiente.modalidadList" 
                                                                          listKey="id" listValue="nombre"
                                                                          headerValue = 'Selecciona una opción'
                                                                          headerKey = '-1'
                                                                          cssClass="form-control"
                                                                          required="true" 
                                                                          data-bv-notempty="true"
                                                                          data-bv-notempty-message="La modalidad es requerida"/>
                                                            </security:authorize> 
                                                            <security:authorize ifNotGranted="ROLE_JEFEBECAS, ROLE_RESPONSABLE_UA, ROLE_ANALISTABECAS">
                                                                <s:select name="alumno.datosAcademicos.modalidad.id" 
                                                                          list="ambiente.modalidadList" 
                                                                          listKey="id" listValue="nombre"
                                                                          headerValue = 'Selecciona una opción'
                                                                          headerKey = '-1'
                                                                          cssClass="form-control"
                                                                          required="true" 
                                                                          data-bv-notempty="true"
                                                                          data-bv-notempty-message="La modalidad es requerida"
                                                                          disabled = "true"
                                                                          />
                                                            </security:authorize>
                                                            <span class="help-block" id="modalidadMessage" />
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label class="col-sm-3 control-label">Carga</label>
                                                        <div class="col-sm-8">
                                                            <security:authorize ifAnyGranted="ROLE_JEFEBECAS, ROLE_RESPONSABLE_UA, ROLE_ANALISTABECAS">
                                                                <s:select name="alumno.datosAcademicos.cumpleCargaMinima" list="service.carga" 
                                                                          headerKey=""
                                                                          cssClass="form-control"
                                                                          headerValue = 'Selecciona una opción'
                                                                          headerKey = '-1'
                                                                          required="true" 
                                                                          data-bv-notempty="true"
                                                                          data-bv-notempty-message="Campo requerido"/>
                                                            </security:authorize> 
                                                            <security:authorize ifNotGranted="ROLE_JEFEBECAS, ROLE_RESPONSABLE_UA, ROLE_ANALISTABECAS">
                                                                <s:select name="alumno.datosAcademicos.cumpleCargaMinima" list="service.carga" 
                                                                          headerKey=""
                                                                          cssClass="form-control"
                                                                          headerValue = 'Selecciona una opción'
                                                                          headerKey = '-1'
                                                                          required="true" 
                                                                          data-bv-notempty="true"
                                                                          data-bv-notempty-message="Campo requerido"
                                                                          disabled = "true"
                                                                          />
                                                            </security:authorize>
                                                            <span class="help-block" id="cargaMessage" />
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label class="col-sm-3 control-label">Materias Reprobadas</label>
                                                        <div class="col-sm-8">
                                                            <security:authorize ifAnyGranted="ROLE_JEFEBECAS, ROLE_RESPONSABLE_UA, ROLE_ANALISTABECAS">
                                                                <s:textfield cssClass="form-control"
                                                                             name="alumno.datosAcademicos.reprobadas" 
                                                                             id="materias-reprobadas" 
                                                                             placeholder="Materias reprobadas"
                                                                             data-bv-message="Este dato no es válido"
                                                                             required="true" 
                                                                             data-bv-notempty="true"
                                                                             data-bv-notempty-message="Las materias reprobadas son un campo requerido"
                                                                             pattern="^[0-9][0-9]?$" 
                                                                             data-bv-regexp-message="Solo se permiten numeros 0 - 99"
                                                                             />
                                                            </security:authorize> 
                                                            <security:authorize ifNotGranted="ROLE_JEFEBECAS, ROLE_RESPONSABLE_UA, ROLE_ANALISTABECAS">
                                                                <s:textfield cssClass="form-control"
                                                                             name="alumno.datosAcademicos.reprobadas" 
                                                                             id="materias-reprobadas" 
                                                                             placeholder="Materias reprobadas"
                                                                             data-bv-message="Este dato no es válido"
                                                                             required="true" 
                                                                             data-bv-notempty="true"
                                                                             data-bv-notempty-message="Las materias reprobadas son un campo requerido"
                                                                             pattern="^[0-9][0-9]?$" 
                                                                             data-bv-regexp-message="Solo se permiten numeros 0 - 99"
                                                                             disabled = "true"
                                                                             />
                                                            </security:authorize> 
                                                            <span class="help-block" id="materiasReprobadasMessage" />
                                                        </div>
                                                    </div>
                                                    <div class="form-group col-sm-offset-3">
                                                        <div class="form-check checkbox-nice checkbox-inline">
                                                            <input id="checkbox-inscrito" 
                                                                   type="checkbox" 
                                                                   name="checkboxInscripcion" 
                                                                   <security:authorize ifNotGranted="ROLE_JEFEBECAS, ROLE_RESPONSABLE_UA, ROLE_ANALISTABECAS">disabled</security:authorize> 
                                                                   >
                                                            <label for="checkbox-inscrito">
                                                                Inscripción
                                                            </label>
                                                        </div>
                                                    </div>
                                                    <div class="form-group col-sm-offset-3">
                                                        <div class="form-check checkbox-nice checkbox-inline">
                                                            <input id="checkbox-regular" 
                                                                   type="checkbox" 
                                                                   name="checkboxRegular" 
                                                                   <security:authorize ifNotGranted="ROLE_JEFEBECAS, ROLE_RESPONSABLE_UA, ROLE_ANALISTABECAS">disabled</security:authorize>
                                                                   >
                                                            <label for="checkbox-regular">
                                                                Regularidad
                                                            </label>
                                                        </div>
                                                    </div>
                                                    <div class="form-group col-sm-offset-3">
                                                        <div class="form-check checkbox-nice checkbox-inline">
                                                            <input id="checkbox-egresado" 
                                                                   type="checkbox" 
                                                                   name="checkboxEgresado" 
                                                                   <security:authorize ifNotGranted="ROLE_JEFEBECAS, ROLE_RESPONSABLE_UA, ROLE_ANALISTABECAS">disabled</security:authorize>
                                                                   >
                                                            <label for="checkbox-egresado">
                                                                Egresado
                                                            </label>
                                                        </div>
                                                    </div>
                                                    <security:authorize ifAnyGranted="ROLE_JEFEBECAS,ROLE_ANALISTABECAS, ROLE_RESPONSABLE_UA">
                                                        <div class="form-group">
                                                            <div class="col-sm-12">
                                                                <button type="submit" class="responsable btn btn-primary pull-right solo-lectura">Guardar</button>
                                                            </div>
                                                            <div class="clearfix" >&nbsp;</div> 
                                                        </div>
                                                    </security:authorize>
                                                    <s:hidden name="alumno.id"/>
                                                    <s:hidden name="alumno.boleta"/>
                                                    <s:hidden name="permisoCambio"/>
                                                    <s:hidden name="salariosId"/>
                                                    <s:hidden name="eseF"/>
                                                </s:if>
                                                <s:else>
                                                    <div class="form-group">
                                                        <label class="col-sm-3 control-label">Semestre</label>
                                                        <div class="col-sm-8">
                                                            <s:textfield cssClass="form-control"
                                                                         name="alumno.datosAcademicos.semestre" 
                                                                         id="semestre" 
                                                                         placeholder="Semestre"
                                                                         readonly = "true"/>
                                                            <span class="help-block" id="semestreMessage" />
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label class="col-sm-3 control-label">Promedio</label>
                                                        <div class="col-sm-8">
                                                            <s:textfield cssClass="form-control"
                                                                         name="promedioA" 
                                                                         id="promedioMinimo" 
                                                                         placeholder="Promedio"
                                                                         readonly = "true"/>
                                                            <span class="help-block" id="promedioMinimoMessage" />
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label class="col-sm-3 control-label">Modalidad</label>
                                                        <div class="col-sm-8">
                                                            <s:select name="alumno.datosAcademicos.modalidad.id" 
                                                                      list="ambiente.modalidadList" 
                                                                      listKey="id" listValue="nombre"
                                                                      headerValue = 'Selecciona una opción'
                                                                      headerKey = '-1'
                                                                      cssClass="form-control"
                                                                      disabled = "true"
                                                                      />
                                                            <span class="help-block" id="modalidadMessage" />
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label class="col-sm-3 control-label">Carga</label>
                                                        <div class="col-sm-8">
                                                            <s:select name="alumno.datosAcademicos.cumpleCargaMinima" list="service.carga" 
                                                                      headerKey=""
                                                                      cssClass="form-control"
                                                                      headerValue = 'Selecciona una opción'
                                                                      headerKey = '-1'
                                                                      disabled = "true"
                                                                      />
                                                            <span class="help-block" id="cargaMessage" />
                                                        </div>
                                                    </div>
                                                    <div class="form-group col-sm-offset-3">
                                                        <div class="form-check checkbox-nice checkbox-inline">
                                                            <input id="checkbox-inscrito" type="checkbox" name="checkboxInscripcion" disabled>
                                                            <label for="checkbox-inscrito">
                                                                Inscripción
                                                            </label>
                                                        </div>
                                                    </div>
                                                    <div class="form-group col-sm-offset-3">
                                                        <div class="form-check checkbox-nice checkbox-inline">
                                                            <input id="checkbox-regular" type="checkbox" name="checkboxRegular" disabled>
                                                            <label for="checkbox-regular">
                                                                Regularidad
                                                            </label>
                                                        </div>
                                                    </div>
                                                    <div class="form-group col-sm-offset-3">
                                                        <div class="form-check checkbox-nice checkbox-inline">
                                                            <input id="checkbox-egresado" type="checkbox" name="checkboxEgresado" disabled>
                                                            <label for="checkbox-egresado">
                                                                Egresado
                                                            </label>
                                                        </div>
                                                    </div>
                                                </s:else>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="tab-pane text-style" id="cotejar">    
                            <h1>Validación de documentos</h1>
                            <s:if test="( valDocs) ">
                                <s:if test="( validacionInscripcion) ">
                                    <div class="row">
                                        <div class="col-lg-12">
                                            <div class="alert alert-warning">
                                                <i class="fa fa-warning fa-fw fa-lg"></i>
                                                <strong>¡Atención!</strong> Los datos que se muestran son del periodo pasado debido a que se ejecutó el proceso de "Validación de Inscripción" en este periodo.
                                            </div>
                                        </div>
                                    </div>
                                </s:if>
                                <s:if test="otorgamientoActual">
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <div class="alert alert-info">
                                                <i class="fa fa-info-circle fa-fw fa-lg"></i>
                                                <strong>Toma en cuenta</strong> El alumno cuenta con un otorgamiento en el periodo actual, por lo que no se puede editar la información.
                                            </div>
                                        </div>
                                    </div>
                                </s:if>
                                <s:if test="clasificacionSolicitudBeca">
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <div class="alert alert-info">
                                                <i class="fa fa-info-circle fa-fw fa-lg"></i>
                                                <strong>Toma en cuenta</strong> El alumno cuenta con una solicitud otorgada, rechazada o en espera, por lo que no se puede editar la información.
                                            </div>
                                        </div>
                                    </div>
                                </s:if>
                                <div class="row">
                                    <div class="col-lg-12">
                                        <div class="main-box clearfix">
                                            <div class="main-box-body">
                                                <form id="cotejarForm" action="/becas/cotejarAsignaciones.action" method="post" class="form-horizontal">
                                                    <s:if test="!otorgamientoActual && !clasificacionSolicitudBeca">
                                                        <div class="form-group">
                                                            <label class="col-sm-8 control-label">¿Documentación básica completa?</label>
                                                            <div class="col-sm-4">
                                                                <label class="radio-inline"><input id="documentacionBasicaCompleta" value="1" type="radio" name="documentacionBasicaCompleta">Si</label>
                                                                <label class="radio-inline"><input id="documentacionBasicaIncompleta" value="0" type="radio" name="documentacionBasicaCompleta">No</label>
                                                            </div>                                   
                                                        </div>
                                                        <table class="table table-bordered">
                                                            <thead>
                                                                <tr>
                                                                    <th>Documento</th>
                                                                    <th class="text-center">Estado</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>      
                                                                <tr>
                                                                    <td>
                                                                        <s:set var="alumno" value="alumno"/>
                                                                        Estudio socioeconómico
                                                                    </td>
                                                                    <td class="text-center">
                                                                        <s:checkbox name="documentos.estudioSocioeconomico" fieldValue="true"/>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td>
                                                                        Carta compromiso
                                                                    </td>
                                                                    <td class="text-center">
                                                                        <s:checkbox name="documentos.cartaCompromiso" fieldValue="true"/>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td>
                                                                        CURP
                                                                    </td>
                                                                    <td class="text-center">
                                                                        <s:checkbox name="documentos.curp" fieldValue="true"/>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td>
                                                                        Comprobante de ingresos y egresos
                                                                    </td>
                                                                    <td class="text-center">
                                                                        <s:checkbox name="documentos.comprobanteIngresosEgresos" fieldValue="true"/>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td>
                                                                        Acuse del SUBES
                                                                        <br>(Opcional para los alumnos de Beca Manutención)
                                                                    </td>
                                                                    <td class="text-center">
                                                                        <s:checkbox name="documentos.acuseSubes" fieldValue="true"/>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td>
                                                                        Acuse del SUBES Transporte
                                                                        <br>(Opcional para los alumnos de Beca Manutención)
                                                                    </td>
                                                                    <td class="text-center">
                                                                        <s:checkbox id="acuseSubes" name="documentos.acuseSubesTransporte" fieldValue="true"/>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td>
                                                                        Estudio socioeconómico Transporte
                                                                        <br>(Opcional para los alumnos de Beca de Transporte Institucional)
                                                                    </td>
                                                                    <td class="text-center">
                                                                        <s:checkbox name="documentos.estudiosocieconomicotransporte" fieldValue="true"/>
                                                                    </td>
                                                                </tr>
                                                            </tbody>
                                                        </table>
                                                        <security:authorize ifAnyGranted="ROLE_JEFEBECAS,ROLE_ANALISTABECAS,ROLE_RESPONSABLE_UA">
                                                            <div class="form-group">
                                                                <div class="col-sm-12">
                                                                    <button type="submit" class="responsable btn btn-primary pull-right solo-lectura">Guardar</button>
                                                                </div>
                                                            </div>
                                                        </security:authorize>
                                                        <s:hidden name="alumno.id"/>
                                                        <s:hidden name="alumno.boleta"/>
                                                        <s:hidden name="documentos.id"/>
                                                        <s:hidden name="permisoCambio"/>
                                                        <s:hidden name="salariosId"/>
                                                        <s:hidden name="eseF"/>
                                                    </s:if>
                                                    <s:else>
                                                        <div class="form-group">
                                                            <label class="col-sm-8 control-label">¿Documentación básica completa?</label>
                                                            <div class="col-sm-4">
                                                                <label class="radio-inline"><input id="documentacionBasicaCompleta" value="1" type="radio" name="documentacionBasicaCompleta" disabled="true">Si</label>
                                                                <label class="radio-inline"><input id="documentacionBasicaIncompleta" value="0" type="radio" name="documentacionBasicaCompleta" disabled="true">No</label>
                                                            </div>                                   
                                                        </div>
                                                        <table class="table table-bordered">
                                                            <thead>
                                                                <tr>
                                                                    <th>Documento</th>
                                                                    <th class="text-center">Estado</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>      
                                                                <tr>
                                                                    <td>
                                                                        <s:set var="alumno" value="alumno"/>
                                                                        Estudio socioeconómico
                                                                    </td>
                                                                    <td class="text-center">
                                                                        <s:checkbox name="documentos.estudioSocioeconomico" fieldValue="true" disabled="true"/>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td>
                                                                        Carta compromiso
                                                                    </td>
                                                                    <td class="text-center">
                                                                        <s:checkbox name="documentos.cartaCompromiso" fieldValue="true" disabled="true"/>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td>
                                                                        CURP
                                                                    </td>
                                                                    <td class="text-center">
                                                                        <s:checkbox name="documentos.curp" fieldValue="true" disabled="true"/>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td>
                                                                        Comprobante de ingresos y egresos
                                                                    </td>
                                                                    <td class="text-center">
                                                                        <s:checkbox name="documentos.comprobanteIngresosEgresos" fieldValue="true" disabled="true"/>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td>
                                                                        Acuse del SUBES
                                                                        <br>(Opcional para los alumnos de Beca Manutención)
                                                                    </td>
                                                                    <td class="text-center">
                                                                        <s:checkbox name="documentos.acuseSubes" fieldValue="true" disabled="true"/>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td>
                                                                        Acuse del SUBES Transporte
                                                                        <br>(Opcional para los alumnos de Beca Manutención)
                                                                    </td>
                                                                    <td class="text-center">
                                                                        <s:checkbox id="acuseSubes" name="documentos.acuseSubesTransporte" fieldValue="true" disabled="true"/>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td>
                                                                        Estudio socioeconómico Transporte
                                                                        <br>(Opcional para los alumnos de Beca de Transporte Institucional)
                                                                    </td>
                                                                    <td class="text-center">
                                                                        <s:checkbox name="documentos.estudiosocieconomicotransporte" fieldValue="true" disabled="true"/>
                                                                    </td>
                                                                </tr>
                                                            </tbody>
                                                        </table>
                                                    </s:else>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </s:if>
                            <s:else>
                                <div class="row">
                                    <div class="col-lg-12">
                                        <div class="alert alert-warning">
                                            <i class="fa fa-warning fa-fw fa-lg"></i>
                                            <strong>¡Atención!</strong> No existen documentos para el periodo anterior.
                                        </div>
                                    </div>
                                </div>
                            </s:else>
                        </div>

                        <div class="tab-pane text-style" id="salarios">
                            <h1>Salarios mínimos</h1>
                            <s:if test="( eseF) ">
                                <s:if test="( validacionInscripcion) ">
                                    <div class="row">
                                        <div class="col-lg-12">
                                            <div class="alert alert-warning">
                                                <i class="fa fa-warning fa-fw fa-lg"></i>
                                                <strong>¡Atención!</strong> Los datos que se muestran son del periodo pasado debido a que se ejecutó el proceso de "Validación de Inscripción" en este periodo.
                                            </div>
                                        </div>
                                    </div>
                                </s:if>
                                <s:if test="otorgamientoActual">
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <div class="alert alert-info">
                                                <i class="fa fa-info-circle fa-fw fa-lg"></i>
                                                <strong>Toma en cuenta</strong> El alumno cuenta con un otorgamiento en el periodo actual, por lo que no se puede editar la información.
                                            </div>
                                        </div>
                                    </div>
                                </s:if>
                                <s:if test="clasificacionSolicitudBeca">
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <div class="alert alert-info">
                                                <i class="fa fa-info-circle fa-fw fa-lg"></i>
                                                <strong>Toma en cuenta</strong> El alumno cuenta con una solicitud otorgada, rechazada o en espera, por lo que no se puede editar la información.
                                            </div>
                                        </div>
                                    </div>
                                </s:if>
                                <div class="row">
                                    <div class="col-lg-12">
                                        <div class="main-box clearfix">
                                            <div class="main-box-body">
                                                <form id="editarForm" action="/misdatos/salariosPermisoCambio.action" method="post"  class="form-horizontal">
                                                    <div class="form-group">
                                                        <label class="col-sm-3 control-label">Salarios mínimos (actuales)</label>
                                                        <div class="col-sm-8">
                                                            <s:select  
                                                                name="salariosA"
                                                                cssClass="form-control"
                                                                list="respuestas" 
                                                                listKey="id" 
                                                                listValue="nombre" 
                                                                data-bv-notempty="true"
                                                                disabled="true"
                                                                data-bv-notempty-message="Los salarios son requeridos" />
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label class="col-sm-3 control-label">Salarios mínimos</label>
                                                        <div class="col-sm-8">
                                                            <s:if test="!otorgamientoActual && !clasificacionSolicitudBeca">
                                                                <s:select  
                                                                    name="salariosN"
                                                                    cssClass="form-control"
                                                                    list="respuestas" 
                                                                    listKey="id" 
                                                                    listValue="nombre" 
                                                                    data-bv-notempty="true"
                                                                    data-bv-notempty-message="Los salarios son requeridos" />
                                                            </s:if>
                                                            <s:else>
                                                                <s:select  
                                                                    name="salariosN"
                                                                    cssClass="form-control"
                                                                    list="respuestas" 
                                                                    listKey="id" 
                                                                    listValue="nombre" 
                                                                    disabled = "true" />
                                                            </s:else>
                                                        </div>
                                                    </div>
                                                    <security:authorize ifAnyGranted="ROLE_JEFEBECAS,ROLE_ANALISTABECAS">
                                                        <s:if test="( !otorgamientoActual) ">
                                                            <div class="form-group">
                                                                <div class="col-sm-12">
                                                                    <button type="submit" class="responsable btn btn-primary pull-right solo-lectura">Guardar</button>
                                                                </div>
                                                                <div class="clearfix" >&nbsp;</div>
                                                            </div>
                                                        </s:if>
                                                    </security:authorize>
                                                    <s:hidden name="alumno.id"/>
                                                    <s:hidden name="alumno.boleta"/>
                                                    <s:hidden name="permisoCambio"/>
                                                    <s:hidden name="salariosId"/>
                                                    <s:hidden name="eseF"/>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </s:if>
                            <s:else>
                                <div class="row">
                                    <div class="col-lg-12">
                                        <div class="alert alert-warning">
                                            <i class="fa fa-warning fa-fw fa-lg"></i>
                                            <strong>¡Atención!</strong> El alumno no ha contestado su ESE
                                        </div>
                                    </div>
                                </div>
                            </s:else>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<s:hidden name="privilegio" />
<s:hidden name="error"  />
<s:hidden name="regular"  />
</body>
<content tag="endScripts">
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>
    <script type="text/javascript" src="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.js"></script>
</content>
<content tag="inlineScripts">
    <script>
        $(document).ready(function () {
            var daEgresado = <s:property value="alumno.datosAcademicos.egresado"/>;
            if (daEgresado === 1) {
                $("#checkbox-egresado").attr('checked', 'checked');
            }
            var daRegular = <s:property value="alumno.datosAcademicos.regular"/>;
            if (daRegular === 1) {
                $("#checkbox-regular").attr('checked', 'checked');
            }
            var daInscrito = <s:property value="alumno.datosAcademicos.inscrito"/>;
            if (daInscrito === 1) {
                $("#checkbox-inscrito").attr('checked', 'checked');
            }
            
            $("#cotejarForm").bootstrapValidator({});
            $("#editarForm").bootstrapValidator({});
            $("#estatusForm").bootstrapValidator({});

            if ($("#documentos_estudioSocioeconomico:checked").val()
                    && $("#documentos_cartaCompromiso:checked").val()
                    && $("#documentos_curp:checked").val()
                    && $("#documentos_comprobanteIngresosEgresos:checked").val()) {
                $("#documentacionBasicaCompleta").attr('checked', 'checked');
            } else {
                $("#documentacionBasicaIncompleta").attr('checked', 'checked');
            }
            $("input[name=documentacionBasicaCompleta]").on("change", function () {
                if ($("input[name=documentacionBasicaCompleta]:checked").val() === "1") {
                    $("#documentos_estudioSocioeconomico").prop("checked", true);
                    $("#documentos_cartaCompromiso").prop("checked", true);
                    $("#documentos_curp").prop("checked", true);
                    $("#documentos_comprobanteIngresosEgresos").prop("checked", true);
                } else {
                    $("#documentos_estudioSocioeconomico").prop("checked", false);
                    $("#documentos_cartaCompromiso").prop("checked", false);
                    $("#documentos_curp").prop("checked", false);
                    $("#documentos_comprobanteIngresosEgresos").prop("checked", false);
                }
            });
            getPlanE();
        });

        function getPlanE() {
            $.ajax({
                type: 'POST',
                url: '/ajax/getPlanEPermisoCambioAjax.action',
                dataType: 'json',
                data: {ua: Number($('#unidadAcademica').val())},
                cache: false,
                success: function (aData) {
                    $('#planE').find('option').remove();
                    $('#planE').append(new Option("Selecciona una opción", "-1"));
                    $.each(aData.data, function (i, item) {
                        $('#planE').append(new Option(item[1], item[0]));
                    });
                    var plan = "<s:property value="alumno.datosAcademicos.carrera.planEstudios"/>";
                    if (plan !== "")
                        $("#planE").val(plan);
                    getCarrera();
                },
                error: function () {
                    BootstrapDialog.alert({
                        title: 'Atención',
                        message: 'Hubo un problema que impidió que se completara la operación.',
                        type: BootstrapDialog.TYPE_DANGER
                    });
                }
            });
            return false;
        }

        function getCarrera() {
            $.ajax({
                type: 'POST',
                url: '/ajax/getCarreraPermisoCambioAjax.action',
                dataType: 'json',
                data: {ua: Number($('#unidadAcademica').val()),
                    plan: String($('#planE').val())
                },
                cache: false,
                success: function (aData) {
                    $('#carrera').find('option').remove();
                    $('#carrera').append(new Option("Selecciona una opción", "-1"));
                    $.each(aData.data, function (i, item) {
                        $('#carrera').append(new Option(item[1], item[0]));
                    });
                    var carrera = "<s:property value='alumno.datosAcademicos.carrera.claveCarrera'/>";
                    if (carrera !== "")
                        $("#carrera").val(carrera);
                    getEspecialidad();
                },
                error: function () {
                    BootstrapDialog.alert({
                        title: 'Atención',
                        message: 'Hubo un problema que impidió que se completara la operación.',
                        type: BootstrapDialog.TYPE_DANGER
                    });
                }
            });
            return false;
        }

        function getEspecialidad() {
            $.ajax({
                type: 'POST',
                url: '/ajax/getEspecialidadPermisoCambioAjax.action',
                dataType: 'json',
                data: {ua: Number($('#unidadAcademica').val()),
                    plan: String($('#planE').val()),
                    crr: String($('#carrera').val())
                },
                cache: false,
                success: function (aData) {
                    $('#especialidad').find('option').remove();
                    $('#especialidad').append(new Option("Selecciona una opción", "-1"));
                    $.each(aData.data, function (i, item) {
                        $('#especialidad').append(new Option(item[1], item[0]));
                    });
                    var esp = "<s:property value='alumno.datosAcademicos.carrera.especialidad'/>";
                    if (esp !== "")
                        $("#especialidad").val(esp);
                },
                error: function () {
                    BootstrapDialog.alert({
                        title: 'Atención',
                        message: 'Hubo un problema que impidió que se completara la operación.',
                        type: BootstrapDialog.TYPE_DANGER
                    });
                }
            });
            return false;
        }

    </script>
</content>
