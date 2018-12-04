package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.DepositosDao;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.Depositos;
import com.becasipn.persistence.model.OrdenDeposito;
import com.becasipn.persistence.model.Otorgamiento;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.TarjetaBancaria;
import com.becasipn.persistence.model.Usuario;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;

/**
 *
 * @author Patricia Benítez
 */
public class DepositosJpaDao extends JpaDaoBase<Depositos, BigDecimal> implements DepositosDao {

    public DepositosJpaDao() {
        super(Depositos.class);
    }

    /**
     * Devuelve el listado de depositos asociados a una orden de desposito
     *
     * @param id
     * @return lista
     */
    @Override
    public List<Object[]> findByOrdenDeposito(BigDecimal id) {
        String consulta = "select '001 Banamex', '03 Plasticos', t.numtarjetabancaria,"
                + " replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace("
                + " replace(replace(replace(replace(replace(replace(replace(replace(replace(replace("
                + " replace(replace(replace(replace(replace( UPPER(trim(a.nombre))||','||UPPER(trim(a.apellidopaterno))||'/'||UPPER(trim(a.apellidomaterno))"
                + " ,'À','A'),'È','E'),'Ì','I'),'Ò','O'),'Ù','U'),'Á','A'),'É','E'),'Í','I'),'Ó','O'),'Ú','U')"
                + " ,'Ñ','@'),'Ë','E'),'-',' '),'.',''),'Ü','U'),'Û','U'),'_',' '),'¿','@'),'°',' '),'ª',' ')"
                + " ,'(',' '),')',' '),'º',' '),'*',' '),'#',' '),'?','@'),'Ö','O'),'´',''),"
                + " '01', to_char(d.monto,'9999.0'), concat(lpad(d.id,10,'0'), tb.clave)"
                + " from ent_deposito d"
                + " inner join ent_alumno a on a.id = d.alumno_id"
                + " inner join ent_tarjeta_bancaria t on d.tarjetabancaria_id = t.id"
                + " inner join ent_otorgamientos o on o.id = d.otorgamiento_id"
                + " inner join ent_tipo_beca_periodo tbp on tbp.id = o.tipobecaperiodo_id"
                + " inner join cat_tipo_beca tb on tb.id = tbp.tipobeca_id"
                + " where d.ordenDeposito_id = ?1";
        List<Object[]> lista = executeNativeQuery(consulta, id);
        return lista;
    }

    /**
     * Devuelve el listado de depositos asociados a una orden de desposito.
     * (Nuevo layout)
     *
     * @param id
     * @param rechazados
     * @return lista
     */
    @Override
    public List<Object[]> obtenerOrdenDeposito(BigDecimal id, boolean rechazados) {
        String consulta = "select case t.cuenta when 1 then concat(concat(substrb(t.numtarjetabancaria,0,4),'/'),substrb(t.numtarjetabancaria,5))"
                + " else to_char (t.numtarjetabancaria) end,"
                + " trim(TRANSLATE( a.nombre,'ÀÈÌÒÙÁÉÍÓÚË-.ÜÛ_¿°ª()º*#?Ñ][;+ÂÐÃ‘','AEIOUAEIOUE  UU          N     NN ' )), "
                + " trim(TRANSLATE( a.APELLIDOPATERNO,'ÀÈÌÒÙÁÉÍÓÚË-.ÜÛ_¿°ª()º*#?Ñ][;+ÂÐÃ‘','AEIOUAEIOUE  UU          N     NN ' )), "
                + " trim(TRANSLATE( a.APELLIDOMATERNO,'ÀÈÌÒÙÁÉÍÓÚË-.ÜÛ_¿°ª()º*#?Ñ][;+ÂÐÃ‘','AEIOUAEIOUE  UU          N     NN ' )),"
                + " to_char(d.monto,'9999.0') as importe, '1', lpad(d.id,15,'0'), '', 'Mismo Día', "
                + (rechazados ? " odanterior.nombreordendeposito, " : "")
                + " a.boleta,"
                + " ua.nombrecorto unidadacademica "
                + " from ent_deposito d"
                + " inner join ent_alumno a on a.id = d.alumno_id"
                + " inner join ent_tarjeta_bancaria t on d.tarjetabancaria_id = t.id"
                + " inner join ent_otorgamientos o on o.id = d.otorgamiento_id"
                + " inner join ent_tipo_beca_periodo tbp on tbp.id = o.tipobecaperiodo_id"
                + " inner join cat_tipo_beca tb on tb.id = tbp.tipobeca_id"
                + " inner join ENT_ALUMNO_DATOS_ACADEMICOS daa on daa.id = o.DATOSACADEMICOS_ID "
                + " inner join cat_unidad_academica ua on ua.id = daa.unidadacademica_id "
                + (rechazados ? " inner join ent_orden_deposito od on od.id = d.ordendeposito_id"
                        + " inner join ent_deposito da on da.alumno_id = d.alumno_id and da.otorgamiento_id = d.otorgamiento_id and d.id <> da.id"
                        + " and da.id in (select min(dd.id) from ent_deposito dd inner join ent_orden_deposito oda on oda.id = dd.ordendeposito_id"
                        + " where  oda.mes = od.mes and dd.alumno_id = d.alumno_id and dd.otorgamiento_id = d.otorgamiento_id and d.id <> dd.id and dd.estatusdeposito_id = 10)"
                        + " inner join ent_orden_deposito odanterior on odanterior.id = da.ordendeposito_id "
                        : "")
                + " where d.ordenDeposito_id = ?1"
                + " order by a.boleta ";
        List<Object[]> lista = executeNativeQuery(consulta, id);
        return lista;
    }

