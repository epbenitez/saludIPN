package com.becasipn.business;

import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.AlumnoTarjetaBancaria;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.EstatusTarjetaBancaria;
import com.becasipn.persistence.model.Otorgamiento;
import com.becasipn.persistence.model.TarjetaBancaria;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.service.Service;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Victor Lozano
 * @date 17/08/2016
 */
public class AlumnoTarjetaBancariaBO extends BaseBO {

    public AlumnoTarjetaBancariaBO(Service service) {
	super(service);
    }

    /**
     * Almacena la información de la entidad en la base de datos
     *
     * @author Victor Lozano
     * @param asignacion
     * @return true si la operación se realizó exitosamente
     */
    public Boolean guardaTarjeta(AlumnoTarjetaBancaria asignacion) {
	try {
	    if (asignacion.getId() == null) {
		service.getAlumnoTarjetaDao().save(asignacion);
		return Boolean.TRUE;
	    } else {
		service.getAlumnoTarjetaDao().update(asignacion);
		return Boolean.TRUE;
	    }
	} catch (Exception e) {
	    return Boolean.FALSE;
	}
    }

    public List<AlumnoTarjetaBancaria> asignarTarjeta(List<AlumnoTarjetaBancaria> alumnos, Usuario usuario,
	    String identificador, String observaciones) {
	TarjetaBO tbo = new TarjetaBO(service);
	List<AlumnoTarjetaBancaria> asignaciones = new ArrayList<>();
	List<TarjetaBancaria> tarjetasParaAsignar = service.getTarjetaBancariaDao().getTarjetasDisponibles(alumnos.size());
	for (int cont = 0; cont < alumnos.size() && cont < tarjetasParaAsignar.size(); cont++) {
	    if (alumnos.get(cont).getTarjetaBancaria() != null) {//Remplazo
		tbo.cambiarEstatus(alumnos.get(cont).getTarjetaBancaria(),
			new EstatusTarjetaBancaria(new BigDecimal(5)), usuario, observaciones);
	    }
	    TarjetaBancaria tarjeta = tarjetasParaAsignar.get(cont);
	    AlumnoTarjetaBancaria asignacion = new AlumnoTarjetaBancaria();
	    asignacion.setAlumno(alumnos.get(cont).getAlumno());
	    asignacion.setVigente(true);
	    asignacion.setTarjetaBancaria(tarjeta);
	    asignacion.setIdentificador(identificador);
	    if (guardaTarjeta(asignacion)) {
		if (tbo.cambiarEstatus(tarjeta, new EstatusTarjetaBancaria(new BigDecimal(3)), usuario, observaciones)) {
		    if (alumnos.get(cont).getTarjetaBancaria() != null) {//Remplazos
			asignacion.setTarjetaAnterior(alumnos.get(cont).getTarjetaBancaria());
		    }
		    asignaciones.add(asignacion);
		}
	    }
	}
	return asignaciones;
    }

