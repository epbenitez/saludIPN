package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.Cache;

/**
 *
 * @author Mario Márquez
 */
@Entity
@Table(name = "vw_depositos_estatus")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class VWDepositosEstatus implements Serializable, BaseEntity {
    @Id
    private BigDecimal id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Otorgamiento otorgamiento;
    @ManyToOne(fetch = FetchType.LAZY)
    private Alumno alumno;
    @ManyToOne(fetch = FetchType.LAZY)
    private Periodo periodo;
    @ManyToOne(fetch = FetchType.LAZY)
    private OrdenDeposito ordenDeposito;
    private Integer mes;
    @ManyToOne(fetch = FetchType.LAZY)
    private  EstatusDeposito estatusDeposito;

    @Override
    public BigDecimal getId() {
        return id;
    }

    @Override
    public void setId(BigDecimal id) {
        this.id = id;
    }

    public Otorgamiento getOtorgamiento() {
        return otorgamiento;
    }

    public void setOtorgamiento(Otorgamiento otorgamiento) {
        this.otorgamiento = otorgamiento;
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

    public OrdenDeposito getOrdenDeposito() {
        return ordenDeposito;
    }

    public void setOrdenDeposito(OrdenDeposito ordenDeposito) {
        this.ordenDeposito = ordenDeposito;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public EstatusDeposito getEstatusDeposito() {
        return estatusDeposito;
    }

    public void setEstatusDeposito(EstatusDeposito estatusDeposito) {
        this.estatusDeposito = estatusDeposito;
    }
    
    
}
