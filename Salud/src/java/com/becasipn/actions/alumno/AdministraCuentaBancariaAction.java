package com.becasipn.actions.alumno;

import com.becasipn.actions.BaseReportAction;
import com.becasipn.business.AlumnoBO;
import com.becasipn.business.AlumnoDatosBancariosBO;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.AlumnoDatosBancarios;
import com.becasipn.persistence.model.AlumnoTarjetaBancaria;
import com.becasipn.persistence.model.BitacoraTarjetaBancaria;
import com.becasipn.persistence.model.EstatusTarjetaBancaria;
import com.becasipn.persistence.model.TarjetaBancaria;
import com.becasipn.persistence.model.Usuario;
import static com.opensymphony.xwork2.Action.ERROR;
import com.opensymphony.xwork2.ActionContext;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Tania G. Sánchez
 */
public class AdministraCuentaBancariaAction extends BaseReportAction implements MensajesAlumno {

    public static final String SOLICITAR = "solicitar";
    public static final String RENUNCIA = "renuncia";
    public static final String CONFIGURACION = "configuracion";
    private String numeroDeBoleta;
    private Boolean menorEdad = Boolean.FALSE;
    private Boolean datosBanco = Boolean.TRUE;
    private Boolean cuentaTutor = Boolean.FALSE;
    private Boolean tarjeta = Boolean.TRUE;
    private Boolean enTramite = Boolean.FALSE;
    AlumnoTarjetaBancaria tarjetaBancaria = new AlumnoTarjetaBancaria();
    String tarjetaAsteriscos = "";
    private boolean cuentaLiberada = Boolean.FALSE;
    private boolean periodoActivo = true;
    private String banco;
    private String numero;
    private boolean cuentaBanamexIPN = false;
    private String mensaje;

    public String solicitar() {
        numeroDeBoleta = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Alumno> listAlumno = getDaos().getAlumnoDao().findBy("boleta", numeroDeBoleta, Boolean.TRUE);
        if (listAlumno == null || listAlumno.isEmpty()) {
            addActionError(getText("registro.boleta.no.encontrada"));
            return ERROR;
        }
        Alumno alumno = listAlumno.get(0);
        AlumnoBO abo = new AlumnoBO(getDaos());
        AlumnoDatosBancariosBO bo = new AlumnoDatosBancariosBO(getDaos());
        //Se investiga si el alumno es menor de edad
        if (abo.calcularEdad(alumno.getFechaDeNacimiento()) < 18) {
            AlumnoDatosBancarios alumnoDatosBancarios = bo.datosVigentes(alumno.getId());
            if (alumnoDatosBancarios == null) {
                //No tienes datos bancarios
                datosBanco = Boolean.FALSE;
            } else {
                menorEdad = Boolean.TRUE;
            }
        } //El alumno es mayor de edad
        else {
            //Obtenemos los datos bancarios
            AlumnoDatosBancarios alumnoDatosBancarios = bo.datosVigentes(alumno.getId());
            if (alumnoDatosBancarios == null) {
                //No tienes datos bancarios
                datosBanco = Boolean.FALSE;
            } else //Verificamos que la cuenta es del padre o tutor.
             if (alumnoDatosBancarios.getNombre() != null && alumnoDatosBancarios.getApellidoPaterno() != null) {
                    cuentaTutor = Boolean.TRUE;
                    AlumnoTarjetaBancaria tarjetaBancaria = getDaos().getAlumnoTarjetaDao().tarjetaDatosBancarios(alumnoDatosBancarios.getId());
                    if (tarjetaBancaria == null) {
                        //La solicitud de tu tarjeta aún no ha sido procesada, por lo que no es posible continuar con el proceso de renuncia
                        tarjeta = Boolean.FALSE;
                    } else if (tarjetaBancaria.getEstatusTarjBanc().getId().equals(new BigDecimal("12"))) { //En trámite
                        //No puede renunciar
                        addActionError(getText("misdatos.alumno.tarjeta.no.renuncia"));
                        enTramite = Boolean.TRUE;
                    } else if (tarjetaBancaria.getEstatusTarjBanc().getId().equals(new BigDecimal("16")) && alumnoDatosBancarios.getVigente() != null
                            && !alumnoDatosBancarios.getVigente()) { //Renuncia
                        //Ya renunciaste
                        addActionMessage(getText("misdatos.alumno.tarjeta.ya.renuncia"));
                        return RENUNCIA;
                    }
                }
        }
        return SOLICITAR;
    }

