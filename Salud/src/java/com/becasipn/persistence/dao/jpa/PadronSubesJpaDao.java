package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.PadronSubesDao;
import com.becasipn.persistence.model.ConvocatoriaSubes;
import com.becasipn.persistence.model.PadronSubes;
import com.becasipn.persistence.model.Periodo;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;

public class PadronSubesJpaDao extends JpaDaoBase<PadronSubes, BigDecimal> implements PadronSubesDao {

    public PadronSubesJpaDao() {
        super(PadronSubes.class);
    }

    @Override
    public PadronSubes getByCurp(String curp) {
        String jpql = "SELECT p FROM PadronSubes p where p.curp=?1";
        return executeSingleQuery(jpql, curp);
    }

    @Override
    public void updateTransporte(String curp, String folioTransporte, String estatusTransporte) {
        String jpql = "UPDATE PadronSubes p SET p.foliotransporte=?1,p.estatustransporte=?2 WHERE p.curp = ?3";
        executeUpdate(jpql, folioTransporte, estatusTransporte, curp);
    }

    @Override
    public void updateEstadoDomicilio() {
        String sql = "UPDATE ENT_PADRON_SUBES p SET p.estado='ESTADO DE MEXICO' WHERE p.estado='MEXICO'";
        executeNativeUpdate(sql);
    }

    @Override
    public void excluirOtorgamiento(ConvocatoriaSubes convocatoriaSubes, Periodo periodo, Integer op) {
        String sql;
        switch (op) {
            case 1:
                sql = "UPDATE ENT_PADRON_SUBES set CONOTORGAMIENTO = 1 "
                        + "WHERE ALUMNO_ID in (SELECT alumno_id FROM ent_OTORGAMIENTOS WHERE periodo_id=?2 AND IDENTIFICADOROTORGAMIENTO_ID != 17) "
                        + "AND convocatoria_id=?1 AND periodo_id=?2 "
                        + "AND CONOTORGAMIENTO is null and FINALIZADO is null";
                executeNativeUpdate(sql, convocatoriaSubes.getId(), periodo.getId());//Marcar con otorgamiento por curp
                break;
            case 2:
                sql = "UPDATE ENT_PADRON_SUBES R SET CONOTORGAMIENTO=1,TIPOCRUCE=1,"
                        + "   ALUMNO_ID=(SELECT A.ID FROM ("
                        + "              SELECT CURP, MAX(FECHAMODIFICACION) FECHAMODIFICACION FROM ENT_ALUMNO GROUP BY CURP) T"
                        + "              JOIN ENT_ALUMNO A ON A.CURP = T.CURP AND A.FECHAMODIFICACION= T.FECHAMODIFICACION"
                        + "		 join ENT_ALUMNO_DATOS_ACADEMICOS da on a.ID = da.ALUMNO_ID and da.PERIODO_ID = (select id from cat_periodo where estatus=1 and rownum = 1) "
                        + "              JOIN CAT_UNIDAD_ACADEMICA UA ON da.UNIDADACADEMICA_ID=UA.ID"
                        + "              WHERE A.CURP = R.CURP AND UA.NIVEL_ID=2"
                        + "   )"
                        + "   WHERE EXISTS (SELECT 1 FROM ENT_OTORGAMIENTOS O"
                        + "       JOIN ENT_ALUMNO A ON O.ALUMNO_ID=A.ID WHERE A.CURP=R.CURP"
                        + "       AND O.periodo_id=?2 AND o.alta=1 and o.proceso_id is not null and nvl(o.IDENTIFICADOROTORGAMIENTO_ID,0)!=13)"
                        + " AND r.convocatoria_id=?1 and r.periodo_id = ?2 AND r.conotorgamiento is null";
                executeNativeUpdate(sql, convocatoriaSubes.getId(), periodo.getId());//Marcar con otorgamiento por curp
                break;
            case 3:
                sql = "UPDATE ENT_PADRON_SUBES R SET CONOTORGAMIENTO=1,TIPOCRUCE=2,"
                        + "     ALUMNO_ID=(SELECT A.ID FROM ENT_ALUMNO A "
                        + "		 join ENT_ALUMNO_DATOS_ACADEMICOS da on a.ID = da.ALUMNO_ID and da.PERIODO_ID = (select id from cat_periodo where estatus=1 and rownum = 1) "
                        + "              JOIN CAT_UNIDAD_ACADEMICA UA ON da.UNIDADACADEMICA_ID=UA.ID"
                        + "              WHERE A.BOLETA = R.MATRICULA AND UA.NIVEL_ID=2) "
                        + "   WHERE EXISTS (SELECT 1 FROM ENT_OTORGAMIENTOS O"
                        + "       JOIN ENT_ALUMNO A ON O.ALUMNO_ID=A.ID WHERE A.BOLETA=R.MATRICULA"
                        + "       AND O.periodo_id=?2 AND o.alta=1 and o.proceso_id is not null and nvl(o.IDENTIFICADOROTORGAMIENTO_ID,0)!=13)"
                        + "     AND r.convocatoria_id=?1 and r.periodo_id = ?2 AND r.conotorgamiento is null";
                executeNativeUpdate(sql, convocatoriaSubes.getId(), periodo.getId());//Marcar con otorgameinto por boleta
                break;
            default:
                break;
        }
    }

