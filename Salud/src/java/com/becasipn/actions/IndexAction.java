package com.becasipn.actions;

import com.becasipn.business.AlumnoBO;
import com.becasipn.persistence.model.Alumno;
import com.opensymphony.xwork2.ActionContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class IndexAction extends BaseAction {

    @Override
    public String execute() {
//        super.setVariablesPersonalizadas();
        if (isAuthenticated()) {
            String[] datos = getDatosPersonales();
            if (datos[0] != null) {
                if (!isAlumno()) {
                    //TABLERO DE CONTROL
                    return INPUT;
                }
                if (isAlumno()) {
//                    //Se obtienen los datos del alumno
//                    AlumnoBO bo = new AlumnoBO(getDaos());
//                    String numeroDeBoleta = SecurityContextHolder.getContext().getAuthentication().getName();
//                    Alumno alumno = getDaos().getAlumnoDao().findByBoleta(numeroDeBoleta);
//                        
//                    Boolean periodoActivo = bo.solicitudActiva(alumno, null);
//                    //Obtiene el valor de la variable ValidarDatosDae
//                    String validarDatosDAE = (String) ActionContext.getContext().getApplication().get("ValidarDatosDae");
//                    //Si el periodo esta activo y se pueden validar los datos de la dae entonces procede a actualizar los datos académicos del alumno.
//                    if (periodoActivo && validarDatosDAE.equals("true")) {
//                        //Se checa que el alumno no tenga un otorgamiento en el periodo actual
//                        Boolean tieneOtorgamientoPeriodoActual = getDaos().getOtorgamientoDao().tieneOtorgamientoPeriodoActual(alumno.getId());
//                        if (!tieneOtorgamientoPeriodoActual) {
//                            try {
//                                bo.datosDAE(alumno, alumno.getCurp());
//                            } catch (Exception ex) {
//                                ex.printStackTrace();
//                            }
//                        }
//                    }
                }
                return SUCCESS;
            } else {
                return "sinacceso";
            }
        } else {
            return ERROR;
        }
    }

    public String error403() {
        return ERROR;
    }
}
