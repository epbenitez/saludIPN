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
@Table(name = "ent_padron_prospera")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class PadronProspera implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(generator = "entPadronProsperaSeq")
    @SequenceGenerator(name = "entPadronProsperaSeq", sequenceName = "ent_padron_prospera_seq", allocationSize = 1)
    private BigDecimal id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Periodo periodo;
    @ManyToOne(fetch = FetchType.LAZY)
    private Alumno alumno;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fecha;

    private String curp;

    private String folio;

    private String nombres;

    private String apellidoPaterno;

    private String apellidoMaterno;

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

    public Alumno getAlumno() {
	return alumno;
    }

    public void setAlumno(Alumno alumno) {
	this.alumno = alumno;
    }

    public Date getFecha() {
	return fecha;
    }

    public void setFecha(Date fecha) {
	this.fecha = fecha;
    }

    public String getCurp() {
	return curp;
    }

    public void setCurp(String curp) {
	this.curp = curp;
    }

    public String getFolio() {
	return folio;
    }

    public void setFolio(String folio) {
	this.folio = folio;
    }

    public String getNombres() {
	return nombres;
    }

    public void setNombres(String nombres) {
	this.nombres = nombres;
    }

    public String getApellidoPaterno() {
	return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
	this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
	return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
	this.apellidoMaterno = apellidoMaterno;
    }

}
