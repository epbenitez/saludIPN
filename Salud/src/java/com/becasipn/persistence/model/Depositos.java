package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.Cache;
import java.util.Date;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.SequenceGenerator;

import javax.persistence.Temporal;

/**
 *
 * @author Patricia Benítez
 */
@Entity
@Table(name = "ent_deposito")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class Depositos implements Serializable, BaseEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "entDepositoSeq")
    @SequenceGenerator(name = "entDepositoSeq", sequenceName = "ent_deposito_id_seq", allocationSize = 1)
    private BigDecimal id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Alumno alumno;
    private BigDecimal monto;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaDeposito;
    /*
     El atributo ya no se encuentra en la base de datos, pendiente de revisión.
     private String nombreRespuestaBancaria;
     */
    @ManyToOne
    private Usuario usuarioModifico;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @ManyToOne
    private EstatusDeposito estatusDeposito;
    private String observaciones;
    @ManyToOne
    private TarjetaBancaria TarjetaBancaria;
    @ManyToOne
    private Otorgamiento otorgamiento;
    
    private ErroresBanamex errorBanamex; 
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaEnvioNotificacion;

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

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Date getFechaDeposito() {
        return fechaDeposito;
    }

    public void setFechaDeposito(Date fechaDeposito) {
        this.fechaDeposito = fechaDeposito;
    }

    /*
     El atributo ya no se encuentra en la base de datos, pendiente de revisión.
     public String getNombreRespuestaBancaria() {
     return nombreRespuestaBancaria;
     }

     public void setNombreRespuestaBancaria(String nombreRespuestaBancaria) {
     this.nombreRespuestaBancaria = nombreRespuestaBancaria;
     }
     */
    public Usuario getUsuarioModifico() {
        return usuarioModifico;
    }

    public void setUsuarioModifico(Usuario usuarioModifico) {
        this.usuarioModifico = usuarioModifico;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public EstatusDeposito getEstatusDeposito() {
        return estatusDeposito;
    }

    public void setEstatusDeposito(EstatusDeposito estatusDeposito) {
        this.estatusDeposito = estatusDeposito;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public TarjetaBancaria getTarjetaBancaria() {
        return TarjetaBancaria;
    }

    public void setTarjetaBancaria(TarjetaBancaria TarjetaBancaria) {
        this.TarjetaBancaria = TarjetaBancaria;
    }

    public Otorgamiento getOtorgamiento() {
        return otorgamiento;
    }

    public void setOtorgamiento(Otorgamiento otorgamiento) {
        this.otorgamiento = otorgamiento;
    }

    public ErroresBanamex getErrorBanamex() {
        return errorBanamex;
    }

    public void setErrorBanamex(ErroresBanamex errorBanamex) {
        this.errorBanamex = errorBanamex;
    }

    public Date getFechaEnvioNotificacion() {
        return fechaEnvioNotificacion;
    }

    public void setFechaEnvioNotificacion(Date fechaEnvioNotificacion) {
        this.fechaEnvioNotificacion = fechaEnvioNotificacion;
    }
}