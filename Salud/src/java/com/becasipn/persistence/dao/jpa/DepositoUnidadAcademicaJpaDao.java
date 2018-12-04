package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.DepositoUnidadAcademicaDao;
import com.becasipn.persistence.model.Beca;
import com.becasipn.persistence.model.DepositoUnidadAcademica;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez
 */
public class DepositoUnidadAcademicaJpaDao extends JpaDaoBase<DepositoUnidadAcademica, BigDecimal> implements DepositoUnidadAcademicaDao {

    public DepositoUnidadAcademicaJpaDao() {
        super(DepositoUnidadAcademica.class);
    }

    @Override
    public List<DepositoUnidadAcademica> getOrigenRecursos(BigDecimal programaBecaId) {
        String sql = "select distinct(dua.CORRESPONDEIPN),1 \n"
                + "from cat_programa_beca pb\n"
                + "inner join cat_tipo_beca tb on pb.id = tb.beca_id \n"
                + "inner join rmm_deposito_unidad_academica dua on tb.id = dua.tipobeca_id\n"
                + "where pb.id = " + programaBecaId;
//        List<DepositoUnidadAcademica> lista = executeNativeQuery(sql,DepositoUnidadAcademica.class);
        List<Object[]> lista = executeNativeQuery(sql);
        List<DepositoUnidadAcademica> lb = new ArrayList<DepositoUnidadAcademica>();
        for (Object[] res : lista) {
            DepositoUnidadAcademica dua = new DepositoUnidadAcademica();
            BigDecimal d = (BigDecimal)res[0]; 
            dua.setCorrespondeIPN( (res[0] ==null || d.compareTo(new BigDecimal(0))==0 )? Boolean.FALSE:Boolean.TRUE);
            lb.add(dua);
        }
       
        return lb==null || lb.isEmpty()?null:lb;
    }
}
