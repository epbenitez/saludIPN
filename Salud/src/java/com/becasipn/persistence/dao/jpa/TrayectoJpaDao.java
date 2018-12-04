package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.TrayectoDao;
import com.becasipn.persistence.model.Trayecto;
import java.math.BigDecimal;

/**
 *
 * @author Tania G. Sánchez
 */
public class TrayectoJpaDao extends JpaDaoBase<Trayecto, BigDecimal> implements TrayectoDao{
    public TrayectoJpaDao() {
        super(Trayecto.class);
    }
}
