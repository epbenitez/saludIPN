package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.PadronProsperaDao;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.PadronProspera;
import java.math.BigDecimal;
import java.util.List;

public class PadronProsperaJpaDao extends JpaDaoBase<PadronProspera, BigDecimal> implements PadronProsperaDao {

    public PadronProsperaJpaDao() {
	super(PadronProspera.class);
    }

    @Override
    public Boolean existeAlumnoPeriodo(Alumno alumno) {
	String jpql = "SELECT p FROM PadronProspera p WHERE p.alumno.id = ?1 AND p.periodo.id = ?2";
	List<PadronProspera> lista = executeQuery(jpql, alumno.getId(), getDaos().getPeriodoDao().getPeriodoActivo().getId());
	return lista == null || lista.isEmpty() ? Boolean.FALSE : Boolean.TRUE;
    }
}
