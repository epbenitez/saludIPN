/**
 * SISTEMA DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE SERVICIOS
 * ESTUDIANTILES 2016
 *
 */
package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.OtorgamientoExternoDao;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.CicloEscolar;
import com.becasipn.persistence.model.OtorgamientoExterno;
import com.becasipn.persistence.model.Nivel;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.UnidadAcademica;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Patricia Benítez
 */
public class OtorgamientoExternoJpaDao extends JpaDaoBase<OtorgamientoExterno, BigDecimal>
        implements OtorgamientoExternoDao {

    public OtorgamientoExternoJpaDao() {
        super(OtorgamientoExterno.class);
    }

    /**
     * Método que verifica si un alumno tiene una beca externa para el cilo
     * escolar indicado.
     *
     * @param alumno
     * @param ciclo
     * @return
     */
    @Override
    public List<OtorgamientoExterno> getAlumno(Alumno alumno, CicloEscolar ciclo) {
        //TODO falta agregar el ciclo
        String jpql = "SELECT o FROM OtorgamientoExterno o WHERE o.curp = ?1 and o.cicloEscolar.id = ?2 ";
        List<OtorgamientoExterno> lista = executeQuery(jpql, alumno.getCurp(), ciclo.getId());
        return lista.isEmpty() ? null : lista;
    }

    public List<OtorgamientoExterno> getOEByName(String nombre) {
        //TODO falta agregar el ciclo
        String jpql = "SELECT o FROM OtorgamientoExterno o WHERE UPPER(o.nombre) LIKE UPPER(?1) ";
        String dato = "%" + nombre + "%";
        List<OtorgamientoExterno> lista = executeQuery(jpql, dato);
        return lista.isEmpty() ? null : lista;
    }

    public List<OtorgamientoExterno> getAllAlumno(Periodo periodo, Nivel nivel, UnidadAcademica unidadAcademica) {
        String jpql = "SELECT o.becaExterna.nombre, o.becaExterna.id, count(o)  FROM OtorgamientoExterno o WHERE 1 = 1 ";
        jpql += " AND o.periodo.id = " + periodo.getId() + " ";
        jpql += (unidadAcademica == null || unidadAcademica.getId().compareTo(BigDecimal.ZERO) == 0) ? "" : " and o.unidadacademica.id = " + unidadAcademica.getId() + " ";
        jpql += (nivel == null || nivel.getId().compareTo(BigDecimal.ZERO) == 0) ? "" : " and o.unidadacademica.nivel.id = " + nivel.getId() + " ";
        jpql += "GROUP BY o.becaExterna.id, o.becaExterna.nombre ORDER BY o.becaExterna.id ASC";
        List<OtorgamientoExterno> lista = executeQuery(jpql);
        return lista.isEmpty() ? null : lista;
    }

    public List<OtorgamientoExterno> getAllUA(Periodo periodo, Nivel nivel, BigDecimal becaId) {
        String jpql = "SELECT o.unidadacademica.nombreCorto, count(o), o.unidadacademica.id   FROM OtorgamientoExterno o WHERE 1 = 1 ";
        jpql += " AND o.periodo.id = " + periodo.getId() + " ";
        jpql += "AND o.becaExterna.id = " + becaId + " ";
        jpql += (nivel == null || nivel.getId().compareTo(BigDecimal.ZERO) == 0) ? "" : " and o.unidadacademica.nivel.id = " + nivel.getId() + " ";
        jpql += "GROUP BY o.unidadacademica.nombreCorto, o.unidadacademica.id ORDER BY o.unidadacademica.id ASC";
        List<OtorgamientoExterno> lista = executeQuery(jpql);
        return lista.isEmpty() ? null : lista;
    }

    public List<Object[]> getUnidadesAcademicas(Periodo periodo, Nivel nivel, BigDecimal becaId) {
        String sql = "SELECT nvl(t0.NOMBRECORTO, 'Sin Unidad Académica'), COUNT(t1.ID), t0.ID \n"
                + "FROM ENT_OTORGAMIENTOS_EXTERNOS t1 \n"
                + "left join  cat_unidad_academica t0 on  t0.ID = t1.UNIDADACADEMICA_ID\n"
                + "WHERE t1.PERIODO_ID = "
                + periodo.getId()
                + " AND  t1.BECAEXTERNA_ID = "
                + becaId
                + ((nivel == null || nivel.getId().compareTo(BigDecimal.ZERO) == 0) ? "" : " and t0.nivel_id = " + nivel.getId())
                + " GROUP BY t0.NOMBRECORTO, t0.ID ORDER BY t0.ID ASC";
        List<Object[]> lista = executeNativeQuery(sql);
        return lista.isEmpty() ? null : lista;
    }

    public List<OtorgamientoExterno> getbyUA(Periodo periodo, UnidadAcademica unidadacademica, BigDecimal becaId) {
        String jpql = "SELECT o FROM OtorgamientoExterno o WHERE 1 = 1";
        jpql += " AND o.periodo.id = " + periodo.getId() + " ";
        jpql += "AND o.becaExterna.id = " + becaId + " ";
        jpql += "AND o.unidadacademica.id = " + unidadacademica.getId() + " ";
        List<OtorgamientoExterno> lista = executeQuery(jpql);
        return lista.isEmpty() ? null : lista;
    }

    // Metodo para sacar reporte de excel
    @Override
    public List<Object[]> reporteEstatus(BigDecimal periodoId, BigDecimal unidadAcademicaId, BigDecimal becaid) {
        StringBuilder sb = new StringBuilder();
        List<String> columnas = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        List<String> criteria = new ArrayList<>();

        columnas.add("o.boleta \"BOLETA\"");
        columnas.add("o.nombre \"NOMBRE\"");
        columnas.add("o.curp \"CURP\"");
        columnas.add("ua.nombrecorto \"UNIDAD ACADÉMICA\"");
        columnas.add("tb.nombre \"BECA SIBEC\"");
        columnas.add("be.nombre \"BECA EXTERNA\"");

        sb.append(" SELECT ");
        agregaColumnas(sb, columnas);

        sb.append(" from ENT_OTORGAMIENTOS_EXTERNOS o ");
        sb.append(" left join vw_otorgamientos s on o.alumno_id = s.alumno_id and s.alta = 1 and o.periodo_id = s.periodo_id");
        sb.append(" left join ent_tipo_beca_periodo tbp on s.tipobecaperiodo_id = tbp.id ");
        sb.append(" left join cat_tipo_beca tb on tbp.tipobeca_id = tb.id ");
        sb.append(" join CAT_BECA_EXTERNA be on be.id = o.becaexterna_id ");
        sb.append(" left join cat_unidad_academica ua on ua.id = o.unidadacademica_id ");

        criteria.add("o.periodo_id = #periodo_id");
        params.put("periodo_id", periodoId);

        if (unidadAcademicaId != null) {
            criteria.add("o.unidadacademica_id = #ua_id");
            params.put("ua_id", unidadAcademicaId);
        }else{
            criteria.add("o.unidadacademica_id IS null");
        }

        criteria.add("o.becaExterna_id = #becaExterna_id");
        params.put("becaExterna_id", becaid);

        agregaCriterios(sb, criteria);
        sb.append(" order by o.nombre");
        List<Object[]> result = executeNativeQuery(sb.toString(), params, null);
        return result;
    }

    /**
     * Método que verifica si un alumno tiene otorgamiento externo
     *
     * @param alumnoId
     * @param boleta
     * @param cicloId
     * @return true si es que tiene
     */
    @Override
    public Boolean tieneOtorgamientoExterno(String curp, BigDecimal cicloId) {
        //TODO falta agregar el ciclo
        StringBuilder sb = new StringBuilder();
        List<String> criteria = new ArrayList<>();

        sb.append(" SELECT COUNT(o)");
        sb.append(" FROM OtorgamientoExterno o ");

        criteria.add("o.curp = ?1 ");
        criteria.add("o.cicloEscolar.id = ?2");
        agregaCriterios(sb, criteria);

        Long result = getCountQuery(sb.toString(), curp, cicloId);

        return result > 0;
    }
}
