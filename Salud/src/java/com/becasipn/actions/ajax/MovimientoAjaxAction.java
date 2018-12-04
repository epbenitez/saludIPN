package com.becasipn.actions.ajax;

import com.becasipn.persistence.model.TipoProceso;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Victor Lozano
 */
public class MovimientoAjaxAction extends JSONAjaxAction {

    private BigDecimal pkMovimiento;

    public BigDecimal getPkMovimiento() {
	return pkMovimiento;
    }

    public void setPkMovimiento(BigDecimal pkMovimiento) {
	this.pkMovimiento = pkMovimiento;
    }

    /**
     * Devuelve el listado de procesos por movimiento
     *
     * @author Victor Lozano
     * @return SUCCESS_JSON
     */
    public String getProcesos() {
	List<TipoProceso> procesos = getDaos().getTipoProcesoDao().procesoByMovimiento(pkMovimiento);
	if (procesos != null) {
	    for (TipoProceso proc : procesos) {
		getJsonResult().add("[\"" + proc.getId()
			+ "\", \"" + proc.getNombre()
			+ " \"]");
	    }
	}
	return SUCCESS_JSON;
    }
}
