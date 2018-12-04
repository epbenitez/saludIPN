package com.becasipn.actions.admin;

import com.becasipn.actions.BaseReportAction;
import com.becasipn.business.AdministracionProcesosBO;
import com.becasipn.business.PeriodoBO;
import com.becasipn.business.ProcesoBO;
import com.becasipn.domain.BecaPeriodoCount;
import com.becasipn.domain.CampoResumen;
import com.becasipn.domain.Resumenes;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.BitacoraEstatusProceso;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.Movimiento;
import com.becasipn.persistence.model.Proceso;
import com.becasipn.persistence.model.Otorgamiento;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.PersonalAdministrativo;
import com.becasipn.persistence.model.TipoProceso;
import com.becasipn.persistence.model.ProcesoEstatus;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.util.ExcelExport;
import com.becasipn.util.AmbienteEnums;

import com.becasipn.util.UtilFile;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author Victor Lozano
 */
public class AdministracionProcesosAction extends BaseReportAction implements MensajesAdmin {

    public static final String FORMULARIO = "formulario";
    public static final String LISTA = "lista";
    public static final String REPORT = "reporte";
    public static final String ACUMULADO = "acumulado";
    public static final String REPORTEX = "exReporte";
    public static final String ACUMULADOX = "exAcumulado";
    public static final String REPORTEP = "reportePeriodo";
    public static final String VALIDACION = "validacion";

    private boolean esAnalista = false;
    private PersonalAdministrativo director = new PersonalAdministrativo();
    private PersonalAdministrativo subdirector = new PersonalAdministrativo();
    private PersonalAdministrativo jefe = new PersonalAdministrativo();
    private PersonalAdministrativo responsable = new PersonalAdministrativo();
    private List<TipoProceso> procesoList = new ArrayList<>();
    private List<BitacoraEstatusProceso> bitacoraEstatusProcesosList = new ArrayList<>();
    private List<Otorgamiento> otorgamientoList = new ArrayList<>();
    private List<BecaPeriodoCount> otorgamientosBecariosCount = new ArrayList<>();
    private List<Resumenes> resumenes = new ArrayList<>();
    private List<CampoResumen> listaCampos = new ArrayList<>();
    private Proceso proceso = new Proceso();
    private Boolean i = true; // Alta o Baja
    private String procesosReportesList;
    private String movimiento;
    private String periodo;
    private String ua;
    private Date fechaHoy;
    private String fechaInicial;
    private String fechaFinal;
    private String becasString;
    private int total;
    private String otorgamientoListStr;
    private String otorgamientosBecariosCountStr;
    private List<Proceso> procesosList = new ArrayList<>();
    private InputStream excelStream;
    private String contentDisposition;
    private String becasL;
    private Integer e = null;
    private String titulo;
    private List<Movimiento> resumenMovimientosList;

    /**
     * Carga el listado para la administración de procesos
     *
     * @author Victor Lozano
     * @return SUCCESS
     */
    public String lista() {
        procesoList = getDaos().getTipoProcesoDao().findAll();
        return SUCCESS;
    }

    public String validacion() {
        esAnalista = isAnalista();
        esAnalista = esAnalista || isJefe();
        return VALIDACION;
    }

    public String resumenAcumulado() {
        if (isFuncionario() || isResponsable()) {
            Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
            ua = getDaos().getPersonalAdministrativoDao().findByUsuario(usuario.getId()).getUnidadAcademica().getId().toString();
        }
        AdministracionProcesosBO bo = AdministracionProcesosBO.getInstance(getDaos());
        resumenMovimientosList = bo.getResumenMovimientosList();
        
        periodo = getDaos().getPeriodoDao().getPeriodoActivo().getId().toString();
        return SUCCESS;
    }

    public String resumenPeriodo() {
        periodo = getDaos().getPeriodoDao().getPeriodoActivo().getId().toString();
        return SUCCESS;
    }

    /**
     * Formulario de registro de un nuevo elemento
     *
     * @author Victor Lozano
     * @return SUCCESS
     */
    public String form() {
        PeriodoBO periodoBO = new PeriodoBO(getDaos());
        proceso.setPeriodo(periodoBO.getPeriodoActivo());
        return SUCCESS;
    }

