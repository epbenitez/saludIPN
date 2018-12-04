package com.becasipn.actions.admin;

import com.becasipn.actions.BaseReportAction;
import com.becasipn.business.PeriodoBO;
import com.becasipn.domain.CampoResumen;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.Cuestionario;
import com.becasipn.persistence.model.CuestionarioRespuestasUsuario;
import com.becasipn.persistence.model.CuestionarioTransporte;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.SolicitudBeca;
import com.becasipn.persistence.model.UnidadAcademica;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.util.ExcelExport;
import com.becasipn.util.UtilFile;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author Tania G. Sánchez
 */
public class AdministracionCuestionariosAction extends BaseReportAction implements MensajesAdmin {

    public static final String ASIGNADOS = "asignados";
    public static final String RECHAZADOS = "rechazados";
    public static final String ESPERA = "espera";
    public static final String PENDIENTE = "pendiente";
    public static final String ARCHIVO = "archivo";
    public static final String PDF = "pdf";
    private BigDecimal alumnoId;
    private String cuestionarioId;
    public List<Periodo> periodos;
    List<CuestionarioRespuestasUsuario> respuestasUsuario = new ArrayList<>();
    private List<Object[]> listaAx = new ArrayList<>();
    private List<CampoResumen> listaCampos = new ArrayList<>();
    private int total;
    private Alumno alumno;
    private UnidadAcademica unidadAcademica;
    private String folio;
    private String periodoActual;
    private String periodo;
    private Date hoy = new Date();
    private CuestionarioTransporte respuestasTransporte;
    private String numeroDeBoleta;
    private Boolean contestoSalud;
    private Boolean contestoCuestionario = false;
    private Boolean validacionInscripcion = false;
    private Integer op;
    private Integer transferencia;
    private InputStream excelStream;
    private String contentDisposition;
    private String ingresoPorPersona = "";    

    public String buscar() {
        return SUCCESS;
    }

    public String ese() {
        Cuestionario c = new Cuestionario();
        //Periodo
        PeriodoBO bo = new PeriodoBO(getDaos());
        Periodo activeTerm = bo.getPeriodoActivo();
        if (cuestionarioId != null && !cuestionarioId.isEmpty()) {
            //Cuestionario
            c = getDaos().getCuestionarioDao().findById(new BigDecimal(cuestionarioId));
        } else {
            addActionError(getText("misdatos.alumno.no.especifico.cuestionario"));
        }
        if (c != null) {
            //Alumno
            alumno = getDaos().getAlumnoDao().findById(alumnoId);

            contestoCuestionario = getDaos().getSolicitudBecaDao().exiteESEPeriodoActivo(alumnoId, c.getId(), activeTerm.getId());
            if (contestoCuestionario) {
                respuestasUsuario = getDaos().getCuestionarioRespuestasUsuarioDao().getResultadosUsuario(c.getId(), alumno.getUsuario().getId(), activeTerm.getId());
            } else {
                validacionInscripcion = getDaos().getOtorgamientoDao().tieneValidacionInscripcion(activeTerm, alumnoId, null);
                if (validacionInscripcion) {
                    contestoCuestionario = getDaos().getSolicitudBecaDao().exiteESEPeriodoActivo(alumnoId, c.getId(), activeTerm.getPeriodoAnterior().getId());
                    if (contestoCuestionario) {
                        respuestasUsuario = getDaos().getCuestionarioRespuestasUsuarioDao().getResultadosUsuario(c.getId(), alumno.getUsuario().getId(), activeTerm.getPeriodoAnterior().getId());
                    }
                }
            }
        } else {
            addActionError(getText("misdatos.alumno.no.existe.cuesionario"));
        }

        SolicitudBeca sb;
        if (validacionInscripcion && contestoCuestionario) {
            sb = getDaos().getSolicitudBecaDao().getESEAlumno(alumnoId, activeTerm.getPeriodoAnterior().getId());
        } else {
            sb = getDaos().getSolicitudBecaDao().getESEAlumno(alumnoId, activeTerm.getId());
        }
        if (sb != null) {
            transferencia = sb.getPermiteTransferencia();
            ingresoPorPersona = sb.getIngresosPercapitaPesos() == null ? "" : sb.getIngresosPercapitaPesos().toString();
        }
        //Bandera para conocer si el alumno ya contesto el censo de salud.
        //2018/08/16: Dr. Gilberto solicita quitar el censo de saludo como requisito para el proceso de becas.
        //No se elimina la lógica.
        //contestoSalud = getDaos().getSolicitudBecaDao().contestoEncuestaSalud(alumnoId, activeTerm.getId());
        contestoSalud = Boolean.TRUE;

        return SUCCESS;
    }

