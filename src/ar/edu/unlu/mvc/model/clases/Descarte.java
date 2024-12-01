package ar.edu.unlu.mvc.model.clases;

import ar.edu.unlu.utilities.Pila;

public class Descarte extends Pila {
    public Descarte() {
        super();
    }

    @Override
    public Carta desapilar() {
        return (Carta) super.desapilar();
    }

    public void vaciarDescarte() {
        while (!isEmpty()) {
            desapilar();
        }
    }

    @Override
    public Carta getTope() {
        return (Carta) super.getTope();
    }
}
