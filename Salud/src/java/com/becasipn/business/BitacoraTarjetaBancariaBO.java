package com.becasipn.business;

import com.becasipn.persistence.model.BitacoraTarjetaBancaria;
import com.becasipn.service.Service;

/**
 * @author Victor Lozano
 * @date 17/08/2016
 */
public class BitacoraTarjetaBancariaBO extends BaseBO {

    public BitacoraTarjetaBancariaBO(Service service) {
        super(service);
    }

    /**
     * Almacena la información de la entidad en la base de datos
     *
     * @author Victor Lozano
     * @param asignacion
     * @return true si la operación se realizó exitosamente
     */
    public Boolean guardaBitacora(BitacoraTarjetaBancaria asignacion) {
        try {
            if (asignacion.getId() == null) {
                service.getBitacoraTarjetaBancariaDao().save(asignacion);
                return Boolean.TRUE;
            } else {
                service.getBitacoraTarjetaBancariaDao().update(asignacion);
                return Boolean.TRUE;
            }
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }
}
