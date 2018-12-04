package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Augusto I. Hernández Ruiz
 */
@Entity
@Table(name = "ent_notificaciones")
public class Notificaciones implements BaseEntity, Serializable, Comparable<Notificaciones> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "SEQ_NOTIFICACIONES")
    @SequenceGenerator(name = "SEQ_NOTIFICACIONES", sequenceName = "SEQ_NOTIFICACIONES", allocationSize = 1)

    private BigDecimal id;

    @Column(length = 2000)
    private String notificacion;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaInicio;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaFinal;
    
    private String titulo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario", nullable = false)
    private Usuario usuario;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tiponotificacion_id", nullable = false)
    private TipoNotificacion tipoNotificacion;    

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(String notificacion) {
        this.notificacion = notificacion;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public TipoNotificacion getTipoNotificacion() {
        return tipoNotificacion;
    }

    public void setTipoNotificacion(TipoNotificacion tipoNotificacion) {
        this.tipoNotificacion = tipoNotificacion;
    }

    @Override
    public int compareTo(Notificaciones o) {
        //ascending order
        return o.getId().compareTo(id);

    }

    @Override
    public String toString() {
        return "Notificaciones{" + "id=" + id + ", notificacion=" + notificacion + ", fechaInicio=" + fechaInicio + ", fechaFinal=" + fechaFinal + ", titulo=" + titulo + ", usuario=" + usuario + ", tipoNotificacion=" + tipoNotificacion + '}';
    }
}
