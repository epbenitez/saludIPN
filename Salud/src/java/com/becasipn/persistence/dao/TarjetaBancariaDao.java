package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.Alumno;
import java.math.BigDecimal;

import com.becasipn.persistence.model.TarjetaBancaria;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Victor Lozano
 */
public interface TarjetaBancariaDao extends DaoBase<TarjetaBancaria, BigDecimal> {

	public String numeroTarjetaAlumno(Long id);

	public List<String> getLotes();

	public List<String> getOpcionesLotes();

	public boolean existeTarjeta(String numero);

	public String countTarjetasDisponibles();

	public List<TarjetaBancaria> getTarjetasDisponibles(int num);

	public List<Object[]> datosAsignaciones(String identificador);

	public TarjetaBancaria tarjetaAnterior(String identificador, Alumno a);

	public List<TarjetaBancaria> tarjetaAnterior(String identificador);

	public TarjetaBancaria findByNumeroCuenta(String num);

	public TarjetaBancaria findReferenciaByNumeroCuenta(String num);
        
        public boolean alumnoConCuentaBanamex(BigDecimal tarjetaId);
        
        public List<LinkedHashMap<String, Object>> findCuentasBySolicitud(BigDecimal solicitudId);
}
