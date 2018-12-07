package com.becasipn.service;

import com.becasipn.persistence.model.Genero;
import com.becasipn.persistence.dao.AlumnoDAEDao;
import com.becasipn.persistence.dao.AlumnoDao;
import com.becasipn.persistence.dao.AlumnoDatosBancariosDao;
import com.becasipn.persistence.dao.AlumnoTarjetaBancariaDao;
import com.becasipn.persistence.dao.AreasConocimientoDao;
import com.becasipn.persistence.dao.BancosDao;
import com.becasipn.persistence.dao.BecaDao;
import com.becasipn.persistence.dao.BecaMontoVariableDao;
import com.becasipn.persistence.dao.BecaUniversalDao;
import com.becasipn.persistence.dao.BitacoraAlumnoDao;
import com.becasipn.persistence.dao.BitacoraDao;
import com.becasipn.persistence.dao.BitacoraEstatusProcesoDao;
import com.becasipn.persistence.dao.BitacoraOtorgamientoExternoDao;
import com.becasipn.persistence.dao.BitacoraOtorgamientosDao;
import com.becasipn.persistence.dao.BitacoraTarjetaBancariaDao;
import com.becasipn.persistence.dao.CargoDao;
import com.becasipn.persistence.dao.CarreraDao;
import com.becasipn.persistence.dao.ConfiguracionDao;
import com.becasipn.persistence.dao.CuestionarioDao;
import com.becasipn.persistence.dao.SolicitudBecaDao;
import com.becasipn.persistence.dao.CuestionarioPreguntaRespuestaDao;
import com.becasipn.persistence.dao.CuestionarioPreguntaTipoDao;
import com.becasipn.persistence.dao.CuestionarioPreguntasDao;
import com.becasipn.persistence.dao.CuestionarioRespuestasDao;
import com.becasipn.persistence.dao.CuestionarioRespuestasUsuarioDao;
import com.becasipn.persistence.dao.CuestionarioSeccionDao;
import com.becasipn.persistence.dao.CuestionarioTransporteDao;
import com.becasipn.persistence.dao.DaoBase;
import com.becasipn.persistence.dao.DepositoNoCoincideDao;
import com.becasipn.persistence.dao.DepositoUnidadAcademicaDao;
import com.becasipn.persistence.dao.DepositosDao;
import com.becasipn.persistence.dao.DireccionDao;
import com.becasipn.persistence.dao.DiscapacidadDao;
import com.becasipn.persistence.dao.DocumentosDao;
import com.becasipn.persistence.dao.EntidadFederativaDao;
import com.becasipn.persistence.dao.ProcesoDao;
import com.becasipn.persistence.dao.EnteroBecaDao;
import com.becasipn.persistence.dao.EstatusDao;
import com.becasipn.persistence.dao.EstatusDepositoDao;
import com.becasipn.persistence.dao.MenuDao;
import com.becasipn.persistence.dao.ModalidadDao;
import com.becasipn.persistence.dao.NivelDao;
import com.becasipn.persistence.dao.MovimientoDao;
import com.becasipn.persistence.dao.OtorgamientoBajasDetalleDao;
import com.becasipn.persistence.dao.OtorgamientoDao;
import com.becasipn.persistence.dao.PaisDao;
import com.becasipn.persistence.dao.PeriodoDao;
import com.becasipn.persistence.dao.PersonalAdministrativoDao;
import com.becasipn.persistence.dao.PresupuestoPeriodoDao;
import com.becasipn.persistence.dao.PresupuestoTipoBecaPeriodoDao;
import com.becasipn.persistence.dao.PresupuestoUnidadAcademicaDao;
import com.becasipn.persistence.dao.ProcesoEstatusDao;
import com.becasipn.persistence.dao.RelacionGeograficaDao;
import com.becasipn.persistence.dao.RelacionMenuRolesDao;
import com.becasipn.persistence.dao.BitacoraPermisoCambioDao;
import com.becasipn.persistence.dao.CensoSaludDao;
import com.becasipn.persistence.dao.ConvocatoriaSubesDao;
import com.becasipn.persistence.dao.DatosAcademicosDao;
import com.becasipn.persistence.dao.TipoPermisoCambioDao;
import com.becasipn.persistence.dao.RolDao;
import com.becasipn.persistence.dao.EstatusTarjetaBancariaDao;
import com.becasipn.persistence.dao.SolicitudCuentasDao;
import com.becasipn.persistence.dao.IdentificadorOtorgamientoDao;
import com.becasipn.persistence.dao.PadronSubesDao;
import com.becasipn.persistence.dao.ParentescoDao;
import com.becasipn.persistence.dao.ProcesoProgramaBecaDao;
import com.becasipn.persistence.dao.TarjetaBancariaDao;
import com.becasipn.persistence.dao.TipoBajasDetalleDao;
import com.becasipn.persistence.dao.TipoBecaDao;
import com.becasipn.persistence.dao.TipoBecaPeriodoDao;
import com.becasipn.persistence.dao.TipoBecaPeriodoHistoricoDao;
import com.becasipn.persistence.dao.TipoDepositoDao;
import com.becasipn.persistence.dao.TipoProcesoDao;
import com.becasipn.persistence.dao.TransporteDao;
import com.becasipn.persistence.dao.TransporteDatosFamiliaresDao;
import com.becasipn.persistence.dao.TransporteTrasladoDao;
import com.becasipn.persistence.dao.TrayectoDao;
import com.becasipn.persistence.dao.UnidadAcademicaDao;
import com.becasipn.persistence.dao.UsuarioDao;
import com.becasipn.persistence.dao.UsuarioPrivilegioDao;
import com.becasipn.persistence.dao.VWPresupuestoPeriodoDao;
import com.becasipn.persistence.dao.VWPresupuestoTipoBecaPeriodoDao;
import com.becasipn.persistence.dao.VWPresupuestoUnidadAcademicaDao;
import com.becasipn.persistence.dao.PadronProsperaDao;
import com.becasipn.persistence.model.CompaniaCelular;
import com.becasipn.persistence.model.EstadoCivil;
import com.becasipn.persistence.model.LocalidadColonia;
import com.becasipn.persistence.model.Nacionalidad;
import com.becasipn.persistence.dao.ClasificacionSolicitudDao;
import com.becasipn.persistence.dao.EstatusReconsideracionDao;
import com.becasipn.persistence.dao.InegiLocalidadDao;
import com.becasipn.persistence.dao.InegiTipoAsentamientoDao;
import com.becasipn.persistence.dao.InegiTipoVialidadDao;
import com.becasipn.persistence.dao.MotivoRechazoSolicitudDao;
import com.becasipn.persistence.dao.ListaEsperaDao;
import com.becasipn.persistence.dao.MontoMaximoIngresosDao;
import com.becasipn.persistence.dao.NotificacionesDao;
import com.becasipn.persistence.dao.TipoNotificacionDao;
import com.becasipn.persistence.dao.NotificacionesRolDao;
import com.becasipn.persistence.dao.OtorgamientoExternoDao;
import com.becasipn.persistence.dao.SolicitudBecaIndividualDao;
import com.becasipn.persistence.dao.SolicitudBecaNivelDao;
import com.becasipn.persistence.dao.SolicitudBecaUADao;
import com.becasipn.persistence.model.BecaExterna;
import com.becasipn.persistence.model.CicloEscolar;
import com.becasipn.persistence.model.ErroresBanamex;
import com.becasipn.persistence.dao.SeguimientoBecariosDao;
import com.becasipn.persistence.dao.SolicitudReconsideracionDao;
import com.becasipn.persistence.dao.TipoInconformidadReconsideracionDao;
import java.math.BigDecimal;

