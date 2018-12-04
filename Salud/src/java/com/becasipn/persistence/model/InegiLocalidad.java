package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.Cache;

/**
 *
 * @author Tania Gabriela Sánchez
 */
@Entity
@Table(name = "cat_inegi_localidad")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class InegiLocalidad  implements Serializable, Comparable<InegiLocalidad>, BaseEntity {

    @Id
    private BigDecimal id;
    @ManyToOne(fetch = FetchType.LAZY)
    private EntidadFederativa entidad;
    @ManyToOne(fetch = FetchType.LAZY)
    private MunicipioDelegacion municipio;
    private Integer claveInegi;
    private String localidad;
    
    public InegiLocalidad() {

    }

    public InegiLocalidad(BigDecimal id) {
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

    public EntidadFederativa getEntidad() {
        return entidad;
    }

    public void setEntidad(EntidadFederativa entidad) {
        this.entidad = entidad;
    }

    public MunicipioDelegacion getMunicipio() {
        return municipio;
    }

    public void setMunicipio(MunicipioDelegacion municipio) {
        this.municipio = municipio;
    }

    public Integer getClaveInegi() {
        return claveInegi;
    }

    public void setClaveInegi(Integer claveInegi) {
        this.claveInegi = claveInegi;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }
    
    /**
     * {@inheritDoc}
     * @param obj
     */
    public int compareTo(InegiLocalidad obj) {
        String nombreTmp = localidad == null ? null : localidad.toLowerCase();
        String objTmp = obj.getLocalidad() == null ? null : obj.getLocalidad().toLowerCase();
        return nombreTmp.compareTo(objTmp);
    }
}
