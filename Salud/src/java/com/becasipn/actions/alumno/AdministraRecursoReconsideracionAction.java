package com.becasipn.actions.alumno;

import com.becasipn.actions.BaseReportAction;
import com.becasipn.business.PeriodoBO;
import com.becasipn.business.RecursoReconsideracionBO;
import com.becasipn.business.SolicitudBecasBO;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.business.AlumnoBO;
import com.becasipn.domain.ParametrosPDF;
import com.becasipn.exception.LoginException;
import com.becasipn.persistence.model.Beca;
import com.becasipn.persistence.model.Cuestionario;
import com.becasipn.persistence.model.CuestionarioPreguntaRespuesta;
import com.becasipn.persistence.model.CuestionarioPreguntas;
import com.becasipn.persistence.model.CuestionarioRespuestas;
import com.becasipn.persistence.model.CuestionarioRespuestasUsuario;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.EstatusReconsideracion;
import com.becasipn.persistence.model.Otorgamiento;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.SolicitudBeca;
import com.becasipn.persistence.model.SolicitudReconsideracion;
import com.becasipn.persistence.model.Usuario;
import com.opensymphony.xwork2.ActionContext;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.PersistenceException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Augusto I. Hernández Ruiz
 *
 * "El problema real no es si las máquinas piensan, sino si lo hacen los
 * hombres" B. F. Skinner.
 */

/*
        - Sólo podrán solicitar un recurso de reconsideración los alumnos que
        tienen una solicitud de beca en el periodo actual.
        
        - Podrá observar el estatus de su solicitud (ingresarán con el estatus
        "En espera").
        
        - La solicitud deberá poderse descargar en PDF con los mismos datos que
        actualmente se utilizan en las solicitudes de beca

 */
public class AdministraRecursoReconsideracionAction extends BaseReportAction {

    public static final String CUESTIONARIO = "cuestionario";
    public static final String PDF = "pdf";
    public static final String DESHABILITADO = "deshabilitado";

    private Periodo periodo;
    private Alumno alumno;
    private EstatusReconsideracion estatusReconsideracion = new EstatusReconsideracion();
    private SolicitudReconsideracion solicitudReconsideracion = new SolicitudReconsideracion();

    private BigDecimal alumnoId;
    private String solicitudReconsideracionId;
    private String numeroDeBoleta;
    private String cuestionarioId;
    private String reason;
    private String folio;
    private String periodoActual;
    private String hResultdos;
    private Boolean tieneSolicitudBeca;
    private Boolean periodoActivo = Boolean.FALSE;
    private Boolean cuestionarioDiv = Boolean.FALSE;
    private Boolean pdfDiv = Boolean.FALSE;

    List<CuestionarioPreguntaRespuesta> preguntas = new ArrayList<>();
    List<CuestionarioRespuestas> respuestas = new ArrayList<>();

    private List<ParametrosPDF> esePdfList1 = new ArrayList<>();
    private List<ParametrosPDF> esePdfList2 = new ArrayList<>();
    private List<ParametrosPDF> esePdfList3 = new ArrayList<>();
    private List<ParametrosPDF> esePdfList4 = new ArrayList<>();
    private List<ParametrosPDF> esePdfList5 = new ArrayList<>();
    List<CuestionarioRespuestasUsuario> respuestasUsuario = new ArrayList<>();
    private List<SolicitudBeca> sbList = new ArrayList<>();
    private List<SolicitudReconsideracion> srList = new ArrayList<>();
    private Date hoy = new Date();

