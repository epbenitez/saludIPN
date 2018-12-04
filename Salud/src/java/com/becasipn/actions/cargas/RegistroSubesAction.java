package com.becasipn.actions.cargas;

import com.becasipn.actions.ajax.JSONAjaxAction;
import com.becasipn.business.SubesBO;
import com.becasipn.domain.CampoResumen;
import com.becasipn.persistence.model.ConvocatoriaSubes;
import com.becasipn.persistence.model.Periodo;
import static com.opensymphony.xwork2.Action.INPUT;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Rafael Cardenas Resendiz
 */
public class RegistroSubesAction extends JSONAjaxAction implements MensajesCargas {

    private File upload;
    private String uploadFileName;
    private String uploadContentType;
    private List<ConvocatoriaSubes> convocatorias;
    private BigDecimal convocatoria;
    private BigDecimal periodo;
    private Integer tipoCarga;
    private InputStream excelStream;
    private String contentDisposition;

    private Integer procesados;
    private Integer correctos;
    private Integer solicitudesCreadas;
    private List<CampoResumen> listaCampos = new ArrayList<>();

    public String form() {
        convocatorias = getDaos().getConvocatoriaSubesDao().findAll();
        return INPUT;
    }

    public String carga() {
        int errorLine = 0;
        SubesBO bo = new SubesBO(getDaos());
        ConvocatoriaSubes conv = getDaos().getConvocatoriaSubesDao().findById(getConvocatoria());
        Periodo p = getDaos().getPeriodoDao().findById(getPeriodo());
        try {
            bo.carga(getUpload(), getUploadContentType(), conv, p, tipoCarga);
            //addActionError("Debes de contestar tu ESE antes de responder el Censo de Salud");
        } catch (Exception ex) {
            System.out.println(ex);
        }

        procesados = getDaos().getPadronSubesDao().getCargados(getDaos().getPeriodoDao().getPeriodoActivo().getId());
        correctos = getDaos().getPadronSubesDao().getSolicitudesCreadas(getDaos().getPeriodoDao().getPeriodoActivo().getId(), new BigDecimal(5));
        solicitudesCreadas = getDaos().getPadronSubesDao().getSolicitudesCreadas(getDaos().getPeriodoDao().getPeriodoActivo().getId(), new BigDecimal(4));

        System.out.println("rows: " + bo.numRow);

        if (bo.cont != bo.numRow) {
            errorLine = bo.cont + 1;
            addActionError("Ocurrió un error al intentar leer la línea: " + errorLine + " verifica el archivo");
        } else {
            System.out.println(new Date());
            addActionMessage("Archivo cargado correctamente");

        }
        return INPUT;
    }

    public String reporte() {
        SubesBO bo = new SubesBO(getDaos());
        excelStream = bo.getInfoExcel(periodo, convocatoria);
        String titulo = bo.getTitulo();
        setContentDisposition("attachment; filename=" + titulo);
        return "archivo";
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

    public List<ConvocatoriaSubes> getConvocatorias() {
        return convocatorias;
    }

    public void setConvocatorias(List<ConvocatoriaSubes> convocatorias) {
        this.convocatorias = convocatorias;
    }

    public BigDecimal getConvocatoria() {
        return convocatoria;
    }

    public void setConvocatoria(BigDecimal convocatoria) {
        this.convocatoria = convocatoria;
    }

    public BigDecimal getPeriodo() {
        return periodo;
    }

    public void setPeriodo(BigDecimal periodo) {
        this.periodo = periodo;
    }

    public Integer getTipoCarga() {
        return tipoCarga;
    }

    public void setTipoCarga(Integer tipoCarga) {
        this.tipoCarga = tipoCarga;
    }

    public Integer getProcesados() {
        return procesados;
    }

    public void setProcesados(Integer procesados) {
        this.procesados = procesados;
    }

    public Integer getCorrectos() {
        return correctos;
    }

    public void setCorrectos(Integer correctos) {
        this.correctos = correctos;
    }

    public Integer getSolicitudesCreadas() {
        return solicitudesCreadas;
    }

    public void setSolicitudesCreadas(Integer solicitudesCreadas) {
        this.solicitudesCreadas = solicitudesCreadas;
    }

    public List<CampoResumen> getListaCampos() {
        return listaCampos;
    }

    public void setListaCampos(List<CampoResumen> listaCampos) {
        this.listaCampos = listaCampos;
    }

    public InputStream getExcelStream() {
        return excelStream;
    }

    public void setExcelStream(InputStream excelStream) {
        this.excelStream = excelStream;
    }

    public String getContentDisposition() {
        return contentDisposition;
    }

    public void setContentDisposition(String contentDisposition) {
        this.contentDisposition = contentDisposition;
    }

}
