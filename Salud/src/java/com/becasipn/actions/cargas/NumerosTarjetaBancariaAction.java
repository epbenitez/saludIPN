/**
 * SISTEMA INFORMÁTICO DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE
 * SERVICIOS ESTUDIANTILES Created on : 07-ene-2016, 13:42:31
 *
 */
package com.becasipn.actions.cargas;

import com.becasipn.actions.ajax.JSONAjaxAction;
import com.becasipn.business.NumerosTarjetaBancariaBO;
import com.becasipn.util.UtilFile;
import static com.opensymphony.xwork2.Action.INPUT;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author Patricia Benitez
 */
public class NumerosTarjetaBancariaAction extends JSONAjaxAction implements MensajesCargas {

    private File upload;
    private String uploadFileName;
    private String uploadContentType;

    private List<String> log = new LinkedList<>();

    private List<String> lote;
    private Date fechaL;
    private String fechaLote;
    private String noLote;
    private String password;

    public String form() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        fechaL = new Date();
        sdf.format(fechaL);
        lote = getDaos().getTarjetaBancariaDao().getOpcionesLotes();
        return INPUT;
    }

    public String carga() {
        LOG.info(String.format("%s : fileUpload", getClass().getName()));
        NumerosTarjetaBancariaBO bo = new NumerosTarjetaBancariaBO(getDaos());
        
        Date fecha = UtilFile.strToDate(fechaLote, "dd/MM/yyyy");

        if (getUpload() != null) {

            // Excel 2003 Format
            if ("application/vnd.ms-excel".equalsIgnoreCase(getUploadContentType())) {
                LOG.debug("Excel 2003 Format");
                LOG.debug("calling readXmlFile(File xls) method...");

                try {
                    //INVOCA PROCESA ARCHIVO
                    Workbook wb = null;
                    if (password == null || password.isEmpty()) {
                        LOG.info("Leyendo archivo sin proteccion");
                        wb = bo.readXlsFile(new FileInputStream(getUpload()));
                    } else {
                        LOG.info("Leyendo archivo protegido con la contraseña: %s", password);
                        wb = bo.readXlsFile(new FileInputStream(getUpload()), password);
                    }
                    log = bo.processFile(wb, noLote, fecha);

                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                    addActionError(getText("carga.archivo.error.formato"));
                    return INPUT;
                } catch (org.apache.poi.hssf.OldExcelFormatException le) {
                    addActionError(getText("carga.archivo.error.sistema.version.excel")); // + hideError(le));
                    le.printStackTrace();
                    return INPUT;
                } catch (java.lang.Exception le) {
                    if (le.toString().contains("password")) {
                        addActionError("La contraseña especificada no es correcta, por favor, verifique.");
                    } else {
                        addActionError(getText("carga.archivo.error.sistema")); // + hideError(le));
                    }
                    le.printStackTrace();
                    return INPUT;
                }
            } // Excel 2007 Format
            else if ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equalsIgnoreCase(getUploadContentType())) {
                LOG.debug("Excel 2007 Format");
                LOG.debug("calling readXmlxFile(File xlsx) method...");

                try {
                    //
//                    Workbook wb = bo.readXlsxFile(new FileInputStream(getUpload()), password);
                    Workbook wb = null;
                    if (password == null || password.isEmpty()) {
                        LOG.info("Leyendo archivo sin proteccion");
                        wb = bo.readXlsxFile(new FileInputStream(getUpload()));
                    } else {
                        LOG.info("Leyendo archivo protegido con la contraseña: %s", password);
                        wb = bo.readXlsxFile(new FileInputStream(getUpload()), password);
                    }
                    log = bo.processFile(wb, noLote, fecha);

                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                    addActionError(getText("carga.archivo.error.formato"));
                    return INPUT;
                } catch (java.lang.Exception le) {
                    addActionError(getText("carga.archivo.error.carga"));
                    le.printStackTrace();
                    return INPUT;
                }
            } // Si no es el content type deseado
            else {
                addActionError(getText("carga.archivo.error.formato"));
                return INPUT;
            }

        } else {
            addActionError(getText("carga.archivo.error.carga"));
            return INPUT;

        }
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

    public List<String> getLog() {
        return log;
    }

    public void setLog(List<String> log) {
        this.log = log;
    }

    public List<String> getLote() {
        return lote;
    }

    public void setLote(List<String> lote) {
        this.lote = lote;
    }

    public Date getFechaL() {
        return fechaL;
    }

    public void setFechaL(Date fechaL) {
        this.fechaL = fechaL;
    }

    public String getFechaLote() {
        return fechaLote;
    }

    public void setFechaLote(String fechaLote) {
        this.fechaLote = fechaLote;
    }

    public String getNoLote() {
        return noLote;
    }

    public void setNoLote(String noLote) {
        this.noLote = noLote;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}