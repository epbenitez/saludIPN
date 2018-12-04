package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.ProcesoDao;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.Proceso;
import com.becasipn.persistence.model.PersonalAdministrativo;
import com.becasipn.persistence.model.ProcesoEstatus;
import com.becasipn.persistence.model.TipoProceso;
import com.becasipn.persistence.model.UnidadAcademica;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.util.PaginateUtil;
import com.becasipn.util.ServerSideUtil;
import static com.becasipn.util.StringUtil.addParameters;
import static com.becasipn.util.StringUtil.buildCountQuery;
import com.becasipn.util.UtilFile;
import com.opensymphony.xwork2.ActionContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Victor Lozano
 */
public class ProcesoJpaDao extends JpaDaoBase<Proceso, BigDecimal> implements ProcesoDao {

    public ProcesoJpaDao() {
        super(Proceso.class);
    }

    /**
     * Devuelve una lista de procesos en uso
     *
     * @author Victor Lozano
     * @param procesoId
     * @return lista
     */
    @Override
    public List<Proceso> procesoEnUso(BigDecimal procesoId) {
        String consulta = "SELECT p FROM Proceso p WHERE p.periodo.id = ?1";
        List<Proceso> lista = executeQuery(consulta, procesoId);
        return lista;
    }

    /**
     * Valida que exista un proceso registrado previamente con los mismos datos
     *
     * @author Victor Lozano
     * @param proceso
     * @return true si existe
     */
    @Override
    public boolean existe(Proceso proceso) {
        String consulta = "SELECT p FROM Proceso p WHERE"
                + " p.unidadAcademica.id = ?1 AND"
                + " p.periodo.id = ?2 AND"
                + " p.tipoProceso.id = ?3";
        List<Proceso> lista = executeQuery(consulta, proceso.getUnidadAcademica().getId(), proceso.getPeriodo().getId(), proceso.getTipoProceso().getId());
        return lista != null && !lista.isEmpty();
    }

    @Override
    public Proceso existe(BigDecimal unidadAcademicaId, BigDecimal periodoId, BigDecimal tipoProcesoId) {
        String consulta = "SELECT p FROM Proceso p WHERE"
                + " p.unidadAcademica.id = ?1 AND"
                + " p.periodo.id = ?2 AND"
                + " p.tipoProceso.id = ?3";
        List<Proceso> lista = executeQuery(consulta, unidadAcademicaId, periodoId, tipoProcesoId);
        return lista.isEmpty() ? null : lista.get(0);
    }

    @Override
    public Proceso getProcesoBkUniversal(Periodo periodo, DatosAcademicos datosAcademicos) {
        BigDecimal uaId = datosAcademicos.getUnidadAcademica().getId();
        BigDecimal periodoId = periodo.getId();

        StringBuilder consulta = new StringBuilder();
        consulta.append("SELECT * FROM ENT_PROCESO")
                .append(" WHERE PERIODO_ID = ?1")
                .append(" AND TIPOPROCESO_ID = 198")
                .append(" AND UNIDADACADEMICA_ID = ?2");

        Query q = entityManager.createNativeQuery(consulta.toString(), Proceso.class);
        q.setParameter(1, periodoId);
        q.setParameter(2, uaId);
        List<Proceso> lista = q.getResultList();

        return lista.isEmpty() ? null : lista.get(0);
    }

    @Override
    public List<Proceso> existenProcesosAsociados(BigDecimal periodoId) {
        String jpql = "SELECT p FROM  Proceso p WHERE p.periodo.id = ?1 and p.procesoEstatus.id = 1";
        //SELECT * FROM  ent_proceso where periodo_id = 2 and estatus = 1;
        List<Proceso> lista = executeQuery(jpql, periodoId);
        return lista;
    }

    @Override
    public List<Proceso> asociadaProceso(BigDecimal unidadAcademicaId) {
        String jpql = "SELECT p FROM  Proceso p WHERE p.unidadAcademica.id = ?1";
        //SELECT * FROM becasipn.ent_proceso where unidadacademica_id=1;
        List<Proceso> lista = executeQuery(jpql, unidadAcademicaId);
        return lista;
    }

