package com.becasipn.persistence.model;

import java.math.BigDecimal;

/**
 * Esta interfaz representa las propiedades basicas que se necesitan de las
 * entidades.
 *
 * @author Patricia Benitez
 * @version $Rev: 1165 $
 * @since 1.0
 */
public interface BaseEntity {

    /**
     * Obtiene el id de la Entidad.
     *
     * @return el valor de la variable id.
     */
    public BigDecimal getId();

    /**
     * Establece el valor del id de la Entidad.
     *
     * @param id
     */
    public void setId(BigDecimal id);

    @Override
    public String toString();

}
