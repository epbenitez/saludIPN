package com.becasipn.actions.alumno;

import com.becasipn.actions.BaseReportAction;
import com.becasipn.business.AdministraCuestionarioBO;
import com.becasipn.business.AlumnoBO;
import com.becasipn.business.DocumentosBO;
import com.becasipn.business.PeriodoBO;
import com.becasipn.domain.IngresosEgresos;
import com.becasipn.domain.ParametrosPDF;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.Beca;
import com.becasipn.persistence.model.Cuestionario;
import com.becasipn.persistence.model.CuestionarioPreguntaRespuesta;
import com.becasipn.persistence.model.CuestionarioRespuestas;
import com.becasipn.persistence.model.CuestionarioRespuestasUsuario;
import com.becasipn.persistence.model.SolicitudBeca;
import com.becasipn.persistence.model.CuestionarioTransporte;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.Otorgamiento;
import com.becasipn.persistence.model.Parentesco;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.TipoBecaPeriodo;
import com.becasipn.persistence.model.Transporte;
import com.becasipn.persistence.model.TransporteDatosFamiliares;
import com.becasipn.persistence.model.TransporteTraslado;
import com.becasipn.persistence.model.Trayecto;
import com.becasipn.persistence.model.Usuario;
import com.opensymphony.xwork2.ActionContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Tania G. Sánchez
 */
public class AdministraBecaTransporteAction extends BaseReportAction implements MensajesAlumno {

    public static final String ESE = "ese";
    public static final String REQUISITO = "requisito";
    public static final String RESULTADO = "resultado";
    public static final String PDF = "pdf";
    CuestionarioTransporte becaTransporte = new CuestionarioTransporte();
    private String cuestionarioId;
    //Datos del archivo con los documentos
    private File upload;
    private String uploadFileName;
    private String uploadContentType;
    //Datos de la tabla dinámica de datos de traslado
    private String transporte;
    private String puntopartida;
    private String puntollegada;
    private String costo;
    private String trayecto;
    //Datos de la tabla dinámica de datos familiares
    private String nombrefamiliar;
    private String parentesco;
    private String edad;
    private String ocupacion;
    private String aportacionmensual;
    private Boolean tieneBecaTransporte;
    private Boolean becaCompatibleESE;
    private Boolean becaCompatiblePasadaESE;
    private Boolean tieneBecaExterna = false;
    private Boolean mandarPorManutencion = false;
    private Boolean sinBeca;
    private Otorgamiento otorgamientoPasado;
    private Boolean periodoActivo = Boolean.FALSE;
    private Boolean tieneValidacionInscripcion = false;
    private List<ParametrosPDF> listaPreguntas1 = new ArrayList<>();
    private List<ParametrosPDF> listaPreguntas2 = new ArrayList<>();
    private List<ParametrosPDF> listaPreguntas3 = new ArrayList<>();
    private List<ParametrosPDF> listaPreguntas4 = new ArrayList<>();
    private BigDecimal alumnoId;
    private IngresosEgresos ie = new IngresosEgresos();
    
    // Para mostrar el eset sólo lectura
    boolean validacionInscripcion;
    CuestionarioTransporte respuestasTransporte;

