package com.becasipn.persistence.model;

/**
 *
 * @author Gustavo A. Alamillo
 */
public class Estadistica {

    public Tipo tipo;
    public String nombre;
    public long total;
    public long totalHombre;
    public long totalMujer;
    public long becarios;

    private long ax1;
    private long ax2;
    private long ax3;
    private long ax4;
    private long ax5;
    private long ax6;
    private long ax7;
    private long ax8;
    private long ax9;
    private long ax10;
    private long ax11;

    public Estadistica() {
    }

    public Estadistica(Tipo tipo, String nombre, Long total, Long becarios, Long totalHombre, Long totalMujer) {
        this.tipo = tipo;
        nombre = checkName(nombre);
        this.nombre = nombre;
        this.total = total;
        this.becarios = becarios;
        this.totalHombre = totalHombre;
        this.totalMujer = totalMujer;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getBecarios() {
        return becarios;
    }

    public void setBecarios(Long becarios) {
        this.becarios = becarios;
    }

    public Long getTotalHombre() {
        return totalHombre;
    }

    public void setTotalHombre(Long totalHombre) {
        this.totalHombre = totalHombre;
    }

    public Long getTotalMujer() {
        return totalMujer;
    }

    public void setTotalMujer(Long totalMujer) {
        this.totalMujer = totalMujer;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        nombre = checkName(nombre);
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "[" + tipo + "] " + nombre + " - Total: " + total + "\t(" + totalMujer + "," + totalHombre + ")";
    }

    private String checkName(String nameCandidate) {

        if (nameCandidate.contains("Proyecto Beca")) {
            nameCandidate = "Proyecto \n transporte IPN";
            return nameCandidate;
        }

        if (nameCandidate.length() > 10 && nameCandidate.contains(" ")) {
            int ordinalIndexOf = ordinalIndexOf(nameCandidate, " ", 2);
            if (ordinalIndexOf >= 0) {
                nameCandidate = nameCandidate.substring(0, ordinalIndexOf).concat("\n").concat(nameCandidate.substring(ordinalIndexOf));
            } else {
                ordinalIndexOf = ordinalIndexOf(nameCandidate, " ", 1);
                if (ordinalIndexOf >= 0) {
                    nameCandidate = nameCandidate.substring(0, ordinalIndexOf).concat("\n").concat(nameCandidate.substring(ordinalIndexOf));
                }
            }
        }

        return nameCandidate;
    }

    public Long getAx1() {
        return ax1;
    }

    public void setAx1(Long ax1) {
        this.ax1 = ax1;
    }

    public Long getAx2() {
        return ax2;
    }

    public void setAx2(Long ax2) {
        this.ax2 = ax2;
    }

    public Long getAx3() {
        return ax3;
    }

    public void setAx3(Long ax3) {
        this.ax3 = ax3;
    }

    public Long getAx4() {
        return ax4;
    }

    public void setAx4(Long ax4) {
        this.ax4 = ax4;
    }

    public Long getAx5() {
        return ax5;
    }

    public void setAx5(Long ax5) {
        this.ax5 = ax5;
    }

    public Long getAx6() {
        return ax6;
    }

    public void setAx6(Long ax6) {
        this.ax6 = ax6;
    }

    public Long getAx7() {
        return ax7;
    }

    public void setAx7(Long ax7) {
        this.ax7 = ax7;
    }

    public Long getAx8() {
        return ax8;
    }

    public void setAx8(Long ax8) {
        this.ax8 = ax8;
    }

    public Long getAx9() {
        return ax9;
    }

    public void setAx9(Long ax9) {
        this.ax9 = ax9;
    }

    public Long getAx10() {
        return ax10;
    }

    public void setAx10(Long ax10) {
        if (ax10 > 0) {
            this.ax10 = ax10;
        }
    }

    public Long getAx11() {
        return ax11;
    }

    public void setAx11(Long ax11) {
        if (ax11 > 0) {
            this.ax11 = ax11;
        }
    }
    
    

    public static int ordinalIndexOf(String str, String substr, int n) {
        int pos = str.indexOf(substr);
        while (--n > 0 && pos != -1) {
            pos = str.indexOf(substr, pos + 1);
        }
        return pos;
    }

    public void setAx(int place, Long info) {
        switch (place) {
            case 1:
                ax1 = info;
                break;
            case 2:
                ax2 = info;
                break;
            case 3:
                ax3 = info;
                break;
            case 4:
                ax4 = info;
                break;
            case 5:
                ax5 = info;
                break;
            case 6:
                ax6 = info;
                break;
            case 7:
                ax7 = info;
                break;
            case 8:
                ax8 = info;
                break;
            case 9:
                ax9 = info;
                break;
            case 10:
                ax10 = info;
                break;
            case 11:
                ax11 = info;
                break;
        }
    }

    public enum Tipo {

        GENERO,
        MOVIMIENTOS,
        PROGRAMA_BECA,
        PROGRAMA_BECAB,
        SEMESTRES,
        PROMEDIOS,
        CARRERA,
        OTORGAMIENTOS,
        REGISTRO,
        BAJAS,
        GENEROB,
        GENEROP,
        PROMEDIOB,
        DEPOSITOS
    }

}
