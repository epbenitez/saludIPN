<%-- 
    Document   : ajaxJSONPaginated
    Author: Jesus Fernandez
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

{
"sEcho": <s:property value="jsonDraw" escape="false" />,
"iTotalRecords": <s:property value="jsonTotalRecords" escape="false" />,
"iTotalDisplayRecords": <s:property value="jsonDisplayRecords" escape="false" />,
"data": <s:property value="jsonResult" escape="false" />
}