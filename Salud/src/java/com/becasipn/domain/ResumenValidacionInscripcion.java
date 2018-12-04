
package com.becasipn.domain;

import com.becasipn.persistence.model.Alumno;

/**
 * 
 * @author Rafael Cárdenas Resendiz
 */
public class ResumenValidacionInscripcion {
    
    private Alumno alumno;
    private String error;

    public ResumenValidacionInscripcion(Alumno alumno, String error) {
        this.alumno = alumno;
        this.error = error;
    }

    public ResumenValidacionInscripcion() {
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
    
}
