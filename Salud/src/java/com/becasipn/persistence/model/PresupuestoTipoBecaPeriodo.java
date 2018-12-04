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
@Table(name = "ent_presupuesto_tipobecaperiod")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class PresupuestoTipoBecaPeriodo implements Serializable, BaseBitacora, BaseEntity {

    @Id
    @GeneratedValue(generator = "entPresupuestoTipobecaperiodSeq")
    @SequenceGenerator(name = "entPresupuestoTipobecaperiodSeq", sequenceName = "ent_presupuesto_tipobecaperi_1", allocationSize = 1)
    private BigDecimal id;
    @ManyToOne(fetch = FetchType.LAZY)
    PresupuestoPeriodo presupuestoPeriodo;
    @ManyToOne(fetch = FetchType.LAZY)
    TipoBecaPeriodo tipoBecaPeriodo;
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

    public PresupuestoPeriodo getPresupuestoPeriodo() {
        return presupuestoPeriodo;
    }

    public void setPresupuestoPeriodo(PresupuestoPeriodo presupuestoPeriodo) {
        this.presupuestoPeriodo = presupuestoPeriodo;
    }

    public TipoBecaPeriodo getTipoBecaPeriodo() {
        return tipoBecaPeriodo;
    }

    public void setTipoBecaPeriodo(TipoBecaPeriodo tipoBecaPeriodo) {
        this.tipoBecaPeriodo = tipoBecaPeriodo;
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
        return "PresupuestoTipoBecaPeriodo{" + "id=" + id + ", presupuestoPeriodo=" + (presupuestoPeriodo == null ? "" : presupuestoPeriodo.getId()) + ", tipoBecaPeriodo=" + (tipoBecaPeriodo == null ? "" : tipoBecaPeriodo.getId()) + ", montoPresupuestado=" + montoPresupuestado + ", usuarioModifico=" + (usuarioModifico == null ? "" : usuarioModifico.getUsuario()) + ", fechaModificacion=" + fechaModificacion + '}';
    }

}
