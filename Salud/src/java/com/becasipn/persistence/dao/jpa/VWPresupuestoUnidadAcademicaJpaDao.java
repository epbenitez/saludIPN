package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.VWPresupuestoUnidadAcademicaDao;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.PresupuestoTipoBecaPeriodo;
import com.becasipn.persistence.model.TipoBecaPeriodo;
import com.becasipn.persistence.model.UnidadAcademica;
import com.becasipn.persistence.model.VWPresupuestoUnidadAcademica;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez
 */
public class VWPresupuestoUnidadAcademicaJpaDao extends JpaDaoBase<VWPresupuestoUnidadAcademica, BigDecimal> implements VWPresupuestoUnidadAcademicaDao {

    public VWPresupuestoUnidadAcademicaJpaDao() {
        super(VWPresupuestoUnidadAcademica.class);
    }

    /**
     * Devuelbe el presupuesto para una unidad academica en el periodo en curso
     *
     * @author Victor Lozano
     * @param unidadAcademicaId
     * @return true si el alumno ya esta asignado
     */
    @Override
    public List<VWPresupuestoUnidadAcademica> getPresupuestosPorUA(BigDecimal unidadAcademicaId) {
        String consulta = "SELECT p FROM VWPresupuestoUnidadAcademica p"
                + " WHERE p.periodo.estatus = 1"
                + " AND p.unidadAcademica.id = ?1";
        List<VWPresupuestoUnidadAcademica> lista = executeQuery(consulta, unidadAcademicaId);
        return lista;
    }

    @Override
    public List<VWPresupuestoUnidadAcademica> presupuestoPeriodoActual() {
        String sql = "SELECT * FROM vw_presupuesto_unidadacademica "
                + "where periodo_id = (select pe.id from cat_periodo pe where pe.estatus = 1 and rownum = 1) "
                + "order by unidadacademica_id";
        List<Object[]> lista = executeNativeQuery(sql);
        List<VWPresupuestoUnidadAcademica> lvpua = new ArrayList<VWPresupuestoUnidadAcademica>();
        for (Object[] res : lista) {
            VWPresupuestoUnidadAcademica vpua = new VWPresupuestoUnidadAcademica();
            //Alumno
            vpua.setId((BigDecimal) res[0]);
            //Periodo
            Periodo periodo = new Periodo();
            periodo.setId((BigDecimal) res[1]);
            vpua.setPeriodo(periodo);
            //TipoBecaPeriodo
            TipoBecaPeriodo tipoBecaPeriodo = new TipoBecaPeriodo();
            tipoBecaPeriodo.setId((BigDecimal) res[2]);
            vpua.setTipoBecaPeriodo(tipoBecaPeriodo);
            //PresupuestoTipoBecaPeriodo
            PresupuestoTipoBecaPeriodo presupuestoTipoBecaPeriodo = new PresupuestoTipoBecaPeriodo();
            presupuestoTipoBecaPeriodo.setId((BigDecimal) res[3]);
            vpua.setPresupuestoTipoBecaPeriodo(presupuestoTipoBecaPeriodo);
            //UnidadAcademica
            UnidadAcademica unidadAcademica = new UnidadAcademica();
            unidadAcademica.setId((BigDecimal) res[4]);
            vpua.setUnidadAcademica(unidadAcademica);
            vpua.setMontoPresupuestado((BigDecimal) res[5]);
            vpua.setMontoEjercido((BigDecimal) res[6]);
            vpua.setMontoPorBeca((BigDecimal) res[7]);
            vpua.setBecasDisponibles((Integer) res[8]);
            lvpua.add(vpua);
        }
        return lvpua;
    }