    public String eset() {
        PeriodoBO bo = new PeriodoBO(getDaos());
        Periodo activeTerm = bo.getPeriodoActivo();
        alumno = getDaos().getAlumnoDao().findById(alumnoId);
        BigDecimal cId = new BigDecimal(cuestionarioId);

        if (cuestionarioId == null || cuestionarioId.isEmpty()) {
            addActionError(getText("misdatos.alumno.no.especifico.cuestionario"));
        }

        contestoCuestionario = getDaos().getSolicitudBecaDao().exiteESEPeriodoActivo(alumnoId, cId, activeTerm.getId());
        if (contestoCuestionario) {
            respuestasTransporte = getDaos().getCuestionarioTransporteDao().respuestasTransporte(alumno.getUsuario().getId(), activeTerm.getId());
        } else {
            validacionInscripcion = getDaos().getOtorgamientoDao().tieneValidacionInscripcion(activeTerm, alumnoId, true);
            if (validacionInscripcion) {
                contestoCuestionario = getDaos().getSolicitudBecaDao().exiteESEPeriodoActivo(alumnoId, cId, activeTerm.getPeriodoAnterior().getId());
                if (contestoCuestionario) {
                    respuestasTransporte = getDaos().getCuestionarioTransporteDao().respuestasTransporte(alumno.getUsuario().getId(), activeTerm.getPeriodoAnterior().getId());
                }
            }
        }

        return SUCCESS;
    }

    public String censoSalud() {
        //Usuario
        Usuario u = (Usuario) ActionContext.getContext().getSession().get("usuario");
        //Cuestionario
        Cuestionario c = getDaos().getCuestionarioDao().findById(new BigDecimal(cuestionarioId));
        //Alumno
        alumno = getDaos().getAlumnoDao().findById(alumnoId);
        //Periodo
        PeriodoBO bo = new PeriodoBO(getDaos());
        Periodo periodo = bo.getPeriodoActivo();
        //Bandera para conocer si el alumno ya contesto el censo de salud.
        if (c == null) {
            addActionError(getText("misdatos.alumno.no.existe.cuesionario"));
        } else if (cuestionarioId == null || cuestionarioId.isEmpty()) {
            addActionError(getText("misdatos.alumno.no.especifico.cuestionario"));
        } else {
            contestoCuestionario = getDaos().getSolicitudBecaDao().exiteESEPeriodoActivo(alumno.getId(), c.getId(), periodo.getId());
            respuestasUsuario = getDaos().getCuestionarioRespuestasUsuarioDao().getResultadosUsuario(c.getId(), alumno.getUsuario().getId(), periodo.getId());
        }
        return SUCCESS;
    }

    public String detalleSolicitudes() {
        getParametros().put("imagenPlaca", "/com/becasipn/jasper/pleca-SEP.jpg");
        Periodo per = getDaos().getPeriodoDao().getPeriodoActivo();
        getParametros().put("periodo", per.getClave());
        if (isFuncionario() || isResponsable() || isFuncionario()) {
            Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
            unidadAcademica = getDaos().getPersonalAdministrativoDao().findByUsuario(usuario.getId()).getUnidadAcademica();
            listaAx = getDaos().getSolicitudBecaDao().alumnosEstatusSolicitud(unidadAcademica.getId(), op < 4 ? new BigDecimal(op) : null, per.getId());
        } else {
            listaAx = getDaos().getSolicitudBecaDao().alumnosEstatusSolicitud(null, op < 4 ? new BigDecimal(op) : null, per.getId());
        }
        total = 0;
        if (listaAx != null) {
            for (Object[] o : listaAx) {
                CampoResumen ax = new CampoResumen();
                ax.setNum(total + 1);
                ax.setBoleta("" + o[0]);//BOLETA
                ax.setCurp("" + o[1]);//NOMBRE ESCUELA
                ax.setNombreCompleto(o[2] == null ? "-" : "" + o[2]);//MOTIVO RECHAZO
                ax.setGenero(o[3] == null ? "-" : "" + o[3]);//TURNO
                ax.setNombreBeca(o[4] == null ? "-" : "" + o[4]);//BECA ASIGNADA
                ax.setNomProceso(o[5] == null ? "-" : "" + o[5]);//BECA SOLICITADA
                ax.setGrupoEdad(o[6] == null ? "-" : "" + o[6]);//GRUPO EDAD
                ax.setEstado(o[7] == null ? "-" : "" + o[7]);//ESTADO
                ax.setMunicipio(o[8] == null ? "-" : "" + o[8]);//MUNICIPIO
                ax.setLocalidad(o[9] == null ? "-" : "" + o[9]);//LOCALIDAD
                if (op == 1 && o == null) {
                } else {
                    listaCampos.add(ax);
                }
                total = total + 1;
            }
            JRBeanCollectionDataSource lista = new JRBeanCollectionDataSource(listaCampos);
            getParametros().put("listaDataSource", lista);
        }
        switch (op) {
            case 1:
                return ASIGNADOS;
            case 2:
                return RECHAZADOS;
            case 3:
                return ESPERA;
            default:
                String[] encabezado = new String[]{"BOLETA", "UA", "BECA SOLICITADA"};
                return datosExcel(listaAx, encabezado, 4);
        }
    }

