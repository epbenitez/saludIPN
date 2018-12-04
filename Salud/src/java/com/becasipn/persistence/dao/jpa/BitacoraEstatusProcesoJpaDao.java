package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.BitacoraEstatusProcesoDao;
import com.becasipn.persistence.model.BitacoraEstatusProceso;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Usre-05
 */
public class BitacoraEstatusProcesoJpaDao extends JpaDaoBase<BitacoraEstatusProceso, BigDecimal> implements BitacoraEstatusProcesoDao {

    public BitacoraEstatusProcesoJpaDao() {
        super(BitacoraEstatusProceso.class);
    }

    @Override
    public List<BitacoraEstatusProceso> getByProceso(BigDecimal proceso) {
        String jpql = "SELECT b FROM BitacoraEstatusProceso b WHERE b.proceso.id = ?1";
        List<BitacoraEstatusProceso> lista = executeQuery(jpql, proceso);
        return lista;
    }
}
