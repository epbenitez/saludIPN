/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.becasipn.exception;

import javax.servlet.ServletException;

/**
 *
 * @author Patricia Benitez
 */
public class CatalogoAdminException extends ServletException {

    public CatalogoAdminException() {
        super();
    }

    public CatalogoAdminException(String message) {
        super(message);
    }

}
