package com.becasipn.business;

import com.becasipn.domain.Requisito;
import com.becasipn.domain.RequisitosPrograma;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.Beca;
import com.becasipn.persistence.model.CuestionarioTransporte;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.Estatus;
import com.becasipn.persistence.model.Nivel;
import com.becasipn.persistence.model.TipoBecaPeriodo;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.SolicitudBeca;
import com.becasipn.persistence.model.TipoBecaPeriodoHistorico;
import com.becasipn.persistence.model.TransporteTraslado;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.service.Service;
import com.opensymphony.xwork2.ActionContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.List;

public class TipoBecaPeriodoBO extends BaseBO {

    private Alumno alumno;
    private SolicitudBeca solicitud;
    private DatosAcademicos datosAcademicos;
    private Beca programaBeca;

    // 
    /**
     * Este constructor está pensado en la sección detalle de asignación de becas, 
     * incluye lo necesario     
     * Author: Mario Márquez
     *
     * @param service 
     * @param alumnoId 
     * @param solicitudId
     * @param programaBecaId
     * @return TipoBecaPeriodoBO
     */
    public static TipoBecaPeriodoBO getInstance(Service service, String alumnoId,
            String solicitudId, String programaBecaId) {

        Alumno alumno = service.getAlumnoDao().findById(new BigDecimal(alumnoId));
        SolicitudBeca solicitud = service.getSolicitudBecaDao().findById(new BigDecimal(solicitudId));
        Beca programaBeca = service.getBecaDao().findById(new BigDecimal(programaBecaId));
        TipoBecaPeriodoBO tbpBP = new TipoBecaPeriodoBO(service, alumno, solicitud,
                programaBeca);

        return tbpBP;
    }
    /**
     * Este constructor está pensado en la sección detalle de asignación de becas, 
     * incluye lo necesario     
     * Author: Mario Márquez
     *
     * @param service 
     * @param alumnoId 
     * @param solicitudId
     * @param programaBecaId
     * @return TipoBecaPeriodoBO
     */
    public static TipoBecaPeriodoBO getInstance(Service service, BigDecimal alumnoId,
            BigDecimal solicitudId, BigDecimal programaBecaId) {

        Alumno alumno = service.getAlumnoDao().findById(alumnoId);
        SolicitudBeca solicitud = service.getSolicitudBecaDao().findById(solicitudId);
        Beca programaBeca = service.getBecaDao().findById(programaBecaId);
        TipoBecaPeriodoBO tbpBP = new TipoBecaPeriodoBO(service, alumno, solicitud,
                programaBeca);

        return tbpBP;
    }

    public TipoBecaPeriodoBO(Service service) {
        super(service);
    }

    private TipoBecaPeriodoBO(Service service, Alumno alumno, SolicitudBeca solicitud,
            Beca programaBeca) {
        super(service);
        this.alumno = alumno;
        this.solicitud = solicitud;
        this.programaBeca = programaBeca;
    }

    public TipoBecaPeriodo getTipoBecaPeriodo(BigDecimal id) {
        TipoBecaPeriodo tipoBecaPeriodo = service.getTipoBecaPeriodoDao().findById(id);
        return tipoBecaPeriodo;
    }

