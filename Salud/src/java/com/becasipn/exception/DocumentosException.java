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
public class DocumentosException extends ServletException {

    public DocumentosException() {
        super();
    }

    public DocumentosException(String message) {
        super(message);
    }

}
