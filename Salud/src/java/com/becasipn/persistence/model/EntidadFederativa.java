package com.becasipn.persistence.model;

import com.becasipn.util.ElementoGeografico;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "cat_estado")
public class EntidadFederativa extends ElementoGeografico<MunicipioDelegacion>
        implements Serializable, Comparable<EntidadFederativa>, BaseEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "catEstadoSeq")
    @SequenceGenerator(name = "catEstadoSeq", sequenceName = "cat_estado_id_seq", allocationSize = 1)
    private BigDecimal id;
    private String nombre;
    private String clave;

    public EntidadFederativa() {
    }

    public EntidadFederativa(BigDecimal id) {
        this.id = id;
    }

    /**
     * Obtiene el valor de la variable id
     *
     * @return el valor de la variable id
     */
    @Override
    public BigDecimal getId() {
        return id;
    }

    /**
     * Establece el valor de la variable id
     *
     * @param id nuevo valor de la variable id
     */
    @Override
    public void setId(BigDecimal id) {
        this.id = id;
    }

    /**
     * Obtiene el valor de la variable nombre
     *
     * @return el valor de la variable nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el valor de la variable nombre
     *
     * @param nombre nuevo valor de la variable nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EntidadFederativa other = (EntidadFederativa) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.nombre == null) ? (other.nombre != null) : !this.nombre.equals(other.nombre)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 89 * hash + (this.nombre != null ? this.nombre.hashCode() : 0);
        return hash;
    }

    @Override
    public int compareTo(EntidadFederativa obj) {
//        int compareValue = 0;
//        if (this.getId() < obj.getId()) {
//            compareValue = -1;
//        } else if (this.getId() > obj.getId()) {
//            compareValue = 1;
//        }
//        //Trato especial de  EntidadFederativa
//        if (this.getId().intValue() == 9) {
//            compareValue = -1;
//        } else if (obj.getId().intValue() == 9) {
//            compareValue = 1;
//        } else if (this.getId().intValue() == 15) {
//            compareValue = -1;
//        } else if (obj.getId().intValue() == 15) {
//            compareValue = 1;
//        }
//        //Si son iguales
//        if (this.getId().intValue() == obj.getId().intValue()) {
//            compareValue = 0;
//        }
//        return compareValue;
        String nombreTmp = nombre == null ? null : nombre.toLowerCase();
        String objTmp = obj.getNombre() == null ? null : obj.getNombre().toLowerCase();

        return nombreTmp.compareTo(objTmp);
    }

    @Override
    public String toString() {
        return "EntidadFederativa{" + id + "," + nombre + '}';
    }
}
