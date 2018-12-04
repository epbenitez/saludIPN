package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.EstatusTarjetaBancariaDao;
import com.becasipn.persistence.model.EstatusTarjetaBancaria;
import com.becasipn.persistence.model.Rol;
import java.math.BigDecimal;
import java.util.List;

public class EstatusTarjetaBancariaJpaDao extends JpaDaoBase<EstatusTarjetaBancaria, BigDecimal> implements EstatusTarjetaBancariaDao {

    public EstatusTarjetaBancariaJpaDao() {
        super(EstatusTarjetaBancaria.class);
    }

    @Override
    public String getEstatusTarjeta(String id) {
        String consulta = "SELECT e FROM EstatusTarjetaBancaria e "
                + "WHERE e.id = ?1";
        List<EstatusTarjetaBancaria> lista = executeQuery(consulta, id);
        return lista == null || lista.isEmpty() ? null : lista.get(0).getNombre();
    }
    
    @Override
    public List<EstatusTarjetaBancaria> posiblesEstatus(EstatusTarjetaBancaria estatus,Rol rol){
        String consulta = "SELECT c.estatusNuevo FROM EstatusTarjetaRol c "
                + " WHERE c.estatus.id=?1 "
                + " AND c.rol.id=?2 ORDER BY c.orden";
        return executeQuery(consulta,estatus.getId(),rol.getId());
    }
}