public class Service {

    private BitacoraDao bitacoraDao;
    private UsuarioDao usuarioDao;
    private RelacionMenuRolesDao relacionMenuRolesDao;
    private RelacionGeograficaDao relacionGeograficaDao;
    private EntidadFederativaDao entidadFederativaDao;
    private PaisDao paisDao;
    private RolDao rolDao;
    private BitacoraPermisoCambioDao bitacoraPermisoCambioDao;
    private TipoPermisoCambioDao tipoPermisoCambioDao;
    private DaoBase<Nacionalidad, Long> nacionalidadDao;
    //   private NacionalidadDao nacionalidadDao;
    private MenuDao menuDao;
    //--------------------------------------------------------------------------
    // CONFIGURACION DEL SISTEMA
    private ConfiguracionDao configuracionDao;
    private UsuarioPrivilegioDao usuarioPrivilegioDao;
    //--------------------------------------------------------------------------
    // REGISTRO DE ALUMNOS
    private AlumnoDao alumnoDao;
    private AlumnoDAEDao alumnoDAEDao;
    private BecaUniversalDao becaUniversalDao;
    private DaoBase<EstadoCivil, BigDecimal> estadoCivilDao;
    //private DaoBase<UnidadAcademica, Long> unidadAcademicaDao;
    private CarreraDao carreraDao;
    private DaoBase<CompaniaCelular, Long> companiaCelularDao;
    private DaoBase<Genero, BigDecimal> generoDao;
    private DaoBase<LocalidadColonia, Long> localidadColoniaDao;
    //CUESTIONARIOS
    private SeguimientoBecariosDao seguimientoBecarios;
    private CensoSaludDao censoSaludDao;
    private CuestionarioDao cuestionarioDao;
    private CuestionarioPreguntaRespuestaDao cuestionarioPreguntaRespuestaDao;
    private CuestionarioPreguntaTipoDao cuestionarioPreguntaTipoDao;
    private CuestionarioPreguntasDao cuestionarioPreguntasDao;
    private CuestionarioRespuestasDao cuestionarioRespuestasDao;
    private CuestionarioRespuestasUsuarioDao cuestionarioRespuestasUsuarioDao;
    private CuestionarioSeccionDao cuestionarioSeccionDao;
    private DatosAcademicosDao datosAcademicosDao;
    private SolicitudBecaDao solicitudBecaDao;
    private SolicitudBecaUADao solicitudBecaUADao;
    private SolicitudBecaNivelDao solicitudBecaNivelDao;
    private SolicitudBecaIndividualDao solicitudBecaIndividualDao;
    private PeriodoDao periodoDao;
    private ProcesoDao procesoDao;
    private NivelDao nivelDao;
    private EstatusDao estatusDao;
    private ModalidadDao modalidadDao;
    private BecaDao becaDao;
    private TipoBecaDao tipoBecaDao;
    private TipoBecaPeriodoDao tipoBecaPeriodoDao;
    private TipoBecaPeriodoHistoricoDao tipoBecaPeriodoHistoricoDao;
    private BitacoraEstatusProcesoDao bitacoraEstatusProcesoDao;
    private TipoBajasDetalleDao tipoBajasDetalleDao;
    private MovimientoDao movimientoDao;
    private UnidadAcademicaDao unidadAcademicaDao;
    private ProcesoEstatusDao procesoEstatusDao;
    private OtorgamientoDao otorgamientoDao;
    private OtorgamientoBajasDetalleDao otorgamientoBajasDetalleDao;
    private AreasConocimientoDao areasConocimientoDao;
    private CargoDao cargoDao;
    private PersonalAdministrativoDao personalAdministrativoDao;
    private DocumentosDao documentosDao;
    private PadronProsperaDao padronProsperaDao;
    private PresupuestoPeriodoDao presupuestoPeriodoDao;
    private PresupuestoTipoBecaPeriodoDao PresupuestoTipoBecaPeriodoDao;
    private PresupuestoUnidadAcademicaDao PresupuestoUnidadAcademicaDao;
    private AlumnoTarjetaBancariaDao alumnoTarjetaDao;
    private BitacoraTarjetaBancariaDao bitacoraTarjetaBancariaDao;
    private VWPresupuestoUnidadAcademicaDao vwPresupuestoUnidadAcademicaDao;
    private VWPresupuestoPeriodoDao vwPresupuestoPeriodoDao;
    private VWPresupuestoTipoBecaPeriodoDao vwPresupuestoTipoBecaPeriodoDao;
    private TipoProcesoDao tipoProcesoDao;
    private DepositosDao depositosDao;
    private EstatusDepositoDao estatusDepositoDao;
    private TarjetaBancariaDao tarjetaBancariaDao;
    private EstatusTarjetaBancariaDao estatusTarjetaBancariaDao;
    private SolicitudCuentasDao solicitudCuentasDao;
    private DepositoUnidadAcademicaDao depositoUnidadAcademicaDao;
    private TipoDepositoDao tipoDepositoDao;
    private DepositoNoCoincideDao depositoNoCoincideDao;
    private BitacoraAlumnoDao bitacoraAlumnoDao;
    private DireccionDao direccionDao;
    private BitacoraOtorgamientosDao bitacoraOtorgamientosDao;
    private TrayectoDao trayectoDao;
    private TransporteDao transporteDao;
    private ParentescoDao parentescoDao;
    private EnteroBecaDao enteroBecaDao;
    private DiscapacidadDao discapacidadDao;
    private CuestionarioTransporteDao cuestionarioTransporteDao;
    private TransporteTrasladoDao transporteTrasladoDao;
    private TransporteDatosFamiliaresDao transporteDatosFamiliaresDao;
    private PadronSubesDao padronSubesDao;
    private ConvocatoriaSubesDao convocatoriaSubesDao;
    private IdentificadorOtorgamientoDao identificadorOtorgamientoDao;
    private BecaMontoVariableDao BecaMontoVariableDao;
    private ProcesoProgramaBecaDao procesoProgramaBecaDao;
    private AlumnoDatosBancariosDao alumnoDatosBancariosDao;
    private ClasificacionSolicitudDao clasificacionSolicitudDao;
    private MotivoRechazoSolicitudDao motivoRechazoSolicitudDao;
    private ListaEsperaDao listaEsperaDao;
    private InegiLocalidadDao inegiLocalidadDao;
    private InegiTipoAsentamientoDao inegiTipoAsentamientoDao;
    private InegiTipoVialidadDao inegiTipoVialidadDao;
    private MontoMaximoIngresosDao montoMaximoIngresosDao;
    private BancosDao bancosDao;

