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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.eclipse.persistence.annotations.Cache;

/**
 *
 * @author Patricia Benitez
 */
@Entity
@Table(name = "rmm_cuestionario_pregunta_resp")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class CuestionarioRespuestasUsuario implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(generator = "rmmCuestionarioPreguntaRespSeq")
    @SequenceGenerator(name = "rmmCuestionarioPreguntaRespSeq", sequenceName = "rmm_cuestionario_pregunta_re_1", allocationSize = 1)
    private BigDecimal id;
    @OneToOne
    private Usuario usuario;
    @ManyToOne(fetch = FetchType.LAZY)
    private CuestionarioPreguntas pregunta;
    @ManyToOne(fetch = FetchType.LAZY)
    private CuestionarioRespuestas respuesta;
    @ManyToOne(fetch = FetchType.LAZY)
    private Cuestionario cuestionario;
    private String respuestaAbierta;
    @Transient
    private CuestionarioSeccion seccion;
    @ManyToOne(fetch = FetchType.LAZY)
    private Periodo periodo;

    @Override
    public BigDecimal getId() {
        return id;
    }

    @Override
    public void setId(BigDecimal id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public CuestionarioPreguntas getPregunta() {
        return pregunta;
    }

    public void setPregunta(CuestionarioPreguntas pregunta) {
        this.pregunta = pregunta;
    }

    public CuestionarioRespuestas getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(CuestionarioRespuestas respuesta) {
        this.respuesta = respuesta;
    }

    public Cuestionario getCuestionario() {
        return cuestionario;
    }

    public void setCuestionario(Cuestionario cuestionario) {
        this.cuestionario = cuestionario;
    }

    public String getRespuestaAbierta() {
        return respuestaAbierta;
    }

    public void setRespuestaAbierta(String respuestaOtra) {
        this.respuestaAbierta = respuestaOtra;
    }

    public CuestionarioSeccion getSeccion() {
        return seccion;
    }

    public void setSeccion(CuestionarioSeccion seccion) {
        this.seccion = seccion;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    @Override
    public String toString() {
        return "CuestionarioRespuestasUsuario{" + "id=" + id + ", usuario=" + usuario + ", pregunta=" + pregunta + ", respuesta=" + respuesta + ", cuestionario=" + cuestionario + ", respuestaAbierta=" + respuestaAbierta + ", seccion=" + seccion + ", periodo=" + periodo + '}';
    }

    public CuestionarioRespuestasUsuario(Usuario usuario, CuestionarioPreguntas pregunta, CuestionarioRespuestas respuesta, Cuestionario cuestionario, Periodo periodo) {
        this.usuario = usuario;
        this.pregunta = pregunta;
        this.respuesta = respuesta;
        this.cuestionario = cuestionario;
        this.periodo = periodo;
    }

    public CuestionarioRespuestasUsuario() {
    }
    
}
