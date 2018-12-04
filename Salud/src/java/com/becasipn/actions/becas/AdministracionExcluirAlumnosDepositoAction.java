package com.becasipn.actions.becas;

import com.becasipn.actions.BaseAction;
import com.becasipn.business.ExcluirDepositoBO;
import com.becasipn.business.OtorgamientoBO;
import com.becasipn.persistence.model.BitacoraOtorgamientos;
import com.becasipn.persistence.model.IdentificadorOtorgamiento;
import com.becasipn.persistence.model.Otorgamiento;
import com.becasipn.persistence.model.Usuario;
import com.opensymphony.xwork2.ActionContext;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author Tania G. Sánchez
 */
public class AdministracionExcluirAlumnosDepositoAction extends BaseAction implements MensajesBecas {

    public static final String BUSCAR = "buscar";
    private Otorgamiento otorgamiento = new Otorgamiento();
    private BigDecimal periodoM;
    private File upload;
    private String uploadFileName;
    private String uploadContentType;
    private List<String> log = new LinkedList<>();
    private Integer accion;

    // Excluir/Incluir alumnos de depósitos
    private List<IdentificadorOtorgamiento> idsOtorgamientos;
    private BigDecimal idOtorgamientoId;
    private BigDecimal idOtorgamiento;
    private String checkboxExcel;
    private boolean desdeExcel;
    int estatusOption;

