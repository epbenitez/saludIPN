package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.BecaDao;
import com.becasipn.persistence.model.Beca;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author User-03
 */
public class BecaJpaDao extends JpaDaoBase<Beca, BigDecimal> implements BecaDao {

    public BecaJpaDao() {
        super(Beca.class);
    }

    //SQLINJECTION
    @Override
    public List<Beca> getProgramaBecaPorOrigenRecursos(BigDecimal origenDeposito) {
        String sql = "select distinct(pb.nombre), pb.id, pb.clave from cat_programa_beca pb "
                + "inner join cat_tipo_beca tb on pb.id = tb.beca_id "
                + "inner join rmm_deposito_unidad_academica dua on tb.id = dua.tipobeca_id "
                + "where correspondeipn=" + origenDeposito;
        List<Object[]> lista = executeNativeQuery(sql);
        List<Beca> lb = new ArrayList<Beca>();
        for (Object[] res : lista) {
            Beca b = new Beca();
            b.setNombre((String) res[0]);
            b.setId((BigDecimal) res[1]);
            b.setClave((String) res[2]);
            lb.add(b);
        }
        if (!lb.isEmpty()) {
            return lb;
        } else {
            return null;
        }
    }

    @Override
    public List<Beca> getTodasExceptuando(List<BigDecimal> becaIds) {
        String jpql = "select pb from Beca pb where pb.id not in(";
        List<Beca> becas;
        for (BigDecimal id : becaIds) {
            if (id.equals(becaIds.get(becaIds.size() - 1))) {
                jpql = jpql + id + ")";
            } else {
                jpql = jpql + id + ",";
            }
        }
        becas = executeQuery(jpql);
        return becas.isEmpty() ? null : becas;
    }

    @Override
    public List<Beca> becasSinTransportePorPeriodoNivel(BigDecimal periodoId, BigDecimal nivelId, Boolean fundacion) {
        //USUARIOS DE PRUEBA, CAMBIAMOS EL ID DEL NIVEL del 4 al 1
        nivelId = nivelId.compareTo(new BigDecimal(4))==0?new BigDecimal(2):nivelId;
        String sql = "select distinct(pb.nombre), pb.id, pb.clave from ent_tipo_beca_periodo tbp"
                + " inner join cat_tipo_beca tb on tb.id = tbp.tipobeca_id"
                + " inner join cat_programa_beca pb on pb.id = tb.beca_id"
                + " where tbp.periodo_id = ?1 and pb.id in (15)" //+ (fundacion ? " and pb.id in (2, 3, 4)" : " and pb.id not in (7,8,9,10)")
                + (nivelId != null ? " and tbp.nivel_id = ?2" : "")
                + " order by pb.id";
        List<Object[]> lista = executeNativeQuery(sql, periodoId, nivelId);
        List<Beca> lb = new ArrayList<Beca>();
        for (Object[] res : lista) {
            Beca b = new Beca();
            b.setNombre((String) res[0]);
            b.setId((BigDecimal) res[1]);
            b.setClave((String) res[2]);
            lb.add(b);
        }
        return lb.isEmpty() ? null : lb;
    }

    // SQLINJECTION OK
    @Override
    public List<Beca> becasPorNivel(BigDecimal nivelId) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT DISTINCT pb");
        jpql.append(" FROM TipoBecaPeriodo tbp");
        jpql.append(" JOIN TipoBeca tb ON tb = tbp.tipoBeca");
        jpql.append(" JOIN Beca pb ON pb.id = tb.beca.id");
        jpql.append(" WHERE tbp.nivel.id = :nivelId");
        jpql.append(" AND pb.id BETWEEN 1 AND 6");
        jpql.append(" ORDER BY pb.id");

        Query q = entityManager.createQuery(jpql.toString());
        q.setParameter("nivelId", nivelId);

        List<Beca> lb = new ArrayList<Beca>();
        lb = q.getResultList();
        return lb.isEmpty() ? null : lb;
    }

    /**
     * Este método consigue los programa de beca a los que un alumno puede competir,
     * sin tomar en cuenta los requisitos. Esto se hace según el nivel del alumno
     * y el periodo actual
     * Author: Mario Márquez
     *
     * @param nivelId 
     * @param periodoId 
     * @return Lista de objetos Beca
     */
    @Override
    public List<Beca> programasPorNivelPeriodo(BigDecimal nivelId, BigDecimal periodoId) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT DISTINCT pb");
        jpql.append(" FROM TipoBecaPeriodo tbp");
        jpql.append(" JOIN tbp.tipoBeca tb ");
        jpql.append(" JOIN tb.beca pb");
        jpql.append(" WHERE tbp.nivel.id = :nivelId");
        jpql.append(" AND tbp.periodo.id = :periodoId");
        jpql.append(" ORDER BY pb.id");

        Query q = entityManager.createQuery(jpql.toString());
        q.setParameter("nivelId", nivelId);
        q.setParameter("periodoId", periodoId);

        List<Beca> lb;
        lb = q.getResultList();
        return lb.isEmpty() ? null : lb;
    }
}
