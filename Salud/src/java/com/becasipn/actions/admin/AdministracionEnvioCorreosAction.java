package com.becasipn.actions.admin;

import com.becasipn.actions.BaseReportAction;
import com.becasipn.business.AlumnoBO;
import com.becasipn.business.EnvioCorreosBO;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.Nivel;
import com.becasipn.persistence.model.PersonalAdministrativo;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.util.ExcelExport;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Workbook;

public class AdministracionEnvioCorreosAction extends BaseReportAction implements MensajesAdmin {

    public static final String ARCHIVO = "archivo";

    private Integer opcion;

    private List<Alumno> alumnosLst = new ArrayList<>();
    private List<Alumno> alumnosError = new ArrayList<>();
    private List<List<String>> boletasError = new ArrayList<>();
    private boolean hayBoletasErroneas;
    private String nombreExcel;
    private String nivel;
    private String unidadAcademica;
    private String beca;
    private String tipoBeca;
    private String movimiento;
    private String proceso;
    private String alumnos;
    private String alumnosL;  
    private BigDecimal periodo;
    private BigDecimal usuarioId;
    private Boolean carga = false;
    
    // Envío de correos
    private String asunto;
    private String atentamente;
    private String mensaje;
    private String correo;
    private String cc;
    private int totalSent; // Total de coreros enviados correctamente
    private int totalFailed; // Total de correos fallidos

    private BigDecimal alumnosRegistrados;
    private BigDecimal revalidantesNoRegistrados;
    private BigDecimal alumnossinESE;
    private BigDecimal alumnosDatosIncorrectos;
    private BigDecimal otorgamientosTarjetaAlumno;

    private File upload;
    private String uploadFileName;
    private String uploadContentType;
    private List<String> log = new LinkedList<>();

    private InputStream excelStream;
    private String contentDisposition;

    public String lista() {
        carga = false;
        if (isFuncionario() || isResponsable()) {
            Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
            unidadAcademica = getDaos().getPersonalAdministrativoDao().findByUsuario(usuario.getId()).getUnidadAcademica().getId().toString();
        }
        // counts();
        return SUCCESS;
    }

    public String form() {
        Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        usuarioId = usuario.getId();
        if (isFuncionario() || isResponsable()) {
            unidadAcademica = getDaos().getPersonalAdministrativoDao().findByUsuario(usuarioId).getUnidadAcademica().getId().toString();
            correo = getDaos().getPersonalAdministrativoDao().findByUsuario(usuarioId).getCorreoElectronico();
        }
        return SUCCESS;
    }
    
    public String enviarPrueba() throws Exception {
        carga = false;
        AlumnoBO bo = new AlumnoBO(getDaos());
        if (asunto == null || mensaje == null) {
            addActionError(getText("correos.error.asuntoymensaje"));
            return "form";
        }
        
        // Crea usuario, y personal, para obtener su mail
        Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        if (isFuncionario() || isResponsable()) {
            unidadAcademica = getDaos().getPersonalAdministrativoDao().findByUsuario(usuario.getId()).getUnidadAcademica().getId().toString();
        }
        PersonalAdministrativo personal = getDaos().getPersonalAdministrativoDao().findByUsuario(usuario.getId());
        if (personal == null) {
            addActionError(getText("correos.error.personal"));
            return "form";
        }
        correo = personal.getCorreoElectronico();
        if (correo == null || !bo.correoValido(correo)) {
            addActionError(getText("correos.error.correo"));
            return "form";
        }
        
        String[] mapData = {usuario.getId().toString(), opcion.toString(), nivel, unidadAcademica, periodo.toString()};
        Map info;
        // si logro aparecer el null al quitar la configuración de struts, sería lo ideal
        // así puedo seguir usando este if, pero si no, hará que subir el tamaño menor a uno en vez del null
        if (getUpload().length() > 1) {
            Boolean wrongSize = getUpload().length() > 200000;
            Boolean correctType = uploadContentType.equals("application/vnd.ms-excel")
                    || uploadContentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    || uploadContentType.equals("application/pdf")
                    || uploadContentType.equals("image/jpeg")
                    || uploadContentType.equals("image/png");
            if (!correctType || wrongSize) {
                addActionError(getText("correos.error.archivo"));
                return "form";
            } else {
                info = bo.modeloGenerico(asunto, atentamente, mensaje, getUpload(), uploadFileName);
            }
        } else
            info = bo.modeloGenerico(asunto, atentamente, mensaje, null, null);

        Boolean res = bo.enviarCorreoCC(correo, info, mapData);
        totalSent = res ? 1 : 0;
        totalFailed = res ? 0 : 1;

        if (res) {
            addActionMessage(getText("correos.enviados"));
            return "form";
        } else {
            addActionError(getText("correos.error.envio"));
            return "form";
        }
    }

