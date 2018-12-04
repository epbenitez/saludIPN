package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.FetchType;

import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.Cache;

/**
 * @author Gustavo A. Alamillo
 */

@Entity
@Table(name = "vw_presupuesto_unidad_pre")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class VWPresupuestoUnidadAcademicaPre implements Serializable, BaseEntity {

    @Id
    private BigDecimal id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Periodo periodo;
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoBecaPeriodo tipoBecaPeriodo;
    @ManyToOne(fetch = FetchType.LAZY)
    private PresupuestoTipoBecaPeriodo presupuestoTipoBecaPeriodo;
    @ManyToOne(fetch = FetchType.LAZY)
    private UnidadAcademica unidadAcademica;
    private BigDecimal montoPresupuestado;
    private BigDecimal montoEjercido;
    private BigDecimal montoPorBeca;
    private Integer becasDisponibles;

    @Override
    public BigDecimal getId() {
        return id;
    }

    @Override
    public void setId(BigDecimal id) {
        this.id = id;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public TipoBecaPeriodo getTipoBecaPeriodo() {
        return tipoBecaPeriodo;
    }

    public void setTipoBecaPeriodo(TipoBecaPeriodo tipoBecaPeriodo) {
        this.tipoBecaPeriodo = tipoBecaPeriodo;
    }

    public PresupuestoTipoBecaPeriodo getPresupuestoTipoBecaPeriodo() {
        return presupuestoTipoBecaPeriodo;
    }

    public void setPresupuestoTipoBecaPeriodo(PresupuestoTipoBecaPeriodo presupuestoTipoBecaPeriodo) {
        this.presupuestoTipoBecaPeriodo = presupuestoTipoBecaPeriodo;
    }

    public UnidadAcademica getUnidadAcademica() {
        return unidadAcademica;
    }

    public void setUnidadAcademica(UnidadAcademica unidadAcademica) {
        this.unidadAcademica = unidadAcademica;
    }

    public BigDecimal getMontoPresupuestado() {
        return montoPresupuestado;
    }

    public void setMontoPresupuestado(BigDecimal montoPresupuestado) {
        this.montoPresupuestado = montoPresupuestado;
    }

    public BigDecimal getMontoEjercido() {
        return montoEjercido;
    }

    public void setMontoEjercido(BigDecimal montoEjercido) {
        this.montoEjercido = montoEjercido;
    }

    public BigDecimal getMontoPorBeca() {
        return montoPorBeca;
    }

    public void setMontoPorBeca(BigDecimal montoPorBeca) {
        this.montoPorBeca = montoPorBeca;
    }

    public Integer getBecasDisponibles() {
        return becasDisponibles;
    }

    public void setBecasDisponibles(Integer becasDisponibles) {
        this.becasDisponibles = becasDisponibles;
    }

    @Override
    public String toString() {
        return "VWPresupuestoUnidadAcademica-Pre{" + "id=" + id + ", periodo=" + periodo + ", tipoBecaPeriodo=" + tipoBecaPeriodo + ", presupuestoTipoBecaPeriodo=" + presupuestoTipoBecaPeriodo + ", unidadAcademica=" + unidadAcademica + ", montoPresupuestado=" + montoPresupuestado + ", montoEjercido=" + montoEjercido + ", montoPorBeca=" + montoPorBeca + ", becasDisponibles=" + becasDisponibles + '}';
    }
}
