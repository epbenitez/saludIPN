package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.Bancos;
import java.math.BigDecimal;

/**
 *
 * @author Tania G. Sánchez Manilla
 */
public interface BancosDao extends DaoBase<Bancos, BigDecimal> {
    public String bancoPorClave(String clave);
}