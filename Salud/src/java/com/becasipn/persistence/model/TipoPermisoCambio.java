package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.Cache;


@Entity
@Table(name = "cat_tipo_permiso_cambio")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class TipoPermisoCambio implements BaseEntity, Comparable, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "catTipoPermisoCambioSeq")
    @SequenceGenerator(name = "catTipoPermisoCambioSeq", sequenceName = "cat_tipo_permiso_cambio_id_seq", allocationSize = 1)
    private BigDecimal id;
    private String nombre;
    private String clave;

    public TipoPermisoCambio() {

    }

    public TipoPermisoCambio(BigDecimal id) {
        this.id = id;
    }

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

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TipoPermisoCambio other = (TipoPermisoCambio) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Nombre{" + id + "," + nombre + '}';
    }

    @Override
    public int compareTo(Object o) {
        return (this.equals(o)) ? 0 : 1;
    }
}
