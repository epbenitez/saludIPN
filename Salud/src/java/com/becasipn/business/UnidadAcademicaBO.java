package com.becasipn.business;

import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.Proceso;
import com.becasipn.persistence.model.OrdenDeposito;
import com.becasipn.persistence.model.PersonalAdministrativo;
import com.becasipn.persistence.model.PresupuestoUnidadAcademica;
import com.becasipn.persistence.model.UnidadAcademica;
import com.becasipn.service.Service;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez
 */
public class UnidadAcademicaBO extends BaseBO {

    public UnidadAcademicaBO(Service service) {
        super(service);
    }

    /**
     * Guarda el objeto UnidadAcademica
     *
     * @param unidadAcademica
     * @return
     */
    public Boolean guardaUnidadAcademica(UnidadAcademica unidadAcademica) {
        try {
            if (unidadAcademica.getId() == null) {
                service.getUnidadAcademicaDao().save(unidadAcademica);
            } else {
                service.getUnidadAcademicaDao().update(unidadAcademica);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public Boolean asociadaProceso(BigDecimal unidadAcademicaId) {
        List<Proceso> lista = service.getProcesoDao().asociadaProceso(unidadAcademicaId);
        return lista == null || lista.isEmpty() ? Boolean.FALSE : Boolean.TRUE;
    }

    public Boolean asociadaAlumno(BigDecimal unidadAcademicaId) {
        List<DatosAcademicos> lista = service.getDatosAcademicosDao().asociadaAlumno(unidadAcademicaId);
        return lista == null || lista.isEmpty() ? Boolean.FALSE : Boolean.TRUE;
    }

    public Boolean asociadaUnidadAcademicaOrdenDeposito(BigDecimal unidadAcademicaId) {
        List<OrdenDeposito> lista = service.getOrdenDepositoDao().asociadaUnidadAcademicaOrdenDeposito(unidadAcademicaId);
        return lista == null || lista.isEmpty() ? Boolean.FALSE : Boolean.TRUE;
    }

    public Boolean asociadaPersonalAdministrativo(BigDecimal unidadAcademicaId) {
        List<PersonalAdministrativo> lista = service.getPersonalAdministrativoDao().asociadaPersonalAdministrativo(unidadAcademicaId);
        return lista == null || lista.isEmpty() ? Boolean.FALSE : Boolean.TRUE;
    }

    public Boolean asociadaPresupuestoUnidadAcademica(BigDecimal unidadAcademicaId) {
        List<PresupuestoUnidadAcademica> lista = service.getPresupuestoUnidadAcademicaDao().asociadaPresupuestoUnidadAcademica(unidadAcademicaId);
        return lista == null || lista.isEmpty() ? Boolean.FALSE : Boolean.TRUE;
    }

    public UnidadAcademica getUnidadAcademica(BigDecimal id) {
        UnidadAcademica ua = service.getUnidadAcademicaDao().findById(id);
        return ua;
    }

    public Boolean eliminaUnidadAcademica(UnidadAcademica ua) {
        try {
            service.getUnidadAcademicaDao().delete(ua);
            return Boolean.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }
}
