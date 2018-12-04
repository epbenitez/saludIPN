package com.becasipn.persistence.model;

/**
 * Enum de la definición de acciones (ALTA | MOFIFICACION | BAJA)
 *
 * @author Maricarmen Agüero Arias <maricarmen.aguero@fit.com.mx>
 */
public enum Accion {

    /**
     * Representación de la accion ALTA
     */
    ALTA("Alta", "0"),
    /**
     * Representación de la accion Modificacion
     */
    MODIFICACION("Modificacion", "1"),
    /**
     * Representación de la accion Baja
     */
    BAJA("Baja", "2"),
    /**
     * Representación de una peticion a un web service
     */
    WEBSERVICE("WebService", "3");

    private String id;
    private String name;

    /**
     * Obtiene el valor de la variable id
     *
     * @return el valor de la variable id
     */
    public String getId() {
        return id;
    }

    /**
     * Obtiene el valor de la variable nombre
     *
     * @return el valor de la variable nombre
     */
    public String getName() {
        return name;
    }

    /**
     * Constructor privado del Genero
     *
     * @param sexo Genero
     */
    private Accion(String name, String id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String toString() {
        return "Accion{" + id + "," + name + '}';
    }

}