    private DaoBase<ErroresBanamex, BigDecimal> erroresBanamexDao;

    //Notificaciones
    private NotificacionesDao notificacionesDao;
    private TipoNotificacionDao tipoNotificacionDao;
    private NotificacionesRolDao notificacionRolDao;

    private DaoBase<BecaExterna, BigDecimal> becaExternaDao;
    private OtorgamientoExternoDao otorgamientoExternoDao;
    private BitacoraOtorgamientoExternoDao bitacoraOtorgamientoExternoDao;
    private DaoBase<CicloEscolar, BigDecimal> cicloEscolarDao;

    //Recurso Inconformidad
    private SolicitudReconsideracionDao solicitudReconsideracionDao;
    private EstatusReconsideracionDao estatusReconsideracionDao;
    private TipoInconformidadReconsideracionDao tipoInconformidadReconsideracionDao;

    public DatosAcademicosDao getDatosAcademicosDao() {
        return datosAcademicosDao;
    }

    public void setDatosAcademicosDao(DatosAcademicosDao datosAcademicosDao) {
        this.datosAcademicosDao = datosAcademicosDao;
    }

    public BitacoraDao getBitacoraDao() {
        return bitacoraDao;
    }

    public void setBitacoraDao(BitacoraDao bitacoraDao) {
        this.bitacoraDao = bitacoraDao;
    }

    public UsuarioDao getUsuarioDao() {
        return usuarioDao;
    }

