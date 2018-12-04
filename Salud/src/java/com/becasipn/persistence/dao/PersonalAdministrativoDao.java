package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.PersonalAdministrativo;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Victor Lozano
 */
public interface PersonalAdministrativoDao extends DaoBase<PersonalAdministrativo, BigDecimal> {

    public PersonalAdministrativo findByUsuario(BigDecimal id);
    
    public PersonalAdministrativo findByUsuario(String usr);

    public PersonalAdministrativo findById(String id);

    public PersonalAdministrativo findByCargo(BigDecimal unidadAcademicaId, BigDecimal cargoId);

    public List<PersonalAdministrativo> findAdministrativos();

    public List<PersonalAdministrativo> asociadaPersonalAdministrativo(BigDecimal unidadAcademicaId);

    public List<PersonalAdministrativo> personalAdministrativoJefeBecas();
    public List<PersonalAdministrativo> personalAdministrativoResponsableBecas(BigDecimal uaId);
}
