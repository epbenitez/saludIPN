package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.EnteroBecaDao;
import com.becasipn.persistence.model.EnteroBeca;
import java.math.BigDecimal;

/**
 *
 * @author Tania G. Sánchez
 */
public class EnteroBecaJpaDao extends JpaDaoBase<EnteroBeca, BigDecimal> implements EnteroBecaDao{
    public EnteroBecaJpaDao() {
        super(EnteroBeca.class);
    }
}