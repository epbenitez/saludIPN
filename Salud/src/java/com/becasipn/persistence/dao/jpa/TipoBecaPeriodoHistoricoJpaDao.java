package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.TipoBecaPeriodoHistoricoDao;
import com.becasipn.persistence.model.TipoBecaPeriodoHistorico;
import java.math.BigDecimal;
import javax.persistence.Query;

/**
 *
 * @author Usre-05
 */
public class TipoBecaPeriodoHistoricoJpaDao extends JpaDaoBase<TipoBecaPeriodoHistorico, BigDecimal> implements TipoBecaPeriodoHistoricoDao {

    public TipoBecaPeriodoHistoricoJpaDao() {
        super(TipoBecaPeriodoHistorico.class);
    }
    
    @Override
    public void borrarBitacoraTBP(BigDecimal tbpId) {
        String jpql = "delete from TipoBecaPeriodoHistorico where tipoBecaPeriodo.id = ?1";
        Query query = getEntityManager().createQuery(jpql);
        query.setParameter(1, tbpId);
        query.executeUpdate();
    }
}
