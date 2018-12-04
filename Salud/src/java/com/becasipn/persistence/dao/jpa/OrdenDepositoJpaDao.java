package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.OrdenDepositoDao;
import com.becasipn.persistence.model.OrdenDeposito;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;

/**
 *
 * @author Victor Lozano
 */
public class OrdenDepositoJpaDao extends JpaDaoBase<OrdenDeposito, BigDecimal> implements OrdenDepositoDao {

    public OrdenDepositoJpaDao() {
        super(OrdenDeposito.class);
    }

    @Override
    public List<OrdenDeposito> existenOrdenesDepositoAsociados(BigDecimal periodoId) {
        String jpql = "select od from OrdenDeposito od where od.periodo.id = ?1";
        List<OrdenDeposito> lista = executeQuery(jpql, periodoId);
        return lista;
    }

    @Override
    public List<OrdenDeposito> asociadaUnidadAcademicaOrdenDeposito(BigDecimal unidadAcademicaId) {
        String jpql = "select od from OrdenDeposito od where od.unidadAcademica.id = ?1";
        List<OrdenDeposito> lista = executeQuery(jpql, unidadAcademicaId);
        return lista;
    }

    @Override
    public OrdenDeposito findByNombre(String nombre) {
        String jpql = "select od from OrdenDeposito od where od.nombreOrdenDeposito = ?1";
        List<OrdenDeposito> lista = executeQuery(jpql, nombre);
        return lista == null || lista.isEmpty() ? null : lista.get(0);
    }

    @Override
    public OrdenDeposito getOrdenDepositoBkUniversal(BigDecimal periodoId, BigDecimal nivelId) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT od FROM OrdenDeposito od")
                .append(" WHERE od.periodo.id = ?1")
                .append(" AND od.programaBeca.id = 10")
                .append(" AND od.nivel.id = ?2");
        List<OrdenDeposito> lista = executeQuery(jpql.toString(), periodoId, nivelId);
        return lista == null || lista.isEmpty() ? null : lista.get(0);
    }

    @Override
    public List<OrdenDeposito> ordenesDepositoPorEstatus(BigDecimal estatus) {
        String jpql = "select od from OrdenDeposito od where od.estatusDeposito.id = ?1";
        List<OrdenDeposito> lista = executeQuery(jpql, estatus);
        return lista;
    }

    /**
     * Si existe algun error en la creación de la orden de deposito se borra la
     * orden de deposito que fallo.
     *
     * @param ordenDepositoId
     */
    @Override
    public void borrarOrdenDeposito(BigDecimal ordenDepositoId) {
        String jpql = "delete from OrdenDeposito where id = ?1";
        Query query = getEntityManager().createQuery(jpql);
        query.setParameter(1, ordenDepositoId);
        query.executeUpdate();
    }

    /**
     *
     * @param estatus 1-Pendientes
     * @param clavesPeriodos Ejemplo: '2017-1','2017-2','2018-1'
     * @return
     */
    @Override
    public List<Object[]> pivotOrdenesPendientesPorEstatus(BigDecimal estatus, String clavesPeriodos) {
        String consulta = "select * from (  \n"
                + "  select \n"
                + "  p.CLAVE periodo,\n"
                + "  DECODE(tb.cuenta, 0, 'Tarjeta',\n"
                + "                    1, 'Cuenta',\n"
                + "                    2, 'Referencia',\n"
                + "                    3, 'CLABE') mediodeposito,\n"
                + "  count(distinct(od.id)) conteo, \n"
                + "  tb.cuenta\n"
                + "  from ent_tarjeta_bancaria tb \n"
                + "  join ent_deposito d ON d.tarjetabancaria_id = tb.id \n"
                + "  join ent_orden_deposito od ON od.id = d.ordendeposito_id \n"
                + "  join cat_periodo p on p.id = od.periodo_id\n"
                + "  where od.ESTATUSDEPOSITO_ID = ?1 "
                + "  group by p.CLAVE, tb.cuenta\n"
                + "  order by 1\n"
                + " ) \n"
                + " PIVOT (sum(conteo) FOR periodo IN(" + clavesPeriodos + "))";
        List<Object[]> lista = executeNativeQuery(consulta, estatus, clavesPeriodos);
        return (lista == null || lista.isEmpty() ? null : lista);
    }

    @Override
    public List<LinkedHashMap<String, Object>> ordenesPendientesPorCuenta(Integer cuenta) {
        LinkedHashMap results = new LinkedHashMap();
        StringBuilder sb = new StringBuilder();
        List<String> llaves = new ArrayList();

        sb.append(" SELECT od.id, p.clave, od.nombreOrdenDeposito, od.estatusDeposito.id,  COUNT(d.id) total, COUNT(despera.id) espera");
        sb.append(" FROM TarjetaBancaria tb");
        sb.append(" JOIN Depositos d ON tb = d.TarjetaBancaria");
        sb.append(" JOIN OrdenDeposito od ON od = d.ordenDeposito");
        sb.append(" JOIN od.periodo p");
        sb.append(" LEFT JOIN Depositos despera");
        sb.append(" ON ((despera.estatusDeposito.id = 1) AND (despera.TarjetaBancaria = tb))");
        sb.append(" WHERE tb.cuenta = ?1");
        sb.append(" GROUP BY od.id, p.clave, od.periodo.id, od.nombreOrdenDeposito, od.estatusDeposito.id");
        sb.append(" ORDER BY p.clave DESC, espera DESC");

        llaves.add("id");
        llaves.add("periodo");
        llaves.add("nombreOrden");
        llaves.add("estsDeposit");
        llaves.add("total");
        llaves.add("enEspera");
        
        List<Object[]> result = executeQueryObject(sb.toString(), cuenta);

        return result != null || !result.isEmpty() ? creaModelo(result, llaves) : null;
    }
}
