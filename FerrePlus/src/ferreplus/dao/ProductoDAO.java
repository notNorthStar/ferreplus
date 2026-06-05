package ferreplus.dao;

import ferreplus.modelo.Producto;
import ferreplus.util.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos de la tabla {@code productos}.
 *
 * <p>Expone las operaciones que necesita el modulo de Inventario:</p>
 * <ul>
 *   <li>{@link #listarTodos()} - usado al cargar la vista y reconstruir el ABB.</li>
 *   <li>{@link #insertar(Producto)} - RF-03.</li>
 *   <li>{@link #actualizar(Producto)} - RF-04.</li>
 *   <li>{@link #eliminar(String)} - RF-05 (la bitacora se inserta por separado).</li>
 *   <li>{@link #existeClave(String)} - para validar antes de insertar.</li>
 *   <li>{@link #descontarStock(String, int)} - para el modulo de Ventas.</li>
 * </ul>
 *
 * <p>La foto del producto se mapea a {@code LONGBLOB} via {@code setBytes}
 * y {@code getBytes}, asi que la clase {@code Producto} guarda la imagen
 * como {@code byte[]} (ver {@link Producto#getFoto()}).</p>
 *
 * @author Sosa Fernandez Luis David
 */
public class ProductoDAO {

    /**
     * Devuelve todos los productos almacenados, en el orden natural de
     * la base de datos. La vista los re-acomoda usando el ABB para
     * mostrar el Inorden (RF-08).
     *
     * @return lista de productos (puede estar vacia)
     * @throws SQLException si MySQL falla
     */
    public List<Producto> listarTodos() throws SQLException {
        String sql = "SELECT clave, nombre, cantidad, ubicacion, precio, foto FROM productos";
        List<Producto> lista = new ArrayList<>();

        Connection con = ConexionBD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearFila(rs));
            }
        }
        return lista;
    }

    /**
     * Busca un producto por clave directamente en la base de datos.
     * Se usa cuando todavia no se ha cargado el ABB en memoria.
     *
     * @param clave clave de 9 digitos
     * @return el producto o {@code null} si no existe
     * @throws SQLException si MySQL falla
     */
    public Producto buscarPorClave(String clave) throws SQLException {
        String sql = "SELECT clave, nombre, cantidad, ubicacion, precio, foto "
                   + "FROM productos WHERE clave = ?";
        Connection con = ConexionBD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, clave);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearFila(rs);
                }
            }
        }
        return null;
    }

    /**
     * Comprueba si una clave ya esta usada. Se llama antes de insertar
     * para dar un mensaje claro al usuario sin esperar a que MySQL
     * lance el error de clave duplicada.
     *
     * @param clave clave a verificar
     * @return {@code true} si ya existe
     * @throws SQLException si MySQL falla
     */
    public boolean existeClave(String clave) throws SQLException {
        String sql = "SELECT 1 FROM productos WHERE clave = ? LIMIT 1";
        Connection con = ConexionBD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, clave);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Inserta un producto nuevo (RF-03).
     *
     * @param p producto a insertar (clave, nombre, cantidad, ubicacion,
     *          precio y foto opcional)
     * @throws SQLException si MySQL falla o la clave ya existe
     */
    public void insertar(Producto p) throws SQLException {
        String sql = "INSERT INTO productos "
                   + "(clave, nombre, cantidad, ubicacion, precio, foto) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        Connection con = ConexionBD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getClave());
            ps.setString(2, p.getNombre());
            ps.setInt(3, p.getCantidad());
            ps.setString(4, p.getUbicacion());
            ps.setDouble(5, p.getPrecio());
            if (p.getFoto() != null) {
                ps.setBytes(6, p.getFoto());
            } else {
                ps.setNull(6, java.sql.Types.LONGVARBINARY);
            }
            ps.executeUpdate();
        }
    }

    /**
     * Actualiza un producto existente (RF-04). La clave NO se modifica.
     *
     * @param p producto con los datos nuevos
     * @throws SQLException si MySQL falla
     */
    public void actualizar(Producto p) throws SQLException {
        String sql = "UPDATE productos "
                   + "SET nombre = ?, cantidad = ?, ubicacion = ?, precio = ?, foto = ? "
                   + "WHERE clave = ?";
        Connection con = ConexionBD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setInt(2, p.getCantidad());
            ps.setString(3, p.getUbicacion());
            ps.setDouble(4, p.getPrecio());
            if (p.getFoto() != null) {
                ps.setBytes(5, p.getFoto());
            } else {
                ps.setNull(5, java.sql.Types.LONGVARBINARY);
            }
            ps.setString(6, p.getClave());
            ps.executeUpdate();
        }
    }

    /**
     * Elimina un producto por su clave (RF-05). El registro en la
     * bitacora se inserta aparte para que esta clase no sepa de
     * bitacoras (separacion de responsabilidades).
     *
     * @param clave clave del producto a eliminar
     * @throws SQLException si MySQL falla
     */
    public void eliminar(String clave) throws SQLException {
        String sql = "DELETE FROM productos WHERE clave = ?";
        Connection con = ConexionBD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, clave);
            ps.executeUpdate();
        }
    }

    /**
     * Resta una cantidad al stock del producto. Se usa al confirmar una
     * venta (modulo Ventas).
     *
     * @param clave    clave del producto
     * @param cantidad unidades a descontar
     * @throws SQLException si MySQL falla
     */
    public void descontarStock(String clave, int cantidad) throws SQLException {
        String sql = "UPDATE productos SET cantidad = cantidad - ? WHERE clave = ?";
        Connection con = ConexionBD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cantidad);
            ps.setString(2, clave);
            ps.executeUpdate();
        }
    }

    // ============================================================
    // HELPER
    // ============================================================

    /** Convierte una fila del ResultSet a un objeto Producto. */
    private Producto mapearFila(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setClave(rs.getString("clave"));
        p.setNombre(rs.getString("nombre"));
        p.setCantidad(rs.getInt("cantidad"));
        p.setUbicacion(rs.getString("ubicacion"));
        p.setPrecio(rs.getDouble("precio"));
        p.setFoto(rs.getBytes("foto"));
        return p;
    }
}
