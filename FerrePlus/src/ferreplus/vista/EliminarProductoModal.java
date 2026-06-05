package ferreplus.vista;

import ferreplus.modelo.Producto;
import ferreplus.util.EstiloUI;
import ferreplus.util.SelectorImagen;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Modal para confirmar la eliminacion de un producto, pidiendo el
 * motivo que se va a guardar en la bitacora (RF-05, RF-06).
 *
 * <p>Muestra los datos del producto en modo read-only y un area de
 * texto para capturar el motivo.</p>
 *
 * @author Sosa Fernandez Luis David
 */
public class EliminarProductoModal extends JDialog {

    private final JTextField txtClave    = new JTextField();
    private final JTextField txtNombre   = new JTextField();
    private final JTextField txtCantidad = new JTextField();
    private final JTextArea  txtMotivo   = new JTextArea(4, 20);
    private final JLabel     lblPreview  = new JLabel();
    private final JButton    btnGuardar  = new JButton("ELIMINAR");
    private final JButton    btnCancelar = new JButton("CANCELAR");

    private boolean confirmado = false;

    /**
     * Crea el modal con los datos del producto pre-cargados.
     *
     * @param padre    ventana padre
     * @param producto producto que se va a eliminar
     */
    public EliminarProductoModal(Window padre, Producto producto) {
        super(padre, java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        setTitle("Eliminar Producto");
        setSize(560, 420);
        setLocationRelativeTo(padre);
        setResizable(false);
        getContentPane().setBackground(EstiloUI.FONDO_OSCURO);

        setLayout(new BorderLayout());
        add(construirCentro(producto), BorderLayout.CENTER);
        add(construirPie(),            BorderLayout.SOUTH);

        cargarProducto(producto);

        btnCancelar.addActionListener(e -> dispose());
        btnGuardar.addActionListener(e -> { confirmado = true; dispose(); });
    }

    /** Construye el cuerpo del modal con los campos pre-cargados. */
    private JPanel construirCentro(Producto producto) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(EstiloUI.FONDO_OSCURO);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(6, 6, 6, 6);

        JLabel lblTitulo = new JLabel("ELIMINAR PRODUCTO", SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        c.gridx = 0; c.gridy = 0; c.gridwidth = 3;
        panel.add(lblTitulo, c);

        c.gridwidth = 1;
        c.gridy++;

        agregarFila(panel, c, "Clave:",    txtClave);
        agregarFila(panel, c, "Nombre:",   txtNombre);
        agregarFila(panel, c, "Cantidad:", txtCantidad);

        // Motivo
        JLabel lblMotivo = new JLabel("Motivo:");
        lblMotivo.setForeground(Color.WHITE);
        c.gridx = 0; c.gridy++; c.weightx = 0;
        panel.add(lblMotivo, c);

        txtMotivo.setLineWrap(true);
        txtMotivo.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(txtMotivo);
        scroll.setPreferredSize(new Dimension(200, 80));
        c.gridx = 1; c.weightx = 1.0;
        panel.add(scroll, c);

        // Preview de la foto a la derecha
        c.gridx = 2; c.gridy = 1; c.gridheight = 4;
        c.fill = GridBagConstraints.BOTH;
        lblPreview.setPreferredSize(new Dimension(140, 140));
        lblPreview.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        lblPreview.setHorizontalAlignment(SwingConstants.CENTER);
        lblPreview.setText("Sin foto");
        lblPreview.setForeground(Color.LIGHT_GRAY);
        panel.add(lblPreview, c);

        // Read-only en los campos del producto
        txtClave.setEditable(false);
        txtNombre.setEditable(false);
        txtCantidad.setEditable(false);

        return panel;
    }

    /** Pie con los botones Eliminar / Cancelar. */
    private JPanel construirPie() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBackground(EstiloUI.FONDO_OSCURO);
        btnGuardar.setBackground(EstiloUI.ROJO_PELIGRO);
        btnGuardar.setPreferredSize(new Dimension(130, 36));
        btnCancelar.setPreferredSize(new Dimension(130, 36));
        panel.add(btnGuardar);
        panel.add(btnCancelar);
        return panel;
    }

    private void agregarFila(JPanel panel, GridBagConstraints c,
                             String etiqueta, JTextField campo) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setForeground(Color.WHITE);
        c.gridx = 0; c.weightx = 0;
        panel.add(lbl, c);

        campo.setPreferredSize(new Dimension(200, 28));
        c.gridx = 1; c.weightx = 1.0;
        panel.add(campo, c);

        c.gridy++;
    }

    /** Llena los campos con los datos del producto a eliminar. */
    private void cargarProducto(Producto p) {
        txtClave.setText(p.getClave());
        txtNombre.setText(p.getNombre());
        txtCantidad.setText(String.valueOf(p.getCantidad()));
        if (p.getFoto() != null) {
            ImageIcon ic = SelectorImagen.iconoDesdeBytes(p.getFoto(), 140, 140);
            if (ic != null) {
                lblPreview.setIcon(ic);
                lblPreview.setText("");
            }
        }
    }

    public boolean fueConfirmado() { return confirmado; }
    public String  getMotivo()     { return txtMotivo.getText().trim(); }
}
