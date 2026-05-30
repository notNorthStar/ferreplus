package ferreplus.dao;

import ferreplus.modelo.Usuario;
import ferreplus.util.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Acceso a datos de la tabla {@code usuarios}.
 *
 * <p>Por ahora solo expone la autenticacion, que es lo unico que
 * el modulo de login necesita. Mas adelante se podrian agregar
 * altas, bajas y cambios de usuarios si la app lo requiere.</p>
 *
 * @author Sosa Fernandez Luis David
 */
public class UsuarioDAO {

    /**
     * Verifica que el {@code username} y la {@code password} dados
     * correspondan a un usuario existente y activo.
     *
     * @param username nombre de usuario tecleado en el login
     * @param password contrasena tecleada en el login
     * @return el {@link Usuario} autenticado, o {@code null} si no coincide
     * @throws SQLException si la base de datos no responde
     */
    public Usuario autenticar(String username, String password) throws SQLException {
        String sql = "SELECT id, username, password, nombre_completo, activo "
                   + "FROM usuarios WHERE username = ? AND password = ? AND activo = TRUE";

        Connection con = ConexionBD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id"));
                    u.setUsername(rs.getString("username"));
                    u.setPassword(rs.getString("password"));
                    u.setNombreCompleto(rs.getString("nombre_completo"));
                    u.setActivo(rs.getBoolean("activo"));
                    return u;
                }
            }
        }
        return null;
    }
}
