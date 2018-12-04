<%-- 
    Document   : resultado
    Created on : 4/07/2016, 11:00:45 AM
    Author     : Tania G. Sánchez
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.0//EN" "http://www.w3.org/TR/REC-html40/strict.dtd">
<html>
    <head>
        <title>ESE Beca de Transporte</title>
    </head>
    <body>  
        <div class="col-md-12">
            <div class="col-sm-12 btn-group-vertical">
                <center><a href="#" target='_blank' onclick="addURL(this)" id="eset" class="btn btn btn-primary"><span class="fa fa-download"></span> Descargar archivo PDF</a></center>
            </div>
        </div>
        <div class="main-box clearfix">
            <div class="otorgamiento col-sm-10 col-sm-offset-1">
                <div class="clearfix" >&nbsp;</div>
                <div class="clearfix" >&nbsp;</div>
                <center><img src="/resources/img/eseTransporte/mensajeESEFinalizado.png"/></center>
                <div class="clearfix" >&nbsp;</div>
                <div class="clearfix" >&nbsp;</div>
            </div>
        </div>
    </body>
</html>

<content tag="inlineScripts">
    <script>
        function addURL(element) {
            $("#eset").blur();// Evita que el botón sea desactivado    
            $(element).attr('href', function () {
                return "/misdatos/descargar/pdfEstudioSocioeconomicoTransporte.action?cuestionarioId=2";
            });
        }
    </script>
</content>