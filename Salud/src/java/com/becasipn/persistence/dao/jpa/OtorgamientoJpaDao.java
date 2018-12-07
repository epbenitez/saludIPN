package com.becasipn.persistence.dao.jpa;

import com.becasipn.business.ProcesoBO;
import com.becasipn.domain.BecaPeriodoCount;
import com.becasipn.persistence.dao.OtorgamientoDao;
import com.becasipn.persistence.model.Otorgamiento;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.Beca;
import com.becasipn.persistence.model.ConvocatoriaSubes;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.Proceso;
import com.becasipn.persistence.model.Estadistica;
import com.becasipn.persistence.model.Movimiento;
import com.becasipn.persistence.model.Nivel;
import com.becasipn.persistence.model.UnidadAcademica;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.SolicitudBeca;
import com.becasipn.persistence.model.TipoBecaPeriodo;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.persistence.model.VWOtorgamiento;
import com.becasipn.util.PaginateUtil;
import com.becasipn.util.ServerSideUtil;
import static com.becasipn.util.StringUtil.buildCountQuery;
import com.opensymphony.xwork2.ActionContext;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Patricia Benítez
 */
public class OtorgamientoJpaDao extends JpaDaoBase<Otorgamiento, BigDecimal> implements OtorgamientoDao {

    public OtorgamientoJpaDao() {
        super(Otorgamiento.class);
    }

    @Override
    public Boolean existeProcesoAsociado(BigDecimal id) {
        String consulta = "SELECT o FROM Otorgamiento o WHERE o.proceso.id = ?1";
        List<Otorgamiento> lista = executeQuery(consulta, id);
        return lista != null && !lista.isEmpty();
    }

    /**
     * Valida si el alumno ya cuenta con un otorgamiento en el periodo en activo
     *
     * @author Victor Lozano
     * @param alumno_id
     * @return true si el alumno ya esta asignado
     */
    @Override
    public Boolean existeAlumnoAsignado(BigDecimal alumno_id) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT o FROM Otorgamiento o");
        sb.append(" WHERE o.alumno.id = ?1");
        sb.append(" AND o.periodo.estatus = 1");
        // Se elimina la restricción para incluir las becas de transporte.
        // sb.append(" AND o.tipoBecaPeriodo.tipoBeca.beca.id NOT IN (7,8,9)");
        sb.append(" AND o.proceso IS NOT NULL");
        sb.append(" AND O.proceso.procesoEstatus.id = 4 ");

        List<Otorgamiento> lista = executeQuery(sb.toString(), alumno_id);
        return (lista == null || lista.size() == 0 ? Boolean.FALSE : Boolean.TRUE);
    }

    @Override
    public Boolean existeAlumnoAnterior(BigDecimal alumno_id) {
        String consulta = "SELECT o FROM Otorgamiento o"
                + " WHERE o.alumno.id = ?1"
                + " AND o.periodo.id = ?2 "
                + " AND o.tipoBecaPeriodo.tipoBeca.beca.id NOT IN (7,8,9)"
                + " AND o.proceso IS NOT NULL";
        List<Otorgamiento> lista = executeQuery(consulta, alumno_id, getDaos().getPeriodoDao().getPeriodoActivo().getId());
        return (lista != null && !lista.isEmpty());
    }

    /**
     * Valida si el alumno ya cuenta con un preotorgamiento en el periodo en
     * activo
     *
     * @author Victor Lozano
     * @param alumno_id
     * @return true si el alumno ya esta asignado
     */
    @Override
    public Boolean existeAlumnoPreasignadoTransporte(BigDecimal alumno_id) {
        String consulta = "SELECT o FROM Otorgamiento o"
                + " WHERE o.alumno.id = ?1"
                + " AND o.periodo.estatus = 1"
                + " AND o.tipoBecaPeriodo.tipoBeca.id = 38"
                + " AND o.proceso IS NULL";
        List<Otorgamiento> lista = executeQuery(consulta, alumno_id);
        return (lista != null && !lista.isEmpty());
    }

    /**
     * Valida si el alumno ya cuenta con un preotorgamiento en el periodo en
     * activo
     *
     * @author Victor Lozano
     * @param alumno_id
     * @return true si el alumno ya esta asignado
     */
    //@Override
    @Override
    public Otorgamiento getPreasignacionTransporte(BigDecimal alumno_id) {
        String consulta = "SELECT o FROM Otorgamiento o"
                + " WHERE o.alumno.id = ?1"
                + " AND o.periodo.estatus = 1"
                + " AND o.tipoBecaPeriodo.tipoBeca.id = 38"
                + " AND o.proceso IS NULL";
        List<Otorgamiento> otorgamientos = executeQuery(consulta, alumno_id);
        return otorgamientos == null || otorgamientos.isEmpty() ? null : otorgamientos.get(0);
    }

    /**
     * Devuelve el otorgamiento de un determinado alumno para cierto periodo
     *
     * @author Victor Lozano
     * @param alumno_id
     * @param periodo_id
     * @return otorgamiento
     */
    @Override
    public Otorgamiento getOtorgamientoAlumno(BigDecimal alumno_id, BigDecimal periodo_id) {
        String consulta = "SELECT o FROM Otorgamiento o"
                + " WHERE o.alumno.id = ?1 "
                + " AND o.periodo.id = ?2 "
                //                + " AND o.alta = 1 "
                + " AND o.tipoBecaPeriodo.tipoBeca.beca.id NOT IN (7,8,9)"
                + " AND o.proceso IS NOT NULL ";
        List<Otorgamiento> lista = executeQuery(consulta, alumno_id, periodo_id);
        return ((lista == null || lista.isEmpty()) ? null : lista.get(0));
    }

    @Override
    public Otorgamiento getOtorgamientoBkUniversalAlumno(BigDecimal alumno_id, BigDecimal periodo_id) {
        StringBuilder consulta = new StringBuilder();
        consulta.append("SELECT o FROM Otorgamiento o")
                .append(" WHERE o.alumno.id = ?1 ")
                .append(" AND o.periodo.id = ?2 ")
                .append(" AND o.proceso.tipoProceso.id = 198");
        List<Otorgamiento> lista = executeQuery(consulta.toString(), alumno_id, periodo_id);
        return ((lista == null || lista.isEmpty()) ? null : lista.get(0));
    }

    /**
     * Devuelve el otorgamiento de transporte un determinado alumno para cierto
     * periodo
     *
     * @author Rafael Cardenas
     * @param alumno_id
     * @param periodo_id
     * @param tipo tipo de transporte 1 transporteManutencion 2 transporte
     * Institucional
     * @return otorgamiento
     */
    @Override
    public Otorgamiento getOtorgamientoTransporteAlumno(BigDecimal alumno_id, BigDecimal periodo_id, int tipo) {
        String jpql = "SELECT o FROM Otorgamiento o WHERE o.alumno.id=?1"
                + " and o.periodo.id=?2"
                + " and o.proceso is not null ";
        if (tipo == 1) {
            jpql += " AND o.tipoBecaPeriodo.tipoBeca.beca.id=7 ";
        } else {
            jpql += "AND o.tipoBecaPeriodo.tipoBeca.beca.id=8";
        }
        List<Otorgamiento> lista = executeQuery(jpql, alumno_id, periodo_id);
        return ((lista == null || lista.isEmpty()) ? null : lista.get(0));
    }

    @Override
    public Otorgamiento getOtorgamientoAlumno(BigDecimal alumno_id, BigDecimal periodo_id, BigDecimal tipoBeca_id) {
        String consulta = "SELECT o FROM Otorgamiento o"
                + " WHERE o.alumno.id = ?1 "
                + " AND o.periodo.id = ?2 "
                + " AND o.alta = 1 "
                + " AND o.tipoBecaPeriodo.tipoBeca.id = ?3"
                + " AND o.proceso IS NOT NULL ";
        List<Otorgamiento> lista = executeQuery(consulta, alumno_id, periodo_id, tipoBeca_id);
        return ((lista == null || lista.isEmpty()) ? null : lista.get(0));
    }

    /**
     * Devuelve el nombre de la beca de un alumno para cierto periodo
     *
     * @author Victor Lozano
     * @param alumno_id
     * @param periodo_id
     * @return nombreBeca
     */
    @Override
    public String getNombreBeca(BigDecimal alumno_id, BigDecimal periodo_id) {
        String consulta = "SELECT o FROM Otorgamiento o"
                + " WHERE o.alumno.id = ?1"
                + " AND o.tipoBecaPeriodo.tipoBeca.beca.id NOT IN (7,8,9)"
                + " AND o.periodo.id = ?2";
        List<Otorgamiento> lista = executeQuery(consulta, alumno_id, periodo_id);
        String beca = null;
        for (Otorgamiento otorgamiento : lista) {
            beca = otorgamiento.getTipoBecaPeriodo().getTipoBeca().getNombre();
        }
        return beca;
    }

    public String getNombreBecaBySolicitud(BigDecimal solicitud_id) {
        String consulta = "SELECT o FROM Otorgamiento o"
                + " WHERE o.solicitudBeca.id = ?1";
        List<Otorgamiento> lista = executeQuery(consulta, solicitud_id);
        String beca = null;
        for (Otorgamiento otorgamiento : lista) {
            beca = otorgamiento.getTipoBecaPeriodo().getTipoBeca().getNombre();
        }
        return beca;
    }

    /**
     * Devuelve el id de la beca de un alumno para cierto periodo
     *
     * @author Victor Lozano
     * @param alumno_id
     * @param periodo_id
     * @return nombreBeca
     */
    @Override
    public BigDecimal getIdBeca(BigDecimal alumno_id, BigDecimal periodo_id) {
        String consulta = "SELECT o FROM Otorgamiento o"
                + " WHERE o.alumno.id = ?1"
                + " AND o.tipoBecaPeriodo.tipoBeca.beca.id NOT IN (7,8,9)"
                + " AND o.periodo.id = ?2";
        List<Otorgamiento> lista = executeQuery(consulta, alumno_id, periodo_id);
        BigDecimal beca = null;
        for (Otorgamiento otorgamiento : lista) {
            beca = otorgamiento.getTipoBecaPeriodo().getTipoBeca().getId();
        }
        return beca;
    }

    @Override
    public List<Otorgamiento> existenOtorgamientosAsociados(BigDecimal periodoId) {
        String jpql = "SELECT o FROM  Otorgamiento o WHERE o.periodo.id = ?1";
        List<Otorgamiento> lista = executeQuery(jpql, periodoId);
        return lista;
    }

    @Override
    public Otorgamiento otorgamientoTransporteAlumnoManutencion(BigDecimal periodoId, BigDecimal otorgamientoId) {
        String sql = "select oo.* from ent_otorgamientos oo "
                + "join ENT_ALUMNO a on a.id = oo.ALUMNO_ID "
                + "where oo.alumno_id in( "
                + "  select o.alumno_id from ent_otorgamientos o "
                + "  where o.TIPOBECAPERIODO_ID in (select id from ENT_TIPO_BECA_PERIODO where TIPOBECA_ID in (32,33,34,35)) "
                + "    and o.PERIODO_ID = ?1 "
                + "    and o.id = ?2 "
                + "  ) "
                + "and oo.TIPOBECAPERIODO_ID in (select id from ENT_TIPO_BECA_PERIODO where TIPOBECA_ID in (38)) "
                + "and oo.periodo_id = ?1 "
                + "and oo.alta = 1";
        List<Object[]> lista = executeNativeQuery(sql, periodoId, otorgamientoId);
        List<Otorgamiento> lo = new ArrayList<>();
        for (Object[] res : lista) {
            Otorgamiento o = getDaos().getOtorgamientoDao().findById((BigDecimal) res[0]);
            lo.add(o);
        }
        return lo.isEmpty() ? null : lo.get(0);
    }
