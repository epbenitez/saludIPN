package com.becasipn.business;

import com.becasipn.persistence.model.MontoMaximoIngresos;
import com.becasipn.service.Service;

/**
 *
 * @author Tania G. Sánchez Manilla
 */
public class MontoMaximoIngresosBO extends BaseBO {

    public MontoMaximoIngresosBO(Service service) {
        super(service);
    }

    /**
     * Guarda el objeto MontoMaximoIngresos
     *
     * @param montoMaximoIngresos
     * @return boolan
     */
    public boolean guarda(MontoMaximoIngresos montoMaximoIngresos) {
        try {
            if (montoMaximoIngresos.getId() == null) {
                service.getMontoMaximoIngresosDao().save(montoMaximoIngresos);
            } else {
                service.getMontoMaximoIngresosDao().update(montoMaximoIngresos);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}