    /**
     * Devuelve el deposito que coincide con la orden de deposito, el número de
     * referncia (id del deposito), el número de tarjeta y el monto.
     *
     * @param ordenId
     * @param referencia
     * @param tarjeta
     * @param monto
     * @return ld
     */
    @Override
    public Depositos depositoCargaRespuesta(BigDecimal ordenId, String referencia, String tarjeta, Float monto) {
        String sql = "select d.* from ent_orden_deposito od "
                + " inner join ent_deposito d on od.id = d.ordendeposito_id"
                + " inner join ent_alumno a on a.id = d.alumno_id"
                + " inner join rmm_alumno_tarjeta_bancaria atb on a.id = atb.alumno_id"
                + " inner join ent_tarjeta_bancaria tb on tb.id = atb.tarjetabancaria_id"
                + " where od.id = ?1 and d.id = ?2"
                + " and tb.numtarjetabancaria = ?3 and d.monto = ?4";

        List<Depositos> depositos = executeNativeQuery(sql, Depositos.class, ordenId, referencia, tarjeta, monto);

        return depositos == null || depositos.isEmpty() ? null : depositos.get(0);
    }

    /**
     * Devuelve la lista de los depositos rechazados para una orden de deposito.
     *
     * @param ordenId
     * @return lista
     */
    @Override
    public List<Depositos> depositosRechazados(BigDecimal ordenId) {
        String jpql = "select d from Depositos d where d.estatusDeposito.id = 4 and d.ordenDeposito.id = ?1";
        List<Depositos> lista = executeQuery(jpql, ordenId);
        return lista;
    }

    /**
     * Devuelve la lista de depositos para un alumno. Opcional se puede
     * especificar periodo y otorgamiento.
     *
     * @author Mario Márquez
     * @param alumnoId
     * @param periodoId
     * @param otorgamientoId
     * @return List<Depositos>
     */
    //SQLINJECTION OK
    @Override
    public List<Depositos> depositosAlumno(BigDecimal alumnoId, BigDecimal periodoId, BigDecimal otorgamientoId) {
        StringBuilder sb = new StringBuilder();
        StringBuilder orEstatus = new StringBuilder();
        List<String> constraints = new ArrayList<>();
        Map<String, Object> params = new HashMap();

        orEstatus.append("(d.estatusDeposito.id = 2");
        orEstatus.append(" or d.estatusDeposito.id = 4");
        orEstatus.append(" or d.estatusDeposito.id = 11");
        orEstatus.append(" or d.estatusDeposito.id = 10)");

        constraints.add("d.alumno.id = :alumnoId");
        params.put("alumnoId", alumnoId);
        constraints.add(orEstatus.toString());
        if (periodoId != null) {
            constraints.add("d.ordenDeposito.periodo.id = :periodoId");
            params.put("periodoId", periodoId);
        }
        if (otorgamientoId != null) {
            constraints.add("d.otorgamiento.id = :otorgamientoId");
            params.put("otorgamientoId", otorgamientoId);
        }

        sb.append("select d ");
        sb.append("from Depositos d");
        agregaCriterios(sb, constraints);
        sb.append(" order by ");
        sb.append(" d.ordenDeposito.periodo.id desc,");
        sb.append(" d.fechaDeposito desc,");
        sb.append(" d.ordenDeposito.mes desc");

        List<Depositos> lista = executeQuery(sb.toString(), params, null);
        return lista;
    }

