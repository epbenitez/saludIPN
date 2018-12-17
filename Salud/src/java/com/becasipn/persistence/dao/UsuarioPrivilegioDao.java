/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.becasipn.persistence.dao;

import java.math.BigDecimal;

import com.becasipn.persistence.model.UsuarioPrivilegio;

/**
 *
 * @author Patricia Benitez
 */
public interface UsuarioPrivilegioDao extends DaoBase<UsuarioPrivilegio, BigDecimal> {

    public UsuarioPrivilegio findByUsuario(BigDecimal usuarioId);
}
