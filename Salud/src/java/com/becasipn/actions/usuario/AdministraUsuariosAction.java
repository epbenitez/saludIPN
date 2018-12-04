/**
 * SISTEMA INFORMÁTICO DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE
 * SERVICIOS ESTUDIANTILES Created on : 18-ago-2015, 12:46:52
 *
 */
package com.becasipn.actions.usuario;

import com.becasipn.actions.BaseUploadFileAction;
import com.becasipn.business.UsuarioBO;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.PersonalAdministrativo;
import com.becasipn.persistence.model.Usuario;
import static com.opensymphony.xwork2.Action.INPUT;
import com.opensymphony.xwork2.ModelDriven;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Patricia Benitez
 */
public class AdministraUsuariosAction extends BaseUploadFileAction
        implements ModelDriven<Usuario>, ServletRequestAware, ServletResponseAware, MensajesUsuario {

    private Usuario usuario = new Usuario();
    private HttpServletRequest request;
    private HttpServletResponse response;

    private String contraseniaActual;
    private String contraseniaNueva;
    private String boleta;
    private String correo;

    @Override
    public Usuario getModel() {
        return usuario;
    }

    public String cambioContrasenia() {
        return INPUT;
    }

    public String actualizaContrasenia() {
        String usuarioSesion = SecurityContextHolder.getContext().getAuthentication().getName();
        UsuarioBO bo = new UsuarioBO(getDaos());
        if (usuarioSesion == null || usuarioSesion.isEmpty()) {
            addActionError(getText("actualiza.contrasenia.error"));
            return INPUT;
        }
        Boolean actualizaContrasenia = bo.actualizaContrasenia(usuarioSesion, contraseniaActual, contraseniaNueva);
        if (actualizaContrasenia) {
            addActionMessage(getText("actualiza.contrasenia.exito"));
        } else {
            addActionError(getText("actualiza.contrasenia.error.actual"));
        }
        return INPUT;
    }

    public String recuperarContrasenia() {
        return "recuperar";
    }

    public String recuperaContrasenia() {
        UsuarioBO bo = new UsuarioBO(getDaos());
        Alumno a = bo.getAlumnoPorBoleta(boleta);
        if (a != null) {
            if (bo.recuperarContrasenia(a)) {
                String[] datos = new String[1];
                datos[0] = a.getCorreoElectronico();
                addActionMessage(getText("recuperar.contrasenia.exito", datos));
                return "login";
            } else {
                addActionError(getText("recuperar.contrasenia.error"));
                return "recuperar";
            }
        } else if (bo.existeUsuario(boleta)) {
            PersonalAdministrativo p = bo.getPersonalByUsuario(boleta);
            if (bo.recuperarContrasenia(p)) {
                String[] datos = new String[1];
                datos[0] = p.getCorreoElectronico();
                addActionMessage(getText("recuperar.contrasenia.exito", datos));
                return "login";
            } else {
                addActionError(getText("recuperar.contrasenia.error.admin"));
                return "recuperar";
            }
        } else {
            addActionError(getText("recuperar.contrasenia.error.boleta.no.existe"));
            return "recuperar";
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public String getContraseniaActual() {
        return contraseniaActual;
    }

    public void setContraseniaActual(String contraseniaActual) {
        this.contraseniaActual = contraseniaActual;
    }

    public String getContraseniaNueva() {
        return contraseniaNueva;
    }

    public void setContraseniaNueva(String contraseniaNueva) {
        this.contraseniaNueva = contraseniaNueva;
    }

    @Override
    public void setServletRequest(HttpServletRequest hsr) {
        this.request = hsr;
    }

    @Override
    public void setServletResponse(HttpServletResponse hsr) {
        this.response = hsr;
    }

    public String getBoleta() {
        return boleta;
    }

    public void setBoleta(String boleta) {
        this.boleta = boleta;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

}