    /**
     * Si existe algun error en la creación de la orden de deposito se borra
     * todo lo que se inserto antes del error.
     *
     * @param ordenDepositoId
     */
    @Override
    public void borrarDeposito(BigDecimal ordenDepositoId) {
        String jpql = "delete from Depositos where ordenDeposito.id = ?1";
        Query query = getEntityManager().createQuery(jpql);
        query.setParameter(1, ordenDepositoId);
        query.executeUpdate();
    }

    /**
     * Devuelve true si existe un deposito para el mes y periodo seleccionado.
     *
     * @param mes
     * @param periodoId
     * @param otorgamientoId
     * @return ld
     */
    @Override
    public Boolean tieneDepositoMesPeriodo(BigDecimal mes, BigDecimal periodoId, BigDecimal otorgamientoId) {
        String jpql = "select d from Depositos d where d.estatusDeposito.id in (1,2,6)"
                + " and d.ordenDeposito.mes = ?1 and d.ordenDeposito.periodo.id = ?2 and d.otorgamiento.id = ?3";
        List<Depositos> lista = executeQuery(jpql, mes, periodoId, otorgamientoId);
        return lista == null || lista.isEmpty() ? Boolean.FALSE : Boolean.TRUE;
    }

    @Override
    public Integer totalDepositos(BigDecimal ordenId) {
        String consulta = "select * from ent_deposito where ordendeposito_id = " + ordenId;
        List<Object[]> lista = executeNativeQuery(consulta);
        return lista.size();
    }

    /**
     * Devuelve el listado de depositos incluyendo datos del SUBES asociados a
     * una orden de desposito.
     *
     * @param ordenId
     * @param folioBeca
     * @param subes
     * @param periodo
     * @return lista
     */
    //SQLINJECTION
    @Override
    public List<Object[]> ordenDepositoSUBES(BigDecimal ordenId, String folioBeca, String subes, Periodo periodo) {
        String p = "";
        if (periodo.getClave().substring(5, 6).equals("1")) {
            p = " and ps.periodo_id = od.periodo_id ";
        } else {
            p = " and ps.periodo_id in (select periodo_id from ent_padron_subes where (periodo_id = " + periodo.getId()
                    + " or periodo_id = " + periodo.getPeriodoAnterior().getId() + ") and rownum = 1 and alumno_id = a.id)";
        }
        String consulta = "select case t.cuenta when 1 then concat(concat(substrb(t.numtarjetabancaria,0,4),'/'),substrb(t.numtarjetabancaria,5))"
                + " else to_char (t.numtarjetabancaria) end, replace(a.nombre, '-', ' '), replace(a.apellidopaterno, '-', ' '), replace(a.apellidomaterno, '-', ' '),"
                + " to_char(d.monto,'9999.0') as importe, '1', lpad(d.id,15,'0'), '', 'Mismo Día', decode(ps.curp, null, 'Sin datos', ps.curp),"
                + folioBeca + " cs.descripcion from ent_deposito d"
                + " inner join ent_orden_deposito od on od.id = d.ordendeposito_id"
                + " inner join ent_alumno a on a.id = d.alumno_id"
                + " inner join ent_tarjeta_bancaria t on d.tarjetabancaria_id = t.id"
                + " inner join ent_otorgamientos o on o.id = d.otorgamiento_id"
                + " inner join ent_tipo_beca_periodo tbp on tbp.id = o.tipobecaperiodo_id"
                + " inner join cat_tipo_beca tb on tb.id = tbp.tipobeca_id"
                + " left join ent_padron_subes ps on ps.alumno_id = a.id"
                + p + subes
                + " left join cat_convocatoria_subes cs on cs.id = ps.convocatoria_id"
                + " where od.id = " + ordenId + " and od.periodo_id = " + periodo.getId();
        List<Object[]> lista = executeNativeQuery(consulta);
        return lista;
    }

