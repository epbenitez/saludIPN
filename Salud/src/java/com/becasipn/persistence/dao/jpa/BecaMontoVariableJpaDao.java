package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.BecaMontoVariableDao;
import com.becasipn.persistence.model.BecaMontoVariable;
import java.math.BigDecimal;

/**
 *
 * @author Tania G. Sánchez
 */
public class BecaMontoVariableJpaDao extends JpaDaoBase<BecaMontoVariable, BigDecimal> implements BecaMontoVariableDao{
    public BecaMontoVariableJpaDao() {
        super(BecaMontoVariable.class);
    }
}