package ferreplus.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Maneja la conexion unica (singleton) a la base de datos MySQL
 * del sistema Ferre+.
 *
 * <p>La idea es que cualquier DAO llame a {@link #getConexion()} y
 * reciba siempre la misma conexion ya abierta. Si la conexion se cae
 * o todavia no existe, se vuelve a abrir de forma transparente.</p>
 *
 * <p>Las credenciales estan hardcodeadas porque el proyecto es local
 * y de uso academico. Si se llegara a desplegar de verdad habria que
 * sacarlas a un archivo de configuracion.</p>
 *
 * @author Gutierrez Colorado Oliver
 */
public class ConexionBD {

    /** URL JDBC de la base de datos local en MySQL 8. */
    private static final String URL =
            "jdbc:mysql://localhost:3306/ferreplus"
            + "?useSSL=false"
            + "&allowPublicKeyRetrieval=true"
            + "&serverTimezone=UTC";

    /** Usuario por defecto de MySQL en la mayoria de instalaciones. */
    private static final String USUARIO = "root";

    /** Contrasena del usuario root. Cambiar si la instalacion local usa otra. */
    private static final String PASSWORD = "1234";

    /** Unica instancia de la conexion compartida en toda la app. */
    private static Connection conexion;

    /** Constructor privado: nadie debe instanciar esta clase. */
    private ConexionBD() {
    }

    /**
     * Devuelve la conexion activa a la base de datos. La crea si todavia
     * no existe o si fue cerrada por alguna razon.
     *
     * @return conexion JDBC lista para usarse
     * @throws SQLException si MySQL no responde o las credenciales son invalidas
     */
    public static Connection getConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
        }
        return conexion;
    }

    /**
     * Cierra la conexion si esta abierta. Se llama cuando la aplicacion
     * termina para no dejar conexiones colgadas.
     */
    public static void cerrar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException ex) {
            // Si no se puede cerrar tampoco se puede hacer mucho mas,
            // solo lo reportamos en consola para que quede registro.
            System.err.println("No se pudo cerrar la conexion: " + ex.getMessage());
        }
    }
}
