package ferreplus.util;

import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.Color;
import java.awt.Font;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Aplica el Look and Feel oscuro de FlatLaf y los colores propios
 * del mockup de Ferre+ (azules de acento y grises de fondo).
 *
 * <p>Se llama una sola vez al arrancar la aplicacion, antes de mostrar
 * cualquier ventana. Si FlatLaf no se puede cargar se cae al L&amp;F
 * por defecto de Swing para no romper la app.</p>
 *
 * @author Gutierrez Colorado Oliver
 */
public class EstiloUI {

    /** Azul principal de los botones (sacado del mockup). */
    public static final Color AZUL_PRIMARIO = new Color(0x19, 0x76, 0xFF);

    /** Azul mas oscuro para hover / pressed. */
    public static final Color AZUL_OSCURO = new Color(0x12, 0x5A, 0xC2);

    /** Gris azulado del fondo principal de las ventanas. */
    public static final Color FONDO_OSCURO = new Color(0x2C, 0x35, 0x40);

    /** Gris un poco mas claro para paneles internos. */
    public static final Color PANEL_OSCURO = new Color(0x3A, 0x44, 0x52);

    /** Rojo para acciones destructivas (eliminar). */
    public static final Color ROJO_PELIGRO = new Color(0xE0, 0x2E, 0x2E);

    /** Tipografia base de la interfaz. */
    public static final Font FUENTE_BASE = new Font("Segoe UI", Font.PLAIN, 14);

    /** Constructor privado para evitar instanciacion. */
    private EstiloUI() {
    }

    /**
     * Aplica FlatDarkLaf y sobrescribe los colores del UIManager para que
     * Swing pinte con los tonos del mockup de Ferre+.
     */
    public static void aplicar() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println("FlatLaf no se pudo cargar, se usa el L&F por defecto.");
        }

        // Colores generales
        UIManager.put("Panel.background", FONDO_OSCURO);
        UIManager.put("OptionPane.background", FONDO_OSCURO);
        UIManager.put("OptionPane.messageForeground", Color.WHITE);

        // Botones
        UIManager.put("Button.background", AZUL_PRIMARIO);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.focusedBackground", AZUL_OSCURO);
        UIManager.put("Button.hoverBackground", AZUL_OSCURO);
        UIManager.put("Button.arc", 12);

        // Campos de texto
        UIManager.put("TextComponent.arc", 10);
        UIManager.put("TextField.background", AZUL_PRIMARIO);
        UIManager.put("TextField.foreground", Color.WHITE);
        UIManager.put("PasswordField.background", AZUL_PRIMARIO);
        UIManager.put("PasswordField.foreground", Color.WHITE);

        // Tipografia
        UIManager.put("defaultFont", FUENTE_BASE);
    }
}
