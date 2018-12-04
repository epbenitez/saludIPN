package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.TipoDepositoDao;
import com.becasipn.persistence.model.TipoDeposito;
import java.math.BigDecimal;

/**
 *
 * @author Tania G. Sánchez
 */
public class TipoDepositoJpaDao extends JpaDaoBase<TipoDeposito, BigDecimal> implements TipoDepositoDao {

    public TipoDepositoJpaDao() {
        super(TipoDeposito.class);
    }
}
