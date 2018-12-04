package com.becasipn.business;

import com.becasipn.persistence.model.PersonalAdministrativo;
import com.becasipn.persistence.model.Rol;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.persistence.model.UsuarioPrivilegio;
import com.becasipn.service.Service;
import com.opensymphony.xwork2.ActionContext;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Victor Lozano
 */
public class PersonalAdministrativoBO extends BaseBO {

    public PersonalAdministrativoBO(Service service) {
        super(service);
    }

    /**
     * Almacena la información de la entidad en la base de datos
     *
     * @author Victor Lozano
     * @param personalAdministrativo
     * @param usuario
     * @param rol
     * @return true si la operación se realizó exitosamente
     */
    public Boolean guardaPersonalAdministrativo(PersonalAdministrativo personalAdministrativo, Usuario usuario, Rol... rol) {
        personalAdministrativo.setNombre(personalAdministrativo.getNombre().toUpperCase());
        personalAdministrativo.setApellidoPaterno(personalAdministrativo.getApellidoPaterno().toUpperCase());
        personalAdministrativo.setApellidoMaterno(personalAdministrativo.getApellidoMaterno().toUpperCase());

        Usuario usuarioAux;
        try {
            if (usuario.getId() == null) {
                usuarioAux = service.getUsuarioDao().save(usuario);
            } else {
                usuarioAux = service.getUsuarioDao().update(usuario);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }

        UsuarioPrivilegio usuarioPrivilegio = service.getUsuarioPrivilegioDao().findByUsuario(usuarioAux.getId());
        try {
            if (usuarioPrivilegio == null) {
                for (Rol roles : rol) {
                    usuarioPrivilegio = new UsuarioPrivilegio(roles, usuarioAux);
                    service.getUsuarioPrivilegioDao().save(usuarioPrivilegio);
                }
            } else {
                do {
                    service.getUsuarioPrivilegioDao().delete(usuarioPrivilegio);
                    usuarioPrivilegio = service.getUsuarioPrivilegioDao().findByUsuario(usuarioAux.getId());
                } while (usuarioPrivilegio != null);
                for (Rol roles : rol) {
                    usuarioPrivilegio = new UsuarioPrivilegio(roles, usuarioAux);
                    service.getUsuarioPrivilegioDao().save(usuarioPrivilegio);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }

        personalAdministrativo.setUsuario(usuarioAux);

        Usuario usuarioModifico = (Usuario) ActionContext.getContext().getSession().get("usuario");
        personalAdministrativo.setUsuarioModifico(usuarioModifico);
        try {
            if (personalAdministrativo.getId() == null) {
                personalAdministrativo.setFechaAlta(new Date());
                service.getPersonalAdministrativoDao().save(personalAdministrativo);
            } else {
                PersonalAdministrativo px = service.getPersonalAdministrativoDao().findById(personalAdministrativo.getId());
                personalAdministrativo.setFechaModificacion(new Date());
                personalAdministrativo.setFechaAlta(px.getFechaAlta());
                service.getPersonalAdministrativoDao().update(personalAdministrativo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    /**
     * Almacena la información del usuario que elimina la entidad
     *
     * @author Victor Lozano
     * @param personalAdministrativo
     * @return {[Boolean]}
     */
    public Boolean eliminaPersonalAdministrativo(PersonalAdministrativo personalAdministrativo) {
        Usuario usuarioElimino = (Usuario) ActionContext.getContext().getSession().get("usuario");
        personalAdministrativo.setUsuarioModifico(usuarioElimino);
        try {
            personalAdministrativo.setFechaBaja(new Date());
            service.getPersonalAdministrativoDao().update(personalAdministrativo);
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    public PersonalAdministrativo getPersonalAdministrativo(BigDecimal id) {
        PersonalAdministrativo personalAdministrativo = service.getPersonalAdministrativoDao().findById(id);
        return personalAdministrativo;
    }
    
    public PersonalAdministrativo getPersonalAdministrativoByUserId(BigDecimal id) {
        PersonalAdministrativo personalAdministrativo = service.getPersonalAdministrativoDao().findByUsuario(id);
        return personalAdministrativo;
    }
}
