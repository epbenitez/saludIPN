package com.becasipn.actions.alumno;

import com.becasipn.actions.BaseReportAction;
import com.becasipn.business.AlumnoBO;
import com.becasipn.business.DocumentosBO;
import com.becasipn.business.PeriodoBO;
import com.becasipn.domain.Egresos;
import com.becasipn.domain.IngresosEgresos;
import com.becasipn.domain.ParametrosPDF;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.Otorgamiento;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.SolicitudBeca;
import com.becasipn.persistence.model.TipoBecaPeriodo;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Tania G. Sánchez
 */
public class AdministraDocumentosAction extends BaseReportAction implements MensajesAlumno {

    public static final String VER = "ver";
    public static final String CARTA = "carta";
    public static final String CARTAMENOR = "cartaMenor";
    public static final String COMPROBANTE = "comprobante";
    public static final String CARTABECALOS = "cartaBecalos";
    public static final String CARTANOCOMPROBABLES = "cartaNoComprobables";
    public static final String CARTADERECHOSYOBLIGACIONES = "cartaDerechosyObligaciones";
    public static final String CARTARENUNCIA = "cartaRenuncia";

    private String fecha;
    private Alumno alumno;
    private String beca = "";
    private Date hoy = new Date();
    private List<Alumno> listAlumno = new ArrayList<>();
    private BigDecimal alumnoId;
    private List<ParametrosPDF> tabla1 = new ArrayList<>();
    private List<ParametrosPDF> tabla2 = new ArrayList<>();
    private List<ParametrosPDF> derechosObligaciones = new ArrayList<>();
    private List<ParametrosPDF> renuncia = new ArrayList<>();
    private Boolean tieneSolicitudTransporte = Boolean.FALSE;
    private Boolean tieneSolicitudOrdinaria = Boolean.FALSE;
    private IngresosEgresos ie = new IngresosEgresos();

    public String ver() {
        String numeroDeBoleta = SecurityContextHolder.getContext().getAuthentication().getName();
        alumno = getDaos().getAlumnoDao().findByBoleta(numeroDeBoleta);
        PeriodoBO boP = new PeriodoBO(getDaos());
        Periodo periodo = boP.getPeriodoActivo();

        DocumentosBO bo = new DocumentosBO(getDaos());
        ie = bo.datosEgresos(alumno, periodo);

        return VER;
    }

    public String cartaCompromiso() {
        //Fecha
        SimpleDateFormat formateador = new SimpleDateFormat("dd MMMM yyyy", new Locale("es"));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", new Locale("es"));
        Date fechaDate = new Date();
        fecha = formateador.format(fechaDate);
        fecha = fecha.toUpperCase();
        String fechaImpresion = sdf.format(fechaDate);
        //Alumno
        if (isAlumno()) {
            String numeroDeBoleta = SecurityContextHolder.getContext().getAuthentication().getName();
            alumno = getDaos().getAlumnoDao().findByBoleta(numeroDeBoleta);
        } else {
            alumno = getDaos().getAlumnoDao().findById(alumnoId);
        }
        if (alumno.getNombre() == null) {
            alumno.setNombre("");
        } else {
            alumno.setNombre(alumno.getNombre().replace("'", "\\\'"));
        }
        if (alumno.getApellidoPaterno() == null) {
            alumno.setApellidoPaterno("");
        } else {
            alumno.setApellidoPaterno(alumno.getApellidoPaterno().replace("'", "\\\'"));
        }
        if (alumno.getApellidoMaterno() == null) {
            alumno.setApellidoMaterno("");
        } else {
            alumno.setApellidoMaterno(alumno.getApellidoMaterno().replace("'", "\\\'"));
        }
        DatosAcademicos datosAcademicos = getDaos().getDatosAcademicosDao().ultimosDatos(alumno.getId());
        String parrafoAlumno = "EL QUE SUSCRIBE " + alumno.getFullName() + " ALUMNO/A DEL I.P.N. CON NÚMERO DE BOLETA " + alumno.getBoleta()
                + " INSCRITO/A EN " + datosAcademicos.getUnidadAcademica().getNombre() + " EN LA CARRERA " + datosAcademicos.getCarrera().getCarrera() + ".";
        getParametros().put("imagenPlaca", "/com/becasipn/jasper/placasep.JPG");
        getParametros().put("fecha", fecha);
        getParametros().put("parrafoAlumno", parrafoAlumno);
        getParametros().put("fechaImpresion", fechaImpresion);
        getParametros().put("nombreAlumno", alumno.getFullName());
        //Se condiciona el archivo según la edad dado que a los menores de edad se les pedira la firma de padre, madre o tutor.
        AlumnoBO bo = new AlumnoBO(getDaos());
        if (bo.calcularEdad(alumno.getFechaDeNacimiento()) < 18) {
            return CARTAMENOR;
        } else {
            return CARTA;
        }
    }

