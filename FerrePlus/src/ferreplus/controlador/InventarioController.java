package ferreplus.controlador;

import ferreplus.dao.BitacoraDAO;
import ferreplus.dao.ProductoDAO;
import ferreplus.modelo.Bitacora;
import ferreplus.modelo.Producto;
import ferreplus.modelo.Usuario;
import ferreplus.util.ABB;
import ferreplus.util.OrdenamientoUtil;
import ferreplus.util.Validador;
import ferreplus.vista.BitacoraView;
import ferreplus.vista.EliminarProductoModal;
import ferreplus.vista.InventarioView;
import ferreplus.vista.ProductoModal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Controlador del modulo de Inventario.
 *
 * <p>Tiene un {@link ABB} de productos en memoria que se carga desde la
 * base de datos al abrir la vista. Las acciones de buscar y mostrar por
 * defecto pasan por el ABB; las acciones de agregar/editar/eliminar
 * pegan a la base de datos y despues refrescan el ABB para mantener
 * todo consistente.</p>
 *
 * @author Sosa Fernandez Luis David
 */
public class InventarioController {

    private final InventarioView vista;
    private final Usuario usuarioActual;
    private final ProductoDAO productoDAO = new ProductoDAO();
    private final BitacoraDAO bitacoraDAO = new BitacoraDAO();

    /** Arbol Binario de Busqueda en memoria con los productos (RF-07). */
    private final ABB<Producto> arbol = new ABB<>();

    /** Lista actualmente mostrada en la tabla (para reordenar in-place). */
    private List<Producto> listaActual = new ArrayList<>();

    /**
     * Engancha los listeners de la vista y carga los productos.
     *
     * @param vista          ventana de inventario
     * @param usuarioActual  usuario con sesion abierta
     */
    public InventarioController(InventarioView vista, Usuario usuarioActual) {
        this.vista = vista;
        this.usuarioActual = usuarioActual;

        vista.getBtnAgregar().addActionListener(e -> abrirAgregar());
        vista.getBtnEditar().addActionListener(e -> abrirEditar());
        vista.getBtnEliminar().addActionListener(e -> abrirEliminar());
        vista.getBtnLog().addActionListener(e -> abrirBitacora());
        vista.getBtnBuscar().addActionListener(e -> buscar());
        vista.getTxtBuscar().addActionListener(e -> buscar());
        vista.getBtnDefault().addActionListener(e -> mostrarInorden());
        vista.getBtnOrdNombre().addActionListener(e -> ordenarPorNombre());
        vista.getBtnOrdPrecio().addActionListener(e -> ordenarPorPrecio());
        vista.getBtnRegresar().addActionListener(e -> vista.dispose());

        cargarDesdeBaseDeDatos();
    }

    // ============================================================
    // CARGA INICIAL + REFRESCO
    // ============================================================

    /** Lee todos los productos de la BD y los mete al ABB. */
    private void cargarDesdeBaseDeDatos() {
        try {
            arbol.limpiar();
            List<Producto> todos = productoDAO.listarTodos();
            for (Producto p : todos) {
                arbol.insertar(p);
            }
            mostrarInorden();
        } catch (SQLException ex) {
            mostrarError("No se pudieron cargar los productos", ex);
        }
    }

    /** Muestra los productos en el orden Inorden del ABB (RF-08). */
    private void mostrarInorden() {
        listaActual = arbol.recorrerInorden();
        pintarTabla(listaActual);
    }

    /** Vuelca la lista dada a la tabla, sobreescribiendo lo que hubiera. */
    private void pintarTabla(List<Producto> lista) {
        DefaultTableModel modelo = vista.getModelo();
        modelo.setRowCount(0);
        for (Producto p : lista) {
            modelo.addRow(new Object[]{
                p.getClave(),
                p.getNombre(),
                p.getCantidad(),
                p.getUbicacion(),
                String.format("$ %.2f", p.getPrecio())
            });
        }
        vista.getLblConteo().setText(lista.size() + " productos");
    }

    // ============================================================
    // BUSCAR (RF-11)
    // ============================================================