    @Override
    public List<Proceso> procesoByMovPerUa(String movimientoId, String periodoId, String uaId) {
        String consulta = "SELECT ep FROM TipoProceso p, Proceso ep WHERE p.id = ep.tipoProceso.id";
        // Cero corresponde al Id del movimiento dummy "Todos", que se muestra 
        // en Resumen de procesos.
        if (!"null".equals(movimientoId) && !"0".equals(movimientoId)) {
            consulta = consulta + " and p.movimiento.id = " + movimientoId;
        }
        if (!"null".equals(periodoId)) {
            consulta = consulta + " and ep.periodo.id = " + periodoId;
        }
        if (!"null".equals(uaId)) {
            consulta = consulta + " and ep.unidadAcademica.id = " + uaId;
        }
        List<Proceso> lista = executeQuery(consulta);
        return lista;
    }

    @Override
    public List<BigDecimal> estatusProcesos(int periodoId, int uaId, int movimientoId, int estatusId) {
        StringBuilder consulta = new StringBuilder();
        consulta.append("select count(id) ")
                .append("from ENT_PROCESO ")
                .append("where periodo_id = ?1");

        if (uaId != 0) {
            consulta.append(" and unidadAcademica_id = ?2");
        }

        if (movimientoId != 0) {
            consulta.append(" and tipoProceso_id = ?3");
        }

        if (estatusId != 0) {
            consulta.append(" and procesoEstatus_id = ?4");
        }

        consulta.append(" group by PROCESOESTATUS_ID");
        consulta.append(" order by PROCESOESTATUS_ID asc");
        Query q = entityManager.createNativeQuery(consulta.toString());
        q.setParameter(1, periodoId);
        q.setParameter(2, uaId);
        q.setParameter(3, movimientoId);
        q.setParameter(4, estatusId);
        List<BigDecimal> lista = q.getResultList();
        return lista;
    }

    /**
     * Devuelve la lista de procesos por unidad academica para los otorgamientos
     *
     * @author Victor Lozano
     * @param unidadAcademicaId
     * @return lista
     */
    @Override
    public List<Proceso> procesosOtorgamientoPorUnidadAcademica(BigDecimal unidadAcademicaId) {
        String consulta;
        List<Proceso> lista;
        if (unidadAcademicaId == null) {
            consulta = "SELECT p FROM  Proceso p"
                    + " WHERE p.periodo.estatus = 1"
                    + " AND p.tipoProceso.movimiento.tipoMovimiento.id =1";
            lista = executeQuery(consulta);
        } else {
            consulta = "SELECT p FROM  Proceso p"
                    + " WHERE p.periodo.estatus = 1"
                    + " AND p.unidadAcademica.id = ?1"
                    + " AND p.tipoProceso.movimiento.tipoMovimiento.id = 1";
            lista = executeQuery(consulta, unidadAcademicaId);
        }

        return lista;
    }

    @Override
    public List<Proceso> procesosNuevosOtorgamientosUnidadAcademica(BigDecimal unidadAcademicaId) {
        List<Proceso> lista;
        String jpql = "SELECT p FROM  Proceso p"
                + " WHERE p.periodo.estatus = 1"
                + " AND p.unidadAcademica.id = ?1"
                + " AND p.tipoProceso.movimiento.clave ='N'"
                + " AND (?2 BETWEEN  p.fechaInicial and p.fechaFinal)"
                + " AND p.procesoEstatus.clave='1'";
        lista = executeQuery(jpql, unidadAcademicaId, new Date());
        return lista;
    }

    @Override
    public List<Proceso> procesosBajasPorUnidadAcademica(BigDecimal unidadAcademica) {
        String consulta;
        List<Proceso> lista;
        if (unidadAcademica == null) {
            consulta = "SELECT p FROM  Proceso p"
                    + " WHERE p.periodo.estatus = 1"
                    + " AND p.tipoProceso.movimiento.id in (4,5,6)";
        } else {
            consulta = "SELECT p FROM  Proceso p"
                    + " WHERE p.periodo.estatus = 1"
                    + " AND p.unidadAcademica.id = ?1"
                    + " AND p.tipoProceso.movimiento.id in (4,5,6)";//NOTA:????????????????????
        }
        lista = executeQuery(consulta, unidadAcademica);
        return lista;
    }