    public Boolean guardaTipoBecaPeriodo(TipoBecaPeriodo tipoBecaPeriodo) {
        try {

            if (tipoBecaPeriodo.getModalidad() != null) {
                if (tipoBecaPeriodo.getModalidad().getId().intValue() == 0) {
                    tipoBecaPeriodo.setModalidad(null);
                }
            }

            if (tipoBecaPeriodo.getAreasconocimiento() != null) {
                if (tipoBecaPeriodo.getAreasconocimiento().getId().intValue() == 0) {
                    tipoBecaPeriodo.setAreasconocimiento(null);
                }
            }

            if (tipoBecaPeriodo.getId() == null) {
                tipoBecaPeriodo.setVisible(Boolean.TRUE);
                TipoBecaPeriodo tipoBecaPeriodoAux = service.getTipoBecaPeriodoDao().save(tipoBecaPeriodo);
                guardaTipoBecaPeriodoHistorico(tipoBecaPeriodoAux);
            } else {
                TipoBecaPeriodo tipoBecaPeriodoAux = service.getTipoBecaPeriodoDao().findById(tipoBecaPeriodo.getId());
                if (!Objects.equals(tipoBecaPeriodoAux.getMonto(), tipoBecaPeriodo.getMonto())) {
                    guardaTipoBecaPeriodoHistorico(tipoBecaPeriodo);
                }
                service.getTipoBecaPeriodoDao().update(tipoBecaPeriodo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public Boolean guardaTipoBecaPeriodoHistorico(TipoBecaPeriodo tipoBecaPeriodo) {
        try {
            TipoBecaPeriodoHistorico tipoBecaPeriodoHistorico = new TipoBecaPeriodoHistorico();

            tipoBecaPeriodoHistorico.setFecha(new Date());
            tipoBecaPeriodoHistorico.setMonto(tipoBecaPeriodo.getMonto().floatValue());
            tipoBecaPeriodoHistorico.setTipoBecaPeriodo(tipoBecaPeriodo);
            Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
            tipoBecaPeriodoHistorico.setUsuario(usuario);
            service.getTipoBecaPeriodoHistoricoDao().save(tipoBecaPeriodoHistorico);
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public Boolean eliminaTipoBecaPeriodo(TipoBecaPeriodo tipoBecaPeriodo) {
        try {
            service.getTipoBecaPeriodoHistoricoDao().borrarBitacoraTBP(tipoBecaPeriodo.getId());
            service.getTipoBecaPeriodoDao().delete(tipoBecaPeriodo);
            return Boolean.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }

    public Boolean tipoBecaPeriodoEnUso(BigDecimal tipoBecaPeriodo) {
        List<TipoBecaPeriodo> lista = service.getTipoBecaPeriodoDao().tipoBecaPeriodoEnUso(tipoBecaPeriodo);
        return lista == null || lista.isEmpty() ? Boolean.FALSE : Boolean.TRUE;
    }

    public Boolean tipoBecaPeriodoEnUsoPresupuesto(BigDecimal tipoBecaPeriodo) {
        List<TipoBecaPeriodo> lista = service.getTipoBecaPeriodoDao().tipoBecaPeriodoEnUsoPresupuesto(tipoBecaPeriodo);
        return lista == null || lista.isEmpty() ? Boolean.FALSE : Boolean.TRUE;
    }

    public Boolean transferenciaNoExiste(TipoBecaPeriodo tb) {
        List<TipoBecaPeriodo> lbp = service.getTipoBecaPeriodoDao().findAll();

        for (TipoBecaPeriodo aux : lbp) {
            if (aux.getTipoBeca().getId().equals(tb.getTipoBeca().getId()) && aux.getPeriodo().getId().equals(tb.getPeriodo().getId())) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    public Boolean guardaTransferencias(String transferencias) {
        Periodo per = service.getPeriodoDao().getPeriodoActivo();
        Estatus est = service.getEstatusDao().findById(new BigDecimal(1));
        Boolean res = true;

        String ids[] = transferencias.split(",");
        for (String id : ids) {
            TipoBecaPeriodo tipoBecaPeriodo = service.getTipoBecaPeriodoDao().findById(new BigDecimal(id));

            if (!Objects.equals(tipoBecaPeriodo.getPeriodo().getId(), per.getId())) {
                tipoBecaPeriodo.setPeriodo(per);
                tipoBecaPeriodo.setEstatus(est);
                tipoBecaPeriodo.setId(null);
                // Se agrega clave de validación cero para toda transferencia nueva
                tipoBecaPeriodo.setValidaciondeinscripcion(0);
                if (tipoBecaPeriodo.getIngresoSalarios() != null && tipoBecaPeriodo.getIngresoSalarios() == 0) {
                    tipoBecaPeriodo.setIngresoSalarios(null);
                }
                if (transferenciaNoExiste(tipoBecaPeriodo)) {
                    res = res & guardaTipoBecaPeriodo(tipoBecaPeriodo);
                    TipoBecaPeriodo ax = service.getTipoBecaPeriodoDao().findById(new BigDecimal(id));
                    ax.setEstatus(service.getEstatusDao().findById(new BigDecimal(2)));
                    service.getTipoBecaPeriodoDao().update(ax);
                }
            }
        }
        if (res) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public RequisitosPrograma getRequisitos() {
        Periodo periodo = service.getPeriodoDao().getPeriodoActivo();
        datosAcademicos = service.getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), periodo.getId());

        if (solicitud != null && datosAcademicos != null) {
            List<List<Requisito>> becaRequisitos = new ArrayList<>();

            
            Nivel nivel = datosAcademicos.getUnidadAcademica().getNivel();
            List<TipoBecaPeriodo> becas = service.getTipoBecaPeriodoDao().becasPorProgramaNivelPeriodo(programaBeca, nivel, periodo);
            for (TipoBecaPeriodo beca : becas) {
                becaRequisitos.add(getRequisitosBeca(beca));
            }
            RequisitosPrograma requisitosPrograma = new RequisitosPrograma.Builder(solicitud)
                    .becaRequisitos(becaRequisitos).build();
            return requisitosPrograma;
            
        } else { // En caso de no haber Datos académicos, se interrumpe el proceso.
            // tal vez hay que considerar que no haya solicitud
            if (solicitud == null) {
                String textoError = "El alumno no tiene solicitud";
            } else {
                String textoError = "El alumno no cuenta con datos académicos";
            }
        }
        
        return null;
    }

    private List<Requisito> getRequisitosBeca(TipoBecaPeriodo beca) {
        List<Requisito> requisitos = new ArrayList<>();

        requisitos.add(new Requisito.Builder().beca(beca.getTipoBeca().getNombre()).build());
        requisitos.add(new Requisito.Builder()
                .ingresos(beca.getIngresoSalarios(), solicitud.getIngresosPercapitaPesos()).build());
        requisitos.add(createTransporte(beca));
        requisitos.add(new Requisito.Builder()
                .vulnera(beca.getVulnerabilidadSubes(), solicitud.getVulnerabilidadSubes())
                .build());
        requisitos.add(new Requisito.Builder().regular(datosAcademicos.getRegular(), beca.getRegular()).build());
        requisitos.add(new Requisito.Builder().inscrito(datosAcademicos.getInscrito()).build());
        requisitos.add(new Requisito.Builder().promedio(beca.getPromedioMinimo(), datosAcademicos.getPromedio()).build());
        requisitos.add(new Requisito.Builder()
                .semestre(beca.getSemestreMinimo(), beca.getSemestreMaximo(), datosAcademicos.getSemestre())
                .build());
        requisitos.add(new Requisito.Builder()
                .carga(beca.getCumpleCargaMinima(), datosAcademicos.getCumpleCargaMinima())
                .build());
        requisitos.add(new Requisito.Builder()
                .areasConocimiento(datosAcademicos.getUnidadAcademica().getAreasConocimiento(),
                        beca.getAreasconocimiento())
                .build());

        return requisitos;
    }

    public Requisito createTransporte(TipoBecaPeriodo beca) {
        Float gastoT = beca.getGastotransporteminimo();
        if (gastoT == null) {
            gastoT = new Float(0);
        }
        BigDecimal cuestId = solicitud.getCuestionario().getId();
        BigDecimal usrId = solicitud.getAlumno().getUsuario().getId();
        // Periodo de la solicitud
        BigDecimal periodoSol = solicitud.getPeriodo().getId();
        // Se revisa que la solicitud sea para una beca de transporte
        if (isBecaTransporte(beca) && cuestId.intValue() == 2) {
            // Se obtiene el cuestionario de ese alumno
            CuestionarioTransporte cuestTrans = service.getCuestionarioTransporteDao().respuestasTransporte(usrId, periodoSol);
            if (cuestTrans == null) {
                return new Requisito.Builder().transporte().build();
            } else {
                List<TransporteTraslado> traslados = service.getTransporteTrasladoDao().respuestasTraslado(cuestTrans.getId());
                return new Requisito.Builder().transporte(gastoT, traslados).build();
            }
        } else if (!isBecaTransporte(beca)) {
            // se revisa primero si la beca no es de transporte, pues sólo si fuera de transporte, 
            // tendría caso revisar la solicitud
            return new Requisito.Builder().noTransporte(false).build();
        } else {
            return new Requisito.Builder().noTransporte(true).build();
        }
    }
    
    private boolean isBecaTransporte(TipoBecaPeriodo beca) {
        int id = beca.getTipoBeca().getBeca().getId().intValue();
        return id == 8;
    }

}
