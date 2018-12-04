package com.becasipn.persistence.dao;

import java.math.BigDecimal;

import com.becasipn.persistence.model.BitacoraEstatusProceso;
import java.util.List;

/**
 *
 * @author Usre-05
 */
public interface BitacoraEstatusProcesoDao extends DaoBase<BitacoraEstatusProceso, BigDecimal> {

    public List<BitacoraEstatusProceso> getByProceso(BigDecimal noBoleta);
}