    /**
     * Devuelve el listado de depositos incluyendo modalidad asociados a una
     * orden de desposito.
     *
     * @param ordenId
     * @return lista
     */
    @Override
    public List<Object[]> ordenDepositoModalidad(BigDecimal ordenId) {
        String consulta = "select case t.cuenta when 1 then concat(concat(substrb(t.numtarjetabancaria,0,4),'/'),substrb(t.numtarjetabancaria,5))"
                + " else to_char (t.numtarjetabancaria) end, replace(a.nombre, '-', ' '), replace(a.apellidopaterno, '-', ' '), replace(a.apellidomaterno, '-', ' '),"
                + " to_char(d.monto,'9999.0') as importe, '1', lpad(d.id,15,'0'), '', 'Mismo Día', m.nombre"
                + " from ent_deposito d"
                + " inner join ent_alumno a on a.id = d.alumno_id"
                + " inner join ent_tarjeta_bancaria t on d.tarjetabancaria_id = t.id"
                + " inner join ent_otorgamientos o on o.id = d.otorgamiento_id"
                + " inner join ent_alumno_datos_academicos da on da.id = o.datosacademicos_id"
                + " inner join cat_modalidad m on m.id = da.modalidad_id"
                + " inner join ent_tipo_beca_periodo tbp on tbp.id = o.tipobecaperiodo_id"
                + " inner join cat_tipo_beca tb on tb.id = tbp.tipobeca_id"
                + " where d.ordenDeposito_id = ?1";
        List<Object[]> lista = executeNativeQuery(consulta, ordenId);
        return lista;
    }

    /**
     * Devuelve true si existen depositos en espera para la orden de deposito.
     *
     * @param ordenId
     * @return Boolean
     */
    @Override
    public Boolean ordenConDepositosEnEspera(BigDecimal ordenId) {
        String consulta = "select  d.* from ENT_ORDEN_DEPOSITO od\n"
                + " inner join ent_deposito d on od.id = d.ORDENDEPOSITO_ID\n"
                + " where od.id = ?1 \n"
                + " and d.ESTATUSDEPOSITO_ID = 1";
        List<Object[]> lista = executeNativeQuery(consulta, ordenId);
        return lista == null || lista.isEmpty() ? Boolean.FALSE : Boolean.TRUE;
    }

    @Override
    public List<Depositos> depositosAlumnoReferencia(BigDecimal idAlumno, BigDecimal idTarjeta, BigDecimal idPeriodo) {
        String consulta = "select d.* from ent_deposito d "
                + "inner join ENT_ORDEN_DEPOSITO od on od.id = d.ORDENDEPOSITO_ID "
                + "inner join ENT_TARJETA_BANCARIA tb on tb.id = d.TARJETABANCARIA_ID and tb.cuenta = 2 "
                + "where " //d.ESTATUSDEPOSITO_ID = 1  and "
                + " d.ALUMNO_ID = ?1 "
                + "and d.TARJETABANCARIA_ID = ?2 "
                + "and od.periodo_id = ?3 ";
        Query q = entityManager.createNativeQuery(consulta, Depositos.class);
        q.setParameter(1, idAlumno);
        q.setParameter(2, idTarjeta);
        q.setParameter(3, idPeriodo);
        List<Depositos> lista = q.getResultList();
        return lista == null || lista.isEmpty() ? null : lista;
    }

