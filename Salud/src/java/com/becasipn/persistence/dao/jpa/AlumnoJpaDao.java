/**
 * SISTEMA INFORMÁTICO DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE
 * SERVICIOS ESTUDIANTILES 2015
 *
 */
package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.AlumnoDao;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.PersonalAdministrativo;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.util.PaginateUtil;
import com.becasipn.util.ServerSideUtil;
import com.becasipn.util.StringUtil;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import static com.becasipn.util.StringUtil.addParameters;
import static com.becasipn.util.StringUtil.buildCountQuery;
import com.opensymphony.xwork2.ActionContext;
import java.util.HashMap;
import javax.persistence.Query;

/**
 *
 * @author Patricia Benitez
 */
public class AlumnoJpaDao extends JpaDaoBase<Alumno, BigDecimal> implements AlumnoDao {

    public AlumnoJpaDao() {
        super(Alumno.class);
    }

    /**
     * Devuelve true en caso que el alumno ya haya entregado los documentos que
     * piden todas las becas
     *
     * @author Victor Lozano
     * @param alumnoId
     * @return true o false
     */
    @Override
    public boolean documentosCompletos(BigDecimal alumnoId) {
        String consulta = "SELECT d FROM Documentos d "
                + " WHERE d.alumno.id=?1 and d.periodo.estatus=1"
                + " AND d.estudioSocioeconomico = 1"
                + " AND d.cartaCompromiso = 1"
                + " AND d.curp = 1"
                + " AND d.comprobanteIngresosEgresos = 1";
        List<Alumno> lista = executeQuery(consulta, alumnoId);
        return lista != null && !lista.isEmpty();
    }

    /**
     * Devuelve true en caso que el alumno haya entregado los documentos para el
     * periodo dato
     *
     * @author Tania Sánchez
     * @param alumnoId
     * @param periodoId
     * @return true o false
     */
    @Override
    public boolean documentosCompletosPeriodo(BigDecimal alumnoId, BigDecimal periodoId) {
        String consulta = "select d from Documentos d "
                + " where d.alumno.id = ?1 and d.periodo.id = ?2"
                + " and d.estudioSocioeconomico = 1"
                + " and d.cartaCompromiso = 1"
                + " and d.curp = 1"
                + " and d.comprobanteIngresosEgresos = 1";
        List<Alumno> lista = executeQuery(consulta, alumnoId, periodoId);
        return lista != null && !lista.isEmpty();
    }

    /**
     * Devuelve true en caso que el alumno ya haya entregado los documentos que
     * piden todas las becas
     *
     * @author Victor Lozano
     * @param alumnoId
     * @return true o false
     */
    @Override
    public boolean documentosCompletosManutencion(BigDecimal alumnoId) {
        String consulta = "SELECT d FROM Documentos d "
                + " WHERE d.alumno.id=?1 and d.periodo.estatus=1"
                + " AND d.estudioSocioeconomico = 1"
                + " AND d.cartaCompromiso = 1"
                + " AND d.curp = 1"
                + " AND d.comprobanteIngresosEgresos = 1"
                + " AND d.acuseSubes = 1"
                + " AND d.folioSubes = 1";
        List<Alumno> lista = executeQuery(consulta, alumnoId);
        return lista != null && !lista.isEmpty();
    }

    @Override
    public List<Alumno> asociadaAlumno(BigDecimal unidadAcademicaId) {
        String jpql = "SELECT a FROM  Alumno a WHERE a.datosAcademicos.unidadAcademica.id = ?1";
        //SELECT * FROM becasipn.ent_alumno where unidadacademica_id=1;
        List<Alumno> lista = executeQuery(jpql, unidadAcademicaId);
        return lista;
    }

    @Override
    public BigDecimal getIdUsuario(BigDecimal idAlumno) {
        String jpql = "SELECT a FROM Alumno a WHERE a.usuario.id = ?1";
        return executeSingleQuery(jpql, idAlumno).getId();
    }

    @Override
    public List<Alumno> getByBoleta(String noBoleta) {
        String jpql = "SELECT a FROM Alumno a WHERE a.boleta = ?1";
        //SELECT * FROM becasipn.ent_alumno where boleta=1234567891;
        List<Alumno> lista = executeQuery(jpql, noBoleta);
        return lista;
    }

    /**
     * Devuelve un alumno que coincida con la boleta a buscar
     *
     * @author Victor Lozano
     * @param boleta
     * @return alumno o null
     */
    @Override
    public Alumno findByBoleta(String boleta) {
        String jpql = "SELECT a FROM Alumno a WHERE a.boleta = ?1 ORDER BY a.estatus DESC, a.id DESC";
        List<Alumno> lista = executeQuery(jpql, boleta);
        return lista == null || lista.isEmpty() ? null : lista.get(0);
    }

    @Override
    public Boolean existeBoleta(String boleta) {
        String jpql = "SELECT a FROM Alumno a WHERE a.boleta = ?1";
        List<Alumno> lista = executeQuery(jpql, boleta);
        return lista == null || lista.isEmpty() ? Boolean.FALSE : Boolean.TRUE;
    }

    @Override
    public Boolean existeCurp(String curp) {
        String jpql = "SELECT a FROM Alumno a WHERE a.curp = ?1";
        List<Alumno> lista = executeQuery(jpql, curp);
        return lista == null || lista.isEmpty() ? Boolean.FALSE : Boolean.TRUE;
    }

    @Override
    public Boolean existeNombre(String nombre, String aPaterno, String aMaterno) {
        String jpql = "select a from Alumno a "
                + "where a.nombre like '%" + nombre + "%'"
                + " and a.apellidoPaterno like '%" + aPaterno + "%'"
                + " and a.apellidoMaterno like '%" + aMaterno + "%'";
        List<Alumno> lista = executeQuery(jpql);
        return lista == null || lista.isEmpty() ? Boolean.FALSE : Boolean.TRUE;
    }

    @Override
    public Alumno findByBoleta(Map<String, Object> parametros) {
        StringBuilder sbQuery = new StringBuilder("SELECT a FROM Alumno a ");
        Object[] params = addParameters(sbQuery, Boolean.TRUE, parametros);
        List<Alumno> al = executeQuery(sbQuery.toString(), params);
        return al == null || al.isEmpty() ? null : al.get(0);
    }

    @Override
    public PaginateUtil findAlumnos(ServerSideUtil ssu) {
        StringBuilder sbQuery = new StringBuilder("select a from Alumno a, Carrera c where a.datosAcademicos.carrera.id = c.id ");
        StringBuilder sbQueryFiltered = new StringBuilder(sbQuery.toString());
        Object[] params = addParameters(sbQuery, Boolean.FALSE, ssu.parametrosServidor);
        Long noTotal = getCountQuery(buildCountQuery(sbQuery.toString(), Boolean.FALSE), params);
        params = addParameters(sbQueryFiltered, Boolean.FALSE, ssu.parametrosServidor, ssu.parametros);
        alumnoFiltroCaja(sbQueryFiltered, ssu.getFilterBox());
        alumnoOrderBy(sbQueryFiltered, ssu.getSortCol(), ssu.getSortDir());
        Long noTotalFiltered = getCountQuery(buildCountQuery(sbQueryFiltered.toString(), Boolean.FALSE), params);
        List<Alumno> result = executeQueryPaginate(sbQueryFiltered.toString(), ssu.getStart(), ssu.getLength(), params);
        return new PaginateUtil(result, noTotal, noTotalFiltered);
    }

    private void alumnoOrderBy(StringBuilder sbQueryFiltered, int sortingCol, String sortDir) {
        if (sortDir != null) {
            StringBuilder orderBy = new StringBuilder(" order by ");
            switch (sortingCol) {
                case 0:
                    orderBy.append("a.boleta ");
                    break;
                case 1:
                    orderBy.append("a.apellidoPaterno ");
                    break;
                case 2:
                    orderBy.append("c.carrera ");
                    break;
                case 3:
                    orderBy.append("a.datosAcademicos.promedio ");
                    break;
                case 4:
                    orderBy.append("a.datosAcademicos.semestre ");
                    break;
                case 5:
                    orderBy.append("a.datosAcademicos.inscrito ");
                    break;
                case 6:
                    orderBy.append("a.datosAcademicos.regular ");
                    break;
                default:
                    return;
            }
            orderBy.append(sortDir);
            sbQueryFiltered.append(orderBy.toString());
        }
    }

    private void alumnoFiltroCaja(StringBuilder sbQueryFiltered, String filtro) {
        if (filtro != null && !filtro.equals("")) {
            sbQueryFiltered.append(" and (")
                    .append("a.boleta like '%")
                    .append(filtro.toUpperCase())
                    .append("%' or a.nombre like '%")
                    .append(filtro.toUpperCase())
                    .append("%' or a.apellidoPaterno like '%")
                    .append(filtro.toUpperCase())
                    .append("%' or a.apellidoMaterno like '%")
                    .append(filtro.toUpperCase())
                    .append("%')");
        }
    }

