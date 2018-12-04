package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.CuestionarioPreguntaTipoDao;
import com.becasipn.persistence.model.CuestionarioPreguntaTipo;
import java.math.BigDecimal;

/**
 *
 * @author Tania G. Sánchez
 */
public class CuestionarioPreguntaTipoJpaDao extends JpaDaoBase<CuestionarioPreguntaTipo, BigDecimal> implements CuestionarioPreguntaTipoDao {

    public CuestionarioPreguntaTipoJpaDao() {
        super(CuestionarioPreguntaTipo.class);
    }
}
