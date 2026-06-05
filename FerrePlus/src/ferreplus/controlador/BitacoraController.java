package ferreplus.controlador;

import ferreplus.dao.BitacoraDAO;
import ferreplus.modelo.Bitacora;
import ferreplus.vista.BitacoraView;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Controlador del modal {@link BitacoraView}.
 *
 * <p>Solo carga los datos al abrirse. La vista es read-only por
 * requerimiento (RNF-S03).</p>
 *
 * @author Sosa Fernandez Luis David
 */
public class BitacoraController {

    private final BitacoraView vista;
    private final BitacoraDAO bitacoraDAO;

    /** Cache simple de id_usuario -> username para no consultar de mas. */
    private final Map<Integer, String> cacheUsuarios = new HashMap<>();

    /** Formato fecha amigable para mostrar en la tabla. */
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Engancha el cierre y carga la tabla al instante.
     *
     * @param vista       modal de bitacora
     * @param bitacoraDAO DAO compartido con el modulo de inventario
     */
    public BitacoraController(BitacoraView vista, BitacoraDAO bitacoraDAO) {
        this.vista = vista;
        this.bitacoraDAO = bitacoraDAO;
        cargar();
    }

    /** Llena la tabla con todas las entradas de la bitacora. */
    private void cargar() {
        try {
            List<Bitacora> entradas = bitacoraDAO.listarTodas();
            DefaultTableModel modelo = vista.getModelo();
            modelo.setRowCount(0);
            for (Bitacora b : entradas) {
                String fecha = b.getFecha() == null ? "" : b.getFecha().format(FMT);
                modelo.addRow(new Object[]{
                    b.getClaveProducto(),
                    b.getNombreProducto(),
                    b.getMotivo(),
                    fecha,
                    obtenerUsername(b.getIdUsuario())
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vista,
                    "No se pudo cargar la bitacora:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Devuelve el username del id dado, usando el cache para no repetir consultas. */
    private String obtenerUsername(int idUsuario) {
        if (cacheUsuarios.containsKey(idUsuario)) {
            return cacheUsuarios.get(idUsuario);
        }
        try {
            String username = bitacoraDAO.obtenerUsername(idUsuario);
            cacheUsuarios.put(idUsuario, username);
            return username;
        } catch (SQLException ex) {
            return "(id " + idUsuario + ")";
        }
    }
}
