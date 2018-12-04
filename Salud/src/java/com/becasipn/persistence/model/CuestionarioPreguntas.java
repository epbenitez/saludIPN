/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
import javax.persistence.Transient;
import org.eclipse.persistence.annotations.Cache;

/**
 *
 * @author Patricia Benitez
 */
@Entity
@Table(name = "ent_cuestionario_pregunta")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class CuestionarioPreguntas implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(generator = "entCuestionarioPreguntaSeq")
    @SequenceGenerator(name = "entCuestionarioPreguntaSeq", sequenceName = "ent_cuestionario_pregunta_id_s", allocationSize = 1)
    private BigDecimal id;
    @ManyToOne(fetch = FetchType.LAZY)
    private CuestionarioPreguntaTipo cuestionarioPreguntaTipo;
    private String nombre;
    private Integer orden;
    private Integer activo;
    private Integer padre;
    private CuestionarioRespuestas respuestaDependiente;
    @Transient
    private String nombreSinHTML;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Integer getActivo() {
        return activo;
    }

    public void setActivo(Integer activo) {
        this.activo = activo;
    }

    public CuestionarioPreguntaTipo getCuestionarioPreguntaTipo() {
        return cuestionarioPreguntaTipo;
    }

    public void setCuestionarioPreguntaTipo(CuestionarioPreguntaTipo cuestionarioPreguntaTipo) {
        this.cuestionarioPreguntaTipo = cuestionarioPreguntaTipo;
    }

    public String getNombreSinHTML() {
        return nombre.replaceAll("<strong>", "").replaceAll("</strong>", "").replaceAll("</br>\\(Selecciona todos los que haya\\)", "").replaceAll("<br>", "");
    }

    public void setNombreSinHTML(String nombreSinHTML) {
        this.nombreSinHTML = nombreSinHTML;
    }

    @Override
    public String toString() {
        return "CuestionarioPreguntas{" + "id=" + id + ", cuestionarioPreguntaTipo=" + cuestionarioPreguntaTipo + ", nombre=" + nombre + ", orden=" + orden + ", activo=" + activo + '}';
    }

    public CuestionarioPreguntas(BigDecimal id) {
        this.id = id;
    }

    public CuestionarioPreguntas() {
    }

    public Integer getPadre() {
        return padre;
    }

    public void setPadre(Integer padre) {
        this.padre = padre;
    }

    public CuestionarioRespuestas getRespuestaDependiente() {
        return respuestaDependiente;
    }

    public void setRespuestaDependiente(CuestionarioRespuestas respuestaDependiente) {
        this.respuestaDependiente = respuestaDependiente;
    }

}