    /**
     * Realiza las validaciones correspondientes para almacenar o actualizar
     * información
     *
     * @author Victor Lozano
     * @return FORMULARIO
     */
    public String guarda() {
        ProcesoBO procesoBO = new ProcesoBO(getDaos());

        if (fechaInicial != null && fechaInicial.length() > 0) {
            Date fInicial = UtilFile.strToDate(fechaInicial, "dd/MM/yyyy");
            proceso.setFechaInicial(fInicial);
        } else {
            addActionMessage("Sin fecha inicial");
            return FORMULARIO;
        }
        if (fechaFinal != null && fechaFinal.length() > 0) {
            Date fInicial = UtilFile.strToDate(fechaFinal, "dd/MM/yyyy");
            Calendar c = Calendar.getInstance();
            c.setTime(fInicial);
            c.set(Calendar.MILLISECOND, 0);
            c.set(Calendar.SECOND, 59);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.HOUR, 23);
            fInicial = c.getTime();
            proceso.setFechaFinal(fInicial);
        } else {
            addActionMessage("Sin fecha final");
            return FORMULARIO;
        }

        if (proceso.getId() == null) {
            BigDecimal id_estatus = new BigDecimal(1);
            proceso.setProcesoEstatus(new ProcesoEstatus());
            proceso.getProcesoEstatus().setId(id_estatus);
        }

        if (!procesoBO.fechaValida(proceso)) {
            addActionError(getText("admin.guardado.error.fecha.invalida"));
            return FORMULARIO;
        }

        if (proceso.getId() == null && procesoBO.existe(proceso)) {
            addActionError(getText("admin.guardado.registro.repetido"));
            return FORMULARIO;
        }

        if (!procesoBO.procesoDentroPeriodo(proceso)) {
            addActionError(getText("admin.guardado.error.fecha.proceso"));
            return FORMULARIO;
        }

        if (becasL == null || becasL.length() < 1) {
            addActionMessage("No ha sido seleccionado ningun tipo de beca. Si no eliges ningún programa de beca, no podrás ver en candidatos a ningún alumno");
//	    return FORMULARIO;
        }

        if (procesoBO.guardaProceso(proceso, becasL)) {
            becasString = procesoBO.getByProceso(proceso.getId());
            addActionMessage(getText("admin.guardado.exito"));
        } else {
            becasString = becasL;
            addActionError(getText("admin.guardado.error"));
        }