    //05/06/18 Actualizacion de reportes excel. Reporte obsoleto,  se comento todo el contenido
    public String solicitudes() {
    /*    Periodo per = getDaos().getPeriodoDao().getPeriodoActivo();
        List<Object[]> listaTmp;
        if (isFuncionario() || isResponsable()) {
            Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
            unidadAcademica = getDaos().getPersonalAdministrativoDao().findByUsuario(usuario.getId()).getUnidadAcademica();
            listaTmp = getDaos().getSolicitudBecaDao().alumnosEstatusSolicitudPendiente(unidadAcademica.getId(), per.getId());
        } else {
            listaTmp = getDaos().getSolicitudBecaDao().alumnosEstatusSolicitudPendiente(null, per.getId());
        }
        total = 0;
        List<Object[]> otX = new ArrayList<>();
        if (listaTmp != null) {
            for (Object[] o : listaTmp) {
                BigDecimal b;
                String sx;
                Object[] ax = new Object[20];
                b = (BigDecimal) o[14];
                ax[0] = o[0] == null ? "-" : o[0];//UA
                ax[1] = o[1] == null ? "-" : o[1];//Boleta
                ax[2] = o[2] == null ? "-" : o[2];//Beca
                ax[3] = o[3] == null ? "-" : o[3];//Ingreso
                if (ax[2].toString().startsWith("Transporte")) {
                    Float gastoTransporte = getDaos().getTransporteTrasladoDao().gastoTraslado(b, per.getId());
                    ax[4] = gastoTransporte == null ? "-" : gastoTransporte;//Gasto Transporte
                } else {
                    CuestionarioRespuestasUsuario cr = getDaos().getCuestionarioRespuestasUsuarioDao().getPreguntaUsuarioActual(b, new BigDecimal(22));
                    ax[4] = cr == null ? "-" : cr.getRespuesta().getNombre();//Gasto Transporte
                }
                ax[5] = o[4] == null ? "-" : o[4];//Promedio
                ax[6] = o[5] == null ? "-" : o[5];//Semestre
                ax[7] = o[6] == null ? "NO" : "SI";//Beca Anterior
                b = (BigDecimal) o[13];
                ax[8] = b == null ? "-" : b.intValue() < 33 ? "MEXICANA" : "EXTRANJERO";//Nacionalidad
                ax[9] = o[7] == null ? "-" : o[7];//Modalidad
                b = (BigDecimal) o[8];
                ax[10] = b == null ? "-" : b.intValue() == 0 ? "NO" : "SI";//Regular
                b = (BigDecimal) o[9];
                switch (b.intValue()) {
                    case 0:
                        sx = "NO CUMPLE";
                        break;
                    case 1:
                        sx = "CARGA MINIMA";
                        break;
                    case 2:
                        sx = "CARGA MEDIA";
                        break;
                    case 3:
                        sx = "CARGA MAXIMA";
                        break;
                    default:
                        sx = "-";
                        break;
                }
                ax[11] = b == null ? "-" : sx;//Carga
                ax[12] = o[10] == null ? "-" : o[10];//Carrera
                ax[13] = o[11] == null ? "NO" : "SI";//Prospera
                b = (BigDecimal) o[13];
                
                ax[14] = b == null ? "-" : b.intValue() == 0 ? "NO" : "SI";//Mun Pobreza		
                ax[15] = o[15] == null ? "Pendiente" : o[15]; // 
                otX.add(ax);
                
            }
        }

        ExcelExport excelExport = new ExcelExport();
        setContentDisposition("attachment; filename=\"SolicitudesPendientes_" + UtilFile.dateToString(new Date(), "yyyy-MM-dd") + ".xlsx\"");

        String[] encabezado = new String[]{"UA", "BOLETA", "BECA PREASIGNADA",
                "INGRESO PER CAPITA", "GASTO TRANS",
                "PROMEDIO", "SEMESTRE", "BECA PERIODO ANT",
                "NACIONALIDAD", "MODALIDAD", "REGULAR", "CARGA",
                "CARRERA", "PROSPERA", "MUN POBREZA", "ESTATUS SOLICITUD"};
        excelStream = excelExport.construyeExcel(encabezado, otX);
            */
        return ARCHIVO;

    }

