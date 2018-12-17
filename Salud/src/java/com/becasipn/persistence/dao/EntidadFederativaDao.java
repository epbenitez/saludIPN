package com.becasipn.persistence.dao;

import java.math.BigDecimal;

import com.becasipn.persistence.model.EntidadFederativa;
import java.util.List;

/**
 * Definición de las operaciones CRUD de las entidades federativas
 *
 * @author Patricia Benitez
 * @version $Rev: 801 $
 * @since 1.0
 */
public interface EntidadFederativaDao extends DaoBase<EntidadFederativa, BigDecimal> {

    public List<EntidadFederativa> findTotal();

}
