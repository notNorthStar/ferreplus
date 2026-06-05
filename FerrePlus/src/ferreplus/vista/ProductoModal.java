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
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Modal para Agregar o Editar un producto.
 *
 * <p>El modo se decide en el constructor: si recibe {@code null} es
 * "Agregar", si recibe un {@link Producto} ya existente es "Editar"
 * (con la clave deshabilitada porque no se puede modificar, RF-04).</p>
 *
 * @author Sosa Fernandez Luis David
 */
public class ProductoModal extends JDialog {

    private final JTextField txtClave     = new JTextField();
    private final JTextField txtNombre    = new JTextField();
    private final JTextField txtCantidad  = new JTextField();
    private final JTextField txtPrecio    = new JTextField();
    private final JTextField txtUbicacion = new JTextField();
    private final JButton    btnFoto      = new JButton("Foto");
    private final JLabel     lblPreview   = new JLabel();
    private final JButton    btnGuardar   = new JButton("GUARDAR");
    private final JButton    btnCancelar  = new JButton("CANCELAR");

    /** Bytes de la foto seleccionada (o la original al editar). */
    private byte[] fotoBytes;

    /** Indica si el usuario confirmo el guardado. */
    private boolean confirmado = false;

    /** True si esta en modo edicion. */
    private final boolean modoEditar;

    /**
     * Crea el modal en modo agregar o editar.
     *
     * @param padre    ventana padre (para centrar)
     * @param producto producto a editar, o {@code null} para agregar
     */
    public ProductoModal(Window padre, Producto producto) {
        super(padre, java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        this.modoEditar = producto != null;
        setTitle(modoEditar ? "Editar Producto" : "Agregar Producto");
        setSize(560, 420);
        setLocationRelativeTo(padre);
        setResizable(false);
        getContentPane().setBackground(EstiloUI.FONDO_OSCURO);

        setLayout(new BorderLayout());
        add(construirCentro(), BorderLayout.CENTER);
        add(construirPie(),    BorderLayout.SOUTH);

        if (modoEditar) {
            cargarProducto(producto);
        }

        btnFoto.addActionListener(e -> elegirFoto());
        btnCancelar.addActionListener(e -> dispose());
        btnGuardar.addActionListener(e -> { confirmado = true; dispose(); });
    }

    /** Construye el formulario con sus labels y campos. */
    private JPanel construirCentro() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(EstiloUI.FONDO_OSCURO);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(6, 6, 6, 6);

        // Titulo
        JLabel lblTitulo = new JLabel(
                modoEditar ? "EDITAR PRODUCTO" : "AGREGAR PRODUCTO",
                SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        c.gridx = 0; c.gridy = 0; c.gridwidth = 3;
        panel.add(lblTitulo, c);

        c.gridwidth = 1;
        c.gridy++;

        agregarFila(panel, c, "Clave:",    txtClave);
        agregarFila(panel, c, "Nombre:",   txtNombre);
        agregarFila(panel, c, "Cantidad:", txtCantidad);
        agregarFila(panel, c, "Precio:",   txtPrecio);
        agregarFila(panel, c, "Ubicacion:", txtUbicacion);

        // Bloque Foto + preview (a la derecha)
        c.gridx = 2; c.gridy = 1; c.gridheight = 5;
        c.fill = GridBagConstraints.BOTH;
        JPanel panelFoto = new JPanel(new BorderLayout(0, 4));
        panelFoto.setBackground(EstiloUI.FONDO_OSCURO);
        btnFoto.setPreferredSize(new Dimension(120, 30));
        panelFoto.add(btnFoto, BorderLayout.NORTH);
        lblPreview.setPreferredSize(new Dimension(140, 140));
        lblPreview.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        lblPreview.setHorizontalAlignment(SwingConstants.CENTER);
        lblPreview.setText("Sin foto");
        lblPreview.setForeground(Color.LIGHT_GRAY);
        panelFoto.add(lblPreview, BorderLayout.CENTER);
        panel.add(panelFoto, c);

        if (modoEditar) {
            txtClave.setEditable(false);
            txtClave.setForeground(Color.LIGHT_GRAY);
        }

        return panel;
    }

    /** Helper: agrega una fila con label + textfield al panel. */
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

    /** Pie con los botones Guardar / Cancelar. */
    private JPanel construirPie() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBackground(EstiloUI.FONDO_OSCURO);
        btnGuardar.setPreferredSize(new Dimension(120, 36));
        btnCancelar.setPreferredSize(new Dimension(120, 36));
        panel.add(btnGuardar);
        panel.add(btnCancelar);
        return panel;
    }

    /** Llena los campos con los datos del producto a editar. */
    private void cargarProducto(Producto p) {
        txtClave.setText(p.getClave());
        txtNombre.setText(p.getNombre());
        txtCantidad.setText(String.valueOf(p.getCantidad()));
        txtPrecio.setText(String.valueOf(p.getPrecio()));
        txtUbicacion.setText(p.getUbicacion());
        if (p.getFoto() != null) {
            fotoBytes = p.getFoto();
            ImageIcon ic = SelectorImagen.iconoDesdeBytes(fotoBytes, 140, 140);
            if (ic != null) {
                lblPreview.setIcon(ic);
                lblPreview.setText("");
            }
        }
    }

    /** Abre el JFileChooser para escoger una imagen. */
    private void elegirFoto() {
        byte[] nueva = SelectorImagen.elegirImagen(this);
        if (nueva != null) {
            fotoBytes = nueva;
            ImageIcon ic = SelectorImagen.iconoDesdeBytes(fotoBytes, 140, 140);
            if (ic != null) {
                lblPreview.setIcon(ic);
                lblPreview.setText("");
            }
        }
    }

    // ============================================================
    // API PARA EL CONTROLADOR
    // ============================================================

    public boolean fueConfirmado() { return confirmado; }

    public String getClave()     { return txtClave.getText().trim(); }
    public String getNombre()    { return txtNombre.getText().trim(); }
    public String getCantidad()  { return txtCantidad.getText().trim(); }
    public String getPrecio()    { return txtPrecio.getText().trim(); }
    public String getUbicacion() { return txtUbicacion.getText().trim(); }
    public byte[] getFotoBytes() { return fotoBytes; }
    public boolean esModoEditar() { return modoEditar; }
}
