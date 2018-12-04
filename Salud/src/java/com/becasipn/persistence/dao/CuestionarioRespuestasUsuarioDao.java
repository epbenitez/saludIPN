package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.CuestionarioRespuestas;
import com.becasipn.persistence.model.CuestionarioRespuestasUsuario;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.Usuario;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez
 */
public interface CuestionarioRespuestasUsuarioDao extends DaoBase<CuestionarioRespuestasUsuario, BigDecimal> {

	public List<CuestionarioRespuestasUsuario> getResultadosUsuario(BigDecimal cuestionarioId, BigDecimal usuario, BigDecimal periodoId);

	public void borrarESEporAlumno(BigDecimal usuarioId, BigDecimal periodoId);

	public Integer getActividadExtraPorUsuario(BigDecimal usuarioId);

	public CuestionarioRespuestas getRespuestaPreguntaActual(BigDecimal usuario, BigDecimal pregunta);

	public CuestionarioRespuestasUsuario getPreguntaUsuarioActual(BigDecimal usuario, BigDecimal pregunta);

	public CuestionarioRespuestasUsuario getPreguntaUsuarioActual(BigDecimal usuario, BigDecimal pregunta, BigDecimal respuesta);

	public CuestionarioRespuestasUsuario respuestaPregunta(BigDecimal usuarioId, BigDecimal preguntaId, BigDecimal cuestionarioId, BigDecimal periodoId);

	public Boolean existeRespuestaPregunta(BigDecimal usuarioId, BigDecimal preguntaId, BigDecimal respuestaId, BigDecimal cuestionarioId, BigDecimal periodoId);

	public CuestionarioRespuestasUsuario respuestaPreguntaCuestionarioPeriodo(BigDecimal usuarioId, BigDecimal preguntaId, BigDecimal respuestaId, BigDecimal cuestionarioId, BigDecimal periodoId);
        
        public List<CuestionarioRespuestasUsuario> getEgresos(Usuario usuario, Periodo p);
}