    @Override
    public Boolean actualizarCurpAlumnoB(ConvocatoriaSubes convocatoriaSubes, Periodo periodo) {
        String sql = "select a.* from ent_alumno a "
                + "where a.id in (SELECT r.alumno_id FROM ent_padron_subes r "
                + "	WHERE a.id=r.alumno_id "
                + "	and r.convocatoria_id=?1 "
                + "	and r.periodo_id = ?2 ) "
                + "and exists (select 1 from ent_padron_subes r1 "
                + "	where r1.alumno_id=a.id "
                + "	and nvl(r1.tipocruce,0)=2 ) ";
        List<Object[]> lista = executeNativeQuery(sql, convocatoriaSubes.getId(), periodo.getId());
        return lista != null && !lista.isEmpty();
    }

    @Override
    public void actualizarCurpAlumno(ConvocatoriaSubes convocatoriaSubes, Periodo periodo) {
        String sql = "UPDATE ent_alumno a SET a.curp=(SELECT r.curp FROM ent_padron_subes r "
                + "        WHERE a.id=r.alumno_id "
                + "            and r.convocatoria_id=?1"
                + "            and r.periodo_id = ?2 )"
                + "    where exists (select 1 from ent_padron_subes r1 "
                + "        where r1.alumno_id=a.id "
                + "            and nvl(r1.tipocruce,0)=2 )";
        executeNativeUpdate(sql, convocatoriaSubes.getId(), periodo.getId());//Actualizar curp
    }

