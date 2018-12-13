package com.becasipn.actions;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.logging.commons.CommonsLogger;
import com.becasipn.business.BaseBO;
import com.becasipn.business.PeriodoBO;
import com.becasipn.exception.LoginException;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.Configuracion;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.PersonalAdministrativo;
import com.becasipn.persistence.model.Rol;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.service.Service;
import com.becasipn.util.Ambiente;
import com.becasipn.util.AmbienteEnums;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Accion base con las propiedades requeridas por todas las acciones del
 * sistema.
 *
 * @author Patricia Benitez
 */
public abstract class BaseAction extends ActionSupport implements Mensajes {

    private static final long serialVersionUID = -4001850137117080470L;
    private Ambiente ambiente = null;
    private Service daos = null;
    private String ubicacion;
    private Boolean ese = Boolean.FALSE;
    private Boolean estatusAlumno = Boolean.FALSE;
    private int notificacionesRolSize = 0;

    /**
     * Inicializa el objeto <code>BaseAction</code>.
     */
    public BaseAction() {
        LOG = new CommonsLogger(new Log4JLogger(LogManager.getLogger(this.getClass().getName())));
        setVariablesPersonalizadas();
        if (SecurityContextHolder.getContext().getAuthentication().getName() != null
                && !SecurityContextHolder.getContext().getAuthentication().getName().equals("")
                && !SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")) {
            HttpServletRequest request = ServletActionContext.getRequest();
            String action = request.getRequestURI();
            getDaos();
            ActionContext.getContext().getSession().put("urlAction", ubicacion);
            // OBTENIENDO LA RUTA EN LA QUE SE ENCUENTRA                                           
            ActionContext.getContext().getSession().put("action", action);
            ese = (Boolean) ActionContext.getContext().getSession().get("ese") == null ? Boolean.FALSE : (Boolean) ActionContext.getContext().getSession().get("ese");
            estatusAlumno = (Boolean) ActionContext.getContext().getSession().get("estatusAlumno") == null ? Boolean.FALSE : (Boolean) ActionContext.getContext().getSession().get("estatusAlumno");
            if (ActionContext.getContext().getSession().get("usuario") == null) {
                try {
                    BaseBO BO = new BaseBO();
                    String folio = SecurityContextHolder.getContext().getAuthentication().getName();
                    Usuario usuario = BO.getUsuarioByFolio(folio, daos);
                    ActionContext.getContext().getSession().put("usuario", usuario);
                    ActionContext.getContext().getSession().put("folio", usuario.getUsuario());
                    ActionContext.getContext().getSession().put("rol", getRol(getRol()));
                    setPrivilegio(usuario);
                    String[] datos = getDatosPersonales();
                    ActionContext.getContext().getSession().put("nombreCompleto", datos[0]);
                    ActionContext.getContext().getSession().put("unidadAcademica", datos[1]);
                    //CONDICIONANDO LA BUSQUEDA DE MENU A URLS QUE NO CONTENGAN LA PALABRA "AJAX"
                    //DEBIDO A QUE LOS QUE CONTIENEN ESA PALABRA SON ACTIONS QUE REALIZAN LA 
                    //BUSQUEDAD DE DATOS, Y NO RECARGAN LA PÁGINA, POR LO CUAL NO ES NECESARIO
                    //QUE SE HAGA LA BUSQUEDA DEL MENU
                    if (action.indexOf("ajax") <= 0) {
                        setMenu(usuario);
                    }
                } catch (LoginException le) {
                    le.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                    ActionContext.getContext().getSession().put("urlAction", "NO IDENTIFICADO");
                }

            }

            if (isJefeAdministrativo() || isJefe()) {
                //Establece alertas de ordenes de deposito pendientes de revisar
            }

            if (action.indexOf("ajax") <= 0) {
                setMenuActivo(action);
            }

        }
    }

