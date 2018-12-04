<%-- 
    Document   : form
    Created on : 28/10/2016, 12:44:47 PM
    Author     : Rafael Cardenas Resendiz
--%>

<%@taglib prefix="s" uri="/struts-tags" %>
<head>
    <title>Resultados de Carga SUBES</title>
    <link rel="stylesheet" href="/css/libs/fileinput.css" type="text/css" />
    <script type="text/javascript" src="/js/fileinput.js"></script>
	<style>
        table, th, td {
            text-align: center;
        }
    </style>
	<link rel="stylesheet" type="text/css" href="/vendors/datatables/datatables.min.css">
    <link rel="stylesheet" type="text/css" href="/resources/css/customTables.min.css" />
</head>

<content tag="tituloJSP">
    Resultados de Carga SUBES
</content>
<body>
    <div class="row">
        <div class="col-sm-12">
            <div class="clearfix">&nbsp;</div>  
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

		<div class="row">
			<div class="col-lg-12">
				<div class="main-box clearfix">
					<div class="clearfix">&nbsp;</div>   
					<h3 style="margin-top: 5px; margin-left: 10px">Resultados</h3>
					<div class="main-box-body clearfix">
						<div class="table-responsive">
							<table class="table">
								<tbody>
									<tr class="warning">
										<td>
											Procesados
										</td>
										<td>
											<s:property value="procesados" />
										</td>
									</tr>
									<tr class="success">
										<td>
											Solicitudes creadas
										</td>
										<td>
											<s:property value="correctos" />
										</td>
									</tr>
									<tr class="info">
										<td>
											Solicitudes transporte
										</td>
										<td>
											<s:property value="solicitudesCreadas" />
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
</body>

<content tag="endScripts">
    <script type="text/javascript" src="/vendors/datatables/datatables.min.js"></script>
</content>

<content tag="inlineScripts">
    <script>

    </script>
</content>