/**
 * SISTEMA INFORMÁTICO DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE
 * SERVICIOS ESTUDIANTILES 2015
 *
 */
package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.AlumnoDao;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.util.PaginateUtil;
import com.becasipn.util.ServerSideUtil;
import java.math.BigDecimal;
import java.util.List;
import static com.becasipn.util.StringUtil.addParameters;
import static com.becasipn.util.StringUtil.buildCountQuery;

/**
 *
 * @author Patricia Benitez
 */
public class AlumnoJpaDao extends JpaDaoBase<Alumno, BigDecimal> implements AlumnoDao {

    public AlumnoJpaDao() {
        super(Alumno.class);
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
}
