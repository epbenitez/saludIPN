/**
 * SISTEMA INFORMÁTICO DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE
 * SERVICIOS ESTUDIANTILES 2015
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
@Table(name = "cat_programa_beca")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class Beca implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(generator = "catProgramaBecaSeq")
    @SequenceGenerator(name = "catProgramaBecaSeq", sequenceName = "cat_programa_beca_ID_SEQ", allocationSize = 1)
    private BigDecimal id;
    private String nombre;
    private String clave;
    private String clavedisp;

    public Beca() {
    }

    public Beca(BigDecimal id) {
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

    public String getClavedisp() {
        return clavedisp;
    }

    public void setClavedisp(String clavedisp) {
        this.clavedisp = clavedisp;
    }

    @Override
    public String toString() {
        return "Beca{" + "id=" + id + ", nombre=" + nombre + ", clave=" + clave + ", clavedisp=" + clavedisp + '}';
    }

    @Override
    public boolean equals(Object obj) {
        Beca another = (Beca) obj;
        if (another instanceof Beca) {
            return another.getId().equals(id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