    /**
     * @param alumnoId
     * @return lista
     */
    //SQLINJECTION OK
    @Override
    public List<LinkedHashMap<String, Object>> depositosReferenciados(BigDecimal alumnoId) {

        StringBuilder sb = new StringBuilder();
        Map<String, Object> params = new HashMap();

        List<String> columnas = new ArrayList<>();
        columnas.add("tb.numtarjetabancaria");
        columnas.add("od.mes");
        columnas.add("ctb.nombre");
        columnas.add("d.monto");
        columnas.add("p.clave");

        sb.append(" select ");
        agregaColumnas(sb, columnas);
        sb.append(" from ent_deposito d ");
        sb.append(" inner join ent_alumno a on d.alumno_id = a.id ");
        sb.append(" inner join ent_tarjeta_bancaria tb on tb.id = d.tarjetabancaria_id ");
        sb.append(" and tb.cuenta = 2 ");
        sb.append(" inner join RMM_ALUMNO_TARJETA_BANCARIA atb on tb.id = atb.TARJETABANCARIA_ID ");
        sb.append(" and atb.ESTATUSTARJBANC_ID = 17 ");
        sb.append(" inner join ent_orden_deposito od on od.id = d.ordendeposito_id ");
        sb.append(" and od.periodo_id = 34 ");
        sb.append(" inner join ent_otorgamientos o on o.id = d.otorgamiento_id ");
        sb.append(" inner join ent_tipo_beca_periodo tbp on tbp.id = o.tipobecaperiodo_id ");
        sb.append(" inner join cat_tipo_beca ctb on ctb.id = tbp.tipobeca_id ");
        sb.append(" inner join cat_periodo p on p.ID = od.periodo_id ");
        sb.append(" where d.estatusdeposito_id = 1 and a.id = #alumnoId ");
        sb.append(" and excluirdeposito = 0 ");
        sb.append(" order by o.tipobecaperiodo_id, od.mes ");

        params.put("alumnoId", alumnoId);

        List<LinkedHashMap<String, Object>> resultados = executeNativeQuery(sb.toString(), params, columnas, null);

        return resultados == null || resultados.isEmpty() ? null : resultados;
    }

    /**
     * @param alumnoId
     * @return Boolean
     */
    @Override
    public Boolean tieneDepositosReferenciados(BigDecimal alumnoId) {
        String consulta = "select count(d.ID) from ent_deposito d"
                + " inner join ent_alumno a on d.alumno_id = a.id"
                + " inner join ent_tarjeta_bancaria tb on tb.id = d.tarjetabancaria_id and tb.cuenta = 2"
                + " inner join RMM_ALUMNO_TARJETA_BANCARIA atb on tb.id = atb.TARJETABANCARIA_ID and atb.ESTATUSTARJBANC_ID = 17"
                + " inner join ent_orden_deposito od on od.id = d.ordendeposito_id and od.periodo_id = 34"
                + " inner join ent_otorgamientos o on o.id = d.otorgamiento_id and o.excluirdeposito = 0"
                + " where d.estatusdeposito_id = 1 and a.id = ?1";
        Long resultado = getCountNativeQuery(consulta, alumnoId);
        return resultado > 0;
    }

    /**
     * @param idOtorgamiento
     * @return lista
     */
    @Override
    public List<Depositos> depositosOtorgamiento(BigDecimal idOtorgamiento) {
        String jpql = "select d from Depositos d where d.estatusDeposito.id  in (1,2) "
                + "and d.otorgamiento.id=?1";
        List<Depositos> lista = executeQuery(jpql, idOtorgamiento);
        return lista;
    }

    /**
     * Devuelve una lista con los estatus y la cantidad de depositos que se
     * encuentran en ese estatus para una orden de deposito.
     *
     * @param ordenId
     * @return Boolean
     */
    @Override
    public List<Object[]> depositosPorEstatus(BigDecimal ordenId) {
        String consulta = "select ed.nombre, count(*) from ent_deposito d"
                + " inner join cat_estatus_deposito ed on ed.id = d.estatusdeposito_id"
                + " where ordendeposito_id = ?1 group by ed.nombre";
        List<Object[]> lista = executeNativeQuery(consulta, ordenId);
        return lista;
    }

