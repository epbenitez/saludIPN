package com.becasipn.actions.admin;

import com.becasipn.actions.BaseReportAction;
import com.becasipn.business.AlumnoBO;
import com.becasipn.persistence.model.PadronProspera;
import static com.opensymphony.xwork2.Action.SUCCESS;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.PersistenceException;
import org.apache.poi.ss.usermodel.Workbook;

public class AdministracionProsperaAction extends BaseReportAction implements MensajesAdmin {

	private List<PadronProspera> alumnosLst = new ArrayList<>();
	private Boolean carga = false;

	private File upload;
	private String successMsj;

	private String uploadFileName;
	private String uploadContentType;
	private List<String> log = new LinkedList<>();

	public String lista() {
		carga = false;
		return SUCCESS;
	}

	public String carga() {
		alumnosLst = null;
		carga = true;
		LOG.info(String.format("%s : fileUpload", getClass().getName()));
		AlumnoBO bo = new AlumnoBO(getDaos());

		if (getUpload() != null) {
			// Excel 2003 Format
			if ("application/vnd.ms-excel".equalsIgnoreCase(getUploadContentType())) {
				LOG.debug("Excel 2003 Format");
				LOG.debug("calling readXmlFile(File xls) method...");
				try {
					//INVOCA PROCESA ARCHIVO
					Workbook wb = null;
					wb = bo.readXlsFile(new FileInputStream(getUpload()));
					alumnosLst = bo.padronProspera(wb);

					int total = wb.getSheetAt(0).getLastRowNum();
					int done = total - alumnosLst.size();
					successMsj = "Fueron procesados "
							+ String.valueOf(done)
							+ " alumnos correctamente.";
					if (alumnosLst.isEmpty()) {
						addActionMessage("El archivo terminó de cargarse.");
					}
				} catch (NumberFormatException nfe) {
					nfe.printStackTrace();
					addActionError(getText("carga.archivo.error.formato"));
					return "lista";
				} catch (org.apache.poi.hssf.OldExcelFormatException le) {
					addActionError(getText("carga.archivo.error.sistema.version.excel")); // + hideError(le));
					le.printStackTrace();
					return "lista";
				} catch (PersistenceException pe) {
					addActionError("Ocurrio un error en base de datos, notifique a su administrador e intentelo mas tarde.");
					pe.printStackTrace();
					return "lista";
				} catch (java.lang.Exception le) {
					if (le.toString().contains("password")) {
						addActionError("La contraseña especificada no es correcta, por favor, verifique.");
					} else {
						addActionError(getText("carga.archivo.error.sistema")); // + hideError(le));
					}
					le.printStackTrace();
					return "lista";
				}
			} // Excel 2007 Format
			else if ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equalsIgnoreCase(getUploadContentType())) {
				LOG.debug("Excel 2007 Format");
				LOG.debug("calling readXmlxFile(File xlsx) method...");

				try {
					Workbook wb = null;
					wb = bo.readXlsxFile(new FileInputStream(getUpload()));
					alumnosLst = bo.padronProspera(wb);

					int total = wb.getSheetAt(0).getLastRowNum();
					int done = total - alumnosLst.size();
					successMsj = "Fueron procesados "
							+ String.valueOf(done)
							+ " alumnos correctamente.";
					if (alumnosLst.isEmpty()) {
//			addActionError("Hay un problema con su archivo, verifique y vuelva a intentarlo");
						addActionMessage("El archivo terminó de cargarse.");
					}
				} catch (NumberFormatException nfe) {
					nfe.printStackTrace();
					addActionError(getText("carga.archivo.error.formato"));
					return "lista";
				} catch (java.lang.Exception le) {
					addActionError(getText("carga.archivo.error.carga"));
					le.printStackTrace();
					return "lista";
				}
			} // Si no es el content type deseado
			else {
				addActionError(getText("carga.archivo.error.formato"));
				return "lista";
			}
		} else {
			addActionError(getText("carga.archivo.error.carga"));
			return "lista";
		}
		return "lista";
	}

	public List<PadronProspera> getAlumnosLst() {
		return alumnosLst;
	}

	public void setAlumnosLst(List<PadronProspera> alumnosLst) {
		this.alumnosLst = alumnosLst;
	}

	public Boolean getCarga() {
		return carga;
	}

	public void setCarga(Boolean carga) {
		this.carga = carga;
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

	public String getSuccessMsj() {
		return successMsj;
	}

	public void setSuccessMsj(String successMsj) {
		this.successMsj = successMsj;
	}
}
