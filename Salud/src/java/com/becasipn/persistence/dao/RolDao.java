package com.becasipn.persistence.dao;

import java.math.BigDecimal;

import com.becasipn.persistence.model.Rol;

/**
 *
 * @author Patricia Benitez
 */
public interface RolDao extends DaoBase<Rol, BigDecimal> {

    public Rol findById(String id);

    public String findByUsuario(BigDecimal id);
    
    public Rol findByName(String nombre);

}
