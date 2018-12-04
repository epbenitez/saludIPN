package com.becasipn.persistence.model;

import com.becasipn.util.ElementoGeografico;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Representación abstracta del catálogo de municipio o delegacion en base de
 * datos
 * <br>Tabla: cat_muni_deleg
 *
 * @author Patricia Benitez
 */
@Entity
@Table(name = "cat_delegacion_municipio")
public class MunicipioDelegacion extends ElementoGeografico<LocalidadColonia> implements Serializable, Comparable<MunicipioDelegacion>, BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "catDelegacionMunicipioSeq")
    @SequenceGenerator(name = "catDelegacionMunicipioSeq", sequenceName = "cat_delegacion_municipio_id_se", allocationSize = 1)
    private BigDecimal id;

    private Integer clave;

    private String nombre;

    public MunicipioDelegacion() {
    }

    public MunicipioDelegacion(BigDecimal id) {
        this.id = id;
    }

    /**
     * Obtiene el valor de la variable id
     *
     * @return el valor de la variable id
     */
    public BigDecimal getId() {
        return id;
    }

    /**
     * Establece el valor de la variable id
     *
     * @param id nuevo valor de la variable id
     */
    public void setId(BigDecimal id) {
        this.id = id;
    }

    /**
     * Obtiene el valor de la variable clave
     *
     * @return el valor de la variable clave
     */
    public Integer getClave() {
        return clave;
    }

    /**
     * Establece el valor de la variable clave
     *
     * @param clave nuevo valor de la variable clave
     */
    public void setClave(Integer clave) {
        this.clave = clave;
    }

    /**
     * Obtiene el valor de la variable nombre
     *
     * @return el valor de la variable nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el valor de la variable nombre
     *
     * @param nombre nuevo valor de la variable nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MunicipioDelegacion other = (MunicipioDelegacion) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.clave == null) ? (other.clave != null) : !this.clave.equals(other.clave)) {
            return false;
        }
        if ((this.nombre == null) ? (other.nombre != null) : !this.nombre.equals(other.nombre)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 89 * hash + (this.clave != null ? this.clave.hashCode() : 0);
        hash = 89 * hash + (this.nombre != null ? this.nombre.hashCode() : 0);
        return hash;
    }

    public int compareTo(MunicipioDelegacion obj) {
        String nombreTmp = nombre == null ? null : nombre.toLowerCase();
        String objTmp = obj.getNombre() == null ? null : obj.getNombre().toLowerCase();

        return nombreTmp.compareTo(objTmp);
    }

    @Override
    public String toString() {
        return "MunicipioDelegacion{" + id + "," + clave + "," + nombre + '}';
    }

}
