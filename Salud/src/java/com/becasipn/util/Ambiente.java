package com.becasipn.util;

import com.becasipn.persistence.model.Cargo;
import com.becasipn.persistence.model.Carrera;
import com.becasipn.persistence.model.CicloEscolar;
import com.becasipn.persistence.model.Configuracion;
import com.becasipn.persistence.model.EntidadFederativa;
import com.becasipn.persistence.model.EstadoCivil;
import com.becasipn.persistence.model.InegiTipoAsentamiento;
import com.becasipn.persistence.model.InegiTipoVialidad;
import com.becasipn.persistence.model.Nacionalidad;
import com.becasipn.persistence.model.Pais;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.PersonalAdministrativo;
import com.becasipn.persistence.model.Rol;
import com.becasipn.persistence.model.UnidadAcademica;
import com.becasipn.service.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Manejo de listas, mapas y otras estructuras de uso general.
 *
 * @author Patricia Benitez
 */
public class Ambiente {

    private Logger log = LogManager.getLogger(this.getClass().getName());
    private Service service;

    /**
     * Obtiene el valor de la variable service
     *
     * @return el valor de la variable service
     */
    public Service getService() {
        return service;
    }

    /**
     * Establece el valor de la variable service
     *
     * @param service nuevo valor de la variable service
     */
    public void setService(Service service) {
        this.service = service;
    }

    /**
     * Lista de Entidades Federativas
     */
    List<EntidadFederativa> entidadFederativaList = new ArrayList<EntidadFederativa>();

    /**
     * Obtiene la lista de entidades federativas
     *
     * @return entidadFederativaList.
     */
    public List<EntidadFederativa> getEntidadFederativa() {

        if (entidadFederativaList == null || entidadFederativaList.isEmpty()) {
            entidadFederativaList = service.getEntidadFederativaDao().findAll();
        }
        Collections.sort(entidadFederativaList);
        return entidadFederativaList;
    }

    /**
     * Lista de Entidades Federativas
     */
    List<EntidadFederativa> entidadFederativaTotalList = new ArrayList<EntidadFederativa>();

    /**
     * Obtiene la lista de entidades federativas
     *
     * @return entidadFederativaList.
     */
    public List<EntidadFederativa> getEntidadFederativaTotal() {

        if (entidadFederativaTotalList == null || entidadFederativaTotalList.isEmpty()) {
            entidadFederativaTotalList = service.getEntidadFederativaDao().findTotal();
        }
        Collections.sort(entidadFederativaTotalList);
        return entidadFederativaTotalList;
    }

    List<Rol> rolList = new ArrayList<>();

    public List<Rol> getAllRoles() {
        if (rolList == null || rolList.isEmpty()) {
            rolList = service.getRolDao().findAll();
        }
        return rolList;
    }


//    List<TipoInconformidadReconsideracion> tipoInconformidadReconsideracionList = new ArrayList<>();
//
//    public List<TipoInconformidadReconsideracion> getTipoInconformidadReconsideracionList() {
//        if (tipoInconformidadReconsideracionList == null || tipoInconformidadReconsideracionList.isEmpty()) {
//            tipoInconformidadReconsideracionList = service.getTipoInconformidadReconsideracionDao().findAll();
//        }
//        return tipoInconformidadReconsideracionList;
//    }
    List<Nacionalidad> nacionalidadList = new ArrayList<>();

    public List<Nacionalidad> getNacionalidadList() {
        if (nacionalidadList == null || nacionalidadList.isEmpty()) {
            nacionalidadList = service.getNacionalidadDao().findAll();
        }
        return nacionalidadList;
    }

    List<EstadoCivil> estadoCivilList = new ArrayList<>();

    public List<EstadoCivil> getEstadoCivil() {
        if (estadoCivilList == null || estadoCivilList.isEmpty()) {
            estadoCivilList = service.getEstadoCivilDao().findAll();
        }
        return estadoCivilList;
    }

    List<Configuracion> configuracionList = new ArrayList<>();

    public List<Configuracion> getConfiguracion() {
        if (configuracionList == null || configuracionList.isEmpty()) {
            configuracionList = service.getConfiguracionDao().findAll();
        }
        return configuracionList;
    }

    public List<Configuracion> reloadConfiguracion() {
//        SE SOLICITA QUE LOS VALORES DE ESTA TABLA NO REQUIERAN REINICIO
        configuracionList = service.getConfiguracionDao().findAll();
        return configuracionList;
    }

    public void setConfiguracionList() {
        configuracionList = service.getConfiguracionDao().findAll();
    }

    private List<Pais> paisNacimientoList = new ArrayList<Pais>();

    public List<Pais> getPaisNacimientoList() {
        if (paisNacimientoList == null || paisNacimientoList.isEmpty()) {
            paisNacimientoList = service.getPaisDao().findAll();

            Collections.sort(paisNacimientoList);
        }

        return paisNacimientoList;
    }

    private List<UnidadAcademica> unidadAcademicaList = new ArrayList<>();

    public List<UnidadAcademica> getUnidadAcademicaList() {
        if (unidadAcademicaList == null || unidadAcademicaList.isEmpty()) {
            unidadAcademicaList = service.getUnidadAcademicaDao().findAll();
        }
        return unidadAcademicaList;
    }

    private List<UnidadAcademica> unidadAcademicaOrderedList = new ArrayList<>();

    public List<UnidadAcademica> getUnidadAcademicaOrderedList() {
        if (unidadAcademicaOrderedList == null || unidadAcademicaOrderedList.isEmpty()) {
            unidadAcademicaOrderedList = service.getUnidadAcademicaDao().findAllAlphabOrder();
        }
        return unidadAcademicaOrderedList;
    }

    private List<UnidadAcademica> unidadAcademicaListx = new ArrayList<>();

    public List<UnidadAcademica> getUnidadAcademicaListx() {
        if (unidadAcademicaListx == null || unidadAcademicaListx.isEmpty()) {
            unidadAcademicaListx = service.getUnidadAcademicaDao().findAllx();
        }
        return unidadAcademicaListx;
    }

    private List<Carrera> carreraList = new ArrayList<>();

    public List<Carrera> getCarreraList() {
        if (carreraList == null || carreraList.isEmpty()) {
            carreraList = service.getCarreraDao().findAll();
        }

        return carreraList;
    }

    private List<Periodo> periodoList = new ArrayList<>();

    public List<Periodo> getPeriodoList() {
        if (periodoList == null || periodoList.isEmpty()) {
            periodoList = service.getPeriodoDao().findAll();
        }
        return periodoList;
    }

    public void setPeriodoList(List<Periodo> periodoList) {
        this.periodoList = periodoList;
    }

    private List<CicloEscolar> cicloList = new ArrayList<>();

    public List<CicloEscolar> getCicloList() {
        if (cicloList == null || cicloList.isEmpty()) {
            cicloList = service.getCicloEscolarDao().findAll();
        }
        return cicloList;
    }

    public void setCicloList(List<CicloEscolar> cicloList) {
        this.cicloList = cicloList;
    }


    public List<Rol> getRolList() {
        if (rolList == null || rolList.isEmpty()) {
            rolList = service.getRolDao().findAll();
        }
        return rolList;
    }

    private List<Cargo> cargoList = new ArrayList<>();

    public List<Cargo> getCargoList() {
        if (cargoList == null || cargoList.isEmpty()) {
            cargoList = service.getCargoDao().findAll();
        }
        return cargoList;
    }

}
