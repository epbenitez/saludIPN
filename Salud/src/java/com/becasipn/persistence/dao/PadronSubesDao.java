package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.ConvocatoriaSubes;
import com.becasipn.persistence.model.PadronSubes;
import com.becasipn.persistence.model.Periodo;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Rafael Cardenas
 */
public interface PadronSubesDao extends DaoBase<PadronSubes, BigDecimal> {

	public PadronSubes getByCurp(String curp);

	public void updateTransporte(String curp, String folioTransporte, String estatusTransporte);

	public void updateEstadoDomicilio();

	public void excluirOtorgamiento(ConvocatoriaSubes convocatoriaSubes, Periodo periodo, Integer op);

	public void marcarAlumnos(ConvocatoriaSubes convocatoriaSubes, Periodo periodo, Integer op);

	public Boolean actualizarCurpAlumnoB(ConvocatoriaSubes convocatoriaSubes, Periodo periodo);

	public void actualizarCurpAlumno(ConvocatoriaSubes convocatoria, Periodo periodo);

	public List<PadronSubes> listadoActualizar(ConvocatoriaSubes convocatoriaSubes, Periodo periodo);

	public List<PadronSubes> listadoInsertar(ConvocatoriaSubes convocatoriaSubes, Periodo periodo);

	public List<PadronSubes> listadoESE(ConvocatoriaSubes convocatoriaSubes, Periodo periodo);

	public List<PadronSubes> listadoConOtorgamiento(ConvocatoriaSubes convocatoriaSubes, Periodo periodo);

	public Boolean existe(ConvocatoriaSubes convocatoriaSubes, Periodo periodo);

	public PadronSubes getAlumnoSUBES(BigDecimal alumnoId);

	public Boolean existeAlumnoCurpPeriodo(String curp, Periodo periodo);

	public Boolean existeAlumnoFolioPeriodo(String folioSubes, Periodo periodo);

	public PadronSubes alumnoCurpPeriodo(String curp, Periodo periodo);

	public Boolean existeAlumnoFolioPeriodoSolicitud(String folioSubes, Periodo periodo, BigDecimal cuestionarioId);

	public List<PadronSubes> cargaDeTabla(Integer tipo);

	public Integer getCargados(BigDecimal periodoId);

	public Integer getSolicitudesCreadas(BigDecimal periodoId,BigDecimal cuestionarioId);
        
        public List<Object[]> resumenProceso(BigDecimal periodoId, BigDecimal convocatoriaId);
}
