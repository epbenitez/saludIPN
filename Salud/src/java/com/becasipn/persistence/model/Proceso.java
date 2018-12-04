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

/**
 *
 * @author Victor Lozano
 */
@Entity
@Table(name = "ent_proceso")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class Proceso implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(generator = "entProcesoSeq")
    @SequenceGenerator(name = "entProcesoSeq", sequenceName = "ent_proceso_id_seq", allocationSize = 1)
    private BigDecimal id;
    @ManyToOne(fetch = FetchType.LAZY)
    private UnidadAcademica unidadAcademica;
    @ManyToOne(fetch = FetchType.LAZY)
    private Periodo periodo;
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoProceso tipoProceso;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaInicial;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaFinal;
    @ManyToOne(fetch = FetchType.LAZY)
    private ProcesoEstatus procesoEstatus;

    @Override
    public BigDecimal getId() {
	return id;
    }

    @Override
    public void setId(BigDecimal id) {
	this.id = id;
    }

    public UnidadAcademica getUnidadAcademica() {
	return unidadAcademica;
    }

    public void setUnidadAcademica(UnidadAcademica unidadAcademica) {
	this.unidadAcademica = unidadAcademica;
    }

    public Periodo getPeriodo() {
	return periodo;
    }

    public void setPeriodo(Periodo periodo) {
	this.periodo = periodo;
    }

    public TipoProceso getTipoProceso() {
	return tipoProceso;
    }

    public void setTipoProceso(TipoProceso tipoProceso) {
	this.tipoProceso = tipoProceso;
    }

    public Date getFechaInicial() {
	return fechaInicial;
    }

    public void setFechaInicial(Date fechaInicial) {
	this.fechaInicial = fechaInicial;
    }

    public Date getFechaFinal() {
	return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
	this.fechaFinal = fechaFinal;
    }

    public ProcesoEstatus getProcesoEstatus() {
	return procesoEstatus;
    }

    public void setProcesoEstatus(ProcesoEstatus procesoEstatus) {
	this.procesoEstatus = procesoEstatus;
    }

}
