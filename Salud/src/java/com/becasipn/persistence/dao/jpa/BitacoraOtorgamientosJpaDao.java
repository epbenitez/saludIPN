package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.BitacoraOtorgamientosDao;
import com.becasipn.persistence.model.BitacoraOtorgamientos;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez Manilla
 */
public class BitacoraOtorgamientosJpaDao extends JpaDaoBase<BitacoraOtorgamientos, BigDecimal> implements BitacoraOtorgamientosDao {

    public BitacoraOtorgamientosJpaDao() {
        super(BitacoraOtorgamientos.class);
    }
    
    /**
     * Regresa una lista de  registros de bitácora del otorgamiento indicado
     * @param otorgamiento_id
     * @return 
     */
    @Override
    public List<BitacoraOtorgamientos> getBitacora(BigDecimal otorgamiento_id) {
        String jpql = "SELECT bo FROM BitacoraOtorgamientos bo WHERE bo.otorgamiento.id=?1  ";
       
        List<BitacoraOtorgamientos> lista = executeQuery(jpql, otorgamiento_id);
        return ((lista == null || lista.isEmpty()) ? null : lista);
    }
    
    /**
     * Elimina todos los registros de bitácora del otorgamiento proporcionado
     * @param otorgamiento_id
     * @return 
     */
     @Override
    public int eliminaBitacora(BigDecimal otorgamiento_id) {
        String jpql = "DELETE FROM BitacoraOtorgamientos ob WHERE ob.otorgamiento.id = ?1";
        return executeUpdate(jpql, otorgamiento_id);
    }

}
