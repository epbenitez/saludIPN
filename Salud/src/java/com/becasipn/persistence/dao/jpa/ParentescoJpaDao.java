package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.ParentescoDao;
import com.becasipn.persistence.model.Parentesco;
import java.math.BigDecimal;

/**
 *
 * @author Tania G. Sánchez
 */
public class ParentescoJpaDao extends JpaDaoBase<Parentesco, BigDecimal> implements ParentescoDao{
    public ParentescoJpaDao() {
        super(Parentesco.class);
    }
}