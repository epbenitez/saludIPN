/**
 * SISTEMA DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE SERVICIOS
 * ESTUDIANTILES 2016
*
 */
package com.becasipn.exception;

import javax.servlet.ServletException;

/**
 *
 * @author Patricia Benítez
 */
public class RegexException  extends ServletException {

    public RegexException() {
        super();
    }

    public RegexException(String message) {
        super(message);
    }

}