    @Override
    public void marcarAlumnos(ConvocatoriaSubes convocatoriaSubes, Periodo periodo, Integer op) {
        String sql;
        switch (op) {
            case 1:
                sql = "UPDATE ENT_PADRON_SUBES r SET tipocruce=1,dae=0,"
                        + "    ALUMNO_ID=(SELECT A.ID FROM "
                        + "              (SELECT CURP, MAX(FECHAMODIFICACION) FECHAMODIFICACION FROM ENT_ALUMNO GROUP BY CURP) T"
                        + "              JOIN ENT_ALUMNO A ON A.CURP = T.CURP AND A.FECHAMODIFICACION= T.FECHAMODIFICACION"
                        + "		 join ENT_ALUMNO_DATOS_ACADEMICOS da on a.ID = da.ALUMNO_ID and da.PERIODO_ID = (select id from cat_periodo where estatus=1 and rownum = 1) "
                        + "              JOIN CAT_UNIDAD_ACADEMICA UA ON da.UNIDADACADEMICA_ID=UA.ID"
                        + "              WHERE A.CURP = R.CURP "
                        + "                AND UA.NIVEL_ID=2 "
                        + "                and a.estatus=1)"
                        + "   where r.conotorgamiento is null "
                        + "     and EXISTS (SELECT 1 FROM ENT_ALUMNO a1 "
                        + "		 join ENT_ALUMNO_DATOS_ACADEMICOS da1 on a1.ID = da1.ALUMNO_ID and da1.PERIODO_ID = (select id from cat_periodo where estatus=1 and rownum = 1) "
                        + "              JOIN CAT_UNIDAD_ACADEMICA UA1 ON da1.UNIDADACADEMICA_ID=UA1.ID"
                        + "                WHERE A1.CURP=R.CURP and a1.estatus=1 and ua1.nivel_id=2 )"
                        + "     and r.alumno_id is null  "
                        + "     and r.convocatoria_id=?1"
                        + "     and r.periodo_id=?2  ";
                executeNativeUpdate(sql, convocatoriaSubes.getId(), periodo.getId());//Marcar alumnos estatus 1 por curp
                break;
            case 2:
                sql = "UPDATE ENT_PADRON_SUBES r SET tipocruce=2,dae=0,"
                        + "    ALUMNO_ID=(SELECT A.ID FROM ENT_ALUMNO A"
                        + "		 join ENT_ALUMNO_DATOS_ACADEMICOS da on a.ID = da.ALUMNO_ID and da.PERIODO_ID = (select id from cat_periodo where estatus=1 and rownum = 1) "
                        + "              JOIN CAT_UNIDAD_ACADEMICA UA ON da.UNIDADACADEMICA_ID=UA.ID"
                        + "              WHERE A.BOLETA = R.MATRICULA "
                        + "                   AND replace(A.NOMBRE,' ','') = replace(R.NOMBRE,' ','')"
                        + "                   AND replace(A.APELLIDOPATERNO ,' ','') = replace(R.APELLIDOPATERNO ,' ','')"
                        + "                   AND replace(A.APELLIDOMATERNO,' ','')=replace(R.APELLIDOMATERNO,' ','')"
                        + "                AND UA.NIVEL_ID=2 "
                        + "                and a.estatus=1)"
                        + "   where r.conotorgamiento is null"
                        + "     and EXISTS (SELECT 1 FROM ENT_ALUMNO a1 "
                        + "		 join ENT_ALUMNO_DATOS_ACADEMICOS da1 on a1.ID = da1.ALUMNO_ID and da1.PERIODO_ID = (select id from cat_periodo where estatus=1 and rownum = 1) "
                        + "              JOIN CAT_UNIDAD_ACADEMICA UA1 ON da1.UNIDADACADEMICA_ID=UA1.ID"
                        + "                WHERE A1.boleta=R.MATRICULA AND replace(A1.NOMBRE,' ','') = replace(R.NOMBRE,' ','')"
                        + "                      AND replace(A1.APELLIDOPATERNO ,' ','') = replace(R.APELLIDOPATERNO ,' ','')"
                        + "                      AND replace(A1.APELLIDOMATERNO,' ','')=replace(R.APELLIDOMATERNO,' ','')"
                        + "                and a1.estatus=1 and ua1.nivel_id=2 )"
                        + "     and r.alumno_id is null  "
                        + "     and r.convocatoria_id=?1"
                        + "     and r.periodo_id=?2  ";
                executeNativeUpdate(sql, convocatoriaSubes.getId(), periodo.getId());//Marcar alumnos estatus 1 por boleta
                break;
            case 3:
                sql = "UPDATE ENT_PADRON_SUBES r SET tipocruce=1,dae=1,"
                        + "    ALUMNO_ID=(SELECT A.ID FROM "
                        + "              (SELECT CURP, MAX(FECHAMODIFICACION) FECHAMODIFICACION FROM ENT_ALUMNO GROUP BY CURP) T"
                        + "              JOIN ENT_ALUMNO A ON A.CURP = T.CURP AND A.FECHAMODIFICACION= T.FECHAMODIFICACION"
                        + "              WHERE A.CURP = R.CURP "
                        + "                and a.estatus=0)"
                        + "   where r.conotorgamiento is null"
                        + "     and dae is null"
                        + "     and EXISTS (SELECT 1 FROM ENT_ALUMNO a1 "
                        + "                WHERE A1.CURP=R.CURP and a1.estatus=0)"
                        + "     and EXISTS(SELECT 1 FROM VW_DAE v  WHERE V.CURP=R.CURP  and v.ESCUELA > 19 )"
                        + "     and r.alumno_id is null  "
                        + "     and r.convocatoria_id=?1"
                        + "     and r.periodo_id=?2";
                executeNativeUpdate(sql, convocatoriaSubes.getId(), periodo.getId());//Marcar alumno estatus 0 por curp
                break;
            case 4:
                sql = "UPDATE ENT_PADRON_SUBES r SET tipocruce=2,dae=1,"
                        + "    ALUMNO_ID=(SELECT A.ID FROM ENT_ALUMNO A"
                        + "              WHERE A.BOLETA = R.MATRICULA "
                        + "                   AND replace(A.NOMBRE,' ','') = replace(R.NOMBRE,' ','')"
                        + "                   AND replace(A.APELLIDOPATERNO ,' ','') = replace(R.APELLIDOPATERNO ,' ','')"
                        + "                   AND replace(A.APELLIDOMATERNO,' ','')=replace(R.APELLIDOMATERNO,' ','')"
                        + "                and a.estatus=0)"
                        + "   where r.conotorgamiento is null"
                        + "     and dae is null"
                        + "     and EXISTS (SELECT 1 FROM ENT_ALUMNO a1 "
                        + "                WHERE A1.boleta=R.MATRICULA "
                        + "                   AND replace(A1.NOMBRE,' ','') = replace(R.NOMBRE,' ','')"
                        + "                   AND replace(A1.APELLIDOPATERNO ,' ','') = replace(R.APELLIDOPATERNO ,' ','')"
                        + "                   AND replace(A1.APELLIDOMATERNO,' ','')=replace(R.APELLIDOMATERNO,' ','')"
                        + "                    and a1.estatus=0)"
                        + "     and EXISTS(SELECT 1 FROM VW_DAE v  WHERE V.boleta=R.Matricula and v.ESCUELA > 19  )"
                        + "     and r.alumno_id is null  "
                        + "     and r.convocatoria_id=?1"
                        + "     and r.periodo_id=?2";
                executeNativeUpdate(sql, convocatoriaSubes.getId(), periodo.getId());//Marcar alumno estatus 0 por boleta
                break;
            case 5:
                sql = "UPDATE ENT_PADRON_SUBES r SET dae=0 "
                        + "where r.conotorgamiento is null "
                        + "and r.dae is null "
                        + "and EXISTS (SELECT 1 FROM ENT_ALUMNO a INNER JOIN ENT_PADRON_SUBES x on a.ID = x.ALUMNO_ID WHERE a.estatus = 1 and x.periodo_id = r.periodo_id and r.alumno_id = x.alumno_id) "
                        + "and r.convocatoria_id=?1 "
                        + "and r.periodo_id=?2 ";
                executeNativeUpdate(sql, convocatoriaSubes.getId(), periodo.getId());//Marcar alumno con alumno ya encontrado en la carga con estatus 1
                break;
            case 6:
                sql = "UPDATE ENT_PADRON_SUBES r SET dae=1 "
                        + "where r.conotorgamiento is null "
                        + "and r.dae is null "
                        + "and EXISTS (SELECT 1  FROM ENT_ALUMNO a INNER JOIN ENT_PADRON_SUBES x on a.ID = x.ALUMNO_ID WHERE a.estatus = 0 and x.periodo_id = r.periodo_id and r.alumno_id = x.alumno_id) "
                        + "and r.convocatoria_id=?1 "
                        + "and r.periodo_id=?2 ";
                executeNativeUpdate(sql, convocatoriaSubes.getId(), periodo.getId());//Marcar alumno con alumno ya encontrado en la carga con estatus 0
                break;
            default:
                break;
        }
    }

