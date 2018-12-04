package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Augusto I.
 */
@Entity
@Table(name = "rmm_notificaciones_rol")
public class NotificacionesRol implements Serializable, BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "SEQ_NOTIFICA_ROL")
    @SequenceGenerator(name = "SEQ_NOTIFICA_ROL", sequenceName = "SEQ_NOTIFICA_ROL", allocationSize = 1)

    private BigDecimal id;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "NOTIFICACION_ID", referencedColumnName = "ID")
    })
    private Notificaciones notificacion;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "ROL_ID", referencedColumnName = "ID")
    })
    private Rol rol;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public Notificaciones getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(Notificaciones notificacion) {
        this.notificacion = notificacion;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "NotificacionesRol{" + "id=" + id + ", notificacion=" + notificacion + ", rol=" + rol + '}';
    }
    
    

}
