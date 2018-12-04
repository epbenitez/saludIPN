package com.becasipn.business;

import com.becasipn.persistence.model.PresupuestoPeriodo;
import com.becasipn.persistence.model.PresupuestoTipoBecaPeriodo;
import com.becasipn.persistence.model.PresupuestoUnidadAcademica;
import com.becasipn.persistence.model.VWPresupuestoTipoBecaPeriodo;
import com.becasipn.service.Service;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author User-02
 */
public class PresupuestoBO extends BaseBO {

    public PresupuestoBO(Service service) {
        super(service);
    }

    /**
     * Guarda el objeto Presupuesto
     *
     * @param p
     * @return
     */
    public Boolean guardaPresupuesto(PresupuestoPeriodo p) {
        try {
            if (p.getId() == null) {
                service.getPresupuestoPeriodoDao().save(p);
            } else {
                service.getPresupuestoPeriodoDao().update(p);
            }
        } catch (Exception e) {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    public Boolean guardaPresupuesto(PresupuestoTipoBecaPeriodo p) {
        try {
            if (p.getId() == null) {
                service.getPresupuestoTipoBecaPeriodoDao().save(p);
            } else {
                service.getPresupuestoTipoBecaPeriodoDao().update(p);
            }
        } catch (Exception e) {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    public Boolean guardaPresupuesto(Boolean existente, PresupuestoUnidadAcademica p) {
        try {
            if (existente) {
                service.getPresupuestoUnidadAcademicaDao().update(p);
            } else {
                service.getPresupuestoUnidadAcademicaDao().save(p);
            }
        } catch (Exception e) {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    public PresupuestoPeriodo getPresupuesto(BigDecimal id) {
        PresupuestoPeriodo p = service.getPresupuestoPeriodoDao().findById(id);
        return p;
    }

    public BigDecimal presupuestoAsignadoPorPeriodo(BigDecimal periodoId) {
        BigDecimal suma = new BigDecimal(0);
        List<VWPresupuestoTipoBecaPeriodo> list = service.getVwPresupuestoTipoBecaPeriodoDao().getPresupuesto(periodoId);
        for (VWPresupuestoTipoBecaPeriodo bp : list) {
            if (bp.getMontoPresupuestado() != null) {
                suma = suma.add(bp.getMontoPresupuestado());
            }
        }

        return suma;
    }

    public PresupuestoPeriodo getPresupuestoPeriodo(BigDecimal periodoId) {
        List<PresupuestoPeriodo> ppList = service.getPresupuestoPeriodoDao().existenPresupuestosAsociados(periodoId);

        return ppList == null ? null : ppList.get(0);
    }
}
