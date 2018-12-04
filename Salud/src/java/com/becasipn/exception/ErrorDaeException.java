/**
* SISTEMA DE BECAS DEL INSTITUTO POLITECNICO NACIONAL
* DIRECCION DE SERVICIOS ESTUDIANTILES
* 2016
**/

package com.becasipn.exception;

import javax.servlet.ServletException;

/**
 *
 * @author Patricia Benítez
 */
public class ErrorDaeException extends ServletException {
public ErrorDaeException() {
        super();
    }

    public ErrorDaeException(String message) {
        super(message);
    }

}
