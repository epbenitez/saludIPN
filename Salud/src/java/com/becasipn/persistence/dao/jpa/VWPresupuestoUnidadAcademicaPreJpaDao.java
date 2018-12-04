package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.VWPresupuestoUnidadAcademicaPreDao;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.VWPresupuestoUnidadAcademicaPre;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Gustavo A. Alamillo
 */
public class VWPresupuestoUnidadAcademicaPreJpaDao extends JpaDaoBase<VWPresupuestoUnidadAcademicaPre, BigDecimal> implements VWPresupuestoUnidadAcademicaPreDao {

    public VWPresupuestoUnidadAcademicaPreJpaDao() {
        super(VWPresupuestoUnidadAcademicaPre.class);
    }

    @Override
    public List<VWPresupuestoUnidadAcademicaPre> presupuestoUAPeriodoNivel(Periodo periodoActual, Integer nivel) {
        String jpql
                = "SELECT P FROM VWPresupuestoUnidadAcademicaPre P "
                + "JOIN TipoBecaPeriodo T ON P.tipoBecaPeriodo.id = T.id "
                + "JOIN TipoBeca C ON T.tipoBeca.id = C.id "
                + "WHERE P.periodo.id = ?1 "
                //+ "AND C.beca.id not in(7,8,9) "
                + "AND P.unidadAcademica.nivel.id = ?2 "
                + "ORDER BY p.tipoBecaPeriodo.prioridad";
        List<VWPresupuestoUnidadAcademicaPre> lista = executeQuery(jpql, periodoActual.getId(), nivel);
        return lista;
    }

}
