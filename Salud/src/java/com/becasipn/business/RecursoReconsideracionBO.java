package com.becasipn.business;

import com.becasipn.persistence.model.EstatusReconsideracion;
import com.becasipn.persistence.model.SolicitudReconsideracion;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.service.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Sistemas DSE
 */
public class RecursoReconsideracionBO extends BaseBO {

    public RecursoReconsideracionBO(Service service) {
        super(service);
    }

    public Boolean tieneSolicitudPeriodoActual(BigDecimal periodoId, BigDecimal alumnoId) {
        if (service.getSolicitudReconsideracionDao().tieneSolicitudPeriodoActual(periodoId, alumnoId)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public Boolean solicitudAlumnoPeriodoActual(BigDecimal periodoId, BigDecimal alumnoId) {
        if (service.getSolicitudReconsideracionDao().tieneSolicitudPeriodoActual(periodoId, alumnoId)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public SolicitudReconsideracion guardaSolicitud(SolicitudReconsideracion sr, Alumno alumno, Periodo periodo, EstatusReconsideracion estatus) {
        sr.setAlumno(alumno);
        sr.setPeriodo(periodo);
        sr.setFechaSolicitud(new Date());
        estatus.setId(BigDecimal.ONE);
        sr.setEstatus(estatus);

        try {
            if (sr.getId() == null) {
                sr = service.getSolicitudReconsideracionDao().save(sr);
            } else {
                sr = service.getSolicitudReconsideracionDao().update(sr);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return sr;
        }
        return sr;
    }

    public List<SolicitudReconsideracion> SolicitudesReconsideracionPorAlumnoList(BigDecimal alumnoId, BigDecimal periodoId) {
        List<SolicitudReconsideracion> list = new ArrayList<>();
        list = service.getSolicitudReconsideracionDao().solicitudesAlumnoPeriodoActual(periodoId, alumnoId);
        return list;
    }

}
