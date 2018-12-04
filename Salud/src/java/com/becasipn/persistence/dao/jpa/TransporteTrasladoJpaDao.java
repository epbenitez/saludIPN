package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.TransporteTrasladoDao;
import com.becasipn.persistence.model.TransporteTraslado;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Tania G. Sánchez
 */
public class TransporteTrasladoJpaDao extends JpaDaoBase<TransporteTraslado, BigDecimal> implements TransporteTrasladoDao {

    public TransporteTrasladoJpaDao() {
        super(TransporteTraslado.class);
    }

    @Override
    public List<TransporteTraslado> respuestasTraslado(BigDecimal transporteId) {
        String jpql = "select t from TransporteTraslado t where t.cuestionarioTransporte.id = ?1 order by t.id";
        List<TransporteTraslado> lista = executeQuery(jpql, transporteId);
        return lista;
    }

    @Override
    public void borrarRespuestasTraslado(BigDecimal cuestionarioTransporteId) {
        String jpql = "delete from TransporteTraslado where cuestionarioTransporte.id = ?1";
        Query query = getEntityManager().createQuery(jpql);
        query.setParameter(1, cuestionarioTransporteId);
        query.executeUpdate();
    }

    //SQLINJECTION
    @Override
    public Float gastoTraslado(BigDecimal usuarioId, BigDecimal periodoId) {
        String sql = "select nvl(sum(tt.costo),0)*21.5 gasto,0 from RMM_TRANSPORTE_TRASLADO tt\n"
                + "inner join ent_cuestionario_transporte tr on tr.id = tt.CUESTIONARIOTRANSPORTE_ID\n"
                + "where tr.USUARIO_ID = "+usuarioId+" \n"
                + "and tr.periodo_id =  "+periodoId+" ";
        List<Object[]> lista = executeNativeQuery(sql);
        Float gasto = lista==null || lista.isEmpty()?0F:new Float(lista.get(0)[0].toString());
        return gasto;
    }
}
