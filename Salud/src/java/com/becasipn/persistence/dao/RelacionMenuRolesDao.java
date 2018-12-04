package com.becasipn.persistence.dao;

import java.math.BigDecimal;

import com.becasipn.persistence.model.RelacionMenuRoles;
import com.becasipn.persistence.model.UsuarioPrivilegio;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Patricia Benitez
 */
public interface RelacionMenuRolesDao extends DaoBase<RelacionMenuRoles, BigDecimal> {

    public String findURLByRols(Set<UsuarioPrivilegio> roles);

    public String findURLESE();

    public List<RelacionMenuRoles> buscadorPorRoles(Set<UsuarioPrivilegio> roles, String busqueda);
}
