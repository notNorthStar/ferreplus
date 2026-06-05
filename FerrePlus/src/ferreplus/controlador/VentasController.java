package ferreplus.controlador;

import ferreplus.dao.ProductoDAO;
import ferreplus.dao.VentaDAO;
import ferreplus.modelo.DetalleVenta;
import ferreplus.modelo.Producto;
import ferreplus.modelo.Usuario;
import ferreplus.modelo.Venta;
import ferreplus.util.ABB;
import ferreplus.util.OrdenamientoUtil;
import ferreplus.util.Validador;
import ferreplus.vista.VentasView;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Controlador del modulo de Ventas (RF-12).
 *
 * <p>Mantiene un ABB con los productos en memoria (para buscar y mostrar
 * en Inorden) y un carrito en memoria (una lista de
 * {@link DetalleVenta}) que se va llenando hasta confirmar la venta.</p>
 *
 * <p>Al confirmar, se calculan los totales con IVA del 16%, se manda
 * todo al {@link VentaDAO} (que descuenta stock en transaccion) y se
 * limpia el carrito.</p>
 *
 * @author Sosa Fernandez Luis David
 */
public class VentasController {

    /** Porcentaje de impuestos aplicado a cada venta. */
    private static final double IVA = 0.16;

    private final VentasView vista;
    private final Usuario usuarioActual;
    private final ProductoDAO productoDAO = new ProductoDAO();
    private final VentaDAO    ventaDAO    = new VentaDAO();

    private final ABB<Producto> arbol = new ABB<>();
    private List<Producto> listaActual = new ArrayList<>();

    /** Carrito en memoria: cada renglon es un {@link DetalleVenta}. */
    private final List<DetalleVenta> carrito = new ArrayList<>();

    /**
     * Engancha listeners y carga el catalogo.
     *
     * @param vista          ventana de ventas
     * @param usuarioActual  usuario en sesion
     */
    public VentasController(VentasView vista, Usuario usuarioActual) {
        this.vista = vista;
        this.usuarioActual = usuarioActual;

        vista.getBtnBuscar().addActionListener(e -> buscar());
        vista.getTxtBuscar().addActionListener(e -> buscar());
        vista.getBtnDefault().addActionListener(e -> mostrarInorden());
        vista.getBtnOrdNombre().addActionListener(e -> ordenarPorNombre());
        vista.getBtnOrdPrecio().addActionListener(e -> ordenarPorPrecio());
        vista.getBtnAgregar().addActionListener(e -> agregarAlCarrito());
        vista.getBtnQuitar().addActionListener(e -> quitarDelCarrito());
        vista.getBtnRealizar().addActionListener(e -> realizarVenta());
        vista.getBtnRegresar().addActionListener(e -> vista.dispose());

        cargarDesdeBaseDeDatos();
    }

    // ============================================================
    // CARGA Y MOSTRADO
    // ============================================================

    private void cargarDesdeBaseDeDatos() {
        try {
            arbol.limpiar();
            for (Producto p : productoDAO.listarTodos()) {
                arbol.insertar(p);
            }
            mostrarInorden();
        } catch (SQLException ex) {
            mostrarError("No se pudieron cargar los productos", ex);
        }
    }

    private void mostrarInorden() {
        listaActual = arbol.recorrerInorden();
        pintarProductos(listaActual);
    }

    private void pintarProductos(List<Producto> lista) {
        DefaultTableModel modelo = vista.getModeloProductos();
        modelo.setRowCount(0);
        for (Producto p : lista) {
            modelo.addRow(new Object[]{
                p.getClave(),
                p.getNombre(),
                String.format("$ %.2f", p.getPrecio())
            });
        }
    }

    private void buscar() {
        String texto = vista.getTxtBuscar().getText().trim();
        if (texto.isEmpty()) {
            mostrarInorden();
            return;
        }
        if (Validador.claveValida(texto)) {
            Producto patron = new Producto();
            patron.setClave(texto);
            Producto encontrado = arbol.buscar(patron);
            listaActual = new ArrayList<>();
            if (encontrado != null) {
                listaActual.add(encontrado);
            }
        } else {
            listaActual = new ArrayList<>();
            String textoMin = texto.toLowerCase();
            for (Producto p : arbol.recorrerInorden()) {
                if (p.getNombre() != null
                        && p.getNombre().toLowerCase().contains(textoMin)) {
                    listaActual.add(p);
                }
            }
        }
        pintarProductos(listaActual);
    }

    private void ordenarPorNombre() {
        OrdenamientoUtil.quickSortPorNombre(listaActual);
        pintarProductos(listaActual);
    }

