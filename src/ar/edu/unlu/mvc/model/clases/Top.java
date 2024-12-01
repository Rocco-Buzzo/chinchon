package ar.edu.unlu.mvc.model.clases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

public class Top implements Serializable {
    private ArrayList<Jugador> top;
    /**
     * Constructor de la clase Top.
     */
    public Top() {
        this.top = new ArrayList<>();

    }
    /**
     * COMPARATOR_BY_VICTORIES es un comparador que se utiliza para ordenar el topFive.
     */
    private static final Comparator<Jugador> COMPARATOR_BY_VICTORIES = (o1, o2) -> Integer.compare(o2.getVictorias(), o1.getVictorias());
    /**
     * @param jugador
     * Agrega al topFive al jugador que gane la partida.
     */
    public void agregarAlTop(Jugador jugador) {
        boolean bandera = false;

        for (int i = 0; i < top.size(); i++) {
            if (top.get(i).getNombre().equals(jugador.getNombre())) {
                top.get(i).aumentarVictorias();
                top.sort(COMPARATOR_BY_VICTORIES);
                bandera = true;
            }
        }

        if (!bandera) {
            jugador.aumentarVictorias();
            ArrayList<Jugador> topFiveCopy = new ArrayList<>(top);
            topFiveCopy.add(jugador);
            topFiveCopy.sort(COMPARATOR_BY_VICTORIES);
            top = topFiveCopy;
        }
        Serializacion.guardarTop(this);
    }

    /**
     * Devuelve el top.
     * @return ArrayList<Jugador>
     */
    public ArrayList<Jugador> getTop() {
        return this.top;
    }
}
