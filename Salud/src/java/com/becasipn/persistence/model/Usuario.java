package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.eclipse.persistence.annotations.Cache;

/**
 *
 * @author Patricia Benitez
 */
@Entity
@Table(name = "ent_usuario")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class Usuario implements BaseEntity, BaseBitacora, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UsrSeq")
    @SequenceGenerator(name = "UsrSeq", sequenceName = "ENT_USUARIO_ID_SEQ", allocationSize = 1)
    @Column(name = "id")
    private BigDecimal id;
    @Column(name = "usuario")
    private String usuario;

    @Column(name = "contrasenia")
    private String password;

    @Column(name = "activo")
    private boolean activo = true;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.PERSIST)
    private Set<UsuarioPrivilegio> privilegios;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaModificacion;

    public Usuario() {
        privilegios = new HashSet<>();
    }

    public Usuario(BigDecimal id) {
        privilegios = new HashSet<>();
        this.id = id;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    /**
     * Obtiene el valor de la variable usuario
     *
     * @return el valor de la variable usuario
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * Establece el valor de la variable usuario
     *
     * @param usuario nuevo valor de la variable usuario
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * Obtiene el valor de la variable password
     *
     * @return el valor de la variable password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece el valor de la variable password
     *
     * @param password nuevo valor de la variable password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtiene el valor de la variable activo
     *
     * @return el valor de la variable activo
     */
    public boolean isActivo() {
        return activo;
    }

    /**
     * Establece el valor de la variable activo
     *
     * @param activo nuevo valor de la variable activo
     */
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    /**
     * Obtiene el valor de la variable privilegios
     *
     * @return el valor de la variable privilegios
     */
    public Set<UsuarioPrivilegio> getPrivilegios() {
        return privilegios;
    }

    /**
     * Establece el valor de la variable privilegios
     *
     * @param privilegios nuevo valor de la variable privilegios
     */
    public void setPrivilegios(Set<UsuarioPrivilegio> privilegios) {
        this.privilegios = privilegios;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", usuario=" + usuario + ", password=" + password + ", activo=" + activo + ", privilegios=" + privilegios + ", fechaModificacion=" + fechaModificacion + '}';
    }
}
