package com.becasipn.actions.ajax;

import com.becasipn.persistence.model.DepositoNoCoincide;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez
 */
public class DepositoNoCoincideAjaxAction extends JSONAjaxAction {

    private Long ordenId;

    public String detalle() {
        List<DepositoNoCoincide> lista = getDaos().getDepositoNoCoincideDao().depositosSinCoincidencias(ordenId);
        for (DepositoNoCoincide d : lista) {
            getJsonResult().add("[\"" + d.getBoleta()
                    + "\", \"" + d.getNumtarjetabancaria()
                    + "\", \"" + d.getMonto()
                    + "\", \"" + d.getEstatusDeposito().getNombre()
                    + " \"]");
        }
        return SUCCESS_JSON;
    }

    public Long getOrdenId() {
        return ordenId;
    }

    public void setOrdenId(Long ordenId) {
        this.ordenId = ordenId;
    }
}
