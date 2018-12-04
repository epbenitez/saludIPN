package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.OneToOne;
import javax.persistence.ParameterMode;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import org.eclipse.persistence.annotations.Cache;

/**
 *
 * @author Patricia Benitez
 */
@Entity
@Table(name = "vw_dae")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(
	    name = "ALUMNOSINSCRITODAE",
	    procedureName = "SP_ALUMNOINSCRITODAE",
	    resultSetMappings = {"AlumnoDAEMapping"},
	    parameters = {
		@StoredProcedureParameter(name = "BOLETA", type = String.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "resultados", type = Object.class, mode = ParameterMode.REF_CURSOR)
	    })
})
@SqlResultSetMapping(
	name = "AlumnoDAEMapping",
	entities = {
	    @EntityResult(entityClass = AlumnoDAE.class) //0
	}
)
public class AlumnoDAE implements Serializable, BaseEntity {

    @Id
    @Column(name = "ID")
    private BigDecimal idDae;
    private String boleta;
    private String curp;            //Puede venir vacio o no venir correcto
    private String nombre;
    private String apellido_pat;    //Pueden venir vacioes
    private String apellido_mat;    //Pueden venir vacioes
    private String escuela;         //Pueden venir con un cero antes 02
    private Float promedio;        //Pueden ser null 
    private String turno;           // 
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fecha_nac;         //Pueden ser null 
    private Integer semestre_min;   //No tan importante
    private Integer semestre_max;   //No tan importante
    private Integer semestre_inscrito;
    private String calle;           //Pueden ser null 
    private String colonia;         //Pueden ser null 
    private String estado;          //Pueden ser null 
    private String cp;              //Pueden ser null 
    private String carrera;
    private String especialidad;
    private String plan_estud;
    private String e_mail;          //Pueden ser null 
    private String tipo_alumno;     //No puede ser null
    private Integer inscrito;        // 0 = No inscrito    0 > Mayor a inscrito
    private String sexo;            // M ó F
    private Float tot_creditos;     //.0000 (Checar)
    private Integer modo_ingreso;   //
    private Integer periodo_escolar_ingreso; //
    @OneToOne(fetch = FetchType.LAZY)
    private Usuario usuario;
    private Integer origen;        // 1 Actualizado por el maestro 0 lo trae de la DAE
    private Integer egresado;
    private int reprobadas;
    private Integer cumple_carga_minima;
//    private Integer confronta_dae;
//    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
//    private Date fecha_ingreso;
    @Transient
    private Carrera carreraObj;
    @Transient
    private Boolean resultadoCarga;
    @Transient
    private Boolean registradoSistema;
    @Transient
    private Boolean llenoESE;

    @Override
    public BigDecimal getId() {
	return idDae;
    }

    @Override
    public void setId(BigDecimal id) {
	this.idDae = id;
    }

    public String getBoleta() {
	return boleta;
    }

