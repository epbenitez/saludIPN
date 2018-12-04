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
 * @author Victor Lozano
 */
@Entity
@Table(name = "cat_estatus_deposito")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class EstatusDeposito implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(generator = "catEstatusDepositoSeq")
    @SequenceGenerator(name = "catEstatusDepositoSeq", sequenceName = "cat_estatus_deposito_id_seq", allocationSize = 1)
    private BigDecimal id;
    private String nombre;

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
}
