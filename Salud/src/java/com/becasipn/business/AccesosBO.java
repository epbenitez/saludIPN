package com.becasipn.business;

import com.becasipn.persistence.model.SolicitudBecaUA;
import com.becasipn.service.Service;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Augusto H
 */
public class AccesosBO extends BaseBO {

    public AccesosBO(Service service) {
        super(service);
    }

    public Boolean nuevoAccesoUA(SolicitudBecaUA nuevoAcceso, String uaL) {
        Date fechaActual = new Date();

        if (uaL != null) {
            List<String> uas = Arrays.asList(uaL.split(","));
            for (String ua : uas) {
                nuevoAcceso.setFechaModificacion(fechaActual);
                nuevoAcceso.setEstatus(1);
                nuevoAcceso.setUnidadAcademica(service.getUnidadAcademicaDao().findById(new BigDecimal(ua.trim())));
                if (nuevoAcceso == null || nuevoAcceso.getId() == null) {
                    service.getSolicitudBecaUADao().save(nuevoAcceso);
                    nuevoAcceso.setId(null);
                } else {
                    service.getSolicitudBecaUADao().update(nuevoAcceso);
                }
            }
        }
        return Boolean.TRUE;
    }

}
