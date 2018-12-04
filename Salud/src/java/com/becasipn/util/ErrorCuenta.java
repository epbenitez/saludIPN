package com.becasipn.util;

/**
 *
 * @author Rafael Cardenas Reséndiz
 */
public enum ErrorCuenta {
    
    NO_ALUMNO("Alumno no encontrado"),
    NO_RELACION("Solicitud no encontrada"),
    CON_CUENTA("El alumno ya tiene una cuenta"),
    CUENTA_DIF("El alumno tiene una cuenta diferente en el sistema"),
    EXISTE_CUENTA("Esta cuenta ya existe"),
    CON_DEPOSITOS("Esta cuenta tiene depositos asociados"),
    SIN_ESTATUS("No hay estatus en la solicitud"),
    ESTATUS_INVALIDO("La cuenta no esta en un estatus válido para ser actualizado");
    
    private String msgUrl;

    private ErrorCuenta(String msgUrl) {
        this.msgUrl = msgUrl;
    }

    public String getMsg() {
        return msgUrl;
    }
    
}
