package com.becasipn.persistence.dao;

import java.math.BigDecimal;

import com.becasipn.persistence.model.Periodo;

/**
 *
 * @Tania G. Sánchez
 */
public interface PeriodoDao extends DaoBase<Periodo, BigDecimal> {

    public Periodo getPeriodoActivo();

}
