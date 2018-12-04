package com.becasipn.business;

import com.becasipn.exception.ErrorDaeException;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.Beca;
import com.becasipn.persistence.model.ClasificacionSolicitud;
import com.becasipn.persistence.model.ConvocatoriaSubes;
import com.becasipn.persistence.model.Cuestionario;
import com.becasipn.persistence.model.SolicitudBeca;
import com.becasipn.persistence.model.CuestionarioPreguntas;
import com.becasipn.persistence.model.CuestionarioRespuestas;
import com.becasipn.persistence.model.CuestionarioRespuestasUsuario;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.Documentos;
import com.becasipn.persistence.model.MotivoRechazoSolicitud;
import com.becasipn.persistence.model.PadronSubes;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.Proceso;
import com.becasipn.persistence.model.TipoBecaPeriodo;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.persistence.model.TipoProceso;
import com.becasipn.persistence.model.UnidadAcademica;
import com.becasipn.service.Service;
import com.becasipn.util.ExcelExport;
import com.becasipn.util.TuplaValidacion;
import com.becasipn.util.UtilFile;
import static com.opensymphony.xwork2.Action.INPUT;
import com.opensymphony.xwork2.ActionContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author Jose Luis Ramirez Mosqueda
 */
public class SubesBO extends XlsLoaderBO {

    public int cont = 0;
    public int numRow = 0;
    private String titulo;

    public SubesBO(Service service) {
        super(service);
    }

