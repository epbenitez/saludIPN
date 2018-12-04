
package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.BitacoraOtorgamientoExternoDao;
import com.becasipn.persistence.model.BitacoraOtorgamientoExterno;
import com.becasipn.persistence.model.OtorgamientoExterno;
import java.math.BigDecimal;
import java.util.List;

public class BitacoraOtorgamientoExternoJpaDao extends JpaDaoBase<BitacoraOtorgamientoExterno,BigDecimal>
    implements BitacoraOtorgamientoExternoDao{
    
    public BitacoraOtorgamientoExternoJpaDao(){
        super(BitacoraOtorgamientoExterno.class);
    }

     @Override
    public List<BitacoraOtorgamientoExterno> bitacoraOtorgamientoExterno(OtorgamientoExterno oe) {
        String consulta = "SELECT oe FROM BitacoraOtorgamientoExterno oe "
                + " WHERE oe.otorgamientoExterno.id=?1 ";
        List<BitacoraOtorgamientoExterno> lista = executeQuery(consulta, oe.getId());
        return lista == null || lista.isEmpty()?null: lista;
    }
    
    
    
}
