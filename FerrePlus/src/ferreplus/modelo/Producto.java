package ferreplus.modelo;

import java.util.Objects;

/**
 * Producto del inventario de la ferreteria.
 *
 * <p>La clave es de exactamente 9 digitos y nunca cambia despues de crearse
 * (regla de negocio RF-04). Por eso al modificarse un producto solo se
 * tocan nombre, cantidad, ubicacion, precio y foto.</p>
 *
 * <p>La foto se guarda como arreglo de bytes para que el DAO pueda mapearla
 * directamente a un {@code LONGBLOB} de MySQL.</p>
 *
 * @author Sosa Fernandez Luis David
 * @author Gutierrez Colorado Oliver
 */
public class Producto {

    /** Clave de 9 digitos. Llave primaria. */
    private String clave;

    /** Nombre comercial del producto. */
    private String nombre;

    /** Existencias actuales en el almacen. */
    private int cantidad;

    /** Ubicacion fisica del producto: estante / pasillo / almacen. */
    private String ubicacion;

    /** Precio de venta al publico. */
    private double precio;

    /** Imagen ilustrativa serializada como bytes. Puede ser null. */
    private byte[] foto;

    /** Constructor vacio para uso del DAO. */
    public Producto() {
    }

    /**
     * Constructor con todos los campos.
     *
     * @param clave     clave de 9 digitos
     * @param nombre    nombre comercial
     * @param cantidad  existencias
     * @param ubicacion estante / almacen
     * @param precio    precio de venta
     * @param foto      imagen ilustrativa como bytes (puede ser null)
     */
    public Producto(String clave, String nombre, int cantidad,
                    String ubicacion, double precio, byte[] foto) {
        this.clave = clave;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.ubicacion = ubicacion;
        this.precio = precio;
        this.foto = foto;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    /**
     * Dos productos se consideran iguales si tienen la misma clave,
     * ya que la clave es unica en todo el inventario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Producto)) {
            return false;
        }
        Producto p = (Producto) o;
        return Objects.equals(clave, p.clave);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clave);
    }

    @Override
    public String toString() {
        return "Producto{clave='" + clave + "', nombre='" + nombre
                + "', cantidad=" + cantidad + ", precio=" + precio + "}";
    }
}
