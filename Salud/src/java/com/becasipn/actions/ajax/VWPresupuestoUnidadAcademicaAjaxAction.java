package com.becasipn.actions.ajax;

import com.becasipn.persistence.model.VWPresupuestoUnidadAcademica;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Victor Lozano
 * @date 15/07/2016
 */
public class VWPresupuestoUnidadAcademicaAjaxAction extends JSONAjaxAction {

    private BigDecimal id_ua;

    /**
     * Devuelve el presupuesto por unidad academica
     *
     * @return SUCCESS_JSON
     */
    public String listado() {
        List<VWPresupuestoUnidadAcademica> lista = getDaos().getVwPresupuestoUnidadAcademicaDao().getPresupuestosPorUA(id_ua);
        if (lista != null && !lista.isEmpty()) {
            for (VWPresupuestoUnidadAcademica presupuesto : lista) {
                getJsonResult().add("[\"" + presupuesto.getTipoBecaPeriodo().getTipoBeca().getNombre()
                        + "\", \"" + presupuesto.getMontoPresupuestado()
                        + "\", \"" + (presupuesto.getMontoEjercido() == null ? "0" : presupuesto.getMontoEjercido())
                        + "\", \"" + presupuesto.getMontoPorBeca()
                        + "\", \"" + presupuesto.getBecasDisponibles()
                        + "\"]");
            }
        }
        return SUCCESS_JSON;
    }

    public BigDecimal getId_ua() {
        return id_ua;
    }

    public void setId_ua(BigDecimal id_ua) {
        this.id_ua = id_ua;
    }
}