    public String cuestionario() {

        numeroDeBoleta = SecurityContextHolder.getContext().getAuthentication().getName();
        alumno = getDaos().getAlumnoDao().findByBoleta(numeroDeBoleta);
        if (alumno == null) {
            addActionError("Boleta no encontrada");
            return DESHABILITADO;
        }

        // Obtiene el periodo activo
        PeriodoBO pbo = new PeriodoBO(getDaos());
        periodo = pbo.getPeriodoActivo();

        // Valida que tenga alguna solicitud de beca en el periodo actual
        RecursoReconsideracionBO rbo = new RecursoReconsideracionBO(getDaos());
        tieneSolicitudBeca = rbo.tieneSolicitudPeriodoActual(periodo.getId(), alumno.getId());

        // Validación de Periodo Activo
        String fInicialStr = (String) ActionContext.getContext().getApplication().get("fechaInicioReconsideracion");
        String fFinalStr = (String) ActionContext.getContext().getApplication().get("fechaFinalReconsideracion");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // Obtiene la fecha Final e Inicial del periodo y la fecha actual.lo compara con la fecha de hoy para establecer si el periodo esta activo o no
            Date dateInicial = formatter.parse(fInicialStr);
            Date dateFinal = formatter.parse(fFinalStr);
            Calendar c = Calendar.getInstance();
            c.setTime(dateFinal);
            c.add(Calendar.DATE, 1);
            dateFinal = c.getTime();
            Date hoy = new Date();

            // Compara la fecha Final e Inicial con la fecha de hoy para establecer si el periodo esta activo o no
            if (hoy.after(dateInicial) && hoy.before(dateFinal)) {
                periodoActivo = Boolean.TRUE;
            } else {
                return CUESTIONARIO;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Si tiene alguna solicitud de beca procede a mostrar el cuestionario, si no tiene
        // manda un mensaje al alumno.  
        if (tieneSolicitudBeca) {

            // Se obtiene la lista de solicitudes de reconsideración para mostrarlas en el select de descarga de PDF
            RecursoReconsideracionBO srbo = new RecursoReconsideracionBO(getDaos());
            srList = srbo.SolicitudesReconsideracionPorAlumnoList(alumno.getId(), periodo.getId());

            //Se valida que tenga algún registro para mandar la bandera de mostrar/no mostrar el div correspondiente
            if (!srList.isEmpty()) {
                pdfDiv = Boolean.TRUE;
            }

            // Se obtiene la lista de solicitudes del alumno en el periodo actual, las cuales no tengan recurso de reconsideración solicitado.
            SolicitudBecasBO sbo = new SolicitudBecasBO(getDaos());
            sbList = sbo.SolicitudesPorAlumnoList(alumno.getId(), periodo.getId());

            //Se valida que tenga algún registro para mandar la bandera de mostrar/no mostrar el div correspondiente
            if (!sbList.isEmpty()) {
                cuestionarioDiv = Boolean.TRUE;
            }

            //Buscar cuestionario correspondiente a Recurso Reconsideración
            DatosAcademicos datosAcademicos = getDaos().getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), periodo.getId());
            cuestionarioId = "7";
            Cuestionario c = getDaos().getCuestionarioDao().findById(new BigDecimal(cuestionarioId));
            if (c == null) {
                addActionError(getText("misdatos.alumno.no.existe.cuesionario"));
            } else if (cuestionarioId == null || cuestionarioId.isEmpty()) {
                addActionError(getText("misdatos.alumno.no.especifico.cuestionario"));
            } else {
                BigDecimal cId = new BigDecimal(cuestionarioId);
                preguntas = getDaos().getCuestionarioPreguntaRespuestaDao().findByCuestionario(cId, datosAcademicos == null ? null : datosAcademicos.getUnidadAcademica().getNivel().getId());
                System.out.println("largo: " + preguntas.size());
            }
            return CUESTIONARIO;
        } else {
            //Si no tiene, manda un mensaje con la leyenda correspondiente
            addActionError("Esta solicitud solo puede ser generada por alumnos que cuenten con una solicitud de beca en el periodo actual");
            //return DESHABILITADO;
            return CUESTIONARIO;
        }

    }

    public String finalizar() throws LoginException {

        // ------------------ Validación de Periodo Activo ------------------------------
        String fInicialStr = (String) ActionContext.getContext().getApplication().get("fechaInicioReconsideracion");
        String fFinalStr = (String) ActionContext.getContext().getApplication().get("fechaFinalReconsideracion");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        //Inicialmente se valida si el registro esta aún activo.
        try {
            //Obtiene la fecha Final e Inicial del periodo y la fecha actual.lo compara con la fecha de hoy para establecer si el periodo esta activo o no
            Date dateInicial = formatter.parse(fInicialStr);
            Date dateFinal = formatter.parse(fFinalStr);
            Calendar c = Calendar.getInstance();
            c.setTime(dateFinal);
            c.add(Calendar.DATE, 1);
            dateFinal = c.getTime();
            Date hoy = new Date();

            //Compara la fecha Final e Inicial con la fecha de hoy para establecer si el periodo esta activo o no
            if (hoy.after(dateInicial) && hoy.before(dateFinal)) {
                periodoActivo = Boolean.TRUE;
            } else {
                return CUESTIONARIO;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // ------------------ Obtención del periodo actual ------------------------------
        PeriodoBO bo = new PeriodoBO(getDaos());
        Periodo periodo = bo.getPeriodoActivo();

        // ------------------ Obtención de datos del Alumno ------------------------------
        numeroDeBoleta = SecurityContextHolder.getContext().getAuthentication().getName();
        alumno = getDaos().getAlumnoDao().findByBoleta(numeroDeBoleta);
        if (alumno == null) {
            reason = "Boleta no encontrada";
            return DESHABILITADO;
        }

        RecursoReconsideracionBO srbo = new RecursoReconsideracionBO(getDaos());

        solicitudReconsideracion = srbo.guardaSolicitud(solicitudReconsideracion, alumno, periodo, estatusReconsideracion);

//        if (hResultdos != null && !hResultdos.equals("")) {
//            String[] resultados = null;
//            String[] valores = null;
//            hResultdos = hResultdos.replaceAll("buscador", "");
//            resultados = hResultdos.split("@");
//            if (resultados.length > 1) {
//                Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
//                if (usuario == null) {
//                    return INPUT;
//                }
//                for (String pregunta : resultados) {
//                    valores = pregunta.split(",");
//                    if (valores.length > 1) {
//                        //System.out.println(valores[0] + "|" + valores[1]);
//                        String noPregunta = valores[0];
//                        String valor = valores[1];
//                        String[] multiple = valor.split("-");
//
//                        for (String m : multiple) {
//                            CuestionarioRespuestasUsuario ru = new CuestionarioRespuestasUsuario();
//                            CuestionarioPreguntas p = getDaos().getCuestionarioPreguntasDao().findById(new BigDecimal(noPregunta));
//                            ru.setPregunta(p);
//                            BigDecimal idRespuesta;
//                            CuestionarioRespuestas cr = new CuestionarioRespuestas();
//                            try {
//                                //Respuesta del catalogo de respuesta
//                                idRespuesta = new BigDecimal(m);
//                                if (p.getCuestionarioPreguntaTipo().getId().equals(new BigDecimal(3))) {
//                                    cr.setId(new BigDecimal(181));
//                                    ru.setRespuestaAbierta(valor);
//                                    ru.setRespuesta(cr);
//                                } else {
//                                    cr = getDaos().getCuestionarioRespuestasDao().findById(idRespuesta);
//                                    ru.setRespuesta(cr);
//                                }
//                            } catch (Exception e) {
//                                //Respuesta Abierta
//                                CuestionarioRespuestas cra = new CuestionarioRespuestas();
//                                cra.setId(new BigDecimal(1));
//                                ru.setRespuesta(cra);
//                                ru.setRespuestaAbierta(m);
//                            }
//                            ru.setPeriodo(periodo);
//                            ru.setUsuario(usuario);
//                            try {
//                                if (ru.getUsuario() != null && ru.getPregunta() != null && ru.getRespuesta() != null && ru.getCuestionario() != null && ru.getPeriodo() != null) {
//                                    getDaos().getCuestionarioRespuestasUsuarioDao().save(ru);
//                                }
//                            } catch (PersistenceException e) {
//                                LOG.warn("Ya existe el ESE para el periodo activo y alumno_id" + alumno.getId() + " Exception: " + e.getMessage());
//                            }
//                        }
//
//                    }
//                }
//                try {
//                    if (sr.getId() == null) {
//                        getDaos().getSolicitudReconsideracionDao().save(sr);
//                        System.out.println("Guardado!");
//                    } else {
//                        getDaos().getSolicitudReconsideracionDao().update(sr);
//                        System.out.println("Updateado!");
//                    }
//
//                } catch (PersistenceException e) {
//                    LOG.warn("Ya existe el Censo de Salud para el perido activo y alumno_id: " + alumno.getId());
//                }
//            }
//
//        }
        return cuestionario();
    }

    public String pdf() {

        //Periodo
        PeriodoBO bo = new PeriodoBO(getDaos());
        Periodo periodo = bo.getPeriodoActivo();

        /*
        Solicitud Reconsideración a Descargar
        El parámetro solicitudReconsideracionId se obtiene desde el jsp
        /alumno/recursoReconsideracion/form.jsp
         */
        System.out.println("lalalalalaal" + solicitudReconsideracionId);
        solicitudReconsideracion = getDaos().getSolicitudReconsideracionDao().findById(new BigDecimal(solicitudReconsideracionId));

        //Alumno
        if (isAlumno()) {
            String numeroDeBoleta = SecurityContextHolder.getContext().getAuthentication().getName();
            alumno = getDaos().getAlumnoDao().findByBoleta(numeroDeBoleta);
        } else {
            alumno = getDaos().getAlumnoDao().findById(alumnoId);
        }

        AlumnoBO abo = new AlumnoBO(getDaos());
        periodoActivo = abo.solicitudActiva(alumno, periodo);

        //Usuario
        Usuario u = (Usuario) ActionContext.getContext().getSession().get("usuario");

        DatosAcademicos datosAcademicos = getDaos().getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), periodo.getId());
        alumno.setDatosAcademicos(datosAcademicos);

        try {
            if (alumno.getNombre() != null) {
                alumno.setNombre(alumno.getNombre().replace("'", "\\\'"));
            }
            if (alumno.getApellidoPaterno() != null) {
                alumno.setApellidoPaterno(alumno.getApellidoPaterno().replace("'", "\\\'"));
            }
            if (alumno.getApellidoMaterno() != null) {
                alumno.setApellidoMaterno(alumno.getApellidoMaterno().replace("'", "\\\'"));
            }
            if (periodo.getClave().length() == 5) {
                periodoActual = periodo.getClave().substring(0, 4) + " / 0" + periodo.getClave().substring(4, 5);
            } else if (periodo.getClave().length() == 6) {
                periodoActual = periodo.getClave().substring(0, 4) + " / 0" + periodo.getClave().substring(5, 6);
            }
            NumberFormat f1 = new DecimalFormat("00");
            NumberFormat f2 = new DecimalFormat("0000000");
            SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
            String hr = sdf.format(hoy);
            Integer h = Integer.parseInt(hr);
            String hexa = Integer.toHexString(h);
            //folio = "ESE" + f1.format(c.getId()) + periodo.getId().toString() + f2.format(alumno.getId()) + hexa;
            ParametrosPDF esePdf = new ParametrosPDF();

            esePdf = new ParametrosPDF();
            esePdf.setParametro1("Beca por la que deseas aplicar el recurso de reconsideración:");
            esePdf.setParametro2(solicitudReconsideracion.getSolicitudBeca().getProgramaBecaSolicitada().getNombre());
            esePdfList5.add(esePdf);
            esePdf = new ParametrosPDF();
            esePdf.setParametro1("Periodo en el que solicitaste tu beca:");
            esePdf.setParametro2(solicitudReconsideracion.getPeriodo().getClave());
            esePdfList5.add(esePdf);
        } catch (Exception ex) {
            Logger.getLogger(AdministraCuestionarioAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        ;
        String leyenda = "Acepto que en caso de no poder ser beneficiado con el tipo de beca que solicité, me pueda ser otorgada otra, previo cumplimiento de los requisitos"
                + " (sujeto a disponibilidad de presupuesto). Así mismo que en caso de resultar becario en este ciclo escolar,"
                + " acepto que en el proceso de validación de mi beca se me transfiera a otra si dejo de cumplir con los requisitos de la que se me haya otorgado inicialmente"
                + " (sujeto a disponibilidad de presupuesto).";

        getParametros()
                .put("imagenPlaca", "/com/becasipn/jasper/placasep.JPG");
        getParametros()
                .put("unidadAcademica", datosAcademicos.getUnidadAcademica().getNombre());
        getParametros()
                .put("nombre", alumno.getFullName());
        getParametros()
                .put("boleta", alumno.getBoleta());
        getParametros()
                .put("curp", alumno.getCurp());
        getParametros()
                .put("telefono", alumno.getCelular());
        getParametros()
                .put("correoElectronico", alumno.getCorreoElectronico());
        getParametros()
                .put("estado", alumno.getDireccion().getRelacionGeografica().getEstado().getNombre());
        getParametros()
                .put("delMun", alumno.getDireccion().getRelacionGeografica().getMunicipio().getNombre());
        getParametros()
                .put("modalidad", datosAcademicos.getModalidad().getNombre());
        getParametros()
                .put("periodo", periodoActual);
        getParametros()
                .put("folio", folio);
        getParametros()
                .put("leyenda", leyenda);
        JRBeanCollectionDataSource seccion1 = new JRBeanCollectionDataSource(esePdfList1);
        JRBeanCollectionDataSource seccion2 = new JRBeanCollectionDataSource(esePdfList2);
        JRBeanCollectionDataSource seccion3 = new JRBeanCollectionDataSource(esePdfList3);
        JRBeanCollectionDataSource seccion4 = new JRBeanCollectionDataSource(esePdfList4);
        JRBeanCollectionDataSource seccion5 = new JRBeanCollectionDataSource(esePdfList5);

        getParametros()
                .put("ese1", seccion1);
        getParametros()
                .put("ese2", seccion2);
        getParametros()
                .put("ese3", seccion3);
        getParametros()
                .put("ese4", seccion4);
        getParametros()
                .put("ese5", seccion5);
        return PDF;

    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public Boolean getTieneSolicitudBeca() {
        return tieneSolicitudBeca;
    }

    public void setTieneSolicitudBeca(Boolean tieneSolicitudBeca) {
        this.tieneSolicitudBeca = tieneSolicitudBeca;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public String getNumeroDeBoleta() {
        return numeroDeBoleta;
    }

    public void setNumeroDeBoleta(String numeroDeBoleta) {
        this.numeroDeBoleta = numeroDeBoleta;
    }

    public Boolean getPeriodoActivo() {
        return periodoActivo;
    }

    public void setPeriodoActivo(Boolean periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public List<SolicitudBeca> getSbList() {
        return sbList;
    }

    public void setSbList(List<SolicitudBeca> sbList) {
        this.sbList = sbList;
    }

    public List<SolicitudReconsideracion> getSrList() {
        return srList;
    }

    public void setSrList(List<SolicitudReconsideracion> srList) {
        this.srList = srList;
    }

    public String getCuestionarioId() {
        return cuestionarioId;
    }

    public void setCuestionarioId(String cuestionarioId) {
        this.cuestionarioId = cuestionarioId;
    }

    public List<CuestionarioPreguntaRespuesta> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(List<CuestionarioPreguntaRespuesta> preguntas) {
        this.preguntas = preguntas;
    }

    public List<CuestionarioRespuestas> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<CuestionarioRespuestas> respuestas) {
        this.respuestas = respuestas;
    }

    public BigDecimal getAlumnoId() {
        return alumnoId;
    }

    public void setAlumnoId(BigDecimal alumnoId) {
        this.alumnoId = alumnoId;
    }

    public Date getHoy() {
        return hoy;
    }

    public void setHoy(Date hoy) {
        this.hoy = hoy;
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

    public List<ParametrosPDF> getEsePdfList1() {
        return esePdfList1;
    }

    public void setEsePdfList1(List<ParametrosPDF> esePdfList1) {
        this.esePdfList1 = esePdfList1;
    }

    public List<ParametrosPDF> getEsePdfList2() {
        return esePdfList2;
    }

    public void setEsePdfList2(List<ParametrosPDF> esePdfList2) {
        this.esePdfList2 = esePdfList2;
    }

    public List<ParametrosPDF> getEsePdfList3() {
        return esePdfList3;
    }

    public void setEsePdfList3(List<ParametrosPDF> esePdfList3) {
        this.esePdfList3 = esePdfList3;
    }

    public List<ParametrosPDF> getEsePdfList4() {
        return esePdfList4;
    }

    public void setEsePdfList4(List<ParametrosPDF> esePdfList4) {
        this.esePdfList4 = esePdfList4;
    }

    public List<ParametrosPDF> getEsePdfList5() {
        return esePdfList5;
    }

    public void setEsePdfList5(List<ParametrosPDF> esePdfList5) {
        this.esePdfList5 = esePdfList5;
    }

    public List<CuestionarioRespuestasUsuario> getRespuestasUsuario() {
        return respuestasUsuario;
    }

    public void setRespuestasUsuario(List<CuestionarioRespuestasUsuario> respuestasUsuario) {
        this.respuestasUsuario = respuestasUsuario;
    }

    public EstatusReconsideracion getEstatusReconsideracion() {
        return estatusReconsideracion;
    }

    public void setEstatusReconsideracion(EstatusReconsideracion estatusReconsideracion) {
        this.estatusReconsideracion = estatusReconsideracion;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String gethResultdos() {
        return hResultdos;
    }

    public void sethResultdos(String hResultdos) {
        this.hResultdos = hResultdos;
    }

    public SolicitudReconsideracion getSolicitudReconsideracion() {
        return solicitudReconsideracion;
    }

    public void setSolicitudReconsideracion(SolicitudReconsideracion solicitudReconsideracion) {
        this.solicitudReconsideracion = solicitudReconsideracion;
    }

    public Boolean getCuestionarioDiv() {
        return cuestionarioDiv;
    }

    public void setCuestionarioDiv(Boolean cuestionarioDiv) {
        this.cuestionarioDiv = cuestionarioDiv;
    }

    public Boolean getPdfDiv() {
        return pdfDiv;
    }

    public void setPdfDiv(Boolean pdfDiv) {
        this.pdfDiv = pdfDiv;
    }

    public String getSolicitudReconsideracionId() {
        return solicitudReconsideracionId;
    }

    public void setSolicitudReconsideracionId(String solicitudReconsideracionId) {
        this.solicitudReconsideracionId = solicitudReconsideracionId;
    }
    

}