    private void setMenuActivo(String action) {
        StringBuilder menuActivo = new StringBuilder("");
        String menuOriginal = String.valueOf(ActionContext.getContext().getSession().get("menuOriginal"));
        ubicacion = daos.getMenuDao().getNombreModulo(action, menuActivo);
        if (menuOriginal != null && !menuActivo.toString().equals("")) {
            Document doc = Jsoup.parseBodyFragment(menuOriginal);
            Elements liHijo = doc.select("ul[class='nav nav-pills nav-stacked']>li>ul>li[id='" + menuActivo.toString() + "']");
            if (ActionContext.getContext().getSession().get("ese") != null
                    && ActionContext.getContext().getSession().get("estatusAlumno") != null
                    && ((Boolean) ActionContext.getContext().getSession().get("ese") == false || (Boolean) ActionContext.getContext().getSession().get("estatusAlumno") == false)) {
                liHijo = doc.select("ul[class='nav nav-pills nav-stacked']>li[id='" + menuActivo.toString() + "']");
            }
            if (liHijo.size() > 0) {
                Element liPapa = liHijo.parents().get(1);
                liHijo.addClass("active");
                liPapa.addClass("active");
                ActionContext.getContext().getSession().put("menu", doc.toString());
            }
        }
    }

    public void setMenu(Usuario usuario) throws LoginException {
        String menuStr = "";
        if (isAlumno()) {
            PeriodoBO bo = new PeriodoBO(getDaos());
            Periodo periodo = bo.getPeriodoActivo();
            Alumno alumno = new Alumno();
            String numeroDeBoleta = SecurityContextHolder.getContext().getAuthentication().getName();
            try {
                List<Alumno> listAlumno = getDaos().getAlumnoDao().getByBoleta(numeroDeBoleta);
                alumno = listAlumno.get(0);
            } catch (Exception e) {
                ActionContext.getContext().getSession().put("menu", "<i>Solicita la verificación de tu usuario en el sistema, debido a que no es posible mostrar las opciones del menú.</i>");
                throw new LoginException("El usuario (" + numeroDeBoleta + ") que intenta ingresar no tiene su registro correspondiente en la tabla de alumnos.");
            }
        }
        menuStr = getDaos().getRelacionMenuRolesDao().findURLByRols(usuario.getPrivilegios());
        ActionContext.getContext().getSession().put("menu", menuStr);
        ActionContext.getContext().getSession().put("menuOriginal", menuStr);
    }

    /**
     *
     * Obtiene de los objects de Ambiente los valores de la configuracion de la
     * aplicacion
     */
    public final void setVariablesPersonalizadas() {
        try {
            List<Configuracion> configuracionList = getAmbiente().getConfiguracion();
            for (Configuracion c : configuracionList) {
                ActionContext.getContext().getApplication().put(c.getPropiedad(), c.getValor());
            }
        } catch (Exception e) {
            e.printStackTrace();
            // VERSION ESTABLECIENDO VARIABLES DE AMBIENTE
        }
    }

    public final void reloadVariablesPersonalizadas() {
        try {
            List<Configuracion> configuracionList = getAmbiente().reloadConfiguracion();
            for (Configuracion c : configuracionList) {
                ActionContext.getContext().getApplication().remove(c.getPropiedad());
                ActionContext.getContext().getApplication().put(c.getPropiedad(), c.getValor());
            }
        } catch (Exception e) {
            e.printStackTrace();
            // VERSION ESTABLECIENDO VARIABLES DE AMBIENTE
        }
    }

    /**
     * Verifica que exista un usuario autentificado.
     *
     * @return true si el usuario se encuentra autentificado, false de lo
     * contrario.
     */
    protected boolean isAuthenticated() {
        Collection<GrantedAuthority> gas = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (GrantedAuthority ga : gas) {
            if (ga.getAuthority().equals("ROLE_ANONYMOUS")) {
                return false;
            }
        }
        return true;
    }

