package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.eclipse.persistence.annotations.Cache;

/**
 *
 * @author Tania G. Sánchez
 */
@Entity
@Table(name = "ent_alumno_datos_bancarios")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class AlumnoDatosBancarios implements Serializable, BaseEntity, BaseBitacora {
    private static final long serialVersionUID = 1L;
    @Id
    private BigDecimal id;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String correoElectronico;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaDeNacimiento;
    private String ocupacion;
    @ManyToOne(fetch = FetchType.LAZY)
    private Alumno alumno;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Direccion direccion;
    @ManyToOne(fetch = FetchType.LAZY)
    private EstadoCivil estadoCivil;
    //Dirección
    private String calle;
    private String numeroInterior;
    private String numeroExterior;
    private String colonia;
    private String municipio;
    private String estado;
    private String codigoPostal;
    @ManyToOne(fetch = FetchType.LAZY)
    private Genero genero;
    private Boolean vigente;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    private Boolean tutor;
    private Boolean cuentaExterna;
    private Integer intentos;

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

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public Date getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(Date fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public EstadoCivil getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(EstadoCivil estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumeroInterior() {
        return numeroInterior;
    }

    public void setNumeroInterior(String numeroInterior) {
        this.numeroInterior = numeroInterior;
    }

    public String getNumeroExterior() {
        return numeroExterior;
    }

    public void setNumeroExterior(String numeroExterior) {
        this.numeroExterior = numeroExterior;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public Boolean getVigente() {
        return vigente;
    }

    public void setVigente(Boolean vigente) {
        this.vigente = vigente;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Boolean getTutor() {
        return tutor;
    }

    public void setTutor(Boolean tutor) {
        this.tutor = tutor;
    }

    public Boolean getCuentaExterna() {
        return cuentaExterna;
    }

    public void setCuentaExterna(Boolean cuentaExterna) {
        this.cuentaExterna = cuentaExterna;
    }

    public Integer getIntentos() {
        return intentos;
    }

    public void setIntentos(Integer intentos) {
        this.intentos = intentos;
    }

    @Override
    public String toString() {
        return "AlumnoDatosBancarios{" + "id=" + id + ", nombre=" + nombre + ", apellidoPaterno=" + apellidoPaterno + ", apellidoMaterno=" + apellidoMaterno + ", correoElectronico=" + correoElectronico + ", fechaDeNacimiento=" + fechaDeNacimiento + ", ocupacion=" + ocupacion + ", alumno=" + alumno.getBoleta() + ", direccion=" + (direccion==null?"":direccion.getId()) + ", estadoCivil=" + estadoCivil + ", calle=" + calle + ", numeroInterior=" + numeroInterior + ", numeroExterior=" + numeroExterior + ", colonia=" + colonia + ", municipio=" + municipio + ", estado=" + estado + ", codigoPostal=" + codigoPostal + ", genero=" + genero + ", vigente=" + vigente + ", fechaModificacion=" + fechaModificacion + ", tutor=" + tutor + '}';
    }
}