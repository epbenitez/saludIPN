/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.Cache;

/**
 *
 * @author Tania G. Sánchez
 */
@Entity
@Table(name = "ent_cuestionario_pregunta_resp")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class CuestionarioPreguntaRespuesta implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(generator = "entCuestionarioPreguntaRespSeq")
    @SequenceGenerator(name = "entCuestionarioPreguntaRespSeq", sequenceName = "ent_cuestionario_pregunta_re_1", allocationSize = 1)
    private BigDecimal id;
    private CuestionarioPreguntas pregunta;
    private List<CuestionarioRespuestas> respuestas;
    private CuestionarioSeccion seccion;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public CuestionarioPreguntas getPregunta() {
        return pregunta;
    }

    public void setPregunta(CuestionarioPreguntas pregunta) {
        this.pregunta = pregunta;
    }

    public List<CuestionarioRespuestas> getRespuestas() {
        return respuestas;
    }

    public void setRespuesta(List<CuestionarioRespuestas> respuesta) {
        this.respuestas = respuesta;
    }

    public CuestionarioSeccion getSeccion() {
        return seccion;
    }

    public void setSeccion(CuestionarioSeccion seccion) {
        this.seccion = seccion;
    }

    @Override
    public String toString() {
        return "CuestionarioPreguntaRespuesta{" + "id=" + id + ", pregunta=" + pregunta + ", respuesta=" + respuestas + ", seccion=" + seccion + '}';
    }
}
