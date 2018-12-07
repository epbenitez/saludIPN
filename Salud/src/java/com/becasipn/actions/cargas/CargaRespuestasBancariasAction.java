package com.becasipn.actions.cargas;

import com.becasipn.actions.ajax.JSONAjaxAction;
import com.becasipn.business.CargaRespuestasBancariasBO;
import com.becasipn.business.CuentaBO;
import com.becasipn.domain.CampoResumen;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.AlumnoTarjetaBancaria;
import com.becasipn.persistence.model.Depositos;
import com.becasipn.persistence.model.EstatusDeposito;
import com.becasipn.persistence.model.SolicitudCuentas;
import com.becasipn.persistence.model.TarjetaBancaria;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.util.Tupla;
import com.becasipn.util.UtilFile;
import static com.opensymphony.xwork2.Action.INPUT;
import com.opensymphony.xwork2.ActionContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author Patricia Benítez
 */
public class CargaRespuestasBancariasAction extends JSONAjaxAction implements MensajesCargas {

    public final String ESTATUS = "estatus";
    private File upload;
    private String uploadFileName;
    private String uploadContentType;
    private BigDecimal ordenId;
    private String nombreOrden;
    private Integer exitosos = 0;
    private Integer rechazados = 0;
    private Integer noProcesados = 0;
    private Integer correosEnviados = 0;
    private Integer total = 0;
    private Integer tipoCarga;
    private Boolean referencia = false;
    private String periodo;
    List<Tupla<String, String>> errores;
    private List<CampoResumen> listaCampos = new ArrayList<>();
    private List<Depositos> depositosRechazados;

    public String form() {
        return INPUT;
    }

    public String formSolicitud() {
        SolicitudCuentas solicitud = getDaos().getSolicitudCuentasDao().findById(ordenId);
        if (solicitud != null) {
            nombreOrden = solicitud.getIdentificador();
        } else {
            nombreOrden = "General";
        }
        return INPUT;
    }

