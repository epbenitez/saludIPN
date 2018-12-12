package com.becasipn.business;

import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.service.Service;
import com.opensymphony.xwork2.ActionContext;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Tania Sánchez
 */
public class PeriodoBO extends BaseBO {

    public PeriodoBO(Service service) {
        super(service);
    }

    /**
     * Guarda el objeto Presupuesto
     *
     * @param p
     * @return
     */
    public Boolean guardaPeriodo(Periodo p) {
        //Usuario
        Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        p.setUsuarioModifico(usuario);
        Date hoy = new Date();
        p.setFechaModifico(hoy);
        try {
            if (p.getId() == null) {
                service.getPeriodoDao().save(p);
            } else {
                service.getPeriodoDao().update(p);
            }
        } catch (Exception e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public Boolean existenPeriodosActivos(BigDecimal periodoId) {
        List<Periodo> lista = service.getPeriodoDao().existenPeriodosActivos(periodoId);
        return lista == null || lista.isEmpty() ? Boolean.FALSE : Boolean.TRUE;
    }

    public Periodo getPeriodo(BigDecimal id) {
        Periodo p = service.getPeriodoDao().findById(id);
        return p;
    }

    public Boolean eliminaPeriodo(Periodo p) {
        try {
            service.getPeriodoDao().delete(p);
            return Boolean.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }

    public Periodo getPeriodoActivo() {
        Periodo periodo = service.getPeriodoDao().getPeriodoActivo();
        return periodo;
    }

    public boolean fechaValida(Periodo p) {
        if (p.getFechaFinal().before(p.getFechaInicial())) {
            return false;
        }
        return true;
    }

    public Boolean existeClave(Periodo periodo) {
        List<Periodo> lista = service.getPeriodoDao().findAll();
        for (Periodo p : lista) {
            if (p.getClave().equals(periodo.getClave()) && !p.getId().equals(periodo.getId())) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Si el periodo es par, regresa true Author: Mario Márquez
     *
     * @param periodo Periodo a analizar
     * @return Boolean es o no par
     */
    public Boolean esPar(Periodo periodo) {
        String keyTerm = periodo.getClave();
        String end = keyTerm.substring(keyTerm.length() - 1);

        // Si la clave termina en dos, regresa true
        return "2".equals(end);
    }
}
