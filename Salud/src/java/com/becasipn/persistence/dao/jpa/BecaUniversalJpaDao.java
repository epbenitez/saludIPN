package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.model.BecaUniversal;
import com.becasipn.persistence.dao.BecaUniversalDao;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Query;
/**
 *
 * @author SISTEMAS
 */
public class BecaUniversalJpaDao extends JpaDaoBase<BecaUniversal, BigDecimal> implements BecaUniversalDao {
    public BecaUniversalJpaDao() {
        super(BecaUniversal.class);
    }
    
    /**
     * Devuelve true si el alumno se encuentra en el padron
     *
     * @param alumnoId
     * @return Boolean
     */
    @Override
    public Boolean existeEnPadron(String boleta) {
        StringBuilder consulta = new StringBuilder();
        consulta.append("SELECT COUNT(ID) FROM ENT_PADRON_BECA_UNIVERSAL")
                .append(" WHERE BOLETA = ?1")
                .append(" AND VISIBLE = 1");
        Long resultado = getCountNativeQuery(consulta.toString(), boleta);
        
        return resultado > 0;
    }
    
    /**
     * Devuelve true si el alumno se encuentra en el padron
     *
     * @param boleta
     * @return BecaUniversal
     */
    @Override
    public BecaUniversal getByBoleta(String boleta) {
        StringBuilder consulta = new StringBuilder();
        consulta.append("SELECT * FROM ENT_PADRON_BECA_UNIVERSAL")
                .append(" WHERE BOLETA = ?1")
                .append(" AND VISIBLE = 1");
        
        Query q = entityManager.createNativeQuery(consulta.toString(), BecaUniversal.class);
        q.setParameter(1, boleta);
        List<BecaUniversal> lista = q.getResultList();
        
        return lista == null || lista.isEmpty() ? null : lista.get(0);
    }
}
