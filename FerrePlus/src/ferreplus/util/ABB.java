package ferreplus.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Arbol Binario de Busqueda generico.
 *
 * <p>Almacena objetos del tipo {@code T} que deben implementar
 * {@link Comparable} para poder ordenarse dentro del arbol. En el proyecto
 * Ferre+ se usa con {@code Producto}, que es Comparable por su clave de
 * 9 digitos (RF-07).</p>
 *
 * <p>La estructura del arbol esta hecha con instancias de {@link Nodo},
 * que es una clase aparte tambien generica. La implementacion es recursiva
 * clasica, sin balanceo, suficiente para el volumen esperado en una
 * ferreteria.</p>
 *
 * <p>Operaciones soportadas:
 * <ul>
 *   <li>{@link #insertar(Comparable)} - RF-07</li>
 *   <li>{@link #buscar(Comparable)} - RF-11 (busqueda exacta)</li>
 *   <li>{@link #eliminar(Comparable)} - RF-05</li>
 *   <li>{@link #recorrerInorden()} - RF-08 (orden por defecto en la tabla)</li>
 *   <li>{@link #estaVacio()}, {@link #getTamano()}, {@link #limpiar()}</li>
 * </ul>
 *
 * <p>La busqueda parcial por nombre (RF-11) no esta dentro del ABB porque
 * requiere comparar campos distintos al de orden; se resuelve fuera del
 * arbol filtrando el resultado de {@link #recorrerInorden()}.</p>
 *
 * @param <T> tipo de dato almacenado, debe ser {@link Comparable}
 * @author Sosa Fernandez Luis David
 */
public class ABB<T extends Comparable<T>> {

    /** Raiz del arbol. Es {@code null} cuando el arbol esta vacio. */
    private Nodo<T> raiz;

    /** Numero de elementos almacenados (evita recorrer para contar). */
    private int tamano;

    /** Crea un arbol vacio. */
    public ABB() {
        this.raiz = null;
        this.tamano = 0;
    }

    /**
     * Indica si el arbol no contiene elementos.
     *
     * @return {@code true} si esta vacio
     */
    public boolean estaVacio() {
        return raiz == null;
    }

    /**
     * Devuelve cuantos elementos hay en el arbol.
     *
     * @return numero de nodos
     */
    public int getTamano() {
        return tamano;
    }

    /** Devuelve la raiz del arbol (puede ser util para recorridos externos). */
    public Nodo<T> getRaiz() {
        return raiz;
    }

    /** Vacia el arbol (el recolector de basura se encarga del resto). */
    public void limpiar() {
        raiz = null;
        tamano = 0;
    }

    // ============================================================
    // INSERTAR (RF-07)
    // ============================================================

    /**
     * Inserta un elemento en el arbol. Si ya existe uno igual no se
     * inserta para mantener la unicidad de la clave (compareTo == 0).
     *
     * @param dato elemento a insertar
     * @return {@code true} si se inserto, {@code false} si era nulo o ya existia
     */
    public boolean insertar(T dato) {
        if (dato == null) {
            return false;
        }
        if (buscar(dato) != null) {
            return false;
        }
        raiz = insertarRecursivo(raiz, dato);
        tamano++;
        return true;
    }

    /** Recorre el arbol buscando el lugar donde insertar el nuevo nodo. */
    private Nodo<T> insertarRecursivo(Nodo<T> actual, T dato) {
        if (actual == null) {
            return new Nodo<>(dato);
        }
        int cmp = dato.compareTo(actual.getDato());
        if (cmp < 0) {
            actual.setIzquierdo(insertarRecursivo(actual.getIzquierdo(), dato));
        } else if (cmp > 0) {
            actual.setDerecho(insertarRecursivo(actual.getDerecho(), dato));
        }
        // cmp == 0 no deberia ocurrir porque ya validamos antes, pero por
        // seguridad no hacemos nada (no se duplica).
        return actual;
    }

    // ============================================================
    // BUSCAR (RF-11, busqueda exacta por la clave del Comparable)
    // ============================================================

    /**
     * Busca un elemento usando la propiedad del BST (O(log n) en promedio).
     * Solo necesita el campo de ordenamiento llenado en el {@code buscado};
     * por ejemplo si {@code T} es Producto basta con que tenga la clave.
     *
     * @param buscado patron a comparar
     * @return el elemento real guardado en el arbol, o {@code null} si no esta
     */
    public T buscar(T buscado) {
        if (buscado == null) {
            return null;
        }
        return buscarRecursivo(raiz, buscado);
    }

    private T buscarRecursivo(Nodo<T> actual, T buscado) {
        if (actual == null) {
            return null;
        }
        int cmp = buscado.compareTo(actual.getDato());
        if (cmp == 0) {
            return actual.getDato();
        } else if (cmp < 0) {
            return buscarRecursivo(actual.getIzquierdo(), buscado);
        } else {
            return buscarRecursivo(actual.getDerecho(), buscado);
        }
    }

    // ============================================================
    // RECORRIDO INORDEN (RF-08)
    // ============================================================

    /**
     * Devuelve los elementos del arbol en orden ascendente segun
     * {@code compareTo}, usando el recorrido Inorden (izquierdo, raiz,
     * derecho). En Ferre+ se usa como orden por defecto de la tabla de
     * productos (RF-08).
     *
     * @return lista de elementos en orden Inorden
     */
    public List<T> recorrerInorden() {
        List<T> lista = new ArrayList<>();
        inordenRecursivo(raiz, lista);
        return lista;
    }

    private void inordenRecursivo(Nodo<T> actual, List<T> lista) {
        if (actual == null) {
            return;
        }
        inordenRecursivo(actual.getIzquierdo(), lista);
        lista.add(actual.getDato());
        inordenRecursivo(actual.getDerecho(), lista);
    }

    // ============================================================
    // ELIMINAR (RF-05)
    // ============================================================

    /**
     * Elimina del arbol el elemento que coincida con el patron dado.
     * Aplica los tres casos clasicos del borrado en BST:
     * <ul>
     *   <li>Hoja: se quita directamente.</li>
     *   <li>Un solo hijo: se reemplaza por ese hijo.</li>
     *   <li>Dos hijos: se reemplaza por el sucesor inorden
     *       (minimo del subarbol derecho).</li>
     * </ul>
     *
     * @param buscado patron a eliminar (basta con la clave para Producto)
     * @return {@code true} si se elimino, {@code false} si no existia
     */
    public boolean eliminar(T buscado) {
        if (buscado == null) {
            return false;
        }
        if (buscar(buscado) == null) {
            return false;
        }
        raiz = eliminarRecursivo(raiz, buscado);
        tamano--;
        return true;
    }

    private Nodo<T> eliminarRecursivo(Nodo<T> actual, T buscado) {
        if (actual == null) {
            return null;
        }
        int cmp = buscado.compareTo(actual.getDato());
        if (cmp < 0) {
            actual.setIzquierdo(eliminarRecursivo(actual.getIzquierdo(), buscado));
        } else if (cmp > 0) {
            actual.setDerecho(eliminarRecursivo(actual.getDerecho(), buscado));
        } else {
            // Nodo encontrado: aplicar uno de los tres casos
            if (actual.getIzquierdo() == null) {
                return actual.getDerecho();
            }
            if (actual.getDerecho() == null) {
                return actual.getIzquierdo();
            }
            // Dos hijos: copiamos el sucesor inorden y lo borramos del subarbol
            // derecho, donde tendra a lo mas un hijo.
            Nodo<T> sucesor = encontrarMinimo(actual.getDerecho());
            actual.setDato(sucesor.getDato());
            actual.setDerecho(eliminarRecursivo(actual.getDerecho(),
                                                sucesor.getDato()));
        }
        return actual;
    }

    /** Devuelve el nodo mas a la izquierda (minimo) de un subarbol. */
    private Nodo<T> encontrarMinimo(Nodo<T> n) {
        while (n.getIzquierdo() != null) {
            n = n.getIzquierdo();
        }
        return n;
    }
}
