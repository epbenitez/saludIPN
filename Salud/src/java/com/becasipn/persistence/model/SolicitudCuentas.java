package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.eclipse.persistence.annotations.Cache;

@Entity
@Table(name = "ent_solicitud_cuentas")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class SolicitudCuentas implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(generator = "solicitudSeq")
    @SequenceGenerator(name = "solicitudSeq", sequenceName = "ENT_SOLICITUD_CUENTAS_I", allocationSize = 1)
    private BigDecimal id;
    private String identificador;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaGeneracion;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaFinalizacion;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaCancelacion;

    @ManyToOne
    private Usuario usuarioGeneracion;

    @ManyToOne
    private Usuario usuarioCancelacion;

    @ManyToOne
    private Usuario usuarioFinalizacion;

    @ManyToOne
    private Periodo periodoGeneracion;

    public SolicitudCuentas() {
    }

    @Override
    public BigDecimal getId() {
	return id;
    }

    @Override
    public void setId(BigDecimal id) {
	this.id = id;
    }

    public String getIdentificador() {
	return identificador;
    }

    public void setIdentificador(String identificador) {
	this.identificador = identificador;
    }

    public Date getFechaGeneracion() {
	return fechaGeneracion;
    }

    public void setFechaGeneracion(Date fechaGeneracion) {
	this.fechaGeneracion = fechaGeneracion;
    }

    public Date getFechaFinalizacion() {
	return fechaFinalizacion;
    }

    public void setFechaFinalizacion(Date fechaFinalizacion) {
	this.fechaFinalizacion = fechaFinalizacion;
    }

    public Date getFechaCancelacion() {
	return fechaCancelacion;
    }

    public void setFechaCancelacion(Date fechaCancelacion) {
	this.fechaCancelacion = fechaCancelacion;
    }

    public Usuario getUsuarioGeneracion() {
	return usuarioGeneracion;
    }

    public void setUsuarioGeneracion(Usuario usuarioGeneracion) {
	this.usuarioGeneracion = usuarioGeneracion;
    }

    public Usuario getUsuarioCancelacion() {
	return usuarioCancelacion;
    }

    public void setUsuarioCancelacion(Usuario usuarioCancelacion) {
	this.usuarioCancelacion = usuarioCancelacion;
    }

    public Usuario getUsuarioFinalizacion() {
	return usuarioFinalizacion;
    }

    public void setUsuarioFinalizacion(Usuario usuarioFinalizacion) {
	this.usuarioFinalizacion = usuarioFinalizacion;
    }

    public Periodo getPeriodoGeneracion() {
	return periodoGeneracion;
    }

    public void setPeriodoGeneracion(Periodo periodoGeneracion) {
	this.periodoGeneracion = periodoGeneracion;
    }

}
