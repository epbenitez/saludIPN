package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.TarjetaBancariaDao;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.TarjetaBancaria;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Victor Lozano
 */
public class TarjetaBancariaJpaDao extends JpaDaoBase<TarjetaBancaria, BigDecimal> implements TarjetaBancariaDao {
    public TarjetaBancariaJpaDao() {
        super(TarjetaBancaria.class);
    }

    /**
     * Devuelve el numero de tarjeta bancaria correspondiente a un alumno
     *
     * @author Victor Lozano
     * @param id
     * @return numeroTarjeta
     */
    @Override
    public String numeroTarjetaAlumno(Long id) {
        String consulta = "SELECT a.tarjetaBancaria FROM  AlumnoTarjetaBancaria a "
                + "WHERE a.alumno.id = ?1";
        List<TarjetaBancaria> lista = executeQuery(consulta, id);
        return lista == null || lista.isEmpty() ? null : lista.get(0).getNumtarjetabancaria();
    }

    @Override
    public List<String> getLotes() {
        String consulta = "SELECT DISTINCT(lote) FROM ent_tarjeta_bancaria";
        List<Object[]> lista = executeNativeQuery(consulta);
        List<String> numeros = new ArrayList<>();
        if (lista == null || lista.isEmpty()) {
        } else {
            for (Object elemento : lista) {
                if (elemento == null) {
                    continue;
                }
                numeros.add(elemento.toString());
            }
            consulta = "SELECT MAX(lote) FROM ent_tarjeta_bancaria";
            lista = executeNativeQuery(consulta);
            int nuevo = 0;
            for (Object elemento : lista) {
                nuevo = Integer.parseInt(elemento.toString()) + 1;
            }
        }
        Collections.sort(numeros, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.valueOf(o1) - Integer.valueOf(o2);
            }
        });
        return numeros;
    }

    /**
     * Devuelve los numeros de lotes para las opciones de carga
     *
     * @author Victor Lozano
     * @return numerosLotes
     */
    @Override
    public List<String> getOpcionesLotes() {
        String consulta = "SELECT DISTINCT(lote) FROM ent_tarjeta_bancaria "
                + "ORDER BY CAST(lote AS NUMBER) DESC";
        List<Object[]> lista = executeNativeQuery(consulta);
        List<String> numeros = new ArrayList<>();
        if (lista == null || lista.isEmpty()) {
            numeros.add("Nuevo (1)");
        } else {
            String maximo = "SELECT MAX(CAST(lote AS NUMBER)) FROM ent_tarjeta_bancaria";
            List<Object[]> loteMaximo = executeNativeQuery(maximo);
            int nuevo = 1;
            for (Object elemento : loteMaximo) {
                nuevo = Integer.parseInt(elemento.toString()) + 1;
            }
            numeros.add("Nuevo (" + nuevo + ")");

            for (Object elemento : lista) {
                if (elemento == null) {
                    continue;
                }
                numeros.add(elemento.toString());
            }

        }
        return numeros;
    }

    /**
     * Valida si existe una tarjeta bancaria
     *
     * @author Victor Lozano
     * @param numero
     * @return true si existe
     */
    @Override
    public boolean existeTarjeta(String numero) {
        String consulta = "select t from TarjetaBancaria t where t.numtarjetabancaria = ?1";
        List<TarjetaBancaria> tarjetas = executeQuery(consulta, numero);
        return !tarjetas.isEmpty();
    }

    /**
     * Devuelve el total de numeros de tarjeta bancaria para asignar
     *
     * @author Victor Lozano
     * @return total
     */
    @Override
    public String countTarjetasDisponibles() {
        String consulta = "SELECT COUNT(tb) FROM TarjetaBancaria tb "
                + "    JOIN BitacoraTarjetaBancaria btc ON tb.id = btc.tarjetaBancaria.id "
                + "WHERE btc.fechaModificacion = (SELECT MAX (btcm.fechaModificacion) "
                + "    FROM BitacoraTarjetaBancaria btcm "
                + "    WHERE btcm.tarjetaBancaria.id = btc.tarjetaBancaria.id ) "
                + "AND btc.estatus.id = 1 "
                + "AND NOT EXISTS (SELECT (1) FROM AlumnoTarjetaBancaria atb WHERE atb.tarjetaBancaria.id=tb.id )";
        return getCountQuery(consulta).toString();
    }

    /**
     * Devuelve las tarjetas bancarias para asignar
     *
     * @author Victor Lozano
     * @return tarjetas
     */
    @Override
    public List<TarjetaBancaria> getTarjetasDisponibles(int num) {
        String consulta = "SELECT tb FROM TarjetaBancaria tb "
                + "    JOIN BitacoraTarjetaBancaria btc ON tb.id = btc.tarjetaBancaria.id "
                + "WHERE btc.fechaModificacion = (SELECT MAX (btcm.fechaModificacion) "
                + "    FROM BitacoraTarjetaBancaria btcm "
                + "    WHERE btcm.tarjetaBancaria.id = btc.tarjetaBancaria.id ) "
                + "AND btc.estatus.id = 1 "
                + "AND NOT EXISTS (SELECT (1) FROM AlumnoTarjetaBancaria atb WHERE atb.tarjetaBancaria.id=tb.id )"
                + "ORDER BY tb.secuencia";
        return executeQuery(num, consulta);
    }

    /**
     * Devuelve los datos de una asignacion
     *
     * @author Victor Lozano
     * @param identificador
     * @return asignadas
     */
    @Override
    public List<Object[]> datosAsignaciones(String identificador) {
        String consulta = "SELECT COUNT(*), bt.fechamodificacion, bt.observaciones, bt.enviocorreo "
                + "FROM rmm_alumno_tarjeta_bancaria at "
                + "JOIN rmm_bitacora_tarjeta_bancaria bt ON at.tarjetaBancaria_id = bt.tarjetaBancaria_id "
                + "WHERE at.identificador LIKE '%?1%' "
                + "AND bt.tarjetabancariaestatus_id = 2 "
                + "GROUP BY bt.fechamodificacion, bt.observaciones, bt.enviocorreo";
        List<Object[]> lista = executeNativeQuery(consulta, identificador);
        return lista;
    }

    @Override
    public List<TarjetaBancaria> tarjetaAnterior(String identificador) {
        String jpql = "SELECT btb.tarjetaBancaria FROM BitacoraTarjetaBancaria btb "
                + " WHERE btb.observaciones LIKE ?1 and btb.estatus.id = 5";
        return executeQuery(jpql, "%" + identificador + "%");
    }

    @Override
    public TarjetaBancaria tarjetaAnterior(String identificador, Alumno a) {
        String jpql = "SELECT btb.tarjetaBancaria FROM BitacoraTarjetaBancaria btb "
                + " JOIN AlumnoTarjetaBancaria atb ON atb.tarjetaBancaria.id=btb.tarjetaBancaria.id"
                + " WHERE btb.observaciones LIKE ?1 and btb.estatus.id = 5 and atb.alumno.id=?2";
        List<TarjetaBancaria> list = executeQuery(jpql, "%" + identificador + "%", a.getId());
        return list == null || list.isEmpty() ? null : list.get(0);
    }

    @Override
    public TarjetaBancaria findByNumeroCuenta(String num) {
        String jpql = "SELECT t FROM TarjetaBancaria t WHERE t.numtarjetabancaria=?1";
        List<TarjetaBancaria> list = executeQuery(jpql, num.toString());
        return list == null || list.isEmpty() ? null : list.get(0);
    }

    @Override
    public TarjetaBancaria findReferenciaByNumeroCuenta(String num) {
        String jpql = "SELECT t FROM TarjetaBancaria t WHERE t.numtarjetabancaria=?1 and t.cuenta = 2";
        List<TarjetaBancaria> list = executeQuery(jpql, num);
        return list == null || list.isEmpty() ? null : list.get(0);
    }

    /**
     * Valida si el alumno ya cuenta con una cuenta Banamex creada por el IPN.
     *
     * @param tarjetaId
     * @return true si existe
     */
    @Override
    public boolean alumnoConCuentaBanamex(BigDecimal tarjetaId) {
        String consulta = "select t from TarjetaBancaria t where t.cuenta = 1 and t.origen is null and t.id = ?1";
        List<TarjetaBancaria> tarjetas = executeQuery(consulta, tarjetaId);
        return !tarjetas.isEmpty();
    }

    @Override
    public List<LinkedHashMap<String, Object>> findCuentasBySolicitud(BigDecimal solicitudId) {
        StringBuilder sb = new StringBuilder();
        
        List<String> columnas = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        
        columnas.add("nombreCorto");
        columnas.add("boleta");
        columnas.add("nombre");
        columnas.add("tarjeta");
        columnas.add("tipo");
        columnas.add("estatus");
        
        sb.append(" SELECT UA.NOMBRECORTO, A.BOLETA, ");
        sb.append(" A.APELLIDOPATERNO||' '||A.APELLIDOMATERNO||' '||A.NOMBRE AS NOMBRE, TB.NUMTARJETABANCARIA,");
        sb.append(" DECODE(TB.CUENTA, 0, 'Tarjeta', 1, 'Cuenta', 2, 'Referencia', 3, 'CLABE') AS TIPO,");
        sb.append(" ETB.NOMBRE AS ESTATUS");
        sb.append(" FROM ENT_TARJETA_BANCARIA TB");
        sb.append(" JOIN RMM_ALUMNO_TARJETA_BANCARIA ATB ON ATB.TARJETABANCARIA_ID = TB.ID");
        sb.append(" JOIN CAT_ESTATUS_TARJETA_BANCARIA ETB ON ETB.ID = ATB.ESTATUSTARJBANC_ID");
        sb.append(" JOIN ENT_ALUMNO A ON A.ID = ATB.ALUMNO_ID");
        sb.append(" JOIN (SELECT * FROM ENT_ALUMNO_DATOS_ACADEMICOS DA");
        sb.append(" WHERE DA.ID = (");
        sb.append(" SELECT MAX(ID) FROM ENT_ALUMNO_DATOS_ACADEMICOS WHERE ALUMNO_ID = DA.ALUMNO_ID GROUP BY ALUMNO_ID)");
        sb.append(" )  ADA ON ADA.ALUMNO_ID = A.ID");
        sb.append(" JOIN CAT_UNIDAD_ACADEMICA UA ON UA.ID = ADA.UNIDADACADEMICA_ID");
        sb.append(" JOIN ENT_SOLICITUD_CUENTAS SC ON SC.ID = ATB.SOLICITUDCUENTA_ID");
        sb.append(" WHERE SC.ID = #solicitudId");
        sb.append(" ORDER BY 1, 2");
        
        map.put("solicitudId", solicitudId);
        
        return executeNativeQuery(sb.toString(), map, columnas, null);
    }
}