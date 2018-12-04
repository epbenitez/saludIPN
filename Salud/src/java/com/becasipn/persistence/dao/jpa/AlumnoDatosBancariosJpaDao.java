package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.AlumnoDatosBancariosDao;
import com.becasipn.persistence.model.AlumnoDatosBancarios;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez
 */
public class AlumnoDatosBancariosJpaDao extends JpaDaoBase<AlumnoDatosBancarios, BigDecimal> implements AlumnoDatosBancariosDao{
    public AlumnoDatosBancariosJpaDao() {
	super(AlumnoDatosBancarios.class);
    }
    
    @Override
   public AlumnoDatosBancarios datosBancarios(BigDecimal alumnoId) {
       String jpql = "select a from AlumnoDatosBancarios a where a.alumno.id = ?1 and a.vigente = 1";
       List<AlumnoDatosBancarios> lista = executeQuery(jpql, alumnoId);
       return lista.isEmpty() || lista == null ? null : lista.get(0);
   }
    
    @Override
    public List<AlumnoDatosBancarios> listaDatosBancarios(BigDecimal alumnoId) {
        String jpql = "select a from AlumnoDatosBancarios a where a.alumno.id = ?1";
        List<AlumnoDatosBancarios> lista = executeQuery(jpql, alumnoId);
        return lista.isEmpty() || lista == null ? null : lista;
    }
}