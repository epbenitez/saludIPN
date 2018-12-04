package com.becasipn.business;

import com.becasipn.persistence.model.AlumnoDatosBancarios;
import com.becasipn.service.Service;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez
 */
public class AlumnoDatosBancariosBO extends BaseBO {

    public AlumnoDatosBancariosBO(Service service) {
        super(service);
    }

    /**
     * Establece la fecha de nacimiento a partir de un string
     *
     * @param fechaDeNacimiento
     * @return
     */
    public Date setFechaNacimiento(String fechaDeNacimiento) {
        Date fecha;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            fecha = dateFormat.parse(fechaDeNacimiento);
            return fecha;
        } catch (Exception ex) {
        }
        return null;
    }

    /**
     * Regresa si los datos bancarios vigentes.
     *
     * @param alumnoId
     * @return
     */
    public AlumnoDatosBancarios datosVigentes(BigDecimal alumnoId) {
        AlumnoDatosBancarios alumnoDatosBancarios = new AlumnoDatosBancarios();
        List<AlumnoDatosBancarios> alumnoDatosBancariosLista = service.getAlumnoDatosBancariosDao().listaDatosBancarios(alumnoId);
        //Si el alumno no tiene datos bancarios regresa null.
        if (alumnoDatosBancariosLista == null) {
            alumnoDatosBancarios = null;
        } //Si el alumno tiene un solo registro regresa los datos de ese registro.
        else if (alumnoDatosBancariosLista.size() == 1) {
            alumnoDatosBancarios = alumnoDatosBancariosLista.get(0);
        } //Si el alumno tiene dos registros
        else {
            for (AlumnoDatosBancarios a : alumnoDatosBancariosLista) {
                //Regresa los datos bancarios vigentes si existe
                if (a.getVigente() != null && a.getVigente()) {
                    alumnoDatosBancarios = a;
                }
            }
            //Si no tiene datos vigentes, regresa los datos que no son del tutor
            if (alumnoDatosBancarios.getId() == null) {
                for (AlumnoDatosBancarios a : alumnoDatosBancariosLista) {
                    if (a.getNombre() == null && a.getApellidoPaterno() == null) {
                        alumnoDatosBancarios = a;
                    }
                }
            }
        }
        return alumnoDatosBancarios;
    }

    /**
     * Guarda o actualiza los datos del alumno
     *
     * @param alumnoDatosBanco
     * @param fechaNacimientoTutor
     * @param renuncia
     * @param intentos
     * @return
     */
    public AlumnoDatosBancarios guarda(AlumnoDatosBancarios alumnoDatosBanco, String fechaNacimientoTutor, Boolean renuncia, Integer intentos) {
        List<AlumnoDatosBancarios> aTmpLst = service.getAlumnoDatosBancariosDao().listaDatosBancarios(alumnoDatosBanco.getAlumno().getId());
        alumnoDatosBanco.setFechaDeNacimiento(this.setFechaNacimiento(fechaNacimientoTutor));
        alumnoDatosBanco.setVigente(Boolean.TRUE);
        alumnoDatosBanco.setFechaModificacion(new Date());
        alumnoDatosBanco.setIntentos((alumnoDatosBanco.getIntentos() == null ? 0 : alumnoDatosBanco.getIntentos()) + intentos);
        //Se asigna si la cuenta es del alumno o del padre o tutor
        if (alumnoDatosBanco.getNombre() == null || alumnoDatosBanco.getApellidoPaterno() == null) {
            alumnoDatosBanco.setTutor(Boolean.FALSE);
            alumnoDatosBanco.setApellidoMaterno(null);
            alumnoDatosBanco.setFechaDeNacimiento(null);
        } else {
            alumnoDatosBanco.setTutor(Boolean.TRUE);
        }
        //Si el alumno renuncio a la cuenta de su padre o tutor le crea un nuevo registro con sus datos
        if (renuncia != null && renuncia) {
            aTmpLst = null;
        }
        if (aTmpLst == null) {
            service.getAlumnoDatosBancariosDao().save(alumnoDatosBanco);
        } else {
            if (alumnoDatosBanco.getId() != null) {
                service.getAlumnoDatosBancariosDao().update(alumnoDatosBanco);
            }
        }
        return alumnoDatosBanco;
    }
}
