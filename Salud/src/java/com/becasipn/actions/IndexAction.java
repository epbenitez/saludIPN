package com.becasipn.actions;

import com.becasipn.business.AlumnoBO;
import com.becasipn.persistence.model.Alumno;
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
                    //Se obtienen los datos del alumno
                    AlumnoBO bo = new AlumnoBO(getDaos());
                    String numeroDeBoleta = SecurityContextHolder.getContext().getAuthentication().getName();
                    Alumno alumno = getDaos().getAlumnoDao().findByBoleta(numeroDeBoleta);
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
