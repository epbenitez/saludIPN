package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.ProcesoEstatusDao;
import com.becasipn.persistence.model.ProcesoEstatus;
import java.math.BigDecimal;

/**
 *
 * @author Victor Lozano
 */
public class ProcesoEstatusJpaDao extends JpaDaoBase<ProcesoEstatus, BigDecimal> implements ProcesoEstatusDao {

    public ProcesoEstatusJpaDao() {
        super(ProcesoEstatus.class);
    }
}
