package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.VWConteoDepositosDao;
import com.becasipn.persistence.model.VWConteoDepositos;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez
 */
public class VWConteoDepositosJpaDao extends JpaDaoBase<VWConteoDepositos, BigDecimal> implements VWConteoDepositosDao {
    public VWConteoDepositosJpaDao() {
        super(VWConteoDepositos.class);
    }
    
    @Override
    public VWConteoDepositos depositosOtorgamiento (BigDecimal otorgamientoId) {
        String jpql = "select d from VWConteoDepositos d where d.otorgamiento.id = ?1";
        List<VWConteoDepositos> lista = executeQuery(jpql, otorgamientoId);
        return lista == null || lista.isEmpty() ? null : lista.get(0);
    }
}