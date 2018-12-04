package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.VWPresupuestoUnidadAcademica;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez
 */
public interface VWPresupuestoUnidadAcademicaDao extends DaoBase<VWPresupuestoUnidadAcademica, BigDecimal> {

    public List<VWPresupuestoUnidadAcademica> getPresupuestosPorUA(BigDecimal unidadAcademicaId);

    public List<VWPresupuestoUnidadAcademica> presupuestoPeriodoActual();
//    public VWPresupuestoUnidadAcademica getPresupuesto(BigDecimal idUnidadAcademica, BigDecimal idTipoBeca);

    public VWPresupuestoUnidadAcademica getPresupuesto(BigDecimal idUnidadAcademica, BigDecimal idTipoBeca, BigDecimal idPeriodo);

    public List<VWPresupuestoUnidadAcademica> getListaPresupuestos(BigDecimal idTipoBecaPeriodo, BigDecimal idPresupuestoTipoBecaPeriodo, BigDecimal idPeriodo);

    public List<VWPresupuestoUnidadAcademica> presupuestoUAPeriodoPrioridad(BigDecimal peridoActualId);

    public VWPresupuestoUnidadAcademica getPresupuesto(BigDecimal unidadAcademicaId, BigDecimal presupuestoTipoBecaPeriodoId);
    
    public VWPresupuestoUnidadAcademica getPresupuestoUnidadAcademicaTipoBeca(BigDecimal unidadAcademicaId, BigDecimal tipobecaperiodoid);

    public List<VWPresupuestoUnidadAcademica> presupuestoUAPeriodoNivel(BigDecimal periodoActual, Integer nivel);
}
