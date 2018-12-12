/**
 * SISTEMA INFORMÁTICO DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE
 * SERVICIOS ESTUDIANTILES 2015
 *
 */
package com.becasipn.actions.alumno;

import com.becasipn.actions.BaseUploadFileAction;
import com.becasipn.business.AlumnoBO;
import com.becasipn.exception.ErrorDaeException;
import com.becasipn.exception.LoginException;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.EstadoCivil;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.Usuario;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Patricia Benitez
 */
public class AdministraAlumnoAction extends BaseUploadFileAction implements ModelDriven<Alumno>, ServletRequestAware, ServletResponseAware, MensajesAlumno {

    public static final String REGISTRO = "registro";
    public static final String EDICION = "edicion";
    public static final String VER = "ver";
    public static final String INPUTUNIVERSAL = "inputUniversal";
    private Alumno alumno = new Alumno();
    private HttpServletRequest request;
    private HttpServletResponse response;
    private String numeroDeBoleta;
    private Boolean activoDatosDAE;
    private Boolean contestoESE;
    private Integer noInscrito = 0;
    private Boolean datosAcademicos = Boolean.TRUE;
    private Boolean guardar = Boolean.FALSE;
    private Boolean alumnoSUBES = Boolean.FALSE;
    private String periodo;
    private String identificador;
    private String identff;
    private String nivel;
    private String ua;
    private Integer tipoAsignacion;
    private String mensajeEstatusTarjeta = null;
    private InputStream excelStream;
    private String contentDisposition;

    @Override
    public Alumno getModel() {
        return alumno;
    }

    /**
     * Formulario de Ingreso
     *
     * @return JSP que solicita la boleta del alumno
     */
    public String inicio() {
        return INPUT;
    }

//    public String guarda() {
//        //Obtiene el valor de la variable EdicionDatosPersonales
//        String edicionDatosPersonales = (String) ActionContext.getContext().getApplication().get("EdicionDatosPersonales");
//        AlumnoBO bo = new AlumnoBO(getDaos());
//        Usuario usuarioNuevo;
//        if (alumno == null) {
//            addActionError(getText("registro.guarda.sin.datos"));
//            return INPUT;
//        }
//        Boolean nuevoUsuario = Boolean.FALSE;
//        EstadoCivil ec = getDaos().getEstadoCivilDao().findById(alumno.getEstadoCivil().getId());
//        alumno.setEstadoCivil(ec);
//        usuarioNuevo = bo.creaUsuario(alumno);
//        if (usuarioNuevo != null) {
//            if (alumno.getDatosAcademicos().getInscrito() == null || alumno.getDatosAcademicos().getInscrito() == 0) {
//                noInscrito = 1;
//            }
//            nuevoUsuario = Boolean.TRUE;
//        }
//        //Se obtienes la fecha de nacimiento, la entidad de nacimiento y el genero del CURP
//        try {
//            bo.obtenerDatosCURP(alumno);
//        } catch (ErrorDaeException ex) {
//            addActionError(ex.getMessage());
//        }
//        alumno.setBeneficiarioOportunidades(null);
//        if (nuevoUsuario) {
//            try {
//                activoDatosDAE = Boolean.TRUE;
//                Alumno alumnoDae = bo.datosDAE(alumno);
//                if (alumnoDae == null) {
//                    addActionError(getText("registro.alumno.no.dae"));
//                    return EDICION;
//                } else {
//                    alumno = alumnoDae;
//                }
//            } catch (ErrorDaeException ex) {
//                addActionError(getText(ex.getMessage()));
//                Logger.getLogger(AdministraAlumnoAction.class.getName()).log(Level.SEVERE, null, ex);
//                return EDICION;
//            }
//            alumno = bo.guarda(alumno);
//            alumno = getDaos().getAlumnoDao().findById(alumno.getId());
//            DatosAcademicos da = getDaos().getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), getDaos().getPeriodoDao().getPeriodoActivo().getId());
//            da = da == null ? new DatosAcademicos() : da;
//            alumno.setDatosAcademicos(da);
//            addActionMessage(getText("registro.exitoso"));
//            return SUCCESS;
//        } else {
//            if (edicionDatosPersonales.equals("true")) {
//                alumno = bo.guarda(alumno);
//                if (alumno.getId() == null) {
//                    //Enviar mensaje de error deiciendo que ya esta registrado cachando el null del metodo anterior
//                    addActionError(getText("registro.boleta.existente"));
//                }
//                alumno = getDaos().getAlumnoDao().findById(alumno.getId());
//                addActionMessage(getText("misdatos.alumno.actualizado"));
//            } else {
//                addActionError(getText("registro.error"));
//            }
//        }
//        try {
//            setMenu((Usuario) ActionContext.getContext().getSession().get("usuario"));
//        } catch (LoginException ex) {
//            Logger.getLogger(AdministraAlumnoAction.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        guardar = Boolean.TRUE;
//        return datos();
//    }

