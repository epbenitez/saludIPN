/**
 * SISTEMA INFORMÁTICO DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE SERVICIOS ESTUDIANTILES Created on : 04-ago-2015, 14:30:41
 *
 */
package com.becasipn.business;

import com.becasipn.domain.ResumenValidacionInscripcion;
import com.becasipn.exception.ErrorDaeException;
import com.becasipn.persistence.model.Genero;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.AlumnoDAE;
import com.becasipn.persistence.model.AlumnoDatosBancarios;
import com.becasipn.persistence.model.AlumnoTarjetaBancaria;
import com.becasipn.persistence.model.BitacoraAlumno;
import com.becasipn.persistence.model.Carrera;
import com.becasipn.persistence.model.CompaniaCelular;
import com.becasipn.persistence.model.CuestionarioRespuestas;
import com.becasipn.persistence.model.CuestionarioRespuestasUsuario;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.Depositos;
import com.becasipn.persistence.model.Direccion;
import com.becasipn.persistence.model.EntidadFederativa;
import com.becasipn.persistence.model.EstadoCivil;
import com.becasipn.persistence.model.EstadoCivilSubes;
import com.becasipn.persistence.model.EstatusTarjetaBancaria;
import com.becasipn.persistence.model.LocalidadColonia;
import com.becasipn.persistence.model.Modalidad;
import com.becasipn.persistence.model.Notificacion;
import com.becasipn.persistence.model.Otorgamiento;
import com.becasipn.persistence.model.PadronProspera;
import com.becasipn.persistence.model.PadronSubes;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.RelacionGeografica;
import com.becasipn.persistence.model.Rol;
import com.becasipn.persistence.model.SolicitudBecaIndividual;
import com.becasipn.persistence.model.SolicitudBecaNivel;
import com.becasipn.persistence.model.SolicitudBecaUA;
import com.becasipn.persistence.model.UnidadAcademica;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.persistence.model.UsuarioPrivilegio;
import com.becasipn.service.Service;
import com.becasipn.util.AmbienteEnums;
import com.becasipn.util.ErrorDAE;
import com.becasipn.util.PasswordGenerator;
import com.becasipn.util.ProgressBarManager;
import com.becasipn.util.StringUtil;
import com.becasipn.util.Tupla;
import com.becasipn.util.TuplaValidacion;
import com.becasipn.util.Util;
import com.opensymphony.xwork2.ActionContext;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.PersistenceException;
import javax.resource.spi.LocalTransactionException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;
import java.util.Calendar;
import java.util.Collections;
import javax.mail.MessagingException;
import oracle.toplink.essentials.exceptions.DatabaseException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.springframework.transaction.UnexpectedRollbackException;

/**
 *
 * @author Patricia Benitez
 */
public class AlumnoBO extends XlsLoaderBO {

    private final SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
    private HashMap<BigDecimal, Integer> relacion;
    private String jsonAlumno;

    public AlumnoBO(Service service) {
        super(service);
    }

    /**
     * Indica si el alumno ya existe en la tabla de alumno
     *
     * @param boleta
     * @return Alumno
     */
    public Alumno getAlumno(String boleta) {
        List<Alumno> a = service.getAlumnoDao().findBy("boleta", boleta, Boolean.TRUE);
        return (a == null || a.isEmpty()) ? null : a.get(0);
    }

    public Alumno daeJsonToEntAlumno(AlumnoDAE alumnoDae) {
        Alumno alumno = new Alumno();
        Periodo periodoActivo = service.getPeriodoDao().getPeriodoActivo();

        alumno.setNombre(alumnoDae.getNombre());
        alumno.setApellidoPaterno(alumnoDae.getApellido_pat());
        alumno.setApellidoMaterno(alumnoDae.getApellido_mat());
        alumno.setCurp(alumnoDae.getCurp());

        // Se obtiene la fecha de nacimiento
        // Todo: A diferencia del daeSubesToAlumno, aquí NO se hace una comparación 
        // antes de seleccionar la fecha de nacimiento
        if (alumnoDae.getFecha_nac() != null) {
            alumno.setFechaDeNacimiento(alumnoDae.getFecha_nac());
        }
        //Academicos
        alumno.setBoleta(alumnoDae.getBoleta());
        alumno.setCorreoElectronico(alumnoDae.getE_mail());
        // Todo: Se eliminaron las asignaciones individuales, ahora se utiliza  este metodo. Hay que revisar los campos extras
        DatosAcademicos da = agregarDatosAcademicos(alumno, alumnoDae, periodoActivo);
        alumno.setDatosAcademicos(da);
        //Se obtiene la dirección
        Direccion d = new Direccion();
        d.setCalle(alumnoDae.getCalle());
        d.setCodigoPostal(alumnoDae.getCp());
        EntidadFederativa edo = alumnoDae.getEstado() == null || alumnoDae.getEstado().equals("")
                ? null : service.getEntidadFederativaDao().findById(new BigDecimal(alumnoDae.getEstado()));
        BigDecimal edoId = edo == null ? null : edo.getId();
        List<LocalidadColonia> col = alumnoDae.getColonia() == null || alumnoDae.getColonia().equals("")
                ? null : service.getLocalidadColoniaDao().findBy("nombre", alumnoDae.getColonia(), Boolean.TRUE);
        BigDecimal colId = (col == null || col.isEmpty()) ? null : col.get(0).getId();

        if (edoId != null || colId != null) {
            RelacionGeografica rg = service.getRelacionGeograficaDao().getRelacionGeografica(edoId, null, colId);
            d.setRelacionGeografica(rg);
        }
        alumno.setDireccion(d);
        //Se obtiene el genero
        Genero gen = service.getGeneroDao().findById(alumnoDae.getSexo().equals("F") ? new BigDecimal("1") : new BigDecimal("2"));
        alumno.setGenero(gen);
        return alumno;
    }

    public Alumno daeSubesToAlumno(AlumnoDAE alumnoDae, PadronSubes padronSubes) {
        Alumno alumno = new Alumno();
        alumno.setNombre(padronSubes.getNombre());
        alumno.setApellidoPaterno(padronSubes.getApellidopaterno());
        alumno.setApellidoMaterno(padronSubes.getApellidomaterno());
        alumno.setCurp(padronSubes.getCurp());
        EstadoCivilSubes edo = EstadoCivilSubes.valueOf(padronSubes.getEstadocivil().replaceAll(" ", ""));
        // Todo: En daeJsonToEntAlumno no se establece estado civil
        alumno.setEstadoCivil(new EstadoCivil(edo.getId()));

        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");//Fecha de Nacimiento
        try {
            alumno.setFechaDeNacimiento(sdf.parse(alumno.getCurp().substring(4, 10)));
        } catch (ParseException ex) {
            log.warn("No se establaceio fechaNac del curp " + alumno.getCurp());
            alumno.setFechaDeNacimiento(alumnoDae.getFecha_nac());
        }
        //Academicos
        alumno.setBoleta(alumnoDae.getBoleta());
        try {
            validarCarrera(alumnoDae);
        } catch (ErrorDaeException ex) {
            log.warn(ex.getMessage());
        }
        Genero gen = service.getGeneroDao().findById(alumnoDae.getSexo().equals("F") ? new BigDecimal("1") : new BigDecimal("2"));
        alumno.setGenero(gen);
        alumno.setEntidadDeNacimiento(service.getEntidadFederativaDao().findByClave(padronSubes.getEntidadnacimiento()));

        //Se obtiene la dirección
        Direccion d = new Direccion();
        d.setCalle(padronSubes.getCalle());
        d.setCodigoPostal(padronSubes.getCodigopostal());
        d.setNumeroExterior(padronSubes.getNumext());
        d.setNumeroInterior(padronSubes.getNumint());
        BigDecimal edoId;
        if (padronSubes.getEstado().equals("MEXICO")) {
            edoId = new BigDecimal(15);
        } else {
            edoId = padronSubes.getEstado().equals("") ? null : service.getEntidadFederativaDao().findByNombreAcentos(padronSubes.getEstado());
        }
        if (edoId != null) {
            RelacionGeografica rg = service.getRelacionGeograficaDao().getRelacionGeografica(edoId, null, null);
            d.setRelacionGeografica(rg);
        }
        alumno.setDireccion(d);
        StringBuilder celular;
        if (padronSubes.getCelular() != null && !padronSubes.getCelular().isEmpty()) {
            celular = new StringBuilder(padronSubes.getCelular().replaceAll("\\(", "").replaceAll("\\)", "").trim());
            celular.insert(2, "-");
        } else {
            celular = new StringBuilder("");
        }
        alumno.setCelular(celular.toString());
        alumno.setCompaniaCelular(new CompaniaCelular(new BigDecimal(7)));
        alumno.setCorreoElectronico(padronSubes.getCorreoelectronico());
        alumno.setBeneficiarioOportunidades(padronSubes.getTieneprospera() == null ? false : padronSubes.getTieneprospera().equals("SI"));
        alumno.setFechaAlta(new Date());
        alumno.setFechaModificacion(new Date());
        Usuario usuario = creaUsuario(alumno);
        alumno.setUsuario(usuario);
        alumno.setEstatus(Boolean.TRUE);
        return alumno;
    }

    /**
     * Obtiene los últimos depósitos del periodo actual
     *
     * @author Mario Márquez
     * @param noIdalumno
     * @return String[][]
     */
    public List<Depositos> getUltimosDepositos(BigDecimal noIdalumno) {
        BigDecimal periodoActivoId = service.getPeriodoDao().getPeriodoActivo().getId();
        List<Depositos> listaDepositos = service.getDepositosDao().depositosAlumno(noIdalumno, periodoActivoId, null);
        if (listaDepositos == null || listaDepositos.isEmpty()) {
            return null;
        }

        return listaDepositos;
    }

    /**
     *
     * @param a
     */
    public void setRelacionGeografica(Alumno a) {
        if (a.getDireccion() != null) {
            if (a.getDireccion().getRelacionGeografica() != null) {
                if (a.getDireccion().getRelacionGeografica().getEstado() != null
                        && a.getDireccion().getRelacionGeografica().getMunicipio() != null
                        && a.getDireccion().getRelacionGeografica().getColonia() != null) {
                    RelacionGeografica rg = service.getRelacionGeograficaDao().getRelacionGeografica(
                            a.getDireccion().getRelacionGeografica().getEstado().getId(),
                            a.getDireccion().getRelacionGeografica().getMunicipio().getId(),
                            a.getDireccion().getRelacionGeografica().getColonia().getId());
                    a.getDireccion().setRelacionGeografica(rg);
                } else {
                    a.getDireccion().setRelacionGeografica(null);
                }
            }
        }
    }

