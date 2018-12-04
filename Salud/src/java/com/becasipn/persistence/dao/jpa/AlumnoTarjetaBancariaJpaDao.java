package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.AlumnoTarjetaBancariaDao;
import com.becasipn.persistence.model.AlumnoTarjetaBancaria;
import com.becasipn.persistence.model.PersonalAdministrativo;
import com.becasipn.persistence.model.TarjetaBancaria;
import com.becasipn.util.PaginateUtil;
import com.becasipn.util.ServerSideUtil;
import static com.becasipn.util.StringUtil.addParameters;
import java.math.BigDecimal;
import java.util.List;
import static com.becasipn.util.StringUtil.buildCountQuery;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Query;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Gustavo Adolfo Alamillo Medina
 * @version 1.0
 */
public class AlumnoTarjetaBancariaJpaDao extends JpaDaoBase<AlumnoTarjetaBancaria, BigDecimal> implements AlumnoTarjetaBancariaDao {

    public AlumnoTarjetaBancariaJpaDao() {
        super(AlumnoTarjetaBancaria.class);
    }

    /**
     * Dado el id del alumno y el último registro de la bitácora, obtiene la
     * tarjeta activa para un alumno.
     *
     * @param alumnoId
     * @param tarjetaBancariaId
     * @return TarjetaAlumno
     */
    @Override
    public AlumnoTarjetaBancaria getTarjetaAlumno(BigDecimal alumnoId, BigDecimal tarjetaBancariaId) {
        String jpql = "select at from AlumnoTarjetaBancaria at where at.alumno.id = ?1 and at.tarjetaBancaria.id = ?2";
        try {
            return executeSingleQuery(jpql, alumnoId, tarjetaBancariaId);
        } catch (javax.persistence.NoResultException e) {
            return null;
        }
    }
    
    /**
     * Verifica si un alumno tiene o ha tenido asociada la tarjeta indicada
     * @param alumnoId
     * @param tarjetaBancaria
     * @return 
     */
    @Override
    public AlumnoTarjetaBancaria getTarjetaAlumno(BigDecimal alumnoId, String tarjetaBancaria) {
        String jpql = "select at from AlumnoTarjetaBancaria at where at.alumno.id = ?1 and at.tarjetaBancaria.numtarjetabancaria = ?2";
        try {
            return executeSingleQuery(jpql, alumnoId, tarjetaBancaria);
        } catch (javax.persistence.NoResultException e) {
            return null;
        }
    }

    @Override
    public AlumnoTarjetaBancaria tarjetaAlumno(BigDecimal alumnoId) {
        String jpql = "select b from AlumnoTarjetaBancaria b where b.alumno.id = ?1 and b.tarjetaActiva = 1";
        List<AlumnoTarjetaBancaria> lista = executeQuery(jpql, alumnoId);
        return lista == null || lista.isEmpty() ? null : lista.get(0);
    }

    /**
     * Dado el id del alumno, obtiene la última tarjeta para un alumno. Ordenado
     * por el id de AlumnoTarjetaBancaria
     *
     * @param alumnoId
     * @return AlumnoTarjetaBancaria más reciente del alumno
     */
    @Override
    public AlumnoTarjetaBancaria tarjetaAlumnoV2(BigDecimal alumnoId) {
        StringBuilder consulta = new StringBuilder();
        consulta.append(" select b from AlumnoTarjetaBancaria b")
                .append(" join BitacoraTarjetaBancaria btb")
                .append(" on btb.tarjetaBancaria.id = b.tarjetaBancaria.id")
                .append(" where b.alumno.id = ?1")
                .append(" and b.estatusTarjBanc.id < 17")
                .append(" order by btb.fechaModificacion desc");        
        List<AlumnoTarjetaBancaria> lista = executeQuery(consulta.toString(), alumnoId);
        return lista == null || lista.isEmpty() ? null : lista.get(0);
    }

    /**
     * Dado el id de la tarjeta, obtiene la relacion alumno-tarjeta para una
     * referencia bancaria
     *
     * @param tarjetaId
     * @return AlumnoTarjetaBancaria
     */
    @Override
    public AlumnoTarjetaBancaria alumnoTarjetaReferencia(BigDecimal tarjetaId) {
        String jpql = "select b from AlumnoTarjetaBancaria b where b.tarjetaBancaria.id = ?1 "
                + "AND b.estatusTarjBanc.id = 17 "
                + "ORDER BY b.id DESC";
        List<AlumnoTarjetaBancaria> lista = executeQuery(jpql, tarjetaId);
        return lista == null || lista.isEmpty() ? null : lista.get(0);
    }