    public String enviarCorreos() {
        
        System.out.println("Envío de correos action");
        carga = false;
        AlumnoBO bo = new AlumnoBO(getDaos());
        List<Alumno> lstAlumnos = new ArrayList<>();
        if (asunto == null || mensaje == null) {
            addActionError(getText("correos.error.asuntoymensaje"));
            return "form";
        }
        Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        if (isFuncionario() || isResponsable()) {
            unidadAcademica = getDaos().getPersonalAdministrativoDao().findByUsuario(usuario.getId()).getUnidadAcademica().getId().toString();
        }

        lstAlumnos = getDestinatarios();
        int totalAlumns = lstAlumnos.size();

        if (lstAlumnos == null || lstAlumnos.isEmpty() ? Boolean.TRUE : Boolean.FALSE) {
            addActionError(getText("correos.error.listavacia"));
            return "form";
        }

        String[] mapData = {usuario.getId().toString(), opcion.toString(), nivel, unidadAcademica, periodo.toString()};
        Map info;
        if (getUpload() != null && getUpload().length() > 0) {            
            info = bo.modeloGenerico(asunto, atentamente, mensaje, getUpload(), uploadFileName);
        } else {
            info = bo.modeloGenerico(asunto, atentamente, mensaje, null, null);
        }

        alumnosError = bo.enviarCorreos(lstAlumnos, info, mapData);
        Boolean res = alumnosError.isEmpty() || alumnosError == null;
        
        totalSent = totalAlumns - alumnosError.size();
        totalFailed = alumnosError.size();

        if (res) {
            addActionMessage(getText("correos.enviados"));
            return "form";
        } else {
            addActionError(getText("correos.error.envio"));
            return "form";
        }
    }

    private List<Alumno> getDestinatarios() {
        List<Alumno> lstDest = new ArrayList<>();
        switch (opcion) {
            case 1:
                lstDest = getDaos().getAlumnoDao().alumnosRegistrados(nivel, unidadAcademica);
                break;
            case 2:
                lstDest = getDaos().getAlumnoDao().alumnosRevalidantesNoRegistrados(nivel, unidadAcademica);
                break;
            case 3:
                lstDest = getDaos().getAlumnoDao().alumnosESEincompleto(nivel, unidadAcademica);
                break;
            case 4:
                lstDest = getDaos().getAlumnoDao().alumnosDatosIncorrectos(nivel, unidadAcademica, periodo);
                break;
            case 5:
                break;
            case 6:
                lstDest = getDaos().getAlumnoDao().alumnosFiltros(nivel, unidadAcademica, beca, tipoBeca, movimiento, proceso, alumnos == null || alumnos.length() < 2 ? "" : alumnos.substring(1, alumnos.length() - 1));
                break;
            case 7:
                return alumnosLst;
            case 8:
                lstDest = getDaos().getAlumnoDao().getSolicitudes(alumnosL);
                break;
            default:
                addActionError(getText("correos.error.accion"));
                return null;
        }
        return lstDest;
    }
    
    public String limpia() {
        alumnosLst.clear();
        setNombreExcel("");
        carga = false;
        
        return "lista";
    }

