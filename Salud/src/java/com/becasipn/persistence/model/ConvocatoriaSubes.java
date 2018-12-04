package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.Cache;

@Entity
@Table(name="CAT_CONVOCATORIA_SUBES")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class ConvocatoriaSubes  implements Serializable,BaseEntity {

    
    @Id 
    private BigDecimal id;
    private String descripcion;
    private String clave;
    private Boolean activo;
    
    public ConvocatoriaSubes() {
    }

	
    public ConvocatoriaSubes(BigDecimal id, String descripcion, String clave) {
        this.id = id;
        this.descripcion = descripcion;
        this.clave = clave;
    }
   
    @Override
    public BigDecimal getId() {
        return this.id;
    }
    
    @Override
    public void setId(BigDecimal id) {
        this.id = id;
    }

    
    public String getDescripcion() {
        return this.descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    
    public String getClave() {
        return this.clave;
    }
    
    public void setClave(String clave) {
        this.clave = clave;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "ConvocatoriaSubes{" + "id=" + id + ", descripcion=" + descripcion + ", clave=" + clave + '}';
    }    
}


