<%-- 
    Document   : ese
    Created on : 15/06/2016, 02:21:03 PM
    Author     : Tania G. Sánchez
    Redesing   : Mario Márquez
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="security" uri="/WEB-INF/decorators/tld/security.tld" %>
<%@taglib prefix="s" uri="/struts-tags" %>

<head>
    <title>Solicitud de Transporte</title>

    <link rel="stylesheet" type="text/css" href="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.css" />    
    <link rel="stylesheet" type="text/css" href="/vendors/kartik-bootstrap-input/fileinput.min.css"/>    

    <style>
        .in-sin-margen {margin: 0;}
        .p-etiqueta {padding-left: 20px;}
    </style>
</head> 

<content tag="tituloJSP">
    Solicitud de Transporte
</content>

<body>
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
            <s:if test="!sinBeca">
                <s:if test="!becaCompatiblePasadaESE">
                    <!--                    Se quita este mensaje por la confusión de la palabra revalidación.
                                            <div class="alert alert-info">
                                            El periodo anterior tuviste la beca <s:property  value="otorgamientoPasado.tipoBecaPeriodo.tipoBeca.nombre"/>, 
                                            por lo que si se te asigna Beca de Transporte Institucional, no podrás revalidar la beca del periodo anterior.
                                        </div>-->
                </s:if>
            </s:if>            
        </div>
    </div>
    <div class="row">

        <div class="alert alert-info fade in">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
            <i class="fa fa-info-circle fa-fw fa-lg"></i>
            Este estudio socioeconómico tiene por objetivo obtener los datos necesarios para 
            evaluar las condiciones geográficas y de salud  que te permiten concursar por una beca de transporte, 
            por lo que se te solicita llenarla <b>verazmente</b>  y complementarla con la <b>documentación probatoria</b>.
        </div>

        <div class="col-xs-12">
            <div class="panel-group accordion" id="accordion">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
                                Aviso de Privacidad Simplificado
                            </a>
                        </h4>
                    </div>
                    <div id="collapseOne" class="panel-collapse collapse">
                        <div class="panel-body">
                            <p style="text-align: justify;">El Instituto Polit&eacute;cnico Nacional, a trav&eacute;s de la 
                                Direcci&oacute;n de Servicios Estudiantiles de conformidad a lo dispuesto en el 
                                art&iacute;culo 53 fracci&oacute;n V del Reglamento Org&aacute;nico del 
                                Instituto Polit&eacute;cnico Nacional, es el responsable del tratamiento de los datos personales 
                                que nos proporciones, los cuales ser&aacute;n protegidos conforme a lo dispuesto por la 
                                Ley General de Protecci&oacute;n de Datos Personales en Posesi&oacute;n de Sujetos Obligados, 
                                y dem&aacute;s normatividad que resulte aplicable.</p>
                            <p style="text-align: justify;"><strong>&iquest;Para qu&eacute; fines utilizaremos sus datos personales?</strong></p>
                            <p style="text-align: justify;">Los datos personales que solicitamos los utilizaremos para las siguientes finalidades: 
                                Llevar a cabo el proceso de selecci&oacute;n y otorgamiento de becas que oferta el Instituto Polit&eacute;cnico Nacional.</p>
                            <p><strong>&iquest;Con qui&eacute;n compartimos su informaci&oacute;n personal y para qu&eacute; fines?</strong></p>
                            <p style="text-align: justify;">Se informa que no se realizar&aacute;n transferencias de datos personales, salvo aqu&eacute;llas 
                                que sean necesarias para atender requerimientos de informaci&oacute;n de una autoridad competente, que est&eacute;n 
                                debidamente fundados y motivados.</p><p style="text-align: justify;">Para conocer mayor informaci&oacute;n sobre los 
                                t&eacute;rminos y condiciones en que ser&aacute;n tratados sus datos personales, como los terceros con quienes 
                                compartimos su informaci&oacute;n personal y la forma en que podr&aacute; ejercer sus derechos ARCO, puede consultar el 
                                aviso de privacidad integral en: <a href="https://www.ipn.mx/dse/becas/Paginas/inicio.aspx" target="_blank"  > <strong>https://www.ipn.mx/dse/becas/Paginas/inicio.aspx</strong> </a>
                            </p>
                        </div>
                    </div>
                </div>
            </div> 
        </div>
        <s:if test="ie.ingresosPerCapita!=null && ie.totalIntegrantes!=null">
            <div class="row">
                <div class="col-lg-6 col-sm-6 col-xs-12">
                    <div class="main-box infographic-box">
                        <i class="fa fa-users yellow-bg"></i>
                        <span class="headline">Integrantes de la familia</span>
                        <span class="value">
                            <span class="timer" data-from="120" data-to="2562" data-speed="1000" data-refresh-interval="50">
                                <s:property  value="getText('{0,number,#,##0}',{ie.totalIntegrantes})"/>
                            </span>
                        </span>
                    </div>
                </div>
                <div class="col-lg-6 col-sm-6 col-xs-12">
                    <div class="main-box infographic-box">
                        <i class="fa fa-money green-bg "></i>
                        <span class="headline">Ingreso Mensual</span>
                        <span class="value">
                            <span class="timer" data-from="120" data-to="2562" data-speed="1000" data-refresh-interval="50">
                                $<s:property value="ie.totalIngresos" />
                            </span>
                        </span>
                    </div>
                </div>
                <div class="col-lg-6 col-sm-6 col-xs-12">
                    <div class="main-box infographic-box">
                        <i class="fa fa-dollar  emerald-bg "></i>
                        <span class="headline">Ingreso Per Cápita</span>
                        <span class="value">
                            <span class="timer" data-from="120" data-to="2562" data-speed="1000" data-refresh-interval="50">
                                $<s:property value="ie.ingresosPerCapita" />
                            </span>
                        </span>
                    </div>
                </div>
                <div class="col-lg-6 col-sm-6 col-xs-12">
                    <div class="main-box infographic-box">
                        <i class="fa fa-lightbulb-o red-bg "></i>
                        <span class="headline">Ésta información ha sido obtenida de los datos ingresados en tu <s:property value="ie.fuenteDeInformacion" escape="false"  />.<br><br></span>
                        <span class="value">

                        </span>
                    </div>
                </div>
            </div>

        </s:if>
    </div>

    <div class="row">
        <div class="col-xs-12">
            <div class="main-box" >
                <div class="main-box-body clearfix">                    
                    <div class="tabs-wrapper">
                        <ul class="nav nav-tabs">
                            <li class="active"><a href="#traslado-tab" data-toggle="tab">Datos de traslado<i class="fa"></i></a></li>
                            <li><a href="#familiares-tab" data-toggle="tab">Datos familiares<i class="fa"></i></a></li>
                            <li><a href="#recursos-tab" data-toggle="tab">Recursos para estudios<i class="fa"></i></a></li>
                            <li><a href="#economico-tab" data-toggle="tab">Situación económica<i class="fa"></i></a></li>
                        </ul>
                        <form id="eseTransporte" class="form-horizontal" action="/misdatos/guardarBecaTransporte.action?cuestionarioId=2" >
                            <div class="tab-content">
                                <div class="tab-pane active" id="traslado-tab">
                                    <p style="font-size:13px;">
                                        <b>Ingresa la ruta del trayecto que realizas a tu Centro de Estudios de ida y vuelta:</b> 
                                        <br>
                                    </p>
                                    <div class="alert alert-info fade in">
                                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                                        <i class="fa fa-info-circle fa-fw fa-lg"></i>
                                        Puedes agregar filas a la tabla, con el botón <strong>"Agregar Fila"</strong> y quitar filas, con el botón <strong>"Eliminar Fila"</strong>.
                                        Dependiendo de que tan largo sea tu recorido.
                                    </div>
                                    <div id="container">
                                        <div class="row-fluid top-buffer">
                                            <div class="responsive-table table-responsive clearfix">
                                                <table id="tblprod" class="table table-hover table-bordered">
                                                    <thead>
                                                        <tr>
                                                            <th class="text-center">Fila </th>
                                                            <th class="text-center">Transporte Utilizado</th>
                                                            <th class="text-center">Punto de <br>Partida</th>
                                                            <th class="text-center">Punto de <br>Llegada</th>
                                                            <th class="text-center">Costo<br>($ sólo números)</th>
                                                            <th class="text-center">Trayecto</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <tr id="1">
                                                            <td class="text-center">1</td>
                                                            <td>
                                                                <s:select id="transporte" name="transporte" cssClass="form-control"
                                                                          list="ambiente.transporteList" listKey="id" listValue="nombre" />
                                                            </td>
                                                            <td >
                                                                <div class="form-group col-lg-12" style="margin: 0;">
                                                                    <s:textfield id="puntopartida" name ="puntopartida" cssClass="form-control" 
                                                                                 data-bv-message="Este dato no es válido"
                                                                                 pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ0-9\s]+$" required="true" 
                                                                                 data-bv-regexp-message="Sólo acepta letras y números."
                                                                                 data-bv-notempty="true"
                                                                                 data-bv-notempty-message="Este dato es requerido"/>
                                                                </div>
                                                            </td>
                                                            <td>
                                                                <div class="form-group col-lg-12" style="margin: 0;">
                                                                    <s:textfield id="puntollegada" name ="puntollegada" cssClass="form-control" 
                                                                                 data-bv-message="Este dato no es válido"
                                                                                 pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ0-9\s]+$" required="true" 
                                                                                 data-bv-regexp-message="Sólo acepta letras y números."
                                                                                 data-bv-notempty="true"
                                                                                 data-bv-notempty-message="Este dato es requerido"/>
                                                                </div>
                                                            </td>
                                                            <td>
                                                                <div class="form-group col-lg-12 " style="margin: 0;">
                                                                    <s:textfield id="costo" name ="costo" cssClass="numero form-control" 
                                                                                 data-bv-message="Este dato no es válido"
                                                                                 pattern="^[0-9.]+$" required="true" 
                                                                                 data-bv-regexp-message="Sólo acepta números."
                                                                                 data-bv-notempty="true"
                                                                                 data-bv-stringlength="true"
                                                                                 data-bv-stringlength-max="6"
                                                                                 data-bv-stringlength-message="Permite hasta 6 digitos."
                                                                                 data-bv-notempty-message="Este dato es requerido"/>
                                                                </div>
                                                            </td>
                                                            <td>
                                                                <s:select id="trayecto" name="trayecto" cssClass="form-control" 
                                                                          list="ambiente.trayectoList" listKey="id" listValue="nombre" />					
                                                            </td>
                                                        </tr>
                                                        <tr id="2">
                                                            <td class="text-center">2</td>
                                                            <td>
                                                                <s:select id="transporte" name="transporte" cssClass="form-control"
                                                                          list="ambiente.transporteList" listKey="id" listValue="nombre" />					
                                                            </td>
                                                            <td>
                                                                <div class="form-group col-lg-12 " style="margin: 0;">
                                                                    <s:textfield id="puntopartida" name="puntopartida" cssClass="form-control" 
                                                                                 data-bv-message="Este dato no es válido"
                                                                                 pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ0-9\s]+$" required="true" 
                                                                                 data-bv-regexp-message="Sólo acepta letras y números."
                                                                                 data-bv-notempty="true"
                                                                                 data-bv-notempty-message="Este dato es requerido"/>
                                                                </div>
                                                            </td>
                                                            <td>
                                                                <div class="form-group col-lg-12" style="margin: 0;">
                                                                    <s:textfield id="puntollegada" name="puntollegada" cssClass="form-control" 
                                                                                 data-bv-message="Este dato no es válido"
                                                                                 pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ0-9\s]+$" required="true" 
                                                                                 data-bv-regexp-message="Sólo acepta letras y números."
                                                                                 data-bv-notempty="true"
                                                                                 data-bv-notempty-message="Este dato es requerido"/>
                                                                </div>
                                                            </td>
                                                            <td>
                                                                <div class="form-group col-lg-12" style="margin: 0;">
                                                                    <s:textfield id="costo" name ="costo" cssClass="numero form-control" 
                                                                                 data-bv-message="Este dato no es válido"
                                                                                 pattern="^[0-9.]+$" required="true" 
                                                                                 data-bv-regexp-message="Sólo acepta números."
                                                                                 data-bv-notempty="true"
                                                                                 data-bv-stringlength="true"
                                                                                 data-bv-stringlength-max="6"
                                                                                 data-bv-stringlength-message="Permite hasta 6 digitos."
                                                                                 data-bv-notempty-message="Este dato es requerido"/>
                                                                </div>
                                                            </td>
                                                            <td>
                                                                <s:select id="trayecto" name="trayecto" cssClass="form-control"
                                                                          list="ambiente.trayectoList" listKey="id" listValue="nombre" />					
                                                            </td>
                                                        </tr>
                                                    </tbody>
                                                </table>
                                                <div class="btn-group pull-right">
                                                    <a id="btnadd"  class="btn btn-default">Agregar Fila</a>
                                                    <a id="btnsubmit" class="btn btn-default">Eliminar Fila</a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="tab-pane" id="familiares-tab">
                                    <p style="font-size:13px;">
                                        <b>Registra los datos de las personas que habitan contigo y su ingreso, INCLUYÉNDOTE 
                                            (Ingresos por nomina, honorarios, comisiones, subsidios, apoyos extraordinarios, utilidades, 
                                            prestaciones sociales, becas distintas a las proporcionadas por el IPN, entre otros).</b> 
                                    </p>
                                    <div class="alert alert-info fade in">
                                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                                        <i class="fa fa-info-circle fa-fw fa-lg"></i>
                                        Puedes agregar filas a la tabla, con el botón <strong>"Agregar Fila"</strong> y quitar filas, con el botón <strong>"Eliminar Fila"</strong>
                                    </div>
                                    <div id="container">
                                        <div class="row-fluid top-buffer">
                                            <div class="responsive-table table-responsive clearfix">
                                                <table id="tblfam" class="table table-hover table-bordered" style="width:100%">
                                                    <thead>
                                                        <tr>
                                                            <th class="text-center">Fila </th>
                                                            <th class="text-center">Nombre del Familiar</th>
                                                            <th class="text-center">Parentesco con el solicitante</th>
                                                            <th class="text-center">Edad</th>
                                                            <th class="text-center">Ocupación</th>
                                                            <th class="text-center">Aportación Económica Mensual<br>($ sólo números)</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <tr id="1">
                                                            <td class="text-center">1</td>
                                                            <td>
                                                                <div class="form-group col-lg-12" style="margin: 0;">
                                                                    <s:textfield id="nombrefamiliar" name="nombrefamiliar" cssClass="form-control" 
                                                                                 data-bv-message="Este dato no es válido"
                                                                                 pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s]+$" required="true" 
                                                                                 data-bv-regexp-message="Sólo acepta letras."
                                                                                 data-bv-notempty="true"
                                                                                 data-bv-notempty-message="Este dato es requerido"/>		
                                                                </div>
                                                            </td>
                                                            <td>
                                                                <s:select id="parentesco" name="parentesco" cssClass="form-control"
                                                                          list="ambiente.parentescoList" listKey="id" listValue="nombre" />						
                                                            </td>
                                                            <td>
                                                                <div class="form-group col-lg-12" style="margin: 0;">
                                                                    <s:textfield id="edad" name="edad" cssClass="form-control numeroE" 
                                                                                 data-bv-message="Este dato no es válido"
                                                                                 pattern="^[0-9.]+$" required="true" 
                                                                                 data-bv-regexp-message="Sólo acepta números."
                                                                                 data-bv-notempty="true"
                                                                                 data-bv-stringlength="true"
                                                                                 data-bv-stringlength-max="2"
                                                                                 data-bv-stringlength-message="Permite hasta 2 digitos."
                                                                                 data-bv-notempty-message="Este dato es requerido"/>
                                                                </div>
                                                            </td>
                                                            <td>
                                                                <div class="form-group col-lg-12" style="margin: 0;">
                                                                    <s:textfield id="ocupacion" name="ocupacion" cssClass="form-control" 
                                                                                 data-bv-message="Este dato no es válido"
                                                                                 pattern="^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s]+$" required="true" 
                                                                                 data-bv-regexp-message="Sólo acepta letras."
                                                                                 data-bv-notempty="true"
                                                                                 data-bv-notempty-message="Este dato es requerido"/>
                                                                </div>
                                                            </td>
                                                            <td>
                                                                <div class="form-group col-lg-12" style="margin: 0;">
                                                                    <s:textfield id="aportacionmensual" name="aportacionmensual" cssClass="form-control numero" 
                                                                                 data-bv-message="Este dato no es válido"
                                                                                 pattern="^[0-9.]+$" required="true" 
                                                                                 data-bv-regexp-message="Sólo acepta números."
                                                                                 data-bv-notempty="true"
                                                                                 data-bv-notempty-message="Este dato es requerido"/>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </tbody>
                                                </table>
                                                <div class="btn-group pull-right">
                                                    <a id="btnaddF"  class="btn btn-default">Agregar Fila</a>
                                                    <a id="btnsubmitF" class="btn btn-default">Eliminar Fila</a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="tab-pane" id="recursos-tab">                                    

                                    <div class="form-group">
                                        <label class="control-label col-sm-3">¿Padeces alguna enfermedad crónica?</label>
                                        <div class="col-sm-6">
                                            <div class="radio col-sm-4" style="padding-left: 20px;">
                                                <input data-bv-notempty="true" data-bv-notempty-message="Por favor, selecciona una opción" type="radio" name="becaTransporte.enfermedadCronica" id="cronicaRadios1" value="1" required>
                                                <label for="cronicaRadios1">
                                                    Si
                                                </label>
                                            </div>
                                            <div class="radio col-sm-4" style="padding-left: 20px;">
                                                <input type="radio" name="becaTransporte.enfermedadCronica" id="cronicaRadios2" value="0" required>
                                                <label for="cronicaRadios2">
                                                    No
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-3">¿Tienes alguna discapacidad?</label>
                                        <div class="col-sm-6">                                    
                                            <s:select id="becaTransporte.discapacidad" name="becaTransporte.discapacidad.id" cssClass="form-control"
                                                      list="ambiente.discapacidadList" listKey="id" listValue="nombre" headerKey=""
                                                      headerValue="-- Selecciona una opción --"
                                                      data-bv-notempty="true"
                                                      data-bv-notempty-message="La entidad es requerida" />
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-3">¿Algún familiar tiene alguna enfermedad crónica?</label>
                                        <div class="col-sm-6">                                    
                                            <div class="radio col-sm-4" style="padding-left: 20px;">
                                                <input data-bv-notempty="true" data-bv-notempty-message="Por favor, selecciona una opción" type="radio" name="becaTransporte.familiarenfermcronica" id="famRadios1" value="1" required>
                                                <label for="famRadios1">
                                                    Si
                                                </label>
                                            </div>
                                            <div class="radio col-sm-4" style="padding-left: 20px;">
                                                <input type="radio" name="becaTransporte.familiarenfermcronica" id="famRadios2" value="0" required>
                                                <label for="famRadios2">
                                                    No
                                                </label>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="control-label col-sm-3">Para continuar con tus estudios de educación superior,
                                            ¿tuviste que cambiar tu lugar de residencia?</label>
                                        <div class="col-sm-6">                                    
                                            <div class="radio col-sm-4" style="padding-left: 20px;">
                                                <input data-bv-notempty="true" data-bv-notempty-message="Por favor, selecciona una opción" type="radio" name="becaTransporte.cambioresidencia" id="cambioRadios1" value="1" required>
                                                <label for="cambioRadios1">
                                                    Si
                                                </label>
                                            </div>
                                            <div class="radio col-sm-4" style="padding-left: 20px;">
                                                <input type="radio" name="becaTransporte.cambioresidencia" id="cambioRadios2" value="0" required>
                                                <label for="cambioRadios2">
                                                    No
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-3">¿Cómo te enteraste del apoyo de esta beca?</label>
                                        <div class="col-sm-6">                                        
                                            <s:select id="becaTransporte.enteroBeca" name="becaTransporte.enteroBeca.id" cssClass="form-control"
                                                      list="ambiente.enteroBecaList" listKey="id" listValue="nombre" headerKey=""
                                                      headerValue="-- Selecciona una opción --"
                                                      data-bv-notempty="true"
                                                      data-bv-notempty-message="La entidad es requerida" />
                                            <span class="help-block" id="estadoMessage" />
                                        </div>
                                    </div>
                                </div>
                                <div class="tab-pane" id="economico-tab">
                                    <p>Registra el gasto familiar mensual en los siguientes rubros:</p><br>
                                    <div class="form-group">
                                        <label class="control-label col-sm-3">Alimentación</label>                                            
                                        <div class="col-sm-6 input-group">
                                            <span class="input-group-addon"><i class="fa fa-dollar"></i></span>
                                                <s:textfield id="becaTransporte.alimentacion" name="becaTransporte.alimentacion" 
                                                             cssClass="form-control" placeholder="Alimentación"
                                                             data-bv-message="Este dato no es válido"
                                                             pattern="^[0-9.]+$" required="true" 
                                                             data-bv-regexp-message="Solo se pueden agregar números"
                                                             data-bv-notempty="true"
                                                             data-bv-stringlength="true"
                                                             data-bv-stringlength-max="8"
                                                             data-bv-notempty-message="Este dato es requerido"/>
                                        </div>                                        
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-3">Renta</label>
                                        <div class="col-sm-6 input-group">
                                            <span class="input-group-addon"><i class="fa fa-dollar"></i></span>
                                                <s:textfield id="becaTransporte.renta" name="becaTransporte.renta" 
                                                             cssClass="numero form-control" placeholder="Renta"
                                                             data-bv-message="Este dato no es válido"
                                                             pattern="^[0-9.]+$" required="true" 
                                                             data-bv-regexp-message="Solo se pueden agregar números"
                                                             data-bv-notempty="true"
                                                             data-bv-stringlength="true"
                                                             data-bv-stringlength-max="8"
                                                             data-bv-notempty-message="Este dato es requerido"/>
                                        </div>

                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-3">Escolares</label>
                                        <div class="col-sm-6 input-group">
                                            <span class="input-group-addon"><i class="fa fa-dollar"></i></span>
                                                <s:textfield id="becaTransporte.escolares" name="becaTransporte.escolares" 
                                                             cssClass="numero form-control" placeholder="Escolares"
                                                             data-bv-message="Este dato no es válido"
                                                             pattern="^[0-9.]+$" required="true" 
                                                             data-bv-regexp-message="Solo se pueden agregar números"
                                                             data-bv-notempty="true"
                                                             data-bv-stringlength="true"
                                                             data-bv-stringlength-max="8"
                                                             data-bv-notempty-message="Este dato es requerido"/>
                                        </div>

                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-3">Salud</label>
                                        <div class="col-sm-6 input-group">
                                            <span class="input-group-addon"><i class="fa fa-dollar"></i></span>
                                                <s:textfield id="becaTransporte.salud" name="becaTransporte.salud" 
                                                             cssClass="numero form-control" placeholder="Salud"
                                                             data-bv-message="Este dato no es válido"
                                                             pattern="^[0-9.]+$" required="true" 
                                                             data-bv-regexp-message="Solo se pueden agregar números"
                                                             data-bv-notempty="true"
                                                             data-bv-stringlength="true"
                                                             data-bv-stringlength-max="8"
                                                             data-bv-notempty-message="Este dato es requerido"/>
                                        </div>

                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-3">Transporte</label>
                                        <div class="col-sm-6 input-group">
                                            <span class="input-group-addon"><i class="fa fa-dollar"></i></span>
                                                <s:textfield id="becaTransporte.transporte" name="becaTransporte.transporte" 
                                                             cssClass="numero form-control" placeholder="Transporte"
                                                             data-bv-message="Este dato no es válido"
                                                             pattern="^[0-9.]+$" required="true" 
                                                             data-bv-regexp-message="Solo se pueden agregar números"
                                                             data-bv-notempty="true"
                                                             data-bv-stringlength="true"
                                                             data-bv-stringlength-max="8"
                                                             data-bv-notempty-message="Este dato es requerido"/>
                                        </div>

                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-3">Actividades culturales y recreativas</label>
                                        <div class="col-sm-6 input-group">
                                            <span class="input-group-addon"><i class="fa fa-dollar"></i></span>
                                                <s:textfield id="becaTransporte.actividades" name="becaTransporte.actividades" 
                                                             cssClass="numero form-control" placeholder="Actividades"
                                                             data-bv-message="Este dato no es válido"
                                                             pattern="^[0-9.]+$" required="true" 
                                                             data-bv-regexp-message="Solo se pueden agregar números"
                                                             data-bv-notempty="true"
                                                             data-bv-stringlength="true"
                                                             data-bv-stringlength-max="8"
                                                             data-bv-notempty-message="Este dato es requerido"/>
                                        </div>

                                    </div>

                                    <div class="form-group">

                                        <label class="control-label col-sm-3">Vivienda</label>
                                        <div class="col-sm-6 input-group">
                                            <span class="input-group-addon"><i class="fa fa-dollar"></i></span>
                                                <s:textfield id="becaTransporte.vivienda" name="becaTransporte.vivienda" 
                                                             cssClass="numero form-control" placeholder="Vivienda"
                                                             data-bv-message="Este dato no es válido"
                                                             pattern="^[0-9.]+$" required="true" 
                                                             data-bv-regexp-message="Solo se pueden agregar números"
                                                             data-bv-notempty="true"
                                                             data-bv-stringlength="true"
                                                             data-bv-stringlength-max="8"
                                                             data-bv-notempty-message="Este dato es requerido"/>                                                                                                                                    
                                        </div>                                              
                                        <span id="helpBlockVivienda" class="help-block col-sm-offset-3">
                                            Agua, Luz, Telefono, TV de paga, Internet, Servicios, etc.
                                        </span>
                                    </div>


                                    <div class="form-group">
                                        <label class="control-label col-sm-3">Otros</label>
                                        <div class="col-sm-6 input-group">
                                            <span class="input-group-addon"><i class="fa fa-dollar"></i></span>
                                                <s:textfield id="becaTransporte.otros" name="becaTransporte.otros" 
                                                             cssClass="numero form-control" placeholder="Otros"
                                                             data-bv-message="Este dato no es válido"
                                                             pattern="^[0-9.]+$" required="true" 
                                                             data-bv-regexp-message="Solo se pueden agregar números"
                                                             data-bv-notempty="true"
                                                             data-bv-stringlength="true"
                                                             data-bv-stringlength-max="8"
                                                             data-bv-notempty-message="Este dato es requerido"/>
                                        </div>

                                    </div>
                                    <div id="error-messages" class="col-sm-offset-3">
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-12">
                                <div id="calculoIPC"></div>
                                <div id="calculoIPCError" class="alert alert-danger fade in">
                                    <b>La información ingresada hasta el momento, 
                                        no es consistente con lo ingresado en tu solicitud ordinaria. 
                                        En caso de que aún falte información por capturar, continúa 
                                        ingresandola. En caso de que ya hayas terminando, ten en cuenta 
                                        que podrás Finalizar tu solicitud tal como está, sin embargo 
                                        esta diferencia puede influir en el resultado de tu solicitud de beca
                                    </b>
                                        </div>

                                        <h2 style="margin-top: 10px;">Finalizar Solicitud</h2>
                                        <p style="text-align: justify;">
                                            Autorizo a la instancia pertinente para que pueda verificar los datos asentados en esta solicitud y 
                                            en caso de falta de probidad, podrá ser motivo de que el apoyo se cancele,  aún cuando este ya se me 
                                            haya asignado.<br>
                                            Manifiesto conocer los derechos y obligaciones asociados a la beca solicitada  y me comprometo a 
                                            cumplir con las responsabilidades  que se deriven de la asignación de la misma.
                                        </p>
                                        <div class="checkbox-nice checkbox">
                                            <input data-bv-notempty="true" 
                                                   data-bv-notempty-message="Tienes que aceptar para poder continuar" 
                                                   type="checkbox" name="checkbox" value="check" id="checkbox">
                                            <label for="checkbox">Sí acepto</label>
                                        </div>
                                </div>
                                <button style="margin-bottom: 10px" id="guardarBoton" type="submit" class="btn btn-primary btn pull-right" onclick="if (!this.form.checkbox.checked) {
                                            ModalGenerator.notificacion({
                                                'titulo': '',
                                                'cuerpo': 'Debes aceptar los términos para continuar.',
                                                'tipo': 'INFO',
                                            });
                                            return false
                                        }
                                        "> Finalizar
                                </button>
                        </form>     
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
<content tag="endScripts">        
    <script type="text/javascript" src="/vendors/bootstrap-validator/bootstrapValidator-0.5.3.min.js"></script>        
    <script type="text/javascript" src="/resources/js/ModalGenerator.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>
                                    $(document).ready(function () {
                                        $("#calculoIPCError").hide();
                                        var count = 2;
                                        var countf = 1;

                                        $("#btnadd").on("click", function (event) {
                                            count++;
                                            if (count <= 20) {
                                                $('#tblprod tr:last').after('<tr class="text-center" id="' + count + '">'
                                                        + '<td>' + count + '</td>'
                                                        + '<td id="mt' + count + '"></td>'
                                                        + '<td><div id="pp' + count + '" class="form-group col-lg-12" style="margin: 0;">'
                                                        + '<input id="puntopartida" name="puntopartida" class="form-control"'
                                                        + '</div></td>'
                                                        + '<td><div class="form-group col-lg-12" style="margin: 0;"><input id="puntollegada" name="puntollegada" class="form-control"/></div></td>'
                                                        + '<td><div id="num" class="form-group col-lg-12 numero"  style="margin: 0;"><input id="costo" name="costo" class="form-control"/></div></td>'
                                                        + '<td id="tv' + count + '" ></td>'
                                                        + '</tr>');
                                                $("#transporte").clone().appendTo("#mt" + count);
                                                $("#trayecto").clone().appendTo("#tv" + count);
                                                $("#num").addClass("form-group col-lg-12 numero");
                                            }
                                            crearValidador();
                                        });
                                        $("#btnsubmit").bind("click", function (event) {
                                            if (count > 1) {
                                                $('#tblprod tr:last').remove();
                                                count = count - 1;
                                            }
                                            crearValidador();
                                        });

                                        $("#btnaddF").on("click", function (event) {
                                            countf++;
                                            if (countf <= 20) {
                                                $('#tblfam tr:last').after('<tr class="text-center" id="' + countf + '">'
                                                        + '<td class="text-center">' + countf + '</td>'
                                                        + '<td><div class="form-group col-lg-12" style="margin: 0;"><input id="nombrefamiliar" name="nombrefamiliar" class="form-control"/></div></td>'
                                                        + '<td id="pt' + countf + '"></td>'
                                                        + '<td><div  style="margin: 0;" id="numE" class="form-group col-lg-12"><input id="edad" name="edad" class="form-control numeroE"/></div></td>'
                                                        + '<td><div  style="margin: 0;" class="form-group col-lg-12"><input id="ocupacion" name="ocupacion" class="form-control"/></div></td>'
                                                        + '<td><div  style="margin: 0;" id="num" class="form-group col-lg-12"><input id="aportacionmensual" name="aportacionmensual" class="form-control numero" onblur="calculoIPC();"/></div></td>'
                                                        + '</tr>')
                                                $("#parentesco").clone().appendTo("#pt" + countf);
                                                $("#num").addClass("form-group col-lg-12 numero");
                                                $("#numE").addClass("form-group col-lg-12 numeroE");
                                            }
                                            crearValidador();
                                            calculoIPC();
                                        });
                                        $("#btnsubmitF").bind("click", function (event) {
                                            if (countf > 1) {
                                                $('#tblfam tr:last').remove();
                                                countf = countf - 1;
                                            }
                                            calculoIPC();
                                            crearValidador();
                                        });
                                        
                                        $("[name='aportacionmensual']").focusout(function () {
                                            calculoIPC();
                                        });

                                        crearValidador();
                                    });

                                    function calculoIPC() {
                                        suma = parseFloat(0);
                                        integrantes = 0;
                                        $('.numero#aportacionmensual').each(function () {
                                            if (isNaN($(this).val()) || $(this).val() === "") {
//                                                suma = parseFloat(suma).toFixed(2)+ parseFloat(0);
                                            } else {
                                                integrantes++;
                                                suma = parseFloat(suma) + parseFloat($(this).val());
                                            }
                                        });
                                        if (integrantes > 0 && suma > 0) {
                                            ipc = (suma / integrantes).toFixed(2);
                                            $("#calculoIPC").html("<b>Ingresos por persona en tu familia: </b> $" + ipc);
        <s:if test="ie.ingresosPerCapita!=null">
                                            var ipcOrdinaria = <s:property value="ie.ingresosPerCapita" />;

                                            if (ipcOrdinaria != "" && ipcOrdinaria != ipc) {
                                                $("#calculoIPCError").show();
                                            } else {
                                                $("#calculoIPCError").hide();
                                            }
        </s:if>
                                        }
                                    }

                                    function crearValidador() {
//                                        $('#eseTransporte').bootstrapValidator('destroy');
                                        $('#eseTransporte').bootstrapValidator({
                                            excluded: [':disabled'],
                                            container: '#error-messages'
                                        }).on('success.form.bv', function (e) {
                                            e.preventDefault();
                                            actualizarDialog();
                                        });
                                    }

                                    function actualizarDialog() {
                                        ModalGenerator.notificacion({
                                            "titulo": "Atención",
                                            "cuerpo": "¿Estas seguro de completar el formulario(Después de guardar no se podrán cambiar las respuestas)?",
                                            "tipo": "WARNING",
                                            "funcionAceptar": function () {
                                                document.getElementById('eseTransporte').submit();
                                            },
                                            "funcionCancelar": function () {
                                                $("#eseTransporte").data('bootstrapValidator').resetForm();
                                            },
                                        });
                                    }
    </script>
</content>