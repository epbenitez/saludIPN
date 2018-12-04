/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.becasipn.domain;

/**
 *
 * @author Patricia Benitez
 */
public enum Genero {

    /**
     * Representación del genero masculino.
     */
    MASCULINO("Masculino", "M"),
    /**
     * Representación del genero femenino.
     */
    FEMENINO("Femenino", "F");

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
    private Genero(String name, String id) {
        this.name = name;
        this.id = id;
    }

}