    public String carga() {
        // alumnosLst = null;
        carga = true;
//        counts();
        LOG.info(String.format("%s : fileUpload", getClass().getName()));
        AlumnoBO alumnoBO = new AlumnoBO(getDaos());
        
        EnvioCorreosBO bo = EnvioCorreosBO.getInstance(getDaos());

        if (alumnosLst.isEmpty() && getUpload() != null) {
            // Excel 2003 Format
            if ("application/vnd.ms-excel".equalsIgnoreCase(getUploadContentType())) {
                LOG.debug("Excel 2003 Format");
                LOG.debug("calling readXmlFile(File xls) method...");
                try {
                    //INVOCA PROCESA ARCHIVO
                    Workbook wb = null;
                    wb = alumnoBO.readXlsFile(new FileInputStream(getUpload()));
                    setNombreExcel(uploadFileName);
                    bo.setAlumnosList(wb);
                    alumnosLst = bo.getAlumnos();
                    setBoletasError(bo.getErrores());
                    if (alumnosLst.isEmpty()) {
                        addActionError("Hay un problema con su archivo, verifique y vuelva a intentarlo");
                    }
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                    addActionError(getText("carga.archivo.error.formato"));
                    return "lista";
                } catch (org.apache.poi.hssf.OldExcelFormatException le) {
                    addActionError(getText("carga.archivo.error.sistema.version.excel")); // + hideError(le));
                    le.printStackTrace();
                    return "lista";
                } catch (java.lang.Exception le) {
                    if (le.toString().contains("password")) {
                        addActionError("La contraseña especificada no es correcta, por favor, verifique.");
                    } else {
                        addActionError(getText("carga.archivo.error.sistema")); // + hideError(le));
                    }
                    le.printStackTrace();
                    return "lista";
                }
            } // Excel 2007 Format
            else if ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equalsIgnoreCase(getUploadContentType())) {
                LOG.debug("Excel 2007 Format");
                LOG.debug("calling readXmlxFile(File xlsx) method...");

                try {
                    Workbook wb = null;
                    wb = alumnoBO.readXlsxFile(new FileInputStream(getUpload()));
                    setNombreExcel(uploadFileName);
                    bo.setAlumnosList(wb);
                    alumnosLst = bo.getAlumnos();
                    setBoletasError(bo.getErrores());
                    if (alumnosLst.isEmpty()) {
                        addActionError("Hay un problema con su archivo, verifique y vuelva a intentarlo");
                    }
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                    addActionError(getText("carga.archivo.error.formato"));
                    return "lista";
                } catch (java.lang.Exception le) {
                    addActionError(getText("carga.archivo.error.carga"));
                    le.printStackTrace();
                    return "lista";
                }
            } // Si no es el content type deseado
            else {
                addActionError(getText("carga.archivo.error.formato"));
                return "lista";
            }
        } else if (!alumnosLst.isEmpty()) {
            // Meter enlace para borrar lista
            StringBuilder sb = new StringBuilder();
            sb.append("El sistema ya tiene una lista de alumnos, fue creada a partir del archivo ");
            sb.append(nombreExcel).append(".");
            sb.append(" Si deseas eliminar la información referente a esa carga, da clic");
            sb.append(" <a href=\"/admin/limpiaEnvioCorreos.action\">aquí</a>.");
                    
            addActionError(sb.toString());
            
            return "lista";
        } else {
            addActionError(getText("carga.archivo.error.carga"));
            return "lista";
        }
        addActionMessage("Se han cargado los numeros de boleta en el listado.");
        return "lista";
    }

    public void counts() {
        BigDecimal periodoId = getDaos().getPeriodoDao().getPeriodoActivo().getId();

        alumnosRegistrados = getDaos().getAlumnoDao().alumnosRegistradosC(nivel, unidadAcademica);
        revalidantesNoRegistrados = getDaos().getAlumnoDao().alumnosRevalidantesNoRegistradosC(nivel, unidadAcademica);
        alumnossinESE = getDaos().getAlumnoDao().alumnosESEincompletoC(nivel, unidadAcademica);
        alumnosDatosIncorrectos = getDaos().getAlumnoDao().alumnosDatosIncorrectosC(nivel, unidadAcademica, periodoId);
        // Fue eliminada la llamada al método otorgamientosTarjetaAlumno
    }

    public String downResumen() {
        List<Alumno> lstAlumnos = new ArrayList<>();
        Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        if (isFuncionario() || isResponsable()) {
            unidadAcademica = getDaos().getPersonalAdministrativoDao().findByUsuario(usuario.getId()).getUnidadAcademica().getId().toString();
        }
        // Se obtiene la listota de alumnos a los que se les enviara el correo
        // seg+un el filtro elegido en el front
        lstAlumnos = getDestinatarios();

        List<Object[]> datos = new ArrayList<>();

        for (Alumno alumno : lstAlumnos) {
            DatosAcademicos dA = getDaos().getDatosAcademicosDao().ultimosDatos(alumno.getId());
            String[] datosAlumno = new String[4];
            datosAlumno[0] = alumno.getBoleta();
            datosAlumno[1] = alumno.getFullName();
            datosAlumno[2] = dA.getUnidadAcademica().getNombreCorto();
            datosAlumno[3] = alumno.getCorreoElectronico();
            datos.add(datosAlumno);
        }

        ExcelExport excelExport = new ExcelExport();
        String[] encabezado = new String[]{"BOLETA", "NOMBRE", "UNIDAD ACADÉMICA", "EMAIL"};
        setContentDisposition("attachment; filename=" + crearNombreArch());
        excelStream = excelExport.construyeExcel(encabezado, datos);

        return ARCHIVO;
    }

    private String crearNombreArch() {
        StringBuilder nombre = new StringBuilder();
        String periodoStrng = getDaos().getPeriodoDao().findById(periodo).getClave();
        Nivel lvl = getDaos().getNivelDao().findById(new BigDecimal(nivel));
        String lvlStr = "";
        if (lvl != null) {
            lvlStr = lvl.getClave();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd");
        Date date = new Date();
        String dateStr = formatter.format(date);
        if (periodoStrng == null) {
            periodoStrng = "";
        }
        String optionStr = crearOpcionStr();
        nombre.append("\"EnvioCorreos-")
                .append(optionStr)
                .append(dateStr)
                .append(lvlStr)
                .append(unidadAcademica)
                .append(periodoStrng)
                .append(".xlsx\"");

        return nombre.toString();
    }

    private String crearOpcionStr() {
        String optionStr = "";
        switch (opcion) {
            case 1:
                optionStr = "Registrados";
                break;
            case 2:
                optionStr = "NoRegsitrados";
                break;
            case 3:
                optionStr = "ESEIncompleto";
                break;
            case 4:
                optionStr = "Bancarios";
                break;
            default:
                break;
        }
        return optionStr;
    }

    public String getUnidadAcademica() {
        return unidadAcademica;
    }

    public void setUnidadAcademica(String unidadAcademica) {
        this.unidadAcademica = unidadAcademica;
    }

    public Integer getOpcion() {
        return opcion;
    }

    public void setOpcion(Integer opcion) {
        this.opcion = opcion;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getBeca() {
        return beca;
    }

    public void setBeca(String beca) {
        this.beca = beca;
    }

    public String getTipoBeca() {
        return tipoBeca;
    }

    public void setTipoBeca(String tipoBeca) {
        this.tipoBeca = tipoBeca;
    }

    public String getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(String movimiento) {
        this.movimiento = movimiento;
    }

    public String getProceso() {
        return proceso;
    }

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    public String getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(String alumnos) {
        this.alumnos = alumnos;
    }

    public String getAlumnosL() {
        return alumnosL;
    }

    public void setAlumnosL(String alumnosL) {
        this.alumnosL = alumnosL;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getAtentamente() {
        return atentamente;
    }

    public void setAtentamente(String atentamente) {
        this.atentamente = atentamente;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public BigDecimal getAlumnosRegistrados() {
        return alumnosRegistrados;
    }

    public void setAlumnosRegistrados(BigDecimal alumnosRegistrados) {
        this.alumnosRegistrados = alumnosRegistrados;
    }

    public BigDecimal getRevalidantesNoRegistrados() {
        return revalidantesNoRegistrados;
    }

    public void setRevalidantesNoRegistrados(BigDecimal revalidantesNoRegistrados) {
        this.revalidantesNoRegistrados = revalidantesNoRegistrados;
    }

    public BigDecimal getAlumnossinESE() {
        return alumnossinESE;
    }

    public void setAlumnossinESE(BigDecimal alumnossinESE) {
        this.alumnossinESE = alumnossinESE;
    }

    public BigDecimal getAlumnosDatosIncorrectos() {
        return alumnosDatosIncorrectos;
    }

    public void setAlumnosDatosIncorrectos(BigDecimal alumnosDatosIncorrectos) {
        this.alumnosDatosIncorrectos = alumnosDatosIncorrectos;
    }

    public BigDecimal getOtorgamientosTarjetaAlumno() {
        return otorgamientosTarjetaAlumno;
    }

    public void setOtorgamientosTarjetaAlumno(BigDecimal otorgamientosTarjetaAlumno) {
        this.otorgamientosTarjetaAlumno = otorgamientosTarjetaAlumno;
    }

    public File getUpload() {
        return upload;
    }

    public void setUpload(File upload) {
        this.upload = upload;
    }

    public String getUploadFileName() {
        return uploadFileName;
    }

    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

    public String getUploadContentType() {
        return uploadContentType;
    }

    public void setUploadContentType(String uploadContentType) {
        this.uploadContentType = uploadContentType;
    }

    public List<String> getLog() {
        return log;
    }

    public void setLog(List<String> log) {
        this.log = log;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public List<Alumno> getAlumnosLst() {
        return alumnosLst;
    }

    public void setAlumnosLst(List<Alumno> alumnosLst) {
        
        this.alumnosLst = alumnosLst;
    }

    public Boolean getCarga() {
        return carga;
    }

    public void setCarga(Boolean carga) {
        this.carga = carga;
    }

    public BigDecimal getPeriodo() {
        return periodo;
    }

    public void setPeriodo(BigDecimal periodo) {
        this.periodo = periodo;
    }

    public BigDecimal getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(BigDecimal usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getContentDisposition() {
        return contentDisposition;
    }

    public void setContentDisposition(String contentDisposition) {
        this.contentDisposition = contentDisposition;
    }

    public InputStream getExcelStream() {
        return excelStream;
    }

    public void setExcelStream(InputStream excelStream) {
        this.excelStream = excelStream;
    }

    public List<Alumno> getAlumnosError() {
        return alumnosError;
    }

    public void setAlumnosError(List<Alumno> alumnosError) {
        this.alumnosError = alumnosError;        
    }

    public int getTotalSent() {
        return totalSent;
    }

    public void setTotalSent(int totalSent) {
        this.totalSent = totalSent;
    }

    public int getTotalFailed() {
        return totalFailed;
    }

    public void setTotalFailed(int totalFailed) {
        this.totalFailed = totalFailed;
    }

    public List<List<String>> getBoletasError() {
        return boletasError;
    }

    public void setBoletasError(List<List<String>> boletasError) {
        this.boletasError = boletasError;
        if (!boletasError.isEmpty()) {
            setHayBoletasErroneas(true);
        }
    }
    
    

    public boolean isHayBoletasErroneas() {
        return hayBoletasErroneas;
    }

    public void setHayBoletasErroneas(boolean hayBoletasErroneas) {
        this.hayBoletasErroneas = hayBoletasErroneas;
    }

    public void setNombreExcel(String nombreExcel) {
        this.nombreExcel = nombreExcel;
    }

    public String getNombreExcel() {
        return nombreExcel;
    }
    
    
}
