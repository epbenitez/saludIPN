package com.becasipn.actions.alumno;

import com.becasipn.actions.BaseReportAction;
import com.becasipn.business.AlumnoBO;
import com.becasipn.business.AlumnoTarjetaBancariaBO;
import com.becasipn.business.BecaUniversalBO;
import com.becasipn.business.DepositosBO;
import com.becasipn.business.OtorgamientoBO;
import com.becasipn.business.PeriodoBO;
import com.becasipn.business.TarjetaBO;
import com.becasipn.domain.ParametrosPDF;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.AlumnoDatosBancarios;
import com.becasipn.persistence.model.AlumnoTarjetaBancaria;
import com.becasipn.persistence.model.BecaUniversal;
import com.becasipn.persistence.model.BitacoraTarjetaBancaria;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.Depositos;
import com.becasipn.persistence.model.EstatusDeposito;
import com.becasipn.persistence.model.EstatusTarjetaBancaria;
import com.becasipn.persistence.model.IdentificadorOtorgamiento;
import com.becasipn.persistence.model.Otorgamiento;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.Proceso;
import com.becasipn.persistence.model.SolicitudCuentas;
import com.becasipn.persistence.model.TarjetaBancaria;
import com.becasipn.persistence.model.TipoBecaPeriodo;
import com.becasipn.persistence.model.Usuario;
import com.opensymphony.xwork2.ActionContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Tania G. Sánchez Manilla
 */
public class AdministracionBecaUniversalAction extends BaseReportAction implements MensajesAlumno {

    public static final String PDF = "pdf";
    public static final String ERROR = "error";
    public static final String PDFMS = "pdfMS";
    
    private final AlumnoBO alumnoBO;
    private final Periodo periodoActivo;
    private final Alumno alumno;
    private DatosAcademicos datosA;
    private BecaUniversalBO bUbo;

    private List<ParametrosPDF> deposito = new ArrayList<>();
    private InputStream universalStream;
    private String contentDisposition;

