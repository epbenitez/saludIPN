/**
* SISTEMA DE BECAS DEL INSTITUTO POLITECNICO NACIONAL
* DIRECCION DE SERVICIOS ESTUDIANTILES
* 2016
**/

package com.becasipn.persistence.model;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.Cache;

/**
 *
 * @author Patricia Benítez
 */
@Entity
@Table(name = "CAT_ERRORES_BANAMEX")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class ErroresBanamex  implements BaseEntity {
    @Id
    private BigDecimal id;
    private String error;
    private String descripcion;
    private String accion;
    
    public ErroresBanamex(){
        
    }
    
    public ErroresBanamex(BigDecimal id){
        this.id = id;
    }
    
    @Override
    public BigDecimal getId() {
        return id;
    }

    @Override
    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }
    
    @Override
    public String toString() {
        return "ErroresBanamex{" + "id=" + id + ", error=" + error + ", descripcion=" + descripcion + ", acción=" + accion + '}';
    }

}
