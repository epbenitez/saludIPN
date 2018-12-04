package com.becasipn.persistence.model;

/**
 *
 * @author Augusto H
 */
public class EstadisticaSolicitudes {

    private String nombre;
    private long total;
    private long totalSolicitudes;

    public EstadisticaSolicitudes() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getTotalSolicitudes() {
        return totalSolicitudes;
    }

    public void setTotalSolicitudes(long totalSolicitudes) {
        this.totalSolicitudes = totalSolicitudes;
    }

    @Override
    public String toString() {
        return "EstadisticaSolicitudes{" + "nombre=" + nombre + ", total=" + total + ", totalSolicitudes=" + totalSolicitudes + '}';
    }

}
