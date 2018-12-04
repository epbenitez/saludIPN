package com.becasipn.persistence.model;

import com.becasipn.util.UtilFile;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import javax.persistence.Table;
import javax.persistence.Temporal;

@Entity
@Table(name = "ent_bitacora_general")
public class Bitacora implements BaseEntity, Serializable, Comparable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "ENTBITACORAGENERALSEQ")
    @SequenceGenerator(name = "ENTBITACORAGENERALSEQ", sequenceName = "ENT_BITACORA_GENERAL_ID_SEQ", allocationSize = 1)
//    @GeneratedValue(strategy=GenerationType.SEQUENCE) 
    private BigDecimal id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USUARIO_ID", nullable = false)
    private Usuario usuario;

    private String objectId;

    private String objectName;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaModificacion;

//    @Column(length = 4000)
//    private String descripcionOld;
    @Column(length = 4000)
    private String descripcion;

    private Boolean administrativo;

    private Accion accion;

    /**
     * Obtiene el valor de la variable id
     *
     * @return el valor de la variable id
     */
    public BigDecimal getId() {
        return id;
    }

    /**
     * Establece el valor de la variable id
     *
     * @param id nuevo valor de la variable id
     */
    public void setId(BigDecimal id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Obtiene el valor de la variable objectId
     *
     * @return el valor de la variable objectId
     */
    public String getObjectId() {
        return objectId;
    }

    /**
     * Establece el valor de la variable objectId
     *
     * @param objectId nuevo valor de la variable objectId
     */
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    /**
     * Obtiene el valor de la variable objectName
     *
     * @return el valor de la variable objectName
     */
    public String getObjectName() {
        return objectName;
    }

    /**
     * Establece el valor de la variable objectName
     *
     * @param objectName nuevo valor de la variable objectName
     */
    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    /**
     * Obtiene el valor de la variable fechaModificacion
     *
     * @return el valor de la variable fechaModificacion
     */
    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    /**
     * Establece el valor de la variable fechaModificacion
     *
     * @param fechaModificacion nuevo valor de la variable fechaModificacion
     */
    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene el valor de la variable administrativo
     *
     * @return el valor de la variable administrativo
     */
    public Boolean getAdministrativo() {
        return administrativo;
    }

    /**
     * Establece el valor de la variable administrativo
     *
     * @param administrativo nuevo valor de la variable administrativo
     */
    public void setAdministrativo(Boolean administrativo) {
        this.administrativo = administrativo;
    }

    /**
     * Obtiene el valor de la variable accion
     *
     * @return el valor de la variable accion
     */
    public Accion getAccion() {
        return accion;
    }

    /**
     * Establece el valor de la variable accion
     *
     * @param accion nuevo valor de la variable accion
     */
    public void setAccion(Accion accion) {
        this.accion = accion;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Bitacora other = (Bitacora) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if (this.usuario != other.usuario && (this.usuario == null || !this.usuario.equals(other.usuario))) {
            return false;
        }
        if (this.fechaModificacion != other.fechaModificacion && (this.fechaModificacion == null || !this.fechaModificacion.equals(other.fechaModificacion))) {
            return false;
        }
        if (this.descripcion != other.descripcion && (this.descripcion == null || !this.descripcion.equals(other.descripcion))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + (this.usuario != null ? this.usuario.hashCode() : 0);
        hash = 83 * hash + (this.fechaModificacion != null ? this.fechaModificacion.hashCode() : 0);
        hash = 83 * hash + (this.descripcion != null ? this.descripcion.hashCode() : 0);
        return hash;
    }

    @Override
    public int compareTo(Object o) {
        if (o == null) {
            return 1;
        }
        if ((o instanceof Bitacora)) {
            return 1;
        }
        return (this.equals(o)) ? 0 : 1;
    }

    @Override
    public String toString() {
        return "|" + (id == null ? "" : id)
                + "|" + UtilFile.dateToString(fechaModificacion, "yyyy-MM-dd hh:mm:ss")
                + "|" + (accion == null ? "-" : accion.getId())
                + "|" + descripcion
                + "|" + administrativo
                + "|" + (usuario == null ? "" : usuario.getId())
                + "|" + objectId
                + "|" + objectName;
    }

}
