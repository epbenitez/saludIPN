package com.becasipn.actions.alumno;

import com.becasipn.actions.BaseReportAction;
import com.becasipn.business.AlumnoBO;
import com.becasipn.business.DepositosBO;
import com.becasipn.business.OtorgamientoBO;
import com.becasipn.business.PanelDeControlBO;
import com.becasipn.business.PeriodoBO;
import com.becasipn.business.TarjetaBO;
import com.becasipn.domain.ParametrosPDF;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.AlumnoDatosBancarios;
import com.becasipn.persistence.model.AlumnoTarjetaBancaria;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.Depositos;
import com.becasipn.persistence.model.EstatusTarjetaBancaria;
import com.becasipn.persistence.model.Notificacion;
import com.becasipn.persistence.model.Otorgamiento;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.util.Util;
import com.opensymphony.xwork2.ActionContext;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Mario Márquez
 */
public class PanelControlAction extends BaseReportAction {

    public static final String PDF = "pdf";

    private final OtorgamientoBO otorgamientoBO;
    private final TarjetaBO tarjetaBO;
    private final AlumnoBO alumnoBO;

    private AlumnoTarjetaBancaria tarjetaBancaria;
    private List<Notificacion> notificaciones;
    private Otorgamiento ultimoOtorgamiento;
    private List<Depositos> depositos;
    private List<List<String>> solicitudList;
    private Boolean hasDescription = true;
    private Boolean isAccount = false;
    private Boolean isItTime = fechaDatos();
    private String mainMsg;
    private String secondMsg;
    private String thirdMsg;
    private String thirdMsgLink;
    private String referencePeriod = "";
    private String refUniversalURL = "";
    private String accountDateMsj;
    private Integer stsId;
    private List<ParametrosPDF> deposito = new ArrayList<>();
    private Boolean mostrarReferencia = Boolean.FALSE;
    private Boolean mostrarReferenciaU = Boolean.FALSE;
    private Boolean mostrarMsjReferenciaUMS = Boolean.FALSE;
    private Boolean hasOtorgamientoPeriodoActual = false;
    //Variable para validar fecha final para cuentas de alta bancaria
    private Boolean validatorFinalAltaCtaBancaria = Boolean.FALSE;
    private boolean errorDeposit;
    // Sección beca
    private String textoPrincipalBeca;
    private String textoSecundarioBeca;
    private String textoEnlaceBeca;
    private String enlaceBeca;

    public PanelControlAction() {
        otorgamientoBO = new OtorgamientoBO(getDaos());
        alumnoBO = new AlumnoBO(getDaos());
        tarjetaBO = new TarjetaBO(getDaos());
    }

