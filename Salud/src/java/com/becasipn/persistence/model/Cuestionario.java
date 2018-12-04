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
@Table(name = "ent_cuestionario")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class Cuestionario implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(generator = "entCuestionarioSeq")
    @SequenceGenerator(name = "entCuestionarioSeq", sequenceName = "ent_cuestionario_id_seq", allocationSize = 1)
    private BigDecimal id;
    private String nombre;
    private Boolean activo;

    public Cuestionario() {

    }

    public Cuestionario(BigDecimal id) {
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

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Cuestionario{" + "id=" + id + ", nombre=" + nombre + ", activo=" + activo + '}';
    }
}
