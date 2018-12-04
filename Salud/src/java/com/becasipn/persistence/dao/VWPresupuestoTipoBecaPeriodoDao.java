package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.VWPresupuestoTipoBecaPeriodo;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Patricia Benítez
 */
public interface VWPresupuestoTipoBecaPeriodoDao extends DaoBase<VWPresupuestoTipoBecaPeriodo, BigDecimal> {

    public List<VWPresupuestoTipoBecaPeriodo> getPresupuesto(BigDecimal tipoBecaId);

    public VWPresupuestoTipoBecaPeriodo getPresupuesto(BigDecimal presupuestoPeriodoId, BigDecimal tipoBecaPeriodo);
}
