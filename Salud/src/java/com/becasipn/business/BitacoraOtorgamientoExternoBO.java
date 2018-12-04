
package com.becasipn.business;

import com.becasipn.persistence.model.BitacoraOtorgamientoExterno;
import com.becasipn.service.Service;
import java.math.BigDecimal;


public class BitacoraOtorgamientoExternoBO extends BaseBO{
    
    public BitacoraOtorgamientoExternoBO(Service service) {
        super(service);
    }
    
    public Boolean guardaBitacoraOtorgamientoExterno(BitacoraOtorgamientoExterno boe) {
        try {
            if (boe.getId() == null) {
                service.getBitacoraOtorgamientoExternoDao().save(boe);
            } else {
                service.getBitacoraOtorgamientoExternoDao().update(boe);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
    
    public BitacoraOtorgamientoExterno getBitacoraOtorgamientoExterno(BigDecimal id) {
        BitacoraOtorgamientoExterno boe = service.getBitacoraOtorgamientoExternoDao().findById(id);
        return boe;
    }
}

