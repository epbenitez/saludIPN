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
@Table(name = "cat_clasificacion_solicitud")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class ClasificacionSolicitud implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(generator = "catClasificacionSolicitudSeq")
    @SequenceGenerator(name = "catClasificacionSolicitudSeq", sequenceName = "cat_programa_beca_ID_SEQ", allocationSize = 1)
    private BigDecimal id;
    private String nombre;
    private String clave;

    public ClasificacionSolicitud(BigDecimal id) {
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
    public String toString() {
	return "Beca{" + "id=" + id + ", nombre=" + nombre + ", clave=" + clave + '}';
    }

    @Override
    public boolean equals(Object obj) {
	ClasificacionSolicitud another = (ClasificacionSolicitud) obj;
	if (another instanceof ClasificacionSolicitud) {
	    return another.getId().equals(id);
	}
	return false;
    }

    @Override
    public int hashCode() {
	return id.hashCode();
    }

    public ClasificacionSolicitud() {
    }

}
