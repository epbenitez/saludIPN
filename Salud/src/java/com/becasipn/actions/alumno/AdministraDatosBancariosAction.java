package com.becasipn.actions.alumno;

import com.becasipn.actions.BaseReportAction;
import com.becasipn.business.AlumnoBO;
import com.becasipn.business.AlumnoDatosBancariosBO;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.AlumnoDatosBancarios;
import com.becasipn.persistence.model.AlumnoTarjetaBancaria;
import com.becasipn.persistence.model.EstatusTarjetaBancaria;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Tania G. Sánchez Manilla
 */
public class AdministraDatosBancariosAction extends BaseReportAction implements MensajesAlumno {

    public static final String MOSTRAR = "mostrar";
    private Alumno alumno = new Alumno();
    private AlumnoDatosBancarios alumnoDatosBanco = new AlumnoDatosBancarios();
    private boolean sinDatosConCuenta = false;
    private String numeroDeBoleta;
    private boolean vigenteTutor = false;
    private Boolean datosAcademicos = Boolean.TRUE;
    private Boolean menorEdad = Boolean.FALSE;
    private String mensajeEstatusTarjeta = null;
    private boolean datosTutor = false;
    private boolean actualizarDatosBanco = true;
    private String fechaNacimientoTutor;

    public String mostrar() {
        AlumnoBO bo = new AlumnoBO(getDaos());
        AlumnoDatosBancariosBO abo = new AlumnoDatosBancariosBO(getDaos());
        //Obtenemos los datos del alumno.
        numeroDeBoleta = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Alumno> listAlumno = getDaos().getAlumnoDao().findBy("boleta", numeroDeBoleta, Boolean.TRUE);
        if (listAlumno == null || listAlumno.isEmpty()) {
            addActionError(getText("registro.boleta.no.encontrada"));
            return ERROR;
        }
        alumno = listAlumno.get(0);
        //Buscamos la relación alumno/tarjeta en estatus 13.
        AlumnoTarjetaBancaria tarjetaBancaria = getDaos().getAlumnoTarjetaDao().tarjetaLiberada(alumno.getId());
        //Si no tiene tarjeta liberada.
        if (tarjetaBancaria == null) {
            //Buscamos los datos vigentes.
            alumnoDatosBanco = abo.datosVigentes(alumno.getId());
            //Se valida si los datos vigentes son del tutor.
            if (alumnoDatosBanco != null && alumnoDatosBanco.getNombre() != null && alumnoDatosBanco.getApellidoPaterno() != null) {
                vigenteTutor = true;
            }
            //Se investiga si el alumno es menor de edad.
            if (bo.calcularEdad(alumno.getFechaDeNacimiento()) < 18) {
                menorEdad = Boolean.TRUE;
            }
            //Si no tiene datos bancarios le mostramos el formulario de datos personales en blanco.
            if (alumnoDatosBanco == null) {
                alumnoDatosBanco = new AlumnoDatosBancarios();
            } //Si tiene datos bancarios.
            else {
                //Se hace uppercase a los datos bancarios que aceptan letras.
                if (alumnoDatosBanco.getNombre() != null) {
                    alumnoDatosBanco.setNombre(alumnoDatosBanco.getNombre().toUpperCase());
                }
                if (alumnoDatosBanco.getApellidoPaterno() != null) {
                    alumnoDatosBanco.setApellidoPaterno(alumnoDatosBanco.getApellidoPaterno().toUpperCase());
                }
                if (alumnoDatosBanco.getApellidoMaterno() != null) {
                    alumnoDatosBanco.setApellidoMaterno(alumnoDatosBanco.getApellidoMaterno().toUpperCase());
                }
                if (alumnoDatosBanco.getCorreoElectronico() != null) {
                    alumnoDatosBanco.setCorreoElectronico(alumnoDatosBanco.getCorreoElectronico().toUpperCase());
                }
                if (alumnoDatosBanco.getOcupacion() != null) {
                    alumnoDatosBanco.setOcupacion(alumnoDatosBanco.getOcupacion().toUpperCase());
                }
                if (alumnoDatosBanco.getCalle() != null) {
                    alumnoDatosBanco.setCalle(alumnoDatosBanco.getCalle().toUpperCase());
                }
                if (alumnoDatosBanco.getNumeroInterior() != null) {
                    alumnoDatosBanco.setNumeroInterior(alumnoDatosBanco.getNumeroInterior().toUpperCase());
                }
                if (alumnoDatosBanco.getNumeroExterior() != null) {
                    alumnoDatosBanco.setNumeroExterior(alumnoDatosBanco.getNumeroExterior().toUpperCase());
                }
                if (alumnoDatosBanco.getEstado() != null) {
                    alumnoDatosBanco.setEstado(alumnoDatosBanco.getEstado().toUpperCase());
                }
                if (alumnoDatosBanco.getMunicipio() != null) {
                    alumnoDatosBanco.setMunicipio(alumnoDatosBanco.getMunicipio().toUpperCase());
                }
                if (alumnoDatosBanco.getColonia() != null) {
                    alumnoDatosBanco.setColonia(alumnoDatosBanco.getColonia().toUpperCase());
                }
                //Se obtienen los datos de la tarjeta bancaria relacionada a los datos vigentes del alumno.
                tarjetaBancaria = getDaos().getAlumnoTarjetaDao().tarjetaDatosBancarios(alumnoDatosBanco.getId());
                if (tarjetaBancaria != null) {
                    if (tarjetaBancaria.getEstatusTarjBanc().getId().equals(new BigDecimal("14")) //Rechazada.
                            || tarjetaBancaria.getEstatusTarjBanc().getId().equals(new BigDecimal("15"))) { //Corrección de datos.
                        //Si el alumno es menor de edad puede editar los datos bancarios de su padre o tutor.
                        if (menorEdad) {
                            if (tarjetaBancaria.getEstatusTarjBanc().getId().equals(new BigDecimal("14"))) { //Rechazada. 
                                mensajeEstatusTarjeta = "La solicitud de alta de cuenta bancaria que realizaste, "
                                        + "no ha sido exitosa. Verifica que tu información coincida exactamente "
                                        + "con tu identificación personal (INE). No olvides revisar el manual y, "
                                        + "en caso de tener dudas, acude con el responsable de becas de tu Unidad Académica.";
                            }
                        } else {
                            //Si el alumno tiene 18 le da opción de ingresar los datos del padre o tutor.
                            datosTutor = bo.calcularEdad(alumno.getFechaDeNacimiento()) == 18;
                            if (datosTutor || (alumnoDatosBanco.getNombre() == null && alumnoDatosBanco.getApellidoPaterno() == null)) {
                                //Si el alumno es mayor de edad sin datos del padre puede editar sus datos bancarios.
                                if (tarjetaBancaria.getEstatusTarjBanc().getId().equals(new BigDecimal("14"))) { //Rechazada.
                                    mensajeEstatusTarjeta = "La solicitud de alta de cuenta bancaria que realizaste, "
                                            + "no ha sido exitosa. Verifica que tu información coincida exactamente "
                                            + "con tu identificación personal (INE). No olvides revisar el manual y, "
                                            + "en caso de tener dudas, acude con el responsable de becas de tu Unidad Académica.";
                                }
                            } else {
                                //Si el alumno es mayor de edad con datos del padre o tutor ya no puede editar los datos bancarios de su padre o tutor.
                                mensajeEstatusTarjeta = "Hemos detectado que ya eres mayor de edad por lo que ya no puedes corregir los datos bancarios de tu padre o tutor. Debes "
                                        + "renunciar primero a esta cuenta y después solicitar una cuenta a tu nombre.";
                                actualizarDatosBanco = false;
                            }
                        }
                    } else if (tarjetaBancaria.getEstatusTarjBanc().getId().equals(new BigDecimal("16"))) { //Cancelada (antes renuncia).
                        mensajeEstatusTarjeta = "Has solicitado el alta de una nueva cuenta bancaria a tu nombre. Proporciona tus datos bancarios, "
                                + "ya que en caso de no hacerlo, no podrá ser solicitada una nueva cuenta";
                        //Le mostramos el formulario de datos personales en blanco.
                        alumnoDatosBanco = new AlumnoDatosBancarios();
                    } else {
                        actualizarDatosBanco = false;
                    }
                }
            }
        }//Si tiene tarjeta liberada.
        else {
            //Si la tarjeta no tiene datos bancarios mostramos msj de que no necesita usar el modulo.
            if (tarjetaBancaria.getDatosBancarios() == null) {
                sinDatosConCuenta = true;
            } //Si la tarjeta tiene datos bancarios usamos esa información y se la mostramos de sólo lectura.
            else {
                alumnoDatosBanco = abo.datosVigentes(alumno.getId());
                actualizarDatosBanco = false;
            }
        }
        return MOSTRAR;
    }

