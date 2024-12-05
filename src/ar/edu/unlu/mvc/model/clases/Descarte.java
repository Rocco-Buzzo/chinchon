package ar.edu.unlu.mvc.model.clases;

import ar.edu.unlu.utilities.Pila;

import java.io.Serial;

public class Descarte extends Pila {
    @Serial
    private static final long serialVersionUID = 4L;
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

    @Override
    public String toString() {
        if (getTope() == null) {
            return "DESCARTE";
        } else {
            return getTope().toString();
        }
    }
}
