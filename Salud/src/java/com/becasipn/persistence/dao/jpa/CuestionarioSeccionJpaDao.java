package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.CuestionarioSeccionDao;
import com.becasipn.persistence.model.CuestionarioSeccion;
import java.math.BigDecimal;

/**
 *
 * @author Tania G. Sánchez
 */
public class CuestionarioSeccionJpaDao extends JpaDaoBase<CuestionarioSeccion, BigDecimal> implements CuestionarioSeccionDao {

    public CuestionarioSeccionJpaDao() {
        super(CuestionarioSeccion.class);
    }
}
