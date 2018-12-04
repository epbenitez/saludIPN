package com.becasipn.actions.ajax;

import static com.becasipn.actions.ajax.JSONAjaxAction.SUCCESS_JSON;
import com.becasipn.business.TarjetaBO;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.AlumnoTarjetaBancaria;
import com.becasipn.persistence.model.BitacoraTarjetaBancaria;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.EstatusTarjetaBancaria;
import com.becasipn.persistence.model.Rol;
import com.becasipn.persistence.model.SolicitudCuentas;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.util.PaginateUtil;
import static com.becasipn.util.Util.CampoValidoAJAX;
import com.opensymphony.xwork2.ActionContext;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TarjetaAjaxAction extends JSONAjaxAction {

    private String tarjeta;
    private String lote;
    private int tipoAsignacion;
    private String boleta;
    private BigDecimal nivel;
    private BigDecimal unidadAcademica;
    private BigDecimal periodoId;
    private String identificador;
    private BigDecimal estatus;
    private final Map<String, Object> parametros = new HashMap<>();
    private String noTarjeta;
    private String curp;
    private String numeroDeBoleta;
    private String nombre;
    private String apPaterno;
    private String apMaterno;
    private String liberadas;
    private String rechazadas;
    private BigDecimal datosBancarios;

    public String listado() {
	setSsu();
	setPu(getDaos().getAlumnoTarjetaDao().getListado(ssu));
	List<AlumnoTarjetaBancaria> lista = getPu().getResultados();
	Rol rol = getRolAlto();
	for (AlumnoTarjetaBancaria tarjeta : lista) {
	    if (tarjeta.getAlumno() != null) {
		getJsonResult().add("[\"" + "<input type=checkbox id='" + tarjeta.getTarjetaBancaria().getId() + "' class='checkTarjeta'/>"
			+ "\", \"<a title='Detalle' class='fancybox fancybox.iframe'  href='/tarjeta/verMonitoreoTarjetaBancaria.action?numeroTarjetaBancaria="
			+ tarjeta.getTarjetaBancaria().getNumtarjetabancaria() + "&numeroDeBoleta=" + tarjeta.getAlumno().getBoleta() + "'>"
			+ tarjeta.getTarjetaBancaria().getNumtarjetabancaria() + "</a>"
			+ "\", \"" + tarjeta.getAlumno().getFullName()
			+ "\", \"" + estatusTarjeta(tarjeta.getTarjetaBancaria().getId()).getNombre()
			+ "\", \"" + addSelect(tarjeta.getTarjetaBancaria().getId(), estatusTarjeta(tarjeta.getTarjetaBancaria().getId()), rol) //Estatus siguiente <select>
			+ "\", \"" + "<input type='text' class='form-control' id='" + tarjeta.getTarjetaBancaria().getId() + "_2' value=''>"//Info
			+ " \"]");
	    }
	}
	return SUCCESS_PAGINATED_JSON;
    }

    public String listadoPersonalizacion() {
	setSsu();
	setPersonalizacionParams();
	setPu(getDaos().getAlumnoTarjetaDao().getListadoPersonalizacion(ssu));
	List<AlumnoTarjetaBancaria> lista = getPu().getResultados();
	int i = 0;
	for (AlumnoTarjetaBancaria t : lista) {
	    BitacoraTarjetaBancaria bitacora = getDaos().getBitacoraTarjetaBancariaDao().ultimaBitacoraTarjeta(t.getTarjetaBancaria().getId());
	    getJsonResult().add("[\"<input type='radio' class='rl' name='resPersonalizacion" + i + "' value='4' id='" + t.getTarjetaBancaria().getId() + "'> "
		    + "\",\" <input type='radio' class='rr' onclick=editarObservaciones(this) name='resPersonalizacion" + i + "' value='6' id='" + t.getTarjetaBancaria().getId() + "'>"
		    + "\",\" " + t.getTarjetaBancaria().getNumtarjetabancaria()
		    + "\", \"" + t.getAlumno().getBoleta()
		    + "\", \"" + t.getAlumno().getFullName()
		    + "\", \"" + "<input type='text' data-bv-message='Este dato no es válido' required='true' data-bv-notempty='true' "
		    + "class='form-control observaciones' id='bitacora" + t.getTarjetaBancaria().getId() + "' value='Tarjeta liberada de la personalización'>"//Info
		    + " \"]");
	    //"+bitacora.getObservaciones().split("\\|")[0]+"
	    i++;
	}
	return SUCCESS_PAGINATED_JSON;
    }

    public EstatusTarjetaBancaria estatusTarjeta(BigDecimal id) {
	BitacoraTarjetaBancaria bitacora = getDaos().getBitacoraTarjetaBancariaDao().ultimaBitacoraTarjeta(id);
	return bitacora.getEstatus();
    }

    public String addSelect(BigDecimal id, EstatusTarjetaBancaria estatus, Rol rol) {
	StringBuilder sb = new StringBuilder();
	List<EstatusTarjetaBancaria> posiblesEstatus = getDaos().getEstatusTarjetaBancariaDao().posiblesEstatus(estatus, rol);
	if (posiblesEstatus != null && !posiblesEstatus.isEmpty()) {
	    sb.append("<select id='" + id + "_1' class='form-control' style='width:100%'>");
	    for (EstatusTarjetaBancaria posible : posiblesEstatus) {
		if (posible.getId().equals(new BigDecimal(5))) {
		    continue;
		}
		sb.append("<option value='").append(posible.getId().intValue()).append("'>").append(posible.getNombre()).append("</option>");
	    }
	} else {
	    sb.append("<select disabled id='" + id + "_1' class='form-control'><option value='0'>El estatus no permite cambios</option>");
	}
	sb.append("</select>");
	return sb.toString();
    }

    /**
     * Devuelve el listado de alumnos que cumplen con los criterios de busqueda
     * o que pertenecen a un identificador de carga
     *
     * @author Victor Lozano
     * @return SUCCESS_JSON
     */
    public String asignacion() {
	setSsu();
	setAsignacionParams();
	List<AlumnoTarjetaBancaria> lista;
	if (tipoAsignacion == 1 || tipoAsignacion == 3) {//Rempazos perdidos y rechazados
	    setPu(getDaos().getAlumnoTarjetaDao().listadoAlumnoTarjetaRemplazar(ssu, periodoId, tipoAsignacion));
	    lista = getPu().getResultados();
	} else {
	    lista = new ArrayList<>();
	    PaginateUtil alumnos = getDaos().getAlumnoDao().listadoAlumnoNueavasTarjetas(ssu);
	    List<Alumno> alumnosResultados = alumnos.getResultados();
	    for (Alumno alumnosResultado : alumnosResultados) {
		AlumnoTarjetaBancaria nuevo = new AlumnoTarjetaBancaria();
		nuevo.setAlumno(alumnosResultado);
		lista.add(nuevo);
	    }
	    PaginateUtil<AlumnoTarjetaBancaria> atbUtil = new PaginateUtil<>(lista, alumnos.getNoResultados(), alumnos.getNoResultadosFiltrados());
	    setPu(atbUtil);
	}
	for (AlumnoTarjetaBancaria alumnoTarjeta : lista) {
            DatosAcademicos datosAcademicos = getDaos().getDatosAcademicosDao().datosPorPeriodo(alumnoTarjeta.getAlumno().getId(), periodoId);            
	    getJsonResult().add("[\"" + datosAcademicos.getUnidadAcademica().getNombreCorto()
		    + "\", \"" + alumnoTarjeta.getAlumno().getFullName()
		    + "\", \"" + alumnoTarjeta.getAlumno().getCurp()
		    + "\", \"" + alumnoTarjeta.getAlumno().getBoleta()
		    + "\", \"" + ((alumnoTarjeta.getTarjetaBancaria() != null)
			    ? alumnoTarjeta.getTarjetaBancaria().getNumtarjetabancaria() + "\"]" : "n/a\"]")
	    );
	}
	return SUCCESS_PAGINATED_JSON;
    }

    /**
     * Devuelve el listado de alumnos que cumplen con los criterios de busqueda
     * o que pertenecen a un identificador de carga
     *
     * @author Victor Lozano
     * @return SUCCESS_JSON
     */
    public String asignados() {
	if (identificador != null || !identificador.equals("")) {
	    Long asignaciones = getDaos().getAlumnoTarjetaDao().asignacionesIdentificador(identificador);
	    BitacoraTarjetaBancaria btb = getDaos().getBitacoraTarjetaBancariaDao().datosAsignacionIdentificador(identificador);
	    if (btb != null) {
		String[] split = btb.getObservaciones().split("\\|\\|");
		String periodo = split[split.length - 1];
		String link = "/admin/descargarTarjeta.action?identificador=" + identificador + "&periodo=" + periodo;
		String observaciones = btb.getObservaciones().split("\\|")[0];
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		getJsonResult().add("[\"" + asignaciones + "\",\"" + sdf.format(btb.getFechaModificacion()) + "\",\"" + observaciones + "\",\"" + (btb.getEnvioCorreo() ? "Si" : "No") + "\",\"" + link + "\"]");
	    }

	}
	return SUCCESS_JSON;
    }

    /**
     * Devuelve el listado de por tarjeta bancaria
     *
     * @author Victor Lozano
     * @return SUCCESS_JSON
     */
    public String alumnosBitacora() {
	if (parametros.isEmpty()) {
	    return SUCCESS_JSON;
	}
            
	List<Alumno> lista = getDaos().getAlumnoDao().busquedaAlumnosTarjeta(parametros);
	for (Alumno alumno : lista) {
            DatosAcademicos datosAcademicos = getDaos().getDatosAcademicosDao().ultimosDatos(alumno.getId());
            if (datosAcademicos == null) {
                getJsonResult().add("[\"" + alumno.getBoleta()
		    + "\", \"" + alumno.getNombre() + " " + alumno.getApellidoPaterno() + " " + alumno.getApellidoMaterno()
		    + "\", \"" + "Sin unidad académica"
		    + "\", \"<a title='Monitoreo' class='fancybox fancybox.iframe table-link'  href='/tarjeta/bitacoraMonitoreoTarjetaBancaria.action?numeroDeBoleta=" + alumno.getBoleta() + "'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-pencil-square-o fa-stack-1x fa-inverse'></i></span></a>"
		    + " \"]");
            } else {
                getJsonResult().add("[\"" + alumno.getBoleta()
		    + "\", \"" + alumno.getNombre() + " " + alumno.getApellidoPaterno() + " " + alumno.getApellidoMaterno()
		    + "\", \"" + datosAcademicos.getUnidadAcademica().getNombreCorto()
		    + "\", \"<a title='Monitoreo' class='fancybox fancybox.iframe table-link'  href='/tarjeta/bitacoraMonitoreoTarjetaBancaria.action?numeroDeBoleta=" + alumno.getBoleta() + "'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-pencil-square-o fa-stack-1x fa-inverse'></i></span></a>"
		    + " \"]");
            }	    
	}
	return SUCCESS_JSON;
    }

    public String identificador() {
	int seq = 1;
	while (getDaos().getAlumnoTarjetaDao().existeIdentificador(identificador + seq)) {
	    seq++;
	}
	String tarjetasDisponibles = getDaos().getTarjetaBancariaDao().countTarjetasDisponibles();
	getJsonResult().add("[\"" + identificador + seq + "\",\"" + tarjetasDisponibles + "\"]");
	return SUCCESS_JSON;
    }

    public String identificadorCuenta() {
	int seq = 1;
	String idf = identificador;
	Boolean x = true;
	do {//CAMBIAR
	    if (getDaos().getSolicitudCuentasDao().existeIdentificador(idf + seq)) {
		seq++;
	    } else {
		x = false;
		identificador = idf + seq;
	    }
	} while (x);
	String tarjetasDisponibles = getDaos().getOtorgamientoDao().countSolicitudes(periodoId.toString(), nivel.toString(), unidadAcademica.toString(), boleta, tipoAsignacion);
	getJsonResult().add("[\"" + identificador + "\",\"" + tarjetasDisponibles + "\"]");
	return SUCCESS_JSON;
    }

    public String identificadorCuentaB() {
	String idf = identificador;
	String solicitudes = getDaos().getOtorgamientoDao().countSolicitudes(idf);
	SolicitudCuentas sc = getDaos().getSolicitudCuentasDao().findByIdentificador(identificador);
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd / HH:mm");
	getJsonResult().add("[\"" + identificador + "\",\"" + solicitudes + "\",\"" + formatter.format(sc.getFechaGeneracion()) + "\",\"" + sc.getUsuarioGeneracion().getUsuario() + "\"]");
	return SUCCESS_JSON;
    }

    public String solicitudCuenta() {
	List<Object[]> lista = getDaos().getOtorgamientoDao().listadoSolicitantes(periodoId.toString(), nivel.toString(), unidadAcademica.toString(), boleta, tipoAsignacion);

	for (Object[] o : lista) {
	    Alumno alumno = getDaos().getAlumnoDao().findById((BigDecimal) o[0]);
            DatosAcademicos datosAcademicos = alumno.getListaDatosAcademicos().get(0);
	    getJsonResult().add("[\"" + datosAcademicos.getUnidadAcademica().getNombreCorto()
		    + "\", \"" + alumno.getFullName()
		    + "\", \"" + alumno.getCurp()
		    + "\", \"" + alumno.getBoleta()
		    + "\"]");
//	    System.out.println(alumno.getBoleta());
	}
	return SUCCESS_JSON;
    }

    /**
     * Metodo para cargar la bitacora de tarjeta de un alumno
     *
     * @author Victor Lozano
     * @return SUCCESS
     */
    public String bitacora() {
	List<Alumno> listAlumno = getDaos().getAlumnoDao().getByBoleta(numeroDeBoleta);
	Alumno alumno = listAlumno.get(0);
	List<BitacoraTarjetaBancaria> list = getDaos().getBitacoraTarjetaBancariaDao().monitoreoTarjetaBancaria(alumno.getId());
	java.text.SimpleDateFormat sdf1 = new java.text.SimpleDateFormat("dd MMMM yyy", new Locale("es", "ES"));
	java.text.SimpleDateFormat sdf2 = new java.text.SimpleDateFormat("hh:mm:ss");
	for (BitacoraTarjetaBancaria monitoreo : list) {
	    getJsonResult().add("["
		    + "\"" + monitoreo.getTarjetaBancaria().getNumtarjetabancaria()
		    + "\", \"" + monitoreo.getEstatus().getNombre()
		    + "\", \"" + sdf1.format(monitoreo.getFechaModificacion())
		    + "\", \"" + sdf2.format(monitoreo.getFechaModificacion())
		    + "\", \"" + (monitoreo.getObservaciones() == null ? "" : monitoreo.getObservaciones())
		    + "\"]");
	}
	return SUCCESS_JSON;
    }

    public String personalizar() {
	TarjetaBO bo = new TarjetaBO(getDaos());
	Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
	if (!liberadas.trim().isEmpty() || !rechazadas.trim().isEmpty()) {
	    Boolean res = bo.personalizar(liberadas, rechazadas, usuario);
	    if (res) {
		getJsonResult().add("[\"success\"]");
	    } else {
		getJsonResult().add("[\"error\"]");
	    }
	}
	return SUCCESS_JSON;
    }

    public int getTipoAsignacion() {
	return tipoAsignacion;
    }

    public void setTipoAsignacion(int tipoAsignacion) {
	this.tipoAsignacion = tipoAsignacion;
    }

    public String getBoleta() {
	return boleta;
    }

    public void setBoleta(String boleta) {
	this.boleta = boleta;
	if (CampoValidoAJAX(boleta)) {
	    ssu.parametros.put("a.boleta", this.boleta);
	}
    }

    public BigDecimal getNivel() {
	return nivel;
    }

    public void setNivel(BigDecimal nivel) {
	this.nivel = nivel;
	if (CampoValidoAJAX(nivel)) {
	    ssu.parametros.put("a.unidadAcademica.nivel.id", this.nivel);
	}
    }

    public BigDecimal getEstatus() {
	return estatus;
    }

    public void setEstatus(BigDecimal estatus) {
	this.estatus = estatus;
	if (CampoValidoAJAX(estatus)) {
	    ssu.parametros.put("btc.estatus.id", this.estatus);
	}
    }

    public BigDecimal getUnidadAcademica() {
	return unidadAcademica;
    }

    public void setUnidadAcademica(BigDecimal unidadAcademica) {
	this.unidadAcademica = unidadAcademica;
	if (CampoValidoAJAX(unidadAcademica)) {
	    ssu.parametros.put("b.alumno.unidadAcademica.id", this.unidadAcademica);
	}
    }

    public String getTarjeta() {
	return tarjeta;
    }

    public void setTarjeta(String tarjeta) {
	this.tarjeta = tarjeta;
	if (CampoValidoAJAX(tarjeta)) {
	    ssu.parametros.put("b.tarjetaBancaria.numtarjetabancaria", this.tarjeta);
	}
    }

    public String getLote() {
	return lote;
    }

    public void setLote(String lote) {
	this.lote = lote;
	if (CampoValidoAJAX(lote)) {
	    ssu.parametros.put("b.tarjetaBancaria.lote", this.lote);
	}
    }

    public String getIdentificador() {
	return identificador;
    }

    public void setIdentificador(String identificador) {
	this.identificador = identificador;
	if (CampoValidoAJAX(identificador)) {
	    ssu.parametros.put("b.identificador", this.identificador);
	}
    }

    public String getCurp() {
	return curp;
    }

    public void setCurp(String curp) {
	this.curp = curp;
	if (CampoValidoAJAX(curp)) {
	    parametros.put("a.curp", this.curp.toUpperCase());
	}
    }

    public String getNumeroDeBoleta() {
	return numeroDeBoleta;
    }

    public void setNumeroDeBoleta(String numeroDeBoleta) {
	this.numeroDeBoleta = numeroDeBoleta;
	if (CampoValidoAJAX(numeroDeBoleta)) {
	    parametros.put("a.boleta", this.numeroDeBoleta.toUpperCase());
	}
    }

    public String getNombre() {
	return nombre;
    }

    public void setNombre(String nombre) {
	this.nombre = nombre;
	if (CampoValidoAJAX(nombre)) {
	    parametros.put("a.nombre", this.nombre.toUpperCase());
	}
    }

    public String getApPaterno() {
	return apPaterno;
    }

    public void setApPaterno(String apPaterno) {
	this.apPaterno = apPaterno;
	if (CampoValidoAJAX(apPaterno)) {
	    parametros.put("a.apellidoPaterno", this.apPaterno.toUpperCase());
	}
    }

    public String getApMaterno() {
	return apMaterno;
    }

    public void setApMaterno(String apMaterno) {
	this.apMaterno = apMaterno;
	if (CampoValidoAJAX(apMaterno)) {
	    parametros.put("a.apellidoMaterno", this.apMaterno.toUpperCase());
	}
    }

    public BigDecimal getPeriodoId() {
	return periodoId;
    }

    public void setPeriodoId(BigDecimal periodoId) {
	this.periodoId = periodoId;
    }

    public String getNoTarjeta() {
	return noTarjeta;
    }

    public void setNoTarjeta(String noTarjeta) {
	this.noTarjeta = noTarjeta;
	if (CampoValidoAJAX(noTarjeta)) {
	    parametros.put("t.numtarjetabancaria", this.noTarjeta);
	}
    }

    private void setAsignacionParams() {
	if (!boleta.isEmpty()) {
	    ssu.parametrosServidor.put(" a.boleta", boleta);
	}
	if (!nivel.equals(BigDecimal.ZERO)) {
	    ssu.parametrosServidor.put(" a.unidadAcademica.nivel.id", nivel);
	}
	if (!unidadAcademica.equals(BigDecimal.ZERO) && !unidadAcademica.equals(new BigDecimal("-1")) && !unidadAcademica.equals(new BigDecimal("-2"))) {
	    ssu.parametrosServidor.put(" a.unidadAcademica.id", unidadAcademica);
	}
    }

    private void setPersonalizacionParams() {
	if (identificador != null && !identificador.isEmpty()) {
	    ssu.parametrosServidor.put(" b.identificador", identificador);
	}
	if (CampoValidoAJAX(unidadAcademica)) {
	    ssu.parametrosServidor.put(" b.alumno.unidadAcademica.id", unidadAcademica);
	}
    }

    public String getLiberadas() {
	return liberadas;
    }

    public void setLiberadas(String liberadas) {
	this.liberadas = liberadas;
    }

    public String getRechazadas() {
	return rechazadas;
    }

    public void setRechazadas(String rechazadas) {
	this.rechazadas = rechazadas;
    }
    
    public String listadoSolicitud() {
	List<AlumnoTarjetaBancaria> alumnoTarjetaBancaria = getDaos().getAlumnoTarjetaDao().tarjetasDatosBancarios(datosBancarios);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        
	for (AlumnoTarjetaBancaria t : alumnoTarjetaBancaria) {
            getJsonResult().add("[\"" + df.format(t.getSolicitudCuentas().getFechaGeneracion())
		    + "\", \"" + t.getEstatusTarjBanc().getNombre()
		    + "\"]");
	}
	return SUCCESS_JSON;
    }

    public BigDecimal getDatosBancarios() {
        return datosBancarios;
    }

    public void setDatosBancarios(BigDecimal datosBancarios) {
        this.datosBancarios = datosBancarios;
    }
}