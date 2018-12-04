package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.UsuarioDao;
import com.becasipn.persistence.model.Usuario;
import java.math.BigDecimal;
import java.util.List;

/**
 * Implementación de las operaciones CRUD.
 *
 * @author Patricia Benitez
 */
public class UsuarioJpaDao extends JpaDaoBase<Usuario, BigDecimal> implements UsuarioDao {

    /**
     * Crea una instancia de una <code>UsuarioJpaDao</code>.
     */
    public UsuarioJpaDao() {
        super(Usuario.class);
    }

    @Override
    public Usuario findById(String id) {

        String jpql = "SELECT u FROM Usuario u WHERE u.usuario = ?1";
        List<Usuario> userLst = executeQuery(jpql, id);

        if (userLst != null && userLst.size() == 1) {
            return userLst.get(0);
        } else {
            return null;
        }
    }

    //SQLINJECTION Eliminado
    // public Usuario findByIdAlumno(BigDecimal idAlumno)

    /**
     * Valida si un usuario no ha sido registrado previamente
     *
     * @author Victor Lozano
     * @param nombre
     * @return true si existe
     */
    @Override
    public Boolean existeUsuario(String nombre) {
        String  consulta = "SELECT u FROM Usuario u WHERE u.usuario = ?1";
        List<Usuario> lista = executeQuery(consulta, nombre); 
        return lista != null && !lista.isEmpty();
    }

    @Override
    public Usuario findByUsuario(String nombre){
      String consulta = "SELECT usuario FROM ent_usuario WHERE usuario = ?1";
        List<Usuario> lista = executeQuery(consulta, nombre);        
        return lista.get(0);
    }
}
