package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.Cache;

/**
 *
 * @author Mario Márquez
 */

@Entity
@Table(name = "ENT_PADRON_BECA_UNIVERSAL")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class BecaUniversal implements Serializable, BaseEntity {
    @Id
    @GeneratedValue(generator = "becaUniversal")
    @SequenceGenerator(name = "becaUniversal", sequenceName = "ENT_PADRON_BECA_UNIVERSAL_SEQ", allocationSize = 1)
    private BigDecimal id;
    private String boleta;
    private String referencia;
    @OneToOne
    @JoinColumn(name="ALUMNO_ID")
    private Alumno alumno;
    private String curp;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String claveUA;
    private Integer estatusReferencia;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getBoleta() {
        return boleta;
    }

    public void setBoleta(String boleta) {
        this.boleta = boleta;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getClaveUA() {
        return claveUA;
    }

    public void setClaveUA(String claveUA) {
        this.claveUA = claveUA;
    }

    public Integer getEstatusReferencia() {
        return estatusReferencia;
    }

    public void setEstatusReferencia(Integer estatusReferencia) {
        this.estatusReferencia = estatusReferencia;
    }
    
    
}


