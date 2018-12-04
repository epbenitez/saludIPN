package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.PresupuestoTipoBecaPeriodoDao;
import com.becasipn.persistence.model.PresupuestoTipoBecaPeriodo;
import java.math.BigDecimal;

/**
 *
 * @author Tania G. Sánchez
 */
public class PresupuestoTipoBecaPeriodoJpaDao extends JpaDaoBase<PresupuestoTipoBecaPeriodo, BigDecimal> implements PresupuestoTipoBecaPeriodoDao {

    public PresupuestoTipoBecaPeriodoJpaDao() {
        super(PresupuestoTipoBecaPeriodo.class);
    }
}
