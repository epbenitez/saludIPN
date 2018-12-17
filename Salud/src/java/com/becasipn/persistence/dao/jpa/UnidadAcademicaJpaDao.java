package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.UnidadAcademicaDao;
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
}
