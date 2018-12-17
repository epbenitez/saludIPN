package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.model.RelacionGeografica;
import com.becasipn.persistence.dao.RelacionGeograficaDao;
import java.math.BigDecimal;

/**
 *
 * @author Patricia Benitez
 */
public class RelacionGeograficaJpaDao extends JpaDaoBase<RelacionGeografica, BigDecimal> implements RelacionGeograficaDao {

    public RelacionGeograficaJpaDao() {
        super(RelacionGeografica.class);
    }
}
