package ar.edu.unlu.mvc.model.clases;

import ar.edu.unlu.mvc.model.enumerates.Palo;

import java.io.Serial;
import java.io.Serializable;

public class Comodin extends Carta implements Serializable {
    @Serial
    private static final long serialVersionUID = 3L;

    public Comodin() {
        super(25, Palo.JOKER);
    }

    @Override
    public boolean isComodin() {
        return true;
    }

}
