package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.Pattern;
import org.eclipse.persistence.annotations.Cache;

/**
 *
 * @author Victor Lozano
 */
@Entity
@Table(name = "ent_personal_administrativo")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class PersonalAdministrativo implements BaseEntity, Comparable, Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "entPersonalAdministrativoSeq")
    @SequenceGenerator(name = "entPersonalAdministrativoSeq", sequenceName = "ent_personal_administrativo_id", allocationSize = 1)
    private BigDecimal id;
    @Pattern(regexp = "^[a-zA-ZáéíóúñÁÉÍÓÚÑ\\s\\.]+$")
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String curp;
    @ManyToOne(fetch = FetchType.EAGER)
    private UnidadAcademica unidadAcademica;
    private String telefono;
    private String extension;
    private String correoElectronico;
    @ManyToOne(fetch = FetchType.LAZY)
    private Cargo cargo;
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuario;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaAlta;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaBaja;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuarioModifico;

    public PersonalAdministrativo() {
    }

    public PersonalAdministrativo(BigDecimal id) {
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

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public UnidadAcademica getUnidadAcademica() {
        return unidadAcademica;
    }

    public void setUnidadAcademica(UnidadAcademica unidadAcademica) {
        this.unidadAcademica = unidadAcademica;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Date getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Usuario getUsuarioModifico() {
        return usuarioModifico;
    }

    public void setUsuarioModifico(Usuario usuarioModifico) {
        this.usuarioModifico = usuarioModifico;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PersonalAdministrativo other = (PersonalAdministrativo) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "PersonalAdministrativo{" + id
                + "," + nombre
                + "," + curp
//                + "," + unidadAcademica
                + "," + telefono
                + "," + extension
                + "," + correoElectronico
                + "," + cargo
                + "," + usuario + '}';
    }

    @Override
    public int compareTo(Object o) {
        return (this.equals(o)) ? 0 : 1;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellidoPaterno + " " + apellidoMaterno;
    }
}