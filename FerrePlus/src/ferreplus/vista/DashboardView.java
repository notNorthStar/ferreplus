package ferreplus.vista;

import ferreplus.modelo.Usuario;
import ferreplus.util.EstiloUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Pantalla principal a la que entra el usuario despues del login.
 *
 * <p>Muestra el saludo "Bienvenido, ..." con el nombre completo y dos
 * tarjetas grandes: una para entrar al modulo de Inventario y otra al
 * modulo de Ventas (RF-02). Tambien tiene un boton "Salir" abajo a la
 * derecha como en el mockup.</p>
 *
 * @author Gutierrez Colorado Oliver
 */
public class DashboardView extends JFrame {

    /** Boton que abrira el modulo de Inventario (RF-02). */
    private final JButton btnInventario = new JButton("<html><center>MODULO<br>INVENTARIO</center></html>");

    /** Boton que abrira el modulo de Ventas (RF-02). */
    private final JButton btnVentas = new JButton("<html><center>MODULO<br>VENTAS</center></html>");

    /** Boton para cerrar la aplicacion. */
    private final JButton btnSalir = new JButton("Salir");

    /**
     * Construye la ventana con el saludo personalizado.
     *
     * @param usuario usuario que inicio sesion
     */
    public DashboardView(Usuario usuario) {
        setTitle("Ferre+ - Inicio");
        setSize(720, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        getContentPane().setBackground(EstiloUI.FONDO_OSCURO);
        setLayout(new BorderLayout());

        add(construirEncabezado(usuario), BorderLayout.NORTH);
        add(construirCentro(), BorderLayout.CENTER);
        add(construirPiePagina(), BorderLayout.SOUTH);
    }

    /** Encabezado con el logo de texto y el saludo al usuario. */
    private JPanel construirEncabezado(Usuario usuario) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(EstiloUI.FONDO_OSCURO);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JLabel lblLogo = new JLabel("FERRE +", SwingConstants.CENTER);
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblLogo.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        JLabel lblBienvenida = new JLabel(
                "Bienvenido, " + usuario.getNombreCompleto(),
                SwingConstants.CENTER);
        lblBienvenida.setForeground(Color.WHITE);
        lblBienvenida.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblBienvenida.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        lblBienvenida.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        panel.add(lblLogo);
        panel.add(lblBienvenida);
        return panel;
    }

    /** Tarjetas centrales con los dos modulos. */
    private JPanel construirCentro() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 30, 0));
        panel.setBackground(EstiloUI.FONDO_OSCURO);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));

        configurarBotonModulo(btnInventario);
        configurarBotonModulo(btnVentas);

        panel.add(btnInventario);
        panel.add(btnVentas);
        return panel;
    }

    /** Pie de pagina con el boton "Salir" alineado a la derecha. */
    private JPanel construirPiePagina() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        panel.setBackground(EstiloUI.FONDO_OSCURO);

        btnSalir.setBackground(EstiloUI.AZUL_PRIMARIO);
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setPreferredSize(new Dimension(100, 36));
        btnSalir.setFocusPainted(false);

        panel.add(Box.createHorizontalGlue());
        panel.add(btnSalir);
        return panel;
    }

    /** Aplica el estilo "tarjeta grande azul" a un boton de modulo. */
    private void configurarBotonModulo(JButton btn) {
        btn.setBackground(EstiloUI.AZUL_PRIMARIO);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));
    }

    public JButton getBtnInventario() {
        return btnInventario;
    }

    public JButton getBtnVentas() {
        return btnVentas;
    }

    public JButton getBtnSalir() {
        return btnSalir;
    }
}
