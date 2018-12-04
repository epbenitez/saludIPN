package com.becasipn.business;

import com.becasipn.persistence.model.SolicitudCuentas;
import com.becasipn.service.Service;

public class SolicitudCuentasBO extends BaseBO {

    public SolicitudCuentasBO(Service service) {
        super(service);
    }

    public Boolean guarda(SolicitudCuentas solicitud) {
        try {
            if (solicitud.getId() == null) {
                service.getSolicitudCuentasDao().save(solicitud);
                return Boolean.TRUE;
            } else {
                service.getSolicitudCuentasDao().update(solicitud);
                return Boolean.TRUE;
            }
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

}
