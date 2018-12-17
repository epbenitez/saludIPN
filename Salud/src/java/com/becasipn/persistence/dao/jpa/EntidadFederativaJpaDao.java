package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.EntidadFederativaDao;
import com.becasipn.persistence.model.EntidadFederativa;
import java.math.BigDecimal;
import java.util.List;

/**
 * Implementación de las operaciones CRUD de las entidades federativas.
 *
 * @author Patricia Benitez
 * @version $Rev: 1169 $
 * @since 1.0
 */
public class EntidadFederativaJpaDao extends JpaDaoBase<EntidadFederativa, BigDecimal> implements EntidadFederativaDao {

    /**
     * Crea una instancia de una <code>EntidadFederativaJpaDao</code>.
     */
    public EntidadFederativaJpaDao() {
        super(EntidadFederativa.class);
    }

    @Override
    public List<EntidadFederativa> findTotal() {
        String jpql = "SELECT estado FROM EntidadFederativa estado";
        List<EntidadFederativa> estados = executeQuery(jpql);

        return estados == null || estados.isEmpty() ? null : estados;
    }
}
