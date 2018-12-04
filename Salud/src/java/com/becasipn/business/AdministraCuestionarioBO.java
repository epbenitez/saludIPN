package com.becasipn.business;

import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.CicloEscolar;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.service.Service;
import com.opensymphony.xwork2.ActionContext;
import java.math.BigDecimal;

/**
 *
 * @author Mario Márquez
 */
public class AdministraCuestionarioBO extends BaseBO {

    private AdministraCuestionarioBO(Service service) {
        super(service);
    }

    public static AdministraCuestionarioBO getInstance(Service service) {
        return new AdministraCuestionarioBO(service);
    }

    /**
     * Si el alumno tuvo beca en el periodo anterior, y el periodo actual es
     * par, regresa true Author: Mario Márquez
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
            if (service.getOtorgamientoDao().tieneOtorgamientoPeriodo(alumno.getId(), lastTerm.getId(), false)) {
                result = true;
            }
        }

        return result;
    }

    /**
     * Si el alumno tiene beca en el ciclo actual, regresa true Author: Mario
     * Márquez
     *
     * @param alumno
     * @return result Boolean Tiene o no
     */
    public Boolean tieneBecaExterna(Alumno alumno) {
        PeriodoBO pBo = new PeriodoBO(service);
        Periodo currentTerm = pBo.getPeriodoActivo();
        CicloEscolar currentC = currentTerm.getCicloEscolar();

        Boolean result = service.getOtorgamientoExternoDao().tieneOtorgamientoExterno(alumno.getCurp(), currentC.getId());

        return result;
    }

    /**
     * Si el alumno es de nuevo ingreso, y no se les permite a los de nuevo
     * ingreso contestar la solicitud, entonces el alumno debería ser redirigido
     * a hacer una solicitud de manutención Author: Mario Márquez
     *
     * @param alumno
     * @return result Boolean false si no es necesario ser redirigido
     */
    public Boolean deberiaIrPorManutencion(Alumno alumno) {
        String permiteSolicitudNuevoIngreso = (String) ActionContext.getContext().getApplication().get("permiteSolicitudNuevoIngreso");

        Boolean result = false;

        if (!permiteSolicitudNuevoIngreso.equals("true")) {
            PeriodoBO pBo = new PeriodoBO(service);
            Periodo currentTerm = pBo.getPeriodoActivo();
            DatosAcademicos da = service.getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), currentTerm.getId());
            // Si el periodo de ingreso es igual al epriodo actual, entonces es de nuevo ingreso
            if (da != null && da.getPeriodoIngreso() != null) {
                if (da.getPeriodoIngreso().equals(currentTerm)) {
                    result = true;
                }
            }
        }

        return result;
    }
}
