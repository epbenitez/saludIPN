package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.CuestionarioDao;
import com.becasipn.persistence.model.Cuestionario;
import java.math.BigDecimal;

/**
 *
 * @author Tania G. Sánchez
 */
public class CuestionarioJpaDao extends JpaDaoBase<Cuestionario, BigDecimal> implements CuestionarioDao {

    public CuestionarioJpaDao() {
        super(Cuestionario.class);
    }
}
