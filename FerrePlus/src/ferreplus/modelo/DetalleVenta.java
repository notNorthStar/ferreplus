package ferreplus.modelo;

/**
 * Renglon dentro de una {@link Venta}: representa un producto vendido
 * con su cantidad, su precio en el momento de la venta y el subtotal
 * de ese renglon.
 *
 * <p>Se guarda el {@code precioUnitario} aunque ya este en
 * {@link Producto} porque el precio puede cambiar despues y los tickets
 * historicos deben conservar el precio original.</p>
 *
 * @author Sosa Fernandez Luis David
 * @author Gutierrez Colorado Oliver
 */
public class DetalleVenta {

    /** Id autogenerado del renglon. */
    private int id;

    /** Id de la venta a la que pertenece este renglon. */
    private int idVenta;

    /** Clave del producto vendido. */
    private String claveProducto;

    /** Cantidad vendida de ese producto. */
    private int cantidad;

    /** Precio del producto al momento de la venta (puede diferir del actual). */
    private double precioUnitario;

    /** Subtotal del renglon = cantidad * precioUnitario. */
    private double subtotal;

    /** Constructor vacio para uso del DAO. */
    public DetalleVenta() {
    }

    /**
     * Constructor para construir el renglon en memoria antes de guardarlo.
     *
     * @param claveProducto   clave del producto
     * @param cantidad        cantidad vendida
     * @param precioUnitario  precio en el momento de la venta
     */
    public DetalleVenta(String claveProducto, int cantidad, double precioUnitario) {
        this.claveProducto = claveProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = cantidad * precioUnitario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public String getClaveProducto() {
        return claveProducto;
    }

    public void setClaveProducto(String claveProducto) {
        this.claveProducto = claveProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    /**
     * Cambia la cantidad y recalcula el subtotal de este renglon.
     *
     * @param cantidad nueva cantidad
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.subtotal = cantidad * precioUnitario;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
        this.subtotal = cantidad * precioUnitario;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}
