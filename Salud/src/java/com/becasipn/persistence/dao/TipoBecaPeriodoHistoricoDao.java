package com.becasipn.persistence.dao;

import java.math.BigDecimal;

import com.becasipn.persistence.model.TipoBecaPeriodoHistorico;

/**
 *
 * @author Usre-05
 */
public interface TipoBecaPeriodoHistoricoDao extends DaoBase<TipoBecaPeriodoHistorico, BigDecimal> {
    public void borrarBitacoraTBP(BigDecimal tbpId);
}
