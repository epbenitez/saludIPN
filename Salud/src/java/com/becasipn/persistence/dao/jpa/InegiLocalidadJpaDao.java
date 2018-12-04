package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.InegiLocalidadDao;
import com.becasipn.persistence.model.InegiLocalidad;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Tania Gabriela Sánchez
 */
public class InegiLocalidadJpaDao extends JpaDaoBase<InegiLocalidad, BigDecimal> implements InegiLocalidadDao {

    public InegiLocalidadJpaDao() {
        super(InegiLocalidad.class);
    }
    
    @Override
    public List<InegiLocalidad> localidadPorEntidadMunicipio (BigDecimal estadoId, BigDecimal municipioId) {
	String consulta = "select l from InegiLocalidad l where l.entidad.id = ?1 and l.municipio.id = ?2";
	List<InegiLocalidad> lista = executeQuery(consulta, estadoId, municipioId);
	return lista == null || lista.isEmpty() ? null : lista;
    }
}