        String becasNombres = procesoBO.getBecasNombreByProceso(proceso.getId());
        BitacoraEstatusProceso bitacora = new BitacoraEstatusProceso();
        bitacora.setFechamodificacion(new Date());
        bitacora.setProceso(proceso);
        bitacora.setProcesoEstatus(proceso.getProcesoEstatus());
        bitacora.setUsuario((Usuario) ActionContext.getContext().getSession().get("usuario"));
        bitacora.setBecasAsociadas(becasNombres);
        getDaos().getBitacoraEstatusProcesoDao().save(bitacora);
        return FORMULARIO;
    }

    /**
     * Realiza validación de formulario para la edición
     *
     * @author Victor Lozano
     * @return SUCCESS
     */
    public String edicion() {
        if (proceso == null || proceso.getId() == null) {
            addActionError(getText("admin.actualizado.error"));
        }
        ProcesoBO procesoBO = new ProcesoBO(getDaos());
        proceso = procesoBO.getProceso(proceso.getId());
        becasString = procesoBO.getByProceso(proceso.getId());
        return SUCCESS;
    }

    /**
     * Realiza validación de formulario para eliminar información
     *
     * @author Victor Lozano
     * @return SUCCESS
     */
    public String eliminar() {
        procesoList = getDaos().getTipoProcesoDao().findAll();
        if (proceso == null || proceso.getId() == null) {
            addActionError(getText("admin.eliminado.error"));
        }
        ProcesoBO procesoBO = new ProcesoBO(getDaos());
        proceso = procesoBO.getProceso(proceso.getId());
        if (procesoBO.existenOtorgamientos(proceso.getId())) {
            addActionError(getText("admin.eliminado.otorgamientos.asociados"));
            if (e != null && e == 1) {
                return validacion();
            } else {
                return LISTA;
            }
        }
        Boolean resultado = procesoBO.eliminaProceso(proceso);
        if (resultado) {
            addActionMessage(getText("admin.eliminado.exito"));
        } else {
            addActionError(getText("admin.eliminado.error"));
        }
        if (e != null && e == 1) {
            return validacion();
        } else {
            return LISTA;
        }
    }

    public String bitacora() {
        if (proceso == null || proceso.getId() == null) {
            addActionError(getText("admin.actualizado.error"));
        }
        ProcesoBO bo = new ProcesoBO(getDaos());
        proceso = bo.getProceso(proceso.getId());
        bitacoraEstatusProcesosList = getDaos().getBitacoraEstatusProcesoDao().getByProceso(proceso.getId());
        return SUCCESS;
    }

    public String cuadroResumen() {
        if (proceso == null || proceso.getId() == null) {
            addActionError(getText("admin.actualizado.error"));
        }
        ProcesoBO bo = new ProcesoBO(getDaos());
        proceso = bo.getProceso(proceso.getId());
        fechaHoy = new Date();
        DateFormat df = new SimpleDateFormat("ddMMyyyyHHmmss");

        Periodo per = getDaos().getPeriodoDao().getPeriodoActivo();
        String periodoActual = per.getClave().substring(0, 4) + "-0" + per.getClave().substring(5, 6);
        NumberFormat f1 = new DecimalFormat("000");
        NumberFormat f2 = new DecimalFormat("0000");
        String folio = "RPRT-" + f1.format(proceso.getTipoProceso().getMovimiento().getId()) + "-" + f2.format(proceso.getUnidadAcademica().getId()) + "-" + df.format(fechaHoy) + periodoActual;
        getParametros().put("FOLIO", folio);

        getParametros().put("LOGOIPN", "/com/becasipn/jasper/logoipn.jpg");
        getParametros().put("LOGODSE", "/com/becasipn/jasper/logodse.png");

        getParametros().put("UA", proceso.getUnidadAcademica().getNombreCorto());
        getParametros().put("PROC", proceso.getTipoProceso().getNombre());
        getParametros().put("PERIODO", proceso.getPeriodo().getClave());

        otorgamientoList = getDaos().getOtorgamientoDao().otorgamientosProceso(proceso.getId());
        total = 0;
        for (Otorgamiento o : otorgamientoList) {
            CampoResumen ax = new CampoResumen();
            Alumno alumno = o.getAlumno();
            DatosAcademicos datosAcademicos = getDaos().getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), per.getId());
            // Se consiguen los últimos datos académicos conocidos en caso de 
            // no haber disponibles para el periodo actual
            if (datosAcademicos == null) {
                datosAcademicos = alumno.getListaDatosAcademicos().get(0);
            }
            ax.setNum(total + 1);
            ax.setBoleta(alumno.getBoleta());
            ax.setCurp(alumno.getCurp());
            ax.setNombreCompleto(alumno.getApellidoPaterno() + " " + alumno.getApellidoMaterno() + " " + alumno.getNombre());
            ax.setGenero(alumno.getGenero().getClave());
            ax.setSemestre(datosAcademicos.getSemestre());
            ax.setPromedio(datosAcademicos.getPromedio());
            ax.setNombreBeca(o.getTipoBecaPeriodo().getTipoBeca().getNombre());
            ax.setFechaI(o.getPeriodo().getFechaInicial());
            ax.setFechaF(o.getPeriodo().getFechaFinal());

            listaCampos.add(ax);
            total = total + 1;
        }
        getParametros().put("TOTAL", total);

        otorgamientosBecariosCount = getDaos().getOtorgamientoDao().otorgamientosBecariosCount(proceso.getId());

        director = getDaos().getPersonalAdministrativoDao().findByCargo(proceso.getUnidadAcademica().getId(), new BigDecimal(5));
        if (director == null || director.getId() == null) {
            director = getDaos().getPersonalAdministrativoDao().findByCargo(proceso.getUnidadAcademica().getId(), new BigDecimal(8));
            if (director == null || director.getId() == null) {
                getParametros().put("DIRECTOR", "---");
            } else {
                getParametros().put("DIRECTOR", director.getNombre() + " " + director.getApellidoPaterno() + " " + director.getApellidoMaterno());
            }
        } else {
            getParametros().put("DIRECTOR", director.getNombre() + " " + director.getApellidoPaterno() + " " + director.getApellidoMaterno());
        }

        subdirector = getDaos().getPersonalAdministrativoDao().findByCargo(proceso.getUnidadAcademica().getId(), new BigDecimal(33));
        if (subdirector == null || subdirector.getId() == null) {
            subdirector = getDaos().getPersonalAdministrativoDao().findByCargo(proceso.getUnidadAcademica().getId(), new BigDecimal(32));
            if (subdirector == null || subdirector.getId() == null) {
                getParametros().put("SUBDIRECTOR", "---");
            } else {
                getParametros().put("SUBDIRECTOR", subdirector.getNombre() + " " + subdirector.getApellidoPaterno() + " " + subdirector.getApellidoMaterno());
            }
        } else {
            getParametros().put("SUBDIRECTOR", subdirector.getNombre() + " " + subdirector.getApellidoPaterno() + " " + subdirector.getApellidoMaterno());
        }

        jefe = getDaos().getPersonalAdministrativoDao().findByCargo(proceso.getUnidadAcademica().getId(), new BigDecimal(17));
        if (jefe == null || jefe.getId() == null) {
            getParametros().put("JEFE", "---");
        } else {
            getParametros().put("JEFE", jefe.getNombre() + " " + jefe.getApellidoPaterno() + " " + jefe.getApellidoMaterno());
        }

        responsable = getDaos().getPersonalAdministrativoDao().findByCargo(proceso.getUnidadAcademica().getId(), new BigDecimal(24));
        if (responsable == null || responsable.getId() == null) {
            getParametros().put("RESPONSABLE", "---");
        } else {
            getParametros().put("RESPONSABLE", responsable.getNombre() + " " + responsable.getApellidoPaterno() + " " + responsable.getApellidoMaterno());
        }

        JRBeanCollectionDataSource lista = new JRBeanCollectionDataSource(listaCampos);
        JRBeanCollectionDataSource listaCount = new JRBeanCollectionDataSource(otorgamientosBecariosCount);
        getParametros().put("listaDataSource", lista);
        getParametros().put("listaCountDataSource", listaCount);
        if (i) {
            return REPORT;
        } else {
            getParametros().put("PERIODO", "Periodo: " + getDaos().getPeriodoDao().getPeriodoActivo().getClave());
            getParametros().put("PROC", "Proceso: " + proceso.getTipoProceso().getNombre());

            return REPORTEX;
        }
    }

    public String listadoPrelacion() {
        if (proceso == null || proceso.getId() == null) {
            addActionError(getText("admin.actualizado.error"));
        }
        ProcesoBO bo = new ProcesoBO(getDaos());
        proceso = bo.getProceso(proceso.getId());

        AdministracionProcesosBO aBO = AdministracionProcesosBO.getInstance(getDaos());
        aBO.setProcesoId(proceso.getId());
        aBO.setTituloPrelacion();

        excelStream = aBO.getInfoExcel();
        setContentDisposition("attachment; filename=" + aBO.getTitulo());
        return "archivo";
    }

    public String cuadroResumenAcumulado() {
        String tmp = procesosReportesList.substring(1, procesosReportesList.length() - 1);
        procesosList = getDaos().getProcesoDao().reportesProcesosList(tmp);

        ProcesoBO bo = new ProcesoBO(getDaos());
        proceso = bo.getProceso(procesosList.get(0).getId());
        fechaHoy = new Date();
        DateFormat df = new SimpleDateFormat("ddMMyyyyHHmmss");

        Periodo per = getDaos().getPeriodoDao().getPeriodoActivo();
        String periodoActual = per.getClave().substring(0, 4) + "-0" + per.getClave().substring(5, 6);
        NumberFormat f1 = new DecimalFormat("000");
        NumberFormat f2 = new DecimalFormat("0000");
        String folio = "RPRT-" + f1.format(proceso.getTipoProceso().getMovimiento().getId()) + "-" + f2.format(proceso.getUnidadAcademica().getId()) + "-" + df.format(fechaHoy) + periodoActual;
        getParametros().put("FOLIO", folio);

        getParametros().put("LOGOIPN", "/com/becasipn/jasper/logoipn.jpg");
        getParametros().put("LOGODSE", "/com/becasipn/jasper/logodse.png");

        getParametros().put("UA", proceso.getUnidadAcademica().getNombreCorto());

        String procs = "";
        for (Proceso p : procesosList) {
            procs += bo.getProceso(p.getId()).getTipoProceso().getNombre() + " / ";
        }

        getParametros().put("PROC", procs.substring(0, procs.length() - 3));
        getParametros().put("PERIODO", proceso.getPeriodo().getClave());
        getParametros().put("MOVIMIENTO", proceso.getTipoProceso().getMovimiento().getNombre());

        total = 0;

        otorgamientoList = getDaos().getOtorgamientoDao().otorgamientosProcesos(proceso.getId(), tmp);
        for (Otorgamiento o : otorgamientoList) {
            Alumno alumno = o.getAlumno();
            DatosAcademicos datosAcademicos = getDaos().getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), per.getId());
            // Se consiguen los últimos datos académicos conocidos en caso de 
            // no haber disponibles para el periodo actual
            if (datosAcademicos == null) {
                datosAcademicos = alumno.getListaDatosAcademicos().get(0);
            }
            CampoResumen ax = new CampoResumen();
            ax.setNum(total + 1);
            ax.setBoleta(alumno.getBoleta());
            ax.setCurp(alumno.getCurp());
            ax.setNombreCompleto(alumno.getApellidoPaterno() + " " + alumno.getApellidoMaterno() + " " + alumno.getNombre());
            ax.setGenero(alumno.getGenero().getClave());
            ax.setSemestre(datosAcademicos.getSemestre());
            ax.setPromedio(datosAcademicos.getPromedio());
            ax.setNombreBeca(o.getTipoBecaPeriodo().getTipoBeca().getNombre());
            ax.setFechaI(o.getPeriodo().getFechaInicial());
            ax.setFechaF(o.getPeriodo().getFechaFinal());

            int movimientoId = proceso.getTipoProceso().getMovimiento().getId().intValue();
            Boolean al = (movimientoId >= 4 && movimientoId <= 6) ? Boolean.TRUE : Boolean.FALSE;
            if (al) {
                ax.setNomProceso(getDaos().getOtorgamientoDao().nombreProcesoBajaOtorgamiento(o.getId()));
            } else {
                ax.setNomProceso(o.getProceso().getTipoProceso().getNombre());

            }

            listaCampos.add(ax);
            total = total + 1;
        }
        getParametros().put("TOTAL", total);

        director = getDaos().getPersonalAdministrativoDao().findByCargo(proceso.getUnidadAcademica().getId(), new BigDecimal(5));
        if (director == null || director.getId() == null) {
            director = getDaos().getPersonalAdministrativoDao().findByCargo(proceso.getUnidadAcademica().getId(), new BigDecimal(8));
            if (director == null || director.getId() == null) {
                getParametros().put("DIRECTOR", "---");
            } else {
                getParametros().put("DIRECTOR", director.getNombre() + " " + director.getApellidoPaterno() + " " + director.getApellidoMaterno());
            }
        } else {
            getParametros().put("DIRECTOR", director.getNombre() + " " + director.getApellidoPaterno() + " " + director.getApellidoMaterno());
        }

        subdirector = getDaos().getPersonalAdministrativoDao().findByCargo(proceso.getUnidadAcademica().getId(), new BigDecimal(33));
        if (subdirector == null || subdirector.getId() == null) {
            subdirector = getDaos().getPersonalAdministrativoDao().findByCargo(proceso.getUnidadAcademica().getId(), new BigDecimal(32));
            if (subdirector == null || subdirector.getId() == null) {
                getParametros().put("SUBDIRECTOR", "---");
            } else {
                getParametros().put("SUBDIRECTOR", subdirector.getNombre() + " " + subdirector.getApellidoPaterno() + " " + subdirector.getApellidoMaterno());
            }
        } else {
            getParametros().put("SUBDIRECTOR", subdirector.getNombre() + " " + subdirector.getApellidoPaterno() + " " + subdirector.getApellidoMaterno());
        }

        jefe = getDaos().getPersonalAdministrativoDao().findByCargo(proceso.getUnidadAcademica().getId(), new BigDecimal(17));
        if (jefe == null || jefe.getId() == null) {
            getParametros().put("JEFE", "---");
        } else {
            getParametros().put("JEFE", jefe.getNombre() + " " + jefe.getApellidoPaterno() + " " + jefe.getApellidoMaterno());
        }

        responsable = getDaos().getPersonalAdministrativoDao().findByCargo(proceso.getUnidadAcademica().getId(), new BigDecimal(24));
        if (responsable == null || responsable.getId() == null) {
            getParametros().put("RESPONSABLE", "---");
        } else {
            getParametros().put("RESPONSABLE", responsable.getNombre() + " " + responsable.getApellidoPaterno() + " " + responsable.getApellidoMaterno());
        }

        JRBeanCollectionDataSource lista = new JRBeanCollectionDataSource(listaCampos);

        otorgamientosBecariosCount = getDaos().getOtorgamientoDao().otorgamientosBecariosCountP(proceso.getId(), tmp);
        JRBeanCollectionDataSource listaCount = new JRBeanCollectionDataSource(otorgamientosBecariosCount);

        getParametros().put("listaDataSource", lista);
        getParametros().put("listaCountDataSource", listaCount);
