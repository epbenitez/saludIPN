package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.EstatusDao;
import com.becasipn.persistence.model.Estatus;
import java.math.BigDecimal;

/**
 *
 * @author User-03
 */
public class EstatusJpaDao extends JpaDaoBase<Estatus, BigDecimal> implements EstatusDao {

    public EstatusJpaDao() {
        super(Estatus.class);
    }
}