    public List<Object[]> imprimirAsignacionTarjeta(String identificador, BigDecimal periodoId, String[] encabezado, int tipoArchivo) {
	List<AlumnoTarjetaBancaria> resultados = service.getAlumnoTarjetaDao().listAsignacionesIdentificador(identificador);
	List<Object[]> list = new ArrayList<>();
	if (tipoArchivo == 1) {
	    for (AlumnoTarjetaBancaria resultado : resultados) {
		Object[] row = new Object[10];
		Otorgamiento otorgamiento = service.getOtorgamientoDao().getOtorgamientoAlumno(resultado.getAlumno().getId(), periodoId);
		if (otorgamiento == null) {
		    otorgamiento = service.getOtorgamientoDao().getOtorgamientoTransporteAlumno(resultado.getAlumno().getId(), periodoId, 2);
		}
		TarjetaBancaria anterior = service.getTarjetaBancariaDao().tarjetaAnterior(identificador, resultado.getAlumno());
                Alumno alumno = resultado.getAlumno();
                DatosAcademicos datosAcademicos = service.getDatosAcademicosDao().ultimosDatos(alumno.getId());
                
		row[0] = resultado.getTarjetaBancaria().getSecuencia();
		row[1] = resultado.getTarjetaBancaria().getNumtarjetabancaria();
		row[2] = anterior != null ? anterior.getNumtarjetabancaria() : "";
		row[3] = datosAcademicos.getUnidadAcademica().getClave();
		row[4] = datosAcademicos.getUnidadAcademica().getNombreCorto();
		row[5] = alumno.getCurp();
		row[6] = alumno.getBoleta();
		row[7] = alumno.getFullName();
		row[8] = otorgamiento != null ? otorgamiento.getTipoBecaPeriodo().getTipoBeca().getNombre() : "";
		row[9] = alumno.getCorreoElectronico();
		list.add(row);
	    }
	} else {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	    for (AlumnoTarjetaBancaria resultado : resultados) {
		Object[] row = new Object[encabezado.length];
		row[0] = "2";
		row[1] = "1";
		row[2] = "1";
		row[3] = "1";
		row[4] = "1";
		row[5] = resultado.getAlumno().getNombre();
		row[6] = resultado.getAlumno().getApellidoPaterno();
		row[7] = resultado.getAlumno().getApellidoMaterno();
		row[8] = "";
		row[9] = "";
		row[10] = sdf.format(resultado.getAlumno().getFechaDeNacimiento());
		row[11] = "";
		row[12] = "AV MIGUEL OTHON DE M ESQ MIGUEL BERNARD S N";
		row[13] = "RESIDENCIAL LA ESCALERA";
		row[14] = "7738";
		row[15] = "";
		row[16] = "";
		row[17] = "";
		row[18] = "";
		row[19] = "DISTRITO FEDERAL";
		row[20] = "1";
		row[21] = resultado.getTarjetaBancaria().getNumtarjetabancaria();
		row[22] = "2";
		row[23] = "0";
		row[24] = "02";
		row[25] = "";
		row[26] = "1";
		row[27] = "1";
		row[28] = "";
		row[29] = "";
		row[30] = "";
		row[31] = "";
		row[32] = "";
		row[33] = "";
		row[34] = "";
		row[35] = "";
		row[36] = "";
		row[37] = "";
		list.add(row);
	    }
	}
	return list;
    }

    public int revertirAsignaciones(String identificador) {
	if (service.getBitacoraTarjetaBancariaDao().verificarEstatusReversion(identificador) > 0) {
	    return -1;
	}
	List<TarjetaBancaria> anteriores = service.getTarjetaBancariaDao().tarjetaAnterior(identificador);
	for (TarjetaBancaria anterior : anteriores) {
	    setVigente(anterior);
	}
	service.getBitacoraTarjetaBancariaDao().eliminarBitacorAsignacion(identificador);
	return service.getAlumnoTarjetaDao().eliminarAsignacion(identificador);
    }

    public Boolean eliminarSolicitudes(BigDecimal solicitudId) {
	List<AlumnoTarjetaBancaria> lst = service.getAlumnoTarjetaDao().getSolicitudes(solicitudId);
	Boolean res = true;
	if (lst != null && !lst.isEmpty()) {
	    for (AlumnoTarjetaBancaria solicitud : lst) {
		try {
		    service.getAlumnoTarjetaDao().delete(solicitud);
		    res = res & true;
		} catch (Exception e) {
		    res = res & false;
		}
	    }
	} else {
	    res = res & false;
	}
	return res;
    }

    public boolean setVigente(TarjetaBancaria tb) {
	AlumnoTarjetaBancaria atb = service.getAlumnoTarjetaDao().findByTarjetaBancaria(tb);
	if (atb != null) {
	    service.getAlumnoTarjetaDao().updateVigente(atb.getAlumno().getId(), atb.getId());
	    return true;
	}
	return false;
    }
}
