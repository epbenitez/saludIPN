package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.AreasConocimientoDao;
import com.becasipn.persistence.model.AreasConocimiento;
import java.math.BigDecimal;

/**
 *
 * @author Tania G. Sánchez
 */
public class AreasConocimientoJpaDao extends JpaDaoBase<AreasConocimiento, BigDecimal> implements AreasConocimientoDao {

    public AreasConocimientoJpaDao() {
        super(AreasConocimiento.class);
    }
}
