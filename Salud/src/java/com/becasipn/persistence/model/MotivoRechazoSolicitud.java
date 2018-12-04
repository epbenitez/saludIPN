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
@Table(name = "cat_motivo_rechazo_solicitud")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class MotivoRechazoSolicitud implements Serializable, BaseEntity {
    @Id
    @GeneratedValue(generator = "catMotivoRechazoSolicitudSeq")
    @SequenceGenerator(name = "catMotivoRechazoSolicitudSeq", sequenceName = "cat_programa_beca_ID_SEQ", allocationSize = 1)
    private BigDecimal id;
    private String nombre;
    private String clave;
    private Boolean visible;

    public MotivoRechazoSolicitud(){
        
    }
    
    public MotivoRechazoSolicitud(BigDecimal id){
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

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    @Override
    public String toString() {
        return "MotivoRechazoSolicitud{" + "id=" + id + ", nombre=" + nombre + ", clave=" + clave + ", visible=" + visible + '}';
    }

    @Override
    public boolean equals(Object obj) {
        MotivoRechazoSolicitud another = (MotivoRechazoSolicitud) obj;
        if (another instanceof MotivoRechazoSolicitud) {
            return another.getId().equals(id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}