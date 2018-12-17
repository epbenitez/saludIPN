/**
 * SISTEMA INFORMÁTICO DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE SERVICIOS ESTUDIANTILES Created on : 04-ago-2015, 14:30:41
 *
 */
package com.becasipn.business;

import com.becasipn.service.Service;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 *
 * @author Patricia Benitez
 */
 public class AlumnoBO extends BaseBO{

    private final SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
    private HashMap<BigDecimal, Integer> relacion;
    private String jsonAlumno;

    public AlumnoBO(Service service) {
        super(service);
    }

}
