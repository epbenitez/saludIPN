package com.becasipn.persistence.dao;

import java.math.BigDecimal;

import com.becasipn.persistence.model.VWPresupuestoPeriodo;

/**
 *
 * @author Patricia Benítez
 */
public interface VWPresupuestoPeriodoDao extends DaoBase<VWPresupuestoPeriodo, BigDecimal> {

    public VWPresupuestoPeriodo getPresupuesto(BigDecimal periodoId);
}
