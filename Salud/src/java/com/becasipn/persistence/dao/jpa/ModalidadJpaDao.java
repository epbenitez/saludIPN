package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.ModalidadDao;
import com.becasipn.persistence.model.Modalidad;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author User-03
 */
public class ModalidadJpaDao extends JpaDaoBase<Modalidad, BigDecimal> implements ModalidadDao {

    public ModalidadJpaDao() {
        super(Modalidad.class);
    }

    @Override
    public List<Modalidad> findAllActive() {
        String jpql = "SELECT m FROM Modalidad m WHERE m.activo = 1";
        List<Modalidad> lista = executeQuery(jpql);
        return lista;
    }
}
