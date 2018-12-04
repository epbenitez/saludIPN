package com.becasipn.actions;

import com.becasipn.util.AsyncMailSender;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author Patricia Benítez
 */
public class SendMailTLS {

    private AsyncMailSender mailSender;

    public static void main(String[] args) {
        try {
            final String username = "info@becas.ipn";
            final String password = "becasipn";
            SendMailTLS s = new SendMailTLS();
            AsyncMailSender sender = s.getMailSender();
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(username);
            helper.setTo(password);
//        helper.setBcc(to);
            helper.setSubject("PRUEBAS");

            helper.setText("<html><body>cuerpo del mensaje</body></html>", true);
            sender.send(message);
        } catch (MessagingException ex) {
            Logger.getLogger(SendMailTLS.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

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

    public void send() {

        final String username = "neooku@gmail.com";
        final String password = "m4y1t4s00";

        Properties props = new Properties();
        props.put("mail.imap.ssl.checkserveridentity", "false");
        props.put("mail.imap.ssl.trust", "*");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.imaps.ssl.trust", "*");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("becasipndse@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("neooku@gmail.com"));
            message.setSubject("Testing Subject");
            message.setText("Dear Mail Crawler,"
                    + "\n\n No spam to my email, please!");

            Transport.send(message);

            //System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
