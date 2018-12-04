package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.CuestionarioPreguntaRespuesta;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez
 */
public interface CuestionarioPreguntaRespuestaDao extends DaoBase<CuestionarioPreguntaRespuesta, BigDecimal> {

    public List<CuestionarioPreguntaRespuesta> findByCuestionario(BigDecimal cuestionarioId, BigDecimal nivelId);

    public Integer totalPreguntas(BigDecimal cuestionarioId);
    
    public Integer preguntasContestadas(BigDecimal cuestionarioId, BigDecimal periodoId, BigDecimal usuarioId);
    
    public List<Object[]> estadisticaCuestionario(BigDecimal periodoId, BigDecimal cuestionarioId);
}