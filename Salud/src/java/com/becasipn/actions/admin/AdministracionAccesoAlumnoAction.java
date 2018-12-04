package com.becasipn.actions.admin;

import com.becasipn.actions.BaseAction;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.SolicitudBecaIndividual;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.util.UtilFile;
import java.util.Date;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Admin CAE
 */
public class AdministracionAccesoAlumnoAction extends BaseAction {

    private SolicitudBecaIndividual nuevoAcceso = new SolicitudBecaIndividual();
    private String fechaInicial;
    private String fechaFinal;

    public String configuracion() {
        return SUCCESS;
    }

    public String form() {
        return SUCCESS;
    }

    public String guardar() {

        Periodo p = getDaos().getPeriodoDao().findById(nuevoAcceso.getPeriodo().getId());
        nuevoAcceso.setPeriodo(p);
        
        //Valida la boleta
        Alumno a = getDaos().getAlumnoDao().findByBoleta(nuevoAcceso.getAlumno().getBoleta());
        if (a == null) {
            addActionError("No se encuentra un alumno con la boleta especificada (" + nuevoAcceso.getAlumno().getBoleta() + ")");
            return "formulario";
        } else {
            nuevoAcceso.setAlumno(a);
        }
        
        //Procesar fechas
        if (fechaInicial != null && fechaInicial.length() > 0) {
            nuevoAcceso.setFechaInicio(UtilFile.strToDate(fechaInicial, "dd/MM/yyyy"));
        }
        else {
            addActionError("Sin fecha inicial");
            return "formulario";
        }
        
        if (fechaFinal != null && fechaFinal.length() > 0) {
            nuevoAcceso.setFechaFin(UtilFile.strToDate(fechaFinal, "dd/MM/yyyy"));
        }
        else {
            addActionError("Sin fecha final");
            return "formulario";
        }
        
        if (nuevoAcceso.getFechaInicio().after(nuevoAcceso.getFechaFin())) {
            addActionError("Fecha inicial mayor que fecha final");
            return "formulario";
        }
        Date fechaActual = new Date();
        nuevoAcceso.setFechaModificacion(fechaActual);
        nuevoAcceso.setEstatus(Boolean.TRUE);
        String usuarioSesion = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario u = getDaos().getUsuarioDao().findById(usuarioSesion);
        nuevoAcceso.setUsuario(u);
        if (nuevoAcceso == null || nuevoAcceso.getId() == null) {
            getDaos().getSolicitudBecaIndividualDao().save(nuevoAcceso);
        } else {
            getDaos().getSolicitudBecaIndividualDao().update(nuevoAcceso);
        }
        addActionMessage("Guardado exitoso");
        return "formulario";
    }

    public String editar() {
        if (nuevoAcceso == null || nuevoAcceso.getId() == null) {
            addActionError("No ha especificado el alumno por editar");
            return "configuracion";
        }
        nuevoAcceso = getDaos().getSolicitudBecaIndividualDao().findById(nuevoAcceso.getId());
        return SUCCESS;
    }
    
    public String eliminar() {
        if (nuevoAcceso == null || nuevoAcceso.getId() == null) {
            addActionError("No ha especificado el alumno por eliminar");
            return "configuracion";
        }
        nuevoAcceso = getDaos().getSolicitudBecaIndividualDao().findById(nuevoAcceso.getId());
        nuevoAcceso.setEstatus(Boolean.FALSE);
        nuevoAcceso.setFechaModificacion(new Date());
        getDaos().getSolicitudBecaIndividualDao().update(nuevoAcceso);
        addActionMessage("Eliminacion exitosa");
        return "configuracion";
    }
    
    public SolicitudBecaIndividual getNuevoAcceso() {
        return nuevoAcceso;
    }

    public void setNuevoAcceso(SolicitudBecaIndividual nuevoAcceso) {
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
}