    @Override
    public List<PadronSubes> listadoActualizar(ConvocatoriaSubes convocatoriaSubes, Periodo periodo) {
        String consulta = "SELECT p.* FROM ENT_PADRON_SUBES p "
                + "inner join ENT_ALUMNO a on p.ALUMNO_ID = a.id "
                + "LEFT JOIN ENT_ALUMNO_DATOS_ACADEMICOS da on da.ALUMNO_ID = a.ID and da.PERIODO_ID = p.PERIODO_ID "
                + "WHERE p.CONOTORGAMIENTO is null and p.dae=1 and p.CONVOCATORIA_ID = ?1 and p.PERIODO_ID = ?2 "
                + "and da.id is null ";
        Query q = entityManager.createNativeQuery(consulta, PadronSubes.class);
        q.setParameter(1, convocatoriaSubes.getId());
        q.setParameter(2, periodo.getId());
        List<PadronSubes> lista = q.getResultList();
        return (lista);
    }

    @Override
    public List<PadronSubes> listadoInsertar(ConvocatoriaSubes convocatoriaSubes, Periodo periodo) {
        String consulta = "SELECT p.* FROM ENT_PADRON_SUBES p WHERE p.CONOTORGAMIENTO is null and p.dae is null and p.CONVOCATORIA_ID = ?1 and p.PERIODO_ID = ?2 and p.ALUMNO_ID is null ";
        Query q = entityManager.createNativeQuery(consulta, PadronSubes.class);
        q.setParameter(1, convocatoriaSubes.getId());
        q.setParameter(2, periodo.getId());
        List<PadronSubes> lista = q.getResultList();
        return (lista);
    }

