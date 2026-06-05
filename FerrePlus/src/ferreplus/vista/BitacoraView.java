package ferreplus.vista;

import ferreplus.util.EstiloUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

/**
 * Vista de la Bitacora de Eliminaciones (RF-06).
 *
 * <p>Es un dialogo modal de solo lectura, como pide el requerimiento
 * RNF-S03 (la bitacora no se puede editar ni eliminar desde la UI).</p>
 *
 * @author Sosa Fernandez Luis David
 */
public class BitacoraView extends JDialog {

    private static final String[] COLUMNAS = {
        "Clave", "Nombre", "Motivo", "Fecha", "Usuario"
    };

    private final DefaultTableModel modelo = new DefaultTableModel(COLUMNAS, 0) {
        @Override public boolean isCellEditable(int row, int col) { return false; }
    };
    private final JTable tabla = new JTable(modelo);
    private final JButton btnCerrar = new JButton("CERRAR");

    /**
     * Construye el modal de bitacora.
     *
     * @param padre ventana padre
     */
    public BitacoraView(Window padre) {
        super(padre, "Bitacora de Eliminaciones",
              java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        setSize(820, 460);
        setLocationRelativeTo(padre);
        getContentPane().setBackground(EstiloUI.FONDO_OSCURO);

        setLayout(new BorderLayout());
        add(construirTitulo(),  BorderLayout.NORTH);
        add(construirCentro(),  BorderLayout.CENTER);
        add(construirPie(),     BorderLayout.SOUTH);

        btnCerrar.addActionListener(e -> dispose());
    }

    private JPanel construirTitulo() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        panel.setBackground(EstiloUI.FONDO_OSCURO);
        JLabel lbl = new JLabel("BITACORA DE ELIMINACIONES", SwingConstants.CENTER);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(lbl);
        return panel;
    }

    private JPanel construirCentro() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(EstiloUI.FONDO_OSCURO);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        tabla.setRowHeight(24);
        tabla.getTableHeader().setReorderingAllowed(false);
        JScrollPane scroll = new JScrollPane(tabla);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel construirPie() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        panel.setBackground(EstiloUI.FONDO_OSCURO);
        btnCerrar.setPreferredSize(new Dimension(120, 36));
        panel.add(btnCerrar);
        return panel;
    }

    public DefaultTableModel getModelo() { return modelo; }
    public JTable            getTabla()  { return tabla; }
    public JButton           getBtnCerrar() { return btnCerrar; }
}
