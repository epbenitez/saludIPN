package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

/**
 * @author Gustavo Adolfo Alamillo Medina
 * @version 1.0
 */
@Entity
@Table(name = "rmm_bitacora_tarjeta_bancaria")
@Cacheable(false)
public class BitacoraTarjetaBancaria implements Serializable, BaseEntity {

    @Id
    private BigDecimal id;
    @JoinColumn(name = "tarjetabancariaestatus_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private EstatusTarjetaBancaria estatus;
    @JoinColumn(name = "tarjetabancaria_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private TarjetaBancaria tarjetaBancaria;
    @JoinColumn(name = "usuariomodifico_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuario;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    private String numReporteBanco;
    private String observaciones;
    private Boolean envioCorreo;
    @Transient
    private String boleta;
    @Transient
    private String nombre;

    public BitacoraTarjetaBancaria() {
    }

    public BitacoraTarjetaBancaria(EstatusTarjetaBancaria estatus, TarjetaBancaria tarjetaBancaria, Usuario usuario, Date fechaModificacion) {
        this.estatus = estatus;
        this.tarjetaBancaria = tarjetaBancaria;
        this.usuario = usuario;
        this.fechaModificacion = fechaModificacion;
    }

    @Override
    public BigDecimal getId() {
        return id;
    }

    @Override
    public void setId(BigDecimal id) {
        this.id = id;
    }

    public EstatusTarjetaBancaria getEstatus() {
        return estatus;
    }

    public void setEstatus(EstatusTarjetaBancaria estatus) {
        this.estatus = estatus;
    }

    public TarjetaBancaria getTarjetaBancaria() {
        return tarjetaBancaria;
    }

    public void setTarjetaBancaria(TarjetaBancaria tarjetaBancaria) {
        this.tarjetaBancaria = tarjetaBancaria;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getNumReporteBanco() {
        return numReporteBanco;
    }

    public void setNumReporteBanco(String numReporteBanco) {
        this.numReporteBanco = numReporteBanco;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Boolean getEnvioCorreo() {
        return envioCorreo;
    }

    public void setEnvioCorreo(Boolean envioCorreo) {
        this.envioCorreo = envioCorreo;
    }

    @Override
    public String toString() {
        return id + "\t" + tarjetaBancaria.getNumtarjetabancaria() + "\t"
                + usuario.getUsuario() + "\t" + fechaModificacion;
    }

    public String getBoleta() {
        return boleta;
    }

    public void setBoleta(String boleta) {
        this.boleta = boleta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}