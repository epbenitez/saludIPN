/**
 * SISTEMA INFORMÁTICO DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE
 * SERVICIOS ESTUDIANTILES 2015
 *
 */
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
 * @author Patricia Benitez
 */
@Entity
@Table(name = "ENT_OTORGAMIENTOS_EXTERNOS")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class OtorgamientoExterno implements Serializable, BaseEntity {

    @Id
    private BigDecimal id;
    private String nombre;
    private String curp;
    private String boleta;
    @ManyToOne
    private Alumno alumno;
    @ManyToOne
    private BecaExterna becaExterna;
    @ManyToOne
    private CicloEscolar cicloEscolar;
    private Boolean activo;
    @ManyToOne
    private UnidadAcademica unidadacademica;
    @ManyToOne
    private Periodo periodo;

    public OtorgamientoExterno() {
    }

    public OtorgamientoExterno(BigDecimal id) {
        this.id = id;
    }

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

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getBoleta() {
        return boleta;
    }

    public void setBoleta(String boleta) {
        this.boleta = boleta;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public BecaExterna getBecaExterna() {
        return becaExterna;
    }

    public void setBecaExterna(BecaExterna becaExterna) {
        this.becaExterna = becaExterna;
    }

    public CicloEscolar getCicloEscolar() {
        return cicloEscolar;
    }

    public void setCicloEscolar(CicloEscolar cicloEscolar) {
        this.cicloEscolar = cicloEscolar;
    }
    
    public Boolean getActivo() {
        return activo;
    }
    
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public UnidadAcademica getUnidadacademica() {
        return unidadacademica;
    }

    public void setUnidadacademica(UnidadAcademica unidadacademica) {
        this.unidadacademica = unidadacademica;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    @Override
    public String toString() {
        return "OtorgamientoExterno{" + "id=" + id + ", nombre=" + nombre + ", curp=" + curp + ", boleta=" + boleta + ", alumno=" + alumno + ", becaExterna=" + becaExterna + ", cicloEscolar=" + cicloEscolar + ", activo=" + activo + ", unidadacademica=" + unidadacademica + ", periodo=" + periodo + '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        OtorgamientoExterno another = (OtorgamientoExterno) obj;
        if (another instanceof OtorgamientoExterno) {
            return another.getId().equals(id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
