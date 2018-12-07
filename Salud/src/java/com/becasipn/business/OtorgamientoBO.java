package com.becasipn.business;

import com.becasipn.domain.AsignacionResumen;
import com.becasipn.domain.PreasignacionResumen;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.Carrera;
import com.becasipn.persistence.model.ClasificacionSolicitud;
import com.becasipn.persistence.model.Cuestionario;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.Depositos;
import com.becasipn.persistence.model.Documentos;
import com.becasipn.persistence.model.EstatusTarjetaBancaria;
import com.becasipn.persistence.model.IdentificadorOtorgamiento;
import com.becasipn.persistence.model.Proceso;
import com.becasipn.persistence.model.ListaEspera;
import com.becasipn.persistence.model.MotivoRechazoSolicitud;
import com.becasipn.persistence.model.Otorgamiento;
import com.becasipn.persistence.model.OtorgamientoExterno;
import com.becasipn.persistence.model.PadronSubes;
import com.becasipn.persistence.model.SolicitudBeca;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.TipoBeca;
import com.becasipn.persistence.model.TipoBecaPeriodo;
import com.becasipn.persistence.model.UnidadAcademica;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.persistence.model.VWPresupuestoUnidadAcademica;
import com.becasipn.service.Service;
import com.becasipn.util.ExcelExport;
import com.becasipn.util.ExcelTitulo;
import com.becasipn.util.ProgressBarManager;
import com.becasipn.util.Tupla;
import com.opensymphony.xwork2.ActionContext;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.PersistenceException;
import org.apache.log4j.LogManager;
import org.apache.poi.hssf.util.AreaReference;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.DataConsolidateFunction;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFPivotTable;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class OtorgamientoBO extends XlsLoaderBO {

    private final String tokenizer = ",";
    private List<String> alumnosConError = new ArrayList<>();
    private List<String> alumnosOtorgados = new ArrayList<>();
    private String errorAsignacion;
    private boolean asignacionIndividual;
    private int razonNoOtorgamiento;
    private String errorEspera;
    private String titulo;
    private BigDecimal periodoId;
    private BigDecimal nivelId;
    private BigDecimal uAId;
    private BigDecimal estatusId;
    protected static org.apache.log4j.Logger writeFileLog = LogManager.getLogger(OtorgamientoBO.class.getName());

    public OtorgamientoBO(Service service) {
        super(service);
    }

    public static OtorgamientoBO getInstance(Service service) {
        return new OtorgamientoBO(service);
    }

    public OtorgamientoBO(boolean asignacionIndividual, Service service) {
        super(service);
        this.asignacionIndividual = asignacionIndividual;
    }

    public Otorgamiento getOtorgamiento(BigDecimal id) {
        Otorgamiento otorgamiento = service.getOtorgamientoDao().findById(id);
        return otorgamiento;
    }

    public Boolean guardaOtorgamiento(Otorgamiento otorgamiento) {
        try {
            if (otorgamiento.getId() == null) {
                service.getOtorgamientoDao().save(otorgamiento);

            } else {
                service.getOtorgamientoDao().update(otorgamiento);
            }
        } catch (Exception e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public Boolean guardaBajas(String Bajas) {
        Boolean res = true;

        String ids[] = Bajas.split(",");
        for (String id : ids) {
            Otorgamiento otorgamiento = service.getOtorgamientoDao().findById(new BigDecimal(id));
            if (otorgamiento.getAlta() == false) {
                otorgamiento.setAlta(true);
            } else {
                otorgamiento.setAlta(false);
            }
            res = res & guardaOtorgamiento(otorgamiento);
        }
        if (res) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public Boolean guardaBaja(Otorgamiento o, Boolean alta) {
        o.setAlta(alta);
        return guardaOtorgamiento(o);
    }

    //ASIGNA ELEMENTOS EN EL LISTADO DE BAJAS
    public List<Otorgamiento> asignarElementos(List<Object[]> list) {
        List<Otorgamiento> lista = new ArrayList<>();
        for (Object[] res : list) {
            Otorgamiento otorgamiento = (Otorgamiento) res[0];
            Alumno a = (Alumno) res[1];
            Carrera c = (Carrera) res[2];
            UnidadAcademica ua = (UnidadAcademica) res[3];
            TipoBeca tb = (TipoBeca) res[4];
            otorgamiento.setTipoBecaPeriodo(new TipoBecaPeriodo());
            otorgamiento.getTipoBecaPeriodo().setTipoBeca(tb);
            a.setDatosAcademicos(new DatosAcademicos());
            a.getDatosAcademicos().setCarrera(c);
            a.getDatosAcademicos().setUnidadAcademica(ua);
            otorgamiento.setAlumno(a);
            lista.add(otorgamiento);
        }
        return (lista);
    }

    //Obtiene las solicitudes candidatas a preasignacion y lleva a cabo el proceso de preasignación
    public Tupla<List<PreasignacionResumen>, String> preasignacionesMasivas(BigDecimal nivel, BigDecimal unidadAcademica, boolean sobreEscribir) {
        //Variables locales
        Periodo periodoActivo = service.getPeriodoDao().getPeriodoActivo();
        String clave = periodoActivo.getClave();
        List<UnidadAcademica> unidadesAcademicas;
        PreasignacionResumen resumenUA = new PreasignacionResumen();;
        List<PreasignacionResumen> result = new ArrayList<>();
        List<SolicitudBeca> solicitudes;
        Alumno alumno;
        TipoBecaPeriodo becaPeriodo;
        Boolean validarBeca;
        MotivoRechazoSolicitud motivo = new MotivoRechazoSolicitud();
        ProgressBarManager manager = new ProgressBarManager();
        Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");

        ExcelExport excelExport = new ExcelExport();
        String[] encabezado = new String[]{"Unidad Academica", "Boleta", "Apellido Paterno", "Apellido Materno", "Nombre",
            "CURP", "Beca Solicitada", "Transferencia", "Tipo Beca Preasignada", "Rechazo Solicitada", "Rechazo Transferencia"};
        List<Object[]> registrosExcel = new ArrayList<>();

        //Obtener unidades academicas sobre las cuales trabajar la preasignación
        if (nivel.equals(BigDecimal.ZERO)) {
            if (unidadAcademica.equals(BigDecimal.ZERO)) {
                unidadesAcademicas = service.getUnidadAcademicaDao().findAll();
            } else {
                unidadesAcademicas = service.getUnidadAcademicaDao().findBy("id", unidadAcademica.toString(), false);
            }
        } else if (unidadAcademica.equals(BigDecimal.ZERO)) {
            unidadesAcademicas = service.getUnidadAcademicaDao().getUnidadAcademicaPorNivel(nivel);
        } else {
            unidadesAcademicas = service.getUnidadAcademicaDao().findBy("id", unidadAcademica.toString(), false);
        }

        //Obtener todas las solicitudes
        if (clave.charAt(clave.length() - 1) == '1') {
            solicitudes = service.getSolicitudBecaDao().getSolicitudesPreasignacionMasiva(periodoActivo.getId(), null, nivel, unidadAcademica, sobreEscribir);
        } else {
            solicitudes = service.getSolicitudBecaDao().getSolicitudesPreasignacionMasiva(periodoActivo.getId(), periodoActivo.getPeriodoAnterior().getId(), nivel, unidadAcademica, sobreEscribir);
        }

        //Inicializar el progressbarmanager con los totales
        manager.setTotales(solicitudes.size(),
                unidadesAcademicas.size(),
                unidadAcademica.intValue(),
                nivel.intValue());

        //Si la opción esta habilitada, se hace un barrido borrando rechazoBecaSolicitada, rechazoTransferencia y tipoBecaPreasignada
        if (sobreEscribir) {
            if (clave.charAt(clave.length() - 1) == '1') {
                service.getSolicitudBecaDao().borradoPreasignacionMasiva(periodoActivo.getId(), null, nivel, unidadAcademica, sobreEscribir);
            } else {
                service.getSolicitudBecaDao().borradoPreasignacionMasiva(periodoActivo.getId(), periodoActivo.getPeriodoAnterior().getId(), nivel, unidadAcademica, sobreEscribir);
            }
        }

        //Recorrer UA por UA el proceso de preasignación y obtener un resúmen por cada una
        UnidadAcademica uaAnterior = null;
        long preasignados = 0;
        long noPreasignados = 0;
        int total;
        for (Iterator<SolicitudBeca> itSB = solicitudes.iterator(); itSB.hasNext();) {
            SolicitudBeca solicitud = itSB.next();
            if (uaAnterior == null || !solicitud.getAlumno().getDatosAcademicos().getUnidadAcademica().getId().equals(uaAnterior.getId())) {
                if (uaAnterior != null) {
                    resumenUA.setSolicitudesPreasignadasUA(preasignados);
                    resumenUA.setSolicitudesNoPreasignadasUA(noPreasignados);
                    resumenUA.setSolicitudesProcesadasUA(preasignados + noPreasignados);
                    result.add(resumenUA);
                }
                resumenUA = new PreasignacionResumen();
                uaAnterior = solicitud.getAlumno().getDatosAcademicos().getUnidadAcademica();
                resumenUA.setUnidadAcademica(uaAnterior.getNombreCorto());
                preasignados = 0;
                noPreasignados = 0;
                total = service.getSolicitudBecaDao().totalAlumnosSolicitudPeriodoEstatusNivelUA(periodoActivo.getId(), null, null, nivel, uaAnterior.getId(), sobreEscribir).intValue()
                        + service.getSolicitudBecaDao().totalAlumnosSolicitudPeriodoEstatusNivelUA(periodoActivo.getId(), null, new BigDecimal(3), nivel, uaAnterior.getId(), sobreEscribir).intValue();
                if (clave.charAt(clave.length() - 1) == '2') {
                    total += service.getSolicitudBecaDao().totalAlumnosSolicitudPeriodoEstatusNivelUA(periodoActivo.getId(), periodoActivo.getPeriodoAnterior().getId(), new BigDecimal(3), nivel, unidadAcademica, sobreEscribir).intValue();
                }

                //Crear barra de progreso para unidad academica
                manager.cambiarUnidadAcademica(total, uaAnterior.getNombreCorto());
                unidadesAcademicas.remove(uaAnterior);
            }
            alumno = solicitud.getAlumno();
            becaPeriodo = becaSolicitud(solicitud, periodoActivo, alumno.getDatosAcademicos(), false, false, null, null);
            if (becaPeriodo != null) {
                validarBeca = validarBeca(alumno, becaPeriodo, solicitud, alumno.getDatosAcademicos(), null);
                if (!validarBeca) {
                    motivo.setId(new BigDecimal(getRazonNoOtorgamiento()));
                    solicitud.setRechazoBecaSolicitada(motivo);
                    if (solicitud.getPermiteTransferencia() == 1) {
                        motivo = new MotivoRechazoSolicitud();
                        List<TipoBecaPeriodo> becasAplicables = service.getTipoBecaPeriodoDao().becasAplicables(alumno, solicitud, false, false);
                        becaPeriodo = null;
                        if (becasAplicables == null || becasAplicables.isEmpty()) {
                            motivo.setId(new BigDecimal(20));
                            solicitud.setRechazoTransferencia(motivo);
                            manager.aumentarNoPreasignada();
                            noPreasignados++;
                        } else {
                            Iterator<TipoBecaPeriodo> it = becasAplicables.iterator();
                            while (it.hasNext() && !validarBeca) {
                                becaPeriodo = it.next();
                                validarBeca = validarBeca(alumno, becaPeriodo, solicitud, alumno.getDatosAcademicos(), null);
                            }
                            if (!validarBeca) {
                                motivo.setId(new BigDecimal(20));
                                solicitud.setRechazoTransferencia(motivo);
                                manager.aumentarNoPreasignada();
                                noPreasignados++;
                            } else {
                                solicitud.setTipoBecaPreasignada(becaPeriodo);
                                manager.aumentarPreasignada();
                                preasignados++;
                            }
                        }
                    } else {
                        manager.aumentarNoPreasignada();
                        noPreasignados++;
                    }
                } else {
                    solicitud.setTipoBecaPreasignada(becaPeriodo);
                    manager.aumentarPreasignada();
                    preasignados++;
                }
            } else if (solicitud.getPermiteTransferencia() == 1) {
                motivo.setId(new BigDecimal(24));
                solicitud.setRechazoBecaSolicitada(motivo);
                List<TipoBecaPeriodo> becasAplicables = service.getTipoBecaPeriodoDao().becasAplicables(alumno, solicitud, false, false);
                becaPeriodo = null;
                motivo = new MotivoRechazoSolicitud();
                if (becasAplicables == null || becasAplicables.isEmpty()) {
                    motivo.setId(new BigDecimal(20));
                    solicitud.setRechazoTransferencia(motivo);
                    manager.aumentarNoPreasignada();
                    noPreasignados++;
                } else {
                    Iterator<TipoBecaPeriodo> it = becasAplicables.iterator();
                    validarBeca = false;
                    while (it.hasNext() && !validarBeca) {
                        becaPeriodo = it.next();
                        validarBeca = validarBeca(alumno, becaPeriodo, solicitud, alumno.getDatosAcademicos(), null);
                    }
                    if (!validarBeca) {
                        motivo.setId(new BigDecimal(20));
                        solicitud.setRechazoTransferencia(motivo);
                        manager.aumentarNoPreasignada();
                        noPreasignados++;
                    } else {
                        solicitud.setTipoBecaPreasignada(becaPeriodo);
                        manager.aumentarPreasignada();
                        preasignados++;
                    }
                }
            } else {
                motivo.setId(new BigDecimal(25));
                solicitud.setRechazoBecaSolicitada(motivo);
                manager.aumentarNoPreasignada();
                noPreasignados++;
            }
            solicitud.setFechaModificacion(new Date());
            solicitud.setUsuario(usuario);
            service.getSolicitudBecaDao().update(solicitud);
            registrosExcel.add(generaRegistroXLSPreasignacionMasiva(solicitud));
            itSB.remove();
        }

        resumenUA.setSolicitudesPreasignadasUA(preasignados);
        resumenUA.setSolicitudesNoPreasignadasUA(noPreasignados);
        resumenUA.setSolicitudesProcesadasUA(preasignados + noPreasignados);
        result.add(resumenUA);

        String excel = excelExport.crearExcelPreasignacionesMasivas(encabezado, registrosExcel);
        Tupla<List<PreasignacionResumen>, String> tuplaResultado = new Tupla<>(result, excel);

        return tuplaResultado;
    }

    public Object[] generaRegistroXLSPreasignacionMasiva(SolicitudBeca solicitud) {
        Object[] row = new Object[11];
        Alumno alumno = solicitud.getAlumno();
        row[0] = alumno.getDatosAcademicos().getUnidadAcademica().getNombreCorto();
        row[1] = alumno.getBoleta();
        row[2] = alumno.getApellidoPaterno();
        row[3] = alumno.getApellidoMaterno();
        row[4] = alumno.getNombre();
        row[5] = alumno.getCurp();
        row[6] = solicitud.getProgramaBecaSolicitada().getNombre();
        row[7] = solicitud.getPermiteTransferencia() == 1 ? "Si" : "No";
        row[8] = solicitud.getTipoBecaPreasignada() == null ? "N/A" : service.getTipoBecaPeriodoDao().findById(solicitud.getTipoBecaPreasignada().getId()).getTipoBeca().getNombre();
        row[9] = solicitud.getRechazoBecaSolicitada() == null ? "N/A" : service.getMotivoRechazoSolicitudDao().findById(solicitud.getRechazoBecaSolicitada().getId()).getNombre();
        row[10] = solicitud.getRechazoTransferencia() == null ? "N/A" : service.getMotivoRechazoSolicitudDao().findById(solicitud.getRechazoTransferencia().getId()).getNombre();

        return row;
    }

    public List<AsignacionResumen> asignacionAutomaticaNuevos(Integer nivel) {

        //Variables locales
        Periodo periodoActivo = service.getPeriodoDao().getPeriodoActivo();
        ProgressBarManager manager = new ProgressBarManager();
        List<AsignacionResumen> result = new ArrayList<>();

        //Preparar archivo de escritura
        BufferedWriter bufferAsignaciones = null;
        try {
            Calendar calendar = Calendar.getInstance();
            String fileName = calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "_" + calendar.get(Calendar.DAY_OF_MONTH) + "-"
                    + calendar.get(Calendar.HOUR_OF_DAY) + "-" + calendar.get(Calendar.MINUTE) + "-Nuevos-" + nivel;
            bufferAsignaciones = Files.newBufferedWriter(Paths.get("C:\\Reportes\\" + fileName + ".csv"),
                    Charset.forName("UTF-8"));
            bufferAsignaciones.append('\ufeff');
            bufferAsignaciones.append("ESTADO,ESCUELA,BOLETA,BECA,PROM,SEMESTRE,FASE,RAZON,DETALLE\n");
        } catch (IOException ex) {
            Logger.getLogger(OtorgamientoBO.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Paso 2 Por cada presupuesto sacar tipo de beca, presupuesto para preasignacioenss
        Map<Tupla<BigDecimal, BigDecimal>, Integer> disponiblesTipoBeca = new HashMap<>();

        //Paso 3 Inicializar manager con totales
        int tiposDeBeca = service.getTipoBecaPeriodoDao().getCountTiposBecasPeriodoPorNivel(nivel).intValue();
        Long totaldeAlumnosNivel = service.getOtorgamientoDao().getCandidatosNuevos(periodoActivo, nivel);
        manager.setTotalDeElementos((int) (tiposDeBeca * totaldeAlumnosNivel), tiposDeBeca, nivel, 1);

        //Paso 4 Recorrer el presupuesto
        TipoBecaPeriodo tipoBecaCicloAnterior = null;
        List<Alumno> alumnosCandidatos;
        AsignacionResumen tipoBecaEscuela;
        Integer becasDisponibles;
        Otorgamiento o = null;

        try {
            bufferAsignaciones.flush();
            bufferAsignaciones.close();
        } catch (IOException ex) {
            Logger.getLogger(OtorgamientoBO.class.getName()).log(Level.SEVERE, null, ex);
        }
        manager.borraBarra();
        return result;
    }

    public List<AsignacionResumen> asignacionAutomaticaRevalidantes(Integer nivel) {

        writeFileLog.info('\ufeff');
        writeFileLog.info("ESTADO,ESCUELA,BOLETA,BECA ANTERIOR,PROM,SEMESTRE,CARGA,FASE,BECA A REVALIDAR,PROM,SEMESTRE,FASE,RAZON,DETALLE");

        List<AsignacionResumen> result = new ArrayList<>();
        Periodo periodoActivo = service.getPeriodoDao().getPeriodoActivo();
        ProgressBarManager manager = new ProgressBarManager();

        //Inicializar manager 
        manager.setTotalDeElementos(service.getOtorgamientoDao().getCandidatosRevalidacion(periodoActivo, nivel),
                service.getTipoBecaPeriodoDao().getCountTiposBecasPeriodoPorNivel(nivel).intValue(), nivel, 2);

        List<Otorgamiento> otorgamientoTipoBecaUnidad;
        TipoBecaPeriodo tipoBecaCicloAnterior = null;
        AsignacionResumen tipoBecaEscuela;
        Integer becasDisponibles;
        boolean asigno = false;


//        try {
//            bufferAsignaciones.flush();
//            bufferAsignaciones.close();
//        } catch (IOException ex) {
//            Logger.getLogger(OtorgamientoBO.class.getName()).log(Level.SEVERE, null, ex);
//        }
        manager.borraBarra();
        return result;
    }

    public List<AsignacionResumen> asignacionAutomaticaOtorgamientosSeleccionados(Integer nivel) {
        writeFileLog.info('\ufeff');
        writeFileLog.info("ESTADO,ESCUELA,BOLETA,BECA ANTERIOR,PROM,SEMESTRE,CARGA,FASE,BECA A REVALIDAR,PROM,SEMESTRE,FASE,RAZON,DETALLE");

        List<AsignacionResumen> result = new ArrayList<>();
        Periodo periodoActivo = service.getPeriodoDao().getPeriodoActivo();
        ProgressBarManager manager = new ProgressBarManager();




        //Inicializar manager 
        manager.setTotalDeElementos(service.getSolicitudBecaDao().getCountCandidatosBecaSolicitada(periodoActivo, nivel),
                service.getTipoBecaPeriodoDao().getCountTiposBecasPeriodoPorNivel(nivel).intValue(), nivel, 3);

        List<SolicitudBeca> solicitudesTipoBecaUnidad;
        TipoBecaPeriodo tipoBecaPeriodo = null;
        AsignacionResumen tipoBecaEscuela;
        Integer becasDisponibles;
        boolean asigno = false;
        manager.borraBarra();
        return result;
    }

    public int revertirAsignacion() {
        int res = service.getOtorgamientoDao().revertirAsignacion();
        return res;
    }

    /**
     * Implementa metodo vacio
     *
     * @author Mario Márquez
     * @param wb
     * @param periodo
     * @param accion
     * @return lista
     * @throws java.lang.Exception
     */
    @Override
    public List<String> processFile(Workbook wb, BigDecimal periodo, Integer accion) throws Exception {
        return null;
    }

    /**
     * Implementa metodo vacio
     *
     * @author Tania G. Sánchez
     * @param <T>
     * @param wb
     * @param lote
     * @param fecha
     * @return lista
     * @throws java.lang.Exception
     */
    @Override
    public <T> List<T> processFile(Workbook wb, String lote, Date fecha) throws Exception {
        return null;
    }

    /**
     * Implementa metodo vacio
     *
     * @author Tania G. Sánchez
     * @param wb
     * @return lista
     * @throws java.lang.Exception
     */
    @Override
    public <T> List<T> processFile(Workbook wb) throws Exception {
        return null;
    }

    /**
     * Implementa metodo vacio
     *
     * @author Tania G. Sánchez
     * @param <T>
     * @param wb
     * @param periodo
     * @return lista
     * @throws java.lang.Exception
     */
    @Override
    public <T> List<T> processFile(Workbook wb, BigDecimal periodo) throws Exception {
        return null;
    }

    /**
     * Implementa metodo vacio
     *
     * @author Tania G. Sánchez
     * @param <T>
     * @param wb
     * @param unidadAcademica
     * @return lista
     * @throws java.lang.Exception
     */
    @Override
    public <T> List<T> processFile(Workbook wb, UnidadAcademica unidadAcademica) throws Exception {
        return null;
    }

    public TipoBecaPeriodo validarBecalos(Otorgamiento otorgamientoAnterior, Boolean tieneUniversal, Boolean tieneComplementaria) {
        TipoBecaPeriodo nuevo;
        if (otorgamientoAnterior.getFase() == null) {
            razonNoOtorgamiento = 3;//La fase del otorgamiento anterior es nula.
            return null;
        }
        if (otorgamientoAnterior.getFase().equals(otorgamientoAnterior.getTipoBecaPeriodo().getFases())) {
            nuevo = service.getTipoBecaPeriodoDao().siguienteBecalosPeriodo(otorgamientoAnterior.getTipoBecaPeriodo(), otorgamientoAnterior.getAlumno(), tieneUniversal, tieneComplementaria);
            if (nuevo == null) {
                razonNoOtorgamiento = 4;//No se ha creado el tipo de beca por periodo correspondiente a la revalidación que se desea realizar.
                return null;
            }
            return nuevo;
        }
        razonNoOtorgamiento = 5;//Error validando bécalos.
        return null;
    }

    /**
     * Verifica, para alumnos con beca en el periodo anterior, y en base a la
     * fase de esa beca, qué beca le corresponde en el periodo actual.
     *
     * @param otorgamientoAnterior
     * @param tieneUniversal
     * @param tieneComplementaria
     * @param padronSubes
     * @param solicitud
     * @param periodoActivo
     * @param datosAcademicos
     * @return
     */
    public TipoBecaPeriodo validarManutencion(Otorgamiento otorgamientoAnterior,
            Boolean tieneUniversal, Boolean tieneComplementaria, PadronSubes padronSubes,
            SolicitudBeca solicitud, Periodo periodoActivo, DatosAcademicos datosAcademicos) {
        TipoBecaPeriodo nuevo;
        String clave = service.getPeriodoDao().getPeriodoActivo().getClave();
        if (otorgamientoAnterior.getFase() == null) {
            razonNoOtorgamiento = 6;//El campo de fase del otorgamiento es nulo.
            return null;
        }
        if (otorgamientoAnterior.getFase() == 1 && clave.charAt(clave.length() - 1) == '2') {
            nuevo = service.getTipoBecaPeriodoDao().nuevoTipoBecaPeriodo(otorgamientoAnterior.getTipoBecaPeriodo().getId(), null, tieneUniversal, tieneComplementaria, padronSubes);
            return nuevo;
        }
        if ((otorgamientoAnterior.getFase() == 2 || otorgamientoAnterior.getDatosAcademicos().getSemestre() % 2 == 0) && clave.charAt(clave.length() - 1) == '1') {
//            2018-11-05
//            Se solicita que no se verifique el órden de las becas, sino que únicamente se considere el cumplimiento de datos
//            académicos no importando si se repite la beca del periodo anterior
//            nuevo = service.getTipoBecaPeriodoDao().siguienteBecaPeriodo(otorgamientoAnterior.getTipoBecaPeriodo(), tieneUniversal, tieneComplementaria, padronSubes);
            nuevo = service.getTipoBecaPeriodoDao().getBecaAlumnoActual(solicitud.getAlumno(), solicitud.getProgramaBecaSolicitada(), periodoActivo.getId(), datosAcademicos, tieneUniversal, tieneComplementaria, padronSubes);
            return nuevo;
        }
        nuevo = service.getTipoBecaPeriodoDao().nuevoTipoBecaPeriodo(otorgamientoAnterior.getTipoBecaPeriodo().getId(), null, tieneUniversal, tieneComplementaria, padronSubes);
        return nuevo;
    }

    /**
     * Método que verifica que los alumnos que han solicitado beca cultural o
     * deportiva sólo se les pueda preasignar ese tipo de beca periodo
     *
     * @param tbp Tipo de Beca periodo que se intenta asignar
     * @param solicitud Solicitud de beca. Se ocupa el probrama de beca
     * solicitado.
     * @return
     */
    public Boolean verificaPreasignacionDeporteCultura(TipoBecaPeriodo tbp, SolicitudBeca solicitud) {
        //Beca Cultural
        if (solicitud.getProgramaBecaSolicitada().getId().compareTo(new BigDecimal(12)) == 0) {
            if (tbp.getTipoBeca().getBeca().getId().compareTo(new BigDecimal(12)) == 0) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
            //Beca Deportiva    
        } else if (solicitud.getProgramaBecaSolicitada().getId().compareTo(new BigDecimal(13)) == 0) {
            if (tbp.getTipoBeca().getBeca().getId().compareTo(new BigDecimal(13)) == 0) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    public boolean validarBeca(Alumno a, TipoBecaPeriodo tbp, SolicitudBeca solicitud, DatosAcademicos datosAcademicos, Proceso proceso) {
        if (!tbp.getPeriodo().equals(service.getPeriodoDao().getPeriodoActivo())) {
            razonNoOtorgamiento = 46; //El periodo actual no coincide con el de la beca
            return false;
        }
        if (tbp.getValidaciondeinscripcion() == null) {
            return validarDatosAcademicos(a, tbp, solicitud, datosAcademicos, proceso);
        }
        Periodo periodo = tbp.getPeriodo();
        if (datosAcademicos == null) {
            datosAcademicos = service.getDatosAcademicosDao().datosPorPeriodo(a.getId(), periodo.getId());
        }
        switch (tbp.getValidaciondeinscripcion()) {
            case 0:
            case 1:
                return validarDatosAcademicos(a, tbp, solicitud, datosAcademicos, proceso);
            case 2:
                if (datosAcademicos.getInscrito() == 0) {
                    razonNoOtorgamiento = 7;//El alumno no esta inscrito
                    return false;
                } else if (datosAcademicos.getEgresado() == 1) {
                    razonNoOtorgamiento = 8;//El alumno es egresado
                    return false;
                } else {
                    return true;
                }
            case 3:
                if (datosAcademicos.getInscrito() == 0) {
                    razonNoOtorgamiento = 7;//El alumno no esta inscrito
                    return false;
                } else if (datosAcademicos.getEgresado() == 1) {
                    razonNoOtorgamiento = 8;//El alumno es egresado
                    return false;
                } else if (!validarCarga(a, tbp, datosAcademicos)) {
                    razonNoOtorgamiento = 9;//El alumno no cumple con la carga del tipo de beca.
                    return false;
                } else if (tbp.getRegular() == 1 && datosAcademicos.getRegular() == 0) {
                    razonNoOtorgamiento = 10;//El alumno no es regular.
                    return false;
                } else {
                    return true;
                }
            default:
                razonNoOtorgamiento = 11;//no se configuro correctamente este tipo de beca
                return false;
        }
    }

    /**
     * Verifica si el alumno está en el rango de semestres que solicita la beca
     * dada
     *
     * @param alumno
     * @param beca
     * @param datosAcademicos
     * @return
     */
    public boolean validarSemestre(Alumno alumno, TipoBecaPeriodo beca) {
        try {
            Periodo periodo = beca.getPeriodo();

            DatosAcademicos datosAcademicos = service.getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), periodo.getId());
            if (!between(datosAcademicos.getSemestre(), beca.getSemestreMinimo(), beca.getSemestreMaximo())) {
                razonNoOtorgamiento = 13; //El semestre del alumno es invalido.
                return false;
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            razonNoOtorgamiento = 19; //El alumno no tiene sus datos académicos completos en la DAE.
            return false;
        }
        return true;
    }

    public boolean validarDatosAcademicos(Alumno alumno, TipoBecaPeriodo beca, SolicitudBeca solicitud, DatosAcademicos datosAcademicos, Proceso proceso) {
        try {
            Periodo periodo = beca.getPeriodo();

            if (datosAcademicos == null) {
                datosAcademicos = service.getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), periodo.getId());
            }

            if (datosAcademicos.getEgresado() == 1) {
                razonNoOtorgamiento = 8; //El alumno es egresado.
                return false;
            }

//            DatosAcademicos datosAcademicosPasado = service.getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), periodo.getPeriodoAnterior().getId());
//            if (datosAcademicosPasado != null) {
//                if (datosAcademicosPasado.getSemestre().equals(datosAcademicos.getSemestre())) {
//                    razonNoOtorgamiento = 2; //El alumno tiene el mismo semestre que el del otorgamiento anterior.
//                    return false;
//                }
//            }
            // Si son nulos, la beca no cuenta con ese requisito.
            if (beca.getReprobadasMinimo() != null && beca.getReprobadasMaximo() != null) {
                if (!validarMateriasReprobadas(beca, datosAcademicos)) {
                    return false;
                }
            }

            if (!between(datosAcademicos.getPromedio(), beca.getPromedioMinimo(), beca.getPromedioMaximo())) {
                razonNoOtorgamiento = 12; //El promedio del alumno es invalido.
                return false;
            }

            if (!between(datosAcademicos.getSemestre(), beca.getSemestreMinimo(), beca.getSemestreMaximo())) {
                razonNoOtorgamiento = 13; //El semestre del alumno es invalido.
                return false;
            }

            if (beca.getRegular() == 1) {
                if (datosAcademicos.getRegular() != 1) {
                    razonNoOtorgamiento = 10; //El alumno no es regular.
                    return false;
                }
            }

            if (beca.getAreasconocimiento() != null
                    && !datosAcademicos.getUnidadAcademica().getAreasConocimiento().getId().equals(new BigDecimal(4))) {
                BigDecimal areasConocimiento;
                if (datosAcademicos.getUnidadAcademica().getAreasConocimiento().getId().intValue() == 1
                        || datosAcademicos.getUnidadAcademica().getAreasConocimiento().getId().intValue() == 2) {
                    areasConocimiento = new BigDecimal(5);
                } else {
                    areasConocimiento = datosAcademicos.getUnidadAcademica().getAreasConocimiento().getId();
                }
                if (beca.getAreasconocimiento().getId().compareTo(areasConocimiento) != 0) {
                    razonNoOtorgamiento = 14; //El área de conocimiento difiere entre alumno y su otorgamiento.
                    return false;
                }
            }

            if (datosAcademicos.getInscrito() == 0) {
                razonNoOtorgamiento = 7; //El alumno no está inscrito.
                return false;
            }

            if (!beca.getTipoBeca().getBeca().getId().equals(new BigDecimal(5)) && !beca.getTipoBeca().getBeca().getId().equals(new BigDecimal(7))) {
                //No validar carga si el proceso es de tipo Validación (7)
                if (proceso == null || (proceso != null && !proceso.getTipoProceso().getMovimiento().getId().equals(new BigDecimal(7)))) {
                    if (!validarCarga(alumno, beca, datosAcademicos)) {
                        razonNoOtorgamiento = 9; //El alumno no cumple con la carga del tipo de beca.
                        return false;
                    }
                }
            }

            if (beca.getModalidad() != null) {
                if (beca.getModalidad().getId().intValue() != datosAcademicos.getModalidad().getId().intValue()) {
                    razonNoOtorgamiento = 15; //La modalidad no es compatible.
                    return false;
                }
            }

            if (datosAcademicos.getSemestre() > datosAcademicos.getCarrera().getNumeroSemestres()) {
                razonNoOtorgamiento = 16; //El semestre del alumno es mayor que el semestre maximo de su carrera.
                return false;
            }

            /*if (beca.getVulnerabilidadSubes() != null && beca.getVulnerabilidadSubes()) {
             if (!solicitud.getVulnerabilidadSubes()) {
             razonNoOtorgamiento = 26;
             return false;
             }
             }*/
            if (solicitud.getPeriodo().getId().equals(periodo.getId())) {
                if (beca.getIngresoSalarios() != null) {
                    if (solicitud.getIngresosPercapitaPesos() > beca.getIngresoSalarios()) {
                        razonNoOtorgamiento = 18; //Los ingresos son mayores a los de la beca.
                        return false;
                    }
                }
            }

        } catch (NullPointerException ex) {
            ex.printStackTrace();
            razonNoOtorgamiento = 19; //El alumno no tiene sus datos académicos completos en la DAE.
            return false;
        }
        return true;
    }

    private boolean between(Integer num, Integer i1, Integer i2) {
        return num >= i1 && num <= i2;
    }

    private boolean between(Double num, Float i1, Float i2) {
        return num.floatValue() >= i1 && num.floatValue() <= i2;
    }

    private void imprimirOtorgamientoNuevo(String tipo, String escuela, String boleta, String beca, Double promedio, Integer semestre,
            String clave, BufferedWriter bufferNuevos) {
        StringBuilder sb = new StringBuilder();
        sb.append(tipo).append(tokenizer);

        //ESTADO,ESCUELA,BOLETA,BECA,PROM,SEMESTRE,FASE,RAZON,DETALLE
        sb.append(escuela).append(tokenizer);                   // ESCUELA
        sb.append(boleta).append(tokenizer);                    // BOLETA 
        sb.append(beca).append(tokenizer);                      // BECA
        sb.append(promedio).append(tokenizer);                  // PROMEDIO DEL OTORGAMIENTO
        sb.append(semestre).append(tokenizer);                  // SEMESTRE DEL OTORGAMIENTO
        sb.append(clave).append(tokenizer);

        /*if (razonNoOtorgamiento != null || !razonNoOtorgamiento.isEmpty()) {
         String[] split = razonNoOtorgamiento.split("::");
         sb.append(split[0]);
         if (split.length > 1) {
         sb.append(tokenizer).append(split[1]);
         }
         }*/
        sb.append("\n");

        try {
            bufferNuevos.write(sb.toString());
            bufferNuevos.flush();
        } catch (IOException ex) {
            Logger.getLogger(OtorgamientoBO.class.getName()).log(Level.SEVERE, null, ex);
        }

        //razonOtorgamiento = "";
    }

    private void imprimirOtorgamientoNuevo(String tipo, Alumno alumno, TipoBecaPeriodo tbp, BufferedWriter bufferNuevos) {
        String clave = tbp.getTipoBeca().getBeca().getClave();
        String claveTBP = "";
        if (clave.equals("M") || clave.equals("B")) {
            claveTBP = String.valueOf(tbp.getFases());
        }
        Periodo periodo = tbp.getPeriodo();
        DatosAcademicos datosAcademicos = service.getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), periodo.getId());
        imprimirOtorgamientoNuevo(tipo, datosAcademicos.getUnidadAcademica().getNombreCorto(), alumno.getBoleta(), tbp.getTipoBeca().getNombre(),
                datosAcademicos.getPromedio(), datosAcademicos.getSemestre(), claveTBP, bufferNuevos);
    }

    private void imprimirOtorgamientoNuevo(String tipo, Otorgamiento otorgamiento, BufferedWriter bufferNuevos) {
        String clave = otorgamiento.getTipoBecaPeriodo().getTipoBeca().getBeca().getClave();
        String claveTBP = "";
        if (clave.equals("M") || clave.equals("B")) {
            claveTBP = String.valueOf(otorgamiento.getTipoBecaPeriodo().getFases());
        }
        imprimirOtorgamientoNuevo(tipo, otorgamiento.getDatosAcademicos().getUnidadAcademica().getNombreCorto(), otorgamiento.getAlumno().getBoleta(),
                otorgamiento.getTipoBecaPeriodo().getTipoBeca().getNombre(), otorgamiento.getDatosAcademicos().getPromedio(), otorgamiento.getDatosAcademicos().getSemestre(), claveTBP, bufferNuevos);
    }

    private void impirmirOtorgamiento(String tipo, Otorgamiento otorgamientoAnterior, Otorgamiento nuevoOtorgamiento) {
        StringBuilder sb = new StringBuilder();
        sb.append(tipo).append(tokenizer);

        //ESTADO,ESCUELA,BOLETA,BECA ANTERIOR,PROM,SEMESTRE,FASE,BECA A REVALIDADAR,PROM,SEMESTRE,FASE,RAZON,DETALLE\n
        sb.append(otorgamientoAnterior.getDatosAcademicos().getUnidadAcademica().getNombreCorto()).append(tokenizer);  // ESCUELA
        sb.append(otorgamientoAnterior.getAlumno().getBoleta()).append(tokenizer);                            // BOLETA 
        sb.append(otorgamientoAnterior.getTipoBecaPeriodo().getTipoBeca().getNombre()).append(tokenizer);     // BECA
        sb.append(otorgamientoAnterior.getDatosAcademicos().getPromedio()).append(tokenizer);                                      // PROMEDIO DEL OTORGAMIENTO
        sb.append(otorgamientoAnterior.getDatosAcademicos().getSemestre()).append(tokenizer);                                      // SEMESTRE DEL OTORGAMIENTO
        String clave = otorgamientoAnterior.getTipoBecaPeriodo().getTipoBeca().getBeca().getClave();
        if (clave.equals("M") || clave.equals("B")) {                                                         // BECALOS y MANUTENCIÓN
            sb.append(otorgamientoAnterior.getFase()).append(tokenizer);
        } else {
            sb.append("").append(tokenizer);                                                                  // SI NO AGREGAR COMA
        }

        if (nuevoOtorgamiento != null) {
            sb.append(nuevoOtorgamiento.getTipoBecaPeriodo().getTipoBeca().getNombre()).append(tokenizer);
        } else {
            sb.append("").append(tokenizer);
        }

        if (nuevoOtorgamiento != null) {
            sb.append(nuevoOtorgamiento.getDatosAcademicos().getPromedio()).append(tokenizer);                                     // PROMEDIO DEL ALUMNO
            sb.append(nuevoOtorgamiento.getDatosAcademicos().getSemestre()).append(tokenizer);                                     // SEMESTRE DEL ALUMNO
            sb.append(nuevoOtorgamiento.getDatosAcademicos().getCumpleCargaMinima());
        } else {
            sb.append(otorgamientoAnterior.getDatosAcademicos().getPromedio()).append(tokenizer);                       // PROMEDIO DEL ALUMNO
            sb.append(otorgamientoAnterior.getDatosAcademicos().getSemestre()).append(tokenizer);                       // SEMESTRE DEL ALUMNO
            sb.append(nuevoOtorgamiento.getDatosAcademicos().getCumpleCargaMinima());
        }

        if (nuevoOtorgamiento == null) {
            sb.append("").append(tokenizer);
        } else {
            clave = nuevoOtorgamiento.getTipoBecaPeriodo().getTipoBeca().getBeca().getClave();
            if (clave.equals("M") || clave.equals("B")) {                                                         // BECALOS y MANUTENCIÓN
                sb.append(otorgamientoAnterior.getFase()).append(tokenizer);
            } else {
                sb.append("").append(tokenizer);                                                                  // SI NO AGREGAR COMA
            }
        }

        /*if (razonNoOtorgamiento != null || !razonNoOtorgamiento.isEmpty()) {
         String[] split = razonNoOtorgamiento.split("::");
         sb.append(split[0]);
         if (split.length > 1) {
         sb.append(tokenizer).append(split[1]);
         }
         }
         writeFileLog.info(sb.toString());

         razonNoOtorgamiento = "";*/
    }

    private void impirmirSolicitud(String tipo, SolicitudBeca solicitud) {
        StringBuilder sb = new StringBuilder();
        sb.append(tipo).append(tokenizer);

        //ESTADO,ESCUELA,BOLETA,BECA,PROM,SEMESTRE,FASE,RAZON,DETALLE
        sb.append(solicitud.getAlumno().getDatosAcademicos().getUnidadAcademica().getNombreCorto()).append(tokenizer);                   // ESCUELA
        sb.append(solicitud.getAlumno().getBoleta()).append(tokenizer);                    // BOLETA 
        sb.append(solicitud.getProgramaBecaSolicitada().getNombre()).append(tokenizer);                      // BECA
        sb.append(solicitud.getAlumno().getDatosAcademicos().getPromedio()).append(tokenizer);                  // PROMEDIO DEL OTORGAMIENTO
        sb.append(solicitud.getAlumno().getDatosAcademicos().getSemestre()).append(tokenizer);                  // SEMESTRE DEL OTORGAMIENTO
        sb.append("").append(tokenizer);
        /*if (razonNoOtorgamiento != null || !razonNoOtorgamiento.isEmpty()) {
         String[] split = razonNoOtorgamiento.split("::");
         sb.append(split[0]);
         if (split.length > 1) {
         sb.append(tokenizer).append(split[1]);
         }
         }
         sb.append("\n");
         writeFileLog.info(sb.toString());
         razonNoOtorgamiento = "";*/
    }

    public void rechazoMasivo(Proceso proceso, List<String> solicitudes,
            MotivoRechazoSolicitud motivo, Usuario usuario) {

        alumnosConError = new ArrayList();
        alumnosOtorgados = new ArrayList();

        for (String solicitude : solicitudes) {
            SolicitudBeca solicitud = service.getSolicitudBecaDao().findById(new BigDecimal(solicitude));
            if (solicitud == null) {
                mensajesAsignaciones(false, solicitud.getAlumno(), "No tiene una solicitud correcta");
            } else {
                rechazar(solicitud, motivo, proceso, usuario);
            }
        }
    }

    public void esperaMasivo(Proceso proceso, List<String> solicitudes, Usuario usuario) {

        alumnosConError = new ArrayList();
        alumnosOtorgados = new ArrayList();

        for (String solicitude : solicitudes) {
            SolicitudBeca solicitud = service.getSolicitudBecaDao().findById(new BigDecimal(solicitude));
            if (solicitud == null) {
                mensajesAsignaciones(false, solicitud.getAlumno(), "No tiene una solicitud correcta");
            } else if (!listaEspera(solicitud, proceso, usuario)) {
                mensajesAsignaciones(false, solicitud.getAlumno(), errorEspera);
            } else {
                mensajesAsignaciones(true, solicitud.getAlumno(), "Se envió a lista de espera correctamente.");
            }
        }

    }

    public void reversionMasiva(List<String> alumnosId, List<String> solicitudIDs, int tab, Proceso proceso) {
        try {
            alumnosConError = new ArrayList();
            alumnosOtorgados = new ArrayList();

            List<Alumno> listaAlumnos = new ArrayList<>();
            for (String id : alumnosId) {
                listaAlumnos.add(service.getAlumnoDao().findById(new BigDecimal(id)));
            }

            Periodo periodoActivo = service.getPeriodoDao().getPeriodoActivo();

            List<SolicitudBeca> listaSolicitudes = new ArrayList<>();
            for (String solicitudId : solicitudIDs) {
                listaSolicitudes.add(service.getSolicitudBecaDao().findById(new BigDecimal(solicitudId)));
            }
            for (int contador = 0; contador < listaAlumnos.size(); contador++) {
                switch (tab) {
                    case 1:
                        Otorgamiento o = service.getOtorgamientoDao().getOtorgamientoSolicitudPeriodo(listaSolicitudes.get(contador).getId(), periodoActivo.getId());

                        if (!service.getAlumnoDao().tieneOtorgamiento(listaAlumnos.get(contador).getId())) {
                            mensajesAsignaciones(false, listaAlumnos.get(contador), "No tiene otorgamiento que revertir.");
                        } else if (o.getTipoBecaPeriodo().getTipoBeca().getBeca().getClave().equals("M")
                                && service.getAlumnoDao().tieneOtorgamientoTransporte(listaAlumnos.get(contador).getId(), 1, periodoActivo)) {
                            mensajesAsignaciones(false, listaAlumnos.get(contador), "Tiene beca Apoya tu transporte asignada.");
                        } else if (tieneDepositos(o)) {
                            mensajesAsignaciones(false, listaAlumnos.get(contador), "No se pudo realizar la reversión debido a que ya tiene depósitos asociados.");
                        } else if (!revertirOtorgamiento(o)) {
                            mensajesAsignaciones(false, listaAlumnos.get(contador), "No se pudo realizar la reversión.");
                        } else {
                            mensajesAsignaciones(true, listaAlumnos.get(contador), "La reversión fue realizada correctamente.");
                        }
                        break;
                    case 2:
                        if (!revertirRechazo(listaSolicitudes.get(contador))) {
                            mensajesAsignaciones(false, listaAlumnos.get(contador), "No se pudo realizar la reversión.");
                        } else {
                            mensajesAsignaciones(true, listaAlumnos.get(contador), "La reversión fue realizada correctamente.");
                        }
                        break;
                    case 3:
                        if (!revertirLista(listaSolicitudes.get(contador), proceso)) {
                            mensajesAsignaciones(false, listaAlumnos.get(contador), "No se pudo realizar la reversión.");
                        } else {
                            mensajesAsignaciones(true, listaAlumnos.get(contador), "La reversión fue realizada correctamente.");
                        }
                        break;

                }

            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public Boolean revertirRechazo(SolicitudBeca solicitudBeca) {
        Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        ListaEspera lista = service.getListaEsperaDao().findBySolicitud(solicitudBeca.getId(), false);
        solicitudBeca.setClasificacionSolicitud(null);
        solicitudBeca.setUsuario(usuario);
        solicitudBeca.setProceso(null);
        solicitudBeca.setMotivoRechazo(null);
        solicitudBeca.setFechaModificacion(new Date());
        if (lista != null) {
            lista.setVigente(1);
            solicitudBeca.setClasificacionSolicitud(new ClasificacionSolicitud(new BigDecimal(3)));
        }
        try {
            service.getSolicitudBecaDao().update(solicitudBeca);
            if (lista != null) {
                service.getListaEsperaDao().update(lista);
            }
        } catch (Exception e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public Boolean revertirLista(SolicitudBeca solicitudBeca, Proceso proceso) {
        Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        solicitudBeca.setClasificacionSolicitud(null);
        solicitudBeca.setUsuario(usuario);
        solicitudBeca.setFechaModificacion(new Date());
        try {
            service.getSolicitudBecaDao().update(solicitudBeca);
            ListaEspera lista = service.getListaEsperaDao().findBySolicitudProceso(solicitudBeca.getId(), proceso.getId());
            service.getListaEsperaDao().delete(lista);
        } catch (Exception e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;

    }

    public void asignacionMasiva(Proceso proceso, List<String> solicitudes,
            Usuario usuario) {

        alumnosConError = new ArrayList();
        alumnosOtorgados = new ArrayList();
        Periodo periodoActivo = service.getPeriodoDao().getPeriodoActivo();
        String clave = periodoActivo.getClave();
        Otorgamiento oAnterior;
        Boolean tieneUniversal, tieneComplementaria;
        TipoBecaPeriodo complementaria;

        for (String solicitude : solicitudes) {
            SolicitudBeca solicitud = service.getSolicitudBecaDao().findById(new BigDecimal(solicitude));
            if (solicitud == null) {
                mensajesAsignaciones(false, null, "No es una solicitud correcta.");
            } else if (solicitud.getTipoBecaPreasignada() == null) {
                mensajesAsignaciones(false, solicitud.getAlumno(), "No tiene preasignacion esta solicitud.");
            } else if (clave.charAt(clave.length() - 1) == '2') {
                oAnterior = service.getOtorgamientoDao().getOtorgamientoSolicitudPeriodo(solicitud.getId(), periodoActivo.getPeriodoAnterior().getId());
                tieneUniversal = service.getOtorgamientoDao().tieneOtorgamientoUniversal(solicitud.getAlumno().getId(), periodoActivo.getId());
                tieneComplementaria = service.getOtorgamientoDao().tieneOtorgamientoComplementaria(solicitud.getAlumno().getId(), periodoActivo.getId());
                if (oAnterior != null) {
                    //TODO verificar si el nulo que se está mandando aplica correctamente en la siguiuente función:
                    TipoBecaPeriodo tbp = service.getTipoBecaPeriodoDao().nuevoTipoBecaPeriodo(oAnterior.getTipoBecaPeriodo().getId(), periodoActivo, tieneUniversal, tieneComplementaria, null);
                    otorgarBeca(proceso, solicitud.getAlumno(), tbp, solicitud, usuario, null);
                } else if (tieneUniversal && !tieneComplementaria) {
                    complementaria = service.getTipoBecaPeriodoDao().getComplementaria(solicitud.getTipoBecaPreasignada().getId());
                    otorgarBeca(proceso, solicitud.getAlumno(), complementaria, solicitud, usuario, null);
                } else {
                    otorgarBeca(proceso, solicitud.getAlumno(), solicitud.getTipoBecaPreasignada(), solicitud, usuario, null);
                }
            } else {
                tieneUniversal = service.getOtorgamientoDao().tieneOtorgamientoUniversal(solicitud.getAlumno().getId(), periodoActivo.getId());
                tieneComplementaria = service.getOtorgamientoDao().tieneOtorgamientoComplementaria(solicitud.getAlumno().getId(), periodoActivo.getId());
                if (tieneUniversal && !tieneComplementaria) {
                    complementaria = service.getTipoBecaPeriodoDao().getComplementaria(solicitud.getTipoBecaPreasignada().getId());
                    otorgarBeca(proceso, solicitud.getAlumno(), complementaria, solicitud, usuario, null);
                } else {
                    otorgarBeca(proceso, solicitud.getAlumno(), solicitud.getTipoBecaPreasignada(), solicitud, usuario, null);
                }
            }
        }
    }

    public boolean otorgarBeca(Proceso proceso, Alumno alumno,
            TipoBecaPeriodo beca, SolicitudBeca solicitudBeca, Usuario usuario,
            String observaciones) {

        Periodo periodoActivo = service.getPeriodoDao().getPeriodoActivo();
        proceso = service.getProcesoDao().findById(proceso.getId());
        String clave = periodoActivo.getClave();

        if (beca != null) {
            beca = service.getTipoBecaPeriodoDao().findById(beca.getId());
        }

        String boletaCurpDuplicado = validaDuplicidadAlumnoCURP(alumno, proceso.getPeriodo());
        if (boletaCurpDuplicado != null) {
            mensajesAsignaciones(false, alumno, "El CURP del alumno especificado (" + alumno.getCurp() + ") ya ha sido otorgado a través del alumno con boleta " + boletaCurpDuplicado + ".");
            return false;
        }

        if (!validaProcesoPrograma(beca, proceso)) {
            mensajesAsignaciones(false, alumno, "No es posible otorgar esta beca en este proceso.");
            return false;
        }

        if (!validarPresupuesto(beca, alumno)) {
            return false;
        }

        if (!validarOtorgamientosActuales(alumno, beca, periodoActivo)) {
            return false;
        }

//        if (service.getAlumnoDao().tieneBaja(alumno.getId())) {
//            mensajesAsignaciones(false, alumno, "El alumno tiene una baja.");
//            return false;
//        }
        if (!validarBeca(alumno, beca, solicitudBeca, null, proceso)) {
            String razon = service.getMotivoRechazoSolicitudDao().findById(new BigDecimal(razonNoOtorgamiento)).getNombre();
            mensajesAsignaciones(false, alumno, razon);
            return false;
        }

        if (proceso.getTipoProceso().getMovimiento().getId().equals(BigDecimal.ONE)) {
            Otorgamiento otorgamientoAnterior = service.getOtorgamientoDao().getOtorgamientoSolicitudPeriodo(solicitudBeca.getId(), periodoActivo.getPeriodoAnterior().getId());
            if (otorgamientoAnterior.getTipoBecaPeriodo().getTipoBeca().getId().equals(beca.getTipoBeca().getId())) {
                mensajesAsignaciones(false, alumno, "No debe revalidar la beca del periodo anterior ya que es una transferencia.");
                return false;
            }
        }

        if (!validarDocumentos(alumno, beca, proceso)) {
            mensajesAsignaciones(false, alumno, "No cuenta con la documentación completa.");
            return false;
        }

        if (!validarRechazo(solicitudBeca)) {
            mensajesAsignaciones(false, alumno, "La solicitud ya tiene rechazo.");
            return false;
        }

        if (!validarListaEspera(solicitudBeca, proceso)) {
            mensajesAsignaciones(false, alumno, "La solicitud ya está en lista de espera de este proceso.");
            return false;
        }

        return guardaNuevoOtorgamiento(alumno, periodoActivo,
                solicitudBeca, beca, proceso, usuario, observaciones);
    }

    /**
     * Método que permite realizar otorgamientos desde el proceso de importación
     * de la información de SUBES. Se utilizó uno particular ya que a diferencia
     * de un otorgamiento para solicitudes ordinarias y de transporte, se
     * requiere que no se valide ni documentos ni información académica
     *
     * @param proceso Proceso en el que se requiere otorgar la beca
     * @param alumno Alumno que gozará de una beca
     * @param beca El tipo de beca periodo que se le será asignado
     * @param solicitudBeca La solicitud de la que parte el otorgamiento
     * @param usuario Usuario que realiza el otorgamiento
     * @param observaciones Texto que se puede enviar para establecer en el
     * otorgamiento.
     * @return
     */
    public boolean otorgarBecaSoloSUBES(Proceso proceso, Alumno alumno,
            TipoBecaPeriodo beca, SolicitudBeca solicitudBeca, Usuario usuario,
            String observaciones) {

        Periodo periodoActivo = service.getPeriodoDao().getPeriodoActivo();
        proceso = service.getProcesoDao().findById(proceso.getId());

        if (beca != null) {
            beca = service.getTipoBecaPeriodoDao().findById(beca.getId());
        }

        String boletaCurpDuplicado = validaDuplicidadAlumnoCURP(alumno, proceso.getPeriodo());
        if (boletaCurpDuplicado != null) {
            mensajesAsignaciones(false, alumno, "El alumno con CURP (" + alumno.getCurp() + ") ya ha sido otorgado");
            return false;
        }

        if (!validaProcesoPrograma(beca, proceso)) {
            mensajesAsignaciones(false, alumno, "No es posible otorgar esta beca en este proceso.");
            return false;
        }

//        Se solicita que no se valide el semestre
//        if(!validarSemestre(alumno, beca)){
//            mensajesAsignaciones(false, alumno, "El semestre del alumno es invalido.");
//            return false;
//        }
        if (!validarPresupuesto(beca, alumno)) {
            return false;
        }

        if (!validarOtorgamientosActuales(alumno, beca, periodoActivo)) {
            return false;
        }

        if (proceso.getTipoProceso().getMovimiento().getId().equals(BigDecimal.ONE)) {
            Otorgamiento otorgamientoAnterior = service.getOtorgamientoDao().getOtorgamientoSolicitudPeriodo(solicitudBeca.getId(), periodoActivo.getPeriodoAnterior().getId());
            if (otorgamientoAnterior.getTipoBecaPeriodo().getTipoBeca().getId().equals(beca.getTipoBeca().getId())) {
                mensajesAsignaciones(false, alumno, "No debe revalidar la beca del periodo anterior ya que es una transferencia.");
                return false;
            }
        }

        if (!validarRechazo(solicitudBeca)) {
            mensajesAsignaciones(false, alumno, "La solicitud ya tiene rechazo.");
            return false;
        }

        if (!validarListaEspera(solicitudBeca, proceso)) {
            mensajesAsignaciones(false, alumno, "La solicitud ya está en lista de espera de este proceso.");
            return false;
        }
        return guardaNuevoOtorgamiento(alumno, periodoActivo,
                solicitudBeca, beca, proceso, usuario, observaciones);
    }

    /**
     * Método que guarda un otorgamiento e indica si se realizó o no
     *
     * @param alumno
     * @param periodoActivo
     * @param solicitudBeca
     * @param beca
     * @param proceso
     * @param usuario
     * @param observaciones
     * @return
     */
    private boolean guardaNuevoOtorgamiento(Alumno alumno, Periodo periodoActivo,
            SolicitudBeca solicitudBeca, TipoBecaPeriodo beca, Proceso proceso, Usuario usuario,
            String observaciones) {

        DatosAcademicos datos = service.getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), periodoActivo.getId());
        int fase = 1;
        if (!solicitudBeca.getPeriodo().getId().equals(periodoActivo.getId())) {
            fase++;
        }
        if (!beca.getTipoBeca().getBeca().getId().equals(new BigDecimal(5)) && solicitudBeca.getProgramaBecaSolicitada().getId().equals(new BigDecimal(5))) {
            solicitudBeca.setCuestionario(new Cuestionario(BigDecimal.ONE));
        }
        Otorgamiento nuevoOtorgamiento = new Otorgamiento(alumno, periodoActivo, beca, proceso, usuario, observaciones, fase, datos, solicitudBeca);
        ListaEspera listaEspera = service.getListaEsperaDao().findBySolicitud(solicitudBeca.getId(), true);
        if (!validarBecasExternas(alumno)) {
//            mensajesAsignaciones(false, alumno, "El alumno ya cuenta con una beca en un programa externo: " + becasExternas);
            nuevoOtorgamiento.setExcluirDeposito(Boolean.TRUE);
            nuevoOtorgamiento.setIdentificadorOtorgamiento(new IdentificadorOtorgamiento(new BigDecimal(15)));
        }

        try {
            service.getOtorgamientoDao().save(nuevoOtorgamiento);
            if (!proceso.getTipoProceso().getMovimiento().getId().equals(new BigDecimal(1)) //Transferencias
                    && !proceso.getTipoProceso().getMovimiento().getId().equals(new BigDecimal(7))) { //Validaciones
                solicitudBeca.setClasificacionSolicitud(new ClasificacionSolicitud(BigDecimal.ONE));
                service.getSolicitudBecaDao().update(solicitudBeca);
            }

            if (listaEspera != null) {
                listaEspera.setVigente(0);
                service.getListaEsperaDao().update(listaEspera);
            }

            mensajesAsignaciones(true, alumno, "Se realizo la asignación correctamente.");
            return true;
        } catch (Exception e) {
            if (e instanceof PersistenceException) {
                log.warn("Otorgamiento duplicado" + alumno.getBoleta() + "\t" + nuevoOtorgamiento.getTipoBecaPeriodo().getTipoBeca().getNombre());
            } else {
                log.warn("Error inesperado: " + e.getMessage());
                service.getOtorgamientoDao().delete(nuevoOtorgamiento);
                if (listaEspera != null) {
                    listaEspera.setVigente(1);
                    service.getListaEsperaDao().update(listaEspera);
                }
                solicitudBeca.setClasificacionSolicitud(null);
                service.getSolicitudBecaDao().update(solicitudBeca);
            }
            return false;
        }
    }

    public Boolean validarOtorgamientosActuales(Alumno alumno, TipoBecaPeriodo beca, Periodo periodoActivo) {

        List<Otorgamiento> otorgamientosActuales = service.getOtorgamientoDao().getOtorgamientosAlumno(alumno.getId(), periodoActivo.getId());
        boolean traeManutencion = false;
        if (otorgamientosActuales != null) {
            for (Otorgamiento otorgamientoActual : otorgamientosActuales) {
                if (otorgamientoActual != null) {
                    if (!otorgamientoActual.getTipoBecaPeriodo().getTipoBeca().getBeca().getClave().equals("BU")) {
                        if (otorgamientoActual.getTipoBecaPeriodo().getTipoBeca().getBeca().getClave().equals("M")) {
                            traeManutencion = true;
                        }
                        if ((!otorgamientoActual.getTipoBecaPeriodo().getTipoBeca().getBeca().getClave().equals("M") || !beca.getTipoBeca().getBeca().getClave().equals("TM"))
                                && (!otorgamientoActual.getTipoBecaPeriodo().getTipoBeca().getBeca().getClave().equals("I") || !beca.getTipoBeca().getBeca().getClave().equals("TI"))
                                && (!otorgamientoActual.getTipoBecaPeriodo().getTipoBeca().getBeca().getClave().equals("TI") || !beca.getTipoBeca().getBeca().getClave().equals("I"))) {
                            mensajesAsignaciones(false, alumno, "Ya tiene un otorgamiento no compatible.");
                            return false;
                        }
                    }
                }
            }
        }
        if (beca.getTipoBeca().getBeca().getClave().equals("TM") && !traeManutencion) {
            mensajesAsignaciones(false, alumno, "No tiene beca manuteción asignada.");
            return false;
        }
        return true;
    }

    public void revertirPreasignacion(BigDecimal alumnoId) {
        List<Otorgamiento> preasignaciones = service.getOtorgamientoDao().getPreasignaciones(alumnoId);
        if (preasignaciones != null && !preasignaciones.isEmpty()) {
            for (Otorgamiento preasignacion : preasignaciones) {
                service.getOtorgamientoDao().delete(preasignacion);
            }
        }
    }

    /**
     * Verifica si el otorgamiento ya tiene depósitos asociados
     *
     * @param otorgamiento
     * @return
     */
    public Boolean tieneDepositos(Otorgamiento otorgamiento) {
        List<Depositos> depositosAlumno = service.getDepositosDao().depositosOtorgamiento(otorgamiento.getId());
        if (depositosAlumno != null && !depositosAlumno.isEmpty()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * Elimina el registro del otorgamiento, y los registros de bitácora
     * asociados a éste Si el otorgamiento usa la solicitud del periodo
     * anterior, no la actualiza
     *
     * @param otorgamiento
     * @return
     */
    public Boolean revertirOtorgamiento(Otorgamiento otorgamiento) {
        Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        SolicitudBeca solicitudBeca = otorgamiento.getSolicitudBeca();
        ListaEspera listaEspera = service.getListaEsperaDao().findBySolicitud(solicitudBeca.getId(), false);

        //Borramos bitácora
        service.getBitacoraOtorgamientosDao().eliminaBitacora(otorgamiento.getId());

        try {
            service.getOtorgamientoDao().delete(otorgamiento);
            if (listaEspera != null) {
                solicitudBeca.setClasificacionSolicitud(new ClasificacionSolicitud(new BigDecimal(3)));
                listaEspera.setVigente(1);
                service.getListaEsperaDao().update(listaEspera);
            }
            if (otorgamiento.getPeriodo().getId() == otorgamiento.getSolicitudBeca().getPeriodo().getId()) {
                solicitudBeca.setClasificacionSolicitud(null);
                solicitudBeca.setUsuario(usuario);
                solicitudBeca.setFechaModificacion(new Date());
                service.getSolicitudBecaDao().update(solicitudBeca);
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private void mensajesAsignaciones(boolean exito, Alumno alumno, String observaciones) {

        if (!asignacionIndividual) {
            StringBuilder sb = new StringBuilder();
            if (alumno == null) {
                sb.append("[\"")
                        .append("N/A").append("\",\"")
                        .append("N/A").append("\",\"")
                        .append(observaciones).append("\"]");
            } else {
                sb.append("[\"")
                        .append(alumno.getBoleta()).append("\",\"")
                        .append(alumno.getFullName()).append("\",\"")
                        .append(observaciones).append("\"]");
            }
            if (exito) {
                alumnosOtorgados.add(sb.toString());
            } else {
                alumnosConError.add(sb.toString());
            }
        } else {
            errorAsignacion = observaciones;
        }
    }

    private Boolean validarDocumentos(Alumno alumno, TipoBecaPeriodo beca, Proceso proceso) {
        Documentos documentos = service.getDocumentosDao().documentosAlumnoPeriodoActual(alumno);
        //Los procesos de Validaciones no verifican documentos
        if (proceso.getTipoProceso().getMovimiento().getId().equals(new BigDecimal(7))) {
            return Boolean.TRUE;
        }

        if (documentos == null) {
            return Boolean.FALSE;
        }
        switch (beca.getTipoBeca().getBeca().getId().intValue()) {
            case 5:
                if (!documentos.getAcuseSubes()
                        || !documentos.getCartaCompromiso()
                        || !documentos.getComprobanteIngresosEgresos()
                        || !documentos.getCurp()
                        || !documentos.getEstudioSocioeconomico()) {
                    return Boolean.FALSE;
                }
                break;
            case 7:
                if (!documentos.getAcuseSubes()
                        || !documentos.getAcuseSubesTransporte()
                        || !documentos.getCartaCompromiso()
                        || !documentos.getComprobanteIngresosEgresos()
                        || !documentos.getCurp()
                        || !documentos.getEstudioSocioeconomico()) {
                    return Boolean.FALSE;
                }
                break;
            case 8:
                if (!documentos.getCartaCompromiso()
                        || !documentos.getComprobanteIngresosEgresos()
                        || !documentos.getCurp()
                        || !documentos.isEstudiosocieconomicotransporte()
                        || !service.getCuestionarioTransporteDao().tieneEseTransporte(alumno)) {
                    return Boolean.FALSE;
                }
                break;
            default:
                if (!documentos.getCartaCompromiso()
                        || !documentos.getComprobanteIngresosEgresos()
                        || !documentos.getCurp()
                        || !documentos.getEstudioSocioeconomico()) {
                    return Boolean.FALSE;
                }
                break;
        }
        return Boolean.TRUE;
    }

    /**
     * Verifica que no haya otro usuario en el sistema que tenga la misma curp y
     * que ya tenga otorgamiento. Si encuentra, regresa la boleta del alumno
     * encontrado.
     *
     * @param a
     * @return
     */
    private String validaDuplicidadAlumnoCURP(Alumno a, Periodo p) {
        List<Otorgamiento> otMismaCurp = service.getOtorgamientoDao().existeDuplicadoCurpConOtorgamiento(a, p);
        if (otMismaCurp == null || otMismaCurp.isEmpty()) {
            return null;
        }
        List<String> alumnos = new ArrayList<String>();
        for (Otorgamiento o : otMismaCurp) {
            alumnos.add(o.getAlumno().getBoleta());
        }
        List<String> alumnosUnicos = new ArrayList<String>(new HashSet<String>(alumnos));
        String boletasCurpDuplicada = "";

        for (String s : alumnosUnicos) {
            boletasCurpDuplicada += s + ",";
        }
        if (boletasCurpDuplicada.trim().length() == 0) {
            return null;
        }
        return (boletasCurpDuplicada.substring(0, boletasCurpDuplicada.lastIndexOf(",")));

    }

    private Boolean validaProcesoPrograma(TipoBecaPeriodo tbp, Proceso proceso) {
        return service.getProcesoProgramaBecaDao().existe(tbp.getTipoBeca().getBeca().getId(), proceso.getId());
    }

    public Boolean validarPresupuesto(TipoBecaPeriodo beca, Alumno alumno) {
        Periodo periodo = beca.getPeriodo();
        DatosAcademicos datosAcademicos = service.getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), periodo.getId());
        VWPresupuestoUnidadAcademica presupuestoAsignado = service.getVwPresupuestoUnidadAcademicaDao().getPresupuestoUnidadAcademicaTipoBeca(datosAcademicos.getUnidadAcademica().getId(), beca.getId());

        if (presupuestoAsignado == null) {
            mensajesAsignaciones(false, alumno, "No se ha asignado presupuesto para su Unidad Académica.");
            return false;
        }
        if (presupuestoAsignado.getBecasDisponibles() == null || presupuestoAsignado.getBecasDisponibles() <= 0) {
            mensajesAsignaciones(false, alumno, "No alcanza el presupuesto para su Unidad Académica.");
            return false;
        }
        return true;
    }

    private Boolean validarMateriasReprobadas(TipoBecaPeriodo beca, DatosAcademicos datosAcademicos) {
        if (beca.getReprobadasMinimo() == null || beca.getReprobadasMaximo() == null) {
            razonNoOtorgamiento = 45; //Las materias reprobadas están mal configuradas
            return false;
        } else if (!between(datosAcademicos.getReprobadas(), beca.getReprobadasMinimo(), beca.getReprobadasMaximo())) {
            razonNoOtorgamiento = 44; //Las materias reprobadas son inválidas.
            return false;
        } else {
            return true;
        }
    }

    public TipoBecaPeriodo becaSolicitud(SolicitudBeca solicitud, Periodo periodoActivo, DatosAcademicos datosAcademicos,
            Boolean tieneUniversal, Boolean tieneComplementaria, Proceso proceso, PadronSubes padronSubes) {

        if (periodoActivo == null) {
            periodoActivo = service.getPeriodoDao().getPeriodoActivo();
        }
        String clave = periodoActivo.getClave();
        Otorgamiento oAnterior;
        //Revisa si es periodo 2, si se le dio otorgamiento del programa de beca solicitado en el periodo 1 y de ser así devuelve el nuevo tipo de beca para el periodo actual.
        if (clave.charAt(clave.length() - 1) == '2') {
            oAnterior = service.getOtorgamientoDao().getOtorgamientoSolicitudPeriodo(solicitud.getId(), periodoActivo.getPeriodoAnterior().getId());
            if (oAnterior != null) {
                return service.getTipoBecaPeriodoDao().nuevoTipoBecaPeriodo(oAnterior.getTipoBecaPeriodo().getId(), periodoActivo, tieneUniversal, tieneComplementaria, padronSubes);
            }
        }
        //Si tuvo otorgamiento en el periodo pasado del programa de beca solicitado, se llena
        oAnterior = service.getOtorgamientoDao().getOtorgamientoAnteriorPrograma(solicitud.getAlumno(), solicitud.getProgramaBecaSolicitada(), periodoActivo);
        //Si no tuvo otorgamiento en el periodo anterior del programa de beca solicitado, devuelve el tipo de beca perteneciente al programa de beca correspondiente que se adecue al nivel, area y promedio del alumno.
        if (oAnterior == null) {
            if (solicitud.getProgramaBecaSolicitada().getId().intValue() == 7) { //Transporte Manutención
                return service.getTipoBecaPeriodoDao().findBecaTransporte(1, solicitud.getAlumno(), periodoActivo.getClave(), tieneUniversal, tieneComplementaria, proceso, padronSubes);
            } else if (solicitud.getProgramaBecaSolicitada().getId().intValue() == 8) { //Transporte Institucional
                return service.getTipoBecaPeriodoDao().findBecaTransporte(2, solicitud.getAlumno(), periodoActivo.getClave(), tieneUniversal, tieneComplementaria, proceso, null);
            } else if (solicitud.getProgramaBecaSolicitada().getId().intValue() == 5) { //Manutención
                return service.getTipoBecaPeriodoDao().getBecaAlumnoActual(solicitud.getAlumno(), solicitud.getProgramaBecaSolicitada(), periodoActivo.getId(), datosAcademicos, tieneUniversal, tieneComplementaria, padronSubes);
            } else { //Otra beca
                return service.getTipoBecaPeriodoDao().getBecaAlumnoActual(solicitud.getAlumno(), solicitud.getProgramaBecaSolicitada(), periodoActivo.getId(), datosAcademicos, tieneUniversal, tieneComplementaria, null);
            }
            /*Si tuvo un otorgramiento en el periodo pasado del programa de beca solicitado, 
             y es I, HH o T devuelve el tipo de beca perteneciente al programa de beca correspondiente que se adecue al nivel, area y promedio del alumno.
             B, revisa si el otorgamiento fue de NMS o el tipobecaperiodo fue IPN B AR NS, de ser asi devuelve el tipo de beca perteneciente al programa de beca correspondiente que se adecue al nivel, area y promedio del alumno.
             de no ser asi, trata de devolver la siguiente beca B que corresponda al alumno.
             M, trata de devolver la siguiente beca M que corresponda al alumno
             TI, TM, devuelve la beca de transporte del periodo actual
             */
        } else {
            int programa = oAnterior.getTipoBecaPeriodo().getTipoBeca().getBeca().getId().intValue();
            switch (programa) {
                case 1:
                case 2:
                case 3:
                    return service.getTipoBecaPeriodoDao().getBecaAlumnoActual(solicitud.getAlumno(), solicitud.getProgramaBecaSolicitada(), null, null, tieneUniversal, tieneComplementaria, null);
                case 4:
                    if (oAnterior.getDatosAcademicos().getUnidadAcademica().getNivel().getId().intValue() == 1
                            || oAnterior.getTipoBecaPeriodo().getTipoBeca().getId().intValue() == 23) {
                        return service.getTipoBecaPeriodoDao().getBecaAlumnoActual(solicitud.getAlumno(), solicitud.getProgramaBecaSolicitada(), periodoActivo.getId(), datosAcademicos, tieneUniversal, tieneComplementaria, null);
                    } else {
                        return validarBecalos(oAnterior, tieneUniversal, tieneComplementaria);
                    }
                case 5:
                    return validarManutencion(oAnterior, tieneUniversal, tieneComplementaria, padronSubes, solicitud, periodoActivo, datosAcademicos);
                case 7:
                    return service.getTipoBecaPeriodoDao().findBecaTransporte(1, solicitud.getAlumno(), periodoActivo.getClave(), tieneUniversal, tieneComplementaria, proceso, padronSubes);
                case 8:
                    return service.getTipoBecaPeriodoDao().findBecaTransporte(2, solicitud.getAlumno(), periodoActivo.getClave(), tieneUniversal, tieneComplementaria, proceso, null);
            }
            return null;
        }
    }

    public List<String> getAlumnosConError() {
        return alumnosConError;
    }

    public List<String> getAlumnosOtorgados() {
        return alumnosOtorgados;
    }

    public boolean isAsignacionIndividual() {
        return asignacionIndividual;
    }

    public void setAsignacionIndividual(boolean asignacionIndividual) {
        this.asignacionIndividual = asignacionIndividual;
    }

    public String getErrorAsignacion() {
        return errorAsignacion;
    }

    public void setErrorAsignacion(String errorAsignacion) {
        this.errorAsignacion = errorAsignacion;
    }

    /**
     * Obtiene el último Otorgamiento asociado al alumno, si no es tiempo de
     * publicar los resultados, regresa el último que no corresponde al periodo
     * activo.
     *
     * @author Mario Márquez
     * @param alumno
     * @param publishResults bandera de publicación de resultados
     * @return Otorgamiento
     */
    public Otorgamiento getUltimoOtorgamiento(Alumno alumno, Boolean publishResults) {
        int index = 0;
        Boolean sameTerm = true;
        PeriodoBO pBO = new PeriodoBO(service);
        Periodo currentTerm = pBO.getPeriodoActivo();
        List<Otorgamiento> otorgamientos = service.getOtorgamientoDao().getUltimoOtorgamiento(alumno);

        if (otorgamientos == null || otorgamientos.isEmpty()) {
            return null;
        }

        // Si es tiempo de publicar los resultados, envía el último disponible
        if (publishResults) {
            return otorgamientos.get(index);
        } else {
            // Si no es tiempo, consigue el último otorgamiento que no es del periodo actual
            while (sameTerm && otorgamientos.size() > index) {
                Periodo oTerm = otorgamientos.get(index).getPeriodo();
                if (oTerm.equals(currentTerm)) {
                    index += 1;
                } else {
                    sameTerm = false;
                }
            }
            if (otorgamientos.size() < index) {
                return null;
            } else {
                return otorgamientos.get(index);
            }
        }
    }

    private boolean validarCarga(Alumno a, TipoBecaPeriodo tbp, DatosAcademicos datosAcademicos) {
        Periodo periodo = tbp.getPeriodo();
        if (datosAcademicos == null) {
            datosAcademicos = service.getDatosAcademicosDao().datosPorPeriodo(a.getId(), periodo.getId());
        }
        if (tbp.getCumpleCargaMinima() == null) {
            return datosAcademicos.getCumpleCargaMinima() > 0;
        } else {
            return tbp.getCumpleCargaMinima() <= datosAcademicos.getCumpleCargaMinima();
        }
    }

    public boolean rechazar(SolicitudBeca solicitud, MotivoRechazoSolicitud motivo, Proceso proceso, Usuario usuario) {

        ListaEspera lista = service.getListaEsperaDao().findBySolicitud(solicitud.getId(), true);
        if (service.getOtorgamientoDao().getOtorgamientoSolicitudPeriodo(solicitud.getId(), proceso.getPeriodo().getId()) != null) {
            mensajesAsignaciones(false, solicitud.getAlumno(), "La solicitud ya tiene un otorgamiento.");
            return false;
        } else if (lista != null && lista.getProceso().getId().equals(proceso.getId())) {
            mensajesAsignaciones(false, solicitud.getAlumno(), "La solicitud ya se encuentra en lista de espera de este proceso.");
            return false;
        } else {
            solicitud.setMotivoRechazo(motivo);
            solicitud.setProceso(proceso);
            solicitud.setClasificacionSolicitud(new ClasificacionSolicitud(new BigDecimal(2)));
            solicitud.setUsuario(usuario);
            solicitud.setFechaModificacion(new Date());
            if (lista != null) {
                lista.setVigente(0);
            }
            try {
                service.getSolicitudBecaDao().update(solicitud);
                if (lista != null) {
                    service.getListaEsperaDao().update(lista);
                }
                mensajesAsignaciones(true, solicitud.getAlumno(), "El rechazo fue realizado correctamente.");
                return true;
            } catch (Exception e) {
                log.warn(e.getMessage());
                mensajesAsignaciones(false, solicitud.getAlumno(), "Error al realizar el rechazo.");
                return false;
            }
        }
    }

    public boolean listaEspera(SolicitudBeca solicitud, Proceso proceso, Usuario usuario) {

        if (service.getOtorgamientoDao().getOtorgamientoSolicitudPeriodo(solicitud.getId(), proceso.getPeriodo().getId()) != null) {
            errorEspera = "La solicitud ya tiene un otorgamiento.";
            return false;
        }
        if (!validarRechazo(service.getSolicitudBecaDao().findById(solicitud.getId()))) {
            errorEspera = "La solicitud ya tiene un rechazo.";
            return false;
        } else if (service.getListaEsperaDao().findBySolicitud(solicitud.getId(), true) != null) {
            errorEspera = "La solicitud ya es parte de la lista de espera.";
            return false;
        } else {
            ListaEspera espera = service.getListaEsperaDao().buscarUltimoTurno(service.getPeriodoDao().getPeriodoActivo().getId(), proceso.getUnidadAcademica().getId());
            int turno;
            if (espera == null) {
                turno = 1;
            } else {
                turno = espera.getOrdenamiento() + 1;
            }
            espera = new ListaEspera(solicitud, solicitud.getAlumno(), proceso, turno, 1);
            solicitud.setClasificacionSolicitud(new ClasificacionSolicitud(new BigDecimal(3)));
            solicitud.setUsuario(usuario);
            solicitud.setFechaModificacion(new Date());
            try {
                service.getListaEsperaDao().save(espera);
                service.getSolicitudBecaDao().update(solicitud);
                return true;
            } catch (Exception e) {
                errorEspera = "Ocurrió un error en la transacción:" + e;
                return false;
            }
        }
    }

    public String getErrorEspera() {
        return errorEspera;
    }

    public void setErrorEspera(String errorEspera) {
        this.errorEspera = errorEspera;
    }

    public int getRazonNoOtorgamiento() {
        return razonNoOtorgamiento;
    }

    public void setRazonNoOtorgamiento(int razonNoOtorgamiento) {
        this.razonNoOtorgamiento = razonNoOtorgamiento;
    }

    public static org.apache.log4j.Logger getWriteFileLog() {
        return writeFileLog;
    }

    public static void setWriteFileLog(org.apache.log4j.Logger writeFileLog) {
        OtorgamientoBO.writeFileLog = writeFileLog;
    }

    private boolean validarRechazo(SolicitudBeca solicitudBeca) {
        if (solicitudBeca.getProceso() == null && solicitudBeca.getClasificacionSolicitud() == null) {
            return true;
        } else if (solicitudBeca.getClasificacionSolicitud().getId().equals(new BigDecimal(2)) || solicitudBeca.getProceso() != null) {
            return false;
        }
        return true;
    }

    private boolean validarListaEspera(SolicitudBeca solicitudBeca, Proceso proceso) {
        ListaEspera le = service.getListaEsperaDao().findBySolicitud(solicitudBeca.getId(), true);
        if (le != null) {
            if (le.getProceso().getId().equals(proceso.getId())) {
                return false;
            }
        }
        return true;
    }

    public String becasExternas = "";

    public boolean validarBecasExternas(Alumno alumno) {
        Periodo p = service.getPeriodoDao().getPeriodoActivo();
        List<OtorgamientoExterno> extLst = service.getOtorgamientoExternoDao().getAlumno(alumno, p.getCicloEscolar());
        if (extLst != null) {
            for (OtorgamientoExterno oe : extLst) {
                becasExternas += oe.getBecaExterna().getNombre() + ", ";
            }
            becasExternas = becasExternas.length() == 0 ? becasExternas : becasExternas.substring(0, becasExternas.length() - 2);
        }
        return extLst == null || extLst.isEmpty();
    }

    public void setTitulo(int total) {
        int k = -1;
        BigDecimal bD = new BigDecimal(k);
        StringBuilder sb = new StringBuilder();
        sb.append("\"Estatus de Cuentas (");
        sb.append(total);
        sb.append(") ");
        sb.append(service.getPeriodoDao().findById(periodoId).getClave());
        if (nivelId != null) {
            sb.append("-");
            sb.append(service.getNivelDao().findById(nivelId).getClave());
        }
        if (uAId != null) {
            UnidadAcademica ua = service.getUnidadAcademicaDao().findById(uAId);
            if (nivelId == null) {
                sb.append(ua.getNivel().getClave());
            }
            sb.append("-");
            sb.append(ua.getNombreCorto());
        }
        if (estatusId != null) {
            if (estatusId.equals(bD)) {
                sb.append("- Total");
            } else {
                EstatusTarjetaBancaria et = service.getEstatusTarjetaBancariaDao().findById(estatusId);
                sb.append("-");
                sb.append(et.getNombre());
            }
        }
        sb.append(".xlsx\"");

        this.titulo = sb.toString();
    }

    public InputStream getInfoExcel() {

        if (periodoId != null) {
            List<Object[]> infoBD = service.getOtorgamientoDao().reporteEstatus(periodoId, nivelId, uAId, estatusId);
            setTitulo(infoBD.size());

            ExcelExport excelExport = new ExcelExport();

            String[] encabezado;
            encabezado = new String[]{"BOLETA", "NOMBRE", "APELLIDO PATERNO",
                "APELLIDO MATERNO", "UNIDAD ACADÉMICA", "ESTATUS"};

            return excelExport.construyeExcel(encabezado, infoBD);
        } else {
            return null;
        }
    }

    public InputStream getInfoExcelProcesoOtorgamiento(BigDecimal becaId) {

        if (periodoId != null) {
            List<Object[]> infoBD = service.getOtorgamientoDao().reporteProceso(periodoId, uAId, becaId);
            if (infoBD != null) {
                ExcelTitulo excelT = new ExcelTitulo.Builder(service, "Proceso de Otorgamiento", infoBD.size())
                        .periodoId(periodoId).nivelId(nivelId).uAId(uAId).build();
                setTitulo(excelT.getTitulo());
            } else {
                return null;
            }

            ExcelExport excelExport = new ExcelExport();
            String[] columnas = new String[]{"BOLETA", "UNIDAD ACADEMICA", "APELLIDO PATERNO", "APELLIDO MATERNO", "NOMBRE", "BECA", "PROMEDIO", "SEMESTRE", "EXCLUIR DEPOSITO", "NOMBRE IO"};
            return excelExport.construyeExcel(columnas, infoBD);
        } else {
            return null;
        }
    }

    public InputStream getExcelTotalBecas(BigDecimal becaId, BigDecimal movimiento) {
        List<Object[]> infoBD = service.getOtorgamientoDao().reporteTotalBecas(periodoId, nivelId, uAId, becaId, movimiento);
        if (infoBD != null) {
            ExcelTitulo excelT = new ExcelTitulo.Builder(service, "Becas Totales", infoBD.size())
                    .periodoId(periodoId).nivelId(nivelId).uAId(uAId).build();
            setTitulo(excelT.getTitulo());
        } else {
            return null;
        }

        String[] columnas = new String[]{"NIVEL", "UNIDAD ACADEMICA", "BECA", "NUMERO"};

        ExcelExport excelExport = ExcelExport.getInstance(columnas, infoBD);

        XSSFWorkbook libro = new XSSFWorkbook();
        XSSFSheet hoja = libro.createSheet("Detalle");
        excelExport.setEncabezado(hoja);

        XSSFSheet pivotSheet = libro.createSheet("TD");
        XSSFPivotTable pivotTable = pivotSheet.createPivotTable(new AreaReference("Detalle!A1:D" + (infoBD.size() + 1) + ""), new CellReference("A1"));
        pivotTable.addRowLabel(0);
        pivotTable.addRowLabel(1);
        pivotTable.addRowLabel(2);
        pivotTable.addColumnLabel(DataConsolidateFunction.SUM, 3, "Total");

        return excelExport.getInputStream(libro, "Detalle");
    }

    public InputStream getExcelTotalBecarios(BigDecimal becaId, BigDecimal movimiento) {
        List<Object[]> infoBD = service.getOtorgamientoDao().reporteTotalBecarios(periodoId, nivelId, uAId, movimiento);
        if (infoBD != null) {
            ExcelTitulo excelT = new ExcelTitulo.Builder(service, "Becarios Totales", infoBD.size())
                    .periodoId(periodoId).nivelId(nivelId).uAId(uAId).build();
            setTitulo(excelT.getTitulo());
        } else {
            return null;
        }

        String[] columnas = new String[]{"NIVEL", "UNIDAD ACADEMICA", "NUMERO"};
        ExcelExport excelExport = ExcelExport.getInstance(columnas, infoBD);

        XSSFWorkbook libro = new XSSFWorkbook();
        XSSFSheet hoja = libro.createSheet("Detalle");
        excelExport.setEncabezado(hoja);

        XSSFSheet pivotSheet = libro.createSheet("TD");
        XSSFPivotTable pivotTable = pivotSheet.createPivotTable(new AreaReference("Detalle!A1:C" + (infoBD.size() + 1) + ""), new CellReference("A1"));
        pivotTable.addRowLabel(0);
        pivotTable.addRowLabel(1);
        pivotTable.addColumnLabel(DataConsolidateFunction.SUM, 2, "Total");

        return excelExport.getInputStream(libro, "Detalle");
    }

    public static boolean isOtorgamientoActualDefinitivo(Otorgamiento o) {
        boolean procesoCerrado = false;
        boolean periodoActivo = false;

        if (o != null) {
            procesoCerrado = o.getProceso().getProcesoEstatus().getId().intValue() == 4;
            periodoActivo = o.getPeriodo().getEstatus();
        }

        return procesoCerrado && periodoActivo;
    }

    public BigDecimal getPeriodoId() {
        return periodoId;
    }

    public void setPeriodoId(String periodoId) {
        this.periodoId = new BigDecimal(periodoId);
    }

    public BigDecimal getNivelId() {
        return nivelId;
    }

    public void setNivelId(String nivelId) {
        this.nivelId = nivelId != null && !nivelId.equals("0") ? new BigDecimal(nivelId) : null;
    }

    public BigDecimal getuAId() {
        return uAId;
    }

    public void setuAId(String uAId) {
        this.uAId = uAId != null && !uAId.equals("0") ? new BigDecimal(uAId) : null;
    }

    public String getBecasExternas() {
        return becasExternas;
    }

    public void setBecasExternas(String becasExternas) {
        this.becasExternas = becasExternas;
    }

    public BigDecimal getEstatusId() {
        return estatusId;
    }

    public void setEstatusId(String estatusId) {
        this.estatusId = estatusId != null && !estatusId.equals("0") ? new BigDecimal(estatusId) : null;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

}
