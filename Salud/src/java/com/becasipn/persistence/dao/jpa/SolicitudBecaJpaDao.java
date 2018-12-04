package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.SolicitudBecaDao;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.AreasConocimiento;
import com.becasipn.persistence.model.Beca;
import com.becasipn.persistence.model.Carrera;
import com.becasipn.persistence.model.ClasificacionSolicitud;
import com.becasipn.persistence.model.Cuestionario;
import com.becasipn.persistence.model.CuestionarioTransporte;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.Modalidad;
import com.becasipn.persistence.model.Nivel;
import com.becasipn.persistence.model.Otorgamiento;
import com.becasipn.persistence.model.Parentesco;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.SolicitudBeca;
import com.becasipn.persistence.model.TipoBecaPeriodo;
import com.becasipn.persistence.model.Transporte;
import com.becasipn.persistence.model.TransporteDatosFamiliares;
import com.becasipn.persistence.model.TransporteTraslado;
import com.becasipn.persistence.model.Trayecto;
import com.becasipn.persistence.model.UnidadAcademica;
import com.becasipn.persistence.model.Usuario;
import com.opensymphony.xwork2.ActionContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Tania G. Sánchez
 */
public class SolicitudBecaJpaDao extends JpaDaoBase<SolicitudBeca, BigDecimal> implements SolicitudBecaDao {

    public SolicitudBecaJpaDao() {
        super(SolicitudBeca.class);
    }

    @Override
    public Boolean exiteESEPeriodoActivo(BigDecimal alumnoId, BigDecimal cuestionario, BigDecimal periodoId) {
        String consulta = "select * from ent_solicitud_becas"
                + " where alumno_id = ?1 and cuestionario_id = ?2"
                + " and periodo_id = ?3 and finalizado = 1";
        List<Object[]> lista = executeNativeQuery(consulta, alumnoId, cuestionario, periodoId);
        return lista != null && !lista.isEmpty();
    }

    @Override
    public SolicitudBeca getESEAlumno(BigDecimal alumnoId, BigDecimal periodoId) {
        String jpql = "select c from SolicitudBeca c where c.alumno.id = ?1 and c.periodo.id = ?2 and c.cuestionario.id = 1";
        List<SolicitudBeca> lista = executeQuery(jpql, alumnoId, periodoId);
        return lista == null || lista.isEmpty() ? null : lista.get(0);
    }

    @Override
    public Boolean tieneESECompleto(BigDecimal alumnoId, BigDecimal periodoId) {
        String jpql = "select c from SolicitudBeca c where c.alumno.id = ?1 and c.periodo.id = ?2 and c.cuestionario.id = 1 and c.finalizado = 1";
        List<SolicitudBeca> lista = executeQuery(jpql, alumnoId, periodoId);
        return lista != null && !lista.isEmpty();
    }

    @Override
    public Boolean tieneSolicitudTransporteAlumno(BigDecimal alumnoId, BigDecimal periodoId) {
        String jpql = "select c from SolicitudBeca c where c.alumno.id = ?1 and c.periodo.id = ?2 and c.cuestionario.id = 4";
        List<SolicitudBeca> lista = executeQuery(jpql, alumnoId, periodoId);
        return lista != null && !lista.isEmpty();
    }

    @Override
    public BigDecimal totalAlumnosCuestionarioCompleto(BigDecimal unidadAcademicaId, BigDecimal cuestionarioId) {
        String sql = "select count(cpa.id),1 from ENT_CENSO_SALUD cpa "
                + "inner join ent_solicitud_becas sb on sb.ALUMNO_ID = cpa.ALUMNO_ID and sb.PERIODO_ID = cpa.PERIODO_ID and sb.CUESTIONARIO_ID = 1 and sb.FINALIZADO = 1 "
                + "inner join ENT_ALUMNO_DATOS_ACADEMICOS a on cpa.ALUMNO_ID = a.ALUMNO_ID and a.PERIODO_ID = cpa.PERIODO_ID "
                + "where cpa.periodo_id = (select p.id from cat_periodo p where p.estatus=1) "
                + "and cpa.CUESTIONARIO_ID=?1 ";

        sql += unidadAcademicaId == null ? "" : " and a.UNIDADACADEMICA_ID = " + unidadAcademicaId;

        List<Object[]> lista = executeNativeQuery(sql, cuestionarioId);
        BigDecimal total = new BigDecimal(0);
        if (lista != null && !lista.isEmpty()) {
            for (Object[] res : lista) {
                total = res[0] == null ? new BigDecimal(0) : ((BigDecimal) res[0]);
            }
        }
        return total;
    }

