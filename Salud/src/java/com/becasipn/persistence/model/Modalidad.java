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
 * @author Usre-05
 */
@Entity
@Table(name = "cat_modalidad")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class Modalidad implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(generator = "catModalidadSeq")
    @SequenceGenerator(name = "catModalidadSeq", sequenceName = "cat_modalidad_id_seq", allocationSize = 1)
    private BigDecimal id;
    private String nombre;
    private Integer activo;

    public Modalidad() {
    }
    
    public Modalidad(BigDecimal id) {
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

    public Integer getActivo() {
        return activo;
    }

    public void setActivo(Integer activo) {
        this.activo = activo;
    }

}
