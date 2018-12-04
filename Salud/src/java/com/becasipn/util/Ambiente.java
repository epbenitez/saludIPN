package com.becasipn.util;

import com.becasipn.persistence.model.AreasConocimiento;
import com.becasipn.persistence.model.Beca;
import com.becasipn.persistence.model.Cargo;
import com.becasipn.persistence.model.Carrera;
import com.becasipn.persistence.model.CicloEscolar;
import com.becasipn.persistence.model.CompaniaCelular;
import com.becasipn.persistence.model.Configuracion;
import com.becasipn.persistence.model.ConvocatoriaSubes;
import com.becasipn.persistence.model.Discapacidad;
import com.becasipn.persistence.model.EnteroBeca;
import com.becasipn.persistence.model.EntidadFederativa;
import com.becasipn.persistence.model.EstadoCivil;
import com.becasipn.persistence.model.Estatus;
import com.becasipn.persistence.model.EstatusTarjetaBancaria;
import com.becasipn.persistence.model.Genero;
import com.becasipn.persistence.model.IdentificadorOtorgamiento;
import com.becasipn.persistence.model.InegiTipoAsentamiento;
import com.becasipn.persistence.model.InegiTipoVialidad;
import com.becasipn.persistence.model.Modalidad;
import com.becasipn.persistence.model.Movimiento;
import com.becasipn.persistence.model.Nacionalidad;
import com.becasipn.persistence.model.Nivel;
import com.becasipn.persistence.model.OrdenDeposito;
import com.becasipn.persistence.model.Pais;
import com.becasipn.persistence.model.Parentesco;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.PersonalAdministrativo;
import com.becasipn.persistence.model.ProcesoEstatus;
import com.becasipn.persistence.model.Rol;
import com.becasipn.persistence.model.TipoBajasDetalle;
import com.becasipn.persistence.model.TipoBecaPeriodo;
import com.becasipn.persistence.model.TipoBeca;
import com.becasipn.persistence.model.TipoInconformidadReconsideracion;
import com.becasipn.persistence.model.TipoProceso;
import com.becasipn.persistence.model.Transporte;
import com.becasipn.persistence.model.Trayecto;
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

    List<EstatusTarjetaBancaria> estatusTarjetaBancariaList = new ArrayList<>();

    public List<EstatusTarjetaBancaria> getEstatusTarjetaBancariaList() {
        if (estatusTarjetaBancariaList == null || estatusTarjetaBancariaList.isEmpty()) {
            estatusTarjetaBancariaList = service.getEstatusTarjetaBancariaDao().findAll();
        }
        return estatusTarjetaBancariaList;
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

    private List<CompaniaCelular> companiaCelularList = new ArrayList<>();

    public List<CompaniaCelular> getCompaniaCelularList() {
        if (companiaCelularList == null || companiaCelularList.isEmpty()) {
            companiaCelularList = service.getCompaniaCelularDao().findAll();
        }

        return companiaCelularList;
    }

    private List<Genero> generoList = new ArrayList<>();

    public List<Genero> getGeneroList() {
        if (generoList == null || generoList.isEmpty()) {
            generoList = service.getGeneroDao().findAll();
        }

        return generoList;
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

    private List<Nivel> nivelList = new ArrayList<>();

    public List<Nivel> getNivelList() {
        if (nivelList == null || nivelList.isEmpty()) {
            nivelList = service.getNivelDao().findAll();
        }
        return nivelList;
    }

    private List<Estatus> estatusList = new ArrayList<>();

    public List<Estatus> getEstatusList() {
        if (estatusList == null || estatusList.isEmpty()) {
            estatusList = service.getEstatusDao().findAll();
        }
        return estatusList;
    }

    private List<Modalidad> modalidadList = new ArrayList<>();

    public List<Modalidad> getModalidadList() {
        if (modalidadList == null || modalidadList.isEmpty()) {
            modalidadList = service.getModalidadDao().findAllActive();
        }
        return modalidadList;
    }

    private List<Movimiento> movimientoList = new ArrayList<>();

    public List<Movimiento> getMovimientoList() {
        if (movimientoList == null || movimientoList.isEmpty()) {
            movimientoList = service.getMovimientoDao().findAll();
        }
        return movimientoList;
    }

    private List<TipoProceso> procesoList = new ArrayList<>();

    public List<TipoProceso> getProcesoList() {
        if (procesoList == null || procesoList.isEmpty()) {
            procesoList = service.getTipoProcesoDao().findAll();
        }
        return procesoList;
    }

    private List<Beca> becaList = new ArrayList<>();

    public List<Beca> getBecaList() {
//        if (becaList == null || becaList.isEmpty()) {
        becaList = service.getBecaDao().findAll();
//        }
        return becaList;
    }
    private List<ProcesoEstatus> procesoEstatusList = new ArrayList<>();

    public List<ProcesoEstatus> getProcesoEstatusList() {
        if (procesoEstatusList == null || procesoEstatusList.isEmpty()) {
            procesoEstatusList = service.getProcesoEstatusDao().findAll();
        }
        return procesoEstatusList;
    }

    private List<TipoBeca> tipoBecaList = new ArrayList<>();

    public List<TipoBeca> getTipoBecaList() {
        if (tipoBecaList == null || tipoBecaList.isEmpty()) {
            tipoBecaList = service.getTipoBecaDao().findAll();
        }
        return tipoBecaList;
    }

    private List<TipoBecaPeriodo> tipoBecaPeriodoAList = new ArrayList<>();

    public List<TipoBecaPeriodo> getTipoBecaPeriodoAList() {
        if (tipoBecaPeriodoAList == null || tipoBecaPeriodoAList.isEmpty()) {
            tipoBecaPeriodoAList = service.getTipoBecaPeriodoDao().existenTiposBecaAsociados(service.getPeriodoDao().getPeriodoActivo().getId());
        }
        return tipoBecaPeriodoAList;
    }

    private List<TipoBajasDetalle> tipoBajasDetalleList = new ArrayList<>();

    public List<TipoBajasDetalle> getTipoBajasDetalleList() {
        if (tipoBajasDetalleList == null || tipoBajasDetalleList.isEmpty()) {
            tipoBajasDetalleList = service.getTipoBajasDetalleDao().findAllAct();
        }
        return tipoBajasDetalleList;
    }

    private List<AreasConocimiento> areasConocimientoList = new ArrayList<>();

    public List<AreasConocimiento> getAreasConocimientoList() {
        if (areasConocimientoList == null || areasConocimientoList.isEmpty()) {
            areasConocimientoList = service.getAreasConocimientoDao().findAll();
        }
        return areasConocimientoList;
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

    private List<TipoProceso> tipoProcesoList = new ArrayList<>();

    public List<TipoProceso> getTipoProcesoList() {
        if (tipoProcesoList == null || tipoProcesoList.isEmpty()) {
            tipoProcesoList = service.getTipoProcesoDao().findAll();
        }
        return tipoProcesoList;
    }

    private List<OrdenDeposito> ordenDepositoList = new ArrayList<>();

    public List<OrdenDeposito> getOrdenDepositoList() {
        if (ordenDepositoList == null || ordenDepositoList.isEmpty()) {
            ordenDepositoList = service.getOrdenDepositoDao().findAll();
        }
        return ordenDepositoList;
    }

    private List<TipoProceso> tipoProcesosActivosList = new ArrayList<>();

    public List<TipoProceso> getTipoProcesosActivosList() {
        if (tipoProcesosActivosList == null || tipoProcesosActivosList.isEmpty()) {
            //tipoProcesosActivosList = service.getTipoProcesoDao().getProcesosActivos();
        }
        return tipoProcesosActivosList;
    }

    private List<Nivel> nivelActivoList = new ArrayList<>();

    public List<Nivel> getnivelActivoList() {
        if (nivelActivoList == null || nivelActivoList.isEmpty()) {
            nivelActivoList = service.getNivelDao().nivelesActivos();
        }
        return nivelActivoList;
    }

    private List<PersonalAdministrativo> contactosList = new ArrayList<>();

    public List<PersonalAdministrativo> getContactosList() {
        if (contactosList == null || contactosList.isEmpty()) {
            contactosList = service.getPersonalAdministrativoDao().personalAdministrativoResponsableBecas(null);
        }
        return contactosList;
    }

    private List<Discapacidad> discapacidadList = new ArrayList<>();

    public List<Discapacidad> getDiscapacidadList() {
        if (discapacidadList == null || discapacidadList.isEmpty()) {
            discapacidadList = service.getDiscapacidadDao().findAll();
        }
        return discapacidadList;
    }

    private List<EnteroBeca> enteroBecaList = new ArrayList<>();

    public List<EnteroBeca> getEnteroBecaList() {
        if (enteroBecaList == null || enteroBecaList.isEmpty()) {
            enteroBecaList = service.getEnteroBecaDao().findAll();
        }
        return enteroBecaList;
    }

    private List<Parentesco> parentescoList = new ArrayList<>();

    public List<Parentesco> getParentescoList() {
        if (parentescoList == null || parentescoList.isEmpty()) {
            parentescoList = service.getParentescoDao().findAll();
        }
        return parentescoList;
    }

    private List<Transporte> transporteList = new ArrayList<>();

    public List<Transporte> getTransporteList() {
        if (transporteList == null || transporteList.isEmpty()) {
            transporteList = service.getTransporteDao().findAll();
        }
        return transporteList;
    }

    private List<Trayecto> trayectoList = new ArrayList<>();

    public List<Trayecto> getTrayectoList() {
        if (trayectoList == null || trayectoList.isEmpty()) {
            trayectoList = service.getTrayectoDao().findAll();
        }
        return trayectoList;
    }

    private List<IdentificadorOtorgamiento> identificadorOtorgamientoList = new ArrayList<>();

    public List<IdentificadorOtorgamiento> getIdentificadorOtorgamientoList() {
        if (identificadorOtorgamientoList == null || identificadorOtorgamientoList.isEmpty()) {
            identificadorOtorgamientoList = service.getIdentificadorOtorgamientoDao().findAll();
        }
        return identificadorOtorgamientoList;
    }

    private List<InegiTipoAsentamiento> inegiTipoAsentamientoList = new ArrayList<>();

    public List<InegiTipoAsentamiento> getInegiTipoAsentamiento() {
        if (inegiTipoAsentamientoList == null || inegiTipoAsentamientoList.isEmpty()) {
            inegiTipoAsentamientoList = service.getInegiTipoAsentamientoDao().findAll();
        }
        return inegiTipoAsentamientoList;
    }

    private List<InegiTipoVialidad> inegiTipoVialidadList = new ArrayList<>();

    public List<InegiTipoVialidad> getInegiTipoVialidad() {
        if (inegiTipoVialidadList == null || inegiTipoVialidadList.isEmpty()) {
            inegiTipoVialidadList = service.getInegiTipoVialidadDao().findAll();
        }
        return inegiTipoVialidadList;
    }

    List<ConvocatoriaSubes> convocatoriasSubesList = new ArrayList<>();

    public List<ConvocatoriaSubes> getConvocatoriasSubesList() {
        if (convocatoriasSubesList == null || convocatoriasSubesList.isEmpty()) {
            convocatoriasSubesList = service.getConvocatoriaSubesDao().findAll();
        }
        return convocatoriasSubesList;
    }

}
