package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.VWPresupuestoUnidadAcademicaPre;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Gustavo A. Alamillo
 */
public interface VWPresupuestoUnidadAcademicaPreDao extends DaoBase<VWPresupuestoUnidadAcademicaPre, BigDecimal> {

    public List<VWPresupuestoUnidadAcademicaPre> presupuestoUAPeriodoNivel(Periodo periodoActual, Integer nivel);

}
