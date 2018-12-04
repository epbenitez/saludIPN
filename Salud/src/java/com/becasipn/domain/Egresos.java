/**
* SISTEMA DE BECAS DEL INSTITUTO POLITECNICO NACIONAL
* DIRECCION DE SERVICIOS ESTUDIANTILES
* 2016
**/

package com.becasipn.domain;

/**
 *
 * @author Patricia Benítez
 */
public class Egresos {
    
    private String nombre;
    private String monto;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    @Override
    public String toString() {
        return "Egresos{" + "nombre=" + nombre + ", monto=" + monto + '}';
    }

}