//------------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public PaginateUtil otorgamientosPeriodoAnterior(ServerSideUtil ssu) {
        String storedProcedure = "BajaBecasDiversas";
        String storedProcedureCount = "CuentaBajaBecasDiversas";
        Long noTotal = getStoredProcedureCount(storedProcedureCount, ssu.parametrosServidor);
        ssu.parametrosServidor.put("TIPOBECA", ssu.parametros.get("TIPOBECA"));
        ssu.parametrosServidor.put("ALUMNOBOLETA", ssu.parametros.get("ALUMNOBOLETA"));
        Long noTotalFiltered = getStoredProcedureCount(storedProcedureCount, ssu.parametrosServidor);
        ssu.parametrosServidor.put("START_", ssu.getStart());
        ssu.parametrosServidor.put("END_", ssu.getStart() + ssu.getLength());
        ssu.parametrosServidor.put("ORDENAPOR", ssu.getSortCol());
        ssu.parametrosServidor.put("ORDENADIR", ssu.getSortDir().equals("desc") ? 1 : 2);
        List<Object[]> lista = executeStoredProcedure(storedProcedure, ssu.parametrosServidor);
        return new PaginateUtil(lista, noTotal, noTotalFiltered);
    }

    @Override
    public PaginateUtil otorgamientosPasantia(ServerSideUtil ssu) {
        String storedProcedure = "BajaBecasPasantia";
        String storedProcedureCount = "CuentaBajaBecasPasantia";
        Long noTotal = getStoredProcedureCount(storedProcedureCount, ssu.parametrosServidor);
        ssu.parametrosServidor.put("TIPOBECA", ssu.parametros.get("TIPOBECA"));
        ssu.parametrosServidor.put("ALUMNOBOLETA", ssu.parametros.get("ALUMNOBOLETA"));
        Long noTotalFiltered = getStoredProcedureCount(storedProcedureCount, ssu.parametrosServidor);
        ssu.parametrosServidor.put("START_", ssu.getStart());
        ssu.parametrosServidor.put("END_", ssu.getStart() + ssu.getLength());
        ssu.parametrosServidor.put("ORDENAPOR", ssu.getSortCol());
        ssu.parametrosServidor.put("ORDENADIR", ssu.getSortDir().equals("desc") ? 1 : 2);
        List<Object[]> lista = executeStoredProcedure(storedProcedure, ssu.parametrosServidor);
        return new PaginateUtil(lista, noTotal, noTotalFiltered);
    }

    @Override
    public PaginateUtil otorgamientosIncumplimiento(ServerSideUtil ssu) {
        String storedProcedure = "BajaBecasIncumplimiento";
        String storedProcedureCount = "CuentaBajaBecasIncumplimiento";
        Long noTotal = getStoredProcedureCount(storedProcedureCount, ssu.parametrosServidor);
        ssu.parametrosServidor.put("TIPOBECA", ssu.parametros.get("TIPOBECA"));
        ssu.parametrosServidor.put("ALUMNOBOLETA", ssu.parametros.get("ALUMNOBOLETA"));
        Long noTotalFiltered = getStoredProcedureCount(storedProcedureCount, ssu.parametrosServidor);
        ssu.parametrosServidor.put("START_", ssu.getStart());
        ssu.parametrosServidor.put("END_", ssu.getStart() + ssu.getLength());
        ssu.parametrosServidor.put("ORDENAPOR", ssu.getSortCol());
        ssu.parametrosServidor.put("ORDENADIR", ssu.getSortDir().equals("desc") ? 1 : 2);
        List<Object[]> lista = executeStoredProcedure(storedProcedure, ssu.parametrosServidor);
        return new PaginateUtil(lista, noTotal, noTotalFiltered);
    }

    @Override
    public Boolean incumplimientoPromedio(BigDecimal alumno_id) {
        String consulta = "SELECT o.id  "
                + "FROM ent_otorgamientos o  "
                + "where o.periodo_id = (SELECT periodoanterior_id from cat_periodo p where p.estatus = 1 and rownum = 1)  "
                + "and o.proceso_id IS NOT NULL  "
                + "and o.alumno_id = ?1 "
                + "and (o.id in ( "
                + "SELECT o.id FROM ent_otorgamientos o, ent_alumno a, ent_tipo_beca_periodo tbp, ENT_TIPO_BECA_PERIODO tbb, CAT_TIPO_BECA tb, ENT_ALUMNO_DATOS_ACADEMICOS da   "
                + "where tbp.TIPOBECA_ID = tbb.TIPOBECA_ID  "
                + "and o.DATOSACADEMICOS_ID = da.ID "
                + "and tbp.TIPOBECA_ID = tb.id "
                + "and tb.ORDEN = 0  "
                + "and tbp.PERIODO_ID = (SELECT ID from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "and o.PERIODO_ID = (SELECT periodoanterior_id from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "and O.TIPOBECAPERIODO_ID = tbb.ID "
                + "and o.proceso_id IS NOT NULL "
                + "and a.id = o.alumno_id "
                + "and ( "
                + "  tbp.FASES = 1 "
                + "  and (da.promedio not between tbp.promedioMinimo and tbp.promedioMaximo)                 "
                + "  ) "
                + ") "
                + "or( o.id in ( "
                + "SELECT o.id FROM ent_tipo_beca_periodo tbp, ent_otorgamientos o, ENT_TIPO_BECA_PERIODO TBB, ent_alumno a, CAT_PERIODO p, ENT_ALUMNO_DATOS_ACADEMICOS da  "
                + "where TBB.TIPOBECA_ID = tbp.TIPOBECA_ID "
                + "and o.DATOSACADEMICOS_ID = da.ID "
                + "AND O.TIPOBECAPERIODO_ID = TBB.ID "
                + "and o.proceso_id IS NOT NULL  "
                + "and a.id = o.alumno_id  "
                + "and o.PERIODO_ID = p.ID "
                + "AND tbp.PERIODO_ID = (SELECT ID from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "AND o.PERIODO_ID = (SELECT periodoanterior_id from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "AND ( "
                + "  (tbp.TIPOBECA_ID in (18, 19, 20) and MOD(da.semestre, 2 ) = 1) "
                + "  OR (tbp.TIPOBECA_ID in (32, 33, 34) and MOD(da.semestre, 2 ) = 1 and tbb.FASES = 1 and o.FASE = 1) "
                + "  OR(tbp.TIPOBECA_ID = 21) "
                + "  OR(tbp.TIPOBECA_ID = 35) "
                + "  ) "
                + "and (da.promedio not between tbp.promedioMinimo and tbp.promedioMaximo) "
                + ") "
                + ")or( o.id in ( "
                + "SELECT o.id FROM ent_tipo_beca_periodo tbp, ent_otorgamientos o, ENT_TIPO_BECA_PERIODO TBB, ent_alumno a, CAT_PERIODO p, ENT_ALUMNO_DATOS_ACADEMICOS da  "
                + "where tbp.TIPOBECA_ID = 19 "
                + "and o.DATOSACADEMICOS_ID = da.ID "
                + "AND TBB.TIPOBECA_ID = 18  "
                + "AND MOD(da.semestre, 2 ) = 0 "
                + "AND O.TIPOBECAPERIODO_ID = TBB.ID  "
                + "and o.proceso_id IS NOT NULL "
                + "and a.id = o.alumno_id "
                + "and o.PERIODO_ID = p.ID "
                + "AND tbp.PERIODO_ID = (SELECT ID from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "AND o.PERIODO_ID = (SELECT periodoanterior_id from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "and (da.promedio not between tbp.promedioMinimo and tbp.promedioMaximo) "
                + " "
                + ") "
                + ")or( o.id in ( "
                + "SELECT o.id FROM ent_tipo_beca_periodo tbp, ent_otorgamientos o, ENT_TIPO_BECA_PERIODO TBB, ent_alumno a, CAT_PERIODO p, ENT_ALUMNO_DATOS_ACADEMICOS da   "
                + "where tbp.TIPOBECA_ID = 20 "
                + "and o.DATOSACADEMICOS_ID = da.ID "
                + "AND TBB.TIPOBECA_ID = 19 "
                + "AND MOD(da.semestre, 2 ) = 0 "
                + "AND O.TIPOBECAPERIODO_ID = TBB.ID  "
                + "and o.proceso_id IS NOT NULL  "
                + "and a.id = o.alumno_id "
                + "and o.PERIODO_ID = p.ID "
                + "AND tbp.PERIODO_ID = (SELECT ID from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "AND o.PERIODO_ID = (SELECT periodoanterior_id from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "and ( da.promedio not between tbp.promedioMinimo and tbp.promedioMaximo) "
                + " "
                + ") "
                + ")or( o.id in ( "
                + "SELECT o.id FROM ent_tipo_beca_periodo tbp, ent_otorgamientos o, ENT_TIPO_BECA_PERIODO TBB, ent_alumno a, CAT_PERIODO p, ENT_ALUMNO_DATOS_ACADEMICOS da  "
                + "where tbp.TIPOBECA_ID = 21 "
                + "and o.DATOSACADEMICOS_ID = da.ID "
                + "AND TBB.TIPOBECA_ID = 20 "
                + "AND MOD(da.semestre, 2 ) = 0 "
                + "AND O.TIPOBECAPERIODO_ID = TBB.ID "
                + "and o.proceso_id IS NOT NULL  "
                + "and a.id = o.alumno_id "
                + "and o.PERIODO_ID = p.ID "
                + "AND tbp.PERIODO_ID = (SELECT ID from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "AND o.PERIODO_ID = (SELECT periodoanterior_id from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "and (da.promedio not between tbp.promedioMinimo and tbp.promedioMaximo) "
                + " "
                + ") "
                + ")or( o.id in ( "
                + "SELECT o.id FROM ent_tipo_beca_periodo tbp, ent_otorgamientos o, ENT_TIPO_BECA_PERIODO TBB, ent_alumno a, CAT_PERIODO p, ENT_ALUMNO_DATOS_ACADEMICOS da  "
                + "where tbp.TIPOBECA_ID = 33 "
                + "and o.DATOSACADEMICOS_ID = da.ID "
                + "AND TBB.TIPOBECA_ID = 32 "
                + "AND O.TIPOBECAPERIODO_ID = TBB.ID "
                + "and o.proceso_id IS NOT NULL  "
                + "and a.id = o.alumno_id "
                + "and o.PERIODO_ID = p.ID "
                + "AND tbp.PERIODO_ID = (SELECT ID from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "AND o.PERIODO_ID = (SELECT periodoanterior_id from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "AND ( "
                + "  (tbb.FASES = 2 and o.FASE = 2)  "
                + "  or "
                + "  ( MOD(da.semestre, 2 ) = 0 and tbb.FASES = 1 and o.FASE = 1)  "
                + "  ) "
                + "and (da.promedio not between tbp.promedioMinimo and tbp.promedioMaximo) "
                + " "
                + ") "
                + ")or( o.id in ( "
                + "SELECT o.id FROM ent_tipo_beca_periodo tbp, ent_otorgamientos o, ENT_TIPO_BECA_PERIODO TBB, ent_alumno a, CAT_PERIODO p, ENT_ALUMNO_DATOS_ACADEMICOS da   "
                + "where tbp.TIPOBECA_ID = 34 "
                + "and o.DATOSACADEMICOS_ID = da.ID "
                + "AND TBB.TIPOBECA_ID = 33 "
                + "AND O.TIPOBECAPERIODO_ID = TBB.ID "
                + "and o.proceso_id IS NOT NULL "
                + "and a.id = o.alumno_id  "
                + "and o.PERIODO_ID = p.ID "
                + "AND tbp.PERIODO_ID = (SELECT ID from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "AND o.PERIODO_ID = (SELECT periodoanterior_id from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "AND ( "
                + "  (tbb.FASES = 2 and o.FASE = 2) "
                + "  or  "
                + "  ( MOD(da.semestre, 2 ) = 0 and tbb.FASES = 1 and o.FASE = 1)  "
                + "  ) "
                + "and (da.promedio not between tbp.promedioMinimo and tbp.promedioMaximo) "
                + " "
                + ") "
                + ") "
                + "or( o.id in ( "
                + "SELECT o.id FROM ent_tipo_beca_periodo tbp, ent_otorgamientos o, ENT_TIPO_BECA_PERIODO TBB, ent_alumno a, CAT_PERIODO p, ENT_ALUMNO_DATOS_ACADEMICOS da  "
                + "where tbp.TIPOBECA_ID = 35 "
                + "and o.DATOSACADEMICOS_ID = da.ID "
                + "AND TBB.TIPOBECA_ID = 34 "
                + "AND O.TIPOBECAPERIODO_ID = TBB.ID "
                + "and o.proceso_id IS NOT NULL "
                + "and a.id = o.alumno_id "
                + "and o.PERIODO_ID = p.ID "
                + "AND tbp.PERIODO_ID = (SELECT ID from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "AND o.PERIODO_ID = (SELECT periodoanterior_id from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "AND (  "
                + "  (tbb.FASES = 2 and o.FASE = 2)                  "
                + "  or  "
                + "  ( MOD(da.semestre, 2 ) = 0 and tbb.FASES = 1 and o.FASE = 1) "
                + "  )         "
                + "and (da.promedio not between tbp.promedioMinimo and tbp.promedioMaximo ) "
                + " "
                + ") "
                + ") "
                + ")";
        List<Object[]> lista = executeNativeQuery(consulta, alumno_id);
        return (lista != null && !lista.isEmpty());
    }

    @Override
    public Boolean incumplimientoSemestre(BigDecimal alumno_id) {
        String consulta = "SELECT o.id  "
                + "FROM ent_otorgamientos o  "
                + "where o.periodo_id = (SELECT periodoanterior_id from cat_periodo p where p.estatus = 1 and rownum = 1)  "
                + "and o.proceso_id IS NOT NULL  "
                + "and o.alumno_id = ?1 "
                + "and (o.id in ( "
                + "SELECT o.id FROM ent_otorgamientos o, ent_alumno a, ent_tipo_beca_periodo tbp, ENT_TIPO_BECA_PERIODO tbb, CAT_TIPO_BECA tb, ENT_ALUMNO_DATOS_ACADEMICOS da "
                + "where tbp.TIPOBECA_ID = tbb.TIPOBECA_ID "
                + "and o.DATOSACADEMICOS_ID = da.ID "
                + "and tbp.TIPOBECA_ID = tb.id "
                + "and tb.ORDEN = 0  "
                + "and tbp.PERIODO_ID = (SELECT ID from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "and o.PERIODO_ID = (SELECT periodoanterior_id from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "and O.TIPOBECAPERIODO_ID = tbb.ID "
                + "and o.proceso_id IS NOT NULL "
                + "and a.id = o.alumno_id "
                + "and ( "
                + "  tbp.FASES = 1 "
                + "  and (da.semestre not between tbp.semestreMinimo and tbp.semestreMaximo)                 "
                + "  ) "
                + ") "
                + "or( o.id in ( "
                + "SELECT o.id FROM ent_tipo_beca_periodo tbp, ent_otorgamientos o, ENT_TIPO_BECA_PERIODO TBB, ent_alumno a, CAT_PERIODO p, ENT_ALUMNO_DATOS_ACADEMICOS da "
                + "where TBB.TIPOBECA_ID = tbp.TIPOBECA_ID "
                + "and o.DATOSACADEMICOS_ID = da.ID "
                + "AND O.TIPOBECAPERIODO_ID = TBB.ID "
                + "and o.proceso_id IS NOT NULL  "
                + "and a.id = o.alumno_id  "
                + "and o.PERIODO_ID = p.ID "
                + "AND tbp.PERIODO_ID = (SELECT ID from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "AND o.PERIODO_ID = (SELECT periodoanterior_id from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "AND ( "
                + "  (tbp.TIPOBECA_ID in (18, 19, 20) and MOD(da.semestre, 2 ) = 1) "
                + "  OR (tbp.TIPOBECA_ID in (32, 33, 34) and MOD(da.semestre, 2 ) = 1 and tbb.FASES = 1 and o.FASE = 1) "
                + "  OR(tbp.TIPOBECA_ID = 21) "
                + "  OR(tbp.TIPOBECA_ID = 35) "
                + "  ) "
                + "and (da.semestre not between tbp.semestreMinimo and tbp.semestreMaximo) "
                + ") "
                + ")or( o.id in ( "
                + "SELECT o.id FROM ent_tipo_beca_periodo tbp, ent_otorgamientos o, ENT_TIPO_BECA_PERIODO TBB, ent_alumno a, CAT_PERIODO p, ENT_ALUMNO_DATOS_ACADEMICOS da "
                + "where tbp.TIPOBECA_ID = 19 "
                + "and o.DATOSACADEMICOS_ID = da.ID "
                + "AND TBB.TIPOBECA_ID = 18  "
                + "AND MOD(da.semestre, 2 ) = 0 "
                + "AND O.TIPOBECAPERIODO_ID = TBB.ID  "
                + "and o.proceso_id IS NOT NULL "
                + "and a.id = o.alumno_id "
                + "and o.PERIODO_ID = p.ID "
                + "AND tbp.PERIODO_ID = (SELECT ID from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "AND o.PERIODO_ID = (SELECT periodoanterior_id from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "and (da.semestre not between tbp.semestreMinimo and tbp.semestreMaximo) "
                + " "
                + ") "
                + ")or( o.id in ( "
                + "SELECT o.id FROM ent_tipo_beca_periodo tbp, ent_otorgamientos o, ENT_TIPO_BECA_PERIODO TBB, ent_alumno a, CAT_PERIODO p, ENT_ALUMNO_DATOS_ACADEMICOS da "
                + "where tbp.TIPOBECA_ID = 20 "
                + "and o.DATOSACADEMICOS_ID = da.ID "
                + "AND TBB.TIPOBECA_ID = 19 "
                + "AND MOD(da.semestre, 2 ) = 0 "
                + "AND O.TIPOBECAPERIODO_ID = TBB.ID  "
                + "and o.proceso_id IS NOT NULL  "
                + "and a.id = o.alumno_id "
                + "and o.PERIODO_ID = p.ID "
                + "AND tbp.PERIODO_ID = (SELECT ID from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "AND o.PERIODO_ID = (SELECT periodoanterior_id from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "and ( da.semestre not between tbp.semestreMinimo and tbp.semestreMaximo) "
                + " "
                + ") "
                + ")or( o.id in ( "
                + "SELECT o.id FROM ent_tipo_beca_periodo tbp, ent_otorgamientos o, ENT_TIPO_BECA_PERIODO TBB, ent_alumno a, CAT_PERIODO p, ENT_ALUMNO_DATOS_ACADEMICOS da  "
                + "where tbp.TIPOBECA_ID = 21 "
                + "and o.DATOSACADEMICOS_ID = da.ID "
                + "AND TBB.TIPOBECA_ID = 20 "
                + "AND MOD(da.semestre, 2 ) = 0 "
                + "AND O.TIPOBECAPERIODO_ID = TBB.ID "
                + "and o.proceso_id IS NOT NULL  "
                + "and a.id = o.alumno_id "
                + "and o.PERIODO_ID = p.ID "
                + "AND tbp.PERIODO_ID = (SELECT ID from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "AND o.PERIODO_ID = (SELECT periodoanterior_id from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "and (da.semestre not between tbp.semestreMinimo and tbp.semestreMaximo) "
                + " "
                + ") "
                + ")or( o.id in ( "
                + "SELECT o.id FROM ent_tipo_beca_periodo tbp, ent_otorgamientos o, ENT_TIPO_BECA_PERIODO TBB, ent_alumno a, CAT_PERIODO p, ENT_ALUMNO_DATOS_ACADEMICOS da  "
                + "where tbp.TIPOBECA_ID = 33 "
                + "and o.DATOSACADEMICOS_ID = da.ID "
                + "AND TBB.TIPOBECA_ID = 32 "
                + "AND O.TIPOBECAPERIODO_ID = TBB.ID "
                + "and o.proceso_id IS NOT NULL  "
                + "and a.id = o.alumno_id "
                + "and o.PERIODO_ID = p.ID "
                + "AND tbp.PERIODO_ID = (SELECT ID from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "AND o.PERIODO_ID = (SELECT periodoanterior_id from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "AND ( "
                + "  (tbb.FASES = 2 and o.FASE = 2)  "
                + "  or "
                + "  ( MOD(da.semestre, 2 ) = 0 and tbb.FASES = 1 and o.FASE = 1)  "
                + "  ) "
                + "and (da.semestre not between tbp.semestreMinimo and tbp.semestreMaximo) "
                + " "
                + ") "
                + ")or( o.id in ( "
                + "SELECT o.id FROM ent_tipo_beca_periodo tbp, ent_otorgamientos o, ENT_TIPO_BECA_PERIODO TBB, ent_alumno a, CAT_PERIODO p, ENT_ALUMNO_DATOS_ACADEMICOS da   "
                + "where tbp.TIPOBECA_ID = 34 "
                + "and o.DATOSACADEMICOS_ID = da.ID "
                + "AND TBB.TIPOBECA_ID = 33 "
                + "AND O.TIPOBECAPERIODO_ID = TBB.ID "
                + "and o.proceso_id IS NOT NULL "
                + "and a.id = o.alumno_id  "
                + "and o.PERIODO_ID = p.ID "
                + "AND tbp.PERIODO_ID = (SELECT ID from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "AND o.PERIODO_ID = (SELECT periodoanterior_id from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "AND ( "
                + "  (tbb.FASES = 2 and o.FASE = 2) "
                + "  or  "
                + "  ( MOD(da.semestre, 2 ) = 0 and tbb.FASES = 1 and o.FASE = 1)  "
                + "  ) "
                + "and (da.semestre not between tbp.semestreMinimo and tbp.semestreMaximo) "
                + " "
                + ") "
                + ") "
                + "or( o.id in ( "
                + "SELECT o.id FROM ent_tipo_beca_periodo tbp, ent_otorgamientos o, ENT_TIPO_BECA_PERIODO TBB, ent_alumno a, CAT_PERIODO p, ENT_ALUMNO_DATOS_ACADEMICOS da "
                + "where tbp.TIPOBECA_ID = 35 "
                + "and o.DATOSACADEMICOS_ID = da.ID "
                + "AND TBB.TIPOBECA_ID = 34 "
                + "AND O.TIPOBECAPERIODO_ID = TBB.ID "
                + "and o.proceso_id IS NOT NULL "
                + "and a.id = o.alumno_id "
                + "and o.PERIODO_ID = p.ID "
                + "AND tbp.PERIODO_ID = (SELECT ID from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "AND o.PERIODO_ID = (SELECT periodoanterior_id from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "AND (  "
                + "  (tbb.FASES = 2 and o.FASE = 2)                  "
                + "  or  "
                + "  ( MOD(da.semestre, 2 ) = 0 and tbb.FASES = 1 and o.FASE = 1) "
                + "  )         "
                + "and (da.semestre not between tbp.semestreMinimo and tbp.semestreMaximo ) "
                + " "
                + ") "
                + ") "
                + ")";
        List<Object[]> lista = executeNativeQuery(consulta, alumno_id);
        return (lista != null && !lista.isEmpty());
    }

    @Override
    public Boolean tieneOtorgamientoHistorico(BigDecimal alumno_id) {
        String consulta = "SELECT * FROM VW_OTORGAMIENTOS WHERE ALUMNO_ID = ?1";
        List<Object[]> lista = executeNativeQuery(consulta, alumno_id);
        return (lista != null && !lista.isEmpty());
    }

    @Override
    public Object[] incumplimientoIngreso(BigDecimal alumno_id) {
        String consulta = "SELECT o.id, r.ID, tbp.INGRESOSALARIOS "
                + "FROM ent_otorgamientos o, ent_alumno a, ent_tipo_beca_periodo tbp, ENT_TIPO_BECA_PERIODO tbb, CAT_TIPO_BECA tb, RMM_CUESTIONARIO_PREGUNTA_RESP cpr, ENT_CUESTIONARIO_RESPUESTA r "
                + "where tbp.TIPOBECA_ID = tbb.TIPOBECA_ID   "
                + "and tbp.TIPOBECA_ID = tb.id  "
                + "and tb.ORDEN = 0   "
                + "and tbp.PERIODO_ID = (SELECT ID from cat_periodo p where p.estatus = 1 and rownum = 1)  "
                + "and o.PERIODO_ID = (SELECT periodoanterior_id from cat_periodo p where p.estatus = 1 and rownum = 1)  "
                + "and O.TIPOBECAPERIODO_ID = tbb.ID  "
                + "and o.proceso_id is not null "
                + "and a.id = o.alumno_id  "
                + "and a.id = ?1 "
                + "and cpr.PERIODO_ID = tbp.PERIODO_ID "
                + "and cpr.USUARIO_ID = a.USUARIO_ID "
                + "and cpr.CUESTIONARIO_ID = 1 "
                + "and cpr.PREGUNTA_ID = 13 "
                + "and cpr.RESPUESTA_ID = r.ID "
                + "and tbp.INGRESOSALARIOS is not null "
                + "and (  "
                + "  tbp.FASES = 1  "
                + ") "
                + "UNION "
                + "SELECT o.id, r.ID, tbp.INGRESOSALARIOS "
                + "FROM ent_tipo_beca_periodo tbp, ent_otorgamientos o, ENT_TIPO_BECA_PERIODO TBB, ent_alumno a, CAT_PERIODO p, RMM_CUESTIONARIO_PREGUNTA_RESP cpr, ENT_CUESTIONARIO_RESPUESTA r, ENT_ALUMNO_DATOS_ACADEMICOS da  "
                + "where TBB.TIPOBECA_ID = tbp.TIPOBECA_ID  "
                + "AND O.TIPOBECAPERIODO_ID = TBB.ID  "
                + "and o.proceso_id IS NOT NULL   "
                + "and a.id = o.alumno_id   "
                + "and o.PERIODO_ID = p.ID  "
                + "AND tbp.PERIODO_ID = (SELECT ID from cat_periodo p where p.estatus = 1 and rownum = 1)  "
                + "AND o.PERIODO_ID = (SELECT periodoanterior_id from cat_periodo p where p.estatus = 1 and rownum = 1)  "
                + "and a.id = ?1 "
                + "and cpr.PERIODO_ID = tbp.PERIODO_ID "
                + "and cpr.USUARIO_ID = a.USUARIO_ID "
                + "and cpr.CUESTIONARIO_ID = 1 "
                + "and cpr.PREGUNTA_ID = 13 "
                + "and cpr.RESPUESTA_ID = r.ID "
                + "and tbp.INGRESOSALARIOS is not null "
                + "AND (  "
                + "  (tbp.TIPOBECA_ID in (18, 19, 20) and MOD(da.semestre, 2 ) = 1)  "
                + "  OR (tbp.TIPOBECA_ID in (32, 33, 34) and MOD(da.semestre, 2 ) = 1 and tbb.FASES = 1 and o.FASE = 1)  "
                + "  OR(tbp.TIPOBECA_ID = 21)  "
                + "  OR(tbp.TIPOBECA_ID = 35)  "
                + "  )  "
                + "UNION "
                + "SELECT o.id, r.ID, tbp.INGRESOSALARIOS  "
                + "FROM ent_tipo_beca_periodo tbp, ent_otorgamientos o, ENT_TIPO_BECA_PERIODO TBB, ent_alumno a, CAT_PERIODO p, RMM_CUESTIONARIO_PREGUNTA_RESP cpr, ENT_CUESTIONARIO_RESPUESTA r, ENT_ALUMNO_DATOS_ACADEMICOS da  "
                + "where tbp.TIPOBECA_ID = 19  "
                + "AND TBB.TIPOBECA_ID = 18   "
                + "AND MOD(da.semestre, 2 ) = 0  "
                + "AND O.TIPOBECAPERIODO_ID = TBB.ID   "
                + "and o.proceso_id IS NOT NULL  "
                + "and a.id = o.alumno_id  "
                + "and o.PERIODO_ID = p.ID  "
                + "and a.id = ?1 "
                + "and cpr.PERIODO_ID = tbp.PERIODO_ID "
                + "and cpr.USUARIO_ID = a.USUARIO_ID "
                + "and cpr.CUESTIONARIO_ID = 1 "
                + "and cpr.PREGUNTA_ID = 13 "
                + "and cpr.RESPUESTA_ID = r.ID "
                + "and tbp.INGRESOSALARIOS is not null "
                + "AND tbp.PERIODO_ID = (SELECT ID from cat_periodo p where p.estatus = 1 and rownum = 1)  "
                + "AND o.PERIODO_ID = (SELECT periodoanterior_id from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "UNION "
                + "SELECT o.id, r.ID, tbp.INGRESOSALARIOS  "
                + "FROM ent_tipo_beca_periodo tbp, ent_otorgamientos o, ENT_TIPO_BECA_PERIODO TBB, ent_alumno a, CAT_PERIODO p, RMM_CUESTIONARIO_PREGUNTA_RESP cpr, ENT_CUESTIONARIO_RESPUESTA r, ENT_ALUMNO_DATOS_ACADEMICOS da  "
                + "where tbp.TIPOBECA_ID = 20  "
                + "AND TBB.TIPOBECA_ID = 19  "
                + "AND MOD(da.semestre, 2 ) = 0  "
                + "AND O.TIPOBECAPERIODO_ID = TBB.ID   "
                + "and o.proceso_id IS NOT NULL   "
                + "and a.id = o.alumno_id  "
                + "and o.PERIODO_ID = p.ID  "
                + "and a.id = ?1 "
                + "and cpr.PERIODO_ID = tbp.PERIODO_ID "
                + "and cpr.USUARIO_ID = a.USUARIO_ID "
                + "and cpr.CUESTIONARIO_ID = 1 "
                + "and cpr.PREGUNTA_ID = 13 "
                + "and cpr.RESPUESTA_ID = r.ID "
                + "and tbp.INGRESOSALARIOS is not null "
                + "AND tbp.PERIODO_ID = (SELECT ID from cat_periodo p where p.estatus = 1 and rownum = 1)  "
                + "AND o.PERIODO_ID = (SELECT periodoanterior_id from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "UNION "
                + "SELECT o.id, r.ID, tbp.INGRESOSALARIOS  "
                + "FROM ent_tipo_beca_periodo tbp, ent_otorgamientos o, ENT_TIPO_BECA_PERIODO TBB, ent_alumno a, CAT_PERIODO p, RMM_CUESTIONARIO_PREGUNTA_RESP cpr, ENT_CUESTIONARIO_RESPUESTA r, ENT_ALUMNO_DATOS_ACADEMICOS da  "
                + "where tbp.TIPOBECA_ID = 21  "
                + "AND TBB.TIPOBECA_ID = 20  "
                + "AND MOD(da.semestre, 2 ) = 0  "
                + "AND O.TIPOBECAPERIODO_ID = TBB.ID  "
                + "and o.proceso_id IS NOT NULL   "
                + "and a.id = o.alumno_id  "
                + "and o.PERIODO_ID = p.ID  "
                + "and a.id = ?1 "
                + "and cpr.PERIODO_ID = tbp.PERIODO_ID "
                + "and cpr.USUARIO_ID = a.USUARIO_ID "
                + "and cpr.CUESTIONARIO_ID = 1 "
                + "and cpr.PREGUNTA_ID = 13 "
                + "and cpr.RESPUESTA_ID = r.ID "
                + "and tbp.INGRESOSALARIOS is not null "
                + "AND tbp.PERIODO_ID = (SELECT ID from cat_periodo p where p.estatus = 1 and rownum = 1)  "
                + "AND o.PERIODO_ID = (SELECT periodoanterior_id from cat_periodo p where p.estatus = 1 and rownum = 1)  "
                + "UNION "
                + "SELECT o.id, r.ID, tbp.INGRESOSALARIOS  "
                + "FROM ent_tipo_beca_periodo tbp, ent_otorgamientos o, ENT_TIPO_BECA_PERIODO TBB, ent_alumno a, CAT_PERIODO p, RMM_CUESTIONARIO_PREGUNTA_RESP cpr, ENT_CUESTIONARIO_RESPUESTA r, ENT_ALUMNO_DATOS_ACADEMICOS da  "
                + "where tbp.TIPOBECA_ID = 33  "
                + "AND TBB.TIPOBECA_ID = 32  "
                + "AND O.TIPOBECAPERIODO_ID = TBB.ID  "
                + "and o.proceso_id IS NOT NULL   "
                + "and a.id = o.alumno_id  "
                + "and o.PERIODO_ID = p.ID  "
                + "and a.id = ?1 "
                + "and cpr.PERIODO_ID = tbp.PERIODO_ID "
                + "and cpr.USUARIO_ID = a.USUARIO_ID "
                + "and cpr.CUESTIONARIO_ID = 1 "
                + "and cpr.PREGUNTA_ID = 13 "
                + "and cpr.RESPUESTA_ID = r.ID "
                + "and tbp.INGRESOSALARIOS is not null "
                + "AND tbp.PERIODO_ID = (SELECT ID from cat_periodo p where p.estatus = 1 and rownum = 1)  "
                + "AND o.PERIODO_ID = (SELECT periodoanterior_id from cat_periodo p where p.estatus = 1 and rownum = 1)  "
                + "AND (  "
                + "  (tbb.FASES = 2 and o.FASE = 2)   "
                + "  or  "
                + "  ( MOD(da.semestre, 2 ) = 0 and tbb.FASES = 1 and o.FASE = 1)   "
                + "  )  "
                + "UNION "
                + "SELECT o.id, r.ID, tbp.INGRESOSALARIOS  "
                + "FROM ent_tipo_beca_periodo tbp, ent_otorgamientos o, ENT_TIPO_BECA_PERIODO TBB, ent_alumno a, CAT_PERIODO p, RMM_CUESTIONARIO_PREGUNTA_RESP cpr, ENT_CUESTIONARIO_RESPUESTA r, ENT_ALUMNO_DATOS_ACADEMICOS da  "
                + "where tbp.TIPOBECA_ID = 34  "
                + "AND TBB.TIPOBECA_ID = 33  "
                + "AND O.TIPOBECAPERIODO_ID = TBB.ID  "
                + "and o.proceso_id IS NOT NULL  "
                + "and a.id = o.alumno_id   "
                + "and o.PERIODO_ID = p.ID  "
                + "AND tbp.PERIODO_ID = (SELECT ID from cat_periodo p where p.estatus = 1 and rownum = 1)  "
                + "AND o.PERIODO_ID = (SELECT periodoanterior_id from cat_periodo p where p.estatus = 1 and rownum = 1)  "
                + "and a.id = ?1 "
                + "and cpr.PERIODO_ID = tbp.PERIODO_ID "
                + "and cpr.USUARIO_ID = a.USUARIO_ID "
                + "and cpr.CUESTIONARIO_ID = 1 "
                + "and cpr.PREGUNTA_ID = 13 "
                + "and cpr.RESPUESTA_ID = r.ID "
                + "and tbp.INGRESOSALARIOS is not null "
                + "AND (  "
                + "  (tbb.FASES = 2 and o.FASE = 2)  "
                + "  or   "
                + "  ( MOD(da.semestre, 2 ) = 0 and tbb.FASES = 1 and o.FASE = 1)   "
                + "  )  "
                + "UNION "
                + "SELECT o.id, r.ID, tbp.INGRESOSALARIOS "
                + "FROM ent_tipo_beca_periodo tbp, ent_otorgamientos o, ENT_TIPO_BECA_PERIODO TBB, ent_alumno a, CAT_PERIODO p, RMM_CUESTIONARIO_PREGUNTA_RESP cpr, ENT_CUESTIONARIO_RESPUESTA r, ENT_ALUMNO_DATOS_ACADEMICOS da  "
                + "where tbp.TIPOBECA_ID = 35  "
                + "AND TBB.TIPOBECA_ID = 34  "
                + "AND O.TIPOBECAPERIODO_ID = TBB.ID  "
                + "and o.proceso_id IS NOT NULL  "
                + "and a.id = o.alumno_id  "
                + "and o.PERIODO_ID = p.ID  "
                + "and a.id = ?1 "
                + "and cpr.PERIODO_ID = tbp.PERIODO_ID "
                + "and cpr.USUARIO_ID = a.USUARIO_ID "
                + "and cpr.CUESTIONARIO_ID = 1 "
                + "and cpr.PREGUNTA_ID = 13 "
                + "and cpr.RESPUESTA_ID = r.ID "
                + "and tbp.INGRESOSALARIOS is not null "
                + "AND tbp.PERIODO_ID = (SELECT ID from cat_periodo p where p.estatus = 1 and rownum = 1)  "
                + "AND o.PERIODO_ID = (SELECT periodoanterior_id from cat_periodo p where p.estatus = 1 and rownum = 1)  "
                + "AND (   "
                + "  (tbb.FASES = 2 and o.FASE = 2)                   "
                + "  or   "
                + "  ( MOD(da.semestre, 2 ) = 0 and tbb.FASES = 1 and o.FASE = 1)  "
                + "  )";
        List<Object[]> lista = executeNativeQuery(consulta, alumno_id);
        return ((lista == null || lista.isEmpty()) ? null : lista.get(0));
    }

    @Override
    public Object[] incumplimientoIngresoNuevos(BigDecimal alumno_id) {
        String consulta = "SELECT o.id, r.id, tbp.INGRESOSALARIOS "
                + "FROM ent_otorgamientos o, ent_alumno a, ent_tipo_beca_periodo tbp, RMM_CUESTIONARIO_PREGUNTA_RESP cpr, ENT_CUESTIONARIO_RESPUESTA r "
                + "where tbp.PERIODO_ID = (SELECT ID from cat_periodo p where p.estatus = 1 and rownum = 1)  "
                + "and o.PERIODO_ID = tbp.PERIODO_ID  "
                + "and o.TIPOBECAPERIODO_ID = tbp.id "
                + "and o.proceso_id is null "
                + "and a.id = o.alumno_id "
                + "and cpr.USUARIO_ID = a.USUARIO_ID "
                + "and cpr.PERIODO_ID = tbp.PERIODO_ID "
                + "and cpr.CUESTIONARIO_ID = 1 "
                + "and cpr.PREGUNTA_ID = 13 "
                + "and cpr.RESPUESTA_ID = r.ID "
                + "and tbp.INGRESOSALARIOS is not null "
                + "and a.id = ?1 ";
        List<Object[]> lista = executeNativeQuery(consulta, alumno_id);
        return ((lista == null || lista.isEmpty()) ? null : lista.get(0));
    }

//-------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public List<Otorgamiento> revalidantesOrdenados() {

        String sql = "select a.id alumno_id, (select pe.id from cat_periodo pe where  pe.estatus = 1 and rownum = 1) periodo_id, "
                + " tbp.id tipobecaperiodo_id, a.semestre, a.promedio from vw_alumno a "
                + "   join ent_solicitud_becas cpa on a.id = cpa.alumno_id "
                + "   join ent_otorgamientos o on a.id = o.alumno_id "
                + "   join ent_tipo_beca_periodo tbp on tbp.id = o.tipobecaperiodo_id "
                + " where a.estatus=1 "
                + " and cpa.periodo_id= (select pe.id from cat_periodo pe where pe.estatus = 1 and rownum = 1) "
                + " and o.periodo_id = (select pe.periodoanterior_id from cat_periodo pe where pe.estatus = 1 and rownum = 1)"
                + " and a.semestre between tbp.semestreMinimo and tbp.semestreMaximo "
                + " and a.promedio between tbp.promedioMinimo and tbp.promedioMaximo "
                + " order by a.unidadacademica_id, o.tipobecaperiodo_id, a.promedio desc";
        List<Object[]> lista = executeNativeQuery(sql);
        List<Otorgamiento> lo = new ArrayList<Otorgamiento>();
        for (Object[] res : lista) {
            Otorgamiento o = new Otorgamiento();
            //Alumno
            Alumno alumno = new Alumno();
            alumno.setId((BigDecimal) res[0]);
            o.setAlumno(alumno);
            //Periodo
            Periodo periodo = new Periodo();
            periodo.setId((BigDecimal) res[1]);
            o.setPeriodo(periodo);
            //TipoBecaPeriodo
            TipoBecaPeriodo tipoBecaPeriodo = new TipoBecaPeriodo();
            tipoBecaPeriodo.setId((BigDecimal) res[2]);
            o.setTipoBecaPeriodo(tipoBecaPeriodo);
            //Usuario
            Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
            o.setUsuario(usuario);
            o.setFecha(new Date());
            DatosAcademicos datosAcademicos = new DatosAcademicos();
            datosAcademicos.setSemestre(((BigDecimal) res[3]).intValue());
            datosAcademicos.setPromedio(((BigDecimal) res[4]).doubleValue());
            o.setDatosAcademicos(datosAcademicos);
            o.setAlta(Boolean.TRUE);
            o.setAutomatico(Boolean.TRUE);
            o.setAsignacionConfirmada(Boolean.TRUE);
            lo.add(o);
        }
        return lo;
    }

    //SQLINJECTION
    @Override
    public List<Object[]> buscarOrdenDeposito(Boolean groupby, BigDecimal periodoId,
            BigDecimal mes, Integer origenRecursos, BigDecimal programaBeca, BigDecimal nivelAcademico,
            BigDecimal uAcademica, BigDecimal tipoProceso, BigDecimal idOtorgamiento,
            String fechaDeposito, BigDecimal formaPago, BigDecimal tipoDeposito, int determinacionRecursos,
            ConvocatoriaSubes convocatoriaSubes) {

        Periodo periodo = getDaos().getPeriodoDao().findById(periodoId);
        String uaps = "";
        String ps = "";
        if (uAcademica.compareTo(new BigDecimal(0)) == 0) {

        } else {
            uaps = uaps + " \n and ua.id = " + uAcademica;
        }
        if (programaBeca.compareTo(new BigDecimal(5)) == 0 || programaBeca.compareTo(new BigDecimal(7)) == 0) {
            //No incluir dua.correspondeipn = X debido a que provoca que no termine de resolver la consulta
        } else {
            uaps = uaps + " \n and dua.correspondeipn = " + origenRecursos;
        }
        if (tipoProceso.compareTo(new BigDecimal(0)) == 0) {
        } else {
            uaps = uaps + " \n and tp.id = " + tipoProceso;
        }
        if (idOtorgamiento.compareTo(new BigDecimal(0)) == 0) {
        } else {
            uaps = uaps + " \n and o.identificadorotorgamiento_id = " + idOtorgamiento;
        }

        if (convocatoriaSubes != null && convocatoriaSubes.getId() != null
                && (programaBeca.compareTo(new BigDecimal(5)) == 0 || programaBeca.compareTo(new BigDecimal(7)) == 0)) { //Sólo para manutención o transporte manutención
            uaps = uaps + " \n and tbp.CONVOCATORIAMANUTENCION  = " + convocatoriaSubes.getId();
        }
        if (periodoId.intValue() >= 33) {
            if (periodo.getClave().substring(5, 6).equals("1")) {
                if (programaBeca.equals(new BigDecimal("5"))) {
                    ps = " \n join ent_padron_subes ps on ps.alumno_id = a.id and ps.periodo_id = p.id ";
                    uaps = uaps + " and ps.estatussubes = 'Aceptado' \n";
                } else if (programaBeca.equals(new BigDecimal("7"))) {
                    ps = " \n join ent_padron_subes ps on ps.alumno_id = a.id and ps.periodo_id = p.id";
                    uaps = uaps + " and ps.estatustransporte = 'Aceptado'";
                }
            } else if (programaBeca.equals(new BigDecimal("5"))) {
                ps = " \n join ent_padron_subes ps on ps.alumno_id = a.id and ps.periodo_id in"
                        + " (select periodo_id from ent_padron_subes where (periodo_id = " + periodo.getId() + " or periodo_id = " + periodo.getPeriodoAnterior().getId() + ")"
                        + " and rownum = 1 and alumno_id = a.id  and estatussubes = 'Aceptado' ) and estatussubes = 'Aceptado' ";
            } else if (programaBeca.equals(new BigDecimal("7"))) {
                ps = " \n join ent_padron_subes ps on ps.alumno_id = a.id and ps.periodo_id in"
                        + " (select periodo_id from ent_padron_subes where (periodo_id = " + periodo.getId() + " or periodo_id = " + periodo.getPeriodoAnterior().getId() + ")"
                        + " and rownum = 1 and alumno_id = a.id  and estatussubes = 'Aceptado' ) and estatustransporte = 'Aceptado'";
            }
        }
        String sql = " \nselect"
                + (groupby ? " ua.id, ua.nombrecorto, tb.id,tb.nombre, count(*), sum(tbp.monto), sum(tbp.montovariable)"
                        : " o.id, a.id, ua.id, tb.id, tbp.id, tbp.monto, tbp.montovariable, bmp.monto, ua.nombrecorto, tb.nombre, atb.tarjetabancaria_id, de.deposito_id")
                + " \n from ent_otorgamientos o"
                + " \n join cat_periodo p on p.id = o.periodo_id"
                + " \n join ent_alumno a on a.id = o.alumno_id"
                + " \n join rmm_alumno_tarjeta_bancaria atb on atb.alumno_id = o.alumno_id and atb.estatustarjbanc_id = " + (formaPago.equals(new BigDecimal(2)) ? 17 : 13) //Estatus 13 para cuenta y clabe, 17 para referencia
                //+ "   join ent_tarjeta_bancaria tab on tab.id = atb.tarjetabancaria_id"
                + " \n join ent_proceso pr on pr.id = o.proceso_id"
                + " \n join cat_unidad_academica ua on ua.id = pr.unidadacademica_id"
                + " \n join cat_nivel n on n.id = ua.nivel_id"
                + " \n join ent_tipo_beca_periodo tbp on tbp.id = o.tipobecaperiodo_id"
                + " \n join cat_tipo_beca tb on tb.id = tbp.tipobeca_id"
                + " \n join cat_programa_beca pb on pb.id = tb.beca_id"
                + " \n join cat_tipo_proceso tp on  tp.id = pr.tipoproceso_id"
                //                + " \n join vw_conteo_depositos cd on cd.alumno_id = a.id and cd.periodo_id = p.id and o.id = cd.otorgamiento_id"
                + " \n join rmm_deposito_unidad_academica dua on dua.tipobeca_id = tb.id and dua.unidadacademica_id = ua.id"
                + " \n left join cat_beca_monto_variable bmp on bmp.tipobecaperiodo_id = tbp.id and TO_DATE('" + fechaDeposito + "','DD/MM/YYYY') between bmp.fechainicial and bmp.fechafinal"
                + " \n left join vw_ultima_baja_detalle obd on obd.periodo_id = p.id and obd.otorgamiento_id = o.id and obd.movimiento_id is not null" + ps
                + " \n left join vw_depositos_estatus de on de.otorgamiento_id = o.id and de.mes = " + mes
                + (tipoDeposito.equals(new BigDecimal("1")) ? " and de.estatusdeposito_id in (1,2,3,4,6)" : " and de.estatusdeposito_id = 4")
                + " \n where p.id = " + periodoId + " and pb.id = " + programaBeca + " and n.id = " + nivelAcademico + uaps
                + " \n and pr.procesoestatus_id = 4 and o.excluirdeposito = 0 and pr.fechafinal < current_timestamp "
                //                + " and cd.conteoDepositos < tbp.duracion"
                + " AND (SELECT COUNT(*)\n"
                + "  FROM ENT_OTORGAMIENTOS o2\n"
                + "       LEFT JOIN ent_deposito d ON o2.id = d.OTORGAMIENTO_ID  and d.ESTATUSDEPOSITO_ID IN (2,3)\n"
                + "       left JOIN ENT_ORDEN_DEPOSITO od ON od.id = d.ORDENDEPOSITO_ID\n"
                + "       WHERE o2.id=o.id AND o2.ALUMNO_ID=o.ALUMNO_ID  and d.alumno_id = o.ALUMNO_ID  and od.periodo_id = o.periodo_id )<tbp.duracion \n"
                //+ " and o.id not in (select obd.otorgamiento_id from ent_otorgamientos_bajas_detall obd where obd.periodo_id = p.id and obd.otorgamiento_id = o.id)"
                + (tipoDeposito.equals(new BigDecimal("1")) ? " \n and de.id is null" : " and de.id is not null")
                + " \n and obd.id is null and exists(select 1 from ent_tarjeta_bancaria tbb where tbb.id = atb.tarjetabancaria_id and tbb.cuenta = " + formaPago + ")" //Se agrega buscar por cuenta, referencia o clabe
                + (determinacionRecursos == 0 ? "" : " \n and o.recursomanutencion = " + determinacionRecursos)
                + (groupby ? " \n group by ua.id,ua.nombrecorto, tb.id,tb.nombre" : "")
                + " \n order by ua.id, tb.id";
        System.out.println("sql ordenes: " + sql);
        List<Object[]> lista = executeNativeQuery(sql);
        return lista;
    }

    @Override
    public List<Otorgamiento> otorgamientosProceso(BigDecimal procesoId) {
        ProcesoBO bo = new ProcesoBO(getDaos());
        Proceso proceso = bo.getProceso(procesoId);
        int movimientoId = proceso.getTipoProceso().getMovimiento().getId().intValue();
        Boolean al = (movimientoId >= 4 && movimientoId <= 6) ? Boolean.TRUE : Boolean.FALSE;
        String consulta;
        if (al) {
            consulta = "SELECT o.* FROM ENT_OTORGAMIENTOS o "
                    + "   join VW_ULTIMA_BAJA_DETALLE obd on obd.OTORGAMIENTO_ID = o.id "
                    + "   join ent_alumno a on a.id = o.alumno_id "
                    + "   join ent_tipo_beca_periodo tbp on tbp.id = o.tipobecaperiodo_id "
                    + "   join cat_tipo_beca b on tbp.tipobeca_id = b.id "
                    + " where o.alta = 0"
                    + " and obd.PROCESO_ID = ?1 "
                    + " ORDER BY b.nombre, a.apellidoPaterno, a.apellidoMaterno, a.nombre";
        } else {
            consulta = "SELECT o.* FROM ENT_OTORGAMIENTOS o "
                    + "   join ent_alumno a on a.id = o.alumno_id "
                    + "   join ent_tipo_beca_periodo tbp on tbp.id = o.tipobecaperiodo_id "
                    + "   join cat_tipo_beca b on tbp.tipobeca_id = b.id ";
            if (getDaos().getPeriodoDao().getPeriodoActivo().equals(proceso.getPeriodo())) {
                consulta = consulta + "where o.alta = 1 ";
            } else {
                consulta = consulta + "where o.id not in (SELECT v.otorgamiento_id FROM vw_ultima_baja_detalle v  WHERE v.PERIODO_ID = o.PERIODO_ID AND v.MOVIMIENTO_ID is not null) ";
            }
            consulta = consulta + " and o.PROCESO_ID = ?1 "
                    + " ORDER BY b.nombre, a.apellidoPaterno, a.apellidoMaterno, a.nombre";
        }
        Query q = entityManager.createNativeQuery(consulta, Otorgamiento.class);
        q.setParameter(1, procesoId);
        List<Otorgamiento> list = q.getResultList();
        return (list);
    }

    @Override
    public List<Object[]> prelacionProceso(BigDecimal procesoId, Boolean procesoManutencion) {
        ProcesoBO bo = new ProcesoBO(getDaos());
        Proceso proceso = bo.getProceso(procesoId);
        Boolean baja = proceso.getTipoProceso().getMovimiento().getTipoMovimiento().getId().intValue() == 2 ? Boolean.TRUE : Boolean.FALSE;

        StringBuilder sb = new StringBuilder();
        StringBuilder subQ = new StringBuilder();
        StringBuilder subQ2 = new StringBuilder();
        StringBuilder decodeGastoTrans = new StringBuilder();

        List<String> columnas = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        List<String> criteria = new ArrayList<>();

        decodeGastoTrans.append(" Decode (Substr(b.nombre, 0, 10),");
        decodeGastoTrans.append(" 'Transporte',");
        decodeGastoTrans.append(" (SELECT TO_CHAR((Nvl(Sum(tt.costo), 0) * 21.5 ))");
        decodeGastoTrans.append(" FROM rmm_transporte_traslado tt");
        decodeGastoTrans.append(" JOIN ent_cuestionario_transporte ct");
        decodeGastoTrans.append("  ON CT.id = TT.cuestionariotransporte_id");
        decodeGastoTrans.append(" WHERE CT.usuario_id = U.id");
        decodeGastoTrans.append(" AND CT.periodo_id = sb.periodo_id),");
        decodeGastoTrans.append(" (SELECT CP.NOMBRE");
        decodeGastoTrans.append(" FROM rmm_cuestionario_pregunta_resp CPR3");
        decodeGastoTrans.append(" JOIN ent_cuestionario_respuesta CP ");
        decodeGastoTrans.append("  ON CP.ID = CPR3.RESPUESTA_ID");
        decodeGastoTrans.append(" WHERE  CPR3.usuario_id = U.id");
        decodeGastoTrans.append("  AND CPR3.periodo_id = sb.periodo_id");
        decodeGastoTrans.append("  AND CPR3.pregunta_id = 22");
        decodeGastoTrans.append("  AND CPR3.cuestionario_id = 1))");
        decodeGastoTrans.append(" \"GASTO EN TRANSPORTE\"");

        //   nvl( , '-')
        columnas.add("nvl(ua.nombrecorto, '-') ");
        columnas.add("nvl(a.BOLETA , '-') ");
        columnas.add("nvl(b.NOMBRE , '-') ");

        if (!procesoManutencion) {
            columnas.add("nvl(TO_CHAR(sb.INGRESOSPERCAPITAPESOS) , '-') ");
            columnas.add(decodeGastoTrans.toString());
        }

        columnas.add("nvl(TO_CHAR(da.PROMEDIO) , '-') ");
        columnas.add("nvl(TO_CHAR(da.SEMESTRE) , '-') ");

        if (baja) {
            columnas.add("CASE WHEN vo.id IS NULL THEN 'NO' ELSE 'SI' END");
        } else {
            columnas.add("CASE WHEN ox.id IS NULL THEN 'NO' ELSE 'SI' END");
        }

        // CASE vo.id WHEN IS NULL THEN 'NO' ELSE 'SI' END
        columnas.add("CASE WHEN a.ENTIDADDENACIMIENTO_ID IS NULL THEN '-' ELSE (CASE WHEN a.ENTIDADDENACIMIENTO_ID < 33 THEN 'MEXICANA' ELSE 'EXTRANJERO' END) END");

        columnas.add("nvl(m.NOMBRE , '-') as modalidad ");
        columnas.add("CASE WHEN da.REGULAR IS NULL THEN '-' ELSE (CASE WHEN da.REGULAR = 0 THEN 'NO' ELSE 'SI' END) END  ");
        columnas.add("CASE WHEN da.CUMPLECARGAMINIMA IS NULL THEN '-' ELSE (CASE da.CUMPLECARGAMINIMA WHEN 0 THEN 'NO CUMPLE' WHEN 1 THEN 'CARGA MINIMA' WHEN 2 THEN 'CARGA MEDIA' WHEN 3 THEN 'CARGA MAXIMA' ELSE '-' END) END");
        columnas.add("nvl(c.CARRERA , '-') ");
        columnas.add("CASE WHEN pp.id IS NULL THEN 'NO' ELSE 'SI' END ");
        //columnas.add("dlm.CONHAMBRE ");
        //columnas.add("a.usuario_id ");
        columnas.add("CASE WHEN a.ENTIDADDENACIMIENTO_ID IS NULL THEN '-' ELSE (CASE WHEN a.ENTIDADDENACIMIENTO_ID = 0 THEN 'NO' ELSE 'SI' END) END");

        sb.append(" SELECT ");
        agregaColumnas(sb, columnas);

        sb.append(" FROM ENT_OTORGAMIENTOS o ");

        if (baja) {
            sb.append(" JOIN  VW_ULTIMA_BAJA_DETALLE obd");
            sb.append(" ON  obd.OTORGAMIENTO_ID = o.id");
        }

        sb.append(" JOIN  ENT_ALUMNO a");
        sb.append(" ON  a.id = o.alumno_id");
        sb.append(" JOIN  ENT_ALUMNO_DATOS_ACADEMICOS da");
        sb.append(" ON  da.id = o.DATOSACADEMICOS_ID");
        sb.append(" JOIN CAT_UNIDAD_ACADEMICA ua ");
        sb.append(" ON  ua.id = da.UNIDADACADEMICA_ID");
        sb.append(" JOIN  ENT_CARRERA c");
        sb.append(" ON  c.id = da.CARRERA_ID");
        sb.append(" JOIN  CAT_MODALIDAD m");
        sb.append(" ON  m.id = da.MODALIDAD_ID");
        sb.append(" JOIN  ENT_DIRECCION dir");
        sb.append(" ON  dir.id = a.DIRECCION_ID ");
        sb.append(" JOIN  RMM_ESTADO_DELEG_COL del");
        sb.append(" ON  del.id = dir.RELACIONGEOGRAFICA_ID");
        sb.append(" JOIN CAT_DELEGACION_MUNICIPIO dlm ");
        sb.append(" ON  dlm.id = del.MUNICIPIO_ID");
        sb.append(" JOIN ENT_SOLICITUD_BECAS sb ");
        sb.append(" ON   sb.id = o.SOLICITUDBECA_ID");
        sb.append(" JOIN  ENT_TIPO_BECA_PERIODO tbp");
        sb.append(" ON  tbp.id = o.tipobecaperiodo_id");
        sb.append(" JOIN CAT_TIPO_BECA b ");
        sb.append(" ON  tbp.tipobeca_id = b.id");

        sb.append(" LEFT JOIN ent_usuario U");
        sb.append("  ON U.ID = a.USUARIO_ID");

        if (baja) {
            subQ.append(" (SELECT oxx.id, oxx.alumno_id \n ");
            subQ.append(" FROM   VW_OTORGAMIENTOS oxx\n ");
            subQ.append(" WHERE  oxx.periodo_id = (SELECT p.periodoanterior_id");
            subQ.append(" FROM   cat_periodo p ");
            subQ.append(" WHERE  p.estatus = 1");
            subQ.append(" AND rownum = 1) )");

            sb.append(" LEFT JOIN ");
            sb.append(subQ.toString());
            sb.append(" vo ");
            sb.append(" ON  vo.id = o.id AND vo.alumno_id = a.id ");

            sb.append(" LEFT JOIN ENT_PADRON_PROSPERA pp");
            sb.append(" ON pp.alumno_id = a.id and pp.periodo_id = o.periodo_id ");

            criteria.add(" o.alta = 0 ");

            criteria.add(" obd.PROCESO_ID = #proceso_id ");
            params.put("proceso_id", procesoId);
        } else {

            subQ2.append(" (select oxx.id from ENT_OTORGAMIENTOS oxx where oxx.periodo_id = ");
            subQ2.append(" (select p.PERIODOANTERIOR_ID from CAT_PERIODO p ");
            subQ2.append(" where p.ESTATUS=1 and rownum = 1) ");
            subQ2.append(" and oxx.alumno_id = a.id and oxx.id not in ");
            subQ2.append(" (SELECT v.otorgamiento_id FROM vw_ultima_baja_detalle v ");
            subQ2.append(" WHERE v.PERIODO_ID = oxx.PERIODO_ID AND v.MOVIMIENTO_ID is not null) ");
            subQ2.append(" and rownum = 1) ");

            sb.append(" LEFT JOIN ENT_OTORGAMIENTOS ox ");
            sb.append(" ON o.id =");
            sb.append(subQ2.toString());

            sb.append(" LEFT JOIN ENT_PADRON_PROSPERA pp ");
            sb.append(" ON pp.alumno_id = a.id and pp.periodo_id = o.periodo_id ");

            if (getDaos().getPeriodoDao().getPeriodoActivo().equals(proceso.getPeriodo())) {
                criteria.add(" o.alta = 1 ");
            } else {
                criteria.add(" o.id not in (SELECT v.otorgamiento_id FROM vw_ultima_baja_detalle v  WHERE v.PERIODO_ID = o.PERIODO_ID AND v.MOVIMIENTO_ID is not null) ");
            }

            criteria.add(" o.PROCESO_ID = #proceso_id ");
            params.put("proceso_id", procesoId);
        }

        agregaCriterios(sb, criteria);
        sb.append(" ORDER BY ua.nombrecorto, a.boleta ");

        List<Object[]> result = executeNativeQuery(sb.toString(), params, null);
        return result;

        /* Query q = entityManager.createNativeQuery(consulta);
         q.setParameter(1, procesoId);
         List<Object[]> list = q.getResultList();
         return (list);*/
    }

    @Override
    public List<Otorgamiento> otorgamientosProcesos(BigDecimal procesoId, String procesos) {
        ProcesoBO bo = new ProcesoBO(getDaos());
        Proceso proceso = bo.getProceso(procesoId);
        int movimientoId = proceso.getTipoProceso().getMovimiento().getId().intValue();
        Boolean al = (movimientoId >= 4 && movimientoId <= 6) ? Boolean.TRUE : Boolean.FALSE;
        String consulta;

        String[] valores = procesos.split(",");
        List<String> listaPr = Arrays.asList(valores);

        if (al) {
            consulta = "SELECT o.* FROM ENT_OTORGAMIENTOS o "
                    + "   join VW_ULTIMA_BAJA_DETALLE obd on obd.OTORGAMIENTO_ID = o.id "
                    + "   join ent_alumno a on a.id = o.alumno_id "
                    + "   join ent_tipo_beca_periodo tbp on tbp.id = o.tipobecaperiodo_id "
                    + "   join cat_tipo_beca b on tbp.tipobeca_id = b.id "
                    + " where o.alta = 0"
                    + " and (";
            int i = 1;
            if (listaPr != null && !listaPr.isEmpty()) {
                for (String l : listaPr) {
                    consulta += " obd.PROCESO_ID = ?" + (i++) + " or ";
                }
                consulta = consulta.substring(0, consulta.lastIndexOf(" or "));
            }
            consulta += ") ORDER BY b.nombre, a.apellidoPaterno, a.apellidoMaterno, a.nombre";
        } else {
            consulta = "SELECT o.* FROM ENT_OTORGAMIENTOS o "
                    + "   join ent_alumno a on a.id = o.alumno_id "
                    + "   join ent_tipo_beca_periodo tbp on tbp.id = o.tipobecaperiodo_id "
                    + "   join cat_tipo_beca b on tbp.tipobeca_id = b.id ";
            if (getDaos().getPeriodoDao().getPeriodoActivo().equals(proceso.getPeriodo())) {
                consulta = consulta + "where o.alta = 1 ";
            } else {
                consulta = consulta + "where o.id not in (SELECT v.otorgamiento_id FROM vw_ultima_baja_detalle v  WHERE v.PERIODO_ID = o.PERIODO_ID AND v.MOVIMIENTO_ID is not null) ";
            }
            consulta = consulta + " and (";
            int i = 1;
            if (listaPr != null && !listaPr.isEmpty()) {
                for (String l : listaPr) {
                    consulta += " o.PROCESO_ID = ?" + (i++) + " or ";
                }
                consulta = consulta.substring(0, consulta.lastIndexOf(" or "));
            }
            consulta += ") ORDER BY b.nombre, a.apellidoPaterno, a.apellidoMaterno, a.nombre";
        }

        Query q = entityManager.createNativeQuery(consulta, Otorgamiento.class);
        int i = 1;
        for (String l : listaPr) {
            q.setParameter((i++), new BigDecimal(l));
        }
        List<Otorgamiento> list = q.getResultList();
        return (list);
    }

    @Override
    public List<Object[]> otorgamientosPeriodo(Periodo periodo, Boolean alta) {
        List<String> columnas = new ArrayList<>();
        List<String> columnasOuter = new ArrayList<>();
        StringBuilder sbInner = new StringBuilder();
        StringBuilder sbOuter = new StringBuilder();
        List<String> criteria = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        BigDecimal ua = null;

        // Comienzan case y nvls
        StringBuilder caseCarga = new StringBuilder();
        caseCarga.append(" CASE da.cumpleCargaMinima");
        caseCarga.append(" WHEN 0 THEN 'Debajo de la mínima'");
        caseCarga.append(" WHEN 1 THEN 'Mínima'");
        caseCarga.append(" WHEN 2 THEN 'Media'");
        caseCarga.append(" WHEN 3 THEN 'Maxima'");
        caseCarga.append(" ELSE '-'");
        caseCarga.append(" END AS carga");

        StringBuilder caseTurno = new StringBuilder();
        caseTurno.append(" CASE da.turno");
        caseTurno.append(" WHEN 'V' THEN 'Vespertino'");
        caseTurno.append(" WHEN 'M' THEN 'Matutino'");
        caseTurno.append(" WHEN 'X' THEN 'Mixto'");
        caseTurno.append(" ELSE '-'");
        caseTurno.append(" END AS turno");

        StringBuilder casePagoIPN = new StringBuilder();
        casePagoIPN.append(" CASE WHEN dua.correspondeipn IS NULL");
        casePagoIPN.append(" OR dua.correspondeipn = 0");
        casePagoIPN.append(" THEN 'No'");
        casePagoIPN.append(" ELSE 'Si'");
        casePagoIPN.append(" END AS pagoipn");

        StringBuilder caseConvocatoria = new StringBuilder();
        caseConvocatoria.append(" CASE WHEN ps.convocatoria_id IS NULL");
        caseConvocatoria.append(" THEN '-'");
        caseConvocatoria.append(" ELSE CASE");
        caseConvocatoria.append("  WHEN ps.convocatoria_id = 1");
        caseConvocatoria.append("  THEN 'Ordinaria'");
        caseConvocatoria.append("  ELSE 'Extraordinaria'");
        caseConvocatoria.append("  END");
        caseConvocatoria.append(" END AS convocatoria");

        // Comienza query interior        
        columnas.add("a.boleta AS boleta");
        columnas.add("a.curp as curp");
        columnas.add("a.nombre as nombre");
        columnas.add("a.apellidopaterno || ' ' || a.apellidomaterno AS apellidos");
        columnas.add("g.clave as genero");
        columnas.add("da.semestre as semestre");
        columnas.add("da.promedio as promedio");
        columnas.add(caseCarga.toString());
        columnas.add("md.nombre as modalidad");
        columnas.add(caseTurno.toString());
        columnas.add("nvl(a.CORREOELECTRONICO, '-') as correo");
        columnas.add("nvl(a.celular, '-') AS celular");
        columnas.add("b.nombre as programa");
        columnas.add("tb.nombre as beca");
        columnas.add("p.fechainicial as inicio");
        columnas.add("p.fechafinal as fin");
        columnas.add("tpr.nombre as proceso");
        columnas.add("epr.nombre as estatusproceso");
        columnas.add("tbp.MONTO_OFICIAL as monto");
        columnas.add("n.nombre as nivel");
        columnas.add("ua.nombrecorto as unidadacademica");
        columnas.add("crr.carrera as carrera");
        if (periodo.getId().intValue() < 35) {
            columnas.add("NVL(cr.nombre, '-') as ingresos");
        } else {
            columnas.add("NVL(TO_CHAR(sb.INGRESOSPERCAPITAPESOS), '-') as ingresos");
        }
        columnas.add(getResumenCaseEstatusTarjeta().toString());
        columnas.add("nvl(btx.numtarjetabancaria, '-') AS tarjeta");
        columnas.add(casePagoIPN.toString());
        columnas.add(getResumenCaseExcluirDep().toString());
        columnas.add("nvl(io.nombre, '-')  as identificadorO");
        columnas.add(getResumenCaseEstatusSubes().toString());
        columnas.add("nvl(ps.FOLIOSUBES, '-') as foliosubes");
        columnas.add(caseConvocatoria.toString());
        columnas.add("ua.nivel_id as od1");
        columnas.add("ua.id as od2");
        columnas.add("tpr.id as od3");
        columnas.add("tb.id as od4");
        columnas.add("a.curp as od5");
        columnas.add(getResumenNvlMonto().toString());
        columnas.add("od.mes as mes");
        columnas.add("etb.nombre as estatustarjetabancaria");
        columnas.add(getResumenPartitionTotal(!alta).toString());

        sbInner.append(" SELECT ");
        agregaColumnas(sbInner, columnas);
        if (alta) {
            sbInner.append(" from VW_OTORGAMIENTOS o");
            sbInner.append(" join ENT_PROCESO pr");
            sbInner.append("  on pr.id = o.PROCESO_ID");
        } else {
            sbInner.append(" from ENT_OTORGAMIENTOS o");
            sbInner.append(" join VW_ULTIMA_BAJA_DETALLE ob");
            sbInner.append("  on OB.OTORGAMIENTO_ID = o.id");
            sbInner.append(" join ENT_PROCESO pr");
            sbInner.append("  on pr.id = ob.PROCESO_ID");
        }
        sbInner.append(" left join ent_solicitud_becas sb");
        sbInner.append("  on sb.id = o.SOLICITUDBECA_ID");
        sbInner.append(" join ent_alumno a");
        sbInner.append("  on a.id = o.alumno_id");
        sbInner.append(" join ENT_ALUMNO_DATOS_ACADEMICOS da");
        sbInner.append("  on da.id = o.datosAcademicos_id");
        sbInner.append(" left join CAT_IDENTIFICADOR_OTORGAMIENTO io");
        sbInner.append("  on io.id = o.IDENTIFICADOROTORGAMIENTO_ID");
        sbInner.append(" join CAT_GENERO g");
        sbInner.append("  on g.id = a.GENERO_ID");
        sbInner.append(" left join cat_modalidad md");
        sbInner.append("  on md.id = da.modalidad_id");
        sbInner.append(" left join VW_ULTIMA_TARJETA maxatb");
        sbInner.append("  on maxatb.alumno_id = a.id");
        sbInner.append(" left join ENT_TARJETA_BANCARIA btx");
        sbInner.append("  on btx.id = maxatb.TARJETABANCARIA_ID");
        sbInner.append(" left join CAT_ESTATUS_TARJETA_BANCARIA etb");
        sbInner.append("  on etb.id = maxatb.ESTATUSTARJBANC_ID");
        sbInner.append(" left join CAT_PERIODO p");
        sbInner.append("  on p.id = o.PERIODO_ID");
        sbInner.append(" join cat_tipo_proceso tpr");
        sbInner.append("  on tpr.id = pr.TIPOPROCESO_ID");
        if (!alta) {
            sbInner.append(" join CAT_MOVIMIENTO m ");
            sbInner.append(" on m.id = tpr.MOVIMIENTO_ID ");
            sbInner.append(" join CAT_TIPO_MOVIMIENTO tm");
            sbInner.append(" on tm.id = m.TIPOMOVIMIENTO_ID");
        }
        sbInner.append(" join CAT_ESTATUS_PROCESO epr");
        sbInner.append("  on epr.id = pr.PROCESOESTATUS_ID");
        sbInner.append(" join CAT_UNIDAD_ACADEMICA ua");
        sbInner.append("  on ua.id = pr.UNIDADACADEMICA_ID");
        sbInner.append(" join CAT_NIVEL n");
        sbInner.append("  on n.id = ua.nivel_id");
        sbInner.append(" left join ENT_CARRERA crr");
        sbInner.append("  on crr.id = da.carrera_id");
        sbInner.append(" join ENT_TIPO_BECA_PERIODO tbp");
        sbInner.append("  on tbp.id = o.TIPOBECAPERIODO_ID");
        sbInner.append(" join CAT_TIPO_BECA tb");
        sbInner.append("  on tb.id = tbp.TIPOBECA_ID");
        sbInner.append(" join CAT_PROGRAMA_BECA b");
        sbInner.append("  on b.id = tb.beca_id");
        sbInner.append(" left join rmm_deposito_unidad_academica dua");
        sbInner.append("  on dua.UNIDADACADEMICA_ID = ua.ID");
        sbInner.append("  and dua.TIPOBECA_ID = tbp.TIPOBECA_ID");
        if (periodo.getId().intValue() < 35) {
            sbInner.append(" join ent_usuario u");
            sbInner.append("  on u.id = a.USUARIO_ID");
            sbInner.append(" left join RMM_CUESTIONARIO_PREGUNTA_RESP cpr");
            sbInner.append("  on cpr.USUARIO_ID = u.id");
            sbInner.append("  and cpr.PREGUNTA_ID = 13");
            sbInner.append("  and cpr.PERIODO_ID = o.PERIODO_ID");
            sbInner.append(" left join ENT_CUESTIONARIO_RESPUESTA cr");
            sbInner.append("  on cr.id = cpr.RESPUESTA_ID");
        }
        sbInner.append(" left join ENT_DEPOSITO ed");
        sbInner.append("  on ed.OTORGAMIENTO_ID = o.ID");
        sbInner.append(" left join cat_estatus_deposito edd");
        sbInner.append("  on ed.estatusdeposito_id = edd.id");
        sbInner.append(" left join ENT_ORDEN_DEPOSITO od");
        sbInner.append("  on od.ID = ed.ORDENDEPOSITO_ID");
        sbInner.append(" left join ent_padron_subes ps");
        sbInner.append("  on ps.alumno_id = a.id");
        sbInner.append("  and ps.periodo_id = o.periodo_id");
        sbInner.append("  and ps.estatussubes = 'Aceptado'");

        // criteria.add("ROWNUM < 1000");
        criteria.add("o.proceso_id IS NOT NULL");
        if (alta) {
            criteria.add("o.periodo_id = #periodoId");
        } else {
            criteria.add("ob.periodo_id = #periodoId");
            criteria.add("o.alta = 0");
        }
        params.put("periodoId", periodo.getId());

        agregaCriterios(sbInner, criteria);
        // Termina query interior

        // Comienza query exterior
        columnasOuter.add("nivel");
        columnasOuter.add("unidadacademica");
        columnasOuter.add("boleta");
        columnasOuter.add("curp");
        columnasOuter.add("nombre");
        columnasOuter.add("apellidos");
        columnasOuter.add("genero");
        columnasOuter.add("semestre");
        columnasOuter.add("promedio");
        columnasOuter.add("carga");
        columnasOuter.add("modalidad");
        columnasOuter.add("turno");
        columnasOuter.add("correo");
        columnasOuter.add("celular");
        columnasOuter.add("programa");
        columnasOuter.add("beca");
        columnasOuter.add("proceso");
        columnasOuter.add("estatusproceso");
        columnasOuter.add("monto");
        columnasOuter.add("carrera");
        columnasOuter.add("ingresos");
        columnasOuter.add("activa");
        columnasOuter.add("tarjeta");
        columnasOuter.add("pagoipn");
        columnasOuter.add("excluir");
        columnasOuter.add("identificadorO");
        columnasOuter.add("estatussubes");
        columnasOuter.add("foliosubes");
        columnasOuter.add("convocatoria");
        if (periodo.getClave().contains("-2")) {
            columnasOuter.add("Nvl(Febrero, '-')");
            columnasOuter.add("Nvl(Marzo, '-')");
            columnasOuter.add("Nvl(Abril, '-')");
            columnasOuter.add("Nvl(Mayo, '-')");
            columnasOuter.add("Nvl(Junio, '-')");
            columnasOuter.add("Nvl(Julio, '-')");
        } else {
            columnasOuter.add("Nvl(Agosto, '-')");
            columnasOuter.add("Nvl(Septiembre, '-')");
            columnasOuter.add("Nvl(Octubre, '-')");
            columnasOuter.add("Nvl(Noviembre, '-')");
            columnasOuter.add("Nvl(Diciembre, '-')");
            columnasOuter.add("Nvl(Enero, '-')");
        }
        columnasOuter.add("Total");

        sbOuter.append(" SELECT ");
        agregaColumnas(sbOuter, columnasOuter);
        sbOuter.append(" from ( ").append(sbInner).append(")");
        // comienza pivot
        sbOuter.append(" PIVOT ( Min(estDeposito)");
        sbOuter.append(" FOR mes in (");
        if (periodo.getClave().contains("-2")) {
            sbOuter.append(" 2 Febrero,");
            sbOuter.append(" 3 Marzo,");
            sbOuter.append(" 4 Abril,");
            sbOuter.append(" 5 Mayo,");
            sbOuter.append(" 6 Junio,");
            sbOuter.append(" 7 Julio");
        } else {
            sbOuter.append(" 8 Agosto,");
            sbOuter.append(" 9 Septiembre,");
            sbOuter.append(" 10 Octubre,");
            sbOuter.append(" 11 Noviembre,");
            sbOuter.append(" 12 Diciembre,");
            sbOuter.append(" 1 Enero");
        }
        sbOuter.append("))");
        // termina pivot
        sbOuter.append(" ORDER BY od1, od2, od3, od4, od5");
        // termina query exterior
        // System.out.println(sbOuter.toString());
        List<Object[]> result = executeNativeQuery(sbOuter.toString(), params, null);
        return result;
    }

    @Override
    public List<Object[]> otorgamientosPeriodoAcotado(Periodo periodo, Boolean alta, BigDecimal ua) {
        List<String> columnas = new ArrayList<>();
        StringBuilder sbInner = new StringBuilder();
        List<String> columnasOuter = new ArrayList<>();
        StringBuilder sbOuter = new StringBuilder();
        List<String> criteria = new ArrayList<>();
        Map<String, Object> params = new HashMap();

        columnas.add("a.boleta AS boleta");
        columnas.add("a.curp as curp");
        columnas.add("a.nombre || ' ' || a.apellidopaterno || ' ' || a.apellidomaterno as nombre");
        columnas.add("g.clave as genero");
        columnas.add("da.semestre as semestre");
        columnas.add("da.promedio as promedio");
        columnas.add("md.nombre as modalidad");
        columnas.add("b.nombre as programa");
        columnas.add("tb.nombre as beca");
        columnas.add("tpr.nombre as proceso");
        columnas.add("tbp.MONTO_OFICIAL as monto");
        columnas.add("crr.carrera as carrera");
        if (periodo.getId().intValue() < 35) {
            columnas.add("NVL(cr.nombre, '-') as ingresos");
        } else {
            columnas.add("NVL(TO_CHAR(sb.INGRESOSPERCAPITAPESOS), '-') as ingresos");
        }
        columnas.add(getResumenCaseEstatusTarjeta().toString());
        columnas.add(getResumenCaseEstatusSubes().toString());
        columnas.add("nvl(btx.numtarjetabancaria, '-') AS tarjeta");
        columnas.add(getResumenCaseExcluirDep().toString());
        columnas.add("nvl(ps.FOLIOSUBES, '-') as foliosubes");
        columnas.add("nvl(a.CORREOELECTRONICO, '-') as correo");
        columnas.add("nvl(a.celular, '-') AS celular");
        columnas.add("tpr.id as od1");
        columnas.add("tb.id as od2");
        columnas.add("a.curp as od3");
        columnas.add(getResumenNvlMonto().toString());
        columnas.add("od.mes as mes");
        columnas.add("etb.nombre as estatustarjetabancaria");
        columnas.add(getResumenPartitionTotal(!alta).toString());

        sbInner.append(" SELECT ");
        agregaColumnas(sbInner, columnas);
        if (alta) {
            sbInner.append(" from VW_OTORGAMIENTOS o");
            sbInner.append(" join ENT_PROCESO pr");
            sbInner.append("  on pr.id = o.PROCESO_ID");
        } else {
            sbInner.append(" from ENT_OTORGAMIENTOS o");
            sbInner.append(" join VW_ULTIMA_BAJA_DETALLE ob");
            sbInner.append("  on OB.OTORGAMIENTO_ID = o.id");
            sbInner.append(" join ENT_PROCESO pr");
            sbInner.append("  on pr.id = ob.PROCESO_ID");
        }
        sbInner.append(" left join ent_solicitud_becas sb");
        sbInner.append("  on sb.id = o.SOLICITUDBECA_ID");
        sbInner.append(" join ent_alumno a");
        sbInner.append("  on a.id = o.alumno_id");
        sbInner.append(" join ENT_ALUMNO_DATOS_ACADEMICOS da");
        sbInner.append("  on da.id = o.datosAcademicos_id");
        sbInner.append(" join CAT_GENERO g");
        sbInner.append("  on g.id = a.GENERO_ID");
        sbInner.append(" left join cat_modalidad md");
        sbInner.append("  on md.id = da.modalidad_id");
        sbInner.append(" left join VW_ULTIMA_TARJETA maxatb");
        sbInner.append("  on maxatb.alumno_id = a.id");
        sbInner.append(" left join ENT_TARJETA_BANCARIA btx");
        sbInner.append("  on btx.id = maxatb.TARJETABANCARIA_ID");
        sbInner.append(" left join CAT_ESTATUS_TARJETA_BANCARIA etb");
        sbInner.append("  on etb.id = maxatb.ESTATUSTARJBANC_ID");
        sbInner.append(" join cat_tipo_proceso tpr");
        sbInner.append("  on tpr.id = pr.TIPOPROCESO_ID");
        sbInner.append(" join CAT_UNIDAD_ACADEMICA ua");
        sbInner.append("  on ua.id = pr.UNIDADACADEMICA_ID");
        sbInner.append(" left join ENT_CARRERA crr");
        sbInner.append("  on crr.id = da.carrera_id");
        sbInner.append(" join ENT_TIPO_BECA_PERIODO tbp");
        sbInner.append("  on tbp.id = o.TIPOBECAPERIODO_ID");
        sbInner.append(" join CAT_TIPO_BECA tb");
        sbInner.append("  on tb.id = tbp.TIPOBECA_ID");
        sbInner.append(" join CAT_PROGRAMA_BECA b");
        sbInner.append("  on b.id = tb.beca_id");
        if (periodo.getId().intValue() < 35) {
            sbInner.append(" join ent_usuario u");
            sbInner.append("  on u.id = a.USUARIO_ID");
            sbInner.append(" left join RMM_CUESTIONARIO_PREGUNTA_RESP cpr");
            sbInner.append("  on cpr.USUARIO_ID = u.id");
            sbInner.append("  and cpr.PREGUNTA_ID = 13");
            sbInner.append("  and cpr.PERIODO_ID = o.PERIODO_ID");
            sbInner.append(" left join ENT_CUESTIONARIO_RESPUESTA cr");
            sbInner.append("  on cr.id = cpr.RESPUESTA_ID");
        }
        sbInner.append(" left join ENT_DEPOSITO ed");
        sbInner.append("  on ed.OTORGAMIENTO_ID = o.ID");
        sbInner.append(" left join cat_estatus_deposito edd");
        sbInner.append("  on ed.estatusdeposito_id = edd.id");
        sbInner.append(" left join ENT_ORDEN_DEPOSITO od");
        sbInner.append("  on od.ID = ed.ORDENDEPOSITO_ID");
        sbInner.append(" left join ent_padron_subes ps");
        sbInner.append("  on ps.alumno_id = a.id");
        sbInner.append("  and ps.periodo_id = o.periodo_id");
        sbInner.append("  and ps.estatussubes = 'Aceptado'");

        criteria.add("o.proceso_id IS NOT NULL");
        if (alta) {
            criteria.add("o.periodo_id = #periodoId");
        } else {
            criteria.add("ob.periodo_id = #periodoId");
            criteria.add("o.alta = 0");
        }
        params.put("periodoId", periodo.getId());
        criteria.add("ua.id = #ua");
        params.put("ua", ua);
        agregaCriterios(sbInner, criteria);
        // Termina query interior

        // Comienza query exterior
        columnasOuter.add("boleta");
        columnasOuter.add("curp");
        columnasOuter.add("nombre");
        columnasOuter.add("genero");
        columnasOuter.add("semestre");
        columnasOuter.add("promedio");
        columnasOuter.add("modalidad");
        columnasOuter.add("programa");
        columnasOuter.add("beca");
        columnasOuter.add("proceso");
        columnasOuter.add("monto");
        columnasOuter.add("carrera");
        columnasOuter.add("ingresos");
        columnasOuter.add("activa");
        columnasOuter.add("tarjeta");
        columnasOuter.add("excluir");
        columnasOuter.add("estatussubes");
        columnasOuter.add("foliosubes");
        columnasOuter.add("correo");
        columnasOuter.add("celular");
        if (periodo.getClave().contains("-2")) {
            columnasOuter.add("Nvl(Febrero, '-')");
            columnasOuter.add("Nvl(Marzo, '-')");
            columnasOuter.add("Nvl(Abril, '-')");
            columnasOuter.add("Nvl(Mayo, '-')");
            columnasOuter.add("Nvl(Junio, '-')");
            columnasOuter.add("Nvl(Julio, '-')");
        } else {
            columnasOuter.add("Nvl(Agosto, '-')");
            columnasOuter.add("Nvl(Septiembre, '-')");
            columnasOuter.add("Nvl(Octubre, '-')");
            columnasOuter.add("Nvl(Noviembre, '-')");
            columnasOuter.add("Nvl(Diciembre, '-')");
            columnasOuter.add("Nvl(Enero, '-')");
        }
        columnasOuter.add("Total");

        sbOuter.append(" SELECT ");
        agregaColumnas(sbOuter, columnasOuter);
        sbOuter.append(" from ( ").append(sbInner).append(")");
        // comienza pivot
        sbOuter.append(" PIVOT ( Min(estDeposito)");
        sbOuter.append(" FOR mes in (");
        if (periodo.getClave().contains("-2")) {
            sbOuter.append(" 2 Febrero,");
            sbOuter.append(" 3 Marzo,");
            sbOuter.append(" 4 Abril,");
            sbOuter.append(" 5 Mayo,");
            sbOuter.append(" 6 Junio,");
            sbOuter.append(" 7 Julio");
        } else {
            sbOuter.append(" 8 Agosto,");
            sbOuter.append(" 9 Septiembre,");
            sbOuter.append(" 10 Octubre,");
            sbOuter.append(" 11 Noviembre,");
            sbOuter.append(" 12 Diciembre,");
            sbOuter.append(" 1 Enero");
        }
        sbOuter.append("))");
        // termina pivot
        sbOuter.append(" ORDER BY od1, od2, od3");
        // termina query exterior

        List<Object[]> result = executeNativeQuery(sbOuter.toString(), params, null);
        return result;
    }

    private StringBuilder getResumenCaseEstatusTarjeta() {
        StringBuilder caseEstatusTarjeta = new StringBuilder();

        caseEstatusTarjeta.append(" CASE ");
        caseEstatusTarjeta.append(" WHEN maxatb.estatustarjbanc_id IS NULL");
        caseEstatusTarjeta.append(" THEN 'Sin solicitud'");
        caseEstatusTarjeta.append(" ELSE Nvl(etb.nombre, ' - ')");
        caseEstatusTarjeta.append(" END AS activa");

        return caseEstatusTarjeta;
    }

    private StringBuilder getResumenCaseEstatusSubes() {
        StringBuilder caseEstatusSubes = new StringBuilder();

        caseEstatusSubes.append(" case b.id");
        caseEstatusSubes.append("  when 5 then");
        caseEstatusSubes.append("   case when ps.estatussubes is null");
        caseEstatusSubes.append("   then '-'");
        caseEstatusSubes.append("   else concat('Manutención ',ps.estatussubes)");
        caseEstatusSubes.append("   end");
        caseEstatusSubes.append("  when 7 then");
        caseEstatusSubes.append("   case when ps.estatustransporte is null");
        caseEstatusSubes.append("   then '-' ");
        caseEstatusSubes.append("   else concat('Manutención Transporte ',ps.estatustransporte)");
        caseEstatusSubes.append("   end");
        caseEstatusSubes.append("  else 'No aplica'");
        caseEstatusSubes.append("  end AS estatussubes");

        return caseEstatusSubes;
    }

    private StringBuilder getResumenNvlMonto() {
        StringBuilder caseMonto = new StringBuilder();
        caseMonto.append(" CASE edd.id");
        caseMonto.append(" WHEN 2");
        caseMonto.append(" THEN Concat('',ed.monto)");
        caseMonto.append(" ELSE Concat('',edd.nombre)");
        caseMonto.append(" END");

        StringBuilder nvlMonto = new StringBuilder();
        nvlMonto.append("nvl(").append(caseMonto).append(", '-')  estDeposito");

        return nvlMonto;
    }

    private StringBuilder getResumenPartitionTotal(boolean isBaja) {
        StringBuilder partitionTotal = new StringBuilder();
        partitionTotal.append(" SUM(");
        partitionTotal.append("  CASE WHEN ed.ESTATUSDEPOSITO_ID = 2");
        if (isBaja) {
            partitionTotal.append("   AND ob.periodo_id = o.periodo_id");
        }
        partitionTotal.append("  THEN ed.monto");
        partitionTotal.append("  ELSE 0");
        partitionTotal.append("  END");
        partitionTotal.append(" ) over (partition by o.id)");
        partitionTotal.append(" as Total");

        return partitionTotal;
    }

    private StringBuilder getResumenCaseExcluirDep() {
        StringBuilder caseExcluirDep = new StringBuilder();
        caseExcluirDep.append(" CASE WHEN o.excluirdeposito IS NULL");
        caseExcluirDep.append(" OR o.excluirdeposito = 0");
        caseExcluirDep.append(" THEN 'No'");
        caseExcluirDep.append(" ELSE 'Si'");
        caseExcluirDep.append(" END AS excluir");

        return caseExcluirDep;
    }

    @Override
    public List<BecaPeriodoCount> otorgamientosBecariosCount(BigDecimal procesoId) {
        List<BecaPeriodoCount> bl = new ArrayList<>();

        ProcesoBO bo = new ProcesoBO(getDaos());
        Proceso proceso = bo.getProceso(procesoId);
        int movimientoId = proceso.getTipoProceso().getMovimiento().getId().intValue();
        Boolean al = (movimientoId >= 4 && movimientoId <= 6) ? Boolean.TRUE : Boolean.FALSE;
        String consulta;
        if (al) {
            consulta = "SELECT b.nombre, count(o.id) FROM ENT_OTORGAMIENTOS o "
                    + "   join VW_ULTIMA_BAJA_DETALLE obd on obd.OTORGAMIENTO_ID = o.id "
                    + "   join ent_tipo_beca_periodo tbp on tbp.id = o.tipobecaperiodo_id "
                    + "   join cat_tipo_beca b on tbp.tipobeca_id = b.id "
                    + " where o.alta = 0"
                    + " and obd.PROCESO_ID = ?1 "
                    + " group by b.nombre";
        } else {
            consulta = "SELECT b.nombre, count(o.id) FROM ENT_OTORGAMIENTOS o "
                    + "   join ent_tipo_beca_periodo tbp on tbp.id = o.tipobecaperiodo_id "
                    + "   join cat_tipo_beca b on tbp.tipobeca_id = b.id ";
            if (getDaos().getPeriodoDao().getPeriodoActivo().equals(proceso.getPeriodo())) {
                consulta = consulta + "where o.alta = 1 ";
            } else {
                consulta = consulta + "where o.id not in (SELECT v.otorgamiento_id FROM vw_ultima_baja_detalle v  WHERE v.PERIODO_ID = o.PERIODO_ID AND v.MOVIMIENTO_ID is not null) ";
            }
            consulta = consulta + " and o.PROCESO_ID = ?1 "
                    + " group by b.nombre";
        }
        Query q = entityManager.createNativeQuery(consulta);
        q.setParameter(1, procesoId);
        List<Object[]> list = q.getResultList();
        for (Object[] aux : list) {
            BecaPeriodoCount b = new BecaPeriodoCount();
            b.setNombre((String) aux[0]);
            b.setCount(((BigDecimal) aux[1]).longValue());
            bl.add(b);
        }
        return bl;
    }

    @Override
    public List<BecaPeriodoCount> otorgamientosBecariosCountP(BigDecimal procesoId, String procesos) {
        String[] valores = procesos.split(",");
        List<String> lista = Arrays.asList(valores);

        List<BecaPeriodoCount> bl = new ArrayList<>();

        ProcesoBO bo = new ProcesoBO(getDaos());
        Proceso proceso = bo.getProceso(procesoId);
        int movimientoId = proceso.getTipoProceso().getMovimiento().getId().intValue();
        Boolean al = (movimientoId >= 4 && movimientoId <= 6) ? Boolean.TRUE : Boolean.FALSE;
        String consulta;

        if (al) {
            consulta = "SELECT b.nombre, count(o.id) FROM ent_otorgamientos o "
                    + "   join ent_tipo_beca_periodo tbp on tbp.id = o.tipobecaperiodo_id "
                    + "   join cat_tipo_beca b on tbp.tipobeca_id = b.id "
                    + "   join VW_ULTIMA_BAJA_DETALLE obd on obd.OTORGAMIENTO_ID = o.id "
                    + "   join ent_alumno a on a.id = o.alumno_id "
                    + " where o.alta = 0"
                    + " and (";
            int i = 1;
            if (lista != null && !lista.isEmpty()) {
                for (String l : lista) {
                    consulta += " obd.PROCESO_ID = ?" + (i++) + " or ";
                }
                consulta = consulta.substring(0, consulta.lastIndexOf(" or "));
            }
            consulta += ") group by b.nombre";
        } else {
            consulta = "SELECT b.nombre, count(o.id) FROM ent_otorgamientos o "
                    + "   join ent_tipo_beca_periodo tbp on tbp.id = o.tipobecaperiodo_id "
                    + "   join cat_tipo_beca b on tbp.tipobeca_id = b.id "
                    + "   join ent_alumno a on a.id = o.alumno_id ";
            if (getDaos().getPeriodoDao().getPeriodoActivo().equals(proceso.getPeriodo())) {
                consulta = consulta + "where o.alta = 1 ";
            } else {
                consulta = consulta + "where o.id not in (SELECT v.otorgamiento_id FROM vw_ultima_baja_detalle v  WHERE v.PERIODO_ID = o.PERIODO_ID AND v.MOVIMIENTO_ID is not null) ";
            }
            consulta = consulta + " and (";
            int i = 1;
            if (lista != null && !lista.isEmpty()) {
                for (String l : lista) {
                    consulta += " o.PROCESO_ID = ?" + (i++) + " or ";
                }
                consulta = consulta.substring(0, consulta.lastIndexOf(" or "));
            }
            consulta += ") group by b.nombre";
        }

        Query q = entityManager.createNativeQuery(consulta);
        int i = 1;
        for (String l : lista) {
            q.setParameter((i++), l);
        }

        List<Object[]> list = q.getResultList();
        for (Object[] aux : list) {
            BecaPeriodoCount b = new BecaPeriodoCount();
            b.setNombre((String) aux[0]);
            b.setCount(((BigDecimal) aux[1]).longValue());
            bl.add(b);
        }
        return bl;
    }

    @Override
    public List<BecaPeriodoCount> otorgamientosBecariosPCount(BigDecimal periodoId, Boolean al) {
        List<BecaPeriodoCount> bl = new ArrayList<>();
        String consulta;
        if (!al) {
            consulta = "SELECT tb.nombre, count(o.id), tb.id "
                    + "FROM ent_otorgamientos o "
                    + "  join ent_alumno a on a.id = o.alumno_id  "
                    + "join CAT_GENERO g on g.id = a.GENERO_ID  "
                    + "join VW_ULTIMA_BAJA_DETALLE ob on OB.OTORGAMIENTO_ID = o.id  "
                    + "  JOIN ENT_PROCESO pr on pr.id = ob.PROCESO_ID  "
                    + "  join cat_tipo_proceso tpr on tpr.id = pr.TIPOPROCESO_ID  "
                    + "  join CAT_MOVIMIENTO m on m.id = tpr.MOVIMIENTO_ID  "
                    + "  join CAT_TIPO_MOVIMIENTO tm on tm.id = m.TIPOMOVIMIENTO_ID  "
                    + "  join CAT_ESTATUS_PROCESO epr on epr.id = pr.PROCESOESTATUS_ID  "
                    + "  join CAT_UNIDAD_ACADEMICA ua on ua.id = pr.UNIDADACADEMICA_ID  "
                    + "  join ENT_TIPO_BECA_PERIODO tbp on tbp.id = o.TIPOBECAPERIODO_ID  "
                    + "  join CAT_TIPO_BECA tb on tb.id = tbp.TIPOBECA_ID  "
                    + "  join ent_usuario u on u.id = a.USUARIO_ID "
                    + "where o.alta = 0  "
                    + "and OB.PERIODO_ID  = ?1  "
                    + "AND o.proceso_id IS NOT NULL "
                    + "group by tb.nombre, tb.id order by tb.id";
        } else {
            consulta = "select tb.NOMBRE, count(o.id), tb.id "
                    + "from ENT_OTORGAMIENTOS o  "
                    + "  join ent_alumno a on a.id = o.alumno_id  "
                    + "left join CAT_IDENTIFICADOR_OTORGAMIENTO io on io.id = o.IDENTIFICADOROTORGAMIENTO_ID  "
                    + "join CAT_GENERO g on g.id = a.GENERO_ID  "
                    + "left join RMM_ALUMNO_TARJETA_BANCARIA t on t.alumno_id = a.id and t.VIGENTE = 1 and t.TARJETAACTIVA = 1  "
                    + "left join CAT_PERIODO p on p.id = o.PERIODO_ID  "
                    + "LEFT join VW_ULTIMA_BITACORA_TARJETA bt on bt.TARJETABANCARIA_ID = t.TARJETABANCARIA_ID and bt.TARJETABANCARIAESTATUS_ID = 10  "
                    + "  JOIN ENT_PROCESO pr on pr.id = o.PROCESO_ID  "
                    + "  join cat_tipo_proceso tpr on tpr.id = pr.TIPOPROCESO_ID  "
                    + "  join CAT_MOVIMIENTO m on m.id = tpr.MOVIMIENTO_ID  "
                    + "  join CAT_TIPO_MOVIMIENTO tm on tm.id = m.TIPOMOVIMIENTO_ID  "
                    + "  join CAT_ESTATUS_PROCESO epr on epr.id = pr.PROCESOESTATUS_ID  "
                    + "  join CAT_UNIDAD_ACADEMICA ua on ua.id = pr.UNIDADACADEMICA_ID  "
                    + "  join ENT_TIPO_BECA_PERIODO tbp on tbp.id = o.TIPOBECAPERIODO_ID  "
                    + "  join CAT_TIPO_BECA tb on tb.id = tbp.TIPOBECA_ID  "
                    + "  join ent_usuario u on u.id = a.USUARIO_ID  "
                    + "left join RMM_CUESTIONARIO_PREGUNTA_RESP cpr on cpr.USUARIO_ID = u.id and cpr.PREGUNTA_ID = 13 and cpr.PERIODO_ID = o.PERIODO_ID  "
                    + "left join ENT_CUESTIONARIO_RESPUESTA cr on cr.id = cpr.RESPUESTA_ID  "
                    + "where o.periodo_id = ?1  "
                    + "AND o.proceso_id IS NOT NULL ";
            if (getDaos().getPeriodoDao().getPeriodoActivo().equals(getDaos().getPeriodoDao().findById(periodoId))) {
                consulta = consulta + "and o.alta = 1 ";
            } else {
                consulta = consulta + "and o.id not in (SELECT v.otorgamiento_id FROM vw_ultima_baja_detalle v  WHERE v.PERIODO_ID = ?1 AND v.MOVIMIENTO_ID is not null) ";
            }
            consulta = consulta + "group by tb.nombre, tb.id order by tb.id";
        }
        Query q = entityManager.createNativeQuery(consulta);
        q.setParameter(1, periodoId);
        List<Object[]> list = q.getResultList();
        for (Object[] aux : list) {
            BecaPeriodoCount b = new BecaPeriodoCount();
            b.setNombre((String) aux[0]);
            b.setCount(((BigDecimal) aux[1]).longValue());
            bl.add(b);
        }
        return bl;
    }

    @Override
    public List<Otorgamiento> alumnosExcluidosPeriodo(BigDecimal periodo) {
        String jpql = "select o from Otorgamiento o where o.periodo.id = ?1 and o.excluirDeposito = 1";
        List<Otorgamiento> lista = executeQuery(jpql, periodo);
        return lista;
    }

    @Override
    public Boolean alumnoExcluidoPeriodo(BigDecimal periodo, BigDecimal alumnoId) {
        String jpql = "select o from Otorgamiento o where o.periodo.id = ?1 and o.alumno.id = ?2 and o.excluirDeposito = 1";
        List<Otorgamiento> lista = executeQuery(jpql, periodo, alumnoId);
        return lista == null || lista.size() == 0 ? Boolean.FALSE : Boolean.TRUE;
    }

    @Override
    public List<Otorgamiento> buscarAlumnoPeriodoBoleta(BigDecimal periodo, String boleta) {
        String jpql = "select o from Otorgamiento o where o.periodo.id = ?1 and o.alumno.boleta = ?2";
        List<Otorgamiento> lista = executeQuery(jpql, periodo, boleta);
        return lista;
    }

    //SQLINJECTION
    @Override
    public List<Otorgamiento> buscarAlumnoPeriodoNombre(BigDecimal periodo, String nombre, String aPaterno, String aMaterno) {
        String nom = "";
        if (!"".equals(nombre)) {
            nom = nom + " and o.alumno.nombre like '%" + nombre.toUpperCase() + "%'";
        }
        if (!"".equals(aPaterno)) {
            nom = nom + " and o.alumno.apellidoPaterno like '%" + aPaterno.toUpperCase() + "%'";
        }
        if (!"".equals(aMaterno)) {
            nom = nom + " and o.alumno.apellidoMaterno like '%" + aMaterno.toUpperCase() + "%'";
        }
        String jpql = "select o from Otorgamiento o where o.periodo.id = ?1" + nom;
        List<Otorgamiento> lista = executeQuery(jpql, periodo);
        return lista;
    }

    //SQLINJECTION
    @Override
    public List<Otorgamiento> buscarAlumnoPeriodoBoletaNombre(BigDecimal periodo, String boleta, String nombre, String aPaterno, String aMaterno) {
        String nom = "";
        if ("".equals(nombre)) {
        } else {
            nom = nom + " and o.alumno.nombre like '%" + nombre + "%'";
        }
        if ("".equals(aPaterno)) {
        } else {
            nom = nom + " and o.alumno.apellidoPaterno like '%" + aPaterno + "%'";
        }
        if ("".equals(aMaterno)) {
        } else {
            nom = nom + " and o.alumno.apellidoMaterno like '%" + aMaterno + "%'";
        }
        String jpql = "select o from Otorgamiento o where o.periodo.id = ?1 and o.alumno.boleta = ?2" + nom;
        List<Otorgamiento> lista = executeQuery(jpql, periodo, boleta);
        return lista;
    }

    //SQLINJECTION
    @Override
    public List<Object[]> estadisticaTodo(BigDecimal periodoId) {
        String consulta = "SELECT n.clave, count(a.genero_id) as total\n"
                + "FROM ent_alumno a, ent_otorgamientos o,\n"
                + "cat_periodo p, cat_unidad_academica u, cat_nivel n\n"
                + "WHERE a.estatus = 1\n"
                + "AND o.alumno_id = a.id\n"
                + "AND o.periodo_id = p.id\n"
                + "AND p.id = " + periodoId + "\n"
                + "AND a.unidadacademica_id = u.id\n"
                + "AND u.nivel_id = n.id\n"
                + "GROUP BY n.clave";
        List<Object[]> lista = executeNativeQuery(consulta);
        return lista;
    }

    //SQLINJECTION
    @Override
    public List<Object[]> estadisticaNivel(BigDecimal periodoId, BigDecimal nivelId) {
        String consulta = "SELECT u.nombreCorto, count(a.genero_id) as total\n"
                + "FROM ent_alumno a, ent_otorgamientos o,\n"
                + "cat_periodo p, cat_unidad_academica u, cat_nivel n\n"
                + "WHERE a.estatus = 1\n"
                + "AND o.alumno_id = a.id\n"
                + "AND o.periodo_id = p.id\n"
                + "AND p.id = " + periodoId + "\n"
                + "AND a.unidadacademica_id = u.id\n"
                + "AND u.nivel_id = n.id\n"
                + "AND n.id = " + nivelId + "\n"
                + "GROUP BY u.nombreCorto";
        List<Object[]> lista = executeNativeQuery(consulta);
        return lista;
    }

    //SQLINJECTION
    @Override
    public List<Object[]> estadisticaUnidad(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadId) {
        String consulta = "SELECT tb.nombre, count(a.genero_id) as total\n"
                + "FROM ent_alumno a, ent_otorgamientos o,\n"
                + "cat_periodo p, cat_unidad_academica u, cat_nivel n,\n"
                + "ent_tipo_beca_periodo tbp, cat_tipo_beca tb\n"
                + "WHERE a.estatus = 1\n"
                + "AND o.alumno_id = a.id\n"
                + "AND o.periodo_id = p.id\n"
                + "AND p.id = " + periodoId + "\n"
                + "AND a.unidadacademica_id = u.id\n"
                + "AND u.nivel_id = n.id\n"
                + "AND o.tipobecaperiodo_id = tbp.id\n"
                + "AND tbp.tipobeca_id = tb.id\n"
                + "AND n.id = " + nivelId + "\n"
                + "AND u.id = " + unidadId + "\n"
                + "GROUP BY tb.nombre";
        List<Object[]> lista = executeNativeQuery(consulta);
        return lista;
    }

    //SQLINJECTION
    @Override
    public List<Object[]> estadisticaNivelGenero(BigDecimal periodoId, BigDecimal nivelId) {
        String consulta = "SELECT g.clave, count(a.genero_id) as total\n"
                + "FROM ent_alumno a, ent_otorgamientos o, cat_genero g,\n"
                + "cat_periodo p, cat_unidad_academica u, cat_nivel n\n"
                + "WHERE a.estatus = 1\n"
                + "AND o.alumno_id = a.id\n"
                + "AND o.periodo_id = p.id\n"
                + "AND p.id = " + periodoId + "\n"
                + "AND a.unidadacademica_id = u.id\n"
                + "AND u.nivel_id = n.id\n"
                + "AND n.id = " + nivelId + "\n"
                + "AND a.genero_id = g.id\n"
                + "GROUP BY g.clave";
        List<Object[]> lista = executeNativeQuery(consulta);
        return lista;
    }

    //SQLINJECTION
    @Override
    public List<Object[]> estadisticaUnidadGenero(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadId) {
        String consulta = "SELECT g.clave, count(a.genero_id) as total\n"
                + "FROM ent_alumno a, ent_otorgamientos o, cat_genero g,\n"
                + "cat_periodo p, cat_unidad_academica u, cat_nivel n\n"
                + "WHERE a.estatus = 1\n"
                + "AND o.alumno_id = a.id\n"
                + "AND o.periodo_id = p.id\n"
                + "AND p.id = " + periodoId + "\n"
                + "AND a.unidadacademica_id = u.id\n"
                + "AND u.nivel_id = n.id\n"
                + "AND n.id = " + nivelId + "\n"
                + "AND u.id = " + unidadId + "\n"
                + "AND a.genero_id = g.id\n"
                + "GROUP BY g.clave";
        List<Object[]> lista = executeNativeQuery(consulta);
        return lista;
    }

    //SQLINJECTION
    @Override
    public List<Object[]> estadisticaBecaGenero(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadId, BigDecimal tipoBecaId) {
        String consulta = "SELECT g.clave, count(a.genero_id) as total\n"
                + "FROM ent_alumno a, ent_otorgamientos o, cat_genero g,\n"
                + "cat_periodo p, cat_unidad_academica u, cat_nivel n,\n"
                + "ent_tipo_beca_periodo tbp, cat_tipo_beca tb\n"
                + "WHERE a.estatus = 1\n"
                + "AND o.alumno_id = a.id\n"
                + "AND o.periodo_id = p.id\n"
                + "AND p.id = " + periodoId + "\n"
                + "AND a.unidadacademica_id = u.id\n"
                + "AND u.nivel_id = n.id\n"
                + "AND o.tipobecaperiodo_id = tbp.id\n"
                + "AND tbp.tipobeca_id = tb.id\n"
                + "AND n.id = " + nivelId + "\n"
                + "AND u.id = " + unidadId + "\n"
                + "AND tb.id = " + tipoBecaId + "\n"
                + "AND a.genero_id = g.id\n"
                + "GROUP BY g.clave";
        List<Object[]> lista = executeNativeQuery(consulta);
        return lista;
    }

    //SQLINJECTION
    @Override
    public List<Object[]> estadisticaNivelDepositos(BigDecimal periodoId, BigDecimal nivelId) {
        String consulta = "SELECT EXTRACT(month FROM d.fechadeposito) as mes, count(d.alumno_id) as total\n"
                + "FROM ent_alumno a, ent_otorgamientos o, cat_periodo p,\n"
                + "cat_unidad_academica u, cat_nivel n,\n"
                + "rmm_alumno_tarjeta_bancaria atb,\n"
                + "ent_deposito d\n"
                + "WHERE a.estatus = 1\n"
                + "AND o.alumno_id = a.id\n"
                + "AND o.periodo_id = p.id\n"
                + "AND p.id = " + periodoId + "\n"
                + "AND a.unidadacademica_id = u.id\n"
                + "AND u.nivel_id = n.id\n"
                + "AND n.id = " + nivelId + "\n"
                + "AND atb.alumno_id = a.id\n"
                + "AND atb.tarjetaactiva = 1\n"
                + "AND d.tarjetabancaria_id = atb.tarjetabancaria_id\n"
                + "AND d.alumno_id = a.id\n"
                + getPeriodo(periodoId)
                + "GROUP BY EXTRACT(month FROM d.fechadeposito)"
                + "ORDER BY EXTRACT(month FROM d.fechadeposito)";
        List<Object[]> lista = executeNativeQuery(consulta);
        return lista;
    }

    //SQLINJECTION
    @Override
    public List<Object[]> estadisticaUnidadDepositos(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadId) {
        String consulta = "SELECT EXTRACT(month FROM d.fechadeposito) as mes, count(d.alumno_id) as total\n"
                + "FROM ent_alumno a, ent_otorgamientos o, cat_periodo p,\n"
                + "cat_unidad_academica u, cat_nivel n,\n"
                + "rmm_alumno_tarjeta_bancaria atb,\n"
                + "ent_deposito d\n"
                + "WHERE a.estatus = 1\n"
                + "AND o.alumno_id = a.id\n"
                + "AND o.periodo_id = p.id\n"
                + "AND p.id = " + periodoId + "\n"
                + "AND a.unidadacademica_id = u.id\n"
                + "AND u.nivel_id = n.id\n"
                + "AND n.id = " + nivelId + "\n"
                + "AND u.id = " + unidadId + "\n"
                + "AND atb.alumno_id = a.id\n"
                + "AND atb.tarjetaactiva = 1\n"
                + "AND d.tarjetabancaria_id = atb.tarjetabancaria_id\n"
                + "AND d.alumno_id = a.id\n"
                + getPeriodo(periodoId)
                + "GROUP BY EXTRACT(month FROM d.fechadeposito)"
                + "ORDER BY EXTRACT(month FROM d.fechadeposito)";
        List<Object[]> lista = executeNativeQuery(consulta);
        return lista;
    }

    //SQLINJECTION
    @Override
    public List<Object[]> estadisticaBecaDepositos(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadId, BigDecimal tipoBecaId) {
        String consulta = "SELECT EXTRACT(month FROM d.fechadeposito) as mes, count(d.alumno_id) as total\n"
                + "FROM ent_alumno a, ent_otorgamientos o, cat_periodo p,\n"
                + "cat_unidad_academica u, cat_nivel n,\n"
                + "rmm_alumno_tarjeta_bancaria atb,\n"
                + "ent_deposito d,\n"
                + "ent_tipo_beca_periodo tbp, cat_tipo_beca tb\n"
                + "WHERE a.estatus = 1\n"
                + "AND o.alumno_id = a.id\n"
                + "AND o.periodo_id = p.id\n"
                + "AND p.id = " + periodoId + "\n"
                + "AND a.unidadacademica_id = u.id\n"
                + "AND u.nivel_id = n.id\n"
                + "AND n.id = " + nivelId + "\n"
                + "AND u.id = " + unidadId + "\n"
                + "AND atb.alumno_id = a.id\n"
                + "AND atb.tarjetaactiva = 1\n"
                + "AND o.tipobecaperiodo_id = tbp.id\n"
                + "AND tbp.tipobeca_id = tb.id\n"
                + "AND tb.id = " + tipoBecaId + "\n"
                + "AND d.tarjetabancaria_id = atb.tarjetabancaria_id\n"
                + "AND d.alumno_id = a.id\n"
                + getPeriodo(periodoId)
                + "GROUP BY EXTRACT(month FROM d.fechadeposito)"
                + "ORDER BY EXTRACT(month FROM d.fechadeposito)";
        List<Object[]> lista = executeNativeQuery(consulta);
        return lista;
    }

    public String getPeriodo(BigDecimal periodoId) {
        if (periodoId.intValueExact() % 2 == 0) {
            return "AND EXTRACT(month FROM d.fechadeposito) IN (2, 3, 4, 5, 6, 7)\n";
        } else {
            return "AND EXTRACT(month FROM d.fechadeposito) IN (8, 9, 10, 11, 12, 1)\n";
        }
    }
//----------------------------------------------------------------------------------------------------------------

    //SQLINJECTION
    @Override
    public List<Object[]> estadisticaNivelEstatusT(BigDecimal periodoId, BigDecimal nivelId) {
        String consulta = "SELECT e.nombre, count(*) as total\n"
                + "FROM(SELECT a.id as id, MAX(btb.tarjetabancariaestatus_id) as estatus\n"
                + "FROM ent_alumno a, ent_otorgamientos o, cat_periodo p,\n"
                + "cat_unidad_academica u, cat_nivel n,\n"
                + "rmm_alumno_tarjeta_bancaria atb,\n"
                + "rmm_bitacora_tarjeta_bancaria btb\n"
                + "WHERE a.estatus = 1\n"
                + "AND o.alumno_id = a.id\n"
                + "AND o.periodo_id = p.id\n"
                + "AND p.id = " + periodoId + "\n"
                + "AND a.unidadacademica_id = u.id\n"
                + "AND u.nivel_id = n.id\n"
                + "AND n.id = " + nivelId + "\n"
                + "AND atb.alumno_id = a.id\n"
                + "AND atb.tarjetaactiva = 1\n"
                + "AND btb.tarjetabancaria_id = atb.tarjetabancaria_id\n"
                + "GROUP BY a.id)\n"
                + "  JOIN cat_estatus_tarjeta_bancaria e ON e.id = estatus\n"
                + "GROUP BY e.nombre";
        List<Object[]> lista = executeNativeQuery(consulta);
        return lista;
    }

    //SQLINJECTION
    @Override
    public List<Object[]> estadisticaUnidadEstatusT(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadId) {
        String consulta = "SELECT e.nombre, count(*) as total\n"
                + "FROM(SELECT a.id as id, MAX(btb.tarjetabancariaestatus_id) as estatus\n"
                + "FROM ent_alumno a, ent_otorgamientos o, cat_periodo p,\n"
                + "cat_unidad_academica u, cat_nivel n,\n"
                + "rmm_alumno_tarjeta_bancaria atb,\n"
                + "rmm_bitacora_tarjeta_bancaria btb\n"
                + "WHERE a.estatus = 1\n"
                + "AND o.alumno_id = a.id\n"
                + "AND o.periodo_id = p.id\n"
                + "AND p.id = " + periodoId + "\n"
                + "AND a.unidadacademica_id = u.id\n"
                + "AND u.nivel_id = n.id\n"
                + "AND n.id = " + nivelId + "\n"
                + "AND u.id = " + unidadId + "\n"
                + "AND atb.alumno_id = a.id\n"
                + "AND atb.tarjetaactiva = 1\n"
                + "AND btb.tarjetabancaria_id = atb.tarjetabancaria_id\n"
                + "GROUP BY a.id)\n"
                + "  JOIN cat_estatus_tarjeta_bancaria e ON e.id = estatus\n"
                + "GROUP BY e.nombre";
        List<Object[]> lista = executeNativeQuery(consulta);
        return lista;
    }

    //SQLINJECTION
    @Override
    public List<Object[]> estadisticaBecaEstatusT(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadId, BigDecimal tipoBecaId) {
        String consulta = "SELECT e.nombre, count(*) as total\n"
                + "FROM(SELECT a.id as id, MAX(btb.tarjetabancariaestatus_id) as estatus\n"
                + "FROM ent_alumno a, ent_otorgamientos o, cat_periodo p,\n"
                + "cat_unidad_academica u, cat_nivel n,\n"
                + "rmm_alumno_tarjeta_bancaria atb,\n"
                + "rmm_bitacora_tarjeta_bancaria btb,\n"
                + "ent_tipo_beca_periodo tbp, cat_tipo_beca tb\n"
                + "WHERE a.estatus = 1\n"
                + "AND o.alumno_id = a.id\n"
                + "AND o.periodo_id = p.id\n"
                + "AND p.id = " + periodoId + "\n"
                + "AND a.unidadacademica_id = u.id\n"
                + "AND u.nivel_id = n.id\n"
                + "AND n.id = " + nivelId + "\n"
                + "AND u.id = " + unidadId + "\n"
                + "AND atb.alumno_id = a.id\n"
                + "AND atb.tarjetaactiva = 1\n"
                + "AND o.tipobecaperiodo_id = tbp.id\n"
                + "AND tbp.tipobeca_id = tb.id\n"
                + "AND tb.id = " + tipoBecaId + "\n"
                + "AND btb.tarjetabancaria_id = atb.tarjetabancaria_id\n"
                + "GROUP BY a.id)\n"
                + "  JOIN cat_estatus_tarjeta_bancaria e ON e.id = estatus\n"
                + "GROUP BY e.nombre";
        List<Object[]> lista = executeNativeQuery(consulta);
        return lista;
    }

    @Override
    public List<LinkedHashMap<String, Object>> getAlumnosConBaja(BigDecimal unidadAcademicaId) {
        StringBuilder sb = new StringBuilder();
        List<String> criteria = new ArrayList<>();
        List<String> columns = new ArrayList<>();
        columns.add("periodo");
        columns.add("bajas");

        // El distinct cambia los resultados para otorgamientos del periodo actual, 
        // puesto que a veces se arrepienten, y hay varias bajas al mismo otorgamiento
        sb.append(" SELECT o.PERIODO_ID, ");
        sb.append(" COUNT(DISTINCT(o.ID)) ");
        sb.append(" FROM VW_ULTIMA_BAJA_DETALLE ob");
        sb.append(" INNER JOIN ENT_OTORGAMIENTOS o ON o.ID = ob.OTORGAMIENTO_ID");

        if (unidadAcademicaId != null) {
            sb.append(" INNER JOIN ENT_ALUMNO_DATOS_ACADEMICOS da ON da.ALUMNO_ID = o.ALUMNO_ID");
            sb.append("  AND da.periodo_id = o.periodo_id");

            criteria.add("da.UNIDADACADEMICA_ID = ?1");
        }

        criteria.add("ob.PERIODO_ID = (SELECT p.ID FROM cat_periodo p WHERE p.estatus=1)");
        criteria.add("o.PROCESO_ID IS NOT NULL");
        criteria.add("o.ALTA = 0");
        agregaCriterios(sb, criteria);

        sb.append(" GROUP BY o.PERIODO_ID");

        List<Object[]> result = executeNativeQuery(sb.toString(), unidadAcademicaId);

        return !result.isEmpty() ? creaModelo(result, columns) : null;
    }

    @Override
    public BigDecimal totalAlumnosConOtorgamiento(BigDecimal unidadAcademicaId) {
        StringBuilder sql = new StringBuilder();

        if (unidadAcademicaId == null) {
            sql.append("select count(*),1 from vw_otorgamientos where alta = 1");
            sql.append(" and periodo_id = (select p.id from cat_periodo p where p.estatus=1) ");
            sql.append(" AND proceso_id IS NOT NULL ");
        } else {
            sql.append("select count(*),1 from vw_otorgamientos o, ENT_ALUMNO a, ENT_ALUMNO_DATOS_ACADEMICOS da ");
            sql.append("where o.alta = 1 ");
            sql.append("and a.id = o.alumno_id ");
            sql.append("and da.alumno_id = o.ALUMNO_ID and da.periodo_id = o.periodo_id ");
            sql.append("AND o.proceso_id IS NOT NULL ");
            sql.append("AND da.UNIDADACADEMICA_ID= ?1");
            sql.append(" and o.periodo_id = (select p.id from cat_periodo p where p.estatus=1) ");
        }

        List<Object[]> lista = executeNativeQuery(sql.toString(), unidadAcademicaId);
        BigDecimal total = new BigDecimal(0);
        if (lista != null && !lista.isEmpty()) {
            for (Object[] res : lista) {
                total = res[0] == null ? new BigDecimal(0) : ((BigDecimal) res[0]);
            }
        }

        return total;
    }

    @Override
    public String totalAlumnosConOtorgamientoD(Boolean alta, BigDecimal nivelId) {
        String consulta = "";
        if (alta) {
            consulta = "select ua.id, ua.nombrecorto, contador  from CAT_UNIDAD_ACADEMICA ua "
                    + " left join (select ua. ID, ua.NOMBRECORTO, count(o.id) as contador "
                    + " from ent_otorgamientos o, ENT_ALUMNO a, CAT_UNIDAD_ACADEMICA ua, ENT_ALUMNO_DATOS_ACADEMICOS da "
                    + " where a.estatus = 1"
                    + " and a.id = o.alumno_id"
                    + " and a.id = da.ALUMNO_ID and da.PERIODO_ID = (select id from cat_periodo where estatus=1 and rownum = 1) "
                    + " and da.UNIDADACADEMICA_ID = ua.ID"
                    + " and ua.NIVEL_ID = ?1"
                    + " and o.proceso_id IS NOT NULL"
                    + " and alta = 1"
                    + " and o.periodo_id = (select p.id from cat_periodo p where p.estatus=1)"
                    + " GROUP BY ua.ID, ua.NOMBRECORTO) datos on ua.id = datos.ID where ua.NIVEL_ID = ?1"
                    + " ORDER BY ua.ID";
        } else {
            consulta = "select ua.id, ua.nombrecorto, contador  from CAT_UNIDAD_ACADEMICA ua "
                    + " left join (select ua.ID, ua.NOMBRECORTO, count(o.id) as contador "
                    + " from ent_otorgamientos o, ENT_ALUMNO a, CAT_UNIDAD_ACADEMICA ua, VW_ULTIMA_BAJA_DETALLE ob, ENT_ALUMNO_DATOS_ACADEMICOS da  "
                    + " where ob.OTORGAMIENTO_ID = o.id "
                    + " and a.id = o.alumno_id "
                    + " and a.id = da.ALUMNO_ID and da.PERIODO_ID = (select id from cat_periodo where estatus=1 and rownum = 1) "
                    + " and da.UNIDADACADEMICA_ID = ua.ID "
                    + " and ua.NIVEL_ID = ?1 "
                    + " and o.proceso_id IS NOT NULL "
                    + " and o.alta = 0 "
                    + " and ob.periodo_id = (select p.id from cat_periodo p where p.estatus=1) "
                    + " GROUP BY ua.ID, ua.NOMBRECORTO) datos on ua.id = datos.ID where ua.NIVEL_ID = ?1 "
                    + " ORDER BY ua.ID";
        }

        List<Object[]> ax = executeNativeQuery(consulta, nivelId);
        String s = "";
        for (Object[] res : ax) {
            s = s + res[1] + "," + res[2];
            s = s + "," + detalleOt((BigDecimal) res[0], alta) + " TOTAL: " + res[2];
            s = s + "/";
        }
        if (s.length() > 1) {
            return s;
        } else {
            return null;
        }
    }

    public String detalleOt(BigDecimal uaId, Boolean alta) {
        String consulta = "";
        if (alta) {
            consulta = consulta + "select m.NOMBRE , count(o.id)"
                    + " from ent_otorgamientos o, ENT_ALUMNO a, CAT_UNIDAD_ACADEMICA ua, CAT_TIPO_PROCESO tp, ENT_PROCESO p, CAT_MOVIMIENTO m, ENT_ALUMNO_DATOS_ACADEMICOS da "
                    + " where a.estatus = 1"
                    + " and a.id = o.alumno_id"
                    + " and a.id = da.ALUMNO_ID and da.PERIODO_ID = (select id from cat_periodo where estatus=1 and rownum = 1) "
                    + " and da.UNIDADACADEMICA_ID = ua.ID"
                    + " and ua.id = ?1"
                    + " and o.proceso_id IS NOT NULL"
                    + " and o.PROCESO_ID = p.id"
                    + " and p.TIPOPROCESO_ID = tp.id"
                    + " and tp.MOVIMIENTO_ID = m.id"
                    + " and alta = 1"
                    + " and o.periodo_id = (select p.id from cat_periodo p where p.estatus=1)"
                    + " GROUP BY m.NOMBRE";
        } else {
            consulta = consulta + "select m.NOMBRE , count(unique (o.id)) "
                    + "from ent_otorgamientos o, ENT_ALUMNO a, CAT_UNIDAD_ACADEMICA ua, ent_otorgamientos_bajas_detall ob, CAT_TIPO_PROCESO tp, ENT_PROCESO p, CAT_MOVIMIENTO m, ENT_ALUMNO_DATOS_ACADEMICOS da "
                    + "where ob.OTORGAMIENTO_ID = o.id "
                    + "and a.id = o.alumno_id "
                    + "and a.id = da.ALUMNO_ID and da.PERIODO_ID = (select id from cat_periodo where estatus=1 and rownum = 1) "
                    + "and da.UNIDADACADEMICA_ID = ua.ID "
                    + "and ua.id = ?1 "
                    + "and o.proceso_id IS NOT NULL "
                    + "and ob.PROCESO_ID = p.id "
                    + "and p.TIPOPROCESO_ID = tp.id "
                    + "and tp.MOVIMIENTO_ID = m.id "
                    + "and o.alta = 0 "
                    + "and ob.periodo_id = (select p.id from cat_periodo p where p.estatus=1) "
                    + "GROUP BY m.NOMBRE";
        }
        List<Object[]> ax = executeNativeQuery(consulta, uaId);
        String s = "";
        for (Object[] res : ax) {
            s = s + res[0] + ": " + res[1] + " - ";
        }
        if (s.length() > 1) {
            return s;
        } else {
            return null;
        }
    }

    @Override
    public int revertirAsignacion() {
        String sql = "delete from ent_otorgamientos where PROCESO_ID IS NULL and AUTOMATICO=1 and PERIODO_ID=?1";
        BigDecimal peroido_id = getDaos().getPeriodoDao().getPeriodoActivo().getId();
        //sql.append(peroido_id.toString
        return executeNativeUpdate(sql, peroido_id);
    }

    @Override
    public Long getCandidatosNuevos(Periodo periodoActivo, Integer nivel) {
        String jpql
                = "SELECT COUNT(A) FROM Alumno A "
                + "JOIN SolicitudBeca C ON C.alumno.id = A.id "
                + "WHERE A.estatus = 1 "
                + "AND NOT EXISTS( "
                + "    SELECT 1 FROM Otorgamiento O "
                + "    WHERE O.periodo.id = ?2 AND O.proceso IS NOT NULL AND O.alta = 1 AND O.alumno.id = A.id "
                + ") AND NOT EXISTS( "
                + "   SELECT 1 FROM Otorgamiento O WHERE O.periodo.id = ?1 AND O.alumno.id = A.id "
                + ") "
                + "AND A.inscrito = 1 "
                + "AND C.periodo.id = ?1 "
                + "AND C.cuestionario.id = 1 "
                + "AND A.datosAcademicos.unidadAcademica.nivel.id = ?3";

        return getCountQuery(jpql, periodoActivo.getId(), periodoActivo.getPeriodoAnterior().getId(), nivel);
    }

    @Override
    public int getCandidatosRevalidacion(Periodo periodoActivo, Integer nivel) {
        String sql
                = "SELECT COUNT(*) "
                + "FROM ENT_OTORGAMIENTOS O "
                + "JOIN ENT_ALUMNO A ON O.ALUMNO_ID = A.ID "
                + "LEFT JOIN ent_solicitud_becas CU ON CU.ALUMNO_ID = A.ID AND CU.PERIODO_ID = ?2 AND CU.CUESTIONARIO_ID = 1 "
                + "JOIN CAT_UNIDAD_ACADEMICA CUA ON CUA.ID = A.UNIDADACADEMICA_ID "
                + "JOIN ENT_TIPO_BECA_PERIODO T ON O.TIPOBECAPERIODO_ID = T.ID "
                + "JOIN CAT_TIPO_BECA C ON T.TIPOBECA_ID = C.ID "
                + "JOIN ENT_TIPO_BECA_PERIODO TA ON TA.TIPOBECA_ID = C.ID AND TA.PERIODO_ID= ?2 "
                + "WHERE O.PERIODO_ID = ?1 "
                + "AND (CU.ID IS NOT NULL OR TA.VALIDACIONDEINSCRIPCION>0)"
                + "AND O.ALTA = 1 "
                + "AND O.proceso_id is not null "
                + "AND A.INSCRITO = 1 "
                + "AND A.ESTATUS = 1 "
                + "AND a.infoActualizadaAdmin=1 "
                + "AND NOT EXISTS(SELECT 1 FROM ENT_OTORGAMIENTOS o2 where o.alumno_id = o2.alumno_id and o2.tipoBecaPeriodo_id=ta.id and o2.periodo_id=?2 ) "
                + "AND CUA.NIVEL_ID = ?3 ";
        //+ "AND C.BECA_ID NOT IN (7,8,9)";
        Long countNativeQuery = getCountNativeQuery(sql, periodoActivo.getPeriodoAnterior().getId(), periodoActivo.getId(), nivel);
        return countNativeQuery.intValue();
    }

    @Override
    public List<Otorgamiento> revalidantes(BigDecimal unidadID, TipoBecaPeriodo tipoBecaPeriodo,
            Integer nivel, BigDecimal periodoAnteriorId, BigDecimal periodoActual) {

        String jpql = "SELECT o FROM Otorgamiento o "
                + "JOIN Alumno a on O.alumno.id = a.id "
                + "LEFT JOIN a.cuestionarios EC on EC.periodo.id=34 and EC.cuestionario.id=1 "
                + "WHERE O.periodo.id = ?1 "
                + "AND O.alta = 1 "
                + "AND A.unidadAcademica.id = ?3 "
                + "AND O.tipoBecaPeriodo.tipoBeca.id = ?4 "
                + "AND A.unidadAcademica.nivel.id = ?5 "
                + "AND O.proceso is not null "
                + "AND (EC.id is not null OR o.tipoBecaPeriodo.tipoBeca.id IN (SELECT tb.id FROM TipoBeca tb"
                + "        JOIN TipoBecaPeriodo tbp ON tbp.tipoBeca.id = tb.id"
                + "        WHERE tbp.periodo.id = ?2 and tbp.validaciondeinscripcion>0))"
                //        + "AND O.tipoBecaPeriodo.tipoBeca.beca.id not in (7, 8, 9) "
                + "AND a.inscrito = 1 "
                + "AND a.estatus = 1 "
                + "AND a.infoActualizadaAdmin=1 "
                + "AND NOT EXISTS(SELECT 1 FROM Otorgamiento o2 where o.alumno.id = o2.alumno.id and o2.tipoBecaPeriodo.tipoBeca.id=?4 and o2.periodo.id=?2 ) "
                + "ORDER BY a.promedio DESC";

        return executeQuery(jpql, periodoAnteriorId, periodoActual, unidadID,
                tipoBecaPeriodo.getTipoBeca().getId(), nivel);
    }

    @Override
    public int getOtorgamientosEscuela(BigDecimal periodoID, BigDecimal tipoBecaPeriodoId, Integer nivel) {
        String sql = "SELECT COUNT(*) FROM ent_otorgamientos o"
                + " JOIN ent_alumno a ON O.ALUMNO_ID = a.id"
                + " JOIN cat_unidad_academica uni ON uni.id = a.unidadacademica_id"
                + " JOIN ent_tipo_beca_periodo T ON O.TIPOBECAPERIODO_ID = T.ID"
                + " JOIN cat_tipo_beca C ON T.TIPOBECA_ID = C.ID"
                + " WHERE O.periodo_id = ?1  and O.ALTA = 1 AND T.id = ?2 "
                + "AND uni.nivel_id =?3 AND C.beca_ID NOT IN(7,8,9)";
        Long results = getCountNativeQuery(sql, periodoID, tipoBecaPeriodoId, nivel);
        return results.intValue();
    }

    /**
     * Devuelve el otorgamiento de un determinado alumno para cierto periodo
     *
     * @author Tania G. Sánchez
     * @param alumno_id
     * @param periodo_id
     * @return otorgamiento
     */
    @Override
    public List<Otorgamiento> getOtorgamientosAlumno(BigDecimal alumno_id, BigDecimal periodo_id
    ) {
        String consulta = "select o from Otorgamiento o "
                + "where o.proceso is not null and o.alumno.id = ?1 and o.periodo.id = ?2 and o.alta = 1";
        List<Otorgamiento> lista = executeQuery(consulta, alumno_id, periodo_id);
        return ((lista == null || lista.isEmpty()) ? null : lista);
    }

    @Override
    public List<Otorgamiento> getOtorgamientosAlumno(BigDecimal alumno_id) {
        String consulta = "select o from Otorgamiento o "
                + " where o.proceso is not null and o.alumno.id = ?1"
                + " order by o.periodo.id desc";
        List<Otorgamiento> lista = executeQuery(consulta, alumno_id);
        return ((lista == null || lista.isEmpty()) ? null : lista);
    }

    @Override
    public Otorgamiento getPreasignacion(BigDecimal alumnoId, Beca beca
    ) {
        String jpql = "SELECT o FROM Otorgamiento o "
                + " WHERE o.alumno.id = ?1 "
                + " AND o.periodo.id = ?2 "
                + " AND o.proceso IS NULL "
                + " AND o.tipoBecaPeriodo.tipoBeca.beca.id=?3";
        BigDecimal periodoId = getDaos().getPeriodoDao().getPeriodoActivo().getId();
        List<Otorgamiento> preasignacion = executeQuery(jpql, alumnoId, periodoId, beca.getId());
        return preasignacion == null || preasignacion.isEmpty() ? null : preasignacion.get(0);
    }

    @Override
    public List<Otorgamiento> getPreasignaciones(BigDecimal alumnoId
    ) {
        String jpql = "SELECT o FROM Otorgamiento o "
                + " WHERE o.alumno.id = ?1 "
                + " AND o.periodo.id = ?2 "
                + " AND o.proceso IS NULL";
        BigDecimal periodoId = getDaos().getPeriodoDao().getPeriodoActivo().getId();
        List<Otorgamiento> preasignacion = executeQuery(jpql, alumnoId, periodoId);
        return preasignacion == null || preasignacion.isEmpty() ? null : preasignacion;
    }

    @Override
    public void savePreAsignacion(Otorgamiento nuevoOtorgamiento) {
        entityManager.persist(nuevoOtorgamiento);
        entityManager.flush();
    }

    @Override
    public void saveSolicitud(SolicitudBeca solicitudBeca) {
        entityManager.persist(solicitudBeca);
        entityManager.flush();
    }

    @Override
    public Otorgamiento otorgamientoDeposito(BigDecimal alumnoId, BigDecimal periodoId, BigDecimal programaBeca) {
        String jpql = "select o from Otorgamiento o where o.alumno.id = ?1 and o.periodo.id = ?2 "
                + " and o.tipoBecaPeriodo.tipoBeca.beca.id=?3";
        List<Otorgamiento> preasignacion = executeQuery(jpql, alumnoId, periodoId, programaBeca);
        return preasignacion == null || preasignacion.isEmpty() ? null : preasignacion.get(0);
    }

    /**
     * Verifica si se le debe mostrar o no al alumno su otorgamiento para el
     * periodo actual.
     *
     * @param alumnoId
     * @return true si el alumno ya puede ver su otorgamiento.
     */
    @Override
    public Boolean mostrarOtorgamiento(BigDecimal alumnoId) {
        String sql = "select * from ent_otorgamientos o"
                + "   join ent_proceso p on p.id = o.proceso_id"
                //                + "   join rmm_alumno_tarjeta_bancaria atb on atb.alumno_id = o.alumno_id"
                + " where p.procesoestatus_id = 4 and p.fechafinal < current_timestamp"
                //				+ " and atb.tarjetaactiva = 1 and atb.vigente = 1"
                + " and o.periodo_id = (select id from cat_periodo where estatus = 1)"
                + " and o.excluirdeposito = 0 and o.alumno_id = ?1";
        List<Object[]> lista = executeNativeQuery(sql, alumnoId);
        return lista != null && !lista.isEmpty();
    }

    //SQLINJECTION OK
    @Override
    public Long getEstadisticaPendientes(Estadistica.Tipo tipo, BigDecimal periodoId, BigDecimal nivelId,
            BigDecimal unidadAcademicaId, BigDecimal becaId, BigDecimal movimientoId, String genero, Object parametro) {

        StringBuilder sb = new StringBuilder();
        List<String> criteria = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        sb.append(" SELECT COUNT(*)");
        sb.append(" FROM VW_OTORGAMIENTOS o");
        sb.append(" INNER JOIN ENT_ALUMNO a");
        sb.append(" ON o.ALUMNO_ID = a.ID");
        sb.append(" INNER JOIN CAT_GENERO g");
        sb.append(" ON g.ID = a.GENERO_ID");

        if (null != unidadAcademicaId || null != nivelId) {
            sb.append(" INNER JOIN ENT_PROCESO p");
            sb.append(" ON o.PROCESO_ID = p.ID");
            sb.append(" INNER JOIN CAT_UNIDAD_ACADEMICA ua");
            sb.append(" ON p.UNIDADACADEMICA_ID = ua.ID");
        }
        if (becaId != null) {
            sb.append(" INNER JOIN ENT_TIPO_BECA_PERIODO tbp");
            sb.append(" ON tbp.ID = o.TIPOBECAPERIODO_ID  ");
            sb.append(" INNER JOIN CAT_TIPO_BECA tb");
            sb.append(" ON tb.ID = tbp.TIPOBECA_ID");
        }

        criteria.add("o.PERIODO_ID = #periodoId");
        params.put("periodoId", periodoId);
        criteria.add("o.IDENTIFICADOROTORGAMIENTO_ID IN ( 7, 13 )");
        criteria.add("g.CLAVE= #genero");
        params.put("genero", genero);

        if (null != unidadAcademicaId) {
            criteria.add("ua.ID = #unidadAcademicaId");
            params.put("unidadAcademicaId", unidadAcademicaId);
        } else if (null != nivelId) {
            criteria.add("ua.NIVEL_ID = #nivelId");
            params.put("nivelId", nivelId);
        }
        if (becaId != null) {
            criteria.add("tb.BECA_ID = #becaId");
            params.put("becaId", becaId);
        }
        agregaCriterios(sb, criteria);

        return getCountNativeQueryMap(sb.toString(), params);
    }

    // SQLINJECTION OK
    @Override
    public Long getEstadistica(Estadistica.Tipo tipo, BigDecimal periodoId, BigDecimal nivelId,
            BigDecimal unidadAcademicaId, BigDecimal becaId, BigDecimal movimientoId, String genero, Object parametro) {

        StringBuilder sb = new StringBuilder();
        List<String> criteria = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        sb.append(" SELECT COUNT(o)");
        sb.append(" FROM VWOtorgamiento o");

        //Si es baja
        if (tipo == Estadistica.Tipo.MOVIMIENTOS) {
            Movimiento mov = (Movimiento) parametro;
            if (mov.getNombre().contains("Baja")) {
                sb.append(" JOIN FETCH o.alumno");
                sb.append(" INNER JOIN OtorgamientoBajasDetalle b ON b.otorgamiento.id = o.id");

//                criteria.add("o.alta = 0");
                criteria.add("AND b.ID IS NOT NULL");
                criteria.add("b.periodo.id = :periodoId");
                params.put("periodoId", periodoId);
                criteria.add("b.proceso.tipoProceso.movimiento = :movimiento");
                params.put("movimiento", mov);
            } else {
                criteria.add("o.proceso.tipoProceso.movimiento = :movimiento");
                params.put("movimiento", mov);
            }
        } else {
            //Otorgamiento
//            criteria.add("o.alta = 1");
            criteria.add("o.periodo.id = :periodoId");
            params.put("periodoId", periodoId);
            criteria.add("o.proceso IS NOT NULL");
        }

        criteria.add("o.alumno.genero.clave = :genero");
        params.put("genero", genero);

        if (null != unidadAcademicaId) {
            criteria.add("o.proceso.unidadAcademica.id = :unidadAcademicaId");
            params.put("unidadAcademicaId", unidadAcademicaId);
        } else if (null != nivelId) {
            criteria.add("o.proceso.unidadAcademica.nivel.id = :nivelId");
            params.put("nivelId", nivelId);
        }
        if (becaId != null) {
            criteria.add("o.tipoBecaPeriodo.tipoBeca.beca.id = :becaId");
            params.put("becaId", becaId);
        }
        if (tipo != Estadistica.Tipo.MOVIMIENTOS && movimientoId != null) {
            criteria.add("o.proceso.tipoProceso.movimiento.id = :movimientoId");
            params.put("movimientoId", movimientoId);
        }

        switch (tipo) {
            case CARRERA:
                if (parametro != null) {
                    criteria.add("o.datosAcademicos.carrera.carrera = :carreraP");
                    params.put("carreraP", parametro);
                } else {
                    return 0L;
                }
                break;
            case PROGRAMA_BECA:
                Beca beca = (Beca) parametro;
                criteria.add("o.tipoBecaPeriodo.tipoBeca.beca = :beca");
                params.put("beca", beca);
                break;
            case PROMEDIOS:
                criteria.add("o.datosAcademicos.promedio " + parametro);
                break;
            case SEMESTRES:
                criteria.add("o.datosAcademicos.semestre " + parametro);
                break;
            case OTORGAMIENTOS:
                SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
                String date = DATE_FORMAT.format(parametro);
                criteria.add("o.fecha LIKE :fecha");
                params.put("fecha", '%' + date + '%');
                break;
        }

        agregaCriterios(sb, criteria);

        return getCountQuery(sb.toString(), params);
    }

    /**
     * Verifica si se le debe mostrar o no al alumno su otorgamiento para el
     * periodo actual.
     *
     * @param alumnoId
     * @return true si el alumno ya puede ver su otorgamiento.
     */
    @Override
    public Boolean tieneOtorgamientoPeriodoActual(BigDecimal alumnoId) {
        String sql = "select * from ent_otorgamientos o"
                + "   join cat_periodo p on p.id = o.periodo_id and p.estatus = 1"
                + " inner join ENT_TIPO_BECA_PERIODO tbp ON o.TIPOBECAPERIODO_ID = tbp.id"
                + " inner join CAT_TIPO_BECA tb ON tbp.TIPOBECA_ID = tb.ID"
                + " inner join CAT_PROGRAMA_BECA b ON tb.BECA_ID = b.ID"
                + " where o.alumno_id = ?1 and o.proceso_id is not null"
                + " and b.ID != 10"
                + " and o.id not in (select otorgamiento_id from ent_otorgamientos_bajas_detall where periodo_id = p.id)";
        List<Object[]> lista = executeNativeQuery(sql, alumnoId);
        return lista != null && !lista.isEmpty();
    }

    /**
     * Verifica si hay otorgamientos para el alumno en el periodo especificado
     * con alta = 1, que no sean otorgamientos de beca universal
     *
     * @param alumnoId
     * @param periodoId
     * @param transporte Es posible buscar por otorgamientos de transporte, o
     * todo lo que no sea transporte. Si es nulo, se busca todo
     * @return true Si hay otorgamientos
     */
    @Override
    public Boolean tieneOtorgamientoPeriodo(BigDecimal alumnoId, BigDecimal periodoId, Boolean transporte) {
        StringBuilder sb = new StringBuilder();
        List<String> criteria = new ArrayList<>();

        sb.append(" select count(*) ");
        sb.append(" from VW_OTORGAMIENTOS o");
        sb.append(" inner join ENT_TIPO_BECA_PERIODO tbp ON o.TIPOBECAPERIODO_ID = tbp.id");
        sb.append(" inner join CAT_TIPO_BECA tb ON tbp.TIPOBECA_ID = tb.ID");
        sb.append(" inner join CAT_PROGRAMA_BECA b ON tb.BECA_ID = b.ID");

        criteria.add("o.alumno_id = ?1");
        criteria.add("o.PERIODO_ID = ?2");
        criteria.add("o.ALTA = 1");
        criteria.add("b.ID != 10");

        if (transporte != null) {
            if (transporte) {
                criteria.add("b.ID IN (7, 8, 9)");
            } else {
                criteria.add("b.ID NOT IN (7, 8, 9)");
            }
        }

        agregaCriterios(sb, criteria);
        Long result = getCountNativeQuery(sb.toString(), alumnoId, periodoId);

        return result > 0;
    }

    @Override
    public List<Otorgamiento> getUltimoOtorgamiento(Alumno alumno) {
        String jpql
                = "SELECT o FROM Otorgamiento o "
                + "WHERE o.alumno.boleta = ?1 "
                // Pensando en que este filtro es para excluir a los alumnos
                // de beca universal
                + "AND o.solicitudBeca.clasificacionSolicitud.id = 1 "
                + "ORDER BY o.periodo.id DESC ";
        return executeQuery(jpql, alumno.getBoleta());
    }

    @Override
    public List<Date> getFechasOtorgamientos(BigDecimal periodoId) {
        String sql = "select DISTINCT trunc(FECHA), 1 from ENT_OTORGAMIENTOS "
                + "where PERIODO_ID = ?1 "
                + "order by trunc(FECHA) DESC";
        List<Object[]> lista = executeNativeQuery(sql, periodoId);
        List<Date> lo = new ArrayList<>();
        for (Object[] res : lista) {
            lo.add((Date) res[0]);
        }
        return lo.isEmpty() ? null : lo;
    }

    //SQLINJECTION OK
    @Override
    public List<Object[]> estadisticaRendimiento(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal becaId) {
        StringBuilder sb = new StringBuilder();
        List<String> criteria = new ArrayList<>();
        Map<String, Object> params = new HashMap();

        sb.append(" select * from ( ");
        sb.append(" select trunc(fecha) as fecha,g.clave as clave, count(*) as total ");
        sb.append(" from ent_otorgamientos o ");
        sb.append("   join ent_alumno a on a.id = o.alumno_id ");
        sb.append("   join cat_genero g on g.id = a.genero_id ");
        sb.append("   join ENT_PROCESO p on p.id = o.proceso_id ");
        sb.append("   join CAT_TIPO_PROCESO tp on tp.id = p.TIPOPROCESO_ID ");
        sb.append("   join CAT_MOVIMIENTO m on m.id = tp.MOVIMIENTO_ID ");
        sb.append("   JOIN CAT_UNIDAD_ACADEMICA ua on ua.id = p.unidadacademica_id ");
        sb.append("   join ENT_TIPO_BECA_PERIODO tbp on tbp.id = o.tipobecaperiodo_id ");
        sb.append("   join CAT_TIPO_BECA tb on tb.id = tbp.TIPOBECA_ID ");
        sb.append("   join CAT_PROGRAMA_BECA b on b.id = tb.beca_id ");

        criteria.add("o.periodo_id = #periodoId");
        params.put("periodoId", periodoId);
        criteria.add("o.alta = 1");
        criteria.add("o.proceso_id IS NOT NULL");

        if (unidadAcademicaId != null && unidadAcademicaId.intValue() != 0) {
            criteria.add("p.unidadAcademica_id = #unidadAcademicaId");
            params.put("unidadAcademicaId", unidadAcademicaId);
        } else if (nivelId != null && nivelId.intValue() != 0) {
            criteria.add("ua.nivel_id = #nivelId");
            params.put("nivelId", nivelId);
        }
        if (becaId != null && becaId.intValue() != 0) {
            criteria.add("b.id = #becaId");
            params.put("becaId", becaId);
        }

        agregaCriterios(sb, criteria);

        sb.append(" group by trunc(o.fecha),g.clave) ");
        sb.append(" PIVOT ( ");
        sb.append(" sum(total) ");
        sb.append(" FOR clave in ('M' M,'F' F)) ");
        sb.append(" order by fecha asc");

        List<Object[]> lista = executeNativeQuery(sb.toString(), params, null);
        return lista.isEmpty() ? null : lista;
    }

    @Override
    public List<Object[]> estadisticaRegistro(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal becaId) {
        String sql = "select * from ( "
                + "select trunc(c.FECHAINGRESO) as fecha,g.clave as clave, count(c.id) as total "
                + "from ent_solicitud_becas c "
                + "  join ent_alumno a on a.id = c.alumno_id "
                + "  join ENT_ALUMNO_DATOS_ACADEMICOS da on da.ALUMNO_ID = c.ALUMNO_ID and da.PERIODO_ID = c.PERIODO_ID "
                + "  join cat_genero g on g.id = a.genero_id "
                + "  JOIN CAT_UNIDAD_ACADEMICA ua on ua.id = da.unidadacademica_id "
                + "where c.finalizado = 1 "
                + "and  c.periodo_id = ?1 ";

        sql += unidadAcademicaId == null || unidadAcademicaId.equals(BigDecimal.ZERO) ? "" : " and da.unidadAcademica_id = " + unidadAcademicaId + " ";
        sql += nivelId == null || nivelId.equals(BigDecimal.ZERO) ? "" : " and ua.nivel_id = " + nivelId + " ";
//		jpql += tipoSolicitud == null || tipoSolicitud.equals(BigDecimal.ZERO) ? "and sb.cuestionario_id in (1,2) " : " and sb.cuestionario_id = " + tipoSolicitud;

        sql += "group by trunc(c.FECHAINGRESO),g.clave) "
                + "PIVOT ( "
                + "sum(total) "
                + "FOR clave in ('M' M,'F' F)) "
                + "order by fecha asc";

        List<Object[]> lista = executeNativeQuery(sql, periodoId);
        return lista.isEmpty() ? null : lista;
    }

    //SQLINJECTION
    @Override
    public List<Object[]> estadisticaRegistroT(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal becaId) {
        String sql = "select * from ( "
                + "select 1 as fecha,g.clave as clave, count(c.id) as total "
                + "from ent_solicitud_becas c "
                + "  join ent_alumno a on a.id = c.alumno_id "
                + "  join ENT_ALUMNO_DATOS_ACADEMICOS da on da.ALUMNO_ID = c.ALUMNO_ID and da.PERIODO_ID = c.PERIODO_ID "
                + "  join cat_genero g on g.id = a.genero_id "
                + "  JOIN CAT_UNIDAD_ACADEMICA ua on ua.id = da.unidadacademica_id "
                + "where c.finalizado = 1 "
                + "and  c.periodo_id = ?1 ";
        sql += unidadAcademicaId == null || unidadAcademicaId.equals(BigDecimal.ZERO) ? "" : " and da.unidadAcademica_id = " + unidadAcademicaId + " ";
        sql += nivelId == null || nivelId.equals(BigDecimal.ZERO) ? "" : " and ua.nivel_id = " + nivelId + " ";
        //		jpql += tipoSolicitud == null || tipoSolicitud.equals(BigDecimal.ZERO) ? "and sb.cuestionario_id in (1,2) " : " and sb.cuestionario_id = " + tipoSolicitud;

        sql += "group by 1, g.clave) "
                + "PIVOT ( "
                + "sum(total) "
                + "FOR clave in ('M' M,'F' F)) ";

        List<Object[]> lista = executeNativeQuery(sql, periodoId);
        return lista.isEmpty() ? null : lista;
    }

    //SQLINJECTION
    @Override
    public List<Object[]> estadisticaDepositosT(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId) {
        String sql = "select * from ( "
                + "select 1 as fecha, ed.NOMBRE as nombre, count(d.id) as total from ENT_DEPOSITO d "
                + "  join CAT_ESTATUS_DEPOSITO ed on ed.id = d.ESTATUSDEPOSITO_ID "
                + "  join ENT_ORDEN_DEPOSITO od on od.id = d.ORDENDEPOSITO_ID "
                + "  join ENT_OTORGAMIENTOS o on o.id = d.OTORGAMIENTO_ID "
                + "  join ENT_TIPO_BECA_PERIODO tbp on tbp.id = o.TIPOBECAPERIODO_ID "
                + "  join CAT_TIPO_BECA tb on tb.id = tbp.TIPOBECA_ID "
                + "  join CAT_PROGRAMA_BECA pb on pb.id = tb.BECA_ID "
                + "left join ENT_ALUMNO_DATOS_ACADEMICOS da on o.DATOSACADEMICOS_ID = da.ID "
                + "left join CAT_UNIDAD_ACADEMICA ua on ua.id = da.UNIDADACADEMICA_ID "
                + "where od.periodo_id = ?1 and pb.id not in(10)";

        sql += unidadAcademicaId == null || unidadAcademicaId.equals(BigDecimal.ZERO) ? "" : " and ua.id = " + unidadAcademicaId + " ";
        sql += nivelId == null || nivelId.equals(BigDecimal.ZERO) ? "" : " and ua.nivel_id = " + nivelId + " ";
        sql += "group by ed.NOMBRE) "
                + "PIVOT (  "
                + "	sum(total)  "
                + "	FOR nombre in ( "
                + "		'En espera' En_espera, "
                + "		'Aplicado' Aplicado, "
                + "		'Reembolso' Reembolso, "
                + "		'Rechazado' Rechazado, "
                + "		'Cancelado' Cancelado, "
                + "		'No coincide boleta' No_coincide_boleta, "
                + "		'No coincide tarjeta' No_coincide_tarjeta, "
                + "		'No coincide monto' No_coincide_monto, "
                + "		'Depósito especial' Depósito_especial "
                + "		))  "
                + "order by fecha asc ";

        List<Object[]> lista = executeNativeQuery(sql, periodoId);
        return lista.isEmpty() ? null : lista;
    }

    //SQLINJECTION OK
    @Override
    public List<Object[]> estadisticaDepositos(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal programaBecaId) {
        boolean ua = unidadAcademicaId != null && !unidadAcademicaId.equals(BigDecimal.ZERO);
        boolean lvl = nivelId != null && !nivelId.equals(BigDecimal.ZERO);
        boolean pb = programaBecaId != null && !programaBecaId.equals(BigDecimal.ZERO);

        StringBuilder sb = new StringBuilder();
        StringBuilder sbSubQ = new StringBuilder();
        List<String> criteria = new ArrayList<>();
        Map<String, Object> params = new HashMap();

        sb.append("SELECT   *");
        sb.append(" FROM     (");
        sb.append(" SELECT    CASE WHEN od.mes = 1 THEN 13");
        sb.append(" ELSE od.mes END AS mes,");
        sb.append(" ed.nombre   AS nombre,");
        sb.append(" Count(d.id) AS total");
        sb.append(" FROM      ent_deposito d");
        sb.append(" join      cat_estatus_deposito ed");
        sb.append(" ON        ed.id = d.estatusdeposito_id");
        sb.append(" join      ent_orden_deposito od");
        sb.append(" ON        od.id = d.ordendeposito_id");
        sb.append(" join      VW_OTORGAMIENTOS o");
        sb.append(" ON        o.id = d.otorgamiento_id");
        sb.append(" join      ent_tipo_beca_periodo tbp");
        sb.append(" ON        tbp.id = o.tipobecaperiodo_id");
        sb.append(" join      cat_tipo_beca tb");
        sb.append(" ON        tb.id = tbp.tipobeca_id");
        sb.append(" join      cat_programa_beca pb");
        sb.append(" ON        pb.id = tb.beca_id");
        if (lvl || ua) {
            sb.append(" join      ent_proceso p");
            sb.append(" ON        p.id = o.proceso_id");
            sb.append(" left join cat_unidad_academica ua");
            sb.append(" ON        ua.id = p.unidadacademica_id");
        }

        criteria.add("od.periodo_id = #periodoId");
        params.put("periodoId", periodoId);
        criteria.add("pb.id NOT IN(10)");
        criteria.add("o.proceso_id IS NOT NULL");
        // Crea subquery para que obtenga únicamente el depósito más reciente
        sbSubQ.append(" (");
        sbSubQ.append(" SELECT     Max(dd.id)");
        sbSubQ.append(" FROM       ent_deposito dd");
        sbSubQ.append(" inner join ent_orden_deposito odd");
        sbSubQ.append(" ON         odd.id = dd.ordendeposito_id");
        sbSubQ.append(" WHERE      dd.otorgamiento_id = d.otorgamiento_id");
        sbSubQ.append(" AND        odd.mes =od.mes");
        sbSubQ.append(" )");
        // termina subquery
        criteria.add("d.id = " + sbSubQ.toString());
        if (ua) {
            criteria.add("ua.id = #unidadAcademicaId");
            params.put("unidadAcademicaId", unidadAcademicaId);
        }
        if (lvl) {
            criteria.add("ua.nivel_id = #nivelId");
            params.put("nivelId", nivelId);
        }

        if (pb) {
            criteria.add("pb.id = #programaBecaId");
            params.put("programaBecaId", programaBecaId);
        }

        agregaCriterios(sb, criteria);

        sb.append(" GROUP BY  ed.nombre,");
        sb.append(" od.mes");
        sb.append(" )");
        sb.append(" pivot ( ");
        sb.append(" SUM(total) FOR nombre IN ( 'En espera' en_espera,");
        sb.append(" 'Aplicado' aplicado,");
        sb.append(" 'Reembolso' reembolso,");
        sb.append(" 'Rechazado' rechazado,");
        sb.append(" 'Cancelado' cancelado,");
        sb.append(" 'Rechazo Trabajado' rechazo_trabajado,");
        sb.append(" 'Referencia expirada' referencia_expirada,");
        // Se ocultan id's 7, 8 y 9
        // sb.append(" 'No coincide boleta' no_coincide_boleta,");
        // sb.append(" 'No coincide tarjeta' no_coincide_tarjeta,");
        // sb.append(" 'No coincide monto' no_coincide_monto,");
        sb.append(" 'Depósito especial' deposito_especial )");
        sb.append(" )");
        sb.append(" ORDER BY mes ASC");

        List<Object[]> lista = executeNativeQuery(sb.toString(), params, null);
        return lista.isEmpty() ? null : lista;
    }

    //SQLINJECTION OK
    @Override
    public Long estadisticaDepositosTotalO(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal programaBecaId, boolean menosMeses) {
        boolean ua = unidadAcademicaId != null && !unidadAcademicaId.equals(BigDecimal.ZERO);
        boolean lvl = nivelId != null && !nivelId.equals(BigDecimal.ZERO);
        boolean pb = programaBecaId != null && !programaBecaId.equals(BigDecimal.ZERO);

        StringBuilder sb = new StringBuilder();
        List<String> criteria = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        sb.append(" select count(*) from VW_OTORGAMIENTOS o ");
        sb.append(" join ENT_TIPO_BECA_PERIODO tbp on tbp.id = o.TIPOBECAPERIODO_ID ");
        sb.append(" join CAT_TIPO_BECA tb on tb.id = tbp.TIPOBECA_ID ");
        sb.append(" join CAT_PROGRAMA_BECA pb on pb.id = tb.BECA_ID ");
        if (lvl || ua) {
            sb.append(" join      ent_proceso p");
            sb.append(" ON        p.id = o.proceso_id");
            sb.append(" left join cat_unidad_academica ua");
            sb.append(" ON        ua.id = p.unidadacademica_id");
        }

        criteria.add("o.periodo_id = #periodoId");
        params.put("periodoId", periodoId);
        criteria.add("pb.id not in(10)");
//        criteria.add("o.alta = 1");
        criteria.add("o.proceso_id IS NOT NULL");
        if (menosMeses) {
            criteria.add("tbp.DURACION < 6");
        }
        if (ua) {
            criteria.add("ua.id = #unidadAcademicaId");
            params.put("unidadAcademicaId", unidadAcademicaId);
        }
        if (lvl) {
            criteria.add("ua.nivel_id = #nivelId");
            params.put("nivelId", nivelId);
        }

        if (pb) {
            criteria.add("pb.id = #programaBecaId");
            params.put("programaBecaId", programaBecaId);
        }

        agregaCriterios(sb, criteria);

        return getCountNativeQueryMap(sb.toString(), params);
    }

    /*
     Se crea esta variable para controlar las funciones "estadisticaCuentasT" y "estadisticaCuentasTabla"
     para que los números de la gráfica y de la tabla cuadren
     */
    private final String subQuery = "left join VW_ULTIMA_TARJETA atb on atb.alumno_id = a.id\n"
            + "left join CAT_ESTATUS_TARJETA_BANCARIA et on et.id = atb.ESTATUSTARJBANC_ID\n"
            + "left join ENT_ALUMNO_DATOS_ACADEMICOS da on da.id = o.DATOSACADEMICOS_ID\n"
            + "left join CAT_UNIDAD_ACADEMICA ua on ua.id = da.UNIDADACADEMICA_ID\n"
            + "where  o.periodo_id = ?1\n"
            + "and o.alta = 1";

    //SQLINJECTION
    @Override
    public List<Object[]> estadisticaCuentasT(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId) {
        String sql = "select * from (  "
                + "	select 1 as fecha, "
                + "	case et.NOMBRE "
                + "	when 'Liberada' then 'Liberada'  "
                + "	when 'En trámite' then 'En trámite'  "
                + "	when 'Depósito por referencia' then 'Depósito por referencia'  "
                + "	when 'Corrección de datos' then 'Corrección de datos'  "
                + "	when 'Cuenta cancelada' then 'Cuenta cancelada'  "
                + "	when 'Referencia Cancelada' then 'Referencia Cancelada'  "
                + "	when 'Rechazada' then 'Rechazada'  "
                + " when 'Referencia cobrada' then 'Referencia cobrada'  "
                + "	else 'Sin Registro' end as nombre, "
                + "	count(distinct(a.id)) as total "
                + " from ent_otorgamientos o "
                + "join ent_alumno a on a.id = o.alumno_id "
                + subQuery;

        sql += unidadAcademicaId == null || unidadAcademicaId.equals(BigDecimal.ZERO) ? "" : " and ua.id = " + unidadAcademicaId + " ";
        sql += nivelId == null || nivelId.equals(BigDecimal.ZERO) ? "" : " and ua.nivel_id = " + nivelId + " ";
        sql += "group by et.NOMBRE) "
                + "PIVOT (  "
                + "	sum(total)  "
                + "	FOR nombre in (  "
                + "		'Liberada' Liberada,  "
                + "		'En trámite' En_tramite,  "
                + "		'Depósito por referencia' Deposito_por_referencia,  "
                + "		'Corrección de datos' Correccion_de_datos,  "
                + "		'Cuenta cancelada' Cuenta_cancelada,  "
                + "		'Referencia Cancelada' Referencia_Cancelada,  "
                + "		'Rechazada' Rechazada , "
                + "		'Referencia cobrada' Referencia_cobrada, "
                + "		'Sin Registro' sin_reg))";
        List<Object[]> lista = executeNativeQuery(sql, periodoId);
        return lista.isEmpty() ? null : lista;
    }

    //SQLINJECTION
    @Override
    public List<Object[]> estadisticaCuentas(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId) {
        String sql = "select * from (  "
                + "	select 1 as fecha, "
                + "	case et.NOMBRE "
                + "	when 'Liberada' then 'Liberada'  "
                + "	when 'En trámite' then 'En trámite'  "
                + "	when 'Depósito por referencia' then 'Depósito por referencia'  "
                + "	when 'Corrección de datos' then 'Corrección de datos'  "
                + "	when 'Cuenta cancelada' then 'Cuenta cancelada'  "
                + "	when 'Referencia Cancelada' then 'Referencia Cancelada'  "
                + "	when 'Rechazada' then 'Rechazada'  "
                + " when 'Referencia cobrada' then 'Referencia cobrada'  "
                + "	else 'Sin Registro' end as nombre, "
                + "	count(distinct(a.id)) as total from ent_otorgamientos o  "
                + "	  join ent_alumno a on a.id = o.alumno_id  "
                + "	left join RMM_ALUMNO_TARJETA_BANCARIA atb on atb.alumno_id = a.id   "
                + "	left join CAT_ESTATUS_TARJETA_BANCARIA et on et.id = atb.ESTATUSTARJBANC_ID  "
                + "	left join ENT_ALUMNO_DATOS_ACADEMICOS da on da.id = o.DATOSACADEMICOS_ID  "
                + "	left join CAT_UNIDAD_ACADEMICA ua on ua.id = da.UNIDADACADEMICA_ID   "
                + "	where  (atb.id = (  "
                + "		select max(id) from RMM_ALUMNO_TARJETA_BANCARIA where alumno_id = a.id  "
                + "		) or atb.id is null)  "
                + "	and o.periodo_id = ?1  "
                + "	and o.alta = 1";

        sql += unidadAcademicaId == null || unidadAcademicaId.equals(BigDecimal.ZERO) ? "" : " and ua.id = " + unidadAcademicaId + " ";
        sql += nivelId == null || nivelId.equals(BigDecimal.ZERO) ? "" : " and ua.nivel_id = " + nivelId + " ";
        sql += "group by et.NOMBRE) "
                + "PIVOT (  "
                + "	sum(total)  "
                + "	FOR nombre in (  "
                + "		'Liberada' Liberada,  "
                + "		'En trámite' En_tramite,  "
                + "		'Depósito por referencia' Deposito_por_referencia,  "
                + "		'Corrección de datos' Correccion_de_datos,  "
                + "		'Cuenta cancelada' Cuenta_cancelada,  "
                + "		'Referencia Cancelada' Referencia_Cancelada,  "
                + "		'Rechazada' Rechazada , "
                + "		'Referencia cobrada' Referencia_cobrada, "
                + "		'Sin Registro' sin_reg))";
        List<Object[]> lista = executeNativeQuery(sql, periodoId);
        return lista.isEmpty() ? null : lista;
    }

    //SQLINJECTION
    @Override
    public PaginateUtil estadisticaCuentasTabla(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal estatusId, ServerSideUtil ssu) {
        String sql = "select a.* "
                + "from ent_alumno a "
                + "  join ent_otorgamientos o on a.id = o.alumno_id and o.id = (select ox.id from ENT_OTORGAMIENTOS ox where ox.alumno_id = a.id and ox.periodo_id = o.periodo_id and ox.alta=1 and rownum=1) "
                + subQuery;

        sql += estatusId == null || estatusId.equals(BigDecimal.ZERO) ? " and et.id is null " : " and et.id = " + estatusId + " ";
        sql += unidadAcademicaId == null || unidadAcademicaId.equals(BigDecimal.ZERO) ? "" : " and ua.id = " + unidadAcademicaId + " ";
        sql += nivelId == null || nivelId.equals(BigDecimal.ZERO) ? "" : " and ua.nivel_id = " + nivelId + " ";
        sql += " order by et.nombre, ua.id, a.boleta";
        Object[] params = {periodoId};
        Long noTotal = getCountNativeQuery(buildCountQuery(sql, Boolean.TRUE), params);
        List<Object[]> lista = executeNativeQueryPagClass(sql, ssu.getStart(), ssu.getLength(), Alumno.class, params);
        return new PaginateUtil(lista, noTotal, noTotal);
    }

    //Consulta para generar archivo de excel
    //Augusto Hernández
    @Override
    public List<Object[]> reporteEstatus(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal estatusId) {
        StringBuilder sb = new StringBuilder();
        List<String> columnas = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        List<String> criteria = new ArrayList<>();
        int k = -1;
        BigDecimal bD = new BigDecimal(k);

        columnas.add("a.boleta \"BOLETA\"");
        columnas.add("a.nombre \"NOMBRE\"");
        columnas.add("a.apellidopaterno \"APELLIDO PATERNO\"");
        columnas.add("a.apellidomaterno \"APELLIDO MATERNO\"");
        columnas.add("ua.nombrecorto \"UNIDAD ACADÉMICA\"");
        columnas.add("NVL(et.nombre, 'Sin cuenta') \"ESTATUS\"");

        sb.append(" SELECT ");
        agregaColumnas(sb, columnas);

        sb.append(" from ent_alumno a ");
        sb.append(" join ent_otorgamientos o on a.id = o.alumno_id and o.id = (select ox.id from ENT_OTORGAMIENTOS ox where ox.alumno_id = a.id and ox.periodo_id = o.periodo_id and ox.alta=1 and rownum=1) ");
        sb.append(" left join VW_ULTIMA_TARJETA atb on atb.alumno_id = a.id ");
        sb.append(" left join CAT_ESTATUS_TARJETA_BANCARIA et on et.id = atb.ESTATUSTARJBANC_ID ");
        sb.append(" left join ENT_ALUMNO_DATOS_ACADEMICOS da on da.id = o.DATOSACADEMICOS_ID ");
        sb.append(" left join CAT_UNIDAD_ACADEMICA ua on ua.id = da.UNIDADACADEMICA_ID ");

        criteria.add("o.periodo_id = #periodo_id");
        params.put("periodo_id", periodoId);
        criteria.add("o.alta = 1");

        if (nivelId != null) {
            criteria.add("ua.nivel_id = #nivel_id");
            params.put("nivel_id", nivelId);
        }
        if (unidadAcademicaId != null) {
            criteria.add("ua.id = #ua_id");
            params.put("ua_id", unidadAcademicaId);
        }
        if (estatusId == null || estatusId.equals(BigDecimal.ZERO)) {
            criteria.add("et.id is null");
        } else if (estatusId.equals(bD)) {
            System.out.println("Nada");
        } else {
            criteria.add("et.id = #et_id");
            params.put("et_id", estatusId);
        }

        agregaCriterios(sb, criteria);
        sb.append(" order by et.nombre, ua.id, a.boleta");
        List<Object[]> result = executeNativeQuery(sb.toString(), params, null);

        return result;
    }

//SQLINJECTION OK
    @Override
    public List<Object[]> estadisticaRendimientoBajas(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal becaId) {
        StringBuilder sb = new StringBuilder();
        List<String> criteria = new ArrayList<>();
        Map<String, Object> params = new HashMap();

        sb.append(" select * from ( ");
        sb.append(" select trunc(obd.fechabaja) as fecha, g.clave as clave,count(obd.id) as total ");
        sb.append(" from VW_ULTIMA_BAJA_DETALLE obd ");
        sb.append("   JOIN ENT_OTORGAMIENTOS o on o.id = obd.OTORGAMIENTO_ID ");
        sb.append("   join ent_alumno a on a.id = o.alumno_id ");
        sb.append("   join cat_genero g on g.id = a.genero_id ");
        sb.append("   join ENT_PROCESO p on p.id = obd.proceso_id ");
        sb.append("   join CAT_TIPO_PROCESO tp on tp.id = p.TIPOPROCESO_ID ");
        sb.append("   join CAT_MOVIMIENTO m on m.id = tp.MOVIMIENTO_ID ");
        sb.append("   JOIN CAT_UNIDAD_ACADEMICA ua on ua.id = p.unidadacademica_id ");
        sb.append("   join ENT_TIPO_BECA_PERIODO tbp on tbp.id = o.tipobecaperiodo_id ");
        sb.append("   join CAT_TIPO_BECA tb on tb.id = tbp.TIPOBECA_ID ");
        sb.append("   join CAT_PROGRAMA_BECA b on b.id = tb.beca_id ");

        criteria.add("obd.periodo_id = #periodoId");
        params.put("periodoId", periodoId);
        criteria.add("o.ALTA = 0 ");

        if (unidadAcademicaId != null && unidadAcademicaId.intValue() != 0) {
            criteria.add("p.unidadAcademica_id = #unidadAcademicaId");
            params.put("unidadAcademicaId", unidadAcademicaId);
        } else if (nivelId != null && nivelId.intValue() != 0) {
            criteria.add("ua.nivel_id = #nivelId");
            params.put("nivelId", nivelId);
        }
        if (becaId != null && becaId.intValue() != 0) {
            criteria.add("b.id = #becaId");
            params.put("becaId", becaId);
        }

        agregaCriterios(sb, criteria);

        sb.append(" group by trunc(obd.fechabaja),g.clave) ");
        sb.append(" PIVOT ( ");
        sb.append(" sum(total) ");
        sb.append(" FOR clave in ('M' M,'F' F)");
        sb.append(" )");
        sb.append("order by fecha asc");

        List<Object[]> lista = executeNativeQuery(sb.toString(), params, null);
        return lista.isEmpty() ? null : lista;
    }

    //SQLINJECTION OK
    @Override
    public List<Object[]> estadisticaBajas(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal becaId, BigDecimal movimiento) {
        StringBuilder sb = new StringBuilder();
        List<String> criteria = new ArrayList<>();
        Map<String, Object> params = new HashMap();

        sb.append("select * from ( ");
        sb.append("  select g.clave as clave,count(obd.id) as total ");
        sb.append("  from VW_ULTIMA_BAJA_DETALLE obd ");
        sb.append("    JOIN ENT_OTORGAMIENTOS o on o.id = obd.OTORGAMIENTO_ID");
        sb.append("    join ent_alumno a on a.id = o.alumno_id ");
        sb.append("    join cat_genero g on g.id = a.genero_id");
        sb.append("    join ENT_PROCESO p on p.id = obd.proceso_id ");
        sb.append("    join CAT_TIPO_PROCESO tp on tp.id = p.TIPOPROCESO_ID ");
        sb.append("    join CAT_MOVIMIENTO m on m.id = tp.MOVIMIENTO_ID ");
        sb.append("    JOIN CAT_UNIDAD_ACADEMICA ua on ua.id = p.unidadacademica_id ");
        sb.append("    join ENT_TIPO_BECA_PERIODO tbp on tbp.id = o.tipobecaperiodo_id");
        sb.append("    join CAT_TIPO_BECA tb on tb.id = tbp.TIPOBECA_ID ");
        sb.append("    join CAT_PROGRAMA_BECA b on b.id = tb.beca_id ");

        criteria.add("obd.periodo_id = #periodoId");
        params.put("periodoId", periodoId);
        criteria.add("o.ALTA = 0");

        if (unidadAcademicaId != null && unidadAcademicaId.intValue() != 0) {
            criteria.add("p.unidadAcademica_id = #unidadAcademicaId");
            params.put("unidadAcademicaId", unidadAcademicaId);
        } else if (nivelId != null && nivelId.intValue() != 0) {
            criteria.add("ua.nivel_id  = #nivelId");
            params.put("nivelId", nivelId);
        }
        if (becaId != null && becaId.intValue() != 0) {
            criteria.add("b.id = #becaId");
            params.put("becaId", becaId);
        }
        if (movimiento != null && movimiento.intValue() != 0) {
            criteria.add("m.id = #movimiento");
            params.put("m.id", movimiento);
        }

        agregaCriterios(sb, criteria);

        sb.append("  group by g.clave) ");
        sb.append(" PIVOT ( ");
        sb.append("  sum(total) FOR clave in ('M' M,'F' F)");
        sb.append(" ) ");

        List<Object[]> lista = executeNativeQuery(sb.toString(), params, null);
        return lista.isEmpty() ? null : lista;
    }

    //SQLINJECTION OK
    @Override
    public List<Object[]> estadisticaProgramaBeca(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal becaId, BigDecimal movimiento) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> params = new HashMap();
        List<String> criteria = new ArrayList<>();

        sb.append(" select * from (");
        sb.append(" select b.nombre as titulo, g.clave as clave, count(*) as total");
        sb.append(" from vw_otorgamientos o ");
        sb.append("   join ent_alumno a on a.id = o.alumno_id ");
        sb.append("   join cat_genero g on g.id = a.genero_id ");
        sb.append("   join ENT_PROCESO p on p.id = o.proceso_id ");
        sb.append("   join CAT_TIPO_PROCESO tp on tp.id = p.TIPOPROCESO_ID ");
        sb.append("   join CAT_MOVIMIENTO m on m.id = tp.MOVIMIENTO_ID ");
        sb.append("   JOIN CAT_UNIDAD_ACADEMICA ua on ua.id = p.unidadacademica_id ");
        sb.append("   join ENT_TIPO_BECA_PERIODO tbp on tbp.id = o.tipobecaperiodo_id ");
        sb.append("   join CAT_TIPO_BECA tb on tb.id = tbp.TIPOBECA_ID ");
        sb.append("   join CAT_PROGRAMA_BECA b on b.id = tb.beca_id ");

        criteria.add("o.periodo_id = #periodoId");
        params.put("periodoId", periodoId);

        if (unidadAcademicaId != null && unidadAcademicaId.intValue() != 0) {
            criteria.add("p.unidadAcademica_id = #unidadAcademicaId");
            params.put("unidadAcademicaId", unidadAcademicaId);
        } else if (nivelId != null && nivelId.intValue() != 0) {
            criteria.add("ua.nivel_id = #nivelId");
            params.put("nivelId", nivelId);
        }
        if (becaId != null && becaId.intValue() != 0) {
            criteria.add("b.id = #becaId");
            params.put("becaId", becaId);
        }
        if (movimiento != null && movimiento.intValue() != 0) {
            criteria.add("m.id = #movimiento");
            params.put("movimiento", movimiento);
        }

        agregaCriterios(sb, criteria);

        sb.append(" group by b.nombre,g.clave) ");
        sb.append(" PIVOT ( ");
        sb.append(" sum(total) ");
        sb.append(" FOR clave in ('M' M,'F' F)");
        sb.append(" ) ");
        sb.append(" order by titulo asc");

        List<Object[]> lista = executeNativeQuery(sb.toString(), params, null);
        return lista.isEmpty() ? null : lista;
    }

    //SQLINJECTION OK
    @Override
    public List<Object[]> estadisticaProgramaBecaB(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal becaId, BigDecimal movimiento) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> params = new HashMap();
        List<String> criteria = new ArrayList<>();

        sb.append(" select * from ( ");
        sb.append(" select b.nombre as titulo, g.clave as clave, count(*) as total");
        sb.append(" from VW_ULTIMA_BAJA_DETALLE obd ");
        sb.append("   JOIN ENT_OTORGAMIENTOS o on o.id = obd.OTORGAMIENTO_ID ");
        sb.append("   join ent_alumno a on a.id = o.alumno_id ");
        sb.append("   join cat_genero g on g.id = a.genero_id ");
        sb.append("   join ENT_PROCESO p on p.id = obd.proceso_id ");
        sb.append("   join CAT_TIPO_PROCESO tp on tp.id = p.TIPOPROCESO_ID ");
        sb.append("   join CAT_MOVIMIENTO m on m.id = tp.MOVIMIENTO_ID ");
        sb.append("   JOIN CAT_UNIDAD_ACADEMICA ua on ua.id = p.unidadacademica_id ");
        sb.append("   join ENT_TIPO_BECA_PERIODO tbp on tbp.id = o.tipobecaperiodo_id ");
        sb.append("   join CAT_TIPO_BECA tb on tb.id = tbp.TIPOBECA_ID ");
        sb.append("   join CAT_PROGRAMA_BECA b on b.id = tb.beca_id ");

        criteria.add("obd.periodo_id = #periodoId");
        params.put("periodoId", periodoId);
        criteria.add("o.alta = 0 ");
        criteria.add("o.proceso_id IS NOT NULL");
        criteria.add("m.TIPOMOVIMIENTO_ID = 2");

        if (unidadAcademicaId != null && unidadAcademicaId.intValue() != 0) {
            criteria.add("p.unidadAcademica_id = #unidadAcademicaId");
            params.put("unidadAcademicaId", unidadAcademicaId);
        } else if (nivelId != null && nivelId.intValue() != 0) {
            criteria.add("ua.nivel_id = #nivelId");
            params.put("nivelId", nivelId);
        }
        if (becaId != null && becaId.intValue() != 0) {
            criteria.add("b.id = #becaId");
            params.put("becaId", becaId);
        }
        if (movimiento != null && movimiento.intValue() != 0) {
            criteria.add("m.id = #movimiento");
            params.put("movimiento", movimiento);
        }
        agregaCriterios(sb, criteria);

        sb.append(" group by b.nombre,g.clave) ");
        sb.append(" PIVOT ( ");
        sb.append(" sum(total) ");
        sb.append(" FOR clave in ('M' M,'F' F) ");
        sb.append(" )");
        sb.append(" order by titulo asc");

        List<Object[]> lista = executeNativeQuery(sb.toString(), params, null);
        return lista.isEmpty() ? null : lista;
    }

    //SQLINJECTION OK
    // Estadística: Bajas por Semestre
    @Override
    public List<Object[]> estadisticaSemestreBecaB(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal becaId, BigDecimal movimiento, Object parametro) {
        String name = parametro.toString();
        if (name.contains("IN")) {
            name = name.replaceAll("IN\\(", "").replaceAll(",", "° y ").replaceAll("\\)", "°");
        } else if (name.contains(">=")) {
            name = name.replaceAll(">=", "").concat("°");
        }

        StringBuilder sb = new StringBuilder();
        List<String> criteria = new ArrayList<>();
        Map<String, Object> params = new HashMap();

        sb.append(" select * from ( ");
        sb.append(" select '");
        sb.append(name);
        sb.append("' as titulo, g.clave as clave, count(*) as total");
        sb.append(" from VW_ULTIMA_BAJA_DETALLE obd ");

        sb.append("   JOIN ENT_OTORGAMIENTOS o on o.id = obd.OTORGAMIENTO_ID ");
        sb.append("   join ent_alumno a on a.id = o.alumno_id ");
        sb.append("   join ENT_ALUMNO_DATOS_ACADEMICOS da on da.id = o.DATOSACADEMICOS_ID ");
        sb.append("   join cat_genero g on g.id = a.genero_id ");
        sb.append("   join ENT_PROCESO p on p.id = obd.proceso_id ");
        sb.append("   join CAT_TIPO_PROCESO tp on tp.id = p.TIPOPROCESO_ID ");
        sb.append("   join CAT_MOVIMIENTO m on m.id = tp.MOVIMIENTO_ID ");
        sb.append("   JOIN CAT_UNIDAD_ACADEMICA ua on ua.id = p.unidadacademica_id ");
        sb.append("   join ENT_TIPO_BECA_PERIODO tbp on tbp.id = o.tipobecaperiodo_id ");
        sb.append("   join CAT_TIPO_BECA tb on tb.id = tbp.TIPOBECA_ID ");
        sb.append("   join CAT_PROGRAMA_BECA b on b.id = tb.beca_id ");

        criteria.add("obd.periodo_id = #periodoId");
        params.put("periodoId", periodoId);
        criteria.add("o.alta = 0");
        criteria.add("o.proceso_id IS NOT NULL");
        criteria.add("m.TIPOMOVIMIENTO_ID = 2");
        criteria.add("da.SEMESTRE " + parametro.toString());

        if (unidadAcademicaId != null && unidadAcademicaId.intValue() != 0) {
            criteria.add("p.unidadAcademica_id = #unidadAcademicaId");
            params.put("unidadAcademicaId", unidadAcademicaId);
        } else if (nivelId != null && nivelId.intValue() != 0) {
            criteria.add("ua.nivel_id = #nivelId");
            params.put("nivelId", nivelId);
        }
        if (becaId != null && becaId.intValue() != 0) {
            criteria.add("b.id = #becaId");
            params.put("becaId", becaId);
        }
        if (movimiento != null && movimiento.intValue() != 0) {
            criteria.add("m.id = #movimiento");
            params.put("movimiento", movimiento);
        }

        agregaCriterios(sb, criteria);

        sb.append(" group by g.clave) ");
        sb.append(" PIVOT ( ");
        sb.append(" sum(total) ");
        sb.append(" FOR clave in ('M' M,'F' F)");
        sb.append(")");

        List<Object[]> lista = executeNativeQuery(sb.toString(), params, null);
        return lista.isEmpty() ? null : lista;
    }

    // SQLINJECTION OK
    // Estadística: Bajas según promedio
    @Override
    public List<Object[]> estadisticaPromedioBecaB(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal becaId, BigDecimal movimiento, Object parametro) {
        String name = parametro.toString();
        if (name.contains("BETWEEN")) {
            name = name.replaceAll("BETWEEN", "ENTRE").replaceAll("AND", "Y");
        }

        StringBuilder sb = new StringBuilder();
        List<String> criteria = new ArrayList<>();
        Map<String, Object> params = new HashMap();

        sb.append(" select * from ( ");
        sb.append(" select '");
        sb.append(name);
        sb.append("' as titulo, g.clave as clave, count(*) as total");
        sb.append(" from VW_ULTIMA_BAJA_DETALLE obd ");
        sb.append("   JOIN ENT_OTORGAMIENTOS o on o.id = obd.OTORGAMIENTO_ID ");
        sb.append("   join ent_alumno a on a.id = o.alumno_id ");
        sb.append("   join ENT_ALUMNO_DATOS_ACADEMICOS da on da.id = o.DATOSACADEMICOS_ID ");
        sb.append("   join cat_genero g on g.id = a.genero_id ");
        sb.append("   join ENT_PROCESO p on p.id = obd.proceso_id ");
        sb.append("   join CAT_TIPO_PROCESO tp on tp.id = p.TIPOPROCESO_ID ");
        sb.append("   join CAT_MOVIMIENTO m on m.id = tp.MOVIMIENTO_ID ");
        sb.append("   JOIN CAT_UNIDAD_ACADEMICA ua on ua.id = p.unidadacademica_id ");
        sb.append("   join ENT_TIPO_BECA_PERIODO tbp on tbp.id = o.tipobecaperiodo_id ");
        sb.append("   join CAT_TIPO_BECA tb on tb.id = tbp.TIPOBECA_ID ");
        sb.append("   join CAT_PROGRAMA_BECA b on b.id = tb.beca_id ");

        criteria.add("obd.periodo_id = #periodoId");
        params.put("periodoId", periodoId);
        criteria.add("o.alta = 0");
        criteria.add("o.proceso_id IS NOT NULL");
        criteria.add("m.TIPOMOVIMIENTO_ID = 2");
        criteria.add("da.PROMEDIO " + parametro.toString());

        if (unidadAcademicaId != null && unidadAcademicaId.intValue() != 0) {
            criteria.add("p.unidadAcademica_id = #unidadAcademicaId");
            params.put("unidadAcademicaId", unidadAcademicaId);
        } else if (nivelId != null && nivelId.intValue() != 0) {
            criteria.add("ua.nivel_id = #nivelId");
            params.put("nivelId", nivelId);
        }
        if (becaId != null && becaId.intValue() != 0) {
            criteria.add("b.id = #becaId");
            params.put("becaId", becaId);
        }
        if (movimiento != null && movimiento.intValue() != 0) {
            criteria.add("m.id = #movimiento");
            params.put("movimiento", movimiento);
        }

        agregaCriterios(sb, criteria);

        sb.append(" group by g.clave) ");
        sb.append(" PIVOT ( ");
        sb.append(" sum(total) ");
        sb.append(" FOR clave in ('M' M,'F' F) ");
        sb.append(" )");

        List<Object[]> lista = executeNativeQuery(sb.toString(), params, null);
        return lista.isEmpty() ? null : lista;
    }

    //SQLINJECTION OK
    @Override
    public List<Object[]> estadisticaMovimientoAltas(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal becaId, BigDecimal movimiento) {
        StringBuilder sb = new StringBuilder();
        List<String> criteria = new ArrayList<>();
        Map<String, Object> params = new HashMap();

        sb.append("select * from ( ");
        sb.append("  select m.nombre as titulo, g.clave as clave, count(*) as total");
        sb.append("  from VW_OTORGAMIENTOS o ");
        sb.append("    join ent_alumno a on a.id = o.alumno_id ");
        sb.append("    join cat_genero g on g.id = a.genero_id ");
        sb.append("    join ENT_PROCESO p on p.id = o.proceso_id ");
        sb.append("    join CAT_TIPO_PROCESO tp on tp.id = p.TIPOPROCESO_ID ");
        sb.append("    join CAT_MOVIMIENTO m on m.id = tp.MOVIMIENTO_ID ");
        sb.append("    JOIN CAT_UNIDAD_ACADEMICA ua on ua.id = p.unidadacademica_id ");
        sb.append("    join ENT_TIPO_BECA_PERIODO tbp on tbp.id = o.TIPOBECAPERIODO_ID ");
        sb.append("    join CAT_TIPO_BECA tb on tb.id = tbp.TIPOBECA_ID ");
        sb.append("    join CAT_PROGRAMA_BECA b on b.id = tb.beca_id ");

        criteria.add("o.periodo_id = #periodoId");
        params.put("periodoId", periodoId);
        criteria.add("m.TIPOMOVIMIENTO_ID = 1");

        if (unidadAcademicaId != null && unidadAcademicaId.intValue() != 0) {
            criteria.add("p.unidadAcademica_id = #unidadAcademicaId");
            params.put("unidadAcademicaId", unidadAcademicaId);
        } else if (nivelId != null && nivelId.intValue() != 0) {
            criteria.add("ua.nivel_id = #nivelId");
            params.put("nivelId", nivelId);
        }
        if (becaId != null && becaId.intValue() != 0) {
            criteria.add("b.id = #becaId");
            params.put("becaId", becaId);
        }
        if (movimiento != null && movimiento.intValue() != 0) {
            criteria.add("m.id = #movimiento");
            params.put("movimiento", movimiento);
        }

        agregaCriterios(sb, criteria);

        sb.append("  group by m.nombre,g.clave) ");
        sb.append(" PIVOT ( ");
        sb.append("  sum(total) FOR clave in ('M' M,'F' F)");
        sb.append(" ) order by titulo asc");

        List<Object[]> lista = executeNativeQuery(sb.toString(), params, null);
        return lista.isEmpty() ? null : lista;
    }

    //SQLINJECTION
    @Override
    public List<Object[]> estadisticaMovimientoBajas(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal becaId, BigDecimal movimiento) {
        String sql = "select * from ( ";
        if (unidadAcademicaId.intValue() != 0 && movimiento.intValue() != 0) {
            sql += "select tp.nombre as titulo, g.clave as clave, count(*) as total from VW_ULTIMA_BAJA_DETALLE obd ";
        } else {
            sql += "select m.nombre as titulo, g.clave as clave, count(*) as total from VW_ULTIMA_BAJA_DETALLE obd ";
        }
        sql += "  JOIN ENT_OTORGAMIENTOS o on o.id = obd.OTORGAMIENTO_ID "
                + "  join ent_alumno a on a.id = o.alumno_id "
                + "  join cat_genero g on g.id = a.genero_id "
                + "  join ENT_PROCESO p on p.id = obd.proceso_id "
                + "  join CAT_TIPO_PROCESO tp on tp.id = p.TIPOPROCESO_ID "
                + "  join CAT_MOVIMIENTO m on m.id = tp.MOVIMIENTO_ID "
                + "  JOIN CAT_UNIDAD_ACADEMICA ua on ua.id = p.unidadacademica_id "
                + "  join ENT_TIPO_BECA_PERIODO tbp on tbp.id = o.TIPOBECAPERIODO_ID "
                + "  join CAT_TIPO_BECA tb on tb.id = tbp.TIPOBECA_ID "
                + "  join CAT_PROGRAMA_BECA b on b.id = tb.beca_id "
                + "where obd.periodo_id = ?1 "
                + "and o.ALTA = 0 "
                + "AND o.proceso_id IS NOT NULL "
                + "and m.TIPOMOVIMIENTO_ID = 2 ";

        if (unidadAcademicaId != null && unidadAcademicaId.intValue() != 0) {
            sql += "AND p.unidadAcademica_id = " + unidadAcademicaId + " ";
        } else if (nivelId != null && nivelId.intValue() != 0) {
            sql += "AND ua.nivel_id = " + nivelId + " ";
        }
        if (becaId != null && becaId.intValue() != 0) {
            sql += "AND b.id = " + becaId + " ";
        }
        if (movimiento != null && movimiento.intValue() != 0) {
            sql += "AND m.id = " + movimiento + " ";
        }

        if (unidadAcademicaId.intValue() != 0 && movimiento.intValue() != 0) {
            sql += "group by tp.nombre,g.clave) ";
        } else {
            sql += "group by m.nombre,g.clave) ";
        }
        sql += "PIVOT ( "
                + "sum(total) "
                + "FOR clave in ('M' M,'F' F)) "
                + "order by titulo asc";

        List<Object[]> lista = executeNativeQuery(sql, periodoId);
        return lista.isEmpty() ? null : lista;
    }

    //SQLINJECTION OK
    @Override
    public List<Object[]> estadisticaCarreras(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal becaId, BigDecimal movimiento) {
        StringBuilder sb = new StringBuilder();
        List<String> criteria = new ArrayList<>();
        Map<String, Object> params = new HashMap();

        sb.append("select * from ( ");
        sb.append("  select cr.CARRERA as titulo, g.clave as clave, count(*) as total");
        sb.append("  from VW_OTORGAMIENTOS o ");
        sb.append("    join ent_alumno a on a.id = o.alumno_id ");
        sb.append("    join ENT_ALUMNO_DATOS_ACADEMICOS da on da.id = o.DATOSACADEMICOS_ID ");
        sb.append("    join cat_genero g on g.id = a.genero_id ");
        sb.append("    join ENT_PROCESO p on p.id = o.proceso_id ");
        sb.append("    join CAT_TIPO_PROCESO tp on tp.id = p.TIPOPROCESO_ID ");
        sb.append("    join CAT_MOVIMIENTO m on m.id = tp.MOVIMIENTO_ID ");
        sb.append("    JOIN CAT_UNIDAD_ACADEMICA ua on ua.id = p.unidadacademica_id ");
        sb.append("    JOIN ENT_CARRERA cr on cr.id = da.CARRERA_ID ");
        criteria.add("o.periodo_id = #periodoId");
        params.put("periodoId", periodoId);
        criteria.add("p.unidadAcademica_id = #unidadAcademicaId");
        params.put("unidadAcademicaId", unidadAcademicaId);
        criteria.add("cr.UNIDADACADEMICA_ID = #unidadAcademicaId");

        if (becaId != null && becaId.intValue() != 0) {
            criteria.add("b.id = #becaId");
            params.put("becaId", becaId);
        }
        if (movimiento != null && movimiento.intValue() != 0) {
            criteria.add("m.id = #movimiento");
            params.put("movimiento", movimiento);
        }

        agregaCriterios(sb, criteria);

        sb.append("  group by cr.CARRERA,g.clave) ");
        sb.append(" PIVOT ( ");
        sb.append("  sum(total) ");
        sb.append("  FOR clave in ('M' M,'F' F)");
        sb.append(" )");
        sb.append(" order by titulo asc");

        List<Object[]> lista = executeNativeQuery(sb.toString(), params, null);

        return lista.isEmpty() ? null : lista;
    }

    //SQLINJECTION OK
    @Override
    public List<Object[]> estadisticaCarrerasB(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal becaId, BigDecimal movimiento) {
        StringBuilder sb = new StringBuilder();
        List<String> criteria = new ArrayList<>();
        Map<String, Object> params = new HashMap();

        sb.append("select * from ( ");
        sb.append(" select nvl(cr.CARRERA,'Sin información de carrera') as titulo, g.clave as clave, count(*) as total ");
        sb.append(" from VW_ULTIMA_BAJA_DETALLE obd ");
        sb.append(" JOIN ENT_OTORGAMIENTOS o on o.id = obd.OTORGAMIENTO_ID ");
        sb.append(" join ent_alumno a on a.id = o.alumno_id ");
        sb.append(" left join cat_genero g on g.id = a.genero_id ");
        sb.append(" join ENT_PROCESO p on p.id = obd.proceso_id ");
        sb.append(" left join ENT_ALUMNO_DATOS_ACADEMICOS da on da.periodo_id = p.PERIODO_ID  and da.alumno_id = a.id ");
        sb.append(" join CAT_TIPO_PROCESO tp on tp.id = p.TIPOPROCESO_ID ");
        sb.append(" join CAT_MOVIMIENTO m on m.id = tp.MOVIMIENTO_ID ");
        sb.append(" JOIN CAT_UNIDAD_ACADEMICA ua on ua.id = p.unidadacademica_id ");
        sb.append(" left JOIN ENT_CARRERA cr on cr.id = da.CARRERA_ID and cr.UNIDADACADEMICA_ID = ua.id ");

        criteria.add("obd.periodo_id = #periodoId");
        params.put("periodoId", periodoId);
        criteria.add("p.unidadAcademica_id = #unidadAcademicaId");
        params.put("unidadAcademicaId", unidadAcademicaId);
        criteria.add("o.alta = 0");
        criteria.add("o.proceso_id IS NOT NULL");
        criteria.add("m.TIPOMOVIMIENTO_ID = 2");

        if (becaId != null && becaId.intValue() != 0) {
            criteria.add("b.id = #becaId");
            params.put("becaId", becaId);
        }
        if (movimiento != null && movimiento.intValue() != 0) {
            criteria.add("m.id = #movimiento");
            params.put("movimiento", movimiento);
        }

        agregaCriterios(sb, criteria);

        sb.append(" group by cr.CARRERA,g.clave) ");
        sb.append(" PIVOT ( ");
        sb.append(" sum(total) ");
        sb.append(" FOR clave in ('M' M,'F' F)");
        sb.append(" )");
        sb.append(" order by titulo asc");

        List<Object[]> lista = executeNativeQuery(sb.toString(), params, null);

        return lista.isEmpty() ? null : lista;
    }

    //SQLINJECTION
    @Override
    public List<Object[]> estadisticaTipoBeca(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal becaId, BigDecimal movimiento) {
        String sql = "select * from ( "
                + "select tb.nombre as titulo, g.clave as clave, count(*) as total from VW_OTORGAMIENTOS o "
                + "  join ent_alumno a on a.id = o.alumno_id "
                + "  join cat_genero g on g.id = a.genero_id "
                + "  join ENT_PROCESO p on p.id = o.proceso_id "
                + "  join CAT_TIPO_PROCESO tp on tp.id = p.TIPOPROCESO_ID "
                + "  join CAT_MOVIMIENTO m on m.id = tp.MOVIMIENTO_ID "
                + "  JOIN CAT_UNIDAD_ACADEMICA ua on ua.id = p.unidadacademica_id "
                + "  join ENT_TIPO_BECA_PERIODO tbp on tbp.id = o.tipobecaperiodo_id "
                + "  join CAT_TIPO_BECA tb on tb.id = tbp.TIPOBECA_ID "
                + "  join CAT_PROGRAMA_BECA b on b.id = tb.beca_id "
                + "where o.periodo_id = ?1 "
                + "AND b.id = ?2 "
                + "and alta = 1 "
                + "AND o.proceso_id IS NOT NULL " //		+ "and o.identificadorOtorgamiento_id NOT IN ( 7, 13 ) "
                ;

        if (unidadAcademicaId != null && unidadAcademicaId.intValue() != 0) {
            sql += "AND p.unidadAcademica_id = " + unidadAcademicaId + " ";
        } else if (nivelId != null && nivelId.intValue() != 0) {
            sql += "AND ua.nivel_id = " + nivelId + " ";
        }
        if (movimiento != null && movimiento.intValue() != 0) {
            sql += "AND m.id = " + movimiento + " ";
        }

        sql += "group by tb.nombre,g.clave) "
                + "PIVOT ( "
                + "sum(total) "
                + "FOR clave in ('M' M,'F' F)) "
                + "order by titulo asc";

        List<Object[]> lista = executeNativeQuery(sql, periodoId, becaId);
        return lista.isEmpty() ? null : lista;
    }

    //SQLINJECTION
    @Override
    public List<Object[]> estadisticaTipoBecaB(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal becaId, BigDecimal movimiento) {
        String sql = "select * from ( "
                + "select tb.nombre as titulo, g.clave as clave, count(*) as total from VW_ULTIMA_BAJA_DETALLE obd "
                + "  JOIN ENT_OTORGAMIENTOS o on o.id = obd.OTORGAMIENTO_ID "
                + "  join ent_alumno a on a.id = o.alumno_id "
                + "  join cat_genero g on g.id = a.genero_id "
                + "  join ENT_PROCESO p on p.id = obd.proceso_id "
                + "  join CAT_TIPO_PROCESO tp on tp.id = p.TIPOPROCESO_ID "
                + "  join CAT_MOVIMIENTO m on m.id = tp.MOVIMIENTO_ID "
                + "  JOIN CAT_UNIDAD_ACADEMICA ua on ua.id = p.unidadacademica_id "
                + "  join ENT_TIPO_BECA_PERIODO tbp on tbp.id = o.tipobecaperiodo_id "
                + "  join CAT_TIPO_BECA tb on tb.id = tbp.TIPOBECA_ID "
                + "  join CAT_PROGRAMA_BECA b on b.id = tb.beca_id "
                + "where obd.periodo_id = ?1 "
                + "AND b.id = ?2 "
                + "and alta = 0 "
                + "AND o.proceso_id IS NOT NULL "
                + "and m.TIPOMOVIMIENTO_ID = 2 ";

        if (unidadAcademicaId != null && unidadAcademicaId.intValue() != 0) {
            sql += "AND p.unidadAcademica_id = " + unidadAcademicaId + " ";
        } else if (nivelId != null && nivelId.intValue() != 0) {
            sql += "AND ua.nivel_id = " + nivelId + " ";
        }
        if (movimiento != null && movimiento.intValue() != 0) {
            sql += "AND m.id = " + movimiento + " ";
        }

        sql += "group by tb.nombre,g.clave) "
                + "PIVOT ( "
                + "sum(total) "
                + "FOR clave in ('M' M,'F' F)) "
                + "order by titulo asc";
        List<Object[]> lista = executeNativeQuery(sql, periodoId, becaId);
        return lista.isEmpty() ? null : lista;
    }

    @Override
    public List<Object[]> otorgamientosPasantiaAuto() {
        String sql = "SELECT o.id, 1 "
                + "FROM ent_otorgamientos o  "
                + "  join ent_alumno a on a.id = o.alumno_id  "
                + "  join ENT_ALUMNO_DATOS_ACADEMICOS da on da.id = o.DATOSACADEMICOS_ID "
                + "  join ent_carrera c on da.carrera_id = c.id  "
                + "  join ENT_PROCESO pr on pr.id = o.PROCESO_ID "
                + "  join cat_unidad_academica ua on ua.id = pr.UNIDADACADEMICA_ID  "
                + "where (da.EGRESADO = 1 OR   "
                + "        (SELECT  COUNT(distinct (o2.periodo_id))  "
                + "        FROM ENT_OTORGAMIENTOS o2  "
                + "          join ENT_PROCESO pr2 on pr2.id = o2.PROCESO_ID "
                + "          join cat_unidad_academica ua2 on ua2.id = pr2.unidadacademica_id  "
                + "        WHERE o2.PROCESO_ID is not null   "
                + "        and ua2.NIVEL_ID = ua.nivel_id  "
                + "        AND o2.id not in (SELECT v.otorgamiento_id FROM vw_ultima_baja_detalle v  WHERE v.PERIODO_ID=o2.periodo_id AND v.MOVIMIENTO_ID is not null)  "
                + "        AND o2.ALUMNO_ID=o.alumno_id)>= c.NUMEROSEMESTRES "
                + "        )  "
                + "and o.proceso_id IS NOT NULL  "
                + "and o.alta = 1 "
                + "and o.id not in (SELECT ubd.otorgamiento_id FROM vw_ultima_baja_detalle ubd WHERE ubd.PERIODO_ID=(select p.id from cat_periodo p where p.estatus = 1 and rownum = 1)) "
                + "and o.periodo_id = (select p.PERIODOANTERIOR_ID from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "and o.alumno_id not in ( "
                + "        SELECT o3.alumno_id "
                + "        FROM ENT_OTORGAMIENTOS o3 "
                + "        where o3.proceso_id IS NOT NULL "
                + "        and o3.PERIODO_ID = (SELECT p.ID from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "        and o3.alta = 1 "
                + ")";

        List<Object[]> lista = executeNativeQuery(sql);
        return lista.isEmpty() ? null : lista;
    }

    @Override
    public List<Object[]> otorgamientosIncumplimientoAuto() {
        String sql = "SELECT o.id, 1 "
                + "FROM ENT_OTORGAMIENTOS O   "
                + "LEFT JOIN ENT_OTORGAMIENTOS OA ON OA.ALUMNO_ID = O.ALUMNO_ID AND OA.PERIODO_ID in (SELECT P.ID FROM CAT_PERIODO P WHERE P.ESTATUS = 1 AND ROWNUM = 1) and oa.proceso_id is not null "
                + "WHERE O.PERIODO_ID = (SELECT P.periodoanterior_id FROM CAT_PERIODO P WHERE P.ESTATUS = 1 AND ROWNUM = 1)  AND O.ALTA = 1 AND O.PROCESO_ID IS NOT NULL "
                + "AND OA.ID IS NULL "
                + "AND O.ID NOT IN (SELECT V.OTORGAMIENTO_ID FROM VW_ULTIMA_BAJA_DETALLE V  WHERE V.PERIODO_ID=(SELECT P.ID FROM CAT_PERIODO P WHERE P.ESTATUS = 1 AND ROWNUM = 1)  "
                + "AND V.MOVIMIENTO_ID IS NOT NULL)  ";
        List<Object[]> lista = executeNativeQuery(sql);
        return lista.isEmpty() ? null : lista;
    }

    @Override
    public List<Object[]> otorgamientosReporteBanamex(String periodoId, String nivelId, String unidadAcademicaId, String boleta, Integer tipo) {
        String consulta = getConsultaSolicitantes(periodoId, nivelId, unidadAcademicaId, boleta, tipo, 1);
        List<Object[]> lista = executeNativeQuery(consulta, periodoId);
        return (lista);
    }

    @Override
    public List<Object[]> listadoSolicitantes(String periodoId, String nivelId, String unidadAcademicaId, String boleta, Integer tipo) {
        String consulta = getConsultaSolicitantes(periodoId, nivelId, unidadAcademicaId, boleta, tipo, 3);
        List<Object[]> lista = executeNativeQuery(consulta, periodoId);
        return lista.isEmpty() ? null : lista;
    }

    @Override
    public String countSolicitudes(String periodoId, String nivelId, String unidadAcademicaId, String boleta, Integer tipo) {
        String consulta = getConsultaSolicitantes(periodoId, nivelId, unidadAcademicaId, boleta, tipo, 2);
        return getCountNativeQuery(consulta, periodoId).toString();
    }

    @Override
    public String countSolicitudes(String identificador) {
        String consulta = "SELECT count(atb.id) "
                + "FROM RMM_ALUMNO_TARJETA_BANCARIA atb "
                + "  JOIN ENT_SOLICITUD_CUENTAS s ON s.ID = atb.SOLICITUDCUENTA_ID "
                + "WHERE s.IDENTIFICADOR = ?1 ";
        return getCountNativeQuery(consulta, identificador).toString();
    }

    @Override
    public List<Object[]> getSolicitudes(String identificador) {
        String consulta = "select ua.clave, \n"
                + "a.BOLETA,  \n"
                + "adb.NOMBRE, \n"
                + "adb.APELLIDOPATERNO, \n"
                + "adb.APELLIDOMATERNO, \n"
                + "adb.FECHADENACIMIENTO, \n"
                + "adb.ESTADOCIVIL_ID, \n"
                + "adb.CALLE, \n"
                + "adb.NUMEROEXTERIOR, \n"
                + "adb.COLONIA, \n"
                + "adb.CODIGOPOSTAL, \n"
                + "a.CELULAR, \n"
                + "adb.MUNICIPIO, \n"
                + "adb.ESTADO, \n"
                + "adb.GENERO_ID, \n"
                + "a.CELULAR, \n"
                + "ua.NIVEL_ID as od1, ua.ID as od2, a.CURP as od3, \n"
                + "a.NOMBRE, \n"
                + "a.APELLIDOPATERNO, \n"
                + "a.APELLIDOMATERNO,\n"
                + "a.FECHADENACIMIENTO, \n"
                + "a.ESTADOCIVIL_ID,\n"
                + "a.CURP, \n"
                + "a.GENERO_ID, \n"
                + "ua.NOMBRECORTO \n"
                + ",a.id, adb.id "
                + "from ent_alumno a "
                + "  join RMM_ALUMNO_TARJETA_BANCARIA atb on atb.alumno_id = a.id "
                + "  JOIN ENT_ALUMNO_DATOS_BANCARIOS adb on adb.id = atb.DATOSBANCARIOS_ID "
                + "  JOIN ENT_SOLICITUD_CUENTAS s ON s.ID = atb.SOLICITUDCUENTA_ID "
                + "left join ENT_OTORGAMIENTOS o on a.id = o.alumno_id and o.periodo_id = s.periodogeneracion_id "
                + "left JOIN ENT_PROCESO pr on pr.id = o.PROCESO_ID "
                + "left join CAT_UNIDAD_ACADEMICA ua on ua.id = pr.UNIDADACADEMICA_ID "
                + "WHERE s.IDENTIFICADOR = ?1 "
                + "group by ua.clave, a.BOLETA,  "
                + "adb.NOMBRE, adb.APELLIDOPATERNO, adb.APELLIDOMATERNO, adb.FECHADENACIMIENTO, adb.ESTADOCIVIL_ID, "
                + "adb.CALLE, adb.NUMEROEXTERIOR, adb.COLONIA, adb.CODIGOPOSTAL, a.CELULAR, adb.MUNICIPIO, adb.ESTADO, "
                + "adb.GENERO_ID, a.CELULAR, a.NOMBRE, a.APELLIDOPATERNO, a.APELLIDOMATERNO, a.FECHADENACIMIENTO, "
                + "a.ESTADOCIVIL_ID, a.CURP, ua.NIVEL_ID, ua.ID , a.CURP, a.GENERO_ID, ua.NOMBRECORTO,a.id, adb.id "
                + " ORDER BY od1, od2, od3 ";
        System.out.println(consulta);
        List<Object[]> lista = executeNativeQuery(consulta, identificador);
        return (lista);
    }

    //SQLINJECTION
    protected String getConsultaSolicitantes(String periodoId, String nivelId, String unidadAcademicaId, String boleta, Integer tipo, Integer tipoConsulta) {
        String consulta = "";
        switch (tipoConsulta) {
            case 1:
                consulta += "select ua.clave, \n"
                        + "a.BOLETA,  \n"
                        + "adb.NOMBRE, \n"
                        + "adb.APELLIDOPATERNO, \n"
                        + "adb.APELLIDOMATERNO, \n"
                        + "adb.FECHADENACIMIENTO, \n"
                        + "adb.ESTADOCIVIL_ID, \n"
                        + "adb.CALLE, \n"
                        + "adb.NUMEROEXTERIOR, \n"
                        + "adb.COLONIA, \n"
                        + "adb.CODIGOPOSTAL, \n"
                        + "a.CELULAR, \n"
                        + "adb.MUNICIPIO, \n"
                        + "adb.ESTADO, \n"
                        + "adb.GENERO_ID, \n"
                        + "a.CELULAR, \n"
                        + "ua.NIVEL_ID as od1, ua.ID as od2, a.CURP as od3, \n"
                        + "a.NOMBRE, \n"
                        + "a.APELLIDOPATERNO, \n"
                        + "a.APELLIDOMATERNO,\n"
                        + "a.FECHADENACIMIENTO, \n"
                        + "a.ESTADOCIVIL_ID,\n"
                        + "a.CURP, \n"
                        + "a.GENERO_ID, \n"
                        + "ua.NOMBRECORTO \n"
                        + ",a.id, adb.id ";
                break;
            case 2:
                consulta += "select count(distinct(a.id)) ";
                break;
            case 3:
                consulta += "select distinct(a.id),1 ";
                break;
        }

        switch (tipo) {
            case 1:
                if (getDaos().getPeriodoDao().getPeriodoActivo().getId().equals(getDaos().getPeriodoDao().findById(new BigDecimal(periodoId)).getId())) {
                    consulta += "from ent_alumno a\n"
                            + "  join ENT_OTORGAMIENTOS o on a.id = o.alumno_id  \n"
                            + "  JOIN ENT_ALUMNO_DATOS_BANCARIOS adb on adb.ALUMNO_ID = a.id and adb.vigente = 1\n"
                            + "  JOIN ENT_PROCESO pr on pr.id = o.PROCESO_ID \n"
                            + "  join CAT_UNIDAD_ACADEMICA ua on ua.id = pr.UNIDADACADEMICA_ID  \n"
                            + "left join(select max(id) as id, alumno_id from RMM_ALUMNO_TARJETA_BANCARIA group by alumno_id) maxatb on maxatb.alumno_id = a.id \n"
                            + "left join RMM_ALUMNO_TARJETA_BANCARIA atb on atb.id = maxatb.id \n"
                            + "where o.periodo_id = ?1 \n"
                            + "and (o.alta = 1 OR o.id NOT IN (SELECT obd.otorgamiento_id FROM ent_otorgamientos_bajas_detall obd WHERE periodo_id = o.periodo_id and obd.OTORGAMIENTO_ID = o.id))\n"
                            + "and o.proceso_id IS NOT NULL \n"
                            + "and a.DATOSBANCARIOS = 1\n"
                            + "and (atb.id is null or atb.ESTATUSTARJBANC_ID < 12 or atb.ESTATUSTARJBANC_ID is null) ";
                } else {
                    consulta += "FROM ent_alumno a \n"
                            + "  join ent_otorgamientos o on a.id = o.alumno_id \n"
                            + "  join ENT_ALUMNO_DATOS_ACADEMICOS da on da.id = o.datosAcademicos_id \n"
                            + "  join ENT_PROCESO p on p.id = o.PROCESO_ID \n"
                            + "  join cat_unidad_academica ua on p.unidadacademica_id = ua.id \n"
                            + "  join ent_carrera c on da.carrera_id = c.id \n"
                            + "  join ent_tipo_beca_periodo tbp on tbp.id = o.tipobecaperiodo_id \n"
                            + "  join cat_tipo_beca tb on tb.id = tbp.tipobeca_id \n"
                            + "  join CAT_PROGRAMA_BECA b on b.id = tb.BECA_ID \n"
                            + "  join RMM_DEPOSITO_UNIDAD_ACADEMICA dua on dua.UNIDADACADEMICA_ID = ua.id and dua.TIPOBECA_ID = tb.ID \n"
                            + "  join vw_conteo_depositos cd on cd.otorgamiento_id = o.id \n"
                            + "left join(select max(id) as id, alumno_id from RMM_ALUMNO_TARJETA_BANCARIA group by alumno_id) maxatb on maxatb.alumno_id = a.id \n"
                            + "left join RMM_ALUMNO_TARJETA_BANCARIA atb on atb.id = maxatb.id \n"
                            + "where  cd.conteodepositos <  tbp.DURACION \n"
                            + "and o.id not in (SELECT v.otorgamiento_id FROM vw_ultima_baja_detalle v  WHERE v.PERIODO_ID=o.periodo_id AND v.MOVIMIENTO_ID is not null AND v.otorgamiento_id = o.id) \n"
                            + "and (atb.id is null or atb.ESTATUSTARJBANC_ID < 12 or atb.ESTATUSTARJBANC_ID is null) \n"
                            + "and dua.CORRESPONDEIPN = 1 \n"
                            + "and o.proceso_id IS NOT NULL \n"
                            + "and a.DATOSBANCARIOS = 1 \n"
                            + "and o.periodo_id = ?1 ";
                }
                break;
            case 2:
                consulta += "from ent_alumno a\n"
                        + "  join ENT_OTORGAMIENTOS o on a.id = o.alumno_id  \n"
                        + "  JOIN ENT_ALUMNO_DATOS_BANCARIOS adb on adb.ALUMNO_ID = a.id \n"
                        + "  JOIN ENT_ALUMNO_DATOS_BANCARIOS adb2 on adb2.ALUMNO_ID = a.id \n"
                        + "  JOIN ENT_PROCESO pr on pr.id = o.PROCESO_ID \n"
                        + "  join CAT_UNIDAD_ACADEMICA ua on ua.id = pr.UNIDADACADEMICA_ID  \n"
                        + "  join RMM_ALUMNO_TARJETA_BANCARIA atb on atb.alumno_id = a.id and atb.DATOSBANCARIOS_ID = adb2.id\n"
                        + "left join  RMM_ALUMNO_TARJETA_BANCARIA atb2 on atb2.alumno_id = a.id and atb2.DATOSBANCARIOS_ID = adb.id\n"
                        + "  join ENT_TARJETA_BANCARIA t on t.id = atb.TARJETABANCARIA_ID \n"
                        + "where o.periodo_id = ?1 \n"
                        + "and (o.alta = 1 OR o.id NOT IN (SELECT obd.otorgamiento_id FROM ent_otorgamientos_bajas_detall obd WHERE periodo_id = o.periodo_id and obd.OTORGAMIENTO_ID = o.id))\n"
                        + "and o.proceso_id IS NOT NULL \n"
                        + "and a.DATOSBANCARIOS = 1\n "
                        + "and ( \n "
                        + "atb.estatustarjbanc_id = 16 and t.CUENTA = 1 and adb2.VIGENTE = 0 \n "
                        + "and adb.vigente = 1 and atb2.id is null \n "
                        + ") ";
                break;
            case 3:
                consulta += "from ent_alumno a\n"
                        + "  join ENT_OTORGAMIENTOS o on a.id = o.alumno_id  \n"
                        + "  JOIN ENT_ALUMNO_DATOS_BANCARIOS adb on adb.ALUMNO_ID = a.id and adb.vigente = 1\n"
                        + "  JOIN ENT_PROCESO pr on pr.id = o.PROCESO_ID \n"
                        + "  join CAT_UNIDAD_ACADEMICA ua on ua.id = pr.UNIDADACADEMICA_ID  \n"
                        + "left join(select max(id) as id, alumno_id from RMM_ALUMNO_TARJETA_BANCARIA group by alumno_id) maxatb on maxatb.alumno_id = a.id \n"
                        + "left join RMM_ALUMNO_TARJETA_BANCARIA atb on atb.id = maxatb.id \n"
                        + "where o.periodo_id = ?1 \n"
                        + "and (o.alta = 1 OR o.id NOT IN (SELECT obd.otorgamiento_id FROM ent_otorgamientos_bajas_detall obd WHERE periodo_id = o.periodo_id and obd.OTORGAMIENTO_ID = o.id))\n"
                        + "and o.proceso_id IS NOT NULL \n"
                        + "and a.DATOSBANCARIOS = 1 \n"
                        + "and atb.estatustarjbanc_id = 15 ";
                break;
            default:
                consulta += "from ent_alumno a\n"
                        + "  join ENT_OTORGAMIENTOS o on a.id = o.alumno_id  \n"
                        + "  JOIN ENT_ALUMNO_DATOS_BANCARIOS adb on adb.ALUMNO_ID = a.id and adb.vigente = 1\n"
                        + "  JOIN ENT_PROCESO pr on pr.id = o.PROCESO_ID \n"
                        + "  join CAT_UNIDAD_ACADEMICA ua on ua.id = pr.UNIDADACADEMICA_ID  \n"
                        + "left join(select max(id) as id, alumno_id from RMM_ALUMNO_TARJETA_BANCARIA group by alumno_id) maxatb on maxatb.alumno_id = a.id \n"
                        + "left join RMM_ALUMNO_TARJETA_BANCARIA atb on atb.id = maxatb.id \n"
                        + "where o.periodo_id = ?1 \n"
                        + "and (o.alta = 1 OR o.id NOT IN (SELECT obd.otorgamiento_id FROM ent_otorgamientos_bajas_detall obd WHERE periodo_id = o.periodo_id and obd.OTORGAMIENTO_ID = o.id))\n"
                        + "and o.proceso_id IS NOT NULL \n"
                        + "and a.DATOSBANCARIOS = 1\n"
                        + "and (atb.id is null or atb.ESTATUSTARJBANC_ID < 12 or atb.ESTATUSTARJBANC_ID is null) ";
                break;
        }
        if (isFuncionario() || isResponsable()) {
            Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
            BigDecimal ua = getDaos().getPersonalAdministrativoDao().findByUsuario(usuario.getId()).getUnidadAcademica().getId();
            if (ua != null) {
                consulta = consulta + "and ua.id = " + ua + " ";
            }
        } else if (!"0".equals(unidadAcademicaId)) {
            consulta += "AND ua.id = " + unidadAcademicaId + " ";
        } else if (!"0".equals(nivelId)) {
            consulta += "AND ua.nivel_id = " + nivelId + " ";
        }
        if (boleta.length() > 3) {
            consulta += "AND a.boleta like '%" + boleta + "%' ";
        }
        if (tipoConsulta == 1) {
            consulta = consulta + "group by ua.clave, a.BOLETA,  "
                    + "adb.NOMBRE, adb.APELLIDOPATERNO, adb.APELLIDOMATERNO, adb.FECHADENACIMIENTO, adb.ESTADOCIVIL_ID, "
                    + "adb.CALLE, adb.NUMEROEXTERIOR, adb.COLONIA, adb.CODIGOPOSTAL, a.CELULAR, adb.MUNICIPIO, adb.ESTADO, "
                    + "adb.GENERO_ID, a.CELULAR, a.NOMBRE, a.APELLIDOPATERNO, a.APELLIDOMATERNO, a.FECHADENACIMIENTO, "
                    + "a.ESTADOCIVIL_ID, a.CURP, ua.NIVEL_ID, ua.ID , a.CURP, a.GENERO_ID, ua.NOMBRECORTO,a.id, adb.id "
                    + " ORDER BY od1, od2, od3 ";
        }
        System.out.println(consulta);
        return consulta;
    }

    protected boolean isResponsable() {
        Collection<GrantedAuthority> gas = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (GrantedAuthority ga : gas) {
            if (ga.getAuthority().equals("ROLE_RESPONSABLE_UA")) {
                return true;
            }
        }
        return false;
    }

    protected boolean isFuncionario() {
        Collection<GrantedAuthority> gas = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (GrantedAuthority ga : gas) {
            if (ga.getAuthority().equals("ROLE_FUNCIONARIO_UA")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Busca si la beca tiene o no validación de inscripción, exclusivamente
     * para otorgamientos de transporte (programa beca 7, 8 y 9)
     *
     * @author Mario Márquez
     * @param periodo
     * @param alumnoId
     * @param transporte Nulo todo, true transporte, false institucional
     * @return true si la beca tiene validación de inscripción
     */
    @Override
    public Boolean tieneValidacionInscripcion(Periodo periodo, BigDecimal alumnoId, Boolean transporte) {
        StringBuilder sb = new StringBuilder();
        List<String> criteria = new ArrayList<>();

        sb.append(" select count(*)");
        sb.append(" from ent_otorgamientos o");
        sb.append(" inner join ent_alumno a on o.alumno_id = a.id");
        sb.append(" inner join ent_tipo_beca_periodo tbp on o.tipobecaperiodo_id = tbp.id");
        sb.append(" inner join cat_tipo_beca tb on tb.id = tbp.tipobeca_id");
        sb.append(" inner join cat_programa_beca pb on pb.id = tb.beca_id");

        criteria.add("o.periodo_id = ?1");
        criteria.add("o.proceso_id is not null");
        criteria.add("o.alta = 1");
        criteria.add("tbp.tipobeca_id in (select tipobeca_id from ent_tipo_beca_periodo bp where nvl(bp.validaciondeinscripcion,0) > 0 and periodo_id = ?2)");
        // Nulo todo, true transporte, false institucional
        if (transporte != null) {
            if (transporte) {
                criteria.add("pb.id in (7, 8, 9)");
            } else {
                criteria.add("pb.id = 1");
            }
        } else {
            criteria.add("pb.id not in (7, 8, 9, 10)");
        }

        criteria.add("a.id = ?3");

        agregaCriterios(sb, criteria);

        Long result = getCountNativeQuery(sb.toString(), periodo.getPeriodoAnterior().getId(), periodo.getId(), alumnoId);

        return result > 0;
    }

    @Override
    public List<Object[]> resumenBecalos(String periodoId) {
        String consulta = "select A.CURP,A.BOLETA,A.APELLIDOPATERNO,A.APELLIDOMATERNO,A.NOMBRE, "
                + "extract (day from A.FECHADENACIMIENTO ),extract ( month from A.FECHADENACIMIENTO ), extract ( year from A.FECHADENACIMIENTO ), "
                + "decode(G.CLAVE,'M','H','F','M' ) ,E.NOMBRE,ES.NOMBRE,M.NOMBRE,LC.NOMBRE,D.CALLE||' '||D.NUMEROEXTERIOR||' int'||D.NUMEROINTERIOR,D.CODIGOPOSTAL, "
                + "REPLACE(A.CELULAR,'-',''),A.CORREOELECTRONICO,REPLACE( REPLACE(TB.NOMBRE,'Bécalos ',''),'Ipn ',''),'IPN', "
                + "UA.NOMBRECORTO ,CA.CARRERA,N.NOMBRE,CASE TB.ID  "
                + "WHEN 22 THEN 'Bécalos AR' "
                + "WHEN 23 THEN 'Bécalos AR' "
                + "ELSE 'Bécalos'  END,da.SEMESTRE,'SEMESTRE',CA.NUMEROSEMESTRES,'SEMESTRES',da.PROMEDIO,DECODE(TP.MOVIMIENTO_ID,2,'RE','NI' ) "
                + "from ENT_OTORGAMIENTOS o "
                + "  join ENT_ALUMNO_DATOS_ACADEMICOS da on da.id = o.DATOSACADEMICOS_ID "
                + "join ent_proceso p on O.PROCESO_ID=p.id "
                + "join CAT_TIPO_PROCESO tp on P.TIPOPROCESO_ID=tp.id "
                + "join ent_alumno a on O.ALUMNO_ID=a.id   "
                + "join CAT_UNIDAD_ACADEMICA ua on A.UNIDADACADEMICA_ID=UA.ID "
                + "join CAT_NIVEL n on UA.NIVEL_ID=n.id "
                + "join ENT_CARRERA ca on da.CARRERA_ID=CA.ID "
                + "join CAT_GENERO g on A.GENERO_ID=G.ID "
                + "join CAT_ESTADO_CIVIL e on A.ESTADOCIVIL_ID = E.ID "
                + "join ENT_DIRECCION d on A.DIRECCION_ID=d.id "
                + "join RMM_ESTADO_DELEG_COL r on D.RELACIONGEOGRAFICA_ID=R.ID "
                + "join CAT_ESTADO es on R.ESTADO_ID=ES.ID "
                + "join CAT_DELEGACION_MUNICIPIO m on R.MUNICIPIO_ID=m.id "
                + "join CAT_LOCALIDAD_COLONIA lc on R.COLONIA_ID=LC.ID "
                + "join ENT_TIPO_BECA_PERIODO tbp on O.TIPOBECAPERIODO_ID=tbp.id "
                + "join cat_tipo_beca tb on TBP.TIPOBECA_ID=tb.id "
                + "where o.id not in (SELECT otorgamiento_id FROM VW_ULTIMA_BAJA_DETALLE where movimiento_id is not null and periodo_id = O.PERIODO_ID) "
                + "and O.PROCESO_ID is not null  "
                + "and O.PERIODO_ID= ?1 "
                + "AND tb.beca_id=4 "
                + "order by UA.ID,A.CURP";
        List<Object[]> lista = executeNativeQuery(consulta, periodoId);
        return (lista);
    }

    @Override
    public String nombreProcesoBajaOtorgamiento(BigDecimal otorgamientoId) {
        String consulta = "SELECT tpr.nombre, 1 FROM VW_ULTIMA_BAJA_DETALLE obd "
                + "   join ENT_PROCESO p on p.id = obd.PROCESO_ID "
                + "   join cat_tipo_proceso tpr on tpr.id = p.TIPOPROCESO_ID "
                + " where obd.OTORGAMIENTO_ID = ?1 ";

        List<Object[]> lista = executeNativeQuery(consulta, otorgamientoId);

        return ((String) lista.get(0)[0]);
    }

    @Override
    public Otorgamiento getOtorgamientoSolicitudPeriodo(BigDecimal solicitudId, BigDecimal periodoId) {
        String jpql = " SELECT o FROM Otorgamiento o "
                + " WHERE o.solicitudBeca.id = ?1"
                + " and o.periodo.id=?2 ";
        List<Otorgamiento> executeQuery = executeQuery(jpql, solicitudId, periodoId);
        return executeQuery == null || executeQuery.isEmpty() ? null : executeQuery.get(0);
    }

    @Override
    public Otorgamiento getOtorgamientoAnteriorPrograma(Alumno a, Beca b, Periodo periodo) {
        if (periodo == null) {
            periodo = getDaos().getPeriodoDao().getPeriodoActivo();
        }

        String jpql = "SELECT o FROM Otorgamiento o, VWOtorgamiento vw "
                + " WHERE o.id = vw.id and o.periodo.id = ?1 "
                + " and vw.tipoBecaPeriodo.tipoBeca.beca.id=?2 "
                + " and vw.alumno.id = ?3 "
                + " and vw.proceso is not null ";
        List<Otorgamiento> executeQuery = executeQuery(jpql, periodo.getPeriodoAnterior().getId(), b.getId(), a.getId());
        return executeQuery == null || executeQuery.isEmpty() ? null : executeQuery.get(0);
    }

    public Boolean tieneMovimientoPeriodoActual(BigDecimal alumnoId) {
        String sql = "select * from ent_otorgamientos o"
                + "   join cat_periodo p on p.id = o.periodo_id and p.estatus = 1"
                + "   join ENT_TIPO_BECA_PERIODO tbp on tbp.id = o.TIPOBECAPERIODO_ID"
                + "   join CAT_TIPO_BECA tb on tb.id = tbp.TIPOBECA_ID"
                + " where tb.BECA_ID != 10"
                + " and o.alumno_id = ?1 and o.proceso_id is not null";
        List<Object[]> lista = executeNativeQuery(sql, alumnoId);
        return lista != null && !lista.isEmpty();
    }

    public Boolean tieneOtorgamientoDistintoUniversal(BigDecimal alumno_id) {
        String consulta = "SELECT o FROM Otorgamiento o"
                + " WHERE o.alumno.id = ?1"
                + " AND o.periodo.estatus = 1"
                + " AND o.tipoBecaPeriodo.tipoBeca.beca.id NOT IN (7,8,9,10)"
                + " AND o.proceso IS NOT NULL";
        List<Otorgamiento> lista = executeQuery(consulta, alumno_id);
        return (lista != null && !lista.isEmpty());
    }

    public final List<Otorgamiento> getOtorgamientosNoUniversal(BigDecimal alumno_id) {
        StringBuilder sb = new StringBuilder();
        List<String> criteria = new ArrayList<>();

        sb.append(" SELECT o FROM Otorgamiento o");

        criteria.add("o.alumno.id = ?1");
        criteria.add("o.periodo.estatus = 1");
        criteria.add("o.tipoBecaPeriodo.tipoBeca.beca.id != 10");
        criteria.add("o.proceso IS NOT NULL");

        agregaCriterios(sb, criteria);

        return executeQuery(sb.toString(), alumno_id);
    }

    @Override
    public int contadorAlumnoPorBecaPorUA(BigDecimal becaId, BigDecimal unidadAcademicaId) {
        String ua = unidadAcademicaId == null ? "" : " and o.datosAcademicos.unidadAcademica.id = " + unidadAcademicaId;
        String consulta = "select o from Otorgamiento o where o.periodo.estatus = 1"
                + " and o.tipoBecaPeriodo.tipoBeca.beca.id = ?1" + ua;
        List<Otorgamiento> lista = executeQuery(consulta, becaId);
        return lista == null || lista.isEmpty() ? 0 : lista.size();
    }

    @Override
    public String totalAlumnosBecaUniversal(BigDecimal nivelId) {
        String consulta = "SELECT ua.ID, ua.NOMBRECORTO, count(v.id)\n"
                + "FROM vw_otorgamientos v\n"
                + "JOIN ENT_PROCESO P ON v.PROCESO_ID = P.ID\n"
                + "JOIN ENT_TIPO_BECA_PERIODO tbp on TBP.ID = V.TIPOBECAPERIODO_ID\n"
                + "JOIN CAT_TIPO_BECA tb on TB.ID = TBP.TIPOBECA_ID\n"
                + "JOIN CAT_PROGRAMA_BECA pb on PB.ID = TB.BECA_ID\n"
                + "JOIN CAT_UNIDAD_ACADEMICA UA on ua.id = p.UNIDADACADEMICA_ID\n"
                + "WHERE v.PERIODO_ID=(SELECT ID from cat_periodo pe where pe.estatus = 1 and rownum = 1)\n"
                + "AND ua.nivel_id = ?1\n"
                + "AND pb.id = 10\n"
                + "GROUP BY ua.ID, ua.NOMBRECORTO\n"
                + "ORDER BY ua.ID";
        List<Object[]> ax = executeNativeQuery(consulta, nivelId);
        String s = "";
        for (Object[] res : ax) {
            s = s + res[1] + "," + res[2] + "/";
        }
        if (s.length() > 1) {
            return s;
        } else {
            return null;
        }
    }

    @Override
    public Boolean tieneOtorgamientoManutencionPeriodoActual(BigDecimal alumno_id) {
        String consulta = "select * from ENT_OTORGAMIENTOS o "
                + "  join cat_periodo p on p.id = o.periodo_id and p.estatus = 1 "
                + "  JOIN ENT_TIPO_BECA_PERIODO tbp on tbp.id = o.TIPOBECAPERIODO_ID "
                + "  JOIN CAT_TIPO_BECA tb on tb.id = tbp.TIPOBECA_ID "
                + "WHERE tb.BECA_ID = 5 "
                + "and o.alumno_id = ?1 and o.proceso_id is not null ";
        List<Object[]> lista = executeNativeQuery(consulta, alumno_id);
        return (lista != null && !lista.isEmpty());
    }

    @Override
    public Boolean tieneOtorgamientoUniversal(BigDecimal alumnoId, BigDecimal periodoId) {
        StringBuilder sb = new StringBuilder();

        sb.append(" select o ");
        sb.append(" FROM Otorgamiento o ")
                .append(" WHERE o.periodo.id = :periodoId")
                .append(" AND o.alumno.id = :alumnoId")
                .append(" AND o.alta = 1")
                .append(" AND o.tipoBecaPeriodo.tipoBeca.beca.id = 10 ");

        Map<String, Object> mapa = new HashMap<>();
        mapa.put("periodoId", periodoId);
        mapa.put("alumnoId", alumnoId);

        List<Otorgamiento> lista = executeQuery(sb.toString(), mapa, null);

        return (lista != null && !lista.isEmpty());
    }

    @Override
    public Boolean tieneOtorgamientoComplementaria(BigDecimal alumnoId, BigDecimal periodoId) {
        StringBuilder sb = new StringBuilder();

        sb.append(" select o ");
        sb.append(" FROM Otorgamiento o ")
                .append(" WHERE o.periodo.id = :periodoId ")
                .append(" AND o.alumno.id = :alumnoId ")
                .append(" AND o.alta = 1 ")
                .append(" AND o.tipoBecaPeriodo.visible = 0 ");

        Map<String, Object> mapa = new HashMap<>();

        mapa.put("periodoId", periodoId);
        mapa.put("alumnoId", alumnoId);

        List<Otorgamiento> lista = executeQuery(sb.toString(), mapa, null);

        return (lista != null && !lista.isEmpty());
    }

    /**
     * Verifica que no haya otro alumno con la misma curp a quien se le haya
     * dado un otorgamiento en el mismo periodo
     *
     * @param a
     * @param p
     * @return
     */
    @Override
    public List<Otorgamiento> existeDuplicadoCurpConOtorgamiento(Alumno a, Periodo p) {
        String jpql = "SELECT o FROM Otorgamiento o WHERE o.alumno.curp = ?1 and o.alumno.id != ?2  and o.periodo.id = ?3";
        List<Otorgamiento> lista = executeQuery(jpql, a.getCurp(), a.getId(), p.getId());
        return lista == null || lista.isEmpty() ? null : lista;
    }

    public List<Object[]> reporteProceso(BigDecimal periodoId, BigDecimal unidadAcademicaId, BigDecimal becaid) {
        String sql = "SELECT a.boleta, ua.NOMBRECORTO, a.APELLIDOPATERNO, a.APELLIDOMATERNO, a.NOMBRE, tb.NOMBRE, da.PROMEDIO, da.SEMESTRE, o.EXCLUIRDEPOSITO, io.NOMBRE \n"
                + "from ent_alumno a\n"
                + "inner join vw_otorgamientos o on o.alumno_id = a.id\n"
                + "inner join ENT_TIPO_BECA_PERIODO tbp on tbp.id = o.TIPOBECAPERIODO_ID\n"
                + "inner join CAT_TIPO_BECA tb on tb.id = tbp.TIPOBECA_ID\n"
                + "inner join CAT_PROGRAMA_BECA pb on pb.id = tb.BECA_ID\n"
                + "inner join ENT_ALUMNO_DATOS_ACADEMICOS da on da.alumno_id = a.id and da.periodo_id = o.periodo_id\n"
                + "inner join CAT_UNIDAD_ACADEMICA ua on ua.id = da.UNIDADACADEMICA_ID\n"
                + "left join CAT_IDENTIFICADOR_OTORGAMIENTO io on io.id = o.IDENTIFICADOROTORGAMIENTO_ID\n"
                + "WHERE o.PERIODO_ID = ?1 "
                + ((becaid == null || becaid.compareTo(BigDecimal.ZERO) == 0) ? " " : " AND  pb.id = ?2 ")
                + ((unidadAcademicaId == null || unidadAcademicaId.compareTo(BigDecimal.ZERO) == 0) ? " " : " AND ua.id = ?3 ");
        List<Object[]> lista = executeNativeQuery(sql, periodoId, becaid, unidadAcademicaId);
        return lista.isEmpty() ? null : lista;
    }

    //Obtiene el numero de becarios
    public Long estadisticaBecarios(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal becaId, BigDecimal movimiento) {
        StringBuilder sb = new StringBuilder();
        List<String> criteria = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        sb.append(" SELECT COUNT(DISTINCT o.ALUMNO_ID)");
        sb.append(" FROM VW_OTORGAMIENTOS o");
        sb.append("  join ent_alumno a on a.id = o.alumno_id");
        sb.append(" join cat_genero g on g.id = a.genero_id ");
        sb.append("  join ENT_PROCESO p on p.id = o.proceso_id ");
        sb.append("  join CAT_TIPO_PROCESO tp on tp.id = p.TIPOPROCESO_ID ");
        sb.append("  join CAT_MOVIMIENTO m on m.id = tp.MOVIMIENTO_ID ");
        sb.append("  JOIN CAT_UNIDAD_ACADEMICA ua on ua.id = p.unidadacademica_id ");
        sb.append("  join ENT_TIPO_BECA_PERIODO tbp on tbp.id = o.tipobecaperiodo_id ");
        sb.append("  join CAT_TIPO_BECA tb on tb.id = tbp.TIPOBECA_ID ");
        sb.append("  join CAT_PROGRAMA_BECA b on b.id = tb.beca_id ");
        criteria.add("o.PERIODO_ID = #periodoId");
        params.put("periodoId", periodoId);
        if (nivelId.compareTo(BigDecimal.ZERO) == 0 || nivelId == null) {
        } else {
            criteria.add("ua.NIVEL_ID = #nivelId");
            params.put("nivelId", nivelId);
        }
        if (unidadAcademicaId.compareTo(BigDecimal.ZERO) == 0 || unidadAcademicaId == null) {
        } else {
            criteria.add("ua.ID = #unidadAcademicaId");
            params.put("unidadAcademicaId", unidadAcademicaId);
        }
        if (becaId.compareTo(BigDecimal.ZERO) == 0 || becaId == null) {
        } else {
            criteria.add("b.id = #becaId");
            params.put("becaId", becaId);
        }
        if (movimiento.compareTo(BigDecimal.ZERO) == 0 || movimiento == null) {
        } else {
            criteria.add("m.id = #movimiento");
            params.put("movimiento", movimiento);
        }

        agregaCriterios(sb, criteria);

        return getCountNativeQueryMap(sb.toString(), params);
    }

    //Obtiene la tabla necesaria para crear el Excel que muestra el numero de becas de cada beca de cada unidad academica.
    public List<Object[]> reporteTotalBecas(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal becaid, BigDecimal movimiento) {
        StringBuilder sb = new StringBuilder();
        List<String> criteria = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        sb.append(" SELECT n.nombre, ua.NOMBRECORTO, tb.nombre, count(tb.nombre) ");
        sb.append(" from VW_OTORGAMIENTOS o ");
        sb.append(" join ent_alumno a on a.id = o.alumno_id ");
        sb.append(" join cat_genero g on g.id = a.genero_id  ");
        sb.append(" join ENT_PROCESO p on p.id = o.proceso_id ");
        sb.append(" join CAT_TIPO_PROCESO tp on tp.id = p.TIPOPROCESO_ID ");
        sb.append(" join CAT_MOVIMIENTO m on m.id = tp.MOVIMIENTO_ID ");
        sb.append(" JOIN CAT_UNIDAD_ACADEMICA ua on ua.id = p.unidadacademica_id ");
        sb.append(" join cat_nivel n on ua.nivel_id = n.id ");
        sb.append(" join ENT_TIPO_BECA_PERIODO tbp on tbp.id = o.tipobecaperiodo_id ");
        sb.append(" join CAT_TIPO_BECA tb on tb.id = tbp.TIPOBECA_ID ");
        sb.append(" join CAT_PROGRAMA_BECA b on b.id = tb.beca_id ");
        criteria.add("o.PERIODO_ID = #periodoId");
        params.put("periodoId", periodoId);
        if (nivelId != null && nivelId.intValue() != 0) {
            criteria.add("ua.NIVEL_ID = #nivelId");
            params.put("nivelId", nivelId);
        }
        if (unidadAcademicaId != null && unidadAcademicaId.intValue() != 0) {
            criteria.add("ua.ID = #unidadAcademicaId");
            params.put("unidadAcademicaId", unidadAcademicaId);
        }
        if (becaid != null && becaid.intValue() != 0) {
            criteria.add("b.id = #becaId");
            params.put("becaId", becaid);
        }
        if (movimiento != null && movimiento.intValue() != 0) {
            criteria.add("m.id = #movimiento");
            params.put("movimiento", movimiento);
        }

        agregaCriterios(sb, criteria);

        sb.append(" GROUP BY n.nombre, ua.id, ua.NOMBRECORTO, tb.nombre ");
        sb.append(" ORDER BY ua.ID");

        List<Object[]> lista = executeNativeQuery(sb.toString(), params, null);

        return lista.isEmpty() ? null : lista;
    }

    //Obtiene la tabla necesaria para crear el Excel que muestra el numero de becarios cada unidad academica.
    public List<Object[]> reporteTotalBecarios(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal movimiento) {
        StringBuilder sb = new StringBuilder();
        List<String> criteria = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        sb.append(" SELECT n.nombre, ua.NOMBRECORTO, count(distinct o.ALUMNO_ID) ");
        sb.append(" from VW_OTORGAMIENTOS o ");
        sb.append(" join ent_alumno a on a.id = o.alumno_id ");
        sb.append(" join cat_genero g on g.id = a.genero_id  ");
        sb.append(" join ENT_PROCESO p on p.id = o.proceso_id ");
        sb.append(" join CAT_TIPO_PROCESO tp on tp.id = p.TIPOPROCESO_ID ");
        sb.append(" join CAT_MOVIMIENTO m on m.id = tp.MOVIMIENTO_ID ");
        sb.append(" JOIN CAT_UNIDAD_ACADEMICA ua on ua.id = p.unidadacademica_id ");
        sb.append(" join cat_nivel n on ua.nivel_id = n.id ");
        criteria.add("o.PERIODO_ID = #periodoId");
        params.put("periodoId", periodoId);
        if (nivelId != null && nivelId.intValue() != 0) {
            criteria.add("ua.NIVEL_ID = #nivelId");
            params.put("nivelId", nivelId);
        }
        if (unidadAcademicaId != null && unidadAcademicaId.intValue() != 0) {
            criteria.add("ua.ID = #unidadAcademicaId");
            params.put("unidadAcademicaId", unidadAcademicaId);
        }
        if (movimiento != null && movimiento.intValue() != 0) {
            criteria.add("m.id = #movimiento");
            params.put("movimiento", movimiento);
        }

        agregaCriterios(sb, criteria);

        sb.append(" GROUP BY n.nombre, ua.id, ua.NOMBRECORTO ");
        sb.append(" ORDER BY ua.ID");

        List<Object[]> lista = executeNativeQuery(sb.toString(), params, null);

        return lista.isEmpty() ? null : lista;
    }
}
