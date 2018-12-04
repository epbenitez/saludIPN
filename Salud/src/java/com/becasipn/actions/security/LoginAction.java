package com.becasipn.actions.security;

import com.becasipn.actions.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

/**
 * Acciones que facilitan la autentificación de usuarios.
 *
 * @author Patricia Benitez
 * @version $Rev: 1047 $
 * @since 1.0
 */
public class LoginAction extends BaseAction implements MensajesSecurity {

    private boolean login_error;

    /**
     * Inicializa el objeto <code>LoginAction</code>.
     */
    public LoginAction() {
    }

    /**
     * Obtiene el valor de la variable login_error.
     *
     * @return el valor de la variable login_error.
     */
    public boolean isLogin_error() {
        return login_error;
    }

    /**
     * Establece el valor de la variable login_error.
     *
     * @param login_error nuevo valor de la variable login_error.
     */
    public void setLogin_error(boolean login_error) {
        this.login_error = login_error;
    }

    /**
     * Acción qué realiza el login de usuarios.
     *
     * @return un string representando el resultado lógico de la ejecución de la
     * acción.
     */
    @Override
    public String execute() {
        if (!isAuthenticated()) {
            if (ActionContext.getContext().getSession().get(AbstractAuthenticationProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY) != null) {
                addActionError(
                        ((AuthenticationException) ActionContext.getContext().getSession().get(
                                AbstractAuthenticationProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY)).getLocalizedMessage());
                return INPUT;
            }

            return SUCCESS;
        } else {
//            LO COMENTAMOS YA QUE LE DAMOS PASO AL MENU QUE SALE EN LE PAGINA INICIAL
//            if (isCotejador()) {
//                return "cotejo";
//            }
//            if (isAlumno()) {
//                return "alumno";
//            }
        }
        return "loggedin";
    }
}