    public String carga(File upload, String uploadContentType, ConvocatoriaSubes convocatoria, Periodo periodo, Integer tipo) throws Exception {
        boolean vacio = !upload.exists() || upload.length() == 0;
        if (vacio) {
            System.out.println("Cargar Archivo desde Tabla");
            cargaArchivoDeTabla(convocatoria, periodo, tipo);
        } else {
            // Excel 2003 Format
            if ("application/vnd.ms-excel".equalsIgnoreCase(uploadContentType)) {
                try {
                    //INVOCA PROCESA ARCHIVO
                    Workbook wb = readXlsFile(new FileInputStream(upload));
                    cargaArchivo(wb, convocatoria, periodo, tipo);
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                    return INPUT;
                } catch (org.apache.poi.hssf.OldExcelFormatException le) {
                    le.printStackTrace();
                    return INPUT;
                } catch (java.lang.Exception le) {
                    le.printStackTrace();
                    return INPUT;
                }
            } // Excel 2007 Format
            else if ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equalsIgnoreCase(uploadContentType)) {
                try {
                    Workbook wb = readXlsxFile(new FileInputStream(upload));
                    cargaArchivo(wb, convocatoria, periodo, tipo);
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                    return INPUT;
                } catch (java.lang.Exception le) {
                    le.printStackTrace();
                    return INPUT;
                }
            } // Si no es el content type deseado
            else {
                return INPUT;
            }
        }
        return "PROCESO";
    }

    public void cargaArchivo(Workbook wb, ConvocatoriaSubes convocatoria, Periodo periodo, Integer tipo) {
        Sheet sheet = wb.getSheetAt(0);
        numRow = sheet.getLastRowNum() + 1;
        for (cont = 1; cont <= sheet.getLastRowNum(); cont++) {
            Row row = sheet.getRow(cont);
            PadronSubes padronBean = rowtoPadron(row);
            procesarRegistro(padronBean, convocatoria, periodo, tipo, cont, Boolean.TRUE);
        }
        return;
    }

    private void cargaArchivoDeTabla(ConvocatoriaSubes convocatoria, Periodo periodo, Integer tipo) {
        List<PadronSubes> listadoArchivo = service.getPadronSubesDao().cargaDeTabla(tipo);
        for (PadronSubes padronBean : listadoArchivo) {
            padronBean.setId(null);
            procesarRegistro(padronBean, convocatoria, periodo, tipo, 0, Boolean.FALSE);
        }
        return;
    }

    //El Boolean metodoCarga indica de que forma de va a cargar el archivo: TRUE = Archivo de Excel | FALSE = Desde tabla de la base
    private void procesarRegistro(PadronSubes padronBean, ConvocatoriaSubes convocatoria, Periodo periodo, Integer tipo, Integer count, Boolean metodoCarga) {
        count = count + 1;
        if (tipo == 1) {//SOLICITUDES DE MANUTENCION
            Boolean existeCurp = service.getPadronSubesDao().existeAlumnoCurpPeriodo(padronBean.getCurp(), periodo);
            if (!existeCurp) {
                padronBean.setCatConvocatoriaSubes(convocatoria);
                padronBean.setPeriodo(periodo);
                padronBean.setFechacarga(new Date());
                Alumno a = null;
                List<Alumno> al = service.getAlumnoDao().buscarPorCURP(padronBean.getCurp());
                if (al != null && !al.isEmpty()) {
                    Boolean coincidencia = true;
                    for (Alumno ax : al) {
                        Boolean c = service.getAlumnoDAEDao().existeBoletaDAE(ax.getBoleta());
                        if (c) {
                            a = ax;
                            coincidencia = true;
                            break;
                        } else {
                            coincidencia = coincidencia && c;
                        }
                    }
                    if (!coincidencia) {
                        a = al.get(0);
                    }
                } else {
                    a = null;
                }
                if (a != null) {
                    padronBean.setAlumno(a);
                    padronBean.setTipocruce((short) 1);
                } else {
                    a = service.getAlumnoDao().findByBoleta(padronBean.getMatricula());
                    if (a != null) {
                        if ((padronBean.getMatricula() == null ? " " : padronBean.getMatricula().replaceAll("\\s+", "")).equals((a.getBoleta() == null ? " " : a.getBoleta()).replaceAll("\\s+", ""))
                                && (padronBean.getNombre() == null ? " " : padronBean.getNombre()).replaceAll("\\s+", "").equals((a.getNombre() == null ? " " : a.getNombre()).replaceAll("\\s+", ""))
                                && (padronBean.getApellidopaterno() == null ? " " : padronBean.getApellidopaterno()).replaceAll("\\s+", "").equals((a.getApellidoPaterno() == null ? " " : a.getApellidoPaterno()).replaceAll("\\s+", ""))
                                && (padronBean.getApellidomaterno() == null ? " " : padronBean.getApellidomaterno()).replaceAll("\\s+", "").equals((a.getApellidoMaterno() == null ? " " : a.getApellidoMaterno()).replaceAll("\\s+", ""))) {

                            a.setCurp(padronBean.getCurp());
                            service.getAlumnoDao().update(a);

                            padronBean.setAlumno(a);
                            padronBean.setTipocruce((short) 2);
                        } else {
                            System.out.println("El alumno no se carga al padron por inconsistencia de datos al intentar actualizar su boleta " + count);
                            return;
                        }
                    }
                }
                if (padronBean.getPromedio().length() > 5) {
                    padronBean.setPromedio(padronBean.getPromedio().substring(0, 5));
                }
                padronBean = service.getPadronSubesDao().save(padronBean);

                if (a == null) {//Se intenta insertar al alumno 
                    padronBean = guardaAlumno(padronBean);
                } else {//Se procesa al alumno encontrado
                    if (service.getOtorgamientoDao().tieneOtorgamientoDistintoUniversal(padronBean.getAlumno().getId())) {
                        padronBean = procesarOtorgamientos(padronBean, periodo);
                    } else {
                        padronBean = actualizarDatos(padronBean);
                    }
                }
                if (padronBean.getErrorAsignacion() == null) {
                    padronBean = actualizarPreguntas(padronBean, periodo);
                } else {
                    return;
                }
            } else {
                //El alumno a sido cargado previamente
                System.out.println("El alumno ha sido cargado previamente, para intentar darle solicitud, es preciso borrar su registro de ent_padron_subes " + count + ", FolioSubes:" + padronBean.getFoliosubes());
                return;
            }
        } else if (tipo == 2) {//SOLICITUDES DE TRANSPORTE
            Boolean existeCurp = service.getPadronSubesDao().existeAlumnoCurpPeriodo(padronBean.getCurp(), periodo);
            if (existeCurp) {
                PadronSubes px = service.getPadronSubesDao().alumnoCurpPeriodo(padronBean.getCurp(), periodo);
                if (px.getFoliotransporte() == null) {
                    px.setEstatustransporte(metodoCarga ? padronBean.getEstatussubes().trim() : padronBean.getEstatustransporte().trim());
                    px.setVulnerabilidadtransporte(metodoCarga ? padronBean.getVulnerabilidad().trim() : padronBean.getVulnerabilidadtransporte().trim());
                    px.setValidadoipestransporte(metodoCarga ? padronBean.getValidadoipes().trim() : padronBean.getValidadoipestransporte().trim());
                    service.getPadronSubesDao().update(px);
                    Boolean tieneSolicitudManutencion = service.getPadronSubesDao().existeAlumnoFolioPeriodoSolicitud(padronBean.getCurp(), periodo, new BigDecimal(5));
                    if (tieneSolicitudManutencion) {
                        Boolean tieneSolicitudTransporte = service.getPadronSubesDao().existeAlumnoFolioPeriodoSolicitud(padronBean.getCurp(), periodo, new BigDecimal(4));
                        if (!tieneSolicitudTransporte) {
                            SolicitudBeca solicitud = crearSolicitud(new BigDecimal(4), new BigDecimal(7), px, periodo);
                            if (solicitud != null) {//La solicitud de transporte se proceso correctamente
                                System.out.println("Solicitud creada: " + count);
                                px.setFoliotransporte(metodoCarga ? padronBean.getFoliosubes().trim() : padronBean.getFoliotransporte().trim());
                                service.getPadronSubesDao().update(px);
                            } else {
                                System.out.println("Hubo un error al crear la solicitud: " + count);
                                px.setErrorAsignacionTransporte("Hubo un error al crear la solicitud.");
                                service.getPadronSubesDao().update(px);
                            }
                        } else {
                            System.out.println("Ya tiene Solicitud de Transporte: " + count);
                        }
                    } else {
                        System.out.println("El alumno no cuenta con una solicitud de manutencion: " + count);
                        px.setErrorAsignacionTransporte("El alumno no cuenta con una solicitud de manutencion.");
                        service.getPadronSubesDao().update(px);
                    }
                }
            } else {
                System.out.println("El alumno no está registrado en el padrón del subes en este periodo: " + count);
            }
        }
    }

    private PadronSubes actualizarDatos(PadronSubes padronSubes) {
        AlumnoBO boAlumno = new AlumnoBO(service);
        try {
            Alumno update = boAlumno.datosDAE(padronSubes.getAlumno(), padronSubes.getCurp());
            if (update != null) {
                Alumno a = service.getAlumnoDao().findById(update.getId());
                a.setEstatus(Boolean.TRUE);
                service.getAlumnoDao().update(a);
                padronSubes.setDae((short) 1);
            } else {
                padronSubes.setDae((short) 0);
                padronSubes.setErrorAsignacion("El alumno no se encuentra en la vw_dae");
            }
        } catch (ErrorDaeException ex) {
            padronSubes.setErrorAsignacion("Existio un error al intentar actualizar datos (DAE).");
            System.out.println("alalalalalal");
        } catch (Exception e) {
            System.out.println("Existio un error al intentar actualizar datos. " + padronSubes.getFoliosubes() + "/n" + e);
            padronSubes.setErrorAsignacion("Existio un error al intentar actualizar datos. ");
        }
        padronSubes = service.getPadronSubesDao().update(padronSubes);
        return padronSubes;
    }

    private PadronSubes guardaAlumno(PadronSubes padronSubes) {
        AlumnoBO boAlumno = new AlumnoBO(service);
        TuplaValidacion alumnoValidado = boAlumno.validarAlumno(padronSubes.getMatricula(), true, padronSubes.getCurp());
        padronSubes.setTipocruce((short) 1);
        if (alumnoValidado.getErrorDAE() != null) {
            padronSubes.setTipocruce((short) 2);
            alumnoValidado = boAlumno.validarAlumno(padronSubes.getMatricula(), true);
            if (alumnoValidado.getErrorDAE() != null) {
                padronSubes.setErrorAsignacion("El alumno no se encuentra en la vw_dae");
                System.out.println("El alumno no se encuentra en la vw_dae: " + padronSubes.getCurp());
                padronSubes = service.getPadronSubesDao().update(padronSubes);
                return padronSubes;
            }
            if (alumnoValidado.getAlumnoDAE() == null
                    || !alumnoValidado.getAlumnoDAE().getNombre().equals(padronSubes.getNombre())
                    || !alumnoValidado.getAlumnoDAE().getApellido_pat().equals(padronSubes.getApellidopaterno())) {
                return padronSubes;
            }
        }
        Alumno a;
        try {
            a = boAlumno.daeSubesToAlumno(alumnoValidado.getAlumnoDAE(), padronSubes);
            boAlumno.guardaAlumno(a);

            DatosAcademicos da = boAlumno.agregarDatosAcademicos(a, alumnoValidado.getAlumnoDAE(), service.getPeriodoDao().getPeriodoActivo());
            a.addDatosAcademicos(da);
            boAlumno.guardaAlumno(a);
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            a = service.getAlumnoDao().findByBoleta(alumnoValidado.getAlumnoDAE().getBoleta());
            padronSubes.setAlumno(a);
            padronSubes.setDae((short) 2);
        } catch (Exception e) {
            System.out.println(e);
        }
        if (padronSubes.getAlumno() == null) {
            padronSubes.setErrorAsignacion("No se pudieron crear los registros del alumno");
            System.out.println("No se pudieron crear los registros del alumno");
        }
        padronSubes = service.getPadronSubesDao().update(padronSubes);
        return padronSubes;
    }

    private void guardaRespuesta(CuestionarioRespuestasUsuario pregunta, CuestionarioRespuestas respuesta, PadronSubes padronSubes, Periodo periodo, BigDecimal preguntaId, String respuestaAbierta) {
//        if (pregunta != null) {
//            if (respuestaAbierta != null) {
//                pregunta.setRespuestaAbierta(respuestaAbierta);
//            }
//            try {
//                service.getCuestionarioRespuestasUsuarioDao().update(pregunta);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            pregunta = new CuestionarioRespuestasUsuario(padronSubes.getAlumno().getUsuario(),
//                    new CuestionarioPreguntas(preguntaId), respuesta, new Cuestionario(BigDecimal.ONE), periodo);
//            padronSubes.setEse((short) 2);
//            if (respuestaAbierta != null) {
//                pregunta.setRespuestaAbierta(respuestaAbierta);
//            }
//            service.getCuestionarioRespuestasUsuarioDao().save(pregunta);
//        }
        if (!service.getCuestionarioRespuestasUsuarioDao().existeRespuestaPregunta(padronSubes.getAlumno().getUsuario().getId(), preguntaId, respuesta.getId(), new BigDecimal(5), periodo.getId())) {
            CuestionarioRespuestasUsuario preguntax = new CuestionarioRespuestasUsuario(padronSubes.getAlumno().getUsuario(), new CuestionarioPreguntas(preguntaId), respuesta, new Cuestionario(new BigDecimal(5)), periodo);
            if (respuestaAbierta != null) {
                preguntax.setRespuestaAbierta(respuestaAbierta);
            }
            service.getCuestionarioRespuestasUsuarioDao().save(preguntax);
        }
    }

    private PadronSubes actualizarPreguntas(PadronSubes padronSubes, Periodo periodo) {
        CuestionarioRespuestasUsuario pregunta;
        padronSubes.setEse((short) 1);

        CuestionarioRespuestas respuesta = service.getCuestionarioRespuestasDao().findById(new BigDecimal(181));
        pregunta = service.getCuestionarioRespuestasUsuarioDao().respuestaPreguntaCuestionarioPeriodo(padronSubes.getAlumno().getUsuario().getId(), new BigDecimal(167), respuesta.getId(), BigDecimal.ONE, periodo.getId());
        guardaRespuesta(pregunta, respuesta, padronSubes, periodo, new BigDecimal(167), padronSubes.getIngresototal().toString());

        respuesta = service.getCuestionarioRespuestasDao().findById(new BigDecimal(181));
        pregunta = service.getCuestionarioRespuestasUsuarioDao().respuestaPreguntaCuestionarioPeriodo(padronSubes.getAlumno().getUsuario().getId(), new BigDecimal(168), respuesta.getId(), BigDecimal.ONE, periodo.getId());
        guardaRespuesta(pregunta, respuesta, padronSubes, periodo, new BigDecimal(168), padronSubes.getIntegranteshogar().toString());

        if (padronSubes.getTieneprospera() == null ? false : padronSubes.getTieneprospera().equals("SI")) {
            respuesta = service.getCuestionarioRespuestasDao().respuesta(new BigDecimal(35), "Si");
        } else {
            respuesta = service.getCuestionarioRespuestasDao().respuesta(new BigDecimal(35), "No");
        }
        pregunta = service.getCuestionarioRespuestasUsuarioDao().respuestaPreguntaCuestionarioPeriodo(padronSubes.getAlumno().getUsuario().getId(), new BigDecimal(35), respuesta.getId(), BigDecimal.ONE, periodo.getId());
        guardaRespuesta(pregunta, respuesta, padronSubes, periodo, new BigDecimal(35), null);

        switch (padronSubes.getDiscapacidad()) {
            case "DISCAPACIDAD COGNITIVO-INTELECTUAL":
                respuesta = service.getCuestionarioRespuestasDao().respuesta(new BigDecimal(49), "Cognitivo-Intelectual");
                break;
            case "DISCAPACIDAD MOTRIZ":
                respuesta = service.getCuestionarioRespuestasDao().respuesta(new BigDecimal(49), "Motriz");
                break;
            case "DISCAPACIDAD PSICOSOCIAL":
                respuesta = service.getCuestionarioRespuestasDao().respuesta(new BigDecimal(49), "Psíquica");
                break;
            case "DISCAPACIDAD SENSORIAL":
                respuesta = service.getCuestionarioRespuestasDao().respuesta(new BigDecimal(49), "Sensorial");
                break;
            default:
                respuesta = service.getCuestionarioRespuestasDao().respuesta(new BigDecimal(49), "Ninguna");
                break;
        }
        pregunta = service.getCuestionarioRespuestasUsuarioDao().respuestaPreguntaCuestionarioPeriodo(padronSubes.getAlumno().getUsuario().getId(), new BigDecimal(49), respuesta.getId(), BigDecimal.ONE, periodo.getId());
        guardaRespuesta(pregunta, respuesta, padronSubes, periodo, new BigDecimal(49), null);
        try {
            SolicitudBeca solicitud;
            Boolean ese = service.getSolicitudBecaDao().exiteESEPeriodoActivo(padronSubes.getAlumno().getId(), new BigDecimal("1"), periodo.getId());
            if (!ese) {
                padronSubes.setFinalizado((short) 1);
                solicitud = crearSolicitud(new BigDecimal(5), new BigDecimal(5), padronSubes, periodo);
            } else {
                padronSubes.setFinalizado((short) 0);
                SolicitudBeca solicitudEse = service.getSolicitudBecaDao().getESEAlumno(padronSubes.getAlumno().getId(), periodo.getId());
                if (solicitudEse.getProgramaBecaSolicitada().getId().equals(new BigDecimal(5))) {
                    if ((solicitudEse.getClasificacionSolicitud() == null ? 0 : solicitudEse.getClasificacionSolicitud().getId().intValue()) != 1) {
                        solicitud = actualizarSolicitud(solicitudEse, padronSubes);
                    }
                } else {
                    solicitud = crearSolicitud(new BigDecimal(5), new BigDecimal(5), padronSubes, periodo);
                }
            }
            service.getPadronSubesDao().update(padronSubes);
        } catch (Exception e) {
            System.out.println("SUBES: Error al guardar cuestionario para curp " + padronSubes.getCurp());
            e.printStackTrace();
        }
        padronSubes = service.getPadronSubesDao().update(padronSubes);
        return padronSubes;
    }

    public SolicitudBeca actualizarSolicitud(SolicitudBeca solicitud, PadronSubes padronSubes) {
        solicitud.setCuestionario(new Cuestionario(new BigDecimal(5)));
        DatosAcademicos da = service.getDatosAcademicosDao().datosPorPeriodo(solicitud.getAlumno().getId(), solicitud.getPeriodo().getId());
        updateDatosAcademicosSubes(da, padronSubes);
        OtorgamientoBO oBo = new OtorgamientoBO(service);
        Boolean tieneUniversal = service.getOtorgamientoDao().tieneOtorgamientoUniversal(solicitud.getAlumno().getId(), solicitud.getPeriodo().getId());
        Boolean tieneComplementaria = service.getOtorgamientoDao().tieneOtorgamientoComplementaria(solicitud.getAlumno().getId(), solicitud.getPeriodo().getId());
        TipoBecaPeriodo becaPeriodo = oBo.becaSolicitud(solicitud, null, null, tieneUniversal, tieneComplementaria, null, padronSubes);
        solicitud.setTipoBecaPreasignada(becaPeriodo);
        solicitud.setVulnerabilidadSubes(padronSubes.getVulnerabilidad().equalsIgnoreCase("VULNERABLE") ? Boolean.TRUE : Boolean.FALSE);
        if ((solicitud.getClasificacionSolicitud() == null ? 0 : solicitud.getClasificacionSolicitud().getId().intValue()) == 2) {
            solicitud.setProceso(null);
            solicitud.setClasificacionSolicitud(null);
            solicitud.setMotivoRechazo(null);
        }
        solicitud.setFechaModificacion(new Date());
        try {
            solicitud = service.getSolicitudBecaDao().update(solicitud);
            return crearOtorgamientoSUBES(solicitud, padronSubes, new BigDecimal(5));
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public SolicitudBeca crearSolicitud(BigDecimal cuestionarioId, BigDecimal programaBecaId, PadronSubes padronSubes, Periodo periodo) {
        SolicitudBeca sb = new SolicitudBeca();
        Alumno a = padronSubes.getAlumno();
        if (a == null) {
            System.out.println("Alumno nulo");
            return null;
        }
        if (!periodo.getId().equals(service.getPeriodoDao().getPeriodoActivo().getId())) {
            System.out.println("Periodo Malo");
            return null;
        }
        sb.setAlumno(a);
        sb.setCuestionario(new Cuestionario(cuestionarioId));
        sb.setFechaModificacion(new Date());
        sb.setPeriodo(periodo);
        //Nuevos campos correspondientes a una solicitud de beca
        sb.setPermiteTransferencia(0);
        Beca b = service.getBecaDao().findById(programaBecaId);//Manutencion
        sb.setProgramaBecaSolicitada(b);
        DatosAcademicos da = service.getDatosAcademicosDao().datosPorPeriodo(sb.getAlumno().getId(), sb.getPeriodo().getId());
        updateDatosAcademicosSubes(da, padronSubes);
        OtorgamientoBO oBo = new OtorgamientoBO(service);
        Boolean tieneUniversal = service.getOtorgamientoDao().tieneOtorgamientoUniversal(a.getId(), periodo.getId());
        Boolean tieneComplementaria = service.getOtorgamientoDao().tieneOtorgamientoComplementaria(a.getId(), periodo.getId());
        TipoBecaPeriodo becaPeriodo = oBo.becaSolicitud(sb, null, null, tieneUniversal, tieneComplementaria, null, padronSubes);
        sb.setTipoBecaPreasignada(becaPeriodo);
        sb.setIngresosPercapitaPesos(padronSubes.getIngresototal().floatValue() / padronSubes.getIntegranteshogar().floatValue());
        sb.setFinalizado(Boolean.TRUE);
        sb.setVulnerabilidadSubes(padronSubes.getVulnerabilidad() == null ? false : padronSubes.getVulnerabilidad().equalsIgnoreCase("VULNERABLE") ? true : false);
        sb.setFechaIngreso(new Date());
        try {
            sb = service.getSolicitudBecaDao().save(sb);
            System.out.println("Solicitud creada: " + sb.getAlumno().getCurp());
            return crearOtorgamientoSUBES(sb, padronSubes, cuestionarioId);
        } catch (Exception e) {
            System.out.println("Solicitud error: " + e);
            return null;
        }
    }

    public PadronSubes procesarOtorgamientos(PadronSubes padronSubes, Periodo periodo) {
        TipoProceso proceso = service.getTipoProcesoDao().procesoAutoSubes();
        if (proceso != null) {
            SolicitudBeca sb = new SolicitudBeca();
            Alumno a = padronSubes.getAlumno();
            if (a != null) {
                sb.setAlumno(a);
                sb.setCuestionario(new Cuestionario(new BigDecimal(5)));
                sb.setFechaModificacion(new Date());
                sb.setPeriodo(periodo);
                //Nuevos campos correspondientes a una solicitud de beca
                sb.setPermiteTransferencia(0);
                Beca b = new Beca(new BigDecimal(5));//Manutencion
                sb.setProgramaBecaSolicitada(b);
                DatosAcademicos da = service.getDatosAcademicosDao().datosPorPeriodo(sb.getAlumno().getId(), periodo.getId());
                updateDatosAcademicosSubes(da, padronSubes);
                OtorgamientoBO oBo = new OtorgamientoBO(true, service);
                Boolean tieneUniversal = service.getOtorgamientoDao().tieneOtorgamientoUniversal(a.getId(), periodo.getId());
                Boolean tieneComplementaria = service.getOtorgamientoDao().tieneOtorgamientoComplementaria(a.getId(), periodo.getId());
                TipoBecaPeriodo becaPeriodo = oBo.becaSolicitud(sb, null, null, tieneUniversal, tieneComplementaria, null, padronSubes);
                sb.setTipoBecaPreasignada(becaPeriodo);
                sb.setIngresosPercapitaPesos(padronSubes.getIngresototal().floatValue() / padronSubes.getIntegranteshogar().floatValue());
                sb.setIngresosPercapitaPesos(padronSubes.getIngresototal().floatValue() / padronSubes.getIntegranteshogar().floatValue());
                sb.setFinalizado(Boolean.TRUE);
                sb.setVulnerabilidadSubes(padronSubes.getVulnerabilidad() == null ? false : padronSubes.getVulnerabilidad().equalsIgnoreCase("VULNERABLE") ? true : false);

                //Insertar rechazos       
                BigDecimal idProceso = proceso.getId();
                ProcesoBO bp = new ProcesoBO(service);
                Proceso pr = bp.existeONuevo(da.getUnidadAcademica().getId(), sb.getPeriodo().getId(), idProceso);
                sb.setProceso(pr);
                sb.setMotivoRechazo(new MotivoRechazoSolicitud(new BigDecimal(39)));
                sb.setClasificacionSolicitud(new ClasificacionSolicitud(new BigDecimal(2)));
                padronSubes.setErrorAsignacion("Verificar error en tabla CAT_MOTIVO_RECHAZO_SOLICITUD.");

                sb.setFechaIngreso(new Date());
                try {
                    padronSubes.setConotorgamiento((short) 1);
                    sb = service.getSolicitudBecaDao().save(sb);
                } catch (Exception e) {
                    System.out.println(e);
                }
                if (sb.getId() == null) {
                    padronSubes.setErrorAsignacion("No se pudo crear la solicitud de rechazo por otorgamiento.");
                }
            } else {
                padronSubes.setErrorAsignacion("No pueden existir padrones con otorgamiento sin un alumno identificado.");
            }
        } else {
            padronSubes.setErrorAsignacion("No existe el proceso para crear las Solicitudes.");
        }
        padronSubes = service.getPadronSubesDao().update(padronSubes);
        return padronSubes;
    }

    public SolicitudBeca crearOtorgamientoSUBES(SolicitudBeca sb, PadronSubes padronSubes, BigDecimal cuestionarioId) {
        OtorgamientoBO bo = new OtorgamientoBO(true, service);
        TipoProceso proceso = service.getTipoProcesoDao().procesoAutoSubes();
        Usuario u = (Usuario) ActionContext.getContext().getSession().get("usuario");
        if (proceso != null) {
            BigDecimal idProceso = proceso.getId();
            ProcesoBO bp = new ProcesoBO(service);
            DatosAcademicos da = service.getDatosAcademicosDao().datosPorPeriodo(sb.getAlumno().getId(), sb.getPeriodo().getId());
            updateDatosAcademicosSubes(da, padronSubes);
            Proceso pr = bp.existeONuevo(da.getUnidadAcademica().getId(), sb.getPeriodo().getId(), idProceso);
            ProcesoBO pbo = new ProcesoBO(service);
            pbo.guardaProceso(pr, "5, 7");

            if (cuestionarioId.intValue() == 4) {//Checar beca transporte
                if (padronSubes.getEstatustransporte().trim().equalsIgnoreCase("Finalizado")) {
                    if (padronSubes.getValidadoipestransporte().trim().equalsIgnoreCase("Aceptado")) {
                        if (padronSubes.getVulnerabilidadtransporte() == null ? false : padronSubes.getVulnerabilidadtransporte().trim().equalsIgnoreCase("VULNERABLE")) {
                            Boolean tieneOtorgamientoManutencion = service.getOtorgamientoDao().tieneOtorgamientoManutencionPeriodoActual(padronSubes.getAlumno().getId());
                            if (tieneOtorgamientoManutencion) {
                                Documentos documentos = service.getDocumentosDao().documentosAlumnoPeriodoActual(sb.getAlumno());
                                if (documentos == null) {
                                    documentos = new Documentos(service.getPeriodoDao().getPeriodoActivo(), sb.getAlumno());
                                }
                                documentos.setAcuseSubes(true);
                                documentos.setAcuseSubesTransporte(true);
                                documentos.setCartaCompromiso(true);
                                documentos.setComprobanteIngresosEgresos(true);
                                documentos.setCurp(true);
                                documentos.setEstudioSocioeconomico(true);
                                if (documentos.getId() != null) {
                                    service.getDocumentosDao().update(documentos);
                                } else {
                                    service.getDocumentosDao().save(documentos);
                                }
                                if (sb.getTipoBecaPreasignada() != null) {
                                    try {
                                        if (bo.otorgarBecaSoloSUBES(pr, sb.getAlumno(), sb.getTipoBecaPreasignada(), sb, u, null)) {
                                            System.out.println("Beca Transporte Otorgada al alumno " + sb.getAlumno().getCurp());
                                            return sb;
                                        } else {
                                            padronSubes.setErrorAsignacionTransporte(bo.getErrorAsignacion());
                                            sb.setMotivoRechazo(new MotivoRechazoSolicitud(new BigDecimal(35)));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        System.out.println("El método de otorgamiento fallo al asignar.");
                                        padronSubes.setErrorAsignacionTransporte("El método de otorgamiento fallo al asignar.");
                                        sb.setMotivoRechazo(new MotivoRechazoSolicitud(new BigDecimal(35)));
                                    }
                                } else {
                                    System.out.println("No se pudo preasignar beca con los datos otorgados.");
                                    padronSubes.setErrorAsignacionTransporte("El tipo de beca preasignada es nulo");
                                    sb.setMotivoRechazo(new MotivoRechazoSolicitud(new BigDecimal(35)));
                                }
                            } else {
                                System.out.println("El alumno no cuenta con una beca de manutención");
                                sb.setMotivoRechazo(new MotivoRechazoSolicitud(new BigDecimal(41)));
                                padronSubes.setErrorAsignacionTransporte("El alumno no cuenta con una beca de manutención");
                            }
                        } else {
                            System.out.println("El alumno está marcado como  No Vulnerable");
                            sb.setMotivoRechazo(new MotivoRechazoSolicitud(new BigDecimal(38)));
                            padronSubes.setErrorAsignacionTransporte("El alumno está marcado como  No Vulnerable");
                        }
                    } else if (padronSubes.getValidadoipestransporte().trim().equalsIgnoreCase("Rechazado")) {
                        System.out.println("El alumno no cumplió todos los requisitos en SUBES");
                        sb.setMotivoRechazo(new MotivoRechazoSolicitud(new BigDecimal(48)));
                        padronSubes.setErrorAsignacionTransporte("Validado IPES invalido");
                    } else {
                        sb.setMotivoRechazo(new MotivoRechazoSolicitud(new BigDecimal(37)));
                        System.out.println("El alumno está marcado como Sin Calificación");
                        padronSubes.setErrorAsignacionTransporte("El alumno está marcado como Sin Calificación");
                    }
                } else {
                    sb.setMotivoRechazo(new MotivoRechazoSolicitud(new BigDecimal(40)));
                    System.out.println("El alumno no concluyó su registro en SUBES");
                    padronSubes.setErrorAsignacionTransporte("El alumno no finalizó su registro en SUBES");
                }
            } else {
                if (padronSubes.getEstatussubes().trim().equalsIgnoreCase("Finalizado")) {
                    if (padronSubes.getValidadoipes().trim().equalsIgnoreCase("Aceptado")) {
                        if (padronSubes.getVulnerabilidad() == null ? false : padronSubes.getVulnerabilidad().trim().equalsIgnoreCase("VULNERABLE")) {
                            Documentos documentos = service.getDocumentosDao().documentosAlumnoPeriodoActual(sb.getAlumno());
                            if (documentos == null) {
                                documentos = new Documentos(service.getPeriodoDao().getPeriodoActivo(), sb.getAlumno());
                            }
                            documentos.setAcuseSubes(true);
                            documentos.setCartaCompromiso(true);
                            documentos.setComprobanteIngresosEgresos(true);
                            documentos.setCurp(true);
                            documentos.setEstudioSocioeconomico(true);
                            if (documentos.getId() != null) {
                                service.getDocumentosDao().update(documentos);
                            } else {
                                service.getDocumentosDao().save(documentos);
                            }
                            if (sb.getTipoBecaPreasignada() != null) {
                                try {
                                    if (bo.otorgarBecaSoloSUBES(pr, sb.getAlumno(), sb.getTipoBecaPreasignada(), sb, u, null)) {
                                        System.out.println("Beca Manutención Otorgada al alumno " + sb.getAlumno().getCurp());
                                        return sb;
                                    } else {
                                        padronSubes.setErrorAsignacion(bo.getErrorAsignacion());
                                        sb.setMotivoRechazo(new MotivoRechazoSolicitud(new BigDecimal(35)));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    System.out.println("El método de otorgamiento fallo al asignar.");
                                    padronSubes.setErrorAsignacion("El método de otorgamiento fallo al asignar.");
                                    sb.setMotivoRechazo(new MotivoRechazoSolicitud(new BigDecimal(35)));
                                }
                            } else {
                                System.out.println("No se pudo preasignar beca con los datos otorgados.");
                                padronSubes.setErrorAsignacion("No se pudo preasignar beca con los datos otorgados.");
                                sb.setMotivoRechazo(new MotivoRechazoSolicitud(new BigDecimal(35)));
                            }
                        } else {
                            sb.setMotivoRechazo(new MotivoRechazoSolicitud(new BigDecimal(38)));
                            System.out.println("El alumno está marcado como  No Vulnerable");
                            padronSubes.setErrorAsignacion("Verificar error en tabla CAT_MOTIVO_RECHAZO_SOLICITUD.");
                        }
                    } else if (padronSubes.getValidadoipes().trim().equalsIgnoreCase("Rechazado")) {
                        System.out.println("El alumno no cumplió todos los requisitos en SUBES");
                        sb.setMotivoRechazo(new MotivoRechazoSolicitud(new BigDecimal(36)));
                        padronSubes.setErrorAsignacion("Verificar error en tabla CAT_MOTIVO_RECHAZO_SOLICITUD.");
                    } else {
                        sb.setMotivoRechazo(new MotivoRechazoSolicitud(new BigDecimal(37)));
                        System.out.println("El alumno está marcado como Sin Calificación");
                        padronSubes.setErrorAsignacion("Verificar error en tabla CAT_MOTIVO_RECHAZO_SOLICITUD.");
                    }
                } else {
                    sb.setMotivoRechazo(new MotivoRechazoSolicitud(new BigDecimal(40)));
                    System.out.println("El alumno no concluyó su registro en SUBES");
                    padronSubes.setErrorAsignacion("Verificar error en tabla CAT_MOTIVO_RECHAZO_SOLICITUD.");
                }
            }
            sb.setProceso(pr);
            sb.setClasificacionSolicitud(new ClasificacionSolicitud(new BigDecimal(2)));
            return service.getSolicitudBecaDao().update(sb);
        } else {
            padronSubes.setErrorAsignacion("No existe el proceso para crear las Solicitudes.");
        }
        service.getPadronSubesDao().update(padronSubes);
        return sb;
    }

    public void updateDatosAcademicosSubes(DatosAcademicos da, PadronSubes padronSubes) {
        Double d;
        try {
            d = new Double(padronSubes.getPromedio());
            da.setPromedio(d != null ? d : da.getPromedio());
        } catch (Exception e) {
            d = null;
        }
        Integer i;
        try {
            i = new Integer(padronSubes.getSemestre());
            da.setSemestre(i != null ? i : da.getSemestre());
        } catch (Exception e) {
            i = null;
        }
        da.setRegular(padronSubes.getRegularidad() == null ? 0 : padronSubes.getRegularidad().equalsIgnoreCase("SI") ? 1 : 0);
        try {
            service.getDatosAcademicosDao().update(da);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public PadronSubes rowtoPadron(Row r) {
        PadronSubes ps = new PadronSubes();
        ps.setFoliosubes(cellString(r.getCell(0)));
        ps.setCurp(cellString(r.getCell(1)));
        ps.setNombre(cellString(r.getCell(2)));
        ps.setApellidopaterno(cellString(r.getCell(3)));
        ps.setApellidomaterno(cellString(r.getCell(4)));
        ps.setCorreoelectronico(cellString(r.getCell(5)));
        ps.setCelular(cellString(r.getCell(7)));
        ps.setEstadocivil(cellString(r.getCell(10)));
        ps.setEntidadnacimiento(cellString(r.getCell(11)) == null ? cellString(r.getCell(11)) : cellString(r.getCell(11)).length() > 20 ? cellString(r.getCell(11)).substring(0, 20) : cellString(r.getCell(11)));
        ps.setEstado(cellString(r.getCell(13)) == null ? cellString(r.getCell(13)) : cellString(r.getCell(13)).length() > 255 ? cellString(r.getCell(13)).substring(0, 255) : cellString(r.getCell(13)));
        ps.setMunicipio(cellString(r.getCell(14)) == null ? cellString(r.getCell(14)) : cellString(r.getCell(14)).length() > 255 ? cellString(r.getCell(14)).substring(0, 255) : cellString(r.getCell(14)));
        ps.setLocalidad(cellString(r.getCell(18)) == null ? cellString(r.getCell(18)) : cellString(r.getCell(18)).length() > 255 ? cellString(r.getCell(18)).substring(0, 255) : cellString(r.getCell(18)));
        ps.setColonia(cellString(r.getCell(19)) == null ? cellString(r.getCell(19)) : cellString(r.getCell(19)).length() > 255 ? cellString(r.getCell(19)).substring(0, 255) : cellString(r.getCell(19)));
        ps.setCodigopostal(cellString(r.getCell(20)) == null ? cellString(r.getCell(20)) : cellString(r.getCell(20)).length() > 255 ? cellString(r.getCell(20)).substring(0, 255) : cellString(r.getCell(20)));
        ps.setCalle(cellString(r.getCell(21)) == null ? cellString(r.getCell(21)) : cellString(r.getCell(21)).length() > 255 ? cellString(r.getCell(21)).substring(0, 255) : cellString(r.getCell(21)));
        ps.setNumext(cellString(r.getCell(22)) == null ? cellString(r.getCell(22)) : cellString(r.getCell(22)).length() > 20 ? cellString(r.getCell(22)).substring(0, 20) : cellString(r.getCell(22)));
        ps.setNumint(cellString(r.getCell(23)) == null ? cellString(r.getCell(23)) : cellString(r.getCell(23)).length() > 20 ? cellString(r.getCell(23)).substring(0, 20) : cellString(r.getCell(23)));
        ps.setPlantel(cellString(r.getCell(28)));
        ps.setCarrera(cellString(r.getCell(29)));
        //ps.setPromedio(cellString(r.getCell(31)).substring(0, 5));
        ps.setPromedio(cellString(r.getCell(31)));
        ps.setSemestre(cellString(r.getCell(33)));
        ps.setMatricula(cellString(r.getCell(35)));
        ps.setRegularidad(cellString(r.getCell(36)));
        ps.setVulnerabilidad(cellString(r.getCell(37)));
        ps.setEstatussubes(cellString(r.getCell(39)));
        ps.setValidadoipes(cellString(r.getCell(40)));
        ps.setIntegranteshogar(new Integer(cellString(r.getCell(41))));
        ps.setIngresototal(new Double(cellString(r.getCell(42))));
        ps.setDiscapacidad(cellString(r.getCell(47)));
        ps.setTieneprospera(cellString(r.getCell(48)));
        return ps;
    }

    public String cellString(Cell c) {
        try {
            c.setCellType(Cell.CELL_TYPE_STRING);
            return c.toString().trim();
        } catch (Exception e) {
            return "";
        }
    }

    public InputStream getInfoExcel(BigDecimal periodoId, BigDecimal convocatoriaId) {
        if (periodoId != null) {
            List<Object[]> infoBD = service.getPadronSubesDao().resumenProceso(periodoId, convocatoriaId);
            setTitulo(infoBD.size(), periodoId);

            ExcelExport excelExport = new ExcelExport();

            String[] encabezado = {"APELLIDO PATERNO", "APELLIDO MATERNO", "NOMBRE", "BOLETA",
                "CURP", "PLANTEL", "CARRERA", "FOLIO SUBES", "ESTATUS SUBES",
                "VULNERABILIDAD SUBES", "VALIDADO IPES SUBES", "BECA ASIGNADA SUBES",
                "MOTIVO RECHAZO SUBES", "FOLIO TRANSPORTE", "ESTATUS TRANSPORTE",
                "VULNERABILIDAD TRANSPORTE", "VALIDADO IPES TRANSPORTE", "BECA ASIGNADA TRANSPORTE",
                "MOTIVO RECHAZO TRANSPORTE"};

            return excelExport.construyeExcel(encabezado, infoBD);
        } else {
            return null;
        }

    }

    public void setTitulo(int total, BigDecimal periodoId) {
        Date hoy = new Date();
        StringBuilder sb = new StringBuilder();
        sb.append("\"Resumen Padron subes (");
        sb.append(total);
        sb.append(") ");
        sb.append(".xlsx\"");

        this.titulo = sb.toString();
    }

    public String getTitulo() {
        return titulo;
    }

    @Override
    public <T> List<T> processFile(Workbook wb, String lote, Date fecha) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T> List<T> processFile(Workbook wb) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T> List<T> processFile(Workbook wb, BigDecimal periodo) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T> List<T> processFile(Workbook wb, BigDecimal periodo, Integer accion) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T> List<T> processFile(Workbook wb, UnidadAcademica unidadAcademica) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private String getUploadContentType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
