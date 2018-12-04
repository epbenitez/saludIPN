package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.CuestionarioPreguntaRespuestaDao;
import com.becasipn.persistence.model.Beca;
import com.becasipn.persistence.model.CuestionarioPreguntaRespuesta;
import com.becasipn.persistence.model.CuestionarioPreguntaTipo;
import com.becasipn.persistence.model.CuestionarioPreguntas;
import com.becasipn.persistence.model.CuestionarioRespuestas;
import com.becasipn.persistence.model.CuestionarioSeccion;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez
 */
public class CuestionarioPreguntaRespuestaJpaDao extends JpaDaoBase<CuestionarioPreguntaRespuesta, BigDecimal> implements CuestionarioPreguntaRespuestaDao {

    public CuestionarioPreguntaRespuestaJpaDao() {
        super(CuestionarioPreguntaRespuesta.class);
    }

    @Override
    public List<CuestionarioPreguntaRespuesta> findByCuestionario(BigDecimal cuestionarioId, BigDecimal nivelId) {
        List<CuestionarioPreguntaRespuesta> cuestionario = new ArrayList<>();
        List<CuestionarioRespuestas> res = new ArrayList<>();
        CuestionarioPreguntaRespuesta cpr = new CuestionarioPreguntaRespuesta();
        String sql = "select cp.id, cp.orden, cs.id, cs.nombre, cs.orden, cp.activo, cp.cuestionariopreguntatipo_id,"
                + " cp.nombre,cpr.respuesta_id,cr.nombre, cpr.orden, cp.padre, cp.respuestadependiente_id"
                + " from ent_cuestionario_pregunta_resp cpr, ent_cuestionario_respuesta cr, ent_cuestionario_pregunta cp,"
                + " ent_cuestionario_seccion cs, ent_cuestionario c"
                + " where c.activo = 1 and cp.activo = 1 and cs.activo = 1 and c.id = ?1"
                + " and cp.id = cpr.pregunta_id and cs.id = cpr.seccion_id and cpr.respuesta_id = cr.id and c.id = cs.cuestionario_id"
                + " order by cs.orden, cp.orden, cpr.orden";
        Integer actual;
        List<Object[]> lista = executeNativeQuery(sql, cuestionarioId);
        actual = -1;
        for (Object[] pregunta : lista) {
            if (actual != Integer.parseInt(pregunta[0].toString())) {
                if (actual != -1) {
                    cpr.setRespuesta(res);
                    cuestionario.add(cpr);
                }
                cpr = new CuestionarioPreguntaRespuesta();
                cpr.setId(new BigDecimal(pregunta[0].toString()));
                CuestionarioSeccion cs = new CuestionarioSeccion();
                cs.setId(new BigDecimal(pregunta[2].toString()));
                cs.setNombre((String) pregunta[3]);
                cs.setOrden(new Integer(pregunta[4].toString()));
                cpr.setSeccion(cs);
                CuestionarioPreguntas cp = new CuestionarioPreguntas();
                cp.setActivo(1);
                CuestionarioPreguntaTipo cpt = getDaos().getCuestionarioPreguntaTipoDao().findById((BigDecimal) pregunta[6]);
                cp.setCuestionarioPreguntaTipo(cpt);
                cp.setNombre((String) pregunta[7]);
                if (pregunta[11] == null) {
                    cp.setPadre(0);
                } else {
                    cp.setPadre(new Integer(pregunta[11].toString()));
                }
                if (pregunta[12] != null) {
                    //System.out.println(pregunta[12]);
                    CuestionarioRespuestas cr = getDaos().getCuestionarioRespuestasDao().findById((BigDecimal) pregunta[12]);
                    cp.setRespuestaDependiente(cr);
                } else {
                    CuestionarioRespuestas cr = new CuestionarioRespuestas();
                    cr.setId(BigDecimal.valueOf(0));
                    cp.setRespuestaDependiente(cr);
                }
                cpr.setPregunta(cp);
                res = new ArrayList<>();
                actual = Integer.parseInt(pregunta[0].toString());
            }
            CuestionarioRespuestas cr = new CuestionarioRespuestas();
            cr.setId((BigDecimal) pregunta[8]);
            cr.setNombre((String) pregunta[9]);
            res.add(cr);
        }
        cpr.setRespuesta(res);
        cuestionario.add(cpr);

        if (cuestionarioId.compareTo(new BigDecimal(1)) == 0) {
            //Se agrega la sección Selección de Beca cuando se solicita mostrar el formulario de Solicitud de Beca. Para otros cuestionarios, no aplica.
            cpr = new CuestionarioPreguntaRespuesta();
            cpr.setId(new BigDecimal("0"));
            CuestionarioSeccion cs = new CuestionarioSeccion();
            cs.setId(new BigDecimal("5"));
            cs.setNombre("Selección de Beca");
            cs.setOrden(5);
            cpr.setSeccion(cs);
            CuestionarioPreguntas cp = new CuestionarioPreguntas();
            cp.setActivo(1);
            CuestionarioPreguntaTipo cpt = new CuestionarioPreguntaTipo();//getDaos().getCuestionarioPreguntaTipoDao().findById((BigDecimal) pregunta[6]);
            cpt.setId(new BigDecimal("0"));
            cp.setCuestionarioPreguntaTipo(cpt);
            cpr.setPregunta(cp);
            List<Beca> becasPeriodo = getDaos().getBecaDao().becasSinTransportePorPeriodoNivel(getDaos().getPeriodoDao().getPeriodoActivo().getId(), nivelId, Boolean.FALSE);
            res = new ArrayList<>();
            if (becasPeriodo != null && !becasPeriodo.isEmpty()) {
                for (Beca b : becasPeriodo) {
                // Fix 15/02/18 horripilante, se oculta beca manutención
                    // en duro
                    if (!b.getId().equals(new BigDecimal(5))) {
                        CuestionarioRespuestas cr = new CuestionarioRespuestas();
                        cr.setId(b.getId());
                        cr.setNombre(b.getNombre());
                        res.add(cr);
                    }
                }
            }
            cpr.setRespuesta(res);
            cuestionario.add(cpr);
        }
        return cuestionario;
    }

