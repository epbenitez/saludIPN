package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.PresupuestoUnidadAcademica;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez
 */
public interface PresupuestoUnidadAcademicaDao extends DaoBase<PresupuestoUnidadAcademica, BigDecimal> {

    public List<PresupuestoUnidadAcademica> asociadaPresupuestoUnidadAcademica(BigDecimal unidadAcademicaId);

    public PresupuestoUnidadAcademica getPresupuesto(BigDecimal unidadAcademicaId, BigDecimal presupuestoTipoBecaPeriodoId);
}
