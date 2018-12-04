/**
 * SISTEMA INFORMÁTICO DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE
 * SERVICIOS ESTUDIANTILES 2016
 *
 */
package com.becasipn.exception;

import javax.servlet.ServletException;

/**
 *
 * @author Patricia Benítez
 */
public class LoginException extends ServletException {

    public LoginException() {
        super();
    }

    public LoginException(String message) {
        super(message);
    }

}
