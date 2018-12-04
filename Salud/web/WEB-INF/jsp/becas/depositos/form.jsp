<%-- 
    Document   : ver
    Created on : 15/08/2016, 09:45:36 AM
    Author     : Victor Lozano
    Redesing   : Mario Márquez
--%>

<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>    
    <title>Consulta de depósitos</title>
</head> 

<content tag="tituloJSP">
    Consulta de depósitos
</content>

<body>
    <div class="row">
        <div class="col-xs-12">
            <s:if test="hasActionErrors()">
                <div class="alert alert-danger">
                    <i class="fa fa-times-circle fa-fw fa-sm"></i>
                    <strong>&iexcl;Error!</strong> <s:actionerror/>
                </div>
            </s:if>
            <s:else>
                <div class="main-box clearfix">      
                    <div class="main-box-body">
                        <form class="form-horizontal">
                            <div class="form-group col-sm-12 ">
                                <label class="col-sm-2 control-label">
                                    Boleta
                                    <s:hidden name="idPeriodo"/>
                                </label>
                                <div class="col-sm-10">
                                    <s:textfield name="alumno.boleta" cssClass="form-control" readonly="true"/>
                                </div>
                            </div>
                            <div class="form-group col-sm-12">
                                <label class="col-sm-2 control-label">Nombre</label>
                                <div class="col-sm-4">
                                    <s:textfield name="alumno.nombre" cssClass="form-control" readonly="true"/>
                                </div>
                                <div class="col-sm-3">
                                    <s:textfield name="alumno.apellidoPaterno" cssClass="form-control" readonly="true"/>
                                </div>
                                <div class="col-sm-3">
                                    <s:textfield name="alumno.apellidoMaterno" cssClass="form-control" readonly="true"/>
                                </div>
                            </div>
                            <div class="form-group col-sm-12">
                                <label class="col-sm-2 control-label">Unidad Académica</label>
                                <div class="col-sm-10">
                                    <s:textfield name="alumno.datosAcademicos.unidadAcademica.nombreCorto" cssClass="form-control" readonly="true"/>
                                </div>
                            </div>
                            <!--                            <div class="form-group col-sm-12">
                                                            <label class="col-sm-2 control-label">Tarjeta activa</label>
                                                            <div class="col-sm-10">
                            <s:textfield name="numero_tarjeta" cssClass="form-control" readonly="true"/>
                        </div>
                    </div> -->

                            <security:authorize ifAnyGranted="ROLE_ANALISTABECAS,ROLE_ANALISTAADMINISTRATIVO,ROLE_FUNCIONARIO_UA,ROLE_JEFEBECAS,ROLE_EJECUTIVO,ROLE_CONSULTA">
                                <div class="form-group col-sm-12 ">
                                    <label class="col-sm-2 control-label">
                                        Identificador de Otorgamiento
                                        <s:hidden name="idPeriodo"/>
                                    </label>
                                    <div class="col-sm-10">
                                        <s:textfield name="otorgamiento.identificadorOtorgamiento.nombre" cssClass="form-control" readonly="true"/>
                                    </div>
                                </div>
                            </security:authorize>

                            <div class="form-group col-sm-12 ">
                                <label class="col-sm-2 control-label">
                                    Excluir depósito
                                    <s:hidden name="idPeriodo"/>
                                </label>
                                <div class="col-sm-10">
                                    <s:if test="otorgamiento.excluirDeposito">
                                        <s:textfield name="otorgamiento.excluirDeposito" value="Si" cssClass="form-control" readonly="true"/>
                                    </s:if> 
                                    <s:else>
                                        <s:textfield name="otorgamiento.excluirDeposito" value="No" cssClass="form-control" readonly="true"/>
                                    </s:else>


                                </div>
                            </div>
                        </form>
                    </div>                                        
                </div>         
                <!-- DIV para la tabla -->
                <div class="row" id="div-tabla" style="display: none;">
                    <div class="col-xs-12">  
                        <div class="main-box clearfix">
                            <div class="col-md-12" style="padding: 0px">
                                <div class="responsive">                        
                                    <table id="listado" class="table table-striped table-hover dt-responsive" style="width: 100%">
                                        <thead>
                                            <tr>
                                                <th data-orderable="true">Periodo</th>
                                                <th>Tipo de beca</th>
                                                <th>Mes</th>
                                                <th>Fecha de depósito</th>
                                                <th>Monto</th>
                                                <th>Tarjeta/Cuenta</th>
                                                <th>Estatus</th>
                                            </tr>
                                        </thead>
                                    </table>                        
                                </div>
                            </div>
                        </div>
                    </div>                
                </div>

                <security:authorize ifAnyGranted="ROLE_JEFEBECAS">
                    <div class="clearfix" >&nbsp;</div>
                    <div class="form-group">
                        <div class="col-sm-12 col-md-12 col-lg-12">
                            <div class="pull-right">
                                <a class="btn btn-primary fancybox fancybox.iframe table-link"  href="javascript:history.back()"><i class="fa fa-arrow-circle-left"></i></span> REGRESAR</a>
                                <!--<a href="javascript:history.back()" class="btn btn-primary" role="button">Regresar</a>-->
                            </div>
                        </div>
                    </div>
                </security:authorize>



            </s:else>
        </div>
    </div    
</body>

<content tag="endScripts"> 
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js" ></script>
    <script type="text/javascript" src="/resources/js/generador-tablas.min.js" 
            data-init="true" 
            data-div="div-tabla"
            data-function="despuesCargarTabla"
            data-ajax='/ajax/listadoDepositoAjax.action?noIdalumno=<s:property value="alumno.id"/>&idOtorgamiento=<s:property value="idOtorgamiento"/>'>'>
    </script>
</content>
<content tag="inlineScripts">
    <script>
        function despuesCargarTabla() { 
            // Agrega funcionalidad a las etiquetas de la tabla
            $('.estatus-deposito').on('click', function () {
                textoHeader = $(this).data('encabezado');
                if (textoHeader) {
                    textoDescript = $(this).data('descript') + " ";
                    textoSugerencia = $(this).data('sugerencia');
                
                    if ($(this).hasClass('label-danger')) {
                        cuerpo = textoDescript;
                        cuerpo += "<h3>Acción recomendada</h3>";
                        cuerpo += textoSugerencia;
                        
                        ModalGenerator.notificacion({
                            "titulo": textoHeader,
                            "tipo": "ALERT",
                            "cuerpo": cuerpo
                        });
                    } else {
                        ModalGenerator.notificacion({
                            "titulo": textoHeader,
                            "tipo": "PRIMARY",
                            "cuerpo": textoDescript + textoSugerencia
                        });
                    }
                }
            });
        }
    </script>
</content>