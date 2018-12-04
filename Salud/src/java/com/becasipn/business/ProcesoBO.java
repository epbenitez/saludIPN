package com.becasipn.business;

import com.becasipn.persistence.model.BitacoraEstatusProceso;
import com.becasipn.persistence.model.Proceso;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.ProcesoEstatus;
import com.becasipn.persistence.model.ProcesoProgramaBeca;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.service.Service;
import com.opensymphony.xwork2.ActionContext;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Victor Lozano
 */
public class ProcesoBO extends BaseBO {

    public ProcesoBO(Service service) {
        super(service);
    }

    /**
     * Almacena la información de la entidad en la base de datos y crea un
     * registro en la bitacora
     *
     * @author Victor Lozano
     * @param proceso
     * @return true si la operación se realizó exitosamente
     */
    public Boolean guardaProceso(Proceso proceso) {
        try {
            if (proceso.getId() == null) {
                Proceso procesoAuxiliar = service.getProcesoDao().save(proceso);

                Usuario usuarioModifico = (Usuario) ActionContext.getContext().getSession().get("usuario");
                ProcesoEstatus procesoEstatus = service.getProcesoEstatusDao().findById(new BigDecimal(1));
                BitacoraEstatusProceso bitacoraEstatusProceso = new BitacoraEstatusProceso(new Date(), procesoAuxiliar, procesoEstatus, usuarioModifico);
                service.getBitacoraEstatusProcesoDao().save(bitacoraEstatusProceso);
            } else {
                service.getProcesoDao().update(proceso);
            }
        } catch (Exception e) {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    public Boolean guardaProcesoBeca(String becasL, BigDecimal procesoId) {
        try {
            if (becasL != null) {
                List<String> becas = Arrays.asList(becasL.split(","));
                for (String beca : becas) {
                    if (!service.getProcesoProgramaBecaDao().existe(beca.trim(), procesoId)) {
                        ProcesoProgramaBeca ptbp = new ProcesoProgramaBeca();
                        ptbp.setProgramaBeca(service.getBecaDao().findById(new BigDecimal(beca.trim())));
                        ptbp.setProceso(service.getProcesoDao().findById(procesoId));

                        service.getProcesoProgramaBecaDao().save(ptbp);
                    }
                }
            }
            List<ProcesoProgramaBeca> becasProceso;
            if (becasL != null) {
                becasProceso = service.getProcesoProgramaBecaDao().noUsadas(becasL, procesoId);
            } else {
                becasProceso = service.getProcesoProgramaBecaDao().getByProceso(procesoId);
            }
            if (becasProceso != null) {
                for (ProcesoProgramaBeca becaProceso : becasProceso) {
                    service.getProcesoProgramaBecaDao().delete(becaProceso);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public Boolean guardaProceso(Proceso proceso, String becasL) {
        Proceso ax = new Proceso();
        try {
            if (proceso.getId() == null) {
                ax = service.getProcesoDao().save(proceso);

                Usuario usuarioModifico = (Usuario) ActionContext.getContext().getSession().get("usuario");
                ProcesoEstatus procesoEstatus = service.getProcesoEstatusDao().findById(new BigDecimal(1));
                BitacoraEstatusProceso bitacoraEstatusProceso = new BitacoraEstatusProceso(new Date(), ax, procesoEstatus, usuarioModifico);
                service.getBitacoraEstatusProcesoDao().save(bitacoraEstatusProceso);
            } else {
                ax = service.getProcesoDao().update(proceso);
            }
        } catch (Exception e) {
            return Boolean.FALSE;
        }

        return guardaProcesoBeca(becasL, ax.getId());
    }

    public Boolean guardaBitacoraEstatusProceso(Proceso proceso, ProcesoEstatus procesoEstatus) {
        try {
            BitacoraEstatusProceso bitacoraEstatusProceso = new BitacoraEstatusProceso();

            bitacoraEstatusProceso.setFechamodificacion(new Date());
            bitacoraEstatusProceso.setProceso(proceso);
            bitacoraEstatusProceso.setProcesoEstatus(procesoEstatus);
            Usuario usuario = service.getUsuarioDao().findById(SecurityContextHolder.getContext().getAuthentication().getName());
            bitacoraEstatusProceso.setUsuario(usuario);

            service.getBitacoraEstatusProcesoDao().save(bitacoraEstatusProceso);
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public Proceso getProceso(BigDecimal id) {
        Proceso proceso = service.getProcesoDao().findById(id);
        return proceso;
    }

    /**
     * Elimina el proceso
     *
     * @author Victor Lozano
     * @param proceso
     * @return true si la operación se realizó exitosamente
     */
    public Boolean eliminaProceso(Proceso proceso) {
        List<BitacoraEstatusProceso> bitacora = service.getBitacoraEstatusProcesoDao().getByProceso(proceso.getId());
        for (BitacoraEstatusProceso estado : bitacora) {
            service.getBitacoraEstatusProcesoDao().delete(estado);
        }

        List<ProcesoProgramaBeca> becasProceso = service.getProcesoProgramaBecaDao().getByProceso(proceso.getId());
        if (becasProceso != null) {
            for (ProcesoProgramaBeca becaProceso : becasProceso) {
                service.getProcesoProgramaBecaDao().delete(becaProceso);
            }
        }
        try {
            service.getProcesoDao().delete(proceso);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Valida que el periodo indicado sea valido
     *
     * @author Victor Lozano
     * @param proceso
     * @return true si las fechas son validas
     */
    public boolean fechaValida(Proceso proceso) {
        return !proceso.getFechaFinal().before(proceso.getFechaInicial());
    }

    /**
     * Valida si el proceso ya se ha registrado previamente
     *
     * @author Victor Lozano
     * @param proceso
     * @return true si existe el proceso
     */
    public boolean existe(Proceso proceso) {
        return service.getProcesoDao().existe(proceso);
    }

    /**
     * Valida que el proceso se encuentre dentro del periodo
     *
     * @author Victor Lozano
     * @param proceso
     * @return true si el proceso se encuentra dentro del periodo de validez
     */
    public boolean procesoDentroPeriodo(Proceso proceso) {
        Periodo periodo = service.getPeriodoDao().findById(proceso.getPeriodo().getId());
        return !(periodo.getFechaInicial().after(proceso.getFechaInicial())
                || periodo.getFechaFinal().before(proceso.getFechaFinal()));
    }

    public Boolean existenOtorgamientos(BigDecimal id) {
        return service.getOtorgamientoDao().existeProcesoAsociado(id);
    }

    public String getByProceso(BigDecimal procesoId) {
        List<ProcesoProgramaBeca> ppb = service.getProcesoProgramaBecaDao().getByProceso(procesoId);
        String ax = "";
        if (ppb != null && !ppb.isEmpty()) {
            for (ProcesoProgramaBeca p : ppb) {
                ax += p.getProgramaBeca().getId() + ",";
            }
        }
        return (ppb != null && !ppb.isEmpty()) ? (ax.substring(0, ax.length() - 1)) : "";
    }

    public String getBecasNombreByProceso(BigDecimal procesoId) {
        List<ProcesoProgramaBeca> ppb = service.getProcesoProgramaBecaDao().getByProceso(procesoId);
        String ax = "";
        if (ppb != null && !ppb.isEmpty()) {
            for (ProcesoProgramaBeca p : ppb) {
                ax += p.getProgramaBeca().getNombre() + ",";
            }
        }
        return (ppb != null && !ppb.isEmpty()) ? (ax.substring(0, ax.length() - 1)) : "";
    }

    public Proceso existeONuevo(BigDecimal unidadAcademicaId, BigDecimal periodoId, BigDecimal tipoProcesoId) {
        Proceso proceso = service.getProcesoDao().existe(unidadAcademicaId, periodoId, tipoProcesoId);
        Date hoy = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(hoy);
        c.add(Calendar.DAY_OF_YEAR, 5);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.HOUR, 23);
        Date fin = c.getTime();

        if (proceso == null) {
            proceso = new Proceso();
            proceso.setUnidadAcademica(service.getUnidadAcademicaDao().findById(unidadAcademicaId));
            proceso.setPeriodo(service.getPeriodoDao().findById(periodoId));
            proceso.setTipoProceso(service.getTipoProcesoDao().findById(tipoProcesoId));
            proceso.setFechaInicial(hoy);
            proceso.setFechaFinal(fin);
            proceso.setProcesoEstatus(service.getProcesoEstatusDao().findById(new BigDecimal(1)));
            proceso = service.getProcesoDao().save(proceso);
            //TO DO: crear relaciones con los programas de beca (?)
        }
        return proceso;
    }

    public String creaCheckboxes(BigDecimal estatus_proceso_id, BigDecimal proceso_id) {

        StringBuilder checkbox = new StringBuilder();
        Integer ax = estatus_proceso_id.intValue();
        Map map = new HashMap();
        Map mapUnderscore = new HashMap();

        // Se crean botones simples
        map.put("checked", "");
        String disabled = "\", \"" + creaTexto("velocity/Admin/procesos/check.vm", map).replace("\n", "").replace("\r", "");
        map.put("checked", "checked");
        String disabledChecked = "\", \"" + creaTexto("velocity/Admin/procesos/check.vm", map).replace("\n", "").replace("\r", "");
        map.put("checked", "");

        // Se asignan valores a los parámetros
        if (ax != 4) {
            map.put("id", proceso_id);
            map.put("value", estatus_proceso_id);
            map.put("onclick", "validar(this.checked, this.value, this.id, this.id);");
        } else {
            // Cuando es 4, se asignan valores especiales
            map.put("id", proceso_id + "_3");
            map.put("value", "3");
            map.put("onclick", "validar(this.checked, this.value, " + proceso_id + ", this.id);");
            map.put("checked", "checked");
        }
        mapUnderscore.put("id", proceso_id + "_1");
        mapUnderscore.put("value", "1");
        mapUnderscore.put("onclick", "validar(this.checked, this.value, " + proceso_id + ", this.id);");
        mapUnderscore.put("checked", "");

        switch (ax) {
            case 1:
                if (isResponsable() || isJefe()) {
                    String boton = creaTexto("velocity/Admin/procesos/check-full.vm", map).replace("\n", "").replace("\r", "");

                    checkbox.append("\", \"").append(boton);
                    checkbox.append(disabled);
                    checkbox.append(disabled);
                } else {
                    checkbox.append(disabled);
                    checkbox.append(disabled);
                    checkbox.append(disabled);
                }
                break;
            case 2:
                if (isFuncionario()) {
                    String boton = creaTexto("velocity/Admin/procesos/check-full.vm", map).replace("\n", "").replace("\r", "");

                    checkbox.append(disabledChecked);
                    checkbox.append("\", \"").append(boton);
                    checkbox.append(disabled);
                } else if (isAnalista()) {
                    mapUnderscore.put("checked", "checked");
                    String botonUnderscore = creaTexto("velocity/Admin/procesos/check-full.vm", mapUnderscore).replace("\n", "").replace("\r", "");

                    checkbox.append("\", \"").append(botonUnderscore);
                    checkbox.append(disabled);
                    checkbox.append(disabled);
                } else if (isJefe()) {
                    mapUnderscore.put("checked", "checked");
                    String botonUnderscore = creaTexto("velocity/Admin/procesos/check-full.vm", mapUnderscore).replace("\n", "").replace("\r", "");
                    String boton = creaTexto("velocity/Admin/procesos/check-full.vm", map).replace("\n", "").replace("\r", "");

                    checkbox.append("\", \"").append(botonUnderscore);
                    checkbox.append("\", \"").append(boton);
                    checkbox.append(disabled);
                } else {
                    checkbox.append(disabledChecked);
                    checkbox.append(disabled);
                    checkbox.append(disabled);
                }
                break;
            case 3:
                if (isAnalista()) {
                    mapUnderscore.put("checked", "checked");
                    String botonUnderscore = creaTexto("velocity/Admin/procesos/check-full.vm", mapUnderscore).replace("\n", "").replace("\r", "");
                    String boton = creaTexto("velocity/Admin/procesos/check-full.vm", map).replace("\n", "").replace("\r", "");

                    checkbox.append("\", \"").append(botonUnderscore);
                    checkbox.append(disabledChecked);
                    checkbox.append("\", \"").append(boton);
                } else if (isJefe()) {
                    mapUnderscore.put("checked", "checked");
                    String botonUnderscore = creaTexto("velocity/Admin/procesos/check-full.vm", mapUnderscore).replace("\n", "").replace("\r", "");
                    String boton = creaTexto("velocity/Admin/procesos/check-full.vm", map).replace("\n", "").replace("\r", "");

                    checkbox.append("\", \"").append(botonUnderscore);
                    checkbox.append(disabledChecked);
                    checkbox.append("\", \"").append(boton);
                } else {
                    checkbox.append(disabledChecked);
                    checkbox.append(disabledChecked);
                    checkbox.append(disabled);
                }
                break;
            case 4:
                if (isAnalista() || isJefe()) {
                    mapUnderscore.put("checked", "checked");
                    String botonUnderscore = creaTexto("velocity/Admin/procesos/check-full.vm", mapUnderscore).replace("\n", "").replace("\r", "");
                    String boton = creaTexto("velocity/Admin/procesos/check-full.vm", map).replace("\n", "").replace("\r", "");

                    checkbox.append("\", \"").append(botonUnderscore);
                    checkbox.append(disabledChecked);
                    checkbox.append("\", \"").append(boton);
                } else {
                    checkbox.append(disabledChecked);
                    checkbox.append(disabledChecked);
                    checkbox.append(disabledChecked);
                }
                break;
            default:
                checkbox.append(disabled);
                checkbox.append(disabled);
                checkbox.append(disabled);
                break;
        }
        return checkbox.toString();

    }
}
