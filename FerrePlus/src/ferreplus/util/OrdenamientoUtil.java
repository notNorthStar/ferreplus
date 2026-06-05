package ferreplus.util;

import ferreplus.modelo.Producto;
import java.util.ArrayList;
import java.util.List;

/**
 * Algoritmos de ordenamiento para listas de productos.
 *
 * <p>El proyecto pide dos algoritmos distintos para que cada uno se vea
 * en accion:</p>
 * <ul>
 *   <li>{@link #quickSortPorNombre(List)} - ordena por nombre del producto
 *       usando QuickSort (RF-09).</li>
 *   <li>{@link #mergeSortPorPrecio(List)} - ordena por precio del producto
 *       usando MergeSort (RF-10).</li>
 * </ul>
 *
 * <p>Ambos modifican la lista in-place ("se debera ordenar la lista
 * original"), no devuelven una copia.</p>
 *
 * @author Sosa Fernandez Luis David
 */
public class OrdenamientoUtil {

    /** Constructor privado: clase de utilidad, no se instancia. */
    private OrdenamientoUtil() {
    }

    // ============================================================
    // QUICKSORT POR NOMBRE (RF-09)
    // ============================================================

    /**
     * Ordena la lista de productos por nombre alfabeticamente
     * (sin distinguir mayusculas) usando el algoritmo QuickSort.
     *
     * @param lista lista a ordenar (se modifica en sitio)
     */
    public static void quickSortPorNombre(List<Producto> lista) {
        if (lista == null || lista.size() <= 1) {
            return;
        }
        quickSortRecursivo(lista, 0, lista.size() - 1);
    }

    /**
     * Recursion clasica de QuickSort: particiona y se llama a si misma
     * sobre las dos mitades.
     */
    private static void quickSortRecursivo(List<Producto> lista, int inicio, int fin) {
        if (inicio < fin) {
            int posPivote = particionarPorNombre(lista, inicio, fin);
            quickSortRecursivo(lista, inicio, posPivote - 1);
            quickSortRecursivo(lista, posPivote + 1, fin);
        }
    }

    /**
     * Particion estilo Lomuto: toma como pivote el ultimo elemento y
     * coloca a la izquierda los que son menores o iguales que el pivote.
     *
     * @return posicion final del pivote dentro de la lista
     */
    private static int particionarPorNombre(List<Producto> lista, int inicio, int fin) {
        String pivote = nombreSeguro(lista.get(fin));
        int i = inicio - 1;
        for (int j = inicio; j < fin; j++) {
            if (nombreSeguro(lista.get(j)).compareTo(pivote) <= 0) {
                i++;
                intercambiar(lista, i, j);
            }
        }
        intercambiar(lista, i + 1, fin);
        return i + 1;
    }

    /** Devuelve el nombre en minusculas, o cadena vacia si es nulo. */
    private static String nombreSeguro(Producto p) {
        String n = p.getNombre();
        return n == null ? "" : n.toLowerCase();
    }

    // ============================================================
    // MERGESORT POR PRECIO (RF-10)
    // ============================================================

    /**
     * Ordena la lista de productos por precio ascendente usando el
     * algoritmo MergeSort.
     *
     * @param lista lista a ordenar (se modifica en sitio)
     */
    public static void mergeSortPorPrecio(List<Producto> lista) {
        if (lista == null || lista.size() <= 1) {
            return;
        }
        // Lista auxiliar del mismo tamano que la original; se reutiliza
        // en todas las llamadas recursivas para no estar creando y
        // destruyendo memoria a cada paso.
        List<Producto> auxiliar = new ArrayList<>(lista);
        mergeSortRecursivo(lista, auxiliar, 0, lista.size() - 1);
    }

    /**
     * Recursion clasica de MergeSort: divide a la mitad, ordena cada
     * mitad y al final las mezcla en orden.
     */
    private static void mergeSortRecursivo(List<Producto> lista,
                                           List<Producto> auxiliar,
                                           int inicio, int fin) {
        if (inicio < fin) {
            int medio = (inicio + fin) / 2;
            mergeSortRecursivo(lista, auxiliar, inicio, medio);
            mergeSortRecursivo(lista, auxiliar, medio + 1, fin);
            mezclarPorPrecio(lista, auxiliar, inicio, medio, fin);
        }
    }

    /**
     * Mezcla los rangos {@code [inicio..medio]} y {@code [medio+1..fin]}
     * (que ya estan ordenados) en uno solo dentro de {@code lista}.
     */
    private static void mezclarPorPrecio(List<Producto> lista,
                                         List<Producto> auxiliar,
                                         int inicio, int medio, int fin) {
        // Copiar al auxiliar el segmento que vamos a mezclar
        for (int k = inicio; k <= fin; k++) {
            auxiliar.set(k, lista.get(k));
        }

        int i = inicio;       // recorre la mitad izquierda
        int j = medio + 1;    // recorre la mitad derecha
        int k = inicio;       // posicion donde escribimos en lista

        while (i <= medio && j <= fin) {
            if (auxiliar.get(i).getPrecio() <= auxiliar.get(j).getPrecio()) {
                lista.set(k++, auxiliar.get(i++));
            } else {
                lista.set(k++, auxiliar.get(j++));
            }
        }
        // Quedaron elementos en la mitad izquierda
        while (i <= medio) {
            lista.set(k++, auxiliar.get(i++));
        }
        // Quedaron elementos en la mitad derecha
        while (j <= fin) {
            lista.set(k++, auxiliar.get(j++));
        }
    }

    // ============================================================
    // HELPER COMPARTIDO
    // ============================================================

    /** Intercambia dos posiciones de la lista. */
    private static void intercambiar(List<Producto> lista, int i, int j) {
        Producto temp = lista.get(i);
        lista.set(i, lista.get(j));
        lista.set(j, temp);
    }
}
