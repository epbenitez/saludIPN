package com.becasipn.persistence.model;

import java.math.BigDecimal;

/**
 *
 * @author Augusto I. Hernández Ruiz
 */
public class EstadisticaCuestionarios {
    
    public BigDecimal cuestionarioId;
    
    private BigDecimal preguntaId;
    private String pregunta;
    private BigDecimal respuestaId;
    private String respuesta;
    private long totalRespuesta;
    private BigDecimal seccion;

    public EstadisticaCuestionarios() {
    }

    public BigDecimal getCuestionarioId() {
        return cuestionarioId;
    }

    public void setCuestionarioId(BigDecimal cuestionarioId) {
        this.cuestionarioId = cuestionarioId;
    }


    public BigDecimal getPreguntaId() {
        return preguntaId;
    }

    public void setPreguntaId(BigDecimal preguntaId) {
        this.preguntaId = preguntaId;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public BigDecimal getRespuestaId() {
        return respuestaId;
    }

    public void setRespuestaId(BigDecimal respuestaId) {
        this.respuestaId = respuestaId;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public long getTotalRespuesta() {
        return totalRespuesta;
    }

    public void setTotalRespuesta(long totalRespuesta) {
        this.totalRespuesta = totalRespuesta;
    }

    public BigDecimal getSeccion() {
        return seccion;
    }

    public void setSeccion(BigDecimal seccion) {
        this.seccion = seccion;
    }

    @Override
    public String toString() {
        return "EstadisticaCuestionarios{" + "cuestionarioId=" + cuestionarioId + ", preguntaId=" + preguntaId + ", pregunta=" + pregunta + ", respuestaId=" + respuestaId + ", respuesta=" + respuesta + ", totalRespuesta=" + totalRespuesta + ", seccion=" + seccion + '}';
    }
    
}