    protected void setPrivilegio(Usuario usuario) {
        String privilegio = getDaos().getUsuarioPrivilegioDao().findByUsuario(usuario.getId()).getPrivilegio().getDescripcion();
        ActionContext.getContext().getSession().put("privilegio", privilegio);
    }

    protected List<Rol> getRol() {
        Collection<GrantedAuthority> gas = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        List<Rol> roles = new ArrayList<Rol>();
        for (GrantedAuthority ga : gas) {
            if (ga.getAuthority().equals("ROLE_ADMIN")) {
                Rol r = getDaos().getRolDao().findById(new BigDecimal(1));
                roles.add(r);
            }
            if (ga.getAuthority().equals("ROLE_ALUMNO")) {
                Rol r = getDaos().getRolDao().findById(new BigDecimal(2));
                roles.add(r);
            }
            if (ga.getAuthority().equals("ROLE_JEFEBECAS")) {
                Rol r = getDaos().getRolDao().findById(new BigDecimal(3));
                roles.add(r);
            }
            if (ga.getAuthority().equals("ROLE_ANALISTABECAS")) {
                Rol r = getDaos().getRolDao().findById(new BigDecimal(4));
                roles.add(r);
            }
            if (ga.getAuthority().equals("ROLE_RESPONSABLE_UA")) {
                Rol r = getDaos().getRolDao().findById(new BigDecimal(5));
                roles.add(r);
            }
            if (ga.getAuthority().equals("ROLE_FUNCIONARIO_UA")) {
                Rol r = getDaos().getRolDao().findById(new BigDecimal(6));
                roles.add(r);
            }
            if (ga.getAuthority().equals("ROLE_JEFEADMINISTRATIVO")) {
                Rol r = getDaos().getRolDao().findById(new BigDecimal(7));
                roles.add(r);
            }
            if (ga.getAuthority().equals("ROLE_ANALISTAADMINISTRATIVO")) {
                Rol r = getDaos().getRolDao().findById(new BigDecimal(8));
                roles.add(r);
            }
            if (ga.getAuthority().equals("ROLE_EJECUTIVO")) {
                Rol r = getDaos().getRolDao().findById(new BigDecimal(9));
                roles.add(r);
            }
            if (ga.getAuthority().equals("ROLE_CONSULTA")) {
                Rol r = getDaos().getRolDao().findById(new BigDecimal(29));
                roles.add(r);
            }
            if (ga.getAuthority().equals("ROLE_ESTADISTICAS")) {
                Rol r = getDaos().getRolDao().findById(new BigDecimal(10));
                roles.add(r);
            }
        }
        return roles;
    }

    protected String getRol(List<Rol> roles) {
        StringBuilder rolesStr = new StringBuilder();
        for (Rol r : roles) {
            rolesStr.append(r.getClave()).append(",");
        }

        return rolesStr.toString().length() == 0 ? "" : rolesStr.toString().substring(0, rolesStr.toString().length() - 1);
    }

    protected Rol getRolAlto() {
        Collection<GrantedAuthority> gas = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (GrantedAuthority ga : gas) {
            if (ga.getAuthority().equals("ROLE_ADMIN")) {
                return getDaos().getRolDao().findById(new BigDecimal(1));
            }
            if (ga.getAuthority().equals("ROLE_JEFEBECAS")) {
                return getDaos().getRolDao().findById(new BigDecimal(3));
            }
            if (ga.getAuthority().equals("ROLE_ANALISTABECAS")) {
                return getDaos().getRolDao().findById(new BigDecimal(4));
            }
            if (ga.getAuthority().equals("ROLE_JEFEADMINISTRATIVO")) {
                return getDaos().getRolDao().findById(new BigDecimal(7));
            }
            if (ga.getAuthority().equals("ROLE_ANALISTAADMINISTRATIVO")) {
                return getDaos().getRolDao().findById(new BigDecimal(8));
            }
            if (ga.getAuthority().equals("ROLE_RESPONSABLE_UA")) {
                return getDaos().getRolDao().findById(new BigDecimal(5));
            }
            if (ga.getAuthority().equals("ROLE_FUNCIONARIO_UA")) {
                return getDaos().getRolDao().findById(new BigDecimal(6));
            }
            if (ga.getAuthority().equals("ROLE_ALUMNO")) {
                return getDaos().getRolDao().findById(new BigDecimal(2));
            }
        }
        return null;
    }

