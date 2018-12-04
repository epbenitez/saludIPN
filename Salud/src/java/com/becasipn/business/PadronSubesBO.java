package com.becasipn.business;

import com.becasipn.domain.CampoResumen;
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
import com.becasipn.service.Service;
import com.becasipn.util.ParseDoubleComa;
import com.becasipn.util.TuplaValidacion;
import com.opensymphony.xwork2.ActionContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.prefs.CsvPreference;

/**
 *
 * @author Rafael Cardenas
 */
public class PadronSubesBO extends BaseBO {

	private Integer procesados;
	private Integer correctos;
	private Integer solicitudesCreadas;
	private List<CampoResumen> listaCampos = new ArrayList<>();

	public PadronSubesBO(Service service) {
		super(service);
	}

	public String carga(File upload, ConvocatoriaSubes convocatoria, Periodo periodo, Integer tipo) {
		procesados = 0;
		correctos = 0;
		solicitudesCreadas = 0;
		listaCampos = new ArrayList<>();

		String resultado;
		boolean vacio = !upload.exists() || upload.length() == 0;
		if (vacio) {
			resultado = cargaArchivoDeTabla(convocatoria, periodo, tipo);
		} else {
			resultado = cargaArchivo(upload, convocatoria, periodo, tipo);
		}
		if (!resultado.equals("carga.archivo.success")) {
			return resultado;
		}

		if (tipo == 1) {
			procesarOtorgamientos(convocatoria, periodo);
			consolidarAlumno(convocatoria, periodo);
			actualizarPreguntas(convocatoria, periodo);
		}
		return "PROCESO";
	}

