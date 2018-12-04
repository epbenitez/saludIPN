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
@Table(name = "ent_solicitud_becas")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class SolicitudBeca implements Serializable, BaseEntity, BaseBitacora {

    @Id
    @GeneratedValue(generator = "SEQ_SOLICITUD_BECAS")
    @SequenceGenerator(name = "SEQ_SOLICITUD_BECAS", sequenceName = "SEQ_SOLICITUD_BECAS", allocationSize = 1)
    private BigDecimal id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Alumno alumno;
    @ManyToOne(fetch = FetchType.LAZY)
    private Cuestionario cuestionario;
    @ManyToOne(fetch = FetchType.LAZY)
    private Periodo periodo;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @ManyToOne(fetch = FetchType.LAZY)
    private ClasificacionSolicitud clasificacionSolicitud;
    @ManyToOne(fetch = FetchType.LAZY)
    private MotivoRechazoSolicitud motivoRechazo;
    @ManyToOne(fetch = FetchType.LAZY)
    private Proceso proceso;
    @ManyToOne(fetch = FetchType.LAZY)
    private Beca programaBecaSolicitada;
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoBecaPeriodo tipoBecaPreasignada;
    private Integer permiteTransferencia;
    private Boolean finalizado;
    @ManyToOne(fetch = FetchType.LAZY)
    private MotivoRechazoSolicitud rechazoBecaSolicitada;
    private Float ingresosPercapitaPesos;
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuario;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @ManyToOne(fetch = FetchType.LAZY)
    private MotivoRechazoSolicitud rechazoTransferencia;
    private Boolean vulnerabilidadSubes;
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoBecaPeriodo tipoBecaPreasignadaOriginal;
    private float ingresoTotalMensual;
    private int numeroDeIntegrantes;
    private String gastoEnTransporte;

    @Override
    public BigDecimal getId() {
        return id;
    }

    @Override
    public void setId(BigDecimal id) {
        this.id = id;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Cuestionario getCuestionario() {
        return cuestionario;
    }

    public void setCuestionario(Cuestionario cuestionario) {
        this.cuestionario = cuestionario;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public ClasificacionSolicitud getClasificacionSolicitud() {
        return clasificacionSolicitud;
    }

    public void setClasificacionSolicitud(ClasificacionSolicitud clasificacionSolicitud) {
        this.clasificacionSolicitud = clasificacionSolicitud;
    }

    public MotivoRechazoSolicitud getMotivoRechazo() {
        return motivoRechazo;
    }

    public void setMotivoRechazo(MotivoRechazoSolicitud motivoRechazo) {
        this.motivoRechazo = motivoRechazo;
    }

    public Proceso getProceso() {
        return proceso;
    }

    public void setProceso(Proceso proceso) {
        this.proceso = proceso;
    }

    public Beca getProgramaBecaSolicitada() {
        return programaBecaSolicitada;
    }

    public void setProgramaBecaSolicitada(Beca programaBecaSolicitada) {
        this.programaBecaSolicitada = programaBecaSolicitada;
    }

    public TipoBecaPeriodo getTipoBecaPreasignada() {
        return tipoBecaPreasignada;
    }

    public void setTipoBecaPreasignada(TipoBecaPeriodo tipoBecaPreasignada) {
        this.tipoBecaPreasignada = tipoBecaPreasignada;
    }

    public Integer getPermiteTransferencia() {
        return permiteTransferencia;
    }

    public void setPermiteTransferencia(Integer permiteTransferencia) {
        this.permiteTransferencia = permiteTransferencia;
    }

    public Boolean getFinalizado() {
        return finalizado;
    }

    public void setFinalizado(Boolean finalizado) {
        this.finalizado = finalizado;
    }

    public MotivoRechazoSolicitud getRechazoBecaSolicitada() {
        return rechazoBecaSolicitada;
    }

    public void setRechazoBecaSolicitada(MotivoRechazoSolicitud rechazoBecaSolicitada) {
        this.rechazoBecaSolicitada = rechazoBecaSolicitada;
    }

    public Float getIngresosPercapitaPesos() {
        return ingresosPercapitaPesos;
    }

    public void setIngresosPercapitaPesos(Float ingresosPercapitaPesos) {
        this.ingresosPercapitaPesos = ingresosPercapitaPesos;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public MotivoRechazoSolicitud getRechazoTransferencia() {
        return rechazoTransferencia;
    }

    public void setRechazoTransferencia(MotivoRechazoSolicitud rechazoTransferencia) {
        this.rechazoTransferencia = rechazoTransferencia;
    }

    public Boolean getVulnerabilidadSubes() {
        return vulnerabilidadSubes;
    }

    public void setVulnerabilidadSubes(Boolean vulnerabilidadSubes) {
        this.vulnerabilidadSubes = vulnerabilidadSubes;
    }

    public TipoBecaPeriodo getTipoBecaPreasignadaOriginal() {
        return tipoBecaPreasignadaOriginal;
    }

    public void setTipoBecaPreasignadaOriginal(TipoBecaPeriodo tipoBecaPreasignadaOriginal) {
        this.tipoBecaPreasignadaOriginal = tipoBecaPreasignadaOriginal;
    }

    public float getIngresoTotalMensual() {
        return ingresoTotalMensual;
    }

    public void setIngresoTotalMensual(float ingresoTotalMensual) {
        this.ingresoTotalMensual = ingresoTotalMensual;
    }

    public int getNumeroDeIntegrantes() {
        return numeroDeIntegrantes;
    }

    public void setNumeroDeIntegrantes(int numeroDeIntegrantes) {
        this.numeroDeIntegrantes = numeroDeIntegrantes;
    }

    public String getGastoEnTransporte() {
        return gastoEnTransporte;
    }

    public void setGastoEnTransporte(String gastoEnTransporte) {
        this.gastoEnTransporte = gastoEnTransporte;
    }
    
    @Override
    public String toString() {
        return "SolicitudBeca{" + "id=" + id + ", alumno=" + alumno + ", cuestionario=" + cuestionario + ", periodo=" + periodo + ", fechaModificacion=" + fechaModificacion + ", clasificacionSolicitud=" + clasificacionSolicitud + ", motivoRechazo=" + motivoRechazo + ", proceso=" + proceso + ", programaBecaSolicitada=" + programaBecaSolicitada + ", tipoBecaPreasignada=" + tipoBecaPreasignada + ", permiteTransferencia=" + permiteTransferencia + ", finalizado=" + finalizado + ", rechazoBecaSolicitada=" + rechazoBecaSolicitada + ", ingresosPercapitaPesos=" + ingresosPercapitaPesos + ", usuario=" + usuario + ", fechaIngreso=" + fechaIngreso + ", rechazoTransferencia=" + rechazoTransferencia + ", vulnerabilidadSubes=" + vulnerabilidadSubes + ", tipoBecaPreasignadaOriginal=" + tipoBecaPreasignadaOriginal + "ingresoTotalMensual, =" + ingresoTotalMensual + "numeroDeIntegrantes, =" + numeroDeIntegrantes + "gastoEnTransporte, =" + gastoEnTransporte + '}';
    }

}
