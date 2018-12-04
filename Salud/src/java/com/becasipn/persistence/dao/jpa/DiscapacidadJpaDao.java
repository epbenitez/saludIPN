package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.DiscapacidadDao;
import com.becasipn.persistence.model.Discapacidad;
import java.math.BigDecimal;

/**
 *
 * @author Tania G. Sánchez
 */
public class DiscapacidadJpaDao extends JpaDaoBase<Discapacidad, BigDecimal> implements DiscapacidadDao{
    public DiscapacidadJpaDao() {
        super(Discapacidad.class);
    }
}