//        if (i) {
        return ACUMULADO;
//        } else {
//            return ACUMULADOX;
//        }
    }

    public String descargar() {
        if ("x".equals(periodo)) {
            periodo = getDaos().getPeriodoDao().getPeriodoActivo().getId().toString();
        }
        
        Usuario usuario = null;
        boolean esAcotado = isFuncionario() || isResponsable();
        if (esAcotado) {
            usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        }
        AdministracionProcesosBO aBO = AdministracionProcesosBO.getInstance(getDaos(), 
                new BigDecimal(periodo), i, esAcotado, usuario);
        
        excelStream = aBO.getInfoExcelResumenPeriodo();
        setContentDisposition("attachment; filename=" + aBO.getTitulo());
        
        return "archivo";
    }

    public Proceso getProceso() {
        return proceso;
    }

    public void setProceso(Proceso proceso) {
        this.proceso = proceso;
    }

    public List<TipoProceso> getProcesoList() {
        return procesoList;
    }

    public void setProcesoList(List<TipoProceso> procesoList) {
        this.procesoList = procesoList;
    }

    public List<BitacoraEstatusProceso> getBitacoraEstatusProcesosList() {
        return bitacoraEstatusProcesosList;
    }

    public void setBitacoraEstatusProcesosList(List<BitacoraEstatusProceso> bitacoraEstatusProcesosList) {
        this.bitacoraEstatusProcesosList = bitacoraEstatusProcesosList;
    }

    public boolean isEsAnalista() {
        return esAnalista;
    }

    public void setEsAnalista(boolean esAnalista) {
        this.esAnalista = esAnalista;
    }

    public List<Otorgamiento> getOtorgamientoList() {
        return otorgamientoList;
    }

    public void setOtorgamientoList(List<Otorgamiento> otorgamientoList) {
        this.otorgamientoList = otorgamientoList;
    }

    public List<BecaPeriodoCount> getOtorgamientosBecariosCount() {
        return otorgamientosBecariosCount;
    }

    public void setOtorgamientosBecariosCount(List<BecaPeriodoCount> otorgamientosBecariosCount) {
        this.otorgamientosBecariosCount = otorgamientosBecariosCount;
    }

    public PersonalAdministrativo getDirector() {
        return director;
    }

    public void setDirector(PersonalAdministrativo director) {
        this.director = director;
    }

    public PersonalAdministrativo getSubdirector() {
        return subdirector;
    }

    public void setSubdirector(PersonalAdministrativo subdirector) {
        this.subdirector = subdirector;
    }

    public PersonalAdministrativo getResponsable() {
        return responsable;
    }

    public void setResponsable(PersonalAdministrativo responsable) {
        this.responsable = responsable;
    }

    public String getProcesosReportesList() {
        return procesosReportesList;
    }

    public void setProcesosReportesList(String procesosReportesList) {
        this.procesosReportesList = procesosReportesList;
    }

    public String getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(String movimiento) {
        this.movimiento = movimiento;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }

    public List<Proceso> getProcesosList() {
        return procesosList;
    }

    public void setProcesosList(List<Proceso> procesosList) {
        this.procesosList = procesosList;
    }

    public List<Resumenes> getResumenes() {
        return resumenes;
    }

    public void setResumenes(List<Resumenes> resumenes) {
        this.resumenes = resumenes;
    }

    public Date getFechaHoy() {
        return fechaHoy;
    }

    public void setFechaHoy(Date fechaHoy) {
        this.fechaHoy = fechaHoy;
    }

    public String getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(String fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public String getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(String fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public String getOtorgamientoListStr() {
        return otorgamientoListStr;
    }

    public void setOtorgamientoListStr(String otorgamientoListStr) {
        this.otorgamientoListStr = otorgamientoListStr;
    }

    public String getOtorgamientosBecariosCountStr() {
        return otorgamientosBecariosCountStr;
    }

    public void setOtorgamientosBecariosCountStr(String otorgamientosBecariosCountStr) {
        this.otorgamientosBecariosCountStr = otorgamientosBecariosCountStr;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public PersonalAdministrativo getJefe() {
        return jefe;
    }

    public void setJefe(PersonalAdministrativo jefe) {
        this.jefe = jefe;
    }

    public Boolean getI() {
        return i;
    }

    public void setI(Boolean i) {
        this.i = i;
    }

    public List<CampoResumen> getListaCampos() {
        return listaCampos;
    }

    public void setListaCampos(List<CampoResumen> listaCampos) {
        this.listaCampos = listaCampos;
    }

    public InputStream getExcelStream() {
        return excelStream;
    }

    public void setExcelStream(InputStream excelStream) {
        this.excelStream = excelStream;
    }

    public String getContentDisposition() {
        return contentDisposition;
    }

    public void setContentDisposition(String contentDisposition) {
        this.contentDisposition = contentDisposition;
    }

    public String getBecasL() {
        return becasL;
    }

    public void setBecasL(String becasL) {
        this.becasL = becasL;
    }

    public String getBecasString() {
        return becasString;
    }

    public void setBecasString(String becasString) {
        this.becasString = becasString;
    }

    public Integer getE() {
        return e;
    }

    public void setE(Integer e) {
        this.e = e;
    }

    public String reporteAutoevaluacion() {
        return SUCCESS;
    }

    public String descargarReporte() {
        if ("x".equals(periodo)) {
            periodo = getDaos().getPeriodoDao().getPeriodoActivo().getId().toString();
        }
        Periodo p = getDaos().getPeriodoDao().findById(new BigDecimal(periodo));
        List<Object[]> ot = getDaos().getAlumnoDao().alumnosAutoevaluacion(periodo);
        List<Object[]> otX = new ArrayList<>();
        total = 0;
        for (Object[] o : ot) {
            Object[] ax = new Object[32];
            ax[0] = o[0];//Nivel
            ax[1] = o[1];//Turno
            ax[2] = o[2];//Unidad Academica
            ax[3] = o[3];//Boleta
            ax[4] = o[4];//CURP
            ax[5] = o[5];//Nombre
            ax[6] = o[6];//Apellido Paterno
            ax[7] = o[7];//Apellido Materno
            ax[8] = o[8];//Genero
            ax[9] = o[9];//Beca
            ax[10] = o[10];//Programa
            ax[11] = o[11];//Modalidad
            ax[12] = o[12];//Carrera
            ax[13] = o[13];//Monto
            otX.add(ax);
        }
        Date d = new Date();
        SimpleDateFormat formater = new SimpleDateFormat("-ddMMyy-HHmmss");
        String f = formater.format(d);
        ExcelExport excelExport = new ExcelExport();
        String[] encabezado = new String[]{"NIVEL", "TURNO", "UNIDAD ACADEMICA", "BOLETA", "CURP", "NOMBRE", "APELLIDO PATERNO", "APELLIDO MATERNO",
            "GENERO", "BECA", "PROGRAMA", "MODALIDAD", "CARRERA", "MONTO"};
        setContentDisposition("attachment; filename=\"ReporteAutoevaluación-" + getDaos().getPeriodoDao().findById(new BigDecimal(periodo)).getClave() + f + ".xlsx\"");
        excelStream = excelExport.construyeExcel(encabezado, otX);
        return "archivo";
    }

    public List<Movimiento> getResumenMovimientosList() {
        return resumenMovimientosList;
    }

    public void setResumenMovimientosList(List<Movimiento> resumenMovimientosList) {
        this.resumenMovimientosList = resumenMovimientosList;
    }
}