    @Override
    public List<PadronSubes> listadoESE(ConvocatoriaSubes convocatoriaSubes, Periodo periodo) {
        // Los alumnos con "conotorgamiento=0 " y "dae 1=null" se utilizarán para trabajar con la bandera ESE
        String jpql = "SELECT p FROM PadronSubes p WHERE p.conotorgamiento is null and p.dae is not null "
                + " and p.convocatoriaSubes.id = ?1 and p.periodo.id = ?2 and p.ese is null and p.alumno is not null";
        return executeQuery(jpql, convocatoriaSubes.getId(), periodo.getId());
    }

    @Override
    public List<PadronSubes> listadoConOtorgamiento(ConvocatoriaSubes convocatoriaSubes, Periodo periodo) {
        String consulta = "SELECT p.* FROM ENT_PADRON_SUBES p "
                + "left join ENT_SOLICITUD_BECAS b on b.ALUMNO_ID = p.ALUMNO_ID and p.PERIODO_ID = b.PERIODO_ID and b.CUESTIONARIO_ID = 5 "
                + "WHERE p.conotorgamiento = 1 and p.CONVOCATORIA_ID = ?1 and p.periodo_id = ?2 and p.FINALIZADO is null and b.id is null ";
        Query q = entityManager.createNativeQuery(consulta, PadronSubes.class);
        q.setParameter(1, convocatoriaSubes.getId());
        q.setParameter(2, periodo.getId());
        List<PadronSubes> lista = q.getResultList();
        return (lista);
    }

    @Override
    public Boolean existe(ConvocatoriaSubes convocatoriaSubes, Periodo periodo) {
        String jpql = "SELECT count(p) FROM PadronSubes p WHERE "
                + " p.convocatoriaSubes.id = ?1 and p.periodo.id = ?2 AND p.dae is not null ";
        Long countQuery = getCountQuery(jpql, convocatoriaSubes.getId(), periodo.getId());
        return countQuery > 0;
    }

    @Override
    public PadronSubes getAlumnoSUBES(BigDecimal alumnoId) {
        String consulta = "select p from PadronSubes p where p.alumno.id = ?1 order by p.periodo.id";
        List<PadronSubes> lista = executeQuery(consulta, alumnoId);
        return lista == null || lista.isEmpty() ? null : lista.get(0);
    }

    @Override
    public Boolean existeAlumnoCurpPeriodo(String curp, Periodo periodo) {
        String jpql = "SELECT count(p) FROM PadronSubes p WHERE "
                + " p.curp = ?1 and p.periodo.id = ?2";
        Long countQuery = getCountQuery(jpql, curp, periodo.getId());
        return countQuery > 0;
    }

    @Override
    public Boolean existeAlumnoFolioPeriodo(String folioSubes, Periodo periodo) {
        String jpql = "SELECT count(p) FROM PadronSubes p WHERE "
                + " p.foliosubes = ?1 and p.periodo.id = ?2";
        Long countQuery = getCountQuery(jpql, folioSubes, periodo.getId());
        return countQuery > 0;
    }

    @Override
    public PadronSubes alumnoCurpPeriodo(String curp, Periodo periodo) {
        String jpql = "SELECT p FROM PadronSubes p WHERE "
                + " p.curp = ?1 and p.periodo.id = ?2";
        return executeSingleQuery(jpql, curp, periodo.getId());
    }

    @Override
    public Boolean existeAlumnoFolioPeriodoSolicitud(String curp, Periodo periodo, BigDecimal cuestionarioId) {
        String sql = "SELECT count(*) from ENT_PADRON_SUBES s where s.ALUMNO_ID in (SELECT alumno_id FROM ENT_SOLICITUD_BECAS WHERE periodo_id=s.PERIODO_ID AND CUESTIONARIO_ID= ?3) "
                + " AND s.CURP = ?1 and s.PERIODO_ID = ?2";
        return getCountNativeQuery(sql, curp, periodo.getId(), cuestionarioId).intValue() > 0;
    }

