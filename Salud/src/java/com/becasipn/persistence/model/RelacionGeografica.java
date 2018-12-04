package com.becasipn.persistence.model;

import org.eclipse.persistence.annotations.Cache;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Representación abstracta de una relacion geografica
 * (País-Entidad-Localidad-cp) en base de datos
 * <br>Tabla: rmm_pais_enfed_del_col
 *
 * @author Patricia Benitez
 * @version $Rev: 1165 $
 * @since 1.0
 */
@Entity
@Table(name = "rmm_estado_deleg_col")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class RelacionGeografica implements Serializable, Comparable, BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "rmmEstadoDelegColSeq")
    @SequenceGenerator(name = "rmmEstadoDelegColSeq", sequenceName = "rmm_estado_deleg_col_id_seq", allocationSize = 1)
    private BigDecimal id;

//    @ManyToOne
//    private Pais pais;
    @ManyToOne(fetch = FetchType.LAZY)
    private EntidadFederativa estado;

    @ManyToOne(fetch = FetchType.LAZY)
    private MunicipioDelegacion municipio;

    @ManyToOne(fetch = FetchType.LAZY)
    private LocalidadColonia colonia;

    public RelacionGeografica() {

    }

    public RelacionGeografica(BigDecimal id) {
        this.id = id;
    }

    /**
     * Obtiene el valor de la variable id
     *
     * @return el valor de la variable id
     */
    @Override
    public BigDecimal getId() {
        return id;
    }

    /**
     * Establece el valor de la variable id
     *
     * @param id nuevo valor de la variable id
     */
    @Override
    public void setId(BigDecimal id) {
        this.id = id;
    }

    /**
     * Obtiene el valor de la variable pais
     *
     * @return el valor de la variable pais
     */
//    public Pais getPais() {
//        return pais;
//    }
    /**
     * Establece el valor de la variable pais
     *
     * @param pais nuevo valor de la variable pais
     */
//    public void setPais(Pais pais) {
//        this.pais = pais;
//    }
    /**
     * Obtiene el valor de la variable estado
     *
     * @return el valor de la variable estado
     */
    public EntidadFederativa getEstado() {
        return estado;
    }

    /**
     * Establece el valor de la variable estado
     *
     * @param estado nuevo valor de la variable estado
     */
    public void setEstado(EntidadFederativa estado) {
        this.estado = estado;
    }

    /**
     * Obtiene el valor de la variable municipio
     *
     * @return el valor de la variable municipio
     */
    public MunicipioDelegacion getMunicipio() {
        return municipio;
    }

    /**
     * Establece el valor de la variable municipio
     *
     * @param municipio nuevo valor de la variable municipio
     */
    public void setMunicipio(MunicipioDelegacion municipio) {
        this.municipio = municipio;
    }

    /**
     * Obtiene el valor de la variable colonia
     *
     * @return el valor de la variable colonia
     */
    public LocalidadColonia getColonia() {
        return colonia;
    }

    /**
     * Establece el valor de la variable colonia
     *
     * @param colonia nuevo valor de la variable colonia
     */
    public void setColonia(LocalidadColonia colonia) {
        this.colonia = colonia;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RelacionGeografica other = (RelacionGeografica) obj;
//        if (this.pais != other.pais && (this.pais == null || !this.pais.equals(other.pais))) {
//            return false;
//        }
        if (this.estado != other.estado && (this.estado == null || !this.estado.equals(other.estado))) {
            return false;
        }
        if (this.municipio != other.municipio && (this.municipio == null || !this.municipio.equals(other.municipio))) {
            return false;
        }
        if (this.colonia != other.colonia && (this.colonia == null || !this.colonia.equals(other.colonia))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
//        hash = 83 * hash + (this.pais != null ? this.pais.hashCode() : 0);
        hash = 83 * hash + (this.estado != null ? this.estado.hashCode() : 0);
        hash = 83 * hash + (this.municipio != null ? this.municipio.hashCode() : 0);
        hash = 83 * hash + (this.colonia != null ? this.colonia.hashCode() : 0);
        return hash;
    }

    @Override
    public int compareTo(Object o) {
        return (this.equals(o)) ? 0 : 1;
    }

    @Override
    public String toString() {
        return "RelacionGeografica{" + "id=" + id + "estado=" + estado + "municipio=" + municipio + "colonia=" + colonia + '}';
    }

}
