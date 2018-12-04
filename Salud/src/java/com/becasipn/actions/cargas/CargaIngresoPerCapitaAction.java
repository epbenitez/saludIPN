package com.becasipn.actions.cargas;

import com.becasipn.actions.ajax.JSONAjaxAction;
import com.becasipn.business.CuestionarioPreguntasRespuestasBO;
import com.becasipn.business.PeriodoBO;
import com.becasipn.persistence.model.Periodo;
import static com.opensymphony.xwork2.Action.INPUT;
import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author Tania Gabriela Sánchez Manilla
 */
public class CargaIngresoPerCapitaAction extends JSONAjaxAction implements MensajesCargas {

	private File upload;
	private String uploadFileName;
	private String uploadContentType;
	private String password;

	private List<String> log = new LinkedList<>();

	public String form() {
		return INPUT;
	}

	public String carga() {
		LOG.info(String.format("%s : fileUpload", getClass().getName()));
		CuestionarioPreguntasRespuestasBO bo = new CuestionarioPreguntasRespuestasBO(getDaos());
		//Periodo
		PeriodoBO periodoBO = new PeriodoBO(getDaos());
		Periodo periodo = periodoBO.getPeriodoActivo();
		if (getUpload() != null) {
			// Excel 2003 Format
			if ("application/vnd.ms-excel".equalsIgnoreCase(getUploadContentType())) {
				LOG.debug("Excel 2003 Format");
				LOG.debug("calling readXmlFile(File xls) method...");
				try {
					//INVOCA PROCESA ARCHIVO
					LOG.info("Leyendo archivo XLS");
					Workbook wb = bo.readXlsFile(new FileInputStream(getUpload()));
					log = bo.processFile(wb, periodo.getId());
				} catch (NumberFormatException nfe) {
					nfe.printStackTrace();
					addActionError(getText("carga.archivo.error.formato"));
					return INPUT;
				} catch (org.apache.poi.hssf.OldExcelFormatException le) {
					addActionError(getText("carga.archivo.error.sistema.version.excel")); // + hideError(le));
					le.printStackTrace();
					return INPUT;
				} catch (java.lang.Exception le) {
					addActionError(getText("carga.archivo.error.sistema")); // + hideError(le));
					le.printStackTrace();
					return INPUT;
				}
			} // Excel 2007 Format
			else if ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equalsIgnoreCase(getUploadContentType())) {
				LOG.debug("Excel 2007 Format");
				LOG.debug("calling readXmlxFile(File xlsx) method...");
				try {
					LOG.info("Leyendo archivo XLSX");
					Workbook wb = bo.readXlsxFile(new FileInputStream(getUpload()));
					log = bo.processFile(wb, periodo.getId());
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

	public String formIntegrantes() {
		return INPUT;
	}

	public String cargaIntegrantes() {
		LOG.info(String.format("%s : fileUpload", getClass().getName()));
		CuestionarioPreguntasRespuestasBO bo = new CuestionarioPreguntasRespuestasBO(getDaos());
		//Periodo
		PeriodoBO periodoBO = new PeriodoBO(getDaos());
		Periodo periodo = periodoBO.getPeriodoActivo();
		if (getUpload() != null) {
			// Excel 2003 Format
			if ("application/vnd.ms-excel".equalsIgnoreCase(getUploadContentType())) {
				LOG.debug("Excel 2003 Format");
				LOG.debug("calling readXmlFile(File xls) method...");
				try {
					//INVOCA PROCESA ARCHIVO
					LOG.info("Leyendo archivo XLS");
					Workbook wb = bo.readXlsFile(new FileInputStream(getUpload()));
					log = bo.salariosIntegrantes(wb, periodo.getId());
				} catch (NumberFormatException nfe) {
					nfe.printStackTrace();
					addActionError(getText("carga.archivo.error.formato"));
					return INPUT;
				} catch (org.apache.poi.hssf.OldExcelFormatException le) {
					addActionError(getText("carga.archivo.error.sistema.version.excel")); // + hideError(le));
					le.printStackTrace();
					return INPUT;
				} catch (java.lang.Exception le) {
					addActionError(getText("carga.archivo.error.sistema")); // + hideError(le));
					le.printStackTrace();
					return INPUT;
				}
			} // Excel 2007 Format
			else if ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equalsIgnoreCase(getUploadContentType())) {
				LOG.debug("Excel 2007 Format");
				LOG.debug("calling readXmlxFile(File xlsx) method...");
				try {
					LOG.info("Leyendo archivo XLSX");
					Workbook wb = bo.readXlsxFile(new FileInputStream(getUpload()));
					log = bo.salariosIntegrantes(wb, periodo.getId());
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getLog() {
		return log;
	}

	public void setLog(List<String> log) {
		this.log = log;
	}
}