    @Override
    public List<Object[]> reporteDepositosMensual(int periodoId, int nivelId, int uAId, int mesId, int pbId) {
        Map<String, Object> params = new HashMap();

        StringBuilder sb = new StringBuilder();
        List<String> columnas = new ArrayList<>();
        List<String> criteria = new ArrayList<>();

        // Inicia Query de otorgamientos
        StringBuilder subQotorgamientos = new StringBuilder();
        List<String> subColumnasO = new ArrayList<>();
        List<String> criteriaSubO = new ArrayList<>();

        subColumnasO.add("o.id otorgamientoId");
        subColumnasO.add("d.id depositoId");
        subColumnasO.add("o.alumno_id");
        subColumnasO.add("o.tipobecaperiodo_id");
        subColumnasO.add("o.proceso_id");
        subColumnasO.add("o.excluirdeposito");

        subQotorgamientos.append("SELECT ");
        agregaColumnas(subQotorgamientos, subColumnasO);
        subQotorgamientos.append(" FROM vw_otorgamientos o");
        subQotorgamientos.append(" LEFT JOIN ent_deposito d");
        subQotorgamientos.append("  ON o.id = d.otorgamiento_id");
        subQotorgamientos.append(" LEFT JOIN ent_orden_deposito od");
        subQotorgamientos.append("  ON od.id = d.ordendeposito_id");
        if (mesId != 0) {
            subQotorgamientos.append("  AND od.mes = #mesId");
            params.put("mesId", mesId);
        }
        criteriaSubO.add("o.periodo_id = #periodoId");
        params.put("periodoId", periodoId);
        criteriaSubO.add("o.proceso_id IS NOT NULL");
        agregaCriterios(subQotorgamientos, criteriaSubO);
        subQotorgamientos.append(" ORDER  BY o.id, d.id");

        // Termina query de otorgamientos
        // Inicia subquery todo
        StringBuilder subQ = new StringBuilder();
        List<String> subColumnas = new ArrayList<>();

        subColumnas.add("Max(do.depositoid) depositoId");
        subColumnas.add("do.otorgamientoid");
        subColumnas.add("do.alumno_id");
        subColumnas.add("do.tipobecaperiodo_id");
        subColumnas.add("do.proceso_id");
        subColumnas.add("do.excluirdeposito");

        subQ.append("SELECT ");
        agregaColumnas(subQ, subColumnas);
        subQ.append(" FROM (");
        subQ.append(subQotorgamientos);
        subQ.append(") do");
        subQ.append(" GROUP  BY do.otorgamientoid, do.alumno_id,");
        subQ.append(" do.tipobecaperiodo_id, do.proceso_id, do.excluirdeposito");
        // Termina subquery 

        // Inicia query principal
        columnas.add("n.NOMBRE \"Nivel académico\"");
        columnas.add("ua.NOMBRECORTO \"Unidad académica\"");
        columnas.add("a.BOLETA \"Boleta\"");
        columnas.add("a.CURP \"CURP\"");
        columnas.add("a.APELLIDOPATERNO || ' ' || a.APELLIDOMATERNO || ' ' || a.NOMBRE \"Nombre\"");
        columnas.add("ed.nombre  \"Estatus\"");
        columnas.add("CASE WHEN ed.ID = 4 THEN TO_CHAR(dep.ERRORBANAMEX_ID) ELSE 'No aplica' end \"Código Error Banamex\"");
        columnas.add("CASE WHEN ed.ID = 4 THEN errb.ERROR ELSE 'No aplica' end \"Error Banamex\"");
        columnas.add(decodeBoolean("todo.EXCLUIRDEPOSITO", "Excluir Depósito")); // aquí
        columnas.add("Decode(Nvl(tbanc.id, 0), 0, 'No', 'Sí') \"Con Cuenta Bancaria\"");
        columnas.add("cep.NOMBRE \"Estatus de Proceso\"");
        columnas.add("tb.NOMBRE \"Beca Asignada\"");
        columnas.add("dep.fechadeposito \"Fecha Depósito\"");
        columnas.add(" CASE etb.cuenta  WHEN 0 THEN 'Tarjeta'  WHEN 1 THEN 'Cuenta'  WHEN 2 THEN 'Referencia'  WHEN 3  THEN 'CLABE' else  'Sin forma de pago'  END \"Tipo de Cuenta\"");

        sb.append(" SELECT ");
        agregaColumnas(sb, columnas);

        sb.append(" FROM (");
        sb.append(subQ);
        sb.append(" ) todo");
        sb.append(" INNER JOIN ENT_ALUMNO a");
        sb.append(" ON a.ID = todo.ALUMNO_ID");
        sb.append(" LEFT JOIN ent_deposito dep");
        sb.append("  ON dep.id = todo.depositoid");
        sb.append(" LEFT JOIN CAT_ERRORES_BANAMEX errb");
        sb.append(" ON errb.ID = dep.ERRORBANAMEX_ID");
        sb.append(" LEFT JOIN      cat_estatus_deposito ed");
        sb.append(" ON        ed.id = dep.estatusdeposito_id");
        sb.append(" JOIN      ent_tipo_beca_periodo tbp");
        sb.append(" ON        tbp.id = todo.tipobecaperiodo_id");
        sb.append(" JOIN      cat_tipo_beca tb");
        sb.append(" ON        tb.id = tbp.tipobeca_id");
        sb.append(" JOIN      cat_programa_beca pb");
        sb.append(" ON        pb.id = tb.beca_id");
        sb.append(" JOIN      ent_proceso p");
        sb.append(" ON        p.id = todo.proceso_id");
        sb.append(" JOIN      CAT_ESTATUS_PROCESO cep");
        sb.append(" ON        p.PROCESOESTATUS_ID = cep.ID");
        sb.append(" LEFT JOIN      rmm_alumno_tarjeta_bancaria tbanc");
        sb.append("  ON        tbanc.ALUMNO_ID = todo.alumno_id");
        sb.append("  AND       tbanc.ESTATUSTARJBANC_ID = 13");
        sb.append(" LEFT JOIN ENT_TARJETA_BANCARIA etb ");
        sb.append("  on etb.id = tbanc.TARJETABANCARIA_ID ");
        sb.append(" LEFT JOIN cat_unidad_academica ua");
        sb.append(" ON        ua.id = p.unidadacademica_id");
        sb.append(" INNER JOIN CAT_NIVEL n");
        sb.append(" ON n.ID = ua.NIVEL_ID");

        criteria.add("pb.id NOT IN(10)");

        if (nivelId != 0) {
            criteria.add("N.ID = #nivelId");
            params.put("nivelId", nivelId);
        }
        if (uAId != 0) {
            criteria.add("UA.ID = #uAId");
            params.put("uAId", uAId);
        }
        if (pbId != 0) {
            criteria.add("pb.ID = #pbId");
            params.put("pbId", pbId);
        }
        agregaCriterios(sb, criteria);
        // Termina query principal

        List<Object[]> result = executeNativeQuery(sb.toString(), params, null);

        return result;
    }

