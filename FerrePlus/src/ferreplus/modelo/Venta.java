package ferreplus.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Cabecera de una venta realizada desde el modulo de Ventas.
 *
 * <p>Una venta agrupa varios {@link DetalleVenta} (los renglones del ticket)
 * y guarda el resumen de subtotal, impuestos y total para no tener que
 * recalcularlos cada vez que se consulta el historico.</p>
 *
 * @author Sosa Fernandez Luis David
 * @author Gutierrez Colorado Oliver
 */
public class Venta {

    /** Id autogenerado de la venta. */
    private int id;

    /** Fecha y hora en que se registro la venta. */
    private LocalDateTime fecha;

    /** Suma de los subtotales de los renglones, sin impuestos. */
    private double subtotal;

    /** Monto de impuestos aplicados a la venta. */
    private double impuestos;

    /** Total a cobrar al cliente (subtotal + impuestos). */
    private double total;

    /** Id del usuario que realizo la venta. */
    private int idUsuario;

    /** Renglones del ticket. Se inicializa vacio para evitar NPEs. */
    private List<DetalleVenta> detalles = new ArrayList<>();

    /** Constructor vacio para uso del DAO. */
    public Venta() {
    }

    /**
     * Constructor con los campos principales (sin id porque lo genera la BD).
     *
     * @param fecha      fecha y hora
     * @param subtotal   subtotal calculado
     * @param impuestos  impuestos calculados
     * @param total      total a cobrar
     * @param idUsuario  usuario que realiza la venta
     */
    public Venta(LocalDateTime fecha, double subtotal, double impuestos,
                 double total, int idUsuario) {
        this.fecha = fecha;
        this.subtotal = subtotal;
        this.impuestos = impuestos;
        this.total = total;
        this.idUsuario = idUsuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(double impuestos) {
        this.impuestos = impuestos;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }

    @Override
    public String toString() {
        return "Venta{id=" + id + ", total=" + total
                + ", renglones=" + detalles.size() + "}";
    }
}