    private void ordenarPorPrecio() {
        OrdenamientoUtil.mergeSortPorPrecio(listaActual);
        pintarProductos(listaActual);
    }

    // ============================================================
    // CARRITO
    // ============================================================

    /** Agrega 1 unidad del producto seleccionado al carrito. */
    private void agregarAlCarrito() {
        int fila = vista.getTablaProductos().getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(vista,
                    "Selecciona un producto de la tabla primero.",
                    "Sin seleccion", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Producto p = listaActual.get(fila);
        if (p.getCantidad() <= 0) {
            JOptionPane.showMessageDialog(vista,
                    "No hay existencias de \"" + p.getNombre() + "\".",
                    "Sin stock", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Si ya estaba en el carrito, sumamos uno mas (sin pasarnos del stock)
        DetalleVenta existente = buscarEnCarrito(p.getClave());
        if (existente != null) {
            if (existente.getCantidad() + 1 > p.getCantidad()) {
                JOptionPane.showMessageDialog(vista,
                        "No hay suficientes existencias para agregar otra unidad.",
                        "Stock insuficiente", JOptionPane.WARNING_MESSAGE);
                return;
            }
            existente.setCantidad(existente.getCantidad() + 1);
        } else {
            carrito.add(new DetalleVenta(p.getClave(), 1, p.getPrecio()));
        }
        refrescarCarrito();
    }

    /** Quita la fila seleccionada del carrito. */
    private void quitarDelCarrito() {
        int fila = vista.getTablaCarrito().getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(vista,
                    "Selecciona un renglon del carrito primero.",
                    "Sin seleccion", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        carrito.remove(fila);
        refrescarCarrito();
    }

    /** Devuelve el renglon del carrito con esa clave, o null. */
    private DetalleVenta buscarEnCarrito(String clave) {
        for (DetalleVenta d : carrito) {
            if (d.getClaveProducto().equals(clave)) {
                return d;
            }
        }
        return null;
    }

    /** Repinta el carrito y recalcula totales. */
    private void refrescarCarrito() {
        DefaultTableModel modelo = vista.getModeloCarrito();
        modelo.setRowCount(0);
        double subtotal = 0;
        for (DetalleVenta d : carrito) {
            String nombre = nombreDeClave(d.getClaveProducto());
            modelo.addRow(new Object[]{
                nombre,
                d.getCantidad(),
                String.format("$ %.2f", d.getSubtotal())
            });
            subtotal += d.getSubtotal();
        }
        double impuestos = subtotal * IVA;
        double total = subtotal + impuestos;
        vista.getLblSubtotal().setText(String.format("$ %.2f", subtotal));
        vista.getLblImpuestos().setText(String.format("$ %.2f", impuestos));
        vista.getLblTotal().setText(String.format("$ %.2f", total));
    }

    /** Busca el nombre del producto con esa clave en el ABB. */
    private String nombreDeClave(String clave) {
        Producto patron = new Producto();
        patron.setClave(clave);
        Producto p = arbol.buscar(patron);
        return p != null ? p.getNombre() : clave;
    }

    // ============================================================
    // CONFIRMAR VENTA
    // ============================================================

    /** Calcula totales, guarda la venta en BD y limpia el carrito. */
    private void realizarVenta() {
        if (carrito.isEmpty()) {
            JOptionPane.showMessageDialog(vista,
                    "El carrito esta vacio.",
                    "Sin productos", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        double subtotal = 0;
        for (DetalleVenta d : carrito) {
            subtotal += d.getSubtotal();
        }
        double impuestos = subtotal * IVA;
        double total = subtotal + impuestos;

        Venta venta = new Venta();
        venta.setFecha(LocalDateTime.now());
        venta.setSubtotal(subtotal);
        venta.setImpuestos(impuestos);
        venta.setTotal(total);
        venta.setIdUsuario(usuarioActual.getId());
        venta.setDetalles(new ArrayList<>(carrito));

        try {
            int idGenerado = ventaDAO.registrarVenta(venta);
            JOptionPane.showMessageDialog(vista,
                    String.format("Venta #%d realizada por $ %.2f.", idGenerado, total),
                    "Venta exitosa", JOptionPane.INFORMATION_MESSAGE);
            carrito.clear();
            refrescarCarrito();
            // Recargamos el ABB porque el stock cambio
            cargarDesdeBaseDeDatos();
        } catch (SQLException ex) {
            mostrarError("No se pudo registrar la venta", ex);
        }
    }

    private void mostrarError(String contexto, SQLException ex) {
        JOptionPane.showMessageDialog(vista,
                contexto + ":\n" + ex.getMessage(),
                "Error de base de datos",
                JOptionPane.ERROR_MESSAGE);
    }
}
