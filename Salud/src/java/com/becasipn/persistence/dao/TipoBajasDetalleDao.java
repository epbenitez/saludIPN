package com.becasipn.persistence.dao;

import java.math.BigDecimal;

import com.becasipn.persistence.model.TipoBajasDetalle;
import java.util.List;

/**
 *
 * @author Usre-05
 */
public interface TipoBajasDetalleDao extends DaoBase<TipoBajasDetalle, BigDecimal> {
        public List<TipoBajasDetalle> findAllAct();
}
