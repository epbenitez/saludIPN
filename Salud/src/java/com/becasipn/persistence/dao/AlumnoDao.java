/**
 * SISTEMA INFORMÁTICO DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE
 * SERVICIOS ESTUDIANTILES 2015
 *
 */
package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.UnidadAcademica;
import com.becasipn.util.PaginateUtil;
import com.becasipn.util.ServerSideUtil;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Patricia Benitez
 */
public interface AlumnoDao extends DaoBase<Alumno, BigDecimal> {

    public boolean documentosCompletos(BigDecimal alumnoId);

    public boolean documentosCompletosPeriodo(BigDecimal alumnoId, BigDecimal periodoId);

    public boolean documentosCompletosManutencion(BigDecimal alumnoId);

    public List<Alumno> asociadaAlumno(BigDecimal unidadAcademicaId);

    public BigDecimal getIdUsuario(BigDecimal idAlumno);

    public List<Alumno> getByBoleta(String noBoleta);

    public Alumno findByBoleta(String boleta);

    public Boolean existeBoleta(String boleta);

    public Boolean existeCurp(String curp);

    public Boolean existeNombre(String nombre, String aPaterno, String aMaterno);

    public Alumno findByBoleta(Map<String, Object> parametros);

    public PaginateUtil findAsignaciones(int tipoProceso, int asignado , ServerSideUtil ssu);

    public PaginateUtil listadoAlumnoNueavasTarjetas(ServerSideUtil ssu);

    public List<Alumno> listadoAlumnoNueavasTarjetas(String boleta, BigDecimal nivel, BigDecimal unidadAcademica);

    public Boolean estatusAlumno(String noBoleta);

    public PaginateUtil solicitudAlumnos(ServerSideUtil ssu);

    public List<Alumno> alumnosRegistrados(String nivel, String unidadAcademica);

    public List<Alumno> alumnosRevalidantesNoRegistrados(String nivel, String unidadAcademica);

    public List<Alumno> alumnosESEincompleto(String nivel, String unidadAcademica);
    
    public List<Alumno> alumnosOtorgamientoTarjetaUA(String nivel, String unidadAcademica);

    public List<Alumno> alumnosOtorgamientoTarjetaEntregada(String nivel, String unidadAcademica);
    
    public List<Alumno> alumnosDatosIncorrectos(String nivel, String unidadAcademica, BigDecimal periodoId);

    public List<Alumno> alumnosFiltros(String nivel, String unidadAcademica, String beca, String tipoBeca, String movimiento, String proceso, String alumnos);

    public List<Alumno> alumnosL(String nivel, String unidadAcademica, String alumnosL);

    public BigDecimal alumnosRegistradosC(String nivel, String unidadAcademica);

    public BigDecimal alumnosRevalidantesNoRegistradosC(String nivel, String unidadAcademica);

    public BigDecimal alumnosESEincompletoC(String nivel, String unidadAcademica);

    public BigDecimal alumnosOtorgamientoTarjetaUAC(String nivel, String unidadAcademica);

    public BigDecimal alumnosOtorgamientoTarjetaEntregadaC(String nivel, String unidadAcademica);
    
    public BigDecimal alumnosDatosIncorrectosC(String nivel, String unidadAcademica, BigDecimal periodoId);

    public PaginateUtil alumnosFiltros(ServerSideUtil ssu);

    public PaginateUtil alumnosL(ServerSideUtil ssu, String alumnosL);

    public Integer alumnos0sRevalidantes();

    public Integer alumnos0sNuevos();

    public List<Alumno> nuevosOtorgamientosUABeca(BigDecimal uaId, Integer semMin, Integer semMax, Float promMin, Float promMax, BigDecimal modalidad, Integer nivel);

    public Integer alumnosNoAsinadosAutomaticamente();

    public Boolean tieneOtorgamiento(BigDecimal idAlumno);

    public Boolean tieneOtorgamientoTransporte(BigDecimal idAlumno, int tipo, Periodo periodo);

    public Boolean tieneBaja(BigDecimal idAlumno);

    public List<Alumno> buscarAlumnos(BigDecimal periodoId, Integer mes, Integer origenRecursos, BigDecimal programaBeca,
            BigDecimal nivelAcademico, BigDecimal uAcademica, BigDecimal tipoProceso, BigDecimal idOtorgamiento, String fechaDeposito);

    public Alumno getByUsuario(BigDecimal usuarioId);

    public BigDecimal totalAlumnosRegistrados(BigDecimal unidadAcademicaID);

    public String totalAlumnosRegistradosD(BigDecimal nivelId);

    public PaginateUtil busquedaAlumnos(ServerSideUtil ssu);

    public List<Alumno> busquedaAlumnosTarjeta(Map<String, Object> parametros);

    public Integer alumnos0sRevalidantes(boolean b);

    public Integer alumnos0sNuevos(boolean b);

    public PaginateUtil findAlumnos(ServerSideUtil ssu);

    public List<Alumno> buscarPorNombre(String nombre, String aPaterno, String aMaterno);

    public List<Alumno> buscarPorCURP(String curp);

    public List<Alumno> buscarPorCURPNombre(String curp, String nombre, String aPaterno, String aMaterno);

    public List<Alumno> buscarPorBoletaNombre(String boleta, String nombre, String aPaterno, String aMaterno);

    public List<Alumno> buscarPorBoletaCURP(String boleta, String curp);

    public List<Alumno> buscarPorBoletaCURPNombre(String boleta, String curp, String nombre, String aPaterno, String aMaterno);

    public List<Alumno> nuevos(BigDecimal id, Integer nivel, Periodo periodoActivo);

    public List<Alumno> validacionInscripcion(BigDecimal periodoId, BigDecimal periodoAnteriorId);

    public Long countValidacionInscripcion(BigDecimal periodoId, BigDecimal periodoAnteriorId);

    public List<Alumno> getSolicitudes(String identificador);

    public boolean esEgresado(BigDecimal alumnoId);

    public Alumno findByPreboleta(String preboleta);

    public List<Object[]> alumnosAutoevaluacion(String periodoId);
    
    public List<Object[]> reporteFundacion (BigDecimal periodoId, String beca, BigDecimal origenRecursosId);
    
    public PaginateUtil alumnosConfiguracionCuenta(ServerSideUtil ssu);
    
}