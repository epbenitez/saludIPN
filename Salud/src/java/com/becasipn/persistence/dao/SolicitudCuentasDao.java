package com.becasipn.persistence.dao;

import java.math.BigDecimal;

import com.becasipn.persistence.model.SolicitudCuentas;
import com.becasipn.util.PaginateUtil;
import com.becasipn.util.ServerSideUtil;

public interface SolicitudCuentasDao extends DaoBase<SolicitudCuentas, BigDecimal> {

    public SolicitudCuentas findByIdentificador(String identificador);
    
    public boolean existeIdentificador(String identificador);
    
    public PaginateUtil getListado(ServerSideUtil ssu);

}
