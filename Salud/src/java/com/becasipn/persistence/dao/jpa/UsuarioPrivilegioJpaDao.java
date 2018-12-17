/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.UsuarioPrivilegioDao;
import com.becasipn.persistence.model.UsuarioPrivilegio;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Patricia Benitez
 */
public class UsuarioPrivilegioJpaDao extends JpaDaoBase<UsuarioPrivilegio, BigDecimal> implements UsuarioPrivilegioDao {

    public UsuarioPrivilegioJpaDao() {
        super(UsuarioPrivilegio.class);
    }

    /**
     * Devuelve el privilegio que tiene determinado usuario
     *
     * @author Victor Lozano
     * @param usuarioId
     * @return UsuarioPrivilegio
     */
    @Override
    public UsuarioPrivilegio findByUsuario(BigDecimal usuarioId) {
        String consulta = "SELECT u FROM UsuarioPrivilegio u WHERE u.usuario.id = ?1";
        List<UsuarioPrivilegio> lista = executeQuery(consulta, usuarioId);
        return lista == null || lista.isEmpty() ? null : lista.get(0);
    }
}
