package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.Cache;

/**
 *
 * @author Tania G. Sánchez
 */
@Entity
@Table(name = "cat_identificador_otorgamiento")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class IdentificadorOtorgamiento implements Serializable, BaseEntity {
    @Id
    private BigDecimal id;
    private String nombre;

    @Override
    public BigDecimal getId() {
        return id;
    }

    @Override
    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public IdentificadorOtorgamiento() {
    }

    public IdentificadorOtorgamiento(BigDecimal id) {
        this.id = id;
    }
    
    @Override
    public String toString() {
        return "IdentificadorOtorgamiento{" + "id=" + id + ", nombre=" + nombre + '}';
    }
}