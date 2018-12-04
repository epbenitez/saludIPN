/**
* SISTEMA DE BECAS DEL INSTITUTO POLITECNICO NACIONAL
* DIRECCION DE SERVICIOS ESTUDIANTILES
* 2016
**/

package com.becasipn.domain;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Patricia Benítez
 */
public class IngresosEgresos {
    
    private List<Egresos> egresos = new ArrayList<>();
    private Float ingresosPerCapita;
    private Float totalIntegrantes;
    private Float totalIngresos;
    private String fuenteDeInformacion;
    private Boolean tieneSolicitudOrdinaria = Boolean.FALSE;
    private Boolean tieneSolicitudTransporte = Boolean.FALSE;

    public List<Egresos> getEgresos() {
        return egresos;
    }

    public void setEgresos(List<Egresos> egresos) {
        this.egresos = egresos;
    }

    public Float getIngresosPerCapita() {
        return ingresosPerCapita;
    }

    public void setIngresosPerCapita(Float ingresosPerCapita) {
        this.ingresosPerCapita = ingresosPerCapita;
    }

    public Float getTotalIntegrantes() {
        return totalIntegrantes;
    }

    public void setTotalIntegrantes(Float totalIntegrantes) {
        this.totalIntegrantes = totalIntegrantes;
    }

    public String getFuenteDeInformacion() {
        return fuenteDeInformacion;
    }

    public void setFuenteDeInformacion(String fuenteDeInformacion) {
        this.fuenteDeInformacion = fuenteDeInformacion;
    }

    public Boolean getTieneSolicitudOrdinaria() {
        return tieneSolicitudOrdinaria;
    }

    public void setTieneSolicitudOrdinaria(Boolean tieneSolicitudOrdinaria) {
        this.tieneSolicitudOrdinaria = tieneSolicitudOrdinaria;
    }

    public Boolean getTieneSolicitudTransporte() {
        return tieneSolicitudTransporte;
    }

    public void setTieneSolicitudTransporte(Boolean tieneSolicitudTransporte) {
        this.tieneSolicitudTransporte = tieneSolicitudTransporte;
    }

    public Float getTotalIngresos() {
        return totalIngresos;
    }

    public void setTotalIngresos(Float totalIngresos) {
        this.totalIngresos = totalIngresos;
    }
    
}
