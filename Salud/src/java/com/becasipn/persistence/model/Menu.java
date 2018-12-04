package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.Cache;

@Entity
@Table(name = "ent_menu")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class Menu implements Serializable, BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id
    private BigDecimal id;

    private String url;

    private String nombre;

    private String icono;

    private String palabrasClave;

    /**
     * Obtiene el valor de id
     *
     * @return el valor de id
     */
    public BigDecimal getId() {
        return id;
    }

    /**
     * Establece el valor de id
     *
     * @param id nuevo valor de id
     */
    public void setId(BigDecimal id) {
        this.id = id;
    }

    /**
     * Obtiene el valor de url
     *
     * @return el valor de url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Establece el valor de url
     *
     * @param url nuevo valor de url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public String getPalabrasClave() {
        return palabrasClave;
    }

    public void setPalabrasClave(String palabrasClave) {
        this.palabrasClave = palabrasClave;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Menu other = (Menu) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Menu{" + "id=" + id + ", url=" + url + ", nombre=" + nombre + ", icono=" + icono + ", palabrasClave=" + palabrasClave + '}';
    }

}