    @Override
    public PaginateUtil findAsignaciones(int tipoProceso, int tab, ServerSideUtil ssu) {
        PaginateUtil pu = null;
        StringBuilder sb = new StringBuilder();
        StringBuilder filters = new StringBuilder();
        StringBuilder campos = new StringBuilder();
        Periodo p = getDaos().getPeriodoDao().getPeriodoActivo();
        String clave = p.getClave();
        String boleta = null;
        ssu.parametrosServidor.put("PERIODOACTUAL", p.getId());
        ssu.parametrosServidor.put("PERIODOANTERIOR", p.getPeriodoAnterior().getId());

        StringBuilder soloValidaciones = new StringBuilder();
        soloValidaciones.append(" INNER JOIN ent_tipo_beca_periodo tbpotorgamiento  ON tbpotorgamiento.id = os.TIPOBECAPERIODO_ID  and tbpotorgamiento.TIPOBECA_ID IN  (SELECT tbp.TIPOBECA_ID FROM ent_tipo_beca_periodo tbp   WHERE tbp.periodo_id = ?1 and tbp.validaciondeinscripcion > 0) ");

        campos.append("SELECT s.id,a.id,A.BOLETA,A.APELLIDOPATERNO||' '||A.APELLIDOMATERNO||' '||A.NOMBRE AS NOMBRE,C.CARRERA ,TO_CHAR(da.promedio, '99.99'), "
                + "    da.semestre, DECODE(PP.ID, null, 0, 1) as prospera, dm.conhambre, "
                + "    BITAND "
                + "    (BITAND "
                + "        (BITAND  ( nvl(D.CARTACOMPROMISO, 0) , nvl(D.COMPROBANTEINGRESOSEGRESOS,0)), "
                + "                DECODE(( "
                + "                    DECODE(( "
                + "                        BITAND(nvl(D.ESTUDIOSOCIOECONOMICO,0), DECODE(S.CUESTIONARIO_ID, 1, 1, 0)) + "
                + "                        BITAND(nvl(D.estudiosocieconomicotransporte, 0), DECODE(S.CUESTIONARIO_ID, 2, 1, 0)) "
                + "                    ),0,0,1) + "
                + "                    DECODE(( "
                + "                        BITAND(nvl(D.ACUSESUBES, 0), DECODE(S.CUESTIONARIO_ID, 5, 1, 0)) + "
                + "                        BITAND(nvl(D.ACUSESUBESTRANSPORTE, 0), DECODE(S.CUESTIONARIO_ID, 4, 1, 0)) "
                + "                    ), 0,0,1) "
                + "                ), 0, 0, 1) "
                + "        ) , "
                + "        nvl(D.CURP,0) "
                + "    ) as documentos ,"
                + "    S.PROGRAMABECASOLICITADA_ID,b.nombre AS SOLICITADA,decode(os.alumno_id,null,0,1) as antecedente ");

        String query = " FROM ENT_SOLICITUD_BECAS s "
                + "    JOIN ENT_ALUMNO a on S.ALUMNO_ID=a.id "
                + "    JOIN ENT_ALUMNO_DATOS_ACADEMICOS da on A.ID=DA.ALUMNO_ID and DA.PERIODO_ID= ?1 "
                + "    JOIN ENT_CARRERA c on DA.CARRERA_ID=C.ID "
                + "    JOIN CAT_PROGRAMA_BECA  b on  S.PROGRAMABECASOLICITADA_ID =b.id "
                + "    LEFT JOIN ENT_TIPO_BECA_PERIODO tbp on S.TIPOBECAPREASIGNADA_ID=tbp.id "
                + "    LEFT JOIN CAT_TIPO_BECA TB ON TBP.TIPOBECA_ID=TB.ID "
                + "    LEFT JOIN RMM_DOCUMENTOS_ALUMNO d ON D.ALUMNO_ID=a.id and D.PERIODO_ID = ?1 "
                + "    LEFT JOIN (SELECT distinct(O.ALUMNO_ID),o.alta, o.solicitudbeca_id, o.proceso_id,TIPOBECAPERIODO_ID  FROM VW_OTORGAMIENTOS o "
                + "                                       WHERE O.PERIODO_ID=?2) os ON os.alumno_id = a.id   and os.solicitudbeca_id = s.id"
                + (tipoProceso==7?soloValidaciones:"")
                + "    JOIN ENT_DIRECCION dir on dir.id = a.direccion_id "
                + "    JOIN RMM_ESTADO_DELEG_COL edc on edc.id = dir.relaciongeografica_id "
                + "    JOIN CAT_DELEGACION_MUNICIPIO dm on dm.id = edc.municipio_id "
                + "    LEFT JOIN ENT_PADRON_PROSPERA pp ON PP.ALUMNO_ID = A.ID AND PP.PERIODO_ID = ?1 ";

        String condiciones = "where A.ESTATUS=1 "
                + "    and S.CUESTIONARIO_ID in (1,2,4,5) "
                + "    and S.FINALIZADO=1 "
                + "    and DA.UNIDADACADEMICA_ID = ?4 ";

        if (clave.charAt(clave.length() - 1) == '1') {
            if (tipoProceso == 7) { //Solo validaciones/transferencias (solicitudes del periodo anterior)
                condiciones += "    and S.PERIODO_ID in (?2)    and ( os.alta=1 ) and da.unidadacademica_id =  (select unidadacademica_id from ent_proceso where id = os.proceso_id)";
            } else {                  //Sólo nuevos (solicitudes periodo actual)
                condiciones += "    and S.PERIODO_ID in (?1) ";
            }
        } else {
            if (tipoProceso == 7 || tipoProceso == 1) { //Solo validaciones/transferencias (solicitudes del periodo anterior)
                condiciones += "    and S.PERIODO_ID in (?2)    and ( os.alta=1 ) and da.unidadacademica_id =  (select unidadacademica_id from ent_proceso where id = os.proceso_id)";
            } else {                  //Sólo nuevos (solicitudes periodo actual)
                condiciones += "    and S.PERIODO_ID in (?1) ";
            }
        }

        if (tipoProceso == 1) {
            condiciones = condiciones + " and S.PERMITETRANSFERENCIA = 1";
        }

        switch (tab) {
            case 0:
                campos.append(" , S.TIPOBECAPREASIGNADA_ID AS PRE_ID, ")
                        .append(" DECODE(TBP.VISIBLE, 1, TB.NOMBRE, TB.NOMBRE||' (C)') AS PRE, L.ORDENAMIENTO as turno, ")
                        .append(" edc.GRADOMARGINACION ");
                sb.append(query).append(" LEFT JOIN ENT_OTORGAMIENTOS o2 ON o2.solicitudbeca_id = s.id and o2.periodo_id=?1 ")
                        .append("    LEFT JOIN ENT_LISTA_ESPERA l on L.SOLICITUDBECA_ID=S.ID and L.VIGENTE=1 ")
                        .append(condiciones)
                        .append("    and nvl(S.CLASIFICACIONSOLICITUD_ID,0) not in (2) ")
                        .append("    and o2.id is null")
                        .append("    and not exists (select 1 from ENT_LISTA_ESPERA le where le.proceso_id=?3 and s.id=LE.SOLICITUDBECA_ID ) ");
                filters.append("  and (S.PROGRAMABECASOLICITADA_ID=?5 OR ?5 is null ) ")
                        .append(" and (decode(s.TIPOBECAPREASIGNADA_ID, null, 2, 1)=?9 OR ?9 is null) ");
                //.append();
                break;
            case 1://Asignados
                campos.append(" ,RO.TIPOBECAPERIODO_ID AS TBPO_ID, DECODE(TBPO.VISIBLE, 1, TBO.NOMBRE, TBO.NOMBRE||' (C)') ")
                        .append(",1, edc.GRADOMARGINACION ");
                sb.append(query)
                        .append("    LEFT JOIN ENT_LISTA_ESPERA l on L.SOLICITUDBECA_ID=S.ID and L.VIGENTE=1 ")
                        .append(" JOIN VW_OTORGAMIENTOS ro ON (RO.SOLICITUDBECA_ID = S.ID AND RO.PROCESO_ID = ?3) ")
                        .append(" JOIN ENT_TIPO_BECA_PERIODO tbpo ON RO.TIPOBECAPERIODO_ID = TBPO.ID ")
                        .append(" JOIN CAT_TIPO_BECA tbo ON TBPO.TIPOBECA_ID = TBO.ID ")
                        .append(condiciones)
                        .append("    and nvl(S.CLASIFICACIONSOLICITUD_ID,0) in (1) ");
                filters.append("  and (tbpo.id=?5 OR ?5 is null ) ")
                        .append(" and (decode(s.TIPOBECAPREASIGNADA_ID, null, 2, 1)=?9 OR ?9 is null) ");
                break;
            case 2://Rechazados
                campos.append(" ,M.ID as MOV_ID,M.NOMBRE as motivo ")
                        .append(",1, edc.GRADOMARGINACION ");
                sb.append(query).append(" JOIN CAT_MOTIVO_RECHAZO_SOLICITUD M ON m.id=s.motivorechazo_id  ")
                        .append("    LEFT JOIN ENT_LISTA_ESPERA l on L.SOLICITUDBECA_ID=S.ID and L.VIGENTE=1 ")
                        .append(condiciones).append(" and  nvl(S.CLASIFICACIONSOLICITUD_ID,0) in (2)")
                        .append(" and s.proceso_id=?3 ");
                filters.append("  and (S.PROGRAMABECASOLICITADA_ID=?5 OR ?5 is null ) ")
                        .append(" and (decode(s.TIPOBECAPREASIGNADA_ID, null, 2, 1)=?9 OR ?9 is null) ");
                break;
            case 3:
                campos.append(" ,l.id as lista_id, l.ordenamiento as ord ")
                        .append(",1, edc.GRADOMARGINACION ");
                sb.append(query).append(" JOIN ENT_LISTA_ESPERA L ON l.proceso_id=?3 and s.id=L.SOLICITUDBECA_ID ")
                        .append(condiciones)
                        .append(" and nvl(S.CLASIFICACIONSOLICITUD_ID,0) in (3) ");
                filters.append("  and (S.PROGRAMABECASOLICITADA_ID=?5 OR ?5 is null ) ")
                        .append(" and (decode(s.TIPOBECAPREASIGNADA_ID, null, 2, 1)=?9 OR ?9 is null) ");
                break;
        }

        //Al agregar el inner join de "soloValidaciones", el count lo resuelve demasiado lento, por lo
        //que se realizó el query normal, y se contabiliza el número de registros
        String s = campos.toString() + sb.toString() ;
        List<Object[]>  sinFiltros = executeNativeQuery(s, p.getId(),
                p.getPeriodoAnterior().getId(), ssu.parametrosServidor.get("PROCESO"), ssu.parametrosServidor.get("UNIDADACADEMICA"));

        Long noTotal = sinFiltros==null?0L:sinFiltros.size();
        
        //Filters
        filters.append("    and (a.boleta like ?6 OR ?6 IS NULL) ")
                .append(" and (BITAND(BITAND (BITAND  ( nvl(D.CARTACOMPROMISO,0) , nvl(D.COMPROBANTEINGRESOSEGRESOS,0)), ")
                .append(" nvl(D.ESTUDIOSOCIOECONOMICO,0)) , nvl(D.CURP,0))=?7 or ?7 is null ) ")
                .append(" and (decode(os.alumno_id,null,2,1)=?8 or ?8 is null) ");

        sb.append(filters);

        if (ssu.parametros.get("ALUMNOBOLETA") != null) {
            boleta = "%" + ssu.parametros.get("ALUMNOBOLETA") + "%";
        }

        //Al agregar el inner join de "soloValidaciones", el count lo resuelve demasiado lento, por lo
        //que se realizó el query normal, y se contabiliza el número de registros
        s = campos.toString() + sb.toString() ;
        List<Object[]>  todo = executeNativeQuery(s, p.getId(),
                p.getPeriodoAnterior().getId(), ssu.parametrosServidor.get("PROCESO"), ssu.parametrosServidor.get("UNIDADACADEMICA"),
                ssu.parametros.get("TIPOBECA"), boleta, ssu.parametros.get("DOCUMENTOSCOMPLETOS"),
                ssu.parametros.get("ANTECEDENTE"), ssu.parametros.get("PREASIGNADO"));
        Long noTotalFiltered = todo==null?0L:todo.size();

        //Order
        String order = orderFindAsignaciones(ssu.getSortDir(), ssu.getSortCol(), tab);
        s = campos.toString() + sb.toString() + order;
        pu = new PaginateUtil(executeNativeQueryPaginate(s, ssu.getStart(),
                ssu.getLength(), p.getId(), p.getPeriodoAnterior().getId(), ssu.parametrosServidor.get("PROCESO"),
                ssu.parametrosServidor.get("UNIDADACADEMICA"), ssu.parametros.get("TIPOBECA"),
                boleta, ssu.parametros.get("DOCUMENTOSCOMPLETOS"),
                ssu.parametros.get("ANTECEDENTE"), ssu.parametros.get("PREASIGNADO")), noTotal, noTotalFiltered);

        return pu;
    }