    public List<Object[]> ReportePagos(int periodoId, int nivelId, int unidadAcademicaId, int pbId, String clavePeriodo) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> params = new HashMap();
        List<String> criteria = new ArrayList<>();
        sb.append(" SELECT * FROM (  ");
        sb.append(" SELECT n.nombre nivel, ua.nombrecorto, a.boleta, a.curp, a.nombre nombres, ");
        sb.append(" a.apellidopaterno || ' ' || a.apellidomaterno apellidos,  ");
        sb.append(" b.nombre programabeca, tb.nombre tipobeca, tp.nombre tipoproceso, ep.nombre estatusproceso, tjb.numtarjetabancaria cuenta, ");
        sb.append(" etb.nombre estatustarjetabancaria, ");
        sb.append(" Decode (o.excluirdeposito, 0, 'No', 1, 'Si') EXCLUIRDEPOSITO, ");
        sb.append(" cio.nombre, ");
        sb.append(" tbp.monto_oficial montomensual, (tbp.monto_oficial * tbp.duracion) montosemestral, ");
        sb.append(" od.mes, ");
        sb.append("  nvl((CASE WHEN d.estatusdeposito_id = 2 THEN TO_CHAR(d.monto) ELSE NULL END ),edd.nombre) monto, ");
        sb.append(" d.fechadeposito, ");
        sb.append(" Sum(CASE  WHEN d.estatusdeposito_id = 2 THEN d.monto  ELSE 0  END) ");
        sb.append(" OVER (  partition BY o.id)  AS total , ");
        sb.append(" (tbp.monto_oficial * tbp.duracion) -  (Sum(CASE  WHEN d.estatusdeposito_id = 2 THEN d.monto   ELSE 0   END) ");
        sb.append(" OVER (  partition BY o.id)  ) as adeudo ");
        sb.append(" FROM   vw_otorgamientos o ");
        sb.append(" JOIN ent_alumno a ON a.id = o.alumno_id ");
        sb.append(" JOIN ent_proceso p ON p.id = o.proceso_id ");
        sb.append(" JOIN cat_tipo_proceso tp ON tp.id = p.tipoproceso_id ");
        sb.append(" JOIN cat_estatus_proceso ep ON ep.id = p.procesoestatus_id ");
        sb.append(" JOIN cat_unidad_academica ua ON ua.id = p.unidadacademica_id ");
        sb.append(" JOIN cat_nivel n ON ua.nivel_id = n.id ");
        sb.append(" JOIN ent_tipo_beca_periodo tbp ON tbp.id = o.tipobecaperiodo_id ");
        sb.append(" JOIN cat_tipo_beca tb ON tb.id = tbp.tipobeca_id ");
        sb.append(" JOIN cat_programa_beca b ON b.id = tb.beca_id ");
        sb.append(" LEFT JOIN ent_deposito d ON d.alumno_id = a.id and d.OTORGAMIENTO_ID = o.id ");
        sb.append(" LEFT join ENT_ORDEN_DEPOSITO od on od.id = d.ORDENDEPOSITO_ID ");
        sb.append(" LEFT JOIN cat_estatus_deposito edd ON d.estatusdeposito_id = edd.id ");
        sb.append(" LEFT JOIN VW_ULTIMA_TARJETA atb ON atb.alumno_id = a.id ");
        sb.append(" LEFT JOIN cat_estatus_tarjeta_bancaria etb ON etb.id = atb.estatustarjbanc_id ");
        sb.append(" LEFT JOIN ent_tarjeta_bancaria tjb ON tjb.id = atb.tarjetabancaria_id ");
        sb.append(" JOIN cat_identificador_otorgamiento cio ON cio.id = o.IDENTIFICADOROTORGAMIENTO_ID ");

