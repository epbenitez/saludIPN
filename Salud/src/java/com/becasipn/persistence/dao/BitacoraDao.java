package com.becasipn.persistence.dao;

import java.math.BigDecimal;

import com.becasipn.persistence.model.Bitacora;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Patricia Benitez
 */
public interface BitacoraDao extends DaoBase<Bitacora, BigDecimal> {

    public List<Bitacora> findByParameters(Map objetos, Date fechaIni, Date fechaFin, String objectName, String adminUserId);

}