    public String resumenPeriodo() {
        periodo = getDaos().getPeriodoDao().getPeriodoActivo().getId().toString();
        periodos = getDaos().getPeriodoDao().findAllFrom(new BigDecimal(35));
        return SUCCESS;
    }

    
    public String descargar() {
        Periodo p = getDaos().getPeriodoDao().findById(new BigDecimal(periodo));

        if (isFuncionario() || isResponsable()) {
            Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
            unidadAcademica = getDaos().getPersonalAdministrativoDao().findByUsuario(usuario.getId()).getUnidadAcademica();
            listaAx = getDaos().getSolicitudBecaDao().alumnosEstatusSolicitud(unidadAcademica.getId(), op < 4 ? new BigDecimal(op) : null, p.getId());
        } else {
            listaAx = getDaos().getSolicitudBecaDao().alumnosEstatusSolicitud(null, op < 4 ? new BigDecimal(op) : null, p.getId());
        }
        String[] encabezado;
        switch (op) {
            case 1:
                encabezado = new String[]{"BOLETA", "UA", "BECA ASIGNADA"};
                return datosExcel(listaAx, encabezado, op);
            case 2:
                encabezado = new String[]{"BOLETA", "UA", "MOTIVO RECHAZO"};
                return datosExcel(listaAx, encabezado, op);
            case 3:
                encabezado = new String[]{"BOLETA", "UA", "TURNO EN LISTA ESPERA"};
                return datosExcel(listaAx, encabezado, op);
            default:
                encabezado = new String[]{"BOLETA", "UA", "BECA SOLICITADA"};
                return datosExcel(listaAx, encabezado, 4);
        }
    }

//06/06/18 Actualizacion de reportes excel. Reporte obsoleto,  se comento todo el contenido
    public String descarga() {
    /*    Periodo p = getDaos().getPeriodoDao().findById(new BigDecimal(periodo));

        if (isFuncionario() || isResponsable()) {
            Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
            unidadAcademica = getDaos().getPersonalAdministrativoDao().findByUsuario(usuario.getId()).getUnidadAcademica();
            listaAx = getDaos().getSolicitudBecaDao().alumnosEstatusSolicitudT(unidadAcademica.getId(), p.getId());
        } else {
            listaAx = getDaos().getSolicitudBecaDao().alumnosEstatusSolicitudT(null, p.getId());
        }

        String[] encabezado = new String[]{"BOLETA", "UA", "BECA ASIGNADA"};
        List<Object[]> otX = new ArrayList<>();
        for (Object[] o : listaAx) {
            Object[] ax = new Object[20];
            ax[0] = o[0];
            ax[1] = o[1];
            ax[2] = o[2] + " " + o[3] + " " + o[4];
            ax[3] = o[5] == null ? "-" : o[5];
            ax[4] = o[6] == null ? "-" : o[6];
            ax[5] = o[7] == null ? "-" : o[7];
            ax[6] = o[8] == null ? "-" : o[8];
            ax[7] = o[9] == null ? "-" : o[9];
            ax[8] = o[10] == null ? "-" : o[10];
            ax[9] = o[11] == null ? "-" : o[11];
            ax[10] = o[12] == null ? "-" : o[12];
            ax[11] = o[13] == null ? "-" : o[13];
            ax[12] = o[14] == null ? "-" : o[14];
            ax[12] = o[15] == null ? "-" : o[15];
            ax[14] = o[17] == null ? "-" : o[17];
            otX.add(ax);
            total = total + 1;
        }
        ExcelExport excelExport = new ExcelExport();
        setContentDisposition("attachment; filename=\"CuadroResumenSolicitudes-" + p.getClave() + ".xlsx\"");
        excelStream = excelExport.construyeExcel(encabezado, otX);
            */
        return ARCHIVO;
    }
    //06/06/18 Actualizacion de reportes excel. Reporte obsoleto,  se comento todo el contenido
    private String datosExcel(List<Object[]> lx, String[] encabezado, Integer estatusId) {
        
        /*List<Object[]> otX = new ArrayList<>();
        String titulo;
        Integer id;
        switch (estatusId) {
            case 1:
                titulo = "Asignados";
                id = 4;
                break;
            case 2:
                titulo = "Rechazados";
                id = 2;
                break;
            case 3:
                titulo = "EnEspera";
                id = 3;
                break;
            default:
                titulo = "Pendientes";
                id = 5;
                break;
        }

        for (Object[] o : lx) {
            Object[] ax = new Object[4];
            ax[0] = o[0];
            ax[1] = o[1];
            ax[2] = o[id] == null ? "-" : o[id];
            otX.add(ax);
            total = total + 1;
        }
        ExcelExport excelExport = new ExcelExport();
        setContentDisposition("attachment; filename=\"CuadroResumen" + titulo + ".xlsx\"");
        excelStream = excelExport.construyeExcel(encabezado, otX);
                */
        return ARCHIVO;
    }

