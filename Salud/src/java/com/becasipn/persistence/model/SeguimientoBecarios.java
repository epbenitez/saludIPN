package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.eclipse.persistence.annotations.Cache;

/**
 *
 * Augusto I. Hernández Ruiz
 *
 * "Cualquier tecnología suficientemente avanzada es indistinguible de la magia"
 *
 */
@Entity
@Table(name = "ENT_SEGUIMIENTO_BECARIOS")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)

public class SeguimientoBecarios implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(generator = "SEQ_SEGUIMIENTO_BECA")
    @SequenceGenerator(name = "SEQ_SEGUIMIENTO_BECA", sequenceName = "SEQ_SEGUIMIENTO_BECA", allocationSize = 1)

    private BigDecimal id;
    @OneToOne
    private Alumno alumno;
    @ManyToOne(fetch = FetchType.LAZY)
    private Periodo periodo;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaModificacion;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    @Override
    public String toString() {
        return "SeguimientoBecarios{" + "id=" + id + ", alumno=" + alumno + ", periodo=" + periodo + ", fechaModificacion=" + fechaModificacion + '}';
    }

    public SeguimientoBecarios(BigDecimal id, Alumno alumno, Periodo periodo, Date fechaModificacion) {
        this.id = id;
        this.alumno = alumno;
        this.periodo = periodo;
        this.fechaModificacion = fechaModificacion;
    }

    public SeguimientoBecarios() {
    }

}
