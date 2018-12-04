package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.Cuestionario;
import com.becasipn.persistence.model.CuestionarioTransporte;
import com.becasipn.persistence.model.Periodo;
import java.math.BigDecimal;

import com.becasipn.persistence.model.SolicitudBeca;
import com.becasipn.persistence.model.TipoBecaPeriodo;
import com.becasipn.persistence.model.Usuario;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez
 */
public interface SolicitudBecaDao extends DaoBase<SolicitudBeca, BigDecimal> {

    public Boolean exiteESEPeriodoActivo(BigDecimal alumnoId, BigDecimal cuestionario, BigDecimal periodoId);

    public SolicitudBeca getESEAlumno(BigDecimal alumnoId, BigDecimal periodoId);

    public BigDecimal totalAlumnosCuestionarioCompleto(BigDecimal unidadAcademicaId, BigDecimal cuestionarioId);

    public String totalAlumnosCuestionarioCompletoD(BigDecimal nivelId, BigDecimal cuestionarioId);

    public List<Object[]> totalAlumnosCuestionarioCompletoD(BigDecimal cuestionarioId);

    public Boolean contestoEncuestaSalud(BigDecimal alumnoId, BigDecimal periodoId);

    public void eliminarFinalizarESEporAlumno(BigDecimal cuestionarioId, BigDecimal periodoId, BigDecimal alumnoId);

    public int getCountCandidatosBecaSolicitada(Periodo periodoActivo, Integer nivel);

    public Long getCountCandidatosPorBecaSolicitada(BigDecimal unidadID, TipoBecaPeriodo tipoBecaPeriodo, Integer nivel, BigDecimal periodoActual);

    public List<SolicitudBeca> getCandidatosBecaSolicitada(BigDecimal unidadID, TipoBecaPeriodo tipoBecaPeriodo, Integer nivel, BigDecimal periodoActual);

    public BigDecimal totalAlumnosEstatusSolicitud(BigDecimal unidadAcademicaId, BigDecimal estatusSolicitud, Boolean manutencion);

    public BigDecimal totalAlumnosSolicitudPeriodoEstatusNivelUA(BigDecimal periodoId, BigDecimal periodoAnteriorId, BigDecimal estatusSolicitud, BigDecimal nivelId, BigDecimal unidadAcademicaId, boolean sobreEscribir);

    public BigDecimal totalAlumnosConSolicitud(BigDecimal unidadAcademicaId, BigDecimal tipoSolicitud);

    public List<Object[]> alumnosEstatusSolicitud(BigDecimal unidadAcademicaId, BigDecimal estatusSolicitud, BigDecimal periodoId);

    public List<Object[]> alumnosEstatusSolicitudPendiente(BigDecimal unidadAcademicaId, BigDecimal periodoId);

    public List<Object[]> alumnosEstatusSolicitudT(BigDecimal unidadAcademicaId, BigDecimal periodoId);

    public BigDecimal totalAlumnosPreAsignados(BigDecimal nivelId, BigDecimal unidadAcademicaId);

    public String totalAlumnosEstatusSolicitudD(BigDecimal nivelId, BigDecimal estatusSolicitud);

    public String totalAlumnosConSolicitudD(BigDecimal nivelId, BigDecimal tipoSolicitud);

    public List<SolicitudBeca> getSolicitudesPreasignacionMasiva(BigDecimal periodoId, BigDecimal periodoAnteriorId, BigDecimal nivelId, BigDecimal unidadAcademicaId, boolean sobreEscribir);

    public List<SolicitudBeca> getSolicitudesPorAlumno(BigDecimal alumnoId, BigDecimal periodoId);

    public int borradoPreasignacionMasiva(BigDecimal periodoId, BigDecimal periodoAnteriorId, BigDecimal nivelId, BigDecimal unidadAcademicaId, boolean sobreEscribir);

    public BigDecimal totalAlumnosEstatusPendiente(BigDecimal unidadAcademicaId, BigDecimal estatusSolicitud);

    public String totalAlumnosBecaPendienteManutencion(BigDecimal nivelId);

    public String totalAlumnosBecaPendienteDiferenteManutencion(BigDecimal nivelId);

    public Boolean tieneESECompleto(BigDecimal alumnoId, BigDecimal periodoId);

    public Boolean tieneSolicitudTransporteAlumno(BigDecimal alumnoId, BigDecimal periodoId);

    public List<SolicitudBeca> getSolicitudesSinReconsideracionAlumno(BigDecimal alumnoId, BigDecimal periodoId);

    //public List<SolicitudBeca> getSolicitudesValidacionInscripcion(BigDecimal periodoActualId, BigDecimal periodoAnteriorId);
    public int[] getSolicitudesValidacionInscripcion(BigDecimal periodoActualId, BigDecimal periodoAnteriorId, HashMap<BigDecimal, BigDecimal> map);

    /**
     * Crea una lista de arreglos objeto, que contiene la información para el
     * reporte excel de solicitudes. Cada decode o nvl del select, se arma por
     * separado.
     *
     * @author Mario Márquez
     * @param periodoId BigDecimal
     * @param nivelId BigDecimal
     * @param uAId BigDecimal
     * @param upper String Fecha límite superior
     * @param lower String Fecha límite inferior
     * @return List<Object[]> Lista de objetos
     */
    public List<Object[]> reporteSolicitudes(BigDecimal periodoId, BigDecimal nivelId, BigDecimal uAId, String upper, String lower);

    public boolean guardasolicitudbecatransporte(CuestionarioTransporte becaTransporte, Cuestionario cuestionario, Usuario usuario, Periodo periodo, String transporte, String puntopartida, String puntollegada, String costo, String trayecto, String nombrefamiliar, String parentesco, String edad, String ocupacion, String aportacionmensual, Alumno alumno);

    /**
     * Crea una lista de arreglos objeto, que contiene la información para la
     * gráfica de solicitudes de beca preasignadas en
     * /reporteEstadisticaSolicitudes.action
     *
     * @author Augusto H.
     * @param periodoId BigDecimal
     * @param uaId
     * @param nivelId BigDecimal
     * @param fechaInicial
     * @param fechaFinal
     * @return List<Object[]> Lista de objetos
     */
    public List<Object[]> estadisticaPreasignadasData(BigDecimal periodoId, BigDecimal uaId, BigDecimal nivelId, String fechaInicial, String fechaFinal);

    /**
     * Crea una lista de arreglos objeto, que contiene la información para la
     * gráfica de solicitudes de beca solicitadas en
     * /reporteEstadisticaSolicitudes.action
     *
     * @author Augusto H.
     * @param periodoId BigDecimal
     * @param nivelId BigDecimal
     * @return List<Object[]> Lista de objetos
     */
    public List<Object[]> estadisticaSolicitadasData(BigDecimal periodoId, BigDecimal uaId, BigDecimal nivelId, String fechaInicial, String fechaFinal);

}
