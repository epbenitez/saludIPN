package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.UnidadAcademica;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez
 */
public interface UnidadAcademicaDao extends DaoBase<UnidadAcademica, BigDecimal> {
    public List<UnidadAcademica> findAllAlphabOrder();
    public List<UnidadAcademica> findAllx();
}