    @Override
    public Boolean procesosBajaPorUnidadAcademica(String unidadAcademica, String proceso) {
        String consulta = "SELECT p FROM  Proceso p"
                + " WHERE p.periodo.estatus = 1"
                + " AND p.unidadAcademica.id = ?1"
                + " AND p.id = ?2"
                + " AND ?3 between p.fechaInicial and p.fechaFinal";
        List<Proceso> lista = executeQuery(consulta, new BigDecimal(unidadAcademica), new BigDecimal(proceso), new Date());
        return lista == null || lista.isEmpty() ? false : true;
    }

    //SQLINJECTION OK
    @Override
    public PaginateUtil validacionProcesosList(ServerSideUtil ssu, int search_id_periodo, int search_id_ua, int search_id_proceso, int search_id_estatus) {
        Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        PersonalAdministrativo personal = getDaos().getPersonalAdministrativoDao().findByUsuario(usuario.getId());
        StringBuilder sb = new StringBuilder();

        sb.append(" SELECT * ");
        sb.append(" FROM ent_proceso pr ");
        sb.append(" inner join CAT_UNIDAD_ACADEMICA ua on ua.id = pr.UNIDADACADEMICA_ID");
        sb.append(" inner join CAT_TIPO_PROCESO tp on tp.id = pr.TIPOPROCESO_ID");

        List<String> criteria = new ArrayList<>();
        Map<String, Object> params = new HashMap();

        criteria.add("pr.periodo_id = #periodo_id");
        if (search_id_periodo == 0) {
            Periodo periodo = getDaos().getPeriodoDao().getPeriodoActivo();
            params.put("periodo_id", periodo.getId());
        } else {
            params.put("periodo_id", search_id_periodo);
        }
        if (!isAnalista() && !isJefe()) {
            criteria.add("ua.id = #ua_id");
            params.put("ua_id", personal.getUnidadAcademica().getId());
        } else {
            if (search_id_ua != 0) {
                criteria.add("ua.id = #ua_id");
                params.put("ua_id", search_id_ua);
            }
        }
        if (search_id_proceso != 0) {
            criteria.add("tp.MOVIMIENTO_ID = #proceso_id");
            params.put("proceso_id", search_id_proceso);
        }
        if (search_id_estatus != 0) {
            params.put("pr.PROCESOESTATUS_ID = #estatus_id", search_id_estatus);
        }

        agregaCriterios(sb, criteria);
        // primero obtiene el total para mostrarlo en la tabla
        String countQ = buildCountQuery(sb.toString(), true);
        Long total = getCountNativeQueryMap(countQ, params);
        // Se agregan los order by
        validacionProcesosOrderBy(sb, ssu.getSortCol(), ssu.getSortDir());
        // Se ejecuta la query para un rango en específico
        List<Proceso> lista = executeNativeQueryPaginateMap(sb.toString(), ssu.getStart(), ssu.getLength(), params, Proceso.class);

        return new PaginateUtil(lista, total, total);
    }

    /**
     * Agrega a la query orden, según lo que la tabla del front especifique.
     * Author: Mario Márquez
     *
     * @param sb StringBuilder a modificar
     * @param sortingCol Opción a ordenar
     * @param sortDir Dirección del ordenamiento
     */
    private void validacionProcesosOrderBy(StringBuilder sb, int sortingCol, String sortDir) {
        if (sortDir != null) {
            sb.append(" order by ");
            switch (sortingCol) {
                case 0:
                    sb.append(" ua.id ");
                    break;
                case 1:
                    sb.append(" pr.TIPOPROCESO_ID ");
                    break;
                case 2:
                    sb.append(" pr.FECHAINICIAL ");
                    break;
                case 3:
                    sb.append(" pr.FECHAFINAL ");
                    break;
                default:
                    return;
            }
            sb.append(sortDir);
        }
    }

//-------------------------------------------------------------------------------------------------------------------------------------------------------
    public String agregarParametros() {
        String parametros = "";
        if (!isAnalista() && !isJefe()) {
            parametros = parametros + " and p.unidadAcademica.id = ?1";
        }
        return parametros;
    }

