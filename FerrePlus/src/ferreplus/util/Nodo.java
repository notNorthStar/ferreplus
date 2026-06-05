package ferreplus.util;

/**
 * Nodo generico para usar en estructuras enlazadas (arbol binario,
 * lista enlazada, etc).
 *
 * <p>Guarda un {@code dato} del tipo {@code T} y dos referencias a otros
 * nodos. En el contexto del {@link ABB} de Ferre+ los hijos representan
 * los subarboles izquierdo y derecho del BST.</p>
 *
 * @param <T> tipo de dato almacenado en el nodo
 * @author Sosa Fernandez Luis David
 */
public class Nodo<T> {

    /** Dato guardado en este nodo. */
    private T dato;

    /** Hijo izquierdo. {@code null} si no tiene. */
    private Nodo<T> izquierdo;

    /** Hijo derecho. {@code null} si no tiene. */
    private Nodo<T> derecho;

    /**
     * Crea un nodo nuevo con el dato dado y sin hijos.
     *
     * @param dato dato a guardar
     */
    public Nodo(T dato) {
        this.dato = dato;
        this.izquierdo = null;
        this.derecho = null;
    }

    public T getDato() {
        return dato;
    }

    public void setDato(T dato) {
        this.dato = dato;
    }

    public Nodo<T> getIzquierdo() {
        return izquierdo;
    }

    public void setIzquierdo(Nodo<T> izquierdo) {
        this.izquierdo = izquierdo;
    }

    public Nodo<T> getDerecho() {
        return derecho;
    }

    public void setDerecho(Nodo<T> derecho) {
        this.derecho = derecho;
    }

    /**
     * Indica si este nodo es una hoja (no tiene hijos).
     *
     * @return {@code true} si no tiene hijo izquierdo ni derecho
     */
    public boolean esHoja() {
        return izquierdo == null && derecho == null;
    }
}
