package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.EstatusReconsideracionDao;
import com.becasipn.persistence.model.EstatusReconsideracion;
import java.math.BigDecimal;

/**
 *
 * @author Augusto I. Hernández Ruiz
 *
 * "El problema real no es si las máquinas piensan, sino si lo hacen los
 * hombres" B. F. Skinner.
 */
public class EstatusReconsideracionJpaDao extends  JpaDaoBase<EstatusReconsideracion, BigDecimal> implements EstatusReconsideracionDao{
    
    public EstatusReconsideracionJpaDao()
    {
        super(EstatusReconsideracion.class);
    }
    
}
