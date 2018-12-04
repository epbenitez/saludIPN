package com.becasipn.business;

import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.TipoProceso;
import com.becasipn.persistence.model.Configuracion;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.Movimiento;
import com.becasipn.persistence.model.Proceso;
import com.becasipn.persistence.model.Otorgamiento;
import com.becasipn.persistence.model.OtorgamientoBajasDetalle;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.service.Service;
import com.opensymphony.xwork2.ActionContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.mail.MessagingException;

public class OtorgamientoBajasDetalleBO extends BaseBO {

    private List<String> alumnosConError;
    private List<String> alumnosOtorgados;

    public OtorgamientoBajasDetalleBO(Service service) {
        super(service);
    }

    public OtorgamientoBajasDetalle getOtorgamientoBajasDetalle(BigDecimal id) {
        OtorgamientoBajasDetalle otorgamientoBajasDetalleDao = service.getOtorgamientoBajasDetalleDao().findById(id);
        return otorgamientoBajasDetalleDao;
    }

    public Boolean guardaOtorgamientoBajasDetalle(OtorgamientoBajasDetalle otorgamientoBajasDetalle) {
        try {
            if (otorgamientoBajasDetalle.getId() == null) {
                service.getOtorgamientoBajasDetalleDao().save(otorgamientoBajasDetalle);
            } else {
                service.getOtorgamientoBajasDetalleDao().update(otorgamientoBajasDetalle);
            }
        } catch (Exception e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public void guardaBajasDetalle(String bajas, BigDecimal idTipoBaja, String observaciones, BigDecimal proceso, BigDecimal movimiento, Boolean alta) {
        try {
            OtorgamientoBO bo = new OtorgamientoBO(service);
            alumnosConError = new ArrayList();
            alumnosOtorgados = new ArrayList();
            Periodo p = service.getPeriodoDao().getPeriodoActivo();
            Movimiento m = service.getMovimientoDao().findById(movimiento);
            Proceso pr = service.getProcesoDao().findById(proceso);
            String ids[] = bajas.split(",");
            for (String id : ids) {
                Otorgamiento otorgamiento = service.getOtorgamientoDao().findById(new BigDecimal(id));
                DatosAcademicos da = service.getDatosAcademicosDao().ultimosDatos(otorgamiento.getAlumno().getId());
                if (Objects.equals(otorgamiento.getAlta(), alta)) {
                    OtorgamientoBajasDetalle otorgamientoBajasDetalle = new OtorgamientoBajasDetalle();
                    Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
                    otorgamientoBajasDetalle.setUsuariomodifico(usuario);
                    otorgamientoBajasDetalle.setFechaBaja(new Date());
                    otorgamientoBajasDetalle.setPeriodo(p);
                    otorgamientoBajasDetalle.setMovimiento(m);
                    otorgamientoBajasDetalle.setProceso(pr);
                    otorgamientoBajasDetalle.setOtorgamiento(otorgamiento);
                    otorgamientoBajasDetalle.setPromedio(da.getPromedio());
                    otorgamientoBajasDetalle.setSemestre(da.getSemestre());
                    if (alta == false) {
                        if (!bo.validarPresupuesto(otorgamiento.getTipoBecaPeriodo(), otorgamiento.getAlumno())) {
                            mensajes(false, otorgamiento.getAlumno(), "No se pudo realizar la reversión, debido a falta de presupuesto.");
                        } else {
                            otorgamientoBajasDetalle.setMovimiento(null);
                            otorgamientoBajasDetalle.setTipoBajasDetalle(null);
                            otorgamientoBajasDetalle.setObservaciones("reactivación");
                            if (!bo.guardaBaja(otorgamiento, Boolean.TRUE)) {
                                mensajes(false, otorgamiento.getAlumno(), "No se pudo realizar la reversión.");
                            } else {
                                mensajes(true, otorgamiento.getAlumno(), "La reversión fue realizada correctamente.");
                                guardaOtorgamientoBajasDetalle(otorgamientoBajasDetalle);
                            }
                        }
                    } else {
                        if (movimiento.compareTo(new BigDecimal(6)) == 0) {
                            otorgamientoBajasDetalle.setTipoBajasDetalle(service.getTipoBajasDetalleDao().findById(idTipoBaja));
                            otorgamientoBajasDetalle.setObservaciones(observaciones);
                        }
                        if (!bo.guardaBaja(otorgamiento, Boolean.FALSE)) {
                            mensajes(false, otorgamiento.getAlumno(), "No se pudo realizar la baja.");
                        } else {
                            mensajes(true, otorgamiento.getAlumno(), "La baja fue realizada correctamente.");
                            guardaOtorgamientoBajasDetalle(otorgamientoBajasDetalle);
                            Otorgamiento ox = service.getOtorgamientoDao().otorgamientoTransporteAlumnoManutencion(otorgamiento.getPeriodo().getId(), otorgamiento.getId());
                            if (ox != null) {
                                if (!bo.guardaBaja(ox, Boolean.FALSE)) {
                                    mensajes(false, otorgamiento.getAlumno(), "No se pudo realizar la baja de transporte asociada.");
                                } else {
                                    mensajes(true, otorgamiento.getAlumno(), "La baja fue realizada correctamente.");
                                    otorgamientoBajasDetalle.setOtorgamiento(ox);
                                    otorgamientoBajasDetalle.setId(null);
                                    guardaOtorgamientoBajasDetalle(otorgamientoBajasDetalle);
                                }
                            }
                        }
                    }
                } else {
                    mensajes(true, otorgamiento.getAlumno(), "El otorgamiento ya tenia el movimiento aplicado.");
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
            String[] to = {"charlyfroy@gmail.com", "epbenitez@ipn.mx"};
            try { //Correo con la intención de saber si se puede generar una baja sin su detalle
                sendEmail(to, "SIBec: Error en Baja", e.getMessage());
            } catch (MessagingException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void guardaBajasDetalleAuto(String tipoBaja, Boolean alta) {
        try {
            OtorgamientoBO bo = new OtorgamientoBO(service);
            alumnosConError = new ArrayList();
            alumnosOtorgados = new ArrayList();
            BigDecimal movimiento = new BigDecimal(tipoBaja);
            Configuracion c = service.getConfiguracionDao().secuenciaProcesosAutomaticos();
            if (c != null) {
                List<Object[]> lista;
                TipoProceso proceso = service.getTipoProcesoDao().procesoAutoByMovimiento(movimiento);
                if (proceso != null) {
                    BigDecimal idProceso = proceso.getId();
                    if (movimiento.intValue() == 5) {
                        lista = service.getOtorgamientoDao().otorgamientosPasantiaAuto();
                    } else if (movimiento.intValue() == 4) {
                        lista = service.getOtorgamientoDao().otorgamientosIncumplimientoAuto();
                    } else {
                        lista = null;
                    }
                    if (lista != null) {
//			Object[] o = lista.get(0);
                        for (Object[] o : lista) {
                            Otorgamiento otorgamiento = service.getOtorgamientoDao().findById((BigDecimal) o[0]);
                            if (Objects.equals(otorgamiento.getAlta(), alta)) {
                                ProcesoBO bp = new ProcesoBO(service);
                                Proceso pr = bp.existeONuevo(otorgamiento.getProceso().getUnidadAcademica().getId(), service.getPeriodoDao().getPeriodoActivo().getId(), idProceso);

                                OtorgamientoBajasDetalle otorgamientoBajasDetalle = new OtorgamientoBajasDetalle();
                                Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
                                otorgamientoBajasDetalle.setUsuariomodifico(usuario);
                                otorgamientoBajasDetalle.setFechaBaja(new Date());
                                otorgamientoBajasDetalle.setPeriodo(service.getPeriodoDao().getPeriodoActivo());
                                otorgamientoBajasDetalle.setMovimiento(service.getMovimientoDao().findById(movimiento));
                                otorgamientoBajasDetalle.setProceso(pr);
                                otorgamientoBajasDetalle.setOtorgamiento(otorgamiento);
                                otorgamientoBajasDetalle.setPromedio(otorgamiento.getDatosAcademicos().getPromedio());
                                otorgamientoBajasDetalle.setSemestre(otorgamiento.getDatosAcademicos().getSemestre());
                                otorgamientoBajasDetalle.setObservaciones("Baja generada automaticamente");

                                if (!bo.guardaBaja(otorgamiento, Boolean.FALSE)) {
                                    mensajes(false, otorgamiento.getAlumno(), "No se pudo realizar la baja.");
                                } else {
                                    mensajes(true, otorgamiento.getAlumno(), "La baja fue realizada correctamente.");
                                    guardaOtorgamientoBajasDetalle(otorgamientoBajasDetalle);
                                    Otorgamiento ox = service.getOtorgamientoDao().otorgamientoTransporteAlumnoManutencion(otorgamiento.getPeriodo().getId(), otorgamiento.getId());
                                    if (ox != null) {
                                        if (!bo.guardaBaja(ox, Boolean.FALSE)) {
                                            mensajes(false, otorgamiento.getAlumno(), "No se pudo realizar la baja de transporte asociada.");
                                        } else {
                                            mensajes(true, otorgamiento.getAlumno(), "La baja fue realizada correctamente.");
                                            otorgamientoBajasDetalle.setOtorgamiento(ox);
                                            otorgamientoBajasDetalle.setId(null);
                                            otorgamientoBajasDetalle.setObservaciones("Baja de transporte manutencion generada automaticamente");
                                            guardaOtorgamientoBajasDetalle(otorgamientoBajasDetalle);
                                        }
                                    }
                                }

                            } else {
//				mensajes(true, otorgamiento.getAlumno(), "El otorgamiento ya tenia el movimiento aplicado.");
                            }
                        }
                        ConfiguracionAppBO configuracionBO = new ConfiguracionAppBO(service);
                        if (movimiento.intValue() == 4) {
                            configuracionBO.guardaConfiguracion(c.getId(), c.getTip(), "1");
                        } else if (movimiento.intValue() == 5) {
                            configuracionBO.guardaConfiguracion(c.getId(), c.getTip(), "2");
                        }

                    } else {
                        mensajes(false, null, "No hay alumnos para dar de baja.");
                    }
                } else {
                    mensajes(false, null, "No existe un proceso automático para este movimiento en el catalogo de procesos.");
                }

            } else {
                mensajes(false, null, "Esta operacion no esta permitida. La secuencia de Procesos Automaticos no existe.");
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void mensajes(boolean exito, Alumno alumno, String observaciones) {
        StringBuilder sb = new StringBuilder();
        sb.append("[\"")
                .append(alumno != null ? alumno.getBoleta() : "-").append("\",\"")
                .append(alumno != null ? alumno.getFullName() : "-").append("\",\"")
                .append(observaciones).append("\"]");
        if (exito) {
            alumnosOtorgados.add(sb.toString());
        } else {
            alumnosConError.add(sb.toString());
        }
    }

    public List<String> getAlumnosConError() {
        return alumnosConError;
    }

    public void setAlumnosConError(List<String> alumnosConError) {
        this.alumnosConError = alumnosConError;
    }

    public List<String> getAlumnosOtorgados() {
        return alumnosOtorgados;
    }

    public void setAlumnosOtorgados(List<String> alumnosOtorgados) {
        this.alumnosOtorgados = alumnosOtorgados;
    }

}
