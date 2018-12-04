/**
 * SISTEMA INFORMÁTICO DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE
 * SERVICIOS ESTUDIANTILES 2015
 */
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
 * @author Patricia Benitez
 */
@Entity
@Table(name = "ent_tipo_beca_periodo")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class TipoBecaPeriodo implements Serializable, Comparable<TipoBecaPeriodo>, BaseEntity {
    @Id
    @GeneratedValue(generator = "TbpSeq")
    @SequenceGenerator(name = "TbpSeq", sequenceName = "ENT_TIPO_BECA_PERIODO_ID_SEQ", allocationSize = 1)
    private BigDecimal id;
    private BigDecimal monto;
    private Integer regular;
    private Integer estudiosPrevios;
    private Float promedioMinimo;
    private Float promedioMaximo;
    private Integer semestreMinimo;
    private Integer semestreMaximo;
    private Integer numMateriasAprobadas;
    private Integer duracion;
    private Float porcentMateriasAprobadas;
    private Integer recursarMaterias;
    private Integer cumpleCargaMinima;
    private Float porcentCreditos;
    private Float ingresoSalarios;
    private Integer actividadExtra;
    private Integer escasosRecursos;
    private Integer residenteDF;
    private Integer nacionalidad;
    private Integer prioridad;
    private Integer fases;
    private Float ingresoMinimo;
    private Float ingresoMaximo;
    private Float gastotransportemaximo;
    private Float gastotransporteminimo;
    private Boolean montoVariable;
    private Integer validaciondeinscripcion;
    @ManyToOne
    private Nivel nivel;
    @ManyToOne
    private Periodo periodo;
    @ManyToOne
    private Estatus estatus;
    @ManyToOne
    private Modalidad modalidad;
    @ManyToOne
    private AreasConocimiento areasconocimiento;
    @ManyToOne
    private TipoBeca tipoBeca;
    private Boolean montoExpresadoEnDeciles;
    private Boolean vulnerabilidadSubes;
    private Boolean visible;
    private Integer convocatoriaManutencion;
    private Integer reprobadasMinimo;
    private Integer reprobadasMaximo;

    @Override
    public BigDecimal getId() {
        return id;
    }

    @Override
    public void setId(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Integer getRegular() {
        return regular;
    }

    public void setRegular(Integer regular) {
        this.regular = regular;
    }

    public Integer getEstudiosPrevios() {
        return estudiosPrevios;
    }

    public void setEstudiosPrevios(Integer estudiosPrevios) {
        this.estudiosPrevios = estudiosPrevios;
    }

    public Float getPromedioMinimo() {
        return promedioMinimo;
    }

    public void setPromedioMinimo(Float promedioMinimo) {
        this.promedioMinimo = promedioMinimo;
    }

    public Float getPromedioMaximo() {
        return promedioMaximo;
    }

    public void setPromedioMaximo(Float promedioMaximo) {
        this.promedioMaximo = promedioMaximo;
    }

    public Integer getSemestreMinimo() {
        return semestreMinimo;
    }

    public void setSemestreMinimo(Integer semestreMinimo) {
        this.semestreMinimo = semestreMinimo;
    }

    public Integer getSemestreMaximo() {
        return semestreMaximo;
    }

    public void setSemestreMaximo(Integer semestreMaximo) {
        this.semestreMaximo = semestreMaximo;
    }

    public Integer getNumMateriasAprobadas() {
        return numMateriasAprobadas;
    }

    public void setNumMateriasAprobadas(Integer numMateriasAprobadas) {
        this.numMateriasAprobadas = numMateriasAprobadas;
    }

    public Float getPorcentMateriasAprobadas() {
        return porcentMateriasAprobadas;
    }

    public void setPorcentMateriasAprobadas(Float porcentMateriasAprobadas) {
        this.porcentMateriasAprobadas = porcentMateriasAprobadas;
    }

    public Integer getRecursarMaterias() {
        return recursarMaterias;
    }

    public void setRecursarMaterias(Integer recursarMaterias) {
        this.recursarMaterias = recursarMaterias;
    }

    public Integer getCumpleCargaMinima() {
        return cumpleCargaMinima;
    }

    public void setCumpleCargaMinima(Integer cumpleCargaMinima) {
        this.cumpleCargaMinima = cumpleCargaMinima;
    }

    public Float getPorcentCreditos() {
        return porcentCreditos;
    }

    public void setPorcentCreditos(Float porcentCreditos) {
        this.porcentCreditos = porcentCreditos;
    }

    public Float getIngresoSalarios() {
        return ingresoSalarios;
    }

    public void setIngresoSalarios(Float ingresoSalarios) {
        this.ingresoSalarios = ingresoSalarios;
    }

    public Integer getActividadExtra() {
        return actividadExtra;
    }

    public void setActividadExtra(Integer actividadExtra) {
        this.actividadExtra = actividadExtra;
    }

    public Integer getEscasosRecursos() {
        return escasosRecursos;
    }

    public void setEscasosRecursos(Integer escasosRecursos) {
        this.escasosRecursos = escasosRecursos;
    }

    public Integer getResidenteDF() {
        return residenteDF;
    }

    public void setResidenteDF(Integer residenteDF) {
        this.residenteDF = residenteDF;
    }

    public Integer getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(Integer nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public Estatus getEstatus() {
        return estatus;
    }

    public void setEstatus(Estatus estatus) {
        this.estatus = estatus;
    }

    public Integer getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Integer prioridad) {
        this.prioridad = prioridad;
    }

    public Modalidad getModalidad() {
        return modalidad;
    }

    public void setModalidad(Modalidad modalidad) {
        this.modalidad = modalidad;
    }

    public AreasConocimiento getAreasconocimiento() {
        return areasconocimiento;
    }

    public void setAreasconocimiento(AreasConocimiento areasconocimiento) {
        this.areasconocimiento = areasconocimiento;
    }

    public TipoBeca getTipoBeca() {
        return tipoBeca;
    }

    public void setTipoBeca(TipoBeca tipoBeca) {
        this.tipoBeca = tipoBeca;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public Integer getFases() {
        return fases;
    }

    public void setFases(Integer fases) {
        this.fases = fases;
    }

    public Float getIngresoMinimo() {
        return ingresoMinimo;
    }

    public void setIngresoMinimo(Float ingresoMinimo) {
        this.ingresoMinimo = ingresoMinimo;
    }

    public Float getIngresoMaximo() {
        return ingresoMaximo;
    }

    public void setIngresoMaximo(Float ingresoMaximo) {
        this.ingresoMaximo = ingresoMaximo;
    }

    public Float getGastotransportemaximo() {
        return gastotransportemaximo;
    }

    public void setGastotransportemaximo(Float gastotransportemaximo) {
        this.gastotransportemaximo = gastotransportemaximo;
    }

    public Float getGastotransporteminimo() {
        return gastotransporteminimo;
    }

    public void setGastotransporteminimo(Float gastotransporteminimo) {
        this.gastotransporteminimo = gastotransporteminimo;
    }

    public Boolean getMontoVariable() {
        return montoVariable;
    }

    public void setMontoVariable(Boolean montoVariable) {
        this.montoVariable = montoVariable;
    }

    public Integer getValidaciondeinscripcion() {
        return validaciondeinscripcion;
    }

    public void setValidaciondeinscripcion(Integer validaciondeinscripcion) {
        this.validaciondeinscripcion = validaciondeinscripcion;
    }

    public int compareTo(TipoBecaPeriodo obj) {
        String nombreTmp = tipoBeca.getNombre() == null ? null : tipoBeca.getNombre().toLowerCase();
        String objTmp = obj.getTipoBeca().getNombre() == null ? null : obj.getTipoBeca().getNombre().toLowerCase();

        return nombreTmp.compareTo(objTmp);
    }

    public Boolean getMontoExpresadoEnDeciles() {
        return montoExpresadoEnDeciles;
    }

    public void setMontoExpresadoEnDeciles(Boolean montoExpresadoEnDeciles) {
        this.montoExpresadoEnDeciles = montoExpresadoEnDeciles;
    }

    public Boolean getVulnerabilidadSubes() {
        return vulnerabilidadSubes;
    }

    public void setVulnerabilidadSubes(Boolean vulnerabilidadSubes) {
        this.vulnerabilidadSubes = vulnerabilidadSubes;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Integer getConvocatoriaManutencion() {
        return convocatoriaManutencion;
    }

    public void setConvocatoriaManutencion(Integer convocatoriaManutencion) {
        this.convocatoriaManutencion = convocatoriaManutencion;
    }

    public Integer getReprobadasMinimo() {
        return reprobadasMinimo;
    }

    public void setReprobadasMinimo(Integer reprobadasMinimo) {
        this.reprobadasMinimo = reprobadasMinimo;
    }

    public Integer getReprobadasMaximo() {
        return reprobadasMaximo;
    }

    public void setReprobadasMaximo(Integer reprobadasMaximo) {
        this.reprobadasMaximo = reprobadasMaximo;
    }
}