    public String orderFindAsignaciones(String s, int i, int tab) {
        //NombreQuery:      s.id, a.id, A.BOLETA, NOMBRE, C.CARRERA, da.promedio, da.semestre,  prospera, dm.conhambre, documentos, S.PROGRAMABECASOLICITADA_ID, SOLICITADA, antecedente,   PRE_ID/ASI_ID/MOT_ID/LE_ID, NOMBRE/TURNO
        //SecuenciaQuery:    1      2      3        4        5             6             7        8           9             10                         11         12            13                      14                   15
        //SecuenciaAJAX:     0      1      2        3        4             5             6        7           8             9                          10         11            12                      13                   14 
        //SecuenciaTabla:   scid    0      1        2        3             4             5        6           7             8                          N/A        tab!=0:9      tab!=0:N/A              N/A                 tab!=0:10 
        //                                                                                                                                                        tab=0:10      tab=0:9                                     tab=0:11                         
        i += 2;
        String order = " order by ";

        if (i == 11 && tab != 0) {//Si se ordena por solicitada en tab distinto de candidatos sumar 1 porque en el query esta antes el idSolicitada
            i++;
        } else if (i == 12 && tab != 0) {//Si se ordena por NOMBRE/TURNO en tab distinto de candidatos sumar 3 porque en el query esta antes idSolicitada, antecedente y PRE_ID/ASI_ID/MOT_ID/LE_ID
            i += 3;
        } else if ((i == 11 || i == 13) && tab == 0) {//Si se ordena por antecedente o NOMBRE/TURNO en tab de candidatos se suman 2  porque en el query esta antes idSolicitada y PRE_ID/ASI_ID/MOT_ID/LE_ID
            i += 2;
        }

        order = order + i;

        if (s.equals("desc")) {
            order = order + " desc ,";
        } else {
            order = order + " asc ,";
        }

        //" order by antecedente desc,L.ORDENAMIENTO, prospera desc,DA.PROMEDIO"
        if (i == 6) {//Si se ordena por promedio
            order = order + " antecedente desc, l.ordenamiento, prospera desc";
        } else if (i == 8) {//Si se ordena por prospera
            order = order + " antecedente desc, l.ordenamiento, da.promedio desc";
        } else if (i == 13 && tab == 0) {//Si se ordena por antecedente en tab de candidatos
            order = order + " l.ordenamiento, prospera desc, da.promedio desc";
        } else {//Si se ordena por cualquier otra cosa
            order = order + " antecedente desc, l.ordenamiento, prospera desc, da.promedio desc";
        }

        return order;
    }

    /**
     * Devuelve un alumno que coincida con la boleta a buscar
     *
     * @author Victor Lozano
     * @param tipoAsignacion
     * @param ssu
     * @return alumnos suceptibles a asignacion de tarjeta
     */
    @Override
    public PaginateUtil listadoAlumnoNueavasTarjetas(ServerSideUtil ssu) {
        Long noTotal;
        StringBuilder consultaTotal = new StringBuilder();
        consultaTotal.append("SELECT a FROM Alumno a ")
                .append("WHERE EXISTS(SELECT 1 FROM Otorgamiento o ")
                .append("    JOIN Proceso pr ON o.proceso.id = pr.id ")
                .append("    JOIN DepositoUnidadAcademica d ON d.unidadAcademica.id=o.datosAcademicos.unidadAcademica.id and o.tipoBecaPeriodo.tipoBeca.id = d.tipoBeca.id ")
                .append("    WHERE o.alumno.id = a.id ")
                .append("        and (o.alta = 1 or not exists (select 1 from OtorgamientoBajasDetalle otd where otd.otorgamiento.id = o.id and otd.periodo.estatus=1))")
                .append("        and o.proceso is not null ")
                .append("        and pr.procesoEstatus.id=4 ")
                .append("        and o.periodo.estatus=1 ")
                .append("        and o.excluirDeposito=0 ")
                .append("        and d.tarjetaipn=1) ")
                .append("AND NOT EXISTS(SELECT 1 FROM AlumnoTarjetaBancaria atb WHERE atb.alumno.id = a.id )");
        Object[] paramsTotal = addParameters(consultaTotal, Boolean.FALSE, ssu.parametrosServidor);
        noTotal = getCountQuery(buildCountQuery(consultaTotal.toString(), Boolean.FALSE), paramsTotal);
        consultaTotal.append(" ORDER BY a.unidadAcademica.id,a.curp");
        return new PaginateUtil(executeQueryPaginate(consultaTotal.toString(), ssu.getStart(), ssu.getLength(), paramsTotal), noTotal, noTotal);
    }

    /**
     * Devuelve un alumno que coincida con la boleta a buscar
     *
     * @author Victor Lozano
     * @param tipoAsignacion
     * @param boleta
     * @param nivel
     * @param unidadAcademica
     * @return alumnos suceptibles a asignacion de tarjeta
     */
    @Override
    public List<Alumno> listadoAlumnoNueavasTarjetas(String boleta, BigDecimal nivel, BigDecimal unidadAcademica) {
        StringBuilder consultaTotal = new StringBuilder();
        consultaTotal.append("SELECT a FROM Alumno a ")
                .append("WHERE EXISTS(SELECT 1 FROM Otorgamiento o ")
                .append("    JOIN Proceso pr ON o.proceso.id = pr.id ")
                .append("    JOIN DepositoUnidadAcademica d ON d.unidadAcademica.id=o.datosAcademicos.unidadAcademica.id and o.tipoBecaPeriodo.tipoBeca.id = d.tipoBeca.id ")
                .append("    WHERE o.alumno.id = a.id ")
                .append("        and (o.alta = 1 or not exists (select 1 from OtorgamientoBajasDetalle otd where otd.otorgamiento.id = o.id and otd.periodo.estatus=1))")
                .append("        and o.proceso is not null ")
                .append("        and pr.procesoEstatus.id=4 ")
                .append("        and o.periodo.estatus=1 ")
                .append("        and o.excluirDeposito=0 ")
                .append("        and d.tarjetaipn=1) ")
                .append("AND NOT EXISTS(SELECT 1 FROM AlumnoTarjetaBancaria atb WHERE atb.alumno.id = a.id )");
        Map<String, Object> parametrosServidor = new HashMap<>();
        if (boleta != null && !boleta.isEmpty()) {
            parametrosServidor.put("a.boleta", boleta);
        }
        if (nivel != null && !nivel.equals(BigDecimal.ZERO)) {
            parametrosServidor.put("a.unidadAcademica.nivel.id", nivel);
        }
        if (unidadAcademica != null && !unidadAcademica.equals(BigDecimal.ZERO)) {
            parametrosServidor.put("a.unidadAcademica.id", unidadAcademica);
        }
        Object[] paramsFiltered = addParameters(consultaTotal, Boolean.FALSE, parametrosServidor);
        consultaTotal.append(" ORDER BY a.unidadAcademica.id,a.curp");
        return executeQuery(consultaTotal.toString(), paramsFiltered);
    }

    private void becariosOrderBy(StringBuilder consultaTotal, int sortingCol, String sortDir) {
        switch (sortingCol) {
            case 1:
                consultaTotal.append(" order by atb.a.apellidopaterno ").append(sortDir);
                break;
            case 2:
                consultaTotal.append(" order by atb.a.curp ").append(sortDir);
                break;
            case 3:
                consultaTotal.append(" order by atb.a.boleta ").append(sortDir);
                break;
            case 4:
                consultaTotal.append(" order by atb.tarjetaBancaria.numtarjetabancaria ").append(sortDir);
                break;
            default:
                if (sortDir == null || sortDir.equals("")) {
                    sortDir = "asc";
                }
                consultaTotal.append(" order by atb.alumno.unidadAcademica.id ").append(sortDir);
                break;
        }
    }

    @Override
    public Boolean estatusAlumno(String noBoleta) {
        String jpql = "SELECT a FROM Alumno a WHERE a.boleta = ?1";
        //select estatus from ent_alumno where boleta = 1234567891;
        return executeSingleQuery(jpql, noBoleta).getEstatus();
    }

    @Override
    public PaginateUtil solicitudAlumnos(ServerSideUtil ssu) {
        StringBuilder sbQuery = new StringBuilder("SELECT a FROM Alumno a")
                .append(" JOIN DatosAcademicos da ON da.alumno.id = a.id")
                .append(" WHERE 1 = 1")
                .append(" AND da.id = (")
                .append(" SELECT MAX(dda.id) FROM DatosAcademicos dda WHERE dda.alumno.id = a.id")
                .append(" )");
        Object[] params = addParameters(sbQuery, Boolean.FALSE, ssu.parametros);
        Long noTotal = getCountQuery(buildCountQuery(sbQuery.toString(), Boolean.FALSE), params);
        solicitudAlumnosOrderBy(sbQuery, ssu.getSortCol(), ssu.getSortDir());
        List<Alumno> lista = executeQueryPaginate(sbQuery.toString(), ssu.getStart(), ssu.getLength(), params);
        return new PaginateUtil(lista, noTotal, noTotal);
    }

    private void envCorreosAlumnosOrderBy(StringBuilder sbQuery, int sortingCol, String sortDir) {
        if (sortDir != null) {
            StringBuilder orderBy = new StringBuilder(" order by ");
            switch (sortingCol) {
                case 0:
                    orderBy.append("a.boleta ");
                    break;
                case 1:
                    orderBy.append("a.apellidoPaterno ");
                    break;
                case 2:
                    orderBy.append("ua.nombreCorto ");
                    break;
                default:
                    return;
            }
            orderBy.append(sortDir);
            sbQuery.append(orderBy.toString());
        }
    }