    public String renuncia() {
        numeroDeBoleta = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Alumno> listAlumno = getDaos().getAlumnoDao().findBy("boleta", numeroDeBoleta, Boolean.TRUE);
        if (listAlumno == null || listAlumno.isEmpty()) {
            addActionError(getText("registro.boleta.no.encontrada"));
            return ERROR;
        }
        Alumno alumno = listAlumno.get(0);
        AlumnoBO abo = new AlumnoBO(getDaos());
        AlumnoDatosBancariosBO bo = new AlumnoDatosBancariosBO(getDaos());
        //Se investiga si el alumno es menor de edad
        if (abo.calcularEdad(alumno.getFechaDeNacimiento()) < 18) {
            return solicitar();
        } else {
            //Obtenemos los datos bancarios
            AlumnoDatosBancarios alumnoDatosBancarios = bo.datosVigentes(alumno.getId());
            if (alumnoDatosBancarios == null) {
                return solicitar();
            } else //Verificamos que la cuenta es del padre o tutor.
             if (alumnoDatosBancarios.getNombre() != null && alumnoDatosBancarios.getApellidoPaterno() != null) {
                    AlumnoTarjetaBancaria tarjetaBancaria = getDaos().getAlumnoTarjetaDao().tarjetaDatosBancarios(alumnoDatosBancarios.getId());
                    if (tarjetaBancaria == null) {
                        return solicitar();
                    } else if (tarjetaBancaria.getEstatusTarjBanc().getId().equals(new BigDecimal("12"))) { //En trámite
                        return solicitar();
                    } else {
                        //Puede renunciar
                        //La cuenta del padre o tutor se pone vigente = 0
                        alumnoDatosBancarios.setVigente(Boolean.FALSE);
                        getDaos().getAlumnoDatosBancariosDao().update(alumnoDatosBancarios);
                        //Se le cambia el estatus a la tarjeta del padre o tutor a "Renuncia"
                        EstatusTarjetaBancaria estatus = new EstatusTarjetaBancaria();
                        estatus.setId(new BigDecimal("16")); //Renuncia
                        tarjetaBancaria.setEstatusTarjBanc(estatus);
                        getDaos().getAlumnoTarjetaDao().update(tarjetaBancaria);
                        addActionMessage(getText("misdatos.alumno.tarjeta.renuncia"));
                    }
                } else {
                    return solicitar();
                }
        }
        return SUCCESS;
    }

