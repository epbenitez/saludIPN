package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.MovimientoDao;
import com.becasipn.persistence.model.Movimiento;
import java.math.BigDecimal;

/**
 *
 * @author Victor Lozano
 */
public class MovimientoJpaDao extends JpaDaoBase<Movimiento, BigDecimal> implements MovimientoDao {

    public MovimientoJpaDao() {
        super(Movimiento.class);
    }
}