    protected boolean isAnalista() {
        Collection<GrantedAuthority> gas = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (GrantedAuthority ga : gas) {
            if (ga.getAuthority().equals("ROLE_ANALISTABECAS")) {
                return true;
            }
        }
        return false;
    }

    protected boolean isJefe() {
        Collection<GrantedAuthority> gas = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (GrantedAuthority ga : gas) {
            if (ga.getAuthority().equals("ROLE_JEFEBECAS")) {
                return true;
            }
        }
        return false;
    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public List<Proceso> reportesProcesosList(String listaProcesos) {
        String consulta = "SELECT ep FROM TipoProceso p, Proceso ep"
                + " WHERE p.id = ep.tipoProceso.id"
                + " and ep.id IN (" + listaProcesos + ")";
        List<Proceso> lista = executeQuery(consulta);
        return lista;
    }

    @Override
    public List<Proceso> reportesPeriodoList(Boolean alta, String periodoId) {
        String consulta = "SELECT ep FROM Proceso ep "
                + "WHERE ep.periodo.id = " + periodoId;
        if (alta) {
            consulta = consulta + " and ep.tipoProceso.movimiento.tipoMovimiento.id = 1";
        } else {
            consulta = consulta + " and ep.tipoProceso.movimiento.tipoMovimiento.id = 2";
        }
        consulta = consulta + " order by ep.unidadAcademica.id";

        List<Proceso> lista = executeQuery(consulta);
        return lista;
    }

    @Override
    public Boolean procesoActivoByUa(BigDecimal uaId) {
        String consulta = "SELECT p FROM Proceso p"
                + " WHERE p.periodo.estatus = 1"
                + " AND p.unidadAcademica.id = ?1"
                + " AND p.tipoProceso.movimiento.id < 4"
                + " AND ?2 between p.fechaInicial and p.fechaFinal";
        List<Proceso> lista = executeQuery(consulta, uaId, new Date());
        return lista == null || lista.isEmpty() ? false : true;
    }

    @Override
    public PaginateUtil findAllProcesos(ServerSideUtil ssu) {
        String sbQuery = "select ep from Proceso ep";
        StringBuilder sbQueryFiltered = new StringBuilder(sbQuery);
        Long noTotal = getCountQuery(buildCountQuery(sbQuery, Boolean.FALSE));
        Object[] params = addParameters(sbQueryFiltered, Boolean.TRUE, ssu.parametros);
        procesosOrderBy(sbQueryFiltered, ssu.getSortCol(), ssu.getSortDir());
        Long noTotalFiltered = getCountQuery(buildCountQuery(sbQueryFiltered.toString(), Boolean.FALSE), params);
        List<Proceso> result = executeQueryPaginate(sbQueryFiltered.toString(), ssu.getStart(), ssu.getLength(), params);
        return new PaginateUtil(result, noTotal, noTotalFiltered);
    }

    private void procesosOrderBy(StringBuilder sbQueryFiltered, int sortingCol, String sortDir) {
        if (sortDir != null) {
            StringBuilder orderBy = new StringBuilder(" order by ");
            switch (sortingCol) {
                case 0:
                    orderBy.append("ep.unidadAcademica.id ");
                    break;
                case 1:
                    orderBy.append("ep.periodo.id ");
                    break;
                case 2:
                    orderBy.append("ep.tipoProceso.nombre ");
                    break;
                case 3:
                    orderBy.append("ep.fechaInicial ");
                    break;
                case 4:
                    orderBy.append("ep.fechaFinal ");
                    break;
                case 5:
                    orderBy.append("ep.procesoEstatus.nombre ");
                    break;
                default:
                    return;
            }
            orderBy.append(sortDir);
            sbQueryFiltered.append(orderBy.toString());
        }
    }
}
