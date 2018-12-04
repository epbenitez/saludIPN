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
 * @author Tania G. Sánchez
 */
@Entity
@Table(name = "cat_areas_conocimiento")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class AreasConocimiento implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(generator = "catAreasSeq")
    @SequenceGenerator(name = "catAreasSeq", sequenceName = "cat_areas_conocimiento_ID_SEQ", allocationSize = 1)
    private BigDecimal id;
    private String nombre;
    private String clave;

    public AreasConocimiento(){
    }
    
    public AreasConocimiento(BigDecimal id) {
        this.id = id;
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

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    @Override
    public String toString() {
        return "AreasConocimiento{" + "id=" + id + ", nombre=" + nombre + ", clave=" + clave + '}';
    }
}
