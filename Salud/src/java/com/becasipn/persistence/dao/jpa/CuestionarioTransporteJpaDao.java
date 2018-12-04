package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.CuestionarioTransporteDao;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.CuestionarioTransporte;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Tania G. Sánchez
 */
public class CuestionarioTransporteJpaDao extends JpaDaoBase<CuestionarioTransporte, BigDecimal> implements CuestionarioTransporteDao{
    public CuestionarioTransporteJpaDao() {
        super(CuestionarioTransporte.class);
    }
    
    /**
     * Devuelve si el alumno ya contesto su ESE de transporte.
     *
     * @author Tania G. Sánchez
     * @param usuarioId
     * @param periodoId
     * @return lista
     */
    @Override
    public Boolean tieneEseTransporte(BigDecimal usuarioId, BigDecimal periodoId) {
        String jpql = "select c from CuestionarioTransporte c where c.usuario.id = ?1 and c.periodo.id = ?2";
        List<CuestionarioTransporte> lista = executeQuery(jpql, usuarioId, periodoId);
        return lista == null || lista.isEmpty() ? Boolean.FALSE : Boolean.TRUE;
    }
    
    /**
     * Devuelve las respuestas del alumno de su ESE de transporte.
     *
     * @author Tania G. Sánchez
     * @param usuarioId
     * @param periodoId
     * @return lista
     */
    @Override
    public CuestionarioTransporte respuestasTransporte(BigDecimal usuarioId, BigDecimal periodoId) {
        String jpql = "select c from CuestionarioTransporte c where c.usuario.id = ?1 and c.periodo.id = ?2";
        List<CuestionarioTransporte> lista = executeQuery(jpql, usuarioId, periodoId);
        return lista == null || lista.isEmpty() ? null : lista.get(0);
    }

    @Override
    public Boolean tieneEseTransporte(Alumno a) {
        String jpql = "select c from CuestionarioTransporte c,Alumno a "
                + "where c.usuario.id = a.usuario.id "
                + "and a.id =?1 "
                + "and c.periodo.estatus=1";
        List<CuestionarioTransporte> lista = executeQuery(jpql, a.getId());
        return lista == null || lista.isEmpty() ? Boolean.FALSE : Boolean.TRUE;
    }
    
    @Override
    public void borrarCuestionarioTransporte(BigDecimal cuestionarioTransporteId) {
        String jpql = "delete from CuestionarioTransporte where id = ?1";
        Query query = getEntityManager().createQuery(jpql);
        query.setParameter(1, cuestionarioTransporteId);
        query.executeUpdate();
    }
}