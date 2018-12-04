package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.Cache;

/**
 *
 * @author Tania G. Sánchez
 */

@Entity
@Table(name = "vw_conteo_depositos")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class VWConteoDepositos implements Serializable, BaseEntity {
    @Id
    private BigDecimal id;
    @ManyToOne
    private Otorgamiento otorgamiento;
    @ManyToOne
    private Alumno alumno;
    @ManyToOne
    private Periodo periodo;
    private Integer conteoDepositos;

    @Override
    public BigDecimal getId() {
        return id;
    }

    @Override
    public void setId(BigDecimal id) {
        this.id = id;
    }

    public Otorgamiento getOtorgamiento() {
        return otorgamiento;
    }

    public void setOtorgamiento(Otorgamiento otorgamiento) {
        this.otorgamiento = otorgamiento;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public Integer getConteoDepositos() {
        return conteoDepositos;
    }

    public void setConteoDepositos(Integer conteoDepositos) {
        this.conteoDepositos = conteoDepositos;
    }

    @Override
    public String toString() {
        return "VWConteoDepositos{" + "id=" + id + ", otorgamiento=" + otorgamiento + ", alumno=" + alumno + ", periodo=" + periodo + ", conteoDepositos=" + conteoDepositos + '}';
    }
}