    public String requisito() {
        PeriodoBO bo = new PeriodoBO(getDaos());
        Periodo periodo = bo.getPeriodoActivo();
        Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        Alumno alumno = getDaos().getAlumnoDao().findByBoleta(usuario.getUsuario());
        //Se buscan los datos académicos del alumno para el periodo vigente.
        DatosAcademicos datosAcademicos = getDaos().getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), periodo.getId());
        //Si el alumno no tiene datos no lo dejaremos contestar.
        if (datosAcademicos == null) {
            addActionError(getText("misdatos.alumno.sin.datos.academicos"));
            return ERROR;
        }

        AlumnoBO abo = new AlumnoBO(getDaos());
        periodoActivo = abo.solicitudActiva(alumno, periodo);

        // Si el alumno está en lista negra, no lo dejamos contestar.
        AdministraCuestionarioBO cBO = AdministraCuestionarioBO.getInstance(getDaos());
        tieneBecaExterna = cBO.tieneBecaExterna(alumno);
        if (tieneBecaExterna) {
            return REQUISITO;
        }

        // Si la beca transporte, sólo tiene validación de inscripción, 
        // ya no es necesario que vuelva a contestar el ESET
        tieneValidacionInscripcion = getDaos().getOtorgamientoDao().tieneValidacionInscripcion(periodo, alumno.getId(), true);
        if (tieneValidacionInscripcion) {
            Otorgamiento otorgamientoAnterior = getDaos().getOtorgamientoDao().getOtorgamientoAlumno(alumno.getId(), periodo.getPeriodoAnterior().getId());
            Boolean cambioDeUA = false;
            if (otorgamientoAnterior != null) {
                cambioDeUA = !datosAcademicos.getUnidadAcademica().getId().equals(otorgamientoAnterior.getProceso().getUnidadAcademica().getId());
            }
            // Si no cambió de UA, no lo dejamos continuar
            if (!cambioDeUA) {
                return REQUISITO;
            }
        }

        Boolean eseTransporte = getDaos().getCuestionarioTransporteDao().tieneEseTransporte(usuario.getId(), periodo.getId());
        List<Otorgamiento> otorgamientoActual = getDaos().getOtorgamientoDao().getOtorgamientosAlumno(alumno.getId(), periodo.getId());
        // Revisa si tiene un ESE de transporte
        if (eseTransporte) {
            return resultado(true);
        } else if (otorgamientoActual == null) {
            // si no tiene ESE transporte y no tiene otorgamiento, podría contestar
            // la solicitud.
            // Sin embargo, si tiene validación de inscripción correspondiente a 
            // otra que no sea institucional, entonces no podrá solicitar transporte
            Boolean tuvoOtra = getDaos().getOtorgamientoDao().tieneValidacionInscripcion(periodo, alumno.getId(), null);
            if (tuvoOtra) {
                Boolean tuvoInstitucional = getDaos().getOtorgamientoDao().tieneValidacionInscripcion(periodo, alumno.getId(), false);
                if (tuvoInstitucional) {
                    becaCompatibleESE = Boolean.TRUE;
                } else {
                    becaCompatibleESE = Boolean.FALSE;
                }
            } else {
                // Si no tuvo, ni tiene nada
                becaCompatibleESE = Boolean.TRUE;
                // Si el alumno es de nuevo ingreso y la variable correspondiente 
                // no permite a los de nuevo ingreso contestar solicitudes, no lo dejamos contestar.
                mandarPorManutencion = cBO.deberiaIrPorManutencion(alumno);
            }
        } else {
            // si no tiene ESE transporte
            for (Otorgamiento o : otorgamientoActual) {
                if (o.getTipoBecaPeriodo().getTipoBeca().getClave().equals("BTI")) {
                    // Si es tipo beca: Proyecto Beca de Transporte IPN
                    return resultado(false);
                }
                if (o.getTipoBecaPeriodo().getTipoBeca().getBeca().getClave().equals("I")) {
                    // Si el programa es Institucional
                    becaCompatibleESE = Boolean.TRUE;
                } else {
                    becaCompatibleESE = Boolean.FALSE;
                }
            }
        }

        return REQUISITO;
    }

    public String ese() {
        Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        Alumno alumno = getDaos().getAlumnoDao().findByBoleta(usuario.getUsuario());
        PeriodoBO bo = new PeriodoBO(getDaos());
        Periodo periodo = bo.getPeriodoActivo();
        
        AlumnoBO abo = new AlumnoBO(getDaos());
        periodoActivo = abo.solicitudActiva(alumno, periodo);
        
        if (!periodoActivo) {
            return requisito();
        }
        
        DocumentosBO boD = new DocumentosBO(getDaos());
        ie = boD.datosEgresos(alumno, periodo);
        
        Otorgamiento otorgamientoActual;
        otorgamientoActual = getDaos().getOtorgamientoDao().getOtorgamientoAlumno(alumno.getId(), periodo.getId());
        if (otorgamientoActual == null || otorgamientoActual.getTipoBecaPeriodo().getTipoBeca().getBeca().getClave().equals("I")) {
            becaCompatibleESE = Boolean.TRUE;
            //Si el alumno no tiene beca asignada para el periodo actual.
            if (otorgamientoActual == null) {
                otorgamientoPasado = getDaos().getOtorgamientoDao().getOtorgamientoAlumno(alumno.getId(), periodo.getPeriodoAnterior().getId());
                //Si el alumno no tuvo beca el periodo anterior.
                if (otorgamientoPasado == null) {
                    becaCompatiblePasadaESE = Boolean.FALSE;
                    sinBeca = Boolean.TRUE;
                } else if (otorgamientoPasado.getTipoBecaPeriodo().getTipoBeca().getBeca().getClave().equals("I")) {
                    becaCompatiblePasadaESE = Boolean.TRUE;
                    sinBeca = Boolean.FALSE;
                } else {
                    becaCompatiblePasadaESE = Boolean.FALSE;
                    sinBeca = Boolean.FALSE;
                }
            }
        } else {
            becaCompatibleESE = Boolean.FALSE;
            return REQUISITO;
        }
        return ESE;
    }

    public String guardar() {
        //Se busca al usuario actual
        Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        //Alumno
        Alumno alumno = getDaos().getAlumnoDao().findByBoleta(usuario.getUsuario());
        //Se busca el periodo activo
        PeriodoBO bo = new PeriodoBO(getDaos());
        Periodo periodo = bo.getPeriodoActivo();
        
        AlumnoBO abo = new AlumnoBO(getDaos());
        periodoActivo = abo.solicitudActiva(alumno, periodo);
        
        if (!periodoActivo) {
            return requisito();
        }
        //Se busca el cuestionario 
        Cuestionario cuestionario = getDaos().getCuestionarioDao().findById(new BigDecimal("2"));
        
        if(getDaos().getSolicitudBecaDao().guardasolicitudbecatransporte( becaTransporte, cuestionario, usuario, periodo, transporte, puntopartida, puntollegada, costo, trayecto, nombrefamiliar, parentesco, edad, ocupacion, aportacionmensual, alumno)){
            return resultado(false);
        }else{
            return ese();
        }
    }

    public String resultado(Boolean eseTransporte) {
        Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        Alumno alumno = getDaos().getAlumnoDao().findByBoleta(usuario.getUsuario());
        PeriodoBO bo = new PeriodoBO(getDaos());
        Periodo periodo = bo.getPeriodoActivo();
        // Si no tenía hay que revisar de nuez
        if (!eseTransporte) {
            eseTransporte = getDaos().getCuestionarioTransporteDao().tieneEseTransporte(usuario.getId(), periodo.getId());
        }
        
        if (!eseTransporte) {
            List<Otorgamiento> otorgamientoActual = getDaos().getOtorgamientoDao().getOtorgamientosAlumno(alumno.getId(), periodo.getId());
            for (Otorgamiento o : otorgamientoActual) {
                if (o.getTipoBecaPeriodo().getTipoBeca().getBeca().getClave().equals("I")) {
                    becaCompatibleESE = Boolean.TRUE;
                } else {
                    becaCompatibleESE = Boolean.FALSE;
                }
            }
            // Si no se encuentra un ese, hay que regresar a la solicitud
            return REQUISITO;
        } else {
            // Si hay ese, entonces muestra la pantalla de finalizado
            setInfoSolicitudTransporte(alumno, periodo);
            return RESULTADO;
        }
    }
    
    public void setInfoSolicitudTransporte(Alumno alumno, Periodo periodoActivo) {
        validacionInscripcion = getDaos().getOtorgamientoDao().tieneValidacionInscripcion(periodoActivo, alumnoId, true);
        respuestasTransporte = getDaos().getCuestionarioTransporteDao().respuestasTransporte(alumno.getUsuario().getId(), periodoActivo.getId());
        alumnoId = alumno.getId();
    }

    public void saveFile(File f, InputStream istream) throws IOException {
        InputStream inputStream = istream;
        OutputStream out = null;
        try {
            out = new FileOutputStream(f);
        } catch (Exception e) {
            f.createNewFile();
            out = new FileOutputStream(f);
        }
        byte buf[] = new byte[1024];
        int len;
        while ((len = inputStream.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.close();
        inputStream.close();
        LOG.debug("File " + f.getName() + " created.");
    }

    public String pdf() {
        //Usuario
        Usuario u = (Usuario) ActionContext.getContext().getSession().get("usuario");
        //Cuestionario
        Cuestionario c = getDaos().getCuestionarioDao().findById(new BigDecimal(cuestionarioId));
        //Periodo
        PeriodoBO bo = new PeriodoBO(getDaos());
        Periodo periodo = bo.getPeriodoActivo();
        //Alumno
        Alumno alumno;
        if (isAlumno()) {
            String numeroDeBoleta = SecurityContextHolder.getContext().getAuthentication().getName();
            alumno = getDaos().getAlumnoDao().findByBoleta(numeroDeBoleta);
        } else {
            alumno = getDaos().getAlumnoDao().findById(alumnoId);
        }
        DatosAcademicos datosAcademicos = getDaos().getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), periodo.getId());
        respuestasTransporte = getDaos().getCuestionarioTransporteDao().respuestasTransporte(alumno.getUsuario().getId(), periodo.getId());
        List<TransporteTraslado> transporteTraslado = getDaos().getTransporteTrasladoDao().respuestasTraslado(respuestasTransporte.getId());
        List<TransporteDatosFamiliares> transporteDatosFamiliares = getDaos().getTransporteDatosFamiliaresDao().respuestasTransporte(respuestasTransporte.getId());
        ParametrosPDF esePdf;
        Float total = 0F;
        for (TransporteTraslado tt : transporteTraslado) {
            total = total + tt.getCosto();
            esePdf = new ParametrosPDF();
            esePdf.setParametro1(tt.getTransporte().getNombre());
            esePdf.setParametro2(tt.getPuntopartida());
            esePdf.setParametro3(tt.getPuntollegada());
            esePdf.setParametro4(tt.getCosto().toString());
            esePdf.setParametro5(tt.getTrayecto().getNombre());
            listaPreguntas1.add(esePdf);
        }
        esePdf = new ParametrosPDF();
        esePdf.setParametro1("");
        esePdf.setParametro2("");
        esePdf.setParametro3("Total");
        esePdf.setParametro4(total.toString());
        esePdf.setParametro5("");
        listaPreguntas1.add(esePdf);
        TransporteTraslado tt = new TransporteTraslado();
        tt.setPuntollegada("Total");
        tt.setCosto(total);
        System.out.println(tt.getPuntollegada() + " " + tt.getCosto());
        for (TransporteDatosFamiliares df : transporteDatosFamiliares) {
            esePdf = new ParametrosPDF();
            esePdf.setParametro1(df.getNombrefamiliar());
            esePdf.setParametro2(df.getParentesco().getNombre());
            esePdf.setParametro3(df.getEdad().toString());
            esePdf.setParametro4(df.getOcupacion());
            esePdf.setParametro5(df.getAportacionmensual().toString());
            listaPreguntas2.add(esePdf);
        }
        //Datos Personales
        esePdf = new ParametrosPDF();
        esePdf.setParametro1("¿Padeces alguna enfermedad crónica?");
        esePdf.setParametro2(respuestasTransporte.getEnfermedadCronica() == 1 ? "Si" : "No");
        listaPreguntas3.add(esePdf);
        esePdf = new ParametrosPDF();
        esePdf.setParametro1("¿Tienes alguna discapacidad?");
        esePdf.setParametro2(respuestasTransporte.getDiscapacidad().getNombre());
        listaPreguntas3.add(esePdf);
        esePdf = new ParametrosPDF();
        esePdf.setParametro1("¿Algún familiar tiene alguna enfermedad crónica?");
        esePdf.setParametro2(respuestasTransporte.getFamiliarenfermcronica() == 1 ? "Si" : "No");
        listaPreguntas3.add(esePdf);
        esePdf = new ParametrosPDF();
        esePdf.setParametro1("Para continuar con tus estudios de educación superior, ¿tuviste que cambiar tu lugar de residencia?");
        esePdf.setParametro2(respuestasTransporte.getCambioresidencia() == 1 ? "Si" : "No");
        listaPreguntas3.add(esePdf);
        esePdf = new ParametrosPDF();
        esePdf.setParametro1("¿Cómo te enteraste del apoyo de esta beca?");
        esePdf.setParametro2(respuestasTransporte.getEnteroBeca().getNombre());
        listaPreguntas3.add(esePdf);
        //Situación económica
        esePdf = new ParametrosPDF();
        esePdf.setParametro1("Gasto familiar mensual de los siguientes rubros:");
        esePdf.setParametro2("");
        listaPreguntas4.add(esePdf);
        esePdf = new ParametrosPDF();
        esePdf.setParametro1("Alimentación");
        esePdf.setParametro2(respuestasTransporte.getAlimentacion().toString());
        listaPreguntas4.add(esePdf);
        esePdf = new ParametrosPDF();
        esePdf.setParametro1("Renta");
        esePdf.setParametro2(respuestasTransporte.getRenta().toString());
        listaPreguntas4.add(esePdf);
        esePdf = new ParametrosPDF();
        esePdf.setParametro1("Vivienda (Agua, Luz, Telefono, TV de paga, Internet, Servicios, etc.)");
        esePdf.setParametro2(respuestasTransporte.getVivienda().toString());
        listaPreguntas4.add(esePdf);
        esePdf = new ParametrosPDF();
        esePdf.setParametro1("Escolares");
        esePdf.setParametro2(respuestasTransporte.getEscolares().toString());
        listaPreguntas4.add(esePdf);
        esePdf = new ParametrosPDF();
        esePdf.setParametro1("Salud");
        esePdf.setParametro2(respuestasTransporte.getSalud().toString());
        listaPreguntas4.add(esePdf);
        esePdf = new ParametrosPDF();
        esePdf.setParametro1("Transporte");
        esePdf.setParametro2(respuestasTransporte.getTransporte().toString());
        listaPreguntas4.add(esePdf);
        esePdf = new ParametrosPDF();
        esePdf.setParametro1("Actividades culturales y recreativas");
        esePdf.setParametro2(respuestasTransporte.getActividades().toString());
        listaPreguntas4.add(esePdf);
        esePdf = new ParametrosPDF();
        esePdf.setParametro1("Otros");
        esePdf.setParametro2(respuestasTransporte.getOtros().toString());
        listaPreguntas4.add(esePdf);
        NumberFormat f1 = new DecimalFormat("00");
        NumberFormat f2 = new DecimalFormat("0000000");
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        String hr = sdf.format(new Date());
        Integer h = Integer.parseInt(hr);
        String hexa = Integer.toHexString(h);
        String folio = "EST" + f1.format(c.getId()) + periodo.getId().toString() + f2.format(alumno.getId()) + "T" + total.toString().replace(".", "") + "T" + hexa.toUpperCase();
        getParametros().put("imagenPlaca", "/com/becasipn/jasper/placasep.JPG");
        getParametros().put("unidadAcademica", datosAcademicos.getUnidadAcademica().getNombre());
        getParametros().put("nombre", alumno.getFullName());
        getParametros().put("boleta", alumno.getBoleta());
        getParametros().put("curp", alumno.getCurp());
        getParametros().put("telefono", alumno.getCelular());
        getParametros().put("correoElectronico", alumno.getCorreoElectronico());
        getParametros().put("estado", alumno.getDireccion().getRelacionGeografica().getEstado().getNombre());
        getParametros().put("delMun", alumno.getDireccion().getRelacionGeografica().getMunicipio().getNombre());
        getParametros().put("modalidad", datosAcademicos.getModalidad().getNombre());
        getParametros().put("periodo", periodo.getClave());
        getParametros().put("folio", folio);
        JRBeanCollectionDataSource preguntas1 = new JRBeanCollectionDataSource(listaPreguntas1);
        JRBeanCollectionDataSource preguntas2 = new JRBeanCollectionDataSource(listaPreguntas2);
        JRBeanCollectionDataSource preguntas3 = new JRBeanCollectionDataSource(listaPreguntas3);
        JRBeanCollectionDataSource preguntas4 = new JRBeanCollectionDataSource(listaPreguntas4);
        getParametros().put("ese1", preguntas1);
        getParametros().put("ese2", preguntas2);
        getParametros().put("ese3", preguntas3);
        getParametros().put("ese4", preguntas4);
        return PDF;
    }

    public CuestionarioTransporte getBecaTransporte() {
        return becaTransporte;
    }

    public void setBecaTransporte(CuestionarioTransporte becaTransporte) {
        this.becaTransporte = becaTransporte;
    }

    public String getCuestionarioId() {
        return cuestionarioId;
    }

    public void setCuestionarioId(String cuestionarioId) {
        this.cuestionarioId = cuestionarioId;
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

    public String getTransporte() {
        return transporte;
    }

    public void setTransporte(String transporte) {
        this.transporte = transporte;
    }

    public String getPuntopartida() {
        return puntopartida;
    }

    public void setPuntopartida(String puntopartida) {
        this.puntopartida = puntopartida;
    }

    public String getPuntollegada() {
        return puntollegada;
    }

    public void setPuntollegada(String puntollegada) {
        this.puntollegada = puntollegada;
    }

    public String getCosto() {
        return costo;
    }

    public void setCosto(String costo) {
        this.costo = costo;
    }

    public String getTrayecto() {
        return trayecto;
    }

    public void setTrayecto(String trayecto) {
        this.trayecto = trayecto;
    }

    public String getNombrefamiliar() {
        return nombrefamiliar;
    }

    public void setNombrefamiliar(String nombrefamiliar) {
        this.nombrefamiliar = nombrefamiliar;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public String getAportacionmensual() {
        return aportacionmensual;
    }

    public void setAportacionmensual(String aportacionmensual) {
        this.aportacionmensual = aportacionmensual;
    }

    public Boolean getTieneBecaTransporte() {
        return tieneBecaTransporte;
    }

    public void setTieneBecaTransporte(Boolean tieneBecaTransporte) {
        this.tieneBecaTransporte = tieneBecaTransporte;
    }

    public Boolean getBecaCompatibleESE() {
        return becaCompatibleESE;
    }

    public void setBecaCompatibleESE(Boolean becaCompatibleESE) {
        this.becaCompatibleESE = becaCompatibleESE;
    }

    public Boolean getBecaCompatiblePasadaESE() {
        return becaCompatiblePasadaESE;
    }

    public void setBecaCompatiblePasadaESE(Boolean becaCompatiblePasadaESE) {
        this.becaCompatiblePasadaESE = becaCompatiblePasadaESE;
    }

    public Boolean getSinBeca() {
        return sinBeca;
    }

    public void setSinBeca(Boolean sinBeca) {
        this.sinBeca = sinBeca;
    }

    public Otorgamiento getOtorgamientoPasado() {
        return otorgamientoPasado;
    }

    public void setOtorgamientoPasado(Otorgamiento otorgamientoPasado) {
        this.otorgamientoPasado = otorgamientoPasado;
    }

    public Boolean getPeriodoActivo() {
        return periodoActivo;
    }

    public void setPeriodoActivo(Boolean periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public List<ParametrosPDF> getListaPreguntas1() {
        return listaPreguntas1;
    }

    public void setListaPreguntas1(List<ParametrosPDF> listaPreguntas1) {
        this.listaPreguntas1 = listaPreguntas1;
    }

    public List<ParametrosPDF> getListaPreguntas2() {
        return listaPreguntas2;
    }

    public void setListaPreguntas2(List<ParametrosPDF> listaPreguntas2) {
        this.listaPreguntas2 = listaPreguntas2;
    }

    public List<ParametrosPDF> getListaPreguntas3() {
        return listaPreguntas3;
    }

    public void setListaPreguntas3(List<ParametrosPDF> listaPreguntas3) {
        this.listaPreguntas3 = listaPreguntas3;
    }

    public List<ParametrosPDF> getListaPreguntas4() {
        return listaPreguntas4;
    }

    public void setListaPreguntas4(List<ParametrosPDF> listaPreguntas4) {
        this.listaPreguntas4 = listaPreguntas4;
    }

    public BigDecimal getAlumnoId() {
        return alumnoId;
    }

    public void setAlumnoId(BigDecimal alumnoId) {
        this.alumnoId = alumnoId;
    }

    public Boolean getTieneValidacionInscripcion() {
        return tieneValidacionInscripcion;
    }

    public void setTieneValidacionInscripcion(Boolean tieneValidacionInscripcion) {
        this.tieneValidacionInscripcion = tieneValidacionInscripcion;
    }

    public Boolean getTieneBecaExterna() {
        return tieneBecaExterna;
    }

    public void setTieneBecaExterna(Boolean tieneBecaExterna) {
        this.tieneBecaExterna = tieneBecaExterna;
    }

    public Boolean getMandarPorManutencion() {
        return mandarPorManutencion;
    }

    public void setMandarPorManutencion(Boolean mandarPorManutencion) {
        this.mandarPorManutencion = mandarPorManutencion;
    }

    public IngresosEgresos getIe() {
        return ie;
    }

    public void setIe(IngresosEgresos ie) {
        this.ie = ie;
    }

    public boolean isValidacionInscripcion() {
        return validacionInscripcion;
    }

    public CuestionarioTransporte getRespuestasTransporte() {
        return respuestasTransporte;
    }

    public void setRespuestasTransporte(CuestionarioTransporte respuestasTransporte) {
        this.respuestasTransporte = respuestasTransporte;
    }
}
