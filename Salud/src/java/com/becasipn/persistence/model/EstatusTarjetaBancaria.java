package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.Cache;

@Entity
@Table(name = "cat_estatus_tarjeta_bancaria")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class EstatusTarjetaBancaria implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(generator = "catEstatusTarjetaBancariaSeq")
    @SequenceGenerator(name = "catEstatusTarjetaBancariaSeq", sequenceName = "cat_estatus_tarjeta_bancaria_i", allocationSize = 1)
    private BigDecimal id;

    private String nombre;
    private String descripcion;    
    private String observaciones;
    private String resumen;   
    
    @OneToMany(mappedBy = "estatus")
    private Set<BitacoraTarjetaBancaria> rmmBitacoraTarjetaBancariaSet;

    public EstatusTarjetaBancaria() {
    }

    public EstatusTarjetaBancaria(BigDecimal id) {
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
    
    public String getDescripcion() {
        return descripcion;
    }

    public String getObservaciones() {
        return observaciones;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public String getResumen() {
        return resumen;
    }
    
    public Set<BitacoraTarjetaBancaria> getRmmBitacoraTarjetaBancariaSet() {
        return rmmBitacoraTarjetaBancariaSet;
    }

    public void setRmmBitacoraTarjetaBancariaSet(Set<BitacoraTarjetaBancaria> rmmBitacoraTarjetaBancariaSet) {
        this.rmmBitacoraTarjetaBancariaSet = rmmBitacoraTarjetaBancariaSet;
    }

    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EstatusTarjetaBancaria other = (EstatusTarjetaBancaria) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
