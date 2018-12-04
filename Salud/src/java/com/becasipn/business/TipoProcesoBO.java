package com.becasipn.business;

import com.becasipn.persistence.model.TipoProceso;
import com.becasipn.service.Service;
import java.math.BigDecimal;
import java.util.List;

public class TipoProcesoBO extends BaseBO {

    public TipoProcesoBO(Service service) {
	super(service);
    }

    public Boolean guardaProceso(TipoProceso proceso) {
	try {
	    if (proceso.getId() == null) {
		service.getTipoProcesoDao().save(proceso);
	    } else {
		service.getTipoProcesoDao().update(proceso);
	    }
	} catch (Exception e) {
	    return Boolean.FALSE;
	}
	return Boolean.TRUE;
    }

    public TipoProceso getProceso(BigDecimal id) {
	TipoProceso proceso = service.getTipoProcesoDao().findById(id);
	return proceso;
    }

    public Boolean eliminaProceso(TipoProceso proceso) {
	try {
	    service.getTipoProcesoDao().delete(proceso);
	    return true;
	} catch (Exception e) {
	    return false;
	}
    }

    public Boolean procesoEnUso(BigDecimal procesoId) {
	List<TipoProceso> lista = service.getTipoProcesoDao().procesoEnUso(procesoId);
	return !lista.isEmpty();
    }
}