    /**
     * Verifica que el usuario sea un administrador.
     *
     * @return true si es un admistrador, false de lo contrario.
     */
    protected boolean isAdministrator() {
        Collection<GrantedAuthority> gas = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (GrantedAuthority ga : gas) {
            if (ga.getAuthority().equals("ROLE_ADMIN")) {
                return true;
            }
        }
        return false;
    }

    protected boolean isAlumno() {
        Collection<GrantedAuthority> gas = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (GrantedAuthority ga : gas) {
            if (ga.getAuthority().equals("ROLE_ALUMNO")) {
                return true;
            }
        }
        return false;
    }

    protected boolean isAnalista() {
        Collection<GrantedAuthority> gas = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (GrantedAuthority ga : gas) {
            if (ga.getAuthority().equals("ROLE_ANALISTABECAS")) {
                return true;
            }
        }
        return false;
    }

    protected boolean isAnalistaAdmin() {
        Collection<GrantedAuthority> gas = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (GrantedAuthority ga : gas) {
            if (ga.getAuthority().equals("ROLE_ANALISTAADMINISTRATIVO")) {
                return true;
            }
        }
        return false;
    }

    protected boolean isResponsable() {
        Collection<GrantedAuthority> gas = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (GrantedAuthority ga : gas) {
            if (ga.getAuthority().equals("ROLE_RESPONSABLE_UA")) {
                return true;
            }
        }
        return false;
    }

    protected boolean isFuncionario() {
        Collection<GrantedAuthority> gas = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (GrantedAuthority ga : gas) {
            if (ga.getAuthority().equals("ROLE_FUNCIONARIO_UA")) {
                return true;
            }
        }
        return false;
    }

    protected boolean isJefe() {
        Collection<GrantedAuthority> gas = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (GrantedAuthority ga : gas) {
            if (ga.getAuthority().equals("ROLE_JEFEBECAS")) {
                return true;
            }
        }
        return false;
    }

