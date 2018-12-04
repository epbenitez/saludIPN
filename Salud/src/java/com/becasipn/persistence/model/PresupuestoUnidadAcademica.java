/**
 * SISTEMA INFORMÁTICO DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE
 * SERVICIOS ESTUDIANTILES 2015
 *
 */
package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.eclipse.persistence.annotations.Cache;

/**
 *
 * @author Tania G. Sánchez
 */
@Entity
@Table(name = "ent_presupuesto_unidadacademic")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class PresupuestoUnidadAcademica implements Serializable, BaseBitacora, BaseEntity {

    @Id
    @GeneratedValue(generator = "entPresupuestoUnidadacademicSeq")
    @SequenceGenerator(name = "entPresupuestoUnidadacademicSeq", sequenceName = "ent_presupuesto_unidadacadem_1", allocationSize = 1)
    private BigDecimal id;
    @ManyToOne(fetch = FetchType.LAZY)
    PresupuestoTipoBecaPeriodo presupuestoTipoBecaPeriodo;
    @ManyToOne(fetch = FetchType.LAZY)
    private UnidadAcademica unidadAcademica;
    private BigDecimal montoPresupuestado;
    private Usuario usuarioModifico;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaModificacion;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public PresupuestoTipoBecaPeriodo getPresupuestoTipoBecaPeriodo() {
        return presupuestoTipoBecaPeriodo;
    }

    public void setPresupuestoTipoBecaPeriodo(PresupuestoTipoBecaPeriodo presupuestoTipoBecaPeriodo) {
        this.presupuestoTipoBecaPeriodo = presupuestoTipoBecaPeriodo;
    }

    public UnidadAcademica getUnidadAcademica() {
        return unidadAcademica;
    }

    public void setUnidadAcademica(UnidadAcademica unidadAcademica) {
        this.unidadAcademica = unidadAcademica;
    }

    public BigDecimal getMontoPresupuestado() {
        return montoPresupuestado;
    }

    public void setMontoPresupuestado(BigDecimal montoPresupuestado) {
        this.montoPresupuestado = montoPresupuestado;
    }

    public Usuario getUsuarioModifico() {
        return usuarioModifico;
    }

    public void setUsuarioModifico(Usuario usuarioModifico) {
        this.usuarioModifico = usuarioModifico;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    @Override
    public String toString() {
        return "PresupuestoUnidadAcademica{" + "id=" + id + ", presupuestoTipoBecaPeriodo=" + (presupuestoTipoBecaPeriodo == null ? "" : presupuestoTipoBecaPeriodo.getId()) + ", unidadAcademica=" + (unidadAcademica == null ? "" : unidadAcademica.getNombreCorto()) + ", montoPresupuestado=" + montoPresupuestado + ", usuarioModifico=" + (usuarioModifico == null ? "" : usuarioModifico.getUsuario()) + ", fechaModificacion=" + fechaModificacion + '}';
    }

}
