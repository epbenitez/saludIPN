/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
@Table(name = "cat_nacionalidad")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class Nacionalidad implements Comparable, Serializable, BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "catNacionalidadSeq")
    @SequenceGenerator(name = "catNacionalidadSeq", sequenceName = "cat_nacionalidad_id_seq", allocationSize = 1)
    private BigDecimal id;
    private String nombre;

    public Nacionalidad() {
    }

    public Nacionalidad(BigDecimal id) {
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
    public int compareTo(Object o) {
        return (this.equals(o)) ? 0 : 1;
    }

    @Override
    public String toString() {
        return "Nacionalidad{" + id + "," + nombre + '}';
    }

}
