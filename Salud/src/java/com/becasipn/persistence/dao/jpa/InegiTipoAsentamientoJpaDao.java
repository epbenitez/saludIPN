package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.InegiTipoAsentamientoDao;
import com.becasipn.persistence.model.InegiTipoAsentamiento;
import java.math.BigDecimal;

/**
 *
 * @author Tania Gabriela Sánchez
 */
public class InegiTipoAsentamientoJpaDao extends JpaDaoBase<InegiTipoAsentamiento, BigDecimal> implements InegiTipoAsentamientoDao {

    public InegiTipoAsentamientoJpaDao() {
        super(InegiTipoAsentamiento.class);
    }
}
