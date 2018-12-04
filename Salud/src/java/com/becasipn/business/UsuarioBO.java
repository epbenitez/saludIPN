/**
 * SISTEMA INFORMÁTICO DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE
 * SERVICIOS ESTUDIANTILES Created on : 18-ago-2015, 12:52:17
 *
 */
package com.becasipn.business;

import com.becasipn.persistence.model.Usuario;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.PersonalAdministrativo;
import com.becasipn.service.Service;
import com.opensymphony.xwork2.ActionContext;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import javax.mail.MessagingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;

/**
 *
 * @author Patricia Benitez
 */
public class UsuarioBO extends BaseBO {

    public UsuarioBO(Service service) {
        super(service);
    }

    public Boolean actualizaContrasenia(String usuario, String contraseniaActual, String contraseniaNueva) {
        Usuario u = (Usuario) ActionContext.getContext().getSession().get("usuario");
        if (u == null) {
            return false;
        }
        if (u.getPassword().equals(contraseniaActual)) {
            u.setPassword(contraseniaNueva);
            u.setFechaModificacion(new Date());
            service.getUsuarioDao().update(u);
            return true;
        }
        return false;
    }

    public Alumno getAlumnoPorBoleta(String boleta) {
        List<Alumno> a = service.getAlumnoDao().getByBoleta(boleta);
        return a == null || a.isEmpty() ? null : a.get(0);
    }

    public PersonalAdministrativo getPersonalByUsuario(String usr) {
        PersonalAdministrativo p = service.getPersonalAdministrativoDao().findByUsuario(usr);
        return p == null ? null : p;
    }

    public Boolean existeBoleta(String boleta) {
        List<Alumno> a = service.getAlumnoDao().getByBoleta(boleta);
        return a == null || a.isEmpty() ? false : true;
    }

    public Boolean recuperarContrasenia(Alumno alumno) {
        Usuario u = alumno.getUsuario();
        u.setPassword(newPassword());
        service.getUsuarioDao().update(u);
        Boolean res = enviarCorreo(alumno);
        return res;
    }

    public Boolean recuperarContrasenia(PersonalAdministrativo personal) {
        Usuario u = personal.getUsuario();
        u.setPassword(newPassword());
        service.getUsuarioDao().update(u);
        Boolean res = enviarCorreo(personal);
        return res;

    }

    public String newPassword() {
        final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();

        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }

