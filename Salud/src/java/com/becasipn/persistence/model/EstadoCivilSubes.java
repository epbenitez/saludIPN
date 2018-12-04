package com.becasipn.persistence.model;

import java.math.BigDecimal;

/**
 *
 * @author Rafael Cardenas Resendiz
 */
public enum EstadoCivilSubes {

    SOLTERO("SOLTERO", new BigDecimal(1)),
    CASADO("CASADO", new BigDecimal(2)),
    DIVORCIADO("DIVORCIADO",  new BigDecimal(3)),
    SEPARADO("SEPARADO", new BigDecimal(3)),
    VIUDO("VIUDO", new BigDecimal(4)),
    UNIONLIBRE("UNION LIBRE", new BigDecimal(5));

    private BigDecimal id;
    private String nombre;

    public BigDecimal getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }

    private EstadoCivilSubes(String nombre, BigDecimal id) {
        this.nombre = nombre;
        this.id = id;
    }

}