    public void setUsuarioDao(UsuarioDao usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    public EntidadFederativaDao getEntidadFederativaDao() {
        return entidadFederativaDao;
    }

    public void setEntidadFederativaDao(EntidadFederativaDao entidadFederativaDao) {
        this.entidadFederativaDao = entidadFederativaDao;
    }

    public PaisDao getPaisDao() {
        return paisDao;
    }

    public void setPaisDao(PaisDao paisDao) {
        this.paisDao = paisDao;
    }

    public ConfiguracionDao getConfiguracionDao() {
        return configuracionDao;
    }

    public void setConfiguracionDao(ConfiguracionDao configuracionDao) {
        this.configuracionDao = configuracionDao;
    }

    public UsuarioPrivilegioDao getUsuarioPrivilegioDao() {
        return usuarioPrivilegioDao;
    }

    public void setUsuarioPrivilegioDao(UsuarioPrivilegioDao usuarioPrivilegioDao) {
        this.usuarioPrivilegioDao = usuarioPrivilegioDao;
    }

    public CuestionarioDao getCuestionarioDao() {
        return cuestionarioDao;
    }

    public void setCuestionarioDao(CuestionarioDao cuestionarioDao) {
        this.cuestionarioDao = cuestionarioDao;
    }

    public CuestionarioPreguntaRespuestaDao getCuestionarioPreguntaRespuestaDao() {
        return cuestionarioPreguntaRespuestaDao;
    }

    public void setCuestionarioPreguntaRespuestaDao(CuestionarioPreguntaRespuestaDao cuestionarioPreguntaRespuestaDao) {
        this.cuestionarioPreguntaRespuestaDao = cuestionarioPreguntaRespuestaDao;
    }

    public CuestionarioPreguntaTipoDao getCuestionarioPreguntaTipoDao() {
        return cuestionarioPreguntaTipoDao;
    }

    public void setCuestionarioPreguntaTipoDao(CuestionarioPreguntaTipoDao cuestionarioPreguntaTipoDao) {
        this.cuestionarioPreguntaTipoDao = cuestionarioPreguntaTipoDao;
    }

    public CuestionarioPreguntasDao getCuestionarioPreguntasDao() {
        return cuestionarioPreguntasDao;
    }

    public void setCuestionarioPreguntasDao(CuestionarioPreguntasDao cuestionarioPreguntasDao) {
        this.cuestionarioPreguntasDao = cuestionarioPreguntasDao;
    }

    public CuestionarioRespuestasDao getCuestionarioRespuestasDao() {
        return cuestionarioRespuestasDao;
    }

    public void setCuestionarioRespuestasDao(CuestionarioRespuestasDao cuestionarioRespuestasDao) {
        this.cuestionarioRespuestasDao = cuestionarioRespuestasDao;
    }

    public CuestionarioRespuestasUsuarioDao getCuestionarioRespuestasUsuarioDao() {
        return cuestionarioRespuestasUsuarioDao;
    }

    public void setCuestionarioRespuestasUsuarioDao(CuestionarioRespuestasUsuarioDao cuestionarioRespuestasUsuarioDao) {
        this.cuestionarioRespuestasUsuarioDao = cuestionarioRespuestasUsuarioDao;
    }

    public CuestionarioSeccionDao getCuestionarioSeccionDao() {
        return cuestionarioSeccionDao;
    }

    public void setCuestionarioSeccionDao(CuestionarioSeccionDao cuestionarioSeccionDao) {
        this.cuestionarioSeccionDao = cuestionarioSeccionDao;
    }

    public SolicitudBecaDao getSolicitudBecaDao() {
        return solicitudBecaDao;
    }

    public void setSolicitudBecaDao(SolicitudBecaDao solicitudBecaDao) {
        this.solicitudBecaDao = solicitudBecaDao;
    }

    public SolicitudBecaUADao getSolicitudBecaUADao() {
        return solicitudBecaUADao;
    }

    public void setSolicitudBecaUADao(SolicitudBecaUADao solicitudBecaUADao) {
        this.solicitudBecaUADao = solicitudBecaUADao;
    }

    public SolicitudBecaNivelDao getSolicitudBecaNivelDao() {
        return solicitudBecaNivelDao;
    }

    public void setSolicitudBecaNivelDao(SolicitudBecaNivelDao solicitudBecaNivelDao) {
        this.solicitudBecaNivelDao = solicitudBecaNivelDao;
    }

    public SolicitudBecaIndividualDao getSolicitudBecaIndividualDao() {
        return solicitudBecaIndividualDao;
    }

    public void setSolicitudBecaIndividualDao(SolicitudBecaIndividualDao solicitudBecaIndividualDao) {
        this.solicitudBecaIndividualDao = solicitudBecaIndividualDao;
    }

    public RolDao getRolDao() {
        return rolDao;
    }

    public void setRolDao(RolDao rolDao) {
        this.rolDao = rolDao;
    }

    public DaoBase<Nacionalidad, Long> getNacionalidadDao() {
        return nacionalidadDao;
    }

    public void setNacionalidadDao(DaoBase<Nacionalidad, Long> nacionalidadDao) {
        this.nacionalidadDao = nacionalidadDao;
    }

    public RelacionMenuRolesDao getRelacionMenuRolesDao() {
        return relacionMenuRolesDao;
    }

    public void setRelacionMenuRolesDao(RelacionMenuRolesDao relacionMenuRolesDao) {
        this.relacionMenuRolesDao = relacionMenuRolesDao;
    }

    public RelacionGeograficaDao getRelacionGeograficaDao() {
        return relacionGeograficaDao;
    }

    public void setRelacionGeograficaDao(RelacionGeograficaDao relacionGeograficaDao) {
        this.relacionGeograficaDao = relacionGeograficaDao;
    }

    public MenuDao getMenuDao() {
        return menuDao;
    }

    public void setMenuDao(MenuDao menuDao) {
        this.menuDao = menuDao;
    }

    public AlumnoDao getAlumnoDao() {
        return alumnoDao;
    }

    public void setAlumnoDao(AlumnoDao alumnoDao) {
        this.alumnoDao = alumnoDao;
    }

    public AlumnoDAEDao getAlumnoDAEDao() {
        return alumnoDAEDao;
    }

    public void setAlumnoDAEDao(AlumnoDAEDao alumnoDAEDao) {
        this.alumnoDAEDao = alumnoDAEDao;
    }

    public DaoBase<EstadoCivil, BigDecimal> getEstadoCivilDao() {
        return estadoCivilDao;
    }

    public void setEstadoCivilDao(DaoBase<EstadoCivil, BigDecimal> estadoCivilDao) {
        this.estadoCivilDao = estadoCivilDao;
    }

    /*public DaoBase<UnidadAcademica, Long> getUnidadAcademicaDao() {
     return unidadAcademicaDao;
     }

     public void setUnidadAcademicaDao(DaoBase<UnidadAcademica, Long> unidadAcademicaDao) {
     this.unidadAcademicaDao = unidadAcademicaDao;
     }*/
    public CarreraDao getCarreraDao() {
        return carreraDao;
    }

    public void setCarreraDao(CarreraDao carreraDao) {
        this.carreraDao = carreraDao;
    }

    public DaoBase<CompaniaCelular, Long> getCompaniaCelularDao() {
        return companiaCelularDao;
    }

    public void setCompaniaCelularDao(DaoBase<CompaniaCelular, Long> companiaCelularDao) {
        this.companiaCelularDao = companiaCelularDao;
    }

    public DaoBase<Genero, BigDecimal> getGeneroDao() {
        return generoDao;
    }

    public void setGeneroDao(DaoBase<Genero, BigDecimal> generoDao) {
        this.generoDao = generoDao;
    }

    public DaoBase<LocalidadColonia, Long> getLocalidadColoniaDao() {
        return localidadColoniaDao;
    }

    public void setLocalidadColoniaDao(DaoBase<LocalidadColonia, Long> localidadColoniaDao) {
        this.localidadColoniaDao = localidadColoniaDao;
    }

    public PeriodoDao getPeriodoDao() {
        return periodoDao;
    }

    public void setPeriodoDao(PeriodoDao periodoDao) {
        this.periodoDao = periodoDao;
    }

    public NivelDao getNivelDao() {
        return nivelDao;
    }

    public void setNivelDao(NivelDao nivelDao) {
        this.nivelDao = nivelDao;
    }

    public EstatusDao getEstatusDao() {
        return estatusDao;
    }

    public void setEstatusDao(EstatusDao estatusDao) {
        this.estatusDao = estatusDao;
    }

    public ModalidadDao getModalidadDao() {
        return modalidadDao;
    }

    public void setModalidadDao(ModalidadDao modalidadDao) {
        this.modalidadDao = modalidadDao;
    }

    public TipoBecaDao getTipoBecaDao() {
        return tipoBecaDao;
    }

    public void setTipoBecaDao(TipoBecaDao tipoBecaDao) {
        this.tipoBecaDao = tipoBecaDao;
    }

    public ProcesoDao getProcesoDao() {
        return procesoDao;
    }

    public void setProcesoDao(ProcesoDao procesoDao) {
        this.procesoDao = procesoDao;
    }

    public MovimientoDao getMovimientoDao() {
        return movimientoDao;
    }

    public void setMovimientoDao(MovimientoDao movimientoDao) {
        this.movimientoDao = movimientoDao;
    }

    public BecaDao getBecaDao() {
        return becaDao;
    }

    public void setBecaDao(BecaDao becaDao) {
        this.becaDao = becaDao;
    }

    public UnidadAcademicaDao getUnidadAcademicaDao() {
        return unidadAcademicaDao;
    }

    public void setUnidadAcademicaDao(UnidadAcademicaDao unidadAcademicaDao) {
        this.unidadAcademicaDao = unidadAcademicaDao;
    }

    public ProcesoEstatusDao getProcesoEstatusDao() {
        return procesoEstatusDao;
    }

    public void setProcesoEstatusDao(ProcesoEstatusDao procesoEstatusDao) {
        this.procesoEstatusDao = procesoEstatusDao;
    }

    public TipoBecaPeriodoDao getTipoBecaPeriodoDao() {
        return tipoBecaPeriodoDao;
    }

    public void setTipoBecaPeriodoDao(TipoBecaPeriodoDao tipoBecaPeriodoDao) {
        this.tipoBecaPeriodoDao = tipoBecaPeriodoDao;
    }

    public OtorgamientoDao getOtorgamientoDao() {
        return otorgamientoDao;
    }

    public void setOtorgamientoDao(OtorgamientoDao otorgamientoDao) {
        this.otorgamientoDao = otorgamientoDao;
    }

    public OtorgamientoBajasDetalleDao getOtorgamientoBajasDetalleDao() {
        return otorgamientoBajasDetalleDao;
    }

    public void setOtorgamientoBajasDetalleDao(OtorgamientoBajasDetalleDao otorgamientoBajasDetalleDao) {
        this.otorgamientoBajasDetalleDao = otorgamientoBajasDetalleDao;
    }

    public CargoDao getCargoDao() {
        return cargoDao;
    }

    public void setCargoDao(CargoDao cargoDao) {
        this.cargoDao = cargoDao;
    }

    public PersonalAdministrativoDao getPersonalAdministrativoDao() {
        return personalAdministrativoDao;
    }

    public void setPersonalAdministrativoDao(PersonalAdministrativoDao personalAdministrativoDao) {
        this.personalAdministrativoDao = personalAdministrativoDao;
    }

    public AreasConocimientoDao getAreasConocimientoDao() {
        return areasConocimientoDao;
    }

    public void setAreasConocimientoDao(AreasConocimientoDao areasConocimientoDao) {
        this.areasConocimientoDao = areasConocimientoDao;
    }

    public TipoBecaPeriodoHistoricoDao getTipoBecaPeriodoHistoricoDao() {
        return tipoBecaPeriodoHistoricoDao;
    }

    public void setTipoBecaPeriodoHistoricoDao(TipoBecaPeriodoHistoricoDao tipoBecaPeriodoHistoricoDao) {
        this.tipoBecaPeriodoHistoricoDao = tipoBecaPeriodoHistoricoDao;
    }

    public DocumentosDao getDocumentosDao() {
        return documentosDao;
    }

    public void setDocumentosDao(DocumentosDao documentosDao) {
        this.documentosDao = documentosDao;
    }

    public PresupuestoPeriodoDao getPresupuestoPeriodoDao() {
        return presupuestoPeriodoDao;
    }

    public void setPresupuestoPeriodoDao(PresupuestoPeriodoDao presupuestoPeriodoDao) {
        this.presupuestoPeriodoDao = presupuestoPeriodoDao;
    }

    public PresupuestoTipoBecaPeriodoDao getPresupuestoTipoBecaPeriodoDao() {
        return PresupuestoTipoBecaPeriodoDao;
    }

    public void setPresupuestoTipoBecaPeriodoDao(PresupuestoTipoBecaPeriodoDao PresupuestoTipoBecaPeriodoDao) {
        this.PresupuestoTipoBecaPeriodoDao = PresupuestoTipoBecaPeriodoDao;
    }

    public PresupuestoUnidadAcademicaDao getPresupuestoUnidadAcademicaDao() {
        return PresupuestoUnidadAcademicaDao;
    }

    public void setPresupuestoUnidadAcademicaDao(PresupuestoUnidadAcademicaDao PresupuestoUnidadAcademicaDao) {
        this.PresupuestoUnidadAcademicaDao = PresupuestoUnidadAcademicaDao;
    }

    public TipoBajasDetalleDao getTipoBajasDetalleDao() {
        return tipoBajasDetalleDao;
    }

    public void setTipoBajasDetalleDao(TipoBajasDetalleDao tipoBajasDetalleDao) {
        this.tipoBajasDetalleDao = tipoBajasDetalleDao;
    }

    public EstatusTarjetaBancariaDao getEstatusTarjetaBancariaDao() {
        return estatusTarjetaBancariaDao;
    }

    public void setEstatusTarjetaBancariaDao(EstatusTarjetaBancariaDao estatusTarjetaBancariaDao) {
        this.estatusTarjetaBancariaDao = estatusTarjetaBancariaDao;
    }

    public SolicitudCuentasDao getSolicitudCuentasDao() {
        return solicitudCuentasDao;
    }

    public void setSolicitudCuentasDao(SolicitudCuentasDao solicitudCuentasDao) {
        this.solicitudCuentasDao = solicitudCuentasDao;
    }

    public AlumnoTarjetaBancariaDao getAlumnoTarjetaDao() {
        return alumnoTarjetaDao;
    }

    public void setAlumnoTarjetaDao(AlumnoTarjetaBancariaDao alumnoTarjetaDao) {
        this.alumnoTarjetaDao = alumnoTarjetaDao;
    }

    public BitacoraTarjetaBancariaDao getBitacoraTarjetaBancariaDao() {
        return bitacoraTarjetaBancariaDao;
    }

    public void setBitacoraTarjetaBancariaDao(BitacoraTarjetaBancariaDao bitacoraTarjetaBancariaDao) {
        this.bitacoraTarjetaBancariaDao = bitacoraTarjetaBancariaDao;
    }

    public VWPresupuestoUnidadAcademicaDao getVwPresupuestoUnidadAcademicaDao() {
        return vwPresupuestoUnidadAcademicaDao;
    }

    public void setVwPresupuestoUnidadAcademicaDao(VWPresupuestoUnidadAcademicaDao vwPresupuestoUnidadAcademicaDao) {
        this.vwPresupuestoUnidadAcademicaDao = vwPresupuestoUnidadAcademicaDao;
    }

    public VWPresupuestoPeriodoDao getVwPresupuestoPeriodoDao() {
        return vwPresupuestoPeriodoDao;
    }

    public void setVwPresupuestoPeriodoDao(VWPresupuestoPeriodoDao vwPresupuestoPeriodoDao) {
        this.vwPresupuestoPeriodoDao = vwPresupuestoPeriodoDao;
    }

    public DepositosDao getDepositosDao() {
        return depositosDao;
    }

    public void setDepositosDao(DepositosDao depositosDao) {
        this.depositosDao = depositosDao;
    }

    public VWPresupuestoTipoBecaPeriodoDao getVwPresupuestoTipoBecaPeriodoDao() {
        return vwPresupuestoTipoBecaPeriodoDao;
    }

    public void setVwPresupuestoTipoBecaPeriodoDao(VWPresupuestoTipoBecaPeriodoDao vwPresupuestoTipoBecaPeriodoDao) {
        this.vwPresupuestoTipoBecaPeriodoDao = vwPresupuestoTipoBecaPeriodoDao;
    }

    public BitacoraEstatusProcesoDao getBitacoraEstatusProcesoDao() {
        return bitacoraEstatusProcesoDao;
    }

    public void setBitacoraEstatusProcesoDao(BitacoraEstatusProcesoDao bitacoraEstatusProcesoDao) {
        this.bitacoraEstatusProcesoDao = bitacoraEstatusProcesoDao;
    }

    public TipoProcesoDao getTipoProcesoDao() {
        return tipoProcesoDao;
    }

    public void setTipoProcesoDao(TipoProcesoDao tipoProcesoDao) {
        this.tipoProcesoDao = tipoProcesoDao;
    }

    public EstatusDepositoDao getEstatusDepositoDao() {
        return estatusDepositoDao;
    }

    public void setEstatusDepositoDao(EstatusDepositoDao estatusDepositoDao) {
        this.estatusDepositoDao = estatusDepositoDao;
    }

    public TarjetaBancariaDao getTarjetaBancariaDao() {
        return tarjetaBancariaDao;
    }

    public void setTarjetaBancariaDao(TarjetaBancariaDao tarjetaBancariaDao) {
        this.tarjetaBancariaDao = tarjetaBancariaDao;
    }

    public DepositoUnidadAcademicaDao getDepositoUnidadAcademicaDao() {
        return depositoUnidadAcademicaDao;
    }

    public void setDepositoUnidadAcademicaDao(DepositoUnidadAcademicaDao depositoUnidadAcademicaDao) {
        this.depositoUnidadAcademicaDao = depositoUnidadAcademicaDao;
    }

    public TipoDepositoDao getTipoDepositoDao() {
        return tipoDepositoDao;
    }

    public void setTipoDepositoDao(TipoDepositoDao tipoDepositoDao) {
        this.tipoDepositoDao = tipoDepositoDao;
    }

    public DepositoNoCoincideDao getDepositoNoCoincideDao() {
        return depositoNoCoincideDao;
    }

    public void setDepositoNoCoincideDao(DepositoNoCoincideDao depositoNoCoincideDao) {
        this.depositoNoCoincideDao = depositoNoCoincideDao;
    }

    public BitacoraAlumnoDao getBitacoraAlumnoDao() {
        return bitacoraAlumnoDao;
    }

    public void setBitacoraAlumnoDao(BitacoraAlumnoDao bitacoraAlumnoDao) {
        this.bitacoraAlumnoDao = bitacoraAlumnoDao;
    }

    public DireccionDao getDireccionDao() {
        return direccionDao;
    }

    public void setDireccionDao(DireccionDao direccionDao) {
        this.direccionDao = direccionDao;
    }

    public BitacoraOtorgamientosDao getBitacoraOtorgamientosDao() {
        return bitacoraOtorgamientosDao;
    }

    public void setBitacoraOtorgamientosDao(BitacoraOtorgamientosDao bitacoraOtorgamientosDao) {
        this.bitacoraOtorgamientosDao = bitacoraOtorgamientosDao;
    }

    public TrayectoDao getTrayectoDao() {
        return trayectoDao;
    }

    public void setTrayectoDao(TrayectoDao trayectoDao) {
        this.trayectoDao = trayectoDao;
    }

    public TransporteDao getTransporteDao() {
        return transporteDao;
    }

    public void setTransporteDao(TransporteDao transporteDao) {
        this.transporteDao = transporteDao;
    }

    public ParentescoDao getParentescoDao() {
        return parentescoDao;
    }

    public void setParentescoDao(ParentescoDao parentescoDao) {
        this.parentescoDao = parentescoDao;
    }

    public EnteroBecaDao getEnteroBecaDao() {
        return enteroBecaDao;
    }

    public void setEnteroBecaDao(EnteroBecaDao enteroBecaDao) {
        this.enteroBecaDao = enteroBecaDao;
    }

    public DiscapacidadDao getDiscapacidadDao() {
        return discapacidadDao;
    }

    public void setDiscapacidadDao(DiscapacidadDao discapacidadDao) {
        this.discapacidadDao = discapacidadDao;
    }

    public CuestionarioTransporteDao getCuestionarioTransporteDao() {
        return cuestionarioTransporteDao;
    }

    public void setCuestionarioTransporteDao(CuestionarioTransporteDao cuestionarioTransporteDao) {
        this.cuestionarioTransporteDao = cuestionarioTransporteDao;
    }

    public TransporteTrasladoDao getTransporteTrasladoDao() {
        return transporteTrasladoDao;
    }

    public void setTransporteTrasladoDao(TransporteTrasladoDao transporteTrasladoDao) {
        this.transporteTrasladoDao = transporteTrasladoDao;
    }

    public TransporteDatosFamiliaresDao getTransporteDatosFamiliaresDao() {
        return transporteDatosFamiliaresDao;
    }

    public void setTransporteDatosFamiliaresDao(TransporteDatosFamiliaresDao transporteDatosFamiliaresDao) {
        this.transporteDatosFamiliaresDao = transporteDatosFamiliaresDao;
    }

    public BitacoraPermisoCambioDao getBitacoraPermisoCambioDao() {
        return bitacoraPermisoCambioDao;
    }

    public void setBitacoraPermisoCambioDao(BitacoraPermisoCambioDao bitacoraPermisoCambioDao) {
        this.bitacoraPermisoCambioDao = bitacoraPermisoCambioDao;
    }

    public TipoPermisoCambioDao getTipoPermisoCambioDao() {
        return tipoPermisoCambioDao;
    }

    public void setTipoPermisoCambioDao(TipoPermisoCambioDao tipoPermisoCambioDao) {
        this.tipoPermisoCambioDao = tipoPermisoCambioDao;
    }

    public PadronSubesDao getPadronSubesDao() {
        return padronSubesDao;
    }

    public void setPadronSubesDao(PadronSubesDao padronSubesDao) {
        this.padronSubesDao = padronSubesDao;
    }

    public ConvocatoriaSubesDao getConvocatoriaSubesDao() {
        return convocatoriaSubesDao;
    }

    public void setConvocatoriaSubesDao(ConvocatoriaSubesDao convocatoriaSubesDao) {
        this.convocatoriaSubesDao = convocatoriaSubesDao;
    }

    public IdentificadorOtorgamientoDao getIdentificadorOtorgamientoDao() {
        return identificadorOtorgamientoDao;
    }

    public void setIdentificadorOtorgamientoDao(IdentificadorOtorgamientoDao identificadorOtorgamientoDao) {
        this.identificadorOtorgamientoDao = identificadorOtorgamientoDao;
    }

    public BecaMontoVariableDao getBecaMontoVariableDao() {
        return BecaMontoVariableDao;
    }

    public void setBecaMontoVariableDao(BecaMontoVariableDao BecaMontoVariableDao) {
        this.BecaMontoVariableDao = BecaMontoVariableDao;
    }

    public ProcesoProgramaBecaDao getProcesoProgramaBecaDao() {
        return procesoProgramaBecaDao;
    }

    public void setProcesoProgramaBecaDao(ProcesoProgramaBecaDao procesoProgramaBecaDao) {
        this.procesoProgramaBecaDao = procesoProgramaBecaDao;
    }

    public AlumnoDatosBancariosDao getAlumnoDatosBancariosDao() {
        return alumnoDatosBancariosDao;
    }

    public void setAlumnoDatosBancariosDao(AlumnoDatosBancariosDao alumnoDatosBancariosDao) {
        this.alumnoDatosBancariosDao = alumnoDatosBancariosDao;
    }

    public PadronProsperaDao getPadronProsperaDao() {
        return padronProsperaDao;
    }

    public void setPadronProsperaDao(PadronProsperaDao padronProsperaDao) {
        this.padronProsperaDao = padronProsperaDao;
    }

    public ClasificacionSolicitudDao getClasificacionSolicitudDao() {
        return clasificacionSolicitudDao;
    }

    public void setClasificacionSolicitudDao(ClasificacionSolicitudDao clasificacionSolicitudDao) {
        this.clasificacionSolicitudDao = clasificacionSolicitudDao;
    }

    public MotivoRechazoSolicitudDao getMotivoRechazoSolicitudDao() {
        return motivoRechazoSolicitudDao;
    }

    public void setMotivoRechazoSolicitudDao(MotivoRechazoSolicitudDao motivoRechazoSolicitudDao) {
        this.motivoRechazoSolicitudDao = motivoRechazoSolicitudDao;
    }

    public ListaEsperaDao getListaEsperaDao() {
        return listaEsperaDao;
    }

    public void setListaEsperaDao(ListaEsperaDao listaEsperaDao) {
        this.listaEsperaDao = listaEsperaDao;
    }

    public InegiLocalidadDao getInegiLocalidadDao() {
        return inegiLocalidadDao;
    }

    public void setInegiLocalidadDao(InegiLocalidadDao inegiLocalidadDao) {
        this.inegiLocalidadDao = inegiLocalidadDao;
    }

    public InegiTipoAsentamientoDao getInegiTipoAsentamientoDao() {
        return inegiTipoAsentamientoDao;
    }

    public void setInegiTipoAsentamientoDao(InegiTipoAsentamientoDao inegiTipoAsentamientoDao) {
        this.inegiTipoAsentamientoDao = inegiTipoAsentamientoDao;
    }

    public InegiTipoVialidadDao getInegiTipoVialidadDao() {
        return inegiTipoVialidadDao;
    }

    public void setInegiTipoVialidadDao(InegiTipoVialidadDao inegiTipoVialidadDao) {
        this.inegiTipoVialidadDao = inegiTipoVialidadDao;
    }

    public MontoMaximoIngresosDao getMontoMaximoIngresosDao() {
        return montoMaximoIngresosDao;
    }

    public void setMontoMaximoIngresosDao(MontoMaximoIngresosDao montoMaximoIngresosDao) {
        this.montoMaximoIngresosDao = montoMaximoIngresosDao;
    }

    public BecaUniversalDao getBecaUniversalDao() {
        return becaUniversalDao;
    }

    public void setBecaUniversalDao(BecaUniversalDao becaUniversalDao) {
        this.becaUniversalDao = becaUniversalDao;
    }

    public BancosDao getBancosDao() {
        return bancosDao;
    }

    public void setBancosDao(BancosDao bancosDao) {
        this.bancosDao = bancosDao;
    }

    public NotificacionesDao getNotificacionesDao() {
        return notificacionesDao;
    }

    public void setNotificacionesDao(NotificacionesDao notificacionesDao) {
        this.notificacionesDao = notificacionesDao;
    }

    public TipoNotificacionDao getTipoNotificacionDao() {
        return tipoNotificacionDao;
    }

    public void setTipoNotificacionDao(TipoNotificacionDao tipoNotificacionDao) {
        this.tipoNotificacionDao = tipoNotificacionDao;
    }

    public NotificacionesRolDao getNotificacionRolDao() {
        return notificacionRolDao;
    }

    public void setNotificacionRolDao(NotificacionesRolDao notificacionRolDao) {
        this.notificacionRolDao = notificacionRolDao;
    }

    public DaoBase<ErroresBanamex, BigDecimal> getErroresBanamexDao() {
        return erroresBanamexDao;
    }

    public void setErroresBanamexDao(DaoBase<ErroresBanamex, BigDecimal> erroresBanamexDao) {
        this.erroresBanamexDao = erroresBanamexDao;
    }

    public DaoBase<BecaExterna, BigDecimal> getBecaExternaDao() {
        return becaExternaDao;
    }

    public void setBecaExternaDao(DaoBase<BecaExterna, BigDecimal> becaExternaDao) {
        this.becaExternaDao = becaExternaDao;
    }

    public OtorgamientoExternoDao getOtorgamientoExternoDao() {
        return otorgamientoExternoDao;
    }

    public void setOtorgamientoExternoDao(OtorgamientoExternoDao otorgamientoExternoDao) {
        this.otorgamientoExternoDao = otorgamientoExternoDao;
    }

    public DaoBase<CicloEscolar, BigDecimal> getCicloEscolarDao() {
        return cicloEscolarDao;
    }

    public void setCicloEscolarDao(DaoBase<CicloEscolar, BigDecimal> cicloEscolarDao) {
        this.cicloEscolarDao = cicloEscolarDao;
    }

    public BitacoraOtorgamientoExternoDao getBitacoraOtorgamientoExternoDao() {
        return bitacoraOtorgamientoExternoDao;
    }

    public void setBitacoraOtorgamientoExternoDao(BitacoraOtorgamientoExternoDao bitacoraOtorgamientoExternoDao) {
        this.bitacoraOtorgamientoExternoDao = bitacoraOtorgamientoExternoDao;
    }

    public CensoSaludDao getCensoSaludDao() {
        return censoSaludDao;
    }

    public void setCensoSaludDao(CensoSaludDao censoSaludDao) {
        this.censoSaludDao = censoSaludDao;
    }

    public SeguimientoBecariosDao getSeguimientoBecariosDao() {
        return seguimientoBecarios;
    }

    public void setSeguimientoBecariosDao(SeguimientoBecariosDao seguimientoBecarios) {
        this.seguimientoBecarios = seguimientoBecarios;
    }

    public SeguimientoBecariosDao getSeguimientoBecarios() {
        return seguimientoBecarios;
    }

    public void setSeguimientoBecarios(SeguimientoBecariosDao seguimientoBecarios) {
        this.seguimientoBecarios = seguimientoBecarios;
    }

    public SolicitudReconsideracionDao getSolicitudReconsideracionDao() {
        return solicitudReconsideracionDao;
    }

    public void setSolicitudReconsideracionDao(SolicitudReconsideracionDao solicitudReconsideracionDao) {
        this.solicitudReconsideracionDao = solicitudReconsideracionDao;
    }

    public EstatusReconsideracionDao getEstatusReconsideracionDao() {
        return estatusReconsideracionDao;
    }

    public void setEstatusReconsideracionDao(EstatusReconsideracionDao estatusReconsideracionDao) {
        this.estatusReconsideracionDao = estatusReconsideracionDao;
    }

    public TipoInconformidadReconsideracionDao getTipoInconformidadReconsideracionDao() {
        return tipoInconformidadReconsideracionDao;
    }

    public void setTipoInconformidadReconsideracionDao(TipoInconformidadReconsideracionDao tipoInconformidadReconsideracionDao) {
        this.tipoInconformidadReconsideracionDao = tipoInconformidadReconsideracionDao;
    }

}