    public String carga() {
        if (referencia) {
            return referenciaBancaria();
        }
        LOG.info(String.format("%s : fileUpload", getClass().getName()));
        CargaRespuestasBancariasBO bo = new CargaRespuestasBancariasBO(getDaos());
        bo.setOrdenId(ordenId);

        if (getUpload() != null) {
            try {
                List<String> renglones = bo.processFile(upload);
                bo.procesaInfo(renglones);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // En caso de que haya errores
            if (bo.getError()) {
                addActionError(getText("carga.respuesta.bancaria.error") + bo.getMensajeError());
            } else {
                depositosRechazados = bo.getDepositosRechazados();
                exitosos = bo.getExitosos();
                rechazados = bo.getRechazados();
                noProcesados = bo.getNoProcesados();
                correosEnviados = bo.getCorreosEnviados();
                total = bo.getTotal();
            }
        }

        return INPUT;
    }

    public String referenciaBancaria() {
        if (referencia) {
            exitosos = 0;
            rechazados = 0;
            total = 0;
            listaCampos = new ArrayList<>();
            EstatusDeposito estatusDeposito = getDaos().getEstatusDepositoDao().findById(new BigDecimal(2));
            Date fecha;
            LOG.info(String.format("%s : fileUpload", getClass().getName()));
            List<Depositos> depositosLista = new ArrayList<>();
            Set<Depositos> depositosSet = new HashSet<Depositos>();

            if (getUpload() != null) {
                try {
                    List<String> lista = CargaRespuestasBancariasBO.processFile(upload);
                    DateFormat formatter = new SimpleDateFormat("ddMMyy");
                    String ax = lista.get(0);
                    if (ax.length() > 5) {
                        fecha = formatter.parse(ax.substring(0, 6));
                    } else {
                        fecha = formatter.parse("0" + ax.substring(0, 5));
                    }
                    for (int i = 2; i < lista.size(); i++) {
                        String linea = lista.get(i);
                        String[] ref = linea.split("\\s+|IPN");
                        if (ref.length > 1) {
                            if (ref[1].length() < 12) {  //Se asumen líneas que no corresponden al formato de respuesta
                                continue;
                            }
                            TarjetaBancaria t = getDaos().getTarjetaBancariaDao().findReferenciaByNumeroCuenta(ref[1]);
                            if (t != null) {
                                AlumnoTarjetaBancaria atb = getDaos().getAlumnoTarjetaDao().alumnoTarjetaReferencia(t.getId());
                                if (atb != null) {
                                    Alumno a = atb.getAlumno();
                                    List<Depositos> depositosAlumno = getDaos().getDepositosDao().depositosAlumnoReferencia(a.getId(), t.getId(), new BigDecimal(periodo));
                                    if (depositosAlumno != null) {

                                        for (Depositos d : depositosAlumno) {
                                            if (d.getEstatusDeposito().getId().equals(new BigDecimal(1))) { //Sólo si está en estatus 1 (En espera)
                                                exitosos += 1;
                                                d.setEstatusDeposito(estatusDeposito);
                                                d.setFechaDeposito(fecha);
                                                d.setFechaModificacion(new Date());
                                                depositosLista.add(d);
                                                depositosSet.add(d);
                                            } else {
                                                Usuario u = getDaos().getUsuarioDao().findById(d.getUsuarioModifico().getId());
                                                CampoResumen r = new CampoResumen();
                                                r.setNombreCompleto(linea);
                                                r.setStatProceso("El depósito ya tiene cargada una respuesta bancaria del día " + UtilFile.dateToString(d.getFechaModificacion(), "dd/MM/yyyy") + (u == null ? "" : " por el usuario " + u.getUsuario()));
                                                listaCampos.add(r);
                                                rechazados += 1;
                                            }
                                        }
                                    } else {
                                        CampoResumen r = new CampoResumen();
                                        r.setNombreCompleto(linea);
                                        r.setStatProceso("No se encontraron pagos relacionados a esta referencia y periodo.");
                                        listaCampos.add(r);
                                        rechazados += 1;
                                    }
                                } else {
                                    CampoResumen r = new CampoResumen();
                                    r.setNombreCompleto(linea);
                                    r.setStatProceso("No existe ningun alumno ligado a esta referencia de pago.");
                                    listaCampos.add(r);
                                    rechazados += 1;
                                }
                            } else {
                                CampoResumen r = new CampoResumen();
                                r.setNombreCompleto(linea);
                                r.setStatProceso("La referencia de pago no fue encontrada en el sistema.");
                                listaCampos.add(r);
                                rechazados += 1;
                            }
                        }
                    }
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
            } else {
                addActionError("Hay un error con su archivo.");
            }
            if (!depositosLista.isEmpty()) {
                for (Depositos d : depositosSet) {
                    getDaos().getDepositosDao().update(d);
                }
            } else {
                addActionError("No se encontraron coincidencias en su archivo o existen errores que podrá verificar en la lista inferior.");
            }
        } else {
            addActionError("Ocurrio un error.");
        }
        total = exitosos + rechazados;
        if (exitosos > 0) {
            addActionMessage("Se han actualizado los depositos de los alumnos.");
        }
        return INPUT;
    }

    public String cargaSolicitudesCuentas() {
        CuentaBO bo = new CuentaBO(getDaos());
        SolicitudCuentas sc = null;
        if (ordenId != null) {
            sc = getDaos().getSolicitudCuentasDao().findById(ordenId);
        }

        Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        if (getUpload() != null) {

            if ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equalsIgnoreCase(getUploadContentType())) {

                try {
                    Workbook wb = bo.readXlsxFile(new FileInputStream(getUpload()));
                    if (tipoCarga == 2) {
                        errores = bo.processFile(wb, sc);
                        return ESTATUS;
                    } else {
                        Tupla<Integer[], List<Tupla<String, String>>> result = bo.processFile(wb, sc, tipoCarga, usuario);
                        total = result.getK1()[0];
                        exitosos = result.getK1()[1];
                        rechazados = result.getK1()[2];
                        errores = result.getK2();
                    }
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                    addActionError(getText("carga.archivo.error.formato"));
                } catch (java.lang.Exception le) {
                    addActionError(getText("carga.archivo.error.carga"));
                    le.printStackTrace();
                }
            } // Si no es el content type deseado
            else {
                addActionError(getText("carga.archivo.error.formato"));
            }
        } else {
            addActionError(getText("carga.archivo.error.carga"));
        }
        return INPUT;
    }

    public String rechazado() {
        return INPUT;
    }

    public String noCoincide() {
        return INPUT;
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

    public BigDecimal getOrdenId() {
        return ordenId;
    }

    public void setOrdenId(BigDecimal ordenId) {
        this.ordenId = ordenId;
    }

    public String getNombreOrden() {
        return nombreOrden;
    }

    public void setNombreOrden(String nombreOrden) {
        this.nombreOrden = nombreOrden;
    }

    public Integer getExitosos() {
        return exitosos;
    }

    public void setExitosos(Integer exitosos) {
        this.exitosos = exitosos;
    }

    public Integer getRechazados() {
        return rechazados;
    }

    public void setRechazados(Integer rechazados) {
        this.rechazados = rechazados;
    }

    public Integer getNoProcesados() {
        return noProcesados;
    }

    public void setNoProcesados(Integer noProcesados) {
        this.noProcesados = noProcesados;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getTipoCarga() {
        return tipoCarga;
    }

    public void setTipoCarga(Integer tipoCarga) {
        this.tipoCarga = tipoCarga;
    }

    public List<Tupla<String, String>> getErrores() {
        return errores;
    }

    public void setErrores(List<Tupla<String, String>> errores) {
        this.errores = errores;
    }

    public Boolean getReferencia() {
        return referencia;
    }

    public void setReferencia(Boolean referencia) {
        this.referencia = referencia;
    }

    public List<CampoResumen> getListaCampos() {
        return listaCampos;
    }

    public void setListaCampos(List<CampoResumen> listaCampos) {
        this.listaCampos = listaCampos;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public List<Depositos> getDepositosRechazados() {
        return depositosRechazados;
    }

    public void setDepositosRechazados(List<Depositos> depositosRechazados) {
        this.depositosRechazados = depositosRechazados;
    }

    public Integer getCorreosEnviados() {
        return correosEnviados;
    }

    public void setCorreosEnviados(Integer correosEnviados) {
        this.correosEnviados = correosEnviados;
    }
}
