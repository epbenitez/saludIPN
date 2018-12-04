package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.PresupuestoUnidadAcademicaDao;
import com.becasipn.persistence.model.PresupuestoUnidadAcademica;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez
 */
public class PresupuestoUnidadAcademicaJpaDao extends JpaDaoBase<PresupuestoUnidadAcademica, BigDecimal> implements PresupuestoUnidadAcademicaDao {

    public PresupuestoUnidadAcademicaJpaDao() {
        super(PresupuestoUnidadAcademica.class);
    }

    @Override
    public List<PresupuestoUnidadAcademica> asociadaPresupuestoUnidadAcademica(BigDecimal unidadAcademicaId) {
        String jpql = "SELECT p FROM  PresupuestoUnidadAcademica p WHERE p.unidadAcademica.id = ?1";
        //SELECT * FROM becasipn.ent_presupuesto_unidadacademica where unidadacademica_id=1;
        List<PresupuestoUnidadAcademica> lista = executeQuery(jpql, unidadAcademicaId);
        return lista;
    }

    @Override
    public PresupuestoUnidadAcademica getPresupuesto(BigDecimal unidadAcademicaId, BigDecimal presupuestoTipoBecaPeriodoId) {
        String jpql = "SELECT p FROM  PresupuestoUnidadAcademica p WHERE p.unidadAcademica.id = ?1 and p.presupuestoTipoBecaPeriodo.id = ?2 ";
        List<PresupuestoUnidadAcademica> lista = executeQuery(jpql, unidadAcademicaId, presupuestoTipoBecaPeriodoId);
        return (lista == null || lista.isEmpty()) ? null : lista.get(0);
    }

}
