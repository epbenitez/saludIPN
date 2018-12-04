package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author César Hernández Tavera
 */
@Entity
@Table(name = "ENT_SOLICITUD_BECAS_ACCESOIND")
public class SolicitudBecaIndividual implements Serializable, BaseEntity {
    
    @Id
    @GeneratedValue(generator = "solicitudIndividualSeq")
    @SequenceGenerator(name = "solicitudIndividualSeq", sequenceName = "SEQ_ENT_SOL_BEC_ACC_IND", allocationSize = 1)
    private BigDecimal id;
    @ManyToOne
    private Periodo periodo;
    @ManyToOne
    private Alumno alumno;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @OneToOne
    private Usuario usuario;
    private Boolean estatus;

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

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }
    
    public Date getFechaModificacion() {
        return fechaModificacion;
    }
    
    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Boolean getEstatus() {
        return estatus;
    }

    public void setEstatus(Boolean estatus) {
        this.estatus = estatus;
    }
    
    @Override
    public String toString() {
        return "SolicitudBecaIndividual{" + "id=" + id + ", periodo=" + periodo + ", alumno=" + alumno + ", fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin + '}';
    } 
}
