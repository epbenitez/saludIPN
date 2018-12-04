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

@Entity
@Table(name = "ent_lista_espera")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class ListaEspera implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(generator = "ListaEsperaSeq")
    @SequenceGenerator(name = "ListaEsperaSeq", sequenceName = "ent_lista_espera_seq", allocationSize = 1)
    private BigDecimal id;
    @ManyToOne(fetch = FetchType.LAZY)
    private SolicitudBeca solicitudBeca;
    @ManyToOne(fetch = FetchType.LAZY)
    private Alumno alumno;
    @ManyToOne(fetch = FetchType.LAZY)
    private Proceso proceso;

    private Integer ordenamiento;
    private Integer vigente;

    public ListaEspera(SolicitudBeca solicitudBeca, Alumno alumno, Proceso proceso, Integer ordenamiento, Integer vigente) {
        this.solicitudBeca = solicitudBeca;
        this.alumno = alumno;
        this.proceso = proceso;
        this.ordenamiento = ordenamiento;
        this.vigente = vigente;
    }

    public ListaEspera() {
    }

    @Override
    public BigDecimal getId() {
	return id;
    }

    @Override
    public void setId(BigDecimal id) {
	this.id = id;
    }

    public Alumno getAlumno() {
	return alumno;
    }

    public void setAlumno(Alumno alumno) {
	this.alumno = alumno;
    }

    public SolicitudBeca getSolicitudBeca() {
	return solicitudBeca;
    }

    public void setSolicitudBeca(SolicitudBeca solicitudBeca) {
	this.solicitudBeca = solicitudBeca;
    }

    public Proceso getProceso() {
	return proceso;
    }

    public void setProceso(Proceso proceso) {
	this.proceso = proceso;
    }

    public Integer getOrdenamiento() {
	return ordenamiento;
    }

    public void setOrdenamiento(Integer ordenamiento) {
	this.ordenamiento = ordenamiento;
    }

    public Integer getVigente() {
	return vigente;
    }

    public void setVigente(Integer vigente) {
	this.vigente = vigente;
    }
}
