package ferreplus.modelo;

import java.time.LocalDateTime;

/**
 * Entrada de la bitacora de eliminaciones de productos.
 *
 * <p>Cuando un usuario elimina un producto (RF-05) se debe dejar registrado
 * por que lo elimino, quien lo elimino y cuando lo elimino (RF-06).
 * La bitacora no se puede editar ni borrar desde la UI (RNF-S03), por eso
 * esta clase es practicamente de solo lectura una vez que se inserta.</p>
 *
 * @author Gutierrez Colorado Oliver
 * @author Sosa Fernandez Luis David
 */
public class Bitacora {

    /** Id autogenerado en la base de datos. */
    private int id;

    /** Clave del producto eliminado (no es FK porque el producto ya no existe). */
    private String claveProducto;

    /** Nombre que tenia el producto al momento de eliminarlo. */
    private String nombreProducto;

    /** Razon que dio el usuario para eliminar el producto. */
    private String motivo;

    /** Fecha y hora exacta de la eliminacion. */
    private LocalDateTime fecha;

    /** Id del usuario que ejecuto la eliminacion. */
    private int idUsuario;

    /** Constructor vacio para uso del DAO. */
    public Bitacora() {
    }

    /**
     * Constructor con todos los campos.
     *
     * @param id              id en la tabla
     * @param claveProducto   clave del producto eliminado
     * @param nombreProducto  nombre del producto al momento del borrado
     * @param motivo          razon de la eliminacion
     * @param fecha           fecha y hora del evento
     * @param idUsuario       usuario que ejecuto la accion
     */
    public Bitacora(int id, String claveProducto, String nombreProducto,
                    String motivo, LocalDateTime fecha, int idUsuario) {
        this.id = id;
        this.claveProducto = claveProducto;
        this.nombreProducto = nombreProducto;
        this.motivo = motivo;
        this.fecha = fecha;
        this.idUsuario = idUsuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClaveProducto() {
        return claveProducto;
    }

    public void setClaveProducto(String claveProducto) {
        this.claveProducto = claveProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public String toString() {
        return "Bitacora{clave='" + claveProducto + "', motivo='"
                + motivo + "', fecha=" + fecha + "}";
    }
}