    public BigDecimal getAlumnoId() {
        return alumnoId;
    }

    public void setAlumnoId(BigDecimal alumnoId) {
        this.alumnoId = alumnoId;
    }

    public String getCuestionarioId() {
        return cuestionarioId;
    }

    public void setCuestionarioId(String cuestionarioId) {
        this.cuestionarioId = cuestionarioId;
    }

    public List<CuestionarioRespuestasUsuario> getRespuestasUsuario() {
        return respuestasUsuario;
    }

    public void setRespuestasUsuario(List<CuestionarioRespuestasUsuario> respuestasUsuario) {
        this.respuestasUsuario = respuestasUsuario;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getPeriodoActual() {
        return periodoActual;
    }

    public void setPeriodoActual(String periodoActual) {
        this.periodoActual = periodoActual;
    }

    public Date getHoy() {
        return hoy;
    }

    public void setHoy(Date hoy) {
        this.hoy = hoy;
    }

    public CuestionarioTransporte getRespuestasTransporte() {
        return respuestasTransporte;
    }

    public void setRespuestasTransporte(CuestionarioTransporte respuestasTransporte) {
        this.respuestasTransporte = respuestasTransporte;
    }

    public String getNumeroDeBoleta() {
        return numeroDeBoleta;
    }

    public void setNumeroDeBoleta(String numeroDeBoleta) {
        this.numeroDeBoleta = numeroDeBoleta;
    }

    public Boolean getContestoSalud() {
        return contestoSalud;
    }

    public void setContestoSalud(Boolean contestoSalud) {
        this.contestoSalud = contestoSalud;
    }

    public Boolean getContestoCuestionario() {
        return contestoCuestionario;
    }

    public void setContestoCuestionario(Boolean contestoCuestionario) {
        this.contestoCuestionario = contestoCuestionario;
    }

    public Boolean getValidacionInscripcion() {
        return validacionInscripcion;
    }

    public void setValidacionInscripcion(Boolean validacionInscripcion) {
        this.validacionInscripcion = validacionInscripcion;
    }

    public List<CampoResumen> getListaCampos() {
        return listaCampos;
    }

    public void setListaCampos(List<CampoResumen> listaCampos) {
        this.listaCampos = listaCampos;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public UnidadAcademica getUnidadAcademica() {
        return unidadAcademica;
    }

    public void setUnidadAcademica(UnidadAcademica unidadAcademica) {
        this.unidadAcademica = unidadAcademica;
    }

    public Integer getOp() {
        return op;
    }

    public void setOp(Integer op) {
        this.op = op;
    }

    public Integer getTransferencia() {
        return transferencia;
    }

    public void setTransferencia(Integer transferencia) {
        this.transferencia = transferencia;
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

    public String getIngresoPorPersona() {
        return ingresoPorPersona;
    }

    public void setIngresoPorPersona(String ingresoPorPersona) {
        this.ingresoPorPersona = ingresoPorPersona;
    }

    public List<Object[]> getListaAx() {
        return listaAx;
    }

    public void setListaAx(List<Object[]> listaAx) {
        this.listaAx = listaAx;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public List<Periodo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<Periodo> periodos) {
        this.periodos = periodos;
    }

}
