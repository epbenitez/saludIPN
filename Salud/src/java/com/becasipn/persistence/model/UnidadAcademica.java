/**
 * SISTEMA INFORMÁTICO DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE
 * SERVICIOS ESTUDIANTILES 2015
 *
 */
package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.Cache;

/**
 *
 * @author Patricia Benitez
 */
@Entity
@Table(name = "cat_unidad_academica")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class UnidadAcademica implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(generator = "UniAcSeq")
    @SequenceGenerator(name = "UniAcSeq", sequenceName = "CAT_UNIDAD_ACADEMICA_ID_SEQ", allocationSize = 1)
    private BigDecimal id;
    private String nombre;
    private String nombreCorto;
    private String clave;
    @ManyToOne(fetch = FetchType.LAZY)
    private Nivel nivel;
    @ManyToOne(fetch = FetchType.LAZY)
    private AreasConocimiento areasConocimiento;
    private String direccion;
    private String telefono;
    @ManyToOne(fetch = FetchType.LAZY)
    private PersonalAdministrativo contactoPrincipal;
    @ManyToOne(fetch = FetchType.LAZY)
    private PersonalAdministrativo contactoAlterno;
    private Float latitud;
    private Float longitud;
    private Boolean dependencia;
    
    public UnidadAcademica() {
    }
    
    public UnidadAcademica(BigDecimal id) {
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

    public String getNombreCorto() {
        return nombreCorto;
    }

    public void setNombreCorto(String nombreCorto) {
        this.nombreCorto = nombreCorto;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public AreasConocimiento getAreasConocimiento() {
        return areasConocimiento;
    }

    public void setAreasConocimiento(AreasConocimiento areasConocimiento) {
        this.areasConocimiento = areasConocimiento;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public PersonalAdministrativo getContactoPrincipal() {
        return contactoPrincipal;
    }

    public void setContactoPrincipal(PersonalAdministrativo contactoPrincipal) {
        this.contactoPrincipal = contactoPrincipal;
    }

    public PersonalAdministrativo getContactoAlterno() {
        return contactoAlterno;
    }

    public void setContactoAlterno(PersonalAdministrativo contactoAlterno) {
        this.contactoAlterno = contactoAlterno;
    }

    public Float getLatitud() {
        return latitud;
    }

    public void setLatitud(Float latitud) {
        this.latitud = latitud;
    }

    public Float getLongitud() {
        return longitud;
    }

    public void setLongitud(Float longitud) {
        this.longitud = longitud;
    }

    public String getNombreSemiLargo() {
        if (id.equals(new BigDecimal(19))) {
            return nombreCorto + " - UNIDAD ZACATECAS";
        } else if (nivel.getClave().equals("NMS")) {
            String[] split = nombre.split("\\)");
            return (nombreCorto + " - " + split[1]).replaceAll("”", "");
        } else {
            return nombreCorto;
        }
    }

    public Boolean getDependencia() {
        return dependencia;
    }

    public void setDependencia(Boolean dependencia) {
        this.dependencia = dependencia;
    }

    @Override
    public String toString() {
        return "UnidadAcademica{" + "id=" + id + ", nombre=" + nombre + ", nombreCorto=" + nombreCorto + ", clave=" + clave + ", nivel=" + nivel +'}';
    }
}