package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.CascadeType;
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
 * @author Augusto I. Hernández Ruiz
 */
@Entity
@Table(name = "ENT_SOLICITUD_RECONSIDERACION")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class SolicitudReconsideracion implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(generator = "SolRecoSeq")
    @SequenceGenerator(name = "SolRecoSeq", sequenceName = "SEQ_SOLICITUD_RECONSIDERA", allocationSize = 1)
    private BigDecimal id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Alumno alumno;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaSolicitud;
    @ManyToOne(fetch = FetchType.LAZY)
    private SolicitudBeca solicitudBeca;
    @ManyToOne(fetch = FetchType.LAZY)
    private EstatusReconsideracion estatus;
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoInconformidadReconsideracion tipoInconformidad;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Usuario usuario;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaAtencion;
    @ManyToOne(fetch = FetchType.LAZY)
    private Periodo periodo;
    private String solicitud;

    public SolicitudReconsideracion() {
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

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public SolicitudBeca getSolicitudBeca() {
        return solicitudBeca;
    }

    public void setSolicitudBeca(SolicitudBeca solicitudBeca) {
        this.solicitudBeca = solicitudBeca;
    }

    public EstatusReconsideracion getEstatus() {
        return estatus;
    }

    public void setEstatus(EstatusReconsideracion estatus) {
        this.estatus = estatus;
    }

    public TipoInconformidadReconsideracion getTipoInconformidad() {
        return tipoInconformidad;
    }

    public void setTipoInconformidad(TipoInconformidadReconsideracion tipoInconformidad) {
        this.tipoInconformidad = tipoInconformidad;
    }


    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getFechaAtencion() {
        return fechaAtencion;
    }

    public void setFechaAtencion(Date fechaAtencion) {
        this.fechaAtencion = fechaAtencion;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public String getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(String solicitud) {
        this.solicitud = solicitud;
    }

    @Override
    public String toString() {
        return "SolicitudReconsideracion{" + "id=" + id + ", alumno=" + alumno + ", fechaSolicitud=" + fechaSolicitud + ", solicitudBeca=" + solicitudBeca + ", estatus=" + estatus + ", tipoInconformidad=" + tipoInconformidad + ", usuario=" + usuario + ", fechaAtencion=" + fechaAtencion + ", periodo=" + periodo + ", solicitud=" + solicitud + '}';
    }



}
