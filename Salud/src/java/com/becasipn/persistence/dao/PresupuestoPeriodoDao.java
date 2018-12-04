package com.becasipn.persistence.dao;

import java.math.BigDecimal;

import com.becasipn.persistence.model.PresupuestoPeriodo;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez
 */
public interface PresupuestoPeriodoDao extends DaoBase<PresupuestoPeriodo, BigDecimal> {

    public List<PresupuestoPeriodo> existenPresupuestosAsociados(BigDecimal periodoId);
}
