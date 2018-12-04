package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.PresupuestoPeriodoDao;
import com.becasipn.persistence.model.PresupuestoPeriodo;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez
 */
public class PresupuestoPeriodoJpaDao extends JpaDaoBase<PresupuestoPeriodo, BigDecimal> implements PresupuestoPeriodoDao {

    public PresupuestoPeriodoJpaDao() {
        super(PresupuestoPeriodo.class);
    }

    @Override
    public List<PresupuestoPeriodo> existenPresupuestosAsociados(BigDecimal periodoId) {
        String jpql = "SELECT p FROM  PresupuestoPeriodo p WHERE p.periodo.id = ?1";
        //SELECT * FROM becasipn.ent_presupuesto_periodo where periodo_id=2;;
        List<PresupuestoPeriodo> lista = executeQuery(jpql, periodoId);
        return lista;
    }

}
