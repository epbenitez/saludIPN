package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.BitacoraPermisoCambioDao;
import com.becasipn.persistence.model.BitacoraPermisoCambio;
import java.math.BigDecimal;

public class BitacoraPermisoCambioJpaDao extends JpaDaoBase<BitacoraPermisoCambio, BigDecimal> implements BitacoraPermisoCambioDao {

    public BitacoraPermisoCambioJpaDao() {
        super(BitacoraPermisoCambio.class);
    }
}
