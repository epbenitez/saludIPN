/**
 * SISTEMA INFORMÁTICO DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE
 * SERVICIOS ESTUDIANTILES 2015
 *
 */
package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.Cache;

/**
 *
 * @author Tania G. Sánchez
 */
@Entity
@Table(name = "cat_tipo_proceso")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class TipoProceso implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(generator = "catTipoProcesoSeq")
    @SequenceGenerator(name = "catTipoProcesoSeq", sequenceName = "cat_tipo_proceso_id_seq", allocationSize = 1)
    private BigDecimal id;
    private String nombre;
    private String descripcion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Movimiento movimiento;

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

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Movimiento getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(Movimiento movimiento) {
        this.movimiento = movimiento;
    }

    @Override
    public String toString() {
        return "TipoProceso{" + "id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", movimiento=" + movimiento + '}';
    }
}
