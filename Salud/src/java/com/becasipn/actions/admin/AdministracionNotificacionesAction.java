package com.becasipn.actions.admin;

import com.becasipn.business.NotificacionesBO;

import com.becasipn.actions.BaseReportAction;
import com.becasipn.business.NotificacionesRolBO;
import com.becasipn.persistence.model.Notificaciones;
import com.becasipn.persistence.model.NotificacionesRol;
import com.becasipn.persistence.model.Rol;
import com.becasipn.persistence.model.TipoNotificacion;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Augusto I. Hernández Ruiz
 */
public class AdministracionNotificacionesAction extends BaseReportAction {

    public static final String ADMINISTRAR = "administrar";
    public static final String EDICION = "edicion";
    public static final String ELIMINAR = "eliminar";

    private Notificaciones notificacion = new Notificaciones();
    private String checkboxRol;
    private TipoNotificacion finalTipo;
    private final Rol rol = new Rol();
    List<TipoNotificacion> tipoNotificacionList = new ArrayList<>();
    private String error;
    private String correcto;

    public String crear() {
        tipoNotificacionList.clear();
        NotificacionesBO bo = new NotificacionesBO(getDaos());
        tipoNotificacionList = bo.getAllTipoNotificacion();
        return SUCCESS;
    }

    public String administrar() {
        return SUCCESS;
    }

    public String guarda() {
        //Guardamos la notificación.
        NotificacionesBO bo = new NotificacionesBO(getDaos());
        notificacion = bo.guardaNotificacion(notificacion);

        //Guardamos los registros de roles en rmm_notificaciones_rol asociados a la notificacion.
        if (notificacion.getId() != null) {
            if (guardaNotificacionRol(notificacion, checkboxRol)) {
                addActionMessage(getText("Notificación guardada con Éxito"));
            }
        } else {
            addActionError(getText("Error al guardar la notificación"));
        }
        return ADMINISTRAR;
    }

    public boolean guardaNotificacionRol(Notificaciones notificacion, String checkboxRol) {
        //Borramos todos los registros de rmm_notificaciones_rol asociados a la notificacion.
        NotificacionesRolBO bo = new NotificacionesRolBO(getDaos());
        if (bo.eliminaRegistros(notificacion)) {

            //Hacemos un registro por cada Rol
            String[] parts = checkboxRol.split(",");
            for (String part : parts) {
                NotificacionesRol notificacionesRol = new NotificacionesRol();
                notificacionesRol.setNotificacion(notificacion);
                BigDecimal id_aux = new BigDecimal(Integer.parseInt(part));
                rol.setId(id_aux);
                notificacionesRol.setRol(rol);
                if (bo.guardaNotificacion(notificacionesRol)) {

                } else {
                    addActionError(getText("Error al guardar la notificación"));
                }
            }
            return Boolean.TRUE;
        } else {
            addActionError(getText("Error al guardar la notificación"));
            return Boolean.FALSE;
        }
    }

    public String edicion() {
        tipoNotificacionList.clear();
        NotificacionesBO bo = new NotificacionesBO(getDaos());
        tipoNotificacionList = bo.getAllTipoNotificacion();

        checkboxRol = "";
        List<BigDecimal> lista = getDaos().getNotificacionRolDao().rolesByNotificacionId(notificacion);
        for (BigDecimal element : lista) {
            checkboxRol = checkboxRol + ", " + element;
        }
        checkboxRol = checkboxRol.replaceFirst(",", "");

        if (notificacion == null || notificacion.getId() == null) {
            addActionError(getText("Error al guardar la notificación"));
        }

        //Traemos todos los datos de mi objeto
        notificacion = bo.getNotificaciones(notificacion.getId());
        return EDICION;
    }

    public String eliminar() {
        if (notificacion == null || notificacion.getId() == null) {
            addActionError(getText("Error al eliminar la notificación"));
        }
        //Borramos todos los registros de rmm_notificaciones_rol asociados a la notificacion.
        NotificacionesRolBO nrbo = new NotificacionesRolBO(getDaos());
        if (nrbo.eliminaRegistros(notificacion)) {

            NotificacionesBO bo = new NotificacionesBO(getDaos());

            Boolean res = bo.eliminaNotificacion(notificacion);
            if (res) {
                NotificacionesRolBO boo = new NotificacionesRolBO(getDaos());
                if (boo.eliminaRegistros(notificacion)) {
                    addActionMessage(getText("Notificación eliminada correctamente"));
                    correcto = "Notificación eliminada correctamente";
                }
                addActionMessage(getText("Notificación eliminada correctamente"));
                correcto = "Notificación eliminada correctamente";
            } else {
                addActionError(getText("Error al eliminar la notificación"));
                error = "Error al eliminar la notificación!";
            }
        } else {
            addActionError(getText("Error al eliminar la notificación"));
            error = "Error al eliminar la notificación!";
        }
        return ELIMINAR;
    }

    public Notificaciones getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(Notificaciones notificacion) {
        this.notificacion = notificacion;
    }

    public List<TipoNotificacion> getTipoNotificacionList() {
        return tipoNotificacionList;
    }

    public void setTipoNotificacionList(List<TipoNotificacion> tipoNotificacionList) {
        this.tipoNotificacionList = tipoNotificacionList;
    }

    public String getCheckboxRol() {
        return checkboxRol;
    }

    public void setCheckboxRol(String checkboxRol) {
        this.checkboxRol = checkboxRol;
    }

    public TipoNotificacion getFinalTipo() {
        return finalTipo;
    }

    public void setFinalTipo(TipoNotificacion finalTipo) {
        this.finalTipo = finalTipo;
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
