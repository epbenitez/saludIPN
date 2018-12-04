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
 *
 * @author Patricia Benítez
 */
@Entity
@Table(name = "vw_presupuesto_tipobecaperiodo")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class VWPresupuestoTipoBecaPeriodo implements Serializable, BaseEntity {

    @Id
    private BigDecimal id;
    @ManyToOne(fetch = FetchType.LAZY)
    private VWPresupuestoPeriodo presupuestoPeriodo;
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoBecaPeriodo tipoBecaPeriodo;
    private BigDecimal montoPresupuestado;
    private BigDecimal montoAsignado;
    private BigDecimal montoEjercido;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public VWPresupuestoPeriodo getPresupuestoPeriodo() {
        return presupuestoPeriodo;
    }

    public void setPresupuestoPeriodo(VWPresupuestoPeriodo presupuestoPeriodo) {
        this.presupuestoPeriodo = presupuestoPeriodo;
    }

    public TipoBecaPeriodo getTipoBecaPeriodo() {
        return tipoBecaPeriodo;
    }

    public void setTipoBecaPeriodo(TipoBecaPeriodo tipoBecaPeriodo) {
        this.tipoBecaPeriodo = tipoBecaPeriodo;
    }

    public BigDecimal getMontoPresupuestado() {
        return montoPresupuestado;
    }

    public void setMontoPresupuestado(BigDecimal montoPresupuestado) {
        this.montoPresupuestado = montoPresupuestado;
    }

    public BigDecimal getMontoAsignado() {
        return montoAsignado;
    }

    public void setMontoAsignado(BigDecimal montoAsignado) {
        this.montoAsignado = montoAsignado;
    }

    public BigDecimal getMontoEjercido() {
        return montoEjercido;
    }

    public void setMontoEjercido(BigDecimal montoEjercido) {
        this.montoEjercido = montoEjercido;
    }

}
