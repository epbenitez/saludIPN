/**
 * SISTEMA INFORMÁTICO DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE
 * SERVICIOS ESTUDIANTILES 2015
 *
 */
package com.becasipn.actions.alumno;

import com.becasipn.actions.BaseUploadFileAction;
import com.becasipn.business.AlumnoBO;
import com.becasipn.business.AlumnoDatosBancariosBO;
import com.becasipn.business.AlumnoTarjetaBancariaBO;
import com.becasipn.business.SolicitudCuentasBO;
import com.becasipn.exception.ErrorDaeException;
import com.becasipn.exception.LoginException;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.AlumnoDatosBancarios;
import com.becasipn.persistence.model.AlumnoTarjetaBancaria;
import com.becasipn.persistence.model.BecaUniversal;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.Direccion;
import com.becasipn.persistence.model.EntidadFederativa;
import com.becasipn.persistence.model.EstadoCivil;
import com.becasipn.persistence.model.EstatusTarjetaBancaria;
import com.becasipn.persistence.model.Otorgamiento;
import com.becasipn.persistence.model.PadronSubes;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.RelacionGeografica;
import com.becasipn.persistence.model.SolicitudCuentas;
import com.becasipn.persistence.model.TarjetaBancaria;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.util.TuplaValidacion;
import com.becasipn.util.ErrorDAE;
import com.becasipn.util.ExcelExport;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.security.core.context.SecurityContextHolder;
import static com.becasipn.util.StringUtil.formatoCorrectoCurp;

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

    /**
     * Formulario de Ingreso de beca universal
     *
     * @return JSP que solicita la boleta del alumno
     */
    public String inicioBecaUniversal() {
        return INPUTUNIVERSAL;
    }

    /**
     * Formulario de Ingreso de beca universal
     *
     * @return JSP que solicita la boleta del alumno
     */
    public String guardarBecaUniversal() {
        //Se busca al alumno en el padrón de beca universal.
        BecaUniversal alumnoBecaUniversal = getDaos().getBecaUniversalDao().getByBoleta(numeroDeBoleta);
        //Se valida que el usuario haya escrito un número de boleta
        if (numeroDeBoleta == null || numeroDeBoleta.isEmpty()) {
            addActionError(getText("registro.boleta"));
        } //Validar que el número de boleta existe en padrón beca universal.
        else if (alumnoBecaUniversal!=null ) {
            // Si el alumno ya existe en la tabla de alumno, le indicamos que tiene que ingresar con su usuario y contraseña.
            AlumnoBO bo = new AlumnoBO(getDaos());
            alumno = bo.getAlumno(numeroDeBoleta);
            if (alumno == null) {
                //Con los datos de la DAE crear alumno, datos academicos y usuario.
                try {
                    //Obtenemos los datos de la DAE.
                    TuplaValidacion tupla = bo.validarAlumno(numeroDeBoleta, true);
                    if (tupla.getErrorDAE() == null) {
                        //Validar carrera
                        ErrorDAE validarCarreraError = bo.validarCarrera(tupla.getAlumnoDAE());
                        boolean tieneCorreo = true;
                        if (validarCarreraError == null) {
                            //Obtiene los datos de la DAE
                            alumno = bo.daeJsonToEntAlumno(tupla.getAlumnoDAE());
                            //Se valida el formato del CURP.
                            if (alumno.getCurp() != null && formatoCorrectoCurp(alumno.getCurp())) {
                                if (alumno.getEstadoCivil() == null) {
                                    EstadoCivil estadoCivil = getDaos().getEstadoCivilDao().findById(new BigDecimal(1));
                                    alumno.setEstadoCivil(estadoCivil);
                                }
                                if (alumno.getEntidadDeNacimiento() == null) {
                                    EntidadFederativa entidadFederativa = getDaos().getEntidadFederativaDao().findById(new BigDecimal(9));
                                    alumno.setEntidadDeNacimiento(entidadFederativa);
                                }
                                if (alumno.getCorreoElectronico() == null) {
                                    alumno.setCorreoElectronico("sincorreo@ipn.mx");
                                    tieneCorreo = false;
                                }
                                //Dirección
                                if (alumno.getDireccion() == null) {
                                    Direccion direccion = new Direccion();
                                    direccion.setCalle("MIGUEL OTHON DE MENDIZABAL");
                                    direccion.setCodigoPostal("07320");
                                    RelacionGeografica relacionGeografica = getDaos().getRelacionGeograficaDao().findById(new BigDecimal(912));
                                    direccion.setRelacionGeografica(relacionGeografica);
                                    alumno.setDireccion(direccion);
                                } else {
                                    if (alumno.getDireccion().getCalle() == null) {
                                        alumno.getDireccion().setCalle("MIGUEL OTHON DE MENDIZABAL");
                                    }
                                    if (alumno.getDireccion().getCodigoPostal() == null) {
                                        alumno.getDireccion().setCodigoPostal("07320");
                                    }
                                    RelacionGeografica relacionGeografica = getDaos().getRelacionGeograficaDao().findById(new BigDecimal(912));
                                    alumno.getDireccion().setRelacionGeografica(relacionGeografica);
                                }
                                //Creamos el usuario.
                                bo.creaUsuario(alumno);
                                alumno = bo.guarda(alumno);
                                if (alumno.getDatosAcademicos().getId() == null) {
                                    getDaos().getDatosAcademicosDao().save(alumno.getDatosAcademicos());
                                }
                                if (tieneCorreo) {
                                    //Mandar datos al de inicio de sesión al correo
                                    String[] datos = new String[1];
                                    datos[0] = alumno.getCorreoElectronico();
                                    addActionMessage(getText("registro.exitoso.beca.universal", datos));
                                    mandarCorreo();
                                } else {
                                    addActionError(getText("No tienes correo electronico registrado en la DAE. Necesitas acudir con tu responsable de becas."));
                                }
                            } else {
                                addActionError(getText("El CURP que nos ha sido proporcionado por la  Dirección de Administración Escolar no parece ser válido. Por favor, acude con el Jefe de Control Escolar de tu Unidad Académica para solventar ésta situación. Una vez corregido, intenta de nuevo tu registro en SIBec."));
                            }
                        } else if (validarCarreraError != null) {
                            addActionError(getText(validarCarreraError.getMsg()));
                            //return INPUTUNIVERSAL;
                        }
                    } else if (tupla.getErrorDAE() != null) {
                        if (tupla.getErrorDAE().equals(ErrorDAE.NO_INSCRITO)) {
                        } else {
                            addActionError(getText(tupla.getErrorDAE().getMsg()));
                            //return INPUTUNIVERSAL;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    addActionError(getText("registro.error"));
                    //return INPUTUNIVERSAL;
                }
            } else {
                //Mandar datos al de inicio de sesión al correo
                String[] datos = new String[1];
                datos[0] = alumno.getCorreoElectronico();
                addActionError(getText("registro.boleta.existente.tabla.alumno", datos));
                mandarCorreo();
            }
        } else {
            addActionError(getText("El número de boleta ingresado no es candidato a beca universal, realiza tu registro normal."));
        }
        return INPUTUNIVERSAL;
    }

    public void mandarCorreo(Alumno a, String msgPropKey) {
        alumno = a;
    }

    private void mandarCorreo() {
        //Variables necesarias para el envío de correos electronicos.
        HttpServletRequest req = (HttpServletRequest) request;
        int port = req == null ? 8080 : req.getServerPort();
        String host = (port == 443 ? "https://" : "http://") + request.getServerName() + (port != 80 && port != 443 ? ":" + port : "");
        String subject = "Sistema Informático de Becas IPN";
        StringBuilder body = new StringBuilder("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html> <head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /> <title>Sistema Inform&aacute;tico de Becas</title> </head> <body leftmargin=\"0\" marginwidth=\"0\" topmargin=\"0\" marginheight=\"0\" offset=\"0\"> <center> <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" height=\"100%\" width=\"100%\" id=\"backgroundTable\"> <tr> <td align=\"center\" valign=\"top\"> <!-- // Begin Template Preheader \\\\ --> <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"600\" id=\"templatePreheader\"> <tr> <td valign=\"top\" class=\"preheaderContent\"> <!-- // Begin Module: Standard Preheader \\ --> <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"100%\"> <tr> <td valign=\"top\"> <div mc:edit=\"std_preheader_content\"> <!--Recuperaci&oacute;n de Contraseña--> </div> </td> <!-- *|IFNOT:ARCHIVE_PAGE|* --> <td valign=\"top\" width=\"170\"> <div mc:edit=\"std_preheader_links\"> </div> </td> <!-- *|END:IF|* --> </tr> </table> <!-- // End Module: Standard Preheader \\ --> </td> </tr> </table> <!-- // End Template Preheader \\\\ --> <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" id=\"templateContainer\"> <tr> <td align=\"center\" valign=\"top\"> <!-- // Begin Template Header \\\\ --> <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" style=\"background-color:#FFFFFF;border-bottom:5px solid #505050;\"> <tr> <td class=\"headerContent\" width=\"100%\" style=\"padding-left:20px; padding-right:10px;\"> <div mc:edit=\"Header_content\"> <h1></h1> </div> </td> <td class=\"headerContent\"> <img src=\"https://www.sibec.ipn.mx/resources/img/login/logo-sibec.svg\" style=\"max-width:180px;\" id=\"headerImage campaign-icon\" mc:label=\"header_image\" mc:edit=\"header_image\" mc:allowtext /> </td> </tr> </table> <!-- // End Template Header \\\\ --> </td> </tr> <tr> <td align=\"center\" valign=\"top\"> <!-- // Begin Template Body \\\\ --> <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"600\" id=\"templateBody\"> <tr> <td valign=\"top\" class=\"bodyContent\"> <!-- // Begin Module: Standard Content \\\\ --> <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"100%\"> <tr> <td valign=\"top\" style=\"padding-right:0;\"> <div mc:edit=\"std_content00\">");
        body.append(" <h2 class=\"h2\">Envío de usuario y contraseña</h2>");
        body.append(" <h3 class=\"h3\">Estimado(a), ").append(alumno.getFullName()).append("</h3> <p>Tus datos de acceso son los siguientes: </p> ");
        body.append(" <p><b>Usuario: </b> ").append(alumno.getUsuario().getUsuario()).append("<br> <b>Contraseña: </b> ").append(alumno.getUsuario().getPassword()).append("</p><a href=\"").append(host).append("\">").append(host).append("</a>");
        body.append(" <p>Atentamente<br> Direcci&oacute;n de Servicios Estudiantiles del Instituto Polit&eacute;cnico Nacional </p> </div> </td> </tr> </table> <!-- // End Module: Standard Content \\\\ --> </td> <!-- // Begin Sidebar \\\\ --> <td valign=\"top\" width=\"180\" id=\"templateSidebar\"> <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"> <tr> <td valign=\"top\"> <!-- // Begin Module: Standard Content \\\\ --> <table border=\"0\" cellpadding=\"20\" cellspacing=\"0\" width=\"100%\" class=\"sidebarContent\"> <tr> <td valign=\"top\" style=\"padding-right:10px;\"> <div mc:edit=\"std_content01\"> <!-- <strong>Basic content module</strong> <br /> Far far away, behind the word mountains. <br /> <br /> <strong>Far from the countries</strong> <br /> Vokalia and Consonantia, there live the blind texts.--> </div> </td> </tr> </table> <!-- // End Module: Standard Content \\\\ --> </td> </tr> </table> </td> <!-- // End Sidebar \\\\ --> </tr> </table> <!-- // End Template Body \\\\ --> </td> </tr> <tr> <td align=\"center\" valign=\"top\"> <!-- // Begin Template Footer \\\\ --> <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" id=\"templateFooter\"> <tr> <td valign=\"top\" class=\"footerContent\"> <!-- // Begin Module: Standard Footer \\\\ --> <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"100%\"> <!-- <tr> <td colspan=\"2\" valign=\"middle\" id=\"social\"> <div mc:edit=\"std_social\"> &nbsp;<a href=\"*|TWITTER:PROFILEURL|*\">follow on Twitter</a> | <a href=\"*|FACEBOOK:PROFILEURL|*\">friend on Facebook</a> | <a href=\"*|FORWARD|*\">forward to a friend</a>&nbsp; </div> </td> </tr>--> <tr> <td valign=\"top\" width=\"350\"> <div mc:edit=\"std_footer\"> <em>Copyright &copy; 2016 Instituto Politécnico Nacional, todos los derechos reservados.</em> <p>Unidad Profesional \"Adolfo López Mateos\", Av. Miguel Bernard Esq. Miguel Othón de Mendizabal s/n Col. <br> Residencial La Escalera, Edif. Secretaría Gestión Estratégica 2° Piso, Ciudad de México, C.P. 07738 <br> Tel. 57296000, Ext. 51844</p> </div> </td> <td valign=\"top\" width=\"190\" id=\"monkeyRewards\"> <div mc:edit=\"monkeyrewards\"> &nbsp; </div> </td> </tr> <tr> <td colspan=\"2\" valign=\"middle\" id=\"utility\"> <div mc:edit=\"std_utility\"> &nbsp; </div> </td> </tr> </table> <!-- // End Module: Standard Footer \\\\ --> </td> </tr> </table> <!-- // End Template Footer \\\\ --> </td> </tr> </table> <br /> </td> </tr> </table> </center> </body></html>");
        try {
            sendEmail(alumno.getCorreoElectronico(), subject, body.toString());
        } catch (MessagingException ex) {
            Logger.getLogger(AdministraAlumnoAction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String acceso() throws ParseException {
        try {
            //Se valida que el usuario haya escrito un número de boleta
            if (numeroDeBoleta == null || numeroDeBoleta.isEmpty()) {
                addActionError(getText("registro.boleta"));
                return INPUT;
            }
            // Si el alumno ya existe en la tabla de alumno, le indicamos que tiene que ingresar con su usuario y contraseña.
            AlumnoBO bo = new AlumnoBO(getDaos());
            alumno = bo.getAlumno(numeroDeBoleta);
            if (alumno != null) {
                String[] datos = new String[1];
                datos[0] = alumno.getCorreoElectronico();
                addActionError(getText("registro.boleta.existente.tabla.alumno", datos));
                mandarCorreo();
                return INPUT;
            }
            //Se validan datos con la DAE
            String validarDatosDAE = (String) ActionContext.getContext().getApplication().get("ValidarDatosDae");
            if (validarDatosDAE.equals("true")) {
                activoDatosDAE = Boolean.TRUE;
                TuplaValidacion tupla = bo.validarAlumno(numeroDeBoleta, true);
                if (tupla.getErrorDAE() != null) {
                    if (tupla.getErrorDAE().equals(ErrorDAE.NO_INSCRITO)) {
                    } else {
                        addActionError(getText(tupla.getErrorDAE().getMsg()));
                        return INPUT;
                    }
                }
                //Validar carrera
                ErrorDAE validarCarreraError = bo.validarCarrera(tupla.getAlumnoDAE());
                if (validarCarreraError != null) {
                    addActionError(getText(validarCarreraError.getMsg()));
                    return INPUT;
                }
                //Obtiene los datos de la DAE
                alumno = bo.daeJsonToEntAlumno(tupla.getAlumnoDAE());
                //Validamos si el alumno existe en ent_padron_subes.
                PadronSubes padronSubes = getDaos().getPadronSubesDao().getAlumnoSUBES(alumno.getId());
                if (padronSubes != null) {
                    if (padronSubes.getMatricula().equals(alumno.getBoleta())) {
                        alumnoSUBES = Boolean.TRUE;
                        if (padronSubes.getCurp().equals(alumno.getCurp())) {
                        } else {
                            alumno.setCurp(padronSubes.getCurp());
                        }
                    }
                }
                //Se obtienes la fecha de nacimiento, la entidad de nacimiento y el genero del CURP
                if (alumno.getCurp() != null && alumno.getCurp().length() == 18) {
                    try {
                        bo.obtenerDatosCURP(alumno);
                    } catch (ErrorDaeException ex) {
                        addActionError(ex.getMessage());
                    }
                }
            } else {
                activoDatosDAE = Boolean.FALSE;
                alumno = new Alumno();
                alumno.setBoleta(numeroDeBoleta);
            }
        } catch (Exception e) {
            e.printStackTrace();
            addActionError(getText("registro.error"));
            return INPUT;
        }
        return REGISTRO;
    }

    public String guarda() {
        //Obtiene el valor de la variable EdicionDatosPersonales
        String edicionDatosPersonales = (String) ActionContext.getContext().getApplication().get("EdicionDatosPersonales");
        AlumnoBO bo = new AlumnoBO(getDaos());
        Usuario usuarioNuevo;
        if (alumno == null) {
            addActionError(getText("registro.guarda.sin.datos"));
            return INPUT;
        }
        Boolean nuevoUsuario = Boolean.FALSE;
        EstadoCivil ec = getDaos().getEstadoCivilDao().findById(alumno.getEstadoCivil().getId());
        alumno.setEstadoCivil(ec);
        usuarioNuevo = bo.creaUsuario(alumno);
        if (usuarioNuevo != null) {
            if (alumno.getDatosAcademicos().getInscrito() == null || alumno.getDatosAcademicos().getInscrito() == 0) {
                noInscrito = 1;
            }
            nuevoUsuario = Boolean.TRUE;
        }
        //Se obtienes la fecha de nacimiento, la entidad de nacimiento y el genero del CURP
        try {
            bo.obtenerDatosCURP(alumno);
        } catch (ErrorDaeException ex) {
            addActionError(ex.getMessage());
        }
        alumno.setBeneficiarioOportunidades(null);
        if (nuevoUsuario) {
            try {
                activoDatosDAE = Boolean.TRUE;
                Alumno alumnoDae = bo.datosDAE(alumno);
                if (alumnoDae == null) {
                    addActionError(getText("registro.alumno.no.dae"));
                    return EDICION;
                } else {
                    alumno = alumnoDae;
                }
            } catch (ErrorDaeException ex) {
                addActionError(getText(ex.getMessage()));
                Logger.getLogger(AdministraAlumnoAction.class.getName()).log(Level.SEVERE, null, ex);
                return EDICION;
            }
            alumno = bo.guarda(alumno);
            alumno = getDaos().getAlumnoDao().findById(alumno.getId());
            DatosAcademicos da = getDaos().getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), getDaos().getPeriodoDao().getPeriodoActivo().getId());
            da = da == null ? new DatosAcademicos() : da;
            alumno.setDatosAcademicos(da);
            addActionMessage(getText("registro.exitoso"));
            mandarCorreo();
            return SUCCESS;
        } else {
            if (edicionDatosPersonales.equals("true")) {
                alumno = bo.guarda(alumno);
                if (alumno.getId() == null) {
                    //Enviar mensaje de error deiciendo que ya esta registrado cachando el null del metodo anterior
                    addActionError(getText("registro.boleta.existente"));
                }
                alumno = getDaos().getAlumnoDao().findById(alumno.getId());
                addActionMessage(getText("misdatos.alumno.actualizado"));
            } else {
                addActionError(getText("registro.error"));
            }
        }
        try {
            setMenu((Usuario) ActionContext.getContext().getSession().get("usuario"));
        } catch (LoginException ex) {
            Logger.getLogger(AdministraAlumnoAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        guardar = Boolean.TRUE;
        return datos();
    }

    /**
     * El alumno puede ver sus datos
     *
     * @return
     */
    public String datos() {
        //Obtiene el valor de la variable EdicionDatosPersonales
        String edicionDatosPersonales = (String) ActionContext.getContext().getApplication().get("EdicionDatosPersonales");
        numeroDeBoleta = SecurityContextHolder.getContext().getAuthentication().getName();
        AlumnoBO bo = new AlumnoBO(getDaos());
        AlumnoDatosBancariosBO abo = new AlumnoDatosBancariosBO(getDaos());
        List<Alumno> listAlumno = getDaos().getAlumnoDao().findBy("boleta", numeroDeBoleta, Boolean.TRUE);
        if (listAlumno == null || listAlumno.isEmpty()) {
            addActionError(getText("registro.boleta.no.encontrada"));
            return ERROR;
        }
        alumno = listAlumno.get(0);

        Periodo p = getDaos().getPeriodoDao().getPeriodoActivo();
        DatosAcademicos da = getDaos().getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), p.getId());
        da = da == null ? new DatosAcademicos() : da;
        alumno.setDatosAcademicos(da);

        Otorgamiento otorgamientoAnterior = getDaos().getOtorgamientoDao().getOtorgamientoAlumno(alumno.getId(),
                p.getPeriodoAnterior().getId());
        Boolean oAnt = otorgamientoAnterior != null;
        if (oAnt) {
            alumno.getDatosAcademicos().setSemestre(otorgamientoAnterior.getDatosAcademicos().getSemestre() + 1);
        }
        if (alumno != null) {
            if (alumno.getNombre() != null) {
                alumno.setNombre(alumno.getNombre().toUpperCase());
            }
            if (alumno.getApellidoPaterno() != null) {
                alumno.setApellidoPaterno(alumno.getApellidoPaterno().toUpperCase());
            }
            if (alumno.getApellidoMaterno() != null) {
                alumno.setApellidoMaterno(alumno.getApellidoMaterno().toUpperCase());
            }
            if (alumno.getCurp() != null) {
                alumno.setCurp(alumno.getCurp().toUpperCase());
            }
            if (alumno.getDireccion() != null) {
                if (alumno.getDireccion().getCalle() != null) {
                    alumno.getDireccion().setCalle(alumno.getDireccion().getCalle().toUpperCase());
                }
                if (alumno.getDireccion().getNumeroExterior() != null) {
                    alumno.getDireccion().setNumeroExterior(alumno.getDireccion().getNumeroExterior().toUpperCase());
                }
                if (alumno.getDireccion().getNumeroInterior() != null) {
                    alumno.getDireccion().setNumeroInterior(alumno.getDireccion().getNumeroInterior().toUpperCase());
                }
            }
        }
        if (edicionDatosPersonales.equals("true")) {
            try {
                setMenu(alumno.getUsuario());
            } catch (Exception e) {
                LOG.warn("AdministraAlumnoAction::datos()::" + e.getMessage());
            }
            if (alumno.getDatosAcademicos().getInscrito() == null
                    || alumno.getDatosAcademicos().getUnidadAcademica().getId() == null
                    || alumno.getDatosAcademicos().getSemestre() == null
                    || alumno.getDatosAcademicos().getPromedio() == null
                    || alumno.getDatosAcademicos().getCarrera() == null) {
                datosAcademicos = Boolean.FALSE;
            } else {
                if (guardar && alumno.getDatosAcademicos().getInscrito() == 0) {
                    noInscrito = 1;
                }
            }
            //Validamos si el alumno existe en ent_padron_subes.
            PadronSubes padronSubes = getDaos().getPadronSubesDao().getAlumnoSUBES(alumno.getId());
            if (padronSubes != null) {
                if (padronSubes.getMatricula().equals(alumno.getBoleta())) {
                    alumnoSUBES = Boolean.TRUE;
                    if (padronSubes.getCurp().equals(alumno.getCurp())) {
                    } else {
                        alumno.setCurp(padronSubes.getCurp());
                    }
                }
            }
            //Se obtienes la fecha de nacimiento, la entidad de nacimiento y el genero del CURP
            try {
                bo.obtenerDatosCURP(alumno);
            } catch (ErrorDaeException ex) {
                addActionError(ex.getMessage());
            }
            return EDICION;
        } else {
            return VER;
        }
    }

    public String listado() {
        periodo = getDaos().getPeriodoDao().getPeriodoActivo().getId().toString();
        return SUCCESS;
    }
    //Este metodo esta en desuso por lo tanto no se le hizo las actualizaciones al reporte de excel
    public String descargarListadoIdnt() {
        List<Object[]> ot = getDaos().getOtorgamientoDao().getSolicitudes(identificador);
        List<Object[]> otX = new ArrayList<>();

        for (Object[] o : ot) {
            Object[] ax = new Object[30];
            ax[0] = o[0] == null ? "-" : o[0];//ID Nombre Escuela
            ax[1] = o[1];//ID Becario
            ax[2] = o[2] == null ? o[19] : o[2];//Nombre
            ax[3] = o[2] == null ? o[20] : o[3];//AP Paterno
            ax[4] = o[2] == null ? o[21] : o[4];//AP Materno
            ax[5] = o[2] == null ? fechaN(o[22]) : fechaN(o[5]);//Fecha Nacimiento
            ax[6] = o[2] == null ? objectCivil(o[23]) : objectCivil(o[6]);//Estado Civil
            ax[7] = o[7];//Calle
            ax[8] = o[8];//Num Domicilio
            ax[9] = o[9];//Colonia
            ax[10] = o[10];//CP
            ax[11] = o[11];//Clave larga distancia + Tel
            ax[12] = o[12];//Municipio
            ax[13] = o[13];//Estado
            ax[14] = o[2] == null ? objectGenero(o[25]) : objectGenero(o[14]);//Sexo
            ax[15] = o[15];//Tel 10 posiciones
            ax[16] = o[24];//CURP
            ax[17] = o[26] == null ? "Sin Otorgamiento" : o[26];//Nombre escuela
            otX.add(ax);
        }
        ExcelExport excelExport = new ExcelExport();
        String[] encabezado = new String[]{"ID Nombre de la escuela ", "id_becario", "Nombre del Empleado", "Apellido Paterno del Empleado", "Apellido Materno del Empleado", "Fecha de Nacimiento aaaa/mm/dd", "Estado Civil", "Domicilio Nombre de la Calle ", "Numero de domicilio ", "Colonia", "Código Postal", "Clave de Larga Distancia + Teléfono Domicilio", "Población o Delegación", "Estado", "Sexo ", "Numero telefonico a 10 posiciones ", "CURP", "Unidad Academica"};
        setContentDisposition("attachment; filename=\"" + identificador + ".xlsx\"");
        excelStream = excelExport.construyeExcel(encabezado, otX);

        return "archivo";
    }
    
    //Este metodo esta en desuso por lo tanto no se le hizo las actualizaciones al reporte de excel
    public String descargarListado() {
        if ("x".equals(periodo)) {
            periodo = getDaos().getPeriodoDao().getPeriodoActivo().getId().toString();
        }

        SolicitudCuentas sc = new SolicitudCuentas();
        sc.setIdentificador(identificador);
        sc.setFechaGeneracion(new Date());
        sc.setUsuarioGeneracion((Usuario) ActionContext.getContext().getSession().get("usuario"));
        sc.setPeriodoGeneracion(getDaos().getPeriodoDao().findById(new BigDecimal(periodo)));
        SolicitudCuentasBO sbo = new SolicitudCuentasBO(getDaos());
        sbo.guarda(sc);

        List<Object[]> ot = getDaos().getOtorgamientoDao().otorgamientosReporteBanamex(periodo, nivel, ua, numeroDeBoleta, tipoAsignacion);
        List<Object[]> otX = new ArrayList<>();

        for (Object[] o : ot) {
            Alumno a = getDaos().getAlumnoDao().findById((BigDecimal) o[27]);
            AlumnoDatosBancarios adb = getDaos().getAlumnoDatosBancariosDao().findById((BigDecimal) o[28]);
            EstatusTarjetaBancaria et = getDaos().getEstatusTarjetaBancariaDao().findById(new BigDecimal(12));//ESTATUS: EN TRAMITE
            AlumnoTarjetaBancaria at = new AlumnoTarjetaBancaria();
            at.setAlumno(a);
            at.setDatosBancarios(adb);
            at.setEstatusTarjBanc(et);
            at.setSolicitudCuentas(sc);
            AlumnoTarjetaBancariaBO abo = new AlumnoTarjetaBancariaBO(getDaos());
            abo.guardaTarjeta(at);

            Object[] ax = new Object[30];
            ax[0] = o[0];//ID Nombre Escuela
            ax[1] = o[1];//ID Becario
            ax[2] = o[2] == null ? o[19] : o[2];//Nombre
            ax[3] = o[2] == null ? o[20] : o[3];//AP Paterno
            ax[4] = o[2] == null ? o[21] : o[4];//AP Materno
            ax[5] = o[2] == null ? fechaN(o[22]) : fechaN(o[5]);//Fecha Nacimiento
            ax[6] = o[2] == null ? objectCivil(o[23]) : objectCivil(o[6]);//Estado Civil
            ax[7] = o[7];//Calle
            ax[8] = o[8];//Num Domicilio
            ax[9] = o[9];//Colonia
            ax[10] = o[10];//CP
            ax[11] = o[11];//Clave larga distancia + Tel
            ax[12] = o[12];//Municipio
            ax[13] = o[13];//Estado
            ax[14] = o[2] == null ? objectGenero(o[25]) : objectGenero(o[14]);//Sexo
            ax[15] = o[15];//Tel 10 posiciones
            ax[16] = o[24];//CURP
            ax[17] = o[26];//Nombre escuela
            otX.add(ax);
        }
        ExcelExport excelExport = new ExcelExport();
        String[] encabezado = new String[]{"ID Nombre de la escuela ", "id_becario", "Nombre del Empleado", "Apellido Paterno del Empleado", "Apellido Materno del Empleado", "Fecha de Nacimiento aaaa/mm/dd", "Estado Civil", "Domicilio Nombre de la Calle ", "Numero de domicilio ", "Colonia", "Código Postal", "Clave de Larga Distancia + Teléfono Domicilio", "Población o Delegación", "Estado", "Sexo ", "Numero telefonico a 10 posiciones ", "CURP", "Unidad Academica"};
        setContentDisposition("attachment; filename=\"" + identificador + ".xlsx\"");
        excelStream = excelExport.construyeExcel(encabezado, otX);

        return "archivo";
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

    public String cancelarSolicitud() {
        if (identff == null || identff.length() == 0) {
            addActionError("No es posible encontrar el identificador");
        }
        SolicitudCuentas sc = getDaos().getSolicitudCuentasDao().findByIdentificador(identff);
        if (sc == null) {
            addActionError("No existe una solicitud con el identificador otorgado");
        } else {
            AlumnoTarjetaBancariaBO bo = new AlumnoTarjetaBancariaBO(getDaos());
            Boolean res = bo.eliminarSolicitudes(sc.getId());
            if (res) {
                sc.setFechaCancelacion(new Date());
                sc.setUsuarioCancelacion((Usuario) ActionContext.getContext().getSession().get("usuario"));
                SolicitudCuentasBO sbo = new SolicitudCuentasBO(getDaos());
                sbo.guarda(sc);
                addActionMessage("La orden ha sido cancelada exitosamente.");
            } else {
                addActionError("Existio un problema con tu solicitud, vuelva a intentarlo.");
            }
        }
        return "listado";
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

    public Boolean getContestoESE() {
        contestoESE = getDaos().getSolicitudBecaDao().exiteESEPeriodoActivo(alumno.getId(), new BigDecimal("1"), getDaos().getPeriodoDao().getPeriodoActivo().getId());
        return contestoESE;
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