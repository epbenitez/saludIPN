package com.becasipn.actions.becas;

import com.becasipn.actions.BaseAction;
import com.becasipn.business.AlumnoBO;
import com.becasipn.business.DocumentosBO;
import com.becasipn.business.OtorgamientoBO;
import com.becasipn.business.PeriodoBO;
import com.becasipn.business.TipoBecaPeriodoBO;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.Beca;
import com.becasipn.persistence.model.BitacoraAlumno;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.Documentos;
import com.becasipn.persistence.model.Proceso;
import com.becasipn.persistence.model.MotivoRechazoSolicitud;
import com.becasipn.persistence.model.Nivel;
import com.becasipn.persistence.model.Otorgamiento;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.PersonalAdministrativo;
import com.becasipn.persistence.model.SolicitudBeca;
import com.becasipn.persistence.model.TipoBecaPeriodo;
import com.becasipn.persistence.model.UnidadAcademica;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.persistence.model.VWPresupuestoUnidadAcademica;
import com.opensymphony.xwork2.ActionContext;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

/**
 *
 * @author Victor Lozano
 */
public class AdministracionAsignacionesAction extends BaseAction implements MensajesBecas {

    private static final String FORMULARIO = "formulario";
    private static final String VALIDACION = "validacion";
    private List<TipoBecaPeriodo> tipoBecaPeriodo = new ArrayList<>();
    private List<TipoBecaPeriodo> becasPeriodo = new ArrayList<>();
    private List<UnidadAcademica> unidadesAcademicas = new ArrayList<>();
    private TipoBecaPeriodo tipoBeca;
    private Alumno alumno = new Alumno();
    private Documentos documentos = new Documentos();
    private Otorgamiento otorgamiento = new Otorgamiento();
    private List<VWPresupuestoUnidadAcademica> presupuesto = new ArrayList<>();
    private String periodo;
    private String egresado;
    private UnidadAcademica UA;
    private String alumnos;
    private BigDecimal privilegio;
    private boolean error;
    private boolean regular;
    private Boolean warning = Boolean.FALSE;
    private String alumnosError;
    private boolean permisoCambio;
    private boolean procesoActivo;
    private boolean cumpleRequisitos;
    private boolean errorSemestre = false;
    private int tab;
    private Usuario usuario;
    private int uAJefe;
    private Boolean validacionInscripcion = Boolean.FALSE;
    private Boolean valDocs = Boolean.TRUE;
    private SolicitudBeca solicitudBeca;
    private List<Proceso> procesoList;
    private Proceso proceso;
    private int tipoProceso;
    private String observaciones;
    private List<MotivoRechazoSolicitud> motivoRechazo;
    private MotivoRechazoSolicitud motivo;
    private String ingresoFormateado;
    private List<Beca> programasBeca;
    private String mensajeRequisitos = "";
    private boolean validacionRequisitos;
    private String checkboxEgresado;
    //private ListaEspera listaEspera;

    /**
     * Función para inicializar los datos de la interfaz del listado
     *
     * @author Victor Lozano
     * @return SUCCESS
     */
    private void inicializarInterfaz() {
        permisoCambio = false;
        usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        privilegio = getDaos().getUsuarioPrivilegioDao().findByUsuario(usuario.getId()).getPrivilegio().getId();
        BigDecimal nivelId;
        PersonalAdministrativo personal = getDaos().getPersonalAdministrativoDao().findByUsuario(usuario.getId());
        if (isResponsable()) {
            nivelId = personal.getUnidadAcademica().getNivel().getId();
            UA = personal.getUnidadAcademica();
        } else {
            nivelId = null;
        }
        becasPeriodo = getDaos().getTipoBecaPeriodoDao().becasPorNivelPeriodo(nivelId, getDaos().getPeriodoDao().getPeriodoActivo().getPeriodoAnterior().getId());
        unidadesAcademicas = getDaos().getUnidadAcademicaDao().findAll();
        motivoRechazo = getDaos().getMotivoRechazoSolicitudDao().getMotivosVisibles();
        alumnosError = "";
    }

