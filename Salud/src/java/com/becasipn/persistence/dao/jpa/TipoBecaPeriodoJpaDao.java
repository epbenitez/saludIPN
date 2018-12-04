package com.becasipn.persistence.dao.jpa;

import com.becasipn.domain.ParametrosPDF;
import com.becasipn.persistence.dao.TipoBecaPeriodoDao;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.Beca;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.Nivel;
import com.becasipn.persistence.model.PadronSubes;
import com.becasipn.persistence.model.Proceso;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.SolicitudBeca;
import com.becasipn.persistence.model.TipoBeca;
import com.becasipn.persistence.model.TipoBecaPeriodo;
import com.becasipn.persistence.model.UnidadAcademica;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;

/**
 *
 * @author Usre-05
 */
public class TipoBecaPeriodoJpaDao extends JpaDaoBase<TipoBecaPeriodo, BigDecimal> implements TipoBecaPeriodoDao {

    public TipoBecaPeriodoJpaDao() {
        super(TipoBecaPeriodo.class);
    }

    @Override
    public List<TipoBecaPeriodo> tipoBecaPeriodoEnUso(BigDecimal tipoBeca) {
        String jpql = "SELECT o.id FROM Otorgamiento o WHERE o.tipoBecaPeriodo.id = ?1";
        List<TipoBecaPeriodo> lista = executeQuery(jpql, tipoBeca);
        return lista;
    }

    @Override
    public List<TipoBecaPeriodo> tipoBecaPeriodoEnUsoPresupuesto(BigDecimal tipoBeca) {
        String jpql = "SELECT p.id FROM PresupuestoTipoBecaPeriodo p WHERE p.tipoBecaPeriodo.id = ?1";
        List<TipoBecaPeriodo> lista = executeQuery(jpql, tipoBeca);
        return lista;
    }

    @Override
    public List<TipoBecaPeriodo> existenTiposBecaAsociados(BigDecimal periodoId) {
        String jpql = "select p from TipoBecaPeriodo p WHERE p.periodo.id = ?1";
        List<TipoBecaPeriodo> lista = executeQuery(jpql, periodoId);
        return lista;
    }

