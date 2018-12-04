package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.SolicitudCuentasDao;
import com.becasipn.persistence.model.SolicitudCuentas;
import com.becasipn.util.PaginateUtil;
import com.becasipn.util.ServerSideUtil;
import static com.becasipn.util.StringUtil.buildCountQuery;
import java.math.BigDecimal;

public class SolicitudCuentasJpaDao extends JpaDaoBase<SolicitudCuentas, BigDecimal> implements SolicitudCuentasDao {

    public SolicitudCuentasJpaDao() {
	super(SolicitudCuentas.class);
    }

    @Override
    public SolicitudCuentas findByIdentificador(String identificador) {
	String jpql = "SELECT sc FROM SolicitudCuentas sc "
		+ " WHERE sc.identificador =?1";
	return executeSingleQuery(jpql, identificador);
    }

    @Override
    public boolean existeIdentificador(String identificador) {
	String jpql = "SELECT COUNT(sc) FROM SolicitudCuentas sc "
		+ " WHERE sc.identificador =?1";
	Long sc = getCountQuery(jpql, identificador);
	return sc.equals(1L);
    }
    
     @Override
    public PaginateUtil getListado(ServerSideUtil ssu) {
	StringBuilder sbQuery = new StringBuilder("SELECT sc FROM SolicitudCuentas sc");
	StringBuilder sbQueryFiltered = new StringBuilder(sbQuery.toString());
	Long noTotal = getCountQuery(buildCountQuery(sbQuery.toString(), Boolean.FALSE));
	solicitudOrderBy(sbQueryFiltered, ssu.getSortCol(), ssu.getSortDir());
	Long noTotalFiltered = getCountQuery(buildCountQuery(sbQueryFiltered.toString(), Boolean.FALSE));
	return new PaginateUtil(executeQueryPaginate(sbQueryFiltered.toString(), ssu.getStart(), ssu.getLength()), noTotal, noTotalFiltered);
    }

    private void solicitudOrderBy(StringBuilder sbQueryFiltered, int sortingCol, String sortDir) {
	if (sortDir != null) {
	    StringBuilder orderBy = new StringBuilder(" order by ");
	    switch (sortingCol) {
		case 1:
		    orderBy.append("sc.identificador ");
		    break;
		case 2:
		    orderBy.append("sc.fechaGeneracion ");
		    break;
		case 3:
		    orderBy.append("sc.fechaFinalizacion ");
		    break;
		default:
		    return;
	    }
	    orderBy.append(sortDir);
	    sbQueryFiltered.append(orderBy.toString());
	}
    }
    
}