    @Override
    public PaginateUtil getListado(ServerSideUtil ssu) {
        StringBuilder sbQuery = new StringBuilder("select b from AlumnoTarjetaBancaria b "
                + " JOIN BitacoraTarjetaBancaria btc ON b.tarjetaBancaria.id = btc.tarjetaBancaria.id "
                + " WHERE btc.fechaModificacion = (SELECT MAX (btcm.fechaModificacion) "
                + "    FROM BitacoraTarjetaBancaria btcm "
                + "    WHERE btcm.tarjetaBancaria.id = btc.tarjetaBancaria.id ) "
                + " AND btc.estatus.id >3 "
                + " AND btc.estatus.id <10 "
                + " AND b.vigente=1");
        StringBuilder sbQueryFiltered = new StringBuilder(sbQuery.toString());
        PersonalAdministrativo padmin = getDaos().getPersonalAdministrativoDao().findByUsuario(SecurityContextHolder.getContext().getAuthentication().getName());
        if (isResponsable()) {
            ssu.parametrosServidor.put("b.alumno.unidadAcademica.id", padmin.getUnidadAcademica().getId());
        }
        Object[] params = addParameters(sbQuery, Boolean.FALSE, ssu.parametrosServidor);
        Long noTotal = getCountQuery(buildCountQuery(sbQuery.toString(), Boolean.FALSE), params);
        params = addParameters(sbQueryFiltered, Boolean.FALSE, ssu.parametrosServidor, ssu.parametros);
        tarjetaFiltroCaja(sbQueryFiltered, ssu.getFilterBox());
        tarjetaOrderBy(sbQueryFiltered, ssu.getSortCol(), ssu.getSortDir());
        Long noTotalFiltered = getCountQuery(buildCountQuery(sbQueryFiltered.toString(), Boolean.FALSE), params);
        return new PaginateUtil(executeQueryPaginate(sbQueryFiltered.toString(), ssu.getStart(), ssu.getLength(), params), noTotal, noTotalFiltered);
    }

    @Override
    public PaginateUtil listadoAlumnoTarjetaRemplazar(ServerSideUtil ssu, BigDecimal periodoId, int tipo) {
        Long noTotal;
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT atb FROM AlumnoTarjetaBancaria atb JOIN Alumno a ON atb.alumno.id = a.id ")
                .append(" JOIN BitacoraTarjetaBancaria btc ON atb.tarjetaBancaria.id = btc.tarjetaBancaria.id")
                .append(" WHERE btc.fechaModificacion = (SELECT MAX (btcm.fechaModificacion)")
                .append("    FROM BitacoraTarjetaBancaria btcm")
                .append("    WHERE btcm.tarjetaBancaria.id = btc.tarjetaBancaria.id )")
                .append(" AND atb.vigente=1");
        if (tipo == 1) {
            jpql.append(" AND btc.estatus.id = 11");
        } else {
            jpql.append(" AND btc.estatus.id = 4");
        }
        jpql.append(" AND EXISTS (SELECT 1 FROM Otorgamiento o JOIN DepositoUnidadAcademica d ")
                .append("            ON d.unidadAcademica.id=o.datosAcademicos.unidadAcademica.id and o.tipoBecaPeriodo.tipoBeca.id = d.tipoBeca.id")
                .append(" WHERE o.alumno.id = atb.alumno.id ")
                .append(" and (o.alta = 1 or not exists (select 1 from OtorgamientoBajasDetalle otd where otd.otorgamiento.id = o.id and otd.periodo.id =")
                .append(periodoId.toString()).append("))")
                .append(" and o.proceso is not null and o.periodo.id =").append(periodoId.toString())
                .append(" and d.tarjetaipn=1 and o.excluirDeposito=0 )");
        Object[] paramsTotal = addParameters(jpql, Boolean.FALSE, ssu.parametrosServidor);
        noTotal = getCountQuery(buildCountQuery(jpql.toString(), Boolean.FALSE), paramsTotal);
        jpql.append(" ORDER BY atb.alumno.unidadAcademica.id,atb.alumno.curp");
        return new PaginateUtil(executeQueryPaginate(jpql.toString(), ssu.getStart(), ssu.getLength(), paramsTotal), noTotal, noTotal);
    }

