package com.becasipn.persistence.dao;

import java.math.BigDecimal;

import com.becasipn.persistence.model.EstatusTarjetaBancaria;
import com.becasipn.persistence.model.Rol;
import java.util.List;

public interface EstatusTarjetaBancariaDao extends DaoBase<EstatusTarjetaBancaria, BigDecimal> {

    public String getEstatusTarjeta(String id);
    
    public List<EstatusTarjetaBancaria> posiblesEstatus(EstatusTarjetaBancaria estatus,Rol rol);
    
}
