package ferreplus.dao;

import ferreplus.modelo.Bitacora;
import ferreplus.util.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos de la tabla {@code bitacora_eliminaciones} (RF-06).
 *
 * <p>Solo expone {@link #insertar(Bitacora)} y {@link #listarTodas()}:
 * la bitacora es de solo lectura desde la interfaz (RNF-S03), por lo
 * que no hay update ni delete a proposito.</p>
 *
 * @author Sosa Fernandez Luis David
 */
public class BitacoraDAO {

    /**
     * Inserta un registro nuevo en la bitacora. Se llama desde el
     * controlador justo antes (o justo despues) de borrar el producto.
     *
     * @param entrada datos del evento (clave, nombre, motivo, fecha, usuario)
     * @throws SQLException si MySQL falla
     */
    public void insertar(Bitacora entrada) throws SQLException {
        String sql = "INSERT INTO bitacora_eliminaciones "
                   + "(clave_producto, nombre_producto, motivo, fecha, id_usuario) "
                   + "VALUES (?, ?, ?, ?, ?)";
        Connection con = ConexionBD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entrada.getClaveProducto());
            ps.setString(2, entrada.getNombreProducto());
            ps.setString(3, entrada.getMotivo());
            ps.setTimestamp(4, Timestamp.valueOf(entrada.getFecha()));
            ps.setInt(5, entrada.getIdUsuario());
            ps.executeUpdate();
        }
    }

    /**
     * Devuelve todas las entradas de la bitacora ordenadas por fecha
     * descendente (las mas nuevas arriba).
     *
     * @return lista de registros (puede estar vacia)
     * @throws SQLException si MySQL falla
     */
    public List<Bitacora> listarTodas() throws SQLException {
        String sql = "SELECT id, clave_producto, nombre_producto, motivo, "
                   + "fecha, id_usuario "
                   + "FROM bitacora_eliminaciones "
                   + "ORDER BY fecha DESC";
        List<Bitacora> lista = new ArrayList<>();

        Connection con = ConexionBD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Bitacora b = new Bitacora();
                b.setId(rs.getInt("id"));
                b.setClaveProducto(rs.getString("clave_producto"));
                b.setNombreProducto(rs.getString("nombre_producto"));
                b.setMotivo(rs.getString("motivo"));
                Timestamp ts = rs.getTimestamp("fecha");
                if (ts != null) {
                    b.setFecha(ts.toLocalDateTime());
                } else {
                    b.setFecha(LocalDateTime.now());
                }
                b.setIdUsuario(rs.getInt("id_usuario"));
                lista.add(b);
            }
        }
        return lista;
    }

    /**
     * Devuelve el nombre del usuario que hizo cada eliminacion, util
     * para mostrarlo junto a la fecha en la vista de bitacora.
     *
     * @param idUsuario id del usuario
     * @return username, o cadena vacia si no se encuentra
     * @throws SQLException si MySQL falla
     */
    public String obtenerUsername(int idUsuario) throws SQLException {
        String sql = "SELECT username FROM usuarios WHERE id = ?";
        Connection con = ConexionBD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("username");
                }
            }
        }
        return "";
    }
}
