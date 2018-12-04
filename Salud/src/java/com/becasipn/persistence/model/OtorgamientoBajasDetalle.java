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
@Table(name = "ent_otorgamientos_bajas_detall")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class OtorgamientoBajasDetalle implements Serializable, BaseEntity, BaseBitacora {

    @Id
    @GeneratedValue(generator = "obdSeq")
    @SequenceGenerator(name = "obdSeq", sequenceName = "ENT_OTORGAMIENTOS_BAJAS_DETA_1", allocationSize = 1)
    private BigDecimal id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Otorgamiento otorgamiento;
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoBajasDetalle tipoBajasDetalle;
    @ManyToOne//(fetch = FetchType.LAZY)
    private Periodo periodo;
    @ManyToOne(fetch = FetchType.LAZY)
    private Movimiento movimiento;
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuariomodifico;
    @ManyToOne(fetch = FetchType.LAZY)
    private Proceso proceso;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaBaja;

    private String observaciones;
    private Double promedio;
    private Integer semestre;

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

    public TipoBajasDetalle getTipoBajasDetalle() {
        return tipoBajasDetalle;
    }

    public void setTipoBajasDetalle(TipoBajasDetalle tipoBajasDetalle) {
        this.tipoBajasDetalle = tipoBajasDetalle;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public Date getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Movimiento getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(Movimiento movimiento) {
        this.movimiento = movimiento;
    }

    public Usuario getUsuariomodifico() {
        return usuariomodifico;
    }

    public void setUsuariomodifico(Usuario usuariomodifico) {
        this.usuariomodifico = usuariomodifico;
    }

    public Proceso getProceso() {
        return proceso;
    }

    public void setProceso(Proceso proceso) {
        this.proceso = proceso;
    }

    public Double getPromedio() {
        return promedio;
    }

    public void setPromedio(Double promedio) {
        this.promedio = promedio;
    }

    public Integer getSemestre() {
        return semestre;
    }

    public void setSemestre(Integer semestre) {
        this.semestre = semestre;
    }
}
