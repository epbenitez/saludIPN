package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.eclipse.persistence.annotations.Cache;

/**
 *
 * @author Mario Márquez
 */
@Entity
@Table(name = "vw_ultima_baja_detalle")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class VWUltimaBajaDetalle implements Serializable, BaseEntity {
    @Id
    private BigDecimal id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Otorgamiento otorgamiento;
    @ManyToOne(fetch = FetchType.LAZY)
    private Movimiento movimiento;
    @ManyToOne(fetch = FetchType.LAZY)
    private Periodo periodo;
    @ManyToOne(fetch = FetchType.LAZY)
    private Proceso proceso;
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuarioModifico;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaBaja;

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

    public Movimiento getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(Movimiento movimiento) {
        this.movimiento = movimiento;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public Proceso getProceso() {
        return proceso;
    }

    public void setProceso(Proceso proceso) {
        this.proceso = proceso;
    }

    public Usuario getUsuarioModifico() {
        return usuarioModifico;
    }

    public void setUsuarioModifico(Usuario usuarioModifico) {
        this.usuarioModifico = usuarioModifico;
    }

    public Date getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }
    
    
}
