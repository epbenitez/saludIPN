/**
 * SISTEMA INFORMÁTICO DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE
 * SERVICIOS ESTUDIANTILES Created on : 07-ago-2015, 9:51:25
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
 * @author Patricia Benitez
 */
@Entity
@Table(name = "cat_compania_celular")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class CompaniaCelular implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(generator = "catCompaniaCelularSeq")
    @SequenceGenerator(name = "catCompaniaCelularSeq", sequenceName = "cat_compania_celular_id_seq", allocationSize = 1)
    private BigDecimal id;
    private String nombre;

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

    public CompaniaCelular() {
    }

    public CompaniaCelular(BigDecimal id) {
        this.id = id;
    }
    
}