    public String mostrar() throws ParseException {
        Alumno alumno = alumnoBO.getAlumno(SecurityContextHolder.getContext().getAuthentication().getName());
        Usuario u = (Usuario) ActionContext.getContext().getSession().get("usuario");

        // mostrarReferencia = getDaos().getDepositosDao().tieneDepositosReferenciados(alumno.getId());
        mostrarReferenciaU = getDaos().getBecaUniversalDao().existeEnPadron(alumno.getBoleta());
        refUniversalURL = "/misdatos/descargar/inicioBecaUniversal.action";

        if (mostrarReferencia) {
            List<LinkedHashMap<String, Object>> depositosRef = getDaos().getDepositosDao().depositosReferenciados(alumno.getId());
            referencePeriod = depositosRef.get(0).get("p.clave").toString();
        }

        //Último objeto tipo tarjeta asignado.
        tarjetaBancaria = tarjetaBO.obtenerTarjetaV2(u.getId());
        // ¿Es una cuenta?
        EstatusTarjetaBancaria statusCC = tarjetaBancaria != null ? tarjetaBancaria.getEstatusTarjBanc() : null;

        // ¿La tarjeta/cuenta tiene estatus?
        Boolean hasSts = statusCC != null;
        stsId = hasSts ? statusCC.getId().intValue() : -1;
        Boolean relevantSts = stsId >= 12;

        //Depositos
        depositos = alumnoBO.getUltimosDepositos(alumno.getId());
        // Decide si hay depósitos con rechazo (id 4)
        errorDeposit = DepositosBO.hasRechazados(depositos);

        // ¿El estatus necesita ir acompañado de información específica?
        if (relevantSts) {
            isAccount = true;
            if (stsId == 13) {
                // Para saber si es cuenta o clabe
                Integer cuenta = tarjetaBancaria.getTarjetaBancaria().getCuenta();
                // Bandera: cuenta o no cuenta con descripción
                hasDescription = false;
                // Mensaje Grande
                mainMsg = tarjetaBancaria.getTarjetaBancaria().getNumtarjetabancaria();
                if (cuenta == 1) {
                    // Subtítulo
                    secondMsg = statusCC.getObservaciones();
                } else {
                    secondMsg = "CLABE interbancaria";
                }
                //Muro
                notificaciones = alumnoBO.obtenerNotificaciones(alumno, depositos);
            } else {
                // Mensaje Grande
                mainMsg = statusCC.getDescripcion();
                // Subtítulo
                secondMsg = statusCC.getResumen();
                //Muro Vacío
                notificaciones = alumnoBO.obtenerNotificaciones(alumno, depositos);

            }
            // Mensaje para el enlace
            thirdMsg = crearTextoEnlace(stsId);
            // Enlace ver más
            thirdMsgLink = crearUrlEnlace(stsId);
        } else {
            // Mensaje Grande
            mainMsg = "Sin cuenta bancaria";
            // Subtítulo
            secondMsg = "No tienes una cuenta bancaria";
            notificaciones = alumnoBO.obtenerNotificaciones(alumno, depositos);
            // Mensaje para el enlace
            thirdMsg = crearTextoEnlace(stsId);
            // Enlace ver más
            thirdMsgLink = crearUrlEnlace(stsId);
        }

        // Para mostrar en la tabla únicamente 2
        depositos = DepositosBO.getUltimosAplicados(depositos);

        //Ultima beca asignada        
        ultimoOtorgamiento = otorgamientoBO.getUltimoOtorgamiento(alumno, isItTime);

        PanelDeControlBO panelDeControlBO = PanelDeControlBO.getInstance(getDaos(), alumno, ultimoOtorgamiento);
        panelDeControlBO.setSeccionBeca(stsId);
        textoPrincipalBeca = panelDeControlBO.getTextoPrincipalBeca();
        textoSecundarioBeca = panelDeControlBO.getTextoSecundarioBeca();
        textoEnlaceBeca = panelDeControlBO.getTextoEnlaceBeca();
        enlaceBeca = panelDeControlBO.getEnlaceBeca();
        hasOtorgamientoPeriodoActual = panelDeControlBO.isHasOtorgamientoPeriodoActual();
        
        solicitudList = panelDeControlBO.getInfoSolicitudes();

        // ---------------------- Validar que la fecha actual este entre la fecha inicio y la fecha final para la alta de Cuentas Bancarias (Aviso) ------------------------
        //Variables para almacenar la Fecha Final e Inicial para cuentas de alta Bancaria.
        String FechaInicialAltaCtaBancaria = (String) ActionContext.getContext().getApplication().get("fechaInicioAltaCuentasBancarias ");
        String FechaFinalAltaCtaBancaria = (String) ActionContext.getContext().getApplication().get("fechaFinalAltaCuentasBancarias  ");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date dateBancariaInicial = formatter.parse(FechaInicialAltaCtaBancaria);
        Date dateBancariaFinal = formatter.parse(FechaFinalAltaCtaBancaria);
        Calendar ca = Calendar.getInstance();
        ca.setTime(dateBancariaFinal);
        ca.add(Calendar.DATE, 1);
        dateBancariaFinal = ca.getTime();
        Date hoy = new Date();

        // Compara la fecha actual con la Fecha Final para cuentas de alta Bancaria, para validar que sigue en un periodo válido.
        if (hoy.after(dateBancariaInicial) && hoy.before(dateBancariaFinal)) {
            validatorFinalAltaCtaBancaria = Boolean.TRUE;
            Map datesMap = PanelDeControlBO.creaModelo(dateBancariaInicial, dateBancariaFinal);
            accountDateMsj = panelDeControlBO.creaTexto("velocity/PanelControl/mensajeCuentas.vm", datesMap);
        }

        return "succes";
    }

    private String crearTextoEnlace(int stsId) {
        switch (stsId) {
            case 14:
                return "Para corregir esto, da clic aquí ";
            case 16:
                return "Para actualizarla, da clic aquí ";
            default:
                return "Para más información sobre tu cuenta, da clic aquí ";
        }
    }