        criteria.add("o.PERIODO_ID = #periodoId");
        params.put("periodoId", periodoId);

        if (nivelId != 0) {
            criteria.add("N.ID = #nivelId");
            params.put("nivelId", nivelId);
        }
        if (unidadAcademicaId != 0) {
            criteria.add("UA.ID = #uAId");
            params.put("uAId", unidadAcademicaId);
        }
        if (pbId != 0) {
            criteria.add("b.ID = #pbId");
            params.put("pbId", pbId);
        }

        agregaCriterios(sb, criteria);

        sb.append(" ORDER BY ua.id, ua.nombrecorto, a.nombre ");
        
        if (clavePeriodo.equals("2")) {
            sb.append(" ) PIVOT ( min(monto), min(fechadeposito) AS deposito FOR (mes, mes) IN ( (2,2) Febrero, ");
            sb.append(" (3,3) Marzo, ");
            sb.append(" (4,4) Abril, ");
            sb.append(" (5,5) Mayo, ");
            sb.append(" (6,6) Junio, ");
            sb.append(" (7,7) Julio)) ");
        } else {
            sb.append(" ) PIVOT ( min(monto), min(fechadeposito) AS deposito FOR (mes, mes) IN ( (8,8) Agosto, ");
            sb.append(" (9,9) Septiembre, ");
            sb.append(" (10,10) Octubre, ");
            sb.append(" (11,11) Noviembre, ");
            sb.append(" (12,12) Diciembre, ");
            sb.append(" (1,1) Enero)) ");
        }

        List<Object[]> lista = executeNativeQuery(sb.toString(), params, null);

        return lista.isEmpty() ? null : lista;
    }
}
