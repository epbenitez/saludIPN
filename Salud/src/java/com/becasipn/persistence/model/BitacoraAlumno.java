package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 * @author Victor Lozano
 * @date 20/04/2016
 */
@Entity
@Table(name = "rmm_bitacora_alumno")
@Cacheable(false)
public class BitacoraAlumno implements Serializable, BaseEntity {

    @Id
     @GeneratedValue(generator = "rmmBitacoraAlumnoSeq")
    @SequenceGenerator(name = "rmmBitacoraAlumnoSeq", sequenceName = "rmm_bitacora_alumno_id_seq", allocationSize = 1)
    private BigDecimal id;
    @JoinColumn(name = "usuariomodifico_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuario;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @JoinColumn(name = "alumno_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Alumno alumno;
    @JoinColumn(name = "periodo_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Periodo periodo;
    private Integer semestre;
    private Double promedio;
    private Integer inscrito;
    private Integer regular;
    private String curp;
    @JoinColumn(name = "SALARIOSMINCUESTRESP_ID", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CuestionarioRespuestas salariosMinimos;

    public BitacoraAlumno() {
    }

    public BitacoraAlumno(Usuario usuario, Date fechaModificacion, Alumno alumno,
            Periodo periodo, Integer semestre, Double promedio, Integer inscrito, Integer regular) {
        this.usuario = usuario;
        this.fechaModificacion = fechaModificacion;
        this.alumno = alumno;
        this.periodo = periodo;
        this.semestre = semestre;
        this.promedio = promedio;
        this.inscrito = inscrito;
        this.regular = regular;
    }

    public BitacoraAlumno(Usuario usuario, Date fechaModificacion, Alumno alumno,
            Periodo periodo, Integer semestre, Double promedio, Integer inscrito,
            Integer regular, CuestionarioRespuestas salariosMinimos) {
        this.usuario = usuario;
        this.fechaModificacion = fechaModificacion;
        this.alumno = alumno;
        this.periodo = periodo;
        this.semestre = semestre;
        this.promedio = promedio;
        this.inscrito = inscrito;
        this.regular = regular;
        this.salariosMinimos = salariosMinimos;
    }

    public BitacoraAlumno(Usuario usuario, Date fechaModificacion, Alumno alumno, Periodo periodo, Integer semestre, Double promedio, Integer inscrito, Integer regular, String curp) {
        this.usuario = usuario;
        this.fechaModificacion = fechaModificacion;
        this.alumno = alumno;
        this.periodo = periodo;
        this.semestre = semestre;
        this.promedio = promedio;
        this.inscrito = inscrito;
        this.regular = regular;
        this.curp = curp;
    }
    
    @Override
    public BigDecimal getId() {
        return id;
    }

    @Override
    public void setId(BigDecimal id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
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

    public Integer getSemestre() {
        return semestre;
    }

    public void setSemestre(Integer semestre) {
        this.semestre = semestre;
    }

    public Double getPromedio() {
        return promedio;
    }

    public void setPromedio(Double promedio) {
        this.promedio = promedio;
    }

    public Integer getInscrito() {
        return inscrito;
    }

    public void setInscrito(Integer inscrito) {
        this.inscrito = inscrito;
    }

    public Integer getRegular() {
        return regular;
    }

    public void setRegular(Integer regular) {
        this.regular = regular;
    }

    public CuestionarioRespuestas getSalariosMinimos() {
        return salariosMinimos;
    }

    public void setSalariosMinimos(CuestionarioRespuestas salariosMiimos) {
        this.salariosMinimos = salariosMinimos;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }
    
    @Override
    public String toString() {
        return "BitacoraAlumno{" + "id=" + id + ", usuario=" + usuario + ", fechaModificacion=" + fechaModificacion + ", alumno=" + alumno + ", periodo=" + periodo + ", semestre=" + semestre + ", promedio=" + promedio + ", inscrito=" + inscrito + ", regular=" + regular + '}';
    }

}