    private String crearUrlEnlace(int stsId) {
        switch (stsId) {
            case 14:
                return "/misdatos/datosRegistro.action?wrongDataB=true";
            case 16:
                return "/misdatos/configuracionCuentaBancaria.action";
            default:
                return "/tarjeta/verMonitoreoTarjetaBancaria.action";
        }
    }

    public String pdf() {
        PeriodoBO bo = new PeriodoBO(getDaos());
        Periodo periodo = bo.getPeriodoActivo();
        Periodo periodoDepo = new Periodo();
        Alumno alumno = alumnoBO.getAlumno(SecurityContextHolder.getContext().getAuthentication().getName());
        DatosAcademicos da = getDaos().getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), getDaos().getPeriodoDao().getPeriodoActivo().getId());
        alumno.setDatosAcademicos(da);
        AlumnoDatosBancarios datosBancarios = getDaos().getAlumnoDatosBancariosDao().datosBancarios(alumno.getId());
        String tutor = "Sin registro";
        if (datosBancarios.getNombre() != null && datosBancarios.getApellidoPaterno() != null) {
            tutor = datosBancarios.getApellidoPaterno() + " " + datosBancarios.getApellidoMaterno() + " " + datosBancarios.getNombre();
        }
        List<LinkedHashMap<String, Object>> ob = getDaos().getDepositosDao().depositosReferenciados(alumno.getId());
        ParametrosPDF parametros = new ParametrosPDF();
        //La primera fila se agrega en blanco porque se pierde al imprimir el archivo.
        parametros.setParametro1("");
        parametros.setParametro2("");
        parametros.setParametro3("");
        deposito.add(parametros);
        DecimalFormat df = new DecimalFormat("0.00");
        float total = 0;
        for (Map<String, Object> map : ob) {
            parametros = new ParametrosPDF();
            parametros.setParametro1(nombreMes(Integer.parseInt(map.get("od.mes").toString())));
            parametros.setParametro2(map.get("ctb.nombre").toString());
            parametros.setParametro3("$" + df.format(map.get("d.monto")));
            deposito.add(parametros);
            total = total + Float.parseFloat(map.get("d.monto").toString());
        }
        parametros = new ParametrosPDF();
        parametros.setParametro1("");
        parametros.setParametro2("Total");
        parametros.setParametro3("$" + df.format(total));
        deposito.add(parametros);
        NumberFormat nf = new DecimalFormat("000000000000");
        String folio = "IPN" + nf.format(new Long(ob.get(0).get("tb.numtarjetabancaria").toString()));
        getParametros().put("imagenPlaca", "/com/becasipn/jasper/placasep.JPG");
        getParametros().put("unidadAcademica", alumno.getDatosAcademicos() == null ? "" : alumno.getDatosAcademicos().getUnidadAcademica().getNombre());
        getParametros().put("nombre", alumno.getFullName());
        getParametros().put("boleta", alumno.getBoleta());
        getParametros().put("curp", alumno.getCurp());
        getParametros().put("tutor", tutor);
        getParametros().put("periodo", "2017-2");
        getParametros().put("folio", folio);
        return PDF;
    }

    private String nombreMes(Integer mes) {
        switch (mes) {
            case 1:
                return "Enero";
            case 2:
                return "Febrero";
            case 3:
                return "Marzo";
            case 4:
                return "Abril";
            case 5:
                return "Mayo";
            case 6:
                return "Junio";
            case 7:
                return "Julio";
            case 8:
                return "Agosto";
            case 9:
                return "Septiembre";
            case 10:
                return "Octubre";
            case 11:
                return "Noviembre";
            case 12:
                return "Diciembre";
            default:
                return "" + mes;
        }
    }

    public Boolean fechaDatos() {
        return Util.fechaDatos((String) ActionContext.getContext().getApplication().get("PublicacionResultadosOtorgamientos"));
    }

    public AlumnoTarjetaBancaria getTarjetaBancaria() {
        return tarjetaBancaria;
    }

    public void setTarjetaBancaria(AlumnoTarjetaBancaria tarjetaBancaria) {
        this.tarjetaBancaria = tarjetaBancaria;
    }

    public Otorgamiento getUltimoOtorgamiento() {
        return ultimoOtorgamiento;
    }

    public void setUltimoOtorgamiento(Otorgamiento ultimoOtorgamiento) {
        this.ultimoOtorgamiento = ultimoOtorgamiento;
    }

    public List<Depositos> getDepositos() {
        return depositos;
    }

    public void setDepositos(List<Depositos> depositos) {
        this.depositos = depositos;
    }

    public List<Notificacion> getNotificaciones() {
        return notificaciones;
    }

    public void setNotificaciones(List<Notificacion> notificaciones) {
        this.notificaciones = notificaciones;
    }

    public void setHasDescription(Boolean hasDescription) {
        this.hasDescription = hasDescription;
    }

    public Boolean getHasDescription() {
        return hasDescription;
    }

    public void setIsAccount(Boolean isAccount) {
        this.isAccount = isAccount;
    }

    public Boolean getIsAccount() {
        return isAccount;
    }

    public void setMainMsg(String mainMsg) {
        this.mainMsg = mainMsg;
    }

    public String getMainMsg() {
        return mainMsg;
    }

    public void setSecondMsg(String secondMsg) {
        this.secondMsg = secondMsg;
    }

    public String getSecondMsg() {
        return secondMsg;
    }

    public void setThirdMsg(String thirdMsg) {
        this.thirdMsg = thirdMsg;
    }

    public String getThirdMsg() {
        return thirdMsg;
    }

    public void setThirdMsgLink(String thirdMsgLink) {
        this.thirdMsgLink = thirdMsgLink;
    }

    public String getThirdMsgLink() {
        return thirdMsgLink;
    }

    public void setStsId(Integer stsId) {
        this.stsId = stsId;
    }

    public Integer getStsId() {
        return stsId;
    }

    public List<ParametrosPDF> getDeposito() {
        return deposito;
    }

    public void setDeposito(List<ParametrosPDF> deposito) {
        this.deposito = deposito;
    }

    public Boolean getMostrarReferencia() {
        return mostrarReferencia;
    }

    public void setMostrarReferencia(Boolean mostrarReferencia) {
        this.mostrarReferencia = mostrarReferencia;
    }

    public String getReferencePeriod() {
        return referencePeriod;
    }

    public void setReferencePeriod(String referencePeriod) {
        this.referencePeriod = referencePeriod;
    }

    public Boolean getIsItTime() {
        return isItTime;
    }

    public void setIsItTime(Boolean isItTime) {
        this.isItTime = isItTime;
    }

    public Boolean getMostrarReferenciaU() {
        return mostrarReferenciaU;
    }

    public void setMostrarReferenciaU(Boolean mostrarReferenciaU) {
        this.mostrarReferenciaU = mostrarReferenciaU;
    }

    public String getRefUniversalURL() {
        return refUniversalURL;
    }

    public void setRefUniversalURL(String refUniversalURL) {
        this.refUniversalURL = refUniversalURL;
    }

    public Boolean getMostrarMsjReferenciaUMS() {
        return mostrarMsjReferenciaUMS;
    }

    public void setMostrarMsjReferenciaUMS(Boolean mostrarMsjReferenciaUMS) {
        this.mostrarMsjReferenciaUMS = mostrarMsjReferenciaUMS;
    }

    public Boolean getHasOtorgamientoPeriodoActual() {
        return hasOtorgamientoPeriodoActual;
    }

    public void setHasOtorgamientoPeriodoActual(Boolean hasOtorgamientoPeriodoActual) {
        this.hasOtorgamientoPeriodoActual = hasOtorgamientoPeriodoActual;
    }

    public Boolean getValidatorFinalAltaCtaBancaria() {
        return validatorFinalAltaCtaBancaria;
    }

    public void setValidatorFinalAltaCtaBancaria(Boolean validatorFinalAltaCtaBancaria) {
        this.validatorFinalAltaCtaBancaria = validatorFinalAltaCtaBancaria;
    }

    public String getAccountDateMsj() {
        return accountDateMsj;
    }

    public void setAccountDateMsj(String accountDateMsj) {
        this.accountDateMsj = accountDateMsj;
    }

    public boolean isErrorDeposit() {
        return errorDeposit;
    }

    public String getTextoPrincipalBeca() {
        return textoPrincipalBeca;
    }

    public String getTextoSecundarioBeca() {
        return textoSecundarioBeca;
    }

    public String getTextoEnlaceBeca() {
        return textoEnlaceBeca;
    }

    public String getEnlaceBeca() {
        return enlaceBeca;
    }

    public List<List<String>> getSolicitudList() {
        return solicitudList;
    }
}
