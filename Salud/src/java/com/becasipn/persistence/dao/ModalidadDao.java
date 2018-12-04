package com.becasipn.persistence.dao;

import java.math.BigDecimal;

import com.becasipn.persistence.model.Modalidad;
import java.util.List;

/**
 *
 * @author User-03
 */
public interface ModalidadDao extends DaoBase<Modalidad, BigDecimal> {

    public List<Modalidad> findAllActive();

}
