package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.ProcesoProgramaBecaDao;
import com.becasipn.persistence.model.ProcesoProgramaBeca;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Usre-05
 */
public class ProcesoProgramaBecaJpaDao extends JpaDaoBase<ProcesoProgramaBeca, BigDecimal> implements ProcesoProgramaBecaDao {

	public ProcesoProgramaBecaJpaDao() {
		super(ProcesoProgramaBeca.class);
	}

	@Override
	public Boolean existe(String id, BigDecimal proceso) {
		String consulta = "SELECT p FROM ProcesoProgramaBeca p "
				+ "WHERE p.programaBeca.id = ?1 "
				+ "AND p.proceso.id = ?2 ";
		List<ProcesoProgramaBeca> lista = executeQuery(consulta, new BigDecimal(id), proceso);
		return lista != null && !lista.isEmpty();
	}

	@Override
	public Boolean existe(BigDecimal programaBecaId, BigDecimal procesoId) {
		String consulta = "SELECT p FROM ProcesoProgramaBeca p "
				+ "WHERE p.programaBeca.id = ?1 "
				+ "AND p.proceso.id = ?2 ";
		List<ProcesoProgramaBeca> lista = executeQuery(consulta, programaBecaId, procesoId);
		return lista != null && !lista.isEmpty();
	}

	@Override
	public List<ProcesoProgramaBeca> getByProceso(BigDecimal proceso) {
		String consulta = "SELECT p FROM ProcesoProgramaBeca p "
				+ "WHERE p.proceso.id = ?1 ";
		List<ProcesoProgramaBeca> lista = executeQuery(consulta, proceso);
		return lista == null || lista.isEmpty() ? null : lista;
	}

	@Override
	public List<ProcesoProgramaBeca> noUsadas(String ids, BigDecimal proceso) {
		String consulta = "SELECT p FROM ProcesoProgramaBeca p "
				+ "WHERE p.programaBeca.id not in (" + ids + ") "
				+ "AND p.proceso.id = ?1 ";
		List<ProcesoProgramaBeca> lista = executeQuery(consulta, proceso);
		return lista == null || lista.isEmpty() ? null : lista;
	}

	@Override
	public Boolean soloManutencion(BigDecimal proceso) {
		String sql = "select pb.PROCESO_ID from rmm_proceso_programa_beca pb "
				+ "where pb.PROGRAMABECA_ID in (5,7) "
				+ "and pb.PROCESO_ID not in ( "
				+ "		select px.PROCESO_ID from rmm_proceso_programa_beca px "
				+ "		where px.PROGRAMABECA_ID in (1,2,3,4,6,8,9,10) "
				+ "		and px.PROCESO_ID = pb.PROCESO_ID "
				+ ") "
				+ "and pb.PROCESO_ID = ?1";
		List<Object[]> lista = executeNativeQuery(sql, proceso);
		return lista != null && !lista.isEmpty();
	}

}
