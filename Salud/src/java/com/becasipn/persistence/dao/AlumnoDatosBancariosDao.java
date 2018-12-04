package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.AlumnoDatosBancarios;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez
 */
public interface AlumnoDatosBancariosDao extends DaoBase<AlumnoDatosBancarios, BigDecimal>{
    public AlumnoDatosBancarios datosBancarios(BigDecimal alumnoId);
    public List<AlumnoDatosBancarios> listaDatosBancarios(BigDecimal alumnoId);
}