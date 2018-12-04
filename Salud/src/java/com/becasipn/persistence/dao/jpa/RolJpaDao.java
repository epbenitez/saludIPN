package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.RolDao;
import com.becasipn.persistence.model.Rol;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Luis Tlatelpa Fonseca
 */
public class RolJpaDao extends JpaDaoBase<Rol, BigDecimal> implements RolDao {

    /**
     * Crea una instancia de una <code>RolJpaDao</code>.
     */
    public RolJpaDao() {
        super(Rol.class);
    }

    /**
     * Localiza un Rol, guiandose por el String que es Id del Rol en la Tabla
     *
     * @param id Es el identificador de tipo String, que es la llave (nombre)
     * del Rol
     * @return Elemento coincidente
     */
    @Override
    public Rol findById(String id) {

        Rol rol = new Rol();

        String jpql = "SELECT R FROM Rol r WHERE r.id = ?1";
        rol = executeSingleQuery(jpql, id);

        return rol;
    }

    @Override
    public Rol findByName(String nombre) {

        Rol rol = new Rol();

        String jpql = "SELECT R FROM Rol r WHERE r.descripcion = ?1";
        rol = executeSingleQuery(jpql, nombre);

        return rol;
    }

    /**
     * Busca el rol o los roles que corresponden a un usuario
     *
     * @param id
     * @return listado de roles del usuario
     */
    @Override
    public String findByUsuario(BigDecimal id) {
        String consulta = "SELECT r FROM Rol r, UsuarioPrivilegio up WHERE r.id = up.privilegio.id AND up.usuario.id = ?1";
        List<Rol> lista = executeQuery(consulta, id);
        String roles = "";
        for (int cont = 0; cont < lista.size(); cont++) {
            roles += lista.get(cont).getClave() + (cont + 1 == lista.size() ? "" : ", ");
        }
        return roles.equals("") ? "N/A" : roles;
    }

}