    public String cargar() {
        idsOtorgamientos = ExcluirDepositoBO.ubicaIdentificadorOtorgamiento(getDaos(), new BigDecimal(12)); // 12 sin observación
        LOG.info(String.format("%s : fileUpload", getClass().getName()));
        ExcluirDepositoBO bo = ExcluirDepositoBO.getInstance(getDaos());
        if (getUpload() != null) {
            // Excel 2003 Format
            if ("application/vnd.ms-excel".equalsIgnoreCase(getUploadContentType())) {
                LOG.debug("Excel 2003 Format");
                LOG.debug("calling readXmlFile(File xls) method...");
                try {
                    //INVOCA PROCESA ARCHIVO
                    LOG.info("Leyendo archivo XLS");
                    bo.setXlsFile(new FileInputStream(getUpload()), false);
                    log = bo.processFile(periodoM, accion, idOtorgamientoId, desdeExcel);
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                    addActionError(getText("carga.archivo.error.formato"));
                    return BUSCAR;
                } catch (org.apache.poi.hssf.OldExcelFormatException le) {
                    addActionError(getText("carga.archivo.error.sistema.version.excel")); // + hideError(le));
                    le.printStackTrace();
                    return BUSCAR;
                } catch (java.lang.Exception le) {
                    addActionError(getText("carga.archivo.error.sistema")); // + hideError(le));
                    le.printStackTrace();
                    return BUSCAR;
                }
            } // Excel 2007 Format
            else if ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equalsIgnoreCase(getUploadContentType())) {
                LOG.debug("Excel 2007 Format");
                LOG.debug("calling readXmlxFile(File xlsx) method...");
                try {
                    LOG.info("Leyendo archivo XLSX");
                    bo.setXlsFile(new FileInputStream(getUpload()), true);
                    log = bo.processFile(periodoM, accion, idOtorgamientoId, desdeExcel);

                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                    addActionError(getText("carga.archivo.error.formato"));
                    return BUSCAR;
                } catch (java.lang.Exception le) {
                    addActionError(getText("carga.archivo.error.carga"));// + hideError(le));
                    le.printStackTrace();
                    return BUSCAR;
                }
            } // Si no es el content type deseado
            else {
                addActionError(getText("carga.archivo.error.no.archivo"));
                return BUSCAR;
            }
        } else {
            addActionError(getText("carga.archivo.error.carga"));
            return BUSCAR;
        }
        return BUSCAR;
    }

    public String buscar() {
        idsOtorgamientos = ExcluirDepositoBO.ubicaIdentificadorOtorgamiento(getDaos(), new BigDecimal(12)); // 12 sin observación
        return SUCCESS;
    }

    public String editar() {
        setInfoEditar();
        return SUCCESS;
    }
    
    private void setInfoEditar() {
        otorgamiento = getDaos().getOtorgamientoDao().findById(idOtorgamiento);
        BigDecimal idIdentificadorOtorgamiento = otorgamiento.getIdentificadorOtorgamiento().getId(); // Obtener el identificador actual
        idsOtorgamientos = ExcluirDepositoBO.ubicaIdentificadorOtorgamiento(getDaos(), idIdentificadorOtorgamiento);
        if (otorgamiento.getExcluirDeposito()) {
            estatusOption = 1;
        }
    }

    public String guardar() {
        ExcluirDepositoBO bo = ExcluirDepositoBO.getInstance(getDaos());
        String resultados = bo.guardar(idOtorgamiento, idOtorgamientoId, estatusOption);
        if (resultados.isEmpty()) {
            addActionMessage("Éxito");
        } else {
            addActionError(resultados);
        }

        setInfoEditar();
        
        return "editar";
    }

    public String excluir() {
        BitacoraOtorgamientos bOt = new BitacoraOtorgamientos();
        Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        otorgamiento = getDaos().getOtorgamientoDao().findById(otorgamiento.getId());
        otorgamiento.setExcluirDeposito(Boolean.TRUE);
        bOt.setOtorgamiento(otorgamiento);
        bOt.setExcluirDeposito(Boolean.TRUE);
        bOt.setUsuarioModifico(usuario);
        bOt.setFechaModificacion(new Date());
        getDaos().getBitacoraOtorgamientosDao().save(bOt);
        try {
            getDaos().getOtorgamientoDao().update(otorgamiento);
            addActionMessage(getText("becas.alumno.excluir.deposito.exito"));
        } catch (Exception e) {
            addActionError(getText("becas.alumno.excluir.deposito.error"));
        }
        return BUSCAR;
    }

    public String incluir() {
        BitacoraOtorgamientos bOt = new BitacoraOtorgamientos();
        Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        otorgamiento = getDaos().getOtorgamientoDao().findById(otorgamiento.getId());
        otorgamiento.setExcluirDeposito(Boolean.FALSE);
        bOt.setOtorgamiento(otorgamiento);
        bOt.setExcluirDeposito(Boolean.FALSE);
        bOt.setUsuarioModifico(usuario);
        bOt.setFechaModificacion(new Date());
        getDaos().getBitacoraOtorgamientosDao().save(bOt);
        try {
            getDaos().getOtorgamientoDao().update(otorgamiento);
            addActionMessage(getText("becas.alumno.incluir.deposito.exito"));
        } catch (Exception e) {
            addActionError(getText("becas.alumno.incluir.deposito.error"));
        }
        return BUSCAR;
    }

    public Otorgamiento getOtorgamiento() {
        return otorgamiento;
    }

    public void setOtorgamiento(Otorgamiento otorgamiento) {
        this.otorgamiento = otorgamiento;
    }

    public BigDecimal getPeriodoM() {
        return periodoM;
    }

    public void setPeriodoM(BigDecimal periodoM) {
        this.periodoM = periodoM;
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

    public Integer getAccion() {
        return accion;
    }

    public void setAccion(Integer accion) {
        this.accion = accion;
    }

    public List<IdentificadorOtorgamiento> getIdsOtorgamientos() {
        return idsOtorgamientos;
    }

    public void setIdOtorgamientoId(BigDecimal idOtorgamientoId) {
        this.idOtorgamientoId = idOtorgamientoId;
    }

    public void setCheckboxExcel(String checkboxExcel) {
        if (checkboxExcel.equals("on")) {
            this.desdeExcel = true;
        }
        this.checkboxExcel = checkboxExcel;
    }

    public int getEstatusOption() {
        return estatusOption;
    }

    public void setEstatusOption(int estatusOption) {
        this.estatusOption = estatusOption;
    }

    public BigDecimal getIdOtorgamiento() {
        return idOtorgamiento;
    }

    public void setIdOtorgamiento(BigDecimal idOtorgamiento) {
        this.idOtorgamiento = idOtorgamiento;
    }
}