	public String cargaArchivo(File upload, ConvocatoriaSubes convocatoria, Periodo periodo, Integer tipo) {
		ICsvBeanReader beanReader = null;
		CellProcessor[] processors;
		String[] header;

		if (tipo == 1) {
			processors = new CellProcessor[]{
				new NotNull(), // folioSubes
				new NotNull(), // curp
				null, // nombre
				null, // apellidopaterno
				null, // apelllidomaterno
				null, // email
				null, // telefonofijo
				null, // celular
				null, // fecha_nacimineto
				null, // sexo
				null, // estadocivil
				null, // entidad de nacimiento
				null, // nacionalidad
				null, // estado
				null, // municipio
				null, // violencia
				null, // hambre
				null, // marginacion
				null, // localidad
				null, // colonia
				null, // cp
				null, // calle
				null, // noExt
				null, // noInt
				null, // clabe
				null, // fe estado
				null, // fe municipio
				null, // fe institucion
				null, // escuela
				null, // carrera
				null, // promedio anterior
				null, // promedio gral
				null, // total periodos
				null, // periodo actual
				null, // tipoperiodo
				new NotNull(), //boleta
				null, // regular
				null,//Vulnerabilidad
				null, // ficha activada
				new NotNull(), //Estatus
				null, // ipes
				new ParseInt(), //integrantes
				new ParseDoubleComa(), //ingreso_total
				null, // indigena
				null, // comunidad indigena
				null, // padre madre
				null, // embarazo
				null,//Discapacidad
				null,//prospera
				null, // folio prospera
				null, // gasto mensual trans
				null, // medio trans
				null, // tiempo trans
				null, // km trans
				null, // priorizacion
				null, // folio cruce
				null, // fecha prospera
				null, // promajoven
				null, // fecha promajoven
			};
			header = new String[]{
				"foliosubes",
				"curp",
				"nombre",
				"apellidopaterno",
				"apellidomaterno",
				"correoelectronico",
				"telefono_fijo",
				"celular",
				"fecha_nacimiento",
				"sexo",
				"estadocivil",
				"entidadnacimiento",
				"nacionalidad",
				"estado",
				"municipio",
				"municipio_con_violencia",
				"municipio_contra_hambre",
				"municipio_con_marginacion",
				"localidad",
				"colonia",
				"codigopostal",
				"calle",
				"numext",
				"numint",
				"clabe",
				"ficha_escolar_estado",
				"ficha_escolar_municipio",
				"ficha_escolar_institucion",
				"plantel",
				"carrera",
				"ficha_escolar_promedio_anterior",
				"promedio",
				"ficha_escolar_total_periodos",
				"semestre",
				"ficha_escolar_tipo_periodo",
				"matricula",
				"regularidad",
				"vulnerabilidad",
				"ficha_activada",
				"estatussubes",
				"validadoipes",
				"integranteshogar",
				"ingresototal",
				"es_indigena",
				"comunidad_indigena",
				"es_padre_o_madre",
				"alumna_embarazada",
				"discapacidad",
				"tieneprospera",
				"folio_prospera",
				"gasto_mensual_transporte",
				"principal_medio_transporte",
				"tiempo_transporte",
				"kilometros_transporte",
				"priorizacion_prospera",
				"folio_cruce_prospera",
				"fecha_prospera",
				"folio_promajoven",
				"fecha_promajoven"
			};
		} else {
			processors = new CellProcessor[]{
				new NotNull(), // folioTransporte
				new NotNull(), // curp
				new NotNull(), //Estatus
				null,//Vulnerabilidad
				null//ipes

			};
			header = new String[]{
				"foliotransporte",
				"curp",
				"estatustransporte",
				"vulnerabilidadtransporte",
				"validadoipestransporte"
			};
		}

		try {
			beanReader = new CsvBeanReader(new InputStreamReader(new FileInputStream(upload), "UTF-8"),
					CsvPreference.STANDARD_PREFERENCE);
			beanReader.getHeader(true);
			PadronSubes padronBean;

			while ((padronBean = beanReader.read(PadronSubes.class, header, processors)) != null) {
				procesados++;
				if (tipo == 1) {
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
								if (padronBean.getMatricula().replaceAll("\\s+", "").equals(a.getBoleta().replaceAll("\\s+", ""))
										&& padronBean.getNombre().replaceAll("\\s+", "").equals(a.getNombre().replaceAll("\\s+", ""))
										&& padronBean.getApellidopaterno().replaceAll("\\s+", "").equals(a.getApellidoPaterno().replaceAll("\\s+", ""))
										&& padronBean.getApellidomaterno().replaceAll("\\s+", "").equals(a.getApellidoMaterno().replaceAll("\\s+", ""))) {

									a.setCurp(padronBean.getCurp());
									service.getAlumnoDao().update(a);

									padronBean.setAlumno(a);
									padronBean.setTipocruce((short) 2);
								} else {
									agregar("No se puede procesar el alumno / Error CURP, BOLETA ", padronBean);
									continue;
								}
							}
						}
						if (a == null) {
							agregar("No se encontro en la tabla de alumno, se intentara insertar al alumno.", padronBean);
						} else {
							agregar("SUBES/ Correcto", padronBean);
						}
						service.getPadronSubesDao().save(padronBean);
						correctos++;
					} else {
						agregar("El alumno a sido cargado previamente", padronBean);
						continue;
					}
				} else if (tipo == 2) {
					Boolean existeCurp = service.getPadronSubesDao().existeAlumnoCurpPeriodo(padronBean.getCurp(), periodo);
					if (existeCurp) {
						PadronSubes px = service.getPadronSubesDao().alumnoCurpPeriodo(padronBean.getCurp(), periodo);
						if (px.getFoliotransporte() == null) {
							px.setEstatustransporte(padronBean.getEstatustransporte());
							px.setVulnerabilidadtransporte(padronBean.getVulnerabilidadtransporte());
							px.setValidadoipestransporte(padronBean.getValidadoipestransporte());
							service.getPadronSubesDao().update(px);
							Boolean tieneSolicitudManutencion = service.getPadronSubesDao().existeAlumnoFolioPeriodoSolicitud(padronBean.getCurp(), periodo, new BigDecimal(5));
							if (tieneSolicitudManutencion) {
								SolicitudBeca solicitud = crearSolicitud(new BigDecimal(4), new BigDecimal(7), px, periodo);
								if (solicitud != null) {
									px.setFoliotransporte(padronBean.getFoliotransporte());
									service.getPadronSubesDao().update(px);
									agregar("Transporte/ Correcto", padronBean);
									correctos++;
								} else {
									px.setErrorAsignacionTransporte("Hubo un error al crear la solicitud.");
									service.getPadronSubesDao().update(px);
									agregar("No se pudo crear la solicitud de transporte", padronBean);
								}
							} else {
								px.setErrorAsignacionTransporte("El alumno no cuenta con una solicitud de manutencion.");
								service.getPadronSubesDao().update(px);
							}
						} else {
							agregar("El alumno a sido cargado previamente", padronBean);
						}
					} else {
						agregar("No se encontro el alumno", padronBean);
					}
				}
			}
		} catch (FileNotFoundException ex) {
			return "carga.archivo.error.carga";
		} catch (IOException ex) {
			return "carga.archivo.error.formato";
		} catch (IllegalArgumentException ex) {
			System.out.println(ex);
			return "carga.archivo.error.formato";
		} finally {
			if (beanReader != null) {
				try {
					beanReader.close();
				} catch (IOException ex) {
					log.warn("Error al cerrar flujo cargaSubes ");
				}
			}
		}
		return "carga.archivo.success";
	}

	private String cargaArchivoDeTabla(ConvocatoriaSubes convocatoria, Periodo periodo, Integer tipo) {
		List<PadronSubes> listadoArchivo = service.getPadronSubesDao().cargaDeTabla(tipo);
		for (PadronSubes padronBean : listadoArchivo) {
			padronBean.setId(null);
			procesados++;
			if (tipo == 1) {
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
								agregar("No se puede procesar el alumno / Error CURP, BOLETA ", padronBean);
								continue;
							}
						}
					}
					if (a == null) {
						agregar("No se encontro en la tabla de alumno, se intentara insertar al alumno.", padronBean);
					} else {
						agregar("SUBES/ Correcto", padronBean);
					}
					service.getPadronSubesDao().save(padronBean);
					correctos++;
				} else {
					agregar("El alumno a sido cargado previamente", padronBean);
					continue;
				}
			} else if (tipo == 2) {
				Boolean existeCurp = service.getPadronSubesDao().existeAlumnoCurpPeriodo(padronBean.getCurp(), periodo);
				if (existeCurp) {
					PadronSubes px = service.getPadronSubesDao().alumnoCurpPeriodo(padronBean.getCurp(), periodo);
					if (px.getFoliotransporte() == null) {
						px.setEstatustransporte(padronBean.getEstatustransporte().trim());
						px.setVulnerabilidadtransporte(padronBean.getVulnerabilidadtransporte().trim());
						px.setValidadoipestransporte(padronBean.getValidadoipestransporte().trim());
						service.getPadronSubesDao().update(px);
						Boolean tieneSolicitudManutencion = service.getPadronSubesDao().existeAlumnoFolioPeriodoSolicitud(padronBean.getCurp(), periodo, new BigDecimal(5));
						if (tieneSolicitudManutencion) {
							SolicitudBeca solicitud = crearSolicitud(new BigDecimal(4), new BigDecimal(7), px, periodo);
							if (solicitud != null) {
								px.setFoliotransporte(padronBean.getFoliotransporte());
								service.getPadronSubesDao().update(px);
								agregar("Transporte/ Correcto", padronBean);
								correctos++;
							} else {
								px.setErrorAsignacionTransporte("Hubo un error al crear la solicitud.");
								service.getPadronSubesDao().update(px);
								agregar("No se pudo crear la solicitud de transporte", padronBean);
							}
						} else {
							px.setErrorAsignacionTransporte("El alumno no cuenta con una solicitud de manutencion.");
							service.getPadronSubesDao().update(px);
						}
					} else {
						agregar("El alumno a sido cargado previamente", padronBean);
					}
				} else {
					agregar("No se encontro el alumno", padronBean);
				}
			}
		}
		return "carga.archivo.success";
	}

	private void actualizarDatos(ConvocatoriaSubes convocatoria, Periodo periodo) {
		int i = 0;
		List<PadronSubes> listadoActualizar = service.getPadronSubesDao().listadoActualizar(convocatoria, periodo);
		AlumnoBO boAlumno = new AlumnoBO(service);
		for (PadronSubes padronSubes : listadoActualizar) {
			System.out.println("SUBES (actualizarDatos): " + (++i) + "/" + listadoActualizar.size() + "  -   " + padronSubes.getFoliosubes());
			try {
				Alumno update = boAlumno.datosDAE(padronSubes.getAlumno());
				if (update != null) {
					update.setEstatus(Boolean.TRUE);
					service.getAlumnoDao().update(update);
				} else {
					padronSubes.setDae(null);
					service.getPadronSubesDao().update(padronSubes);
				}
			} catch (ErrorDaeException ex) {
				padronSubes.setDae(null);
				service.getPadronSubesDao().update(padronSubes);
			} catch (Exception e) {
				agregar("No se pudo realizar el update del alumno", padronSubes);
			}
		}
	}

	private void guardaAlumno(ConvocatoriaSubes convocatoria, Periodo periodo) {
		int i = 0;
		AlumnoBO boAlumno = new AlumnoBO(service);
		List<PadronSubes> listadoInsertar = service.getPadronSubesDao().listadoInsertar(convocatoria, periodo);
		for (PadronSubes padronSubes : listadoInsertar) {
			System.out.println("SUBES (guardaAlumno): " + (++i) + "/" + listadoInsertar.size() + "  -   " + padronSubes.getFoliosubes());
			TuplaValidacion alumnoValidado = boAlumno.validarAlumno(padronSubes.getMatricula(), true, padronSubes.getCurp());
			padronSubes.setTipocruce((short) 1);
			if (alumnoValidado.getErrorDAE() != null) {
				padronSubes.setTipocruce((short) 2);
				alumnoValidado = boAlumno.validarAlumno(padronSubes.getMatricula(), true);
				if (alumnoValidado.getErrorDAE() != null) {
					continue;
				}
				if (alumnoValidado.getAlumnoDAE() == null
						|| !alumnoValidado.getAlumnoDAE().getNombre().equals(padronSubes.getNombre())
						|| !alumnoValidado.getAlumnoDAE().getApellido_pat().equals(padronSubes.getApellidopaterno())) {
					continue;
				}
			}
			Alumno a;
			try {
				a = boAlumno.daeSubesToAlumno(alumnoValidado.getAlumnoDAE(), padronSubes);
				boAlumno.guardaAlumno(a);

				DatosAcademicos da = boAlumno.agregarDatosAcademicos(a, alumnoValidado.getAlumnoDAE(), periodo);
				a.addDatosAcademicos(da);
				boAlumno.guardaAlumno(a);
			} catch (Exception e) {
				System.out.println(e);
				continue;
			}
			try {
				a = service.getAlumnoDao().findByBoleta(alumnoValidado.getAlumnoDAE().getBoleta());
				padronSubes.setAlumno(a);
				padronSubes.setDae((short) 2);
				service.getPadronSubesDao().update(padronSubes);
			} catch (Exception e) {
				agregar("No se pudieron crear los registros del alumno", padronSubes);
				System.out.println(e);
			}
		}

	}

	private void consolidarAlumno(ConvocatoriaSubes convocatoria, Periodo periodo) {
		service.getPadronSubesDao().marcarAlumnos(convocatoria, periodo, 1);
		service.getPadronSubesDao().marcarAlumnos(convocatoria, periodo, 2);
		service.getPadronSubesDao().marcarAlumnos(convocatoria, periodo, 3);
		service.getPadronSubesDao().marcarAlumnos(convocatoria, periodo, 4);
		service.getPadronSubesDao().marcarAlumnos(convocatoria, periodo, 5);
		service.getPadronSubesDao().marcarAlumnos(convocatoria, periodo, 6);
		actualizarDatos(convocatoria, periodo);
		guardaAlumno(convocatoria, periodo);
	}

	private void guardaRespuesta(CuestionarioRespuestasUsuario pregunta, CuestionarioRespuestas respuesta, PadronSubes padronSubes, Periodo periodo, BigDecimal preguntaId, String respuestaAbierta) {
		if (pregunta != null) {
			if (respuestaAbierta != null) {
				pregunta.setRespuestaAbierta(respuestaAbierta);
			}
			try {
				service.getCuestionarioRespuestasUsuarioDao().update(pregunta);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			pregunta = new CuestionarioRespuestasUsuario(padronSubes.getAlumno().getUsuario(),
					new CuestionarioPreguntas(preguntaId), respuesta, new Cuestionario(BigDecimal.ONE), periodo);
			padronSubes.setEse((short) 2);
			if (respuestaAbierta != null) {
				pregunta.setRespuestaAbierta(respuestaAbierta);
			}
			service.getCuestionarioRespuestasUsuarioDao().save(pregunta);
		}
		if (!service.getCuestionarioRespuestasUsuarioDao().existeRespuestaPregunta(padronSubes.getAlumno().getUsuario().getId(), preguntaId, respuesta.getId(), new BigDecimal(5), periodo.getId())) {
			CuestionarioRespuestasUsuario preguntax = new CuestionarioRespuestasUsuario(padronSubes.getAlumno().getUsuario(), new CuestionarioPreguntas(preguntaId), respuesta, new Cuestionario(new BigDecimal(5)), periodo);
			if (respuestaAbierta != null) {
				preguntax.setRespuestaAbierta(respuestaAbierta);
			}
			service.getCuestionarioRespuestasUsuarioDao().save(preguntax);
		}
	}

	private void actualizarPreguntas(ConvocatoriaSubes convocatoria, Periodo periodo) {
		int i = 0;
		List<PadronSubes> listadoESE = service.getPadronSubesDao().listadoESE(convocatoria, periodo);
		CuestionarioRespuestasUsuario pregunta;
		for (PadronSubes padronSubes : listadoESE) {
			System.out.println("SUBES (actualizarPreguntas): " + (i++) + "/" + listadoESE.size() + " --- " + padronSubes.getFoliosubes());
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
				SolicitudBeca solicitud = null;
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
		}
	}

	public SolicitudBeca actualizarSolicitud(SolicitudBeca solicitud, PadronSubes padronSubes) {
		solicitud.setCuestionario(new Cuestionario(new BigDecimal(5)));
		DatosAcademicos da = service.getDatosAcademicosDao().datosPorPeriodo(solicitud.getAlumno().getId(), solicitud.getPeriodo().getId());
		updateDatosAcademicosSubes(da, padronSubes);
		OtorgamientoBO oBo = new OtorgamientoBO(service);
                Boolean tieneUniversal = service.getOtorgamientoDao().tieneOtorgamientoUniversal(solicitud.getAlumno().getId(), solicitud.getPeriodo().getId());
                Boolean tieneComplementaria = service.getOtorgamientoDao().tieneOtorgamientoComplementaria(solicitud.getAlumno().getId(), solicitud.getPeriodo().getId());
		TipoBecaPeriodo becaPeriodo = oBo.becaSolicitud(solicitud, null, null, tieneUniversal, tieneComplementaria,null,padronSubes);
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
			solicitudesCreadas++;
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
			return null;
		}
		if (!periodo.getId().equals(service.getPeriodoDao().getPeriodoActivo().getId())) {
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
		TipoBecaPeriodo becaPeriodo = oBo.becaSolicitud(sb, null, null, tieneUniversal, tieneComplementaria,null,padronSubes);
		sb.setTipoBecaPreasignada(becaPeriodo);
		sb.setIngresosPercapitaPesos(padronSubes.getIngresototal().floatValue() / padronSubes.getIntegranteshogar().floatValue());
		sb.setFinalizado(Boolean.TRUE);
		sb.setVulnerabilidadSubes(padronSubes.getVulnerabilidad() == null ? false : padronSubes.getVulnerabilidad().equalsIgnoreCase("VULNERABLE") ? true : false);
		sb.setFechaIngreso(new Date());
		try {
			sb = service.getSolicitudBecaDao().save(sb);
			solicitudesCreadas++;
			return crearOtorgamientoSUBES(sb, padronSubes, cuestionarioId);
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	public void procesarOtorgamientos(ConvocatoriaSubes convocatoria, Periodo periodo) {
		service.getPadronSubesDao().excluirOtorgamiento(convocatoria, periodo, 1);
		if (periodo.getId().equals(service.getPeriodoDao().getPeriodoActivo().getId())) {
			TipoProceso proceso = service.getTipoProcesoDao().procesoAutoSubes();
			if (proceso != null) {
				List<PadronSubes> listadoOtorgamientos = service.getPadronSubesDao().listadoConOtorgamiento(convocatoria, periodo);
				for (PadronSubes padronSubes : listadoOtorgamientos) {
					SolicitudBeca sb = new SolicitudBeca();
					Alumno a = padronSubes.getAlumno();
					if (a == null) {
						continue;
					}
					sb.setAlumno(a);
					sb.setCuestionario(new Cuestionario(new BigDecimal(5)));
					sb.setFechaModificacion(new Date());
					sb.setPeriodo(periodo);
					//Nuevos campos correspondientes a una solicitud de beca
					sb.setPermiteTransferencia(0);
					Beca b = service.getBecaDao().findById(new BigDecimal(5));//Manutencion
					sb.setProgramaBecaSolicitada(b);
					DatosAcademicos da = service.getDatosAcademicosDao().datosPorPeriodo(sb.getAlumno().getId(), periodo.getId());
					updateDatosAcademicosSubes(da, padronSubes);
					OtorgamientoBO oBo = new OtorgamientoBO(true, service);
                                        Boolean tieneUniversal = service.getOtorgamientoDao().tieneOtorgamientoUniversal(a.getId(), periodo.getId());
                                        Boolean tieneComplementaria = service.getOtorgamientoDao().tieneOtorgamientoComplementaria(a.getId(), periodo.getId());                
					TipoBecaPeriodo becaPeriodo = oBo.becaSolicitud(sb, null, null, tieneUniversal, tieneComplementaria,null,padronSubes);
					sb.setTipoBecaPreasignada(becaPeriodo);
					sb.setIngresosPercapitaPesos(padronSubes.getIngresototal().floatValue() / padronSubes.getIntegranteshogar().floatValue());
					sb.setFinalizado(Boolean.TRUE);
					sb.setVulnerabilidadSubes(padronSubes.getVulnerabilidad() == null ? false : padronSubes.getVulnerabilidad().equalsIgnoreCase("VULNERABLE") ? true : false);

					//Insertar rechazos
					BigDecimal idProceso = proceso.getId();
					ProcesoBO bp = new ProcesoBO(service);
					Proceso pr = bp.existeONuevo(da.getUnidadAcademica().getId(), sb.getPeriodo().getId(), idProceso);
					sb.setProceso(pr);
					sb.setMotivoRechazo(service.getMotivoRechazoSolicitudDao().findById(new BigDecimal(39)));
					sb.setClasificacionSolicitud(service.getClasificacionSolicitudDao().findById(new BigDecimal(2)));

					sb.setFechaIngreso(new Date());
					try {
						sb = service.getSolicitudBecaDao().save(sb);
						solicitudesCreadas++;
					} catch (Exception e) {
						System.out.println(e);
					}
				}
			}
		}
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
										if (bo.otorgarBeca(pr, sb.getAlumno(), sb.getTipoBecaPreasignada(), sb, u, null)) {
											return sb;
										} else {
											padronSubes.setErrorAsignacionTransporte(bo.getErrorAsignacion());
											sb.setMotivoRechazo(service.getMotivoRechazoSolicitudDao().findById(new BigDecimal(35)));
										}
									} catch (Exception e) {
										e.printStackTrace();
										padronSubes.setErrorAsignacion("El método de otorgamiento fallo al asignar.");
										sb.setMotivoRechazo(new MotivoRechazoSolicitud(new BigDecimal(35)));
									}
								} else {
									padronSubes.setErrorAsignacionTransporte("No se pudo preasignar beca con los datos otorgados.");
									sb.setMotivoRechazo(new MotivoRechazoSolicitud(new BigDecimal(35)));
								}
							} else {
								sb.setMotivoRechazo(new MotivoRechazoSolicitud(new BigDecimal(41)));
							}
						} else {
							sb.setMotivoRechazo(new MotivoRechazoSolicitud(new BigDecimal(38)));
						}
					} else if (padronSubes.getValidadoipestransporte().trim().equalsIgnoreCase("Rechazado")) {
						sb.setMotivoRechazo(new MotivoRechazoSolicitud(new BigDecimal(36)));
					} else {
						sb.setMotivoRechazo(new MotivoRechazoSolicitud(new BigDecimal(37)));
					}
				} else {
					sb.setMotivoRechazo(new MotivoRechazoSolicitud(new BigDecimal(40)));
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
									if (bo.otorgarBeca(pr, sb.getAlumno(), sb.getTipoBecaPreasignada(), sb, u, null)) {
										return sb;
									} else {
										padronSubes.setErrorAsignacion(bo.getErrorAsignacion());
										sb.setMotivoRechazo(new MotivoRechazoSolicitud(new BigDecimal(35)));
									}
								} catch (Exception e) {
									e.printStackTrace();
									padronSubes.setErrorAsignacion("El método de otorgamiento fallo al asignar.");
									sb.setMotivoRechazo(new MotivoRechazoSolicitud(new BigDecimal(35)));
								}
							} else {
								padronSubes.setErrorAsignacion("No se pudo preasignar beca con los datos otorgados.");
								sb.setMotivoRechazo(new MotivoRechazoSolicitud(new BigDecimal(35)));
							}
						} else {
							sb.setMotivoRechazo(new MotivoRechazoSolicitud(new BigDecimal(38)));
						}
					} else if (padronSubes.getValidadoipes().trim().equalsIgnoreCase("Rechazado")) {
						sb.setMotivoRechazo(new MotivoRechazoSolicitud(new BigDecimal(36)));
					} else {
						sb.setMotivoRechazo(new MotivoRechazoSolicitud(new BigDecimal(37)));
					}
				} else {
					sb.setMotivoRechazo(new MotivoRechazoSolicitud(new BigDecimal(40)));
				}
			}
			sb.setProceso(pr);
			sb.setClasificacionSolicitud(new ClasificacionSolicitud(new BigDecimal(2)));
			return service.getSolicitudBecaDao().update(sb);
		}
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

	public void agregar(String estatus, PadronSubes padronBean) {
		CampoResumen cr = new CampoResumen();
		Alumno a = padronBean.getAlumno();
		cr.setBoleta(a != null ? a.getBoleta() : "-");
		cr.setCurp(padronBean.getCurp());
		cr.setNombreCompleto(a != null ? a.getApellidoPaterno() + " " + a.getApellidoMaterno() + " " + a.getNombre() : "-");
		cr.setNomProceso(estatus);
		listaCampos.add(cr);
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

}
