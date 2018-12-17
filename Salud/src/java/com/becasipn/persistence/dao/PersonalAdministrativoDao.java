package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.PersonalAdministrativo;
import java.math.BigDecimal;

/**
 *
 * @author Victor Lozano
 */
public interface PersonalAdministrativoDao extends DaoBase<PersonalAdministrativo, BigDecimal> {

    public PersonalAdministrativo findByUsuario(BigDecimal id);

}
