package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.Cache;

/**
 * Representación abstracta de una direccion en base de datos
 * <br>Tabla: ent_direccion
 *
 * @author Patricia Benitez
 * @version $Rev: 1165 $
 * @since 1.0
 */
@Entity
@Table(name = "ent_direccion")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class Direccion implements Serializable, BaseEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "entDireccionSeq")
    @SequenceGenerator(name = "entDireccionSeq", sequenceName = "ent_direccion_id_seq", allocationSize = 1)
    private BigDecimal id;
    @OneToOne(fetch = FetchType.LAZY)
    private RelacionGeografica relacionGeografica;
    private String calle;
    private String codigoPostal;
    private String numeroInterior;
    private String numeroExterior;
    @ManyToOne(fetch = FetchType.LAZY)
    private InegiLocalidad inegiLocalidad;
    @ManyToOne(fetch = FetchType.LAZY)
    private InegiTipoAsentamiento inegiTipoAsentamiento;
    @ManyToOne(fetch = FetchType.LAZY)
    private InegiTipoVialidad inegiTipoVialidad;

    public Direccion() {
    }

    /**
     * Obtiene el valor de id
     *
     * @return el valor de id
     */
    @Override
    public BigDecimal getId() {
        return id;
    }

    /**
     * Establece el valor de id
     *
     * @param id nuevo valor de id
     */
    @Override
    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getNumeroExterior() {
        return numeroExterior;
    }

    public void setNumeroExterior(String numeroExterior) {
        this.numeroExterior = numeroExterior;
    }

    public String getNumeroInterior() {
        return numeroInterior;
    }

    public void setNumeroInterior(String numeroInterior) {
        this.numeroInterior = numeroInterior;
    }

    public RelacionGeografica getRelacionGeografica() {
        return relacionGeografica;
    }

    public void setRelacionGeografica(RelacionGeografica relacionGeografica) {
        this.relacionGeografica = relacionGeografica;
    }

    public InegiLocalidad getInegiLocalidad() {
        return inegiLocalidad;
    }

    public void setInegiLocalidad(InegiLocalidad inegiLocalidad) {
        this.inegiLocalidad = inegiLocalidad;
    }

    public InegiTipoAsentamiento getInegiTipoAsentamiento() {
        return inegiTipoAsentamiento;
    }

    public void setInegiTipoAsentamiento(InegiTipoAsentamiento inegiTipoAsentamiento) {
        this.inegiTipoAsentamiento = inegiTipoAsentamiento;
    }

    public InegiTipoVialidad getInegiTipoVialidad() {
        return inegiTipoVialidad;
    }

    public void setInegiTipoVialidad(InegiTipoVialidad inegiTipoVialidad) {
        this.inegiTipoVialidad = inegiTipoVialidad;
    }

    @Override
    public String toString() {
        return "Direccion{" + "id=" + id + "relacionGeografica=" + relacionGeografica
                + "calle=" + calle + "codigoPostal=" + codigoPostal
                + "numeroInterior=" + numeroInterior + "numeroExterior=" + numeroExterior + '}';
    }
}
