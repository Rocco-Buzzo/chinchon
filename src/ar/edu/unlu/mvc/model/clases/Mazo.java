package ar.edu.unlu.mvc.model.clases;

import ar.edu.unlu.mvc.model.enumerates.Palo;
import ar.edu.unlu.utilities.Pila;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Random;

public class Mazo extends Pila {
    @Serial
    private static final long serialVersionUID = 10L;
    private final boolean contieneComodin;
    private final int sizeInicial;

    private static final int[] VALORES40 = {1, 2, 3, 4, 5, 6, 7, 10, 11, 12};
    private static final int[] VALORES48 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

    public Mazo() {
        this.sizeInicial = 0;
        this.contieneComodin = false;
    }

    public Mazo(boolean contiene, int cantidadCartas) {
        contieneComodin = contiene;
        sizeInicial = cantidadCartas;
        if (sizeInicial == 40) {
            for (int valor : VALORES40) {
                super.apilar(new Carta(valor, Palo.ESPADA));
                super.apilar(new Carta(valor, Palo.BASTO));
                super.apilar(new Carta(valor, Palo.ORO));
                super.apilar(new Carta(valor, Palo.COPA));
            }
        } else if (sizeInicial == 48) {
            for (int valor : VALORES48) {
                apilar(new Carta(valor, Palo.ESPADA));
                apilar(new Carta(valor, Palo.BASTO));
                apilar(new Carta(valor, Palo.ORO));
                apilar(new Carta(valor, Palo.COPA));
            }
        }
        if (contieneComodin) {
            apilar(new Comodin());
            apilar(new Comodin());
        }

        Random random = new Random();
        int vecesBarajar = 1 + random.nextInt(4); // Genera un número aleatorio entre 1 y 4

        for (int i = 0; i < vecesBarajar; i++) {
            barajar();
        }
    }

    public void barajar() {
        ArrayList<Object> lista = new ArrayList<>();
        while (!isEmpty()) {
            lista.add(desapilar());
        }

        // Mezclar la lista usando Fisher-Yates y un generador de números aleatorios
        Random random = new Random(System.currentTimeMillis());
        for (int i = lista.size() - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            Object temp = lista.get(index);
            lista.set(index, lista.get(i));
            lista.set(i, temp);
        }

        for (Object elemento : lista) {
            super.apilar(elemento);
        }
    }

    public Carta sacar() {
        if (getTope() != null) {
            return (Carta) super.desapilar();
        }
        return null;
    }

    public int getSizeInicial() {
        return sizeInicial;
    }

    public boolean isContieneComodin() {
        return contieneComodin;
    }
}
