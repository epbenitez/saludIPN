package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.MontoMaximoIngresosDao;
import com.becasipn.persistence.model.MontoMaximoIngresos;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez Manilla
 */
public class MontoMaximoIngresosJpaDao extends JpaDaoBase<MontoMaximoIngresos, BigDecimal> implements MontoMaximoIngresosDao {
    public MontoMaximoIngresosJpaDao() {
        super(MontoMaximoIngresos.class);
    }

    //Metódo para saber si existe un registro para el periodo y el tipo de ingreso mandado.
    @Override
    public boolean existeRegistro (BigDecimal periodoId, boolean deciles) {
	String consulta = "select m from MontoMaximoIngresos m"
		+ " where m.periodo.id = ?1 and m.deciles = ?2";
	List<MontoMaximoIngresos> lista = executeQuery(consulta, periodoId, deciles);
	return lista != null && !lista.isEmpty();
    }
}