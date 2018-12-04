package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.Cache;

/**
 *
 * @author Tania G. Sánchez
 */
@Entity
@Table(name = "rmm_transporte_traslado")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class TransporteTraslado implements Serializable, BaseEntity {
    @Id
    @GeneratedValue(generator = "TranTrasSeq")
    @SequenceGenerator(name = "TranTrasSeq", sequenceName = "RMM_TRANSP_TRASL_ID_SEQ", allocationSize = 1)
    private BigDecimal id;
    @ManyToOne
    private CuestionarioTransporte cuestionarioTransporte;
    @ManyToOne
    private Transporte transporte;
    private String puntopartida;
    private String puntollegada;
    private Float costo;
    @ManyToOne
    private Trayecto trayecto;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public CuestionarioTransporte getCuestionarioTransporte() {
        return cuestionarioTransporte;
    }

    public void setCuestionarioTransporte(CuestionarioTransporte cuestionarioTransporte) {
        this.cuestionarioTransporte = cuestionarioTransporte;
    }

    public Transporte getTransporte() {
        return transporte;
    }

    public void setTransporte(Transporte transporte) {
        this.transporte = transporte;
    }

    public String getPuntopartida() {
        return puntopartida;
    }

    public void setPuntopartida(String puntopartida) {
        this.puntopartida = puntopartida;
    }

    public String getPuntollegada() {
        return puntollegada;
    }

    public void setPuntollegada(String puntollegada) {
        this.puntollegada = puntollegada;
    }

    public Float getCosto() {
        return costo;
    }

    public void setCosto(Float costo) {
        this.costo = costo;
    }

    public Trayecto getTrayecto() {
        return trayecto;
    }

    public void setTrayecto(Trayecto trayecto) {
        this.trayecto = trayecto;
    }

    @Override
    public String toString() {
        return "TransporteTraslado{" + "id=" + id + ", cuestionarioTransporte=" + cuestionarioTransporte + ", transporte=" + transporte + ", puntopartida=" + puntopartida + ", puntollegada=" + puntollegada + ", costo=" + costo + ", trayecto=" + trayecto + '}';
    }
}