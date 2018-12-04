package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.TipoProceso;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez
 */
public interface TipoProcesoDao extends DaoBase<TipoProceso, BigDecimal> {

	public List<TipoProceso> getProcesosActivosPeriodo(BigDecimal periodoId);

	public List<TipoProceso> procesoEnUso(BigDecimal procesoId);

	public List<TipoProceso> procesoByMovimiento(BigDecimal movimientoId);

	public TipoProceso procesoAutoByMovimiento(BigDecimal movimientoId);

	public TipoProceso procesoAutoSubes();
}
