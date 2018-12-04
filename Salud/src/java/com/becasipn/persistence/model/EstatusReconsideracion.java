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
 * @author Augusto I. Hernández Ruiz
 *
 * "El problema real no es si las máquinas piensan, sino si lo hacen los
 * hombres" B. F. Skinner.
 */
@Entity
@Table(name = "CAT_ESTATUS_RECONSIDERACION")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class EstatusReconsideracion implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(generator = "EstReconSeq")
    @SequenceGenerator(name = "EstReconSeq", sequenceName = "SEQ_CAT_STAT_RECONSIDERA", allocationSize = 1)
    private BigDecimal id;
    private String Nombre;

    public EstatusReconsideracion() {
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    @Override
    public String toString() {
        return "EstatusReconsideracion{" + "id=" + id + ", Nombre=" + Nombre + '}';
    }
    
    
    
}