    public String configuracion() {
        String fInicialStr = (String) ActionContext.getContext().getApplication().get("fechaInicioAltaCuentasBancarias ");
        String fFinalStr = (String) ActionContext.getContext().getApplication().get("fechaFinalAltaCuentasBancarias  ");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dia = new SimpleDateFormat("dd");
        SimpleDateFormat mes = new SimpleDateFormat("MMMM", new Locale("es", "ES"));
        //Inicialmente se valida si el registro esta aún activo.
        try {
            //Obtiene la fecha Final e Inicial del periodo y la fecha actual.lo compara con la fecha de hoy para establecer si el periodo esta activo o no
            Date dateInicial = formatter.parse(fInicialStr);
            Date dateFinal = formatter.parse(fFinalStr);
            Calendar c = Calendar.getInstance();
            c.setTime(dateFinal);
            c.add(Calendar.DATE, 1);
            dateFinal = c.getTime();
            Date hoy = new Date();
            //Compara la fecha Final e Inicial con la fecha de hoy para establecer si el periodo esta activo o no
            if (isAlumno() && !(hoy.after(dateInicial) && hoy.before(dateFinal))) {
                periodoActivo = false;
            }
            mensaje = "Este módulo sólo estuvo disponible del " + dia.format(dateInicial) + " de " + mes.format(dateInicial);
            mensaje += " al " + dia.format(dateFinal) + " de " + mes.format(dateFinal);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (isAlumno()) {
            numeroDeBoleta = SecurityContextHolder.getContext().getAuthentication().getName();
        }
        List<Alumno> listAlumno = getDaos().getAlumnoDao().findBy("boleta", numeroDeBoleta, Boolean.TRUE);
        if (listAlumno == null || listAlumno.isEmpty()) {
            addActionError(getText("registro.boleta.no.encontrada"));
            return ERROR;
        }
        Alumno alumno = listAlumno.get(0);
        tarjetaBancaria = getDaos().getAlumnoTarjetaDao().tarjetaLiberada(alumno.getId());
        if (tarjetaBancaria != null && tarjetaBancaria.getEstatusTarjBanc() != null && tarjetaBancaria.getEstatusTarjBanc().getId().equals((new BigDecimal("13")))) { //Liberada
            cuentaBanamexIPN = getDaos().getTarjetaBancariaDao().alumnoConCuentaBanamex(tarjetaBancaria.getTarjetaBancaria().getId());
            // Datos de lectura, permite dar de de baja la cuenta
            int longitudTarjeta = tarjetaBancaria.getTarjetaBancaria().getNumtarjetabancaria().length();
            for (int i = 0; i < longitudTarjeta; i++) {
                if (i < (longitudTarjeta - 4)) {
                    tarjetaAsteriscos += "*";
                } else {
                    tarjetaAsteriscos += tarjetaBancaria.getTarjetaBancaria().getNumtarjetabancaria().substring(longitudTarjeta - 4, longitudTarjeta);
                    break;
                }
            }
            //Si es cuenta el banco siempre es Banamex
            if (tarjetaBancaria.getTarjetaBancaria().getCuenta() == 1) {
                banco = "Banamex";
            } //Si es clabe se busca el banco con la clave.
            else {
                String clave = tarjetaBancaria.getTarjetaBancaria().getNumtarjetabancaria().substring(0, 3);
                banco = getDaos().getBancosDao().bancoPorClave(clave);
            }
            cuentaLiberada = true;
        } else {
            // Permite completar datos y guardar.
            cuentaLiberada = false;
        }
        return CONFIGURACION;
    }

    public String baja() {
        //Falta solicitar y validar la contraseña.
        if (isAlumno()) {
            numeroDeBoleta = SecurityContextHolder.getContext().getAuthentication().getName();
        }
        List<Alumno> listAlumno = getDaos().getAlumnoDao().findBy("boleta", numeroDeBoleta, Boolean.TRUE);
        if (listAlumno == null || listAlumno.isEmpty()) {
            addActionError(getText("registro.boleta.no.encontrada"));
            return ERROR;
        }
        Alumno alumno = listAlumno.get(0);
        tarjetaBancaria = getDaos().getAlumnoTarjetaDao().tarjetaLiberada(alumno.getId());
        if (tarjetaBancaria != null && tarjetaBancaria.getEstatusTarjBanc() != null && tarjetaBancaria.getEstatusTarjBanc().getId().equals((new BigDecimal("13")))) { //Liberada
            //Se cambia el estatus de la tarjeta a cancelada.
            EstatusTarjetaBancaria estatusTarjetaBancaria = new EstatusTarjetaBancaria();
            estatusTarjetaBancaria.setId(new BigDecimal(16));//Cuenta cancelada
            tarjetaBancaria.setEstatusTarjBanc(estatusTarjetaBancaria);
            try {
                getDaos().getAlumnoTarjetaDao().update(tarjetaBancaria);
                //Se guarda en bitacora el cambio
                BitacoraTarjetaBancaria bitacoraTarjeta = new BitacoraTarjetaBancaria();
                bitacoraTarjeta.setEstatus(estatusTarjetaBancaria);
                bitacoraTarjeta.setTarjetaBancaria(tarjetaBancaria.getTarjetaBancaria());
                bitacoraTarjeta.setUsuario((Usuario) ActionContext.getContext().getSession().get("usuario"));
                bitacoraTarjeta.setFechaModificacion(new Date());
                bitacoraTarjeta.setNumReporteBanco(null);
                bitacoraTarjeta.setObservaciones("Configuración de Cuenta");
                bitacoraTarjeta.setEnvioCorreo(null);
                getDaos().getBitacoraTarjetaBancariaDao().save(bitacoraTarjeta);
                addActionMessage(getText("Se ha dado de baja la cuenta correctamente."));
            } catch (Exception e) {
                addActionError(getText("Ocurrio un error que no permitio dat de baja la cuenta. Intentalo más tarde."));
            }
        } else {
            addActionError(getText("No puedes dar de baja tu cuenta dado que no esta liberada."));
        }
        return configuracion();
    }

    public String guardar() {
        if (isAlumno()) {
            numeroDeBoleta = SecurityContextHolder.getContext().getAuthentication().getName();
        }
        List<Alumno> listAlumno = getDaos().getAlumnoDao().findBy("boleta", numeroDeBoleta, Boolean.TRUE);
        if (listAlumno == null || listAlumno.isEmpty()) {
            addActionError(getText("registro.boleta.no.encontrada"));
            return ERROR;
        }
        Alumno alumno = listAlumno.get(0);
        tarjetaBancaria = getDaos().getAlumnoTarjetaDao().tarjetaLiberada(alumno.getId());
        if (tarjetaBancaria != null && tarjetaBancaria.getEstatusTarjBanc() != null && tarjetaBancaria.getEstatusTarjBanc().getId().equals((new BigDecimal("13")))) { //Liberada
            addActionError(getText("No puedes ingresar una cuenta/clabe dado que tienes una cuenta/clabe liberada."));
        } else {
            //Se inserta la cuenta/clabe.
            TarjetaBancaria cuentaClabe = new TarjetaBancaria();
            cuentaClabe.setNumtarjetabancaria(numero);
            cuentaClabe.setFechaLote(new Date());
            cuentaClabe.setOrigen(2); //Externo
            if (numero.length() == 11) {
                cuentaClabe.setCuenta(1); //Cuenta
            } else if (numero.length() == 18) {
                cuentaClabe.setCuenta(3);//Clabe
                if (getDaos().getBancosDao().bancoPorClave(numero.substring(0, 3)).equals("BANAMEX")) {
                    addActionError(getText("La cuenta clabe ingresada corresponde a Banamex. Si tu cuenta es Banamex debes ingresar el número de cuenta."));
                    return configuracion();
                }
            } else {
                addActionError(getText("Sólo se permiten 11 dígitos para cuenta BANAMEX o los 18 dígitos para cuenta CLABE."));
                return configuracion();
            }
            //Se inserta la relación con el alumno.
            AlumnoTarjetaBancaria alumnoTarjetaBancaria = new AlumnoTarjetaBancaria();
            alumnoTarjetaBancaria.setAlumno(alumno);
            alumnoTarjetaBancaria.setEstatusTarjBanc(getDaos().getEstatusTarjetaBancariaDao().findById(new BigDecimal("13")));
            alumnoTarjetaBancaria.setTarjetaBancaria(cuentaClabe);
            try {
                getDaos().getAlumnoTarjetaDao().save(alumnoTarjetaBancaria);
                addActionMessage(getText("Se ha guardado la cuenta correctamente."));
                //Se guarda en bitacora el cambio
                BitacoraTarjetaBancaria bitacoraTarjeta = new BitacoraTarjetaBancaria();
                bitacoraTarjeta.setEstatus(alumnoTarjetaBancaria.getEstatusTarjBanc());
                bitacoraTarjeta.setTarjetaBancaria(alumnoTarjetaBancaria.getTarjetaBancaria());
                bitacoraTarjeta.setUsuario((Usuario) ActionContext.getContext().getSession().get("usuario"));
                bitacoraTarjeta.setFechaModificacion(new Date());
                bitacoraTarjeta.setNumReporteBanco(null);
                bitacoraTarjeta.setObservaciones("Configuración de Cuenta");
                bitacoraTarjeta.setEnvioCorreo(null);
                getDaos().getBitacoraTarjetaBancariaDao().save(bitacoraTarjeta);
            } catch (Exception e) {

                if (isAlumno()) {
                    AlumnoTarjetaBancaria atb = getDaos().getAlumnoTarjetaDao().getTarjetaAlumno(alumno.getId(), numero);
                    if (atb != null) {
                        //La cuenta que se intenta ingresar ya estaba asociada al alumno previamente, por lo que 
                        //solamente se cambia el estatus
                        atb.setEstatusTarjBanc(new EstatusTarjetaBancaria(new BigDecimal(13)));
                        getDaos().getAlumnoTarjetaDao().update(atb);

                        addActionMessage(getText("Se ha guardado la cuenta correctamente."));

                        //Se guarda en bitacora el cambio
                        BitacoraTarjetaBancaria bitacoraTarjeta = new BitacoraTarjetaBancaria();
                        bitacoraTarjeta.setEstatus(atb.getEstatusTarjBanc());
                        bitacoraTarjeta.setTarjetaBancaria(atb.getTarjetaBancaria());
                        bitacoraTarjeta.setUsuario((Usuario) ActionContext.getContext().getSession().get("usuario"));
                        bitacoraTarjeta.setFechaModificacion(new Date());
                        bitacoraTarjeta.setNumReporteBanco(null);
                        bitacoraTarjeta.setObservaciones("Configuración de Cuenta");
                        bitacoraTarjeta.setEnvioCorreo(null);
                        getDaos().getBitacoraTarjetaBancariaDao().save(bitacoraTarjeta);
                    } else {
                        AlumnoTarjetaBancaria posibleHomonimoConMismaCuenta = getDaos().getAlumnoTarjetaDao().findByNumeroTarjetaBancaria(alumnoTarjetaBancaria);
                        if (posibleHomonimoConMismaCuenta == null) {
                            //No debería ocurrir éste escenario
                        } else if (posibleHomonimoConMismaCuenta.getAlumno().getCurp().equals(alumnoTarjetaBancaria.getAlumno().getCurp())) { //ALUMNOS CON DOS CUENTAS EN SIBEC (BOLETA Y PREBOLETA) SI PUEDEN COMPARTIR LA MISMA CUENTA
                            //DEBIDO AL CASCADE.PERSIST CONFIGURADO, ES NECESARIO PRIMERO GUARDAR EL REGISTRO 
                            //CON EL CAMPO TARJETA BANCARIA EN NULO Y DESPUÉS ACTUALIZAR EL VALOR DE LA TARJETABANCARIA
                            alumnoTarjetaBancaria.setTarjetaBancaria(null);
                            getDaos().getAlumnoTarjetaDao().save(alumnoTarjetaBancaria);
                            alumnoTarjetaBancaria.setTarjetaBancaria(posibleHomonimoConMismaCuenta.getTarjetaBancaria());
                            getDaos().getAlumnoTarjetaDao().update(alumnoTarjetaBancaria);
                            addActionMessage(getText("Se ha guardado la cuenta correctamente."));
                        } else { // Posibles Hermanos o diferentes usuarios con la misma cuenta
                            addActionError("La cuenta/clabe que intentas ingresar, ya está asociada a otro alumno. Para poder ingresarla, necesitas acudir a la Dirección de Servicios Estudiantiles para que te ayuden a configurarla.");
                        }
                    }
                } else {
                    //Buscamos el id de la cuenta/clabe que ya existe en base de datos.
                    cuentaClabe = getDaos().getTarjetaBancariaDao().findByNumeroCuenta(numero);
                    //Buscamos si el alumno tiene un registro previo en la relación alumno/tarjeta bancaria.
                    AlumnoTarjetaBancaria existente = getDaos().getAlumnoTarjetaDao().getTarjetaAlumno(alumno.getId(), cuentaClabe.getId());
                    //Si no tiene se inserta un nuevo registro.
                    if (existente == null) {
                        //DEBIDO AL CASCADE.PERSIST CONFIGURADO, ES NECESARIO PRIMERO GUARDAR EL REGISTRO 
                        //CON EL CAMPO TARJETA BANCARIA EN NULO Y DESPUÉS ACTUALIZAR EL VALOR DE LA TARJETABANCARIA
                        alumnoTarjetaBancaria.setTarjetaBancaria(null);
                        getDaos().getAlumnoTarjetaDao().save(alumnoTarjetaBancaria);
                        alumnoTarjetaBancaria.setTarjetaBancaria(cuentaClabe);
                        getDaos().getAlumnoTarjetaDao().update(alumnoTarjetaBancaria);
                        addActionMessage(getText("Se ha guardado la cuenta correctamente. Le informamos que ésta cuenta ya estaba asociada a un alumno."));
                    } //Si tiene modificamos el estatus y actualizamos
                    else {
                        alumnoTarjetaBancaria = existente;
                        alumnoTarjetaBancaria.setEstatusTarjBanc(getDaos().getEstatusTarjetaBancariaDao().findById(new BigDecimal("13")));
                        getDaos().getAlumnoTarjetaDao().update(alumnoTarjetaBancaria);
                        addActionMessage(getText("Se ha actualizado el estatus de la cuenta correctamente."));
                    }
                    //Se guarda en bitacora el cambio
                    BitacoraTarjetaBancaria bitacoraTarjeta = new BitacoraTarjetaBancaria();
                    bitacoraTarjeta.setEstatus(alumnoTarjetaBancaria.getEstatusTarjBanc());
                    bitacoraTarjeta.setTarjetaBancaria(alumnoTarjetaBancaria.getTarjetaBancaria());
                    bitacoraTarjeta.setUsuario((Usuario) ActionContext.getContext().getSession().get("usuario"));
                    bitacoraTarjeta.setFechaModificacion(new Date());
                    bitacoraTarjeta.setNumReporteBanco(null);
                    bitacoraTarjeta.setObservaciones("Configuración de Cuenta");
                    bitacoraTarjeta.setEnvioCorreo(null);
                    getDaos().getBitacoraTarjetaBancariaDao().save(bitacoraTarjeta);
                }
            }
        }
        return configuracion();
    }

    public String getNumeroDeBoleta() {
        return numeroDeBoleta;
    }

    public void setNumeroDeBoleta(String numeroDeBoleta) {
        this.numeroDeBoleta = numeroDeBoleta;
    }

    public Boolean getMenorEdad() {
        return menorEdad;
    }

    public void setMenorEdad(Boolean menorEdad) {
        this.menorEdad = menorEdad;
    }

    public Boolean getDatosBanco() {
        return datosBanco;
    }

    public void setDatosBanco(Boolean datosBanco) {
        this.datosBanco = datosBanco;
    }

    public Boolean getCuentaTutor() {
        return cuentaTutor;
    }

    public void setCuentaTutor(Boolean cuentaTutor) {
        this.cuentaTutor = cuentaTutor;
    }

    public Boolean getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(Boolean tarjeta) {
        this.tarjeta = tarjeta;
    }

    public Boolean getEnTramite() {
        return enTramite;
    }

    public void setEnTramite(Boolean enTramite) {
        this.enTramite = enTramite;
    }

    public AlumnoTarjetaBancaria getTarjetaBancaria() {
        return tarjetaBancaria;
    }

    public void setTarjetaBancaria(AlumnoTarjetaBancaria tarjetaBancaria) {
        this.tarjetaBancaria = tarjetaBancaria;
    }

    public String getTarjetaAsteriscos() {
        return tarjetaAsteriscos;
    }

    public void setTarjetaAsteriscos(String tarjetaAsteriscos) {
        this.tarjetaAsteriscos = tarjetaAsteriscos;
    }

    public boolean isCuentaLiberada() {
        return cuentaLiberada;
    }

    public void setCuentaLiberada(boolean cuentaLiberada) {
        this.cuentaLiberada = cuentaLiberada;
    }

    public boolean isPeriodoActivo() {
        return periodoActivo;
    }

    public void setPeriodoActivo(boolean periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public boolean isCuentaBanamexIPN() {
        return cuentaBanamexIPN;
    }

    public void setCuentaBanamexIPN(boolean cuentaBanamexIPN) {
        this.cuentaBanamexIPN = cuentaBanamexIPN;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
