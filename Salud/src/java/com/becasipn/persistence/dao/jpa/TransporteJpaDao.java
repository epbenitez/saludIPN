package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.TransporteDao;
import com.becasipn.persistence.model.Transporte;
import java.math.BigDecimal; 

/**
 *
 * @author Tania G. Sánchez
 */
public class TransporteJpaDao extends JpaDaoBase<Transporte, BigDecimal> implements TransporteDao{
    public TransporteJpaDao() {
        super(Transporte.class);
    }
}