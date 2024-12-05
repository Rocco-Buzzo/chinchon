package ar.edu.unlu.utilities;

import java.io.Serial;
import java.io.Serializable;

public class Cola<T> implements Serializable{
    @Serial
    private static final long serialVersionUID = 8L;

    private static class Nodo<T> implements Serializable {
        private final T dato;
        private Nodo<T> siguiente;

        public Nodo(T dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }

    private Nodo<T> frente;
    private Nodo<T> fondo;
    private int size;

    public Cola() {
        frente = null;
        fondo = null;
        size = 0;
    }

    public boolean estaVacia() {
        return frente == null;
    }

    public void encolar(T dato) {
        Nodo<T> nuevoNodo = new Nodo<>(dato);
        if (estaVacia()) {
            frente = nuevoNodo;
            fondo = nuevoNodo;
        } else {
            fondo.siguiente = nuevoNodo;
            fondo = nuevoNodo;
        }
        size++;
    }

    public T desencolar() {
        if (estaVacia()) {
            throw new IllegalStateException("La cola está vacía.");
        }
        T dato = frente.dato;
        frente = frente.siguiente;
        if (frente == null) {
            fondo = null;
        }
        size--;
        return dato;
    }

    public void moverAlFondo() {
        if (!estaVacia() && size > 1) {
            encolar(desencolar());
        }
    }

    public T frente() {
        if (estaVacia()) {
            throw new IllegalStateException("La cola está vacía.");
        }
        return frente.dato;
    }

    public T fondo() {
        if (estaVacia()) {
            throw new IllegalStateException("La cola está vacía.");
        }
        return fondo.dato;
    }

    public int size() {
        return size;
    }

    public void vaciar() {
        frente = null;
        fondo = null;
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Cola: ");
        Nodo<T> actual = frente;
        while (actual != null) {
            sb.append(actual.dato).append(" -> ");
            actual = actual.siguiente;
        }
        sb.append("null");
        return sb.toString();
    }
}


