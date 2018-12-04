package com.becasipn.actions.ajax;

import static com.becasipn.actions.ajax.JSONAjaxAction.SUCCESS_JSON;
import com.becasipn.persistence.model.Periodo;
import java.math.BigDecimal;

/**
 *
 * @author Tania G. Sánchez
 */
public class MesAjaxAction extends JSONAjaxAction {

    private BigDecimal pkPeriodo;

    public BigDecimal getPkPeriodo() {
        return pkPeriodo;
    }

    public void setPkPeriodo(BigDecimal pkPeriodo) {
        this.pkPeriodo = pkPeriodo;
    }

    /**
     * Acción que obtiene el mes dado el periodo.
     *
     * @return un string representando el resultado lógico de la ejecución de la
     * acción: SUCCESS
     */
    public String getMesPeriodo() {
        Periodo periodo = getDaos().getPeriodoDao().findById(pkPeriodo);
        if (periodo.getClave().substring(5, 6).equals("1")) {
            getJsonResult().add("[\"" + 8
                    + "\", \"" + "Agosto"
                    + " \"]");
            getJsonResult().add("[\"" + 9
                    + "\", \"" + "Septiembre"
                    + " \"]");
            getJsonResult().add("[\"" + 10
                    + "\", \"" + "Octubre"
                    + " \"]");
            getJsonResult().add("[\"" + 11
                    + "\", \"" + "Noviembre"
                    + " \"]");
            getJsonResult().add("[\"" + 12
                    + "\", \"" + "Diciembre"
                    + " \"]");
            getJsonResult().add("[\"" + 1
                    + "\", \"" + "Enero"
                    + " \"]");
        } else if (periodo.getClave().substring(5, 6).equals("2")) {
            getJsonResult().add("[\"" + 2
                    + "\", \"" + "Febrero"
                    + " \"]");
            getJsonResult().add("[\"" + 3
                    + "\", \"" + "Marzo"
                    + " \"]");
            getJsonResult().add("[\"" + 4
                    + "\", \"" + "Abril"
                    + " \"]");
            getJsonResult().add("[\"" + 5
                    + "\", \"" + "Mayo"
                    + " \"]");
            getJsonResult().add("[\"" + 6
                    + "\", \"" + "Junio"
                    + " \"]");
            getJsonResult().add("[\"" + 7
                    + "\", \"" + "Julio"
                    + " \"]");
        }
        return SUCCESS_JSON;
    }
}