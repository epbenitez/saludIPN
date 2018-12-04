/**
 * SISTEMA INFORMÁTICO DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE
 * SERVICIOS ESTUDIANTILES Created on : 05-ago-2015, 17:58:55
 *
 */
package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.Cache;

/**
 *
 * @author Patricia Benitez
 */
@Entity
@Table(name = "cat_estado_civil")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class EstadoCivil implements Serializable, Comparable, BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "catEstadoCivilSeq")
    @SequenceGenerator(name = "catEstadoCivilSeq", sequenceName = "cat_estado_civil_id_seq", allocationSize = 1)
    private BigDecimal id;

    private String nombre;

    public EstadoCivil() {
    }

    public EstadoCivil(BigDecimal id) {
        this.id = id;
    }

    /**
     * Obtiene el valor de la variable id
     *
     * @return el valor de la variable id
     */
    @Override
    public BigDecimal getId() {
        return id;
    }

    /**
     * Establece el valor de la variable id
     *
     * @param id nuevo valor de la variable id
     */
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EstadoCivil other = (EstadoCivil) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.nombre == null) ? (other.nombre != null) : !this.nombre.equals(other.nombre)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 61 * hash + (this.nombre != null ? this.nombre.hashCode() : 0);
        return hash;
    }

    public int compareTo(Object o) {
        return (this.equals(o)) ? 0 : 1;
    }

    @Override
    public String toString() {
        return "EstadoCivil{" + id + "," + nombre + '}';
    }

}
