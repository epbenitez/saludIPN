package com.becasipn.persistence.dao;

import java.math.BigDecimal;

import com.becasipn.persistence.model.ProcesoProgramaBeca;
import java.util.List;

/**
 *
 * @author Usre-05
 */
public interface ProcesoProgramaBecaDao extends DaoBase<ProcesoProgramaBeca, BigDecimal> {

	public Boolean existe(String id, BigDecimal procesoId);

	public Boolean existe(BigDecimal programaBecaId, BigDecimal procesoId);

	public List<ProcesoProgramaBeca> getByProceso(BigDecimal procesoId);

	public List<ProcesoProgramaBeca> noUsadas(String ids, BigDecimal procesoId);

	public Boolean soloManutencion(BigDecimal proceso);
}