    @Override
    public VWPresupuestoUnidadAcademica getPresupuesto(BigDecimal idUnidadAcademica, BigDecimal idTipoBeca, BigDecimal idPeriodo) {
        String consulta = "SELECT v FROM VWPresupuestoUnidadAcademica v"
                + " WHERE v.unidadAcademica.id = ?1"
                + " AND v.tipoBecaPeriodo.id = ?2"
                + " and v.periodo.id = ?3";
        List<VWPresupuestoUnidadAcademica> lista = executeQuery(consulta, idUnidadAcademica, idTipoBeca, idPeriodo);
        return ((lista == null || lista.isEmpty()) ? null : lista.get(0));
    }

    //SQLINJECTION
    @Override
    public List<VWPresupuestoUnidadAcademica> getListaPresupuestos(BigDecimal idTipoBecaPeriodo, BigDecimal idPresupuestoTipoBecaPeriodo, BigDecimal idPeriodo) {
        String sql = "SELECT p.id, "
                + "    " + idPeriodo + " periodo_id, "
                + "    " + idTipoBecaPeriodo + " tipobecaperiodo_id,"
                + "    " + idPresupuestoTipoBecaPeriodo + " presupuestotipobecaperiodo_id, "
                + "    u.id unidadacademica_id, "
                + "    p.montopresupuestado, "
                + "    p.montoejercido, "
                + "    p.montoporbeca, "
                + "    p.becasdisponibles, "
                + "    u.nombreCorto,"
                + "    p.duracion,"
                + "   (select pe.estatus from cat_periodo pe where pe.id = " + idPeriodo + " )  estatus  "
                + "from cat_unidad_academica u "
                + " left join vw_presupuesto_unidadacademica p on p.unidadacademica_id = u.id"
                + " and (p.tipobecaperiodo_id = " + idTipoBecaPeriodo + " or p.tipobecaperiodo_id is null) "
                + " left join ent_tipo_beca_periodo tbp on tbp.NIVEL_ID = u.NIVEL_ID and p.TIPOBECAPERIODO_ID = tbp.id "
                //                + " inner join cat_periodo per on per.id = " + idPeriodo 
                + " where p.periodo_id = " + idPeriodo + " or p.periodo_id  is null "
                + " and u.NIVEL_ID = (select nivel_id from ent_tipo_beca_periodo where id= " + idTipoBecaPeriodo + ") ";
//                + "  and p.tipobecaperiodo_id = "+idTipoBecaPeriodo+" or p.tipobecaperiodo_id is null";
        List<Object[]> lista = executeNativeQuery(sql);
        List<VWPresupuestoUnidadAcademica> lvpua = new ArrayList<VWPresupuestoUnidadAcademica>();
        for (Object[] res : lista) {
            VWPresupuestoUnidadAcademica vpua = new VWPresupuestoUnidadAcademica();
            vpua.setId(res[0] == null || res[0].equals("null") ? null : new BigDecimal(res[0].toString()));
            //Periodo
            Periodo periodo = new Periodo();
            periodo.setId(res[1] == null ? null : (BigDecimal) res[1]);
            BigDecimal bd = res[11] == null ? BigDecimal.ZERO : (BigDecimal) res[11];
            periodo.setEstatus(bd.intValue() == 0 ? Boolean.FALSE : Boolean.TRUE);
            vpua.setPeriodo(periodo);
            //TipoBecaPeriodo
            TipoBecaPeriodo tipoBecaPeriodo = new TipoBecaPeriodo();
            tipoBecaPeriodo.setId(res[2] == null ? null : (BigDecimal) res[2]);
            BigDecimal duracion = res[10] == null ? null : (BigDecimal) res[10];
            tipoBecaPeriodo.setDuracion(duracion == null ? 0 : duracion.intValue());
            vpua.setTipoBecaPeriodo(tipoBecaPeriodo);
            //PresupuestoTipoBecaPeriodo
            PresupuestoTipoBecaPeriodo presupuestoTipoBecaPeriodo = new PresupuestoTipoBecaPeriodo();
            presupuestoTipoBecaPeriodo.setId(res[3] == null ? null : (BigDecimal) res[3]);
            vpua.setPresupuestoTipoBecaPeriodo(presupuestoTipoBecaPeriodo);
            //UnidadAcademica
            UnidadAcademica unidadAcademica = new UnidadAcademica();
            unidadAcademica.setId(res[4] == null ? null : (BigDecimal) res[4]);
            unidadAcademica.setNombreCorto(res[9] == null ? "" : (String) res[9]);

            vpua.setUnidadAcademica(unidadAcademica);
            vpua.setMontoPresupuestado(res[5] == null ? new BigDecimal(0) : (BigDecimal) res[5]);
            vpua.setMontoEjercido(res[6] == null ? new BigDecimal(0) : (BigDecimal) res[6]);
            BigDecimal montoPorBeca = res[7] == null ? new BigDecimal(0) : (BigDecimal) res[7];
            vpua.setMontoPorBeca(montoPorBeca);
            BigDecimal becasDisponibles = (res[8] == null ? new BigDecimal(0) : (BigDecimal) res[8]);
            vpua.setBecasDisponibles(becasDisponibles.intValue());
            lvpua.add(vpua);
        }
        return lvpua;
    }