    //SQLINJECTION
    @Override
    public List<AlumnoTarjetaBancaria> listadoAlumnoTarjetaRemplazar(String boleta, BigDecimal nivel, BigDecimal unidadAcademica, BigDecimal periodoId, int tipo) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT atb FROM AlumnoTarjetaBancaria atb JOIN Alumno a ON atb.alumno.id = a.id ")
                .append(" JOIN BitacoraTarjetaBancaria btc ON atb.tarjetaBancaria.id = btc.tarjetaBancaria.id")
                .append(" WHERE btc.fechaModificacion = (SELECT MAX (btcm.fechaModificacion)")
                .append("    FROM BitacoraTarjetaBancaria btcm")
                .append("    WHERE btcm.tarjetaBancaria.id = btc.tarjetaBancaria.id )")
                .append(" AND atb.vigente=1");
        if (tipo == 1) {
            jpql.append(" AND btc.estatus.id = 11");
        } else {
            jpql.append(" AND btc.estatus.id = 4");
        }
        jpql.append(" AND EXISTS (SELECT 1 FROM Otorgamiento o JOIN DepositoUnidadAcademica d ")
                .append("            ON d.unidadAcademica.id=o.datosAcademicos.unidadAcademica.id and o.tipoBecaPeriodo.tipoBeca.id = d.tipoBeca.id")
                .append(" WHERE o.alumno.id = atb.alumno.id ")
                .append(" and (o.alta = 1 or not exists (select 1 from OtorgamientoBajasDetalle otd where otd.otorgamiento.id = o.id and otd.periodo.id =")
                .append(periodoId.toString()).append("))")
                .append(" and o.proceso is not null and o.periodo.id =").append(periodoId.toString())
                .append(" and d.tarjetaipn=1 and o.excluirDeposito=0 )");

