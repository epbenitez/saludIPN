package com.becasipn.service;

import com.becasipn.persistence.dao.AlumnoDao;
import com.becasipn.persistence.dao.CargoDao;
import com.becasipn.persistence.dao.CarreraDao;
import com.becasipn.persistence.dao.ConfiguracionDao;
import com.becasipn.persistence.dao.CuestionarioDao;
import com.becasipn.persistence.dao.CuestionarioPreguntaRespuestaDao;
import com.becasipn.persistence.dao.CuestionarioPreguntaTipoDao;
import com.becasipn.persistence.dao.CuestionarioPreguntasDao;
import com.becasipn.persistence.dao.CuestionarioRespuestasDao;
import com.becasipn.persistence.dao.CuestionarioRespuestasUsuarioDao;
import com.becasipn.persistence.dao.CuestionarioSeccionDao;
import com.becasipn.persistence.dao.DaoBase;
import com.becasipn.persistence.dao.DireccionDao;
import com.becasipn.persistence.dao.EntidadFederativaDao;
import com.becasipn.persistence.dao.PaisDao;
import com.becasipn.persistence.dao.PeriodoDao;
import com.becasipn.persistence.dao.PersonalAdministrativoDao;
import com.becasipn.persistence.dao.CensoSaludDao;
import com.becasipn.persistence.dao.RolDao;
import com.becasipn.persistence.dao.DatosAcademicosDao;
import com.becasipn.persistence.dao.UnidadAcademicaDao;
import com.becasipn.persistence.dao.UsuarioDao;
import com.becasipn.persistence.dao.UsuarioPrivilegioDao;
import com.becasipn.persistence.model.EstadoCivil;
import com.becasipn.persistence.model.LocalidadColonia;
import com.becasipn.persistence.model.Nacionalidad;
import com.becasipn.persistence.dao.InegiLocalidadDao;
import com.becasipn.persistence.dao.InegiTipoAsentamientoDao;
import com.becasipn.persistence.dao.InegiTipoVialidadDao;
import com.becasipn.persistence.dao.RelacionMenuRolesDao;
import com.becasipn.persistence.model.CicloEscolar;
import com.becasipn.persistence.model.RelacionMenuRoles;
import java.math.BigDecimal;

public class Service {

    private UsuarioDao usuarioDao;
    private RelacionMenuRolesDao relacionMenuRolesDao;
    private EntidadFederativaDao entidadFederativaDao;
    private PaisDao paisDao;
    private RolDao rolDao;
    private DaoBase<Nacionalidad, Long> nacionalidadDao;
    //   private NacionalidadDao nacionalidadDao;
    //--------------------------------------------------------------------------
    // CONFIGURACION DEL SISTEMA
    private ConfiguracionDao configuracionDao;
    private UsuarioPrivilegioDao usuarioPrivilegioDao;
    //--------------------------------------------------------------------------
    // REGISTRO DE ALUMNOS
    private AlumnoDao alumnoDao;
    private DaoBase<EstadoCivil, BigDecimal> estadoCivilDao;
    //private DaoBase<UnidadAcademica, Long> unidadAcademicaDao;
    private CarreraDao carreraDao;
    private DaoBase<LocalidadColonia, Long> localidadColoniaDao;
    //CUESTIONARIOS
    private CensoSaludDao censoSaludDao;
    private CuestionarioDao cuestionarioDao;
    private CuestionarioPreguntaRespuestaDao cuestionarioPreguntaRespuestaDao;
    private CuestionarioPreguntaTipoDao cuestionarioPreguntaTipoDao;
    private CuestionarioPreguntasDao cuestionarioPreguntasDao;
    private CuestionarioRespuestasDao cuestionarioRespuestasDao;
    private CuestionarioRespuestasUsuarioDao cuestionarioRespuestasUsuarioDao;
    private CuestionarioSeccionDao cuestionarioSeccionDao;
    private PeriodoDao periodoDao;
    private DatosAcademicosDao datosAcademicosDao;
    private UnidadAcademicaDao unidadAcademicaDao;
    private CargoDao cargoDao;
    private PersonalAdministrativoDao personalAdministrativoDao;
    private DireccionDao direccionDao;
    private InegiLocalidadDao inegiLocalidadDao;
    private InegiTipoAsentamientoDao inegiTipoAsentamientoDao;
    private InegiTipoVialidadDao inegiTipoVialidadDao;

    //Notificaciones
    private DaoBase<CicloEscolar, BigDecimal> cicloEscolarDao;

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

    public DaoBase<EstadoCivil, BigDecimal> getEstadoCivilDao() {
        return estadoCivilDao;
    }

    public void setEstadoCivilDao(DaoBase<EstadoCivil, BigDecimal> estadoCivilDao) {
        this.estadoCivilDao = estadoCivilDao;
    }

    public CarreraDao getCarreraDao() {
        return carreraDao;
    }

    public void setCarreraDao(CarreraDao carreraDao) {
        this.carreraDao = carreraDao;
    }

    public DaoBase<LocalidadColonia, Long> getLocalidadColoniaDao() {
        return localidadColoniaDao;
    }

    public void setLocalidadColoniaDao(DaoBase<LocalidadColonia, Long> localidadColoniaDao) {
        this.localidadColoniaDao = localidadColoniaDao;
    }

    public CensoSaludDao getCensoSaludDao() {
        return censoSaludDao;
    }

    public void setCensoSaludDao(CensoSaludDao censoSaludDao) {
        this.censoSaludDao = censoSaludDao;
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

    public PeriodoDao getPeriodoDao() {
        return periodoDao;
    }

    public void setPeriodoDao(PeriodoDao periodoDao) {
        this.periodoDao = periodoDao;
    }

    public UnidadAcademicaDao getUnidadAcademicaDao() {
        return unidadAcademicaDao;
    }

    public void setUnidadAcademicaDao(UnidadAcademicaDao unidadAcademicaDao) {
        this.unidadAcademicaDao = unidadAcademicaDao;
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

    public DireccionDao getDireccionDao() {
        return direccionDao;
    }

    public void setDireccionDao(DireccionDao direccionDao) {
        this.direccionDao = direccionDao;
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

    public DaoBase<CicloEscolar, BigDecimal> getCicloEscolarDao() {
        return cicloEscolarDao;
    }

    public void setCicloEscolarDao(DaoBase<CicloEscolar, BigDecimal> cicloEscolarDao) {
        this.cicloEscolarDao = cicloEscolarDao;
    }

    public AlumnoDao getAlumnoDao() {
        return alumnoDao;
    }

    public void setAlumnoDao(AlumnoDao alumnoDao) {
        this.alumnoDao = alumnoDao;
    }

    public DatosAcademicosDao getDatosAcademicosDao() {
        return datosAcademicosDao;
    }

    public void setDatosAcademicosDao(DatosAcademicosDao datosAcademicosDao) {
        this.datosAcademicosDao = datosAcademicosDao;
    }

    public RelacionMenuRolesDao getRelacionMenuRolesDao() {
        return relacionMenuRolesDao;
    }

    public void setRelacionMenuRolesDao(RelacionMenuRolesDao relacionMenuRolesDao) {
        this.relacionMenuRolesDao = relacionMenuRolesDao;
    }
    

}