    protected boolean isJefeAdministrativo() {
        Collection<GrantedAuthority> gas = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (GrantedAuthority ga : gas) {
            if (ga.getAuthority().equals("ROLE_JEFEADMINISTRATIVO")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Valida si el usuario está autenticado y obtiene los privilegios del
     * usuario y obtiene las opciones (ligas) del menú del usuario con base en
     * su rol
     *
     * @return Un string con formato HTML de las opciones de menú disponibles
     * para el usuario con base en la lista de roles asociados al usuario
     */
    public String getMenuUsuario() {
        if (!isAuthenticated()) {
            return "";
        }
        Usuario usuario = getDaos().getUsuarioDao().findById(SecurityContextHolder.getContext().getAuthentication().getName());
        return getDaos().getRelacionMenuRolesDao().findURLByRols(usuario.getPrivilegios());
    }

    public String[] getDatosPersonales() {
        String fullname = "NOMBRE DE USUARIO";
        String uacademica = "S/I";

        if (isAlumno()) {
            List<Alumno> aList = getDaos().getAlumnoDao().findBy("boleta", String.valueOf(SecurityContextHolder.getContext().getAuthentication().getName()), Boolean.TRUE);
            if (aList != null && !aList.isEmpty()) {
                Alumno a = aList.get(0);
                DatosAcademicos da = getDaos().getDatosAcademicosDao().datosPorPeriodo(a.getId(), getDaos().getPeriodoDao().getPeriodoActivo().getId());
                fullname = a.getFullName();
                uacademica = a == null
                        ? fullname
                        : da == null ? "" : (da.getUnidadAcademica() == null
                                ? ""
                                : da.getUnidadAcademica().getNombreCorto());
            }
        } else {
            Usuario u = (Usuario) ActionContext.getContext().getSession().get("usuario");
            PersonalAdministrativo pa = getDaos().getPersonalAdministrativoDao().findByUsuario(u.getId());
            fullname = pa == null ? fullname : pa.getNombreCompleto();
            uacademica = pa == null ? fullname : pa.getUnidadAcademica().getNombreCorto();
        }

        String[] datos = new String[2];
        datos[0] = fullname;
        datos[1] = uacademica;

        return datos;
    }

    /**
     * Obtiene el ambiente donde estan listas y otros objetos comunes.
     *
     * @return ambiente general.
     * @throws Exception
     * @see Ambiente
     */
    public Ambiente getAmbiente() throws Exception {
        if (ambiente == null) {
            try {
                ApplicationContext applicationContext
                        = WebApplicationContextUtils.getWebApplicationContext(ServletActionContext.getServletContext());
                ambiente = (Ambiente) applicationContext.getBean("ambiente");
            } catch (BeansException beansException) {
                beansException.printStackTrace();
            }
        }
        return ambiente;
    }

    /**
     * Obtiene los DAOs, donde podremos encontrar el manejo de persitencia de
     * los objetos.
     *
     * @return service para el manejo de persistencia.
     */
    public final Service getDaos() {
        if (daos == null) {
            try {
                ApplicationContext applicationContext
                        = WebApplicationContextUtils.getWebApplicationContext(ServletActionContext.getServletContext());
                daos = (Service) applicationContext.getBean("service");
            } catch (BeansException beansException) {
                beansException.printStackTrace();
            }
        }
        return daos;
    }

    /**
     * Obtiene el ambiente donde estan listas pertenecientes a un catalogo.
     *
     * @return ambiente donde estan listas pertenecientes a un catalogo.
     */
    public AmbienteEnums getService() {
        return AmbienteEnums.getInstance();
    }

    /**
     * Obtiene el nombre de la acción que se está ejecutando.
     *
     * @return Nombre de acción que se ejecuta.
     */
    public String getActionName() {
        LOG.debug("Obteniedo el Accion Name:" + ActionContext.getContext().getName());
        return ActionContext.getContext().getName();
    }

    /**
     * Accion Base: En caso de que no se sobreescriba este metodo, la ejecución
     * directa de dicha acción generará un error.
     *
     * @return un string representando el resultado lógico de la ejecución de la
     * acción : ERROR
     */
    @Override
    public String execute() {
        LOG.error("Accion %s ejecutada directamente", this.getClass().getName());
        return ERROR;
    }

    /**
     * Acción Cancelar: Cancela la ejecución de la acción.
     *
     * @return un string representando el resultado lógico de la ejecución de la
     * acción: cancel
     */
    public String accionCancelar() {
        return "cancel";
    }

    /**
     * Acción Cancelar: Cancela la ejecución de la acción y regresa al menú
     * principal
     *
     * @return un string representando el resultado lógico de la ejecución de la
     * acción: cancel
     */
    public String cancelar() {
        return "cancel";
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Boolean getEse() {
        return ese;
    }

    public void setEse(Boolean ese) {
        this.ese = ese;
    }

    public Boolean getEstatusAlumno() {
        return estatusAlumno;
    }

    public void setEstatusAlumno(Boolean estatusAlumno) {
        this.estatusAlumno = estatusAlumno;
    }

    public int getNotificacionesRolSize() {
        return notificacionesRolSize;
    }

    public void setNotificacionesRolSize(int notificacionesRolSize) {
        this.notificacionesRolSize = notificacionesRolSize;
    }

}
