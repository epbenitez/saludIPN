package com.becasipn.business;

import com.becasipn.persistence.model.TipoNotificacion;
import com.becasipn.service.Service;
import java.math.BigDecimal;

/**
 *
 * @author Sistemas DSE
 */
public class TipoNotificacionBO extends BaseBO {

    public TipoNotificacionBO(Service service) {
        super(service);
    }

    public Boolean guardaTipoNotificacion(TipoNotificacion tipoNotificacion) {
        try {
            if (tipoNotificacion.getId() == null) {
                service.getTipoNotificacionDao().save(tipoNotificacion);
            } else {
                service.getTipoNotificacionDao().update(tipoNotificacion);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public TipoNotificacion getNotificaciones(BigDecimal id) {
        TipoNotificacion ua = service.getTipoNotificacionDao().findById(id);
        return ua;
    }

    public Boolean eliminaNotificacion(TipoNotificacion tipoNotificacion) {
        try {
            if (service.getTipoNotificacionDao().consistencia(tipoNotificacion)) {
                return Boolean.FALSE;
            } else {
                service.getTipoNotificacionDao().delete(tipoNotificacion);
                return Boolean.TRUE;
            }
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

}
