package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.IdentificadorOtorgamientoDao;
import com.becasipn.persistence.model.IdentificadorOtorgamiento;
import java.math.BigDecimal;

/**
 *
 * @author Tania G. Sánchez
 */
public class IdentificadorOtorgamientoJpaDao extends JpaDaoBase<IdentificadorOtorgamiento, BigDecimal> implements IdentificadorOtorgamientoDao {

    public IdentificadorOtorgamientoJpaDao() {
        super(IdentificadorOtorgamiento.class);
    }
}