    /**
     * Obtiene el objeto Carrera correspondiente a su Plan de Estudios, el
     * catálogo de carrera y su especialidad
     *
     * @param a
     */
    public void setCarrera(Alumno a) {
        DatosAcademicos datosAcademicos = service.getDatosAcademicosDao().ultimosDatos(a.getId());
        if (datosAcademicos == null) {
            datosAcademicos = new DatosAcademicos();
        }

        if (datosAcademicos.getCarrera() != null) {
            if (datosAcademicos.getCarrera().getClaveCarrera() != null) {
                if (datosAcademicos.getUnidadAcademica().getId() != null) {
                    if (datosAcademicos.getCarrera().getEspecialidad() != null && datosAcademicos.getCarrera().getPlanEstudios() != null) {
                        Carrera c = service.getCarreraDao().getCarrera(datosAcademicos.getCarrera().getPlanEstudios(), datosAcademicos.getCarrera().getClaveCarrera(), datosAcademicos.getCarrera().getEspecialidad(), datosAcademicos.getUnidadAcademica().getId());
                        datosAcademicos.setCarrera(c);
                    } else {
                        datosAcademicos.setCarrera(null);
                    }
                }
            }
        }
    }

    /**
     * Establece la fecha de nacimiento a partir de un string
     *
     * @param fechaDeNacimiento
     * @return
     */
    public Date setFechaNacimiento(String fechaDeNacimiento) {
        Date fecha = new Date();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            fecha = dateFormat.parse(fechaDeNacimiento);
        } catch (Exception ex) {
        }
        return fecha;
    }

    /**
     * Guarda o actualiza los datos del alumno
     *
     * @param alumno
     * @return
     */
    public Alumno guarda(Alumno alumno) {
        List<Alumno> aTmpLst = service.getAlumnoDao().getByBoleta(alumno.getBoleta());//("boleta", a.getBoleta(), Boolean.TRUE);
        alumno.setFechaModificacion(new Date());
        this.setRelacionGeografica(alumno);
        if (aTmpLst == null || aTmpLst.isEmpty()) {
            try {
                alumno.setEstatus(Boolean.TRUE);
                alumno.setFechaAlta(new Date());
                alumno = service.getAlumnoDao().save(alumno);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            alumno.setUsuario(aTmpLst.get(0).getUsuario());
            if (alumno.getId() != null) {
                service.getAlumnoDao().update(alumno);
            }
        }
        return alumno;
    }

    public Usuario creaUsuario(Alumno a) {
        if (a == null || a.getUsuario() != null) {
            return null;
        } else {
            String usuario = a.getBoleta();
            String password = PasswordGenerator.generatePassword();
            Usuario u = new Usuario();
            u.setActivo(Boolean.TRUE);
            u.setUsuario(usuario);
            u.setPassword(password);
            Set<UsuarioPrivilegio> roles = new HashSet<UsuarioPrivilegio>();
            UsuarioPrivilegio privilegio = new UsuarioPrivilegio();
            privilegio.setPrivilegio(new Rol(new BigDecimal(2))); //ALUMNO
            privilegio.setUsuario(u);
            roles.add(privilegio);
            u.setPrivilegios(roles);
            u.setFechaModificacion(new Date());
            a.setUsuario(u);
            return u;
        }
    }

    public Tupla<Alumno, ErrorDAE> establecerDatosAcademicosDAE(Alumno alumno) throws ErrorDaeException {
        Tupla<Alumno, ErrorDAE> tupla = new Tupla<>();
        TuplaValidacion tuplaValidacion = validarAlumno(alumno.getBoleta(), false);
        AlumnoDAE a;
        Periodo periodoActivo = service.getPeriodoDao().getPeriodoActivo();
        // Si hubiera un error al validar el alumno, se asigna
        // el error a la tupla, y se suspende el progreso
        if (tuplaValidacion.getErrorDAE() != null) {
            if (!tuplaValidacion.getErrorDAE().equals(ErrorDAE.NO_INSCRITO)) {
                tupla.setK2(tuplaValidacion.getErrorDAE());
                return tupla;
            }
        }
        // Si no falla, crea un alumno a partir de los datos de la DAE
        a = tuplaValidacion.getAlumnoDAE();
        DatosAcademicos datosAcademicos = service.getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), periodoActivo.getId());
        if (datosAcademicos != null) {
            datosAcademicos = actualizarDatosAcademicos(alumno, a, periodoActivo);
        } else {
            datosAcademicos = agregarDatosAcademicos(alumno, a, periodoActivo);
        }

        //if (alumno.getId() == null) { Actualizar el semetre de todos los alumnos
        Otorgamiento otorgamientoAnterior = service.getOtorgamientoDao().getOtorgamientoAlumno(alumno.getId(),
                service.getPeriodoDao().getPeriodoActivo().getPeriodoAnterior().getId());
        if (otorgamientoAnterior != null) {
            datosAcademicos.setSemestre(otorgamientoAnterior.getDatosAcademicos().getSemestre() + 1);
        } else {
            otorgamientoAnterior = service.getOtorgamientoDao().getOtorgamientoTransporteAlumno(alumno.getId(),
                    service.getPeriodoDao().getPeriodoActivo().getPeriodoAnterior().getId(), 2);
            if (otorgamientoAnterior != null) {
                datosAcademicos.setSemestre(otorgamientoAnterior.getDatosAcademicos().getSemestre() + 1);
            } else {
                datosAcademicos.setSemestre(a.getSemestre_inscrito());
            }
        }
        //}
