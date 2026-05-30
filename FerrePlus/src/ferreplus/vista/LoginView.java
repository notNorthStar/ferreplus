package ferreplus.vista;

import ferreplus.util.EstiloUI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Ventana de inicio de sesion de Ferre+.
 *
 * <p>Reproduce el mockup vertical con el logo / texto "FERRE +", un par
 * de campos para usuario y contrasena, y el boton para iniciar sesion.
 * No tiene logica de validacion, solo expone los componentes para que
 * el {@code LoginController} les enganche listeners.</p>
 *
 * @author Sosa Fernandez Luis David
 */
public class LoginView extends JFrame {

    /** Campo de texto donde el usuario teclea su nombre de usuario. */
    private final JTextField txtUsuario = new JTextField();

    /** Campo oculto para la contrasena (RNF-S01). */
    private final JPasswordField txtPassword = new JPasswordField();

    /** Boton que dispara el intento de autenticacion. */
    private final JButton btnIniciar = new JButton("INICIAR SESION");

    /** Construye la ventana de login con el layout del mockup. */
    public LoginView() {
        setTitle("Ferre+ - Iniciar sesion");
        setSize(360, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        getContentPane().setBackground(EstiloUI.FONDO_OSCURO);
        setContentPane(construirPanel());
    }

    /**
     * Construye el panel principal con el layout del mockup
     * (centrado verticalmente con margenes generosos).
     */
    private JPanel construirPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(EstiloUI.FONDO_OSCURO);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.insets = new Insets(8, 0, 8, 0);

        // Titulo "FERRE +"
        JLabel lblTitulo = new JLabel("FERRE +", SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        c.gridy = 0;
        c.insets = new Insets(10, 0, 30, 0);
        panel.add(lblTitulo, c);

        // Icono / placeholder
        JLabel lblIcono = new JLabel("⚙", SwingConstants.CENTER);
        lblIcono.setForeground(Color.WHITE);
        lblIcono.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 72));
        c.gridy = 1;
        c.insets = new Insets(0, 0, 30, 0);
        panel.add(lblIcono, c);

        // Campo usuario
        txtUsuario.setPreferredSize(new Dimension(220, 36));
        txtUsuario.setHorizontalAlignment(SwingConstants.CENTER);
        txtUsuario.setBackground(EstiloUI.AZUL_PRIMARIO);
        txtUsuario.setForeground(Color.WHITE);
        txtUsuario.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        txtUsuario.setToolTipText("Usuario");
        c.gridy = 2;
        c.insets = new Insets(6, 0, 6, 0);
        panel.add(txtUsuario, c);

        // Campo contrasena
        txtPassword.setPreferredSize(new Dimension(220, 36));
        txtPassword.setHorizontalAlignment(SwingConstants.CENTER);
        txtPassword.setBackground(EstiloUI.AZUL_PRIMARIO);
        txtPassword.setForeground(Color.WHITE);
        txtPassword.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        txtPassword.setToolTipText("Contrasena");
        c.gridy = 3;
        panel.add(txtPassword, c);

        // Boton iniciar sesion
        btnIniciar.setBackground(EstiloUI.AZUL_PRIMARIO);
        btnIniciar.setForeground(Color.WHITE);
        btnIniciar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnIniciar.setFocusPainted(false);
        btnIniciar.setPreferredSize(new Dimension(160, 40));
        c.gridy = 4;
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(30, 0, 0, 0);
        panel.add(btnIniciar, c);

        return panel;
    }

    public JTextField getTxtUsuario() {
        return txtUsuario;
    }

    public JPasswordField getTxtPassword() {
        return txtPassword;
    }

    public JButton getBtnIniciar() {
        return btnIniciar;
    }
}
