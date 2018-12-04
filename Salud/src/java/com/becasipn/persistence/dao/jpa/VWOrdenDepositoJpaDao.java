package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.VWOrdenDepositoDao;
import com.becasipn.persistence.model.Nivel;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.UnidadAcademica;
import com.becasipn.persistence.model.VWOrdenDeposito;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Victor Lozano
 */
public class VWOrdenDepositoJpaDao extends JpaDaoBase<VWOrdenDeposito, BigDecimal> implements VWOrdenDepositoDao {

    public VWOrdenDepositoJpaDao() {
        super(VWOrdenDeposito.class);
    }

    /**
     * Devuelve el listado de las ordenes de deposito generadas en el sistema en
     * el periodo activo
     *
     * @author Victor Lozano
     * @return listado
     */
    @Override
    public List<VWOrdenDeposito> findOrdenesDepositoPeriodoActivo() {
        String consulta = "SELECT o FROM VWOrdenDeposito o WHERE o.periodo.id = ?1";
        List<VWOrdenDeposito> lista = executeQuery(consulta, getDaos().getPeriodoDao().getPeriodoActivo().getId());
        return lista;
    }
}
