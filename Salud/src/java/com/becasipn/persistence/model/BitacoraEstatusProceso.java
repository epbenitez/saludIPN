package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.eclipse.persistence.annotations.Cache;

@Entity
@Table(name = "rmm_bitacora_estatus_proceso")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class BitacoraEstatusProceso implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(generator = "rmmBitacoraEstatusProcesoSeq")
    @SequenceGenerator(name = "rmmBitacoraEstatusProcesoSeq", sequenceName = "rmm_bitacora_estatus_proceso_i", allocationSize = 1)
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigDecimal id;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechamodificacion;

    @ManyToOne(fetch = FetchType.LAZY)
    private Proceso proceso;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProcesoEstatus procesoEstatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuario;

    private String becasAsociadas;

    public BitacoraEstatusProceso(Date fechamodificacion, Proceso proceso, ProcesoEstatus procesoEstatus, Usuario usuario) {
        this.fechamodificacion = fechamodificacion;
        this.proceso = proceso;
        this.procesoEstatus = procesoEstatus;
        this.usuario = usuario;
    }

    public String getBecasAsociadas() {
        return becasAsociadas;
    }

    public void setBecasAsociadas(String becasAsociadas) {
        this.becasAsociadas = becasAsociadas;
    }

    public BitacoraEstatusProceso() {
    }

    @Override
    public BigDecimal getId() {
        return id;
    }

    @Override
    public void setId(BigDecimal id) {
        this.id = id;
    }

    public Date getFechamodificacion() {
        return fechamodificacion;
    }

    public void setFechamodificacion(Date fechamodificacion) {
        this.fechamodificacion = fechamodificacion;
    }

    public Proceso getProceso() {
        return proceso;
    }

    public void setProceso(Proceso proceso) {
        this.proceso = proceso;
    }

    public ProcesoEstatus getProcesoEstatus() {
        return procesoEstatus;
    }

    public void setProcesoEstatus(ProcesoEstatus procesoEstatus) {
        this.procesoEstatus = procesoEstatus;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
