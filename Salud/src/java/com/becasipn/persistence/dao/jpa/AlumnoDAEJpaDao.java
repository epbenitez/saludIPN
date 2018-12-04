/**
 * SISTEMA INFORMÁTICO DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE
 * SERVICIOS ESTUDIANTILES 2015
 *
 */
package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.AlumnoDAEDao;
import com.becasipn.persistence.model.AlumnoDAE;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.resource.spi.LocalTransactionException;

/**
 *
 * @author Patricia Benitez
 */
public class AlumnoDAEJpaDao extends JpaDaoBase<AlumnoDAE, BigDecimal> implements AlumnoDAEDao {

    public AlumnoDAEJpaDao() {
        super(AlumnoDAE.class);
    }

    @Override
    public List<AlumnoDAE> findByBoleta(String boleta) throws LocalTransactionException {
        Map parametros = new HashMap();
        parametros.put("BOLETA", boleta);
        return executeStoredProcedureGeneric("ALUMNOSINSCRITODAE",parametros);
    }

    @Override
    public List<AlumnoDAE> findByCurpManutencion(String curp) {
        String jpql = "SELECT d FROM AlumnoDAE d WHERE d.curp = ?1";
        return executeQuery(jpql, curp);
    }
	
	@Override
	public Boolean existeBoletaDAE(String boleta) {
		String sql = " select count(*) from VW_DAE where BOLETA = ?1 AND ESCUELA > 20 ";
		return getCountNativeQuery(sql, boleta).intValue() > 0;
	}
}
