package com.becasipn.actions.ajax;

import com.becasipn.persistence.model.TipoProceso;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @Tania G. Sánchez
 */
public class TipoProcesoAjaxAction extends JSONAjaxAction {

    private BigDecimal pkPeriodo;
    private BigDecimal pkMovimiento;

    /**
     * Acción que obtiene el tipo de proceso dado el periodo.
     *
     * @return un string representando el resultado lógico de la ejecución de la
     * acción: SUCCESS
     */
    public String getTipoProceso() {
	List<TipoProceso> tipoProceso = getDaos().getTipoProcesoDao().getProcesosActivosPeriodo(pkPeriodo);
	if (tipoProceso == null) {
	} else {
	    for (TipoProceso tp : tipoProceso) {
		getJsonResult().add("[\"" + tp.getId()
			+ "\", \"" + tp.getNombre()
			+ " \"]");
	    }
	}
	return SUCCESS_JSON;
    }

    /**
     * Devuelve el listado de procesos del sistema
     *
     * @author Victor Lozano
     * @return SUCCESS_JSON
     */
    public String listado() {
	List<TipoProceso> lista = getDaos().getTipoProcesoDao().findAll();
	for (TipoProceso proceso : lista) {
	    getJsonResult().add("[\"" + proceso.getNombre()
		    //                    + "\", \"" + proceso.getDescripcion()
		    + "\", \"" + proceso.getMovimiento().getNombre()
		    + "\", \"<a title='Editar proceso' class='fancybox fancybox.iframe solo-lectura'  href='/catalogos/edicionTipoProceso.action?proceso.id=" + proceso.getId() + "'> <span class=\\\"fa-stack\\\"><i class=\\\"fa fa-square fa-stack-2x\\\"></i><i class=\\\"fa fa-pencil-square-o fa-stack-1x fa-inverse\\\"></i></span> </a>"
		    + "\", \"<a title='Eliminar proceso' onclick='eliminar(" + proceso.getId() + ")' class='solo-lectura table-link danger'> <span class=\\\"fa-stack\\\"><i class=\\\"fa fa-square fa-stack-2x\\\"></i><i class=\\\"fa fa-trash-o fa-stack-1x fa-inverse\\\"></i></span> </a>\"]");
	}
	return SUCCESS_JSON;
    }

    public String get() {
	List<TipoProceso> procesos;
	if (pkMovimiento == null || pkMovimiento.equals(new BigDecimal(0))) {
	    procesos = getDaos().getTipoProcesoDao().findAll();
	    getJsonResult().add("[\"0\", \"-Todos- \",\"0\"]");
	} else {
	    procesos = getDaos().getTipoProcesoDao().procesoByMovimiento(pkMovimiento);
	    getJsonResult().add("[\"0\", \"-Todos " + getDaos().getMovimientoDao().findById(pkMovimiento).getNombre() + "- \",\"0\"]");
	}
	for (TipoProceso pr : procesos) {
	    getJsonResult().add("[\"" + pr.getId()
		    + "\", \"" + pr.getNombre()
		    + " \",\"" + pr.getDescripcion() + "\"]");
	}
	return SUCCESS_JSON;
    }

    public BigDecimal getPkPeriodo() {
	return pkPeriodo;
    }

    public void setPkPeriodo(BigDecimal pkPeriodo) {
	this.pkPeriodo = pkPeriodo;
    }

    public BigDecimal getPkMovimiento() {
	return pkMovimiento;
    }

    public void setPkMovimiento(BigDecimal pkMovimiento) {
	this.pkMovimiento = pkMovimiento;
    }
}
