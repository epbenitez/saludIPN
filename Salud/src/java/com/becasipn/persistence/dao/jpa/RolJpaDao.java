package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.RolDao;
import com.becasipn.persistence.model.Rol;
import java.math.BigDecimal;

/**
 *
 * @author Luis Tlatelpa Fonseca
 */
public class RolJpaDao extends JpaDaoBase<Rol, BigDecimal> implements RolDao {

    /**
     * Crea una instancia de una <code>RolJpaDao</code>.
     */
    public RolJpaDao() {
        super(Rol.class);
    }

}
