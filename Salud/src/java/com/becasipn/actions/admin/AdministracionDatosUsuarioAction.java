package com.becasipn.actions.admin;

import com.becasipn.actions.BaseAction;
import com.becasipn.business.PersonalAdministrativoBO;
import com.becasipn.persistence.model.PersonalAdministrativo;
import com.becasipn.persistence.model.Rol;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.persistence.model.UsuarioPrivilegio;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class AdministracionDatosUsuarioAction extends BaseAction implements MensajesAdmin {
    public static final String FORMULARIO = "formulario";
    public static final String LISTA = "lista";

    private Usuario usuario = new Usuario();
    private PersonalAdministrativo personalAdministrativo = new PersonalAdministrativo();
    private List<UsuarioPrivilegio> usuarioPrivilegios;
    private List<Rol> roles;
    private BigDecimal privilegio;
    private Boolean lectura;

    public String form() {
        lectura = false;
        roles = getDaos().getRolDao().findAll();
        Rol rol = new Rol(BigDecimal.valueOf(29));
        roles.remove(rol);
        
        usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        PersonalAdministrativoBO personalAdministrativoBO = new PersonalAdministrativoBO(getDaos());        
        personalAdministrativo = personalAdministrativoBO.getPersonalAdministrativoByUserId(usuario.getId());

        usuarioPrivilegios = getDaos().getUsuarioPrivilegioDao().getPrivilegiosByUser(usuario);

        for (UsuarioPrivilegio user : usuarioPrivilegios) {
            if (user.getPrivilegio().getId().intValue() != 29) {
                privilegio = user.getPrivilegio().getId();
            } else if (user.getPrivilegio().getId().intValue() == 29) {
                lectura = true;
            }
        }
        return SUCCESS;
    }

    public String guarda() {
        roles = getDaos().getRolDao().findAll();
        if (lectura != null) {
            Rol rolLectura = getDaos().getRolDao().findByName("ROLE_CONSULTA");
            Rol rolPersonal = getDaos().getRolDao().findById(privilegio);
            PersonalAdministrativoBO personalAdministrativoBO = new PersonalAdministrativoBO(getDaos());
            if (usuario.getId() == null && getDaos().getUsuarioDao().existeUsuario(usuario.getUsuario())) {
                addActionError(getText("admin.usuario.repetido"));
                return FORMULARIO;
            }

            usuario.setFechaModificacion(new Date());

            if (personalAdministrativoBO.guardaPersonalAdministrativo(personalAdministrativo, usuario, rolLectura, rolPersonal)) {
                addActionMessage(getText("admin.guardado.exito"));
            } else {
                addActionError(getText("admin.guardado.error"));
            }
        } else {
            Rol rolPersonal = getDaos().getRolDao().findById(privilegio);
            PersonalAdministrativoBO personalAdministrativoBO = new PersonalAdministrativoBO(getDaos());
            if (usuario.getId() == null && getDaos().getUsuarioDao().existeUsuario(usuario.getUsuario())) {
                addActionError(getText("admin.usuario.repetido"));
                return FORMULARIO;
            }

            usuario.setFechaModificacion(new Date());

            if (personalAdministrativoBO.guardaPersonalAdministrativo(personalAdministrativo, usuario, rolPersonal)) {
                addActionMessage(getText("admin.guardado.exito"));
            } else {
                addActionError(getText("admin.guardado.error"));
            }
        }
        return FORMULARIO;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public PersonalAdministrativo getPersonalAdministrativo() {
        return personalAdministrativo;
    }

    public void setPersonalAdministrativo(PersonalAdministrativo personalAdministrativo) {
        this.personalAdministrativo = personalAdministrativo;
    }

    public List<Rol> getRoles() {
        return roles;
    }

    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }

    public BigDecimal getPrivilegio() {
        return privilegio;
    }

    public void setPrivilegio(BigDecimal privilegio) {
        this.privilegio = privilegio;
    }

    public Boolean getLectura() {
        return lectura;
    }

    public void setLectura(Boolean lectura) {
        this.lectura = lectura;
    }
}
