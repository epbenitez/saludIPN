package com.becasipn.actions.admin;

import com.becasipn.actions.BaseAction;
import com.becasipn.business.AlumnoBO;
import com.becasipn.business.OtorgamientoBO;
import com.becasipn.persistence.model.UnidadAcademica;
import com.becasipn.persistence.model.Usuario;
import com.opensymphony.xwork2.ActionContext;
import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author Tania G. Sánchez
 */
public class AdministracionCuentasExternasAction extends BaseAction implements MensajesAdmin {
    public static final String PERMITIR = "permitir";
    private File upload;
    private String uploadFileName;
    private String uploadContentType;
    private List<String> log = new LinkedList<>();
    
    public String permitir() {
        return SUCCESS;
    }
    
    public String cargar() {
        LOG.info(String.format("%s : fileUpload", getClass().getName()));
        AlumnoBO bo = new AlumnoBO(getDaos());
        UnidadAcademica unidadAcademica = null;
        if (isResponsable()) {
            Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
	    unidadAcademica = getDaos().getPersonalAdministrativoDao().findByUsuario(usuario.getId()).getUnidadAcademica();
        }
        if (getUpload() != null) {
            // Excel 2003 Format
            if ("application/vnd.ms-excel".equalsIgnoreCase(getUploadContentType())) {
                LOG.debug("Excel 2003 Format");
                LOG.debug("calling readXmlFile(File xls) method...");
                try {
                    //INVOCA PROCESA ARCHIVO
                    LOG.info("Leyendo archivo XLS");
                    Workbook wb = bo.readXlsFile(new FileInputStream(getUpload()));
                    log = bo.processFile(wb, unidadAcademica);
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                    addActionError(getText("carga.archivo.error.formato"));
                    return PERMITIR;
                } catch (org.apache.poi.hssf.OldExcelFormatException le) {
                    addActionError(getText("carga.archivo.error.sistema.version.excel")); // + hideError(le));
                    le.printStackTrace();
                    return PERMITIR;
                } catch (java.lang.Exception le) {
                    addActionError(getText("carga.archivo.error.sistema")); // + hideError(le));
                    le.printStackTrace();
                    return PERMITIR;
                }
            } // Excel 2007 Format
            else if ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equalsIgnoreCase(getUploadContentType())) {
                LOG.debug("Excel 2007 Format");
                LOG.debug("calling readXmlxFile(File xlsx) method...");
                try {
                    LOG.info("Leyendo archivo XLSX");
                    Workbook wb = bo.readXlsxFile(new FileInputStream(getUpload()));
                    log = bo.processFile(wb, unidadAcademica);
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                    addActionError(getText("carga.archivo.error.formato"));
                    return PERMITIR;
                } catch (java.lang.Exception le) {
                    addActionError(getText("carga.archivo.error.carga"));// + hideError(le));
                    le.printStackTrace();
                    return PERMITIR;
                }
            } // Si no es el content type deseado
            else {
                addActionError(getText("carga.archivo.error.no.archivo"));
                return PERMITIR;
            }
        } else {
            addActionError(getText("carga.archivo.error.carga"));
            return PERMITIR;
        }
        return PERMITIR;
    }
    
    public String buscarCuentaConfiguracion() {
        return SUCCESS;
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
}