    public String comprobante() {
        //Fecha
        SimpleDateFormat formateador = new SimpleDateFormat("dd MMMM yyyy", new Locale("es"));
        Date fechaDate = new Date();
        fecha = formateador.format(fechaDate);
        fecha = fecha.toUpperCase();
        //Alumno
        String numeroDeBoleta = SecurityContextHolder.getContext().getAuthentication().getName();
        listAlumno = getDaos().getAlumnoDao().findBy("boleta", numeroDeBoleta, Boolean.TRUE);
        alumno = listAlumno.get(0);
        if (alumno.getNombre() != null) {
            alumno.setNombre(alumno.getNombre().replace("'", "\\\'"));
        }
        if (alumno.getApellidoPaterno() != null) {
            alumno.setApellidoPaterno(alumno.getApellidoPaterno().replace("'", "\\\'"));
        }
        if (alumno.getApellidoMaterno() != null) {
            alumno.setApellidoMaterno(alumno.getApellidoMaterno().replace("'", "\\\'"));
        } else {
            alumno.setApellidoMaterno("");
        }
        //Periodo
        PeriodoBO bo = new PeriodoBO(getDaos());
        Periodo periodo = bo.getPeriodoActivo();
        DatosAcademicos datosAcademicos = getDaos().getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), periodo.getId());
        alumno.setDatosAcademicos(datosAcademicos);
        //Beca
        Otorgamiento otorgamiento = getDaos().getOtorgamientoDao().getOtorgamientoAlumno(alumno.getId(), periodo.getId());
        if (otorgamiento != null) {
            beca = otorgamiento.getTipoBecaPeriodo().getTipoBeca().getBeca().getNombre().toUpperCase();
        }
        ParametrosPDF parametrosPDF = new ParametrosPDF();
        parametrosPDF.setParametro1(alumno.getApellidoPaterno());
        parametrosPDF.setParametro2(alumno.getApellidoMaterno());
        parametrosPDF.setParametro3(alumno.getNombre());
        tabla1.add(parametrosPDF);
        parametrosPDF = new ParametrosPDF();
        parametrosPDF.setParametro1(alumno.getDatosAcademicos().getUnidadAcademica().getNombre());
        parametrosPDF.setParametro2(alumno.getTelefonoCasa() == null ? "-" : alumno.getTelefonoCasa());
        parametrosPDF.setParametro3(alumno.getCelular());
        tabla2.add(parametrosPDF);
        //Parametros del reporte
        getParametros().put("logoIPN", "/com/becasipn/jasper/logoipn.jpg");
        getParametros().put("logoDSE", "/com/becasipn/jasper/logodse.png");
        getParametros().put("fecha", fecha);
        getParametros().put("beca", beca);
        JRBeanCollectionDataSource s1 = new JRBeanCollectionDataSource(tabla1);
        JRBeanCollectionDataSource s2 = new JRBeanCollectionDataSource(tabla2);
        getParametros().put("nombre", s1);
        getParametros().put("datosAlumno", s2);
        getParametros().put("nombreCompleto", alumno.getApellidoPaterno() + " " + alumno.getApellidoMaterno() + " " + alumno.getNombre());
        return COMPROBANTE;
    }

    public String cartaBecalos() {
        Periodo periodo = getDaos().getPeriodoDao().getPeriodoActivo();
        String numeroDeBoleta = SecurityContextHolder.getContext().getAuthentication().getName();
        listAlumno = getDaos().getAlumnoDao().findBy("boleta", numeroDeBoleta, Boolean.TRUE);
        alumno = listAlumno.get(0);
        DatosAcademicos datosAcademicos = getDaos().getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), periodo.getId());
        SolicitudBeca sb = getDaos().getSolicitudBecaDao().getESEAlumno(alumno.getId(), periodo.getId());
        ParametrosPDF parametrosPDF = new ParametrosPDF();
        parametrosPDF.setParametro1("Agosto");
        parametrosPDF.setParametro2("Septiembre");
        parametrosPDF.setParametro3("Octubre");
        parametrosPDF.setParametro4("Noviembre");
        parametrosPDF.setParametro5("Diciembre");
        parametrosPDF.setParametro6("Enero");
        tabla1.add(parametrosPDF);
        parametrosPDF = new ParametrosPDF();
        parametrosPDF.setParametro1("");
        parametrosPDF.setParametro2("");
        parametrosPDF.setParametro3("");
        parametrosPDF.setParametro4("");
        parametrosPDF.setParametro5("");
        parametrosPDF.setParametro6("");
        tabla1.add(parametrosPDF);
        parametrosPDF = new ParametrosPDF();
        parametrosPDF.setParametro1("Febrero");
        parametrosPDF.setParametro2("Marzo");
        parametrosPDF.setParametro3("Abril");
        parametrosPDF.setParametro4("Mayo");
        parametrosPDF.setParametro5("Junio");
        parametrosPDF.setParametro6("Julio");
        tabla1.add(parametrosPDF);
        parametrosPDF = new ParametrosPDF();
        parametrosPDF.setParametro1("");
        parametrosPDF.setParametro2("");
        parametrosPDF.setParametro3("");
        parametrosPDF.setParametro4("");
        parametrosPDF.setParametro5("");
        parametrosPDF.setParametro6("");
        tabla1.add(parametrosPDF);
        getParametros().put("imagenPlaca", "/com/becasipn/jasper/placasep.JPG");
        getParametros().put("nombreCompleto", alumno.getApellidoPaterno() + " " + alumno.getApellidoMaterno() + " " + alumno.getNombre());
        getParametros().put("beca", sb == null ? "__________________________" : sb.getTipoBecaPreasignada() == null ? "__________________________"
                : sb.getTipoBecaPreasignada().getTipoBeca().getBeca().getId().equals(new BigDecimal("4")) ? sb.getTipoBecaPreasignada().getTipoBeca().getNombre() : "__________________________");
        getParametros().put("nivel", datosAcademicos == null ? "" : datosAcademicos.getUnidadAcademica() == null ? "" : datosAcademicos.getUnidadAcademica().getNivel().getNombre());
        getParametros().put("curp", alumno.getCurp());
        JRBeanCollectionDataSource s1 = new JRBeanCollectionDataSource(tabla1);
        getParametros().put("meses", s1);
        return CARTABECALOS;
    }

    public String cartaNoComprobables() {
        Periodo periodo = getDaos().getPeriodoDao().getPeriodoActivo();
        Date hoy = new Date();
        SimpleDateFormat dia = new SimpleDateFormat("dd", new Locale("es"));
        SimpleDateFormat mes = new SimpleDateFormat("MMMM", new Locale("es"));
        SimpleDateFormat año = new SimpleDateFormat("yyyy", new Locale("es"));
        NumberFormat f1 = new DecimalFormat("0000000");
        String fechaDia = dia.format(hoy);
        String fechaMes = mes.format(hoy);
        String fechaAño = año.format(hoy);
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        String hr = sdf.format(hoy);
        Integer h = Integer.parseInt(hr);
        String hexa = Integer.toHexString(h);
        String numeroDeBoleta = SecurityContextHolder.getContext().getAuthentication().getName();
        listAlumno = getDaos().getAlumnoDao().findBy("boleta", numeroDeBoleta, Boolean.TRUE);
        alumno = listAlumno.get(0);
        String folio = "CARTAINGRESOS" + periodo.getId().toString() + f1.format(alumno.getId()) + "S" + hexa.toUpperCase();
        getParametros().put("fecha", "Ciudad de México a " + fechaDia + " de " + fechaMes + " de " + fechaAño + ".");
        getParametros().put("boleta", alumno.getBoleta());
        getParametros().put("nombreCompleto", alumno.getApellidoPaterno() + " " + alumno.getApellidoMaterno() + " " + alumno.getNombre());
        getParametros().put("folio", folio);
        return CARTANOCOMPROBABLES;
    }

    public String cartaDerechosyObligaciones() {
        Periodo periodo = getDaos().getPeriodoDao().getPeriodoActivo();
        Date hoy = new Date();
        SimpleDateFormat dia = new SimpleDateFormat("dd", new Locale("es"));
        SimpleDateFormat mes = new SimpleDateFormat("MMMM", new Locale("es"));
        SimpleDateFormat año = new SimpleDateFormat("yyyy", new Locale("es"));
        NumberFormat f1 = new DecimalFormat("0000000");
        String fechaDia = dia.format(hoy);
        String fechaMes = mes.format(hoy);
        String fechaAño = año.format(hoy);
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        String hr = sdf.format(hoy);
        Integer h = Integer.parseInt(hr);
        String hexa = Integer.toHexString(h);
        String numeroDeBoleta = SecurityContextHolder.getContext().getAuthentication().getName();
        listAlumno = getDaos().getAlumnoDao().findBy("boleta", numeroDeBoleta, Boolean.TRUE);
        alumno = listAlumno.get(0);
        //DatosAcademicos datosAcademicos = getDaos().getDatosAcademicosDao().ultimosDatos(alumno.getId());
        TipoBecaPeriodo tbp = getDaos().getTipoBecaPeriodoDao().becaAlumnoPeriodoActual(periodo, alumno.getId());
        String folio = "CARTADERECHOSYOBLIGACIONES" + periodo.getId().toString() + f1.format(alumno.getId()) + "S" + hexa.toUpperCase();
        getParametros().put("fecha", "Ciudad de México a " + fechaDia + " de " + fechaMes + " de " + fechaAño + ".");
        getParametros().put("cicloEscolar", periodo.getClave());
        getParametros().put("organizacion", "WAYNE ENTERPRISES");
        getParametros().put("nivel", tbp.getNivel().getNombre());
        getParametros().put("promedio", Float.toString(tbp.getPromedioMinimo()));
        getParametros().put("estado", alumno.getEntidadDeNacimiento().getNombre());
        getParametros().put("curp", alumno.getCurp());
        getParametros().put("nombreAlumno", alumno.getApellidoPaterno() + " " + alumno.getApellidoMaterno() + " " + alumno.getNombre());
        getParametros().put("imagenPlaca", "/com/becasipn/jasper/placasep.JPG");
        getParametros().put("folio", folio);
        return CARTADERECHOSYOBLIGACIONES;
    }

    public String cartaRenuncia() {
        Periodo periodo = getDaos().getPeriodoDao().getPeriodoActivo();
        Date hoy = new Date();
        SimpleDateFormat dia = new SimpleDateFormat("dd", new Locale("es"));
        SimpleDateFormat mes = new SimpleDateFormat("MMMM", new Locale("es"));
        SimpleDateFormat año = new SimpleDateFormat("yyyy", new Locale("es"));
        NumberFormat f1 = new DecimalFormat("0000000");
        String fechaDia = dia.format(hoy);
        String fechaMes = mes.format(hoy);
        String fechaAño = año.format(hoy);
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        String hr = sdf.format(hoy);
        Integer h = Integer.parseInt(hr);
        String hexa = Integer.toHexString(h);
        String numeroDeBoleta = SecurityContextHolder.getContext().getAuthentication().getName();
        listAlumno = getDaos().getAlumnoDao().findBy("boleta", numeroDeBoleta, Boolean.TRUE);
        alumno = listAlumno.get(0);
        DatosAcademicos datosAcademicos = getDaos().getDatosAcademicosDao().ultimosDatos(alumno.getId());
        //TipoBecaPeriodo tbp = getDaos().getTipoBecaPeriodoDao().becaAlumnoPeriodoActual(periodo, alumno.getId());
        String folio = "CARTARENUNCIA " + periodo.getId().toString() + f1.format(alumno.getId()) + "S" + hexa.toUpperCase();
        getParametros().put("fecha", "Ciudad de México a " + fechaDia + " de " + fechaMes + " de " + fechaAño + ".");
        getParametros().put("periodo", periodo.getClave());
        getParametros().put("nombreAlumno", alumno.getApellidoPaterno() + " " + alumno.getApellidoMaterno() + " " + alumno.getNombre());
        getParametros().put("boleta", alumno.getBoleta());
        getParametros().put("ua", datosAcademicos.getUnidadAcademica().getNombreCorto());
        getParametros().put("semestre", datosAcademicos.getSemestre().toString());
        getParametros().put("regular", datosAcademicos.getRegular() == 1 ? "Regular" : "Irregular");
        getParametros().put("pleca", "/com/becasipn/jasper/placasep.JPG");
        getParametros().put("folio", folio);
        return CARTARENUNCIA;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public String getBeca() {
        return beca;
    }

    public void setBeca(String beca) {
        this.beca = beca;
    }

    public Date getHoy() {
        return hoy;
    }

    public void setHoy(Date hoy) {
        this.hoy = hoy;
    }

    public List<Alumno> getListAlumno() {
        return listAlumno;
    }

    public void setListAlumno(List<Alumno> listAlumno) {
        this.listAlumno = listAlumno;
    }

    public BigDecimal getAlumnoId() {
        return alumnoId;
    }

    public void setAlumnoId(BigDecimal alumnoId) {
        this.alumnoId = alumnoId;
    }

    public List<ParametrosPDF> getTabla1() {
        return tabla1;
    }

    public void setTabla1(List<ParametrosPDF> tabla1) {
        this.tabla1 = tabla1;
    }

    public List<ParametrosPDF> getTabla2() {
        return tabla2;
    }

    public void setTabla2(List<ParametrosPDF> tabla2) {
        this.tabla2 = tabla2;
    }

    public IngresosEgresos getIe() {
        return ie;
    }

    public void setIe(IngresosEgresos ie) {
        this.ie = ie;
    }

    public List<ParametrosPDF> getDerechosObligaciones() {
        return derechosObligaciones;
    }

    public void setDerechosObligaciones(List<ParametrosPDF> derechosObligaciones) {
        this.derechosObligaciones = derechosObligaciones;
    }

    public List<ParametrosPDF> getRenuncia() {
        return renuncia;
    }

    public void setRenuncia(List<ParametrosPDF> renuncia) {
        this.renuncia = renuncia;
    }

    public Boolean getTieneSolicitudTransporte() {
        return tieneSolicitudTransporte;
    }

    public void setTieneSolicitudTransporte(Boolean tieneSolicitudTransporte) {
        this.tieneSolicitudTransporte = tieneSolicitudTransporte;
    }

    public Boolean getTieneSolicitudOrdinaria() {
        return tieneSolicitudOrdinaria;
    }

    public void setTieneSolicitudOrdinaria(Boolean tieneSolicitudOrdinaria) {
        this.tieneSolicitudOrdinaria = tieneSolicitudOrdinaria;
    }

}