    /**
     * El alumno puede ver sus datos
     *
     * @return
     */

//    public String datos() {
//        //Obtiene el valor de la variable EdicionDatosPersonales
//        String edicionDatosPersonales = (String) ActionContext.getContext().getApplication().get("EdicionDatosPersonales");
//        numeroDeBoleta = SecurityContextHolder.getContext().getAuthentication().getName();
//        AlumnoBO bo = new AlumnoBO(getDaos());
//        List<Alumno> listAlumno = getDaos().getAlumnoDao().findBy("boleta", numeroDeBoleta, Boolean.TRUE);
//        if (listAlumno == null || listAlumno.isEmpty()) {
//            addActionError(getText("registro.boleta.no.encontrada"));
//            return ERROR;
//        }
//        alumno = listAlumno.get(0);
//
//        Periodo p = getDaos().getPeriodoDao().getPeriodoActivo();
//        if (alumno != null) {
//            if (alumno.getNombre() != null) {
//                alumno.setNombre(alumno.getNombre().toUpperCase());
//            }
//            if (alumno.getApellidoPaterno() != null) {
//                alumno.setApellidoPaterno(alumno.getApellidoPaterno().toUpperCase());
//            }
//            if (alumno.getApellidoMaterno() != null) {
//                alumno.setApellidoMaterno(alumno.getApellidoMaterno().toUpperCase());
//            }
//            if (alumno.getCurp() != null) {
//                alumno.setCurp(alumno.getCurp().toUpperCase());
//            }
//            if (alumno.getDireccion() != null) {
//                if (alumno.getDireccion().getCalle() != null) {
//                    alumno.getDireccion().setCalle(alumno.getDireccion().getCalle().toUpperCase());
//                }
//                if (alumno.getDireccion().getNumeroExterior() != null) {
//                    alumno.getDireccion().setNumeroExterior(alumno.getDireccion().getNumeroExterior().toUpperCase());
//                }
//                if (alumno.getDireccion().getNumeroInterior() != null) {
//                    alumno.getDireccion().setNumeroInterior(alumno.getDireccion().getNumeroInterior().toUpperCase());
//                }
//            }
//        }
//        if (edicionDatosPersonales.equals("true")) {
//            try {
//                setMenu(alumno.getUsuario());
//            } catch (Exception e) {
//                LOG.warn("AdministraAlumnoAction::datos()::" + e.getMessage());
//            }
//            if (alumno.getDatosAcademicos().getInscrito() == null
//                    || alumno.getDatosAcademicos().getUnidadAcademica().getId() == null
//                    || alumno.getDatosAcademicos().getSemestre() == null
//                    || alumno.getDatosAcademicos().getPromedio() == null
//                    || alumno.getDatosAcademicos().getCarrera() == null) {
//                datosAcademicos = Boolean.FALSE;
//            } else {
//                if (guardar && alumno.getDatosAcademicos().getInscrito() == 0) {
//                    noInscrito = 1;
//                }
//            }
//
//            return EDICION;
//        } else {
//            return VER;
//        }
//    }

    public String listado() {
        periodo = getDaos().getPeriodoDao().getPeriodoActivo().getId().toString();
        return SUCCESS;
    }

    
    public String objectCivil(Object o) {
        String aux;
        BigDecimal b = (BigDecimal) o;
        if (b != null) {
            switch (b.intValue()) {
                case 1:
                    aux = "1";
                    break;
                case 2:
                    aux = "2";
                    break;
                case 3:
                    aux = "4";
                    break;
                case 4:
                    aux = "3";
                    break;
                case 5:
                    aux = "7";
                    break;
                default:
                    aux = "1";
                    break;
            }
        } else {
            aux = "1";
        }
        return aux;
    }

    public String objectGenero(Object o) {
        String aux;
        BigDecimal b = (BigDecimal) o;
        if (b != null) {
            switch (b.intValue()) {
                case 1:
                    aux = "2";
                    break;
                case 2:
                    aux = "1";
                    break;
                default:
                    aux = "1";
                    break;
            }
        } else {
            aux = "1";
        }
        return aux;
    }

    public String fechaN(Object o) {
        String aux;
        SimpleDateFormat formater = new SimpleDateFormat("yyyy/MM/dd");
        Date d = (Date) o;
        if (d != null) {
            aux = formater.format(d);
        } else {
            aux = "-";
        }
        return aux;
    }

    @Override
    public void setServletRequest(HttpServletRequest hsr) {
        this.request = hsr;
    }

    @Override
    public void setServletResponse(HttpServletResponse hsr) {
        this.response = hsr;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public String getNumeroDeBoleta() {
        return numeroDeBoleta;
    }

    public void setNumeroDeBoleta(String numeroDeBoleta) {
        this.numeroDeBoleta = numeroDeBoleta;
    }

    public Boolean getActivoDatosDAE() {
        return activoDatosDAE;
    }

    public void setActivoDatosDAE(Boolean activoDatosDAE) {
        this.activoDatosDAE = activoDatosDAE;
    }
    
    public void setContestoESE(Boolean contestoESE) {
        this.contestoESE = contestoESE;
    }

    public Integer getNoInscrito() {
        return noInscrito;
    }

    public void setNoInscrito(Integer noInscrito) {
        this.noInscrito = noInscrito;
    }

    public Boolean getDatosAcademicos() {
        return datosAcademicos;
    }

    public void setDatosAcademicos(Boolean datosAcademicos) {
        this.datosAcademicos = datosAcademicos;
    }

    public Boolean getGuardar() {
        return guardar;
    }

    public void setGuardar(Boolean guardar) {
        this.guardar = guardar;
    }

    public Boolean getAlumnoSUBES() {
        return alumnoSUBES;
    }

    public void setAlumnoSUBES(Boolean alumnoSUBES) {
        this.alumnoSUBES = alumnoSUBES;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
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

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }

    public Integer getTipoAsignacion() {
        return tipoAsignacion;
    }

    public void setTipoAsignacion(Integer tipoAsignacion) {
        this.tipoAsignacion = tipoAsignacion;
    }

    public String getMensajeEstatusTarjeta() {
        return mensajeEstatusTarjeta;
    }

    public void setMensajeEstatusTarjeta(String mensajeEstatusTarjeta) {
        this.mensajeEstatusTarjeta = mensajeEstatusTarjeta;
    }

    public String getIdentff() {
        return identff;
    }

    public void setIdentff(String identff) {
        this.identff = identff;
    }
}