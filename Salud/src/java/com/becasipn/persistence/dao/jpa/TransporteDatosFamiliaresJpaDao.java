package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.TransporteDatosFamiliaresDao;
import com.becasipn.persistence.model.TransporteDatosFamiliares;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Tania G. Sánchez
 */
public class TransporteDatosFamiliaresJpaDao extends JpaDaoBase<TransporteDatosFamiliares, BigDecimal> implements TransporteDatosFamiliaresDao{
    public TransporteDatosFamiliaresJpaDao() {
        super(TransporteDatosFamiliares.class);
    }
    
    @Override
    public List<TransporteDatosFamiliares> respuestasTransporte (BigDecimal transporteId) {
        String jpql = "select t from TransporteDatosFamiliares t where t.cuestionarioTransporte.id = ?1 order by t.id";
        List<TransporteDatosFamiliares> lista = executeQuery(jpql, transporteId);
        return lista;
    }
    
    //SQLINJECTION
    @Override
    public Float ingresoPercapita (BigDecimal transporteId) {
        Float ingresoPercapita = 0F;
        String sql = "select count(*), sum (aportacionmensual) from rmm_transporte_datos_fam where cuestionariotransporte_id = " + transporteId;
        List<Object[]> lista = executeNativeQuery(sql);
        for (Object[] res : lista) {
            Integer total = Integer.parseInt(res[0].toString());
            Float monto = Float.parseFloat(res[1].toString());
            ingresoPercapita = monto / total;
        }
        return ingresoPercapita;
    }
    
    @Override
    public HashMap<String, Float> ingesoEIntegrantes (BigDecimal transporteId) {
        HashMap<String, Float> map = new HashMap<>(); 
        Float ingresoPercapita = 0F;
        String sql = "select count(*), sum (aportacionmensual) from rmm_transporte_datos_fam where cuestionariotransporte_id = " + transporteId;
        List<Object[]> lista = executeNativeQuery(sql);
        for (Object[] res : lista) {
            Integer total = Integer.parseInt(res[0].toString());
            Float monto = Float.parseFloat(res[1].toString());
            ingresoPercapita = monto / total;
            map.put("integrantes", total.floatValue());
            map.put("ingresoMensual", monto);
            map.put("ingresoPerCapita", ingresoPercapita);
        }
        return map;
    }
    
    @Override
    public void borrarRespuestasDatosFamiliares(BigDecimal cuestionarioTransporteId) {
        String jpql = "delete from TransporteDatosFamiliares where cuestionarioTransporte.id = ?1";
        Query query = getEntityManager().createQuery(jpql);
        query.setParameter(1, cuestionarioTransporteId);
        query.executeUpdate();
    }
}