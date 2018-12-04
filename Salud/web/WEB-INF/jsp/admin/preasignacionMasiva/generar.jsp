<%-- 
    Document   : generar.jsp
    Created on : 29/08/2017, 16:38 PM
    Author     : Othoniel Herrera
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<head>
    <title>Presignación masiva</title>
    <style>
        .dt-buttons{
            margin-top: 10px;
            margin-left: 10px;
        }
    </style>
</head>

<content tag="tituloJSP">
    Preasignación masiva
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
    <div class="row main_content">
        <div class="clearfix" >&nbsp;</div>
        <s:if test="%{solicitudesProcesadas>0}">
            <div class="col-sm-4 col-xs-12">
                <div class="main-box infographic-box colored" style="background-color: #9c27b0">
                    <i class="fa fa-magic"></i>
                    <span class="headline no-newline" >Solicitudes procesadas</span>
                    <span id="solicitudesProcesadas" class="value"><s:property value="solicitudesProcesadas"/></span>
                    <a  title='Resumen' target='_blank' href='/admin/descargarPreasignacionMasiva.action' style="color: white;">
                        <i class='fa fa-file-excel-o' style="font-size: 20px; position: absolute; bottom: 0; right: -25px;" ></i>
                    </a>
                </div>
            </div>
            <div class="col-sm-4 col-xs-12">
                <div class="main-box infographic-box colored" style="background-color: #8bc34a">
                    <i class="fa fa-check-circle-o"></i>
                    <span class="headline no-newline" >Con preasignación</span>
                    <span id="solicitudesPreasignadas" class="value"><s:property value="solicitudesPreasignadas"/></span>
                </div>
            </div>
            <div class="col-sm-4 col-xs-12">
                <div class="main-box infographic-box colored" style="background-color: #e84e40">
                    <i class="fa fa-times-circle-o"></i>
                    <span class="headline no-newline" >Sin preasignación</span>
                    <span id="solicitudesNoPreasignadas" class="value"><s:property value="solicitudesNoPreasignadas"/></span>
                </div>
            </div>
        </s:if>
        <div class="col-sm-12">
            <div class="responsive-table table-responsive main-box clearfix">
                <table id="listado" class="table table-striped table-hover">
                    <thead>
                        <tr>
                            <th>Unidad Académica</th>
                            <th align="center">Solicitudes procesadas</th>
                            <th align="center">Solicitudes preasignadas</th>
                            <th align="center">Solicitudes no preasignadas</th>
                        </tr>
                    </thead>
                    <s:iterator value="resumen">
                        <tr>
                            <td ><s:property  value="unidadAcademica" /></td>
                            <td align="center"><s:property  value="solicitudesProcesadasUA" /></td>
                            <td align="center"><s:property  value="solicitudesPreasignadasUA" /></td>
                            <td align="center"><s:property  value="solicitudesNoPreasignadasUA" /></td>
                        </tr>
                    </s:iterator>
                </table>
            </div>
        </div>
    </div>
</body>