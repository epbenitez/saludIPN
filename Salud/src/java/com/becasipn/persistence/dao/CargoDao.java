package com.becasipn.persistence.dao;

import java.math.BigDecimal;

import com.becasipn.persistence.model.Cargo;

/**
 *
 * @author Victor Lozano
 */
public interface CargoDao extends DaoBase<Cargo, BigDecimal> {

    public Cargo findById(String id);
}
