package com.becasipn.business;

import com.becasipn.domain.Requisito;
import com.becasipn.domain.RequisitosPrograma;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.CuestionarioTransporte;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.Otorgamiento;
import com.becasipn.persistence.model.PadronSubes;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.SolicitudBeca;
import com.becasipn.persistence.model.TipoBecaPeriodo;
import com.becasipn.persistence.model.TransporteTraslado;
import com.becasipn.service.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mario Márquez
 */
public class EstatusSolicitudBO extends BaseBO {

    private List<SolicitudBeca> solicitudes;
    private DatosAcademicos dA;
    private SolicitudBeca solicitud;

    public EstatusSolicitudBO(Service service) {
        super(service);
    }

    public EstatusSolicitudBO(Service service, List<SolicitudBeca> solicitudes) {
        super(service);
        this.solicitudes = solicitudes;
    }

    public List<RequisitosPrograma> getRequisitos() {
        String becaotorgada;
        List<RequisitosPrograma> result = new ArrayList<>();

        if (solicitudes != null && !solicitudes.isEmpty()) {
            for (SolicitudBeca solicitude : solicitudes) {
                solicitud = solicitude;

                List<List<Requisito>> becaRequisitos = new ArrayList<>();
                List<TipoBecaPeriodo> becas = service.getTipoBecaPeriodoDao().getBecasPorSolicitud(solicitud.getId());
                // Para cada beca de la solicitud crea un requisito
                for (TipoBecaPeriodo beca : becas) {
                    becaRequisitos.add(createRequisitosBeca(beca));
                }

                PadronSubes padronSbs = service.getPadronSubesDao()
                        .getAlumnoSUBES(solicitud.getAlumno().getId());
                Otorgamiento solicitudO = service.getOtorgamientoDao()
                        .getOtorgamientoSolicitudPeriodo(solicitud.getId(), solicitud.getPeriodo().getId());
                if (solicitudO != null) {
                    becaotorgada = solicitudO.getTipoBecaPeriodo().getTipoBeca().getNombre();
                } else {
                    becaotorgada = "";
                }
                RequisitosPrograma requisitosPrograma = new RequisitosPrograma.Builder(solicitud)
                        .estatusId().estatus().motivo(padronSbs).beca(becaotorgada)
                        .otorgamientoDefinitivo(OtorgamientoBO.isOtorgamientoActualDefinitivo(solicitudO))
                        .becaRequisitos(becaRequisitos).build();

                result.add(requisitosPrograma);
            }

        }

        return result;
    }

    public boolean isSolicitudAnteriorVigente(boolean conSolicitud, Alumno alumno) {
        boolean result = false;
        
        if (!conSolicitud) {
            Periodo periodoActivo = service.getPeriodoDao().getPeriodoActivo();
            Otorgamiento otorgamiento = service.getOtorgamientoDao().getOtorgamientoAlumno(alumno.getId(), periodoActivo.getId());

            // En caso de que no haya solicitud y ya tenga otorgamiento, la validación 
            // de inscripción se toma del otorgamiento. Si hay otorgamiento, es probable que ya tuviera el pasado
            // por lo tanto no se revisa si tuvo otorgamiento en el pasado
            if (otorgamiento != null && otorgamiento.getTipoBecaPeriodo() != null) { //Verificando Validación de Inscripción
                if (otorgamiento.getTipoBecaPeriodo().getValidaciondeinscripcion() > 0) {
                    result = true;
                }
            }
            // Si no hay ni solicitud ni otorgamiento actual, pero si hay otorgamiento anterior, se revisa si el tipo 
            // de beca periodo del periodo actual, correspondiente a la del 
            // otorgamiento anterior, tiene validación
            if (otorgamiento == null) {
                Otorgamiento otorgamientoAnterior = service.getOtorgamientoDao().getOtorgamientoAlumno(alumno.getId(), periodoActivo.getPeriodoAnterior().getId());
                if (otorgamientoAnterior != null) {
                    TipoBecaPeriodo tipoBecaPeriodoActual = service.getTipoBecaPeriodoDao().getTipoBecaPeriodo(otorgamientoAnterior.getTipoBecaPeriodo(), periodoActivo);
                    if (tipoBecaPeriodoActual.getValidaciondeinscripcion() > 0) {
                        result = true;
                    }
                }
            }
        }
            

        return result;
    }

    private List<Requisito> createRequisitosBeca(TipoBecaPeriodo beca) {
        List<Requisito> requisitosB = new ArrayList<>();
        dA = service.getDatosAcademicosDao().datosPorPeriodo(solicitud.getAlumno().getId(), solicitud.getPeriodo().getId());

        getRequisitosBase(requisitosB, beca);
        if (dA != null) {
            setDANotNull(requisitosB, beca);
        } else {
            setDANull(requisitosB);
        }

        return requisitosB;
    }

    private void getRequisitosBase(List<Requisito> requisitosB, TipoBecaPeriodo beca) {
        TipoBecaPeriodoBO tbpBO = TipoBecaPeriodoBO.getInstance(service, solicitud.getAlumno().getId(), solicitud.getId(), beca.getId());
        requisitosB.add(new Requisito.Builder().beca(beca.getTipoBeca().getNombre()).build());
        requisitosB.add(new Requisito.Builder()
                .ingresos(beca.getIngresoSalarios(), solicitud.getIngresosPercapitaPesos()).build());
        requisitosB.add(tbpBO.createTransporte(beca));
        requisitosB.add(new Requisito.Builder()
                .vulnera(beca.getVulnerabilidadSubes(), solicitud.getVulnerabilidadSubes())
                .build());
    }

    private void setDANotNull(List<Requisito> requisitosB, TipoBecaPeriodo beca) {
        requisitosB.add(new Requisito.Builder().regular(dA.getRegular(), beca.getRegular()).build());
        requisitosB.add(new Requisito.Builder().inscrito(dA.getInscrito()).build());
        requisitosB.add(new Requisito.Builder().promedio(beca.getPromedioMinimo(), dA.getPromedio()).build());
        requisitosB.add(new Requisito.Builder()
                .semestre(beca.getSemestreMinimo(), beca.getSemestreMaximo(), dA.getSemestre())
                .build());
        requisitosB.add(new Requisito.Builder()
                .carga(beca.getCumpleCargaMinima(), dA.getCumpleCargaMinima())
                .build());
    }

    private void setDANull(List<Requisito> requisitosB) {
        // Cinco requisitos default
        // regular
        // inscripcion
        // promedio
        // semestre
        // carga
        for (int i = 0; i < 5; i++) {
            requisitosB.add(new Requisito.Builder().build());
        }
    }

}
