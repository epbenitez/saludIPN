package com.becasipn.persistence.dao;

import java.math.BigDecimal;

import com.becasipn.persistence.model.VWOrdenDeposito;
import java.util.List;

/**
 *
 * @author Victor Lozano
 */
public interface VWOrdenDepositoDao extends DaoBase<VWOrdenDeposito, BigDecimal> {

    public List<VWOrdenDeposito> findOrdenesDepositoPeriodoActivo();
}
