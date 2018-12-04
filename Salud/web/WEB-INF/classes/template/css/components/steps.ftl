

<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td valign="bottom" align="right">
            <table width="300" border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td valign="bottom"><img src="<@s.url value="/resources/images/stepComp/${parameters.accion?default('')}.gif" />"  /></td>
                </tr>
            </table>
        </td>
        <td valign="bottom">
            <table width="330" border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <#list 1..parameters.maxStep as i>
                        <td><div class="pasos_${parameters.maxStep}"><@s.text name="${parameters.raiz?default('')}.paso${i}" /></div></td>
                    </#list>
                </tr>
                <tr>
                    <td colspan="${parameters.maxStep}" valign="bottom"><img src="<@s.url value="/resources/images/stepComp/${parameters.step}de${parameters.maxStep}.gif" />" /></td>
                </tr>
            </table>
        </td>
    </tr>
</table>