package ferreplus.controlador;

import ferreplus.modelo.Usuario;
import ferreplus.vista.DashboardView;
import javax.swing.JOptionPane;

/**
 * Controlador del {@link DashboardView}.
 *
 * <p>Por ahora solo maneja los listeners de los tres botones. Los modulos
 * de Inventario y Ventas todavia no existen, asi que muestra un mensaje
 * de "En desarrollo" como placeholder. El boton "Salir" cierra la app.</p>
 *
 * @author Gutierrez Colorado Oliver
 */
public class DashboardController {

    /** Vista que este controlador maneja. */
    private final DashboardView vista;

    /** Usuario que tiene la sesion activa (se pasara a los modulos). */
    private final Usuario usuarioActual;

    /**
     * Engancha los listeners a los botones del dashboard.
     *
     * @param vista          ventana del dashboard
     * @param usuarioActual  usuario autenticado
     */
    public DashboardController(DashboardView vista, Usuario usuarioActual) {
        this.vista = vista;
        this.usuarioActual = usuarioActual;

        vista.getBtnInventario().addActionListener(e -> abrirInventario());
        vista.getBtnVentas().addActionListener(e -> abrirVentas());
        vista.getBtnSalir().addActionListener(e -> salir());
    }

    /**
     * Placeholder mientras no exista la vista del modulo Inventario.
     * Aqui despues se hara: {@code new InventarioView(usuarioActual)} ...
     */
    private void abrirInventario() {
        JOptionPane.showMessageDialog(vista,
                "El modulo de Inventario esta en desarrollo.",
                "Proximamente",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Placeholder mientras no exista la vista del modulo Ventas.
     */
    private void abrirVentas() {
        JOptionPane.showMessageDialog(vista,
                "El modulo de Ventas esta en desarrollo.",
                "Proximamente",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Pide confirmacion al usuario y, si acepta, cierra toda la aplicacion.
     */
    private void salir() {
        int opcion = JOptionPane.showConfirmDialog(vista,
                "Vas a cerrar la sesion de " + usuarioActual.getNombreCompleto() + ".\n"
                        + "Estas seguro?",
                "Salir de Ferre+",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            vista.dispose();
            System.exit(0);
        }
    }
}