    @Override
    public Integer totalPreguntas(BigDecimal cuestionarioId) {
        String sql = "select distinct pregunta_id from ent_cuestionario_pregunta_resp cpr"
                + " left join ent_cuestionario_pregunta cp on cp.id = cpr.pregunta_id"
                + " left join ent_cuestionario_seccion cs on cs.id = cpr.seccion_id"
                + " where cs.activo=1 and cp.activo=1 and cuestionario_id = ?1";
        List<Object[]> lista = executeNativeQuery(sql, cuestionarioId);
        return lista.size();
    }

    @Override
    public Integer preguntasContestadas(BigDecimal cuestionarioId, BigDecimal periodoId, BigDecimal usuarioId) {
        String sql = "select pregunta_id from rmm_cuestionario_pregunta_resp "
                + " where cuestionario_id = ?1 and periodo_id = ?2 and usuario_id = ?3"
                + " group by pregunta_id";
        List<Object[]> lista = executeNativeQuery(sql, cuestionarioId, periodoId, usuarioId);
        return lista.size();
    }

    @Override
    public List<Object[]> estadisticaCuestionario(BigDecimal periodoId, BigDecimal cuestionarioId) {
        String sql = "select cpr.pregunta_id, p.NOMBRE, cpr.respuesta_id, r.NOMBRE, count(*), prb.SECCION_ID "
                + "from RMM_CUESTIONARIO_PREGUNTA_RESP cpr "
                + "inner join ENT_CUESTIONARIO_PREGUNTA p on p.id = cpr.PREGUNTA_ID "
                + "inner join ENT_CUESTIONARIO_RESPUESTA r on r.id = cpr.RESPUESTA_ID "
                + "inner join ENT_CUESTIONARIO_PREGUNTA_RESP prb on prb.PREGUNTA_ID = cpr.PREGUNTA_ID and prb.RESPUESTA_ID = cpr.RESPUESTA_ID "
                + "inner join ENT_CUESTIONARIO_SECCION secc on secc.id = prb.SECCION_ID "
                + "where cpr.cuestionario_id = ?1 and cpr.PERIODO_ID = ?2 and "
                + "p.CUESTIONARIOPREGUNTATIPO_ID in (1,2) "
                + "group by prb.SECCION_ID, cpr.pregunta_id, p.NOMBRE, cpr.respuesta_id, r.NOMBRE "
                + "order by  prb.SECCION_ID,cpr.pregunta_id,cpr.respuesta_id";
        List<Object[]> lista = executeNativeQuery(sql, cuestionarioId, periodoId);
        return lista;
    }
}
