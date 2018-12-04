package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.Cache;

@Entity
@Table(name = "rmm_rol_usuario")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class UsuarioPrivilegio implements Serializable, BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "catUsuarioPrivilegioSeq")
    @SequenceGenerator(name = "catUsuarioPrivilegioSeq", sequenceName = "RMM_ROL_USUARIO_ID_SEQ", allocationSize = 1)
    private BigDecimal id;

    @JoinColumn(name = "rol_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Rol privilegio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public UsuarioPrivilegio() {
    }

    public UsuarioPrivilegio(Rol rol, Usuario usuario) {
        privilegio = rol;
        this.usuario = usuario;
    }

    public Rol getPrivilegio() {
        return privilegio;
    }

    public void setPrivilegio(Rol privilegio) {
        this.privilegio = privilegio;
    }

    /**
     * Obtiene el valor de la variable usuario.
     *
     * @return el valor de la variable usuario.
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Establece el valor de la variable usuario.
     *
     * @param usuario nuevo valor de la variable usuario.
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UsuarioPrivilegio other = (UsuarioPrivilegio) obj;
        if ((this.usuario.getId() != other.usuario.getId())
                || (this.privilegio.getId() == null ? other.privilegio.getId() != null : !this.privilegio.getId().equals(other.privilegio.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UsuarioPrivilegio{" + privilegio + ","
                + (usuario == null ? "-" : usuario.getUsuario()) + '}';
//        return "";
    }

    @Override
    public BigDecimal getId() {
        return id;
    }

    @Override
    public void setId(BigDecimal id) {
        this.id = id;
    }

}
