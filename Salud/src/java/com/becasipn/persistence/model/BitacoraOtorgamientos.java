package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.eclipse.persistence.annotations.Cache;

/**
 *
 * @author Tania G. Sánchez Manilla
 */
@Entity
@Table(name = "rmm_bitacora_otorgamientos")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class BitacoraOtorgamientos implements Serializable, BaseEntity {

    @Id
    private BigDecimal id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Otorgamiento otorgamiento;
    private Boolean excluirDeposito;
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuarioModifico;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaModificacion;
    @ManyToOne(fetch = FetchType.LAZY)
    private Proceso proceso;
    private Boolean alta;
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoBecaPeriodo tipoBecaPeriodo;
    @ManyToOne(fetch = FetchType.LAZY)
    private Periodo periodo;
    @ManyToOne(fetch = FetchType.LAZY)
    private IdentificadorOtorgamiento identificadorOtorgamiento;

    public BitacoraOtorgamientos() {
    }

    public BitacoraOtorgamientos(Otorgamiento otorgamiento, Boolean excluirDeposito, Usuario usuarioModifico, Date fechaModificacion, Proceso proceso, Boolean alta, TipoBecaPeriodo tipoBecaPeriodo, Periodo periodo) {
        this.otorgamiento = otorgamiento;
        this.excluirDeposito = excluirDeposito;
        this.usuarioModifico = usuarioModifico;
        this.fechaModificacion = fechaModificacion;
        this.proceso = proceso;
        this.alta = alta;
        this.tipoBecaPeriodo = tipoBecaPeriodo;
        this.periodo = periodo;
    }

    @Override
    public BigDecimal getId() {
        return id;
    }

    @Override
    public void setId(BigDecimal id) {
        this.id = id;
    }

    public Otorgamiento getOtorgamiento() {
        return otorgamiento;
    }

    public void setOtorgamiento(Otorgamiento otorgamiento) {
        this.otorgamiento = otorgamiento;
    }

    public Boolean getExcluirDeposito() {
        return excluirDeposito;
    }

    public void setExcluirDeposito(Boolean excluirDeposito) {
        this.excluirDeposito = excluirDeposito;
    }

    public Usuario getUsuarioModifico() {
        return usuarioModifico;
    }

    public void setUsuarioModifico(Usuario usuarioModifico) {
        this.usuarioModifico = usuarioModifico;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Proceso getProceso() {
        return proceso;
    }

    public void setProceso(Proceso proceso) {
        this.proceso = proceso;
    }

    public Boolean getAlta() {
        return alta;
    }

    public void setAlta(Boolean alta) {
        this.alta = alta;
    }

    public TipoBecaPeriodo getTipoBecaPeriodo() {
        return tipoBecaPeriodo;
    }

    public void setTipoBecaPeriodo(TipoBecaPeriodo tipoBecaPeriodo) {
        this.tipoBecaPeriodo = tipoBecaPeriodo;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    @Override
    public String toString() {
        return "BitacoraOtorgamientos{" + "id=" + id + ", otorgamiento=" + otorgamiento + ", excluirDeposito=" + excluirDeposito + ", usuarioModifico=" + usuarioModifico + ", fechaModificacion=" + fechaModificacion + ", proceso=" + proceso + ", alta=" + alta + ", tipoBecaPeriodo=" + tipoBecaPeriodo + ", periodo=" + periodo + ", identificadorOtorgamiento" + identificadorOtorgamiento + '}';
    }

    public IdentificadorOtorgamiento getIdentificadorOtorgamiento() {
        return identificadorOtorgamiento;
    }

    public void setIdentificadorOtorgamiento(IdentificadorOtorgamiento identificadorOtorgamiento) {
        this.identificadorOtorgamiento = identificadorOtorgamiento;
    }

    
}
