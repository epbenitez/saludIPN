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
 * @author Othoniel Herrera
 */
@Entity
@Table(name = "ENT_PADRON_BECA_UNIVERSAL")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class BecasComplementarias implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(generator = "becasComplementarias")
    @SequenceGenerator(name = "becasComplementarias", sequenceName = "SEQ_ENT_BECAS_COMP", allocationSize = 1)
    private BigDecimal id;
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoBecaPeriodo normal;
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoBecaPeriodo complementaria;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public TipoBecaPeriodo getNormal() {
        return normal;
    }

    public void setNormal(TipoBecaPeriodo normal) {
        this.normal = normal;
    }

    public TipoBecaPeriodo getComplementaria() {
        return complementaria;
    }

    public void setComplementaria(TipoBecaPeriodo complementaria) {
        this.complementaria = complementaria;
    }
}
