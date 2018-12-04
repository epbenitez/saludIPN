package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.eclipse.persistence.annotations.Cache;

/**
 * @author Gustavo Adolfo Alamillo Medina
 * @version 1.0
 */
@Entity
@Table(name = "ent_tarjeta_bancaria")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class TarjetaBancaria implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(generator = "InvSeq")
    @SequenceGenerator(name = "InvSeq", sequenceName = "ENT_TARJETA_BANCARIA_ID_SEQ", allocationSize = 1)
    private BigDecimal id;
    private String numtarjetabancaria;
    private String lote;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaLote;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "TarjetaBancaria")
    @OrderBy("fechaModificacion DESC")
    private List<BitacoraTarjetaBancaria> bitacoraTarjetaBancarias = new ArrayList();
    private BigDecimal secuencia;
    private Integer cuenta;
    private Integer origen;
    
    public TarjetaBancaria() {
    }

    public TarjetaBancaria(String numtarjetabancaria, String lote, Date fechaLote) {
        this.numtarjetabancaria = numtarjetabancaria;
        this.lote = lote;
        this.fechaLote = fechaLote;
    }

    public TarjetaBancaria(String numtarjetabancaria, Date fechaLote, Integer cuenta) {
        this.numtarjetabancaria = numtarjetabancaria;
        this.fechaLote = fechaLote;
        this.cuenta = cuenta;
    }
    

    @Override
    public BigDecimal getId() {
        return id;
    }

    @Override
    public void setId(BigDecimal id) {
        this.id = id;
    }

    public List<BitacoraTarjetaBancaria> getBitacoraTarjetaBancarias() {
        return bitacoraTarjetaBancarias;
    }

    public void setBitacoraTarjetaBancarias(List<BitacoraTarjetaBancaria> bitacoraTarjetaBancarias) {
        this.bitacoraTarjetaBancarias = bitacoraTarjetaBancarias;
    }

    public String getNumtarjetabancaria() {
        return numtarjetabancaria;
    }

    public void setNumtarjetabancaria(String numtarjetabancaria) {
        this.numtarjetabancaria = numtarjetabancaria;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public Date getFechaLote() {
        return fechaLote;
    }

    public void setFechaLote(Date fechaLote) {
        this.fechaLote = fechaLote;
    }

    public BigDecimal getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public Integer getCuenta() {
        return cuenta;
    }

    public void setCuenta(Integer cuenta) {
        this.cuenta = cuenta;
    }

    public Integer getOrigen() {
        return origen;
    }

    public void setOrigen(Integer origen) {
        this.origen = origen;
    }
}