    public Boolean enviarCorreo(Alumno alumno) {
        HttpServletRequest request = ServletActionContext.getRequest();
        int port = request.getServerPort();
        String host = (port == 443 ? "https://" : "http://") + request.getServerName() + (port != 80 && port != 443 ? ":" + port : "");

        String subject = "Sistema Informático de Becas IPN";
//        String body = "<br><b>Hola, " + alumno.getFullName()
//                + ":</b><br><br>Has solicitado la recuperacion de tu contraseña,ingresa a nuestro sistema con los siguientes datos.<br><br>Usuario: <b>" + alumno.getUsuario().getUsuario()
//                + "</b><br>Contraseña: <b>" + alumno.getUsuario().getPassword()
//                + "</b><br><br> <a href=\"" + host + "/login.action"
//                + "\">" + host + "</a><br><br><br>"
//                + "Atentamente<br>"
//                + "DSE - IPN";

        StringBuilder body = new StringBuilder("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html> <head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /> <title>Sistema Inform&aacute;tico de Becas</title> </head> <body leftmargin=\"0\" marginwidth=\"0\" topmargin=\"0\" marginheight=\"0\" offset=\"0\"> <center> <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" height=\"100%\" width=\"100%\" id=\"backgroundTable\"> <tr> <td align=\"center\" valign=\"top\"> <!-- // Begin Template Preheader \\\\ --> <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"600\" id=\"templatePreheader\"> <tr> <td valign=\"top\" class=\"preheaderContent\"> <!-- // Begin Module: Standard Preheader \\ --> <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"100%\"> <tr> <td valign=\"top\"> <div mc:edit=\"std_preheader_content\"> <!--Recuperaci&oacute;n de Contraseña--> </div> </td> <!-- *|IFNOT:ARCHIVE_PAGE|* --> <td valign=\"top\" width=\"170\"> <div mc:edit=\"std_preheader_links\"> </div> </td> <!-- *|END:IF|* --> </tr> </table> <!-- // End Module: Standard Preheader \\ --> </td> </tr> </table> <!-- // End Template Preheader \\\\ --> <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" id=\"templateContainer\"> <tr> <td align=\"center\" valign=\"top\"> <!-- // Begin Template Header \\\\ --> <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" style=\"background-color:#FFFFFF;border-bottom:5px solid #505050;\"> <tr> <td class=\"headerContent\" width=\"100%\" style=\"padding-left:20px; padding-right:10px;\"> <div mc:edit=\"Header_content\"> <h1></h1> </div> </td> <td class=\"headerContent\"> <img src=\"https://www.sibec.ipn.mx/resources/img/login/logo-sibec.svg\" style=\"max-width:180px;\" id=\"headerImage campaign-icon\" mc:label=\"header_image\" mc:edit=\"header_image\" mc:allowtext /> </td> </tr> </table> <!-- // End Template Header \\\\ --> </td> </tr> <tr> <td align=\"center\" valign=\"top\"> <!-- // Begin Template Body \\\\ --> <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"600\" id=\"templateBody\"> <tr> <td valign=\"top\" class=\"bodyContent\"> <!-- // Begin Module: Standard Content \\\\ --> <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"100%\"> <tr> <td valign=\"top\" style=\"padding-right:0;\"> <div mc:edit=\"std_content00\">");
        body.append(" <h2 class=\"h2\">Recuperaci&oacute;n de Contraseña</h2>");
        body.append(" <h3 class=\"h3\">Hola, ").append(alumno.getFullName()).append("</h3> <p>Has solicitado la recuperaci&oacute;n de tu contraseña. Por seguridad, te hemos generado una nueva, la cual podrás cambiar una vez que inicies tu sesión. Tus datos de acceso son los siguientes:</p> ");
        body.append(" <p><b>Usuario:</b> ").append(alumno.getUsuario().getUsuario()).append("<br> <b>Contraseña:</b> ").append(alumno.getUsuario().getPassword()).append("</p><a href=\"").append(host).append("\">").append(host).append("</a>");
        body.append(" <p>Atentamente<br> Direcci&oacute;n de Servicios Estudiantiles del Instituto Polit&eacute;cnico Nacional </p> </div> </td> </tr> </table> <!-- // End Module: Standard Content \\\\ --> </td> <!-- // Begin Sidebar \\\\ --> <td valign=\"top\" width=\"180\" id=\"templateSidebar\"> <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"> <tr> <td valign=\"top\"> <!-- // Begin Module: Standard Content \\\\ --> <table border=\"0\" cellpadding=\"20\" cellspacing=\"0\" width=\"100%\" class=\"sidebarContent\"> <tr> <td valign=\"top\" style=\"padding-right:10px;\"> <div mc:edit=\"std_content01\"> <!-- <strong>Basic content module</strong> <br /> Far far away, behind the word mountains. <br /> <br /> <strong>Far from the countries</strong> <br /> Vokalia and Consonantia, there live the blind texts.--> </div> </td> </tr> </table> <!-- // End Module: Standard Content \\\\ --> </td> </tr> </table> </td> <!-- // End Sidebar \\\\ --> </tr> </table> <!-- // End Template Body \\\\ --> </td> </tr> <tr> <td align=\"center\" valign=\"top\"> <!-- // Begin Template Footer \\\\ --> <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" id=\"templateFooter\"> <tr> <td valign=\"top\" class=\"footerContent\"> <!-- // Begin Module: Standard Footer \\\\ --> <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"100%\"> <!-- <tr> <td colspan=\"2\" valign=\"middle\" id=\"social\"> <div mc:edit=\"std_social\"> &nbsp;<a href=\"*|TWITTER:PROFILEURL|*\">follow on Twitter</a> | <a href=\"*|FACEBOOK:PROFILEURL|*\">friend on Facebook</a> | <a href=\"*|FORWARD|*\">forward to a friend</a>&nbsp; </div> </td> </tr>--> <tr> <td valign=\"top\" width=\"350\"> <div mc:edit=\"std_footer\"> <em>Copyright &copy; 2016 Instituto Politécnico Nacional, todos los derechos reservados.</em> <p>Unidad Profesional \"Adolfo López Mateos\", Av. Miguel Bernard Esq. Miguel Othón de Mendizabal s/n Col. <br> Residencial La Escalera, Edif. Secretaría Gestión Estratégica 2° Piso, Ciudad de México, C.P. 07738 <br> Tel. 57296000, Ext. 51844</p> </div> </td> <td valign=\"top\" width=\"190\" id=\"monkeyRewards\"> <div mc:edit=\"monkeyrewards\"> &nbsp; </div> </td> </tr> <tr> <td colspan=\"2\" valign=\"middle\" id=\"utility\"> <div mc:edit=\"std_utility\"> &nbsp; </div> </td> </tr> </table> <!-- // End Module: Standard Footer \\\\ --> </td> </tr> </table> <!-- // End Template Footer \\\\ --> </td> </tr> </table> <br /> </td> </tr> </table> </center> </body></html>");
        try {
            sendEmail(alumno.getCorreoElectronico(), subject, body.toString());
            return true;
        } catch (MessagingException ex) {
            Logger.getLogger(UsuarioBO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public Boolean enviarCorreo(PersonalAdministrativo personal) {
        HttpServletRequest request = ServletActionContext.getRequest();
        int port = request.getServerPort();
        String host = (port == 443 ? "https://" : "http://") + request.getServerName() + (port != 80 && port != 443 ? ":" + port : "");

        String subject = "Sistema Informático de Becas IPN";
//        String body = "<br><b>Hola, " + personal.getNombreCompleto()
//                + ":</b><br><br>Ha solicitado la recuperacion de tu contraseña,ingresa a nuestro sistema con los siguientes datos.<br>Usuario: <b>" + personal.getUsuario().getUsuario()
//                + "</b><br>Contraseña: <b>" + personal.getUsuario().getPassword()
//                + "</b><br><br> <a href=\"http://" + host + "/login.action"
//                + "\">BECAS IPN</a><br><br><br>"
//                + "Atentamente<br>"
//                + "DSE - IPN";
        StringBuilder body = new StringBuilder("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html> <head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /> <title>Sistema Inform&aacute;tico de Becas</title> </head> <body leftmargin=\"0\" marginwidth=\"0\" topmargin=\"0\" marginheight=\"0\" offset=\"0\"> <center> <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" height=\"100%\" width=\"100%\" id=\"backgroundTable\"> <tr> <td align=\"center\" valign=\"top\"> <!-- // Begin Template Preheader \\\\ --> <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"600\" id=\"templatePreheader\"> <tr> <td valign=\"top\" class=\"preheaderContent\"> <!-- // Begin Module: Standard Preheader \\ --> <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"100%\"> <tr> <td valign=\"top\"> <div mc:edit=\"std_preheader_content\"> <!--Recuperaci&oacute;n de Contraseña--> </div> </td> <!-- *|IFNOT:ARCHIVE_PAGE|* --> <td valign=\"top\" width=\"170\"> <div mc:edit=\"std_preheader_links\"> </div> </td> <!-- *|END:IF|* --> </tr> </table> <!-- // End Module: Standard Preheader \\ --> </td> </tr> </table> <!-- // End Template Preheader \\\\ --> <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" id=\"templateContainer\"> <tr> <td align=\"center\" valign=\"top\"> <!-- // Begin Template Header \\\\ --> <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" style=\"background-color:#FFFFFF;border-bottom:5px solid #505050;\"> <tr> <td class=\"headerContent\" width=\"100%\" style=\"padding-left:20px; padding-right:10px;\"> <div mc:edit=\"Header_content\"> <h1></h1> </div> </td> <td class=\"headerContent\"> <img src=\"https://www.sibec.ipn.mx/resources/img/login/logo-sibec.svg\" style=\"max-width:180px;\" id=\"headerImage campaign-icon\" mc:label=\"header_image\" mc:edit=\"header_image\" mc:allowtext /> </td> </tr> </table> <!-- // End Template Header \\\\ --> </td> </tr> <tr> <td align=\"center\" valign=\"top\"> <!-- // Begin Template Body \\\\ --> <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"600\" id=\"templateBody\"> <tr> <td valign=\"top\" class=\"bodyContent\"> <!-- // Begin Module: Standard Content \\\\ --> <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"100%\"> <tr> <td valign=\"top\" style=\"padding-right:0;\"> <div mc:edit=\"std_content00\">");
        body.append(" <h2 class=\"h2\">Recuperaci&oacute;n de Contraseña</h2>");
        body.append(" <h3 class=\"h3\">Hola, ").append(personal.getNombreCompleto()).append("</h3> <p>Ha solicitado la recuperaci&oacute;n de su contraseña. Por seguridad, le hemos generado una nueva, la cual podrá cambiar una vez que inicie su sesión. Sus datos de acceso son los siguientes:</p> ");
        body.append(" <p><b>Usuario:</b> ").append(personal.getUsuario().getUsuario()).append("<br> <b>Contraseña:</b> ").append(personal.getUsuario().getPassword()).append("</p><a href=\"").append(host).append("\">").append(host).append("</a>");
        body.append(" <p>Atentamente<br> Direcci&oacute;n de Servicios Estudiantiles del Instituto Polit&eacute;cnico Nacional </p> </div> </td> </tr> </table> <!-- // End Module: Standard Content \\\\ --> </td> <!-- // Begin Sidebar \\\\ --> <td valign=\"top\" width=\"180\" id=\"templateSidebar\"> <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"> <tr> <td valign=\"top\"> <!-- // Begin Module: Standard Content \\\\ --> <table border=\"0\" cellpadding=\"20\" cellspacing=\"0\" width=\"100%\" class=\"sidebarContent\"> <tr> <td valign=\"top\" style=\"padding-right:10px;\"> <div mc:edit=\"std_content01\"> <!-- <strong>Basic content module</strong> <br /> Far far away, behind the word mountains. <br /> <br /> <strong>Far from the countries</strong> <br /> Vokalia and Consonantia, there live the blind texts.--> </div> </td> </tr> </table> <!-- // End Module: Standard Content \\\\ --> </td> </tr> </table> </td> <!-- // End Sidebar \\\\ --> </tr> </table> <!-- // End Template Body \\\\ --> </td> </tr> <tr> <td align=\"center\" valign=\"top\"> <!-- // Begin Template Footer \\\\ --> <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" id=\"templateFooter\"> <tr> <td valign=\"top\" class=\"footerContent\"> <!-- // Begin Module: Standard Footer \\\\ --> <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"100%\"> <!-- <tr> <td colspan=\"2\" valign=\"middle\" id=\"social\"> <div mc:edit=\"std_social\"> &nbsp;<a href=\"*|TWITTER:PROFILEURL|*\">follow on Twitter</a> | <a href=\"*|FACEBOOK:PROFILEURL|*\">friend on Facebook</a> | <a href=\"*|FORWARD|*\">forward to a friend</a>&nbsp; </div> </td> </tr>--> <tr> <td valign=\"top\" width=\"350\"> <div mc:edit=\"std_footer\"> <em>Copyright &copy; 2016 Instituto Politécnico Nacional, todos los derechos reservados.</em> <p>Unidad Profesional \"Adolfo López Mateos\", Av. Miguel Bernard Esq. Miguel Othón de Mendizabal s/n Col. <br> Residencial La Escalera, Edif. Secretaría Gestión Estratégica 2° Piso, Ciudad de México, C.P. 07738 <br> Tel. 57296000, Ext. 51844</p> </div> </td> <td valign=\"top\" width=\"190\" id=\"monkeyRewards\"> <div mc:edit=\"monkeyrewards\"> &nbsp; </div> </td> </tr> <tr> <td colspan=\"2\" valign=\"middle\" id=\"utility\"> <div mc:edit=\"std_utility\"> &nbsp; </div> </td> </tr> </table> <!-- // End Module: Standard Footer \\\\ --> </td> </tr> </table> <!-- // End Template Footer \\\\ --> </td> </tr> </table> <br /> </td> </tr> </table> </center> </body></html>");

        try {
            sendEmail(personal.getCorreoElectronico(), subject, body.toString());
            return true;
        } catch (MessagingException ex) {
            Logger.getLogger(UsuarioBO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public Usuario getUsuario(BigDecimal id) {
        Usuario usuario = service.getUsuarioDao().findById(id);
        return usuario;
    }

    /**
     * Actualiza la información del usuario para ponerlo inactivo
     *
     * @author Victor Lozano
     * @param usuario
     * @return true si la operación se realizó exitosamente
     */
    public Boolean borrarUsuario(Usuario usuario) {
        try {
            usuario.setFechaModificacion(new Date());
            service.getUsuarioDao().update(usuario);
        } catch (Exception e) {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    public Boolean existeUsuario(String usr) {
        return service.getUsuarioDao().existeUsuario(usr);
    }

    public Usuario findByUsuario(String usr) {
        return service.getUsuarioDao().findByUsuario(usr);
    }
}