    @Override
    public List<PadronSubes> cargaDeTabla(Integer tipo) {
        String consulta;
        if (tipo == 1) {
            consulta = "select FOLIO_SOLICITUD as id, "
                    + "m.FOLIO_SOLICITUD as foliosubes, "
                    + "m.CURP as curp, "
                    + "m.NOMBRE as nombre, "
                    + "m.APELLIDO_PATERNO as apellidopaterno, "
                    + "m.APELLIDO_MATERNO as apellidomaterno, "
                    + "m.EMAIL as correoelectronico, "
                    + "m.TELEFONO_CELULAR as celular, "
                    + "m.ESTADO_CIVIL as estadocivil, "
                    + "m.ENTIDAD_NACIMIENTO as entidadnacimiento, "
                    + "m.DOMICILIO_ESTADO as estado, "
                    + "m.DOMICILIO_MUNICIPIO as municipio, "
                    + "m.DOMICILIO_LOCALIDAD as localidad, "
                    + "m.DOMICILIO_COLONIA as colonia, "
                    + "m.DOMICILIO_CODIGO_POSTAL as codigopostal, "
                    + "m.DOMICILIO_CALLE as calle, "
                    + "m.NUM_EXT as numext, "
                    + "m.NUM_INT as numint, "
                    + "m.FICHA_ESCOLAR_PLANTEL as plantel, "
                    + "m.FICHA_ESCOLAR_CARRERA as carrera, "
                    + "m.FICHA_ESCOLAR_PROMEDIO_GRAL as promedio, "
                    + "m.FICHA_ESCOLAR_PERIODO_ACTUAL as semestre, "
                    + "m.FICHA_ESCOLAR_MATRICULA as matricula, "
                    + "m.FICHA_ESCOLAR_REGULAR as regularidad, "
                    + "TRIM(m.VULNERABILIDAD) as vulnerabilidad, "
                    + "TRIM(m.ESTATUS_SOLICITUD) as estatussubes, "
                    + "TRIM(m.VALIDADO_IPES) as validadoipes, "
                    + "m.INTEGRANTES_HOGAR as integranteshogar, "
                    + "m.INGRESO_TOTAL_MENSUAL as ingresototal, "
                    + "m.DISCAPACIDAD as discapacidad, "
                    + "m.TIENE_PROSPERA as tieneprospera "
                    + "from PADRON_MANUTENCION m";
        } else {
            consulta = "select FOLIO_SOLICITUD as id, "
                    + "t.FOLIO_SOLICITUD as foliotransporte, "
                    + "t.CURP as curp, "
                    + "TRIM(t.VULNERABILIDAD) as vulnerabilidadtransporte, "
                    + "TRIM(t.ESTATUS_SOLICITUD) as estatustransporte, "
                    + "TRIM(t.VALIDADO_IPES) as validadoipestransporte "
                    + "from PADRON_TRANSPORTE t";
        }
//		consulta += " where CURP = 'VAAO960701MDFLLF08' ";
        Query q = entityManager.createNativeQuery(consulta, PadronSubes.class);
        List<PadronSubes> lista = q.getResultList();
        return lista == null || lista.isEmpty() ? null : lista;
    }

    @Override
    public Integer getCargados(BigDecimal periodoId) {
        String consulta = "select count(*) from ENT_PADRON_SUBES where PERIODO_ID = ?1";
        return getCountNativeQuery(consulta, periodoId).intValue();
    }

    @Override
    public Integer getSolicitudesCreadas(BigDecimal periodoId, BigDecimal cuestionarioId) {
        String consulta = "select count(*) from ENT_SOLICITUD_BECAS where CUESTIONARIO_ID = ?1 and PERIODO_ID = ?2";
        return getCountNativeQuery(consulta, cuestionarioId, periodoId).intValue();
    }

