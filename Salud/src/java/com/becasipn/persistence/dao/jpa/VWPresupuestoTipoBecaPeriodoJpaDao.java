package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.VWPresupuestoTipoBecaPeriodoDao;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.TipoBeca;
import com.becasipn.persistence.model.TipoBecaPeriodo;
import com.becasipn.persistence.model.VWPresupuestoPeriodo;
import com.becasipn.persistence.model.VWPresupuestoTipoBecaPeriodo;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Patricia Benítez
 */
public class VWPresupuestoTipoBecaPeriodoJpaDao extends JpaDaoBase<VWPresupuestoTipoBecaPeriodo, BigDecimal> implements VWPresupuestoTipoBecaPeriodoDao {

    public VWPresupuestoTipoBecaPeriodoJpaDao() {
        super(VWPresupuestoTipoBecaPeriodo.class);
    }

    //SQLINJECTION
    @Override
    public List<VWPresupuestoTipoBecaPeriodo> getPresupuesto(BigDecimal periodoId) {

        String sql = "SELECT  vw.id, "
                + "(select id from vw_presupuesto_periodo where periodo_id = " + periodoId + " ) presupuestoperiodo_id, "
                + "bp.id tipobecaperiodo_id, "
                + "vw.montopresupuestado, "
                + "vw.montoasignado, "
                + "vw.montoejercido, "
                + "cat.nombre, "
                + "p.estatus "
                + " FROM ent_tipo_beca_periodo bp "
                + " left join vw_presupuesto_tipobecaperiodo vw    on  vw.tipobecaperiodo_id = bp.id  "
                + " inner join cat_tipo_beca cat on cat.id = bp.tipobeca_id "
                + " inner join cat_periodo p on p.id = " + periodoId 
                + " where bp.periodo_id = " + periodoId 
                + " and bp.visible is not null "
                + " order by bp.NIVEL_ID, cat.nombre ";
        List<Object[]> lista = executeNativeQuery(sql);
        List<VWPresupuestoTipoBecaPeriodo> ppList = new LinkedList<VWPresupuestoTipoBecaPeriodo>();
        for (Object[] res : lista) {
            VWPresupuestoPeriodo p = new VWPresupuestoPeriodo();
            p.setId(res[1] == null ? null : (BigDecimal) res[1]);
            
            Periodo periodo = new Periodo();
            periodo.setId(periodoId);
            BigDecimal bd = res[7] == null ? BigDecimal.ZERO : (BigDecimal) res[7];
            periodo.setEstatus(bd.intValue()==0?Boolean.FALSE: Boolean.TRUE);
            p.setPeriodo(periodo);

            TipoBeca beca = new TipoBeca();
            beca.setNombre(res[6] == null ? null : (String) res[6]);
            TipoBecaPeriodo tbp = new TipoBecaPeriodo();
            tbp.setId(res[2] == null ? null : (BigDecimal) res[2]);
            tbp.setTipoBeca(beca);

            VWPresupuestoTipoBecaPeriodo pp = new VWPresupuestoTipoBecaPeriodo();
            pp.setId(res[0] == null ? null : (BigDecimal) res[0]);
            pp.setPresupuestoPeriodo(p);
            pp.setTipoBecaPeriodo(tbp);

            BigDecimal monto = res[3] == null ? null : (BigDecimal) res[3];
            pp.setMontoPresupuestado(monto);
            BigDecimal m = res[4] == null ? null : (BigDecimal) res[4];
            pp.setMontoAsignado(m);
            m = res[5] == null ? null : (BigDecimal) res[5];
            pp.setMontoEjercido(m);

            ppList.add(pp);
        }
        return ppList;
    }

    @Override
    public VWPresupuestoTipoBecaPeriodo getPresupuesto(BigDecimal presupuestoPeriodoId, BigDecimal tipoBecaPeriodo) {
        String jpql = "SELECT p FROM  VWPresupuestoTipoBecaPeriodo p WHERE p.presupuestoPeriodo.id = ?1"
                + " and p.tipoBecaPeriodo.id = ?2 ";
        List<VWPresupuestoTipoBecaPeriodo> lista = executeQuery(jpql, presupuestoPeriodoId, tipoBecaPeriodo);
        return (lista == null || lista.isEmpty()) ? null : lista.get(0);
    }
}