    /** Decide si la busqueda es por clave (9 digitos) o por nombre. */
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
            // Busqueda parcial por nombre: recorremos el ABB Inorden y filtramos
            listaActual = new ArrayList<>();
            String textoMin = texto.toLowerCase();
            for (Producto p : arbol.recorrerInorden()) {
                if (p.getNombre() != null
                        && p.getNombre().toLowerCase().contains(textoMin)) {
                    listaActual.add(p);
                }
            }
        }
        pintarTabla(listaActual);
    }

    // ============================================================
    // ORDENAR (RF-09, RF-10)
    // ============================================================

    /** Ordena la lista actual por nombre con QuickSort. */
    private void ordenarPorNombre() {
        OrdenamientoUtil.quickSortPorNombre(listaActual);
        pintarTabla(listaActual);
    }

    /** Ordena la lista actual por precio con MergeSort. */
    private void ordenarPorPrecio() {
        OrdenamientoUtil.mergeSortPorPrecio(listaActual);
        pintarTabla(listaActual);
    }

    // ============================================================
    // CRUD (RF-03, RF-04, RF-05)
    // ============================================================

    /** Abre el modal en modo "Agregar". */
    private void abrirAgregar() {
        ProductoModal modal = new ProductoModal(vista, null);
        modal.setVisible(true);
        if (!modal.fueConfirmado()) return;

        Producto p = leerModal(modal);
        if (p == null) return;

        try {
            if (productoDAO.existeClave(p.getClave())) {
                JOptionPane.showMessageDialog(vista,
                        "Ya existe un producto con esa clave.",
                        "Clave duplicada", JOptionPane.WARNING_MESSAGE);
                return;
            }
            productoDAO.insertar(p);
            arbol.insertar(p);
            mostrarInorden();
            JOptionPane.showMessageDialog(vista, "Producto agregado.",
                    "Listo", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            mostrarError("No se pudo guardar el producto", ex);
        }
    }

    /** Abre el modal en modo "Editar" con el producto seleccionado. */
    private void abrirEditar() {
        Producto seleccionado = obtenerProductoSeleccionado();
        if (seleccionado == null) return;

        ProductoModal modal = new ProductoModal(vista, seleccionado);
        modal.setVisible(true);
        if (!modal.fueConfirmado()) return;

        Producto editado = leerModal(modal);
        if (editado == null) return;

        try {
            productoDAO.actualizar(editado);
            // Actualizar in-place el producto del ABB (la clave no cambio)
            seleccionado.setNombre(editado.getNombre());
            seleccionado.setCantidad(editado.getCantidad());
            seleccionado.setUbicacion(editado.getUbicacion());
            seleccionado.setPrecio(editado.getPrecio());
            seleccionado.setFoto(editado.getFoto());
            pintarTabla(listaActual);
            JOptionPane.showMessageDialog(vista, "Producto actualizado.",
                    "Listo", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            mostrarError("No se pudo actualizar el producto", ex);
        }
    }

    /** Abre el modal de eliminar con captura de motivo y registra en bitacora. */
    private void abrirEliminar() {
        Producto seleccionado = obtenerProductoSeleccionado();
        if (seleccionado == null) return;

        EliminarProductoModal modal = new EliminarProductoModal(vista, seleccionado);
        modal.setVisible(true);
        if (!modal.fueConfirmado()) return;

        String motivo = modal.getMotivo();
        if (!Validador.noVacio(motivo)) {
            JOptionPane.showMessageDialog(vista,
                    "Debes indicar la razon de la eliminacion.",
                    "Motivo requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // 1) Registrar en bitacora primero (RF-06)
            Bitacora b = new Bitacora();
            b.setClaveProducto(seleccionado.getClave());
            b.setNombreProducto(seleccionado.getNombre());
            b.setMotivo(motivo);
            b.setFecha(LocalDateTime.now());
            b.setIdUsuario(usuarioActual.getId());
            bitacoraDAO.insertar(b);

            // 2) Borrar el producto de la BD y del ABB
            productoDAO.eliminar(seleccionado.getClave());
            Producto patron = new Producto();
            patron.setClave(seleccionado.getClave());
            arbol.eliminar(patron);
            mostrarInorden();

            JOptionPane.showMessageDialog(vista, "Producto eliminado y registrado en bitacora.",
                    "Listo", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            mostrarError("No se pudo eliminar el producto", ex);
        }
    }

    /** Abre la ventana de bitacora (RF-06). */
    private void abrirBitacora() {
        BitacoraView dlg = new BitacoraView(vista);
        new BitacoraController(dlg, bitacoraDAO);
        dlg.setVisible(true);
    }

    // ============================================================
    // VALIDACION + LECTURA DEL MODAL
    // ============================================================

    /**
     * Lee los campos del modal de Producto y devuelve un Producto valido,
     * o {@code null} si la validacion falla (en cuyo caso ya se mostro
     * un mensaje al usuario).
     */
    private Producto leerModal(ProductoModal modal) {
        String clave     = modal.getClave();
        String nombre    = modal.getNombre();
        String cantidad  = modal.getCantidad();
        String precio    = modal.getPrecio();
        String ubicacion = modal.getUbicacion();

        if (!Validador.claveValida(clave)) {
            JOptionPane.showMessageDialog(vista,
                    "La clave debe tener exactamente 9 digitos numericos.",
                    "Clave invalida", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        if (!Validador.noVacio(nombre)) {
            JOptionPane.showMessageDialog(vista,
                    "El nombre no puede estar vacio.",
                    "Nombre invalido", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        if (!Validador.enteroNoNegativo(cantidad)) {
            JOptionPane.showMessageDialog(vista,
                    "La cantidad debe ser un numero entero mayor o igual a cero.",
                    "Cantidad invalida", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        if (!Validador.decimalNoNegativo(precio)) {
            JOptionPane.showMessageDialog(vista,
                    "El precio debe ser un numero mayor o igual a cero.",
                    "Precio invalido", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        if (!Validador.noVacio(ubicacion)) {
            JOptionPane.showMessageDialog(vista,
                    "La ubicacion no puede estar vacia.",
                    "Ubicacion invalida", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        return new Producto(
                clave, nombre,
                Validador.parsearEntero(cantidad, 0),
                ubicacion,
                Validador.parsearDecimal(precio, 0.0),
                modal.getFotoBytes());
    }

    /**
     * Devuelve el producto correspondiente a la fila seleccionada
     * o {@code null} si no hay seleccion (mostrando un aviso).
     */
    private Producto obtenerProductoSeleccionado() {
        int fila = vista.getTabla().getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(vista,
                    "Selecciona un producto de la tabla primero.",
                    "Sin seleccion", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        return listaActual.get(fila);
    }

    /** Muestra un dialogo de error con la causa SQL. */
    private void mostrarError(String contexto, SQLException ex) {
        JOptionPane.showMessageDialog(vista,
                contexto + ":\n" + ex.getMessage(),
                "Error de base de datos",
                JOptionPane.ERROR_MESSAGE);
    }
}
