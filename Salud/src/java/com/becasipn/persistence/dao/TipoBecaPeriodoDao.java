package com.becasipn.persistence.dao;

import com.becasipn.domain.ParametrosPDF;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.Proceso;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.Beca;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.Nivel;
import com.becasipn.persistence.model.PadronSubes;
import com.becasipn.persistence.model.SolicitudBeca;
import com.becasipn.persistence.model.TipoBeca;
import com.becasipn.persistence.model.TipoBecaPeriodo;
import com.becasipn.persistence.model.UnidadAcademica;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Usre-05
 */
public interface TipoBecaPeriodoDao extends DaoBase<TipoBecaPeriodo, BigDecimal> {

    public List<TipoBecaPeriodo> tipoBecaPeriodoEnUso(BigDecimal tipoBeca);

    public List<TipoBecaPeriodo> tipoBecaPeriodoEnUsoPresupuesto(BigDecimal tipoBeca);

    public List<TipoBecaPeriodo> existenTiposBecaAsociados(BigDecimal periodoId);

    public List<TipoBecaPeriodo> becasAplicables(Alumno alumno, SolicitudBeca solicitud, Boolean tieneUniversal, Boolean tieneComplementaria, Proceso... proceso);

    public TipoBecaPeriodo montoBecaAlumno(BigDecimal alumnoId, BigDecimal periodoId);

    public TipoBecaPeriodo nuevoTipoBecaPeriodo(BigDecimal tipoBecaAnterior, Periodo periodoActivo, Boolean tieneUniversal, Boolean tieneComplementaria, PadronSubes padronSubes);

    public List<TipoBecaPeriodo> becasPorNivelPeriodo(BigDecimal nivelId, BigDecimal periodoId);

    public TipoBecaPeriodo findBecaTransporte(int tipo, Alumno a, String clave, Boolean tieneUniversal, Boolean tieneComplementaria, Proceso proceso, PadronSubes padronSubes);

    public TipoBecaPeriodo siguienteBecaPeriodo(TipoBecaPeriodo tipoBecaAnterior, Boolean tieneUniversal, Boolean tieneComplementaria, PadronSubes padronSubes);

    public TipoBecaPeriodo siguienteBecalosPeriodo(TipoBecaPeriodo tipoBecaAnterior, Alumno a, Boolean tieneUniversal, Boolean tieneComplementaria);

    public Long getCountTiposBecasPeriodoPorNivel(Integer nivel);

    public Long getCountTotalRealesPorBeca(TipoBecaPeriodo tipoBecaPeriodo, Integer nivel, BigDecimal periodoAnteriorId, BigDecimal periodoActual);

    public Long getCountTotalNuevosBeca(TipoBecaPeriodo tipoBecaCicloAnterior, Integer nivel, Periodo periodoActivo, UnidadAcademica ua);

    public TipoBecaPeriodo getBecaAlumnoActual(Alumno a, Beca b, BigDecimal pId, DatosAcademicos da, Boolean tieneUniversal, Boolean tieneComplementaria, PadronSubes padronSubes);

    public TipoBecaPeriodo getBkUniversalPorNivel(Periodo periodo, DatosAcademicos da);

    public List<ParametrosPDF> rangoIngresoPorPersonaPorBeca(BigDecimal periodoId, BigDecimal nivelId);

    public List<String> getBecasConValidacionInscripcion();

    public List<TipoBecaPeriodo> getBecasPorSolicitud(BigDecimal solicitudId);

    public TipoBecaPeriodo getComplementaria(BigDecimal tipoBecaPeriodoNormal);

    public TipoBecaPeriodo getTipoBecaPeriodo(TipoBecaPeriodo anterior, Periodo p);

    /**
     * Devuelve los objetos tipo beca periodo, por periodo, nivel académico y
     * programa de beca.
     *
     * @author Mario Márquez
     * @param programaBeca
     * @param nivel
     * @param periodo
     * @return lista de objetos tipo beca periodo
     */
    public List<TipoBecaPeriodo> becasPorProgramaNivelPeriodo(Beca programaBeca, Nivel nivel, Periodo periodo);

    /**
     * Devuelve los objetos tipo beca periodo, por periodo, nivel académico y
     * programa de beca.
     *
     * @author Mario Márquez
     * @param actual (Periodo)
     * @return lista de objetos con los id de los tipos de beca del periodo
     * anterior y el actual
     */
    public List<Object[]> getTbpActualAnterior(Periodo actual);

    /**
     * Devuelve un objeto tipo beca periodo´por alumno
     *
     * @author Augusto Hernández
     * @param actual (Periodo)
     * @param alumnoId (BigDecimal)
     * @return Objeto con el tipo beca del alumno para el periodo actual
     * anterior y el actual
     */
    public TipoBecaPeriodo becaAlumnoPeriodoActual(Periodo actual, BigDecimal alumnoId);

    /**
     * Devuelve las becas activas para el periodo en curso por Unidad Académica
     *
     * @author Augusto H.
     * @param nivelId
     * @param periodoId
     * @param uaId
     * @return lista
     */
    public List<Beca> becasPorNivelPeriodoUa(BigDecimal nivelId, BigDecimal periodoId, BigDecimal uaId);
}
