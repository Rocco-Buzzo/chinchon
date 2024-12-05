package ar.edu.unlu.utilities;

import java.io.Serial;
import java.io.Serializable;
import java.util.NoSuchElementException;

public class Pila implements Serializable {
    @Serial
    private static final long serialVersionUID = 9L;
    private Nodo tope;
    private int size;

    /**
     * Constructor de la pila.
     */
    public Pila() {
        this.tope = null;
        this.size = 0;
    }

    /**
     * Agrega un elemento al tope de la pila.
     * @param item el elemento a agregar.
     */
    public void apilar(Object item) {
        Nodo nuevoNodo = new Nodo(item);
        nuevoNodo.siguiente = tope;
        tope = nuevoNodo;
        size++;
    }

    /**
     * Saca un elemento del tope de la pila.
     */
    public Object desapilar() {
        if (isEmpty()) {
            throw new NoSuchElementException("La pila está vacía");
        }
        Object dato = tope.dato;
        tope = tope.siguiente;
        size--;
        return dato;
    }

    /**
     * @return el elemento que está en el tope de la pila.
     */
    public Object getTope() {
        if (isEmpty()) {
            return null;
        }
        return tope.dato;
    }

    /**
     * @return la cantidad de elementos en la pila.
     */
    public int getSize() {
        return size;
    }

    /**
     * @return true si la pila está vacía, false en caso contrario.
     */
    public boolean isEmpty() {
        return tope == null && size == 0;
    }

    /**
     * Clase interna que representa un nodo de la pila.
     */
    private static class Nodo implements Serializable {
        private final Object dato;
        private Nodo siguiente;
        /**
         * Constructor de un nodo.
         * @param dato el dato que contiene el nodo.
         */
        public Nodo(Object dato) {
            this.dato = dato;
            siguiente = null;
        }
    }
}