    //SQLINJECTION
    @Override
    public List<TipoBecaPeriodo> becasAplicables(Alumno alumno, SolicitudBeca solicitud, Boolean tieneUniversal, Boolean tieneComplementaria, Proceso... proceso) {
        BigDecimal periodoId = getDaos().getPeriodoDao().getPeriodoActivo().getId();
        DatosAcademicos datosAcademicos;
        if (alumno.getDatosAcademicos() == null) {
            datosAcademicos = getDaos().getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), periodoId);
            if (datosAcademicos == null) {
                datosAcademicos = getDaos().getDatosAcademicosDao().ultimosDatos(alumno.getId());
                if (datosAcademicos == null) {
                    datosAcademicos = new DatosAcademicos();
                }
            }
        } else {
            datosAcademicos = alumno.getDatosAcademicos();
        }

        BigDecimal areasConocimiento;
        BigDecimal nivelId;
        BigDecimal periodoDatosId;
        Integer regular;
        Double promedio;
        Integer semestre;
        Float ingresos;
        Integer inscrito;
        String consulta = "select p from TipoBecaPeriodo p"
                + " where p.periodo.id = ?1"
                + " and ((p.regular = 1 and p.regular = ?2) or (p.regular = 0))"
                + " and (?8 = 1)"
                + " and ?3 between p.promedioMinimo and p.promedioMaximo"
                + " and ?4 between p.semestreMinimo and p.semestreMaximo"
                + " and (p.ingresoSalarios is null or p.ingresoSalarios >= ?5)"
                + " and p.nivel.id = ?6"
                + " and p.tipoBeca.beca.clave != 'TM'"
                + " and p.tipoBeca.beca.clave != 'TI'"
                + " and (p.areasconocimiento is null or p.areasconocimiento.id = ?7 or ?7 = 4)"
                + " and p.estatus.id = 1";
        if (tieneUniversal && !tieneComplementaria) {
            consulta += " and p.visible = 0";
        } else {
            consulta += " and p.visible = 1 ";
        }

        if (proceso.length > 0) {
            consulta = consulta + " and p.tipoBeca.beca.id in (select pb.programaBeca.id from ProcesoProgramaBeca pb where pb.proceso.id = " + proceso[0].getId() + " )";
        }
        consulta = consulta + " order by p.prioridad";
        if (datosAcademicos.getUnidadAcademica() == null) {
            nivelId = new BigDecimal(0);
            areasConocimiento = new BigDecimal(0);
        } else {
            nivelId = datosAcademicos.getUnidadAcademica().getNivel().getId();
            if (datosAcademicos.getUnidadAcademica().getAreasConocimiento().getId().intValue() == 1
                    || datosAcademicos.getUnidadAcademica().getAreasConocimiento().getId().intValue() == 2) {
                areasConocimiento = new BigDecimal(5);
            } else {
                areasConocimiento = datosAcademicos.getUnidadAcademica().getAreasConocimiento().getId();
            }
        }
        periodoDatosId = datosAcademicos.getPeriodo() == null ? new BigDecimal(0) : datosAcademicos.getPeriodo().getId();
        regular = datosAcademicos.getRegular() == null ? 0 : datosAcademicos.getRegular();
        promedio = datosAcademicos.getPromedio() == null ? new Double(0) : datosAcademicos.getPromedio();
        semestre = datosAcademicos.getSemestre() == null ? 0 : datosAcademicos.getSemestre();
        ingresos = solicitud == null ? getDaos().getSolicitudBecaDao().getSolicitudesPorAlumno(alumno.getId(), periodoId).get(0).getIngresosPercapitaPesos() : solicitud.getIngresosPercapitaPesos();
        inscrito = datosAcademicos.getInscrito() == null ? 0 : datosAcademicos.getInscrito();
        List<TipoBecaPeriodo> lista = executeQuery(consulta,
                periodoDatosId,
                regular,
                promedio,
                semestre,
                ingresos,
                nivelId,
                areasConocimiento,
                inscrito);

        for (Iterator<TipoBecaPeriodo> it = lista.iterator(); it.hasNext();) {
            TipoBecaPeriodo tbp = it.next();
            if (tbp.getMonto().equals(BigDecimal.ZERO)) {
                it.remove();
            }
        }

        return lista;
    }

    /**
     * Devuelve los objetos tipo beca periodo, por periodo, nivel académico y
     * programa de beca.
     *
     * @author Mario Márquez
     * @param programaBeca
     * @param nivel
     * @param periodo
     * @return lista de objetos tipo beca periodo
     */
    @Override
    public List<TipoBecaPeriodo> becasPorProgramaNivelPeriodo(Beca programaBeca, Nivel nivel, Periodo periodo) {
        StringBuilder sb = new StringBuilder();
        List<String> criteria = new ArrayList<>();
        Map<String, Object> params = new HashMap();

        sb.append(" SELECT eB FROM TipoBecaPeriodo eB");
        sb.append(" INNER JOIN eB.tipoBeca tB");
        sb.append(" INNER JOIN tB.beca pB");

        criteria.add("eB.periodo = :periodo");
        params.put("periodo", periodo);
        criteria.add("eB.nivel = :nivel");
        params.put("nivel", nivel);
        criteria.add("pB = :programaBeca");
        params.put("programaBeca", programaBeca);
        criteria.add("eB.visible = 1");

        agregaCriterios(sb, criteria);

        sb.append(" ORDER BY tB.nombre ASC");

        List<TipoBecaPeriodo> lista = executeQuery(sb.toString(), params, null);

        return lista.isEmpty() ? null : lista;
    }

    @Override
    public TipoBecaPeriodo montoBecaAlumno(BigDecimal alumnoId, BigDecimal periodoId) {
        String sql = "select tbp.id, tbp.monto from ent_alumno a "
                + "inner join ent_otorgamientos o on a.id = o.alumno_id "
                + "inner join ent_tipo_beca_periodo tbp on tbp.id = o.tipobecaperiodo_id "
                + "where a.id = " + alumnoId + " and o.periodo_id = " + periodoId;
        List<Object[]> lista = executeNativeQuery(sql);
        List<TipoBecaPeriodo> ltbp = new ArrayList<>();
        for (Object[] res : lista) {
            TipoBecaPeriodo tbp = new TipoBecaPeriodo();
            tbp.setId((BigDecimal) res[0]);
            tbp.setMonto((BigDecimal) res[1]);
            ltbp.add(tbp);
        }
        return ltbp.get(0);
    }

    @Override
    public TipoBecaPeriodo nuevoTipoBecaPeriodo(BigDecimal tipoBecaAnterior,
            Periodo periodoActivo, Boolean tieneUniversal, Boolean tieneComplementaria,
            PadronSubes padronSubes) {
        if (periodoActivo == null) {
            periodoActivo = getDaos().getPeriodoDao().getPeriodoActivo();
        }
        String sql = "select p from TipoBecaPeriodo p where p.id=?1";
        BigDecimal tipoBecaId = executeQuery(sql, tipoBecaAnterior).get(0).getTipoBeca().getId();
        sql = "select p from TipoBecaPeriodo p where p.tipoBeca.id=?1"
                + " and p.periodo.id=?2";
        if (tieneUniversal && !tieneComplementaria) {
            sql += " and p.visible = 0";
        } else {
            sql += " and p.visible = 1 ";
        }
        sql += (padronSubes == null || padronSubes.getConvocatoriaSubes() == null || padronSubes.getConvocatoriaSubes().getId() == null ? "" : " and p.convocatoriaManutencion =  " + padronSubes.getConvocatoriaSubes().getId());

        List<TipoBecaPeriodo> res = executeQuery(sql, tipoBecaId,
                periodoActivo.getId());
        return res == null || res.isEmpty() ? null : res.get(0);
    }

    /**
     * Devuelve las becas activas para el periodo en curso en un nivel académico
     *
     * @author Victor Lozano
     * @param nivelId
     * @param periodoId
     * @return lista
     */
    @Override
    public List<TipoBecaPeriodo> becasPorNivelPeriodo(BigDecimal nivelId, BigDecimal periodoId) {
        BigDecimal activoId = getDaos().getPeriodoDao().getPeriodoActivo().getId();
        String consulta;
        List<TipoBecaPeriodo> lista;
        if (nivelId != null) {
            consulta = "select distinct(b) from TipoBecaPeriodo b join fetch b.tipoBeca where b.periodo.id = ?1"
                    + " and b.nivel.id = ?2 and b.visible in (0,1) "
                    + (activoId.equals(periodoId) ? " and b.estatus.id = 1" : " ")
                    + " order by b.tipoBeca.nombre";
            lista = executeQuery(consulta, periodoId, nivelId);
        } else {
            consulta = "select distinct(b) from TipoBecaPeriodo b join fetch b.tipoBeca where b.periodo.id = ?1 and b.visible in (0,1) "
                    + (activoId.equals(periodoId) ? " and b.estatus.id = 1" : " ")
                    + " order by b.tipoBeca.nombre";
            lista = executeQuery(consulta, periodoId);
        }
        return lista.isEmpty() ? null : lista;
    }

    /**
     * Devuelve las becas activas para el periodo en curso en un nivel académico
     * y en una Unidad Académica
     *
     * @author Augusto H.
     * @param nivelId
     * @param periodoId
     * @param uaId
     * @return lista
     */
    @Override
    public List<Beca> becasPorNivelPeriodoUa(BigDecimal nivelId, BigDecimal periodoId, BigDecimal uaId) {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT pb.id, pb.nombre, pb.clave, pb.clavedisp FROM CAT_PROGRAMA_BECA pb ");
        sb.append(" INNER JOIN RMM_PROCESO_PROGRAMA_BECA ppb ON ppb.PROGRAMABECA_ID= pb.id ");
        sb.append(" INNER JOIN ENT_PROCESO pr ON pr.id = ppb.PROCESO_ID ");
        sb.append(" INNER JOIN CAT_UNIDAD_ACADEMICA ua ON ua.id = pr.UNIDADACADEMICA_ID ");

        sb.append(" WHERE pr.periodo_id = " + periodoId + "");

        if (nivelId.intValue() != 0) {
            sb.append(" and ua.nivel_id = " + nivelId + " ");
        }

        if (uaId.intValue() != 0) {
            sb.append(" and ua.id = " + uaId + " ");
        }

        sb.append(" GROUP BY pb.id, pb.nombre, pb.clave, pb.clavedisp ");
        sb.append(" ORDER BY pb.id ");

        Query q = entityManager.createNativeQuery(sb.toString(), Beca.class);
        List<Beca> lista = q.getResultList();
        return lista.isEmpty() ? null : lista;
    }

    /**
     * Devuelve las becas correspondientes a la solicitudes de un alumno
     *
     * @author Mario Márquez
     * @param solicitudId
     * @return lista TipoBecaPeriodo
     */
    //SQLINJECTION OK
    @Override
    public List<TipoBecaPeriodo> getBecasPorSolicitud(BigDecimal solicitudId) {
        StringBuilder sb = new StringBuilder();

        sb.append(" SELECT eB FROM SolicitudBeca s");
        sb.append(" INNER JOIN s.programaBecaSolicitada pB ");
        sb.append(" INNER JOIN TipoBeca tB ON pB = tB.beca");
        sb.append(" INNER JOIN DatosAcademicos aD ON aD.alumno = s.alumno");
        sb.append(" AND aD.periodo = s.periodo");
        sb.append(" INNER JOIN aD.unidadAcademica uA");
        sb.append(" INNER JOIN TipoBecaPeriodo eB ON eB.tipoBeca = tB");
        sb.append(" AND eB.nivel = uA.nivel");
        sb.append(" AND eB.periodo = s.periodo");
        sb.append(" AND eB.visible = 1");
        sb.append(" WHERE s.id = ?1");
        sb.append(" ORDER BY tB.nombre ASC");

        List<TipoBecaPeriodo> lista = executeQuery(sb.toString(), solicitudId);

        return lista.isEmpty() ? null : lista;
    }

    /**
     * Devuelve la beca de transporte del periodo en curso
     *
     * @param tipo
     * @param a
     * @param clave
     * @param tieneUniversal
     * @param tieneComplementaria
     * @param proceso
     * @param padronSubes
     * @return
     */
    @Override
    public TipoBecaPeriodo findBecaTransporte(int tipo, Alumno a, String clave,
            Boolean tieneUniversal, Boolean tieneComplementaria, Proceso proceso,
            PadronSubes padronSubes) {
        TipoBecaPeriodo beca;
        if (clave == null) {
            clave = getDaos().getPeriodoDao().getPeriodoActivo().getClave();
        }

        String consulta = "select b from TipoBecaPeriodo b where b.periodo.id = ?1";
        if (tipo == 1) {
            consulta = consulta + " and b.tipoBeca.beca.clave = 'TM'";
            consulta += (padronSubes == null && padronSubes.getConvocatoriaSubes().getId() == null ? "" : " and b.convocatoriaManutencion =  " + padronSubes.getConvocatoriaSubes().getId());
            List<TipoBecaPeriodo> listaTransporteManutención = executeQuery(consulta, getDaos().getPeriodoDao().getPeriodoActivo().getId());
            beca = (listaTransporteManutención == null || listaTransporteManutención.isEmpty()) ? null : listaTransporteManutención.get(0);
        } else {
            consulta = consulta + " and b.tipoBeca.beca.clave = 'TI'";
            consulta += " and (select sum(tt.costo)*21.5 from TransporteTraslado tt"
                    + " where tt.cuestionarioTransporte.usuario.id = ?2";
            if (proceso != null && proceso.getTipoProceso().getMovimiento().getId().equals(new BigDecimal(3))) {//Nuevo
                consulta += " and tt.cuestionarioTransporte.periodo.id = ?1  and ?4 = ?4";
            } else {
                if (clave.charAt(clave.length() - 1) == '2') {
                    consulta += " and tt.cuestionarioTransporte.periodo.id = ?4";
                } else {
                    consulta += " and tt.cuestionarioTransporte.periodo.id = ?1  and ?4 = ?4";
                }
            }
            consulta += ") > b.gastotransporteminimo"
                    + " and b.nivel.id = ?3";
            if (tieneUniversal && !tieneComplementaria) {
                consulta += " and b.visible = 0";
            } else {
                consulta += " and b.visible = 1 ";
            }

            consulta += (padronSubes == null || padronSubes.getConvocatoriaSubes() == null ? "" : " and b.convocatoriaManutencion =  " + padronSubes.getConvocatoriaSubes().getId());

            consulta += " order by b.monto desc";
            BigDecimal periodoId = getDaos().getPeriodoDao().getPeriodoActivo().getId();
            DatosAcademicos datosAcademicos = getDaos().getDatosAcademicosDao().datosPorPeriodo(a.getId(), periodoId);
            List<TipoBecaPeriodo> listaTransporte;

            listaTransporte = executeQuery(consulta, periodoId,
                    a.getUsuario().getId(),
                    datosAcademicos.getUnidadAcademica().getNivel().getId(),
                    getDaos().getPeriodoDao().getPeriodoActivo().getPeriodoAnterior().getId());

            beca = listaTransporte == null || listaTransporte.isEmpty() ? null : listaTransporte.get(0);
        }
        //TipoBecaPeriodo beca = executeQuery(consulta,getDaos().getPeriodoDao().getPeriodoActivo().getId()).get(1);
        return beca;
    }

    @Override
    public TipoBecaPeriodo siguienteBecaPeriodo(TipoBecaPeriodo tipoBecaAnterior,
            Boolean tieneUniversal, Boolean tieneComplementaria, PadronSubes padronSubes) {
        TipoBeca tbNuevo = getDaos().getTipoBecaDao().siguienteTipoBeca(tipoBecaAnterior.getTipoBeca(), padronSubes);
        if (tbNuevo == null) {
            return null;
        }
        String jpql = "SELECT t FROM TipoBecaPeriodo t "
                + " WHERE t.tipoBeca.id=?1 and t.periodo=?2 ";
        if (tieneUniversal && !tieneComplementaria) {
            jpql += " and t.visible = 0";
        } else {
            jpql += " and t.visible = 1 ";
        }

        jpql += (padronSubes == null && padronSubes.getConvocatoriaSubes().getId() == null ? "" : " and t.convocatoriaManutencion =  " + padronSubes.getConvocatoriaSubes().getId());

        List<TipoBecaPeriodo> res = executeQuery(jpql, tbNuevo.getId(), getDaos().getPeriodoDao().getPeriodoActivo());
        return res == null || res.isEmpty() ? null : res.get(0);
    }

    @Override
    public TipoBecaPeriodo siguienteBecalosPeriodo(TipoBecaPeriodo tipoBecaAnterior, Alumno alumno, Boolean tieneUniversal, Boolean tieneComplementaria) {
        Beca beca = tipoBecaAnterior.getTipoBeca().getBeca();
        String jpql = "SELECT t FROM TipoBecaPeriodo t WHERE t.tipoBeca.beca.id=?1 "
                + " and t.nivel.id = ?5 "
                + " and t.periodo.estatus=1"
                + " and ?2 between t.promedioMinimo and t.promedioMaximo "
                + " and ?3 between t.semestreMinimo and t.semestreMaximo "
                + " and ?4 = t.regular"
                + " and t.tipoBeca.id in (18,19,20,21)";
        if (tieneUniversal && !tieneComplementaria) {
            jpql += " and t.visible = 0";
        } else {
            jpql += " and t.visible = 1 ";
        }
        BigDecimal periodoId = getDaos().getPeriodoDao().getPeriodoActivo().getId();
        DatosAcademicos datosAcademicos = getDaos().getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), periodoId);
        jpql += tipoBecaAnterior.getAreasconocimiento() == null ? "" : " and " + datosAcademicos.getUnidadAcademica().getAreasConocimiento().getId() + " = t.areasconocimiento.id ";
        jpql += tipoBecaAnterior.getModalidad() == null ? "" : " and " + datosAcademicos.getModalidad().getId() + " = t.modalidad.id ";
        List<TipoBecaPeriodo> posiblesBeca = executeQuery(jpql, beca.getId(),
                datosAcademicos.getPromedio(), datosAcademicos.getSemestre(), datosAcademicos.getRegular(), datosAcademicos.getUnidadAcademica().getNivel().getId());
        return posiblesBeca == null || posiblesBeca.isEmpty() ? null : posiblesBeca.get(0);
    }

    @Override
    public Long getCountTiposBecasPeriodoPorNivel(Integer nivel) {
        Periodo periodoActivo = getDaos().getPeriodoDao().getPeriodoActivo();
        String sql = "SELECT COUNT(*) from ent_tipo_beca_periodo e"
                + " WHERE e.periodo_id = ?1"
                + " AND E.NIVEL_ID = ?2";
        return getCountNativeQuery(sql, periodoActivo.getId(), nivel);

    }

    @Override
    public Long getCountTotalRealesPorBeca(TipoBecaPeriodo tipoBecaPeriodo, Integer nivel, BigDecimal periodoAnteriorId, BigDecimal periodoActual) {
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
                + "AND TA.ID=?4 "
                + "AND A.INSCRITO = 1 "
                + "AND A.ESTATUS = 1 AND A.INFOACTUALIZADAADMIN=1 "
                + "AND NOT EXISTS(SELECT 1 FROM ENT_OTORGAMIENTOS o2 where o.alumno_id = o2.alumno_id and o2.tipoBecaPeriodo_id=ta.id and o2.periodo_id=?2 ) "
                + "AND CUA.NIVEL_ID = ?3 ";
        //+ "AND C.BECA_ID NOT IN (7,8,9)";
        Long countNativeQuery = getCountNativeQuery(sql, periodoAnteriorId, periodoActual, nivel, tipoBecaPeriodo.getId());
        return countNativeQuery;
    }

    @Override
    public Long getCountTotalNuevosBeca(TipoBecaPeriodo tipoBecaCicloAnterior, Integer nivel,
            Periodo periodoActivo, UnidadAcademica ua) {
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
                + "AND A.unidadAcademica.nivel.id = ?3  ";
        return getCountQuery(jpql, periodoActivo.getId(), periodoActivo.getPeriodoAnterior().getId(), nivel);
    }

    @Override
    public TipoBecaPeriodo getBecaAlumnoActual(Alumno a, Beca b, BigDecimal periodoId,
            DatosAcademicos datosAcademicos, Boolean tieneUniversal, Boolean tieneComplementaria,
            PadronSubes padronSubes) {
        if (periodoId == null) {
            periodoId = getDaos().getPeriodoDao().getPeriodoActivo().getId();
        }
        if (datosAcademicos == null) {
            datosAcademicos = getDaos().getDatosAcademicosDao().datosPorPeriodo(a.getId(), periodoId);
            if (datosAcademicos == null) {
                datosAcademicos = getDaos().getDatosAcademicosDao().ultimosDatos(a.getId());
                if (datosAcademicos == null) {
                    datosAcademicos = new DatosAcademicos();
                }
            }
        }
        BigDecimal areasConocimiento;
        BigDecimal nivelId;
        Double promedio;
        Integer semestre;
        String consulta = "select p from TipoBecaPeriodo p"
                + " where p.periodo.id = ?1"
                + " and ?2 between p.promedioMinimo and p.promedioMaximo"
                + " and ?3 between p.semestreMinimo and p.semestreMaximo"
                + " and p.nivel.id = ?4"
                + " and p.tipoBeca.beca.id = ?5 "
                + " and (p.areasconocimiento is null or p.areasconocimiento.id = ?6 or ?6 = 4)"
                + " and p.estatus.id = 1";
        if (tieneUniversal && !tieneComplementaria) {
            consulta += " and p.visible = 0";
        } else {
            consulta += " and p.visible = 1 ";
        }
        consulta += (padronSubes == null || padronSubes.getConvocatoriaSubes().getId() == null ? "" : " and p.convocatoriaManutencion =  " + padronSubes.getConvocatoriaSubes().getId());

        if (datosAcademicos.getUnidadAcademica() == null) {
            nivelId = new BigDecimal(0);
            areasConocimiento = new BigDecimal(0);
        } else {
            nivelId = datosAcademicos.getUnidadAcademica().getNivel().getId();
            if (datosAcademicos.getUnidadAcademica().getAreasConocimiento().getId().intValue() == 1
                    || datosAcademicos.getUnidadAcademica().getAreasConocimiento().getId().intValue() == 2) {
                areasConocimiento = new BigDecimal(5);
            } else {
                areasConocimiento = datosAcademicos.getUnidadAcademica().getAreasConocimiento().getId();
            }
        }
        promedio = datosAcademicos.getPromedio() == null ? new Double(0) : datosAcademicos.getPromedio();
        semestre = datosAcademicos.getSemestre() == null ? 0 : datosAcademicos.getSemestre();
        List<TipoBecaPeriodo> res = executeQuery(consulta,
                periodoId,
                promedio,
                semestre,
                nivelId,
                b.getId(),
                areasConocimiento);
        return res == null || res.isEmpty() ? null : res.get(0);
    }

    @Override
    public TipoBecaPeriodo getBkUniversalPorNivel(Periodo periodo, DatosAcademicos datosAcademicos) {
        BigDecimal nivelId = datosAcademicos.getUnidadAcademica().getNivel().getId();
        BigDecimal periodoId = periodo.getId();
        StringBuilder consulta = new StringBuilder();
        consulta.append("SELECT tbp.* FROM CAT_TIPO_BECA tb")
                .append(" INNER JOIN ENT_TIPO_BECA_PERIODO tbp")
                .append("  ON tbp.TIPOBECA_ID = tb.ID")
                .append("  AND tbp.PERIODO_ID = ?1")
                .append("  AND tbp.NIVEL_ID = ?2")
                .append("  AND tbp.VISIBLE = 1")
                .append(" WHERE tb.BECA_ID = 10");

        Query q = entityManager.createNativeQuery(consulta.toString(), TipoBecaPeriodo.class);
        q.setParameter(1, periodoId);
        q.setParameter(2, nivelId);
        List<TipoBecaPeriodo> res = q.getResultList();

        return res == null || res.isEmpty() ? null : res.get(0);
    }

    @Override
    public List<ParametrosPDF> rangoIngresoPorPersonaPorBeca(BigDecimal periodoId, BigDecimal nivelId) {
        String sql = "select tb.beca_id, case when (min(ingresosalarios)) is null"
                + " then concat('Sin rango de ingresos por persona en la familia requerido.', min(ingresosalarios))"
                + " else concat('Rango de ingresos por persona en la familia requeridos: $0 - $', min(ingresosalarios)) end"
                + " from ent_tipo_beca_periodo tbp"
                + " inner join cat_tipo_beca tb on tb.id = tbp.tipobeca_id"
                + " where tbp.periodo_id = " + periodoId + " and nivel_id = " + nivelId
                + " group by tb.beca_id, tb.beca_id order by tb.beca_id";
        List<Object[]> lista = executeNativeQuery(sql);
        List<ParametrosPDF> lp = new ArrayList<>();
        for (Object[] res : lista) {
            ParametrosPDF p = new ParametrosPDF();
            p.setParametro1(res[0].toString());
            p.setParametro2(res[1].toString());
            lp.add(p);
        }
        return lp;
    }

    @Override
    public List<String> getBecasConValidacionInscripcion() {
        String jpql
                = "select tbp.tipoBeca.nombre from TipoBecaPeriodo tbp "
                + "where tbp.periodo.estatus = 1 "
                + "and tbp.validaciondeinscripcion > 0";
        return (List<String>) executeQueryObject(jpql);
    }

    @Override
    public TipoBecaPeriodo getComplementaria(BigDecimal tipoBecaPeriodoNormal) {
        String jpql = " select tbp from TipoBecaPeriodo tbp "
                + " join BecasComplementarias bc on bc.complementaria.id = tbp.id "
                + " where bc.normal.id = ?1 ";
        List<TipoBecaPeriodo> res = executeQuery(jpql, tipoBecaPeriodoNormal);

        return res == null || res.isEmpty() ? null : res.get(0);
    }

    /**
     * Encuentra el tipo de beca para el periodo señalado de acuerdo al tipo
     * beca periodo ingresado
     *
     * @param anterior
     * @param p
     * @return
     */
    @Override
    public TipoBecaPeriodo getTipoBecaPeriodo(TipoBecaPeriodo anterior, Periodo p) {
        String jpql = " select tbp from TipoBecaPeriodo tbp "
                + " where tbp.periodo.id = ?1 and tbp.tipoBeca.id = ?2 ";
        List<TipoBecaPeriodo> res = executeQuery(jpql, p.getId(), anterior.getTipoBeca().getId());

        return res == null || res.isEmpty() ? null : res.get(0);
    }

    @Override
    public List<Object[]> getTbpActualAnterior(Periodo actual) {
        String consulta = "select tbpO.id anterior, tbpA.id nueva from ent_tipo_beca_periodo tbpO "
                + "inner join ent_tipo_beca_periodo tbpA on tbpA.TIPOBECA_ID = tbpO.TIPOBECA_ID and tbpA.periodo_id = " + actual.getId() + " "
                + "where tbpO.id in"
                + "(select  distinct(sb.tipobecapreasignada_id) "
                + "from ent_alumno a "
                + "join ent_otorgamientos o on o.ALUMNO_ID = a.id "
                + "join ent_tipo_beca_periodo tbpAn on tbpAn.id = o.TIPOBECAPERIODO_ID "
                + "join ent_solicitud_becas sb on sb.id = o.SOLICITUDBECA_ID "
                + "left join ENT_ALUMNO_DATOS_ACADEMICOS da on da.ALUMNO_ID = a.id and da.periodo_id = " + actual.getId() + " "
                + "where o.alta=1 and o.periodo_id = " + actual.getPeriodoAnterior().getId() + " and o.proceso_id is not null "
                + "and da.id is not null "
                + "and tbpAn.TIPOBECA_ID IN "
                + "(SELECT tbp.TIPOBECA_ID FROM ent_tipo_beca_periodo tbp "
                + "WHERE tbp.periodo_id = " + actual.getId() + " and tbp.validaciondeinscripcion > 0))";
        List<Object[]> lista = executeNativeQuery(consulta);
        return lista;
    }

    @Override
    public TipoBecaPeriodo becaAlumnoPeriodoActual(Periodo actual, BigDecimal alumnoId) {
        String consulta = "select * from ENT_TIPO_BECA_PERIODO tbp\n"
                + " inner join ENT_OTORGAMIENTOS o on o.TIPOBECAPERIODO_ID = tbp.id\n"
                + " where o.ALUMNO_ID = " + alumnoId + " and o.PERIODO_ID = " + actual.getPeriodoAnterior().getId() + " ";
        Query q = entityManager.createNativeQuery(consulta, TipoBecaPeriodo.class);
        List<TipoBecaPeriodo> lista = q.getResultList();
        return lista.isEmpty() ? null : lista.get(0);
    }
}
