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

@Entity
@Table(name = "rmm_proceso_programa_beca")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class ProcesoProgramaBeca implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(generator = "prPBSeq")
    @SequenceGenerator(name = "prPBSeq", sequenceName = "RMM_PROCESO_PB_ID_SEQ", allocationSize = 1)
    private BigDecimal id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Proceso proceso;
    @ManyToOne(fetch = FetchType.LAZY)
    private Beca programaBeca;

    @Override
    public BigDecimal getId() {
        return id;
    }

    @Override
    public void setId(BigDecimal id) {
        this.id = id;
    }

    public Proceso getProceso() {
        return proceso;
    }

    public void setProceso(Proceso proceso) {
        this.proceso = proceso;
    }

    public Beca getProgramaBeca() {
        return programaBeca;
    }

    public void setProgramaBeca(Beca programaBeca) {
        this.programaBeca = programaBeca;
    }

}
