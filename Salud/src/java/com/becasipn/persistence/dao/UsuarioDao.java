/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.Usuario;
import java.math.BigDecimal;

/**
 *
 * @author Patricia Benitez
 */
public interface UsuarioDao extends DaoBase<Usuario, BigDecimal> {

    public Usuario findById(String id);

}
