package com.becasipn.business;

import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.service.Service;

/**
 *
 * @author SISTEMAS
 */
public class AdministraBecaTransporteBO extends BaseBO {
    
    private AdministraBecaTransporteBO (Service service) {
        super(service);
    }
    
    public static AdministraBecaTransporteBO getInstance(Service service) {
        return new AdministraBecaTransporteBO(service);
    }
    
    /**
     * Si el alumno tuvo beca en el periodo anterior, y el periodo actual
     * es par, regresa true
     * Author: Mario Márquez
     *
     * @param boleta boleta del alumno String
     * @return result Boolean Prevalece o no
     */
    public Boolean laSolicitudPrevalece(String boleta) {
        Boolean result = false;        
        PeriodoBO pBo = new PeriodoBO(service);
        Periodo currentTerm = pBo.getPeriodoActivo();
        // Si es par
        if (pBo.esPar(currentTerm)) {
            Alumno alumno = service.getAlumnoDao().findByBoleta(boleta);
            Periodo lastTerm = currentTerm.getPeriodoAnterior();
            // Si tuvo otorgamientos
            if (service.getOtorgamientoDao().tieneOtorgamientoPeriodo(alumno.getId(), lastTerm.getId(), true)) {
                result = true;
            }
        }
        
        return result;
    }
}
