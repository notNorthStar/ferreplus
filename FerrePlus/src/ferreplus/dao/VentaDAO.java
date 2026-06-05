package ferreplus.dao;

import ferreplus.modelo.DetalleVenta;
import ferreplus.modelo.Venta;
import ferreplus.util.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.ResultSet;

/**
 * Acceso a datos de las tablas {@code ventas} y {@code detalle_venta}.
 *
 * <p>El registro de una venta involucra dos tablas y la actualizacion del
 * stock en {@code productos}. Para que el ticket quede consistente
 * (o se guarda todo, o no se guarda nada) se usa una transaccion: se
 * desactiva el autocommit, se hacen los inserts y los updates, y al
 * final se hace commit o rollback segun salga la cosa.</p>
 *
 * @author Sosa Fernandez Luis David
 */
public class VentaDAO {

    /** DAO de productos para descontar stock como parte de la transaccion. */
    private final ProductoDAO productoDAO = new ProductoDAO();

    /**
     * Inserta la cabecera de la venta, todos los renglones del carrito
     * y descuenta el stock correspondiente, todo en una sola transaccion.
     *
     * @param venta venta con sus detalles ya cargados en la lista interna
     * @return id autogenerado de la venta, o -1 si algo fallo
     * @throws SQLException si MySQL falla; se hace rollback antes de relanzar
     */
    public int registrarVenta(Venta venta) throws SQLException {
        Connection con = ConexionBD.getConexion();
        boolean autoCommitOriginal = con.getAutoCommit();
        con.setAutoCommit(false);

        int idGenerado = -1;
        try {
            idGenerado = insertarCabecera(con, venta);
            venta.setId(idGenerado);

            for (DetalleVenta d : venta.getDetalles()) {
                d.setIdVenta(idGenerado);
                insertarDetalle(con, d);
                productoDAO.descontarStock(d.getClaveProducto(), d.getCantidad());
            }

            con.commit();
            return idGenerado;
        } catch (SQLException ex) {
            con.rollback();
            throw ex;
        } finally {
            con.setAutoCommit(autoCommitOriginal);
        }
    }

    /** Inserta la cabecera y devuelve el id autogenerado. */
    private int insertarCabecera(Connection con, Venta venta) throws SQLException {
        String sql = "INSERT INTO ventas (fecha, subtotal, impuestos, total, id_usuario) "
                   + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setTimestamp(1, Timestamp.valueOf(venta.getFecha()));
            ps.setDouble(2, venta.getSubtotal());
            ps.setDouble(3, venta.getImpuestos());
            ps.setDouble(4, venta.getTotal());
            ps.setInt(5, venta.getIdUsuario());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    /** Inserta un renglon del ticket. */
    private void insertarDetalle(Connection con, DetalleVenta d) throws SQLException {
        String sql = "INSERT INTO detalle_venta "
                   + "(id_venta, clave_producto, cantidad, precio_unitario, subtotal) "
                   + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, d.getIdVenta());
            ps.setString(2, d.getClaveProducto());
            ps.setInt(3, d.getCantidad());
            ps.setDouble(4, d.getPrecioUnitario());
            ps.setDouble(5, d.getSubtotal());
            ps.executeUpdate();
        }
    }
}
