package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.Cache;

@Entity
@Table(name = "rmm_menu_roles")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class RelacionMenuRoles implements Serializable, BaseEntity {

    @Id
    private BigDecimal id;

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "MENU_ID", referencedColumnName = "ID")
    })
    private Menu menu;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "ROL_ID", referencedColumnName = "ID")
    })
    private Rol rol;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "AGRUPADOR_ID", referencedColumnName = "ID")
    })
    private Agrupador agrupador;

    private boolean activo;

    private Integer orden;

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Agrupador getAgrupador() {
        return agrupador;
    }

    public void setAgrupador(Agrupador agrupador) {
        this.agrupador = agrupador;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RelacionMenuRoles other = (RelacionMenuRoles) obj;
        if (this.menu != other.menu && (this.menu == null || !this.menu.equals(other.menu))) {
            return false;
        }
        if (this.rol != other.rol && (this.rol == null || !this.rol.equals(other.rol))) {
            return false;
        }
        if (this.agrupador != other.agrupador && (this.agrupador == null || !this.agrupador.equals(other.agrupador))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    @Override
    public String toString() {
        return "RelacionMenuRoles{" + id + ","
                + (menu == null ? "-" : menu.getNombre()) + ","
                + (rol == null ? "-" : rol.getDescripcion()) + ","
                + (agrupador == null ? "-" : agrupador.getDescripcion()) + ","
                + activo + '}';
    }

}
