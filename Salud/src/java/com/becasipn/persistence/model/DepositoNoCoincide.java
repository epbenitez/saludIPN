package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.Cache;

/**
 *
 * @author Tania G. Sánchez
 */
@Entity
@Table(name = "ent_deposito_nocoincide")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class DepositoNoCoincide implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(generator = "entDepositoNocoincideSeq")
    @SequenceGenerator(name = "entDepositoNocoincideSeq", sequenceName = "ent_deposito_nocoincide_id_seq", allocationSize = 1)
    private BigDecimal id;
    private String numtarjetabancaria;
    private String boleta;
    private Float monto;
    @ManyToOne(fetch = FetchType.LAZY)
    private OrdenDeposito ordenDeposito;
    @ManyToOne(fetch = FetchType.LAZY)
    private EstatusDeposito estatusDeposito;

    @Override
    public BigDecimal getId() {
        return id;
    }

    @Override
    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getNumtarjetabancaria() {
        return numtarjetabancaria;
    }

    public void setNumtarjetabancaria(String numtarjetabancaria) {
        this.numtarjetabancaria = numtarjetabancaria;
    }

    public String getBoleta() {
        return boleta;
    }

    public void setBoleta(String boleta) {
        this.boleta = boleta;
    }

    public Float getMonto() {
        return monto;
    }

    public void setMonto(Float monto) {
        this.monto = monto;
    }

    public OrdenDeposito getOrdenDeposito() {
        return ordenDeposito;
    }

    public void setOrdenDeposito(OrdenDeposito ordenDeposito) {
        this.ordenDeposito = ordenDeposito;
    }

    public EstatusDeposito getEstatusDeposito() {
        return estatusDeposito;
    }

    public void setEstatusDeposito(EstatusDeposito estatusDeposito) {
        this.estatusDeposito = estatusDeposito;
    }
}
