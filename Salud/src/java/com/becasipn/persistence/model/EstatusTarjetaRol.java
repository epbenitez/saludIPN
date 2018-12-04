
package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.Cache;

/**
 * 
 * @author Rafael Cardenas
 */
@Entity
@Table(name = "rmm_estatus_tarjeta_rol")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class EstatusTarjetaRol implements Serializable, BaseEntity {
    
    @Id
    private BigDecimal id;
    @JoinColumn(name = "estatus_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private EstatusTarjetaBancaria estatus;
    @JoinColumn(name = "estatusnuevo_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private EstatusTarjetaBancaria estatusNuevo;
    @JoinColumn(name = "rol_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Rol rol;
    private Integer orden;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public EstatusTarjetaBancaria getEstatus() {
        return estatus;
    }

    public void setEstatus(EstatusTarjetaBancaria estatus) {
        this.estatus = estatus;
    }

    public EstatusTarjetaBancaria getEstatusNuevo() {
        return estatusNuevo;
    }

    public void setEstatusNuevo(EstatusTarjetaBancaria estatusNuevo) {
        this.estatusNuevo = estatusNuevo;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

}
