package com.becasipn.actions.ajax;

import static com.becasipn.actions.ajax.JSONAjaxAction.SUCCESS_JSON;
import com.becasipn.persistence.model.MontoMaximoIngresos;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez Manilla
 */
public class MontoMaximoIngresosAjaxAction extends JSONAjaxAction {
    public String listado() {
        List<MontoMaximoIngresos> lista = getDaos().getMontoMaximoIngresosDao().findAll();
        for (MontoMaximoIngresos montoMaximoIngresos : lista) {
            getJsonResult().add("[\"" + montoMaximoIngresos.getPeriodo().getClave()
                    + "\", \"" + montoMaximoIngresos.getMonto()
                    + "\", \"" + (montoMaximoIngresos.getDeciles() ? "Si" : "No")
                    + "\", \"<a title='Editar periodo' class='fancybox fancybox.iframe'  href='/catalogos/formularioMontoMaximoIngresos.action?montoMaximoIngresos.id=" + montoMaximoIngresos.getId() + "'>"
                    + "<span class=\\\"fa-stack\\\"><i class=\\\"fa fa-square fa-stack-2x\\\"></i> "
                    + "<i class=\\\"fa fa-pencil-square-o fa-stack-1x fa-inverse\\\"></i> "
                    + "</span>"
                    + "</a>\"]");
        }
        return SUCCESS_JSON;
    }
}