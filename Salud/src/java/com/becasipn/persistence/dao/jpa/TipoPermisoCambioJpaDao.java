package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.TipoPermisoCambioDao;
import com.becasipn.persistence.model.TipoPermisoCambio;
import java.math.BigDecimal;

public class TipoPermisoCambioJpaDao extends JpaDaoBase<TipoPermisoCambio, BigDecimal> implements TipoPermisoCambioDao {

    public TipoPermisoCambioJpaDao() {
        super(TipoPermisoCambio.class);
    }

//    @Override
//    public TipoPermisoCambio findById(String id) {
//        TipoPermisoCambio tipoPermisoCambio = new TipoPermisoCambio();
//
//        String jpql = "SELECT tpc FROM TipoPermisoCambio tpc WHERE tpc.id = ?1";
//        tipoPermisoCambio = executeSingleQuery(jpql, id);
//        return tipoPermisoCambio;
//    }
}
 