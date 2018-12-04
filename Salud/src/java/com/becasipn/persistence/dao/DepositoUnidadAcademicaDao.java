package com.becasipn.persistence.dao;

import java.math.BigDecimal;

import com.becasipn.persistence.model.DepositoUnidadAcademica;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez
 */
public interface DepositoUnidadAcademicaDao extends DaoBase<DepositoUnidadAcademica, BigDecimal> {
    
    public List<DepositoUnidadAcademica> getOrigenRecursos(BigDecimal programaBecaId);
}
