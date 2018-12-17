package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.PersonalAdministrativoDao;
import com.becasipn.persistence.model.PersonalAdministrativo;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Victor Lozano
 */
public class PersonalAdministrativoJpaDao extends JpaDaoBase<PersonalAdministrativo, BigDecimal> implements PersonalAdministrativoDao {

    public PersonalAdministrativoJpaDao() {
        super(PersonalAdministrativo.class);
    }

   
    /**
     * Devuelve el personal administrativo que corresponde a un id de usuario
     *
     * @author Victor Lozano
     * @param id
     * @return PersonalAdministrativo
     */
    @Override
    public PersonalAdministrativo findByUsuario(BigDecimal id) {
        String consulta = "SELECT p FROM PersonalAdministrativo p "
                + "WHERE p.usuario.id = ?1";
        List<PersonalAdministrativo> personalAdministrativo = executeQuery(consulta, id);

        return personalAdministrativo == null || personalAdministrativo.isEmpty() ? null : personalAdministrativo.get(0);
    }
}