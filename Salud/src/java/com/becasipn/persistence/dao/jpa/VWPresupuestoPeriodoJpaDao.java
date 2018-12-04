package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.VWPresupuestoPeriodoDao;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.VWPresupuestoPeriodo;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Patricia Benítez
 */
public class VWPresupuestoPeriodoJpaDao extends JpaDaoBase<VWPresupuestoPeriodo, BigDecimal> implements VWPresupuestoPeriodoDao {

    public VWPresupuestoPeriodoJpaDao() {
        super(VWPresupuestoPeriodo.class);
    }

    @Override
    public VWPresupuestoPeriodo getPresupuesto(BigDecimal periodoId) {
        String jpql = "SELECT p FROM  VWPresupuestoPeriodo p WHERE p.periodo.id = ?1";
        List<VWPresupuestoPeriodo> lista = executeQuery(jpql, periodoId);
        return (lista == null || lista.isEmpty()) ? null : lista.get(0);
    }

    @Override
    public List<VWPresupuestoPeriodo> findAll() {
        String sql = "SELECT pp.id, p.id periodo_id, p.clave, pp.montopresupuestado, pp.montoasignado, pp.montoejercido, p.estatus "
                + " FROM cat_periodo p "
                + " left join vw_presupuesto_periodo pp on p.id = pp.periodo_id "
                + " order by p.clave desc";
        List<Object[]> lista = executeNativeQuery(sql);
        List<VWPresupuestoPeriodo> ppList = new LinkedList<VWPresupuestoPeriodo>();
        for (Object[] res : lista) {
            Periodo p = new Periodo();
            p.setId((BigDecimal) res[1]);
            p.setClave((String) res[2]);
            BigDecimal bd = (BigDecimal) res[6];
            p.setEstatus(bd.intValue()==0?Boolean.FALSE:Boolean.TRUE);

            VWPresupuestoPeriodo pp = new VWPresupuestoPeriodo();
            pp.setPeriodo(p);
            BigDecimal id = res[0] == null ? null : (BigDecimal) res[0];
            BigDecimal monto = res[3] == null ? null : (BigDecimal) res[3];
            pp.setId(id);
            pp.setMontoPresupuestado(monto);
            BigDecimal m = res[4] == null ? null : (BigDecimal) res[4];
            pp.setMontoAsignado(m);
            m = res[5] == null ? null : (BigDecimal) res[5];
            pp.setMontoEjercido(m);

            ppList.add(pp);
        }
        return ppList;
    }
}
