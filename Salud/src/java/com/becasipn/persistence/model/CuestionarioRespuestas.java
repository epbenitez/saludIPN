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
@Table(name = "ent_cuestionario_respuesta")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class CuestionarioRespuestas implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(generator = "entCuestionarioRespuestaSeq")
    @SequenceGenerator(name = "entCuestionarioRespuestaSeq", sequenceName = "ent_cuestionario_respuesta_id_", allocationSize = 1)
    private BigDecimal id;
    private String nombre;

    public CuestionarioRespuestas() {

    }

    public BigDecimal getId() {
        return id;
    }

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
        return "CuestionarioRespuestas{" + "id=" + id + ", nombre=" + nombre + '}';
    }

}
