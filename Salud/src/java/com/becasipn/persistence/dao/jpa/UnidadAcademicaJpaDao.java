package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.UnidadAcademicaDao;
import com.becasipn.persistence.model.AreasConocimiento;
import com.becasipn.persistence.model.Nivel;
import com.becasipn.persistence.model.UnidadAcademica;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez
 */
public class UnidadAcademicaJpaDao extends JpaDaoBase<UnidadAcademica, BigDecimal> implements UnidadAcademicaDao {

    public UnidadAcademicaJpaDao() {
        super(UnidadAcademica.class);
    }

    @Override
    public List<UnidadAcademica> findAll() {
        return entityManager.createQuery("select u from " + clase.getSimpleName() + " u where u.dependencia = 0").getResultList();
    }

    @Override
    public List<UnidadAcademica> findAllx() {
        return entityManager.createQuery("select u from " + clase.getSimpleName() + " u ").getResultList();
    }

    @Override
    public List<UnidadAcademica> findAllAlphabOrder() {
        List<UnidadAcademica> list = new ArrayList<UnidadAcademica>();
        list = entityManager.createQuery("select u from " + clase.getSimpleName() + " u WHERE u.nivel.id = 1").getResultList();
        list.addAll(entityManager.createQuery("select u from " + clase.getSimpleName() + " u WHERE u.nivel.id = 2 ORDER BY u.nombreCorto").getResultList());
        return list;
    }

    //SQLINJECTION OK
    @Override
    public List<UnidadAcademica> getUnidadAcademicaPorOrigenRecursosProgramaBecaNivel(BigDecimal origenRecursos, BigDecimal programaBecaId, BigDecimal nivelId) {
        String sql = "select distinct(ua.id), ua.nombre, ua.nombreCorto, ua.clave, ua.nivel_id, ua.areasconocimiento_id"
                + " from cat_unidad_academica ua"
                + " inner join cat_nivel n on n.id = ua.nivel_id"
                + " inner join rmm_deposito_unidad_academica dua on dua.unidadacademica_id = ua.id"
                + " inner join cat_tipo_beca tb on dua.tipobeca_id = tb.id"
                + " inner join cat_programa_beca pb on tb.beca_id = pb.id"
                + " where n.id = ?1 and dua.correspondeipn = ?2 and pb.id = ?3"
                + " order by ua.id";
        List<Object[]> lista = executeNativeQuery(sql, nivelId, origenRecursos, programaBecaId);
        List<UnidadAcademica> lua = new ArrayList<UnidadAcademica>();
        for (Object[] res : lista) {
            UnidadAcademica ua = new UnidadAcademica();
            ua.setId((BigDecimal) res[0]);
            ua.setNombre((String) res[1]);
            ua.setNombreCorto((String) res[2]);
            ua.setClave((String) res[3]);
            //Nivel
            Nivel n = new Nivel();
            n.setId((BigDecimal) res[4]);
            ua.setNivel(n);
            //AreasConocimiento
            AreasConocimiento ac = new AreasConocimiento();
            ac.setId((BigDecimal) res[5]);
            ua.setAreasConocimiento(ac);
            lua.add(ua);
        }
        if (!lua.isEmpty()) {
            return lua;
        } else {
            return null;
        }
    }

    @Override
    public UnidadAcademica getByClave(String clave) {
        String jpql = "SELECT ua FROM UnidadAcademica ua WHERE ua.clave = ?1";
        List<UnidadAcademica> lista = executeQuery(jpql, clave);
        return lista == null || lista.isEmpty() ? null : lista.get(0);
    }

    @Override
    public UnidadAcademica getByNombreCorto(String nombreCorto) {
        String jpql = "SELECT ua FROM UnidadAcademica ua WHERE ua.nombreCorto = ?1";
        List<UnidadAcademica> lista = executeQuery(jpql, nombreCorto);
        return lista == null || lista.isEmpty() ? null : lista.get(0);
    }

    @Override
    public List<UnidadAcademica> getUnidadAcademicaPorNivel(BigDecimal nivelId) {
        String consulta = "SELECT u FROM UnidadAcademica u WHERE u.nivel.id = ?1";
        List<UnidadAcademica> lista = executeQuery(consulta, nivelId);
        return lista;
    }
}
