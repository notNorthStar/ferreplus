package ferreplus.vista;

import ferreplus.modelo.Usuario;
import ferreplus.util.EstiloUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

/**
 * Vista del modulo de Ventas (RF-12).
 *
 * <p>Tiene dos tablas lado a lado: a la izquierda el catalogo de productos
 * (con buscador y botones de ordenamiento) y a la derecha el carrito.
 * Abajo a la derecha aparecen subtotal, impuestos, total y el boton
 * "Realizar Venta".</p>
 *
 * @author Sosa Fernandez Luis David
 */
public class VentasView extends JFrame {

    private static final String[] COLS_PRODUCTOS = { "Clave", "Nombre", "Precio" };
    private static final String[] COLS_CARRITO   = { "Producto", "Cant", "Precio" };

    private final JTextField txtBuscar    = new JTextField();
    private final JButton    btnBuscar    = new JButton("Buscar");
    private final JButton    btnAgregar   = new JButton("Agregar al carrito");
    private final JButton    btnQuitar    = new JButton("Quitar del carrito");
    private final JButton    btnDefault   = new JButton("Inorden ABB");
    private final JButton    btnOrdNombre = new JButton("Nombre");
    private final JButton    btnOrdPrecio = new JButton("Precio");
    private final JButton    btnRealizar  = new JButton("Realizar Venta");
    private final JButton    btnRegresar  = new JButton("← Regresar");

    private final JLabel lblSubtotal  = new JLabel("$ 0.00");
    private final JLabel lblImpuestos = new JLabel("$ 0.00");
    private final JLabel lblTotal     = new JLabel("$ 0.00");

    private final DefaultTableModel modeloProductos = new DefaultTableModel(COLS_PRODUCTOS, 0) {
        @Override public boolean isCellEditable(int row, int col) { return false; }
    };
    private final DefaultTableModel modeloCarrito = new DefaultTableModel(COLS_CARRITO, 0) {
        @Override public boolean isCellEditable(int row, int col) { return false; }
    };
    private final JTable tablaProductos = new JTable(modeloProductos);
    private final JTable tablaCarrito   = new JTable(modeloCarrito);

    /**
     * Construye la ventana del modulo Ventas.
     *
     * @param usuario usuario en sesion
     */
    public VentasView(Usuario usuario) {
        setTitle("Ferre+ - Ventas");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        getContentPane().setBackground(EstiloUI.FONDO_OSCURO);
        setLayout(new BorderLayout());

        add(construirEncabezado(usuario), BorderLayout.NORTH);
        add(construirCentro(),            BorderLayout.CENTER);
    }

    /** Encabezado igual al de Inventario: logo + titulo + usuario. */
    private JPanel construirEncabezado(Usuario usuario) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(EstiloUI.FONDO_OSCURO);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblLogo = new JLabel("FERRE +");
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JLabel lblTitulo = new JLabel("VENTAS", SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JLabel lblUsuario = new JLabel(usuario.getNombreCompleto(), SwingConstants.RIGHT);
        lblUsuario.setForeground(Color.WHITE);
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Boton regresar + logo agrupados a la izquierda
        JPanel izquierda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        izquierda.setBackground(EstiloUI.FONDO_OSCURO);
        btnRegresar.setPreferredSize(new Dimension(110, 32));
        izquierda.add(btnRegresar);
        izquierda.add(lblLogo);

        panel.add(izquierda,  BorderLayout.WEST);
        panel.add(lblTitulo,  BorderLayout.CENTER);
        panel.add(lblUsuario, BorderLayout.EAST);
        return panel;
    }