    @Override
    public List<VWPresupuestoUnidadAcademica> presupuestoUAPeriodoPrioridad(BigDecimal peridoActualId) {
        String jpql = "SELECT p FROM VWPresupuestoUnidadAcademica p WHERE p.periodo.id = ?1 "
                + "and p.tipoBecaPeriodo.periodo.id = ?1 order by p.tipoBecaPeriodo.prioridad";
        /* SELECT * FROM vw_presupuesto_unidadacademica vw
         inner join ent_tipo_beca_periodo tbp on vw.tipobecaperiodo_id=tbp.id
         where vw.periodo_id=(select pe.id from cat_periodo pe where pe.estatus = 1 and rownum = 1)
         and tbp.periodo_id=(select pe.id from cat_periodo pe where pe.estatus = 1 and rownum = 1); */
        List<VWPresupuestoUnidadAcademica> lista = executeQuery(jpql, peridoActualId);
        return lista;
    }

    @Override
    public VWPresupuestoUnidadAcademica getPresupuesto(BigDecimal unidadAcademicaId, BigDecimal presupuestoTipoBecaPeriodoId) {
        String jpql = "SELECT p FROM  VWPresupuestoUnidadAcademica p WHERE p.unidadAcademica.id = ?1 and p.presupuestoTipoBecaPeriodo.id = ?2 ";
        List<VWPresupuestoUnidadAcademica> lista = executeQuery(jpql, unidadAcademicaId, presupuestoTipoBecaPeriodoId);
        return (lista == null || lista.isEmpty()) ? null : lista.get(0);
    }
    
    @Override
    public VWPresupuestoUnidadAcademica getPresupuestoUnidadAcademicaTipoBeca(BigDecimal unidadAcademicaId, BigDecimal tipobecaperiodoid) {
        String jpql = "SELECT p FROM  VWPresupuestoUnidadAcademica p WHERE p.unidadAcademica.id = ?1 and p.tipoBecaPeriodo.id = ?2 ";
        List<VWPresupuestoUnidadAcademica> lista = executeQuery(jpql, unidadAcademicaId,tipobecaperiodoid);
        return (lista == null || lista.isEmpty()) ? null : lista.get(0);
    }

    @Override
    public List<VWPresupuestoUnidadAcademica> presupuestoUAPeriodoNivel(BigDecimal periodoActual, Integer nivel) {
        String jpql = "SELECT p FROM  VWPresupuestoUnidadAcademica p "
                + " JOIN TipoBecaPeriodo T ON P.tipoBecaPeriodo.id=T.id "
                + " JOIN TipoBeca C ON T.tipoBeca.id=C.id "
                + " WHERE p.periodo.id=?1 "
                + " and C.beca.id not in(7,8,9) "
                + "and p.unidadAcademica.nivel.id=?2 order by p.tipoBecaPeriodo.prioridad";
        List<VWPresupuestoUnidadAcademica> lista = executeQuery(jpql, periodoActual, nivel);
        return lista;

    }

}
