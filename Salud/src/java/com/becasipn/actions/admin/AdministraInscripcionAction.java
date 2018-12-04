package com.becasipn.actions.admin;

import com.becasipn.actions.BaseAction;
import com.becasipn.business.AlumnoBO;
import com.becasipn.domain.ResumenValidacionInscripcion;
import com.becasipn.persistence.model.Periodo;
import java.util.List;

/**
 *
 * @author Rafael Cárdenas Reséndiz.
 */
public class AdministraInscripcionAction extends BaseAction implements MensajesAdmin {

    Long total, buenos, malos, sinCambios;
    List<ResumenValidacionInscripcion> resumen;
    List<String> becas;

    public String form() {
        becas = getDaos().getTipoBecaPeriodoDao().getBecasConValidacionInscripcion();
        return SUCCESS;
    }

    public String lista() {
        Periodo periodo = getDaos().getPeriodoDao().getPeriodoActivo();
        total = getDaos().getAlumnoDao().countValidacionInscripcion(periodo.getId(), periodo.getPeriodoAnterior().getId());
        AlumnoBO alumnoBO = new AlumnoBO(getDaos());
        resumen = alumnoBO.validarInscripcion();
        malos = (long) resumen.size();
        buenos = total - malos;
        
        return SUCCESS;
    }

    public String resumen() {
        int[] info = new int[4];
        AlumnoBO alumnoBO = new AlumnoBO(getDaos());
        try {
            info = alumnoBO.actualizarPreasignacion();
        } catch (Exception e) {
            addActionError(e.getMessage());
            e.printStackTrace();
        }
        total = (long) info[0];
        buenos = (long) info[1];
        malos = (long) info[2];
        sinCambios = (long) info[3];
        return SUCCESS;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getBuenos() {
        return buenos;
    }

    public void setBuenos(Long buenos) {
        this.buenos = buenos;
    }

    public Long getMalos() {
        return malos;
    }

    public void setMalos(Long malos) {
        this.malos = malos;
    }

    public List<ResumenValidacionInscripcion> getResumen() {
        return resumen;
    }

    public void setResumen(List<ResumenValidacionInscripcion> resumen) {
        this.resumen = resumen;
    }

    public List<String> getBecas() {
        return becas;
    }

    public void setBecas(List<String> becas) {
        this.becas = becas;
    }

    public Long getSinCambios() {
        return sinCambios;
    }

    public void setSinCambios(Long sinCambios) {
        this.sinCambios = sinCambios;
    }
    
}
