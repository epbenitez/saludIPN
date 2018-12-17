/**
 * SISTEMA INFORMÁTICO DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE
 * SERVICIOS ESTUDIANTILES 2017
 *
 */
package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.DatosAcademicosDao;
import com.becasipn.persistence.model.DatosAcademicos;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Mario Márquez
 */
public class DatosAcademicosJpaDao extends JpaDaoBase<DatosAcademicos, BigDecimal> implements DatosAcademicosDao {

    public DatosAcademicosJpaDao() {
        super(DatosAcademicos.class);
    }

    @Override
    public DatosAcademicos datosPorPeriodo(BigDecimal alumnoId, BigDecimal periodoId) {
        String jpql = "select da from DatosAcademicos da where da.alumno.id = ?1 and da.periodo.id = ?2";
        List<DatosAcademicos> listaDA = executeQuery(jpql, alumnoId, periodoId);
        return listaDA.isEmpty()?null:listaDA.get(0);
    }
    
    @Override
    public DatosAcademicos ultimosDatos(BigDecimal alumnoId) {
        String jpql = "select da from DatosAcademicos da where da.alumno.id = ?1 order by da.id desc";
        List<DatosAcademicos> listaDA = executeQuery(jpql, alumnoId);
        return listaDA.isEmpty()?null:listaDA.get(0);
    }
}
