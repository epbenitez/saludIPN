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
@Table(name = "vw_presupuesto_periodo")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class VWPresupuestoPeriodo implements Serializable, BaseEntity {

    @Id
    private BigDecimal id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Periodo periodo;
    private BigDecimal montoPresupuestado;
    private BigDecimal montoAsignado;
    private BigDecimal montoEjercido;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
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
