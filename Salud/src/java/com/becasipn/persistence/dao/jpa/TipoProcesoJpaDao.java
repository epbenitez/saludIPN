package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.TipoProcesoDao;
import com.becasipn.persistence.model.Movimiento;
import com.becasipn.persistence.model.TipoProceso;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez
 */
public class TipoProcesoJpaDao extends JpaDaoBase<TipoProceso, BigDecimal> implements TipoProcesoDao {

    public TipoProcesoJpaDao() {
        super(TipoProceso.class);
    }

    @Override
    public List<TipoProceso> getProcesosActivosPeriodo(BigDecimal periodoId) {
        String sql = "select tp.* from cat_tipo_proceso tp"
                + " inner join ent_proceso ep on tp.id = ep.tipoproceso_id"
                + " inner join cat_movimiento cm on tp.movimiento_id = cm.id"
                + " inner join cat_tipo_movimiento ctm on cm.tipomovimiento_id = ctm.id"
                + " where ep.fechafinal < current_timestamp and ep.procesoestatus_id = 4 "
                + " and ctm.clave = 'A' and ep.periodo_id = ?1"
                + " group by tp.id, tp.nombre, tp.descripcion, tp.movimiento_id"
                + " order by tp.movimiento_id";
        List<Object[]> lista = executeNativeQuery(sql, periodoId);
        List<TipoProceso> ltp = new ArrayList<TipoProceso>();
        for (Object[] res : lista) {
            TipoProceso tp = new TipoProceso();
            tp.setId((BigDecimal) res[0]);
            tp.setNombre((String) res[1]);
            tp.setDescripcion((String) res[2]);
            //Movimiento
            Movimiento m = new Movimiento();
            m.setId((BigDecimal) res[3]);
            tp.setMovimiento(m);
            ltp.add(tp);
        }
        return ltp;
    }

    @Override
    public List<TipoProceso> procesoEnUso(BigDecimal procesoId) {
        String jpql = "SELECT tp.id FROM TipoProceso tp INNER JOIN Proceso p ON tp.id = p.tipoProceso.id WHERE tp.id = ?1";
        return executeQuery(jpql, procesoId);
    }

    /**
     * Devuelve el listado de procesos por movimiento
     *
     * @author Victor Lozano
     * @param movimientoId
     * @return lista
     */
    @Override
    public List<TipoProceso> procesoByMovimiento(BigDecimal movimientoId) {
        String consulta = "SELECT p FROM TipoProceso p WHERE p.movimiento.id = ?1";
        List<TipoProceso> lista = executeQuery(consulta, movimientoId);
        return lista;
    }

    @Override
    public TipoProceso procesoAutoByMovimiento(BigDecimal movimientoId) {
        String consulta = "select id,1 from CAT_TIPO_PROCESO "
                + "where NOMBRE like '%Auto%' "
                + "and MOVIMIENTO_ID = ?1";
        List<Object[]> lista = executeNativeQuery(consulta, movimientoId);
        TipoProceso p;
        if (!lista.isEmpty()) {
            Object[] res = lista.get(0);
            p = getDaos().getTipoProcesoDao().findById((BigDecimal) res[0]);
        } else {
            p = null;
        }
        return p;
    }

    @Override
    public TipoProceso procesoAutoSubes() {
        String consulta = "select id,1 from CAT_TIPO_PROCESO "
                + "where NOMBRE like '%SUBES Auto%' "
                + "and MOVIMIENTO_ID = 3";
        List<Object[]> lista = executeNativeQuery(consulta);
        TipoProceso p;
        if (!lista.isEmpty()) {
            Object[] res = lista.get(0);
            p = getDaos().getTipoProcesoDao().findById((BigDecimal) res[0]);
        } else {
            p = null;
        }
        return p;
    }
}
