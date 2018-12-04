package com.becasipn.actions.becas;

import com.becasipn.actions.BaseAction;
import com.becasipn.business.DepositosBO;
import com.becasipn.business.EstadisticasBO;
import com.becasipn.business.OrdenDepositoBO;
import com.becasipn.domain.OrdenDepositoPivot;
import com.becasipn.persistence.model.OrdenDeposito;
import com.becasipn.persistence.model.TipoProceso;
import com.becasipn.persistence.model.UnidadAcademica;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.persistence.model.VWOrdenDeposito;
import com.becasipn.util.CSVExport;
import com.becasipn.util.ExcelExport;
import com.opensymphony.xwork2.ActionContext;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Victor Lozano
 */
public class AdministracionOrdenesDepositoAction extends BaseAction implements MensajesBecas {

    private static final String LISTA = "lista";
    private static final String ARCHIVO = "archivo";
    private BigDecimal ordenId;
    private BigDecimal privilegio;
    private VWOrdenDeposito ordenDeposito;
    private String nombreOrden;

    private InputStream excelStream;
    private Integer cuentaId;
    private String contentDisposition;
    private String documentFormat = "xlsx";
    private String datosGrafica;

    private List<OrdenDepositoPivot> datosPivot = new ArrayList<OrdenDepositoPivot>();

    /**
     * Función para cargar el listado de ordenes de deposito
     *
     * @author Victor Lozano
     * @return SUCCESS
     */
    public String lista() {
        Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        privilegio = getDaos().getUsuarioPrivilegioDao().findByUsuario(usuario.getId()).getPrivilegio().getId();

        OrdenDepositoBO bo = new OrdenDepositoBO(getDaos());
//        datosPivot = null;
         datosPivot = bo.informacionPivotOrdenesDeposito(new BigDecimal(1));
        return SUCCESS;
    }

    public String descargaPendientes() {
        
        OrdenDepositoBO bo = new OrdenDepositoBO(getDaos());
        ExcelExport excelExport = new ExcelExport();
        String cuentaStr = bo.getFileNamePendientes(cuentaId);
        String[] encabezado = new String[]{"Periodo", "Orden de depósito", "Total",
            "Espera", "Faltantes"};
        setContentDisposition("attachment; filename=\"Ordenes de deposito pendientes - " + cuentaStr + ".xlsx\"");
        excelStream = excelExport.construyeExcel(encabezado, bo.getInfoExcelPendientes(cuentaId));

        return "archivo";
    }

    /**
     * Función para cargar el detalle de una orden de deposito
     *
     * @author Victor Lozano
     * @return SUCCESS
     */
    public String detalle() {
        DepositosBO depositoBO = new DepositosBO(getDaos());
        datosGrafica = depositoBO.graficaEstatusDepositos(ordenId);
        ordenDeposito = getDaos().getVwOrdenDepositoDao().findById(ordenId);
        if (ordenDeposito.getUnidadAcademica() == null) {
            UnidadAcademica ua = new UnidadAcademica();
            ua.setNombre("Todas");
            ordenDeposito.setUnidadAcademica(ua);
        }
        if (ordenDeposito.getTipoProceso() == null) {
            TipoProceso tp = new TipoProceso();
            tp.setNombre("Todos");
            ordenDeposito.setTipoProceso(tp);
        }
        ordenDeposito.setNombreMes(EstadisticasBO.getMes(Integer.toString(ordenDeposito.getMes())));
        return SUCCESS;

    }