//        validarCarrera(a);
        alumno.setEstatus(Boolean.TRUE);
        //setRelacionGeografica(alumno);
        alumno.setOrigenDAE(a);
        guardaDatosAcademicos(alumno, datosAcademicos);
        tupla.setK1(alumno);
        return tupla;

    }

    /**
     * La actualización de datos automática (al login) debe hacerse mientras:
     * 1.El registro esté abierto. (IndexAction) 2. El alumno no tenga un
     * otorgamiento en el periodo actual (IndexAction) 3. Estatus = 0 actualiza
     * datos 4. Estatus = 1 & "infoactualizadaAdmin" = 0, compara si hay
     * diferencias, si las hay, actualiza datos 5. Estatus = 1 &
     * "infoactualizadaAdmin" = 1 no actualiza datos Si el campo "origen" de
     * vw_dae = "maestro", se coloca el campo "infoactualizadaAdmin" = 1
     *
     * @param alumno
     * @param curp
     * @return
     * @throws ErrorDaeException
     */
    public Alumno datosDAE(Alumno alumno, String... curp) throws ErrorDaeException {
        Boolean estatus = alumno.getEstatus() == null ? false : alumno.getEstatus();
        TuplaValidacion tuplaValidacion = validarAlumno(alumno.getBoleta(), true, curp);
        AlumnoDAE a = new AlumnoDAE();
        if (tuplaValidacion.getErrorDAE() != null) {
            if (tuplaValidacion.getErrorDAE().equals(ErrorDAE.NO_INSCRITO)) {
                a = tuplaValidacion.getAlumnoDAE();
            } else {
                System.out.println("El alumno con curp: " + curp[0] + " no se encuentra en vw_dae");
                return null;
            }
        }
        a = tuplaValidacion.getAlumnoDAE();
        Periodo periodoActivo = service.getPeriodoDao().getPeriodoActivo();
        DatosAcademicos datosAcademicos = service.getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), periodoActivo.getId());

        if (estatus) {
            // Si estatus = 1 DEBERÍA tener datos académicos SIEMPRE
            // 4. Estatus = 1 & "infoactualizadaAdmin" = 0, compara si hay diferencias, si las hay, actualiza datos 
            if (datosAcademicos != null && datosAcademicos.getInfoActualizadaAdmin() == 0) {
                if (existenCambios(datosAcademicos, a)) {
                    asociaDatosAcademicos(datosAcademicos, alumno, a, periodoActivo);
                }
            }
            // 5. Estatus = 1 & "infoactualizadaAdmin" = 1 no actualiza datos 

        } else {
            //3. Estatus = 0 actualiza datos
            asociaDatosAcademicos(datosAcademicos, alumno, a, periodoActivo);
        }

        alumno.setOrigenDAE(a);
        return alumno;
    }

    /**
     * Asocia datos académicos al alumno Establece el semestre de acuerdo al
     * consecutivo de los datos académicos del periodo anterior (en caso de
     * tener ese dato). Se actualiza la bandera InfoActualizadaAdmin que indica
     * si los datos ya son definitivos.
     *
     * @param datosAcademicos
     * @param alumno
     * @param a
     * @param periodoActivo
     */
    private void asociaDatosAcademicos(DatosAcademicos datosAcademicos, Alumno alumno, AlumnoDAE a, Periodo periodoActivo) {
        if (datosAcademicos == null) {
            datosAcademicos = agregarDatosAcademicos(alumno, a, periodoActivo);
        } else {
            datosAcademicos = actualizarDatosAcademicos(alumno, a, periodoActivo);
        }
        //Se coloca el semestre consecutivo del periodo anterior
        DatosAcademicos datosAcademicosAnterior = service.getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), periodoActivo.getPeriodoAnterior().getId());
        if (datosAcademicosAnterior != null && datosAcademicosAnterior.getSemestre() != null) {
            datosAcademicos.setSemestre(datosAcademicosAnterior.getSemestre() + 1);
        } else {
            Integer semestre = a.getSemestre_inscrito() == null ? 0 : a.getSemestre_inscrito();
            datosAcademicos.setSemestre(semestre);
        }

        //Se establece la bandera InfoActualizadaAdmin que si se marca en 1, 
        //indica que los datos académicos ingresados, ya son definitivos.
        if (datosAcademicos.getInfoActualizadaAdmin() != null && datosAcademicos.getInfoActualizadaAdmin() == 0) {
            if (a.getOrigen() != null && a.getOrigen() == 1) {
                datosAcademicos.setInfoActualizadaAdmin(1);
            }
        }

        guardaDatosAcademicos(alumno, datosAcademicos);
    }

    /**
     * Verifica que los datos académicos actuales no contengan cambios con
     * respecto a los que indica la vista de la DAE
     *
     * @param actuales
     * @param dae
     * @return
     */
    public Boolean existenCambios(DatosAcademicos actuales, AlumnoDAE dae) {
        Boolean sincambios = Boolean.TRUE;

        sincambios = (sincambios && Util.equals(actuales.getCarrera().getId(), dae.getCarrera()));
        sincambios = (sincambios && Util.equals(actuales.getCumpleCargaMinima(), dae.getCumple_carga_minima()));
        sincambios = (sincambios && Util.equals(actuales.getEgresado(), dae.getEgresado()));
        sincambios = (sincambios && Util.equals(actuales.getInscrito(), dae.getInscrito()));
        sincambios = (sincambios && Util.equals(actuales.getModalidadDAE(), dae.getModo_ingreso()));
        sincambios = (sincambios && Util.equals(actuales.getPromedio(), dae.getPromedio()));
        sincambios = (sincambios && Util.equals(actuales.getRegular(), (dae.getTipo_alumno().equals("R") ? 1 : 0)));
        sincambios = (sincambios && Util.equals(actuales.getSemestre(), dae.getSemestre_inscrito()));
        sincambios = (sincambios && Util.equals(actuales.getTotalCreditos(), dae.getTot_creditos()));
        sincambios = (sincambios && Util.equals(actuales.getTurno(), dae.getTurno()));
        dae.setEscuela(dae.getEscuela().startsWith("0") ? dae.getEscuela().substring(1, dae.getEscuela().length()) : dae.getEscuela());
        sincambios = (sincambios && Util.equals(actuales.getUnidadAcademica().getId(), dae.getEscuela()));
        return sincambios ? Boolean.FALSE : Boolean.TRUE;
    }

    public Boolean guardaAlumno(Alumno alumno) {
        try {
            if (alumno.getId() == null) {
                service.getAlumnoDao().save(alumno);
            } else {
                service.getAlumnoDao().update(alumno);
            }
        } catch (Exception e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public Boolean guardaDatosAcademicos(Alumno alumno, DatosAcademicos datosAcademicos) {
        try {
            if (datosAcademicos.getId() != null) {
                service.getDatosAcademicosDao().update(datosAcademicos);
                if (!alumno.getEstatus()) {
                    alumno.setEstatus(Boolean.TRUE);
                    guardaAlumno(alumno);
                }
            } else {
                alumno.setEstatus(Boolean.TRUE);
                alumno.addDatosAcademicos(datosAcademicos);
                guardaAlumno(alumno);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public Boolean actualizaSalarios(Alumno a, CuestionarioRespuestas salarios) {
        CuestionarioRespuestasUsuario respuestaAlumno = service.getCuestionarioRespuestasUsuarioDao()
                .getPreguntaUsuarioActual(a.getUsuario().getId(), new BigDecimal(13));
        respuestaAlumno.setRespuesta(salarios);
        try {
            service.getCuestionarioRespuestasUsuarioDao().update(respuestaAlumno);
        } catch (Exception e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public ErrorDAE validarCarrera(AlumnoDAE alumnoDae) throws ErrorDaeException {
        UnidadAcademica uA = service.getUnidadAcademicaDao().getByClave((String.valueOf(Integer.parseInt(alumnoDae.getEscuela()))));
        Carrera carrera = service.getCarreraDao().getCarrera(alumnoDae.getPlan_estud(), alumnoDae.getCarrera(), alumnoDae.getEspecialidad(), uA.getId());
        if (carrera == null) {
            avisoErrorDatosAcademicos("Especialidad incorrecta: " + alumnoDae);
            throw new ErrorDaeException(ErrorDAE.ESPCIALIDAD_INCORRECTA.getMsg());
        }
        alumnoDae.setCarreraObj(carrera);
        return null;
    }

    public Boolean alumnoAsignado(BigDecimal id) {
        return service.getOtorgamientoDao().existeAlumnoAsignado(id);
    }

    public boolean documentosCompletos(BigDecimal id) {
        return service.getAlumnoDao().documentosCompletos(id);
    }

    public String becaAnterior(BigDecimal alumnoId) {
        BigDecimal periodoId = service.getPeriodoDao().getPeriodoActivo().getPeriodoAnterior().getId();
        return service.getOtorgamientoDao().getNombreBeca(alumnoId, periodoId);
    }

    public BigDecimal becaAnteriorId(BigDecimal alumnoIr) {
        BigDecimal periodoId = service.getPeriodoDao().getPeriodoActivo().getPeriodoAnterior().getId();
        return service.getOtorgamientoDao().getIdBeca(alumnoIr, periodoId);
    }

    public String becaPropuesta(BigDecimal alumnoId) {
        BigDecimal periodoId = service.getPeriodoDao().getPeriodoActivo().getId();
        return service.getOtorgamientoDao().getNombreBeca(alumnoId, periodoId);
    }

    public BigDecimal becaPropuestaId(BigDecimal alumnoId) {
        BigDecimal periodoId = service.getPeriodoDao().getPeriodoActivo().getId();
        return service.getOtorgamientoDao().getIdBeca(alumnoId, periodoId);
    }

    public Boolean estatusAlumno(String noBoleta) {
        return service.getAlumnoDao().estatusAlumno(noBoleta);
    }

    /**
     * Procesa el archivo de Excel con los promedios y semestres de los alumnos
     * Para agergar o quitar una columna, hay que hacer lo siguiente: modificar
     * la variable columnas, agregar a la tabla info anterior, info nueva,
     * mensaje de error; agregar al if de hayErrores y huboCambios, la
     * comprobación del nuevo dato
     *
     * @author Victor Lozano
     * @param wb Archivo Excel
     * @return lista
     * @throws java.lang.Exception
     */
    @Override
    public List<List<String>> processFile(Workbook wb) throws Exception {
        relacion = service.getCarreraDao().getSemestresMaximos();
        List<List<String>> logScreen = new ArrayList<>();
        Sheet sheet = wb.getSheetAt(0);

        Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        String rol = service.getUsuarioPrivilegioDao().findByUsuario(usuario.getId()).getPrivilegio().getDescripcion();
        boolean personalUA = rol.equals("ROLE_RESPONSABLE_UA") || rol.equals("ROLE_FUNCIONARIO_UA");
        UnidadAcademica uAcademicaAdministrativo = null;
        BigDecimal unidadAcademicaId;
        // Define el número de columnas vacías a agregar.
        Integer columnas = 10;
        if (personalUA) {
            uAcademicaAdministrativo = service.getPersonalAdministrativoDao().findByUsuario(usuario.getId()).getUnidadAcademica();
        }

        for (int cont = 1; cont <= sheet.getLastRowNum(); cont++) {
            Row row = sheet.getRow(cont);
            log.info("Procesando la fila: " + cont + " de " + sheet.getLastRowNum());
            // StringBuilder para armar la tabla
            StringBuilder sb = new StringBuilder();
            List<String> aux = new ArrayList<>();
            DataFormatter formatter = new DataFormatter();
            String boleta = "";
            Boolean formatoFunciono = false;
            try {
                boleta = formatter.formatCellValue(row.getCell(0));
                formatoFunciono = true;
            } catch (Exception e) {
                e.printStackTrace();
                if (row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING) {
                    String num = row.getCell(0) + "";
                    boleta = new BigInteger(num) + "";
                } else if (row.getCell(0).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    String num = row.getCell(0).getNumericCellValue() + "";
                    boleta = new BigDecimal(num).toBigInteger() + "";
                }

                aux.add(String.valueOf(cont));
                aux.add(boleta);
                for (int i = 0; i < columnas; i++) {
                    aux.add("N/A");
                }
                aux.add("<span class='label label-danger'  data-toggle='tooltip' title='Error en el formato de la columna.' data-placement='left' data-trigger='hover' data-content='Código de error EE01'>EE01</span>");

                logScreen.add(aux);
            }

            if (formatoFunciono) {
                Alumno alumno = service.getAlumnoDao().findByBoleta(boleta);
                DatosAcademicos datosAcademicos = null;
                Periodo currentTerm = null;

                if (alumno != null) {
                    PeriodoBO pBO = new PeriodoBO(service);
                    currentTerm = pBO.getPeriodoActivo(); // Esta declaración se utiliza fuera del if
                    datosAcademicos = service.getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), currentTerm.getId()); // Se obtienen los datos del periodo actual
                }

                if (datosAcademicos != null) {
                    // Si hubiera UA relacionada al administrativo se usa, si no usa la del alumno
                    if (uAcademicaAdministrativo == null) {
                        unidadAcademicaId = datosAcademicos.getUnidadAcademica().getId();
                    } else {
                        unidadAcademicaId = uAcademicaAdministrativo.getId();
                    }
                    // Si es personal admin, se revisa que su UA coincida con la del alumno
                    if (!personalUA || (personalUA && unidadAcademicaId.equals(datosAcademicos.getUnidadAcademica().getId()))) {
                        // Se obtienen datos desde BD
                        Double promedio = datosAcademicos.getPromedio();
                        Integer semestre = datosAcademicos.getSemestre();
                        Integer inscrito = datosAcademicos.getInscrito();
                        Integer regular = datosAcademicos.getRegular();
                        Integer cargaA = datosAcademicos.getCumpleCargaMinima();
                        // Crea versiones para la UI de los datos de la BD
                        String inscritoInitial = creaVersionUI(inscrito);
                        String regularInitial = creaVersionUI(regular);
                        String cargaInitial = creaVersionUICarga(cargaA);
                        // Se obtienen datos desde el archivo
                        String semestreCell = formatter.formatCellValue(row.getCell(1)).trim();
                        String promedioCell = formatter.formatCellValue(row.getCell(2)).trim();
                        String inscritoCell = limpiaString(formatter.formatCellValue(row.getCell(3)));
                        String regularCell = limpiaString(formatter.formatCellValue(row.getCell(4)));
                        String cargaCell = limpiaString(formatter.formatCellValue(row.getCell(5)));
                        // Crea versiones para la BD, de los datos del archivo
                        Integer inscritoCellInt = creaVersionBD(inscritoCell);
                        Integer regularCellInt = creaVersionBD(regularCell);
                        Integer cargaCellInt = creaVersionBDCarga(cargaCell);
                        // Crea versiones de los datos del archivo, para la UI 
                        String inscritoCellStr = creaVersionUI(inscritoCellInt);
                        String regularCellStr = creaVersionUI(regularCellInt);
                        String cargaCellStr = creaVersionUICarga(cargaCellInt);

                        // Si es necesario, asigna los valores nuevos al objeto datos academicos
                        if (semestreCell == null || semestreCell.equals("")) {
                            semestreCell = "Vacío";
                        } else {
                            try {
                                datosAcademicos.setSemestre(Integer.parseInt(semestreCell));
                            } catch (NumberFormatException nfe) {
                                datosAcademicos.setSemestre(-1);
                            }
                        }
                        if (promedioCell == null || promedioCell.equals("")) {
                            promedioCell = "Vacío";
                        } else {
                            String ax = promedioCell.replace(",", ".").replace(" ", "");
                            try {
                                datosAcademicos.setPromedio(Double.parseDouble(ax));
                            } catch (NumberFormatException nfe) {
                                datosAcademicos.setPromedio(new Double(-1));
                            }
                        }
                        if (inscritoCell == null || inscritoCell.equals("")) {
                            inscritoCellStr = "Vacío";
                        } else {
                            datosAcademicos.setInscrito(inscritoCellInt);
                        }
                        if (regularCell == null || regularCell.equals("")) {
                            regularCellStr = "Vacío";
                        } else {
                            datosAcademicos.setRegular(regularCellInt);
                        }
                        if (cargaCell == null || cargaCell.equals("")) {
                            cargaCellStr = "Vacío";
                        } else {
                            datosAcademicos.setCumpleCargaMinima(cargaCellInt);
                        }

                        // Crea banderas de error
                        Boolean regularOK = datosAcademicos.getRegular() != -1;
                        Boolean inscritoOK = datosAcademicos.getInscrito() != -1;
                        Boolean promedioOK = promedioValido(datosAcademicos.getPromedio());
                        Boolean carreraOK = datosAcademicos.getCarrera() != null;
                        Boolean semestreOK = semestreValido(alumno, datosAcademicos.getSemestre());
                        Boolean cargaOK = datosAcademicos.getCumpleCargaMinima() != -1;

                        // Comienza Crea tabla para UI
                        aux.add(String.valueOf(cont));
                        // Boleta
                        if (carreraOK) {
                            aux.add(boleta);
                        } else {
                            aux.add("<span class='label label-danger'>" + boleta + "</span>");
                        }
                        aux.add(String.valueOf(promedio));
                        // Promedio
                        if (promedioOK) {
                            aux.add(promedioCell);
                        } else {
                            aux.add("<span class='label label-danger'>" + promedioCell + "</span>");
                        }
                        aux.add(String.valueOf(semestre));
                        // Semestre
                        if (semestreOK) {
                            aux.add(semestreCell);
                        } else {
                            aux.add("<span class='label label-danger'>" + semestreCell + "</span>");
                        }
                        aux.add(inscritoInitial);
                        // Inscrito
                        if (inscritoOK) {
                            aux.add(inscritoCellStr);
                        } else {
                            aux.add("<span class='label label-danger'>" + inscritoCellStr + "</span>");
                        }
                        aux.add(regularInitial);
                        // Regular
                        if (regularOK) {
                            aux.add(regularCellStr);
                        } else {
                            aux.add("<span class='label label-danger'>" + regularCellStr + "</span>");
                        }
                        aux.add(cargaInitial);
                        // Carga académica
                        if (cargaOK) {
                            aux.add(cargaCellStr);
                        } else {
                            aux.add("<span class='label label-danger'>" + cargaCellStr + "</span>");
                        }

                        // Inicia proceso guardado
                        Boolean noHayErrores = regularOK && inscritoOK
                                && promedioOK && carreraOK && semestreOK && cargaOK;
                        if (noHayErrores) {
                            Boolean huboCambios = !promedio.equals(datosAcademicos.getPromedio())
                                    || !semestre.equals(datosAcademicos.getSemestre())
                                    || !regular.equals(datosAcademicos.getRegular())
                                    || !inscrito.equals(datosAcademicos.getInscrito())
                                    || !cargaA.equals(datosAcademicos.getCumpleCargaMinima());

                            // Si hubo cambios y no tiene otorgamiento, guarda (datosAcademicos ya podría traer cambios)
                            Boolean tieneO = service.getOtorgamientoDao().tieneOtorgamientoDistintoUniversal(alumno.getId());
                            if (huboCambios && !tieneO) {
                                try {
                                    service.getDatosAcademicosDao().update(datosAcademicos);
                                    BitacoraAlumno bitacora = new BitacoraAlumno(usuario, new Date(), alumno, currentTerm,
                                            datosAcademicos.getSemestre(), datosAcademicos.getPromedio(),
                                            datosAcademicos.getInscrito(), datosAcademicos.getRegular());
                                    service.getBitacoraAlumnoDao().save(bitacora);

                                    aux.add("<span class='label label-success' data-toggle='tooltip' title='Operación realizada exitosamente.' data-placement='left' data-trigger='hover' data-content='Resultado'>OK</span>");
                                } catch (PersistenceException pe) {
                                    pe.printStackTrace();
                                }

                            } else if (tieneO) {
                                aux.add("<span class='label label-danger'  data-toggle='tooltip' title='El alumno ya cuenta con un otorgamiento.' data-placement='left' data-trigger='hover' data-content='Código de error EE05'>EE05</span>");
                            } else if (!huboCambios) {
                                aux.add("<span class='label label-warning'  data-toggle='tooltip' title='Los datos se cargaron igual a los datos que ya se encontraban en el sistema.' data-placement='left' data-trigger='hover' data-content='Código de error AD01'>AD01</span>");
                            }
                        } else if (!regularOK) {
                            aux.add("<span class='label label-danger'  data-toggle='tooltip' title='El estatus de regularidad no es valido.' data-placement='left' data-trigger='hover' data-content='Código de error EE07'>EE07</span>");
                        } else if (!inscritoOK) {
                            aux.add("<span class='label label-danger'  data-toggle='tooltip' title='El estatus de inscripción no es valido.' data-placement='left' data-trigger='hover' data-content='Código de error EE06'>EE06</span>");
                        } else if (!promedioOK) {
                            aux.add("<span class='label label-danger'  data-toggle='tooltip' title='El promedio no es valido.' data-placement='left' data-trigger='hover' data-content='Código de error EE03'>EE03</span>");
                        } else if (!carreraOK) {
                            aux.add("<span class='label label-danger'  data-toggle='tooltip' title='El alumno parece no tener sus datos consolidados en la DAE, favor de verificar su carrera.' data-placement='left' data-trigger='hover' data-content='Código de error EE08'>EE08</span>");
                        } else if (!semestreOK) {
                            aux.add("<span class='label label-danger'  data-toggle='tooltip' title='El semestre no es valido para la carrera del alumno.' data-placement='left' data-trigger='hover' data-content='Código de error EE04'>EE04</span>");
                        } else if (!cargaOK) {
                            aux.add("<span class='label label-danger'  data-toggle='tooltip' title='La carga académica no es válida.' data-placement='left' data-trigger='hover' data-content='Código de error EE11'>EE11</span>");
                        }

                        logScreen.add(aux);
                    } else {
                        aux.add(String.valueOf(cont));
                        aux.add(boleta);
                        for (int i = 0; i < columnas; i++) {
                            aux.add("N/A");
                        }
                        aux.add("<span class='label label-danger'  data-toggle='tooltip' title='El alumno no pertenece a su Unidad Académica.' data-placement='left' data-trigger='hover' data-content='Código de error EE10'>EE10</span>");

                        logScreen.add(aux);
                    }
                } else if (alumno == null) {
                    aux.add(String.valueOf(cont));
                    aux.add(boleta);
                    for (int i = 0; i < columnas; i++) {
                        aux.add("N/A");
                    }
                    aux.add("<span class='label label-danger'  data-toggle='tooltip' title='No existe un alumno con ésta boleta.' data-placement='left' data-trigger='hover' data-content='Código de error EE02'>EE02</span>");

                    logScreen.add(aux);

                } else {
                    aux.add(String.valueOf(cont));
                    aux.add(boleta);
                    for (int i = 0; i < columnas; i++) {
                        aux.add("N/A");
                    }
                    aux.add("<span class='label label-danger' data-toggle='tooltip' title='El alumno no cuenta con datos académicos para este periodo.' data-placement='left' data-trigger='hover' data-content='Código de error EE09'>EE09</span>");

                    logScreen.add(aux);
                }
            }
        }
        return logScreen;
    }

    /**
     * Procesa una cadena para regresar un Int que sea congruente con el campo
     * CUMPLECARGAMINIMA de ENT_ALUMNO_DATOS_ACADEMICOS. 0: No cumple, 1: Si
     * cumple, 2:Carga Media, 3: Carga Máxima
     *
     * @author Mario Márquez
     * @param String cargaCell Cadena de texto MAXIMA, MEDIA, MENOR, MINIMA
     * @return Integer
     */
    private Integer creaVersionBDCarga(String cargaCell) {
        Integer result = -1;

        AmbienteEnums enums = AmbienteEnums.getInstance();
        // recorre el enum de carga en búsqueda de uno que coincida
        for (Map.Entry<String, String> entry : enums.getCarga().entrySet()) {
            if (limpiaString(entry.getValue()).equals(cargaCell)) {
                result = Integer.parseInt(entry.getKey());
            }
        }

        return result;
    }

    /**
     * Procesa una cadena afirmativa o negativa, y la traduce a binario
     *
     * @author Mario Márquez
     * @param String infoCell Cadena de texto SI, NO
     * @return Integer
     */
    private Integer creaVersionBD(String infoCell) {
        Integer result = -1;

        switch (infoCell) {
            case "SI":
            case "1":
                result = 1;
                break;
            case "NO":
            case "0":
                result = 0;
                break;
        }

        return result;
    }

    /**
     * Procesa un Integer a cadena afirmativa o negativa.
     *
     * @author Mario Márquez
     * @param Integer infoCell Entero 1, 0, -1
     * @return String
     */
    private String creaVersionUI(Integer infoCell) {
        String result = "Error";

        switch (infoCell) {
            case 1:
                result = "Sí";
                break;
            case 0:
                result = "No";
                break;
        }

        return result;
    }

    /**
     * Procesa un Integer a cadena descriptiva sobre el estado de la carga
     * académica
     *
     * @author Mario Márquez
     * @param Integer infoCell 0, 1, 2, 3
     * @return String
     */
    private String creaVersionUICarga(Integer infoCell) {
        String result = "Error";

        AmbienteEnums enums = AmbienteEnums.getInstance();
        String carga = enums.getCarga().get(infoCell.toString());
        if (carga != null) {
            result = carga;
        }

        return result;
    }

    /**
     * Regresa una cadena sin acento en la i, e, sin espacios y en mayúsculas
     *
     * @author Mario Márquez
     * @param String
     * @return String
     */
    private String limpiaString(String string) {
        String result = string.toUpperCase().replace("Í", "I").replace("Á", "A").trim();;

        return result;
    }

    

    public List<PadronProspera> padronProspera(Workbook wb) throws Exception {
        List<PadronProspera> lst = new ArrayList<>();
        Sheet sheet = wb.getSheetAt(0);

        for (int cont = 1; cont <= sheet.getLastRowNum(); cont++) {
            Row row = sheet.getRow(cont);
            String curp = row.getCell(5).toString();
            String nom = row.getCell(8).toString();
            String app = row.getCell(6).toString();
            String apm = row.getCell(7).toString();
            Alumno a;
            PadronProspera pp = new PadronProspera();

            if (service.getAlumnoDao().existeCurp(curp)) {
                a = service.getAlumnoDao().buscarPorCURP(curp).get(0);
                if (!service.getPadronProsperaDao().existeAlumnoPeriodo(a)) {
                    pp.setAlumno(a);
                    if (row.getCell(4).getCellType() == Cell.CELL_TYPE_STRING) {
                        pp.setFolio(row.getCell(4).toString());
                    } else if (row.getCell(4).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        String num = row.getCell(4).getNumericCellValue() + "";
                        pp.setFolio(new BigDecimal(num).toBigInteger() + "");
                    } else {
                        pp.setFolio(row.getCell(4).toString());
                    }
                    pp.setPeriodo(service.getPeriodoDao().getPeriodoActivo());
                    pp.setFecha(new Date());
                    pp.setCurp(curp);
                    pp.setNombres(nom);
                    pp.setApellidoPaterno(app);
                    pp.setApellidoMaterno(apm);
                    service.getPadronProsperaDao().save(pp);
                }
            } else if (service.getAlumnoDao().existeNombre(nom, app, apm)) {
                a = service.getAlumnoDao().buscarPorNombre(nom, app, apm).get(0);
                if (!service.getPadronProsperaDao().existeAlumnoPeriodo(a)) {
                    pp.setAlumno(a);
                    if (row.getCell(4).getCellType() == Cell.CELL_TYPE_STRING) {
                        pp.setFolio(row.getCell(4).toString());
                    } else if (row.getCell(4).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        String num = row.getCell(4).getNumericCellValue() + "";
                        pp.setFolio(new BigDecimal(num).toBigInteger() + "");
                    } else {
                        pp.setFolio(row.getCell(4).toString());
                    }
                    pp.setPeriodo(service.getPeriodoDao().getPeriodoActivo());
                    pp.setFecha(new Date());
                    pp.setCurp("-");
                    pp.setNombres(nom);
                    pp.setApellidoPaterno(app);
                    pp.setApellidoMaterno(apm);
                    service.getPadronProsperaDao().save(pp);
                }
            } else {    //EL ALUMNO NO FUE ENCONTRADO DENTRO DEL SISTEMA
                if (row.getCell(4).getCellType() == Cell.CELL_TYPE_STRING) {
                    pp.setFolio(row.getCell(4).toString());
                } else if (row.getCell(4).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    String num = row.getCell(4).getNumericCellValue() + "";
                    pp.setFolio(new BigDecimal(num).toBigInteger() + "");
                } else {
                    pp.setFolio(row.getCell(4).toString());
                }
                pp.setCurp(curp);
                pp.setNombres(nom);
                pp.setApellidoPaterno(app);
                pp.setApellidoPaterno(apm);
                lst.add(pp);
            }

        }
        return lst;
    }

    public List<AlumnoDAE> cargaFichaEscolar(Workbook wb) throws Exception {
        List<AlumnoDAE> lst = new ArrayList<>();
        Sheet sheet = wb.getSheetAt(0);

        for (int cont = 1; cont <= sheet.getLastRowNum(); cont++) {
            Row row = sheet.getRow(cont);
            try {
                String boleta = row.getCell(0).toString();
                Cell c = row.getCell(0);
                c.setCellType(Cell.CELL_TYPE_STRING);
                boleta = c.toString();
                List<AlumnoDAE> lstX = service.getAlumnoDAEDao().findByBoleta(boleta);
                if (lstX != null && lstX.size() > 0) {
                    AlumnoDAE ad = lstX.get(0);
                    ad = datosRowAlumno(row, ad, Boolean.TRUE);
                    if (ad != null) {
                        lst.add(ad);
                    }
                } else {
                    AlumnoDAE ad = new AlumnoDAE();
                    ad = datosRowAlumno(row, ad, Boolean.FALSE);
                    if (ad != null) {
                        lst.add(ad);
                    }
                }
            } catch (PersistenceException pe) {
                pe.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return lst;
    }

    public AlumnoDAE datosRowAlumno(Row row, AlumnoDAE adae, Boolean encontrado) {
        if (row.getCell(0) != null) {
            adae.setBoleta(row.getCell(0) == null ? null : row.getCell(0).toString());
            adae.setCurp(row.getCell(1) == null ? null : row.getCell(1).toString());
            adae.setNombre(row.getCell(2) == null ? null : row.getCell(2).toString());
            adae.setApellido_pat(row.getCell(3) == null ? null : row.getCell(3).toString());
            adae.setApellido_mat(row.getCell(4) == null ? null : row.getCell(4).toString());
            adae.setEscuela(row.getCell(5) == null ? null : row.getCell(5).toString());
            adae.setPromedio(row.getCell(6) == null ? null : Float.valueOf(row.getCell(6).toString()));
            adae.setTurno(row.getCell(7) == null ? null : row.getCell(7).toString());
            adae.setFecha_nac(row.getCell(8) == null ? null : new Date(row.getCell(8).toString()));
            adae.setSemestre_min(row.getCell(9) == null ? null : row.getCell(9).getCellType() == Cell.CELL_TYPE_STRING ? Integer.parseInt(row.getCell(9).toString()) : (int) row.getCell(9).getNumericCellValue());
            adae.setSemestre_max(row.getCell(10) == null ? null : row.getCell(10).getCellType() == Cell.CELL_TYPE_STRING ? Integer.parseInt(row.getCell(10).toString()) : (int) row.getCell(10).getNumericCellValue());
            adae.setSemestre_inscrito(row.getCell(11) == null ? null : row.getCell(11).getCellType() == Cell.CELL_TYPE_STRING ? Integer.parseInt(row.getCell(11).toString()) : (int) row.getCell(11).getNumericCellValue());
            adae.setCalle(row.getCell(12) == null ? null : row.getCell(12).toString());
            adae.setColonia(row.getCell(13) == null ? null : row.getCell(13).toString());
            adae.setEstado(row.getCell(14) == null ? null : row.getCell(14).toString());
            adae.setCp(row.getCell(15) == null ? null : row.getCell(15).toString());
            adae.setCarrera(row.getCell(16) == null ? null : row.getCell(16).toString());
            adae.setEspecialidad(row.getCell(17) == null ? null : row.getCell(17).toString());
            adae.setPlan_estud(row.getCell(18) == null ? null : row.getCell(18).toString());
            adae.setE_mail(row.getCell(19) == null ? null : row.getCell(19).toString());
            adae.setTipo_alumno(row.getCell(20) == null ? null : row.getCell(20).toString());
            adae.setInscrito(row.getCell(21) == null ? null : (int) row.getCell(21).getNumericCellValue());
            adae.setSexo(row.getCell(22) == null ? null : row.getCell(22).toString());
            adae.setTot_creditos(row.getCell(23) == null ? null : Float.valueOf(row.getCell(23).toString()));
            adae.setModo_ingreso(row.getCell(24) == null ? null : (int) row.getCell(24).getNumericCellValue());
            adae.setPeriodo_escolar_ingreso(row.getCell(25) == null ? null : row.getCell(25).getCellType() == Cell.CELL_TYPE_STRING ? Integer.parseInt(row.getCell(25).toString()) : (int) row.getCell(25).getNumericCellValue());
            adae.setEgresado(row.getCell(26) == null ? null : row.getCell(26).getCellType() == Cell.CELL_TYPE_STRING ? Integer.parseInt(row.getCell(26).toString()) : (int) row.getCell(26).getNumericCellValue());
            adae.setCumple_carga_minima(row.getCell(27) == null ? null : row.getCell(27).getCellType() == Cell.CELL_TYPE_STRING ? Integer.parseInt(row.getCell(27).toString()) : (int) row.getCell(27).getNumericCellValue());

            adae.setOrigen(1);
//	    adae.setFecha_ingreso(new Date());
            Usuario u = (Usuario) ActionContext.getContext().getSession().get("usuario");
            adae.setUsuario(u);
//	    adae.setConfronta_dae(1);

            Alumno a = service.getAlumnoDao().findByBoleta(adae.getBoleta());
            if (a != null) {
                adae.setRegistradoSistema(Boolean.TRUE);
                if (service.getSolicitudBecaDao().exiteESEPeriodoActivo(a.getId(), BigDecimal.ONE, service.getPeriodoDao().getPeriodoActivo().getId())) {
                    adae.setLlenoESE(Boolean.TRUE);
                } else {
                    adae.setLlenoESE(Boolean.FALSE);
                }
            } else {
                adae.setRegistradoSistema(Boolean.FALSE);
                adae.setLlenoESE(Boolean.FALSE);
            }

            if (encontrado && adae.getId() != null) {
                adae.setResultadoCarga(Boolean.TRUE);
                service.getAlumnoDAEDao().update(adae);
            } else {
                adae.setResultadoCarga(Boolean.FALSE);
                service.getAlumnoDAEDao().save(adae);
            }

            return adae;
        } else {
            return null;
        }
    }

    /**
     * Implementa metodo vacio
     *
     * @author Victor Lozano
     * @param <T>
     * @param wb
     * @param lote
     * @param fecha
     * @return lista
     * @throws java.lang.Exception
     */
    @Override
    public <T> List<T> processFile(Workbook wb, String lote, Date fecha) throws Exception {
        return null;
    }

    /**
     * Implementa metodo vacio
     *
     * @author Tania G. Sánchez
     * @param <T>
     * @param wb
     * @param periodo
     * @return lista
     * @throws java.lang.Exception
     */
    @Override
    public <T> List<T> processFile(Workbook wb, BigDecimal periodo) throws Exception {
        return null;
    }

    /**
     * Implementa metodo vacio
     *
     * @author Tania G. Sánchez
     * @param <T>
     * @param wb
     * @param periodo
     * @param accion
     * @return lista
     * @throws java.lang.Exception
     */
    @Override
    public <T> List<T> processFile(Workbook wb, BigDecimal periodo, Integer accion) throws Exception {
        return null;
    }

    /**
     * Valida el nuevo promedio del alumno
     *
     * @author Victor Lozano
     * @param promedio
     * @return validez del promedio
     */
    private Boolean promedioValido(Double promedio) {
        if (promedio >= 0 && promedio <= 10) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * Valida el nuevo semestre del alumno
     *
     * @author Victor Lozano
     * @param alumno
     * @return validez del semestre
     */
    private Boolean semestreValido(Alumno alumno, Integer semestre) {
        DatosAcademicos datosAcademicos = service.getDatosAcademicosDao().ultimosDatos(alumno.getId());
        Integer semestreMaximo = relacion.get(datosAcademicos.getCarrera().getId());
        if (semestre >= 1 && semestre <= semestreMaximo) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public Alumno datosAlumnoDAE(Alumno alumno, Boolean oAnt) throws ErrorDaeException {
//        DatosAcademicos datosAcademicos = new DatosAcademicos();
        TuplaValidacion tuplaValidacion = validarAlumno(alumno.getBoleta(), true);
        Periodo periodo = service.getPeriodoDao().getPeriodoActivo();
        AlumnoDAE a = new AlumnoDAE();
        if (tuplaValidacion.getErrorDAE() != null) {
            if (tuplaValidacion.getErrorDAE().equals(ErrorDAE.NO_INSCRITO)) {

            } else {
//                return null;
                throw new ErrorDaeException(tuplaValidacion.getErrorDAE().getMsg());
            }
        }
        a = tuplaValidacion.getAlumnoDAE();
        agregarDatosAcademicos(alumno, a, periodo);
//        datosAcademicos.setUnidadAcademica(service.getUnidadAcademicaDao().getByClave(String.valueOf(Integer.parseInt(a.getEscuela()))));
//        datosAcademicos.setAlumno(alumno);
//        datosAcademicos.setPeriodo(periodo);

//        if (!oAnt) {
//            datosAcademicos.setSemestre(a.getSemestre_inscrito());
//        }
        if (oAnt) {
            DatosAcademicos datosAcademicos = service.getDatosAcademicosDao().ultimosDatos(alumno.getId());
            datosAcademicos.setSemestre(0);
        }
//        datosAcademicos.setPromedio(a.getPromedio() == null ? 0 : (double) a.getPromedio());
        //Se obtiene la carrera
//        validarCarrera(a);

//        datosAcademicos.setCarrera(a.getCarreraObj());
//        datosAcademicos.setInscrito(a.getInscrito() == 1 ? 1 : 0);
        return alumno;
    }

    // Información para correo genérico (Envío de correos)
    public final Map modeloGenerico(String asunto, String atentamente, String mensaje, File file, String fileName) {
        // Arma un mapa con la información que contendrá el correo
        Map model = new HashMap();
        // model.put("alumno", alumno);
        model.put("asunto", asunto);
        model.put("mensaje", mensaje);
        model.put("atentamente", atentamente);
        if (file != null) {
            model.put("nombreArchivo", fileName);
            model.put("archivo", file);
        }

        return model;
    }

    // Información para correo genérico (Depósitos)
    public final Map infoCorreo(Alumno alumno, String asunto, String atentamente, String mensaje, File file, String fileName) {
        // Arma un mapa con la información que contendrá el correo
        Map model = new HashMap();
        model.put("alumno", alumno.getFullName());
        model.put("asunto", asunto);
        model.put("mensaje", mensaje);
        model.put("atentamente", atentamente);
        if (file != null) {
            model.put("nombreArchivo", fileName);
            model.put("archivo", file);
        }

        return model;
    }

    public Boolean enviarCorreo(String destinatario, Map info) {
        try {
            // Envío síncrono
            sendEmailSync(destinatario, info);
            // Envío asíncrono
            // sendEmail(alumno.getCorreoElectronico(), asunto, body.toString());
            return true;
        } catch (Exception ex) {
            Logger.getLogger(AlumnoBO.class.getName()).log(Level.WARNING, "envío de correos", ex);
            return false;
        }
    }

    public List<Alumno> enviarCorreos(List<Alumno> lstAlumnos, Map mailInfo, String[] mapData) {
        System.out.println("Envío correos BO");
        String asunto = mailInfo.get("asunto").toString();
        // Variables para dividir la lista de alumnos
        List<List<Alumno>> partitions = new LinkedList<>();
        List<Alumno> alumnosError = new ArrayList<>();
        int partitionSize = 100;

        ProgressBarManager manager = new ProgressBarManager();
        Boolean res = false;

        // Ajusta cantidades para la barra de progreso
        manager.setTotalDeElementosCorreos(lstAlumnos.size());
        if (!manager.cambiarElementoCorreo(lstAlumnos.size(), mapData)) {
            // Si no es posible cambiar el elemento, entonces ya existe un envío 
            // en curso, toda la lista se regresa como lista de errores
            return lstAlumnos;
        }

        // Crea grupos de alumnos
        for (int i = 0; i < lstAlumnos.size(); i += partitionSize) {
            partitions.add(lstAlumnos.subList(i,
                    Math.min(i + partitionSize, lstAlumnos.size())));
        }
        
        for (List<Alumno> partition : partitions) {
            // Para cada alumno envía un correo
            for (Alumno alumno : partition) {
                String mailAdrss = alumno.getCorreoElectronico();
                // Si el correo es válido, lo envía
                if (correoValido(mailAdrss)) {
                    // Se agrega el alumno al modelo
                    mailInfo.put("alumno", alumno);
                    // Envía el correo
                    res = enviarCorreo(mailAdrss, mailInfo);
                }
                // Según tenga éxito, aumenta el contador correspondiente.
                if (res) {
                    manager.aumentarRevalidacionAsignada();
                } else {
                    manager.aumentarRevalidacionNoAsignada();
                    alumnosError.add(alumno);
                }
            }
            // Si hay más de un conjunto de alumnos, el envío descansa 
            if (partitions.size() > 1) {
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AlumnoBO.class.getName()).log(Level.FINE, null, ex);
                }
            }
        }
        lstAlumnos.clear(); // Libera memoria

        // Limpia información del admin de la barra de progreso
        manager.borraBarraCorreo(mapData);

        return alumnosError;
    }

    // Para evaluar la dirección de correo
    public final Boolean correoValido(String mail) {
        Pattern patternMail = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
        Matcher matcher = patternMail.matcher(mail);
        return matcher.matches();
    }

    public Boolean enviarCorreoCC(String mailAdrss, Map mailInfo, String[] mapData) {
        String asunto = mailInfo.get("asunto").toString();

        ProgressBarManager manager = new ProgressBarManager();
        Boolean res = false;

        // Ajusta cantidades para la barra de progreso
        manager.setTotalDeElementosCorreos(1);
        manager.cambiarElementoCorreo(1, mapData);

        // Se agrega un alumno vacío al modelo
        Alumno alumno = new Alumno();
        mailInfo.put("alumno", alumno);

        // Envía el correo
        res = enviarCorreo(mailAdrss, mailInfo);
        // Según tenga éxito, aumenta el contador correspondiente.
        if (res) {
            manager.aumentarRevalidacionAsignada();
        } else {
            manager.aumentarRevalidacionNoAsignada();
        }

        // Limpia información del admin de la barra de progreso
        manager.borraBarraCorreo(mapData);

        return res;
    }

    public TuplaValidacion validarAlumno(String numeroDeBoleta, Boolean monitoreo, String... curp) {
        TuplaValidacion tuplaValidacion = new TuplaValidacion();

        List<AlumnoDAE> lista = null;
        try {
            if (curp != null && curp.length > 0) {
                lista = service.getAlumnoDAEDao().findByCurpManutencion(curp[0]);
            }
            if (lista == null || lista.isEmpty()) {
                lista = service.getAlumnoDAEDao().findByBoleta(numeroDeBoleta);
            }
        } catch (UnexpectedRollbackException | PersistenceException | DatabaseException | LocalTransactionException ex) {
            log.warn("AlumnoBO::validarAlumno(" + numeroDeBoleta + ")\n" + ex.getMessage());
            tuplaValidacion.setErrorDAE(ErrorDAE.NO_CONEXION);
            return tuplaValidacion;
        }

        AlumnoDAE posibleCandidato = null;
        AlumnoDAE posibleCandidatoNoInscrito = null;

        if (lista == null || lista.isEmpty()) {
            tuplaValidacion.setErrorDAE(ErrorDAE.NO_ENCONTRADO);
            return tuplaValidacion;
        } else {
            for (AlumnoDAE alumnoDAE : lista) {
                try {
                    if (!alumnoDAE.getInscrito().equals("0")) {
                        if (posibleCandidato == null) {
                            posibleCandidato = alumnoDAE;
                        } else if (alumnoDAE.getPeriodo_escolar_ingreso() > posibleCandidato.getPeriodo_escolar_ingreso()) {
                            posibleCandidato = alumnoDAE;
                        }
                    } else if (posibleCandidatoNoInscrito == null) {
                        posibleCandidatoNoInscrito = alumnoDAE;
                    } else if (alumnoDAE.getPeriodo_escolar_ingreso() > posibleCandidatoNoInscrito.getPeriodo_escolar_ingreso()) {
                        posibleCandidatoNoInscrito = alumnoDAE;
                    }
                } catch (NullPointerException ex) {
                    tuplaValidacion.setErrorDAE(ErrorDAE.MUCHOS_ENCONTRADO);
//                    RESERVAMOS EL ERROR CON LA INTENCIÓN DE QUE APAREZCA PRIMERO EL ERROR DE "NO INSCRITO" Y DESPUÉS EL DE
//                    MULTIPLES REGISTROS (linea 788)
//                    return tuplaValidacion;
                }
            }
            if (posibleCandidato == null) {
                tuplaValidacion.setAlumnoDAE(posibleCandidatoNoInscrito);
                tuplaValidacion.setErrorDAE(ErrorDAE.NO_INSCRITO);
                return tuplaValidacion;
            }
            if (tuplaValidacion.getErrorDAE() != null) {
                return tuplaValidacion;
            }
        }

        posibleCandidato.setPromedio(posibleCandidato.getPromedio() == null ? Float.valueOf("0") : posibleCandidato.getPromedio());
        //Validar campos academicos
        boolean camposValidos = validarCampos(posibleCandidato.getEscuela(), posibleCandidato.getPromedio(),
                posibleCandidato.getSemestre_inscrito(), posibleCandidato.getCarrera(),
                posibleCandidato.getEspecialidad(), posibleCandidato.getInscrito(),
                posibleCandidato.getModo_ingreso());

        if (!camposValidos) {
            tuplaValidacion.setErrorDAE(ErrorDAE.DATOS_ACADEMIOS);
            if (monitoreo) {
                avisoErrorDatosAcademicos("El alumno " + numeroDeBoleta + " presenta nulos en alguno de los datos necesarios para obtener su carrera: \n" + posibleCandidato);
            }
            return tuplaValidacion;
        }
        tuplaValidacion.setAlumnoDAE(posibleCandidato);
        return tuplaValidacion;
    }

    private void avisoErrorDatosAcademicos(String texto) {
        String[] to = {"charlyfroy@gmail.com", "epbenitez@ipn.mx"};
        try {
            sendEmail(to, "SIBec: No se encuentra carrera", texto);
        } catch (MessagingException ex) {
            Logger.getLogger(AlumnoBO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean validarCampos(Object... campos) {
        for (Object campo : campos) {
            if (campo == null) {
                return false;
            }
            if (campo instanceof String) {
                if (((String) campo).isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<ResumenValidacionInscripcion> validarInscripcion() {
        Periodo activo = service.getPeriodoDao().getPeriodoActivo();

        List<Alumno> validacionInscripcion = service.getAlumnoDao().validacionInscripcion(activo.getId(), activo.getPeriodoAnterior().getId());
        List<ResumenValidacionInscripcion> errores = new ArrayList<>();
        int k = 1;

        for (Alumno alumno : validacionInscripcion) {
            try {
                DatosAcademicos daAnteriores = service.getDatosAcademicosDao().ultimosDatos(alumno.getId());
                Tupla<Alumno, ErrorDAE> tuplaDAE = establecerDatosAcademicosDAE(alumno);

                if (tuplaDAE.getK1() != null) {
                    DatosAcademicos daNuevos = service.getDatosAcademicosDao().ultimosDatos(alumno.getId());
                    if (daAnteriores.getUnidadAcademica().getId().intValue() != daNuevos.getUnidadAcademica().getId().intValue()) {
                        errores.add(new ResumenValidacionInscripcion(alumno, "El alumno cambio de escuela"));
                    }
                    //System.out.println("Bien " + k + " " + alumno.getId());
                } else {
                    errores.add(new ResumenValidacionInscripcion(alumno, tuplaDAE.getK2().getMsg()));
                    //System.out.println("Mal " + k + " " + alumno.getId());
                }
            } catch (ErrorDaeException ex) {
                errores.add(new ResumenValidacionInscripcion(alumno, "El alumno no tiene una carrera válida"));
            }
            k++;
        }
        return errores;
    }

    /**
     * Devuelve un arreglo de 3 enteros que se usará para mandar la información
     * al jsp con el resumen de la preasignación de los alumnos
     *
     * El resumen se almacenará de la siguiente forma:
     *
     * info[0] = total -> Total de alumnos que se intentará preasignar info[0] =
     * buenos -> Total de alumnos preasignados info[0] = malos -> Total de
     * alumnos NO preasignados
     *
     * @author Augusto H.
     * @return int[] info
     */
    public int[] actualizarPreasignacion() throws Exception {

        //Arreglo que contendrá los numeros de alumnos dependiendo del resultado de su preasignación
        int[] info = new int[4];

        /*
        Cambios del módulo Validación de Inscripción para el periodo 2019-1
        1.- Buscar la solicitud de beca del alumno correspondiente al periodo anterior.
        2.- Copiar el campo TIPOBECAPREASIGNADA_ID al campo TIPOBECAPREASIGNADAORIGINAL_ID de la tabla ENT_SOLICITUD_BECAS.
        3.- Buscar en la tabla ENT_TIPO_BECA_PERIODO el id del punto anterior (TIPOBECAPREASIGNADA_ID).        
        4.- Verificar el ID del campo TIPOBECA_ID.        
        5.- Buscar el ID  del campo TIPOBECA_ID para el periodo actual en la tabla  ENT_TIPO_BECA_PERIODO.
        6.- Actualizar el campo  TIPOBECAPREASIGNADA_ID de la solicitud del punto 1, con el id encontrado en el punto 5.
         */
        Periodo activo = service.getPeriodoDao().getPeriodoActivo();

        //Obtenemos la lista de tbp.id anterior con el actual
        List<Object[]> hmap = service.getTipoBecaPeriodoDao().getTbpActualAnterior(activo);

        if (hmap.isEmpty()) {
            throw new Exception("No se encontraron tipos de beca correspondientes para el periodo actual");
        }

        //Hash Map para tbp.id anterior con el actual
        HashMap<BigDecimal, BigDecimal> map = new HashMap<>();

        //Asignamos los (key,value)
        for (Object[] tbp : hmap) {
            map.put((BigDecimal) tbp[0], (BigDecimal) tbp[1]);
        }

        //Buscar la solicitud de beca del alumno correspondiente al periodo anterior.
        info = service.getSolicitudBecaDao().getSolicitudesValidacionInscripcion(activo.getId(), activo.getPeriodoAnterior().getId(), map);
        return info;
    }

    /**
     * Obtiene los últimos 2 depósitos aplicados, para mostrarse en el panel
     * alumno
     *
     * @author Mario Márquez
     * @param depositos
     * @return List<Depositos>
     */
    public List<Notificacion> obtenerNotificaciones(Alumno alumno, List<Depositos> depositos) {
        List<Notificacion> notificaciones = new ArrayList<>();

        //Depositos
        if (depositos != null) {
            for (Depositos deposito : depositos) {
                notificaciones.add(getNotificacionDeposito(deposito));
            }
        }

        if (notificaciones.isEmpty()) {
            notificaciones.addAll(obtenerNotificacionVacia());
        } else {
            Collections.sort(notificaciones, Collections.reverseOrder());
            if (notificaciones.size() > 5) {
                notificaciones = notificaciones.subList(0, 5); // Mostrar sólo 6
            }
        }

        return notificaciones;
    }

    /**
     * Obtiene los últimos 2 depósitos aplicados, para mostrarse en el panel
     * alumno
     *
     * @author Mario Márquez
     * @param depositos
     * @return List<Depositos>
     */
    private Notificacion getNotificacionDeposito(Depositos deposito) {
        int id = deposito.getEstatusDeposito().getId().intValue();
        StringBuilder mensaje = new StringBuilder();
        Notificacion notificacion = new Notificacion();

        mensaje.append("<b>").append(deposito.getOtorgamiento()
                .getTipoBecaPeriodo().getTipoBeca().getNombre()).append("</b>");
        switch (id) {
            case 2:
                mensaje.append(" Haz recibido un depósito (");
                mensaje.append(EstadisticasBO.getMes(String.valueOf(deposito.getOrdenDeposito().getMes())));
                mensaje.append(") por una cantidad de $ ").append(deposito.getMonto());
                notificacion.setTipo(Notificacion.Tipo.DEPOSITO);
                break;
            case 4:
                mensaje.append(" Hubo un problema al intentar hacer el depósito correspondiente al mes de ");
                mensaje.append(EstadisticasBO.getMes(String.valueOf(deposito.getOrdenDeposito().getMes())));
                mensaje.append(" revisa tu historial de depósitos");
                notificacion.setTipo(Notificacion.Tipo.ERRORDEPOSITO);
                break;
            case 10:
                mensaje.append(" Se ha intentado realizar un nuevo depósito a tu cuenta.");
                mensaje.append(" Es necesario esperar la respuesta del banco.");
                notificacion.setTipo(Notificacion.Tipo.ENTREGA);
                break;
            default:
                mensaje.append(" Depósito con estatus desconocido correspondiente al mes de ");
                mensaje.append(EstadisticasBO.getMes(String.valueOf(deposito.getOrdenDeposito().getMes())));
                notificacion.setTipo(Notificacion.Tipo.ERRORCONFIG);
                break;
        }
        notificacion.setMensaje(mensaje.toString());
        notificacion.setFecha(deposito.getFechaDeposito());

        return notificacion;
    }

    public List<Notificacion> obtenerNotificacionVacia() {
        List<Notificacion> notificaciones = new ArrayList<>();

        Notificacion not = new Notificacion();
        not.setFecha(new Date());
        not.setMensaje("No tienes mensajes");
        not.setTipo(Notificacion.Tipo.VACIA);
        notificaciones.add(not);

        return notificaciones;
    }

    /**
     * Obtiene la fecha de nacimiento, la entidad de nacimiento y el genero
     * basandose en el CURP.
     *
     * @param alumno
     */
    public void obtenerDatosCURP(Alumno alumno) throws ErrorDaeException {
        try {
            //Se obtiene la entidad de nacimiento del CURP
            EntidadFederativa entidadFederativa = service.getEntidadFederativaDao().findByClave(alumno.getCurp().substring(11, 13));
            alumno.setEntidadDeNacimiento(entidadFederativa);
            //Se obtiene la fecha de nacimiento del CURP.
            String año = "";
            if (Integer.parseInt(alumno.getCurp().substring(4, 6)) < 17) {
                año = "20" + alumno.getCurp().substring(4, 6);
            } else {
                año = "19" + alumno.getCurp().substring(4, 6);
            }
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            try {
                Date fechaDeNacimiento = formatter.parse(alumno.getCurp().substring(8, 10) + "-" + alumno.getCurp().substring(6, 8) + "-" + año);
                alumno.setFechaDeNacimiento(fechaDeNacimiento);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //Se obtiene el genero de nacimiento del CURP
            Genero genero = new Genero();
            if ((alumno.getCurp().substring(10, 11)).equals("M")) {
                genero = service.getGeneroDao().findById(new BigDecimal("1"));
            } else if ((alumno.getCurp().substring(10, 11)).equals("H")) {
                genero = service.getGeneroDao().findById(new BigDecimal("2"));
            }
            alumno.setGenero(genero);
        } catch (Exception e) {
            e.printStackTrace();
            String curp = alumno.getCurp();
            alumno.setCurp(null);
            throw new ErrorDaeException("El CURP reportado por la DAE, no tiene el formato esperado: " + curp);

        }
    }

    public Integer calcularEdad(Date fechaNacimiento) {
        //Obtenemos el día de hoy
        Date hoy = new Date();
        SimpleDateFormat sdfDia = new SimpleDateFormat("dd");
        SimpleDateFormat sdfMes = new SimpleDateFormat("MM");
        SimpleDateFormat sdfAño = new SimpleDateFormat("yyyy");
        int a = Integer.parseInt(sdfAño.format(hoy)) - Integer.parseInt(sdfAño.format(fechaNacimiento));
        int m = Integer.parseInt(sdfMes.format(hoy)) - Integer.parseInt(sdfMes.format(fechaNacimiento));
        int d = Integer.parseInt(sdfDia.format(hoy)) - Integer.parseInt(sdfDia.format(fechaNacimiento));
        if (m < 0) {
            a = a - 1;
        } else if (m == 0) {
            if (d < 0) {
                a = a - 1;
            }
        }
        return a;
    }

    @Override
    public List<String> processFile(Workbook wb, UnidadAcademica unidadAcademica) throws Exception {
        relacion = service.getCarreraDao().getSemestresMaximos();
        List<String> logScreen = new LinkedList<>();
        Sheet sheet = wb.getSheetAt(0);
        AlumnoDatosBancariosBO abo = new AlumnoDatosBancariosBO(service);
        AlumnoDatosBancarios alumnoDatosBanco;
        for (int cont = 1; cont <= sheet.getLastRowNum(); cont++) {
            Row row = sheet.getRow(cont);
            log.info("Procesando la fila: " + cont + " de " + sheet.getLastRowNum());
            try {
                Alumno alumno = null;
                String boleta = "";
                if (row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING) {
                    String num = row.getCell(0) + "";
                    try {
                        boleta = new BigInteger(num) + "";
                    } catch (Exception e) {
                        boleta = num;
                    }
                } else if (row.getCell(0).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    String num = row.getCell(0).getNumericCellValue() + "";
                    boleta = new BigDecimal(num).toBigInteger() + "";
                }
                alumno = service.getAlumnoDao().findByBoleta(boleta);
                DatosAcademicos da = service.getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), service.getPeriodoDao().getPeriodoActivo().getId());
                alumno.setPermiteingresocuentaexterna(Boolean.TRUE);
                if (unidadAcademica != null && !unidadAcademica.getId().equals(da.getUnidadAcademica().getId())) {
                    logScreen.add("<td>" + alumno.getBoleta() + "</td>"
                            + "<td>" + alumno.getFullName() + "</td>"
                            + "<td><span class='label label-danger'>El alumno no pertenece a tu unidad académica.</span></td>"
                            + "</td>");
                } else {
                    AlumnoTarjetaBancaria tarjetaBancaria = null;
                    //Se obtienen los datos bancarios vigentes.
                    alumnoDatosBanco = abo.datosVigentes(alumno.getId());
                    //Se obtienen los datos de la tarjeta bancaria relacionada a los datos vigentes del alumno.
                    if (alumnoDatosBanco != null) {
                        tarjetaBancaria = service.getAlumnoTarjetaDao().tarjetaDatosBancarios(alumnoDatosBanco.getId());
                    }
                    if (tarjetaBancaria == null) {
                        service.getAlumnoDao().update(alumno);
                        logScreen.add("<td>" + alumno.getBoleta() + "</td>"
                                + "<td>" + alumno.getFullName() + "</td>"
                                + "<td><span class='label label-success'>Ok.</span></td>"
                                + "</td>");
                    } else if (tarjetaBancaria.getEstatusTarjBanc().getId().equals(new BigDecimal("12"))) { //En trámite
                        //No puede ingresar una cuenta externa
                        logScreen.add("<td>" + alumno.getBoleta() + "</td>"
                                + "<td>" + alumno.getFullName() + "</td>"
                                + "<td><span class='label label-warning'>La solicitud no puede ser procesada dado que el alumno tiene en trámite una tarjeta.</span></td>");
                    } else if (tarjetaBancaria.getEstatusTarjBanc().getId().equals(new BigDecimal("13"))) { //Liberada
                        //No puede ingresar una cuenta externa
                        logScreen.add("<td>" + alumno.getBoleta() + "</td>"
                                + "<td>" + alumno.getFullName() + "</td>"
                                + "<td><span class='label label-warning'>La solicitud no puede ser procesada dado que el alumno tiene Liberada una tarjeta.</span></td>");
                    } else if (tarjetaBancaria.getEstatusTarjBanc().getId().equals(new BigDecimal("14"))) { //Rechazada
                        //Puede ingresar una cuenta externa
                        service.getAlumnoDao().update(alumno);
                        logScreen.add("<td>" + alumno.getBoleta() + "</td>"
                                + "<td>" + alumno.getFullName() + "</td>"
                                + "<td><span class='label label-success'>Ok.</span></td>"
                                + "</td>");
                    } else if (tarjetaBancaria.getEstatusTarjBanc().getId().equals(new BigDecimal("15"))) { //Corrección de datos
                        //Actualizamos el estatus  de la relación alumno-tarjeta a "Rechazada"
                        EstatusTarjetaBancaria estatusTarjetaBancaria = service.getEstatusTarjetaBancariaDao().findById(new BigDecimal("14")); //Corrección de datos
                        tarjetaBancaria.setEstatusTarjBanc(estatusTarjetaBancaria);
                        //Puede ingresar una cuenta externa
                        service.getAlumnoDao().update(alumno);
                        logScreen.add("<td>" + alumno.getBoleta() + "</td>"
                                + "<td>" + alumno.getFullName() + "</td>"
                                + "<td><span class='label label-success'>Ok.</span></td>"
                                + "</td>");
                    } else if (tarjetaBancaria.getEstatusTarjBanc().getId().equals(new BigDecimal("16"))) { //Renuncia
                        //Puede ingresar una cuenta externa
                        service.getAlumnoDao().update(alumno);
                        logScreen.add("<td>" + alumno.getBoleta() + "</td>"
                                + "<td>" + alumno.getFullName() + "</td>"
                                + "<td><span class='label label-success'>Ok.</span></td>"
                                + "</td>");
                    }
                }
            } catch (PersistenceException e) {
                e.printStackTrace();
            } catch (Exception e) {
                String boleta = "";
                if (row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING) {
                    String num = row.getCell(0) + "";
                    boleta = new BigInteger(num) + "";
                } else if (row.getCell(0).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    String num = row.getCell(0).getNumericCellValue() + "";
                    boleta = new BigDecimal(num).toBigInteger() + "";
                }
                logScreen.add("<td>" + boleta + "</td>"
                        + "<td>" + "" + "</td>"
                        + "<td><span class='label label-warning'>El múmero de boleta no existe.</span></td>");
            }
        }
        return logScreen;
    }

    public DatosAcademicos agregarDatosAcademicos(Alumno alumno, AlumnoDAE alumnoDae, Periodo periodo) {
        Double promedio = alumnoDae.getPromedio().equals("") ? 0 : (double) alumnoDae.getPromedio();
        UnidadAcademica uA = service.getUnidadAcademicaDao().getByClave((String.valueOf(Integer.parseInt(alumnoDae.getEscuela()))));
        Integer regular = (alumnoDae.getTipo_alumno()).equals("R") ? 1 : 0;
        Integer inscrito = alumnoDae.getInscrito() == 1 ? 1 : 0;
        Integer modalidadDae = alumnoDae.getModo_ingreso();
        Integer infoActualizada = 0;

        if (alumnoDae.getOrigen() != null && alumnoDae.getOrigen() == 1) {
            infoActualizada = 1;
        }

        Modalidad modalidad = new Modalidad();
        modalidad.setId(getModalidadId(modalidadDae));

        try {
            validarCarrera(alumnoDae);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        DatosAcademicos datosAcademicos = new DatosAcademicos(alumno, periodo,
                promedio, alumnoDae.getSemestre_inscrito(), alumnoDae.getTot_creditos(),
                uA, alumnoDae.getCarreraObj(), regular, inscrito, modalidadDae,
                modalidad, alumnoDae.getTurno(), alumnoDae.getReprobadas(), alumnoDae.getEgresado(),
                alumnoDae.getCumple_carga_minima(), infoActualizada, new Date());

        return datosAcademicos;
    }

    public DatosAcademicos actualizarDatosAcademicos(Alumno alumno, AlumnoDAE alumnoDae, Periodo periodo) {
        DatosAcademicos datosAcademicos = service.getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), periodo.getId());
        Double promedio = alumnoDae.getPromedio().equals("") ? 0 : (double) alumnoDae.getPromedio();
        UnidadAcademica uA = service.getUnidadAcademicaDao().getByClave((String.valueOf(Integer.parseInt(alumnoDae.getEscuela()))));
        Integer regular = (alumnoDae.getTipo_alumno()).equals("R") ? 1 : 0;
        Integer inscrito = alumnoDae.getInscrito() == 1 ? 1 : 0;
        Integer modalidadDae = alumnoDae.getModo_ingreso();
        Integer infoActualizada = 0;

        if (alumnoDae.getOrigen() != null && alumnoDae.getOrigen() == 1) {
            infoActualizada = 1;
        }

        Modalidad modalidad = new Modalidad();
        modalidad.setId(getModalidadId(modalidadDae));

        try {
            validarCarrera(alumnoDae);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        datosAcademicos.setPromedio(promedio);
        datosAcademicos.setPeriodo(periodo);
        datosAcademicos.setSemestre(alumnoDae.getSemestre_inscrito());
        datosAcademicos.setTotalCreditos(alumnoDae.getTot_creditos());
        datosAcademicos.setUnidadAcademica(uA);
        datosAcademicos.setCarrera(alumnoDae.getCarreraObj());
        datosAcademicos.setRegular(regular);
        datosAcademicos.setInscrito(inscrito);
        datosAcademicos.setModalidadDAE(modalidadDae);
        datosAcademicos.setModalidad(modalidad);
        datosAcademicos.setTurno(alumnoDae.getTurno());
        datosAcademicos.setReprobadas(alumnoDae.getReprobadas());
        datosAcademicos.setEgresado(alumnoDae.getEgresado());
        datosAcademicos.setCumpleCargaMinima(alumnoDae.getCumple_carga_minima());
        datosAcademicos.setInfoActualizadaAdmin(infoActualizada);
        datosAcademicos.setFechaModificacion(new Date());

        return datosAcademicos;
    }

    public static BigDecimal getModalidadId(int modalidadDae) {
        if (modalidadDae == 2 || modalidadDae == 3) {
            return new BigDecimal(1);
        } else {
            return new BigDecimal(modalidadDae);
        }
    }

    public boolean solicitudActiva(Alumno alumno, Periodo periodo) {

        boolean periodoActivo = Boolean.FALSE;

        if (periodo == null) {
            periodo = service.getPeriodoDao().getPeriodoActivo();
        }

        String fInicialStr = (String) ActionContext.getContext().getApplication().get("FechaRegistroInicial");
        String fFinalStr = (String) ActionContext.getContext().getApplication().get("FechaRegistroFinal");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        //Inicialmente se valida si el registro esta aún activo.
        try {
            //Obtiene la fecha Final e Inicial del periodo y la fecha actual.lo compara con la fecha de hoy para establecer si el periodo esta activo o no
            Date dateInicial = formatter.parse(fInicialStr);
            Date dateFinal = formatter.parse(fFinalStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateFinal);
            cal.add(Calendar.DATE, 1);
            dateFinal = cal.getTime();
            Date hoy = new Date();
            //Compara la fecha Final e Inicial con la fecha de hoy para establecer si el periodo esta activo o no
            if (hoy.after(dateInicial) && hoy.before(dateFinal)) {
                periodoActivo = Boolean.TRUE;
            }
            if (!periodoActivo) {
                SolicitudBecaIndividual individual = service.getSolicitudBecaIndividualDao().buscaAlumno(alumno, periodo);
                if (individual != null) {
                    periodoActivo = Boolean.TRUE;
                }
            }
            DatosAcademicos datosAcademicos = service.getDatosAcademicosDao().ultimosDatos(alumno.getId());
            if (!periodoActivo) {
                SolicitudBecaUA solicitudUA = service.getSolicitudBecaUADao().buscarUA(datosAcademicos.getUnidadAcademica(), periodo);
                if (solicitudUA != null) {
                    periodoActivo = Boolean.TRUE;
                }
            }
            if (!periodoActivo) {
                SolicitudBecaNivel solicitudNivel = service.getSolicitudBecaNivelDao().buscarNivel(datosAcademicos.getUnidadAcademica().getNivel(), periodo);
                if (solicitudNivel != null) {
                    periodoActivo = Boolean.TRUE;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return periodoActivo;
    }
}
