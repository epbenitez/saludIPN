package com.becasipn.persistence.dao;

import java.math.BigDecimal;
import com.becasipn.persistence.model.BecaUniversal;

/**
 *
 * @author SISTEMAS
 */
public interface BecaUniversalDao extends DaoBase<BecaUniversal, BigDecimal> {
    public Boolean existeEnPadron(String boleta);
    public BecaUniversal getByBoleta(String boleta);
}