    @Override
    public String totalAlumnosCuestionarioCompletoD(BigDecimal nivelId, BigDecimal cuestionarioId) {
        String consulta = "select ua.id, ua.nombrecorto, contador  from CAT_UNIDAD_ACADEMICA ua "
                + "left join (select ua.ID, ua.NOMBRECORTO, count(cpa.id) as contador from ENT_CENSO_SALUD cpa "
                + "inner join ent_solicitud_becas sb on sb.ALUMNO_ID = cpa.ALUMNO_ID and sb.PERIODO_ID = cpa.PERIODO_ID and sb.CUESTIONARIO_ID = 1 and sb.FINALIZADO = 1 "
                + "inner join ENT_ALUMNO_DATOS_ACADEMICOS a on cpa.ALUMNO_ID = a.ALUMNO_ID and a.PERIODO_ID = cpa.PERIODO_ID "
                + "inner join CAT_UNIDAD_ACADEMICA ua on ua.id = a.UNIDADACADEMICA_ID "
                + "where cpa.periodo_id = (select p.id from cat_periodo p where p.estatus=1) "
                + "and ua.NIVEL_ID = ?1 "
                + "and cpa.CUESTIONARIO_ID=?2 "
                + "GROUP BY ua.ID, ua.NOMBRECORTO) datos on ua.id = datos.ID where ua.NIVEL_ID = ?1 "
                + "ORDER BY ua.ID";
        List<Object[]> ax = executeNativeQuery(consulta, nivelId, cuestionarioId);
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

    public List<Object[]> totalAlumnosCuestionarioCompletoD(BigDecimal cuestionarioId) {
        StringBuilder sb = new StringBuilder();
        List<String> columnas = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        List<String> criteria = new ArrayList<>();
        StringBuilder sbSub = new StringBuilder();
        StringBuilder sbWhere = new StringBuilder();

        //columnas.add("ua.ID");
        columnas.add("ua.nombrecorto \"UNIDAD ACADÉMICA\"");
        columnas.add("count(cpa.id) \"NUMERO ALUMNOS\"");

        sb.append(" SELECT ");
        agregaColumnas(sb, columnas);

        sb.append(" from ent_censo_salud cpa ");

        sb.append(" inner join ent_solicitud_becas sb ");
        sb.append(" ON sb.ALUMNO_ID = cpa.ALUMNO_ID and sb.PERIODO_ID = cpa.PERIODO_ID and sb.CUESTIONARIO_ID = 1 and sb.FINALIZADO = 1 ");
        sb.append(" inner join ENT_ALUMNO_DATOS_ACADEMICOS a ");
        sb.append(" ON cpa.ALUMNO_ID = a.ALUMNO_ID and a.PERIODO_ID = cpa.PERIODO_ID ");
        sb.append(" inner join CAT_UNIDAD_ACADEMICA ua ");
        sb.append("  ON ua.id = a.UNIDADACADEMICA_ID ");

        sbSub.append(" (select p.id from cat_periodo p where p.estatus=1) ");
        sbWhere.append(" cpa.periodo_id = ");
        sbWhere.append(sbSub);

        criteria.add(sbWhere.toString());

        if (cuestionarioId != null) {
            criteria.add("cpa.CUESTIONARIO_ID= #cuestionario_id");
            params.put("cuestionario_id", cuestionarioId);
        }

        if (isFuncionario() || isResponsable()) {
            Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
            BigDecimal ua = getDaos().getPersonalAdministrativoDao().findByUsuario(usuario.getId()).getUnidadAcademica().getId();

            if (ua == null) {
                sb.append("");
            } else {
                criteria.add("a.UNIDADACADEMICA_ID = #ua_id");
                params.put("ua_id", ua);
            }
        }

        agregaCriterios(sb, criteria);

        sb.append(" GROUP BY ua.ID, ua.NOMBRECORTO ");
        sb.append(" ORDER BY ua.ID ");

        //List<LinkedHashMap<String, Object>> result = executeNativeQuery(sb.toString(), params, columnas, null);
        List<Object[]> result = executeNativeQuery(sb.toString(), params, null);

        return result;

    }

    @Override
    public Boolean contestoEncuestaSalud(BigDecimal alumnoId, BigDecimal periodoId) {
        String sql = "select * from ent_censo_salud where cuestionario_id = 3 and periodo_id = ?1 and alumno_id = ?2";
        List<Object[]> lista = executeNativeQuery(sql, periodoId, alumnoId);
        return lista != null && !lista.isEmpty();
    }

    @Override
    public void eliminarFinalizarESEporAlumno(BigDecimal cuestionarioId, BigDecimal periodoId, BigDecimal alumnoId) {
        String jpql = "delete from SolicitudBeca where cuestionario.id = ?1 and periodo.id = ?2 and alumno.id = ?3";
        Query query = getEntityManager().createQuery(jpql);
        query.setParameter(1, cuestionarioId);
        query.setParameter(2, periodoId);
        query.setParameter(3, alumnoId);
        query.executeUpdate();
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

    @Override
    public int getCountCandidatosBecaSolicitada(Periodo periodoActivo, Integer nivel) {
        String sql = "SELECT COUNT(*) "
                + "from ENT_SOLICITUD_BECAS sb "
                + "join ENT_ALUMNO ea on ea.id = sb.ALUMNO_ID "
                + "join ENT_ALUMNO_DATOS_ACADEMICOS a on ea.id = a.ALUMNO_ID and a.PERIODO_ID = sb.PERIODO_ID " + "join CAT_UNIDAD_ACADEMICA ua ON ua.ID = a.UNIDADACADEMICA_ID "
                + "join CAT_PROGRAMA_BECA b on b.id = sb.BECASOLICITADA_ID "
                + "join CAT_TIPO_BECA tb on b.id = tb.BECA_ID "
                + "join ENT_TIPO_BECA_PERIODO tbp on tbp.TIPOBECA_ID = tb.id and tbp.PERIODO_ID = sb.PERIODO_ID "
                + "where sb.CLASIFICACIONSOLICITUD_ID is NULL "
                + "and sb.BECASOLICITADA_ID is not NULL "
                + "and sb.TIPOBECAPREASIGNADA_ID is NULL "
                + "and sb.PERIODO_ID =  ?1 "
                + "and ua.NIVEL_ID = ?2 "
                + "and a.PROMEDIO between tbp.PROMEDIOMINIMO and tbp.PROMEDIOMAXIMO "
                + "and a.SEMESTRE between tbp.SEMESTREMINIMO and tbp.SEMESTREMAXIMO "
                + "and a.REGULAR = tbp.REGULAR ";
        Long countNativeQuery = getCountNativeQuery(sql, periodoActivo.getId(), nivel);
        return countNativeQuery.intValue();
    }

    @Override
    public Long getCountCandidatosPorBecaSolicitada(BigDecimal unidadID, TipoBecaPeriodo tipoBecaPeriodo, Integer nivel, BigDecimal periodoActual) {
        String sql = "SELECT COUNT(*) "
                + "from ENT_SOLICITUD_BECAS sb "
                + "join ENT_ALUMNO ea on ea.id = sb.ALUMNO_ID "
                + "join ENT_ALUMNO_DATOS_ACADEMICOS a on ea.id = a.ALUMNO_ID and a.PERIODO_ID = sb.PERIODO_ID "
                + "join CAT_UNIDAD_ACADEMICA ua ON ua.ID = a.UNIDADACADEMICA_ID "
                + "join CAT_PROGRAMA_BECA b on b.id = sb.BECASOLICITADA_ID "
                + "join CAT_TIPO_BECA tb on b.id = tb.BECA_ID "
                + "join ENT_TIPO_BECA_PERIODO tbp on tbp.TIPOBECA_ID = tb.id and tbp.PERIODO_ID = sb.PERIODO_ID "
                + "where sb.CLASIFICACIONSOLICITUD_ID is NULL "
                + "and sb.BECASOLICITADA_ID is not NULL "
                + "and sb.TIPOBECAPREASIGNADA_ID is NULL "
                + "and sb.PERIODO_ID =  ?1 "
                + "and ua.NIVEL_ID = ?2 "
                + "and ua.id = ?3 "
                + "and tbp.id = ?4 "
                + "and a.PROMEDIO between tbp.PROMEDIOMINIMO and tbp.PROMEDIOMAXIMO "
                + "and a.SEMESTRE between tbp.SEMESTREMINIMO and tbp.SEMESTREMAXIMO "
                + "and a.REGULAR = tbp.REGULAR ";
        Long countNativeQuery = getCountNativeQuery(sql, periodoActual, nivel, unidadID, tipoBecaPeriodo.getId());
        return countNativeQuery;
    }

    @Override
    public List<SolicitudBeca> getCandidatosBecaSolicitada(BigDecimal unidadID, TipoBecaPeriodo tipoBecaPeriodo, Integer nivel, BigDecimal periodoActual) {
        String sql = "SELECT sb.* "
                + "from ENT_SOLICITUD_BECAS sb "
                + "join ENT_ALUMNO ea on ea.id = sb.ALUMNO_ID "
                + "join ENT_ALUMNO_DATOS_ACADEMICOS a on ea.id = a.ALUMNO_ID and a.PERIODO_ID = sb.PERIODO_ID "
                + "join CAT_UNIDAD_ACADEMICA ua ON ua.ID = a.UNIDADACADEMICA_ID "
                + "join CAT_PROGRAMA_BECA b on b.id = sb.BECASOLICITADA_ID "
                + "join CAT_TIPO_BECA tb on b.id = tb.BECA_ID "
                + "join ENT_TIPO_BECA_PERIODO tbp on tbp.TIPOBECA_ID = tb.id and tbp.PERIODO_ID = sb.PERIODO_ID "
                + "where sb.CLASIFICACIONSOLICITUD_ID is NULL "
                + "and sb.BECASOLICITADA_ID is not NULL "
                + "and sb.TIPOBECAPREASIGNADA_ID is NULL "
                + "and sb.PERIODO_ID =  ?1 "
                + "and ua.NIVEL_ID = ?2 "
                + "and ua.id = ?3 "
                + "and tbp.id = ?4 "
                + "and a.PROMEDIO between tbp.PROMEDIOMINIMO and tbp.PROMEDIOMAXIMO "
                + "and a.SEMESTRE between tbp.SEMESTREMINIMO and tbp.SEMESTREMAXIMO "
                + "and a.REGULAR = tbp.REGULAR "
                + "order by a.PROMEDIO desc, ea.APELLIDOPATERNO, ea.APELLIDOMATERNO ";
        Query q = entityManager.createNativeQuery(sql, SolicitudBeca.class);
        q.setParameter(1, periodoActual);
        q.setParameter(2, nivel);
        q.setParameter(3, unidadID);
        q.setParameter(4, tipoBecaPeriodo.getId());
        List<SolicitudBeca> lista = q.getResultList();
        return lista;
    }

    @Override
    public BigDecimal totalAlumnosEstatusSolicitud(BigDecimal unidadAcademicaId, BigDecimal estatusSolicitud, Boolean manutencion) {
        BigDecimal periodoId = getDaos().getPeriodoDao().getPeriodoActivo().getId();
        String jpql = "select count(sb) from SolicitudBeca sb "
                + "join DatosAcademicos da on da.alumno.id = sb.alumno.id and da.periodo.id = sb.periodo.id "
                + "where sb.periodo.id=?1 "
                + "and sb.cuestionario.id in (1,2,4,5) and sb.finalizado=1 ";
        jpql += unidadAcademicaId == null || unidadAcademicaId.equals(BigDecimal.ZERO) ? "" : " and da.unidadAcademica.id = ?2 ";
        jpql += estatusSolicitud == null || estatusSolicitud.equals(BigDecimal.ZERO) ? "and sb.clasificacionSolicitud is null" : " and sb.clasificacionSolicitud.id = " + estatusSolicitud;
        jpql += manutencion == null ? "" : manutencion ? " and sb.programaBecaSolicitada.id = 5" : " and sb.programaBecaSolicitada.id != 5";
        BigDecimal result = new BigDecimal(unidadAcademicaId == null || unidadAcademicaId.equals(BigDecimal.ZERO) ? getCountQuery(jpql, periodoId) : getCountQuery(jpql, periodoId, unidadAcademicaId));
        return result == null ? BigDecimal.ZERO : result;
    }

    @Override
    public BigDecimal totalAlumnosEstatusPendiente(BigDecimal unidadAcademicaId, BigDecimal estatusSolicitud) {
        BigDecimal periodoId = getDaos().getPeriodoDao().getPeriodoActivo().getId();
        String jpql = "select count(sb) from SolicitudBeca sb "
                + "join DatosAcademicos da on da.alumno.id = sb.alumno.id and da.periodo.id = sb.periodo.id "
                + "where sb.periodo.id= ?1 "
                + "and sb.cuestionario.id in (1,2,4,5) "
                + "and sb.finalizado = 1 ";
        jpql += unidadAcademicaId == null || unidadAcademicaId.equals(BigDecimal.ZERO) ? "" : " and da.unidadAcademica.id = ?2 ";
        jpql += estatusSolicitud == null || estatusSolicitud.equals(BigDecimal.ZERO) ? "and sb.clasificacionSolicitud is null" : " and sb.clasificacionSolicitud.id = " + estatusSolicitud;
        BigDecimal result = new BigDecimal(unidadAcademicaId == null || unidadAcademicaId.equals(BigDecimal.ZERO) ? getCountQuery(jpql, periodoId) : getCountQuery(jpql, periodoId, unidadAcademicaId));
        return result == null ? BigDecimal.ZERO : result;
    }

    @Override
    public BigDecimal totalAlumnosConSolicitud(BigDecimal unidadAcademicaId, BigDecimal tipoSolicitud) {
        String jpql = "select count(sb.id) "
                + "from ENT_SOLICITUD_BECAS sb "
                + "inner join ENT_ALUMNO_DATOS_ACADEMICOS da on da.ALUMNO_ID = sb.ALUMNO_ID and da.PERIODO_ID = sb.PERIODO_ID "
                + "where sb.PERIODO_ID = (SELECT ID from cat_periodo p where p.estatus = 1 and rownum = 1)"
                + "and sb.finalizado=1 ";
        jpql += unidadAcademicaId == null || unidadAcademicaId.equals(BigDecimal.ZERO) ? "" : " and da.unidadAcademica_id = ?1 ";
        jpql += tipoSolicitud == null || tipoSolicitud.equals(BigDecimal.ZERO) ? "and sb.cuestionario_id in (1,2,4,5) " : tipoSolicitud.intValue() == 1 ? "and sb.cuestionario_id in (1,5) " : "and sb.cuestionario_id in (2,4) ";
        BigDecimal result = new BigDecimal(unidadAcademicaId == null || unidadAcademicaId.equals(BigDecimal.ZERO) ? getCountNativeQuery(jpql) : getCountNativeQuery(jpql, unidadAcademicaId));
        return result == null ? BigDecimal.ZERO : result;
    }

    //SQLINJECTION
    @Override
    public String totalAlumnosEstatusSolicitudD(BigDecimal nivelId, BigDecimal estatusSolicitud) {
        String consulta = "select ua.id, ua.nombrecorto, contador  from CAT_UNIDAD_ACADEMICA ua "
                + "left join (select ua.ID, ua.NOMBRECORTO, count(sb.id) as contador "
                + "from ENT_SOLICITUD_BECAS sb "
                + "inner join ENT_ALUMNO_DATOS_ACADEMICOS da on da.ALUMNO_ID = sb.ALUMNO_ID and da.PERIODO_ID = sb.PERIODO_ID "
                + "inner join CAT_UNIDAD_ACADEMICA ua on da.UNIDADACADEMICA_ID = ua.id "
                + "where sb.PERIODO_ID = (SELECT ID from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "and ua.NIVEL_ID = ?1 "
                + "and sb.CUESTIONARIO_ID in (1,2,4,5) "
                + "and sb.finalizado =  1 ";
        consulta += estatusSolicitud == null ? "and sb.CLASIFICACIONSOLICITUD_ID is null" : " and sb.CLASIFICACIONSOLICITUD_ID = " + estatusSolicitud;
        consulta += " GROUP BY ua.ID, ua.NOMBRECORTO) datos on ua.id = datos.ID where ua.NIVEL_ID = ?1 ORDER BY ua.ID";
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
    public String totalAlumnosBecaPendienteManutencion(BigDecimal nivelId) {
        String consulta = "SELECT ua.ID, ua.NOMBRECORTO, count(sb.id)\n"
                + "from ENT_SOLICITUD_BECAS sb\n"
                + "inner join ENT_ALUMNO_DATOS_ACADEMICOS da on da.ALUMNO_ID = sb.ALUMNO_ID and da.PERIODO_ID = sb.PERIODO_ID\n"
                + "inner join CAT_UNIDAD_ACADEMICA ua on da.UNIDADACADEMICA_ID = ua.id\n"
                + "where sb.PERIODO_ID = (SELECT ID from cat_periodo p where p.estatus = 1 and rownum = 1)\n"
                + "and ua.NIVEL_ID = ?1 \n"
                + "and sb.CUESTIONARIO_ID in (1,2,4,5)\n"
                + "and SB.FINALIZADO = 1\n"
                + "and sb.CLASIFICACIONSOLICITUD_ID is null\n"
                + "and SB.PROGRAMABECASOLICITADA_ID = 5\n"
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
    public String totalAlumnosBecaPendienteDiferenteManutencion(BigDecimal nivelId) {
        String consulta = "SELECT ua.ID, ua.NOMBRECORTO, count(sb.id)\n"
                + "from ENT_SOLICITUD_BECAS sb\n"
                + "inner join ENT_ALUMNO_DATOS_ACADEMICOS da on da.ALUMNO_ID = sb.ALUMNO_ID and da.PERIODO_ID = sb.PERIODO_ID\n"
                + "inner join CAT_UNIDAD_ACADEMICA ua on da.UNIDADACADEMICA_ID = ua.id\n"
                + "where sb.PERIODO_ID = (SELECT ID from cat_periodo p where p.estatus = 1 and rownum = 1)\n"
                + "and ua.NIVEL_ID = ?1 \n"
                + "and sb.CUESTIONARIO_ID in (1,2,4,5)\n"
                + "and SB.FINALIZADO = 1\n"
                + "and sb.CLASIFICACIONSOLICITUD_ID is null\n"
                + "and SB.PROGRAMABECASOLICITADA_ID != 5\n"
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

    //SQLINJECTION
    public String totalAlumnosConSolicitudD(BigDecimal nivelId, BigDecimal tipoSolicitud) {
        String consulta = "select ua.id, ua.nombrecorto, contador, periodo_id from CAT_UNIDAD_ACADEMICA ua "
                + "left join (select ua.ID, count(sb.id) as contador, sb.periodo_id as periodo_id "
                + "from ENT_SOLICITUD_BECAS sb "
                + "inner join ENT_ALUMNO_DATOS_ACADEMICOS da on da.ALUMNO_ID = sb.ALUMNO_ID and da.PERIODO_ID = sb.PERIODO_ID "
                + "inner join CAT_UNIDAD_ACADEMICA ua on da.UNIDADACADEMICA_ID = ua.id "
                + "where sb.PERIODO_ID = (SELECT ID from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "and sb.finalizado = 1 "
                + "and ua.NIVEL_ID = ?1 ";
        consulta += tipoSolicitud == null ? "and sb.CUESTIONARIO_ID in (1,2,4,5) " : " and sb.CUESTIONARIO_ID = " + tipoSolicitud;
        consulta += " GROUP BY ua.ID, ua.NOMBRECORTO, sb.periodo_id) datos on ua.id = datos.ID where ua.NIVEL_ID = ?1 ORDER BY ua.ID";
        List<Object[]> ax = executeNativeQuery(consulta, nivelId);
        List<Object[]> xx;
        String s = "";
        for (Object[] res : ax) {
            s = s + res[1] + "," + res[2];

            /*Se hace la consulta para obtener los tipos de beca solicitada por unidad académica para el periodo actual
              estadisticaSolicitadasData(BigDecimal periodoId, BigDecimal uaId, BigDecimal nivelId, String fechaInicial, String fechaFinal)
             */
            xx = estadisticaSolicitadasData((BigDecimal) res[3], (BigDecimal) res[0], nivelId, null, null);
            String bs = "";
            for (Object[] pbs : xx) {
                bs = bs + pbs[3] + ":" + pbs[1] + " - ";
            }
            s = s + "," + bs + "TOTAL: " + res[2];
            s = s + "/";
        }
        if (s.length() > 1) {
            return s;
        } else {
            return null;
        }
    }

    @Override
    public List<Object[]> alumnosEstatusSolicitud(BigDecimal unidadAcademicaId, BigDecimal estatusSolicitud, BigDecimal periodoId) {
        String consulta = "select a.BOLETA, ua.nombrecorto, mr.nombre, l.ordenamiento, tb.nombre as tipobeca, b.nombre as solicitada, "
                + "case \n"
                + "when TRUNC( MONTHS_BETWEEN (SYSDATE,getFechaNacimiento(a.CURP) ) /12 ) between 13 and 17 then 'Adolescente'\n"
                + "when TRUNC( MONTHS_BETWEEN (SYSDATE,getFechaNacimiento(a.CURP) ) /12 ) between 18 and 29 then 'Jóven'\n"
                + "when TRUNC( MONTHS_BETWEEN (SYSDATE,getFechaNacimiento(a.CURP) ) /12 ) between 30 and 64 then 'Adulto'\n"
                + "when TRUNC( MONTHS_BETWEEN (SYSDATE,getFechaNacimiento(a.CURP) ) /12 ) >= 65 then 'Adulto mayor'\n"
                + "end as \"Grupo de Edad\", "
                + "edo.nombre as estado, muni.nombre as municipio, loc.nombre as localidad "
                + "from ENT_SOLICITUD_BECAS sb "
                + "inner join ent_alumno a on a.id = sb.alumno_id "
                + "inner join ENT_DIRECCION dir on dir.ID = a.DIRECCION_ID "
                + "inner join RMM_ESTADO_DELEG_COL edc on edc.ID = dir.RELACIONGEOGRAFICA_ID "
                + "left join CAT_ESTADO edo on edo.ID = edc.ESTADO_ID "
                + "left join CAT_DELEGACION_MUNICIPIO muni on muni.ID = edc.MUNICIPIO_ID "
                + "left join CAT_LOCALIDAD_COLONIA loc on loc.ID = edc.COLONIA_ID "
                + "inner join ENT_ALUMNO_DATOS_ACADEMICOS da on da.alumno_id = sb.alumno_id and da.periodo_id = sb.periodo_id "
                + "inner join CAT_UNIDAD_ACADEMICA ua on ua.id = da.unidadacademica_id "
                + "left join ent_lista_espera l on l.solicitudbeca_id = sb.id and l.vigente = 1 and rownum = 1  "
                + "left join cat_motivo_rechazo_solicitud mr on mr.id = sb.motivorechazo_id "
                + "left join vw_otorgamientos o on o.solicitudbeca_id = sb.id and o.periodo_id = sb.periodo_id "
                + "left join ent_tipo_beca_periodo tbp on tbp.id = o.tipobecaperiodo_id "
                + "left join cat_tipo_beca tb on tb.id = tbp.tipobeca_id "
                + "left join cat_programa_beca b on b.id = sb.PROGRAMABECASOLICITADA_ID "
                + "where sb.periodo_id=?1 "
                //				+ "and ua.id NOT IN (32)" // No se muestra EST a solicitud de Luis
                + "and sb.finalizado=1 ";
        consulta += unidadAcademicaId == null || unidadAcademicaId.equals(BigDecimal.ZERO) ? "" : " and da.unidadAcademica_id = ?2";
        consulta += estatusSolicitud == null || estatusSolicitud.equals(BigDecimal.ZERO) ? " and sb.clasificacionSolicitud_id is null" : " and sb.clasificacionSolicitud_id = " + estatusSolicitud;
        consulta += " order by da.unidadAcademica_id, da.turno, l.ordenamiento, a.boleta";
        Query q = entityManager.createNativeQuery(consulta);
        q.setParameter(1, periodoId);
        if (unidadAcademicaId != null) {
            q.setParameter(2, unidadAcademicaId);
        }
        List<Object[]> lista = q.getResultList();
        return lista == null || lista.isEmpty() ? null : lista;
    }

    @Override
    public List<Object[]> alumnosEstatusSolicitudPendiente(BigDecimal unidadAcademicaId, BigDecimal periodoId) {
        String consulta = "SELECT ua.nombrecorto, \n"
                + "       a.boleta, \n"
                + "       b.nombre, \n"
                + "       sb.ingresospercapitapesos, \n"
                + "       da.promedio, \n"
                + "       da.semestre, \n"
                + "       ox.id, \n"
                + "       m.nombre AS modalidad, \n"
                + "       da.regular, \n"
                + "       da.cumplecargaminima, \n"
                + "       c.carrera, \n"
                + "       pp.id, \n"
                + "       dlm.conhambre, \n"
                + "       a.entidaddenacimiento_id, \n"
                + "       a.usuario_id,  \n"
                + "       cs.nombre \n"
                + "from ENT_SOLICITUD_BECAS sb\n"
                + "inner join ent_alumno a on a.id = sb.alumno_id \n"
                + "inner join ENT_ALUMNO_DATOS_ACADEMICOS da on da.alumno_id = sb.alumno_id and da.periodo_id = sb.periodo_id \n"
                + "inner join CAT_UNIDAD_ACADEMICA ua on ua.id = da.unidadacademica_id\n"
                + "join ENT_CARRERA c on c.id = da.CARRERA_ID\n"
                + "join CAT_MODALIDAD m on m.id = da.MODALIDAD_ID \n"
                + "join ENT_DIRECCION dir on dir.id = a.DIRECCION_ID \n"
                + "join RMM_ESTADO_DELEG_COL del on del.id = dir.RELACIONGEOGRAFICA_ID \n"
                + "join CAT_DELEGACION_MUNICIPIO dlm on dlm.id = del.MUNICIPIO_ID \n"
                + "left join ent_lista_espera l on l.solicitudbeca_id = sb.id and l.vigente = 1 and rownum = 1 \n"
                + "left join cat_motivo_rechazo_solicitud mr on mr.id = sb.motivorechazo_id\n"
                + "left join vw_otorgamientos o on o.solicitudbeca_id = sb.id and o.periodo_id = sb.periodo_id \n"
                + "left join ent_tipo_beca_periodo tbp on tbp.id = o.tipobecaperiodo_id \n"
                + "left join cat_tipo_beca tb on tb.id = tbp.tipobeca_id\n"
                + "left join cat_programa_beca b on b.id = sb.PROGRAMABECASOLICITADA_ID \n"
                + "left join ENT_OTORGAMIENTOS ox on o.id = (select oxx.id from ENT_OTORGAMIENTOS oxx where oxx.periodo_id = (select p.PERIODOANTERIOR_ID from CAT_PERIODO p where p.ESTATUS=1 and rownum = 1) and oxx.alumno_id = a.id and oxx.id not in (SELECT v.otorgamiento_id FROM vw_ultima_baja_detalle v  WHERE v.PERIODO_ID = oxx.PERIODO_ID AND v.MOVIMIENTO_ID is not null) and rownum = 1)\n"
                + "left join ENT_PADRON_PROSPERA pp on pp.alumno_id = a.id and pp.periodo_id = o.periodo_id "
                + "left join CAT_CLASIFICACION_SOLICITUD cs on cs.id = sb.CLASIFICACIONSOLICITUD_ID \n"
                + "where sb.periodo_id=?1 "
                //+ " and sb.clasificacionSolicitud_id is null "
                + "and sb.finalizado=1 ";
        consulta += unidadAcademicaId == null || unidadAcademicaId.equals(BigDecimal.ZERO) ? "" : " and da.unidadAcademica_id = ?2";
        consulta += " order by da.unidadAcademica_id, da.turno, l.ordenamiento, a.boleta";
        Query q = entityManager.createNativeQuery(consulta);
        q.setParameter(1, periodoId);
        if (unidadAcademicaId != null) {
            q.setParameter(2, unidadAcademicaId);
        }
        List<Object[]> lista = q.getResultList();
        return lista == null || lista.isEmpty() ? null : lista;
    }

    @Override
    public List<Object[]> alumnosEstatusSolicitudT(BigDecimal unidadAcademicaId, BigDecimal periodoId) {
        String consulta = "select a.BOLETA, a.CURP, a.APELLIDOPATERNO, a.APELLIDOMATERNO, a.NOMBRE, a.CORREOELECTRONICO,  "//0-5
                + "n.NOMBRE, ua.nombrecorto, crr.CARRERA, da.SEMESTRE, da.PROMEDIO, da.CUMPLECARGAMINIMA, " //6-10
                + "c.NOMBRE, "
                + "sb.PERMITETRANSFERENCIA, "
                + "b.nombre as solicitada, "
                + "l.ordenamiento, "
                + "mr.nombre as rechazo, "
                + "tb.nombre as tipobeca, "
                + "bt.NUMTARJETABANCARIA " //17
                + "from ENT_SOLICITUD_BECAS sb   "
                + "inner join ENT_CUESTIONARIO c on c.id = sb.CUESTIONARIO_ID "
                + "inner join ent_alumno a on a.id = sb.alumno_id   "
                + "inner join ENT_ALUMNO_DATOS_ACADEMICOS da on da.alumno_id = sb.alumno_id and da.periodo_id = sb.periodo_id  "
                + "inner join ENT_CARRERA crr on crr.id = da.CARRERA_ID "
                + "inner join CAT_UNIDAD_ACADEMICA ua on ua.id = da.unidadacademica_id  "
                + "inner join CAT_NIVEL n on ua.NIVEL_ID = n.id "
                + "left join ent_lista_espera l on l.solicitudbeca_id = sb.id and l.vigente = 1 "
                + "left join cat_motivo_rechazo_solicitud mr on mr.id = sb.motivorechazo_id  "
                + "left join ent_otorgamientos o on o.solicitudbeca_id = sb.id and o.periodo_id = sb.periodo_id  "
                + "left join ent_tipo_beca_periodo tbp on tbp.id = o.tipobecaperiodo_id  "
                + "left join cat_tipo_beca tb on tb.id = tbp.tipobeca_id  "
                + "left join cat_programa_beca b on b.id = sb.PROGRAMABECASOLICITADA_ID  "
                + "left join(select max(id) as id, alumno_id from RMM_ALUMNO_TARJETA_BANCARIA where estatustarjbanc_id = 13 group by alumno_id) maxatb on maxatb.alumno_id = a.id  "
                + "left join RMM_ALUMNO_TARJETA_BANCARIA t on t.id = maxatb.id "
                + "left join ENT_TARJETA_BANCARIA bt on bt.id = t.TARJETABANCARIA_ID "
                + "where sb.periodo_id=?1 "
                + "and sb.finalizado=1";
        consulta += unidadAcademicaId == null || unidadAcademicaId.equals(BigDecimal.ZERO) ? "" : " and da.unidadAcademica_id = ?2";
        consulta += " order by da.unidadAcademica_id, da.turno, l.ordenamiento, a.boleta";
        Query q = entityManager.createNativeQuery(consulta);
        q.setParameter(1, periodoId);
        if (unidadAcademicaId != null) {
            q.setParameter(2, unidadAcademicaId);
        }
        List<Object[]> lista = q.getResultList();
        return lista == null || lista.isEmpty() ? null : lista;
    }

    @Override
    public BigDecimal totalAlumnosPreAsignados(BigDecimal nivel, BigDecimal unidadAcademicaId) {
        BigDecimal periodoId = getDaos().getPeriodoDao().getPeriodoActivo().getId();
        String jpql = "select count(sb) from SolicitudBeca sb "
                + "join DatosAcademicos da on da.alumno.id = sb.alumno.id and da.periodo.id = sb.periodo.id "
                + "where sb.tipoBecaPreasignada is not null "
                + "and sb.periodo.id = ?1 "
                + "and sb.finalizado = 1 ";
        jpql += unidadAcademicaId == null || unidadAcademicaId.equals(BigDecimal.ZERO) ? "" : " and da.unidadAcademica.id = ?2 ";
        jpql += nivel == null || nivel.equals(BigDecimal.ZERO) ? " " : " and da.unidadAcademica.nivel.id = " + nivel;
        return new BigDecimal(unidadAcademicaId == null || unidadAcademicaId.equals(BigDecimal.ZERO) ? getCountQuery(jpql, periodoId) : getCountQuery(jpql, periodoId, unidadAcademicaId));
    }

    @Override
    public List<SolicitudBeca> getSolicitudesPreasignacionMasiva(BigDecimal periodoId, BigDecimal periodoAnteriorId, BigDecimal nivelId, BigDecimal unidadAcademicaId, boolean sobreEscribir) {
        List<SolicitudBeca> lista = new ArrayList<>();
        List<Object[]> result;
        SolicitudBeca s;
        Alumno a;
        Beca b;
        DatosAcademicos da;
        UnidadAcademica ua;
        Carrera ca;
        String jpql;
        String condiciones;
        String campos = "select sb.id, a.id, a.boleta, a.usuario_id, a.apellidopaterno, a.apellidomaterno, a.nombre, a.curp, "
                + "sb.cuestionario_id, sb.periodo_id, sb.clasificacionsolicitud_id, "
                + "sb.programabecasolicitada_id, b.nombre, sb.permitetransferencia, sb.fechaingreso, sb.finalizado, "
                + "sb.ingresospercapitapesos, sb.vulnerabilidadsubes, da.id, da.periodo_id , da.promedio, da.semestre, "
                + "ua.id, ua.nombrecorto, ua.nivel_id, ua.areasconocimiento_id, ca.id, ca.numerosemestres, "
                + "da.regular, da.inscrito, da.modalidaddae, da.modalidad_id, da.turno, da.egresado, da.cumplecargaminima, "
                + "da.infoactualizadaadmin, da.fechamodificacion, da.totalcreditos ";
        String tablas = "from ent_solicitud_becas sb "
                + "join ent_alumno a on (a.id = sb.alumno_id) "
                + "join cat_programa_beca b on (b.id = sb.programabecasolicitada_id) "
                + "join ent_alumno_datos_academicos da on (da.alumno_id = sb.alumno_id and da.periodo_id = ?1) "
                + "join cat_unidad_academica ua on (da.unidadacademica_id = ua.id) "
                + "join ent_carrera ca on (ca.id = da.carrera_id)";

        if (periodoAnteriorId == null || periodoAnteriorId.equals(BigDecimal.ZERO)) {
            tablas += "left join ent_lista_espera le on (le.solicitudbeca_id = sb.id and le.vigente = 1) ";
            condiciones = "where sb.periodo_id = ?1 and sb.cuestionario_id in (1,2,4,5) and sb.finalizado = 1 "
                    + "and (sb.clasificacionsolicitud_id is null or sb.clasificacionsolicitud_id = 3) "
                    + "and (ua.nivel_id = ?2 or ?2 is null or ?2 = 0) "
                    + "and (da.unidadacademica_id = ?3 or ?3 is null or ?3 = 0) ";
            if (!sobreEscribir) {
                condiciones += "and sb.tipobecapreasignada_id is null and sb.rechazobecasolicitada_id is null and sb.rechazotransferencia_id is null ";
            }
            jpql = campos + tablas + condiciones + " order by ua.id ";
            result = executeNativeQuery(jpql, periodoId, nivelId, unidadAcademicaId);
        } else {
            tablas += "left join ent_lista_espera le on (le.solicitudbeca_id = sb.id and le.vigente = 1) ";
            condiciones = "where sb.periodo_id in (?1,?4) and sb.cuestionario_id in (1,2,4,5) and sb.finalizado = 1 "
                    + "and (sb.clasificacionsolicitud_id is null or sb.clasificacionsolicitud_id = 3) "
                    + "and (ua.nivel_id = ?2 or ?2 is null or ?2 = 0) "
                    + "and (da.unidadacademica_id = ?3 or ?3 is null or ?3 = 0) ";
            if (!sobreEscribir) {
                condiciones += "and sb.tipobecapreasignada_id is null and sb.rechazobecasolicitada_id is null and sb.rechazotransferencia_id is null ";
            }
            jpql = campos + tablas + condiciones + " order by ua.id ";
            result = executeNativeQuery(jpql, periodoId, nivelId, unidadAcademicaId, periodoAnteriorId);
        }

        System.out.println("Ejecutada");

        int i;
        for (Object[] resultado : result) {
            s = new SolicitudBeca();
            a = new Alumno();
            b = new Beca();
            da = new DatosAcademicos();
            ua = new UnidadAcademica();
            ca = new Carrera();
            i = 0;
            try {
                s.setId((BigDecimal) resultado[i]);
                a.setId((BigDecimal) resultado[++i]);
                a.setBoleta((String) resultado[++i]);
                a.setUsuario(new Usuario((BigDecimal) resultado[++i]));
                a.setApellidoPaterno((String) resultado[++i]);
                a.setApellidoMaterno((String) resultado[++i]);
                a.setNombre((String) resultado[++i]);
                a.setCurp((String) resultado[++i]);
                s.setCuestionario(new Cuestionario((BigDecimal) resultado[++i]));
                s.setPeriodo(new Periodo((BigDecimal) resultado[++i]));
                s.setClasificacionSolicitud(resultado[++i] == null ? null : new ClasificacionSolicitud((BigDecimal) resultado[i]));
                b.setId((BigDecimal) resultado[++i]);
                b.setNombre((String) resultado[++i]);
                s.setProgramaBecaSolicitada(b);
                s.setPermiteTransferencia(((BigDecimal) resultado[++i]).intValue());
                s.setFechaIngreso((Date) resultado[++i]);
                s.setFinalizado((((BigDecimal) resultado[++i]).intValue() != 0));
                s.setIngresosPercapitaPesos(((BigDecimal) resultado[++i]).floatValue());
                s.setVulnerabilidadSubes(resultado[++i] == null ? null : (((BigDecimal) resultado[i]).intValue() != 0));
                da.setId((BigDecimal) resultado[++i]);
                da.setPeriodo(new Periodo((BigDecimal) resultado[++i]));
                da.setPromedio(((BigDecimal) resultado[++i]).doubleValue());
                da.setSemestre(((BigDecimal) resultado[++i]).intValue());
                ua.setId((BigDecimal) resultado[++i]);
                ua.setNombreCorto((String) resultado[++i]);
                ua.setNivel(new Nivel((BigDecimal) resultado[++i]));
                ua.setAreasConocimiento(new AreasConocimiento((BigDecimal) resultado[++i]));
                da.setUnidadAcademica(ua);
                ca.setId((BigDecimal) resultado[++i]);
                ca.setNumeroSemestres(((BigDecimal) resultado[++i]).intValue());
                da.setCarrera(ca);
                da.setRegular(((BigDecimal) resultado[++i]).intValue());
                da.setInscrito(((BigDecimal) resultado[++i]).intValue());
                da.setModalidadDAE(((BigDecimal) resultado[++i]).intValue());
                da.setModalidad(new Modalidad((BigDecimal) resultado[++i]));
                da.setTurno((String) resultado[++i]);
                da.setEgresado(((BigDecimal) resultado[++i]).intValue());
                da.setCumpleCargaMinima(((BigDecimal) resultado[++i]).intValue());
                da.setInfoActualizadaAdmin(((BigDecimal) resultado[++i]).intValue());
                da.setFechaModificacion((Date) resultado[++i]);
                da.setTotalCreditos(((BigDecimal) resultado[++i]).floatValue());
                a.setDatosAcademicos(da);
                s.setAlumno(a);

                lista.add(s);
                //System.gc();
            } catch (NullPointerException e) {
                System.out.println("Solicitud: " + resultado[0] + " Campo: " + i);
            }
        }
        System.out.println("Terminaron de asignarse");

        return lista == null || lista.isEmpty() ? null : lista;
    }

    @Override
    public BigDecimal totalAlumnosSolicitudPeriodoEstatusNivelUA(BigDecimal periodoId, BigDecimal periodoAnteriorId, BigDecimal estatusSolicitud, BigDecimal nivelId, BigDecimal unidadAcademicaId, boolean sobreEscribir) {
        Map<String, Object> parametros = new HashMap<>();

        parametros.put("periodoId", periodoId);
        String jpql = "select count(sb) from SolicitudBeca sb "
                + "join DatosAcademicos da on (da.alumno.id = sb.alumno.id and da.periodo.id = :periodoId) ";
        if (estatusSolicitud != null && estatusSolicitud.equals(new BigDecimal(3))) {
            if (periodoAnteriorId == null) {
                jpql += "join ListaEspera le on (le.solicitudBeca.id = sb.id and le.vigente = 1) "
                        + "where sb.periodo.id = :periodoId "
                        + "and sb.cuestionario.id in (1,2,4,5) and sb.finalizado = 1 ";
            } else {
                jpql += "join ListaEspera le on (le.solicitudBeca.id = sb.id and le.vigente = 1) "
                        + "where sb.periodo.id = :periodoAnteriorId "
                        + "and sb.cuestionario.id in (1,2,4,5) and sb.finalizado = 1 ";
                parametros.put("periodoAnteriorId", periodoAnteriorId);
            }
        } else {
            jpql += "where sb.periodo.id = :periodoId "
                    + "and sb.cuestionario.id in (1,2,4,5) and sb.finalizado = 1 ";
        }

        jpql += estatusSolicitud == null || estatusSolicitud.equals(BigDecimal.ZERO) ? "and sb.clasificacionSolicitud is null " : " and sb.clasificacionSolicitud.id = " + estatusSolicitud + " ";

        if (!(nivelId == null || nivelId.equals(BigDecimal.ZERO))) {
            parametros.put("nivelId", nivelId);
            jpql += "and da.unidadAcademica.nivel.id = :nivelId ";
        }

        if (!(unidadAcademicaId == null || unidadAcademicaId.equals(BigDecimal.ZERO))) {
            parametros.put("unidadAcademicaId", unidadAcademicaId);
            jpql += " and da.unidadAcademica.id = :unidadAcademicaId ";
        }

        if (!sobreEscribir) {
            jpql += " and sb.tipoBecaPreasignada is null and sb.rechazoBecaSolicitada is null and sb.rechazoTransferencia is null ";
        }

        BigDecimal result = new BigDecimal(getCountQueryNamedParameters(jpql, parametros));

        return result == null ? BigDecimal.ZERO : result;
    }

    /**
     * Devuelve las solicitudes de un alumno para un periodo en específico.
     *
     * @author Mario Márquez
     * @param alumnoId
     * @param periodoId
     * @return lista SolicitudBeca
     */
    //SQLINJECTION OK
    @Override
    public List<SolicitudBeca> getSolicitudesPorAlumno(BigDecimal alumnoId, BigDecimal periodoId) {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT s ");
        sb.append(" FROM SolicitudBeca s ");
        sb.append(" WHERE ");
        sb.append(" s.alumno.id = ?1 ");
        sb.append(" AND s.periodo.id = ?2 ");
        // Fix 17/10/18: Se estaban incluyendo en estatus de solicitud,
        // solicitudes que no estaban terminadas. Se agrega esa restricción
        // para evitar este problema.
        sb.append(" AND s.finalizado = 1 ");

        List<SolicitudBeca> lista = executeQuery(sb.toString(), alumnoId, periodoId);

        return lista.isEmpty() ? null : lista;
    }

    /**
     * Devuelve las solicitudes de un alumno para un periodo en específico.
     *
     * @author Augusto H.
     * @param alumnoId
     * @param periodoId
     * @return lista SolicitudBeca
     */
    @Override
    public List<SolicitudBeca> getSolicitudesSinReconsideracionAlumno(BigDecimal alumnoId, BigDecimal periodoId) {
        String consulta = "select * from ENT_SOLICITUD_BECAS sb\n"
                + " left join ENT_SOLICITUD_RECONSIDERACION sr on\n"
                + " sb.id = sr.SOLICITUDBECA_ID\n"
                + " where\n"
                + " sb.PERIODO_ID = " + periodoId + "\n"
                + " and sb.ALUMNO_ID = " + alumnoId + "\n"
                + " and sr.ID is null\n";
        List<SolicitudBeca> lista = executeNativeQuery(consulta, SolicitudBeca.class);
        return lista;
    }
    /**
     * Devuelve las solicitudes de los alumnos para validar su inscripción en el
     * periodo actual Esta Query está ligada a 'validacionInscripcion' en
     * AlumnoJpaDao Si se desea modificar alguna de las 2, deben de modificarse
     * ambas.
     *
     * Se solicita que la preasignación se realice para todos los alumnos con
     * beca marcada como validación de inscripción, no importando su aún no
     * tienen datos académicos actuales, con la finalidad de que, ya sea el
     * mismo alumno al iniciar su sesión, o un responsable de beca pasen sus
     * datos académicos, la preasignación ya esté lista y les permita realizar
     * el otorgamiento sin ninguna actividad manual adicional
     *
     * @author Augusto H.
     * @param periodoActualId
     * @param periodoAnteriorId
     * @param map
     * @return lista SolicitudBeca
     */
    @Override
    public int[] getSolicitudesValidacionInscripcion(BigDecimal periodoActualId, BigDecimal periodoAnteriorId, HashMap<BigDecimal, BigDecimal> map) {
        //Arreglo que contendrá los numeros de alumnos dependiendo del resultado de su preasignación
        int[] info = new int[4];
        int buenos = 0;
        int malos = 0;
        int sinCambios = 0;

        String consulta = "select sb.* \n"
                + " from ent_alumno a\n"
                + " join ent_otorgamientos o on o.ALUMNO_ID = a.id\n"
                + " join ent_tipo_beca_periodo tbpAn on tbpAn.id = o.TIPOBECAPERIODO_ID\n"
                + " join ent_solicitud_becas sb on sb.id = o.SOLICITUDBECA_ID\n"
                //+ " left join ENT_ALUMNO_DATOS_ACADEMICOS da on da.ALUMNO_ID = a.id and da.periodo_id = " + periodoActualId + "\n"
                + " where o.alta=1 and o.periodo_id = " + periodoAnteriorId + " and o.proceso_id is not null\n"
                //+ " and da.id is not null\n"
                + " and tbpAn.TIPOBECA_ID IN\n"
                + " (SELECT tbp.TIPOBECA_ID FROM ent_tipo_beca_periodo tbp\n"
                + " WHERE tbp.periodo_id = " + periodoActualId + " and tbp.validaciondeinscripcion > 0)";
        List<SolicitudBeca> list = executeNativeQuery(consulta, SolicitudBeca.class);
        info[0] = list == null ? 0 : list.size();
        if (list != null) {
            for (SolicitudBeca sb : list) {
                if (sb.getTipoBecaPreasignada() != null) {
                    sb.setTipoBecaPreasignadaOriginal(sb.getTipoBecaPreasignada());
                    Otorgamiento o = getDaos().getOtorgamientoDao().getOtorgamientoSolicitudPeriodo(sb.getId(), periodoAnteriorId);
//                    BigDecimal tbaId = map.get(sb.getTipoBecaPreasignada().getId());
                    BigDecimal tbaId = map.get(o.getTipoBecaPeriodo().getId());

                    if (tbaId == null) {
                        malos++;
                    } else if (sb.getTipoBecaPreasignada().getPeriodo().getId().compareTo(periodoActualId) == 0) {
                        sinCambios++;
                    } else {
                        TipoBecaPeriodo tba = new TipoBecaPeriodo();
                        tba.setId(tbaId);
                        sb.setTipoBecaPreasignada(tba);
//                    service.getSolicitudBecaDao().update(sb);
                        sb = update(sb);
                        buenos++;
                    }
                } else {
                    malos++;
                }
            }
        }
        info[1] = buenos;
        info[2] = malos;
        info[3] = sinCambios;
        return info;
    }

    /**
     * Devuelve las solicitudes de un alumno para un periodo en específico.
     *
     * @author Othoniel Herrerra
     * @param solicitudesIds
     * @return boolean
     */
//SQLINJECTION
    @Override
    public int borradoPreasignacionMasiva(BigDecimal periodoId, BigDecimal periodoAnteriorId, BigDecimal nivelId, BigDecimal unidadAcademicaId, boolean sobreEscribir) {
        String sqlSelect;
        String sqlUpdate;
        String condiciones;
        String campos = "select sb.id ";
        String tablas = "from ent_solicitud_becas sb "
                + "join ent_alumno a on (a.id = sb.alumno_id) "
                + "join cat_programa_beca b on (b.id = sb.programabecasolicitada_id) "
                + "join ent_alumno_datos_academicos da on (da.alumno_id = sb.alumno_id and da.periodo_id = ?1) "
                + "join cat_unidad_academica ua on (da.unidadacademica_id = ua.id) ";

        if (periodoAnteriorId == null || periodoAnteriorId.equals(BigDecimal.ZERO)) {
            tablas += "left join ent_lista_espera le on (le.solicitudbeca_id = sb.id and le.vigente = 1) ";
            condiciones = "where sb.periodo_id = ?1 and sb.cuestionario_id in (1,2,4,5) and sb.finalizado = 1 "
                    + "and (sb.clasificacionsolicitud_id is null or sb.clasificacionsolicitud_id = 3) "
                    + "and (ua.nivel_id = ?2 or ?2 is null or ?2 = 0) "
                    + "and (da.unidadacademica_id = ?3 or ?3 is null or ?3 = 0) ";
            if (!sobreEscribir) {
                condiciones += "and sb.tipobecapreasignada_id is null and sb.rechazobecasolicitada_id is null and sb.rechazotransferencia_id is null ";
            }
            sqlSelect = campos + tablas + condiciones;

            sqlUpdate = "UPDATE ENT_SOLICITUD_BECAS "
                    + "SET TIPOBECAPREASIGNADA_ID = NULL, RECHAZOBECASOLICITADA_ID = NULL, RECHAZOTRANSFERENCIA_ID = NULL "
                    + "WHERE ID IN (" + sqlSelect + ") ";
            return executeNativeUpdate(sqlUpdate, periodoId, nivelId, unidadAcademicaId);
        } else {
            tablas += "left join ent_lista_espera le on (le.solicitudbeca_id = sb.id and le.vigente = 1) ";
            condiciones = "where sb.periodo_id in (?1,?4) and sb.cuestionario_id in (1,2,4,5) and sb.finalizado = 1 "
                    + "and (sb.clasificacionsolicitud_id is null or sb.clasificacionsolicitud_id = 3) "
                    + "and (ua.nivel_id = ?2 or ?2 is null or ?2 = 0) "
                    + "and (da.unidadacademica_id = ?3 or ?3 is null or ?3 = 0) ";
            if (!sobreEscribir) {
                condiciones += "and sb.tipobecapreasignada_id is null and sb.rechazobecasolicitada_id is null and sb.rechazotransferencia_id is null ";
            }
            sqlSelect = campos + tablas + condiciones;

            sqlUpdate = "UPDATE ENT_SOLICITUD_BECAS "
                    + "SET TIPOBECAPREASIGNADA_ID = NULL, RECHAZOBECASOLICITADA_ID = NULL, RECHAZOTRANSFERENCIA_ID = NULL "
                    + "WHERE ID IN (" + sqlSelect + ") ";
            return executeNativeUpdate(sqlUpdate, periodoId, nivelId, unidadAcademicaId, periodoAnteriorId);
        }
    }

    /**
     * Crea una lista de arreglos objeto, que contiene la información para el
     * reporte excel de solicitudes. Cada decode o nvl del select, se arma por
     * separado.
     *
     * @author Mario Márquez
     * @param periodoId BigDecimal
     * @param nivelId BigDecimal
     * @param uAId BigDecimal
     * @param upper String Fecha límite superior
     * @param lower String Fecha límite inferior
     * @return List<Object[]> Lista de objetos
     */
    @Override
    public List<Object[]> reporteSolicitudes(BigDecimal periodoId, BigDecimal nivelId, BigDecimal uAId, String upper, String lower) {
        StringBuilder sb = new StringBuilder();
        List<String> columnas = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        List<String> criteria = new ArrayList<>();
        StringBuilder decodeCarga = new StringBuilder();
        StringBuilder nvlFecha = new StringBuilder();
        StringBuilder caseNacionalidad = new StringBuilder();

        decodeCarga.append(" Decode(D.cumplecargaminima,");
        decodeCarga.append(" 0, 'Debajo de la mínima',");
        decodeCarga.append(" 1, 'Mínima',");
        decodeCarga.append(" 2, 'Media',");
        decodeCarga.append(" 3, 'Máxima',");
        decodeCarga.append(" 'Error'");
        decodeCarga.append(" )");
        decodeCarga.append(" \"CARGA ACADÉMICA\"");

        caseNacionalidad.append(" CASE");
        caseNacionalidad.append(" WHEN B.entidaddenacimiento_id < 33");
        caseNacionalidad.append(" THEN 'México'");
        caseNacionalidad.append(" WHEN B.entidaddenacimiento_id >= 33");
        caseNacionalidad.append(" THEN 'Extranjero'");
        caseNacionalidad.append(" ELSE '-'");
        caseNacionalidad.append(" END");
        caseNacionalidad.append(" \"NACIONALIDAD\"");

        nvlFecha.append(" NVL(");
        nvlFecha.append(" to_char(A.fechaingreso,");
        nvlFecha.append(" 'yyyy/MM/dd hh:mm:ss'),");
        nvlFecha.append(" 'Error')");
        nvlFecha.append(" \"FECHA SOLICITUD\"");

        columnas.add("N.nombre \"NIVEL ACADÉMICO\"");
        columnas.add("UA.nombrecorto \"UNIDAD ACADÉMICA\"");
        columnas.add("B.boleta \"BOLETA\"");
        columnas.add("B.curp \"CURP\"");
        columnas.add(nvlFecha.toString());
        columnas.add("B.apellidopaterno \"APELLIDO PATERNO\"");
        columnas.add("B.apellidomaterno \"APELLIDO MATERNO\"");
        columnas.add("B.nombre \"NOMBRE\"");
        columnas.add(decodeBoolean("D.inscrito", "INSCRITO"));
        columnas.add(decodeBoolean("D.egresado", "EGRESADO"));
        columnas.add("D.promedio \"PROMEDIO\"");
        columnas.add("D.semestre \"SEMESTRE\"");
        columnas.add(decodeBoolean("D.regular", "REGULAR"));
        columnas.add(decodeCarga.toString());
        columnas.add("M.nombre \"MODALIDAD\"");
        columnas.add("PB.nombre \"PROGRAMA BECA SOLICITADA\"");
        columnas.add(decodeBoolean("A.PERMITETRANSFERENCIA", "PERMITE TRANSFERENCIA"));
        columnas.add("Nvl(C.nombre, 'Pendiente') \"CLASIFICACIÓN SOLICITUD\"");
        columnas.add("RS.nombre \"MOTIVO RECHAZO\"");
        columnas.add("TB.nombre \"BECA ASIGNADA\"");
        columnas.add("A.ingresospercapitapesos \"INGRESOS PERCAPITA PESOS\"");
        columnas.add("Nvl(A.INGRESOTOTALMENSUAL, 0)");
        columnas.add("Nvl(A.NUMERODEINTEGRANTES, 0)");
        columnas.add("Nvl(A.GASTOENTRANSPORTE, 0)");

        columnas.add("B.correoelectronico \"CORREO ELECTRONICO\"");
        columnas.add("B.celular \"CELULAR\"");
        columnas.add("B.telefonocasa \"TELEFONO CASA\"");
        columnas.add("Nvl(tbpre.nombre, ' - ') \"BECA PREASIGNADA\"");
        columnas.add("Decode(Nvl(ox.id, 0), 0, 'No', 'Sí') \"BECA PERIODO ANT\"");
        columnas.add(caseNacionalidad.toString());
        columnas.add("EC.carrera \"CARRERA\"");
        columnas.add("Decode(Nvl(pp.id, 0), 0, 'No', 'Sí') \"PROSPERA\"");
        columnas.add("Decode(dlm.conhambre, 0, 'No', 1, 'Sí', '-') \"MUN POBREZA\"");
        sb.append(" SELECT ");
        agregaColumnas(sb, columnas);

        sb.append(" FROM ent_solicitud_becas A");
        sb.append(" left join ent_alumno B");
        sb.append("  ON B.id = A.alumno_id");
        sb.append(" left join cat_clasificacion_solicitud C ");
        sb.append("  ON C.id = A.clasificacionsolicitud_id");
        sb.append(" left join ent_alumno_datos_academicos D ");
        sb.append("  ON A.alumno_id = D.alumno_id");
        sb.append("  AND D.periodo_id = A.PERIODO_ID");
        sb.append(" left join cat_unidad_academica UA ");
        sb.append("  ON D.unidadacademica_id = UA.id");
        sb.append(" left join cat_nivel N ");
        sb.append("  ON N.id = UA.nivel_id");
        sb.append(" left join cat_motivo_rechazo_solicitud RS ");
        sb.append("  ON RS.id = A.motivorechazo_id");
        sb.append(" left join cat_programa_beca PB ");
        sb.append("  ON PB.id = A.programabecasolicitada_id");
        sb.append(" left join vw_otorgamientos O ");
        sb.append("  ON O.solicitudbeca_id = A.id");
        sb.append("  AND O.periodo_id = A.PERIODO_ID");
        sb.append(" left join ent_tipo_beca_periodo TBP ");
        sb.append("  ON TBP.id = O.tipobecaperiodo_id");
        sb.append(" left join cat_tipo_beca TB ");
        sb.append("  ON TB.id = TBP.tipobeca_id");
        sb.append(" left join ent_tipo_beca_periodo TBPPre ");
        sb.append("  ON A.tipobecapreasignada_id = TBPPre.ID");
        sb.append(" left JOIN CAT_TIPO_BECA tbpre");
        sb.append("  ON tbpre.id = TBPPre.tipobeca_id");
        sb.append(" left join cat_modalidad M ");
        sb.append("  ON M.id = D.modalidad_id");
        sb.append(" left join ENT_PADRON_PROSPERA pp ");
        sb.append("  ON pp.alumno_id = B.id and pp.periodo_id = O.periodo_id ");
        sb.append(" left join ENT_OTORGAMIENTOS ox ");
        sb.append("  ON O.id = (select oxx.id from vw_otorgamientos oxx where oxx.periodo_id = (select p.PERIODOANTERIOR_ID from CAT_PERIODO p where p.ID = #periodo_id and rownum = 1) and oxx.alumno_id = B.id and rownum = 1)");
        sb.append(" join ENT_CARRERA EC on EC.id = D.CARRERA_ID ");
        sb.append(" join ENT_DIRECCION dir on dir.id = B.DIRECCION_ID ");
        sb.append(" join RMM_ESTADO_DELEG_COL del on del.id = dir.RELACIONGEOGRAFICA_ID ");
        sb.append(" join CAT_DELEGACION_MUNICIPIO dlm on dlm.id = del.MUNICIPIO_ID ");

        criteria.add("A.periodo_id = #periodo_id");
        params.put("periodo_id", periodoId);
        criteria.add("A.finalizado = 1");
        if (nivelId != null) {
            criteria.add("N.ID = #nivel_id");
            params.put("nivel_id", nivelId);
        }
        if (uAId != null) {
            criteria.add("UA.ID = #UA_id");
            params.put("UA_id", uAId);
        }
        if (upper != null) {
            criteria.add("Trunc(A.fechaingreso) <= #upper");
            params.put("upper", upper);
        }
        if (lower != null) {
            criteria.add("Trunc(A.fechaingreso) >= #lower");
            params.put("lower", lower);
        }

        agregaCriterios(sb, criteria);

        // Ordena por nivel, unidad académica, y fecha de solicitud
        sb.append(" ORDER  BY N.ID ASC, UA.ID ASC, A.fechaingreso ASC");

        List<Object[]> result = executeNativeQuery(sb.toString(), params, null);

        System.out.println("EFÍMERO SUPERFLUO INEFABLE INCONMENSURABLE ETÉREO SEMPITERNO PETRICOR PERENNE RESILENCIA");
        return result;
    }

    public boolean guardasolicitudbecatransporte(CuestionarioTransporte becaTransporte, Cuestionario cuestionario, Usuario usuario, Periodo periodo, String transporte, String puntopartida, String puntollegada, String costo, String trayecto, String nombrefamiliar, String parentesco, String edad, String ocupacion, String aportacionmensual, Alumno alumno) {
        // Variables para datos solicitud
        float ingresoTotal = 0f;
        int integrantesFamilia = 0;
        float gastoTransporteMensual = 0f;
        float gastoTransporteDiario = 0f;
        becaTransporte.setCuestionario(cuestionario);
        becaTransporte.setUsuario(usuario);
        becaTransporte.setPeriodo(periodo);
        getDaos().getCuestionarioTransporteDao().save(becaTransporte);
        //Se realiza el split para los datos de traslado.
        String[] transporteArreglo = transporte.split(",");
        String[] partidaArreglo = puntopartida.split(",");
        String[] llegadaArreglo = puntollegada.split(",");
        String[] costoArreglo = costo.split(",");
        String[] trayectoArreglo = trayecto.split(",");
        TransporteTraslado traslado;
        Transporte trans;
        Trayecto tray;
        for (int i = 0; i < transporteArreglo.length; i++) {
            float trasladoCost = Float.parseFloat(costoArreglo[i]);
            traslado = new TransporteTraslado();
            traslado.setCuestionarioTransporte(becaTransporte);
            trans = new Transporte();
            trans.setId(new BigDecimal(transporteArreglo[i].replace(" ", "")));
            traslado.setTransporte(trans);
            traslado.setPuntopartida(partidaArreglo[i].trim());
            traslado.setPuntollegada(llegadaArreglo[i].trim());
            traslado.setCosto(trasladoCost);
            tray = new Trayecto();
            tray.setId(new BigDecimal(trayectoArreglo[i].trim()));
            traslado.setTrayecto(tray);
            getDaos().getTransporteTrasladoDao().save(traslado);
            // Se multiplica el gasto diario, por el número de días del mes.
            gastoTransporteDiario += trasladoCost;
        }
        gastoTransporteMensual = gastoTransporteDiario * 21.5f;
        //Se realiza el split para los datos de traslado.
        String[] familiarArreglo = nombrefamiliar.split(",");
        String[] parentescoArreglo = parentesco.split(",");
        String[] edadArreglo = edad.split(",");
        String[] ocupacionArreglo = ocupacion.split(",");
        String[] aportacionArreglo = aportacionmensual.split(",");
        TransporteDatosFamiliares familiar;
        Parentesco paren;
        for (int i = 0; i < parentescoArreglo.length; i++) {
            float ingresoMensual = Float.parseFloat(aportacionArreglo[i].trim());

            familiar = new TransporteDatosFamiliares();
            familiar.setCuestionarioTransporte(becaTransporte);
            familiar.setNombrefamiliar(familiarArreglo[i].trim());
            paren = new Parentesco();
            paren.setId(new BigDecimal(parentescoArreglo[i].trim()));
            familiar.setParentesco(paren);
            familiar.setEdad(Integer.parseInt(edadArreglo[i].trim()));
            familiar.setOcupacion(ocupacionArreglo[i].trim());
            familiar.setAportacionmensual(ingresoMensual);
            getDaos().getTransporteDatosFamiliaresDao().save(familiar);

            ingresoTotal += ingresoMensual;
            integrantesFamilia++;
        }

        //Se insertan los datos en SolicitudBecas 
        SolicitudBeca sb = new SolicitudBeca();
        sb.setCuestionario(cuestionario);
        sb.setAlumno(alumno);
        sb.setPeriodo(periodo);
        sb.setFechaModificacion(new Date());
        sb.setFechaIngreso(new Date());
        sb.setFinalizado(Boolean.TRUE);
        sb.setPermiteTransferencia(0);
        sb.setVulnerabilidadSubes(Boolean.FALSE);
        sb.setIngresosPercapitaPesos(getDaos().getTransporteDatosFamiliaresDao().ingresoPercapita(becaTransporte.getId()));
        sb.setIngresoTotalMensual(ingresoTotal);
        sb.setGastoEnTransporte(String.valueOf(gastoTransporteMensual));
        sb.setNumeroDeIntegrantes(integrantesFamilia);
        Beca b = new Beca();
        b.setId(new BigDecimal("8")); //Transporte Institucional
        sb.setProgramaBecaSolicitada(b);
        TipoBecaPeriodo tbp = getDaos().getTipoBecaPeriodoDao().findBecaTransporte(2, alumno, null, false, false, null, null);
        sb.setTipoBecaPreasignada(tbp);
        getDaos().getSolicitudBecaDao().save(sb);
        return true;
    }

    public List<Object[]> estadisticaPreasignadasData(BigDecimal periodoId, BigDecimal uaId, BigDecimal nivelId, String fechaInicial, String fechaFinal) {
        StringBuilder sb = new StringBuilder();
        List<String> criteria = new ArrayList<>();
        Map<String, Object> params = new HashMap();

        sb.append(" SELECT ");
        sb.append(" tb.nombre as tipobecaperiodo, count(c.id) as total , pb.id ");
        sb.append(" from ent_solicitud_becas c ");
        sb.append(" join ent_alumno a on a.id = c.alumno_id ");
        sb.append(" join ENT_ALUMNO_DATOS_ACADEMICOS da on da.ALUMNO_ID = c.ALUMNO_ID and da.PERIODO_ID = c.PERIODO_ID ");
        sb.append(" left join ent_tipo_beca_periodo tbp on tbp.id = c.TIPOBECAPREASIGNADA_ID ");
        sb.append(" join CAT_TIPO_BECA tb on tb.id = tbp.TIPOBECA_ID ");
        sb.append(" join cat_genero g on g.id = a.genero_id ");
        sb.append(" JOIN CAT_UNIDAD_ACADEMICA ua on ua.id = da.unidadacademica_id ");
        sb.append(" join CAT_PROGRAMA_BECA pb on pb.id = tb.BECA_ID ");

        criteria.add(" c.finalizado = 1 ");
        criteria.add(" c.periodo_id = #periodo_id ");
        params.put("periodo_id", periodoId);
        if (uaId != null && uaId.intValue() != 0) {
            criteria.add("da.unidadacademica_id = #ua_id");
            params.put("ua_id", uaId);
        }
        if (nivelId != null && nivelId.intValue() != 0) {
            criteria.add("ua.nivel_id = #nivel_id");
            params.put("nivel_id", nivelId);
        }
        if (fechaInicial != null) {
            criteria.add("c.fechaingreso BETWEEN TO_DATE(#fechainicio,'DD/MM/YYYY') and TO_DATE(#fechafin,'DD/MM/YYYY')");
            params.put("fechainicio", fechaInicial);
            params.put("fechafin", fechaFinal);
        }
        agregaCriterios(sb, criteria);

        sb.append(" group by tb.nombre,pb.id ");
        sb.append(" order by pb.id ");

        List<Object[]> result = executeNativeQuery(sb.toString(), params, null);

        return result;
    }

    public List<Object[]> estadisticaSolicitadasData(BigDecimal periodoId, BigDecimal uaId, BigDecimal nivelId, String fechaInicial, String fechaFinal) {
        StringBuilder sb = new StringBuilder();
        List<String> criteria = new ArrayList<>();
        Map<String, Object> params = new HashMap();

        sb.append(" SELECT ");
        sb.append(" pb.nombre as solicitada, count(c.id) as total, pb.id, pb.clave  ");
        sb.append(" from ent_solicitud_becas c ");
        sb.append(" join ent_alumno a on a.id = c.alumno_id ");
        sb.append(" join ENT_ALUMNO_DATOS_ACADEMICOS da on da.ALUMNO_ID = c.ALUMNO_ID and da.PERIODO_ID = c.PERIODO_ID ");
        sb.append(" left join CAT_PROGRAMA_BECA pb on pb.id = c.PROGRAMABECASOLICITADA_ID ");
        sb.append(" join cat_genero g on g.id = a.genero_id ");
        sb.append(" JOIN CAT_UNIDAD_ACADEMICA ua on ua.id = da.unidadacademica_id ");

        criteria.add(" c.finalizado = 1 ");
        criteria.add(" c.periodo_id = #periodo_id ");
        params.put("periodo_id", periodoId);

        if (uaId != null && uaId.intValue() != 0) {
            criteria.add("da.unidadacademica_id = #ua_id");
            params.put("ua_id", uaId);
        }
        if (nivelId != null && nivelId.intValue() != 0) {
            criteria.add("ua.nivel_id = #nivel_id");
            params.put("nivel_id", nivelId);
        }
        if (fechaInicial != null) {
            criteria.add("c.fechaingreso BETWEEN TO_DATE(#fechainicio,'DD/MM/YYYY') and TO_DATE(#fechafin,'DD/MM/YYYY')");
            params.put("fechainicio", fechaInicial);
            params.put("fechafin", fechaFinal);
        }
        agregaCriterios(sb, criteria);

        sb.append(" group by pb.nombre,pb.id,pb.clave ");
        sb.append(" order by pb.id  ");

        List<Object[]> result = executeNativeQuery(sb.toString(), params, null);

        return result;
    }
}
