package com.becasipn.persistence.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Gustavo A. Alamillo
 */
public class Notificacion implements Comparable<Notificacion> {

    private final SimpleDateFormat formater = new SimpleDateFormat("EEEE d 'de' MMMM, yyyy - hh:mm a");
    private final SimpleDateFormat shortFormater = new SimpleDateFormat("dd/MM/yy");

    private String mensaje;
    private Date fecha;
    private Tipo tipo;

    public Notificacion() {
    }

    public Notificacion(String mensaje, Date fecha, Tipo tipo) {
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.tipo = tipo;
    }
    
    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getFechaString() {
        String format = formater.format(fecha);
        return format.substring(0, 1).toUpperCase().concat(format.substring(1, format.length()));
    }
    
    public String getFechaCorta() {
        String format = shortFormater.format(fecha);
        return format.substring(0, 1).toUpperCase().concat(format.substring(1, format.length()));
    }

    /**
     * Compara dos objetos Notificacion, por fecha y después por mensaje.
     *
     * @author Mario Márquez
     * @param otraNotificacion
     * @return List<Depositos>
     */
    @Override
    public int compareTo(Notificacion otraNotificacion) {
        if (this.fecha.compareTo(otraNotificacion.getFecha()) == 0) {
            return this.mensaje.compareTo(otraNotificacion.getMensaje());
        } else {
            return this.fecha.compareTo(otraNotificacion.getFecha());
        }
        
    }

    public enum Tipo {
        ACTIVAR,
        DEPOSITO,
        ERRORDEPOSITO,
        ERRORCONFIG,
        ENTREGA,
        VACIA
    }

}