    public void setBoleta(String boleta) {
	this.boleta = boleta;
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

    public String getApellido_pat() {
	return apellido_pat;
    }

    public void setApellido_pat(String apellido_pat) {
	this.apellido_pat = apellido_pat;
    }

    public String getApellido_mat() {
	return apellido_mat;
    }

    public void setApellido_mat(String apellido_mat) {
	this.apellido_mat = apellido_mat;
    }

    public String getEscuela() {
	return escuela;
    }

    public void setEscuela(String escuela) {
	this.escuela = escuela;
    }

    public Float getPromedio() {
	return promedio;
    }

    public void setPromedio(Float promedio) {
	this.promedio = promedio;
    }

    public String getTurno() {
	return turno;
    }

    public void setTurno(String turno) {
	this.turno = turno;
    }

    public Date getFecha_nac() {
	return fecha_nac;
    }

    public void setFecha_nac(Date fecha_nac) {
	this.fecha_nac = fecha_nac;
    }

    public Integer getSemestre_min() {
	return semestre_min;
    }

    public void setSemestre_min(Integer semestre_min) {
	this.semestre_min = semestre_min;
    }

    public Integer getSemestre_max() {
	return semestre_max;
    }

    public void setSemestre_max(Integer semestre_max) {
	this.semestre_max = semestre_max;
    }

    public Integer getSemestre_inscrito() {
	return semestre_inscrito;
    }

    public void setSemestre_inscrito(Integer semestre_inscrito) {
	this.semestre_inscrito = semestre_inscrito;
    }

    public String getCalle() {
	return calle;
    }

    public void setCalle(String calle) {
	this.calle = calle;
    }

    public String getColonia() {
	return colonia;
    }

    public void setColonia(String colonia) {
	this.colonia = colonia;
    }

    public String getEstado() {
	return estado;
    }

    public void setEstado(String estado) {
	this.estado = estado;
    }

    public String getCp() {
	return cp;
    }

    public void setCp(String cp) {
	this.cp = cp;
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

    public String getPlan_estud() {
	return plan_estud;
    }

    public void setPlan_estud(String plan_estud) {
	this.plan_estud = plan_estud;
    }

    public String getE_mail() {
	return e_mail;
    }

    public void setE_mail(String e_mail) {
	this.e_mail = e_mail;
    }

    public String getTipo_alumno() {
	return tipo_alumno;
    }

    public void setTipo_alumno(String tipo_alumno) {
	this.tipo_alumno = tipo_alumno;
    }

    public Integer getInscrito() {
	return inscrito;
    }

    public void setInscrito(Integer inscrito) {
	this.inscrito = inscrito;
    }

    public String getSexo() {
	return sexo;
    }

    public void setSexo(String sexo) {
	this.sexo = sexo;
    }

    public Float getTot_creditos() {
	return tot_creditos;
    }

    public void setTot_creditos(Float tot_creditos) {
	this.tot_creditos = tot_creditos;
    }

    public Integer getModo_ingreso() {
	return modo_ingreso;
    }

    public void setModo_ingreso(Integer modo_ingreso) {
	this.modo_ingreso = modo_ingreso;
    }

    public Integer getPeriodo_escolar_ingreso() {
	return periodo_escolar_ingreso;
    }

    public void setPeriodo_escolar_ingreso(Integer periodo_escolar_ingreso) {
	this.periodo_escolar_ingreso = periodo_escolar_ingreso;
    }

    public Carrera getCarreraObj() {
	return carreraObj;
    }

    public void setCarreraObj(Carrera carreraObj) {
	this.carreraObj = carreraObj;
    }

    public Usuario getUsuario() {
	return usuario;
    }

    public void setUsuario(Usuario usuario) {
	this.usuario = usuario;
    }

    public BigDecimal getIdDae() {
	return idDae;
    }

    public void setIdDae(BigDecimal idDae) {
	this.idDae = idDae;
    }

    public Integer getOrigen() {
	return origen;
    }

    public void setOrigen(Integer origen) {
	this.origen = origen;
    }

    public Integer getEgresado() {
	return egresado;
    }

    public void setEgresado(Integer egresado) {
	this.egresado = egresado;
    }

    public int getReprobadas() {
        return reprobadas;
    }

    public void setReprobadas(int reprobadas) {
        this.reprobadas = reprobadas;
    }
    
    public Integer getCumple_carga_minima() {
	return cumple_carga_minima;
    }

    public void setCumple_carga_minima(Integer cumple_carga_minima) {
	this.cumple_carga_minima = cumple_carga_minima;
    }

//    public Integer getConfronta_dae() {
//	return confronta_dae;
//    }
//
//    public void setConfronta_dae(Integer confronta_dae) {
//	this.confronta_dae = confronta_dae;
//    }

//    public Date getFecha_ingreso() {
//	return fecha_ingreso;
//    }
//
//    public void setFecha_ingreso(Date fecha_ingreso) {
//	this.fecha_ingreso = fecha_ingreso;
//    }

    public Boolean getResultadoCarga() {
	return resultadoCarga;
    }

    public void setResultadoCarga(Boolean resultadoCarga) {
	this.resultadoCarga = resultadoCarga;
    }

    public Boolean getRegistradoSistema() {
	return registradoSistema;
    }

    public void setRegistradoSistema(Boolean registradoSistema) {
	this.registradoSistema = registradoSistema;
    }

    public Boolean getLlenoESE() {
	return llenoESE;
    }

    public void setLlenoESE(Boolean llenoESE) {
	this.llenoESE = llenoESE;
    }

    @Override
    public String toString() {
        return "AlumnoDAE{" + "idDae=" + idDae + ", boleta=" + boleta + ", curp=" + curp + ", nombre=" + nombre + ", apellido_pat=" + apellido_pat + ", apellido_mat=" + apellido_mat + ", escuela=" + escuela + ", promedio=" + promedio + ", turno=" + turno + ", fecha_nac=" + fecha_nac + ", semestre_min=" + semestre_min + ", semestre_max=" + semestre_max + ", semestre_inscrito=" + semestre_inscrito + ", calle=" + calle + ", colonia=" + colonia + ", estado=" + estado + ", cp=" + cp + ", carrera=" + carrera + ", especialidad=" + especialidad + ", plan_estud=" + plan_estud + ", e_mail=" + e_mail + ", tipo_alumno=" + tipo_alumno + ", inscrito=" + inscrito + ", sexo=" + sexo + ", tot_creditos=" + tot_creditos + ", modo_ingreso=" + modo_ingreso + ", periodo_escolar_ingreso=" + periodo_escolar_ingreso + ", usuario=" + usuario + ", origen=" + origen + ", egresado=" + egresado + ", materias reprobadas=" + reprobadas + ", cumple_carga_minima=" + cumple_carga_minima + ", carreraObj=" + carreraObj + ", resultadoCarga=" + resultadoCarga + ", registradoSistema=" + registradoSistema + ", llenoESE=" + llenoESE + '}';
    }
    
}
