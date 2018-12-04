package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.DepositoNoCoincide;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez
 */
public interface DepositoNoCoincideDao extends DaoBase<DepositoNoCoincide, Long> {

    public List<DepositoNoCoincide> depositosSinCoincidencias(Long ordenId);
}
