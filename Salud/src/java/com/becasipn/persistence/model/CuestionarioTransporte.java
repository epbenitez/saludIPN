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
@Table(name = "ent_cuestionario_transporte")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class CuestionarioTransporte implements Serializable, BaseEntity {
    @Id
    @GeneratedValue(generator = "TranSeq")
    @SequenceGenerator(name = "TranSeq", sequenceName = "CAT_CUESTIONARIO_TRANSP_ID_SEQ", allocationSize = 1)
    private BigDecimal id;
    private Integer enfermedadCronica;
    @ManyToOne
    private Discapacidad discapacidad;
    private Integer familiarenfermcronica;
    private Integer cambioresidencia;
    @ManyToOne
    private EnteroBeca enteroBeca;
    private Float alimentacion;
    private Float renta;
    private Float vivienda;
    private Float escolares;
    private Float salud;
    private Float transporte;
    private Float actividades;
    private Float otros;
    private String urldocumento;
    @ManyToOne
    private Cuestionario cuestionario;
    @ManyToOne
    private Usuario usuario;
    @ManyToOne
    private Periodo periodo;

    @Override
    public BigDecimal getId() {
        return id;
    }

    @Override
    public void setId(BigDecimal id) {
        this.id = id;
    }

    public Integer getEnfermedadCronica() {
        return enfermedadCronica;
    }

    public void setEnfermedadCronica(Integer enfermedadCronica) {
        this.enfermedadCronica = enfermedadCronica;
    }

    public Discapacidad getDiscapacidad() {
        return discapacidad;
    }

    public void setDiscapacidad(Discapacidad discapacidad) {
        this.discapacidad = discapacidad;
    }

    public Integer getFamiliarenfermcronica() {
        return familiarenfermcronica;
    }

    public void setFamiliarenfermcronica(Integer familiarenfermcronica) {
        this.familiarenfermcronica = familiarenfermcronica;
    }

    public Integer getCambioresidencia() {
        return cambioresidencia;
    }

    public void setCambioresidencia(Integer cambioresidencia) {
        this.cambioresidencia = cambioresidencia;
    }

    public EnteroBeca getEnteroBeca() {
        return enteroBeca;
    }

    public void setEnteroBeca(EnteroBeca enteroBeca) {
        this.enteroBeca = enteroBeca;
    }

    public Float getAlimentacion() {
        return alimentacion;
    }

    public void setAlimentacion(Float alimentacion) {
        this.alimentacion = alimentacion;
    }

    public Float getRenta() {
        return renta;
    }

    public void setRenta(Float renta) {
        this.renta = renta;
    }

    public Float getVivienda() {
        return vivienda;
    }

    public void setVivienda(Float vivienda) {
        this.vivienda = vivienda;
    }

    public Float getEscolares() {
        return escolares;
    }

    public void setEscolares(Float escolares) {
        this.escolares = escolares;
    }

    public Float getSalud() {
        return salud;
    }

    public void setSalud(Float salud) {
        this.salud = salud;
    }

    public Float getTransporte() {
        return transporte;
    }

    public void setTransporte(Float transporte) {
        this.transporte = transporte;
    }

    public Float getActividades() {
        return actividades;
    }

    public void setActividades(Float actividades) {
        this.actividades = actividades;
    }

    public Float getOtros() {
        return otros;
    }

    public void setOtros(Float otros) {
        this.otros = otros;
    }

    public String getUrldocumento() {
        return urldocumento;
    }

    public void setUrldocumento(String urldocumento) {
        this.urldocumento = urldocumento;
    }

    public Cuestionario getCuestionario() {
        return cuestionario;
    }

    public void setCuestionario(Cuestionario cuestionario) {
        this.cuestionario = cuestionario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    @Override
    public String toString() {
        return "CuestionarioTransporte{" + "id=" + id + ", enfermedadCronica=" + enfermedadCronica + ", discapacidad=" + discapacidad + ", familiarenfermcronica=" + familiarenfermcronica + ", cambioresidencia=" + cambioresidencia + ", enteroBeca=" + enteroBeca + ", alimentacion=" + alimentacion + ", renta=" + renta + ", vivienda=" + vivienda + ", escolares=" + escolares + ", salud=" + salud + ", transporte=" + transporte + ", actividades=" + actividades + ", otros=" + otros + ", urldocumento=" + urldocumento + ", cuestionario=" + cuestionario + ", usuario=" + usuario + ", periodo=" + periodo + '}';
    }
}