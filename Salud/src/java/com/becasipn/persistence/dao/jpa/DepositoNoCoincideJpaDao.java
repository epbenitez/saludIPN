package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.DepositoNoCoincideDao;
import com.becasipn.persistence.model.DepositoNoCoincide;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez
 */
public class DepositoNoCoincideJpaDao extends JpaDaoBase<DepositoNoCoincide, Long> implements DepositoNoCoincideDao {

    public DepositoNoCoincideJpaDao() {
        super(DepositoNoCoincide.class);
    }

    /**
     * Devuelve la lista de los depositos sin coincidencias para una orden de
     * deposito.
     *
     * @param ordenId
     * @return lista
     */
    @Override
    public List<DepositoNoCoincide> depositosSinCoincidencias(Long ordenId) {
        String jpql = "select d from DepositoNoCoincide d where d.ordenDeposito.id = ?1";
        List<DepositoNoCoincide> lista = executeQuery(jpql, ordenId);
        return lista;
    }
}
