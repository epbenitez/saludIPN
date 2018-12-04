package com.becasipn.actions.alumno;

import com.becasipn.actions.BaseAction;
import com.becasipn.business.PeriodoBO;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.Documentos;
import com.becasipn.persistence.model.Periodo;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Tania G. Sánchez
 */
public class AdministraCotejoDocumentosAction extends BaseAction implements MensajesAlumno {
    private String numeroDeBoleta;
    private Alumno alumno = new Alumno();
    private Documentos documentos = new Documentos();

    public String ver() {
        numeroDeBoleta = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Alumno> listAlumno = getDaos().getAlumnoDao().getByBoleta(numeroDeBoleta);
        alumno = listAlumno.get(0);
        //Periodo
	PeriodoBO bo = new PeriodoBO(getDaos());
	Periodo periodo = bo.getPeriodoActivo();
        //Si el alumno tiene la validación de inscripción no le permitira constestar el estudio socioeconomico.
	Boolean validacionInscripcion = getDaos().getOtorgamientoDao().tieneValidacionInscripcion(periodo, alumno.getId(), null);
        documentos = getDaos().getDocumentosDao().documentosAlumnoPeriodo(alumno, periodo.getId());
	if (validacionInscripcion && documentos == null) {
            documentos = getDaos().getDocumentosDao().documentosAlumnoPeriodo(alumno, periodo.getPeriodoAnterior().getId());
        }
        return SUCCESS;
    }

    public String getNumeroDeBoleta() {
        return numeroDeBoleta;
    }

    public void setNumeroDeBoleta(String numeroDeBoleta) {
        this.numeroDeBoleta = numeroDeBoleta;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Documentos getDocumentos() {
        return documentos;
    }

    public void setDocumentos(Documentos documentos) {
        this.documentos = documentos;
    }
}