package com.becasipn.persistence.dao;

import java.math.BigDecimal;
import com.becasipn.persistence.model.AlumnoTarjetaBancaria;
import com.becasipn.persistence.model.TarjetaBancaria;
import com.becasipn.util.PaginateUtil;
import com.becasipn.util.ServerSideUtil;
import java.util.List;

/**
 * @author Gustavo Adolfo Alamillo Medina
 * @version 1.0
 */
public interface AlumnoTarjetaBancariaDao extends DaoBase<AlumnoTarjetaBancaria, BigDecimal> {

	public AlumnoTarjetaBancaria getTarjetaAlumno(BigDecimal alumnoId, BigDecimal tarjetaBancariaId);
        
        public AlumnoTarjetaBancaria getTarjetaAlumno(BigDecimal alumnoId, String tarjetaBancaria);

	public AlumnoTarjetaBancaria tarjetaAlumno(BigDecimal alumnoId);

	public AlumnoTarjetaBancaria tarjetaAlumnoV2(BigDecimal alumnoId);

	public AlumnoTarjetaBancaria alumnoTarjetaReferencia(BigDecimal tarjetaId);

	public PaginateUtil getListado(ServerSideUtil ssu);

	public PaginateUtil getListadoPersonalizacion(ServerSideUtil ssu);

	public PaginateUtil listadoAlumnoTarjetaRemplazar(ServerSideUtil ssu, BigDecimal periodoId, int tipo);

	public List<AlumnoTarjetaBancaria> listadoAlumnoTarjetaRemplazar(String boleta, BigDecimal nivel, BigDecimal unidadAcademica, BigDecimal periodoId, int tipo);

	public boolean existeIdentificador(String identificador);

	public Long asignacionesIdentificador(String identificador);

	public List<AlumnoTarjetaBancaria> listAsignacionesIdentificador(String identificador);

	public int eliminarAsignacion(String identificador);

	public AlumnoTarjetaBancaria findByTarjetaBancaria(TarjetaBancaria tb);
        
        public AlumnoTarjetaBancaria findByNumeroTarjetaBancaria(AlumnoTarjetaBancaria tb);

	public int updateVigente(BigDecimal alumnoId, BigDecimal relacionId);

	public List<String> identificadores();

	public AlumnoTarjetaBancaria tarjetaDatosBancarios(BigDecimal datosBancariosId);

	public List<AlumnoTarjetaBancaria> tarjetasDatosBancarios(BigDecimal datosBancariosId);

	public List<AlumnoTarjetaBancaria> getSolicitudes(BigDecimal identificadorId);

	public AlumnoTarjetaBancaria findBySolicitudCuenta(BigDecimal scId, BigDecimal alumnoId);

	public int finalizarSolicitudes(BigDecimal scId);

	public List<Object[]> estadisticaSolicitudes(BigDecimal scId);

	public List<AlumnoTarjetaBancaria> listSolicitudes(BigDecimal scId);
        
        public AlumnoTarjetaBancaria tarjetaLiberada (BigDecimal alumnoId);
}
