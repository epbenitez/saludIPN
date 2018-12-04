package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import javax.persistence.Table;

/**
 *
 * @author Augusto H.
 */

@Entity
@Table(name = "cat_tipo_notificacion")
public class TipoNotificacion implements BaseEntity, Serializable, Comparable<TipoNotificacion>{

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(generator = "SEQ_CAT_TIPO_NOTIFICA")
    @SequenceGenerator(name = "SEQ_CAT_TIPO_NOTIFICA", sequenceName = "SEQ_CAT_TIPO_NOTIFICA", allocationSize = 1)
        
        private BigDecimal id;
        private String Nombre;
        private String Color;
        private String Icono;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String Color) {
        this.Color = Color;
    }

    public String getIcono() {
        return Icono;
    }

    public void setIcono(String Icono) {
        this.Icono = Icono;
    }

    @Override
    public String toString() {
        return "TipoNotificacion{" + "id=" + id + ", Nombre=" + Nombre + ", Color=" + Color + ", Icono=" + Icono + '}';
    }          

    @Override
    public int compareTo(TipoNotificacion o) {
        //ascending order
        return o.getId().compareTo(id);
    }

}