    public String guarda() {
        AlumnoBO bo = new AlumnoBO(getDaos());
        AlumnoDatosBancariosBO abo = new AlumnoDatosBancariosBO(getDaos());
        Integer intentos = 0;
        alumno = getDaos().getAlumnoDao().findById(alumno.getId());
        if (alumno == null) {
            addActionError(getText("registro.guarda.sin.datos"));
            return INPUT;
        }
        //Se realcionan los datos bancarios al alumno.
        alumnoDatosBanco.setAlumno(alumno);
        //Se checa si el alumno tiene datos bancarios.
        AlumnoDatosBancarios alumnoDatosBancarios = abo.datosVigentes(alumno.getId());
        //Alumno sin datos bancarios.
        if (alumnoDatosBancarios == null) {
            alumnoDatosBanco = abo.guarda(alumnoDatosBanco, fechaNacimientoTutor, null, intentos);
        } //Alumno con datos bancarios
        else {
            //Se obtienen los datos de la tarjeta bancaria relacionada a los datos vigentes del alumno
            AlumnoTarjetaBancaria alumnoTarjetaBancaria = getDaos().getAlumnoTarjetaDao().tarjetaDatosBancarios(alumnoDatosBanco.getId());
            //Si no tiene tarjeta relacionada
            if (alumnoTarjetaBancaria == null) {
                //Se busca si tiene una tarjeta cancelada para poder crear un nuevo registro.
                alumnoTarjetaBancaria = getDaos().getAlumnoTarjetaDao().tarjetaDatosBancarios(alumnoDatosBancarios.getId());
                if (alumnoTarjetaBancaria == null) {
                    alumnoDatosBanco = abo.guarda(alumnoDatosBanco, fechaNacimientoTutor, null, intentos);
                } else if (alumnoTarjetaBancaria.getEstatusTarjBanc().getId().equals(new BigDecimal("16"))) { //Cancelada (antes Renuncia)
                    alumnoDatosBanco = abo.guarda(alumnoDatosBanco, fechaNacimientoTutor, Boolean.TRUE, intentos);
                }
            } //Si tiene tarjeta relacionada
            else {
                if (alumnoTarjetaBancaria.getEstatusTarjBanc().getId().equals(new BigDecimal("14")) //Rechazada
                        || alumnoTarjetaBancaria.getEstatusTarjBanc().getId().equals(new BigDecimal("15"))) { //Corrección de datos
                    if (alumnoTarjetaBancaria.getEstatusTarjBanc().getId().equals(new BigDecimal("14"))) {
                        intentos = 1;
                    }
                    //A la tarjeta se le asignara el estatus Corrección de datos
                    EstatusTarjetaBancaria estatusTarjetaBancaria = getDaos().getEstatusTarjetaBancariaDao().findById(new BigDecimal("15")); //Corrección de datos
                    alumnoTarjetaBancaria.setEstatusTarjBanc(estatusTarjetaBancaria);
                    //Se actualizan los datos
                    alumnoDatosBanco = abo.guarda(alumnoDatosBanco, fechaNacimientoTutor, null, intentos);
                    getDaos().getAlumnoTarjetaDao().update(alumnoTarjetaBancaria);
                }
            }
        }
        //Una vez que se guardan los datos bancarios se marca en 1 el campo datosBancarios del alumno si aun no lo tiene marcado.
        if (!alumno.getDatosBancarios()) {
            alumno.setDatosBancarios(Boolean.TRUE);
            alumno = bo.guarda(alumno);
        }
        addActionMessage(getText("misdatos.alumno.actualizado"));
        return mostrar();
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public AlumnoDatosBancarios getAlumnoDatosBanco() {
        return alumnoDatosBanco;
    }

    public void setAlumnoDatosBanco(AlumnoDatosBancarios alumnoDatosBanco) {
        this.alumnoDatosBanco = alumnoDatosBanco;
    }

    public Boolean getSinDatosConCuenta() {
        return sinDatosConCuenta;
    }

    public void setSinDatosConCuenta(Boolean sinDatosConCuenta) {
        this.sinDatosConCuenta = sinDatosConCuenta;
    }

    public String getNumeroDeBoleta() {
        return numeroDeBoleta;
    }

    public void setNumeroDeBoleta(String numeroDeBoleta) {
        this.numeroDeBoleta = numeroDeBoleta;
    }

    public Boolean getVigenteTutor() {
        return vigenteTutor;
    }

    public void setVigenteTutor(Boolean vigenteTutor) {
        this.vigenteTutor = vigenteTutor;
    }

    public Boolean getDatosAcademicos() {
        return datosAcademicos;
    }

    public void setDatosAcademicos(Boolean datosAcademicos) {
        this.datosAcademicos = datosAcademicos;
    }

    public Boolean getMenorEdad() {
        return menorEdad;
    }

    public void setMenorEdad(Boolean menorEdad) {
        this.menorEdad = menorEdad;
    }

    public String getMensajeEstatusTarjeta() {
        return mensajeEstatusTarjeta;
    }

    public void setMensajeEstatusTarjeta(String mensajeEstatusTarjeta) {
        this.mensajeEstatusTarjeta = mensajeEstatusTarjeta;
    }

    public Boolean getDatosTutor() {
        return datosTutor;
    }

    public void setDatosTutor(Boolean datosTutor) {
        this.datosTutor = datosTutor;
    }

    public Boolean getActualizarDatosBanco() {
        return actualizarDatosBanco;
    }

    public void setActualizarDatosBanco(Boolean actualizarDatosBanco) {
        this.actualizarDatosBanco = actualizarDatosBanco;
    }

    public String getFechaNacimientoTutor() {
        return fechaNacimientoTutor;
    }

    public void setFechaNacimientoTutor(String fechaNacimientoTutor) {
        this.fechaNacimientoTutor = fechaNacimientoTutor;
    }
}
