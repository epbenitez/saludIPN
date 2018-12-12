package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.CuestionarioRespuestasUsuarioDao;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.CuestionarioPreguntaTipo;
import com.becasipn.persistence.model.CuestionarioPreguntas;
import com.becasipn.persistence.model.CuestionarioRespuestas;
import com.becasipn.persistence.model.CuestionarioRespuestasUsuario;
import com.becasipn.persistence.model.CuestionarioSeccion;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.Usuario;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Tania G. Sánchez
 */
public class CuestionarioRespuestasUsuarioJpaDao extends JpaDaoBase<CuestionarioRespuestasUsuario, BigDecimal> implements CuestionarioRespuestasUsuarioDao {

    public CuestionarioRespuestasUsuarioJpaDao() {
        super(CuestionarioRespuestasUsuario.class);
    }

    @Override
    public List<CuestionarioRespuestasUsuario> getResultadosUsuario(BigDecimal cuestionarioId, BigDecimal usuario, BigDecimal periodoId) {
        String sql = "select cpru.id, cs.id, cs.nombre, cp.id, cp.cuestionariopreguntatipo_id, cp.nombre, cp.orden, cr.id, cr.nombre, cpru.respuestaabierta"
                + " from rmm_cuestionario_pregunta_resp cpru"
                + " inner join ent_cuestionario_pregunta cp on cpru.pregunta_id = cp.id"
                + " inner join ent_cuestionario_respuesta cr on cpru.respuesta_id = cr.id"
                + " inner join ent_cuestionario_pregunta_resp cpr on cpr.pregunta_id = cp.id"
                + " inner join ent_cuestionario_seccion cs on cs.id = cpr.seccion_id"
                + " where cs.activo = ?1 and cp.activo = ?2 and cpru.cuestionario_id= ?3"
                + " and cpru.usuario_id= ?4 and cpru.periodo_id = ?5"
                + " group by cpru.id, cs.id, cs.nombre, cp.id, cp.cuestionariopreguntatipo_id, cp.nombre, cp.orden, cr.id, cr.nombre, cpru.respuestaabierta"
                + " order by cs.id, cp.orden";
        List<Object[]> lista = executeNativeQuery(sql, 1, 1, cuestionarioId, usuario, periodoId);
        List<CuestionarioRespuestasUsuario> lcru = new ArrayList<>();
        for (Object[] res : lista) {
            CuestionarioRespuestasUsuario cru = new CuestionarioRespuestasUsuario();
            cru.setId((BigDecimal) res[0]);
            //Sección
            CuestionarioSeccion cs = new CuestionarioSeccion();
            cs.setId((BigDecimal) res[1]);
            cs.setNombre((String) res[2]);
            cru.setSeccion(cs);
            //Preguntas
            CuestionarioPreguntas cp = new CuestionarioPreguntas();
            cp.setId((BigDecimal) res[3]);
            //CuestionarioPreguntaTipo
            CuestionarioPreguntaTipo cpt = new CuestionarioPreguntaTipo();
            cpt.setId((BigDecimal) res[4]);
            cp.setCuestionarioPreguntaTipo(cpt);
            cp.setNombre((String) res[5]);
            cp.setOrden(Integer.parseInt(res[6].toString()));
            cru.setPregunta(cp);
            //Respuestas
            CuestionarioRespuestas cr = new CuestionarioRespuestas();
            cr.setId((BigDecimal) res[7]);
            cr.setNombre((String) res[8]);
            cru.setRespuesta(cr);
            cru.setRespuestaAbierta((String) res[9]);
            lcru.add(cru);
        }
        return lcru;
    }

    @Override
    public CuestionarioRespuestas getRespuestaPreguntaActual(BigDecimal usuario, BigDecimal pregunta) {
        String jpql = "SELECT p.respuesta FROM CuestionarioRespuestasUsuario p "
                + " WHERE p.usuario.id = ?1 "
                + " AND p.pregunta.id= ?2 "
                + " AND p.periodo.estatus= 1 ";
        return (CuestionarioRespuestas) executeQueryObject(jpql, usuario, pregunta).get(0);
    }

    @Override
    public CuestionarioRespuestasUsuario getPreguntaUsuarioActual(BigDecimal usuario, BigDecimal pregunta) {
        String jpql = "SELECT p FROM CuestionarioRespuestasUsuario p "
                + " WHERE p.usuario.id = ?1 "
                + " AND p.pregunta.id= ?2 "
                + " AND p.periodo.estatus= 1 ";
        List<CuestionarioRespuestasUsuario> result = executeQuery(jpql, usuario, pregunta);
        return result == null || result.isEmpty() ? null : result.get(0);
    }

    @Override
    public CuestionarioRespuestasUsuario getPreguntaUsuarioActual(BigDecimal usuario, BigDecimal pregunta, BigDecimal respuesta) {
        String jpql = "SELECT p FROM CuestionarioRespuestasUsuario p "
                + " WHERE p.usuario.id = ?1 "
                + " AND p.pregunta.id= ?2 "
                + " AND p.respuesta.id =?3"
                + " AND p.periodo.estatus= 1 ";
        List<CuestionarioRespuestasUsuario> result = executeQuery(jpql, usuario, pregunta, respuesta);
        return result == null || result.isEmpty() ? null : result.get(0);
    }

