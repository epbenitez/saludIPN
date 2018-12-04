/**
 * SISTEMA DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE SERVICIOS
 * ESTUDIANTILES 2016
*
 */
package com.becasipn.domain;

/**
 *
 * @author Patricia Benítez
 */
public class OrdenDepositoPivot {

    private String nombre;
    private String valoresStr;
    private String valoresStrEjeX;
    private Integer cuentaId;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getValoresStr() {
        return valoresStr;
    }

    public void setValoresStr(String valoresStr) {
        this.valoresStr = valoresStr;
    }

    public Integer getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(Integer cuentaId) {
        this.cuentaId = cuentaId;
    }
    

    public String getValoresStrEjeX() {
        return valoresStrEjeX;
    }

    public void setValoresStrEjeX(String valoresStrEjeX) {
        this.valoresStrEjeX = valoresStrEjeX;
    }
    

}
