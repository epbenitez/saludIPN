package com.becasipn.actions.admin;

import com.becasipn.actions.BaseReportAction;
import com.becasipn.persistence.model.TipoNotificacion;
import com.becasipn.business.TipoNotificacionBO;

/**
 *
 * @author augusto H.
 */
public class AdministracionTipoNotificacionAction extends BaseReportAction {

    public static final String ADMINISTRAR = "administrar";
    public static final String EDICION = "edicion";
    public static final String ELIMINAR = "eliminar";

    private TipoNotificacion tipoNotificacion = new TipoNotificacion();
    private String error;
    private String correcto;

    public String crear() {
        return SUCCESS;
    }

    public String edicion() {
        if (tipoNotificacion == null || tipoNotificacion.getId() == null) {
            addActionError(getText("Error al guardar la notificación"));
        }

        TipoNotificacionBO bo = new TipoNotificacionBO(getDaos());
        //Traemos todos los datos de mi objeto
        tipoNotificacion = bo.getNotificaciones(tipoNotificacion.getId());
        return EDICION;
    }

    public String guarda() {
        TipoNotificacionBO bo = new TipoNotificacionBO(getDaos());
        if (bo.guardaTipoNotificacion(tipoNotificacion)) {
            addActionMessage(getText("Tipo Notificación guardada con Éxito"));
        } else {
            addActionError(getText("Error al guardar el Tipo de Notificación"));
        }
        return ADMINISTRAR;
    }

    public String administrar() {
        return SUCCESS;
    }

    public String eliminar() {
        if (tipoNotificacion == null || tipoNotificacion.getId() == null) {
            addActionError(getText("Error al eliminar la notificación"));
        }
        TipoNotificacionBO bo = new TipoNotificacionBO(getDaos());

        Boolean res = bo.eliminaNotificacion(tipoNotificacion);
        if (res) {
            correcto = "Notificación eliminada correctamente";
            addActionMessage(getText("Notificación eliminada correctamente"));
        } else {
            error = "No se puede borrar la notificación porque se encuentra en uso!";
            addActionError(getText("No se puede borrar la notificación porque se encuentra en uso!"));
        }
        return ELIMINAR;
    }

    public TipoNotificacion getTipoNotificacion() {
        return tipoNotificacion;
    }

    public void setTipoNotificacion(TipoNotificacion tipoNotificacion) {
        this.tipoNotificacion = tipoNotificacion;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getCorrecto() {
        return correcto;
    }

    public void setCorrecto(String correcto) {
        this.correcto = correcto;
    }
    
}
