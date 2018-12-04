package com.becasipn.domain;

import java.math.BigDecimal;

/**
 *
 * @author Tania G. Sánchez
 */
public class AsignacionResumen {

    private String unidadAcademica;
    private String tipoDeBeca;
    private Long alumnosAsignados;
    private Long alumnosNoAsignados;
    private BigDecimal montoPorBeca;
    private BigDecimal presupuestoAsignado;

    public String getUnidadAcademica() {
        return unidadAcademica;
    }

    public void setUnidadAcademica(String unidadAcademica) {
        this.unidadAcademica = unidadAcademica;
    }

    public String getTipoDeBeca() {
        return tipoDeBeca;
    }

    public void setTipoDeBeca(String tipoDeBeca) {
        this.tipoDeBeca = tipoDeBeca;
    }

    public Long getAlumnosAsignados() {
        return alumnosAsignados;
    }

    public void setAlumnosAsignados(Long alumnosAsignados) {
        this.alumnosAsignados = alumnosAsignados;
    }

    public Long getAlumnosNoAsignados() {
        return alumnosNoAsignados;
    }

    public void setAlumnosNoAsignados(Long alumnosNoAsignados) {
        this.alumnosNoAsignados = alumnosNoAsignados;
    }

    public BigDecimal getMontoPorBeca() {
        return montoPorBeca;
    }

    public void setMontoPorBeca(BigDecimal montoPorBeca) {
        this.montoPorBeca = montoPorBeca;
    }

    public BigDecimal getPresupuestoAsignado() {
        return presupuestoAsignado;
    }

    public void setPresupuestoAsignado(BigDecimal presupuestoAsignado) {
        this.presupuestoAsignado = presupuestoAsignado;
    }

    @Override
    public String toString() {
        return "AsignacionResumen{" + "unidadAcademica=" + unidadAcademica + ", tipoDeBeca=" + tipoDeBeca + ", alumnosAsignados=" + alumnosAsignados + ", alumnosNoAsignados=" + alumnosNoAsignados + ", montoPorBeca=" + montoPorBeca + ", presupuestoAsignado=" + presupuestoAsignado + '}';
    }
}