    @Override
    public List<Object[]> resumenProceso(BigDecimal periodoId, BigDecimal convocatoriaId) {
        StringBuilder sb = new StringBuilder();
        List<String> columnas = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        List<String> criteria = new ArrayList<>();

        columnas.add("ps.APELLIDOPATERNO");
        columnas.add("ps.APELLIDOMATERNO");
        columnas.add("ps.NOMBRE");
        columnas.add("ps.MATRICULA");
        columnas.add("ps.CURP");
        columnas.add("ps.PLANTEL");
        columnas.add("ps.CARRERA");
        columnas.add("ps.foliosubes");
        columnas.add("ps.ESTATUSSUBES");
        columnas.add("ps.VULNERABILIDAD");
        columnas.add("ps.VALIDADOIPES");
        columnas.add("nvl(ctb.nombre,'-')");
        columnas.add("nvl(mrs.nombre,'-')");
        columnas.add("ps.FOLIOTRANSPORTE");
        columnas.add("ps.ESTATUSTRANSPORTE");
        columnas.add("ps.VULNERABILIDADTRANSPORTE");
        columnas.add("ps.VALIDADOIPESTRANSPORTE");
        columnas.add("nvl(ctbT.nombre,'-')");
        columnas.add("nvl(mrsT.nombre,'-')");

        sb.append(" SELECT ");

        agregaColumnas(sb, columnas);

        sb.append(" from ENT_PADRON_SUBES ps");
        sb.append(" left join ENT_SOLICITUD_BECAS sb");
        sb.append(" on ps.ALUMNO_ID = sb.ALUMNO_ID and ps.PERIODO_ID = sb.PERIODO_ID and sb.CUESTIONARIO_ID = 5");
        sb.append(" left join VW_OTORGAMIENTOS o");
        sb.append(" on sb.ALUMNO_ID = o.ALUMNO_ID and o.PERIODO_ID = ps.PERIODO_ID and o.SOLICITUDBECA_ID = sb.id");
        sb.append(" left join ENT_TIPO_BECA_PERIODO tbp");
        sb.append(" on o.TIPOBECAPERIODO_ID = tbp.ID and tbp.PERIODO_ID = ps.PERIODO_ID");
        sb.append(" left join CAT_TIPO_BECA ctb");
        sb.append(" on tbp.TIPOBECA_ID = ctb.ID");
        sb.append(" left join ENT_TIPO_BECA_PERIODO tbps");
        sb.append(" on sb.TIPOBECAPREASIGNADA_ID= tbps.ID and tbps.PERIODO_ID = sb.PERIODO_ID");
        sb.append(" left join CAT_MOTIVO_RECHAZO_SOLICITUD mrs");
        sb.append(" on sb.MOTIVORECHAZO_ID = mrs.id");
        sb.append(" left join ENT_SOLICITUD_BECAS sbT");
        sb.append(" on ps.ALUMNO_ID = sbT.ALUMNO_ID and ps.PERIODO_ID = sbT.PERIODO_ID and sbT.CUESTIONARIO_ID = 4");
        sb.append(" left join VW_OTORGAMIENTOS oT");
        sb.append(" on sbT.ALUMNO_ID = oT.ALUMNO_ID and oT.PERIODO_ID = ps.PERIODO_ID and oT.SOLICITUDBECA_ID = sbT.id");
        sb.append(" left join ENT_TIPO_BECA_PERIODO tbpT");
        sb.append(" on oT.TIPOBECAPERIODO_ID = tbpT.ID and tbpT.PERIODO_ID = ps.PERIODO_ID");
        sb.append(" left join CAT_TIPO_BECA ctbT");
        sb.append(" on tbpT.TIPOBECA_ID = ctbT.ID");
        sb.append(" left join ENT_TIPO_BECA_PERIODO tbpsT");
        sb.append(" on sbT.TIPOBECAPREASIGNADA_ID= tbpsT.ID and tbpsT.PERIODO_ID = sbT.PERIODO_ID");
        sb.append(" left join CAT_MOTIVO_RECHAZO_SOLICITUD mrsT");
        sb.append(" on sbT.MOTIVORECHAZO_ID = mrsT.id");

        criteria.add("ps.PERIODO_ID = #periodo_id");
        params.put("periodo_id", periodoId);
        criteria.add("ps.CONVOCATORIA_ID = #convocatoria_id");
        params.put("convocatoria_id", convocatoriaId);

        agregaCriterios(sb, criteria);

        // Ordena por nivel, unidad académica, y fecha de solicitud
        sb.append(" order by ps.PLANTEL, ps.CARRERA, ps.APELLIDOPATERNO, ps.APELLIDOMATERNO, ps.NOMBRE");

        List<Object[]> result = executeNativeQuery(sb.toString(), params, null);

        return result;

    }
}
