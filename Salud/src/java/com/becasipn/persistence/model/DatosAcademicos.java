package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.eclipse.persistence.annotations.Cache;

/**
 *
 * @author Mario Márquez
 */
@Entity
@Table(name = "ENT_ALUMNO_DATOS_ACADEMICOS")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class DatosAcademicos implements Serializable,BaseBitacora, BaseEntity {
    @Id
    @GeneratedValue(generator = "datosAcademicos")
    @SequenceGenerator(name = "datosAcademicos", sequenceName = "ENT_ALUMNO_DA_SEQ", allocationSize = 1)
    private BigDecimal id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ALUMNO_ID")
    private Alumno alumno;
    @ManyToOne(fetch = FetchType.LAZY)
    private Periodo periodo;
    private Double promedio;
    private Integer semestre;
    private Float totalCreditos;
    @ManyToOne(fetch = FetchType.LAZY)
    private UnidadAcademica unidadAcademica;
    @ManyToOne(fetch = FetchType.LAZY)
    private Carrera carrera;
    private Integer regular;
    private Integer inscrito;
    private Integer modalidadDAE;
    @ManyToOne(fetch = FetchType.LAZY)
    private Modalidad modalidad;
    private String turno;
    private int egresado; // Aquí es integer en BD es varchar
    private int reprobadas; // Aquí es integer en BD es varchar
    private Integer cumpleCargaMinima; // Aquí es integer en BD es varchar
    private Integer infoActualizadaAdmin;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @ManyToOne(fetch = FetchType.LAZY)
    private Periodo periodoIngreso;

    public DatosAcademicos(Alumno alumno, Periodo periodo, Double promedio, 
            Integer semestre, Float totalCreditos, UnidadAcademica unidadAcademica, 
            Carrera carrera, Integer regular, Integer inscrito, Integer modalidadDAE, 
            Modalidad modalidad, String turno, int reprobadas, Integer egresado, 
            Integer cumpleCargaMinima, Integer infoActualizadaAdmin, Date fechaModificacion) {
        this.alumno = alumno;
        this.periodo = periodo;
        this.promedio = promedio;
        this.semestre = semestre;
        this.totalCreditos = totalCreditos;
        this.unidadAcademica = unidadAcademica;
        this.carrera = carrera;
        this.regular = regular;
        this.inscrito = inscrito;
        this.modalidadDAE = modalidadDAE;
        this.modalidad = modalidad;
        this.turno = turno;
        this.reprobadas = reprobadas;
        this.egresado = egresado;
        this.cumpleCargaMinima = cumpleCargaMinima;
        this.infoActualizadaAdmin = infoActualizadaAdmin;
        this.fechaModificacion = fechaModificacion;
    }

    public DatosAcademicos() {
    }
    
    @Override
    public BigDecimal getId() {
        return id;
    }
    @Override
    public void setId(BigDecimal id) {
        this.id = id;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public Double getPromedio() {
        return promedio;
    }

    public void setPromedio(Double promedio) {
        this.promedio = promedio;
    }

    public Integer getSemestre() {
        return semestre;
    }

    public void setSemestre(Integer semestre) {
        this.semestre = semestre;
    }

    public Carrera getCarrera() {
        return carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

    public Integer getRegular() {
        return regular;
    }

    public void setRegular(Integer regular) {
        this.regular = regular;
    }

    public Integer getInscrito() {
        return inscrito;
    }

    public void setInscrito(Integer inscrito) {
        this.inscrito = inscrito;
    }

    public Integer getModalidadDAE() {
        return modalidadDAE;
    }

    public void setModalidadDAE(Integer modalidadDAE) {
        this.modalidadDAE = modalidadDAE;
    }

    public Modalidad getModalidad() {
        return modalidad;
    }

    public void setModalidad(Modalidad modalidad) {
        this.modalidad = modalidad;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public int getReprobadas() {
        return reprobadas;
    }

    public void setReprobadas(int reprobadas) {
        this.reprobadas = reprobadas;
    }
    
    public Integer getEgresado() {
        return egresado;
    }

    public void setEgresado(Integer egresado) {
        this.egresado = egresado;
    }

    public Integer getCumpleCargaMinima() {
        return cumpleCargaMinima;
    }

    public void setCumpleCargaMinima(Integer cumpleCargaMinima) {
        this.cumpleCargaMinima = cumpleCargaMinima;
    }
    
    public Integer getInfoActualizadaAdmin() {
        return infoActualizadaAdmin;
    }

    public void setInfoActualizadaAdmin(Integer infoActualizadaAdmin) {
        this.infoActualizadaAdmin = infoActualizadaAdmin;
    }

    public Float getTotalCreditos() {
        return totalCreditos;
    }

    public void setTotalCreditos(Float totalCreditos) {
        this.totalCreditos = totalCreditos;
    }

    public UnidadAcademica getUnidadAcademica() {
        return unidadAcademica;
    }

    public void setUnidadAcademica(UnidadAcademica unidadAcademica) {
        this.unidadAcademica = unidadAcademica;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Periodo getPeriodoIngreso() {
        return periodoIngreso;
    }

    public void setPeriodoIngreso(Periodo periodoIngreso) {
        this.periodoIngreso = periodoIngreso;
    }
}