    public AdministracionBecaUniversalAction() {
        alumnoBO = new AlumnoBO(getDaos());
        periodoActivo = getDaos().getPeriodoDao().getPeriodoActivo();
        alumno = alumnoBO.getAlumno(SecurityContextHolder.getContext().getAuthentication().getName());        
        datosA = getDaos().getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), periodoActivo.getId());
    }
    
    private Boolean actualizarDA() {
        if (datosA == null) {
            try {
                alumnoBO.datosDAE(alumno);
                datosA = getDaos().getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), periodoActivo.getId());
                bUbo = new BecaUniversalBO(getDaos(), alumno);
                return true;                
            } catch (Exception ex) {
                Logger.getLogger(AdministracionBecaUniversalAction.class.getName()).log(Level.INFO, null, ex);
                return false;
            }
        } else {
            bUbo = new BecaUniversalBO(getDaos(), alumno);
            return true;            
        }
    }
    
    public String inicio() {
        if (!actualizarDA() || !acciones()) {
            if (datosA == null) {
                addActionError(getText("becaUniversal.error.datosA"));
            }
            return ERROR;
        } else {
            return pdf();
            // Fix 19/12/17 Descomentar esto en caso de volver a necesitar 
            // extraer archivos pre cargados
            // Integer nivel = datosA.getUnidadAcademica().getNivel().getId().intValue();
            // if (nivel == 1) {
                // return pdfMS();
            // } else {
                // return pdf();
            // }
        }
    }

    private String pdfMS() {
        String boleta = alumno.getBoleta();
        BecaUniversal becaUni = getDaos().getBecaUniversalDao().getByBoleta(boleta);
        String fileN = boleta + " " + becaUni.getReferencia() + ".pdf";
        setContentDisposition("attachment; filename=\"" + "pago_beca_universal.pdf\"");
        try {
            // Ruta absoluta
            universalStream = new FileInputStream(new File("C:/Referencias/" + fileN));
            // Ruta relativa
            // universalStream = request.getServletContext().getResourceAsStream("/resources/downloadable/NombreDelArchivo.pdf");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AdministracionBecaUniversalAction.class.getName()).log(Level.WARNING, null, ex);
            addActionError(getText("becaUniversal.error.fileNotFound"));
            return ERROR;
        }

        return PDFMS;
    }

    private String pdf() {
        PeriodoBO bo = new PeriodoBO(getDaos());
//        Periodo periodo = bo.getPeriodoActivo();
        BecaUniversal becaUni = getDaos().getBecaUniversalDao().getByBoleta(alumno.getBoleta());
        alumno.setDatosAcademicos(datosA);
        ParametrosPDF parametros = new ParametrosPDF();
        //La primera fila se agrega en blanco porque se pierde al imprimir el archivo.
        parametros.setParametro1("");
        parametros.setParametro2("");
        parametros.setParametro3("");
        deposito.add(parametros);
        //A partir de esta fila se muestran en el resporte.
        parametros = new ParametrosPDF();
        parametros.setParametro1("Todos los meses");
        parametros.setParametro2("Beca Universal");
        parametros.setParametro3("$1500.00");
        deposito.add(parametros);
        NumberFormat nf = new DecimalFormat("000000000000");
        String folio = becaUni.getReferencia();
        //Datos del reporte jasper.
        getParametros().put("imagenPlaca", "/com/becasipn/jasper/placasep.JPG");
        getParametros().put("unidadAcademica", datosA == null ? "" : datosA.getUnidadAcademica().getNombre());
        getParametros().put("nombre", alumno.getFullName());
        getParametros().put("boleta", alumno.getBoleta());
        getParametros().put("curp", alumno.getCurp());
        getParametros().put("periodo", periodoActivo.getClave());
        getParametros().put("folio", folio);
        return PDF;
    }

    private Boolean acciones() {
        if (bUbo.creaCC() != null) {
            return true;
        } else {            
            Map<String, Boolean> resultados = bUbo.accionesTrasTelon();

            return !mensajesError(resultados);
        }
    }
    
    // Para agregar nuevos mensajes de error, basta con agregar el caso específico 
    // al switch.
    private Boolean mensajesError(Map<String, Boolean> mapa) {
        Iterator it = mapa.keySet().iterator();
        Boolean result = false;
        while (it.hasNext()) {
            String key = (String) it.next();
            // Para cada falla, se revisa si es necesario avisar al usuario del error
            switch (key) {
                case "exitoO":
                    if (!mapa.get(key)) {
                        if (mapa.containsKey("O") && mapa.get("O")) {
                            addActionError(getText("becaUniversal.error.O"));
                        } else if (mapa.containsKey("otroO") && mapa.get("otroO")) {
                            addActionError(getText("becaUniversal.error.otroO"));
                        }
                        result = true;
                    }
                    break;
                case "exitoACC":
                    if (!mapa.get(key)) {
                        if (mapa.containsKey("ACC") && mapa.get("ACC")) {
                            addActionError(getText("becaUniversal.error.ACC"));
                        }
                        result = true;
                    }
                    break;
                case "exitoB":
                    if (!mapa.get(key)) {
                        if (mapa.containsKey("B") && mapa.get("B")) {
                            addActionError(getText("becaUniversal.error.generico"));
                        }
                        result = true;
                    }
                    break;
                case "exitoD":
                    if (!mapa.get(key)) {
                        if (mapa.containsKey("D") && mapa.get("D")) {
                            addActionError(getText("becaUniversal.error.generico"));
                        }
                        result = true;
                    }
                    break;
            }
        }
        return result;
    }

    public InputStream getUniversalStream() {
        return universalStream;
    }

    public void setUniversalStream(InputStream universalStream) {
        this.universalStream = universalStream;
    }

    public String getContentDisposition() {
        return contentDisposition;
    }

    public void setContentDisposition(String contentDisposition) {
        this.contentDisposition = contentDisposition;
    }
    
    public List<ParametrosPDF> getDeposito() {
        return deposito;
    }

    public void setDeposito(List<ParametrosPDF> deposito) {
        this.deposito = deposito;
    }
}
