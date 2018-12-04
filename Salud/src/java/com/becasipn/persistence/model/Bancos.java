package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.Cache;

/**
 *
 * @author Tania G. Sánchez Manilla
 */
@Entity
@Table(name = "cat_bancos")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class Bancos implements Serializable, BaseEntity, BaseBitacora {
    @Id
    private BigDecimal id;
    private String clave;
    private String nombreCorto;
    
    @Override
    public BigDecimal getId() {
        return id;
    }

    @Override
    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombreCorto() {
        return nombreCorto;
    }

    public void setNombreCorto(String nombreCorto) {
        this.nombreCorto = nombreCorto;
    }

    @Override
    public String toString() {
        return "Bancos{" + "id=" + id + ", clave=" + clave + ", nombreCorto=" + nombreCorto + '}';
    }
}