package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.Cache;

/**
 *
 * @author Tania G. Sánchez Manilla
 */

@Entity
@Table(name = "cat_monto_maximo_ingresos")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class MontoMaximoIngresos implements Serializable, BaseEntity {
    @Id
    @GeneratedValue(generator = "montoMaximoIngresoSeq")
    @SequenceGenerator(name = "montoMaximoIngresoSeq", sequenceName = "cat_monto_max_ing_seq", allocationSize = 1)
    private BigDecimal id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Periodo periodo;
    private Float monto;
    private Boolean deciles;

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

    public Float getMonto() {
        return monto;
    }

    public void setMonto(Float monto) {
        this.monto = monto;
    }

    public Boolean getDeciles() {
        return deciles;
    }

    public void setDeciles(Boolean deciles) {
        this.deciles = deciles;
    }

    @Override
    public String toString() {
        return "MontoMaximoIngresos{" + "id=" + id + ", periodo=" + periodo + ", monto=" + monto + ", deciles=" + deciles + '}';
    }
}