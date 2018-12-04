package com.becasipn.persistence.dao;

import java.math.BigDecimal;

import com.becasipn.persistence.model.CuestionarioRespuestas;
import java.util.List;

/**
 *
 * @author Patricia Benitez
 */
public interface CuestionarioRespuestasDao extends DaoBase<CuestionarioRespuestas, BigDecimal> {
    
    public List<CuestionarioRespuestas> respuestasPregunta(BigDecimal preguntaId);
    
    public CuestionarioRespuestas respuesta(BigDecimal preguntaId,String respuesta);
}
