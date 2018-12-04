package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.InegiTipoVialidadDao;
import com.becasipn.persistence.model.InegiTipoVialidad;
import java.math.BigDecimal;

/**
 *
 * @author Sistemas
 */
public class InegiTipoVialidadJpaDao extends JpaDaoBase<InegiTipoVialidad, BigDecimal> implements InegiTipoVialidadDao {

    public InegiTipoVialidadJpaDao() {
        super(InegiTipoVialidad.class);
    }
}
