/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.becasipn.persistence.dao;

import java.math.BigDecimal;

import com.becasipn.persistence.model.Usuario;
import com.becasipn.persistence.model.UsuarioPrivilegio;
import java.util.List;

/**
 *
 * @author Patricia Benitez
 */
public interface UsuarioPrivilegioDao extends DaoBase<UsuarioPrivilegio, BigDecimal> {

    public List<UsuarioPrivilegio> getPrivilegiosByUser(Usuario u);

    public void borraPrivilegios(Usuario u);

    public UsuarioPrivilegio findByUsuario(BigDecimal usuarioId);
}
