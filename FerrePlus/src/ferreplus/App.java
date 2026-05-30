package ferreplus;

import ferreplus.controlador.LoginController;
import ferreplus.util.ConexionBD;
import ferreplus.util.EstiloUI;
import ferreplus.vista.LoginView;
import javax.swing.SwingUtilities;

/**
 * Punto de entrada de la aplicacion Ferre+.
 *
 * <p>Aqui se aplica el look and feel oscuro, se crea la vista de login
 * y se le engancha su controlador. Cuando la aplicacion termine se
 * cierra la conexion de base de datos en un shutdown hook para no
 * dejar conexiones colgadas.</p>
 *
 * @author Gutierrez Colorado Oliver
 */
public class App {

    /**
     * Lanza la aplicacion.
     *
     * @param args argumentos de linea de comandos (no se usan)
     */
    public static void main(String[] args) {
        // Tema oscuro antes de instanciar cualquier ventana
        EstiloUI.aplicar();

        // Cierre limpio de la conexion al apagar la JVM
        Runtime.getRuntime().addShutdownHook(new Thread(ConexionBD::cerrar));

        // Swing debe construirse en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            LoginView login = new LoginView();
            new LoginController(login);
            login.setVisible(true);
        });
    }
}