    /** Centro: dos columnas (productos | carrito). */
    private JPanel construirCentro() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 16, 0));
        panel.setBackground(EstiloUI.FONDO_OSCURO);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        panel.add(construirPanelProductos());
        panel.add(construirPanelCarrito());
        return panel;
    }

    private JPanel construirPanelProductos() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(EstiloUI.FONDO_OSCURO);

        // Barra de busqueda
        JPanel filaBuscar = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        filaBuscar.setBackground(EstiloUI.FONDO_OSCURO);
        JLabel lblB = new JLabel("Buscar Producto:");
        lblB.setForeground(Color.WHITE);
        filaBuscar.add(lblB);
        txtBuscar.setPreferredSize(new Dimension(220, 30));
        filaBuscar.add(txtBuscar);
        filaBuscar.add(btnBuscar);

        // Barra de ordenamiento
        JPanel filaOrden = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        filaOrden.setBackground(EstiloUI.FONDO_OSCURO);
        JLabel lblO = new JLabel("Ordenar:");
        lblO.setForeground(Color.WHITE);
        filaOrden.add(lblO);
        filaOrden.add(btnDefault);
        filaOrden.add(btnOrdNombre);
        filaOrden.add(btnOrdPrecio);

        JPanel arriba = new JPanel(new BorderLayout(0, 6));
        arriba.setBackground(EstiloUI.FONDO_OSCURO);
        arriba.add(filaBuscar, BorderLayout.NORTH);
        arriba.add(filaOrden,  BorderLayout.SOUTH);

        // Tabla productos
        tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaProductos.setRowHeight(24);
        JScrollPane scroll = new JScrollPane(tablaProductos);

        // Boton Agregar al carrito
        JPanel filaAccion = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        filaAccion.setBackground(EstiloUI.FONDO_OSCURO);
        filaAccion.add(btnAgregar);

        panel.add(arriba,     BorderLayout.NORTH);
        panel.add(scroll,     BorderLayout.CENTER);
        panel.add(filaAccion, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel construirPanelCarrito() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(EstiloUI.PANEL_OSCURO);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTitulo = new JLabel("Carrito", SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));

        tablaCarrito.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaCarrito.setRowHeight(24);
        JScrollPane scroll = new JScrollPane(tablaCarrito);

        JPanel filaQuitar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        filaQuitar.setBackground(EstiloUI.PANEL_OSCURO);
        btnQuitar.setBackground(EstiloUI.ROJO_PELIGRO);
        filaQuitar.add(btnQuitar);

        // Totales + boton
        JPanel totales = new JPanel(new GridBagLayout());
        totales.setBackground(EstiloUI.PANEL_OSCURO);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(4, 4, 4, 4);

        agregarFilaTotal(totales, c, 0, "Subtotal", lblSubtotal);
        agregarFilaTotal(totales, c, 1, "Impuestos (16%)", lblImpuestos);
        agregarFilaTotal(totales, c, 2, "Total", lblTotal);

        btnRealizar.setPreferredSize(new Dimension(180, 36));
        c.gridx = 0; c.gridy = 3; c.gridwidth = 2;
        c.insets = new Insets(12, 4, 4, 4);
        totales.add(btnRealizar, c);

        JPanel abajo = new JPanel(new BorderLayout(0, 6));
        abajo.setBackground(EstiloUI.PANEL_OSCURO);
        abajo.add(filaQuitar, BorderLayout.NORTH);
        abajo.add(totales,    BorderLayout.CENTER);

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(scroll,    BorderLayout.CENTER);
        panel.add(abajo,     BorderLayout.SOUTH);
        return panel;
    }

    private void agregarFilaTotal(JPanel panel, GridBagConstraints c, int fila,
                                  String etiqueta, JLabel valor) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setForeground(Color.WHITE);
        valor.setForeground(Color.WHITE);
        valor.setHorizontalAlignment(SwingConstants.RIGHT);
        c.gridx = 0; c.gridy = fila; c.gridwidth = 1; c.weightx = 0.5;
        panel.add(lbl, c);
        c.gridx = 1; c.weightx = 0.5;
        panel.add(valor, c);
    }

    // ============================================================
    // GETTERS
    // ============================================================

    public JTextField getTxtBuscar()    { return txtBuscar; }
    public JButton    getBtnBuscar()    { return btnBuscar; }
    public JButton    getBtnAgregar()   { return btnAgregar; }
    public JButton    getBtnQuitar()    { return btnQuitar; }
    public JButton    getBtnDefault()   { return btnDefault; }
    public JButton    getBtnOrdNombre() { return btnOrdNombre; }
    public JButton    getBtnOrdPrecio() { return btnOrdPrecio; }
    public JButton    getBtnRealizar()  { return btnRealizar; }
    public JButton    getBtnRegresar()  { return btnRegresar; }
    public JLabel     getLblSubtotal()  { return lblSubtotal; }
    public JLabel     getLblImpuestos() { return lblImpuestos; }
    public JLabel     getLblTotal()     { return lblTotal; }
    public JTable     getTablaProductos() { return tablaProductos; }
    public JTable     getTablaCarrito()   { return tablaCarrito; }
    public DefaultTableModel getModeloProductos() { return modeloProductos; }
    public DefaultTableModel getModeloCarrito()   { return modeloCarrito; }
}
