package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.PersonalAdministrativoDao;
import com.becasipn.persistence.model.PersonalAdministrativo;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Victor Lozano
 */
public class PersonalAdministrativoJpaDao extends JpaDaoBase<PersonalAdministrativo, BigDecimal> implements PersonalAdministrativoDao {

    public PersonalAdministrativoJpaDao() {
        super(PersonalAdministrativo.class);
    }

    @Override
    public PersonalAdministrativo findById(String id) {
        String jpql = "SELECT p FROM PersonalAdministrativo p "
                + "WHERE p.id = ?1";
        PersonalAdministrativo personalAdministrativo = executeSingleQuery(jpql, id);

        return personalAdministrativo;
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

    @Override
    public PersonalAdministrativo findByUsuario(String usr) {
        String consulta = "SELECT p FROM PersonalAdministrativo p, Usuario u "
                + "WHERE p.usuario.id = u.id "
                + "AND u.usuario = ?1";
        List<PersonalAdministrativo> personalAdministrativo = executeQuery(consulta, usr);

        return personalAdministrativo == null || personalAdministrativo.isEmpty() ? null : personalAdministrativo.get(0);
    }

    @Override
    public PersonalAdministrativo findByCargo(BigDecimal unidadAcademicaId, BigDecimal cargoId) {
        String consulta = "SELECT p FROM PersonalAdministrativo p "
                + "WHERE p.unidadAcademica.id = ?1 "
                + "AND p.cargo.id = ?2";
        List<PersonalAdministrativo> personalAdministrativo = executeQuery(consulta, unidadAcademicaId, cargoId);

        return personalAdministrativo == null || personalAdministrativo.isEmpty() ? null : personalAdministrativo.get(0);
    }

    /**
     * Devuelve el listado de usuarios administrativos del sistema
     *
     * @author Victor Lozano
     * @return personal
     */
    @Override
    public List<PersonalAdministrativo> findAdministrativos() {
        String consulta = "SELECT p FROM PersonalAdministrativo p "
                + "WHERE p.usuario.id IN "
                + "(SELECT DISTINCT(p.usuario.id) FROM PersonalAdministrativo p, Usuario u, UsuarioPrivilegio up "
                + "WHERE up.privilegio.id != 2 "
                + "AND up.usuario.id = u.id "
                + "AND u.id = p.usuario.id "
                + "AND u.activo = 1)";
        List<PersonalAdministrativo> personal = executeQuery(consulta);
        return personal == null ? null : personal;
    }

    @Override
    public List<PersonalAdministrativo> asociadaPersonalAdministrativo(BigDecimal unidadAcademicaId) {
        String jpql = "SELECT p FROM  PersonalAdministrativo p WHERE p.unidadAcademica.id = ?1";
        //SELECT * FROM becasipn.ent_personal_administrativo where unidadacademica_id=1;
        List<PersonalAdministrativo> lista = executeQuery(jpql, unidadAcademicaId);
        return lista;
    }

    /**
     * Devuelve el listado del personal administrativo que tiene el rol de jefe
     * de becas.
     *
     * @author Tania G. Sánchez
     * @return personal
     */
    @Override
    public List<PersonalAdministrativo> personalAdministrativoJefeBecas() {
        String sql = "select pa.* from ent_personal_administrativo pa "
                + "inner join rmm_rol_usuario ro on pa.usuario_id = ro.usuario_id "
                + "where ro.rol_id = 3";
        List<Object[]> lista = executeNativeQuery(sql);
        List<PersonalAdministrativo> lpa = new ArrayList<PersonalAdministrativo>();
        for (Object[] res : lista) {
            PersonalAdministrativo pa = new PersonalAdministrativo();
            pa.setId((BigDecimal) res[0]);
            pa.setNombre((String) res[1]);
            pa.setApellidoPaterno((String) res[2]);
            pa.setApellidoMaterno((String) res[3]);
            lpa.add(pa);
        }
        return lpa;
    }
    
    @Override
    public List<PersonalAdministrativo> personalAdministrativoResponsableBecas(BigDecimal uaId) {
        String sql = "select pa.* from ent_personal_administrativo pa "
                + " inner join rmm_rol_usuario ro on pa.usuario_id = ro.usuario_id "
                + " where ro.rol_id = 5 "
                + (uaId == null ? "" : " and pa.unidadAcademica_id = " + uaId);
        List<Object[]> lista = executeNativeQuery(sql);
        List<PersonalAdministrativo> lpa = new ArrayList<PersonalAdministrativo>();
        for (Object[] res : lista) {
            PersonalAdministrativo pa = new PersonalAdministrativo();
            pa.setId((BigDecimal) res[0]);
            pa.setNombre((String) res[1]);
            pa.setApellidoPaterno((String) res[2]);
            pa.setApellidoMaterno((String) res[3]);
            lpa.add(pa);
        }
        return lpa;
    }
}