    /**
     * Función para cargar el listado de alumnos de una orden de deposito
     *
     * @author Victor Lozano
     * @return SUCCESS
     */
    public String descargar() {
        boolean rechazados;
        nombreOrden = getDaos().getOrdenDepositoDao().findById(ordenId).getNombreOrdenDeposito();
        int resultado = nombreOrden.indexOf("TDR");
        String[] encabezado;
        if (resultado != -1) {
            rechazados = true;
            encabezado = new String[]{"Número de Cuenta",
                "Nombre(s)",
                "Apellido Paterno",
                "Apellido Materno",
                "Importe",
                "Ref Num",
                "Ref AlfaNum",
                "Concepto/Pago",
                "Plazo",
                "Orden Original",
                "Boleta",
                "Unidad Académica"
            };
        } else {
            rechazados = false;
            encabezado = new String[]{"Número de Cuenta",
                "Nombre(s)",
                "Apellido Paterno",
                "Apellido Materno",
                "Importe",
                "Ref Num",
                "Ref AlfaNum",
                "Concepto/Pago",
                "Plazo",
                "Boleta",
                "Unidad Académica"
            };
        }
        setContentDisposition("attachment; filename=\"" + nombreOrden + ".xls\"");
        List<Object[]> listado = getDaos().getDepositosDao().obtenerOrdenDeposito(ordenId, rechazados);
        ExcelExport excelExport = new ExcelExport();
        excelStream = excelExport.construyeExcel(encabezado, listado);
        return ARCHIVO;
    }

    /**
     * Actualiza el estado de una orden de deposito a "Aplicado"
     *
     * @author Victor Lozano
     * @return LISTA
     */
    public String aplicar() {
        OrdenDepositoBO ordenesDepositoBO = new OrdenDepositoBO(getDaos());
        OrdenDeposito orden = getDaos().getOrdenDepositoDao().findById(ordenId);
        if (orden.getEstatusDeposito().getId().compareTo(new BigDecimal(1)) == 0) {
            orden.setEstatusDeposito(getDaos().getEstatusDepositoDao().findById(new BigDecimal(2)));
            if (ordenesDepositoBO.guardaOrden(orden)) {
                addActionMessage(getText("becas.actualizado.exito"));
            } else {
                addActionError(getText("becas.actualizado.error"));

            }
        } else {
            addActionError(getText("becas.orden.estatus.error"));
        }
        return LISTA;
    }

    /**
     * Actualiza el estado de una orden de deposito a "Rechazado"
     *
     * @author Victor Lozano
     * @return LISTA
     */
    public String rechazar() {
        OrdenDepositoBO ordenesDepositoBO = new OrdenDepositoBO(getDaos());
        OrdenDeposito orden = getDaos().getOrdenDepositoDao().findById(ordenId);
        if (orden.getEstatusDeposito().getId().compareTo(new BigDecimal(1)) == 0) {
            orden.setEstatusDeposito(getDaos().getEstatusDepositoDao().findById(new BigDecimal(4)));
            if (ordenesDepositoBO.guardaOrden(orden)) {
                addActionMessage(getText("becas.actualizado.exito"));
            } else {
                addActionError(getText("becas.actualizado.error"));

            }
        } else {
            addActionError(getText("becas.orden.estatus.error"));
        }
        return LISTA;
    }

    /**
     * Actualiza el estado de una orden de deposito a "Cancelado"
     *
     * @author Victor Lozano
     * @return LISTA
     */
    public String cancela() {
        OrdenDepositoBO ordenesDepositoBO = new OrdenDepositoBO(getDaos());
        OrdenDeposito orden = getDaos().getOrdenDepositoDao().findById(ordenId);
        if (orden.getEstatusDeposito().getId().compareTo(new BigDecimal(1)) == 0) {
            orden.setEstatusDeposito(getDaos().getEstatusDepositoDao().findById(new BigDecimal(5)));
            if (ordenesDepositoBO.guardaOrden(orden)) {
                addActionMessage(getText("becas.actualizado.exito"));
            } else {
                addActionError(getText("becas.actualizado.error"));

            }
        } else {
            addActionError(getText("becas.orden.estatus.error"));
        }
        return LISTA;
    }

