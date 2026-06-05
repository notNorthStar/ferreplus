package ferreplus.util;

/**
 * Validaciones reutilizables para los formularios de Ferre+.
 *
 * <p>Centraliza las reglas de validacion para evitar repetir las mismas
 * comprobaciones en cada modal/controlador y para que los mensajes de
 * error sean consistentes (RNF-C03, RNF-C04).</p>
 *
 * @author Sosa Fernandez Luis David
 */
public class Validador {

    /** Constructor privado: clase de utilidad. */
    private Validador() {
    }

    /**
     * Comprueba que la clave del producto tenga exactamente 9 digitos
     * numericos (RF-03, RNF-S04).
     *
     * @param clave clave a validar
     * @return {@code true} si cumple el formato
     */
    public static boolean claveValida(String clave) {
        return clave != null && clave.matches("^[0-9]{9}$");
    }

    /**
     * Comprueba que un texto no sea nulo ni este compuesto solo de espacios.
     *
     * @param texto texto a validar
     * @return {@code true} si tiene al menos un caracter visible
     */
    public static boolean noVacio(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }

    /**
     * Intenta parsear un texto a entero y verifica que sea mayor o igual
     * a cero. Se usa para cantidades de inventario.
     *
     * @param texto texto que deberia ser un entero
     * @return {@code true} si parsea y es {@code >= 0}
     */
    public static boolean enteroNoNegativo(String texto) {
        if (!noVacio(texto)) {
            return false;
        }
        try {
            int valor = Integer.parseInt(texto.trim());
            return valor >= 0;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    /**
     * Intenta parsear un texto a entero y verifica que sea estrictamente
     * positivo. Se usa para cantidades a vender.
     *
     * @param texto texto que deberia ser un entero
     * @return {@code true} si parsea y es {@code > 0}
     */
    public static boolean enteroPositivo(String texto) {
        if (!noVacio(texto)) {
            return false;
        }
        try {
            return Integer.parseInt(texto.trim()) > 0;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    /**
     * Intenta parsear un texto a double y verifica que sea mayor o igual
     * a cero. Se usa para precios.
     *
     * @param texto texto que deberia ser un decimal
     * @return {@code true} si parsea y es {@code >= 0}
     */
    public static boolean decimalNoNegativo(String texto) {
        if (!noVacio(texto)) {
            return false;
        }
        try {
            double valor = Double.parseDouble(texto.trim());
            return valor >= 0;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    /**
     * Convierte un texto a entero sin lanzar excepcion. Si falla devuelve
     * el valor por defecto dado.
     *
     * @param texto       texto a convertir
     * @param porDefecto  valor a devolver si la conversion falla
     * @return entero parseado o valor por defecto
     */
    public static int parsearEntero(String texto, int porDefecto) {
        if (!noVacio(texto)) {
            return porDefecto;
        }
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException ex) {
            return porDefecto;
        }
    }

    /**
     * Convierte un texto a double sin lanzar excepcion. Si falla devuelve
     * el valor por defecto dado.
     *
     * @param texto       texto a convertir
     * @param porDefecto  valor a devolver si la conversion falla
     * @return double parseado o valor por defecto
     */
    public static double parsearDecimal(String texto, double porDefecto) {
        if (!noVacio(texto)) {
            return porDefecto;
        }
        try {
            return Double.parseDouble(texto.trim());
        } catch (NumberFormatException ex) {
            return porDefecto;
        }
    }
}
