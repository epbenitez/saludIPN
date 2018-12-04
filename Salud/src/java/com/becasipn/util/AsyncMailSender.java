package com.becasipn.util;

import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.commons.CommonsLogger;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import javax.activation.FileTypeMap;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.log4j.LogManager;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;

/**
 *
 * @author Patricia Benitez
 */
public class AsyncMailSender implements JavaMailSender {

    private final Logger LOG = new CommonsLogger(new Log4JLogger(LogManager.getLogger(this.getClass().getName())));
    private JavaMailSenderImpl mailSender;
    private TaskExecutor taskExecutor;

    public AsyncMailSender() {
        mailSender = new JavaMailSenderImpl();
    }

    @Override
    public void send(SimpleMailMessage simpleMailMessage) throws MailException {
        taskExecutor.execute(new AsyncMailTask(simpleMailMessage));
    }

    @Override
    public void send(SimpleMailMessage[] simpleMailMessages)
            throws MailException {
        taskExecutor.execute(new AsyncMailTask(simpleMailMessages));
    }

    @Override
    public void send(MimeMessage mimeMessage) throws MailException {
        taskExecutor.execute(new AsyncMailTask(mimeMessage));

    }

    @Override
    public void send(MimeMessage[] mimeMessages) throws MailException {
        taskExecutor.execute(new AsyncMailTask(mimeMessages));
    }

    @Override
    public void send(MimeMessagePreparator mimeMessagePreparator)
            throws MailException {
        taskExecutor.execute(new AsyncMailTask(mimeMessagePreparator));
    }

    @Override
    public void send(MimeMessagePreparator[] mimeMessagesPreparator)
            throws MailException {
        taskExecutor.execute(new AsyncMailTask(mimeMessagesPreparator));
    }

    @Override
    public MimeMessage createMimeMessage() {
        return mailSender.createMimeMessage();
    }

    @Override
    public MimeMessage createMimeMessage(InputStream inputStream)
            throws MailException {
        return mailSender.createMimeMessage(inputStream);
    }

    private class AsyncMailTask implements Runnable {

        private SimpleMailMessage[] messages = null;
        private MimeMessage[] mimeMessages = null;
        private MimeMessagePreparator[] mimeMessagesPreparator = null;

        private AsyncMailTask(SimpleMailMessage message) {
            this.messages = new SimpleMailMessage[]{message};
        }

        private AsyncMailTask(SimpleMailMessage[] messages) {
            this.messages = messages;
        }

        private AsyncMailTask(MimeMessage message) {
            this.mimeMessages = new MimeMessage[]{message};
        }

        private AsyncMailTask(MimeMessage[] messages) {
            this.mimeMessages = messages;
        }

        private AsyncMailTask(MimeMessagePreparator message) {
            this.mimeMessagesPreparator = new MimeMessagePreparator[]{message};
        }

        private AsyncMailTask(MimeMessagePreparator[] messages) {
            this.mimeMessagesPreparator = messages;
        }

        @Override
        public void run() {
            try {
                if (messages != null) {
                    for (SimpleMailMessage m : messages) {
                        mailSender.send(m);
                    }
                } else if (mimeMessages != null) {
                    for (MimeMessage m : mimeMessages) {
                        mailSender.send(m);
                        LOG.info("SUBJECT:", m.getSubject() + "- FROM: " + m.getSender());
                    }
                } else {
                    for (MimeMessagePreparator m : mimeMessagesPreparator) {
                        mailSender.send(m);
                    }
                }
            } catch (MessagingException ex) {
                java.util.logging.Logger.getLogger(AsyncMailSender.class.getName()).log(Level.SEVERE, null, ex);
            }
            LOG.info("Mail Enviado a" + mailSender.getHost());
        }
    }


    /*
     * Getters and Setters.
     */
    public void setMailSender(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }

    public void setJavaMailProperties(Properties javaMailProperties) {
        mailSender.setJavaMailProperties(javaMailProperties);
    }

    public TaskExecutor getTaskExecutor() {
        return taskExecutor;
    }

    public void setTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public void setProtocol(String protocol) {
        mailSender.setProtocol(protocol);
    }

    public void setHost(String host) {
        mailSender.setHost(host);
    }

    public void setPort(int port) {
        mailSender.setPort(port);
    }

    public void setUsername(String username) {
        mailSender.setUsername(username);
    }

    public void setPassword(String password) {
        mailSender.setPassword(password);
    }

    public void setDefaultEncoding(String defaultEncoding) {
        mailSender.setDefaultEncoding(defaultEncoding);
    }

    public void setDefaultFileTypeMap(FileTypeMap defaultFileTypeMap) {
        mailSender.setDefaultFileTypeMap(defaultFileTypeMap);
    }

    public JavaMailSenderImpl getMailSender() {
        return mailSender;
    }

    public Properties getJavaMailProperties() {
        return mailSender.getJavaMailProperties();
    }

    public String getProtocol() {
        return mailSender.getProtocol();
    }

    public String getHost() {
        return mailSender.getHost();
    }

    public int getPort() {
        return mailSender.getPort();
    }

    public String getUsername() {
        return mailSender.getUsername();
    }

    public String getPassword() {
        return mailSender.getPassword();
    }

    public String getDefaultEncoding() {
        return mailSender.getDefaultEncoding();
    }

    public FileTypeMap getDefaultFileTypeMap() {
        return mailSender.getDefaultFileTypeMap();
    }
}
