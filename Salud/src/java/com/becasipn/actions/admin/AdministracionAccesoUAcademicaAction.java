package com.becasipn.actions.admin;

import com.becasipn.actions.BaseAction;
import com.becasipn.business.AccesosBO;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.SolicitudBecaUA;
import com.becasipn.persistence.model.Usuario;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Admin CAE
 */
public class AdministracionAccesoUAcademicaAction extends BaseAction {

    private SolicitudBecaUA nuevoAcceso = new SolicitudBecaUA();
    private String fechaInicial;
    private String fechaFinal;
    private String uaL;
    private String periodoId;

    public String configuracion() {
        return SUCCESS;
    }

    public String form() {
        return "nuevo";
    }

    public String guardar() throws ParseException {

        Periodo p = getDaos().getPeriodoDao().findById(nuevoAcceso.getPeriodo().getId());
        nuevoAcceso.setPeriodo(p);

        //Procesar fechas
        if (fechaInicial == null || fechaInicial.length() < 0) {
            addActionError("Sin fecha inicial");
            return "formulario";
        } else {
            Date fechaInicio = new SimpleDateFormat("dd/MM/yyyy").parse(fechaInicial);
            nuevoAcceso.setFechaInicio(fechaInicio);
        }

        if (fechaFinal == null || fechaFinal.length() < 0) {
            addActionError("Sin fecha final");
            return "formulario";
        } else {
            Date fechaFin = new SimpleDateFormat("dd/MM/yyyy").parse(fechaFinal);
            nuevoAcceso.setFechaFin(fechaFin);
        }

        if (nuevoAcceso.getFechaInicio().after(nuevoAcceso.getFechaFin())) {
            addActionError("Fecha inicial mayor que fecha final");
            return "formulario";
        }

        String usuarioSesion = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario u = getDaos().getUsuarioDao().findById(usuarioSesion);
        nuevoAcceso.setUsuario(u);

        AccesosBO aBo = new AccesosBO(getDaos());

        if (aBo.nuevoAccesoUA(nuevoAcceso, uaL)) {
            addActionMessage("Guardado exitoso");
            return "formulario";
        } else {
            addActionError("Guardado Erroneo");
            return "formulario";
        }

    }

    public String editar() {
        if (nuevoAcceso == null || nuevoAcceso.getId() == null) {
            addActionError("No ha especificado el alumno por editar");
            return "success";
        }
        nuevoAcceso = getDaos().getSolicitudBecaUADao().findById(nuevoAcceso.getId());
        uaL = nuevoAcceso.getUnidadAcademica().getId().toString();
        return "formulario";
    }

    public String eliminar() {
        if (nuevoAcceso == null || nuevoAcceso.getId() == null) {
            addActionError("No ha especificado el alumno por eliminar");
            return "success";
        }
        nuevoAcceso = getDaos().getSolicitudBecaUADao().findById(nuevoAcceso.getId());
        nuevoAcceso.setEstatus(0);
        nuevoAcceso.setFechaModificacion(new Date());
        getDaos().getSolicitudBecaUADao().update(nuevoAcceso);
        addActionMessage("Eliminacion exitosa");
        return "success";
    }

    public SolicitudBecaUA getNuevoAcceso() {
        return nuevoAcceso;
    }

    public void setNuevoAcceso(SolicitudBecaUA nuevoAcceso) {
        this.nuevoAcceso = nuevoAcceso;
    }

    public String getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(String fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public String getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(String fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public String getUaL() {
        return uaL;
    }

    public void setUaL(String uaL) {
        this.uaL = uaL;
    }

    public String getPeriodoId() {
        return periodoId;
    }

    public void setPeriodoId(String periodoId) {
        this.periodoId = periodoId;
    }

}
