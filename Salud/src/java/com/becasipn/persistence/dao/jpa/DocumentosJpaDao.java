package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.DocumentosDao;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.Documentos;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Victor Lozano
 */
public class DocumentosJpaDao extends JpaDaoBase<Documentos, BigDecimal> implements DocumentosDao {

    public DocumentosJpaDao() {
        super(Documentos.class);
    }

    @Override
    public Documentos documentosAlumnoPeriodoActual(Alumno a) {
        String sql = "SELECT d FROM Documentos d WHERE d.alumno.id = ?1 and d.periodo.estatus=1 ";
        List<Documentos> documentos = executeQuery(sql, a.getId());
        return documentos==null||documentos.isEmpty()?null:documentos.get(0);
    }

    @Override
    public Documentos documentosAlumnoPeriodo(Alumno a, BigDecimal periodoId) {
        String jpql = "SELECT d FROM Documentos d WHERE d.alumno.id=?1 and d.periodo.id=?2";
        List<Documentos> documentos= executeQuery(jpql, a.getId(),periodoId);
        return documentos==null||documentos.isEmpty()?null:documentos.get(0);
    }
}
