/**
 * SISTEMA INFORMÁTICO DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE SERVICIOS ESTUDIANTILES Created on : 04-ago-2015, 14:30:41
 *
 */
package com.becasipn.business;

import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.Rol;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.persistence.model.UsuarioPrivilegio;
import com.becasipn.service.Service;
import java.math.BigDecimal;
import com.becasipn.util.PasswordGenerator;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Patricia Benitez
 */
public class AlumnoBO extends BaseBO{

    private final SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
    private HashMap<BigDecimal, Integer> relacion;
    private String jsonAlumno;

    public AlumnoBO(Service service) {
        super(service);
    }

    /**
     * Guarda o actualiza los datos del alumno
     *
     * @param alumno
     * @return
     */
    public Alumno guarda(Alumno alumno) {
        List<Alumno> aTmpLst = service.getAlumnoDao().getByBoleta(alumno.getBoleta());//("boleta", a.getBoleta(), Boolean.TRUE);
        alumno.setFechaModificacion(new Date());
        if (aTmpLst == null || aTmpLst.isEmpty()) {
            try {
                alumno.setEstatus(Boolean.TRUE);
                alumno.setFechaAlta(new Date());
                alumno = service.getAlumnoDao().save(alumno);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            alumno.setUsuario(aTmpLst.get(0).getUsuario());
            if (alumno.getId() != null) {
                service.getAlumnoDao().update(alumno);
            }
        }
        return alumno;
    }

    public Usuario creaUsuario(Alumno a) {
        if (a == null || a.getUsuario() != null) {
            return null;
        } else {
            String usuario = a.getBoleta();
            String password = PasswordGenerator.generatePassword();
            Usuario u = new Usuario();
            u.setActivo(Boolean.TRUE);
            u.setUsuario(usuario);
            u.setPassword(password);
            Set<UsuarioPrivilegio> roles = new HashSet<UsuarioPrivilegio>();
            UsuarioPrivilegio privilegio = new UsuarioPrivilegio();
            privilegio.setPrivilegio(new Rol(new BigDecimal(2))); //ALUMNO
            privilegio.setUsuario(u);
            roles.add(privilegio);
            u.setPrivilegios(roles);
            u.setFechaModificacion(new Date());
            a.setUsuario(u);
            return u;
        }
    }

    public Boolean guardaAlumno(Alumno alumno) {
        try {
            if (alumno.getId() == null) {
                service.getAlumnoDao().save(alumno);
            } else {
                service.getAlumnoDao().update(alumno);
            }
        } catch (Exception e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

}
