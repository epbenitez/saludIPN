/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.becasipn.business;

import com.becasipn.actions.Mensajes;
import com.becasipn.persistence.model.Depositos;
import com.becasipn.persistence.model.Rol;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.service.Service;
import com.becasipn.util.Ambiente;
import com.becasipn.util.AsyncMailSender;
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
    
    private AsyncMailSender mailSender;

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

    /**
     * Obtiene la interfaz JavaMailSender, para la creación correos
     * electrónicos.
     *
     * @return manejador del envío de emails.
     */
    private AsyncMailSender getMailSender() {
        if (mailSender == null) {
            try {
                ApplicationContext applicationContext
                        = WebApplicationContextUtils.getWebApplicationContext(ServletActionContext.getServletContext());
                mailSender = (AsyncMailSender) applicationContext.getBean("mailSender");
            } catch (BeansException beansException) {
                beansException.printStackTrace();
            }
        }
        return mailSender;
    }

    /**
     * Envía un correo electrónico a una lista de destinatarios junto con un
     * archivo anexo.
     *
     * @param to Lista correos electrónicos de los destinatarios (Para).
     * @param subject Asunto del correo electrónico.
     * @param body Cuerpo del correo electrónico.
     * @param attach Archivo anexo.
     * @throws MessagingException
     */
    protected final void sendEmail(String[] to, String subject, String body, InputStreamSource attach) throws MessagingException {
        AsyncMailSender sender = getMailSender();
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("sibec@ipn.mx");
        helper.setTo(to);
//        helper.setBcc(to);
        helper.setSubject(subject);
        helper.setText("<html><body>" + body + "</body></html>", true);
        sender.send(message);

//        SimpleMailMessage smm = new SimpleMailMessage();
//        smm.setSubject(subject);
//        smm.setTo(to);
//        smm.setFrom("sibec@ipn.mx");
//        smm.setText(body);
//        sender.send(smm);
    }
    
    /**
     * Manda correo de forma asíncrona a cada alumno del depósito correspondiente
     * En caso de reportar un error, se establece el campo fechanotificacion en nulo
     * para el depósito indicado.
     * @param d
     * @param correo
     * @param model 
     */
    protected final void sendEmail(Depositos d,String correo, Map model) {
        AsyncMailSender sender = getMailSender();
        MimeMessage message = sender.createMimeMessage();
        String body = creaTexto("velocity/EnvioCorreos/mail.vm", model);
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("sibec@ipn.mx");
            helper.setTo(correo);
            helper.setSubject(model.get("asunto").toString());
            helper.setText("<html><body>" + body + "</body></html>", true);
            sender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            d.setFechaDeposito(null);
            service.getDepositosDao().update(d);
        }
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
    
    protected final void sendEmailSync(String correo, Map model) throws MessagingException {
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(ServletActionContext.getServletContext());
        JavaMailSenderImpl mailSenderSync = (JavaMailSenderImpl) applicationContext.getBean("mailSenderSync");
        MimeMessage message = mailSenderSync.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        String text = creaTexto("velocity/EnvioCorreos/mail.vm", model);
        
        helper.setFrom("sibec@ipn.mx");
        helper.setTo(correo);
        helper.setSubject(model.get("asunto").toString());
        helper.setText(text, true);
        if (model.containsKey("nombreArchivo")) {
            helper.addAttachment(model.get("nombreArchivo").toString(), (File) model.get("archivo"));
        }
        
        mailSenderSync.send(message);
    }

    /**
     * Envía un correo electrónico al destinatario deseado.
     *
     * @param to Correo electrónico del destinatario (Para).
     * @param subject Asunto del correo electrónico.
     * @param body Cuerpo del correo electrónico.
     * @throws MessagingException
     */
    protected final void sendEmail(String to, String subject, String body) throws MessagingException {
        String[] toArray = {};
        if (to != null && !to.isEmpty()) {
            StringTokenizer st = new StringTokenizer(to, ",");
            toArray = new String[st.countTokens()];
            int i = 0;
            while (st.hasMoreTokens()) {
                toArray[i] = st.nextToken().trim();
                i++;
            }
        }
        sendEmail(toArray, subject, body);
    }

    /**
     * Envía un correo electrónico a una lista de destinatarios.
     *
     * @param to Lista correos electrónicos de los destinatarios (Para).
     * @param subject Asunto del correo electrónico.
     * @param body Cuerpo del correo electrónico.
     * @throws MessagingException
     */
    protected final void sendEmail(String[] to, String subject, String body) throws MessagingException {
        sendEmail(to, subject, body, null);
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
