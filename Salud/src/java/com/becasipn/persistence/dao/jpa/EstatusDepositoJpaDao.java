package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.EstatusDepositoDao;
import com.becasipn.persistence.model.EstatusDeposito;
import java.math.BigDecimal;

/**
 *
 * @author Victor Lozano
 */
public class EstatusDepositoJpaDao extends JpaDaoBase<EstatusDeposito, BigDecimal> implements EstatusDepositoDao {

    public EstatusDepositoJpaDao() {
        super(EstatusDeposito.class);
    }

}
