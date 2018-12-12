/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.becasipn.business;

import com.becasipn.actions.Mensajes;
import com.becasipn.persistence.model.Rol;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.service.Service;
import com.becasipn.util.Ambiente;
import java.math.BigDecimal;
import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.velocity.app.VelocityEngine;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Funciones básicas de la lógica de negocios
 *
 * @author Patricia Benitez
 */
public class BaseBO implements Mensajes {
    
    protected Logger log = LogManager.getLogger(this.getClass().getName());
    
    protected Ambiente ambiente = new Ambiente();
    protected Service service;

    /**
     * Constructor Default
     */
    public BaseBO() {
    }

    /**
     * Constructor que recibe un objeto de tipo <code>service</code>
     *
     * @param service
     * @see mx.gob.sep.pead.service.Service
     */
    public BaseBO(Service service) {
        this.service = service;
        ambiente.setService(service);
    }
    
    public Usuario getUsuarioByFolio(String folio, Service service) {
        Usuario usuario = null;
        
        if (folio != null && !folio.isEmpty() && service != null) {
            usuario = service.getUsuarioDao().findById(folio);
        }
        
        return usuario;
    }

    /**
     * Verifica que el usuario sea un administrativo
     *
     * @return true si es administrativo, false de lo contrario.
     */
    protected boolean isAdministrativo() {
        Collection<GrantedAuthority> gas = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (GrantedAuthority ga : gas) {
            if (ga.getAuthority().equals("ROLE_ALUMNO")) {
                return false;
            }
            if (ga.getAuthority().equals("ROLE_TUTOR")) {
                return false;
            }
            if (ga.getAuthority().equals("ROLE_FACILITADOR")) {
                return false;
            }
            if (ga.getAuthority().equals("ROLE_ANONYMOUS")) {
                return false;
            }
        }
        return true;
    }
  

    // Une html de la plantilla, con variables
    public  final String creaTexto(String ruta, Map model) {
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty("resource.loader", "class");
        velocityEngine.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        velocityEngine.setProperty("class.resource.loader.cache", "true");
        try {
            velocityEngine.init();
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(BaseBO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, ruta, "UTF-8", model);
    }
    
    protected Rol getRolAlto() {
        Collection<GrantedAuthority> gas = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (GrantedAuthority ga : gas) {
            if (ga.getAuthority().equals("ROLE_ADMIN")) {
                return service.getRolDao().findById(new BigDecimal(1));
            }
            if (ga.getAuthority().equals("ROLE_JEFEBECAS")) {
                return service.getRolDao().findById(new BigDecimal(3));
            }
            if (ga.getAuthority().equals("ROLE_ANALISTABECAS")) {
                return service.getRolDao().findById(new BigDecimal(4));
            }
            if (ga.getAuthority().equals("ROLE_JEFEADMINISTRATIVO")) {
                return service.getRolDao().findById(new BigDecimal(7));
            }
            if (ga.getAuthority().equals("ROLE_ANALISTAADMINISTRATIVO")) {
                return service.getRolDao().findById(new BigDecimal(8));
            }
            if (ga.getAuthority().equals("ROLE_RESPONSABLE_UA")) {
                return service.getRolDao().findById(new BigDecimal(5));
            }
            if (ga.getAuthority().equals("ROLE_FUNCIONARIO_UA")) {
                return service.getRolDao().findById(new BigDecimal(6));
            }
            if (ga.getAuthority().equals("ROLE_ALUMNO")) {
                return service.getRolDao().findById(new BigDecimal(2));
            }
        }
        return null;
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
}
