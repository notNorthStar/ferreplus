package ferreplus.controlador;

import ferreplus.dao.UsuarioDAO;
import ferreplus.modelo.Usuario;
import ferreplus.vista.DashboardView;
import ferreplus.vista.LoginView;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * Controlador de la {@link LoginView}.
 *
 * <p>Se encarga de leer lo que el usuario teclea, pasarselo al
 * {@link UsuarioDAO} para autenticar y, si todo va bien, abrir la
 * {@link DashboardView} y cerrar la ventana de login.</p>
 *
 * @author Sosa Fernandez Luis David
 */
public class LoginController {

    /** Vista que este controlador maneja. */
    private final LoginView vista;

    /** DAO que consulta la tabla usuarios. */
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    /**
     * Conecta los listeners de la vista a los metodos del controlador.
     *
     * @param vista ventana de login ya construida
     */
    public LoginController(LoginView vista) {
        this.vista = vista;

        // Click en el boton: intentar login
        vista.getBtnIniciar().addActionListener(e -> intentarLogin());

        // Enter en el campo de contrasena tambien hace login (comodidad)
        vista.getTxtPassword().addActionListener(e -> intentarLogin());
    }

    /**
     * Toma usuario y contrasena de la vista, valida que no esten vacios
     * y consulta al DAO. Si la autenticacion es exitosa abre el dashboard.
     */
    private void intentarLogin() {
        String username = vista.getTxtUsuario().getText().trim();
        String password = new String(vista.getTxtPassword().getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(vista,
                    "Escribe tu usuario y tu contrasena.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Usuario u = usuarioDAO.autenticar(username, password);
            if (u != null) {
                abrirDashboard(u);
            } else {
                JOptionPane.showMessageDialog(vista,
                        "Usuario o contrasena incorrectos.",
                        "Acceso denegado",
                        JOptionPane.ERROR_MESSAGE);
                vista.getTxtPassword().setText("");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vista,
                    "No se pudo conectar a la base de datos:\n" + ex.getMessage(),
                    "Error de conexion",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Cierra el login y abre el dashboard con el usuario autenticado.
     *
     * @param usuario usuario que acaba de iniciar sesion
     */
    private void abrirDashboard(Usuario usuario) {
        DashboardView dashboard = new DashboardView(usuario);
        new DashboardController(dashboard, usuario);
        dashboard.setVisible(true);
        vista.dispose();
    }
}
