package com.becasipn.business;

import com.becasipn.persistence.model.Notificaciones;
import com.becasipn.persistence.model.NotificacionesRol;
import com.becasipn.service.Service;

/**
 *
 * @author Sistemas DSE
 */
public class NotificacionesRolBO extends BaseBO {

    public NotificacionesRolBO(Service service) {
        super(service);
    }

    public Boolean guardaNotificacion(NotificacionesRol notificacionRol) {
        try {
            if (notificacionRol.getId() == null) {
                service.getNotificacionRolDao().save(notificacionRol);
            } else {
                service.getNotificacionRolDao().update(notificacionRol);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public Boolean eliminaRegistros(Notificaciones notificacion) {
        if (notificacion.getId() == null) {
            return Boolean.FALSE;
        } else {
            if (service.getNotificacionRolDao().eliminaRegistrosAsociados(notificacion)) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        }
    }
}
