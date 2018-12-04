/**
 * SISTEMA INFORMÁTICO DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE
 * SERVICIOS ESTUDIANTILES Created on : 07-ago-2015, 12:21:21
 *
 */
package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.CarreraDao;
import com.becasipn.persistence.model.Carrera;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Patricia Benitez
 */
public class CarreraJpaDao extends JpaDaoBase<Carrera, BigDecimal> implements CarreraDao {

    public CarreraJpaDao() {
        super(Carrera.class);
    }

    // SQLINJECTION OK
    @Override
    public Carrera getCarrera(String planEstudios, String carrera, String especialidad, BigDecimal unidadAcademica) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> params = new HashMap();
        planEstudios = planEstudios == null ? null : new Integer(planEstudios).toString();
        
        sb.append(" SELECT c ");
        sb.append(" FROM Carrera c ");

        List<String> criteria = new ArrayList<>();
        if (planEstudios != null) {
            criteria.add("c.planEstudios LIKE :planEstudios");
            params.put("planEstudios", planEstudios);
        }
        if (carrera != null) {
            criteria.add("c.claveCarrera LIKE :carrera");
            params.put("carrera", carrera);
        }
        if (especialidad != null) {
            criteria.add("c.especialidad LIKE :especialidad");
            params.put("especialidad", especialidad);
        }
        if (unidadAcademica != null) {
            criteria.add("c.unidadAcademica.id = :unidadAcademica");
            params.put("unidadAcademica", unidadAcademica);
        }
        agregaCriterios(sb, criteria);
        List<Carrera> rel = executeQuery(sb.toString(), params, null);

        return (rel == null || rel.isEmpty()) ? null : rel.get(0);
    }
    
    //SQLINJECTION Eliminado
    // public Carrera busca(String planEstudios, String carrera, String especialidad, BigDecimal unidadAcademica) {
    
    /**
     * Devuelve la relación semestre máximo, carrera
     *
     * @author Victor Lozano
     * @return lista
     */
    @Override
    public HashMap<BigDecimal, Integer> getSemestresMaximos() {
        HashMap<BigDecimal, Integer> relacion = new HashMap<>();
        List<Carrera> lista = this.findAll();
        for (Carrera carrera : lista) {
            relacion.put(carrera.getId(), carrera.getNumeroSemestres());
        }
        return relacion;
    }

    @Override
    public List<String> findByUnidadAacademica(BigDecimal unidadAcademicaId) {
        String jpql = "SELECT c.carrera FROM Carrera C WHERE C.unidadAcademica.id = ?1  AND C.activo = 1 GROUP BY c.carrera";
        return executeQueryObject(jpql, unidadAcademicaId);
    }

    @Override
    public List<Carrera> findByNivel(BigDecimal nivelId) {
        String jpql = "SELECT C FROM Carrera C WHERE C.unidadAcademica.nivel.id = ?1 ";
        return executeQuery(jpql, nivelId);
    }

    @Override
    public List<Carrera> findAllGrouped() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<String> findPlan(BigDecimal unidadAcademicaId) {
        String jpql = "SELECT DISTINCT c.planEstudios FROM Carrera C WHERE C.unidadAcademica.id = ?1  AND C.activo = 1";
        return executeQueryObject(jpql, unidadAcademicaId);
    }

    @Override
    public List<Carrera> findCarrera(BigDecimal unidadAcademicaId, String planEstudios) {
        String jpql = "SELECT DISTINCT C FROM Carrera C WHERE C.unidadAcademica.id = ?1 AND C.planEstudios = ?2 AND C.activo = 1";
        return executeQueryObject(jpql, unidadAcademicaId, planEstudios);
    }

    @Override
    public List<String> findEspecialidad(BigDecimal unidadAcademicaId, String planEstudios, String carrera) {
        String jpql = "SELECT DISTINCT c.especialidad FROM Carrera C WHERE C.unidadAcademica.id = ?1 AND C.planEstudios = ?2 AND C.claveCarrera = ?3 AND C.activo = 1";
        return executeQueryObject(jpql, unidadAcademicaId, planEstudios, carrera);
    }
}
