package ar.edu.unlu.mvc.model.clases;

import java.io.Serial;
import java.io.Serializable;

public class Jugador implements Serializable {
    @Serial
    private static final long serialVersionUID = 5L;
    private final String nombre;
    private final Mano mano;
    private int puntaje;
    private int cantidadVictorias;

    /**
     * Constructor
     * @param nombre Nombre del Jugador.
     */
    public Jugador(String nombre) {
        this.nombre = nombre;
        this.puntaje = 0;
        this.cantidadVictorias = 0;
        this.mano = new Mano();
    }

    /**
     * Agrega una carta al jugador.
     * @param carta Carta a agregar.
     */
    public void agregarCarta(Carta carta) {
        mano.agregarCarta(carta);
    }

    /**
     * Tira una carta del Jugador.
     * @param n Posicion del la carta a tirar.
     * @return Carta a tirar
     */
    public Carta tirarCarta(int n) {
        return mano.getCarta(n);
    }

    /**
     * Aumenta las vitctorias del jugador.
     */
    public void aumentarVictorias() {
        this.cantidadVictorias++;
    }

    /**
     * Aumenta los puntos del jugador.
     * @param n Puntos a aumentar.
     */
    public void aumentarPuntuacion(int n) {
        this.puntaje += n;
    }

    /**
     * Devuelve el nombre del jugador.
     * @return nombre del jugador.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Devuelve los puntos del jugador.
     * @return puntos del jugador.
     */
    public int getPuntaje() {
        return puntaje;
    }

    /**
     * Devuelve la cantidad de victorias del jugador.
     * @return cantidad de victorias.
     */
    public int getVictorias() {
        return cantidadVictorias;
    }

    /**
     * Devuelve la mano del jugador.
     * @return mano del jugador.
     */
    public Mano getMano() {
        return mano;
    }

    /**
     * Devuelve la cantidad de Jokers que hay en la mano del jugador.
     * @return cantidad de jokers.
     */
    public int cantidadJokers() {
        int acum = 0;
        for (int i = 0; i < getMano().getCartas().size(); i++) {
            if (getMano().getCartas().get(i).isComodin()) {
                acum++;
            }
        }
        return acum;
    }

    public boolean equals(Jugador jugador) {
        return this.nombre.equals(jugador.getNombre());
    }
    public void vaciarCartas() {
        this.getMano().vaciar();
    }
}
