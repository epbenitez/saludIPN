package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.TipoBajasDetalleDao;
import com.becasipn.persistence.model.TipoBajasDetalle;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Usre-05
 */
public class TipoBajasDetalleJpaDao extends JpaDaoBase<TipoBajasDetalle, BigDecimal> implements TipoBajasDetalleDao {

    public TipoBajasDetalleJpaDao() {
        super(TipoBajasDetalle.class);
    }

    @Override
    public List<TipoBajasDetalle> findAllAct() {
        String consulta = "SELECT tbd FROM TipoBajasDetalle tbd"
                + " WHERE tbd.activo = 1";
        List<TipoBajasDetalle> lista = executeQuery(consulta);
        return lista;

    }

}
