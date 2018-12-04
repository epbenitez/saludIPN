package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.Cache;

@Entity
@Table(name = "ent_agrupador_menu")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class Agrupador implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private BigDecimal id;

    private String descripcion;

    private String orden;

    private String icono;

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getOrden() {
        return orden;
    }

    public void setOrden(String orden) {
        this.orden = orden;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Agrupador other = (Agrupador) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        if ((this.descripcion == null) ? (other.descripcion != null) : !this.descripcion.equals(other.descripcion)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 19 * hash + (this.descripcion != null ? this.descripcion.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" + id + "," + descripcion + "}";
    }

}
