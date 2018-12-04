package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.eclipse.persistence.annotations.Cache;

/**
 * @author Gustavo Adolfo Alamillo Medina
 * @version 1.0
 */
@Entity
@Table(name = "rmm_alumno_tarjeta_bancaria")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class AlumnoTarjetaBancaria implements Serializable, BaseEntity {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "AlumnoTarjetaBancariaSeq")
    @SequenceGenerator(name = "AlumnoTarjetaBancariaSeq", sequenceName = "rmm_alumno_tarjeta_bancaria_id", allocationSize = 1)
    private BigDecimal id;
    private boolean tarjetaActiva;
    private boolean vigente;
    @JoinColumn(name = "alumno_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Alumno alumno;
    @JoinColumn(name = "tarjetabancaria_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private TarjetaBancaria tarjetaBancaria;
    private String identificador;
    @Transient
    private TarjetaBancaria tarjetaAnterior;
    @ManyToOne(fetch = FetchType.LAZY)
    private AlumnoDatosBancarios datosBancarios;
    @JoinColumn(name = "ESTATUSTARJBANC_ID", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private EstatusTarjetaBancaria estatusTarjBanc;
    @JoinColumn(name = "SOLICITUDCUENTA_ID", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private SolicitudCuentas solicitudCuentas;

    @Override
    public BigDecimal getId() {
	return id;
    }

    @Override
    public void setId(BigDecimal id) {
	this.id = id;
    }

    public boolean isTarjetaActiva() {
	return tarjetaActiva;
    }

    public void setTarjetaActiva(boolean tarjetaActiva) {
	this.tarjetaActiva = tarjetaActiva;
    }

    public Alumno getAlumno() {
	return alumno;
    }

    public void setAlumno(Alumno alumno) {
	this.alumno = alumno;
    }

    public TarjetaBancaria getTarjetaBancaria() {
	return tarjetaBancaria;
    }

    public void setTarjetaBancaria(TarjetaBancaria tarjetaBancaria) {
	this.tarjetaBancaria = tarjetaBancaria;
    }

    public String getIdentificador() {
	return identificador;
    }

    public void setIdentificador(String identificador) {
	this.identificador = identificador;
    }

    public TarjetaBancaria getTarjetaAnterior() {
	return tarjetaAnterior;
    }

    public void setTarjetaAnterior(TarjetaBancaria tarjetaAnterior) {
	this.tarjetaAnterior = tarjetaAnterior;
    }

    @Override
    public String toString() {
	return alumno.getNombre() + "\t" + tarjetaBancaria.getNumtarjetabancaria();
    }

    public boolean isVigente() {
	return vigente;
    }

    public void setVigente(boolean vigente) {
	this.vigente = vigente;
    }

    public SolicitudCuentas getSolicitudCuentas() {
	return solicitudCuentas;
    }
    public AlumnoDatosBancarios getDatosBancarios() {
        return datosBancarios;
    }

    public void setDatosBancarios(AlumnoDatosBancarios datosBancarios) {
        this.datosBancarios = datosBancarios;
    }

    public EstatusTarjetaBancaria getEstatusTarjBanc() {
        return estatusTarjBanc;
    }

    public void setEstatusTarjBanc(EstatusTarjetaBancaria estatusTarjBanc) {
        this.estatusTarjBanc = estatusTarjBanc;
    }

    public void setSolicitudCuentas(SolicitudCuentas solicitudCuentas) {
	this.solicitudCuentas = solicitudCuentas;
    }
}