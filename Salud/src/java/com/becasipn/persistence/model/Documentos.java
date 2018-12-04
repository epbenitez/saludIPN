package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.Cache;

/**
 *
 * @author Victor Lozano
 */
@Entity
@Table(name = "rmm_documentos_alumno")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class Documentos implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(generator = "entDocumentosSeq")
    @SequenceGenerator(name = "entDocumentosSeq", sequenceName = "RMM_DOCUMENTOS_ALUMNO_ID_SEQ", allocationSize = 1)
    private BigDecimal id;
    private boolean estudioSocioeconomico;
    private boolean cartaCompromiso;
    private boolean curp;
    private boolean comprobanteIngresosEgresos;
    private boolean acuseSubes;
    private boolean folioSubes;
    private boolean acuseSubesTransporte;
    private boolean folioSubesTransporte;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PERIODO_ID")
    private Periodo periodo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ALUMNO_ID")
    private Alumno alumno;
    private boolean estudiosocieconomicotransporte;

    public Documentos() {
    }

    public Documentos(Periodo periodo, Alumno alumno) {
        this.periodo = periodo;
        this.alumno = alumno;
    }
    
    @Override
    public BigDecimal getId() {
        return id;
    }

    @Override
    public void setId(BigDecimal id) {
        this.id = id;
    }

    public boolean getEstudioSocioeconomico() {
        return estudioSocioeconomico;
    }

    public void setEstudioSocioeconomico(boolean estudioSocioeconomico) {
        this.estudioSocioeconomico = estudioSocioeconomico;
    }

    public boolean getCartaCompromiso() {
        return cartaCompromiso;
    }

    public void setCartaCompromiso(boolean cartaCompromiso) {
        this.cartaCompromiso = cartaCompromiso;
    }

    public boolean getCurp() {
        return curp;
    }

    public void setCurp(boolean curp) {
        this.curp = curp;
    }

    public boolean getComprobanteIngresosEgresos() {
        return comprobanteIngresosEgresos;
    }

    public void setComprobanteIngresosEgresos(boolean comprobanteIngresosEgresos) {
        this.comprobanteIngresosEgresos = comprobanteIngresosEgresos;
    }

    public boolean getAcuseSubes() {
        return acuseSubes;
    }

    public void setAcuseSubes(boolean acuseSubes) {
        this.acuseSubes = acuseSubes;
    }

    public boolean getFolioSubes() {
        return folioSubes;
    }

    public void setFolioSubes(boolean folioSubes) {
        this.folioSubes = folioSubes;
    }

    public boolean getAcuseSubesTransporte() {
        return acuseSubesTransporte;
    }

    public void setAcuseSubesTransporte(boolean acuseSubesTransporte) {
        this.acuseSubesTransporte = acuseSubesTransporte;
    }

    public boolean getFolioSubesTransporte() {
        return folioSubesTransporte;
    }

    public void setFolioSubesTransporte(boolean folioSubesTransporte) {
        this.folioSubesTransporte = folioSubesTransporte;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public boolean isEstudiosocieconomicotransporte() {
        return estudiosocieconomicotransporte;
    }

    public void setEstudiosocieconomicotransporte(boolean estudiosocieconomicotransporte) {
        this.estudiosocieconomicotransporte = estudiosocieconomicotransporte;
    }
}