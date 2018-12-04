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
 * @author User-02
 */
@Entity
@Table(name = "ent_presupuesto_periodo")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class Presupuesto implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(generator = "entPresupuestoPeriodoSeq")
    @SequenceGenerator(name = "entPresupuestoPeriodoSeq", sequenceName = "ent_presupuesto_periodo_id_seq", allocationSize = 1)
    private BigDecimal id;
    private Long monto;
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoProceso proceso;
    @ManyToOne(fetch = FetchType.LAZY)
    private UnidadAcademica unidadAcademica;
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoBeca tipoBeca;
    @ManyToOne(fetch = FetchType.LAZY)
    private Periodo periodo;

    @Override
    public BigDecimal getId() {
	return id;
    }

    @Override
    public void setId(BigDecimal id) {
	this.id = id;
    }

    public Long getMonto() {
	return monto;
    }

    public void setMonto(Long monto) {
	this.monto = monto;
    }

    public TipoProceso getProceso() {
	return proceso;
    }

    public void setProceso(TipoProceso proceso) {
	this.proceso = proceso;
    }

    public UnidadAcademica getUnidadAcademica() {
	return unidadAcademica;
    }

    public void setUnidadAcademica(UnidadAcademica unidadAcademica) {
	this.unidadAcademica = unidadAcademica;
    }

    public TipoBeca getTipoBeca() {
	return tipoBeca;
    }

    public void setTipoBeca(TipoBeca tipoBeca) {
	this.tipoBeca = tipoBeca;
    }

    public Periodo getPeriodo() {
	return periodo;
    }

    public void setPeriodo(Periodo periodo) {
	this.periodo = periodo;
    }

}
