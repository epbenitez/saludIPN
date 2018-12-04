package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.TipoBecaDao;
import com.becasipn.persistence.model.PadronSubes;
import com.becasipn.persistence.model.TipoBeca;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Usre-05
 */
public class TipoBecaJpaDao extends JpaDaoBase<TipoBeca, BigDecimal> implements TipoBecaDao {

    public TipoBecaJpaDao() {
        super(TipoBeca.class);
    }

    @Override
    public List<TipoBeca> tipoBecaEnUso(BigDecimal tipoBeca) {

        String jpql = "SELECT tbp.id FROM TipoBecaPeriodo tbp WHERE tbp.tipoBeca.id = ?1";
        List<TipoBeca> lista = executeQuery(jpql, tipoBeca);

        return lista;
    }

    @Override
    public TipoBeca getByNombre(String tipoBeca) {
        String jpql = "SELECT tb FROM TipoBeca tb WHERE tb.nombre = ?1";
        List<TipoBeca> lista = executeQuery(jpql, tipoBeca);
        return lista == null || lista.isEmpty() ? null : lista.get(0);
    }

    @Override
    public TipoBeca siguienteTipoBeca(TipoBeca anterior, PadronSubes padronSubes) {
        String jpql = "SELECT c.tipoBeca FROM TipoBecaPeriodo c "
                + " WHERE c.tipoBeca.beca.id=?1 and c.convocatoriaManutencion =?2  "
                + " and c.periodo.id = ?3 "
                + " ORDER BY c.tipoBeca.orden";
        List<TipoBeca> tipos = executeQuery(jpql, anterior.getBeca().getId(), padronSubes.getConvocatoriaSubes().getId(), padronSubes.getPeriodo().getId());
        if (tipos==null || tipos.isEmpty() ){
            return null;
        }
        if (anterior.getOrden() < tipos.size()) {
            return tipos.get(anterior.getOrden());
        }
        //return null;
        return tipos.get(tipos.size() - 1);
    }

    @Override
    public List<TipoBeca> findByNivel(BigDecimal nivelId) {
        String jpql = "SELECT DISTINCT tb FROM TipoBeca tb, TipoBecaPeriodo tbp"
                + " WHERE tbp.tipoBeca.id = tb.id"
                + " AND tbp.nivel.id = ?1"
                + " order by tb.nombre";
        List<TipoBeca> lista = executeQuery(jpql, nivelId);
        return lista;
    }

    @Override
    public List<TipoBeca> getTipoBecaPorBeca(BigDecimal becaId) {
        String consulta = "SELECT tb FROM TipoBeca tb WHERE tb.beca.id = ?1";
        List<TipoBeca> lista = executeQuery(consulta, becaId);
        return lista;
    }
}
