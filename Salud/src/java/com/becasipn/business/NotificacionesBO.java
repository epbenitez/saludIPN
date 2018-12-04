package com.becasipn.business;

import com.becasipn.persistence.model.Notificaciones;
import com.becasipn.persistence.model.Rol;
import com.becasipn.persistence.model.TipoNotificacion;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.service.Service;
import com.opensymphony.xwork2.ActionContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Augusto I. Hernández Ruiz
 */
public class NotificacionesBO extends BaseBO {

    public NotificacionesBO(Service service) {
        super(service);
    }

    public Notificaciones guardaNotificacion(Notificaciones notificacion) {
        notificacion.setUsuario((Usuario) ActionContext.getContext().getSession().get("usuario"));
        try {
            if (notificacion.getId() == null) {
                notificacion = service.getNotificacionesDao().save(notificacion);
            } else {
                notificacion = service.getNotificacionesDao().update(notificacion);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return notificacion;
        }
        return notificacion;
    }

    public Notificaciones getNotificaciones(BigDecimal id) {
        Notificaciones ua = service.getNotificacionesDao().findById(id);
        return ua;
    }

    public Boolean eliminaNotificacion(Notificaciones notificacion) {
        try {
            service.getNotificacionesDao().delete(notificacion);
            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    List<TipoNotificacion> tipoNotificacion = new ArrayList<>();

    public List<TipoNotificacion> getAllTipoNotificacion() {
        if (tipoNotificacion == null || tipoNotificacion.isEmpty()) {
            tipoNotificacion = service.getTipoNotificacionDao().findAll();
        }
        return tipoNotificacion;
    }

    List<Notificaciones> notificacionByRol = new ArrayList<>();

    public List<Notificaciones> getNotificacionByRol(Rol rol) {
        notificacionByRol = service.getNotificacionesDao().findNotificacionesByRol(rol);
        for (Notificaciones notificacionByRol1 : notificacionByRol) {
            String aux = notificacionByRol1.getNotificacion();
            aux = aux.replaceAll("\"", "'").replaceAll("</p>", "</p><br>");
            notificacionByRol1.setNotificacion(aux);
        }
        return notificacionByRol;
    }
}
