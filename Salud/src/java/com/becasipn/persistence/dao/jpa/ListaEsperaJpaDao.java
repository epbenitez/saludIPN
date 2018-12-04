package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.ListaEsperaDao;
import com.becasipn.persistence.model.ListaEspera;
import java.math.BigDecimal;
import java.util.List;

public class ListaEsperaJpaDao extends JpaDaoBase<ListaEspera, BigDecimal> implements ListaEsperaDao {

    public ListaEsperaJpaDao() {
	super(ListaEspera.class);
    }

    @Override
    public ListaEspera findBySolicitudESE(BigDecimal solicitudId) {
	String jpql = "select l from ListaEspera l "
		+ "join SolicitudBeca sb on l.solicitudBeca.id = sb.id and sb.id = ?1 "
		+ "and sb.cuestionario.id = 1 "
		+ "and l.vigente = 1 ";
	List<ListaEspera> lista = executeQuery(jpql, solicitudId);
	return lista == null || lista.isEmpty() ? null : lista.get(0);
    }

    @Override
    public ListaEspera findBySolicitud(BigDecimal solicitudId, boolean vigente) {
	String jpql = "select l from ListaEspera l"
		+ " join SolicitudBeca sb on l.solicitudBeca.id = sb.id and sb.id = ?1"
		+ (vigente ? " and l.vigente = 1 " : "");
	List<ListaEspera> lista = executeQuery(jpql, solicitudId);
	return lista == null || lista.isEmpty() ? null : lista.get(0);
    }    
    
    @Override
    public int quitarVigente(BigDecimal solicitudId){
        String sql = "UPDATE ENT_LISTA_ESPERA SET VIGENTE=0 WHERE SOLICITUDBECA_ID= ?1";
        return executeNativeUpdate(sql,solicitudId);
    }
    
    @Override
    public ListaEspera findBySolicitudProceso(BigDecimal solicitudId, BigDecimal procesoId){
        String jpql = "select l from ListaEspera l "
		+ " WHERE l.solicitudBeca.id=?1 "
                + " and l.proceso.id=?2";
	List<ListaEspera> lista = executeQuery(jpql, solicitudId,procesoId);
	return lista == null || lista.isEmpty() ? null : lista.get(0);
    }

    @Override
    public ListaEspera buscarUltimoTurno(BigDecimal periodoId, BigDecimal unidadAcademicaId) {
        String jpql = "select l from ListaEspera l "
                + "where l.proceso.periodo.id = ?1 "
                + "and l.proceso.unidadAcademica.id = ?2 "
                + "order by l.ordenamiento desc";
        
        List<ListaEspera> lista = executeQuery(jpql, periodoId, unidadAcademicaId);
        
        return lista == null || lista.isEmpty() ? null : lista.get(0);
    }
}