    private void solicitudAlumnosOrderBy(StringBuilder sbQuery, int sortingCol, String sortDir) {
        if (sortDir != null) {
            StringBuilder orderBy = new StringBuilder(" order by ");
            switch (sortingCol) {
                case 0:
                    orderBy.append("a.boleta ");
                    break;
                case 1:
                    orderBy.append("a.nombre ");
                    break;
                case 2:
                    orderBy.append("a.unidadAcademica.nombre ");
                    break;
                default:
                    return;
            }
            orderBy.append(sortDir);
            sbQuery.append(orderBy.toString());
        }
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
//------------------------------------------------------------------------------------------------------------------------------

    @Override
    public List<Alumno> alumnosRegistrados(String nivel, String unidadAcademica) {
        String consulta = "select a.* from ENT_ALUMNO a "
                + "join ENT_ALUMNO_DATOS_ACADEMICOS da on a.ID = da.ALUMNO_ID and da.PERIODO_ID = (select id from cat_periodo where estatus=1 and rownum = 1) "
                + "join CAT_UNIDAD_ACADEMICA u on u.id = da.UNIDADACADEMICA_ID "
                + "where a.ESTATUS = 1 ";
        consulta = parametros(consulta, nivel, unidadAcademica, null, null, null, null, null, null);
        Query q = entityManager.createNativeQuery(consulta, Alumno.class);
        List<Alumno> lista = q.getResultList();
        return lista;
    }

    @Override
    public List<Alumno> alumnosRevalidantesNoRegistrados(String nivel, String unidadAcademica) {
        String consulta = "select a.* from ENT_ALUMNO a"
                + " join ENT_ALUMNO_DATOS_ACADEMICOS da on a.ID = da.ALUMNO_ID "
                + " join CAT_UNIDAD_ACADEMICA u on u.id = da.UNIDADACADEMICA_ID "
                + " JOIN ENT_OTORGAMIENTOS o on a.id = o.ALUMNO_ID "
                + " where a.estatus = 0 "
                + " AND o.PROCESO_ID is not null "
                + " and o.PERIODO_ID = (SELECT periodoanterior_id from cat_periodo p where p.estatus = 1 and rownum = 1)";
        consulta = parametros(consulta, nivel, unidadAcademica, null, null, null, null, null, null);
        Query q = entityManager.createNativeQuery(consulta, Alumno.class);
        List<Alumno> lista = q.getResultList();
        return lista;
    }

    @Override
    public List<Alumno> alumnosESEincompleto(String nivel, String unidadAcademica) {
        String consulta = "select a.* from ENT_ALUMNO a "
                + "join ENT_ALUMNO_DATOS_ACADEMICOS da on a.ID = da.ALUMNO_ID and da.PERIODO_ID = (select id from cat_periodo where estatus=1 and rownum = 1) "
                + "join CAT_UNIDAD_ACADEMICA u on u.id = da.UNIDADACADEMICA_ID "
                + "where a.ESTATUS = 1 "
                + "and a.id not in( "
                + "select a.id from ENT_SOLICITUD_BECAS c  "
                + "inner join ent_alumno a on a.id = c.alumno_id   "
                + "where c.cuestionario_id= 1  "
                + "and a.estatus = 1   "
                + "and c.periodo_id = (select p.id from cat_periodo p where p.estatus=1) "
                + ")";
        consulta = parametros(consulta, nivel, unidadAcademica, null, null, null, null, null, null);
        Query q = entityManager.createNativeQuery(consulta, Alumno.class);
        List<Alumno> lista = q.getResultList();
        return lista;
    }

    @Override
    public List<Alumno> alumnosOtorgamientoTarjetaUA(String nivel, String unidadAcademica) {
        String consulta = "select a.* from ENT_ALUMNO a "
                + "join ENT_ALUMNO_DATOS_ACADEMICOS da on a.ID = da.ALUMNO_ID and da.PERIODO_ID = (select id from cat_periodo where estatus=1 and rownum = 1) "
                + "join CAT_UNIDAD_ACADEMICA u on u.id = da.UNIDADACADEMICA_ID "
                + "JOIN ENT_OTORGAMIENTOS o on a.id = o.ALUMNO_ID "
                + "JOIN RMM_ALUMNO_TARJETA_BANCARIA at on at.ALUMNO_ID = a.ID "
                + "JOIN VW_ULTIMA_BITACORA_TARJETA t on at.TARJETABANCARIA_ID = t.TARJETABANCARIA_ID "
                + "where o.ALUMNO_ID = a.id "
                + "and o.ALTA = 1 "
                + "AND at.VIGENTE=1 "
                + "and o.PERIODO_ID = (SELECT id from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "and t.TARJETABANCARIAESTATUS_ID = 8";
        consulta = parametros(consulta, nivel, unidadAcademica, null, null, null, null, null, null);
        Query q = entityManager.createNativeQuery(consulta, Alumno.class);
        List<Alumno> lista = q.getResultList();
        return lista;
    }

    @Override
    public List<Alumno> alumnosOtorgamientoTarjetaEntregada(String nivel, String unidadAcademica) {
        String consulta = "select a.* from ENT_ALUMNO a "
                + "join ENT_ALUMNO_DATOS_ACADEMICOS da on a.ID = da.ALUMNO_ID and da.PERIODO_ID = (select id from cat_periodo where estatus=1 and rownum = 1) "
                + "join CAT_UNIDAD_ACADEMICA u on u.id = da.UNIDADACADEMICA_ID "
                + "JOIN ENT_OTORGAMIENTOS o on a.id = o.ALUMNO_ID "
                + "JOIN RMM_ALUMNO_TARJETA_BANCARIA at on at.ALUMNO_ID = a.ID "
                + "JOIN VW_ULTIMA_BITACORA_TARJETA t on at.TARJETABANCARIA_ID = t.TARJETABANCARIA_ID "
                + "where o.ALUMNO_ID = a.id "
                + "and o.ALTA = 1 "
                + "AND at.VIGENTE=1 "
                + "and o.PERIODO_ID = (SELECT id from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "and t.TARJETABANCARIAESTATUS_ID = 9";
        consulta = parametros(consulta, nivel, unidadAcademica, null, null, null, null, null, null);
        Query q = entityManager.createNativeQuery(consulta, Alumno.class);
        List<Alumno> lista = q.getResultList();
        return lista;
    }

    @Override
    public List<Alumno> alumnosDatosIncorrectos(String nivel, String unidadAcademica, BigDecimal periodoId) {
        String dataUA = " INNER join ENT_ALUMNO_DATOS_ACADEMICOS da on a.ID = da.ALUMNO_ID and da.PERIODO_ID = " + periodoId.toString()
                + " INNER join CAT_UNIDAD_ACADEMICA u on u.id = da.UNIDADACADEMICA_ID";
        if (nivel == null || nivel.length() < 1) {
            dataUA = "";
        }
        String queryNormal = " SELECT * FROM ENT_ALUMNO WHERE id in ("
                + " SELECT o.alumno_id AS alumno_id"
                + " from VW_OTORGAMIENTOS o"
                + " inner join ent_tipo_beca_periodo tbp on o.tipobecaperiodo_id = tbp.id"
                + " inner join cat_tipo_beca tb on tbp.tipobeca_id = tb.id"
                + " inner join ENT_ALUMNO a ON o.alumno_id = a.ID"
                + " INNER JOIN RMM_ALUMNO_TARJETA_BANCARIA aTB ON aTB.ALUMNO_ID = a.ID"
                + dataUA
                + " LEFT JOIN ent_deposito d ON o.id = d.OTORGAMIENTO_ID and d.ESTATUSDEPOSITO_ID IN (2,3)"
                + " left JOIN ENT_ORDEN_DEPOSITO od ON od.id = d.ORDENDEPOSITO_ID"
                + " WHERE o.PERIODO_ID = " + periodoId.toString()
                + " and o.alta = 1"
                + " and a.ESTATUS = 1"
                + " AND aTB.ESTATUSTARJBANC_ID = 14";
        String queryTransporte = queryNormal;
        queryNormal += " and tb.beca_id not in (7, 8) ";
        queryTransporte += " and tb.beca_id in (7, 8) ";

        queryNormal = parametros(queryNormal, nivel, unidadAcademica, null, null, null, null, null, null);
        queryTransporte = parametros(queryTransporte, nivel, unidadAcademica, null, null, null, null, null, null);
        queryNormal += " GROUP BY o.alumno_id, tb.beca_id, o.id "
                + " having COUNT (d.id) < 6 )";
        queryTransporte += " GROUP BY o.alumno_id, tb.beca_id, o.id "
                + " having COUNT (d.id) < 5 )";
        Query q = entityManager.createNativeQuery(queryNormal, Alumno.class);
        Query qT = entityManager.createNativeQuery(queryTransporte, Alumno.class);
        List<Alumno> lista = q.getResultList();
        List<Alumno> listaT = qT.getResultList();
        List<Alumno> fullList = new ArrayList<Alumno>(lista);
        fullList.addAll(listaT);
        return fullList;
    }

    @Override
    public List<Alumno> alumnosFiltros(String nivel, String unidadAcademica, String beca, String tipoBeca, String movimiento, String proceso, String alumnos) {
        String consulta = "select distinct a.* from ENT_ALUMNO a "
                + "join ENT_ALUMNO_DATOS_ACADEMICOS da on a.ID = da.ALUMNO_ID and da.PERIODO_ID = (select id from cat_periodo where estatus=1 and rownum = 1) "
                + "JOIN CAT_UNIDAD_ACADEMICA u on u.id = da.UNIDADACADEMICA_ID "
                + "JOIN ENT_OTORGAMIENTOS o on a.id = o.ALUMNO_ID "
                + "LEFT JOIN ENT_PROCESO p on p.id = o.PROCESO_ID "
                + "LEFT JOIN CAT_TIPO_PROCESO tp on tp.id = p.TIPOPROCESO_ID "
                + "LEFT JOIN ENT_TIPO_BECA_PERIODO tbp on tbp.id = o.TIPOBECAPERIODO_ID "
                + "LEFT JOIN CAT_TIPO_BECA tb on tb.id = tbp.TIPOBECA_ID "
                + "LEFT JOIN CAT_PROGRAMA_BECA b on b.id = tb.BECA_ID "
                + "where o.PERIODO_ID = (select p.id from cat_periodo p where p.estatus=1) "
                + "AND o.PROCESO_ID IS NOT NULL";
        consulta = parametros(consulta, nivel, unidadAcademica, beca, tipoBeca, movimiento, proceso, alumnos, null);
        Query q = entityManager.createNativeQuery(consulta, Alumno.class);
        List<Alumno> lista = q.getResultList();
        return lista;
    }
    
    @Override
    public List<Alumno> getSolicitudes(String identificador) {
        String consulta = "select a.* from ENT_ALUMNO a "
                + "INNER join RMM_ALUMNO_TARJETA_BANCARIA atb on atb.alumno_id = a.id \n"
                + "INNER JOIN ENT_SOLICITUD_CUENTAS s ON s.ID = atb.SOLICITUDCUENTA_ID "
                + "WHERE s.IDENTIFICADOR = ?1 ";
        System.out.println(consulta);
        Query q = entityManager.createNativeQuery(consulta, Alumno.class);
        q.setParameter(1, identificador);
        List<Alumno> lista = q.getResultList();
        return (lista);
    }

    @Override
    public List<Alumno> alumnosL(String nivel, String unidadAcademica, String alumnosL) {
        String consulta = "select a.* from ENT_ALUMNO a "
                + "join ENT_ALUMNO_DATOS_ACADEMICOS da on a.ID = da.ALUMNO_ID and da.PERIODO_ID = (select id from cat_periodo where estatus=1 and rownum = 1) "
                + "JOIN CAT_UNIDAD_ACADEMICA u on u.id = da.UNIDADACADEMICA_ID";
        consulta = parametros(consulta, null, unidadAcademica, null, null, null, null, null, alumnosL);
        Query q = entityManager.createNativeQuery(consulta, Alumno.class);
        List<Alumno> lista = q.getResultList();
        return lista;
    }

    public String parametros(String consulta, String nivel, String unidadAcademica, String beca, String tipoBeca, String movimiento, String proceso, String alumnos, String alumnosL) {
        consulta += nivel == null || nivel.length() < 1 || nivel.equals("0") ? "" : " and u.nivel_id = " + nivel;
        consulta += unidadAcademica == null || unidadAcademica.length() < 1 || unidadAcademica.equals("0") ? "" : " and u.id = " + unidadAcademica;
        consulta += beca == null || beca.length() < 1 || beca.equals("0") ? "" : " and b.id = " + beca;
        consulta += tipoBeca == null || tipoBeca.length() < 1 || tipoBeca.equals("0") ? "" : " and tb.id = " + tipoBeca;
        consulta += movimiento == null || movimiento.length() < 1 ? "" : " and tp.MOVIMIENTO_ID = " + movimiento;
        consulta += proceso == null || proceso.length() < 1 ? "" : " and tp.id = " + proceso;
        consulta += alumnos == null || alumnos.length() < 2 ? "" : " and a.id NOT IN (" + alumnos + ")";
        consulta += alumnosL == null || alumnosL.length() < 2 ? "" : " and a.id IN (" + alumnosL + ")";
        return consulta;
    }

    @Override
    public BigDecimal alumnosRegistradosC(String nivel, String unidadAcademica) {
        String consulta = "select count(a.id) from ENT_ALUMNO a "
                + "join ENT_ALUMNO_DATOS_ACADEMICOS da on a.ID = da.ALUMNO_ID and da.PERIODO_ID = (select id from cat_periodo where estatus=1 and rownum = 1) "
                + "join CAT_UNIDAD_ACADEMICA u on u.id = da.UNIDADACADEMICA_ID "
                + "where a.ESTATUS = 1";
        consulta = parametros(consulta, nivel, unidadAcademica, null, null, null, null, null, null);
        List<Object[]> lista = executeNativeQuery(consulta);
        Object x = lista.get(0);
        return (BigDecimal) x;
    }

    @Override
    public BigDecimal alumnosRevalidantesNoRegistradosC(String nivel, String unidadAcademica) {
        String consulta = "select count(a.id) from ENT_ALUMNO a "
                + " join ENT_ALUMNO_DATOS_ACADEMICOS da on a.ID = da.ALUMNO_ID"
                + " join CAT_UNIDAD_ACADEMICA u on u.id = da.UNIDADACADEMICA_ID "
                + " JOIN ENT_OTORGAMIENTOS o on a.id = o.ALUMNO_ID "
                + " where a.estatus = 0 "
                + " AND o.PROCESO_ID is not null "
                + " and o.PERIODO_ID = (SELECT periodoanterior_id from cat_periodo p where p.estatus = 1 and rownum = 1)";
        consulta = parametros(consulta, nivel, unidadAcademica, null, null, null, null, null, null);
        List<Object[]> lista = executeNativeQuery(consulta);
        Object x = lista.get(0);
        return (BigDecimal) x;
    }

    @Override
    public BigDecimal alumnosESEincompletoC(String nivel, String unidadAcademica) {
        String consulta = "select count(a.id) from ENT_ALUMNO a "
                + "join ENT_ALUMNO_DATOS_ACADEMICOS da on a.ID = da.ALUMNO_ID and da.PERIODO_ID = (select id from cat_periodo where estatus=1 and rownum = 1) "
                + "join CAT_UNIDAD_ACADEMICA u on u.id = da.UNIDADACADEMICA_ID "
                + "where a.ESTATUS = 1 "
                + "and a.id not in( "
                + "select a.id from ENT_SOLICITUD_BECAS c  "
                + "inner join ent_alumno a on a.id = c.alumno_id   "
                + "where c.cuestionario_id= 1  "
                + "and a.estatus = 1   "
                + "and c.periodo_id = (select p.id from cat_periodo p where p.estatus=1) "
                + ")";
        consulta = parametros(consulta, nivel, unidadAcademica, null, null, null, null, null, null);
        List<Object[]> lista = executeNativeQuery(consulta);
        Object x = lista.get(0);
        return (BigDecimal) x;
    }

    @Override
    public BigDecimal alumnosOtorgamientoTarjetaUAC(String nivel, String unidadAcademica) {
        String consulta = "select count(a.id) from ENT_ALUMNO a "
                + "join ENT_ALUMNO_DATOS_ACADEMICOS da on a.ID = da.ALUMNO_ID and da.PERIODO_ID = (select id from cat_periodo where estatus=1 and rownum = 1) "
                + "join CAT_UNIDAD_ACADEMICA u on u.id = da.UNIDADACADEMICA_ID "
                + "JOIN ENT_OTORGAMIENTOS o on a.id = o.ALUMNO_ID "
                + "JOIN RMM_ALUMNO_TARJETA_BANCARIA at on at.ALUMNO_ID = a.ID "
                + "JOIN VW_ULTIMA_BITACORA_TARJETA t on at.TARJETABANCARIA_ID = t.TARJETABANCARIA_ID "
                + "where o.ALUMNO_ID = a.id "
                + "and o.ALTA = 1 "
                + "AND at.VIGENTE=1 "
                + "and o.PERIODO_ID = (SELECT id from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "and t.TARJETABANCARIAESTATUS_ID = 8";
        consulta = parametros(consulta, nivel, unidadAcademica, null, null, null, null, null, null);
        List<Object[]> lista = executeNativeQuery(consulta);
        Object x = lista.get(0);
        return (BigDecimal) x;
    }

    @Override
    public BigDecimal alumnosDatosIncorrectosC(String nivel, String unidadAcademica, BigDecimal periodoId) {
        String dataUA = " INNER join ENT_ALUMNO_DATOS_ACADEMICOS da on a.ID = da.ALUMNO_ID and da.PERIODO_ID = ?1"
                + " INNER join CAT_UNIDAD_ACADEMICA u on u.id = da.UNIDADACADEMICA_ID";
        Boolean lvl = nivel == null || nivel.length() < 1;
        Boolean aUnit = unidadAcademica == null || unidadAcademica.length() < 1;
        if (lvl && aUnit) {
            dataUA = "";
        }
        String queryNormal = " SELECT o.alumno_id AS alumno_id"
                + " from VW_OTORGAMIENTOS o"
                + " inner join ent_tipo_beca_periodo tbp on o.tipobecaperiodo_id = tbp.id"
                + " inner join cat_tipo_beca tb on tbp.tipobeca_id = tb.id"
                + " inner join ENT_ALUMNO a ON o.alumno_id = a.ID"
                + " INNER JOIN RMM_ALUMNO_TARJETA_BANCARIA aTB ON aTB.ALUMNO_ID = a.ID"
                + dataUA
                + " LEFT JOIN ent_deposito d ON o.id = d.OTORGAMIENTO_ID and d.ESTATUSDEPOSITO_ID IN (2,3)"
                + " left JOIN ENT_ORDEN_DEPOSITO od ON od.id = d.ORDENDEPOSITO_ID"
                + " WHERE o.PERIODO_ID = ?1"
                + " and o.alta = 1"
                + " and a.ESTATUS = 1"
                + " AND aTB.ESTATUSTARJBANC_ID = 14";
        String queryTransporte = queryNormal;
        queryNormal += " and tb.beca_id not in (7, 8) ";
        queryTransporte += " and tb.beca_id in (7, 8) ";
        queryNormal = parametros(queryNormal, nivel, unidadAcademica, null, null, null, null, null, null);
        queryTransporte = parametros(queryTransporte, nivel, unidadAcademica, null, null, null, null, null, null);
        queryNormal += " GROUP BY o.alumno_id, tb.beca_id, o.id "
                + " having COUNT (d.id) < 6 ";
        queryTransporte += " GROUP BY o.alumno_id, tb.beca_id, o.id "
                + " having COUNT (d.id) < 5 ";
        List<Object[]> lista = executeNativeQuery(buildCountQuery(queryNormal, Boolean.TRUE), periodoId);
        List<Object[]> listaT = executeNativeQuery(buildCountQuery(queryTransporte, Boolean.TRUE), periodoId);
        Object x = lista.get(0);
        Object xT = listaT.get(0);
        return ((BigDecimal) x).add((BigDecimal) xT);
    }

    @Override
    public BigDecimal alumnosOtorgamientoTarjetaEntregadaC(String nivel, String unidadAcademica) {
        String consulta = "select count(a.id) from ENT_ALUMNO a "
                + "join ENT_ALUMNO_DATOS_ACADEMICOS da on a.ID = da.ALUMNO_ID and da.PERIODO_ID = (select id from cat_periodo where estatus=1 and rownum = 1) "
                + "join CAT_UNIDAD_ACADEMICA u on u.id = da.UNIDADACADEMICA_ID "
                + "JOIN ENT_OTORGAMIENTOS o on a.id = o.ALUMNO_ID "
                + "JOIN RMM_ALUMNO_TARJETA_BANCARIA at on at.ALUMNO_ID = a.ID "
                + "JOIN VW_ULTIMA_BITACORA_TARJETA t on at.TARJETABANCARIA_ID = t.TARJETABANCARIA_ID "
                + "where o.ALUMNO_ID = a.id "
                + "and o.ALTA = 1 "
                + "AND at.VIGENTE=1 "
                + "and o.PERIODO_ID = (SELECT id from cat_periodo p where p.estatus = 1 and rownum = 1) "
                + "and t.TARJETABANCARIAESTATUS_ID = 9";
        consulta = parametros(consulta, nivel, unidadAcademica, null, null, null, null, null, null);
        List<Object[]> lista = executeNativeQuery(consulta);
        Object x = lista.get(0);
        return (BigDecimal) x;
    }

    @Override
    public PaginateUtil alumnosFiltros(ServerSideUtil ssu) {
        StringBuilder sbQuery = new StringBuilder("select DISTINCT a.* FROM ent_otorgamientos o "
                + "inner join ent_alumno a on a.id = o.ALUMNO_ID "
                + "inner join cat_periodo p on p.id = o.PERIODO_ID "
                + "inner join ent_proceso pr on pr.id = o.PROCESO_ID "
                + "inner join ENT_ALUMNO_DATOS_ACADEMICOS da on da.id = o.DATOSACADEMICOS_ID "
                + "inner join cat_unidad_academica ua on ua.id = da.UNIDADACADEMICA_ID "
                + "inner join cat_tipo_proceso tp on tp.id = pr.TIPOPROCESO_ID "
                + "inner join ent_tipo_beca_periodo tbp on tbp.id = o.TIPOBECAPERIODO_ID "
                + "inner join cat_tipo_beca tb on tb.id = tbp.TIPOBECA_ID "
                + "WHERE p.ESTATUS = 1 "
                + "AND pr.ID IS NOT NULL ");
        Object[] params = addParameters(sbQuery, Boolean.FALSE, ssu.parametros);
        Long noTotal = getCountNativeQuery(buildCountQuery(sbQuery.toString(), Boolean.TRUE), params);
        envCorreosAlumnosOrderBy(sbQuery, ssu.getSortCol(), ssu.getSortDir());
        return new PaginateUtil(executeNativeQueryPagClass(sbQuery.toString(), ssu.getStart(), ssu.getLength(), Alumno.class, params), noTotal, noTotal);
    }

    @Override
    public PaginateUtil alumnosL(ServerSideUtil ssu, String alumnosL) {
        StringBuilder sbQuery = new StringBuilder("SELECT a FROM Alumno a "
                + "WHERE a.id IN ( "
                + alumnosL.substring(1, alumnosL.length() - 1)
                + ")");
        Object[] params = addParameters(sbQuery, Boolean.FALSE, ssu.parametros);
        Long noTotal = getCountQuery(buildCountQuery(sbQuery.toString(), Boolean.FALSE), params);
        solicitudAlumnosOrderBy(sbQuery, ssu.getSortCol(), ssu.getSortDir());
        List<Alumno> lista = executeQueryPaginate(sbQuery.toString(), ssu.getStart(), ssu.getLength(), params);
        return new PaginateUtil(lista, noTotal, noTotal);
    }
//------------------------------------------------------------------------------------------------------------------------------

    @Override
    public Integer alumnos0sRevalidantes() {
        BigDecimal idActivo = getDaos().getPeriodoDao().getPeriodoActivo().getId();
        BigDecimal idAnterior = getDaos().getPeriodoDao().getPeriodoActivo().getPeriodoAnterior().getId();
        String sql = "select a.* from ent_alumno a "
                + "inner join ENT_SOLICITUD_BECAS cpa on a.id = cpa.alumno_id "
                + "inner join ent_otorgamientos o on a.id = o.alumno_id "
                + "inner join ent_tipo_beca_periodo tbp on tbp.id = o.tipobecaperiodo_id "
                + "where a.estatus=1 and a.inscrito=1 and a.regular=1 and a.promedio=0 "
                + "and cpa.periodo_id=" + idActivo
                + " and o.periodo_id =" + idAnterior
                + " and a.semestre between tbp.semestreMinimo and tbp.semestreMaximo ";
        List<Object[]> lista = executeNativeQuery(sql);
        return lista.size();
    }

    @Override
    public Integer alumnos0sNuevos() {
        BigDecimal idActivo = getDaos().getPeriodoDao().getPeriodoActivo().getId();
        BigDecimal idAnterior = getDaos().getPeriodoDao().getPeriodoActivo().getPeriodoAnterior().getId();
        String sql = "SELECT a.* FROM ent_alumno a "
                + "inner join ENT_SOLICITUD_BECAS cpa on a.id = cpa.alumno_id "
                + "where a.id not in (SELECT alumno_id FROM ent_otorgamientos o "
                + "where o.periodo_id =" + idAnterior
                + " or o.periodo_id=" + idActivo + ")"
                + " and cpa.periodo_id=" + idActivo
                + " and a.estatus=1 and a.inscrito=1 and a.regular=1 and a.promedio = 0 ";
        List<Object[]> lista = executeNativeQuery(sql);
        return lista.size();
    }

    @Override
    public Integer alumnosNoAsinadosAutomaticamente() {
        String sql = "SELECT a.* FROM ent_alumno a "
                + "inner join ENT_SOLICITUD_BECAS cpa on a.id = cpa.alumno_id "
                + "where a.id not in (SELECT alumno_id FROM ent_otorgamientos o "
                + "where o.periodo_id =(select pe.periodoanterior_id from cat_periodo pe where pe.estatus = 1 and rownum = 1) "
                + "or o.periodo_id=(select pe.id from cat_periodo pe where pe.estatus = 1 and rownum = 1)) "
                + "and cpa.periodo_id=(select pe.id from cat_periodo pe where pe.estatus = 1 and rownum = 1) "
                + "and a.estatus=1 and a.inscrito=1 and regular=1 ";
        List<Object[]> lista = executeNativeQuery(sql);
        return lista.size();
    }

    /**
     * @author Victor Lozano
     * @param idAlumno
     * @return true en caso de exito
     */
    @Override
    public Boolean tieneOtorgamiento(BigDecimal idAlumno) {
        String consulta = "SELECT a FROM Alumno a, "
                + "Otorgamiento o "
                + "WHERE a.id = ?1 "
                + "AND a.id = o.alumno.id "
                + "AND o.alta = 1 "
                + "AND o.proceso.id IS NOT NULL "
                + "AND o.periodo.id = " + getDaos().getPeriodoDao().getPeriodoActivo().getId();
        List<Alumno> lista = executeQuery(consulta, idAlumno);
        return lista == null || lista.isEmpty() ? Boolean.FALSE : Boolean.TRUE;
    }

    /**
     * @author Victor Lozano
     * @param idAlumno,tipo,Periodo en el cual buscar tipo 1 Transporte
     * Manutencion tipo 2 Transporte Institucional tipo 0 Transporte de
     * qualquier tipo
     * @return true en caso de exito
     */
    @Override
    public Boolean tieneOtorgamientoTransporte(BigDecimal idAlumno, int tipo, Periodo periodo) {
        String consulta = "SELECT a FROM Alumno a, "
                + "Otorgamiento o "
                + "WHERE a.id = ?1 "
                + "AND a.id = o.alumno.id "
                + "AND o.alta = 1 "
                + "AND o.proceso.id IS NOT NULL ";
        switch (tipo) {
            case 1:
                consulta += "AND o.tipoBecaPeriodo.tipoBeca.beca.clave = 'TM'";
                break;
            case 2:
                consulta += "AND o.tipoBecaPeriodo.tipoBeca.beca.clave = 'TI'";
                break;
            default:
                consulta += "AND o.tipoBecaPeriodo.tipoBeca.beca.clave IN ('TI','TM')";
        }
        consulta += "AND o.periodo.id = ?2 ";
        List<Alumno> lista = executeQuery(consulta, idAlumno, periodo.getId());
        return lista == null || lista.isEmpty() ? Boolean.FALSE : Boolean.TRUE;
    }

    /**
     * @author Victor Lozano
     * @param idAlumno
     * @return true en caso de exito
     */
    @Override
    public Boolean tieneBaja(BigDecimal idAlumno) {
        String consulta = "SELECT * FROM ent_alumno a "
                + "JOIN ent_otorgamientos o ON a.id = o.alumno_id "
                + "JOIN vw_ultima_baja_detalle ubd ON ubd.otorgamiento_id = o.id "
                + "WHERE a.id = ?1 "
                + "AND o.alta = 0 "
                + " AND o.periodo_id = " + getDaos().getPeriodoDao().getPeriodoActivo().getId();
        List<Object[]> lista = executeNativeQuery(consulta, idAlumno);
        return lista == null || lista.isEmpty() ? Boolean.FALSE : Boolean.TRUE;
    }


    /**
     * Devuelve un alumno buscado en relación a su usuario
     *
     * @author Victor Lozano
     * @param usuarioId
     * @return alumno
     */
    @Override
    public Alumno getByUsuario(BigDecimal usuarioId) {
        String consulta = "SELECT a FROM Alumno a WHERE a.usuario.id = ?1";
        List<Alumno> alumnos = executeQuery(consulta, usuarioId);
        return alumnos == null || alumnos.isEmpty() ? null : alumnos.get(0);
    }

    /**
     * Conteo de alumnos inscritos en el periodo activo
     *
     * @author Patricia Benítez
     * @param unidadAcademicaID
     * @return Total de Alumnos
     */
    @Override
    public BigDecimal totalAlumnosRegistrados(BigDecimal unidadAcademicaID) {
        String sql = "select count(a.id),1 from ent_alumno a "
                + "join ENT_ALUMNO_DATOS_ACADEMICOS da on a.id = da.ALUMNO_ID and da.PERIODO_ID = (select id from cat_periodo where estatus=1 and rownum = 1) "
                + "where a.estatus = 1 and not exists (select 1 from ent_otorgamientos o "
                + "JOIN ent_tipo_beca_periodo tbp on o.tipobecaperiodo_id = tbp.id "
                + "JOIN cat_tipo_beca tb on tbp.tipobeca_id=tb.id "
                + "  where o.periodo_id=(select periodoanterior_id from cat_periodo where estatus=1) "
                + "    and proceso_id is not null  "
                + "    and (o.alta=1 or not exists "
                + "       (select 1 from ent_otorgamientos_bajas_detall b where b.otorgamiento_id=o.id and b.periodo_id=(select periodoanterior_id from cat_periodo where estatus=1))) "
                + "    and tb.beca_id!=8 "
                + "    and o.alumno_id=a.id)";
        sql += unidadAcademicaID == null ? "" : " and da.UNIDADACADEMICA_ID = " + unidadAcademicaID;
        List<Object[]> lista = executeNativeQuery(sql);
        BigDecimal total = new BigDecimal(0);
        if (lista != null && !lista.isEmpty()) {
            for (Object[] res : lista) {
                total = res[0] == null ? new BigDecimal(0) : ((BigDecimal) res[0]);
            }
        }
        return total;
    }

    @Override
    public String totalAlumnosRegistradosD(BigDecimal nivelId) {
        String consulta = "select ua. ID, ua.NOMBRECORTO, count(a.id) "
                + " from ENT_ALUMNO a, CAT_UNIDAD_ACADEMICA ua, ENT_ALUMNO_DATOS_ACADEMICOS da "
                + " where a.estatus = 1 and not exists (select 1 from ent_otorgamientos o "
                + "JOIN ent_tipo_beca_periodo tbp on o.tipobecaperiodo_id = tbp.id "
                + "JOIN cat_tipo_beca tb on tbp.tipobeca_id=tb.id "
                + "  where o.periodo_id=(select periodoanterior_id from cat_periodo where estatus=1) "
                + "    and proceso_id is not null  "
                + "    and (o.alta=1 or not exists "
                + "       (select 1 from ent_otorgamientos_bajas_detall b where b.otorgamiento_id=o.id and b.periodo_id=(select periodoanterior_id from cat_periodo where estatus=1))) "
                + "    and tb.beca_id!=8 "
                + "    and o.alumno_id=a.id)  "
                + " and a.id = da.ALUMNO_ID and da.PERIODO_ID = (select id from cat_periodo where estatus=1 and rownum = 1) "
                + " and da.UNIDADACADEMICA_ID = ua.ID"
                + " and ua.NIVEL_ID = ?1"
                + " GROUP BY ua.ID, ua.NOMBRECORTO"
                + " ORDER BY ua.ID";
        List<Object[]> ax = executeNativeQuery(consulta, nivelId);
        String s = "";
        for (Object[] res : ax) {
            s = s + res[1] + "," + res[2] + "/";
        }
        if (s.length() > 1) {
            return s;
        } else {
            return null;
        }
    }

    /**
     * Busca los alumnos que cumplen con ciertos parametros
     *
     * @author Victor Lozano
     * @param ssu
     * @return SUCCESS
     */
    @Override
    public PaginateUtil busquedaAlumnos(ServerSideUtil ssu) {
        StringBuilder sbQuery = new StringBuilder("SELECT a.* from ENT_ALUMNO a "
                + "JOIN ENT_USUARIO u ON U.ID = a.USUARIO_ID "
                + "JOIN  ENT_ALUMNO_DATOS_ACADEMICOS da on da.ALUMNO_ID = a.id and da.id = (select max(dax.id) from ENT_ALUMNO_DATOS_ACADEMICOS dax WHERE dax.ALUMNO_ID = a.id) "
                + (ssu.getSortCol() == 2 ? "join cat_unidad_academica ua on ua.id = da.UNIDADACADEMICA_ID " : "")
                + "WHERE u.ACTIVO=1 "
        );
        if (String.valueOf(ActionContext.getContext().getSession().get("privilegio")).equals("ROLE_FUNCIONARIO_UA") || String.valueOf(ActionContext.getContext().getSession().get("privilegio")).equals("ROLE_RESPONSABLE_UA")) {
            Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
            PersonalAdministrativo personal = getDaos().getPersonalAdministrativoDao().findByUsuario(usuario.getId());
            BigDecimal unidadAcademicaId = personal.getUnidadAcademica().getId();
            sbQuery.append("AND  da.id=(SELECT max (id) FROM ENT_ALUMNO_DATOS_ACADEMICOS da2 WHERE da2.ALUMNO_ID=a.id) "
                    + "and da.UNIDADACADEMICA_ID = "
            ).append(unidadAcademicaId);
        }

        Object[] params = addParameters(sbQuery, Boolean.FALSE, ssu.parametros);
        Long noTotal = getCountNativeQuery(buildCountQuery(sbQuery.toString(), Boolean.TRUE), params);
        solicitudAlumnosNatOrderBy(sbQuery, ssu.getSortCol(), ssu.getSortDir());
        return new PaginateUtil(executeNativeQueryPagClass(sbQuery.toString(), ssu.getStart(), ssu.getLength(), Alumno.class, params), noTotal, noTotal);
    }

    private void solicitudAlumnosNatOrderBy(StringBuilder sbQuery, int sortingCol, String sortDir) {
        if (sortDir != null) {
            StringBuilder orderBy = new StringBuilder(" order by ");
            switch (sortingCol) {
                case 0:
                    orderBy.append("a.boleta ");
                    break;
                case 1:
                    orderBy.append("a.nombre ");
                    break;
                case 2:
                    orderBy.append("ua.nombre ");
                    break;
                default:
                    return;
            }
            orderBy.append(sortDir);
            sbQuery.append(orderBy.toString());
        }
    }


    /**
     * @param esNivelNMS En caso de ser <b>true</b> Devuelve el count para
     * alumnos Nivel Medio Superior, en caso contrario devuelve de Nivel
     * Superior
     * @return Número de alumnos candidatos a revalidaciones con promedio 0.
     */
    @Override
    public Integer alumnos0sRevalidantes(boolean esNivelNMS) {
        int nivel = esNivelNMS ? 1 : 2;
        BigDecimal idActivo = getDaos().getPeriodoDao().getPeriodoActivo().getId();
        BigDecimal idAnterior = getDaos().getPeriodoDao().getPeriodoActivo().getPeriodoAnterior().getId();
        String sql = "select count(*) from ent_alumno a "
                + "join ENT_ALUMNO_DATOS_ACADEMICOS da on a.ID = da.ALUMNO_ID and da.PERIODO_ID = (select id from cat_periodo where estatus=1 and rownum = 1) "
                + "inner join ENT_SOLICITUD_BECAS cpa on a.id = cpa.alumno_id "
                + "inner join ent_otorgamientos o on a.id = o.alumno_id "
                + "inner join cat_unidad_academica c on c.id = da.unidadacademica_id  "
                + "where a.estatus=1 and da.inscrito=1 and da.promedio=0 "
                + "and cpa.periodo_id=" + idActivo
                + " and CPA.CUESTIONARIO_ID = 1"
                + " and o.periodo_id =" + idAnterior
                + " and o.alta = 1"
                + " and o.proceso_id is not null"
                + " and c.nivel_id = " + nivel;
        return getCountNativeQuery(sql).intValue();
    }

    /**
     * @param esNivelNMS En caso de ser <b>true</b> Devuelve el count para
     * alumnos Nivel Medio Superior, en caso contrario devuelve de Nivel
     * Superior
     * @return Número de alumnos candidatos a nuevos otorgamientos con promedio
     * 0.
     */
    @Override
    public Integer alumnos0sNuevos(boolean esNivelNMS) {
        int nivel = esNivelNMS ? 1 : 2;
        BigDecimal idActivo = getDaos().getPeriodoDao().getPeriodoActivo().getId();
        BigDecimal idAnterior = getDaos().getPeriodoDao().getPeriodoActivo().getPeriodoAnterior().getId();
        String sql = "SELECT count(*) FROM ent_alumno a "
                + "inner join ENT_SOLICITUD_BECAS cpa on a.id = cpa.alumno_id "
                + "join ENT_ALUMNO_DATOS_ACADEMICOS da on a.ID = da.ALUMNO_ID and da.PERIODO_ID = (select id from cat_periodo where estatus=1 and rownum = 1) "
                + "inner join cat_unidad_academica c on c.id = da.unidadacademica_id "
                + "where a.id not in (SELECT alumno_id FROM ent_otorgamientos o "
                + "where o.periodo_id =" + idAnterior
                + " or o.periodo_id=" + idActivo + ")"
                + " and cpa.periodo_id=" + idActivo
                + " and CPA.CUESTIONARIO_ID = 1 "
                + " and a.estatus=1 and da.inscrito=1 and da.promedio = 0 "
                + "and c.nivel_id = " + nivel;
        return getCountNativeQuery(sql).intValue();
    }

    /**
     * Busca un alumno por nombre.
     *
     * @author Tania G. Sánchez
     * @param nombre
     * @param aPaterno
     * @param aMaterno
     * @return
     */
    @Override
    public List<Alumno> buscarPorNombre(String nombre, String aPaterno, String aMaterno) {
        String nom = "";
        if ("".equals(nombre)) {
        } else {
            nom = nom + " and a.nombre like '%" + nombre + "%'";
        }
        if ("".equals(aPaterno)) {
        } else {
            nom = nom + " and a.apellidoPaterno like '%" + aPaterno + "%'";
        }
        if ("".equals(aMaterno)) {
        } else {
            nom = nom + " and a.apellidoMaterno like '%" + aMaterno + "%'";
        }
        String jpql = "select a from Alumno a where 1=1 " + nom;
        List<Alumno> lista = executeQuery(jpql);
        return lista;
    }

    /**
     * Busca un alumno por CURP.
     *
     * @author Tania G. Sánchez
     * @param curp
     * @return
     */
    @Override
    public List<Alumno> buscarPorCURP(String curp) {
        String jpql = "select a from Alumno a where a.curp = ?1 ORDER BY a.estatus DESC, a.id DESC";
        List<Alumno> lista = executeQuery(jpql, curp);
        return lista;
    }

    /**
     * Busca un alumno por CURP y nombre.
     *
     * @author Tania G. Sánchez
     * @param curp
     * @param nombre
     * @param aPaterno
     * @param aMaterno
     * @return
     */
    @Override
    public List<Alumno> buscarPorCURPNombre(String curp, String nombre, String aPaterno, String aMaterno) {
        String nom = "";
        if ("".equals(nombre)) {
        } else {
            nom = nom + " and a.nombre like '%" + nombre + "%'";
        }
        if ("".equals(aPaterno)) {
        } else {
            nom = nom + " and a.apellidoPaterno like '%" + aPaterno + "%'";
        }
        if ("".equals(aMaterno)) {
        } else {
            nom = nom + " and a.apellidoMaterno like '%" + aMaterno + "%'";
        }
        String jpql = "select a from Alumno a where a.curp = ?1 " + nom;
        List<Alumno> lista = executeQuery(jpql, curp);
        return lista;
    }

    /**
     * Busca un alumno por boleta y nombre.
     *
     * @author Tania G. Sánchez
     * @param boleta
     * @param nombre
     * @param aPaterno
     * @param aMaterno
     * @return
     */
    @Override
    public List<Alumno> buscarPorBoletaNombre(String boleta, String nombre, String aPaterno, String aMaterno) {
        String nom = "";
        if ("".equals(nombre)) {
        } else {
            nom = nom + " and a.nombre like '%" + nombre + "%'";
        }
        if ("".equals(aPaterno)) {
        } else {
            nom = nom + " and a.apellidoPaterno like '%" + aPaterno + "%'";
        }
        if ("".equals(aMaterno)) {
        } else {
            nom = nom + " and a.apellidoMaterno like '%" + aMaterno + "%'";
        }
        String jpql = "select a from Alumno a where a.boleta = ?1 " + nom;
        List<Alumno> lista = executeQuery(jpql, boleta);
        return lista;
    }

    /**
     * Busca un alumno por boleta y CURP.
     *
     * @author Tania G. Sánchez
     * @param boleta
     * @param curp
     * @return
     */
    @Override
    public List<Alumno> buscarPorBoletaCURP(String boleta, String curp) {
        String jpql = "select a from Alumno a where a.boleta = ?1 and a.curp = ?2";
        List<Alumno> lista = executeQuery(jpql, boleta, curp);
        return lista;
    }

    /**
     * Busca un alumno por boleta, CURP y nombre.
     *
     * @author Tania G. Sánchez
     * @param boleta
     * @param curp
     * @param nombre
     * @param aPaterno
     * @param aMaterno
     * @return
     */
    @Override
    public List<Alumno> buscarPorBoletaCURPNombre(String boleta, String curp, String nombre, String aPaterno, String aMaterno) {
        String nom = "";
        if ("".equals(nombre)) {
        } else {
            nom = nom + " and a.nombre like '%" + nombre + "%'";
        }
        if ("".equals(aPaterno)) {
        } else {
            nom = nom + " and a.apellidoPaterno like '%" + aPaterno + "%'";
        }
        if ("".equals(aMaterno)) {
        } else {
            nom = nom + " and a.apellidoMaterno like '%" + aMaterno + "%'";
        }
        String jpql = "select a from Alumno a where a.boleta = ?1 and a.curp = ?2 " + nom;
        List<Alumno> lista = executeQuery(jpql, boleta, curp);
        return lista;
    }

    @Override
    public List<Alumno> nuevos(BigDecimal unidadId, Integer nivel, Periodo periodoActivo) {
        String jpql
                = "SELECT A FROM Alumno A "
                + "JOIN SolicitudBeca C ON C.alumno.id = A.id "
                + "WHERE A.estatus = 1 "
                + "AND NOT EXISTS( "
                + "    SELECT 1 FROM Otorgamiento O "
                + "    WHERE O.periodo.id = ?2 AND O.proceso IS NOT NULL AND O.alta = 1 AND O.alumno.id = A.id "
                + ") AND NOT EXISTS( "
                + "   SELECT 1 FROM Otorgamiento O WHERE O.periodo.id = ?1 AND O.alumno.id = A.id "
                + ") "
                + "AND A.inscrito = 1 "
                + "AND C.periodo.id = ?1 "
                + "AND C.cuestionario.id = 1 "
                + "AND A.unidadAcademica.nivel.id = ?3  "
                + "AND A.unidadAcademica.id = ?4 "
                + "ORDER BY A.promedio DESC ";
        return executeQuery(jpql, periodoActivo.getId(), periodoActivo.getPeriodoAnterior().getId(), nivel, unidadId);

    }

    /**
     * Devuelve los alumnos para validar su inscripción en el periodo actual
     * Esta Query está ligada a 'getSolicitudesValidacionInscripcion' en
     * solicitudBecaJpaDao Si se desea modificar alguna de las 2, deben de
     * modificarse ambas.
     *
     * @Edit Augusto H.
     * @param periodoActualId
     * @param periodoAnteriorId
     * @return lista Alumno
     */
    @Override
    public List<Alumno> validacionInscripcion(BigDecimal periodoId, BigDecimal periodoAnteriorId) {
        String consulta = "select distinct(a.id), a.nombre, a.APELLIDOPATERNO, a.APELLIDOMATERNO, a.curp, a.ESTADOCIVIL_ID, "
                + "a.BOLETA, a.FECHADENACIMIENTO, a.GENERO_ID, a.ENTIDADDENACIMIENTO_ID, a.DIRECCION_ID, "
                + "a.CELULAR, a.COMPANIACELULAR_ID, a.CORREOELECTRONICO, a.BENEFICIARIOOPORTUNIDADES, "
                + "a.FECHAALTA, a.FECHAMODIFICACION, a.USUARIO_ID, a.ESTATUS, a.PREBOLETA, a.CORREOELECTRONICOALTERNO, "
                + "a.TELEFONOCASA, a.DATOSBANCARIOS, a.PERMITEINGRESOCUENTAEXTERNA, a.PREREGISTRO "
                + "from ent_alumno a "
                + "join ent_otorgamientos o on o.ALUMNO_ID = a.id "
                + "join ent_tipo_beca_periodo tbpAn on tbpAn.id = o.TIPOBECAPERIODO_ID "
                + "join ent_solicitud_becas sb on sb.id = o.SOLICITUDBECA_ID "
                + "left join ENT_ALUMNO_DATOS_ACADEMICOS da on da.ALUMNO_ID = a.id and da.periodo_id = ?1 "
                + "where o.alta=1 and o.periodo_id = ?2 and o.proceso_id is not null "
                + "and da.id is null "
                + "and tbpAn.TIPOBECA_ID IN "
                + "(SELECT tbp.TIPOBECA_ID FROM ent_tipo_beca_periodo tbp "
                + "WHERE tbp.periodo_id = ?1 and tbp.validaciondeinscripcion > 0) ";
        Query q = entityManager.createNativeQuery(consulta, Alumno.class);
        q.setParameter(1, periodoId);
        q.setParameter(2, periodoAnteriorId);
        List<Alumno> lista = q.getResultList();
        return (lista);
    }

    @Override
    public Long countValidacionInscripcion(BigDecimal periodoId, BigDecimal periodoAnteriorId) {
        String sql = "SELECT COUNT(distinct(a.id)) "
                + "from ent_alumno a "
                + "join ent_otorgamientos o on o.ALUMNO_ID = a.id "
                + "join ent_tipo_beca_periodo tbpAn on tbpAn.id = o.TIPOBECAPERIODO_ID  "
                + "join ent_solicitud_becas sb on sb.id = o.SOLICITUDBECA_ID "
                + "left join ENT_ALUMNO_DATOS_ACADEMICOS da on da.ALUMNO_ID = a.id and da.periodo_id = ?1 "
                + "where o.alta=1 and o.periodo_id = ?2 and o.proceso_id is not null "
                + "and da.id is null "
                + "and tbpAn.TIPOBECA_ID IN "
                + "(SELECT tbp.TIPOBECA_ID FROM ent_tipo_beca_periodo tbp "
                + "WHERE tbp.periodo_id = ?1 and tbp.validaciondeinscripcion > 0) ";
        Long countNativeQuery = getCountNativeQuery(sql, periodoId, periodoAnteriorId);
        return countNativeQuery;
    }

    /**
     * Devuelve true en caso que el alumno ya sea egresado
     *
     * @author Tania G. Sánchez
     * @param alumnoId
     * @return true o false
     */
    @Override
    public boolean esEgresado(BigDecimal alumnoId) {
        String consulta = "select distinct(a.id) from ent_alumno a"
                + " inner join ent_otorgamientos o on a.id = o.alumno_id"
                + " inner join ENT_ALUMNO_DATOS_ACADEMICOS da on da.id = o.DATOSACADEMICOS_ID"
                + " inner join ent_carrera c on da.carrera_id = c.id"
                + " inner join ent_proceso pr on pr.id = o.proceso_id"
                + " inner join cat_unidad_academica ua on ua.id = pr.unidadacademica_id"
                + " where (da.egresado = 1 or"
                + " (select count (distinct (o2.periodo_id))"
                + " from ent_otorgamientos o2"
                + " inner join ent_proceso pr2 on pr2.id = o2.proceso_id"
                + " inner join cat_unidad_academica ua2 on ua2.id = pr2.unidadacademica_id"
                + " where o2.proceso_id is not null"
                + " and ua2.nivel_id = ua.nivel_id"
                + " and o2.id not in (select v.otorgamiento_id from vw_ultima_baja_detalle v"
                + " where v.periodo_id = o2.periodo_id and v.movimiento_id is not null)"
                + " and o2.alumno_id = o.alumno_id) >= c.numerosemestres)"
                + " and a.id = ?1";
        List<Object[]> lista = executeNativeQuery(consulta, alumnoId);
        return lista != null && !lista.isEmpty();
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

    protected boolean isFuncionario() {
        Collection<GrantedAuthority> gas = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (GrantedAuthority ga : gas) {
            if (ga.getAuthority().equals("ROLE_FUNCIONARIO_UA")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Alumno findByPreboleta(String preboleta) {
        String jpql = "SELECT a FROM Alumno a WHERE a.preboleta=?1";
        List<Alumno> lista = executeQuery(jpql, preboleta);
        return lista == null || lista.isEmpty() ? null : lista.get(0);
    }

    @Override
    public List<Object[]> alumnosAutoevaluacion(String periodoId) {
        String consulta = "select nivel, nvl(turno,'M') turno, escuela, boleta, curp, nombre, apellidopaterno, apellidomaterno,"
                + " genero, beca, programa, modalidad, carrera, SUM(monto)"
                + " from (select n.clave  Nivel, da.turno turno, ua.nombrecorto Escuela, a.boleta, a.curp,"
                + " a.nombre, a.apellidopaterno, a.apellidomaterno, g.nombre genero, tb.nombre Beca,"
                + " decode (tb.id, 22, 'Bécalos AR', 23, 'Bécalos AR', pb.nombre) Programa,"
                + " decode(da.modalidad_id, 4, 'No Escolarizada', 'Escolarizada') Modalidad, ca.carrera, d.monto, d.fechadeposito"
                + " from ent_otorgamientos o"
                + " join ent_alumno a on a.id = o.alumno_id"
                + " inner join ent_alumno_datos_academicos da on da.alumno_id = a.id and da.periodo_id = o.periodo_id"
                + " join ent_tipo_beca_periodo tbp on o.tipobecaperiodo_id = tbp.id and o.periodo_id = tbp.periodo_id"
                + " join cat_tipo_beca tb on tbp.tipobeca_id = tb.id"
                + " join ent_proceso p on o.proceso_id = p.id"
                + " join cat_tipo_proceso tp on p.tipoproceso_id=tp.id"
                + " join cat_unidad_academica ua on ua.id = p.unidadacademica_id"
                + " join ent_carrera ca on da.carrera_id = ca.id join cat_programa_beca pb on tb.beca_id = pb.id"
                + " join cat_genero g on a.genero_id = g.id"
                + " join cat_nivel n on ua.nivel_id = n.id"
                + " left join ent_deposito d on d.otorgamiento_id = o.id"
                + " and to_char(d.fechadeposito, 'dd/mm/yyyy') between '01/01/2017' and '01/04/2017' and d.estatusdeposito_id in (2)"
                + " where o.periodo_id = ?1 and o.alta = 1 and o.proceso_id is not null)"
                + " group by nivel ,turno, escuela, boleta, curp, nombre, apellidopaterno, apellidomaterno, genero, beca, programa, modalidad, carrera"
                + " order by 1,2,3";
        List<Object[]> lista = executeNativeQuery(consulta, periodoId);
        return (lista);
    }

    @Override
    public List<Object[]> reporteFundacion(BigDecimal periodoId, String beca, BigDecimal origenRecursosId) {
        String consulta = "select a.curp, a.boleta, a.apellidopaterno, a.apellidomaterno, a.nombre, substr(a.curp,9,2), substr(a.curp,7,2)"
                + " , decode(substr(a.curp,5,1),'0','20','19')||substr(a.curp,5,2), G.nombre, ec.nombre, e.nombre, e.nombre, m.nombre"
                + " , l.nombre, d.calle, d.numerointerior, d.numeroexterior,  d.codigopostal, a.celular, '', a.correoelectronico"
                + " , '', 'IPN', ua.nombrecorto, c.carrera, n.nombre, tb.nombre, da.semestre, 'SEMESTRE', c.numerosemestres"
                + " , 'SEMESTRE', da.promedio, '', '', '', decode(rua.correspondeipn, 1, 'PAGA IPN', 'PAGA FUNDACION')"
                + " from vw_otorgamientos o"
                + " inner join ent_alumno a on a.id = o.alumno_id"
                + " inner join ent_alumno_datos_academicos da on da.alumno_id = a.id and da.periodo_id = o.periodo_id"
                + " inner join ent_proceso p on p.id = o.proceso_id"
                + " inner join cat_unidad_academica ua on ua.id = p.unidadacademica_id"
                + " inner join cat_nivel n on n.id = ua.nivel_id"
                + " inner join ent_tipo_beca_periodo tbp on tbp.id = o.tipobecaperiodo_id"
                + " inner join cat_tipo_beca tb on tb.id = tbp.tipobeca_id"
                + " inner join cat_genero G on G.id = a.genero_id"
                + " inner join cat_estado_civil ec on ec.id = a.estadocivil_id"
                + " inner join ent_carrera c on c.id = da.carrera_id"
                + " left join ent_direccion d on d.id = a.direccion_id"
                + " left join rmm_estado_deleg_col r on r.id = d.relaciongeografica_id"
                + " left join cat_estado e on e.id = r.estado_id"
                + " left join cat_localidad_colonia l on l.id = r.colonia_id"
                + " left join cat_delegacion_municipio m on m.id = r.municipio_id"
                + " inner join rmm_deposito_unidad_academica rua on rua.tipobeca_id = tb.id and rua.unidadacademica_id = ua.id"
                + " where o.periodo_id = ?1 and tb.beca_id in (" + beca + ") and o.proceso_id is not null"
                + (origenRecursosId.equals(new BigDecimal("-1")) ? "" : " and rua.correspondeipn = ?2")
                + " order by n.nombre, ua.nombrecorto, tb.nombre, c.carrera, rua.correspondeipn";
        List<Object[]> lista = executeNativeQuery(consulta, periodoId, origenRecursosId);
        return (lista);
    }

    @Override
    public PaginateUtil alumnosConfiguracionCuenta(ServerSideUtil ssu) {
        StringBuilder sbQuery = new StringBuilder("select a from Alumno a where 1 = 1 ");
        Object[] params = addParameters(sbQuery, Boolean.FALSE, ssu.parametros);
        Long noTotal = getCountQuery(buildCountQuery(sbQuery.toString(), Boolean.FALSE), params);
        solicitudAlumnosOrderBy(sbQuery, ssu.getSortCol(), ssu.getSortDir());
        List<Alumno> lista = executeQueryPaginate(sbQuery.toString(), ssu.getStart(), ssu.getLength(), params);
        return new PaginateUtil(lista, noTotal, noTotal);
    }

    @Override
    public List<Alumno> nuevosOtorgamientosUABeca(BigDecimal uaId, Integer semMin, Integer semMax, Float promMin, Float promMax, BigDecimal modalidad, Integer nivel) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Alumno> buscarAlumnos(BigDecimal periodoId, Integer mes, Integer origenRecursos, BigDecimal programaBeca, BigDecimal nivelAcademico, BigDecimal uAcademica, BigDecimal tipoProceso, BigDecimal idOtorgamiento, String fechaDeposito) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Alumno> busquedaAlumnosTarjeta(Map<String, Object> parametros) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
