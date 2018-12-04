package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
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
@Table(name = "rmm_transporte_datos_fam")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class TransporteDatosFamiliares implements Serializable, BaseEntity {
    @Id
    @GeneratedValue(generator = "TranFamSeq")
    @SequenceGenerator(name = "TranFamSeq", sequenceName = "RMM_TRANSP_DATOS_FAM_ID_SEQ", allocationSize = 1)
    private BigDecimal id;
    @ManyToOne
    private CuestionarioTransporte cuestionarioTransporte;
    private String nombrefamiliar;
    @ManyToOne
    private Parentesco parentesco;
    private Integer edad;
    private String ocupacion;
    private Float aportacionmensual;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public CuestionarioTransporte getCuestionarioTransporte() {
        return cuestionarioTransporte;
    }

    public void setCuestionarioTransporte(CuestionarioTransporte cuestionarioTransporte) {
        this.cuestionarioTransporte = cuestionarioTransporte;
    }

    public String getNombrefamiliar() {
        return nombrefamiliar;
    }

    public void setNombrefamiliar(String nombrefamiliar) {
        this.nombrefamiliar = nombrefamiliar;
    }

    public Parentesco getParentesco() {
        return parentesco;
    }

    public void setParentesco(Parentesco parentesco) {
        this.parentesco = parentesco;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public Float getAportacionmensual() {
        return aportacionmensual;
    }

    public void setAportacionmensual(Float aportacionmensual) {
        this.aportacionmensual = aportacionmensual;
    }

    @Override
    public String toString() {
        return "TransporteDatosFamiliares{" + "id=" + id + ", cuestionarioTransporte=" + cuestionarioTransporte + ", nombrefamiliar=" + nombrefamiliar + ", parentesco=" + parentesco + ", edad=" + edad + ", ocupacion=" + ocupacion + ", aportacionmensual=" + aportacionmensual + '}';
    }
}