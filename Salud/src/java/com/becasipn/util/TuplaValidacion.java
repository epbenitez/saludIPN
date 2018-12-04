package com.becasipn.util;

import com.becasipn.persistence.model.AlumnoDAE;

/**
 *
 * @author Gustavo A. Alamillo
 */
public class TuplaValidacion {

    private ErrorDAE errorDAE;
    private AlumnoDAE alumnoDAE;

    public TuplaValidacion() {
    }

        
    public ErrorDAE getErrorDAE() {
        return errorDAE;
    }

    public void setErrorDAE(ErrorDAE errorDAE) {
        this.errorDAE = errorDAE;
    }

    public AlumnoDAE getAlumnoDAE() {
        return alumnoDAE;
    }

    public void setAlumnoDAE(AlumnoDAE alumnoDAE) {
        this.alumnoDAE = alumnoDAE;
    }

    @Override
    public int hashCode() {
        String hash = errorDAE.hashCode() + "" + alumnoDAE.hashCode();
        return hash.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TuplaValidacion other = (TuplaValidacion) obj;
        if (!this.errorDAE.equals(other.errorDAE)) {
            return false;
        }
        if (!this.alumnoDAE.equals(other.alumnoDAE)) {
            return false;
        }
        return true;
    }

}