    //SQLINJECTION
    @Override
    public Integer getActividadExtraPorUsuario(BigDecimal usuarioId) {
        String consulta = "SELECT cr.nombre, cs.id, cp.id, cr.id  "
                + " FROM rmm_cuestionario_pregunta_resp cpru "
                + " INNER JOIN ent_cuestionario_pregunta cp on cpru.pregunta_id = cp.id "
                + " INNER JOIN ent_cuestionario_respuesta cr on cpru.respuesta_id = cr.id "
                + " INNER JOIN ent_cuestionario_pregunta_resp cpr on cpr.pregunta_id = cp.id "
                + " INNER JOIN ent_cuestionario_seccion cs on cs.id = cpr.seccion_id "
                + " WHERE cs.activo = 1 "
                + " AND cp.activo = 1 "
                + " AND cpru.cuestionario_id = 1 "
                + " AND cpru.usuario_id = " + usuarioId + " "
                + " AND (cp.id = 51 OR cp.id = 53) "
                + " GROUP BY cr.nombre, cs.id, cp.id, cr.id "
                + " ORDER BY cr.nombre, cs.id, cp.id, cr.id";
        List<Object[]> lista = executeNativeQuery(consulta);
        if (lista.isEmpty()) {
            return 0;
        }
        return 1;
    }

    @Override
    public void borrarESEporAlumno(BigDecimal usuarioId, BigDecimal periodoId) {
        String jpql = "delete from CuestionarioRespuestasUsuario where usuario.id = ?1 and periodo.id = ?2 and cuestionario.id = 1";
        Query query = getEntityManager().createQuery(jpql);
        query.setParameter(1, usuarioId);
        query.setParameter(2, periodoId);
        query.executeUpdate();
    }

    @Override
    public CuestionarioRespuestasUsuario respuestaPregunta(BigDecimal usuarioId, BigDecimal preguntaId, BigDecimal cuestionarioId, BigDecimal periodoId) {
        String consulta = "select c from CuestionarioRespuestasUsuario c"
                + " where c.usuario.id = ?1 and c.pregunta.id = ?2 and c.cuestionario.id = ?3 and c.periodo.id = ?4";
        List<CuestionarioRespuestasUsuario> lista = executeQuery(consulta, usuarioId, preguntaId, cuestionarioId, periodoId);
        return lista == null || lista.isEmpty() ? null : lista.get(0);
    }

    @Override
    public Boolean existeRespuestaPregunta(BigDecimal usuarioId, BigDecimal preguntaId, BigDecimal respuestaId, BigDecimal cuestionarioId, BigDecimal periodoId) {
        String consulta = "select c from CuestionarioRespuestasUsuario c"
                + " where c.usuario.id = ?1 "
                + "and c.pregunta.id = ?2 "
                + "and c.respuesta.id = ?3 "
                + "and c.cuestionario.id = ?4 "
                + "and c.periodo.id = ?5";
        List<CuestionarioRespuestasUsuario> lista = executeQuery(consulta, usuarioId, preguntaId, respuestaId, cuestionarioId, periodoId);
        return lista != null && !lista.isEmpty();
    }

    @Override
    public CuestionarioRespuestasUsuario respuestaPreguntaCuestionarioPeriodo(BigDecimal usuarioId, BigDecimal preguntaId, BigDecimal respuestaId, BigDecimal cuestionarioId, BigDecimal periodoId) {
        String consulta = "select c from CuestionarioRespuestasUsuario c"
                + " where c.usuario.id = ?1 "
                + "and c.pregunta.id = ?2 "
                + "and c.respuesta.id = ?3 "
                + "and c.cuestionario.id = ?4 "
                + "and c.periodo.id = ?5";
        List<CuestionarioRespuestasUsuario> lista = executeQuery(consulta, usuarioId, preguntaId, respuestaId, cuestionarioId, periodoId);
        return lista == null || lista.isEmpty() ? null : lista.get(0);
    }

    @Override
    public List<CuestionarioRespuestasUsuario> getEgresos(Usuario usuario, Periodo p) {
        String consulta = "SELECT cru FROM CuestionarioRespuestasUsuario cru "
                + " WHERE cru.usuario.id=?1 and cru.periodo.id= ?2 "
                + " AND ((cru.pregunta.id >= 16 "
                + " AND cru.pregunta.id <=29) "
                + " or (cru.pregunta.id = 167 " // Total del ingreso mensual (en pesos) de los integrantes de la familia, incluyéndote
                + " or cru.pregunta.id = 168 )) "; // Total de integrantes de la familia, incluyéndote
        List<CuestionarioRespuestasUsuario> lista = executeQuery(consulta, usuario.getId(), p.getId());
        return lista == null || lista.isEmpty() ? null : lista;
    }
}
