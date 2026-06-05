package ferreplus.vista;

import ferreplus.modelo.Usuario;
import ferreplus.util.EstiloUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
 * Vista principal del modulo de Inventario.
 *
 * <p>Reproduce el mockup con encabezado (logo + INVENTARIO + nombre del
 * usuario), barra de busqueda y acciones (Agregar / Editar / Eliminar /
 * Log), botones de ordenamiento (Nombre / Precio) y tabla con los
 * productos. Para no inflar la UI, las acciones Editar y Eliminar operan
 * sobre la fila seleccionada en lugar de tener botones por fila.</p>
 *
 * @author Sosa Fernandez Luis David
 */
public class InventarioView extends JFrame {

    /** Columnas que se muestran en la tabla. */
    private static final String[] COLUMNAS = {
        "Clave", "Nombre", "Cantidad", "Ubicacion", "Precio"
    };

    private final JTextField txtBuscar = new JTextField();
    private final JButton btnBuscar   = new JButton("Buscar");
    private final JButton btnAgregar  = new JButton("Agregar");
    private final JButton btnEditar   = new JButton("Editar");
    private final JButton btnEliminar = new JButton("Eliminar");
    private final JButton btnLog      = new JButton("Log");
    private final JButton btnRegresar = new JButton("← Regresar");
    private final JButton btnOrdNombre = new JButton("Nombre");
    private final JButton btnOrdPrecio = new JButton("Precio");
    private final JButton btnDefault   = new JButton("Inorden ABB");
    private final JLabel  lblConteo   = new JLabel(" ");

    private final DefaultTableModel modelo = new DefaultTableModel(COLUMNAS, 0) {
        @Override public boolean isCellEditable(int row, int col) { return false; }
    };
    private final JTable tabla = new JTable(modelo);

    /**
     * Construye la ventana del modulo Inventario.
     *
     * @param usuario usuario en sesion (para mostrar su nombre)
     */
    public InventarioView(Usuario usuario) {
        setTitle("Ferre+ - Inventario");
        setSize(960, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        getContentPane().setBackground(EstiloUI.FONDO_OSCURO);
        setLayout(new BorderLayout());

        add(construirEncabezado(usuario), BorderLayout.NORTH);
        add(construirCentro(),           BorderLayout.CENTER);
        add(construirPie(),              BorderLayout.SOUTH);
    }

    /** Encabezado: logo a la izquierda, INVENTARIO al centro, usuario a la derecha. */
    private JPanel construirEncabezado(Usuario usuario) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(EstiloUI.FONDO_OSCURO);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblLogo = new JLabel("FERRE +");
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JLabel lblTitulo = new JLabel("INVENTARIO", SwingConstants.CENTER);
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

    /** Centro: barra de acciones, barra de ordenamiento y tabla. */
    private JPanel construirCentro() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(EstiloUI.FONDO_OSCURO);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        // Fila 1: buscar + botones de accion
        JPanel filaAcciones = new JPanel(new BorderLayout(8, 0));
        filaAcciones.setBackground(EstiloUI.FONDO_OSCURO);

        JPanel grupoBuscar = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        grupoBuscar.setBackground(EstiloUI.FONDO_OSCURO);
        JLabel lbl = new JLabel("Buscar:");
        lbl.setForeground(Color.WHITE);
        grupoBuscar.add(lbl);
        txtBuscar.setPreferredSize(new Dimension(280, 30));
        txtBuscar.setToolTipText("SKU (9 digitos) o nombre");
        grupoBuscar.add(txtBuscar);
        grupoBuscar.add(btnBuscar);

        JPanel grupoAcc = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        grupoAcc.setBackground(EstiloUI.FONDO_OSCURO);
        grupoAcc.add(btnAgregar);
        grupoAcc.add(btnEditar);
        grupoAcc.add(btnEliminar);
        grupoAcc.add(btnLog);

        filaAcciones.add(grupoBuscar, BorderLayout.WEST);
        filaAcciones.add(grupoAcc,    BorderLayout.EAST);

        // Fila 2: ordenar
        JPanel filaOrden = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        filaOrden.setBackground(EstiloUI.FONDO_OSCURO);
        JLabel lblOrd = new JLabel("Ordenar:");
        lblOrd.setForeground(Color.WHITE);
        filaOrden.add(lblOrd);
        filaOrden.add(btnDefault);
        filaOrden.add(btnOrdNombre);
        filaOrden.add(btnOrdPrecio);

        // Tabla
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(26);
        tabla.getTableHeader().setReorderingAllowed(false);
        JScrollPane scroll = new JScrollPane(tabla);

        JPanel panelArriba = new JPanel(new BorderLayout(0, 6));
        panelArriba.setBackground(EstiloUI.FONDO_OSCURO);
        panelArriba.add(filaAcciones, BorderLayout.NORTH);
        panelArriba.add(filaOrden,    BorderLayout.SOUTH);

        panel.add(panelArriba, BorderLayout.NORTH);
        panel.add(scroll,      BorderLayout.CENTER);
        return panel;
    }

    /** Pie de pagina con el contador de productos. */
    private JPanel construirPie() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 8));
        panel.setBackground(EstiloUI.FONDO_OSCURO);
        lblConteo.setForeground(Color.WHITE);
        panel.add(lblConteo);
        return panel;
    }

    // ============================================================
    // GETTERS PARA EL CONTROLADOR
    // ============================================================

    public JTextField getTxtBuscar()      { return txtBuscar; }
    public JButton    getBtnBuscar()      { return btnBuscar; }
    public JButton    getBtnAgregar()     { return btnAgregar; }
    public JButton    getBtnEditar()      { return btnEditar; }
    public JButton    getBtnEliminar()    { return btnEliminar; }
    public JButton    getBtnLog()         { return btnLog; }
    public JButton    getBtnRegresar()    { return btnRegresar; }
    public JButton    getBtnDefault()     { return btnDefault; }
    public JButton    getBtnOrdNombre()   { return btnOrdNombre; }
    public JButton    getBtnOrdPrecio()   { return btnOrdPrecio; }
    public JTable     getTabla()          { return tabla; }
    public DefaultTableModel getModelo()  { return modelo; }
    public JLabel     getLblConteo()      { return lblConteo; }
}
