package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.NacionalidadDao;
import com.becasipn.persistence.model.Nacionalidad;
import java.math.BigDecimal;

/**
 *
 * @author User-03
 */
public class NacionalidadJpaDao extends JpaDaoBase<Nacionalidad, BigDecimal> implements NacionalidadDao {

    public NacionalidadJpaDao() {
        super(Nacionalidad.class);
    }
}