    /**
     * Función para descargar el listado de alumnos de una orden de deposito
     * incluyendo datos del SUBES.
     *
     * @author Tania G. Sánchez
     * @return SUCCESS
     */
    public String subes() {
        OrdenDeposito ordenDeposito = getDaos().getOrdenDepositoDao().findById(ordenId);
        nombreOrden = ordenDeposito.getNombreOrdenDeposito();
        String folio = "";
        String folioBeca = "";
        String subes = "";
        if (ordenDeposito.getProgramaBeca().getId().equals(new BigDecimal("5"))) { //Manutención
            folio = "Folio SUBES";
            folioBeca = " decode(ps.foliosubes, null, 'Sin datos', ps.foliosubes),";
            subes = " and ps.estatussubes = 'Aceptado'";
        } else if (ordenDeposito.getProgramaBeca().getId().equals(new BigDecimal("7"))) { //Transporte Manutención
            folio = "Folio Transporte";
            folioBeca = " decode(ps.foliotransporte, null, 'Sin datos', ps.foliotransporte),";
            subes = " and ps.estatustransporte = 'Aceptado'";
        }
        String[] encabezado = new String[]{"Número de Cuenta",
            "Nombre(s)",
            "Apellido Paterno",
            "Apellido Materno",
            "Importe",
            "Ref Num",
            "Ref AlfaNum",
            "Concepto/Pago",
            "Plazo",
            "CURP",
            folio,
            "Convocatoria"
        };
        setContentDisposition("attachment; filename=\"" + nombreOrden + "SUBES.csv\"");
        List<Object[]> listado = getDaos().getDepositosDao().ordenDepositoSUBES(ordenId, folioBeca, subes, ordenDeposito.getPeriodo());
        excelStream = CSVExport.construyeCSV(encabezado, listado);
        return ARCHIVO;
    }

    /**
     * Función para descargar el listado de alumnos de una orden de deposito
     * incluyendo la modalidad.
     *
     * @author Tania G. Sánchez
     * @return SUCCESS
     */
    public String modalidad() {
        OrdenDeposito ordenDeposito = getDaos().getOrdenDepositoDao().findById(ordenId);
        nombreOrden = ordenDeposito.getNombreOrdenDeposito();
        String[] encabezado = new String[]{"Número de Cuenta",
            "Nombre(s)",
            "Apellido Paterno",
            "Apellido Materno",
            "Importe",
            "Ref Num",
            "Ref AlfaNum",
            "Concepto/Pago",
            "Plazo",
            "Modalidad"
        };
        setContentDisposition("attachment; filename=\"" + nombreOrden + "Modalidad.xls\"");
        List<Object[]> listado = getDaos().getDepositosDao().ordenDepositoModalidad(ordenId);
        ExcelExport excelExport = new ExcelExport();
        excelStream = excelExport.construyeExcel(encabezado, listado);
        return ARCHIVO;
    }

    public BigDecimal getOrdenId() {
        return ordenId;
    }

    public void setOrdenId(BigDecimal ordenId) {
        this.ordenId = ordenId;
    }

    public BigDecimal getPrivilegio() {
        return privilegio;
    }

    public void setPrivilegio(BigDecimal privilegio) {
        this.privilegio = privilegio;
    }

    public VWOrdenDeposito getOrdenDeposito() {
        return ordenDeposito;
    }

    public void setOrdenDeposito(VWOrdenDeposito ordenDeposito) {
        this.ordenDeposito = ordenDeposito;
    }

    public String getNombreOrden() {
        return nombreOrden;
    }

    public void setNombreOrden(String nombreOrden) {
        this.nombreOrden = nombreOrden;
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

    public String getDocumentFormat() {
        return documentFormat;
    }

    public void setDocumentFormat(String documentFormat) {
        this.documentFormat = documentFormat;
    }

    public String getDatosGrafica() {
        return datosGrafica;
    }

    public void setDatosGrafica(String datosGrafica) {
        this.datosGrafica = datosGrafica;
    }

    public List<OrdenDepositoPivot> getDatosPivot() {
        return datosPivot;
    }

    public void setDatosPivot(List<OrdenDepositoPivot> datosPivot) {
        this.datosPivot = datosPivot;
    }

    public Integer getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(Integer cuentaId) {
        this.cuentaId = cuentaId;
    }
}
