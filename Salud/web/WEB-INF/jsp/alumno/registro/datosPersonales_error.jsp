<%-- 
    Document   : datospersonales_error
    Created on : 19-ago-2015, 09:33:22
    Author     : Patricia Benitez
--%>

<%@taglib prefix="s" uri="/struts-tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.0//EN" "http://www.w3.org/TR/REC-html40/strict.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Datos personales</title>
    </head>
    <body>
        <p>&nbsp;</p>
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

    </body>
</html>
