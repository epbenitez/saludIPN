/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.becasipn.util;

/**
 *
 * @author SISTEMAS
 */
public enum ErrorDAE {
    NO_CONEXION("registro.alumno.no.dae"),
    NO_ENCONTRADO("registro.alumno.no.encontrado"),
    NO_INSCRITO("registro.alumno.no.inscrito"),
    MUCHOS_ENCONTRADO("registro.alumno.multiples"),
    ESPCIALIDAD_INCORRECTA("registro.alummo.error.datos.academicos"),
    DATOS_ACADEMIOS("registro.alummo.error.datos.academicos");
    
    private String msgUrl;

    private ErrorDAE(String msgUrl) {
        this.msgUrl = msgUrl;
    }

    public String getMsg() {
        return msgUrl;
    }
    
}