        Map<String, Object> parametrosServidor = new HashMap<>();
        if (!boleta.isEmpty()) {
            parametrosServidor.put("a.boleta", boleta);
        }
        if (!nivel.equals(BigDecimal.ZERO)) {
            parametrosServidor.put("a.unidadAcademica.nivel.id", nivel);
        }
        if (!unidadAcademica.equals(BigDecimal.ZERO)) {
            parametrosServidor.put("a.unidadAcademica.id", unidadAcademica);
        }
        Object[] paramsFiltered = addParameters(jpql, Boolean.FALSE, parametrosServidor);
        jpql.append(" ORDER BY atb.alumno.unidadAcademica.id,atb.alumno.curp");
        return executeQuery(jpql.toString(), paramsFiltered);
    }

    @Override
    public PaginateUtil getListadoPersonalizacion(ServerSideUtil ssu) {
        String sbQuery = "select b from AlumnoTarjetaBancaria b "
                + "    JOIN BitacoraTarjetaBancaria btc ON btc.tarjetaBancaria.id = b.tarjetaBancaria.id "
                + "WHERE btc.fechaModificacion = (SELECT MAX (btcm.fechaModificacion) "
                + "        FROM BitacoraTarjetaBancaria btcm "
                + "        WHERE btcm.tarjetaBancaria.id = btc.tarjetaBancaria.id ) "
                + "and btc.estatus.id = 3 ";
        //"and atb.identificador = 'RE-20162-1'";
        StringBuilder sbQueryFiltered = new StringBuilder(sbQuery);
        Object[] params = addParameters(sbQueryFiltered, Boolean.FALSE, ssu.parametrosServidor);
        Long noTotal = getCountQuery(buildCountQuery(sbQueryFiltered.toString(), Boolean.FALSE), params);
        tarjetaFiltroCaja(sbQueryFiltered, ssu.getFilterBox());
        tarjetaPersonalizacionOrderBy(sbQueryFiltered, ssu.getSortCol(), ssu.getSortDir());
        Long noTotalFiltered = getCountQuery(buildCountQuery(sbQueryFiltered.toString(), Boolean.FALSE), params);
        return new PaginateUtil(executeQueryPaginate(sbQueryFiltered.toString(), ssu.getStart(), ssu.getLength(), params), noTotal, noTotalFiltered);
    }

    private void tarjetaFiltroCaja(StringBuilder sbQueryFiltered, String filtro) {
        if (!filtro.equals("")) {
            sbQueryFiltered.append(" and (")
                    .append("b.tarjetaBancaria.numtarjetabancaria like '%")
                    .append(filtro.toUpperCase())
                    .append("%' or ")
                    .append("b.alumno.nombre like '%")
                    .append(filtro.toUpperCase())
                    .append("%' or ")
                    .append("b.alumno.boleta like '%")
                    .append(filtro.toUpperCase())
                    .append("%')");
            //System.out.println(sbQueryFiltered.toString());
        }
    }

    private void tarjetaOrderBy(StringBuilder sbQueryFiltered, int sortingCol, String sortDir) {
        if (sortDir != null) {
            StringBuilder orderBy = new StringBuilder(" order by ");
            switch (sortingCol) {
                case 1:
                    orderBy.append("b.tarjetaBancaria.numtarjetabancaria ");
                    break;
                case 2:
                    orderBy.append("b.alumno.apellidoPaterno ");
                    orderBy.append(sortDir);
                    orderBy.append(" ,b.alumno.apellidoMaterno ");
                    orderBy.append(sortDir);
                    orderBy.append(" ,b.alumno.nombre ");
                    break;
                case 3:
                    orderBy.append("btc.estatus.id ");
                    break;
                default:
                    return;
            }
            orderBy.append(sortDir);
            sbQueryFiltered.append(orderBy.toString());
        }
    }

    private void tarjetaPersonalizacionOrderBy(StringBuilder sbQueryFiltered, int sortingCol, String sortDir) {
        StringBuilder orderBy = new StringBuilder(" order by ");
        switch (sortingCol) {
            case 2:
                orderBy.append("b.tarjetaBancaria.numtarjetabancaria ");
                break;
            case 3:
                orderBy.append("b.alumno.boleta ");
                break;
            default:
                return;
        }
        if (sortDir != null) {
            orderBy.append(sortDir);
        }
        sbQueryFiltered.append(orderBy.toString());
    }

    protected boolean isResponsable() {
        Collection<GrantedAuthority> gas = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (GrantedAuthority ga : gas) {
            if (ga.getAuthority().equals("ROLE_RESPONSABLE_UA")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean existeIdentificador(String identificador) {
        String jpql = "SELECT COUNT(atb) FROM AlumnoTarjetaBancaria atb "
                + " WHERE atb.identificador =?1";
        Long atb = getCountQuery(jpql, identificador);
        return !atb.equals(new Long(0l));
    }

    @Override
    public Long asignacionesIdentificador(String identificador) {
        String jpql = "SELECT count(atb) FROM AlumnoTarjetaBancaria atb "
                + " WHERE atb.identificador LIKE ?1 ";
        return getCountQuery(jpql, identificador);
    }

    @Override
    public List<AlumnoTarjetaBancaria> listAsignacionesIdentificador(String identificador) {
        String jpql = "SELECT atb FROM AlumnoTarjetaBancaria atb "
                + " WHERE atb.identificador LIKE ?1 ";
        return executeQuery(jpql, identificador);
    }

    @Override
    public int eliminarAsignacion(String identificador) {
        String jpql = "DELETE FROM AlumnoTarjetaBancaria atb WHERE atb.identificador LIKE ?1";
        return executeUpdate(jpql, identificador);
    }

    @Override
    public AlumnoTarjetaBancaria findByTarjetaBancaria(TarjetaBancaria tb) {
        String jpql = "SELECT atb FROM AlumnoTarjetaBancaria atb "
                + " WHERE atb.tarjetaBancaria.id=?1";
        List<AlumnoTarjetaBancaria> list = executeQuery(jpql, tb.getId());
        return list == null || list.isEmpty() ? null : list.get(0);
    }
    
    @Override
    public AlumnoTarjetaBancaria findByNumeroTarjetaBancaria(AlumnoTarjetaBancaria tb) {
        String jpql = "SELECT atb FROM AlumnoTarjetaBancaria atb "
                + " WHERE atb.tarjetaBancaria.numtarjetabancaria=?1";
        List<AlumnoTarjetaBancaria> list = executeQuery(jpql, tb.getTarjetaBancaria().getNumtarjetabancaria());
        return list == null || list.isEmpty() ? null : list.get(0);
    }

    @Override
    public int updateVigente(BigDecimal alumnoId, BigDecimal relacionId) {
        String sql = "UPDATE rmm_alumno_tarjeta_bancaria SET VIGENTE = DECODE(id,?2,1,0) WHERE (alumno_id = ?1)";
        return executeNativeUpdate(sql, alumnoId, relacionId);
    }

    @Override
    public List<String> identificadores() {
        List<String> res = new ArrayList<>();
        String jpql = "select atb.identificador from AlumnoTarjetaBancaria atb where atb.identificador is not null group by atb.identificador";
        List list = executeQueryObject(jpql);
        for (Object object : list) {
            res.add((String) object);
        }
        return res;
    }

    /**
     * Dado el id de los datos bancarios, obtiene la tarjeta activa se excluyen
     * los estatus correspondientes a referencias bancarias para permitir al
     * alumno seguir el flujo de edición de datos bancarios
     *
     * @param datosBancariosId
     * @return TarjetaAlumno activo del alumno
     */
    @Override
    public AlumnoTarjetaBancaria tarjetaDatosBancarios(BigDecimal datosBancariosId) {
        String jpql = "select t from AlumnoTarjetaBancaria t where t.datosBancarios.id = ?1 and t.estatusTarjBanc.id < 17 "
                + "order by t.id desc";
        List<AlumnoTarjetaBancaria> list = executeQuery(jpql, datosBancariosId);
        return list == null || list.isEmpty() ? null : list.get(0);
    }

    /**
     * Dado el id de los datos bancarios obtiene la tarjetas asociadas a esos
     * datos.
     *
     * @param datosBancariosId
     * @return lista
     */
    @Override
    public List<AlumnoTarjetaBancaria> tarjetasDatosBancarios(BigDecimal datosBancariosId) {
        String jpql = "select t from AlumnoTarjetaBancaria t where t.datosBancarios.id = ?1 order by t.id desc";
        List<AlumnoTarjetaBancaria> lista = executeQuery(jpql, datosBancariosId);
        return lista;
    }

    @Override
    public AlumnoTarjetaBancaria findBySolicitudCuenta(BigDecimal scId, BigDecimal alumnoId) {
        String sql = "SELECT t FROM AlumnoTarjetaBancaria t where t.solicitudCuentas.id=?1 and t.alumno.id=?2";
        List<AlumnoTarjetaBancaria> list = executeQuery(sql, scId, alumnoId);
        return list == null || list.isEmpty() ? null : list.get(0);
    }

    @Override
    public int finalizarSolicitudes(BigDecimal scId) {
        String sql = "UPDATE RMM_ALUMNO_TARJETA_BANCARIA a SET a.ESTATUSTARJBANC_ID=14"
                + " WHERE a.SOLICITUDCUENTA_ID=?1 and a.ESTATUSTARJBANC_ID=12";
        return executeNativeUpdate(sql, scId);
    }

    @Override
    public List<Object[]> estadisticaSolicitudes(BigDecimal scId) {
        String sql = "SELECT a.estatusTarjBanc.nombre,COUNT(a) FROM AlumnoTarjetaBancaria a "
                + " WHERE a.solicitudCuentas.id=?1 "
                + " GROUP BY a.estatusTarjBanc.nombre,a.estatusTarjBanc.id "
                + " ORDER BY a.estatusTarjBanc.id";

        List<Object[]> estadisticas = executeQueryObject(sql, scId);
        return estadisticas;
    }

    @Override
    public List<AlumnoTarjetaBancaria> getSolicitudes(BigDecimal identificadorId) {
        String consulta = "select atb.* "
                + "from RMM_ALUMNO_TARJETA_BANCARIA atb \n"
                + "INNER JOIN ENT_SOLICITUD_CUENTAS s ON s.ID = atb.SOLICITUDCUENTA_ID "
                + "WHERE s.id = ?1 ";
        Query q = entityManager.createNativeQuery(consulta, AlumnoTarjetaBancaria.class);
        q.setParameter(1, identificadorId);
        List<AlumnoTarjetaBancaria> lista = q.getResultList();
        return (lista);
    }

    @Override
    public List<AlumnoTarjetaBancaria> listSolicitudes(BigDecimal scId) {
        String consulta = "SELECT atb FROM AlumnoTarjetaBancaria atb JOIN FETCH atb.alumno WHERE atb.solicitudCuentas.id=?1";
        return executeQuery(consulta, scId);
    }

    @Override
    public AlumnoTarjetaBancaria tarjetaLiberada(BigDecimal alumnoId) {
        String sql = "select t from AlumnoTarjetaBancaria t where t.alumno.id=?1 and t.estatusTarjBanc.id = 13";
        List<AlumnoTarjetaBancaria> lista = executeQuery(sql, alumnoId);
        return lista == null || lista.isEmpty() ? null : lista.get(0);
    }
}
