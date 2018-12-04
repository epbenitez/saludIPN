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
 * @author Patricia Benitez
 */
@Entity
@Table(name = "ent_carrera")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class Carrera implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(generator = "entCarreraSeq")
    @SequenceGenerator(name = "entCarreraSeq", sequenceName = "ent_carrera_id_seq", allocationSize = 1)
    private BigDecimal id;
    private String planEstudios;
    private String claveCarrera;
    private String carrera;
    private String especialidad;
    private Integer numeroSemestres;
    @ManyToOne(fetch = FetchType.LAZY)
    private UnidadAcademica unidadAcademica;
    private Boolean activo;

    public Carrera() {
    }
    
    public Carrera(BigDecimal id) {
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

    public String getPlanEstudios() {
        return planEstudios;
    }

    public void setPlanEstudios(String planEstudios) {
        this.planEstudios = planEstudios;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public Integer getNumeroSemestres() {
        return numeroSemestres;
    }

    public void setNumeroSemestres(Integer numeroSemestres) {
        this.numeroSemestres = numeroSemestres;
    }

    public String getClaveCarrera() {
        return claveCarrera;
    }

    public void setClaveCarrera(String claveCarrera) {
        this.claveCarrera = claveCarrera;
    }

    public UnidadAcademica getUnidadAcademica() {
        return unidadAcademica;
    }

    public void setUnidadAcademica(UnidadAcademica unidadAcademica) {
        this.unidadAcademica = unidadAcademica;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Carrera{" + "id=" + id + ", planEstudios=" + planEstudios + ", claveCarrera=" + claveCarrera + ", carrera=" + carrera + ", especialidad=" + especialidad + ", numeroSemestres=" + numeroSemestres + ", unidadAcademica=" + unidadAcademica + ", activo=" + activo + '}';
    }
}
