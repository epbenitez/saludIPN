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
 * @author Tania G. Sánchez
 */
@Entity
@Table(name = "rmm_deposito_unidad_academica")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class DepositoUnidadAcademica implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(generator = "rmmDepositoUnidadAcademicaSeq")
    @SequenceGenerator(name = "rmmDepositoUnidadAcademicaSeq", sequenceName = "rmm_deposito_unidad_academ_seq", allocationSize = 1)
    private BigDecimal id;
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoBeca tipoBeca;
    @ManyToOne(fetch = FetchType.LAZY)
    private UnidadAcademica unidadAcademica;
    private Boolean correspondeIPN;
    private Boolean tarjetaipn;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public TipoBeca getTipoBeca() {
        return tipoBeca;
    }

    public void setTipoBeca(TipoBeca tipoBeca) {
        this.tipoBeca = tipoBeca;
    }

    public UnidadAcademica getUnidadAcademica() {
        return unidadAcademica;
    }

    public void setUnidadAcademica(UnidadAcademica unidadAcademica) {
        this.unidadAcademica = unidadAcademica;
    }

    public Boolean getCorrespondeIPN() {
        return correspondeIPN;
    }

    public void setCorrespondeIPN(Boolean correspondeIPN) {
        this.correspondeIPN = correspondeIPN;
    }

    public Boolean getTarjetaipn() {
        return tarjetaipn;
    }

    public void setTarjetaipn(Boolean tarjetaipn) {
        this.tarjetaipn = tarjetaipn;
    }

    @Override
    public String toString() {
        return "DepositoUnidadAcademica{" + "id=" + id + ", tipoBeca=" + tipoBeca + ", unidadAcademica=" + unidadAcademica + ", correspondeIPN=" + correspondeIPN + '}';
    }
}
