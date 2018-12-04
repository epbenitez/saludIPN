/**
 * SISTEMA INFORMÁTICO DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE
 * SERVICIOS ESTUDIANTILES 2015
 *
 */
package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;

import javax.persistence.Id;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.Cache;

/**
 *
 * @author Patricia Benitez
 */
@Entity
@Table(name = "CAT_BECA_EXTERNA")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class BecaExterna implements Serializable, BaseEntity {

    @Id
    private BigDecimal id;
    private String nombre;

    public BecaExterna() {
    }

    public BecaExterna(BigDecimal id) {
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

    @Override
    public String toString() {
        return "BecaExterna{" + "id=" + id + ", nombre=" + nombre + '}';
    }

    @Override
    public boolean equals(Object obj) {
        BecaExterna another = (BecaExterna) obj;
        if (another instanceof BecaExterna) {
            return another.getId().equals(id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
