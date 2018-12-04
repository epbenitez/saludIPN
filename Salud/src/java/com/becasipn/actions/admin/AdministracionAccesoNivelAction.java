package com.becasipn.actions.admin;

import com.becasipn.actions.BaseAction;
import com.becasipn.persistence.model.Nivel;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.SolicitudBecaNivel;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.util.UtilFile;
import java.util.Date;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Admin CAE
 */
public class AdministracionAccesoNivelAction extends BaseAction {
    
    private SolicitudBecaNivel nuevoAcceso = new SolicitudBecaNivel();
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
        
        Nivel n = getDaos().getNivelDao().findById(nuevoAcceso.getNivel().getId());
        nuevoAcceso.setNivel(n);
        
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
            getDaos().getSolicitudBecaNivelDao().save(nuevoAcceso);
        } else {
            getDaos().getSolicitudBecaNivelDao().update(nuevoAcceso);
        }
        addActionMessage("Guardado Exitoso!!!");
        return "formulario";
    }
    
    public String editar() {
        if (nuevoAcceso == null || nuevoAcceso.getId() == null) {
            addActionError("No ha especificado el alumno por editar");
            return "configuracion";
        }
        nuevoAcceso = getDaos().getSolicitudBecaNivelDao().findById(nuevoAcceso.getId());
        return SUCCESS;
    }
    
    public String eliminar() {
        if (nuevoAcceso == null || nuevoAcceso.getId() == null) {
            addActionError("No ha especificado el alumno por eliminar");
            return "configuracion";
        }
        nuevoAcceso = getDaos().getSolicitudBecaNivelDao().findById(nuevoAcceso.getId());
        nuevoAcceso.setEstatus(Boolean.FALSE);
        nuevoAcceso.setFechaModificacion(new Date());
        getDaos().getSolicitudBecaNivelDao().update(nuevoAcceso);
        addActionMessage("Eliminacion exitosa");
        return "configuracion";
    }

    public SolicitudBecaNivel getNuevoAcceso() {
        return nuevoAcceso;
    }

    public void setNuevoAcceso(SolicitudBecaNivel nuevoAcceso) {
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