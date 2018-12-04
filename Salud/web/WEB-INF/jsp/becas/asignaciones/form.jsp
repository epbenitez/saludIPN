<%-- 
    Document   : Template SIBEC
    Created on : 28-Septiembre-2016
    Author     : Gustavo Adolfo Alamillo Medina
--%>
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

        .nav-sidebar { 
            width: 100%;
            padding: 30px 0; 
            border-right: 1px solid #ddd;
        }
        .nav-sidebar a {
            color: #333;
            -webkit-transition: all 0.08s linear;
            -moz-transition: all 0.08s linear;
            -o-transition: all 0.08s linear;
            transition: all 0.08s linear;
        }

        .nav-sidebar .active a { 
            cursor: default;
            background-color: #03A9F4; 
            color: #fff; 
        }

        .nav-sidebar .text-overflow a,
        .nav-sidebar .text-overflow .media-body {
            white-space: nowrap;
            overflow: hidden;
            -o-text-overflow: ellipsis;
            text-overflow: ellipsis; 
        }

        .cuest-link {
            margin-bottom: 5%;
        }

    </style>
</head> 

<content tag="tituloJSP">
    Asignación de becas
</content>

<body>

    <div class="row">
        <div class="col-sm-12">
            <s:if test="errorSemestre">
                <div class="alert alert-danger">
                    <i class="fa fa-times-circle fa-fw fa-lg"></i>
                    <strong>&iexcl;Error!</strong> El semestre no es valido.
                </div>
            </s:if>
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
        <div class="col-xs-12">
            <div class="main-box clearfix" >
                <div class="col-sm-3">
                    <nav class="nav-sidebar">
                        <ul class="nav tabs">
                            <li class="active"><a href="#datos" data-toggle="tab">Datos personales </a></li>
                            <li class=""><a href="#editar" data-toggle="tab">Editar datos académicos</a></li>                               
                            <li class=""><a href="#estatus" data-toggle="tab">Editar estatus de inscripción y regularidad</a></li>                               
                            <li class=""><a href="#cotejar" data-toggle="tab">Cotejar documentos</a></li>                               
                                <s:if test="( cumpleRequisitos && (tab==0||tab==1)) ">
                                <li class=""><a href="#cambiar" data-toggle="tab">Asignación de beca</a></li>                               
                                </s:if>
                                <s:if test="( tab==0||tab==2) && (tipoProceso!= 1 && tipoProceso!= 7)">
                                <li class=""><a href="#rechazar" data-toggle="tab">Rechazar Solicitud</a></li>                               
                                </s:if>
                                <%--<s:if test="( tab==0||tab==3) ">
                                <li class=""><a href="#espera" data-toggle="tab">Lista de Espera</a></li>                               
                                </s:if>--%>
                            <li class=""><a href="#validar" data-toggle="tab">Validación de Requisitos</a></li>                               
                        </ul> 
                    </nav>
                </div>
                <!-- tab content -->
                <div class="col-sm-9">
                    <div class="tab-content">
                        <div class="tab-pane active text-style" id="datos">
                            <br/>
                            <div class="row">
                                <div class="col-lg-12 col-md-12 col-sm-12">
                                    <div class="main-box clearfix">
                                        <div class="tabs-wrapper profile-tabs">
                                            <ul class="nav nav-tabs" style="margin-bottom: 0px;">
                                                <li class="active">
                                                    <a href="#personales-tab" data-toggle="tab">
                                                        Personal
                                                        <i class="fa"></i>
                                                    </a>
                                                </li>
                                                <li>
                                                    <a href="#academicos-tab" data-toggle="tab">
                                                        Académica 
                                                        <i class="fa"></i>
                                                    </a>
                                                </li>
                                                <li>
                                                    <a href="#direccion-tab" data-toggle="tab">
                                                        Dirección
                                                        <i class="fa"></i>
                                                    </a>
                                                </li>
                                                <li>
                                                    <a href="#contacto-tab" data-toggle="tab">
                                                        Contacto
                                                        <i class="fa"></i>
                                                    </a>
                                                </li>
                                            </ul>
                                            <div class="main-box-body clearfix">
                                                <form id="accountForm" method="post" class="form-horizontal" style="margin-top: 20px;"  data-toggle="validator" role="form">
                                                    <div class="tab-content">
                                                        <div class="tab-pane active" id="personales-tab">
                                                            <div class="form-group">
                                                                <label class="col-sm-3 control-label">Nombre(s)</label>
                                                                <div class="col-sm-8">
                                                                    <s:textfield cssClass="form-control" 
                                                                                 name="alumno.nombre"
                                                                                 id="nombre" 
                                                                                 placeholder="Nombre"
                                                                                 readonly="true"/>
                                                                </div>
                                                            </div>
                                                            <div class="form-group">
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
                                                            <div class="form-group">
                                                                <label class="col-sm-3 control-label">CURP</label>
                                                                <div class="col-sm-8">
                                                                    <s:textfield cssClass="form-control" 
                                                                                 name="alumno.curp" 
                                                                                 id="curp" 
                                                                                 placeholder="CURP" 
                                                                                 readonly="true"/>
                                                                </div>
                                                            </div>
                                                            <div class="form-group">
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

                                                            <div class="form-group">
                                                                <label class="col-sm-3 control-label">Entidad de nacimiento</label>
                                                                <div class="col-sm-8">
                                                                    <s:textfield cssClass="form-control" 
                                                                                 name="alumno.entidadDeNacimiento.nombre"
                                                                                 id="estadosDireccion"
                                                                                 placeholder="Entidad de nacimiento"
                                                                                 readonly="true"/>
                                                                </div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-sm-3 control-label">Género</label>
                                                                <div class="col-sm-8">
                                                                    <s:textfield cssClass="form-control" 
                                                                                 name="alumno.genero.nombre" 
                                                                                 id="genero" 
                                                                                 placeholder="Género"
                                                                                 readonly="true"/>
                                                                </div>
                                                            </div>
                                                            <div class="form-group">
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
                                                        <div class="tab-pane" id="academicos-tab">
                                                            <div class="form-group">
                                                                <label class="col-sm-3 control-label">Boleta</label>
                                                                <div class="col-sm-8">
                                                                    <s:textfield cssClass="form-control"
                                                                                 name="alumno.boleta" 
                                                                                 id="boleta" 
                                                                                 placeholder="Boleta"
                                                                                 readonly="true"/>
                                                                </div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-sm-3 control-label">Unidad Académica</label>
                                                                <div class="col-sm-8">
                                                                    <s:textfield cssClass="form-control" 
                                                                                 name="alumno.datosAcademicos.unidadAcademica.nombreCorto" 
                                                                                 id="boleta"
                                                                                 placeholder="Unidad Académica"
                                                                                 readonly="true"/>
                                                                </div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-sm-3 control-label">Semestre</label>
                                                                <div class="col-sm-8">
                                                                    <s:textfield cssClass="form-control" 
                                                                                 name="alumno.datosAcademicos.semestre"
                                                                                 id="semestre" 
                                                                                 placeholder="Semestre"
                                                                                 readonly="true"/>
                                                                </div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-sm-3 control-label">Promedio</label>
                                                                <div class="col-sm-8">
                                                                    <s:textfield cssClass="form-control" 
                                                                                 name="alumno.datosAcademicos.promedio" 
                                                                                 id="promedio"
                                                                                 placeholder="Promedio"
                                                                                 readonly="true"/>
                                                                </div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-sm-3 control-label">Carrera</label>
                                                                <div class="col-sm-8">
                                                                    <s:textfield cssClass="form-control" 
                                                                                 name="alumno.datosAcademicos.carrera.carrera" 
                                                                                 id="carrera"
                                                                                 placeholder="Carrera"
                                                                                 readonly="true"/>
                                                                </div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-sm-3 control-label">Plan de estudios</label>
                                                                <div class="col-sm-8">
                                                                    <s:textfield cssClass="form-control"
                                                                                 name="alumno.datosAcademicos.carrera.planEstudios" 
                                                                                 id="planDeEstudios" 
                                                                                 placeholder="Plan de estudios"
                                                                                 readonly="true"/>
                                                                </div>
                                                            </div>
                                                            <div class="form-group col-sm-12">
                                                                <div class="col-sm-offset-3">
                                                                    <div class="form-check checkbox-nice checkbox-inline">
                                                                        <input id="checkbox-egresado-read" 
                                                                               class="col-sm-3"
                                                                               type="checkbox" 
                                                                               name="checkboxEgresadoRead" 
                                                                                disabled
                                                                               >
                                                                        <label for="checkbox-egresado-read">
                                                                            Egresado
                                                                        </label>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="tab-pane" id="direccion-tab">
                                                            <div class="form-group">
                                                                <label class="col-sm-3 control-label">Calle</label>
                                                                <div class="col-sm-8">
                                                                    <s:textfield cssClass="form-control"
                                                                                 name="alumno.direccion.calle" 
                                                                                 id="calle" 
                                                                                 placeholder="Calle"
                                                                                 readonly="true"/>
                                                                </div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-sm-3 control-label">Número</label>
                                                                <div class="col-sm-4">
                                                                    <s:textfield cssClass="form-control" 
                                                                                 name="alumno.direccion.numeroExterior" 
                                                                                 id="numeroExterior" 
                                                                                 placeholder="Número exterior"
                                                                                 readonly="true"/>
                                                                </div>
                                                                <div class="col-sm-4">
                                                                    <s:textfield cssClass="form-control"
                                                                                 name="alumno.direccion.numeroInterior" 
                                                                                 id="numeroInterior"
                                                                                 placeholder="Número interior"
                                                                                 readonly="true"/>
                                                                </div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-sm-3 control-label">Entidad</label>
                                                                <div class="col-sm-8">
                                                                    <s:textfield cssClass="form-control"
                                                                                 name="alumno.direccion.relacionGeografica.estado.nombre"
                                                                                 id="estado" 
                                                                                 placeholder="Entidad"
                                                                                 readonly="true"/>
                                                                </div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-sm-3 control-label">Municipio</label>
                                                                <div class="col-sm-8">
                                                                    <s:textfield cssClass="form-control" 
                                                                                 name="alumno.direccion.relacionGeografica.municipio.nombre" 
                                                                                 id="municipio" 
                                                                                 placeholder="Municipio"
                                                                                 readonly="true"/>
                                                                </div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-sm-3 control-label">Colonia</label>
                                                                <div class="col-sm-8">
                                                                    <s:textfield cssClass="form-control"
                                                                                 name="alumno.direccion.relacionGeografica.colonia.nombre" 
                                                                                 id="colonia" 
                                                                                 placeholder="Colonia"
                                                                                 readonly="true"/>
                                                                </div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-sm-3 control-label">Código Postal</label>
                                                                <div class="col-sm-8">
                                                                    <s:textfield cssClass="form-control" 
                                                                                 name="alumno.direccion.codigoPostal"
                                                                                 id="codigoPostal" 
                                                                                 placeholder="Código Postal"
                                                                                 readonly="true"/>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="tab-pane" id="contacto-tab">
                                                            <div class="form-group">
                                                                <label class="col-sm-3 control-label">Teléfono celular / Casa</label>
                                                                <div class="col-sm-8">
                                                                    <s:textfield cssClass="form-control" 
                                                                                 name="alumno.celular"
                                                                                 id="celular" 
                                                                                 placeholder="55-55555555"
                                                                                 readonly="true"/>
                                                                </div>
                                                            </div> 
                                                            <div class="form-group">
                                                                <label class="col-sm-3 control-label">Compañía celular</label>
                                                                <div class="col-sm-8">
                                                                    <s:textfield cssClass="form-control" 
                                                                                 name="alumno.companiaCelular.nombre" 
                                                                                 id="companiaCelular" 
                                                                                 placeholder="55-55555555"
                                                                                 readonly="true"/>
                                                                </div>
                                                            </div>
                                                            <div class="form-group">
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
                                                </form>      
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>                        
                        <div class="tab-pane text-style" id="editar">
                            <div class="row">
                                <div class="main-box-body clearfix">
                                    <form id="editarForm" action="/becas/editarAsignaciones.action" method="post"  class="form-horizontal">
                                        <security:authorize ifAnyGranted="ROLE_JEFEBECAS, ROLE_ANALISTABECAS, ROLE_RESPONSABLE_UA">
                                            <div class="form-group">
                                                <label class="col-sm-3 control-label">Promedio</label>
                                                <div class="col-sm-8">
                                                    <s:if test="!tab&&procesoActivo">
                                                        <s:textfield cssClass="form-control"
                                                                     name="alumno.datosAcademicos.promedio" 
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
                                                    </s:if>
                                                    <s:else>
                                                        <s:textfield cssClass="form-control"
                                                                     name="alumno.datosAcademicos.promedio" 
                                                                     id="promedioMinimo" 
                                                                     placeholder="Promedio"
                                                                     readonly="true"/>
                                                    </s:else>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-sm-3 control-label">Semestre</label>
                                                <div class="col-sm-8">
                                                    <s:if test="!tab&&procesoActivo">
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
                                                    </s:if>
                                                    <s:else>
                                                        <s:textfield cssClass="form-control"
                                                                     name="alumno.datosAcademicos.semestre" 
                                                                     id="semestre" 
                                                                     placeholder="Semestre"
                                                                     readonly="true"/>
                                                    </s:else>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-sm-3 control-label">CURP</label>
                                                <div class="col-sm-8">
                                                    <s:if test="!tab&&procesoActivo&&(privilegio==3)">
                                                        <s:textfield cssClass="form-control"
                                                                     name="alumno.curp" 
                                                                     id="curp" 
                                                                     placeholder="Curp"
                                                                     data-bv-message="Este dato no es válido"
                                                                     required="true" 
                                                                     data-bv-notempty="true"
                                                                     data-bv-notempty-message="El curp es requerido"
                                                                     pattern="^[a-zA-Z]{4}((\d{2}((0[13578]|1[02])(0[1-9]|[12]\d|3[01])|(0[13456789]|1[012])(0[1-9]|[12]\d|30)|02(0[1-9]|1\d|2[0-8])))|([02468][048]|[13579][26])0229)(H|M)(AS|BC|BS|CC|CL|CM|CS|CH|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|SM|NE)([a-zA-Z]{3})([a-zA-Z0-9\s]{1})\d{1}$" 
                                                                     data-bv-regexp-message="Tu CURP no tiene el formato correcto"
                                                                     data-bv-stringlength="true" 
                                                                     data-bv-stringlength-min="18" 
                                                                     data-bv-stringlength-max="18" 
                                                                     data-bv-stringlength-message="Tu CURP debe estar conformado por 18 caracteres"
                                                                     onblur="$(this).val($(this).val().toUpperCase())"/>
                                                    </s:if>
                                                    <s:else>
                                                        <s:textfield cssClass="form-control"
                                                                     name="alumno.curp" 
                                                                     id="curp" 
                                                                     placeholder="Curp"
                                                                     readonly="true"/>
                                                    </s:else>
                                                    <span class="help-block" id="curpMessage" />
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-sm-3 control-label">Modalidad</label>
                                                <div class="col-sm-8">
                                                    <s:if test="!tab&&procesoActivo">
                                                        <s:select name="alumno.datosAcademicos.modalidad.id" 
                                                                  list="ambiente.modalidadList" 
                                                                  listKey="id" listValue="nombre"
                                                                  cssClass="form-control"
                                                                  required="true" 
                                                                  data-bv-notempty="true"
                                                                  data-bv-notempty-message="La modalidad es requerida"/>
                                                    </s:if>
                                                    <s:else>
                                                        <s:select name="alumno.datosAcademicos.modalidad.id" 
                                                                  list="ambiente.modalidadList" 
                                                                  listKey="id" listValue="nombre"
                                                                  cssClass="form-control"
                                                                  disabled="true"/>
                                                    </s:else>
                                                    <span class="help-block" id="modalidadMessage" />
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-sm-3 control-label">Carga</label>
                                                <div class="col-sm-8">
                                                    <s:if test="!tab&&procesoActivo">
                                                        <s:select name="alumno.datosAcademicos.cumpleCargaMinima" list="service.carga" 
                                                                  headerKey=""
                                                                  cssClass="form-control"
                                                                  required="true" 
                                                                  data-bv-notempty="true"
                                                                  data-bv-notempty-message="Campo requerido"/>
                                                    </s:if>
                                                    <s:else>
                                                        <s:select name="alumno.datosAcademicos.cumpleCargaMinima" list="service.carga" 
                                                                  headerKey=""
                                                                  cssClass="form-control"
                                                                  disabled="true"/>
                                                    </s:else>
                                                    <span class="help-block" id="cargaMessage" />
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-sm-3 control-label">Materias Reprobadas</label>
                                                <div class="col-sm-8">
                                                    <s:if test="!tab&&procesoActivo">
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
                                                    </s:if>
                                                    <s:else>
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
                                                    </s:else>
                                                    <span class="help-block" id="materiasReprobadasMessage" />
                                                </div>
                                            </div>
                                            <div class="form-group col-sm-12">
                                                <div class="col-sm-offset-3">
                                                    <div class="form-check checkbox-nice checkbox-inline">
                                                        <input id="checkbox-egresado" 
                                                               class="col-sm-3"
                                                               type="checkbox" 
                                                               name="checkboxEgresado" 
                                                               <s:if test="tab||(!tab&&!procesoActivo)">disabled</s:if>
                                                               >
                                                        <label for="checkbox-egresado">
                                                            Egresado
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <s:if test="!tab&&procesoActivo">
                                                    <div class="col-sm-12">
                                                        <button type="submit" class="btn btn-primary pull-right">Guardar</button>
                                                    </div>
                                                </s:if>
                                            </div>
                                        </security:authorize>
                                        <security:authorize ifNotGranted="ROLE_JEFEBECAS, ROLE_ANALISTABECAS, ROLE_RESPONSABLE_UA">
                                            <div class="form-group">
                                                <label class="col-sm-2 control-label">Promedio</label>
                                                <div class="col-sm-10">
                                                    <s:textfield cssClass="form-control"
                                                                 name="alumno.datosAcademicos.promedio" 
                                                                 id="promedioMinimo" 
                                                                 placeholder="Promedio"
                                                                 readonly="true"/>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-sm-2 control-label">Semestre</label>
                                                <div class="col-sm-10">
                                                    <s:textfield cssClass="form-control"
                                                                 name="alumno.datosAcademicos.semestre" 
                                                                 id="semestre" 
                                                                 placeholder="Semestre"
                                                                 readonly="true"/>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-sm-2 control-label">CURP</label>
                                                <div class="col-sm-10">
                                                    <s:textfield cssClass="form-control"
                                                                 name="alumno.curp" 
                                                                 id="curp" 
                                                                 placeholder="Curp"
                                                                 readonly="true"/>
                                                    <span class="help-block" id="curpMessage" />
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-sm-2 control-label">Modalidad</label>
                                                <div class="col-sm-10">
                                                    <s:select list="ambiente.modalidadList" 
                                                              listKey="id" listValue="nombre"
                                                              cssClass="form-control" 
                                                              disabled="true"/>
                                                    <s:hidden name="alumno.datosAcademicos.modalidad.id"/>
                                                    <span class="help-block" id="modalidadMessage" />
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-sm-2 control-label">Carga</label>
                                                <div class="col-sm-10">
                                                    <s:select name="alumno.datosAcademicos.cumpleCargaMinima" 
                                                              list="service.carga"
                                                              headerKey=""
                                                              cssClass="form-control"
                                                              disabled="true"/>
                                                    <s:hidden name="alumno.datosAcademicos.cumpleCargaMinima"/>
                                                    <span class="help-block" id="cargaMessage" />
                                                </div>
                                            </div>
                                            <security:authorize ifAnyGranted="ROLE_ANALISTABECAS, ROLE_RESPONSABLE_UA">
                                                <div class="form-group">
                                                    <s:if test="!tab&&procesoActivo">
                                                        <div class="col-sm-12">
                                                            <button type="submit" class="btn btn-primary pull-right">Guardar</button>
                                                        </div>
                                                    </s:if>
                                                </div>
                                            </security:authorize>
                                        </security:authorize>
                                        <s:hidden name="alumno.id"/>
                                        <s:hidden name="alumno.boleta"/>
                                        <s:hidden name="proceso.id" />
                                        <s:hidden name="solicitudBeca.id" />
                                        <s:hidden name="tab"/>
                                    </form>
                                </div>
                            </div>
                        </div>
                        <div class="tab-pane text-style" id="estatus">
                            <div class="row">
                                <div class="main-box-body clearfix">
                                    <form id="estatusForm" action="/becas/estatusAsignaciones.action" method="post" >
                                        <table class="table table-bordered">
                                            <thead>
                                                <tr>
                                                    <th></th>
                                                    <th class="text-center">Estado</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <security:authorize ifAnyGranted="ROLE_JEFEBECAS, ROLE_ANALISTABECAS, ROLE_RESPONSABLE_UA">
                                                    <tr>
                                                        <td>
                                                            Inscripción
                                                        </td>
                                                        <td class="text-center">
                                                            <s:if test="!tab&&procesoActivo">
                                                                <s:checkbox name="alumno.datosAcademicos.inscrito" 
                                                                            fieldValue="1"
                                                                            id="inscrito" />
                                                            </s:if>
                                                            <s:else>
                                                                <s:checkbox name="alumno.datosAcademicos.inscrito" 
                                                                            fieldValue="1"
                                                                            id="inscrito" 
                                                                            disabled="true"/>
                                                            </s:else>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            Regularidad
                                                        </td>
                                                        <td class="text-center">
                                                            <s:if test="!tab&&procesoActivo">
                                                                <s:checkbox name="alumno.datosAcademicos.regular"
                                                                            fieldValue="1"
                                                                            id="regular" />
                                                            </s:if>
                                                            <s:else>
                                                                <s:checkbox name="alumno.datosAcademicos.regular"
                                                                            fieldValue="1"
                                                                            id="regular"
                                                                            disabled="true"/>
                                                            </s:else>
                                                        </td>
                                                    </tr>
                                                </security:authorize>
                                                <security:authorize ifNotGranted="ROLE_JEFEBECAS, ROLE_ANALISTABECAS, ROLE_RESPONSABLE_UA">
                                                    <tr>
                                                        <td>
                                                            Inscripción
                                                        </td>
                                                        <td class="text-center">
                                                            <s:checkbox name="alumno.datosAcademicos.inscrito" 
                                                                        fieldValue="1"
                                                                        id="inscrito" 
                                                                        disabled="true"/>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            Regularidad
                                                        </td>
                                                        <td class="text-center">
                                                            <s:checkbox name="alumno.datosAcademicos.regular"
                                                                        fieldValue="1"
                                                                        id="regular" 
                                                                        disabled="true"/>
                                                        </td>
                                                    </tr>
                                                </security:authorize>
                                            </tbody>
                                        </table>
                                        <div class="form-group">
                                            <s:if test="!tab&&procesoActivo">
                                                <security:authorize ifAnyGranted="ROLE_JEFEBECAS, ROLE_ANALISTABECAS, ROLE_RESPONSABLE_UA">
                                                    <div class="col-sm-12">
                                                        <button type="submit" class="btn btn-primary pull-right">Guardar</button>
                                                    </div>
                                                </security:authorize>
                                            </s:if>
                                            <div class="clearfix" >&nbsp;</div> 
                                        </div>
                                        <s:hidden name="alumno.id"/>
                                        <s:hidden name="alumno.boleta"/>
                                        <s:hidden name="proceso.id" />
                                        <s:hidden name="tab"/>
                                        <s:hidden name="solicitudBeca.id" />
                                    </form>
                                </div>
                            </div>
                        </div>  
                        <div class="tab-pane text-style" id="cotejar">
                            <s:if test="( valDocs) ">
                                <s:if test="( validacionInscripcion) ">
                                    <div class="row">
                                        <div class="col-lg-12">
                                            <div class="alert alert-warning">
                                                <i class="fa fa-warning fa-fw fa-lg"></i>
                                                <strong>Los datos que se muestran son del periodo pasado debido a que se ejecutó el proceso de "Validación de Inscripción" en este periodo.</strong>
                                            </div>
                                        </div>
                                    </div>
                                </s:if>
                                <div class="row">
                                    <div class="main-box-body clearfix">
                                        <form id="form" action="/becas/cotejarAsignaciones.action" method="post" class="form-horizontal">
                                            <div class="form-group">
                                                <label class="col-sm-8 control-label">¿Documentación básica completa?</label>
                                                <div class="col-sm-4">
                                                    <s:if test="!tab&&procesoActivo&&(privilegio!=4)">
                                                        <label class="radio-inline"><input id="documentacionBasicaCompleta" value="1" type="radio" name="documentacionBasicaCompleta">Si</label>
                                                        <label class="radio-inline"><input id="documentacionBasicaIncompleta" value="0" type="radio" name="documentacionBasicaCompleta">No</label>
                                                        </s:if>
                                                        <s:else>
                                                        <label class="radio-inline"><input id="documentacionBasicaCompleta" value="1" type="radio" name="documentacionBasicaCompleta" disabled="true">Si</label>
                                                        <label class="radio-inline"><input id="documentacionBasicaIncompleta" value="0" type="radio" name="documentacionBasicaCompleta" disabled="true">No</label>
                                                        </s:else>
                                                </div>                                   
                                            </div>
                                            <div class="clearfix" >&nbsp;</div>   
                                            <table class="table table-bordered">
                                                <thead>
                                                    <tr>
                                                        <th>Documento</th>
                                                        <th class="text-center">Estado</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <s:if test="!tab&&procesoActivo&&(privilegio!=4)">
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
                                                                <br>(Aplica para los alumnos de Beca Manutención)
                                                            </td>
                                                            <td class="text-center">
                                                                <s:if test="solicitudBeca.programaBecaSolicitada.id==5">
                                                                    <s:checkbox name="documentos.acuseSubes" fieldValue="true"/>
                                                                </s:if>
                                                                <s:else>
                                                                    <s:checkbox name="documentos.acuseSubes" fieldValue="true" disabled="true"/>
                                                                    <s:hidden name="documentos.acuseSubes"/>
                                                                </s:else> 
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td>
                                                                Acuse del SUBES Transporte
                                                                <br>(Aplica para los alumnos de Beca Manutención)
                                                            </td>
                                                            <td class="text-center">
                                                                <s:if test="solicitudBeca.programaBecaSolicitada.id==7">
                                                                    <s:checkbox id="acuseSubes" name="documentos.acuseSubesTransporte" fieldValue="true"/>
                                                                </s:if>
                                                                <s:else>
                                                                    <s:checkbox id="acuseSubes" name="documentos.acuseSubesTransporte" fieldValue="true" disabled="true"/>
                                                                    <s:hidden name="documentos.acuseSubesTransporte"/>
                                                                </s:else>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td>
                                                                Estudio socioeconómico Transporte
                                                                <br>(Aplica para los alumnos de Beca de Transporte Institucional)
                                                            </td>
                                                            <td class="text-center">
                                                                <s:if test="solicitudBeca.cuestionario.id == 2">
                                                                    <s:checkbox name="documentos.estudiosocieconomicotransporte" fieldValue="true"/>
                                                                </s:if>
                                                                <s:else>
                                                                    <s:checkbox name="documentos.estudiosocieconomicotransporte" fieldValue="true" disabled="true"/>
                                                                    <s:hidden name="documentos.estudiosocieconomicotransporte"/>
                                                                </s:else>
                                                            </td>
                                                        </tr>
                                                    </s:if>
                                                    <s:else>
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
                                                                <br>(Aplica para los alumnos de Beca Manutención)
                                                            </td>
                                                            <td class="text-center">
                                                                <s:checkbox name="documentos.acuseSubes" fieldValue="true" disabled="true"/>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td>
                                                                Acuse del SUBES Transporte
                                                                <br>(Aplica para los alumnos de Beca Manutención)
                                                            </td>
                                                            <td class="text-center">
                                                                <s:checkbox id="acuseSubes" name="documentos.acuseSubesTransporte" fieldValue="true" disabled="true"/>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td>
                                                                Estudio socioeconómico Transporte
                                                                <br>(Aplica para los alumnos de Beca de Transporte Institucional)
                                                            </td>
                                                            <td class="text-center">
                                                                <s:checkbox name="documentos.estudiosocieconomicotransporte" fieldValue="true" disabled="true"/>
                                                            </td>
                                                        </tr>
                                                    </s:else>
                                                </tbody>
                                            </table>
                                            <div class="form-group">
                                                <div class="col-sm-12">
                                                    <s:if test="!tab&&procesoActivo&&(privilegio!=4)&&(valDocs)">
                                                        <button type="submit" class="btn btn-primary pull-right">Guardar</button>
                                                    </s:if>
                                                    <s:if test="solicitudBeca.cuestionario.id == 1">
                                                        <a href="#" onclick="addURL(this, 1, <s:property value="alumno.id"/>)" id="ese" class="btn btn-default btn-primary"><span class="fa fa-money"></span> Visualizar Estudio Socioeconómico</a>
                                                    </s:if>
                                                    <s:if test="solicitudBeca.cuestionario.id == 2">
                                                        <a href="#" onclick="addURL(this, 2, <s:property value="alumno.id"/>)" id="ese" class="btn btn-default btn-primary"><span class="fa fa-car"></span> Visualizar Estudio Socioeconómico</a>
                                                    </s:if>
                                                </div>
                                                <div class="clearfix" >&nbsp;</div>
                                            </div>
                                            <s:hidden name="alumno.id"/>
                                            <s:hidden name="alumno.boleta"/>
                                            <s:hidden name="documentos.id"/>
                                            <s:hidden name="proceso.id" />
                                            <s:hidden name="solicitudBeca.id" />
                                            <s:hidden name="tab"/>
                                        </form>
                                    </div>
                                </div>    
                            </s:if>
                            <s:else>
                                <div class="row">
                                    <div class="col-lg-12">
                                        <div class="alert alert-warning">
                                            <i class="fa fa-warning fa-fw fa-lg"></i>
                                            <strong>No existen documentos para el periodo anterior.</strong>
                                        </div>
                                    </div>
                                </div>
                            </s:else>
                        </div>
                        <s:if test="( cumpleRequisitos && (tab==0||tab==1)) ">
                            <div class="tab-pane text-style" id="cambiar">
                                <div class="row">
                                    <div class="main-box-body clearfix">
                                        <form id="cambioBeca" action="/becas/cambiarAsignaciones.action" method="post"  class="form-horizontal">
                                            <div class="form-group">
                                                <label class="col-sm-4 control-label"> Ingresos por persona en su familia: </label>
                                                <div class="col-sm-8">
                                                    <s:textfield cssClass="form-control"
                                                                 name="ingresoFormateado"
                                                                 placeholder="ingresosPercapitaPesos"
                                                                 readonly="true" />
                                                    <span class="help-block" id="ingresosPercapitaPesosMessage" />
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-sm-4 control-label"> Permite transferencia </label>
                                                <div class="col-sm-8">
                                                    <s:checkbox name="solicitudBeca.permiteTransferencia" fieldValue="true" disabled="true"/>
                                                    <span class="help-block" id="permiteTransferenciaMessage" />
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-sm-4 control-label"> Seleccione el tipo de beca </label>
                                                <div class="col-sm-8">
                                                    <s:select id="tipoBecaPeriodo"  
                                                              cssClass="form-control"
                                                              name="tipoBeca.id"
                                                              list="tipoBecaPeriodo" 
                                                              listKey="id" 
                                                              listValue="tipoBeca.nombre" 
                                                              headerKey=""
                                                              data-bv-notempty="true"
                                                              data-bv-notempty-message="El tipo de beca es requerido"
                                                              headerValue="-- Seleccione un tipo de beca --" />
                                                    <span class="help-block" id="tipoBecaPeriodoMessage" />
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-sm-4 control-label"> Observaciones</label>
                                                <div class="col-sm-8">
                                                    <s:textarea cssClass="form-control"
                                                                name="observaciones"
                                                                id="observaciones"
                                                                placeholder="Observaciones"
                                                                cols="45"
                                                                rows="10" 
                                                                data-bv-message="Este dato no es válido"
                                                                required="true" 
                                                                data-bv-notempty="true"
                                                                data-bv-notempty-message="Las observaciones son requeridas"
                                                                pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s1234567890]+$" 
                                                                data-bv-regexp-message="No se permiten caracteres especiales"
                                                                data-bv-stringlength="true" 
                                                                data-bv-stringlength-min="3" 
                                                                data-bv-stringlength-max="250" />
                                                    <span class="help-block" >
                                                        Es obligatorio ingresar las observaciones correspondientes.
                                                    </span>
                                                    <span class="help-block" id="observacionesMessage" />
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <s:if test="procesoActivo&&tab==0">
                                                    <div class="col-sm-12">
                                                        <button id="btnAsignar" type="submit" class="btn btn-primary pull-right">Asignar beca</button>
                                                    </div>
                                                </s:if>
                                                <div class="clearfix" >&nbsp;</div>
                                            </div>
                                            <s:hidden name="alumno.id"/>
                                            <s:hidden name="alumno.boleta"/>
                                            <s:hidden name="proceso.id"/>
                                            <s:hidden name="solicitudBeca.id" />
                                            <s:hidden name="tab"/>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </s:if>
                        <s:if test=" (tab==0||tab==2) ">
                            <div class="tab-pane text-style" id="rechazar">
                                <div class="row">
                                    <div class="main-box-body clearfix">
                                        <form id="cambioBeca" action="/becas/rechazoAsignaciones.action" method="post"  class="form-horizontal">
                                            <div class="form-group">
                                                <label class="col-sm-3 control-label"> Seleccione el motivo </label>
                                                <div class="col-sm-9">
                                                    <s:select id="motivoRechazo"  
                                                              cssClass="form-control"
                                                              name="motivo.id"
                                                              list="motivoRechazo" 
                                                              listKey="id" 
                                                              listValue="nombre" 
                                                              headerKey=""
                                                              data-bv-notempty="true"
                                                              data-bv-notempty-message="El motivo es requerido"
                                                              headerValue="-- Seleccione un motivo --" />
                                                    <span class="help-block" id="tipoBecaPeriodoMessage" />
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <s:if test="procesoActivo&&tab==0">
                                                    <div class="col-sm-12">
                                                        <button id="btnAsignar" type="submit" class="btn btn-primary pull-right">Rechazar solicitud</button>
                                                    </div>
                                                </s:if>
                                                <div class="clearfix" >&nbsp;</div>
                                            </div>
                                            <s:hidden name="alumno.id"/>
                                            <s:hidden name="alumno.boleta"/>
                                            <s:hidden name="proceso.id"/>
                                            <s:hidden name="solicitudBeca.id" />
                                            <s:hidden name="tab"/>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </s:if>
                        <div class="tab-pane text-style" id="validar">
                            <div class="row">
                                <div class="main-box-body clearfix">
                                    <s:if test="validacionRequisitos">
                                    <div class="alert alert-info">
                                        <i class="fa fa-info-circle fa-fw fa-lg"></i>
                                        <strong>Toma en cuenta</strong> Los programas de la siguiente lista corresponden al nivel académico del alumno y al periodo en curso.
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-3 control-label"> Programa de beca</label>
                                        <div class="col-sm-8">
                                            <s:select id="programaBeca"  
                                                      cssClass="form-control"
                                                      name="beca.id"
                                                      list="programasBeca" 
                                                      listKey="id" 
                                                      listValue="nombre" 
                                                      headerKey="-1"
                                                      headerValue="-- Seleccione un programa --" />
                                            <span class="help-block" id="programaBecaMessage" />
                                        </div>
                                    </div>                                        
                                    <div class="row" id="contenedor-tabla" style="display: none;">
                                        <div class="col-xs-12">
                                            <div class="main-box clearfix">
                                                <div class="col-md-12" style="padding: 0px">
                                                    <table id="tabla" class="table table-striped table-hover">
                                                        <thead>
                                                            <tr>
                                                                <th data-center="true">Beca</th>
                                                                <th data-center="true">Ingresos máximos</th>
                                                                <th data-center="true">Gasto Transporte</th>
                                                                <th data-center="true">Vulnerabilidad</th>
                                                                <th data-center="true">Regular</th>
                                                                <th data-center="true">Inscripción</th>
                                                                <th data-center="true">Promedio</th>
                                                                <th data-center="true">Semestre</th>
                                                                <th data-center="true">Carga</th>
                                                                <th data-center="true">Áreas de conocimiento</th>
                                                            </tr>
                                                        </thead>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>                
                                    </div>
                                    </s:if>
                                    <s:else>
                                    <div class="alert alert-warning">
                                        <i class="fa fa-warning fa-fw fa-lg"></i>
                                        <s:property value="mensajeRequisitos"/>;
                                    </div>
                                    </s:else>
                                </div>
                            </div>
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
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js"></script>
    <script type="text/javascript" src="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>
        var PROCE;
        var IDEN;

        $(document).ready(function () {
            $("#programaBeca").on('change', function () {
                cargaTablaRequisitos();
            });
            
            var daEgresado = <s:property value="alumno.datosAcademicos.egresado"/>;
            if (daEgresado === 1) {
                $("#checkbox-egresado, #checkbox-egresado-read").attr('checked', 'checked');
            }
            
            $("#accountForm").bootstrapValidator({});
            $("#editarForm").bootstrapValidator({});
            $("#estatusForm").bootstrapValidator({});
            //$("#cambioBeca").bootstrapValidator({});

            //if ($("#procesosnuevos").length) {
            $('#cambioBeca').bootstrapValidator({
                excluded: ['[data-check]']
            });
            /*} else {
             $('#cambioBeca').bootstrapValidator({});
             }*/

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

        <s:if test="tab==1">
            $("#tipoBecaPeriodo").prop('disabled', true);
            $("#observaciones").prop('disabled', true);
        </s:if>


        <s:if test="tab==2">
            $("#motivoRechazo").prop('disabled', true);
        </s:if>

        <s:set var="idPregunta" value="" />
        <s:set var="idPreguntaCurrent" value="" />
            var idSeccionCurrent = "";
            var idSeccion = "";
            var counter = 0;
            var idPreguntaCurrent = "";
            
            if (countError > 0) {
                BootstrapDialog.alert({
                    title: 'Atención',
                    message: "Por favor, coloque un correo electrónico válido.",
                    type: BootstrapDialog.TYPE_WARNING
                });
            }

            var countError = 0;
            $('#actualizaCorreo').click(function () {
                countError = 0;
                $('#emailEdit small').each(function (index, element) {
                    var small = element.outerHTML;
                    if (small.indexOf("NOT_VALIDATED") >= 0 || small.indexOf("INVALID") >= 0) {
                        countError++;
                    }
                });

            });
        });

        function cargaTablaRequisitos() {
            var programaBecaId = $('#programaBeca').val();

            if (programaBecaId != -1) {
                $("#programaBeca").prop("disabled", true);
                var alumnoId = <s:property value="alumno.id"/>;
                var solicitudId = <s:property value="solicitudBeca.id"/>;
                var url = "/ajax/getRequisitosAsignacionesAjax.action";

                $.getJSON(url, {
                    alumnoId: alumnoId,
                    solicitudId: solicitudId,
                    programaBecaId: programaBecaId
                }).done(function (response) {
                    var programaRequisitos = new Array();
                    $.each(response.data[0].becaRequisitos, function (i, requisitos) {
                        var becaRequisitos = new Array();
                        $.each(requisitos, function (j, requisito) {
                            if (requisito.cadena) {
                                becaRequisitos.push(requisito.texto);
                            } else {
                                if (requisito.cumple) {
                                    becaRequisitos.push("<span data-toggle='tooltip' data-placement='auto' style='color:green;' class='fa-stack' title='" + requisito.texto + "'><i class='fa fa-check-square fa-stack-2x'></i></span>");
                                } else {
                                    becaRequisitos.push("<span data-toggle='tooltip' data-placement='auto' style='color:red;' class='fa-stack' title='" + requisito.texto + "'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-times  fa-stack-1x fa-inverse'></i></span>");
                                }
                            }
                        });
                        programaRequisitos.push(becaRequisitos);
                    });
                    generarTabla("contenedor-tabla", programaRequisitos, despuesCargarTabla, false, null, null, true);
                });
            }
        }

        function despuesCargarTabla() {
            $("#programaBeca").prop("disabled", false);
            $('#contenedor-tabla').css('display', 'block');
//            $('[data-toggle="tooltip"]').tooltip();
        }

        function addURL(element, op, id) {
            $("#ese").blur();// Evita que el botón sea desactivado    
            $(element).attr('href', function () {
                if (op === 1) {
                    return "/admin/eseCuestionario.action?cuestionarioId=1&alumnoId=" + id;
                } else if (op === 2) {
                    return "/admin/esetCuestionario.action?cuestionarioId=2&alumnoId=" + id;
                }
            });
        }
    </script>
</content>