    /**
     * Función para inicializar los datos de la interfaz del formulario
     *
     * @author Victor Lozano
     * @return SUCCESS
     */
    private void inicializaFormulario() {
        inicializarInterfaz();
        AlumnoBO alumnoBO = new AlumnoBO(getDaos());
        alumno = alumnoBO.getAlumno(alumno.getBoleta());
        solicitudBeca = getDaos().getSolicitudBecaDao().findById(solicitudBeca.getId());
        if (proceso != null) {
            proceso = getDaos().getProcesoDao().findById(proceso.getId());
        } else {
            addActionError(getText("becas.advertencia.proceso"));
        }
        tipoProceso = proceso.getTipoProceso().getMovimiento().getId().intValue();
        procesoActivo = estatusProceso(proceso);
        PeriodoBO bo = new PeriodoBO(getDaos());
        OtorgamientoBO oBo = new OtorgamientoBO(getDaos());
        Periodo periodoActivo = bo.getPeriodoActivo();
        String clave = periodoActivo.getClave();
        DatosAcademicos datosAcademicos = getDaos().getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), periodoActivo.getId());
        Nivel nivel = datosAcademicos.getUnidadAcademica().getNivel();
        Boolean tieneUniversal = false;
        Boolean tieneComplementaria = false;
        alumno.setDatosAcademicos(datosAcademicos);
        setEgresado(datosAcademicos.getEgresado());
        cumpleRequisitos = true;
        documentos = getDaos().getDocumentosDao().documentosAlumnoPeriodoActual(alumno);
        validacionInscripcion = getDaos().getOtorgamientoDao().tieneValidacionInscripcion(getDaos().getPeriodoDao().getPeriodoActivo(), alumno.getId(), null);
        if (documentos == null) {
            if (proceso.getTipoProceso().getMovimiento().getId().equals(new BigDecimal(3))) { //Nuevos
                documentos = new Documentos(getDaos().getPeriodoDao().getPeriodoActivo(), alumno);
                valDocs = Boolean.TRUE; //Los procesos de tipo de movimiento Nuevo, siempre deberá permitir guardar sus documentos
            } else {
                if (validacionInscripcion) {
                    documentos = getDaos().getDocumentosDao().documentosAlumnoPeriodo(alumno, getDaos().getPeriodoDao().getPeriodoActivo().getPeriodoAnterior().getId());
                    if (documentos == null) {
                        valDocs = valDocs = Boolean.FALSE;
                    }
                } else {
                    documentos = new Documentos(getDaos().getPeriodoDao().getPeriodoActivo(), alumno);
                    valDocs = Boolean.TRUE;
                }
            }
        }
        if (!oBo.validarBecasExternas(alumno)) {
            clearErrors();
            addActionError(getText("becas.advertencia.beca.externa") + oBo.becasExternas);
            warning = Boolean.TRUE;
        }

        if (solicitudBeca == null) {
            mensajeRequisitos = "No existe la solicitud.";
        }
        if (datosAcademicos == null) {
            mensajeRequisitos = "El alumno no cuenta con datos académicos para este periodo";
        }
        if (mensajeRequisitos.isEmpty()) {
            validacionRequisitos = true;
            programasBeca = getDaos().getBecaDao().programasPorNivelPeriodo(nivel.getId(), periodoActivo.getId());
        }

        switch (tab) {
            case 1:
                tipoBecaPeriodo = new ArrayList<>();
                otorgamiento = getDaos().getOtorgamientoDao().getOtorgamientoSolicitudPeriodo(solicitudBeca.getId(), periodoActivo.getId());
                tipoBecaPeriodo.add(otorgamiento.getTipoBecaPeriodo());
                tipoBeca = otorgamiento.getTipoBecaPeriodo();
                observaciones = otorgamiento.getObservaciones();
                break;
            case 0:
                if (tipoProceso == 1) { //Transferencias
                    otorgamiento = getDaos().getOtorgamientoDao().getOtorgamientoSolicitudPeriodo(solicitudBeca.getId(), periodoActivo.getPeriodoAnterior().getId());
                    if (solicitudBeca.getPermiteTransferencia() > 0) {
                        tieneUniversal = getDaos().getOtorgamientoDao().tieneOtorgamientoUniversal(alumno.getId(), periodoActivo.getId());
                        tieneComplementaria = getDaos().getOtorgamientoDao().tieneOtorgamientoComplementaria(alumno.getId(), periodoActivo.getId());
                        tipoBecaPeriodo = getDaos().getTipoBecaPeriodoDao().becasAplicables(alumno, solicitudBeca, tieneUniversal, tieneComplementaria);
                        //Quitamos de la lista las becas que pertenezcan a mismo programa de beca del otorgamiento
                        //del periodo anterior, ya que en ese caso sería validacion de inscripción
                        //No se permite transferir a becas Manutención ni Transporte Manutención
                        for (ListIterator<TipoBecaPeriodo> iter = tipoBecaPeriodo.listIterator(); iter.hasNext();) {
                            TipoBecaPeriodo tbp = iter.next();
                            if (tbp.getTipoBeca().getBeca().equals(otorgamiento.getTipoBecaPeriodo().getTipoBeca().getBeca())
                                    || tbp.getTipoBeca().getBeca().getId().equals(new BigDecimal(5)) //Manutención
                                    || tbp.getTipoBeca().getBeca().getId().equals(new BigDecimal(7))) {     //Transporte Manutención
                                iter.remove();
                            }
                        }
                    } else {
                        addActionError(getText("becas.error.no.transferencia"));
                    }
                } else if (tipoProceso == 7) { //Validaciones
                    otorgamiento = getDaos().getOtorgamientoDao().getOtorgamientoSolicitudPeriodo(solicitudBeca.getId(), periodoActivo.getPeriodoAnterior().getId());
                    if (otorgamiento.getTipoBecaPeriodo().getTipoBeca().getBeca().getId().equals(new BigDecimal(1))) {
                        tieneUniversal = getDaos().getOtorgamientoDao().tieneOtorgamientoUniversal(alumno.getId(), periodoActivo.getId());
                        tieneComplementaria = getDaos().getOtorgamientoDao().tieneOtorgamientoComplementaria(alumno.getId(), periodoActivo.getId());
                        tipoBecaPeriodo = getDaos().getTipoBecaPeriodoDao().becasAplicables(alumno, solicitudBeca, tieneUniversal, tieneComplementaria);
                        //Quitamos de la lista las becas que pertenezcan a otro programa de beca 
                        //Sólo se permite RECLASIFICACION (Entre becas del programa institucional)                        
                        for (ListIterator<TipoBecaPeriodo> iter = tipoBecaPeriodo.listIterator(); iter.hasNext();) {
                            TipoBecaPeriodo tbp = iter.next();
                            if (!tbp.getTipoBeca().getBeca().getId().equals(new BigDecimal(1))) {
                                iter.remove();
                            }
                        }
                    }
                } else {
                    otorgamiento = getDaos().getOtorgamientoDao().getOtorgamientoSolicitudPeriodo(solicitudBeca.getId(), periodoActivo.getPeriodoAnterior().getId());
                    if (clave.charAt(clave.length() - 1) == '1' || otorgamiento == null) {//Para llenar becas aplicables solo en period impar en periodo para no se debe llenar
                        if (solicitudBeca.getPermiteTransferencia() > 0) {
                            tieneUniversal = getDaos().getOtorgamientoDao().tieneOtorgamientoUniversal(alumno.getId(), periodoActivo.getId());
                            tieneComplementaria = getDaos().getOtorgamientoDao().tieneOtorgamientoComplementaria(alumno.getId(), periodoActivo.getId());
                            tipoBecaPeriodo = getDaos().getTipoBecaPeriodoDao().becasAplicables(alumno, solicitudBeca, tieneUniversal, tieneComplementaria);
                            /*for (ListIterator<TipoBecaPeriodo> iter = tipoBecaPeriodo.listIterator(); iter.hasNext();) {
                             if (iter.next().getTipoBeca().getBeca().equals(solicitudBeca.getProgramaBecaSolicitada())) {
                             iter.remove();
                             }
                             }*/
                        } else {
                            tipoBecaPeriodo = new ArrayList<>();
                            tieneUniversal = getDaos().getOtorgamientoDao().tieneOtorgamientoUniversal(alumno.getId(), periodoActivo.getId());
                            tieneComplementaria = getDaos().getOtorgamientoDao().tieneOtorgamientoComplementaria(alumno.getId(), periodoActivo.getId());
                            TipoBecaPeriodo becaAlumnoActual = oBo.becaSolicitud(solicitudBeca, periodoActivo, datosAcademicos, tieneUniversal, tieneComplementaria, proceso, null);
                            if (becaAlumnoActual != null && !becaAlumnoActual.getMonto().equals(BigDecimal.ZERO)) {
                                tipoBecaPeriodo.add(becaAlumnoActual);
                            }
                        }
                    } else {
                        tipoBecaPeriodo = new ArrayList<>();
                        tieneUniversal = getDaos().getOtorgamientoDao().tieneOtorgamientoUniversal(alumno.getId(), periodoActivo.getId());
                        tieneComplementaria = getDaos().getOtorgamientoDao().tieneOtorgamientoComplementaria(alumno.getId(), periodoActivo.getId());
                        TipoBecaPeriodo becaAlumnoActual = oBo.becaSolicitud(solicitudBeca, periodoActivo, datosAcademicos, tieneUniversal, tieneComplementaria, proceso, null);
                        if (becaAlumnoActual != null) {
                            tipoBecaPeriodo.add(becaAlumnoActual);
                        }
                    }
                }

                if (tipoBecaPeriodo == null || tipoBecaPeriodo.isEmpty()) {
                    cumpleRequisitos = false;
                    warning = Boolean.TRUE;
                    addActionError(getText("becas.advertencia.beca"));
                }

                for (Iterator<TipoBecaPeriodo> it = tipoBecaPeriodo.iterator(); it.hasNext();) {
                    TipoBecaPeriodo tbp = it.next();
                    if (!tbp.getVisible()) {
                        tbp.getTipoBeca().setNombre(tbp.getTipoBeca().getNombre() + " (C)");
                    }
                }
                motivoRechazo = getDaos().getMotivoRechazoSolicitudDao().getMotivosVisibles();
                MessageFormat mf = new MessageFormat("{0,number,$###,###.##}");
                mf.setLocale(Locale.GERMAN);
                ingresoFormateado = mf.format(new Object[]{solicitudBeca.getIngresosPercapitaPesos()});
                break;

            case 2:
                motivoRechazo = getDaos().getMotivoRechazoSolicitudDao().getMotivosVisibles();
                motivo = solicitudBeca.getMotivoRechazo();
                break;
            case 3:
                //listaEspera = getDaos().getListaEsperaDao().findBy("solicitudBeca.id", solicitudBeca.getId().toString(), false).get(0);
                break;
        }

    }

    public String lista() {
        inicializarInterfaz();
        return SUCCESS;
    }

    /**
     * Función para cargar los presupuestos asignados a una unidad academica
     *
     * @author Victor Lozano
     * @return SUCCESS
     */
    public String presupuesto() {
        BigDecimal unidadAcademicaId;
        UnidadAcademica uATemp;

        if (isResponsable()) {
            usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
            PersonalAdministrativo personal = getDaos().getPersonalAdministrativoDao().findByUsuario(usuario.getId());
            UA = personal.getUnidadAcademica();
            unidadAcademicaId = personal.getUnidadAcademica().getId();
        } else {
            unidadesAcademicas = getDaos().getUnidadAcademicaDao().findAll();
            unidadAcademicaId = new BigDecimal(uAJefe);

            // FIX 14/02/17 - Para mostrar en primer lugar la UA que el usuario
            // seleccionó desde "Asignación de Becas"
            for (int i = 0; i < unidadesAcademicas.size(); i++) {
                uATemp = unidadesAcademicas.get(i);
                if (uATemp.getId().intValue() == uAJefe) {
                    unidadesAcademicas.remove(i);
                    unidadesAcademicas.add(0, uATemp);
                }
            }
        }

        presupuesto = getDaos().getVwPresupuestoUnidadAcademicaDao().getPresupuestosPorUA(unidadAcademicaId);
        if (presupuesto.isEmpty() || presupuesto == null) {
            addActionError(getText("becas.error.no.presupuesto"));
            return SUCCESS;
        }
        periodo = presupuesto.get(0).getPeriodo().getDescripcion();
        return SUCCESS;
    }

    /**
     * Función para mostrar los requisitos de las becas
     *
     * @author Victor Lozano
     * @return SUCCESS
     */
    public String requisitos() {
        UnidadAcademica unidadAcademica;
        UnidadAcademica uATemp;

        if (isResponsable()) {
            usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
            PersonalAdministrativo personal = getDaos().getPersonalAdministrativoDao().findByUsuario(usuario.getId());
            UA = personal.getUnidadAcademica();
            unidadAcademica = personal.getUnidadAcademica();
        } else {
            unidadesAcademicas = getDaos().getUnidadAcademicaDao().findAll();
            unidadAcademica = getDaos().getUnidadAcademicaDao().findById(new BigDecimal(uAJefe));

            // FIX 14/02/17 - Para mostrar en primer lugar la UA que el usuario
            // seleccionó desde "Asignación de Becas"
            for (int i = 0; i < unidadesAcademicas.size(); i++) {
                uATemp = unidadesAcademicas.get(i);
                if (uATemp.getId().intValue() == uAJefe) {
                    unidadesAcademicas.remove(i);
                    unidadesAcademicas.add(0, uATemp);
                }
            }
        }

        periodo = getDaos().getPeriodoDao().getPeriodoActivo().getDescripcion();
        becasPeriodo = getDaos().getTipoBecaPeriodoDao().becasPorNivelPeriodo(unidadAcademica.getNivel().getId(), getDaos().getPeriodoDao().getPeriodoActivo().getId());

        return SUCCESS;
    }

    /**
     * Función cargar la información a detalle de un alumno
     *
     * @author Victor Lozano
     * @return SUCCESS en error FORMULARIO en exito
     */
    public String form() {
        inicializaFormulario();
        if (alumno == null || alumno.getBoleta() == null) {
            addActionError(getText("becas.error.visualizar"));
            return SUCCESS;
        }

        return FORMULARIO;
    }

    /**
     * Función cargar la información a detalle de un alumno en el modulo de
     * Administracion de Alumnos
     *
     * @author Jose Luis Ramirez Mosqueda
     * @return SUCCESS
     */
    public String validacion() {
        usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        privilegio = getDaos().getUsuarioPrivilegioDao().findByUsuario(usuario.getId()).getPrivilegio().getId();

        AlumnoBO alumnoBO = new AlumnoBO(getDaos());
        alumno = alumnoBO.getAlumno(alumno.getBoleta());
        if (alumno == null || alumno.getBoleta() == null) {
            addActionError(getText("becas.error.visualizar"));
        }
        documentos = getDaos().getDocumentosDao().documentosAlumnoPeriodoActual(alumno);
        if (documentos == null) {
            documentos = new Documentos(getDaos().getPeriodoDao().getPeriodoActivo(), alumno);
        }
        return SUCCESS;
    }

    /**
     * Función para editar el promedio y semestre de un alumno
     *
     * @author Victor Lozano
     * @return SUCCESS en error FORMULARIO en exito
     */
    public String editar() {
        usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        if (alumno == null || alumno.getId() == null) {
            addActionError(getText("becas.actualizado.error"));
            inicializaFormulario();
            return FORMULARIO;
        }

        Integer semestre = alumno.getDatosAcademicos().getSemestre();
        Double promedio = alumno.getDatosAcademicos().getPromedio();
        String curp = alumno.getCurp();
        BigDecimal mod = alumno.getDatosAcademicos().getModalidad().getId();
        Integer cumpleCargaMinima = alumno.getDatosAcademicos().getCumpleCargaMinima();

        BigDecimal periodoActivoId = getDaos().getPeriodoDao().getPeriodoActivo().getId();
        DatosAcademicos datosAcademicos = getDaos().getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), periodoActivoId);

        if (promedio < 0 || promedio > 10) {
            addActionError(getText("becas.error.promedio"));
            inicializaFormulario();
            return FORMULARIO;
        }
        if (semestre < 0 || semestre > datosAcademicos.getCarrera().getNumeroSemestres()) {
            // Se agrega mensaje individual en el jsp, se reemplaza action error 
            // por la bandera errorSemestre
            // addActionError(getText("becas.error.semestre"));
            errorSemestre = true;
            inicializaFormulario();
            return FORMULARIO;
        }

        datosAcademicos.setSemestre(semestre);
        datosAcademicos.setPromedio(promedio);
        datosAcademicos.setModalidad(getDaos().getModalidadDao().findById(mod));
        datosAcademicos.setCumpleCargaMinima(cumpleCargaMinima);
        datosAcademicos.setInfoActualizadaAdmin(1);
        datosAcademicos.setReprobadas(alumno.getDatosAcademicos().getReprobadas());
        datosAcademicos.setEgresado(alumno.getDatosAcademicos().getEgresado());

        alumno = getDaos().getAlumnoDao().findById(alumno.getId());
        alumno.setCurp(curp);
        //CuestionarioRespuestas salariosRespuesta = getDaos().getCuestionarioRespuestasDao().findById(salarios);

        BitacoraAlumno bitacora = new BitacoraAlumno(usuario, new Date(), alumno,
                getDaos().getPeriodoDao().getPeriodoActivo(), datosAcademicos.getSemestre(),
                datosAcademicos.getPromedio(), datosAcademicos.getInscrito(), datosAcademicos.getRegular(), alumno.getCurp());
        getDaos().getBitacoraAlumnoDao().save(bitacora);

        OtorgamientoBO obo = new OtorgamientoBO(getDaos());
        obo.revertirPreasignacion(alumno.getId());

        AlumnoBO alumnoBO = new AlumnoBO(getDaos());
        if (!getDaos().getOtorgamientoDao().tieneOtorgamientoDistintoUniversal(alumno.getId())) {
            getDaos().getDatosAcademicosDao().update(datosAcademicos);
            if (alumnoBO.guardaAlumno(alumno)) {//&&alumnoBO.actualizaSalarios(alumno, salariosRespuesta)) {
                addActionMessage(getText("becas.actualizado.exito"));
            } else {
                addActionError(getText("becas.actualizado.error"));
            }
        } else {
            addActionError(getText("becas.error.asignacion.tiene.otorgamiento"));
        }
        inicializaFormulario();
        return FORMULARIO;
    }

    /**
     * Función para editar el estatus de inscripción y regularidad de un alumno
     *
     * @author Victor Lozano
     * @return SUCCESS en error FORMULARIO en exito
     */
    public String estatus() {
        usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        if (alumno == null || alumno.getId() == null) {
            addActionError(getText("becas.actualizado.error"));
            inicializaFormulario();
            return FORMULARIO;
        }

        Integer alumnoRegular = alumno.getDatosAcademicos().getRegular();
        Integer alumnoInscrito = alumno.getDatosAcademicos().getInscrito();
        alumno = getDaos().getAlumnoDao().findById(alumno.getId());
        BigDecimal periodoActivoId = getDaos().getPeriodoDao().getPeriodoActivo().getId();
        DatosAcademicos datosAcademicos = getDaos().getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), periodoActivoId);
        datosAcademicos.setRegular(alumnoRegular == null ? 0 : alumnoRegular);
        datosAcademicos.setInscrito(alumnoInscrito == null ? 0 : alumnoInscrito);
        datosAcademicos.setInfoActualizadaAdmin(1);

        BitacoraAlumno bitacora = new BitacoraAlumno(usuario, new Date(), alumno, getDaos().getPeriodoDao().getPeriodoActivo(), datosAcademicos.getSemestre(), datosAcademicos.getPromedio(), datosAcademicos.getInscrito(), datosAcademicos.getRegular());
        getDaos().getBitacoraAlumnoDao().save(bitacora);

        OtorgamientoBO obo = new OtorgamientoBO(getDaos());
        obo.revertirPreasignacion(alumno.getId());

        AlumnoBO alumnoBO = new AlumnoBO(getDaos());
        if (!getDaos().getOtorgamientoDao().existeAlumnoAsignado(alumno.getId())) {
            getDaos().getDatosAcademicosDao().update(datosAcademicos);
            if (alumnoBO.guardaAlumno(alumno)) {
                addActionMessage(getText("becas.actualizado.exito"));
            } else {
                addActionError(getText("becas.actualizado.error"));
            }
        } else {
            addActionError(getText("becas.error.asignacion.tiene.otorgamiento"));
        }
        inicializaFormulario();
        return FORMULARIO;
    }

    /**
     * Función para cotejar los documentos de un alumno
     *
     * @author Victor Lozano
     * @return SUCCESS en error FORMULARIO en exito
     */
    public String cotejar() {
        usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        if (alumno == null || alumno.getId() == null) {
            addActionError(getText("becas.actualizado.error"));
            if (!permisoCambio) {
                inicializaFormulario();
                return FORMULARIO;
            } else {
                return VALIDACION;
            }
        }

        if (proceso != null) {
            Periodo periodoActivo = getDaos().getPeriodoDao().getPeriodoActivo();
            String clave = periodoActivo.getClave();
            if (clave.charAt(clave.length() - 1) == '2') {
                Otorgamiento otorgamientoAnterior = getDaos().getOtorgamientoDao().getOtorgamientoSolicitudPeriodo(solicitudBeca.getId(), periodoActivo.getPeriodoAnterior().getId());
                //Si no tiene otorgamiento anterior, quiere decir que es nuevo y si se le permite guardar documentos
                if (otorgamientoAnterior != null) {
                    TipoBecaPeriodo tbp = getDaos().getTipoBecaPeriodoDao().getTipoBecaPeriodo(otorgamientoAnterior.getTipoBecaPeriodo(), periodoActivo);
                    if (tbp.getValidaciondeinscripcion() > 0) {
                        addActionMessage(getText("becas.actualizado.documentos.validos"));
                        if (!permisoCambio) {
                            inicializaFormulario();
                            return FORMULARIO;
                        } else {
                            return VALIDACION;
                        }
                    }
                }
            }
        }
        alumno = getDaos().getAlumnoDao().findById(alumno.getId());

        DocumentosBO documentosBO = new DocumentosBO(getDaos());
        documentos.setAlumno(alumno);
        documentos.setPeriodo(getDaos().getPeriodoDao().getPeriodoActivo());

        if (documentosBO.validaDocumentos(documentos)) {
            addActionMessage(getText("becas.actualizado.exito"));
        } else {
            addActionError(getText("becas.actualizado.error"));
        }

        if (!permisoCambio) {
            inicializaFormulario();
            return FORMULARIO;
        } else {
            return VALIDACION;
        }
    }

    /**
     * Función para asignar individualmente
     *
     * @author Victor Lozano
     * @return SUCCESS en error FORMULARIO en exito
     */
    public String cambiar() {
        inicializaFormulario();
        OtorgamientoBO obo = new OtorgamientoBO(true, getDaos());
        usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        if (alumno == null || alumno.getId() == null) {
            addActionError(getText("becas.actualizado.error"));
            return FORMULARIO;
        }
        if (tipoBeca == null || tipoBeca.getId() == null) {
            addActionError(getText("becas.actualizado.error.beca"));

        } else {
            alumno = getDaos().getAlumnoDao().findById(alumno.getId());
            tipoBeca = getDaos().getTipoBecaPeriodoDao().findById(tipoBeca.getId());
            alumnosError = "";

            if (proceso == null) {
                addActionError(getText("becas.error.proceso.seleccionado"));
            } else {
                proceso = getDaos().getProcesoDao().findById(proceso.getId());
                if (tab == 0) {
                    obo.setAsignacionIndividual(true);
                    if (obo.otorgarBeca(proceso, alumno, tipoBeca, solicitudBeca, usuario, observaciones)) {
                        addActionMessage(getText("becas.exito.asignacion") + " (" + tipoBeca.getTipoBeca().getNombre() + ")");
                    } else {
                        addActionError(obo.getErrorAsignacion());
                    }
                }
            }
        }
        inicializaFormulario();
        return FORMULARIO;
    }

    public String rechazo() {
        inicializaFormulario();
        OtorgamientoBO obo = new OtorgamientoBO(getDaos());
        clearErrorsAndMessages();
        usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        if (alumno == null || alumno.getId() == null) {
            addActionError(getText("becas.actualizado.error"));
            inicializaFormulario();
            return FORMULARIO;
        }
        if (obo.rechazar(solicitudBeca, motivo, proceso, usuario)) {
            motivo = getDaos().getMotivoRechazoSolicitudDao().findById(motivo.getId());
            addActionMessage(getText("becas.exito.rechazar") + " (" + motivo.getNombre() + ")");
        } else {
            addActionError(obo.getErrorAsignacion());
        }
        inicializaFormulario();
        return FORMULARIO;
    }

    public String espera() {
        inicializaFormulario();
        OtorgamientoBO obo = new OtorgamientoBO(getDaos());
        clearErrorsAndMessages();
        usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        if (alumno == null || alumno.getId() == null) {
            addActionError(getText("becas.actualizado.error"));
            inicializaFormulario();
            return FORMULARIO;
        }
        if (obo.listaEspera(solicitudBeca, proceso, usuario)) {
            addActionMessage(getText("becas.exito.lista.espera"));
        } else {
            addActionError(obo.getErrorAsignacion());
        }
        inicializaFormulario();
        return FORMULARIO;
    }

    private boolean estatusProceso(Proceso pro) {
        Date hoy = new Date();
        if (pro.getProcesoEstatus().getId().equals(new BigDecimal(1))) {
            if (hoy.after(pro.getFechaInicial()) && hoy.before(pro.getFechaFinal())) {
                return true;
            }
        }
        return false;
    }

    public List<Proceso> getProcesoList() {
        return procesoList;
    }

    public void setProcesoList(List<Proceso> procesoList) {
        this.procesoList = procesoList;
    }

    public List<TipoBecaPeriodo> getTipoBecaPeriodo() {
        return tipoBecaPeriodo;
    }

    public void setTipoBecaPeriodo(List<TipoBecaPeriodo> tipoBecaPeriodo) {
        this.tipoBecaPeriodo = tipoBecaPeriodo;
    }

    public List<TipoBecaPeriodo> getBecasPeriodo() {
        return becasPeriodo;
    }

    public void setBecasPeriodo(List<TipoBecaPeriodo> becasPeriodo) {
        this.becasPeriodo = becasPeriodo;
    }

    public List<UnidadAcademica> getUnidadesAcademicas() {
        return unidadesAcademicas;
    }

    public void setUnidadesAcademicas(List<UnidadAcademica> unidadesAcademicas) {
        this.unidadesAcademicas = unidadesAcademicas;
    }

    public TipoBecaPeriodo getTipoBeca() {
        return tipoBeca;
    }

    public void setTipoBeca(TipoBecaPeriodo tipoBeca) {
        this.tipoBeca = tipoBeca;
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

    public Otorgamiento getOtorgamiento() {
        return otorgamiento;
    }

    public void setOtorgamiento(Otorgamiento otorgamiento) {
        this.otorgamiento = otorgamiento;
    }

    public List<VWPresupuestoUnidadAcademica> getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(List<VWPresupuestoUnidadAcademica> presupuesto) {
        this.presupuesto = presupuesto;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public UnidadAcademica getUA() {
        return UA;
    }

    public void setUA(UnidadAcademica UA) {
        this.UA = UA;
    }

    public String getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(String alumnos) {
        this.alumnos = alumnos;
    }

    public BigDecimal getPrivilegio() {
        return privilegio;
    }

    public void setPrivilegio(BigDecimal privilegio) {
        this.privilegio = privilegio;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isRegular() {
        return regular;
    }

    public void setRegular(boolean regular) {
        this.regular = regular;
    }

    public Proceso getProceso() {
        return proceso;
    }

    public void setProceso(Proceso proceso) {
        this.proceso = proceso;
    }

    public Boolean getWarning() {
        return warning;
    }

    public void setWarning(Boolean warning) {
        this.warning = warning;
    }

    public String getAlumnosError() {
        return alumnosError;
    }

    public void setAlumnosError(String alumnosError) {
        this.alumnosError = alumnosError;
    }

    public boolean isCumpleRequisitos() {
        return cumpleRequisitos;
    }

    public void setCumpleRequisitos(boolean cumpleRequisitos) {
        this.cumpleRequisitos = cumpleRequisitos;
    }

    public int getTab() {
        return tab;
    }

    public void setTab(int tab) {
        this.tab = tab;
    }

    public boolean isProcesoActivo() {
        return procesoActivo;
    }

    public void setProcesoActivo(boolean procesoActivo) {
        this.procesoActivo = procesoActivo;
    }

    public boolean isPermisoCambio() {
        return permisoCambio;
    }

    public void setPermisoCambio(boolean permisoCambio) {
        this.permisoCambio = permisoCambio;
    }

    public int getUAJefe() {
        return uAJefe;
    }

    public void setUAJefe(int uAJefe) {
        this.uAJefe = uAJefe;
    }

    public Boolean getValidacionInscripcion() {
        return validacionInscripcion;
    }

    public void setValidacionInscripcion(Boolean validacionInscripcion) {
        this.validacionInscripcion = validacionInscripcion;
    }

    public Boolean getValDocs() {
        return valDocs;
    }

    public void setValDocs(Boolean valDocs) {
        this.valDocs = valDocs;
    }

    public SolicitudBeca getSolicitudBeca() {
        return solicitudBeca;
    }

    public void setSolicitudBeca(SolicitudBeca solicitudBeca) {
        this.solicitudBeca = solicitudBeca;
    }

    public int getTipoProceso() {
        return tipoProceso;
    }

    public void setTipoProceso(int tipoProceso) {
        this.tipoProceso = tipoProceso;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<MotivoRechazoSolicitud> getMotivoRechazo() {
        return motivoRechazo;
    }

    public void setMotivoRechazo(List<MotivoRechazoSolicitud> motivoRechazo) {
        this.motivoRechazo = motivoRechazo;
    }

    public MotivoRechazoSolicitud getMotivo() {
        return motivo;
    }

    public void setMotivo(MotivoRechazoSolicitud motivo) {
        this.motivo = motivo;
    }

    /*public ListaEspera getListaEspera() {
     return listaEspera;
     }

     public void setListaEspera(ListaEspera listaEspera) {
     this.listaEspera = listaEspera;
     }*/
    public boolean isErrorSemestre() {
        return errorSemestre;
    }

    public void setErrorSemestre(boolean errorSemestre) {
        this.errorSemestre = errorSemestre;
    }

    public String getIngresoFormateado() {
        return ingresoFormateado;
    }

    public void setIngresoFormateado(String ingresoFormateado) {
        this.ingresoFormateado = ingresoFormateado;
    }

    public String getEgresado() {
        return egresado;
    }

    public void setEgresado(Integer egresado) {
        this.egresado = egresado == null ? "-" : egresado == 1 ? "Sí" : "No";
    }

    public List<Beca> getProgramasBeca() {
        return programasBeca;
    }

    public String getMensajeRequisitos() {
        return mensajeRequisitos;
    }

    public boolean isValidacionRequisitos() {
        return validacionRequisitos;
    }
    
    public String getCheckboxEgresado() {
        return checkboxEgresado;
    }

    public void setCheckboxEgresado(String checkboxEgresado) {
        this.checkboxEgresado = checkboxEgresado;
        if (checkboxEgresado.equals("on")) {
            alumno.getDatosAcademicos().setEgresado(1);
        } else {
            alumno.getDatosAcademicos().setEgresado(0);
        }
    }
}
