/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import org.eclipse.persistence.annotations.Cache;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Patricia Benitez
 */
@Entity
@Table(name = "cat_configuracion")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class Configuracion implements Serializable, BaseEntity, BaseBitacora {

    @Id
    @GeneratedValue(generator = "catConfiguracionSeq")
    @SequenceGenerator(name = "catConfiguracionSeq", sequenceName = "cat_configuracion_id_seq", allocationSize = 1)
    private BigDecimal id;
    private String nombre;
    private String propiedad;
    private String valor;
    private String tip;

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

    public String getPropiedad() {
        return propiedad;
    }

    public void setPropiedad(String propiedad) {
        this.propiedad = propiedad;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    @Override
    public String toString() {
        return "Configuracion{" + "id=" + id + ", nombre=" + nombre + ", propiedad=" + propiedad + ", valor=" + valor + '}';
    }

}
