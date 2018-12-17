package com.becasipn.business;

import com.becasipn.persistence.model.Periodo;
import com.becasipn.service.Service;

/**
 *
 * @author Tania Sánchez
 */
public class PeriodoBO extends BaseBO {

    public PeriodoBO(Service service) {
        super(service);
    }

    public Periodo getPeriodoActivo() {
        Periodo periodo = service.getPeriodoDao().getPeriodoActivo();
        return periodo;
    }

}
