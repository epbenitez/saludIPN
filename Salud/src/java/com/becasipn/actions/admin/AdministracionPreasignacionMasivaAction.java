package com.becasipn.actions.admin;

import com.becasipn.actions.BaseAction;
import com.becasipn.business.OtorgamientoBO;
import com.becasipn.domain.PreasignacionResumen;
import com.becasipn.persistence.model.Nivel;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.util.Tupla;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Othoniel Herrera
 */
public class AdministracionPreasignacionMasivaAction extends BaseAction {

    private BigDecimal totalAlumnosSolicitudPendienteActivo;
    private BigDecimal totalAlumnosSolicitudEsperaActivo;
    private BigDecimal totalAlumnosSolicitudEsperaPasado;
    private BigDecimal totalAlumnosSolicitud;
    private BigDecimal nivel;
    private BigDecimal unidadAcademica;
    private boolean sobreEscribir;
    private List<Nivel> niveles;
    private List<PreasignacionResumen> resumen = new ArrayList<>();
    private long solicitudesPreasignadas;
    private long solicitudesNoPreasignadas;
    private long solicitudesProcesadas;
    private String nombreExcel;
    private InputStream excelStream;
    private String contentDisposition;
    
    public String form() {
        niveles = getDaos().getNivelDao().nivelesActivos();
        Periodo periodoActivo = getDaos().getPeriodoDao().getPeriodoActivo();
        String clave = periodoActivo.getClave();
        sobreEscribir = true;
        totalAlumnosSolicitudPendienteActivo = getDaos().getSolicitudBecaDao().totalAlumnosSolicitudPeriodoEstatusNivelUA(periodoActivo.getId(), null, null, null, null, sobreEscribir);
        totalAlumnosSolicitudEsperaActivo = getDaos().getSolicitudBecaDao().totalAlumnosSolicitudPeriodoEstatusNivelUA(periodoActivo.getId(), null, new BigDecimal(3), null, null, sobreEscribir);
        if (clave.charAt(clave.length() - 1) == '1') {       
            totalAlumnosSolicitud = totalAlumnosSolicitudPendienteActivo.add(totalAlumnosSolicitudEsperaActivo);
        } else {
            totalAlumnosSolicitudEsperaPasado = getDaos().getSolicitudBecaDao().totalAlumnosSolicitudPeriodoEstatusNivelUA(periodoActivo.getId(), periodoActivo.getPeriodoAnterior().getId(), new BigDecimal(3), null, null, sobreEscribir);
            totalAlumnosSolicitud = totalAlumnosSolicitudPendienteActivo.add(totalAlumnosSolicitudEsperaPasado).add(totalAlumnosSolicitudEsperaActivo);
        }
        
        return SUCCESS;
    }

    public String generar() {
        OtorgamientoBO oBo = new OtorgamientoBO(getDaos());
        Tupla<List<PreasignacionResumen>, String> tuplaResultado = oBo.preasignacionesMasivas(nivel, unidadAcademica, sobreEscribir);
        nombreExcel = tuplaResultado.getK2();
        resumen = tuplaResultado.getK1();
        solicitudesPreasignadas = 0;
        solicitudesNoPreasignadas = 0;
        for (PreasignacionResumen resumenUA : resumen) {
            solicitudesPreasignadas += resumenUA.getSolicitudesPreasignadasUA();
            solicitudesNoPreasignadas += resumenUA.getSolicitudesNoPreasignadasUA();
        }

        solicitudesProcesadas = solicitudesPreasignadas + solicitudesNoPreasignadas;
        return SUCCESS;
    }
    
    public String descargar() {
        
        setContentDisposition("attachment; filename=\"" + nombreExcel.substring(12) + "\"");  
        
        try {
            excelStream = new FileInputStream(new File(nombreExcel));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AdministracionPreasignacionMasivaAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "archivo";
    }

    public BigDecimal getTotalAlumnosSolicitud() {
        return totalAlumnosSolicitud;
    }

    public void setTotalAlumnosSolicitud(BigDecimal totalAlumnosSolicitud) {
        this.totalAlumnosSolicitud = totalAlumnosSolicitud;
    }

    public BigDecimal getTotalAlumnosSolicitudEsperaPasado() {
        return totalAlumnosSolicitudEsperaPasado;
    }

    public void setTotalAlumnosSolicitudEsperaPasado(BigDecimal totalAlumnosSolicitudEsperaPasado) {
        this.totalAlumnosSolicitudEsperaPasado = totalAlumnosSolicitudEsperaPasado;
    }

    public List<Nivel> getNiveles() {
        return niveles;
    }

    public void setNiveles(List<Nivel> niveles) {
        this.niveles = niveles;
    }

    public long getSolicitudesPreasignadas() {
        return solicitudesPreasignadas;
    }

    public void setSolicitudesPreasignadas(long solicitudesPreasignadas) {
        this.solicitudesPreasignadas = solicitudesPreasignadas;
    }

    public long getSolicitudesProcesadas() {
        return solicitudesProcesadas;
    }

    public void setSolicitudesProcesadas(long solicitudesProcesadas) {
        this.solicitudesProcesadas = solicitudesProcesadas;
    }

    public long getSolicitudesNoPreasignadas() {
        return solicitudesNoPreasignadas;
    }

    public void setSolicitudesNoPreasignadas(long solicitudesNoPreasignadas) {
        this.solicitudesNoPreasignadas = solicitudesNoPreasignadas;
    }

    public BigDecimal getTotalAlumnosSolicitudPendienteActivo() {
        return totalAlumnosSolicitudPendienteActivo;
    }

    public void setTotalAlumnosSolicitudPendienteActivo(BigDecimal totalAlumnosSolicitudPendienteActivo) {
        this.totalAlumnosSolicitudPendienteActivo = totalAlumnosSolicitudPendienteActivo;
    }

    public BigDecimal getNivel() {
        return nivel;
    }

    public void setNivel(BigDecimal nivel) {
        this.nivel = nivel;
    }

    public BigDecimal getUnidadAcademica() {
        return unidadAcademica;
    }

    public void setUnidadAcademica(BigDecimal unidadAcademica) {
        this.unidadAcademica = unidadAcademica;
    }

    public List<PreasignacionResumen> getResumen() {
        return resumen;
    }

    public void setResumen(List<PreasignacionResumen> resumen) {
        this.resumen = resumen;
    }

    public BigDecimal getTotalAlumnosSolicitudEsperaActivo() {
        return totalAlumnosSolicitudEsperaActivo;
    }

    public void setTotalAlumnosSolicitudEsperaActivo(BigDecimal totalAlumnosSolicitudEsperaActivo) {
        this.totalAlumnosSolicitudEsperaActivo = totalAlumnosSolicitudEsperaActivo;
    }

    public String getNombreExcel() {
        return nombreExcel;
    }

    public void setNombreExcel(String nombreExcel) {
        this.nombreExcel = nombreExcel;
    }

    public InputStream getExcelStream() {
        return excelStream;
    }

    public void setExcelStream(InputStream excelStream) {
        this.excelStream = excelStream;
    }

    public String getContentDisposition() {
        return contentDisposition;
    }

    public void setContentDisposition(String contentDisposition) {
        this.contentDisposition = contentDisposition;
    }

    public boolean isSobreEscribir() {
        return sobreEscribir;
    }

    public void setSobreEscribir(boolean sobreEscribir) {
        this.sobreEscribir = sobreEscribir;
    } 
}
